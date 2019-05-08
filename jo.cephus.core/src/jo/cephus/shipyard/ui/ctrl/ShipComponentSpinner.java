package jo.cephus.shipyard.ui.ctrl;

import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.util.utils.obj.IntegerUtils;

@SuppressWarnings("serial")
public class ShipComponentSpinner extends JComponent
{
    private final static RuntimeBean mRuntime = RuntimeLogic.getInstance();
    
    private String                  mLabel;
    private IAccessor               mAccessor;

    private JSpinner                 mSpinner;
    private DesignStatPanel          mCostStats;

    public ShipComponentSpinner(String label, IAccessor accessor)
    {
        mLabel = label;
        mAccessor = accessor;
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
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
            int val = Math.max(min, mAccessor.getValue());
            mSpinner.setModel(new SpinnerNumberModel(val, min, null, 1));
        }
    }
    
    protected int getMin()
    {
        return 0;
    }
    
    public interface IAccessor
    {
        public int getValue();
        public void setValue(int val);
        public double getCost(ShipDesignBean ship);
    }
}
