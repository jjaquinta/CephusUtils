package jo.clight.shipyard.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.ShipComponentLogic;
import jo.clight.core.logic.ShipDesignLogic;
import jo.util.utils.obj.IntegerUtils;

public class ShipEditLogic
{
    public static final Set<String> COMPUTER_IDS = new HashSet<>();
    public static final List<ShipComponentBean> COMPUTERS = new ArrayList<>();
    static
    {
        for (ShipComponentBean comp : ShipComponentLogic.getComponentsByType(ShipComponentBean.COMPUTER))
            if (!comp.getID().endsWith("_bis"))
            {
                COMPUTERS.add(comp);
                COMPUTER_IDS.add("$"+comp.getID());
            }
    }

    public static ShipComponentBean getSingletonType(String type)
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

    public static ShipComponentBean getSingletonType(Collection<String> types)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return null;
        ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(ship, types);
        if (inst == null)
            return null;
        return inst.getComponent();
    }

    public static void setSingletonType(ShipComponentBean item, Collection<String> types)
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

    public static int getSingletonCount(String type)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return 0;
        ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(ship, type);
        if (inst == null)
            return 0;
        return inst.getCount();
    }

    public static void setSingletonCount(String type, int count)
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
                inst = ShipComponentLogic.getInstance(type, 1);
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
        double disp = Math.abs(item.getVolume());
        ShipComponentBean bestBridge = null;
        for (ShipComponentBean bridge : ShipComponentLogic.getComponentsByType(ShipComponentBean.BRIDGE))
        {
            int tonsSupported = IntegerUtils.parseInt(bridge.getParams().get("tonsSupported"));
            if (tonsSupported >= disp)
                if ((bestBridge == null) || (bestBridge.getVolume() > bridge.getVolume()))
                    bestBridge = bridge;
        }
        setSingletonType(bestBridge, ShipComponentBean.BRIDGE);
    }

    public static ShipComponentBean getConfig()
    {
        return getSingletonType(ShipComponentBean.CONFIG);
    }

    public static void setConfig(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.CONFIG);
    }

    public static ShipComponentInstanceBean getArmor()
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return null;
        return ShipDesignLogic.getFirstInstance(ship, ShipComponentBean.ARMOR);
    }

    public static void setArmor(ShipComponentBean item, int count)
    {
        setSingletonType(item, ShipComponentBean.ARMOR);
        if (item != null)
            setSingletonCount(item.getID(), count);
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

    public static void setShipName(String shipName)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return;
        if (!shipName.equals(ship.getShipName()))
        {
            ship.setShipName(shipName);
            RuntimeLogic.getInstance().fireMonotonicPropertyChange("ship.shipName", ship.getShipName());
        }
    }

    public static void setShipFunction(String shipFunction)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return;
        if (!shipFunction.equals(ship.getShipFunction()))
        {
            ship.setShipFunction(shipFunction);
            RuntimeLogic.getInstance().fireMonotonicPropertyChange("ship.shipFunction", ship.getShipFunction());
        }
    }

    public static boolean getShipRole(String role)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return false;
        return ship.getRoles().contains(role);
    }

    public static void setShipRole(String role, boolean selected)
    {
        ShipDesignBean ship = RuntimeLogic.getInstance().getShip();
        if (ship == null)
            return;
        if (ship.getRoles().contains(role) == selected)
            return;
        if (selected)
            ship.getRoles().add(role);
        else
            ship.getRoles().remove(role);
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("ship.shipRoles", ship.getRoles());
    }

}
