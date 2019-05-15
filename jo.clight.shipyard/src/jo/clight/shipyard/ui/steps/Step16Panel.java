package jo.clight.shipyard.ui.steps;

import java.util.List;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.logic.ShipComponentLogic;
import jo.clight.shipyard.ui.ctrl.SpinnerPanel;

@SuppressWarnings("serial")
public class Step16Panel extends SpinnerPanel
{
    private String[] mComponents = null;
    
    public Step16Panel()
    {
    }

    @Override
    protected String getTitle()
    {
        return "Crew";
    }
    
    @Override
    protected String[] getShipComponents()
    {
        if (mComponents == null)
        {
            List<ShipComponentBean> hangers = ShipComponentLogic.getComponentsByType(ShipComponentBean.CREW);
            mComponents = new String[hangers.size()];
            for (int i = 0; i < hangers.size(); i++)
                mComponents[i] = "$"+hangers.get(i).getID();
        }
        return mComponents;
    }
}
