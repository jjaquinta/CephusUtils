package jo.cephus.shipyard.ui.steps;

import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.SpinnerPanel;

@SuppressWarnings("serial")
public class Step11Panel extends SpinnerPanel
{
    public Step11Panel()
    {
    }

    @Override
    protected String getTitle()
    {
        return "Turrets";
    }
    
    @Override
    protected String[] getShipComponents()
    {
        return new String[] { ShipEditLogic.TURRET_SINGLE, ShipEditLogic.TURRET_DOUBLE, ShipEditLogic.TURRET_TRIPLE, 
                ShipEditLogic.TURRET_POPUP_SINGLE, ShipEditLogic.TURRET_POPUP_DOUBLE, ShipEditLogic.TURRET_POPUP_TRIPLE, 
                ShipEditLogic.TURRET_FIXED_SINGLE, ShipEditLogic.TURRET_FIXED_DOUBLE, ShipEditLogic.TURRET_FIXED_TRIPLE};
    }
}
