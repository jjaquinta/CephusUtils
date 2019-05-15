package jo.clight.shipyard.ui.steps;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.shipyard.ui.ctrl.SpinnerPanel;

@SuppressWarnings("serial")
public class Step13Panel extends SpinnerPanel
{
    public Step13Panel()
    {
    }

    @Override
    protected String getTitle()
    {
        return "Weapons";
    }
    
    @Override
    protected String[] getShipComponents()
    {
        return new String[] { ShipComponentBean.WEAPON_MISSILE_RACK, ShipComponentBean.WEAPON_PULSE_LASER, 
                ShipComponentBean.WEAPON_SANDCASTER_RACK, ShipComponentBean.WEAPON_PARTICLE_BEAM, ShipComponentBean.WEAPON_BEAM_LASER,
                ShipComponentBean.WEAPON_PLASMA_BEAM, ShipComponentBean.WEAPON_FUSION_BEAM};
    }
}
