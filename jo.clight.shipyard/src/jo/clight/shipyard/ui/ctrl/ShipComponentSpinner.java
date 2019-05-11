package jo.clight.shipyard.ui.ctrl;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.logic.FormatUtils;
import jo.clight.core.logic.ParameterizedLogic;
import jo.clight.core.logic.ShipComponentLogic;
import jo.clight.core.logic.ShipDesignLogic;
import jo.clight.core.logic.text.TextLogic;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;
import jo.clight.shipyard.logic.ShipEditLogic;
import jo.util.utils.obj.IntegerUtils;

@SuppressWarnings("serial")
public class ShipComponentSpinner extends JComponent
{
    private final static RuntimeBean mRuntime = RuntimeLogic.getInstance();
    
    private String                  mLabel;
    private IAccessor               mAccessor;
    private ShipComponentBean       mComponent;

    private JSpinner                 mSpinner;
    private DesignStatPanel          mCostStats;

    public ShipComponentSpinner(String label, IAccessor accessor)
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

    public ShipComponentSpinner(String label, String id)
    {
        mComponent = ShipComponentLogic.getComponent(id);
        if ((label == null) && (mComponent != null))
            label = TextLogic.getString(mComponent.getName());
        init(label, new ShipComponentSpinner.SingletonAccessor(id));
    }

    private void initInstantiate()
    {
        mSpinner = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
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
        setLayout(new GridLayout(1, 3));
        add(new JLabel(mLabel));
        add(mSpinner);
        add(mCostStats);
    }

    private void initLink()
    {
        // UI to Data
        mSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionSpinner();
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

    private void doActionSpinner()
    {
        int count = IntegerUtils.parseInt(mSpinner.getValue());
        if (count != mAccessor.getValue())
            mAccessor.setValue(count);
    }

    private void doNewComponents()
    {
        if (mRuntime.getReport() == null)
        {
            mSpinner.setModel(new SpinnerNumberModel(0, 0, 0, 0));
        }
        else
        {
            int min = getMin();
            Comparable<Integer> max = getMax();
            int val = Math.max(min, mAccessor.getValue());
            if ((max != null) && (max.compareTo(val) <= 0))
                max = val;
            //System.out.println("Spinner "+mLabel+" "+min+" < "+val+" < "+max);
            mSpinner.setModel(new SpinnerNumberModel(val, min, max, 1));
        }
    }
    
    protected int getMin()
    {
        return 0;
    }
    
    protected Comparable<Integer> getMax()
    {
        if (mComponent != null)
        {
            int max;
            ShipComponentInstanceBean inst = ShipDesignLogic.getFirstInstance(mRuntime.getShip(), "$"+mComponent.getID());
            try
            {
                ParameterizedLogic.addContext(mRuntime.getShip());
                if (inst != null)
                    ParameterizedLogic.addContext(inst);
                max = mComponent.getMaxCopies();
            }
            finally
            {
                if (inst != null)
                    ParameterizedLogic.removeContext(inst);
                ParameterizedLogic.removeContext(mRuntime.getShip());
            }
            if (max < 0)
                return null;
            return max;
        }
        return null;
    }
    
    public interface IAccessor
    {
        public int getValue();
        public void setValue(int val);
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
        public int getValue()
        {
            int count = ShipEditLogic.getSingletonCount(mID);
            return count;
        }
        @Override
        public void setValue(int val)
        {
            ShipEditLogic.setSingletonCount(mID, val);
        }
        @Override
        public double getCost(ShipDesignBean ship)
        {
            return ShipDesignLogic.priceAllInstances(ship, mID);
        }
    }
}
