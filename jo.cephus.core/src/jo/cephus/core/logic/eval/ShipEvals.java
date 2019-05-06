// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ShipEvals.java

package jo.cephus.core.logic.eval;

import java.util.Iterator;
import java.util.List;

import jo.cephus.core.data.IParamEval;
import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipComponentInstanceBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ParameterizedLogic;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.util.utils.obj.DoubleUtils;

public class ShipEvals
{

    public ShipEvals()
    {
    }

    public static void init()
    {
        ParameterizedLogic.addEval("comp$bay$maxCopies", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doBayMaxCopies((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$bridge$price", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doBridgePrice((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$comp$bis$maxCopies", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doCompBisMaxCopies((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$comp$bis$price", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doCompBisPrice((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$comp$fib$maxCopies", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doCompFibMaxCopies((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$comp$fib$price", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doCompFibPrice((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$config$price", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doConfigPrice((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$armor$price", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doArmorPrice((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$weapon$maxCopies", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doWeaponMaxCopies((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$hanger$drone$volume", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doHangerDroneVolume((ShipComponentBean)base, hardCoded);
            }

        }
);
        ParameterizedLogic.addEval("comp$hanger$drone$price", new IParamEval() {

            public Object getParameterizedValue(Object base, Object hardCoded)
            {
                return ShipEvals.doHangerDronePrice((ShipComponentBean)base, hardCoded);
            }

        }
);
    }

    private static ShipDesignBean getShip()
    {
        return (ShipDesignBean)ParameterizedLogic.getContext(ShipDesignBean.class);
    }

    private static ShipComponentInstanceBean getInstance()
    {
        return (ShipComponentInstanceBean)ParameterizedLogic.getContext(ShipComponentInstanceBean.class);
    }

    private static Object doBayMaxCopies(ShipComponentBean base, Object hardCoded)
    {
        int val = ShipDesignLogic.getHardpointsFree(getShip());
        ShipComponentInstanceBean inst = getInstance();
        if(inst != null)
            val += inst.getCount();
        return Integer.valueOf(val);
    }

    private static Object doBridgePrice(ShipComponentBean base, Object hardCoded)
    {
        int disp = ShipDesignLogic.getHullDisplacement(getShip());
        double factor = Math.min(1, disp / 100);
        return Double.valueOf(factor * 0.5D);
    }

    private static Object doCompBisMaxCopies(ShipComponentBean base, Object hardCoded)
    {
        int computers = ShipDesignLogic.totalAllInstances(getShip(), ShipComponentBean.COMPUTER);
        return Integer.valueOf(computers);
    }

    private static Object doCompBisPrice(ShipComponentBean base, Object hardCoded)
    {
        List<ShipComponentInstanceBean> computers = ShipDesignLogic.getAllInstances(getShip(), ShipComponentBean.COMPUTER);
        double price = 0.0D;
        for(Iterator<ShipComponentInstanceBean> iterator = computers.iterator(); iterator.hasNext();)
        {
            ShipComponentInstanceBean computer = (ShipComponentInstanceBean)iterator.next();
            price = Math.max(price, computer.getComponent().getPrice());
        }

        return Double.valueOf(price * 0.5D);
    }

    private static Object doCompFibMaxCopies(ShipComponentBean base, Object hardCoded)
    {
        int computers = ShipDesignLogic.totalAllInstances(getShip(), ShipComponentBean.COMPUTER);
        return Integer.valueOf(computers);
    }

    private static Object doCompFibPrice(ShipComponentBean base, Object hardCoded)
    {
        List<ShipComponentInstanceBean> computers = ShipDesignLogic.getAllInstances(getShip(), ShipComponentBean.COMPUTER);
        double price = 0.0D;
        for(Iterator<ShipComponentInstanceBean> iterator = computers.iterator(); iterator.hasNext();)
        {
            ShipComponentInstanceBean computer = iterator.next();
            price = Math.max(price, computer.getComponent().getPrice());
        }

        return Double.valueOf(price * 0.5D);
    }

    private static Object doConfigPrice(ShipComponentBean base, Object hardCoded)
    {
        double mod = DoubleUtils.parseDouble(base.getParams().get("hullCostModifier"));
        ShipComponentInstanceBean hullInstance = ShipDesignLogic.getHull(getShip());
        if(hullInstance == null)
        {
            return hardCoded;
        } else
        {
            ShipComponentBean hull = hullInstance.getComponent();
            return Double.valueOf(hull.getPrice() * (mod - 1.0D));
        }
    }

    private static Object doArmorPrice(ShipComponentBean base, Object hardCoded)
    {
        ShipComponentInstanceBean hull = ShipDesignLogic.getHull(getShip());
        double hullCostModifier = DoubleUtils.parseDouble(base.getParams().get("hullCostModifier"));
        double price = hull.getComponent().getPrice();
        return Double.valueOf(price * hullCostModifier);
    }

    private static Object doWeaponMaxCopies(ShipComponentBean base, Object hardCoded)
    {
        int val = ShipDesignLogic.getHardpointsFree(getShip());
        ShipComponentInstanceBean inst = getInstance();
        if(inst != null)
            val += inst.getCount();
        return Integer.valueOf(val);
    }

    private static Object doHangerDroneVolume(ShipComponentBean base, Object hardCoded)
    {
        int disp = ShipDesignLogic.getHullDisplacement(getShip());
        double volume = disp / 100.0;
        return volume;
    }

    private static Object doHangerDronePrice(ShipComponentBean base, Object hardCoded)
    {
        int disp = ShipDesignLogic.getHullDisplacement(getShip());
        double price = disp * 0.2;
        return price;
    }

}
