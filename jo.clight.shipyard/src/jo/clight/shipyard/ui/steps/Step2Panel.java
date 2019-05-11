package jo.clight.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.FormatUtils;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;
import jo.clight.shipyard.logic.ShipEditLogic;
import jo.clight.shipyard.ui.ctrl.MDriveChooser;
import jo.clight.shipyard.ui.ctrl.ReportStatPanel;

@SuppressWarnings("serial")
public class Step2Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private MDriveChooser mManeuver;
    private ReportStatPanel mThrustStats;
    
    public Step2Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    private void initInstantiate()
    {
        mManeuver = new MDriveChooser();
        mThrustStats = new ReportStatPanel("Thrust", new ReportStatPanel.IReportStat() {
            @Override
            public String getStat(ShipReportBean report)
            {
                return FormatUtils.sGrav(report.getThrustNumber());
            }
        });
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Maneuver Drive"));
        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(1, 2));
        stats.add(mThrustStats);
        
        setLayout(new BorderLayout());
        add("Center", mManeuver);
        add("South", stats);
    }

    private void initLink()
    {
        // UI to Data
        mManeuver.addPropertyChangeListener(new PropertyChangeListener() {            
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
        ShipComponentBean maneuver = mManeuver.getComponent();
        if (maneuver != ShipEditLogic.getManeuver())
            ShipEditLogic.setManeuver(maneuver);
    }
    
    private void doNewComponents()
    {
        if (mRuntime.getShip() == null)
        {
            mManeuver.setComponent(null);
        }
        else
        {
            ShipComponentBean maneuver = ShipEditLogic.getManeuver();
            if (maneuver != mManeuver.getComponent())
                mManeuver.setComponent(maneuver);
        }
    }
}
