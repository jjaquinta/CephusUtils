package jo.cephus.shipyard.ui.steps;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.ComputerChooser;
import jo.cephus.shipyard.ui.ctrl.DesignStatPanel;

@SuppressWarnings("serial")
public class Step7Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private ComputerChooser mComputer;
    private JCheckBox       mFIB;
    private JCheckBox       mBIS;
    private DesignStatPanel mCost;
    
    public Step7Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    private void initInstantiate()
    {
        mComputer = new ComputerChooser();
        mFIB = new JCheckBox("FIB");
        mBIS = new JCheckBox("BIS");
        mCost = new DesignStatPanel("Cost:", new DesignStatPanel.IDesignStat() {
            @Override
            public String getStat(ShipDesignBean ship)
            {
                return getComputerCost(ship);
            }
        });
    }

    private void initLayout()
    {
        setLayout(new GridLayout(5, 1));
        JLabel jLabel = new JLabel("Step 7: Computer");
        Font oldFont = jLabel.getFont();
        jLabel.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2));
        add(jLabel);
        add(mComputer);
        add(mBIS);
        add(mFIB);
        add(mCost);
    }

    private void initLink()
    {
        // UI to Data
        mComputer.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doActionComputer();
            }
        });
        mFIB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doActionFIB();
            }
        });
        mBIS.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doActionBIS();
            }
        });
        // data to UI        
        mRuntime.addPropertyChangeListener("ship.components",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewComponents();
                    }
                });
    }
    
    private void doActionComputer()
    {
        ShipComponentBean comp = mComputer.getComponent();
        if (comp != ShipEditLogic.getComputer())
            ShipEditLogic.setComputer(comp);
    }
    
    private void doActionFIB()
    {
        boolean fib = mFIB.isSelected();
        if (fib != ShipEditLogic.getComputerFib())
            ShipEditLogic.setComputerFib(fib);
    }
    
    private void doActionBIS()
    {
        boolean bis = mBIS.isSelected();
        if (bis != ShipEditLogic.getComputerBis())
            ShipEditLogic.setComputerBis(bis);
    }
    
    private void doNewComponents()
    {
        if (mRuntime.getShip() == null)
        {
            mComputer.setComponent(null);
        }
        else
        {
            ShipComponentBean comp = ShipEditLogic.getComputer();
            if (comp != mComputer.getComponent())
                mComputer.setComponent(comp);
            boolean fib = ShipEditLogic.getComputerFib();
            if (fib != mFIB.isSelected())
                mFIB.setSelected(fib);
            boolean bis = ShipEditLogic.getComputerBis();
            if (bis != mBIS.isSelected())
                mBIS.setSelected(bis);
        }
    }
    
    private String getComputerCost(ShipDesignBean ship)
    {
        if (ship == null)
            return "";
        double cost = ShipDesignLogic.priceAllInstances(ship, ShipComponentBean.COMPUTER);
        return FormatUtils.sCurrency(cost*1000000);
    }
}
