package jo.clight.shipyard.ui.steps;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.logic.FormatUtils;
import jo.clight.core.logic.ShipDesignLogic;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;
import jo.clight.shipyard.logic.ShipEditLogic;
import jo.clight.shipyard.ui.ctrl.ArmorChooser;
import jo.clight.shipyard.ui.ctrl.ConfigChooser;
import jo.clight.shipyard.ui.ctrl.DesignStatPanel;
import jo.clight.shipyard.ui.ctrl.HullChooser;

@SuppressWarnings("serial")
public class Step1Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private HullChooser mHull;
    private ConfigChooser mConfig;
    private ArmorChooser mArmor;
    private JSpinner     mArmorThickness;
    private DesignStatPanel mCost;
    
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
        mArmorThickness = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
        mCost = new DesignStatPanel("Cost:", new DesignStatPanel.IDesignStat() {
            @Override
            public String getStat(ShipDesignBean ship)
            {
                return getHullCost(ship);
            }
        });
    }

    private void initLayout()
    {
        mArmor.add("East", mArmorThickness);
        setBorder(new TitledBorder("Hull"));
        setLayout(new GridLayout(4, 1));
        add(mHull);
        add(mConfig);
        add(mArmor);
        add(mCost);
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
        mArmorThickness.addChangeListener(new ChangeListener() {            
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionArmorThickness();
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
        if (!mArmor.isSelected(ShipEditLogic.getArmor()))
            ShipEditLogic.setArmor(armor, ((Number)mArmorThickness.getValue()).intValue());
    }
    
    private void doActionArmorThickness()
    {
        int count  = ((Number)mArmorThickness.getValue()).intValue();
        ShipComponentInstanceBean armor = ShipEditLogic.getArmor();
        if ((armor == null) || (count != armor.getCount()))
        {
            ShipComponentBean a = mArmor.getComponent();
            if (a != null)
                ShipEditLogic.setArmor(a, count);
        }
    }
    
    private void doNewComponents()
    {
        if (mRuntime.getShip() == null)
        {
            mHull.setComponent(null);
            mConfig.setComponent(null);
            mArmor.setComponent((ShipComponentBean)null);
            mArmorThickness.setValue(0);
        }
        else
        {
            ShipComponentBean hull = ShipEditLogic.getHull();
            if (hull != mHull.getComponent())
                mHull.setComponent(hull);
            ShipComponentBean config = ShipEditLogic.getConfig();
            if (config != mConfig.getComponent())
                mConfig.setComponent(config);
            ShipComponentInstanceBean armor = ShipEditLogic.getArmor();
            if (!mArmor.isSelected(armor))
                mArmor.setComponent(armor);
            if (armor != null)
                if (!mArmorThickness.getValue().equals(armor.getCount()))
                    mArmorThickness.setValue(armor.getCount());                    
        }
    }
    
    private String getHullCost(ShipDesignBean ship)
    {
        if (ship == null)
            return "";
        double hullCost = ShipDesignLogic.priceAllInstances(ship, ShipComponentBean.HULL);
        double armorCost = ShipDesignLogic.priceAllInstances(ship, ShipComponentBean.ARMOR);
        double configCost = ShipDesignLogic.priceAllInstances(ship, ShipComponentBean.CONFIG);
        double cost = (hullCost + armorCost + configCost)*1000000.0;
        return FormatUtils.sCurrency(hullCost)+"/"+FormatUtils.sCurrency(armorCost)+"/"+FormatUtils.sCurrency(configCost)+"="+FormatUtils.sCurrency(cost);
    }
}
