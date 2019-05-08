package jo.cephus.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.MDriveChooser;
import jo.cephus.shipyard.ui.ctrl.ReportStatPanel;

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
        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(1, 2));
        stats.add(mThrustStats);
        
        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel("Maneuver Drive");
        Font oldFont = jLabel.getFont();
        jLabel.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2));
        add("North", jLabel);
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
