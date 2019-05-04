package jo.cephus.core.logic;import java.util.ArrayList;import java.util.Iterator;import java.util.List;import jo.cephus.core.data.ShipComponentBean;import jo.cephus.core.data.ShipComponentInstanceBean;import jo.cephus.core.data.ShipDesignBean;import jo.util.utils.obj.IntegerUtils;public class ShipDesignLogic{    public ShipDesignLogic()    {    }    public static List<ShipComponentInstanceBean> getAllInstances(ShipDesignBean ship, String type)    {        String id = null;        if(type.startsWith("$"))            id = type.substring(1);        List<ShipComponentInstanceBean> instances = new ArrayList<>();        for(ShipComponentInstanceBean instance : ship.getComponents())        {            if(id != null)            {                if(id.equals(instance.getComponentID()))                    instances.add(instance);            } else            {                ShipComponentBean component = instance.getComponent();                if(type.equals(component.getType()))                    instances.add(instance);            }        }        return instances;    }    public static ShipComponentInstanceBean getFirstInstance(ShipDesignBean ship, String type)    {        String id = null;        if(type.startsWith("$"))            id = type.substring(1);        for(ShipComponentInstanceBean instance : ship.getComponents())        {            if(id != null)            {                if(id.equals(instance.getComponentID()))                    return instance;            } else            {                ShipComponentBean component = instance.getComponent();                if(type.equals(component.getType()))                    return instance;            }        }        return null;    }    public static int totalAllInstances(ShipDesignBean ship, String type)    {        List<ShipComponentInstanceBean> instances = getAllInstances(ship, type);        int total = 0;        for(Iterator<ShipComponentInstanceBean> iterator = instances.iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean instance = (ShipComponentInstanceBean)iterator.next();            total += instance.getCount();        }        return total;    }    public static double volumeAllInstances(ShipDesignBean ship, String type)    {        List<ShipComponentInstanceBean> instances = getAllInstances(ship, type);        double total = 0.0D;        for(Iterator<ShipComponentInstanceBean> iterator = instances.iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean instance = (ShipComponentInstanceBean)iterator.next();            total += (double)instance.getCount() * instance.getVolume();        }        return total;    }    public static ShipComponentBean findHighestTech(ShipDesignBean ship, String type)    {        ShipComponentBean best = null;        int bestTech = 0;        for(Iterator<ShipComponentInstanceBean> iterator = getAllInstances(ship, type).iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean component = (ShipComponentInstanceBean)iterator.next();            ShipComponentBean comp = component.getComponent();            if(best == null || comp.getTechLevel() > bestTech)            {                best = comp;                bestTech = comp.getTechLevel();            }        }        return best;    }    public static ShipComponentBean findHighestStringParam(ShipDesignBean ship, String type, String param)    {        ShipComponentBean best = null;        String bestLetter = null;        for(Iterator<ShipComponentInstanceBean> iterator = getAllInstances(ship, type).iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean component = (ShipComponentInstanceBean)iterator.next();            ShipComponentBean comp = component.getComponent();            String letter = comp.getParams().getString(param);            if(best == null || letter.compareTo(bestLetter) > 0)            {                best = comp;                bestLetter = letter;            }        }        return best;    }    public static ShipComponentInstanceBean getHull(ShipDesignBean ship)    {        List<ShipComponentInstanceBean> hulls = getAllInstances(ship, ShipComponentBean.HULL);        if(hulls.size() == 0)            return null;        else            return (ShipComponentInstanceBean)hulls.get(0);    }    public static int getHullDisplacement(ShipDesignBean ship)    {        ShipComponentInstanceBean hull = getHull(ship);        return -(int)hull.getVolume();    }    public static int getHardpoints(ShipDesignBean ship)    {        int disc = getHullDisplacement(ship);        if(disc < 100)            return 1;        else            return disc / 100;    }    public static int getHardpointsUsed(ShipDesignBean ship)    {        int used = 0;        for(Iterator<ShipComponentInstanceBean> iterator = getAllInstances(ship, ShipComponentBean.TURRET).iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean turret = (ShipComponentInstanceBean)iterator.next();            used += turret.getCount();        }        for(Iterator<ShipComponentInstanceBean> iterator1 = getAllInstances(ship, ShipComponentBean.BAY).iterator(); iterator1.hasNext();)        {            ShipComponentInstanceBean bay = (ShipComponentInstanceBean)iterator1.next();            used += bay.getCount();        }        return used;    }    public static int getHardpointsFree(ShipDesignBean ship)    {        return getHardpoints(ship) - getHardpointsUsed(ship);    }    public static int getDrivePerformance(String jumpCode, int hullDisplacement)    {        int col;        for(col = 1; col <= DRIVE_PERFORMANCE[0].length;)            if(IntegerUtils.parseInt(DRIVE_PERFORMANCE[0][col]) >= hullDisplacement)                break;        for(int row = 1; row < DRIVE_PERFORMANCE.length; row++)            if(jumpCode.equals(DRIVE_PERFORMANCE[row][DRIVE_CODE]))                return Integer.parseInt(DRIVE_PERFORMANCE[row][col]);        return 0;    }    private static int getPowerPlantReq(String code, int col)    {        for(int row = 0; row < POWER_PLANT_REQS.length; row++)            if(code.equals(POWER_PLANT_REQS[row][DRIVE_CODE]))                return IntegerUtils.parseInt(POWER_PLANT_REQS[row][col]);        return 0;    }    public static int getFuelPerWeek(String code)    {        return getPowerPlantReq(code, FUEL_PER_WEEK);    }    public static int getMinFuel(String code)    {        return getPowerPlantReq(code, MIN_FUEL);    }    private static final String DRIVE_PERFORMANCE[][] = {        {            "", "100", "200", "300", "400", "500", "600", "700", "800", "900",             "1000", "1200", "1400", "1600", "1800", "2000", "3000", "4000", "5000"        }, {            "A", "2", "1", "", "", "", "", "", "", "",             "", "", "", "", "", "", "", "", ""        }, {            "B", "4", "2", "1", "1", "", "", "", "", "",             "", "", "", "", "", "", "", "", ""        }, {            "C", "6", "3", "2", "1", "1", "1", "", "", "",             "", "", "", "", "", "", "", "", ""        }, {            "D", "", "4", "2", "2", "1", "1", "1", "1", "",             "", "", "", "", "", "", "", "", ""        }, {            "E", "", "5", "3", "2", "2", "1", "1", "1", "1",             "1", "", "", "", "", "", "", "", ""        }, {            "F", "", "6", "4", "3", "2", "2", "1", "1", "1",             "1", "1", "", "", "", "", "", "", ""        }, {            "G", "", "", "4", "3", "2", "2", "2", "2", "1",             "1", "1", "1", "", "", "", "", "", ""        }, {            "H", "", "", "5", "4", "3", "2", "2", "2", "2",             "2", "1", "1", "1", "", "", "", "", ""        }, {            "J", "", "", "6", "4", "3", "3", "2", "2", "2",             "2", "2", "1", "1", "1", "", "", "", ""        }, {            "K", "", "", "", "5", "4", "3", "3", "3", "2",             "2", "2", "2", "1", "1", "1", "", "", ""        }, {            "L", "", "", "", "5", "4", "3", "3", "3", "3",             "3", "2", "2", "2", "1", "1", "", "", ""        }, {            "M", "", "", "", "6", "4", "4", "3", "3", "3",             "3", "3", "2", "2", "2", "1", "", "", ""        }, {            "N", "", "", "", "6", "5", "4", "4", "4", "3",             "3", "3", "3", "2", "2", "2", "", "", ""        }, {            "P", "", "", "", "", "5", "4", "4", "4", "4",             "4", "3", "3", "3", "2", "2", "", "", ""        }, {            "Q", "", "", "", "", "6", "5", "4", "4", "4",             "4", "4", "3", "3", "3", "2", "1", "", ""        }, {            "R", "", "", "", "", "6", "5", "5", "5", "4",             "4", "4", "4", "3", "3", "3", "1", "", ""        }, {            "S", "", "", "", "", "6", "5", "5", "5", "5",             "5", "4", "4", "4", "3", "3", "1", "", ""        }, {            "T", "", "", "", "", "", "6", "5", "5", "5",             "5", "5", "4", "4", "4", "3", "2", "", ""        }, {            "U", "", "", "", "", "", "6", "6", "5", "5",             "5", "5", "4", "4", "4", "4", "2", "", ""        }, {            "V", "", "", "", "", "", "6", "6", "6", "5",             "5", "5", "5", "4", "4", "4", "2", "1", ""        }, {            "W", "", "", "", "", "", "", "6", "6", "6",             "5", "5", "5", "4", "4", "4", "3", "1", "1"        }, {            "X", "", "", "", "", "", "", "6", "6", "6",             "6", "5", "5", "5", "4", "4", "3", "1", "1"        }, {            "Y", "", "", "", "", "", "", "6", "6", "6",             "6", "5", "5", "5", "4", "4", "3", "2", "1"        }, {            "Z", "", "", "", "", "", "", "6", "6", "6",             "6", "6", "5", "5", "5", "4", "4", "2", "2"        }    };    private static final int DRIVE_CODE = 0;    private static final int FUEL_PER_WEEK = 2;    private static final int MIN_FUEL = 3;    private static final String POWER_PLANT_REQS[][] = {        {            "A", "4", "1", "2"        }, {            "B", "7", "2", "4"        }, {            "C", "10", "3", "6"        }, {            "D", "13", "4", "8"        }, {            "E", "16", "5", "10"        }, {            "F", "19", "6", "12"        }, {            "G", "22", "7", "14"        }, {            "H", "25", "8", "16"        }, {            "J", "28", "9", "18"        }, {            "K", "31", "10", "20"        }, {            "L", "34", "11", "22"        }, {            "M", "37", "12", "24"        }, {            "N", "40", "13", "26"        }, {            "P", "43", "14", "28"        }, {            "Q", "46", "15", "30"        }, {            "R", "49", "16", "32"        }, {            "S", "52", "17", "34"        }, {            "T", "55", "18", "36"        }, {            "U", "58", "19", "38"        }, {            "V", "61", "20", "40"        }, {            "W", "64", "21", "42"        }, {            "X", "67", "22", "44"        }, {            "Y", "70", "23", "46"        }, {            "Z", "73", "24", "48"        }    };}