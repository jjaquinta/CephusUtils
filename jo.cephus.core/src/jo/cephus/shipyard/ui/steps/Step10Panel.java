package jo.cephus.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.ShipComponentSpinner;

@SuppressWarnings("serial")
public class Step10Panel extends JComponent
{
    private ShipComponentSpinner    mArmories;
    private ShipComponentSpinner    mBriefingRooms;
    private ShipComponentSpinner    mDetentionCells;
    private ShipComponentSpinner    mLabs;
    private ShipComponentSpinner    mLibraries;
    private ShipComponentSpinner    mLuxuries;
    private ShipComponentSpinner    mValut;

    public Step10Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mArmories = new ShipComponentSpinner("Armories", ShipEditLogic.ARMORY);
        mBriefingRooms = new ShipComponentSpinner("Briefing Rooms", ShipEditLogic.BRIEFING_ROOM);
        mDetentionCells= new ShipComponentSpinner("Detention Cells", ShipEditLogic.DETENTION_CELL);
        mLabs = new ShipComponentSpinner("Labs", ShipEditLogic.LAB);
        mLibraries = new ShipComponentSpinner("Libraries", ShipEditLogic.LIBRARY);
        mLuxuries = new ShipComponentSpinner("Luxuries", ShipEditLogic.LUXURIES);
        mValut = new ShipComponentSpinner("Valuts", ShipEditLogic.VAULT);
    }

    private void initLayout()
    {
        JPanel client = new JPanel();
        client.setLayout(new GridLayout(7, 1));
        client.add(mArmories);
        client.add(mBriefingRooms);
        client.add(mDetentionCells);
        client.add(mLabs);
        client.add(mLibraries);
        client.add(mLuxuries);
        client.add(mValut);

        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel("Additional Features");
        Font oldFont = jLabel.getFont();
        jLabel.setFont(
                new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2));
        add("North", jLabel);
        add("Center", client);
    }

    private void initLink()
    {
        // UI to Data
        // data to UI
    }
}
