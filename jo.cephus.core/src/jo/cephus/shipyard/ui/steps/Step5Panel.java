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

import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.ReportStatPanel;
import jo.util.utils.obj.IntegerUtils;

@SuppressWarnings("serial")
public class Step5Panel extends JComponent
{
    private final static RuntimeBean mRuntime = RuntimeLogic.getInstance();

    private JSpinner                 mWeeksOfPower;
    private JSpinner                 mNumberOfJumps;
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
        JPanel client = new JPanel();
        client.setLayout(new GridLayout(2, 2));
        client.add(new JLabel("Weeks of Power:"));
        client.add(mWeeksOfPower);
        client.add(new JLabel("Number of Jumps:"));
        client.add(mNumberOfJumps);

        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel("Step 5: Fuel Requirements");
        Font oldFont = jLabel.getFont();
        jLabel.setFont(
                new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2));
        add("North", jLabel);
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
            ShipReportBean report = RuntimeLogic.getInstance().getReport();
            int minFuel = ShipDesignLogic.getMinFuel(report.getManeuverCode());
            int val = Math.max(minFuel, report.getWeeksofPower());
            mWeeksOfPower
                    .setModel(new SpinnerNumberModel(val, minFuel, null, 1));
            mNumberOfJumps.setModel(new SpinnerNumberModel(
                    report.getNumberOfJumps(), 0, null, 1));
        }
    }
}
