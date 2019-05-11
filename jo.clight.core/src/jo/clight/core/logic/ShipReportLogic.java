package jo.clight.core.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.audio.util.model.data.AudioMessageBean;
import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.text.TextLogic;
import jo.util.utils.obj.IntegerUtils;

public class ShipReportLogic
{
    public static synchronized ShipReportBean report(ShipDesignBean ship)
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
            reportPower(ship, report);
            int compRating = reportComputer(ship, report);
            reportJump(ship, report, compRating);
            reportManeuver(ship, report);
            reportRefinery(ship, report);
            reportSensors(ship, report);
            report.setNumberOfStaterooms(
                    ShipDesignLogic.countAllInstances(ship, "$stateroom"));
            report.setNumberOfBerths(
                    ShipDesignLogic.countAllInstances(ship, "$lowberth"));
            report.setNumberOfHardpoints(
                    Math.max(1, report.getHullDisplacement() / 100));
            report.setFireControlTonnage(report.getNumberOfHardpoints());
            report.setCargoTonnage(
                    ShipDesignLogic.countAllInstances(ship, "$cargo_hold"));
            reportTurrets(ship, report);
            reportScreens(ship, report);
            reportHangers(ship, report);
            reportHullDetails(ship, report);
            reportPersonnel(ship, report);
            reportCosts(ship, report);
            reportAdditionalComponents(ship, report);
            int space = 0;
            for (ShipComponentInstanceBean comp : ship.getComponents())
                space += comp.getVolume()*comp.getCount();
            space += report.getFireControlTonnage();
            if (space > 0)
                report.getErrors().add("Overrun: Ship can only hold "+report.getHullDisplacement()+"t; contains "+space+"t too much.");
            report.setHullUsed(report.getHullDisplacement() + space);
            report.setCargoTonnage(report.getCargoTonnage() - space);
            checkErrors(ship, report);
            
            reportProse(ship, report);
            
