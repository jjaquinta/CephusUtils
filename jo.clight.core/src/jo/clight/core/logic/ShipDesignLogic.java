package jo.clight.core.logic;import java.util.ArrayList;import java.util.Collection;import java.util.HashMap;import java.util.Iterator;import java.util.List;import java.util.Map;import jo.clight.core.data.ShipComponentBean;import jo.clight.core.data.ShipComponentInstanceBean;import jo.clight.core.data.ShipDesignBean;import jo.util.utils.obj.BooleanUtils;import jo.util.utils.obj.DoubleUtils;import jo.util.utils.obj.IntegerUtils;import jo.util.utils.obj.StringUtils;public class ShipDesignLogic{    public ShipDesignLogic()    {    }    public static List<ShipComponentInstanceBean> getAllInstances(ShipDesignBean ship, String type)    {        if (type.startsWith("$"))            type = type.substring(1);        List<ShipComponentInstanceBean> instances = new ArrayList<>();        for (ShipComponentInstanceBean instance : ship.getComponents())        {            if (type.equals(instance.getComponentID()))                instances.add(instance);            else            {                ShipComponentBean component = instance.getComponent();                if(type.equals(component.getType()))                    instances.add(instance);            }        }        return instances;    }    public static ShipComponentInstanceBean getFirstInstance(ShipDesignBean ship, String type)    {        if (ship == null)            return null;        if (type.startsWith("$"))            type = type.substring(1);        for(ShipComponentInstanceBean instance : ship.getComponents().toArray(new ShipComponentInstanceBean[0]))        {            if (type.equals(instance.getComponentID()))                return instance;            ShipComponentBean component = instance.getComponent();            if (type.equals(component.getType()))                return instance;        }        return null;    }    public static ShipComponentInstanceBean getFirstInstance(ShipDesignBean ship, Collection<String> types)    {        for(ShipComponentInstanceBean instance : ship.getComponents())        {            if (types.contains("$"+instance.getComponentID()))                return instance;            if (types.contains(instance.getComponent().getType()))                return instance;        }        return null;    }    public static int countAllInstances(ShipDesignBean ship, String type)    {        List<ShipComponentInstanceBean> instances = getAllInstances(ship, type);        int total = 0;        for(Iterator<ShipComponentInstanceBean> iterator = instances.iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean instance = (ShipComponentInstanceBean)iterator.next();            total += instance.getCount();        }        return total;    }    public static double priceAllInstances(ShipDesignBean ship, String type)    {        try        {            ParameterizedLogic.addContext(ship);            List<ShipComponentInstanceBean> instances = getAllInstances(ship, type);            double total = 0;            for(Iterator<ShipComponentInstanceBean> iterator = instances.iterator(); iterator.hasNext();)            {                ShipComponentInstanceBean instance = (ShipComponentInstanceBean)iterator.next();                total += instance.getPrice()*instance.getCount();            }            return total;        }        finally        {            ParameterizedLogic.removeContext(ship);        }    }    public static double volumeAllInstances(ShipDesignBean ship, String type)    {        try        {            ParameterizedLogic.addContext(ship);            List<ShipComponentInstanceBean> instances = getAllInstances(ship, type);            double total = 0.0D;            for(Iterator<ShipComponentInstanceBean> iterator = instances.iterator(); iterator.hasNext();)            {                ShipComponentInstanceBean instance = (ShipComponentInstanceBean)iterator.next();                total += (double)instance.getCount() * instance.getVolume();            }            return total;        }        finally        {            ParameterizedLogic.removeContext(ship);        }    }    public static ShipComponentBean findHighestTech(ShipDesignBean ship, String type)    {        ShipComponentBean best = null;        int bestTech = 0;        for(Iterator<ShipComponentInstanceBean> iterator = getAllInstances(ship, type).iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean component = (ShipComponentInstanceBean)iterator.next();            ShipComponentBean comp = component.getComponent();            if(best == null || comp.getTechLevel() > bestTech)            {                best = comp;                bestTech = comp.getTechLevel();            }        }        return best;    }    public static ShipComponentInstanceBean findHighestStringParam(ShipDesignBean ship, String type, String param)    {        ShipComponentInstanceBean best = null;        String bestLetter = null;        for(Iterator<ShipComponentInstanceBean> iterator = getAllInstances(ship, type).iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean component = (ShipComponentInstanceBean)iterator.next();            ShipComponentBean comp = component.getComponent();            String letter = comp.getParams().getString(param);            if (letter == null)                continue;            if(best == null || letter.compareTo(bestLetter) > 0)            {                best = component;                bestLetter = letter;            }        }        return best;    }    public static ShipComponentBean findHighestNumberParam(ShipDesignBean ship, String type, String param)    {        ShipComponentBean best = null;        double bestNumber = 0;        for(Iterator<ShipComponentInstanceBean> iterator = getAllInstances(ship, type).iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean component = (ShipComponentInstanceBean)iterator.next();            ShipComponentBean comp = component.getComponent();            double letter = DoubleUtils.parseDouble(comp.getParams().get(param));            if (letter == 0)                continue;            if(best == null || letter > bestNumber)            {                best = comp;                bestNumber = letter;            }        }        return best;    }    public static ShipComponentInstanceBean getHull(ShipDesignBean ship)    {        ShipComponentInstanceBean hull = getFirstInstance(ship, ShipComponentBean.HULL);        return hull;    }    public static int getHullDisplacement(ShipDesignBean ship)    {        ShipComponentInstanceBean hull = getHull(ship);        if (hull == null)            return 0;        return -(int)hull.getVolume();    }    public static int getHardpoints(ShipDesignBean ship)    {        int disc = getHullDisplacement(ship);        if(disc < 100)            return 1;        else            return disc / 100;    }    public static int getHardpointsUsed(ShipDesignBean ship)    {        int used = 0;        for(Iterator<ShipComponentInstanceBean> iterator = getAllInstances(ship, ShipComponentBean.TURRET).iterator(); iterator.hasNext();)        {            ShipComponentInstanceBean turret = (ShipComponentInstanceBean)iterator.next();            used += turret.getCount();        }        for(Iterator<ShipComponentInstanceBean> iterator1 = getAllInstances(ship, ShipComponentBean.BAY).iterator(); iterator1.hasNext();)        {            ShipComponentInstanceBean bay = (ShipComponentInstanceBean)iterator1.next();            used += bay.getCount();        }        return used;    }    public static int getHardpointsFree(ShipDesignBean ship)    {        return getHardpoints(ship) - getHardpointsUsed(ship);    }    public static int getDrivePerformance(String jumpCode, int hullDisplacement)    {        if (StringUtils.isTrivial(jumpCode) || " ".equals(jumpCode))            return 0;        int col;        for(col = 1; col <= DRIVE_PERFORMANCE[0].length; col++)            if(IntegerUtils.parseInt(DRIVE_PERFORMANCE[0][col]) >= hullDisplacement)                break;        for(int row = 1; row < DRIVE_PERFORMANCE.length; row++)            if(jumpCode.equals(DRIVE_PERFORMANCE[row][DRIVE_CODE]))                return IntegerUtils.parseInt(DRIVE_PERFORMANCE[row][col]);        return 0;    }    private static int getPowerPlantReq(String code, int col)    {        for(int row = 0; row < POWER_PLANT_REQS.length; row++)            if(code.equals(POWER_PLANT_REQS[row][DRIVE_CODE]))                return IntegerUtils.parseInt(POWER_PLANT_REQS[row][col]);        return 0;    }    public static int getFuelPerWeek(String code)    {        return getPowerPlantReq(code, FUEL_PER_WEEK);    }    public static int getMinFuel(String code)    {        return getPowerPlantReq(code, MIN_FUEL);    }    public static Map<String, Integer> getCrew(ShipDesignBean ship)    {        int disp = ShipDesignLogic.getHullDisplacement(ship);        boolean isSmall = disp <= 100;        boolean isLarge = disp >= 1000;        boolean isMilitary = ship.getRoles().contains(ShipDesignBean.ROLE_MILITARY);        //boolean isPassenger = ship.getRoles().contains(ShipDesignBean.ROLE_PASSENGERS);        ShipComponentInstanceBean comp = ShipDesignLogic.findHighestStringParam(ship,                ShipComponentBean.COMPUTER, "rating");        int sensorDM = 0;        if (comp != null)            sensorDM = IntegerUtils.parseInt(comp.getComponent().getParams().getString("sensorDM"));        Map<String, Integer> crew = new HashMap<String, Integer>();        if (isMilitary)            crew.put(ShipComponentBean.CREW_PILOT, 3);        else            crew.put(ShipComponentBean.CREW_PILOT, 1);        if (!isSmall)            if (isMilitary)                crew.put(ShipComponentBean.CREW_SENSOR, 3);            else                crew.put(ShipComponentBean.CREW_SENSOR, 1);        if (!isSmall)        {            double drives = ShipDesignLogic.volumeAllInstances(ship, ShipComponentBean.MDRIVE)                    + ShipDesignLogic.volumeAllInstances(ship, ShipComponentBean.JDRIVE)                    + ShipDesignLogic.volumeAllInstances(ship, ShipComponentBean.PPLANT);            int numEngineers = (int)(drives/35);            if (numEngineers > 0)                crew.put(ShipComponentBean.CREW_ENGINEER, numEngineers);        }        int gunners = countAllInstances(ship, ShipComponentBean.TURRET);        if (sensorDM >= 0)        {            gunners -= (sensorDM + 1);            if (gunners < 0)                gunners = 0;        }        gunners += 2*countAllInstances(ship, ShipComponentBean.BAY);        gunners += countAllInstances(ship, ShipComponentBean.SCREENS);        crew.put(ShipComponentBean.CREW_GUNNER, gunners);        int craft = getCraft(ship);        crew.put(ShipComponentBean.CREW_PILOT, crew.get(ShipComponentBean.CREW_PILOT) + craft);        if (!isMilitary && isLarge)            crew.put(ShipComponentBean.CREW_PURSER, 1);        if (isMilitary || isLarge)            crew.put(ShipComponentBean.CREW_CAPTAIN, 1);        int crewSoFar = 0;        for (Integer v : crew.values())            crewSoFar += v;        int medics = crewSoFar/50;        if (medics > 0)            crew.put(ShipComponentBean.CREW_MEDIC, medics);        return crew;    }    public static int getCraft(ShipDesignBean ship)    {        int craft = 0;        for (ShipComponentInstanceBean hanger : ShipDesignLogic.getAllInstances(ship, ShipComponentBean.HANGER))        {            boolean unmanned = BooleanUtils.parseBoolean(hanger.getComponent().getParams().get("unmanned"));            if (!unmanned)                craft += hanger.getCount();        }        return craft;    }    public static int findMaxTech(ShipDesignBean ship)    {        int tl = 0;        for (ShipComponentInstanceBean component : ship.getComponents())            tl = Math.max(tl, component.getTechLevel());        return tl;    }        private static final String DRIVE_PERFORMANCE[][] = {        {            "", "100", "200", "300", "400", "500", "600", "700", "800", "900",             "1000", "1200", "1400", "1600", "1800", "2000", "3000", "4000", "5000"        }, {            "A", "2", "1", "", "", "", "", "", "", "",             "", "", "", "", "", "", "", "", ""        }, {            "B", "4", "2", "1", "1", "", "", "", "", "",             "", "", "", "", "", "", "", "", ""        }, {            "C", "6", "3", "2", "1", "1", "1", "", "", "",             "", "", "", "", "", "", "", "", ""        }, {            "D", "", "4", "2", "2", "1", "1", "1", "1", "",             "", "", "", "", "", "", "", "", ""        }, {            "E", "", "5", "3", "2", "2", "1", "1", "1", "1",             "1", "", "", "", "", "", "", "", ""        }, {            "F", "", "6", "4", "3", "2", "2", "1", "1", "1",             "1", "1", "", "", "", "", "", "", ""        }, {            "G", "", "", "4", "3", "2", "2", "2", "2", "1",             "1", "1", "1", "", "", "", "", "", ""        }, {            "H", "", "", "5", "4", "3", "2", "2", "2", "2",             "2", "1", "1", "1", "", "", "", "", ""        }, {            "J", "", "", "6", "4", "3", "3", "2", "2", "2",             "2", "2", "1", "1", "1", "", "", "", ""        }, {            "K", "", "", "", "5", "4", "3", "3", "3", "2",             "2", "2", "2", "1", "1", "1", "", "", ""        }, {            "L", "", "", "", "5", "4", "3", "3", "3", "3",             "3", "2", "2", "2", "1", "1", "", "", ""        }, {            "M", "", "", "", "6", "4", "4", "3", "3", "3",             "3", "3", "2", "2", "2", "1", "", "", ""        }, {            "N", "", "", "", "6", "5", "4", "4", "4", "3",             "3", "3", "3", "2", "2", "2", "", "", ""        }, {            "P", "", "", "", "", "5", "4", "4", "4", "4",             "4", "3", "3", "3", "2", "2", "", "", ""        }, {            "Q", "", "", "", "", "6", "5", "4", "4", "4",             "4", "4", "3", "3", "3", "2", "1", "", ""        }, {            "R", "", "", "", "", "6", "5", "5", "5", "4",             "4", "4", "4", "3", "3", "3", "1", "", ""        }, {            "S", "", "", "", "", "6", "5", "5", "5", "5",             "5", "4", "4", "4", "3", "3", "1", "", ""        }, {            "T", "", "", "", "", "", "6", "5", "5", "5",             "5", "5", "4", "4", "4", "3", "2", "", ""        }, {            "U", "", "", "", "", "", "6", "6", "5", "5",             "5", "5", "4", "4", "4", "4", "2", "", ""        }, {            "V", "", "", "", "", "", "6", "6", "6", "5",             "5", "5", "5", "4", "4", "4", "2", "1", ""        }, {            "W", "", "", "", "", "", "", "6", "6", "6",             "5", "5", "5", "4", "4", "4", "3", "1", "1"        }, {            "X", "", "", "", "", "", "", "6", "6", "6",             "6", "5", "5", "5", "4", "4", "3", "1", "1"        }, {            "Y", "", "", "", "", "", "", "6", "6", "6",             "6", "5", "5", "5", "4", "4", "3", "2", "1"        }, {            "Z", "", "", "", "", "", "", "6", "6", "6",             "6", "6", "5", "5", "5", "4", "4", "2", "2"        }    };    private static final int DRIVE_CODE = 0;    private static final int FUEL_PER_WEEK = 2;    private static final int MIN_FUEL = 3;    private static final String POWER_PLANT_REQS[][] = {        {            "A", "4", "1", "2"        }, {            "B", "7", "2", "4"        }, {            "C", "10", "3", "6"        }, {            "D", "13", "4", "8"        }, {            "E", "16", "5", "10"        }, {            "F", "19", "6", "12"        }, {            "G", "22", "7", "14"        }, {            "H", "25", "8", "16"        }, {            "J", "28", "9", "18"        }, {            "K", "31", "10", "20"        }, {            "L", "34", "11", "22"        }, {            "M", "37", "12", "24"        }, {            "N", "40", "13", "26"        }, {            "P", "43", "14", "28"        }, {            "Q", "46", "15", "30"        }, {            "R", "49", "16", "32"        }, {            "S", "52", "17", "34"        }, {            "T", "55", "18", "36"        }, {            "U", "58", "19", "38"        }, {            "V", "61", "20", "40"        }, {            "W", "64", "21", "42"        }, {            "X", "67", "22", "44"        }, {            "Y", "70", "23", "46"        }, {            "Z", "73", "24", "48"        }    };}