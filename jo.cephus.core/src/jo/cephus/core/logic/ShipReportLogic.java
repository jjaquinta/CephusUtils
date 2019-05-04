package jo.cephus.core.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.audio.util.model.data.AudioMessageBean;
import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipComponentInstanceBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.text.TextLogic;
import jo.util.utils.obj.IntegerUtils;

public class ShipReportLogic
{

    public ShipReportLogic()
    {
    }

    public static ShipReportBean report(ShipDesignBean ship)
    {
        try
        {
            ParameterizedLogic.addContext(ship);
            ShipReportBean report = new ShipReportBean();
            report.setShip(ship);
            report.setTechLevel(findMaxTech(ship.getComponents()));
            report.setHullDisplacement(
                    ShipDesignLogic.getHullDisplacement(ship));
            report.setHullDamageValue(report.getHullDisplacement() / 50);
            report.setStructureDamageValue(report.getHullDisplacement() / 50);
            report.setFuelTonnage(
                    (int)ShipDesignLogic.volumeAllInstances(ship, "FUEL"));
            String powCode = reportPower(ship);
            int compRating = reportComputer(ship, report);
            ShipComponentBean jump = reportJump(ship, report, powCode,
                    compRating);
            reportManeuver(ship, report, powCode, jump);
            reportRefinery(ship, report);
            reportSensors(ship, report);
            report.setNumberOfStaterooms(
                    ShipDesignLogic.totalAllInstances(ship, "$stateroom"));
            report.setNumberOfBerths(
                    ShipDesignLogic.totalAllInstances(ship, "$lowberth"));
            report.setNumberOfHardpoints(
                    Math.max(1, report.getHullDisplacement() / 100));
            report.setFireControlTonnage(
                    ShipDesignLogic.totalAllInstances(ship, "BAY"));
            report.setCargoTonnage(
                    ShipDesignLogic.totalAllInstances(ship, "$cargo_hold"));
            reportTurrets(ship, report);
            reportScreens(ship, report);
            reportHangers(ship, report);
            ShipComponentInstanceBean config = ShipDesignLogic.getFirstInstance(ship, ShipComponentBean.CONFIG);
            if (config != null)
                report.setHullConfiguration(config.getComponent().getName());
            else
                report.getErrors().add("No configuration set.");
            ShipComponentInstanceBean armor = ShipDesignLogic.getFirstInstance(ship, ShipComponentBean.ARMOR);
            if (armor != null)
            {
                report.setArmorType(armor.getComponent().getName());
                report.setArmorRating(IntegerUtils.parseInt(armor.getComponent().getParams().get("protection")));
            }
            /*
    private AudioMessageBean mAdditionalComponents;
    private int mCrewTotal;
    private AudioMessageBean mCrewPositions;
    private int mPassengersTotal;
    private double mCost;
    private int mConstructionTime;

             */
            
            return report;
        }
        finally
        {
            ParameterizedLogic.removeContext(ship);
        }
    }

    public static void reportSensors(ShipDesignBean ship, ShipReportBean report)
    {
        ShipComponentBean sensor = ShipDesignLogic.findHighestTech(ship,
                "ELECTRONICS");
        if (sensor != null)
        {
            report.setSensorsType(sensor.getName());
            report.setSensorsDM(
                    Integer.parseInt(sensor.getParams().getString("dm")));
        }
    }

    public static void reportRefinery(ShipDesignBean ship,
            ShipReportBean report)
    {
        double refinery = ShipDesignLogic.volumeAllInstances(ship,
                "$fuel_processor");
        if (refinery > 0.0D)
        {
            int tonsPerDay = (int)(20D * refinery);
            report.setFuelUsageNotes(AudioMessageBean.group(new Object[] {
                    report.getFuelUsageNotes(),
                    new AudioMessageBean("FUEL_REFINERY_CAN_REFINE",
                            new Object[] { Integer.valueOf(tonsPerDay) }) }));
        }
    }

    public static String reportPower(ShipDesignBean ship)
    {
        ShipComponentBean power = ShipDesignLogic.findHighestStringParam(ship,
                "PPLANT", "driveCode");
        String powCode;
        if (power == null)
            powCode = " ";
        else
            powCode = power.getParams().getString("driveCode");
        return powCode;
    }

    public static int reportComputer(ShipDesignBean ship, ShipReportBean report)
    {
        ShipComponentBean comp = ShipDesignLogic.findHighestStringParam(ship,
                "COMPUTER", "rating");
        int compRating = 0;
        if (comp == null)
        {
            report.getErrors().add("No computer installed.");
            report.setComputerModel(null);
        }
        else
        {
            compRating = IntegerUtils
                    .parseInt(comp.getParams().getString("rating"));
            report.setComputerModel(comp.getName());
            List<ShipComponentInstanceBean> bis = ShipDesignLogic
                    .getAllInstances(ship, "$computer_bis");
            if (bis.size() > 0)
            {
                report.setComputerModel(AudioMessageBean
                        .group(new Object[] { report.getComputerModel(),
                                ((ShipComponentInstanceBean)bis.get(0))
                                        .getComponent().getName() }));
                compRating += 5;
            }
            List<ShipComponentInstanceBean> fib = ShipDesignLogic
                    .getAllInstances(ship, "$computer_fib");
            if (fib.size() > 0)
                report.setComputerModel(AudioMessageBean
                        .group(new Object[] { report.getComputerModel(),
                                ((ShipComponentInstanceBean)fib.get(0))
                                        .getComponent().getName() }));
        }
        return compRating;
    }

