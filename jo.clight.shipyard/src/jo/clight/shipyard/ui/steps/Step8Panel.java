package jo.clight.shipyard.ui.steps;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.logic.FormatUtils;
import jo.clight.core.logic.ShipDesignLogic;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;
import jo.clight.shipyard.logic.ShipEditLogic;
import jo.clight.shipyard.ui.ctrl.DesignStatPanel;
import jo.clight.shipyard.ui.ctrl.ElectronicsChooser;

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
        setBorder(new TitledBorder("Electronics"));
        setLayout(new GridLayout(2, 1));
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
