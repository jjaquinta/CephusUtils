package jo.cephus.shipyard.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jo.cephus.core.data.ShipReportBean;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;

@SuppressWarnings("serial")
public class ErrorPanel extends JComponent
{
    private final static RuntimeBean mRuntime = RuntimeLogic.getInstance();

    private JTextArea                mClient;
    private JProgressBar             mHull;
    private JProgressBar             mHardpoints;
    private JProgressBar             mWeapons;
    private JLabel                   mHullMax;
    private JLabel                   mHardpointsMax;
    private JLabel                   mWeaponsMax;

    public ErrorPanel()
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
        mClient.setForeground(Color.RED);
        mHull = new JProgressBar();
        mHull.setStringPainted(true);
        mHardpoints = new JProgressBar();
        mHardpoints.setStringPainted(true);
        mWeapons = new JProgressBar();
        mWeapons.setStringPainted(true);
        mHullMax = new JLabel();
        mHardpointsMax = new JLabel();
        mWeaponsMax = new JLabel();
    }

    private void initLayout()
    {
        JPanel footer = new JPanel();
        footer.setLayout(new GridLayout(3, 3));
        footer.add(new JLabel("Hull:"));
        footer.add(mHull);
        footer.add(mHullMax);
        footer.add(new JLabel("Hardpoints:"));
        footer.add(mHardpoints);
        footer.add(mHardpointsMax);
        footer.add(new JLabel("Weapons:"));
        footer.add(mWeapons);
        footer.add(mWeaponsMax);
        
        setLayout(new BorderLayout());
        add("Center", new JScrollPane(mClient));
        add("South", footer);
    }

    private void initLink()
    {
        // data to UI
        mRuntime.addPropertyChangeListener("report",
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
        {
            StringBuffer text = new StringBuffer();
            for (String err : report.getErrors())
                text.append(err + "\r\n");
            mClient.setText(text.toString());
            mHull.setMinimum(0);
            mHull.setMaximum(report.getHullDisplacement());
            mHullMax.setText(FormatUtils.sTons(report.getHullDisplacement()));
            mHull.setValue(report.getHullUsed());
            mHull.setString(FormatUtils.sTons(report.getHullUsed()));
            mHardpoints.setMinimum(0);
            mHardpoints.setMaximum(report.getNumberOfHardpoints());
            mHardpointsMax.setText(String.valueOf(report.getNumberOfHardpoints()));
            mHardpoints.setValue(report.getNumberOfHardpointsUsed());
            mHardpoints.setString(String.valueOf(report.getNumberOfHardpointsUsed()));
            mWeapons.setMinimum(0);
            mWeapons.setMaximum(report.getWeaponSlots());
            mWeaponsMax.setText(String.valueOf(report.getWeaponSlots()));
            mWeapons.setValue(report.getWeaponSlotsUsed());
            mWeapons.setString(String.valueOf(report.getWeaponSlotsUsed()));
        }
    }
}
