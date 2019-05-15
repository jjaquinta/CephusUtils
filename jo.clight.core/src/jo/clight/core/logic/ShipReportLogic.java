package jo.clight.core.logic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.util.utils.obj.BooleanUtils;
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
            report.setTechLevel(ShipDesignLogic.findMaxTech(ship));
            reportHull(ship, report);
            report.setFuelTonnage((int)ShipDesignLogic.volumeAllInstances(ship, "FUEL"));
            reportPower(ship, report);
            reportComputer(ship, report);
            reportJump(ship, report);
            reportManeuver(ship, report);
            reportRefinery(ship, report);
            reportAccomodation(ship, report);
            reportTurrets(ship, report);
            reportBays(ship, report);
            reportScreens(ship, report);
            reportHangers(ship, report);
            reportPersonnel(ship, report);
            reportCosts(ship, report);
        
            int space = 0;
            for (ShipComponentInstanceBean comp : ship.getComponents())
                space += comp.getVolume()*comp.getCount();
            if (space > 0)
                report.addError(ShipComponentBean.HULL, "Overrun: Ship can only hold "+report.getHullDisplacement()+"t; contains "+space+"t too much.");
            report.setHullUsed(report.getHullDisplacement() + space);
            report.setCargoTonnage(ShipDesignLogic.countAllInstances(ship, ShipComponentBean.ETC_CARGO_HOLD) - space);
            checkErrors(ship, report);
            
            return report;
        }
        finally
        {
            ParameterizedLogic.removeContext(ship);
        }
    }

    public static void reportAccomodation(ShipDesignBean ship,
            ShipReportBean report)
    {
        report.setNumberOfStaterooms(
                ShipDesignLogic.countAllInstances(ship, ShipComponentBean.STATEROOM_));
        report.setNumberOfBerths(
                ShipDesignLogic.countAllInstances(ship, ShipComponentBean.BERTH_LOWBERTH));
        report.setNumberOfEmergencyBerths(
                ShipDesignLogic.countAllInstances(ship, ShipComponentBean.BERTH_EMERGENCY_LOWBERTH));
        report.setFireControlTonnage(ShipDesignLogic.countAllInstances(ship, ShipComponentBean.TURRET));
    }

    public static void reportHull(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> hulls = ShipDesignLogic.getAllInstances(ship, ShipComponentBean.HULL);
        if (hulls.size() == 0)
            report.addError(ShipComponentBean.HULL, "No hull: ship must include a hull");
        else if (hulls.size() > 1)
            report.addError(ShipComponentBean.HULL, "Multiple hulls: ship must include a single hull");
        else
            report.setHull(hulls.get(0));
        List<ShipComponentInstanceBean> configs = ShipDesignLogic.getAllInstances(ship, ShipComponentBean.CONFIG);
        if (configs.size() == 0)
            report.addError(ShipComponentBean.CONFIG, "No config: ship must include a hull configuration");
        else if (configs.size() > 1)
            report.addError(ShipComponentBean.CONFIG, "Multiple config: ship must include a single hull configuration");
        else
            report.setConfig(configs.get(0));
        report.setHullDisplacement(ShipDesignLogic.getHullDisplacement(ship));
        List<ShipComponentInstanceBean> armors = ShipDesignLogic.getAllInstances(ship, ShipComponentBean.ARMOR);
        int armorRating = 0;
        for (ShipComponentInstanceBean armor : armors)
            if (armor.getComponentID().equals(ShipComponentBean.ARMOR_STEALTH))
                report.setStealth(armor);
            else if (report.getArmor() != null)
                report.addError(armor.getComponentID(), "Multiple armor: ship must only have one type of armor");
            else
            {
                report.setArmor(armor);
                int rating = IntegerUtils.parseInt(armor.getComponent().getParams().get("protection"));
                armorRating += rating*armor.getCount();
            }
        report.setArmorRating(armorRating);
        report.setNumberOfHardpoints(Math.max(1, report.getHullDisplacement() / 100));        
    }

    public static void checkErrors(ShipDesignBean ship, ShipReportBean report)
    {
        if (report.getPowerCode().compareTo(report.getJumpCode()) < 0)
            report.addError(ShipComponentBean.PPLANT, "Underpowered. Power plant '"+report.getPowerCode()+"' is less than jump drive '"+report.getJumpCode()+"'");
        if (report.getCompRating() < report.getJumpNumber())
            report.addError(ShipComponentBean.COMPUTER, "Computer has rating of "+report.getCompRating()+", but it needs "+report.getJumpNumber()+" to control jump-"+report.getJumpNumber());
        if (report.getPowerCode().compareTo(report.getManeuverCode()) < 0)
            report.addError(ShipComponentBean.PPLANT, "Underpowered. Power plant '"+report.getPowerCode()+"' is less than maneuver drive '"+report.getManeuverCode()+"'");
        // check bridge
        ShipComponentBean bridge = ShipDesignLogic.findHighestNumberParam(ship, ShipComponentBean.BRIDGE, "tonsSupported");
        if (bridge == null)
            report.addError(ShipComponentBean.BRIDGE, "No bridge: A bridge is required for all starships.");
        else
        {
            int tonsSupported = IntegerUtils.parseInt(bridge.getParams().get("tonsSupported"));
            if (report.getHullDisplacement() > tonsSupported)
                report.addError(ShipComponentBean.BRIDGE, "Small bridge: This bridge can only support up to "+tonsSupported+" tons");
        }
        // check turrets
        int turrets = report.getNumberOfTurrets();
        int bays = report.getNumberOfBays();
        report.setNumberOfHardpointsUsed(turrets + bays);
        if (turrets + bays > report.getNumberOfHardpoints())
            report.addError(ShipComponentBean.HULL, "Too many weapons: Ship only has "+report.getNumberOfHardpoints()+" hard points but has "+turrets+" turrets and "+bays+" bays");
        // check crew
        for (String pos : report.getCrewNeeded().keySet())
        {
            int expected = report.getCrewNeeded().get(pos);
            int actual = report.getCrewHired().containsKey(pos) ? report.getCrewHired().get(pos) : 0;
            if (actual < expected)
                report.addError(pos, "Not enough crew: Ship only has "+actual+" "+pos+" but needs "+expected);
        }
        int crewTotal = report.getCrewTotal();
        if (ship.getRoles().contains(ShipDesignBean.ROLE_MILITARY))
            crewTotal = (crewTotal + 1)/2;
        int pass = report.getNumberOfStaterooms() - crewTotal;
        if (pass < 0)
            report.addError(ShipComponentBean.STATEROOM, "Insufficient staterooms. Need at least "+crewTotal+" for crew, only have "+report.getNumberOfStaterooms());
    }

    public static void reportPersonnel(ShipDesignBean ship,
            ShipReportBean report)
    {
        //Map<String,Integer> crew = ShipDesignLogic.getMaximumCrew(ship);
        Map<String,Integer> crewNeeded = ShipDesignLogic.getCrew(ship);
        Map<String,Integer> crewHired = new HashMap<>();
        int crewTotal = 0;
        for (ShipComponentInstanceBean crew : ShipDesignLogic.getAllInstances(ship, ShipComponentBean.CREW))
        {
            int c = crew.getCount();
            crewTotal += c;
            if (crewHired.containsKey(crew.getComponentID()))
                c += crewHired.get(crew.getComponentID());
            crewHired.put(crew.getComponentID(), c);
        }
        report.setCrewTotal(crewTotal);
        int stateroomsNeeded = crewTotal;
        if (ship.getRoles().contains(ShipDesignBean.ROLE_MILITARY))
            stateroomsNeeded = (stateroomsNeeded + 1)/2;
        int pass = report.getNumberOfStaterooms() - stateroomsNeeded;
        if (pass > 0)
        {
            report.setPassengersTotal(pass);
            int numStewards = pass/3;
            if (numStewards == 0)
                numStewards = 1;
            crewNeeded.put(ShipComponentBean.CREW_STEWARD, numStewards);
        }
        report.setCrewNeeded(crewNeeded);
        report.setCrewHired(crewHired);
    }

    public static void reportCosts(ShipDesignBean ship, ShipReportBean report)
    {
        double cost = 0;
        for (ShipComponentInstanceBean comp : ship.getComponents())
            cost += comp.getPrice()*comp.getCount();
        report.setCost(cost);
        ShipComponentInstanceBean hull = report.getHull();
        if (hull != null)
            report.setConstructionTime(IntegerUtils.parseInt(hull.getComponent().getParams().get("constructionTime")));
    }

    public static void reportRefinery(ShipDesignBean ship,
            ShipReportBean report)
    {
        double refinery = ShipDesignLogic.volumeAllInstances(ship,
                ShipComponentBean.ETC_FUEL_PROCESSOR);
        if (refinery > 0.0D)
        {
            int tonsPerDay = (int)(20D * refinery);
            report.setRefineTonsPerDay(tonsPerDay);
        }
    }

    public static void reportPower(ShipDesignBean ship, ShipReportBean report)
    {
        ShipComponentInstanceBean power = ShipDesignLogic.findHighestStringParam(ship,
                ShipComponentBean.PPLANT, "driveCode");
        if (power == null)
            report.addError(ShipComponentBean.PPLANT, "No power plant: ship must have a power plant");
        else
            report.setPowerPlant(power);
        String powCode;
        if (power == null)
            powCode = " ";
        else
            powCode = power.getComponent().getParams().getString("driveCode");
        report.setPowerCode(powCode);
    }

    public static void reportComputer(ShipDesignBean ship, ShipReportBean report)
    {
        ShipComponentInstanceBean comp = ShipDesignLogic.findHighestStringParam(ship,
                ShipComponentBean.COMPUTER, "rating");
        if (comp == null)
        {
            report.addError(ShipComponentBean.COMPUTER, "No computer installed.");
        }
        else
        {
            report.setComputer(comp);
            int compRating = IntegerUtils
                    .parseInt(comp.getComponent().getParams().getString("rating"));
            int sensorDM = IntegerUtils
                    .parseInt(comp.getComponent().getParams().getString("sensorDM"));
            List<ShipComponentInstanceBean> bis = ShipDesignLogic
                    .getAllInstances(ship, ShipComponentBean.COMPUTER_BIS);
            if (bis.size() > 0)
            {
                report.setComputerBis(bis.get(0));
                compRating++;
            }
            report.setCompRating(compRating);
            report.setSensorDM(sensorDM);
        }
    }

    public static void reportJump(ShipDesignBean ship,
            ShipReportBean report)
    {
        ShipComponentInstanceBean jump = ShipDesignLogic.findHighestStringParam(ship,
                ShipComponentBean.JDRIVE, "driveCode");
        if (jump == null)
        {
            report.setJumpCode(" ");
            report.setJumpNumber(0);
            report.setNumberOfJumps(0);
        }
        else
        {
            report.setJumpDrive(jump);
            String jumpCode = jump.getComponent().getParams().getString("driveCode");
            report.setJumpCode(jumpCode);
            report.setJumpNumber(ShipDesignLogic.getDrivePerformance(jumpCode,
                    report.getHullDisplacement()));
            int singleJump = Math.max(1,
                    (report.getHullDisplacement() * report.getJumpNumber())
                            / 10);
            report.setSingleJumpFuel(singleJump);
            int numberOfJumps = report.getFuelTonnage() / singleJump;
            report.setNumberOfJumps(numberOfJumps);
        }
    }

    public static void reportManeuver(ShipDesignBean ship,
            ShipReportBean report)
    {
        ShipComponentInstanceBean man = ShipDesignLogic.findHighestStringParam(ship,
                ShipComponentBean.MDRIVE, "driveCode");
        if (man == null)
        {
            report.setThrustNumber(0);
            report.setWeeksofPower(0);
            report.setManeuverCode(" ");
        }
        else
        {
            report.setManDrive(man);
            String manCode = man.getComponent().getParams().getString("driveCode");
            report.setManeuverCode(manCode);
            report.setThrustNumber(ShipDesignLogic.getDrivePerformance(manCode,
                    report.getHullDisplacement()));
            int fuelPerWeek = ShipDesignLogic.getFuelPerWeek(manCode);
            int singleJump = report.getSingleJumpFuel();
            int jumpVolume = singleJump*report.getNumberOfJumps();
            int weeksofPower = (report.getFuelTonnage() - jumpVolume) / fuelPerWeek;
            report.setWeeksofPower(weeksofPower);
            report.setFuelPerWeek(fuelPerWeek);
        }
    }

    public static void reportHangers(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> hangers = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.HANGER);
        int numberOfHangers = 0;
        for (ShipComponentInstanceBean hanger : hangers)
        {
            boolean unmanned = BooleanUtils.parseBoolean(hanger.getComponent().getParams().get("unmanned"));
            if (unmanned)
                numberOfHangers += hanger.getCount();
        }
        report.setHangers(hangers);
        report.setNumberOfHangers(numberOfHangers);
    }

    public static void reportScreens(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> screens = ShipDesignLogic
                .getAllInstances(ship, ShipComponentBean.SCREENS);
        int numberOfScreens = 0;
        for (ShipComponentInstanceBean screen : screens)
        {
            numberOfScreens += screen.getCount();
        }
        report.setScreens(screens);
        report.setNumberOfScreens(numberOfScreens);
    }

    public static void reportTurrets(ShipDesignBean ship, ShipReportBean report)
    {
        List<ShipComponentInstanceBean> turrets = ShipDesignLogic.getAllInstances(ship, ShipComponentBean.TURRET);
        List<ShipComponentInstanceBean> allTurrets = ShipComponentLogic.expandInstances(turrets);
        List<ShipComponentInstanceBean> weapons = ShipDesignLogic.getAllInstances(ship, ShipComponentBean.WEAPON);
        List<ShipComponentInstanceBean> allWeapons = ShipComponentLogic.expandInstances(weapons);
        int numTurrets = allTurrets.size();
        ShipComponentInstanceBean[][] weaponsByTurret = new ShipComponentInstanceBean[allTurrets.size()][];
        Iterator<ShipComponentInstanceBean> w = allWeapons.iterator();
        int slots = 0;
        int used = 0;
        for (int i = 0; i < allTurrets.size(); i++)
        {
            ShipComponentInstanceBean turret = allTurrets.get(i);
            int capacity = IntegerUtils.parseInt(turret.getComponent().getParams().get("capacity"));
            weaponsByTurret[i] = new ShipComponentInstanceBean[capacity];
            for (int j = 0; j < weaponsByTurret[i].length; j++)
            {
                if (w.hasNext())
                {
                    weaponsByTurret[i][j] = w.next();
                    used++;
                }
                slots++;
            }
        }
        report.setWeaponsByTurret(weaponsByTurret);
        report.setNumberOfTurrets(numTurrets);
        report.setWeaponSlots(slots);
        report.setWeaponSlotsUsed(used);
    }

    public static void reportBays(ShipDesignBean ship, ShipReportBean report)
    {
        int numberOfBays = ShipDesignLogic.countAllInstances(ship, ShipComponentBean.BAY);
        report.setNumberOfBays(numberOfBays);
    }
}
