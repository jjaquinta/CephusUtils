package jo.cephus.shipyard.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.RuntimeLogic;

@SuppressWarnings("serial")
public class ShipyardPanel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private DefaultComboBoxModel<ShipDesignBean> mSelectShipModel;
    private JComboBox<ShipDesignBean>            mSelectShip;
    private JButton                              mNewShip;
    private JButton                              mDelShip;
    
    private DesignPanel mDesign;
    private ReportPanel mReport;
    private ErrorPanel  mErrors;

    private JTextField                           mStatus;

    public ShipyardPanel()
    {
        initInstantiate();
        initLayout();
        initLink();

        doNewStatus();
        doNewShips();
        doNewShip();
    }

    private void initInstantiate()
    {
        mSelectShipModel = new DefaultComboBoxModel<>();
        mSelectShip = new JComboBox<>(mSelectShipModel);
        mNewShip = new JButton("New");
        mDelShip = new JButton("Del");
        
        mDesign = new DesignPanel();
        mReport = new ReportPanel();
        mErrors = new ErrorPanel();
        
        mStatus = new JTextField();
        mStatus.setEditable(false);
    }

    private void initLayout()
    {
        JPanel headerCmds = new JPanel();
        headerCmds.setLayout(new GridLayout(1, 2));
        headerCmds.add(mDelShip);
        headerCmds.add(mNewShip);
        
        JPanel header = new JPanel();
        header.setLayout(new BorderLayout());
        header.add("West", new JLabel("Ship:"));
        header.add("Center", mSelectShip);
        header.add("East", headerCmds);
        
        JPanel right = new JPanel();
        right.setLayout(new GridLayout(2, 1));
        right.add(mReport);
        right.add(mErrors);
        
        JSplitPane client = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mDesign, right);
        client.setDividerLocation(.5);
        
        setLayout(new BorderLayout());
        add("North", header);
        add("Center", client);
        add("South", mStatus);
    }

    private void initLink()
    {
        // UI to data
        mSelectShip.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent ev)
            {
                doActionSelected();
            }
        });
        mNewShip.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doActionNew();
            }
        });
        mDelShip.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doActionDel();
            }
        });
        // data to UI        
        mRuntime.addPropertyChangeListener("status",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewStatus();
                    }
                });
        mRuntime.addPropertyChangeListener("ships",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewShips();
                    }
                });
        mRuntime.addPropertyChangeListener("ship",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewShip();
                    }
                });
    }

    private void doNewStatus()
    {
        mStatus.setText(mRuntime.getStatus());
    }
    
    private void doNewShips()
    {
        mSelectShipModel.removeAllElements();
        for (ShipDesignBean ship : mRuntime.getShips())
            mSelectShipModel.addElement(ship);
    }
    
    private void doNewShip()
    {
        ShipDesignBean ship = mRuntime.getShip();
        if (mSelectShip.getSelectedItem() != ship)
            mSelectShip.setSelectedItem(ship);
    }
    
    private void doActionNew()
    {
        RuntimeLogic.newShip();
    }
    
    private void doActionDel()
    {
        ShipDesignBean ship = mRuntime.getShip();
        if (ship == null)
            return;
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete "+ship.getShipName()+"?", "Delete Ship", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            RuntimeLogic.deleteShip();
    }
    
    private void doActionSelected()
    {
        ShipDesignBean ship = (ShipDesignBean)mSelectShip.getSelectedItem();
        if (ship != mRuntime.getShip())
            RuntimeLogic.setShip(ship);
    }
}
