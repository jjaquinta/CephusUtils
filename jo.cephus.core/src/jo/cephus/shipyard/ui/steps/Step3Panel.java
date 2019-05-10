package jo.cephus.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.JDriveChooser;
import jo.cephus.shipyard.ui.ctrl.ReportStatPanel;

@SuppressWarnings("serial")
public class Step3Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private JDriveChooser mJump;
    private ReportStatPanel mJumpStats;
    private ReportStatPanel mFuelStats;
    
    public Step3Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    private void initInstantiate()
    {
        mJump = new JDriveChooser();
        mJumpStats = new ReportStatPanel("Range", new ReportStatPanel.IReportStat() {
            @Override
            public String getStat(ShipReportBean report)
            {
                return report.getJumpNumber()+" psc";
            }
        });
        mFuelStats = new ReportStatPanel("Fuel Use", new ReportStatPanel.IReportStat() {
            @Override
            public String getStat(ShipReportBean report)
            {
                int singleJump = Math.max(1,
                        (report.getHullDisplacement() * report.getJumpNumber())
                                / 10);
                return FormatUtils.sTons(singleJump)+"/jump";
            }
        });
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Jump Drive"));
        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(1, 2));
        stats.add(mJumpStats);
        stats.add(mFuelStats);
        
        setLayout(new BorderLayout());
        add("Center", mJump);
        add("South", stats);
    }

    private void initLink()
    {
        // UI to Data
        mJump.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doActionManeuver();
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
    
    private void doActionManeuver()
    {
        ShipComponentBean jump = mJump.getComponent();
        if (jump != ShipEditLogic.getJumpDrive())
            ShipEditLogic.setJumpDrive(jump);
    }
    
    private void doNewComponents()
    {
        if (mRuntime.getShip() == null)
        {
            mJump.setComponent(null);
        }
        else
        {
            ShipComponentBean jump = ShipEditLogic.getJumpDrive();
            if (jump != mJump.getComponent())
                mJump.setComponent(jump);
        }
    }
}
