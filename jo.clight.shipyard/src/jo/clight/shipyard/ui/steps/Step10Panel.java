package jo.clight.shipyard.ui.steps;

import jo.clight.core.data.ShipComponentBean;
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
        return new String[] { ShipComponentBean.ETC_ARMORY, ShipComponentBean.ETC_MEDLAB, ShipComponentBean.ETC_LAB };
    }
}