    public static ShipComponentBean reportJump(ShipDesignBean ship,
            ShipReportBean report, String powCode, int compRating)
    {
        ShipComponentBean jump = ShipDesignLogic.findHighestStringParam(ship,
                "JDRIVE", "driveCode");
        if (jump == null)
        {
            report.setJumpCode(" ");
            report.setJumpNumber(0);
            report.setNumberOfJumps(0);
        }
        else
        {
            String jumpCode = jump.getParams().getString("driveCode");
            report.setJumpCode(jumpCode);
            report.setJumpNumber(ShipDesignLogic.getDrivePerformance(jumpCode,
                    report.getHullDisplacement()));
            if (powCode.compareTo(jumpCode) < 0)
                report.getErrors()
                        .add((new StringBuilder("Underpowered. Power plant '"))
                                .append(powCode)
                                .append("' is less than jump drive '")
                                .append(jumpCode).append("'").toString());
            int singleJump = Math.max(1,
                    (report.getHullDisplacement() * report.getJumpNumber())
                            / 10);
            int numberOfJumps = report.getFuelTonnage() / singleJump;
            report.setNumberOfJumps(numberOfJumps);
            if (compRating / 5 < report.getJumpNumber())
                report.getErrors()
                        .add((new StringBuilder("Computer has rating of "))
                                .append(compRating).append(", but it needs ")
                                .append(report.getJumpNumber() * 5)
                                .append(" to control jump-")
                                .append(report.getJumpNumber()).toString());
        }
        return jump;
    }

    public static void reportManeuver(ShipDesignBean ship,
            ShipReportBean report, String powCode, ShipComponentBean jump)
    {
        ShipComponentBean man = ShipDesignLogic.findHighestStringParam(ship,
                "MDRIVE", "driveCode");
        if (man == null)
        {
            report.setThrustNumber(0);
            report.setWeeksofPower(0);
            report.setFuelUsageNotes(null);
        }
        else
        {
            String manCode = jump.getParams().getString("driveCode");
            report.setThrustNumber(ShipDesignLogic.getDrivePerformance(manCode,
                    report.getHullDisplacement()));
            int fuelPerWeek = ShipDesignLogic.getFuelPerWeek(manCode);
            int weeksofPower = report.getFuelTonnage() / fuelPerWeek;
            report.setWeeksofPower(weeksofPower);
            if (powCode.compareTo(manCode) < 0)
                report.getErrors()
                        .add((new StringBuilder("Underpowered. Power plant '"))
                                .append(powCode)
                                .append("' is less than maneuver drive '")
                                .append(manCode).append("'").toString());
            report.setFuelUsageNotes(new AudioMessageBean("POWER_PLANT_USES",
                    new Object[] { Integer.valueOf(fuelPerWeek) }));
        }
    }

    public static void reportHangers(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> hangers = ShipDesignLogic
                .getAllInstances(ship, "$hanger");
        List<AudioMessageBean> hangersDesc = new ArrayList<>();
        AudioMessageBean hangerDesc;
        for (Iterator<ShipComponentInstanceBean> iterator = hangers
                .iterator(); iterator.hasNext(); hangersDesc.add(hangerDesc))
        {
            ShipComponentInstanceBean hanger = iterator.next();
            hangerDesc = new AudioMessageBean("TON",
                    new Object[] { Integer.valueOf(hanger.getCount()),
                            hanger.getComponent().getName() });
        }

        report.setScreens(TextLogic.compress(hangersDesc));
    }

    public static void reportScreens(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> screens = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.SCREENS);
        List<AudioMessageBean> screenDesc = new ArrayList<>();
        for (Iterator<ShipComponentInstanceBean> iterator = screens
                .iterator(); iterator.hasNext();)
        {
            ShipComponentInstanceBean screen = iterator.next();
            for (int i = 0; i < screen.getCount(); i++)
                screenDesc.add(screen.getComponent().getName());

        }
        report.setScreens(TextLogic.compress(screenDesc));
    }

    public static void reportTurrets(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> turrets = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.TURRET);
        List<ShipComponentInstanceBean> weapons = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.WEAPON);
        for (int i = 0; i < weapons.size(); i++)
        {
            ShipComponentInstanceBean weapon = (ShipComponentInstanceBean)weapons
                    .get(i);
            for (int j = 1; j < weapon.getCount(); j++)
            {
                weapons.add(i, weapon);
                i++;
            }

        }

        List<AudioMessageBean> turretsDesc = new ArrayList<>();
        for (Iterator<ShipComponentInstanceBean> iterator = turrets
                .iterator(); iterator.hasNext();)
        {
            ShipComponentInstanceBean turret = iterator.next();
            int capacity = IntegerUtils.parseInt(
                    turret.getComponent().getParams().get("capacity"));
            for (int i = 0; i < turret.getCount(); i++)
            {
                List<AudioMessageBean> contents = new ArrayList<>();
                for (int j = 0; j < capacity && weapons.size() > 0; j++)
                {
                    contents.add((weapons.get(0)).getComponent().getName());
                    weapons.remove(0);
                }

                AudioMessageBean turretDesc = AudioMessageBean
                        .group(new Object[] { turret.getComponent().getName(),
                                TextLogic.compress(contents) });
                turretsDesc.add(turretDesc);
            }

        }

        report.setTurrets(TextLogic.compress(turretsDesc));
    }

    private static int findMaxTech(List<ShipComponentInstanceBean> components)
    {
        int tl = 0;
        for (Iterator<ShipComponentInstanceBean> iterator = components
                .iterator(); iterator.hasNext();)
        {
            ShipComponentInstanceBean component = iterator.next();
            tl = Math.max(tl, component.getTechLevel());
        }

        return tl;
    }
}
