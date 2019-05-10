package jo.cephus.shipyard.ui.steps;

import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.SpinnerPanel;

@SuppressWarnings("serial")
public class Step12Panel extends SpinnerPanel
{
    public Step12Panel()
    {
    }

    @Override
    protected String getTitle()
    {
        return "Bays";
    }
    
    @Override
    protected String[] getShipComponents()
    {
        return new String[] { ShipEditLogic.BAY_MISSILE, ShipEditLogic.BAY_PARTICLE, 
                ShipEditLogic.BAY_MESON, ShipEditLogic.BAY_FUSION};
    }
}
