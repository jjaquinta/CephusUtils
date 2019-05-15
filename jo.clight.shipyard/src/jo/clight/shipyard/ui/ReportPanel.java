package jo.clight.shipyard.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.ShipTableLogic;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;

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
        mClient.setFont(new Font("Monospaced", Font.PLAIN, 12));
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
        {
            String table = ShipTableLogic.formatTable(ShipTableLogic.toShipSheet(report), "25,25,25,25");
            mClient.setText(table);
        }
    }
}
