package jo.cephus.shipyard.ui.ctrl;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ParameterizedLogic;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.RuntimeLogic;

@SuppressWarnings("serial")
public class DesignStatPanel extends JComponent implements PropertyChangeListener
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private IDesignStat mGetter;
    private String      mLabel;
    
    private JTextField  mClient;

    public DesignStatPanel(String label, IDesignStat getter)
    {
        mLabel = label;
        mGetter = getter;
        initInstantiate();
        initLayout();
        initLink();
        doNewReport();
    }

    private void initInstantiate()
    {
        mClient = new JTextField();
        mClient.setEditable(false);
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("West", new JLabel(mLabel));
        add("Center", mClient);
    }

    private void initLink()
    {
        // data to UI        
        mRuntime.addUIPropertyChangeListener("ship", this);
        mRuntime.addUIPropertyChangeListener("ship.components", this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        doNewReport();
    }

    private void doNewReport()
    {
        ShipDesignBean ship = mRuntime.getShip();
        if (ship == null)
            mClient.setText("");
        else
        {
            try
            {
                ParameterizedLogic.addContext(ship);
                String stat = mGetter.getStat(ship);
                mClient.setText(stat);
            }
            finally
            {
                ParameterizedLogic.removeContext(ship);
            }
        }
    }
    
    public interface IDesignStat
    {
        public String getStat(ShipDesignBean report);
    }
}
