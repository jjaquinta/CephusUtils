package jo.cephus.shipyard.logic;

import java.util.ArrayList;
import java.util.Collection;
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
    // specials (singleton count)
    public static final String VAULT = "$vault";
    public static final String LUXURIES = "$luxuries";
    public static final String LIBRARY = "$library";
    public static final String LAB = "$lab";
    public static final String FUEL_PROCESSOR = "$fuel_processor";
    public static final String DETENTION_CELL = "$detention_cell";
    public static final String BRIEFING_ROOM = "$briefing_room";
    public static final String ARMORY = "$armory";
    public static final String COMPUTER_FIB = "$computer_fib";
    public static final String COMPUTER_BIS = "$computer_bis";
    public static final String TURRET_SINGLE = "$single_turret";
    public static final String TURRET_DOUBLE = "$double_turret";
    public static final String TURRET_TRIPLE = "$triple_turret";
    public static final String TURRET_POPUP_SINGLE = "$single_popup_turret";
    public static final String TURRET_POPUP_DOUBLE = "$double_popup_turret";
    public static final String TURRET_POPUP_TRIPLE = "$triple_popup_turret";
    public static final String TURRET_FIXED_SINGLE = "$single_fixed_turret";
    public static final String TURRET_FIXED_DOUBLE = "$double_fixed_turret";
    public static final String TURRET_FIXED_TRIPLE = "$triple_fixed_turret";
    public static final String BAY_MISSILE = "$missile_bank";
    public static final String BAY_PARTICLE = "$particle_bay";
    public static final String BAY_MESON = "$meson_gun";
    public static final String BAY_FUSION = "$fusion_gun";
    public static final String WEAPON_MISSILE = "$missile_rack";
    public static final String WEAPON_PULSE = "$pulse_laser";
    public static final String WEAPON_SAND = "$sandcaster_rack";
    public static final String WEAPON_PARTICLE = "$particle_beam";
    public static final String WEAPON_BEAM = "$beam_laser";
    public static final String MESON_SCREEN = "$meson_screen";
    public static final String NUCLEAR_DAMPER = "$nuclear_damper";

    public static final String HANGER_ATV = "$hanger_atv";
    public static final String HANGER_RAFT = "$hanger_raft";
    public static final String HANGER_CUTTER = "$hanger_cutter";
    public static final String HANGER_ESCAPE = "$hanger_escape";
    public static final String HANGER_LIFEBOAT = "$hanger_lifeboat";
    public static final String HANGER_DRONE_MINING = "$hanger_drone_mining";
    public static final String HANGER_PINNACE = "$hanger_pinnace";
    public static final String HANGER_DRONE_PROBE = "$hanger_drone_probe";
    public static final String HANGER_DRONE_REPAIR = "$hanger_drone_repair";
    public static final String HANGER_BOAT = "$hanger_boat";
    public static final String HANGER_SHUTTLE = "$hanger_shuttle";
    public static final String HANGER_FIGHTER = "$hanger_fighter";

    // accomodation (singleton count)
    public static final String BARRACKS = "$barracks";
    public static final String EMERGENCY_LOWBERTH = "$emergency_lowberth";
    public static final String LOWBERTH = "$lowberth";
    public static final String STATEROOM = "$stateroom";

    // booleans
    public static final String FUEL_SCOOPS = "$fuel_scoops";

    public static final Set<String> COMPUTER_IDS = new HashSet<>();
    public static final List<ShipComponentBean> COMPUTERS = new ArrayList<>();
    static
    {
        for (ShipComponentBean comp : ShipComponentLogic.getComponentsByType(ShipComponentBean.COMPUTER))
            if (!comp.getID().endsWith("_fib") && !comp.getID().endsWith("_bis"))
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

    public static ShipComponentBean getElectronics()
    {
        return getSingletonType(ShipComponentBean.ELECTRONICS);
    }

    public static void setElectronics(ShipComponentBean item)
    {
        setSingletonType(item, ShipComponentBean.ELECTRONICS);
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

}
