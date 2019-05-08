package jo.cephus.shipyard.ui.steps;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;

import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.ComputerChooser;
import jo.cephus.shipyard.ui.ctrl.ShipComponentCheck;

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
        setLayout(new GridLayout(4, 1));
        JLabel jLabel = new JLabel("Computer");
        Font oldFont = jLabel.getFont();
        jLabel.setFont(
                new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2));
        add(jLabel);
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