            return report;
        }
        finally
        {
            ParameterizedLogic.removeContext(ship);
        }
    }

    public static void checkErrors(ShipDesignBean ship, ShipReportBean report)
    {
        // check bridge
        ShipComponentBean bridge = ShipDesignLogic.findHighestNumberParam(ship, ShipComponentBean.BRIDGE, "tonsSupported");
        if (bridge == null)
            report.getErrors().add("No bridge: A bridge is required for all starships.");
        else
        {
            int tonsSupported = IntegerUtils.parseInt(bridge.getParams().get("tonsSupported"));
            if (report.getHullDisplacement() > tonsSupported)
                report.getErrors().add("Small bridge: This bridge can only support up to "+tonsSupported+" tons");
        }
        // check electronics
        ShipComponentInstanceBean elec = ShipDesignLogic.getFirstInstance(ship, ShipComponentBean.ELECTRONICS);
        if (elec == null)
            report.getErrors().add("No electronics: An electronics package must be installed.");
        // check turrets
        int turrets = ShipDesignLogic.countAllInstances(ship, ShipComponentBean.TURRET);
        int bays = ShipDesignLogic.countAllInstances(ship, ShipComponentBean.BAY);
        report.setNumberOfHardpointsUsed(turrets + bays);
        if (turrets + bays > report.getNumberOfHardpoints())
            report.getErrors().add("Too many weapons: Ship only has "+report.getNumberOfHardpoints()+" hard points but has "+turrets+" turrets and "+bays+" bays");
    }

    public static void reportProse(ShipDesignBean ship, ShipReportBean report)
    {
        report.setProse(new AudioMessageBean(AudioMessageBean.GROUP));
        report.getProse().addToGroup(new AudioMessageBean("USD_PROLOGUE", report.getTechLevel(), ship.getShipName()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE1", report.getHullDisplacement(), report.getHullDamageValue(),
                report.getStructureDamageValue(), ship.getShipName(), ship.getShipFunction()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE2", report.getJumpCode(), report.getManeuverCode(),
                report.getPowerCode(), report.getJumpNumber(), report.getThrustNumber()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE3", report.getFuelTonnage(), report.getWeeksofPower(),
                report.getNumberOfJumps(), report.getJumpNumber()));
        report.getProse().addToGroup(report.getFuelUsageNotes());
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE4", report.getComputerModel()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE5", report.getSensorsType(), 
                report.getSensorsDM()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE6", report.getNumberOfStaterooms(), report.getNumberOfBerths()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE7", report.getNumberOfHardpoints(), report.getFireControlTonnage()));
        if (report.getTurrets().getArgs().length > 0)
            report.getProse().addToGroup(new AudioMessageBean("USD_LINE8", report.getTurrets()));
        if (report.getNumberOfScreens() > 0)
            report.getProse().addToGroup(new AudioMessageBean("USD_LINE9", report.getNumberOfScreens(), report.getScreens()));
        if (report.getNumberOfHangers() > 0)
            report.getProse().addToGroup(new AudioMessageBean("USD_LINE10", report.getNumberOfHangers(), report.getHangers()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE11", report.getCargoTonnage()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE12", report.getHullConfiguration(), report.getArmorType(),
                report.getArmorRating()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE13", report.getAdditionalComponents()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE14", report.getCrewTotal(), report.getCrewPositions()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE15", report.getPassengersTotal(), report.getNumberOfBerths()));
        report.getProse().addToGroup(new AudioMessageBean("USD_LINE16", FormatUtils.sCurrency(report.getCost()*1000000*.9), report.getConstructionTime()));
    }

    public static void reportHullDetails(ShipDesignBean ship,
            ShipReportBean report)
    {
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
    }

    public static void reportPersonnel(ShipDesignBean ship,
            ShipReportBean report)
    {
        //Map<String,Integer> crew = ShipDesignLogic.getMaximumCrew(ship);
        Map<String,Integer> crew = ShipDesignLogic.getMinimumCrew(ship);
        int crewTotal = 0;
        report.setCrewPositions(new AudioMessageBean(AudioMessageBean.LIST));
        for (String position : crew.keySet())
        {
            int quan = crew.get(position);
            if (quan <= 0)
                continue;
            crewTotal += quan;
            if (quan == 1)
                report.getCrewPositions().addToGroup(new AudioMessageBean(position));
            else
                report.getCrewPositions().addToGroup(new AudioMessageBean("BY", new AudioMessageBean(position), quan));
        }
        report.setCrewTotal(crewTotal);
        int pass = report.getNumberOfStaterooms() - crewTotal;
        if (pass < 0)
            report.getErrors().add("Insufficient staterooms. Need at least "+crewTotal+" for crew, only have "+report.getNumberOfStaterooms());
        else if (pass > 0)
            report.setPassengersTotal(pass*2);
    }

    public static void reportCosts(ShipDesignBean ship, ShipReportBean report)
    {
        double cost = 0;
        for (ShipComponentInstanceBean comp : ship.getComponents())
            cost += comp.getPrice()*comp.getCount();
        report.setCost(cost);
        ShipComponentInstanceBean hull = ShipDesignLogic.getHull(ship);
        if (hull == null)
            report.getErrors().add("No hull present");
        else
            report.setConstructionTime(IntegerUtils.parseInt(hull.getComponent().getParams().get("constructionTime")));
    }

    public static void reportAdditionalComponents(ShipDesignBean ship,
            ShipReportBean report)
    {
        report.setAdditionalComponents(new AudioMessageBean(AudioMessageBean.LIST));
        for (ShipComponentInstanceBean comp : ShipDesignLogic.getAllInstances(ship, ShipComponentBean.ETC))
        {
            switch (comp.getComponentID())
            {
                case "cargo_hold":
                //case "fuel_processor":
                //case "launch_tube":
                    break;
                default:
                    if (comp.getCount() == 1)
                        report.getAdditionalComponents().addToGroup(comp.getComponent().getName());
                    else
                        report.getAdditionalComponents().addToGroup(new AudioMessageBean("BY", comp.getComponent().getName(), comp.getCount()));
            }
        }
    }

    public static void reportSensors(ShipDesignBean ship, ShipReportBean report)
    {
        ShipComponentBean sensor = ShipDesignLogic.findHighestTech(ship,
                ShipComponentBean.ELECTRONICS);
        if (sensor != null)
        {
            report.setSensorsType(sensor.getName());
            report.setSensorsDM(
                    Integer.parseInt(sensor.getParams().getString("dm")));
        }
        report.setNumberOfSensors(ShipDesignLogic.countAllInstances(ship, ShipComponentBean.ELECTRONICS));
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

    public static void reportPower(ShipDesignBean ship, ShipReportBean report)
    {
        ShipComponentBean power = ShipDesignLogic.findHighestStringParam(ship,
                ShipComponentBean.PPLANT, "driveCode");
        String powCode;
        if (power == null)
            powCode = " ";
        else
            powCode = power.getParams().getString("driveCode");
        report.setPowerCode(powCode);
    }

    public static int reportComputer(ShipDesignBean ship, ShipReportBean report)
    {
        ShipComponentBean comp = ShipDesignLogic.findHighestStringParam(ship,
                ShipComponentBean.COMPUTER, "rating");
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
            ShipReportBean report, int compRating)
    {
        ShipComponentBean jump = ShipDesignLogic.findHighestStringParam(ship,
                ShipComponentBean.JDRIVE, "driveCode");
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
            String powCode = report.getPowerCode();
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
            ShipReportBean report)
    {
        ShipComponentBean man = ShipDesignLogic.findHighestStringParam(ship,
                ShipComponentBean.MDRIVE, "driveCode");
        if (man == null)
        {
            report.setThrustNumber(0);
            report.setWeeksofPower(0);
            report.setFuelUsageNotes(null);
            report.setManeuverCode(" ");
        }
        else
        {
            String manCode = man.getParams().getString("driveCode");
            report.setManeuverCode(manCode);
            report.setThrustNumber(ShipDesignLogic.getDrivePerformance(manCode,
                    report.getHullDisplacement()));
            int fuelPerWeek = ShipDesignLogic.getFuelPerWeek(manCode);
            int singleJump = Math.max(1,
                    (report.getHullDisplacement() * report.getJumpNumber())
                            / 10);
            int jumpVolume = singleJump*report.getNumberOfJumps();
            int weeksofPower = (report.getFuelTonnage() - jumpVolume) / fuelPerWeek;
            report.setWeeksofPower(weeksofPower);
            if (report.getPowerCode().compareTo(manCode) < 0)
                report.getErrors()
                        .add((new StringBuilder("Underpowered. Power plant '"))
                                .append(report.getPowerCode())
                                .append("' is less than maneuver drive '")
                                .append(manCode).append("'").toString());
            report.setFuelUsageNotes(new AudioMessageBean("POWER_PLANT_USES",
                    new Object[] { Integer.valueOf(fuelPerWeek) }));
        }
    }

    public static void reportHangers(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> hangers = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.HANGER);
        List<AudioMessageBean> hangersDesc = new ArrayList<>();
        int numberOfHangers = 0;
        for (ShipComponentInstanceBean hanger : hangers)
        {
            AudioMessageBean hangerDesc = new AudioMessageBean("BY",
                    new Object[] { hanger.getComponent().getName(), Integer.valueOf(hanger.getCount()) });
            hangersDesc.add(hangerDesc);
            numberOfHangers++;
        }

        report.setHangers(TextLogic.compress(hangersDesc));
        report.setNumberOfHangers(numberOfHangers);
    }

    public static void reportScreens(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> screens = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.SCREENS);
        List<AudioMessageBean> screenDesc = new ArrayList<>();
        int numberOfScreens = 0;
        for (Iterator<ShipComponentInstanceBean> iterator = screens
                .iterator(); iterator.hasNext();)
        {
            ShipComponentInstanceBean screen = iterator.next();
            for (int i = 0; i < screen.getCount(); i++)
            {
                screenDesc.add(screen.getComponent().getName());
                numberOfScreens++;
            }
        }
        report.setScreens(TextLogic.compress(screenDesc));
        report.setNumberOfScreens(numberOfScreens);
    }

    public static void reportTurrets(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> turrets = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.TURRET);
        List<ShipComponentInstanceBean> weapons = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.WEAPON);
        int used = 0;
        for (int i = 0; i < weapons.size(); i++)
        {
            ShipComponentInstanceBean weapon = (ShipComponentInstanceBean)weapons
                    .get(i);
            used += weapon.getCount();
            for (int j = 1; j < weapon.getCount(); j++)
            {
                weapons.add(i, weapon);
                i++;
            }
        }

        List<AudioMessageBean> turretsDesc = new ArrayList<>();
        int slots = 0;
        for (ShipComponentInstanceBean turret : turrets)
        {
            int capacity = IntegerUtils.parseInt(
                    turret.getComponent().getParams().get("capacity"));
            for (int i = 0; i < turret.getCount(); i++)
            {
                slots += capacity;
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
        report.setWeaponSlots(slots);
        report.setWeaponSlotsUsed(used);
    }

    private static int findMaxTech(List<ShipComponentInstanceBean> components)
    {
        int tl = 0;
        for (ShipComponentInstanceBean component : components)
            tl = Math.max(tl, component.getTechLevel());
        return tl;
    }
}
