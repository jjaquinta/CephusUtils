package jo.clight.shipyard.ui.steps;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import jo.clight.shipyard.logic.ShipEditLogic;
import jo.clight.shipyard.ui.ctrl.ComputerChooser;
import jo.clight.shipyard.ui.ctrl.ShipComponentCheck;

@SuppressWarnings("serial")
public class Step7Panel extends JComponent
{
    private ComputerChooser    mComputer;
    private ShipComponentCheck mFIB;
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
        mFIB = new ShipComponentCheck("FIB", ShipEditLogic.COMPUTER_FIB);
        mBIS = new ShipComponentCheck("BIS", ShipEditLogic.COMPUTER_BIS);
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Computer"));
        setLayout(new GridLayout(3, 1));
        add(mComputer);
        add(mBIS);
        add(mFIB);
    }

    private void initLink()
    {
        // UI to Data
        // data to UI
    }
}
