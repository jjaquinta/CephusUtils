package jo.cephus.shipyard.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.text.TextLogic;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.RuntimeLogic;

@SuppressWarnings("serial")
public class ReportPanel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private JTextArea   mClient;

    public ReportPanel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewReport();
    }

    private void initInstantiate()
    {
        mClient = new JTextArea();
        mClient.setEditable(false);
        mClient.setLineWrap(true);
        mClient.setWrapStyleWord(true);
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", new JScrollPane(mClient));
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

    private synchronized void doNewReport()
    {
        ShipReportBean report = mRuntime.getReport();
        if (report == null)
            mClient.setText("");
        else
            mClient.setText(TextLogic.getString(report.getProse()));
    }
}
