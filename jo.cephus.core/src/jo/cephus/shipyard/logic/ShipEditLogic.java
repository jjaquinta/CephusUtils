package jo.cephus.shipyard.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipComponentInstanceBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.ShipComponentLogic;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.util.utils.obj.IntegerUtils;

public class ShipEditLogic
{
    private static final Set<String> COMPUTER_IDS = new HashSet<>();
    public static final List<ShipComponentBean> COMPUTERS = new ArrayList<>();
    private static final Set<String> COMPUTER_FIB_IDS = new HashSet<>();
    private static ShipComponentBean COMPUTER_FIB = null;
    private static final Set<String> COMPUTER_BIS_IDS = new HashSet<>();
    private static ShipComponentBean COMPUTER_BIS = null;
    static
    {
        for (ShipComponentBean comp : ShipComponentLogic.getComponentsByType(ShipComponentBean.COMPUTER))
            if (comp.getID().endsWith("_fib"))
            {
                COMPUTER_FIB = comp;
                COMPUTER_FIB_IDS.add("$"+comp.getID());
            }
            else if (comp.getID().endsWith("_bis"))
            {
                COMPUTER_BIS = comp;
                COMPUTER_BIS_IDS.add("$"+comp.getID());
            }
            else
            {
                COMPUTERS.add(comp);
                COMPUTER_IDS.add("$"+comp.getID());
            }
    }

