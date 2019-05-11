package jo.clight.shipyard.ui.ctrl;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public abstract class SpinnerPanel extends JComponent
{
    private List<ShipComponentSpinner> mControls = new ArrayList<>();

    public SpinnerPanel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    protected abstract String getTitle();
    protected abstract String[] getShipComponents();
    
    private void initInstantiate()
    {
        for (String id : getShipComponents())
        {
            ShipComponentSpinner ctrl = new ShipComponentSpinner(null, id);
            mControls.add(ctrl);
        }
    }

    private void initLayout()
    {
        setBorder(new TitledBorder(getTitle()));
        setLayout(new GridLayout(mControls.size(), 1));
        for (ShipComponentSpinner comp : mControls)
            add(comp);
    }

    private void initLink()
    {
        // UI to Data
        // data to UI
    }
}
