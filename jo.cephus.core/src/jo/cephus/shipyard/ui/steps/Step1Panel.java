package jo.cephus.shipyard.ui.steps;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.ArmorChooser;
import jo.cephus.shipyard.ui.ctrl.ConfigChooser;
import jo.cephus.shipyard.ui.ctrl.HullChooser;

@SuppressWarnings("serial")
public class Step1Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private HullChooser mHull;
    private ConfigChooser mConfig;
    private ArmorChooser mArmor;
    
    public Step1Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    private void initInstantiate()
    {
        mHull = new HullChooser();
        mConfig = new ConfigChooser();
        mArmor = new ArmorChooser();
    }

    private void initLayout()
    {
        setLayout(new GridLayout(4, 1));
        add(new JLabel("Step 1: Hull"));
        add(mHull);
        add(mConfig);
        add(mArmor);
    }

    private void initLink()
    {
        // UI to Data
        mHull.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doActionHull();
            }
        });
        mConfig.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doActionConfig();
            }
        });
        mArmor.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doActionArmor();
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
    
    private void doActionHull()
    {
        ShipComponentBean hull = mHull.getComponent();
        if (hull != ShipEditLogic.getHull())
            ShipEditLogic.setHull(hull);
    }
    
    private void doActionConfig()
    {
        ShipComponentBean config = mConfig.getComponent();
        if (config != ShipEditLogic.getConfig())
            ShipEditLogic.setConfig(config);
    }
    
    private void doActionArmor()
    {
        ShipComponentBean armor = mArmor.getComponent();
        if (armor != ShipEditLogic.getArmor())
            ShipEditLogic.setArmor(armor);
    }
    
    private void doNewComponents()
    {
        if (mRuntime.getShip() == null)
        {
            mHull.setComponent(null);
            mConfig.setComponent(null);
            mArmor.setComponent(null);
        }
        else
        {
            ShipComponentBean hull = ShipEditLogic.getHull();
            if (hull != mHull.getComponent())
                mHull.setComponent(hull);
            ShipComponentBean config = ShipEditLogic.getConfig();
            if (config != mConfig.getComponent())
                mConfig.setComponent(config);
            ShipComponentBean armor = ShipEditLogic.getArmor();
            if (armor != mArmor.getComponent())
                mArmor.setComponent(armor);
        }
    }
}
