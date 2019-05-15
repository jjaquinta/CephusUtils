package jo.clight.core.logic;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipDesignBean;

public class ShipLibraryLogic
{
    public static ShipDesignBean constructDesignExample()
    {
        ShipDesignBean ship = new ShipDesignBean();
        ship.setShipName("Military Transport");
        ship.setShipFunction("an example of a frigate commonly found in operation within an interstellar polity");
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.HULL_300, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.CONFIG_STREAMLINED, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.ARMOR_CRYSTALIRON, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.JDRIVE_C, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.MDRIVE_C, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.PPLANT_C, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.FUEL_, 72));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.BRIDGE_20, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.COMPUTER_2, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.TURRET_TRIPLE_TURRET, 3));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.WEAPON_BEAM_LASER, 3));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.WEAPON_PULSE_LASER, 3));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.WEAPON_SANDCASTER_RACK, 3));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.STATEROOM_, 8));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.BERTH_LOWBERTH, 20));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.HANGER_ESCAPE, 16));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.ETC_FUEL_PROCESSOR, 4));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.CREW_PILOT, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.CREW_SENSOR, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.CREW_ENGINEER, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.CREW_MEDIC, 1));
        ship.getComponents().add(ShipComponentLogic.getInstance(ShipComponentBean.CREW_GUNNER, 3));
        return ship;
    }

}
