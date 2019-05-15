package jo.clight.shipyard.ui.steps;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.shipyard.ui.ctrl.ComputerChooser;
import jo.clight.shipyard.ui.ctrl.ShipComponentCheck;

@SuppressWarnings("serial")
public class Step7Panel extends JComponent
{
    private ComputerChooser    mComputer;
    private ShipComponentCheck mBIS;

    public Step7Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mComputer = new ComputerChooser();
        mBIS = new ShipComponentCheck("BIS", ShipComponentBean.COMPUTER_BIS);
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Computer"));
        setLayout(new GridLayout(2, 1));
        add(mComputer);
        add(mBIS);
    }

    private void initLink()
    {
        // UI to Data
        // data to UI
    }
}
