package jo.cephus.shipyard.ui.steps;

import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.DesignStatPanel;
import jo.cephus.shipyard.ui.ctrl.ElectronicsChooser;

@SuppressWarnings("serial")
public class Step8Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private ElectronicsChooser mElectronics;
    private DesignStatPanel mCost;
    
    public Step8Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    private void initInstantiate()
    {
        mElectronics = new ElectronicsChooser();
        mCost = new DesignStatPanel("Cost:", new DesignStatPanel.IDesignStat() {
            @Override
            public String getStat(ShipDesignBean ship)
            {
                return getElectronicsCost(ship);
            }
        });
    }

    private void initLayout()
    {
        setLayout(new GridLayout(3, 1));
        JLabel jLabel = new JLabel("Electronics");
        Font oldFont = jLabel.getFont();
        jLabel.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2));
        add(jLabel);
        add(mElectronics);
        add(mCost);
    }

    private void initLink()
    {
        // UI to Data
        mElectronics.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doActionElectronics();
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
    
    private void doActionElectronics()
    {
        ShipComponentBean comp = mElectronics.getComponent();
        if (comp != ShipEditLogic.getElectronics())
            ShipEditLogic.setElectronics(comp);
    }
    
    private void doNewComponents()
    {
        if (mRuntime.getShip() == null)
        {
            mElectronics.setComponent(null);
        }
        else
        {
            ShipComponentBean comp = ShipEditLogic.getElectronics();
            if (comp != mElectronics.getComponent())
                mElectronics.setComponent(comp);
        }
    }
    
    private String getElectronicsCost(ShipDesignBean ship)
    {
        if (ship == null)
            return "";
        double cost = ShipDesignLogic.priceAllInstances(ship, ShipComponentBean.ELECTRONICS);
        return FormatUtils.sCurrency(cost*1000000);
    }
}
