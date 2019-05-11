package jo.clight.shipyard.ui.ctrl;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jo.clight.core.data.ShipReportBean;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;

@SuppressWarnings("serial")
public class ReportStatPanel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private IReportStat mGetter;
    private String      mLabel;
    
    private JTextField  mClient;

    public ReportStatPanel(String label, IReportStat getter)
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
        mRuntime.addUIPropertyChangeListener("report",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewReport();
                    }
                });
    }

    private void doNewReport()
    {
        ShipReportBean report = mRuntime.getReport();
        if (report == null)
            mClient.setText("");
        else
            mClient.setText(mGetter.getStat(report));
    }
    
    public interface IReportStat
    {
        public String getStat(ShipReportBean report);
    }
}
