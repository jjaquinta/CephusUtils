package jo.clight.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.audio.tools.TableLayout;
import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.FormatUtils;
import jo.clight.core.logic.ShipDesignLogic;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;
import jo.clight.shipyard.logic.ShipEditLogic;
import jo.clight.shipyard.ui.ctrl.ReportStatPanel;
import jo.clight.shipyard.ui.ctrl.ShipComponentCheck;
import jo.clight.shipyard.ui.ctrl.ShipComponentSpinner;
import jo.util.utils.obj.IntegerUtils;

@SuppressWarnings("serial")
public class Step5Panel extends JComponent
{
    private final static RuntimeBean mRuntime = RuntimeLogic.getInstance();

    private JSpinner                 mWeeksOfPower;
    private JSpinner                 mNumberOfJumps;
    private ShipComponentCheck       mFuelScoops;
    private ShipComponentSpinner     mFuelProcessors;
    private ReportStatPanel          mFuelStats;

    public Step5Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    private void initInstantiate()
    {
        mWeeksOfPower = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        mNumberOfJumps = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        mFuelScoops = new ShipComponentCheck(null, ShipComponentBean.ETC_FUEL_SCOOPS);
        mFuelProcessors = new ShipComponentSpinner(null, ShipComponentBean.ETC_FUEL_PROCESSOR);
        mFuelStats = new ReportStatPanel("Tankage:", new ReportStatPanel.IReportStat() {            
            @Override
            public String getStat(ShipReportBean report)
            {
                return FormatUtils.sTons(report.getFuelTonnage());
            }
        });
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Fuel Requirements"));

        JPanel client = new JPanel();
        client.setLayout(new TableLayout());
        client.add("1,+", new JLabel("Weeks of Power:"));
        client.add("+,. fill=h", mWeeksOfPower);
        client.add("+,. fill=h", mFuelProcessors);
        client.add("1,+", new JLabel("Number of Jumps:"));
        client.add("+,. fill=h", mNumberOfJumps);
        client.add("+,. fill=h", mFuelScoops);

        setLayout(new BorderLayout());
        add("Center", client);
        add("South", mFuelStats);
    }

    private void initLink()
    {
        // UI to Data
        mWeeksOfPower.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionWeeksOfPower();
            }
        });
        mNumberOfJumps.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                doActionNumberOfJumps();
            }
        });
        // data to UI
        mRuntime.addPropertyChangeListener("report",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewComponents();
                    }
                });
    }

    private void doActionWeeksOfPower()
    {
        int weeks = IntegerUtils.parseInt(mWeeksOfPower.getValue());
        if (weeks != ShipEditLogic.getWeeksOfPower())
            ShipEditLogic.setWeeksOfPower(weeks);
    }

    private void doActionNumberOfJumps()
    {
        int jumps = IntegerUtils.parseInt(mNumberOfJumps.getValue());
        if (jumps != ShipEditLogic.getNumberOfJumps())
            ShipEditLogic.setNumberOfJumps(jumps);
    }

    private void doNewComponents()
    {
        if (mRuntime.getReport() == null)
        {
            mWeeksOfPower.setModel(new SpinnerNumberModel(0, 0, 0, 0));
        }
        else
        {
            ShipReportBean report = mRuntime.getReport();
            int minFuelTons = ShipDesignLogic.getMinFuel(report.getManeuverCode());
            int fuelPerWeek = ShipDesignLogic.getFuelPerWeek(report.getManeuverCode());
            int minWeeks = (fuelPerWeek == 0) ? 0 : minFuelTons/fuelPerWeek;
            int val = Math.max(minWeeks, report.getWeeksofPower());
            mWeeksOfPower
                    .setModel(new SpinnerNumberModel(val, minWeeks, null, 1));
            mNumberOfJumps.setModel(new SpinnerNumberModel(
                    report.getNumberOfJumps(), 0, null, 1));
            ShipComponentInstanceBean config = ShipDesignLogic.getFirstInstance(mRuntime.getShip(), ShipComponentBean.CONFIG);
            if ((config == null) || "configDistributed".equals(config.getComponentID()))
            {
                if (ShipEditLogic.getSingletonCount(ShipComponentBean.ETC_FUEL_SCOOPS) > 0)
                    ShipEditLogic.setSingletonCount(ShipComponentBean.ETC_FUEL_SCOOPS, 0);
                mFuelScoops.setEnabled(false);
            }
            else if ("configStandard".equals(config.getComponentID()))
            {
                mFuelScoops.setEnabled(true);
            }
            else if ("configStreamlined".equals(config.getComponentID()))
            {
                if (ShipEditLogic.getSingletonCount(ShipComponentBean.ETC_FUEL_SCOOPS) == 0)
                    ShipEditLogic.setSingletonCount(ShipComponentBean.ETC_FUEL_SCOOPS, 1);
                mFuelScoops.setEnabled(false);
            }
        }
    }
}
