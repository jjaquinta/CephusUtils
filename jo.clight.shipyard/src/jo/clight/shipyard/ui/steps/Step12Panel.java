package jo.clight.shipyard.ui.steps;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.shipyard.ui.ctrl.SpinnerPanel;

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
        return new String[] { ShipComponentBean.BAY_MISSILE_BANK, ShipComponentBean.BAY_PARTICLE_BAY, 
                ShipComponentBean.BAY_MESON_GUN, ShipComponentBean.BAY_FUSION_GUN, ShipComponentBean.BAY_GRAVITIC_LANCE};
    }
}
