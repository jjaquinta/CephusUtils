package jo.clight.shipyard.ui.steps;

import java.util.List;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.logic.ShipComponentLogic;
import jo.clight.shipyard.ui.ctrl.SpinnerPanel;

@SuppressWarnings("serial")
public class Step15Panel extends SpinnerPanel
{
    private String[] mComponents = null;
    
    public Step15Panel()
    {
    }

    @Override
    protected String getTitle()
    {
        return "Hangers";
    }
    
    @Override
    protected String[] getShipComponents()
    {
        if (mComponents == null)
        {
            List<ShipComponentBean> hangers = ShipComponentLogic.getComponentsByType(ShipComponentBean.HANGER);
            mComponents = new String[hangers.size()];
            for (int i = 0; i < hangers.size(); i++)
                mComponents[i] = "$"+hangers.get(i).getID();
        }
        return mComponents;
    }
}
