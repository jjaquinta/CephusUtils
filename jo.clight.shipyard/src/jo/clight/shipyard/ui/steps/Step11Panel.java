package jo.clight.shipyard.ui.steps;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.shipyard.ui.ctrl.SpinnerPanel;

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
        return new String[] { ShipComponentBean.TURRET_SINGLE_TURRET, ShipComponentBean.TURRET_DOUBLE_TURRET, ShipComponentBean.TURRET_TRIPLE_TURRET, 
                ShipComponentBean.TURRET_SINGLE_FIXED_TURRET};
    }
}
