package jo.cephus.shipyard.ui.steps;

import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.SpinnerPanel;

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
        return new String[] { ShipEditLogic.WEAPON_MISSILE, ShipEditLogic.WEAPON_PULSE, 
                ShipEditLogic.WEAPON_SAND, ShipEditLogic.WEAPON_PARTICLE, ShipEditLogic.WEAPON_BEAM};
    }
}
