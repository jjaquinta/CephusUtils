package jo.cephus.shipyard.logic;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipComponentInstanceBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.ShipComponentLogic;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.util.utils.obj.IntegerUtils;

public class ShipEditLogic
{

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

    public static void setSingletonType(ShipComponentBean item, String type)
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
}
