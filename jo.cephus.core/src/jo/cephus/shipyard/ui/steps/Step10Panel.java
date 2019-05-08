package jo.cephus.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ShipDesignLogic;
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
        mArmories = new ShipComponentSpinner("Armories", new ShipComponentSpinner.IAccessor(){
            @Override
            public int getValue()
            {
                return ShipEditLogic.getArmory();
            }
            @Override
            public void setValue(int val)
            {
                ShipEditLogic.setArmory(val);
            }
            @Override
            public double getCost(ShipDesignBean ship)
            {
                return ShipDesignLogic.priceAllInstances(ship, "$armory");
            }
        });
        mBriefingRooms = new ShipComponentSpinner("Briefing Rooms", new ShipComponentSpinner.IAccessor(){
            @Override
            public int getValue()
            {
                return ShipEditLogic.getBriefingRoom();
            }
            @Override
            public void setValue(int val)
            {
                ShipEditLogic.setBriefingRoom(val);
            }
            @Override
            public double getCost(ShipDesignBean ship)
            {
                return ShipDesignLogic.priceAllInstances(ship, "$briefing_room");
            }
        });
        mDetentionCells= new ShipComponentSpinner("Detention Cells", new ShipComponentSpinner.IAccessor(){
            @Override
            public int getValue()
            {
                return ShipEditLogic.getDetentionCell();
            }
            @Override
            public void setValue(int val)
            {
                ShipEditLogic.setDetentionCell(val);
            }
            @Override
            public double getCost(ShipDesignBean ship)
            {
                return ShipDesignLogic.priceAllInstances(ship, "$detention_cell");
            }
        });
        mLabs = new ShipComponentSpinner("Labs", new ShipComponentSpinner.IAccessor(){
            @Override
            public int getValue()
            {
                return ShipEditLogic.getLab();
            }
            @Override
            public void setValue(int val)
            {
                ShipEditLogic.setLab(val);
            }
            @Override
            public double getCost(ShipDesignBean ship)
            {
                return ShipDesignLogic.priceAllInstances(ship, "$lab");
            }
        });
        mLibraries = new ShipComponentSpinner("Libraries", new ShipComponentSpinner.IAccessor(){
            @Override
            public int getValue()
            {
                return ShipEditLogic.getLibrary();
            }
            @Override
            public void setValue(int val)
            {
                ShipEditLogic.setLibrary(val);
            }
            @Override
            public double getCost(ShipDesignBean ship)
            {
                return ShipDesignLogic.priceAllInstances(ship, "$library");
            }
        });
        mLuxuries = new ShipComponentSpinner("Luxuries", new ShipComponentSpinner.IAccessor(){
            @Override
            public int getValue()
            {
                return ShipEditLogic.getLuxuries();
            }
            @Override
            public void setValue(int val)
            {
                ShipEditLogic.setLuxuries(val);
            }
            @Override
            public double getCost(ShipDesignBean ship)
            {
                return ShipDesignLogic.priceAllInstances(ship, "luxuries");
            }
        });
        mValut = new ShipComponentSpinner("Valuts", new ShipComponentSpinner.IAccessor(){
            @Override
            public int getValue()
            {
                return ShipEditLogic.getVault();
            }
            @Override
            public void setValue(int val)
            {
                ShipEditLogic.setVault(val);
            }
            @Override
            public double getCost(ShipDesignBean ship)
            {
                return ShipDesignLogic.priceAllInstances(ship, "vault");
            }
        });
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
        JLabel jLabel = new JLabel("Step 10: Additional Features");
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
