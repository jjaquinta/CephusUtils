package jo.clight.shipyard.ui.steps;

import jo.clight.shipyard.logic.ShipEditLogic;
import jo.clight.shipyard.ui.ctrl.SpinnerPanel;

@SuppressWarnings("serial")
public class Step10Panel extends SpinnerPanel
{
    public Step10Panel()
    {
    }

    @Override
    protected String getTitle()
    {
        return "Extras";
    }
    
    @Override
    protected String[] getShipComponents()
    {
        return new String[] { ShipEditLogic.ARMORY, ShipEditLogic.BRIEFING_ROOM, ShipEditLogic.DETENTION_CELL, 
                ShipEditLogic.LAB, ShipEditLogic.LIBRARY, ShipEditLogic.LUXURIES, ShipEditLogic.VAULT};
    }
}
