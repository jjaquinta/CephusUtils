package jo.clight.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import jo.clight.core.data.ShipDesignBean;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;
import jo.clight.shipyard.logic.ShipEditLogic;

@SuppressWarnings("serial")
public class Step0Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private JTextField  mName;
    private JTextArea   mDesc;
    private JCheckBox   mMilitary;
    private JCheckBox   mDoubleOccupancy;
    
    public Step0Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewName();
        doNewDesc();
        doNewRoles();
    }

    private void initInstantiate()
    {
        mName = new JTextField();
        mDesc = new JTextArea();
        mDesc.setLineWrap(true);
        mDesc.setWrapStyleWord(true);
        mMilitary = new JCheckBox("Military");
        mDoubleOccupancy = new JCheckBox("Double Occupancy");
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Information"));
        JPanel line1 = new JPanel();
        line1.setLayout(new BorderLayout());
        line1.add("West", new JLabel("Name:"));
        line1.add("Center", mName);
        
        JPanel line2 = new JPanel();
        line2.setLayout(new BorderLayout());
        line2.add("West", new JLabel("Purpose:"));
        line2.add("Center", mDesc);
        
        JPanel line3 = new JPanel();
        line3.setLayout(new GridLayout(1, 2));
        line3.add(mMilitary);
        line3.add(mDoubleOccupancy);
        
        JPanel client = new JPanel();
        client.setLayout(new BorderLayout());
        client.add("North", line1);
        client.add("Center", line2);
        client.add("South", line3);
        
        setLayout(new BorderLayout());
        add("Center", client);
    }

    private void initLink()
    {
        // UI to Data
        mName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e)
            {
                doActionName();
            }
        });
        mDesc.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e)
            {
                doActionDesc();
            }
        });
        mMilitary.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e)
            {
                doActionMilitary();
            }
        });
        mDoubleOccupancy.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e)
            {
                doActionDoubleOccupancy();
            }
        });
        // data to UI        
        mRuntime.addUIPropertyChangeListener("ship.shipName",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewName();
                    }
                });
        mRuntime.addUIPropertyChangeListener("ship.shipFunction",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewDesc();
                    }
                });
        mRuntime.addUIPropertyChangeListener("ship.shipRoles",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewRoles();
                    }
                });
    }
    
    private void doActionName()
    {
        ShipEditLogic.setShipName(mName.getText());
    }
    
    private void doActionDesc()
    {
        ShipEditLogic.setShipFunction(mDesc.getText());
    }
    
    private void doActionMilitary()
    {
        ShipEditLogic.setShipRole(ShipDesignBean.ROLE_MILITARY, mMilitary.isSelected());
    }
    
    private void doActionDoubleOccupancy()
    {
        ShipEditLogic.setShipRole(ShipDesignBean.ROLE_DOUBLEOCCUPANCY, mDoubleOccupancy.isSelected());
    }
    
    private void doNewName()
    {
        if (mRuntime.getShip() == null)
            mName.setText("");
        else
            mName.setText(mRuntime.getShip().getShipName());
    }
    
    private void doNewDesc()
    {
        if (mRuntime.getShip() == null)
            mDesc.setText("");
        else
            mDesc.setText(mRuntime.getShip().getShipFunction());
    }
    
    private void doNewRoles()
    {
        if (mRuntime.getShip() == null)
        {
            mMilitary.setSelected(false);
            mDoubleOccupancy.setSelected(false);
        }
        else
        {
            mMilitary.setSelected(ShipEditLogic.getShipRole(ShipDesignBean.ROLE_MILITARY));
            mDoubleOccupancy.setSelected(ShipEditLogic.getShipRole(ShipDesignBean.ROLE_DOUBLEOCCUPANCY));
        }
    }
}
