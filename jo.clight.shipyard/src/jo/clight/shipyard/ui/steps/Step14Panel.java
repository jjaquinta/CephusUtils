package jo.clight.shipyard.ui.steps;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.shipyard.ui.ctrl.ShipComponentCheck;

@SuppressWarnings("serial")
public class Step14Panel extends JComponent
{
    private ShipComponentCheck    mMesonScreen;
    private ShipComponentCheck    mNuclearDamper;

    public Step14Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mMesonScreen = new ShipComponentCheck(null, ShipComponentBean.SCREENS_MESON_SCREEN);
        mNuclearDamper = new ShipComponentCheck(null, ShipComponentBean.SCREENS_NUCLEAR_DAMPER);
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Screens"));
        setLayout(new GridLayout(2, 1));
        add(mMesonScreen);
        add(mNuclearDamper);
    }

    private void initLink()
    {
        // UI to Data
        // data to UI
    }
}
