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
import jo.cephus.core.logic.ShipDesignLogic;
import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.PPlantChooser;
import jo.cephus.shipyard.ui.ctrl.ReportStatPanel;

@SuppressWarnings("serial")
public class Step4Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private PPlantChooser mPower;
    private ReportStatPanel mFuelStats;
    
    public Step4Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewComponents();
    }

    private void initInstantiate()
    {
        mPower = new PPlantChooser();
        mFuelStats = new ReportStatPanel("Fuel Use", new ReportStatPanel.IReportStat() {
            @Override
            public String getStat(ShipReportBean report)
            {
                String manCode = report.getManeuverCode();
                int perWeek = ShipDesignLogic.getFuelPerWeek(manCode);
                return FormatUtils.sTons(perWeek)+"/wk";
            }
        });
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Power Plant"));
        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(1, 2));
        stats.add(mFuelStats);
        
        setLayout(new BorderLayout());
        add("Center", mPower);
        add("South", stats);
   }

    private void initLink()
    {
        // UI to Data
        mPower.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doActionPower();
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
    
    private void doActionPower()
    {
        ShipComponentBean power = mPower.getComponent();
        if (power != ShipEditLogic.getPower())
            ShipEditLogic.setPower(power);
    }
    
    private void doNewComponents()
    {
        if (mRuntime.getShip() == null)
        {
            mPower.setComponent(null);
        }
        else
        {
            ShipComponentBean power = ShipEditLogic.getPower();
            if (power != mPower.getComponent())
                mPower.setComponent(power);
        }
    }
}
