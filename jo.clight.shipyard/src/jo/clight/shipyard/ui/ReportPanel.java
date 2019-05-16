package jo.clight.shipyard.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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

    private ButtonGroup mGroup;
    private JRadioButton mShip;
    private JRadioButton mDesign;
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
        mGroup = new ButtonGroup();
        mShip = new JRadioButton("Ship Sheet");
        mGroup.add(mShip);
        mDesign = new JRadioButton("Design Sheet");
        mGroup.add(mDesign);
        mShip.setSelected(true);
        mClient = new JTextArea();
        mClient.setEditable(false);
        mClient.setLineWrap(true);
        mClient.setWrapStyleWord(true);
        mClient.setFont(new Font("Monospaced", Font.PLAIN, 12));
    }

    private void initLayout()
    {
        JPanel header = new JPanel();
        header.setLayout(new GridLayout(1, 2));
        header.add(mShip);
        header.add(mDesign);
        setLayout(new BorderLayout());
        add("North", header);
        add("Center", new JScrollPane(mClient));
    }

    private void initLink()
    {
        ActionListener al = new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                doNewReport();
            }
        };
        mShip.addActionListener(al);
        mDesign.addActionListener(al);
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
            String table;
            if (mShip.isSelected())
                table = ShipTableLogic.formatTable(ShipTableLogic.toShipSheet(report), "25,25,25,25");
            else
                table = ShipTableLogic.formatTable(ShipTableLogic.toDesignSheet(report.getShip()), "0,40|wrap,0|right,0.2,0.2");
            mClient.setText(table);
        }
    }
}
