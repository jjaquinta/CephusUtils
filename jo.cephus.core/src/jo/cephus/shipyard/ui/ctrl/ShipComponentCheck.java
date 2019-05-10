package jo.cephus.shipyard.ui.ctrl;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ShipComponentLogic;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.cephus.core.logic.text.TextLogic;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;

@SuppressWarnings("serial")
public class ShipComponentCheck extends JComponent
{
    private final static RuntimeBean mRuntime = RuntimeLogic.getInstance();
    
    private String                  mLabel;
    private IAccessor               mAccessor;

    private JCheckBox                mChecker;
    private DesignStatPanel          mCostStats;

    public ShipComponentCheck(String label, IAccessor accessor)
    {
        init(label, accessor);
    }

    public void init(String label, IAccessor accessor)
    {
        mLabel = label;
        mAccessor = accessor;
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    public ShipComponentCheck(String label, String id)
    {
        ShipComponentBean comp = ShipComponentLogic.getComponent(id);
        if ((label == null) && (comp != null))
            label = TextLogic.getString(comp.getName());
        init(label, new ShipComponentCheck.SingletonAccessor(id));
    }

    private void initInstantiate()
    {
        mChecker = new JCheckBox(mLabel);
        mCostStats = new DesignStatPanel("Cost:", new DesignStatPanel.IDesignStat() {            
            @Override
            public String getStat(ShipDesignBean ship)
            {
                double cost = mAccessor.getCost(ship);
                return FormatUtils.sCurrency(cost*1000000);
            }
        });
    }

    private void initLayout()
    {
        setLayout(new GridLayout(1, 2));
        add(mChecker);
        add(mCostStats);
    }

    private void initLink()
    {
        // UI to Data
        mChecker.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionChecker();
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

    private void doActionChecker()
    {
        boolean val = mChecker.isSelected();
        if (val != mAccessor.getValue())
            mAccessor.setValue(val);
    }

    private void doNewComponents()
    {
        if (mRuntime.getReport() == null)
        {
            mChecker.setSelected(false);
        }
        else
        {
            boolean val = mAccessor.getValue();
            if (val != mChecker.isSelected())
                mChecker.setSelected(val);
        }
    }
    
    public interface IAccessor
    {
        public boolean getValue();
        public void setValue(boolean val);
        public double getCost(ShipDesignBean ship);
    }
    
    public class SingletonAccessor implements IAccessor
    {
        private String mID;
        public SingletonAccessor(String id)
        {
            mID = id;
        }
        @Override
        public boolean getValue()
        {
            return ShipEditLogic.getSingletonCount(mID) > 0;
        }
        @Override
        public void setValue(boolean val)
        {
            ShipEditLogic.setSingletonCount(mID, val ? 1 : 0);
        }
        @Override
        public double getCost(ShipDesignBean ship)
        {
            return ShipDesignLogic.priceAllInstances(ship, mID);
        }
    }
}
