package jo.cephus.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.DesignStatPanel;
import jo.cephus.shipyard.ui.ctrl.ReportStatPanel;
import jo.util.utils.obj.IntegerUtils;

@SuppressWarnings("serial")
public class Step9Panel extends JComponent
{
    private final static RuntimeBean mRuntime = RuntimeLogic.getInstance();

    private JSpinner                 mStaterooms;
    private JSpinner                 mBerths;
    private JSpinner                 mBarracks;
    private JSpinner                 mEmergency;
    private ReportStatPanel          mCrewStats;
    private DesignStatPanel          mCostStats;

    public Step9Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    private void initInstantiate()
    {
        mStaterooms = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        mBerths = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        mBarracks = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        mEmergency = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        mCostStats = new DesignStatPanel("Cost:", new DesignStatPanel.IDesignStat() {            
            @Override
            public String getStat(ShipDesignBean ship)
            {
                double cost = ShipDesignLogic.priceAllInstances(ship, ShipComponentBean.STATEROOM);
                return FormatUtils.sCurrency(cost*1000000);
            }
        });
        mCrewStats = new ReportStatPanel("Crew:", new ReportStatPanel.IReportStat() {            
            @Override
            public String getStat(ShipReportBean report)
            {
                return String.valueOf(report.getCrewTotal());
            }
        });
    }

    private void initLayout()
    {
        JPanel client = new JPanel();
        client.setLayout(new GridLayout(4, 2));
        client.add(new JLabel("Staterooms:"));
        client.add(mStaterooms);
        client.add(new JLabel("Low Berths:"));
        client.add(mBerths);
        client.add(new JLabel("Barracks:"));
        client.add(mBarracks);
        client.add(new JLabel("Emergency Berths:"));
        client.add(mEmergency);
        
        JPanel footer = new JPanel();
        footer.setLayout(new GridLayout(1, 2));
        footer.add(mCrewStats);
        footer.add(mCostStats);

        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel("Step 9: Accomodation");
        Font oldFont = jLabel.getFont();
        jLabel.setFont(
                new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2));
        add("North", jLabel);
        add("Center", client);
        add("South", footer);
    }

    private void initLink()
    {
        // UI to Data
        mStaterooms.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionStaterooms();
            }
        });
        mBerths.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionBerths();
            }
        });
        mBarracks.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionBarracks();
            }
        });
        mEmergency.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionEmergency();
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

    private void doActionStaterooms()
    {
        int count = IntegerUtils.parseInt(mStaterooms.getValue());
        if (count != ShipEditLogic.getStaterooms())
            ShipEditLogic.setStaterooms(count);
    }

    private void doActionBerths()
    {
        int count = IntegerUtils.parseInt(mBerths.getValue());
        if (count != ShipEditLogic.getBerths())
            ShipEditLogic.setBerths(count);
    }

    private void doActionBarracks()
    {
        int count = IntegerUtils.parseInt(mBarracks.getValue());
        if (count != ShipEditLogic.getBarracks())
            ShipEditLogic.setBarracks(count);
    }

    private void doActionEmergency()
    {
        int count = IntegerUtils.parseInt(mEmergency.getValue());
        if (count != ShipEditLogic.getEmergencyBerths())
            ShipEditLogic.setEmergencyBerths(count);
    }

    private void doNewComponents()
    {
        if (mRuntime.getReport() == null)
        {
            mStaterooms.setModel(new SpinnerNumberModel(0, 0, 0, 0));
        }
        else
        {
            ShipReportBean report = RuntimeLogic.getInstance().getReport();
            int min = report.getCrewTotal();
            int val = Math.max(min, ShipEditLogic.getStaterooms());
            mStaterooms
                    .setModel(new SpinnerNumberModel(val, min, null, 1));
        }
    }
}
