package jo.cephus.shipyard.logic;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipComponentInstanceBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ShipComponentLogic;
import jo.cephus.core.logic.ShipDesignLogic;

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

}