    private static ShipComponentBean getSingletonType(String type)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return null;
        ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(ship, type);
        if (inst == null)
            return null;
        return inst.getComponent();
    }

    private static void setSingletonType(ShipComponentBean item, String type)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return;
        ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(ship, type);
        if (inst != null)
            ship.getComponents().remove(inst);
        if (item != null)
        {
            inst = ShipComponentLogic.getInstance(item.getID(), 1);
            ship.getComponents().add(inst);
        }
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("ship.components", ship.getComponents());
    }

    private static ShipComponentBean getSingletonType(Set<String> types)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return null;
        ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(ship, types);
        if (inst == null)
            return null;
        return inst.getComponent();
    }

    private static void setSingletonType(ShipComponentBean item, Set<String> types)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return;
        ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(ship, types);
        if (inst != null)
            ship.getComponents().remove(inst);
        if (item != null)
        {
            inst = ShipComponentLogic.getInstance(item.getID(), 1);
            ship.getComponents().add(inst);
        }
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("ship.components", ship.getComponents());
    }

    private static int getSingletonCount(String type)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return 0;
        ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(ship, type);
        if (inst == null)
            return 0;
        return inst.getCount();
    }

    private static void setSingletonCount(int count, String type)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return;
        ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(ship, type);
        if (count == 0)
        {
            if (inst != null)
                ship.getComponents().remove(inst);
        }
        else
        {
            if (inst == null)
            {
                inst = ShipComponentLogic.getInstance(type.substring(1), 1);
                ship.getComponents().add(inst);
            }
            inst.setCount(count);
        }
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("ship.components", ship.getComponents());
    }

    public static ShipComponentBean getHull()
    {
        return getSingletonType(ShipComponentBean.HULL);
    }

    public static void setHull(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.HULL);
        for (ShipComponentBean bridge : ShipComponentLogic.getComponentsByType(ShipComponentBean.BRIDGE))
        {
            int tonsSupported = IntegerUtils.parseInt(bridge.getParams().get("tonsSupported"));
            if (tonsSupported >= item.getVolume())
            {
                setSingletonType(bridge, ShipComponentBean.BRIDGE);
            }
        }
    }

    public static ShipComponentBean getConfig()
    {
        return getSingletonType(ShipComponentBean.CONFIG);
    }

    public static void setConfig(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.CONFIG);
    }

    public static ShipComponentBean getArmor()
    {
        return getSingletonType(ShipComponentBean.ARMOR);
    }

    public static void setArmor(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.ARMOR);
    }

    public static ShipComponentBean getManeuver()
    {
        return getSingletonType(ShipComponentBean.MDRIVE);
    }

    public static void setManeuver(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.MDRIVE);
    }

    public static ShipComponentBean getJumpDrive()
    {
        return getSingletonType(ShipComponentBean.JDRIVE);
    }

    public static void setJumpDrive(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.JDRIVE);
    }

    public static ShipComponentBean getPower()
    {
        return getSingletonType(ShipComponentBean.PPLANT);
    }

    public static void setPower(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.PPLANT);
    }

    public static int getWeeksOfPower()
    {
        ShipReportBean report = RuntimeLogic.getInstance().getReport();
        if (report == null)
            return 0;
        return report.getWeeksofPower();
    }

    public static void setWeeksOfPower(int weeks)
    {
        ShipReportBean report = RuntimeLogic.getInstance().getReport();
        if (report == null)
            return;
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return;
        String manCode = report.getManeuverCode();
        int fuelPerWeek = ShipDesignLogic.getFuelPerWeek(manCode);
        int singleJump = Math.max(1,
                (report.getHullDisplacement() * report.getJumpNumber())
                        / 10);
        int jumpVolume = singleJump*report.getNumberOfJumps();
        //int weeksofPower = (report.getFuelTonnage() - jumpVolume) / fuelPerWeek;
        int desiredTonnage = weeks*fuelPerWeek + jumpVolume;
        
        ShipComponentInstanceBean fuelInst = ShipDesignLogic.getFirstInstance(ship, ShipComponentBean.FUEL);
        if (fuelInst == null)
        {
            fuelInst = ShipComponentLogic.getInstance("fuel", 0);
            ship.getComponents().add(fuelInst);
        }
        fuelInst.setCount(desiredTonnage);
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("ship.components", ship.getComponents());
    }
    
    public static int getNumberOfJumps()
    {
        ShipReportBean report = RuntimeLogic.getInstance().getReport();
        if (report == null)
            return 0;
        return report.getNumberOfJumps();
    }

    public static void setNumberOfJumps(int jumps)
    {
        ShipReportBean report = RuntimeLogic.getInstance().getReport();
        if (report == null)
            return;
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return;
        String manCode = report.getManeuverCode();
        int fuelPerWeek = ShipDesignLogic.getFuelPerWeek(manCode);
        int weeksOfPower = report.getWeeksofPower();
        int singleJump = Math.max(1,
                (report.getHullDisplacement() * report.getJumpNumber())
                        / 10);
        int jumpVolume = singleJump*jumps;
        int desiredTonnage = weeksOfPower*fuelPerWeek + jumpVolume;
        
        ShipComponentInstanceBean fuelInst = ShipDesignLogic.getFirstInstance(ship, ShipComponentBean.FUEL);
        if (fuelInst == null)
        {
            fuelInst = ShipComponentLogic.getInstance("fuel", 0);
            ship.getComponents().add(fuelInst);
        }
        fuelInst.setCount(desiredTonnage);
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("ship.components", ship.getComponents());
    }

    public static ShipComponentBean getComputer()
    {
        return getSingletonType(COMPUTER_IDS);
    }

    public static void setComputer(ShipComponentBean item)
    {
        setSingletonType(item, COMPUTER_IDS);
    }

    public static boolean getComputerFib()
    {
        return getSingletonType(COMPUTER_FIB_IDS) != null;
    }

    public static void setComputerFib(boolean item)
    {
        setSingletonType(item ? COMPUTER_FIB : null, COMPUTER_FIB_IDS);
    }

    public static boolean getComputerBis()
    {
        return getSingletonType(COMPUTER_BIS_IDS) != null;
    }

    public static void setComputerBis(boolean item)
    {
        setSingletonType(item ? COMPUTER_BIS : null, COMPUTER_BIS_IDS);
    }

    public static ShipComponentBean getElectronics()
    {
        return getSingletonType(ShipComponentBean.ELECTRONICS);
    }

    public static void setElectronics(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.ELECTRONICS);
    }

    public static int getStaterooms()
    {
        return getSingletonCount("$stateroom");
    }

    public static void setStaterooms(int count)
    {
        setSingletonCount(count, "$stateroom");
    }

    public static int getBerths()
    {
        return getSingletonCount("$lowberth");
    }

    public static void setBerths(int count)
    {
        setSingletonCount(count, "$lowberth");
    }

    public static int getEmergencyBerths()
    {
        return getSingletonCount("$emergency_lowberth");
    }

    public static void setEmergencyBerths(int count)
    {
        setSingletonCount(count, "$emergency_lowberth");
    }

    public static int getBarracks() { return getSingletonCount("$barracks"); } public static void setBarracks(int count) { setSingletonCount(count, "$barracks"); }
    public static int getArmory() { return getSingletonCount("$armory"); } public static void setArmory(int count) { setSingletonCount(count, "$armory"); }
    public static int getBriefingRoom() { return getSingletonCount("$briefing_room"); } public static void setBriefingRoom(int count) { setSingletonCount(count, "$briefing_room"); }
    public static int getDetentionCell() { return getSingletonCount("$detention_cell"); } public static void setDetentionCell(int count) { setSingletonCount(count, "$detention_cell"); }
    public static int getFuelScoops() { return getSingletonCount("$fuel_scoops"); } public static void setFuelScoops(int count) { setSingletonCount(count, "$fuel_scoops"); }
    public static int getFuelProcessor() { return getSingletonCount("$fuel_processor"); } public static void setFuelProcessor(int count) { setSingletonCount(count, "$fuel_processor"); }
    public static int getLab() { return getSingletonCount("$lab"); } public static void setLab(int count) { setSingletonCount(count, "$lab"); }
    public static int getLibrary() { return getSingletonCount("$library"); } public static void setLibrary(int count) { setSingletonCount(count, "$library"); }
    public static int getLuxuries() { return getSingletonCount("$luxuries"); } public static void setLuxuries(int count) { setSingletonCount(count, "$luxuries"); }
    public static int getVault() { return getSingletonCount("$vault"); } public static void setVault(int count) { setSingletonCount(count, "$vault"); }
}
