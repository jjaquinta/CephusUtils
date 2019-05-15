package jo.clight.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.FormatUtils;
import jo.clight.core.logic.ShipDesignLogic;
import jo.clight.shipyard.logic.RuntimeLogic;
import jo.clight.shipyard.ui.ctrl.DesignStatPanel;
import jo.clight.shipyard.ui.ctrl.ReportStatPanel;
import jo.clight.shipyard.ui.ctrl.ShipComponentSpinner;

@SuppressWarnings("serial")
public class Step9Panel extends JComponent
{
    private ShipComponentSpinner     mStaterooms;
    private ShipComponentSpinner     mBerths;
    private ShipComponentSpinner     mEmergency;
    private ReportStatPanel          mCrewStats;
    private DesignStatPanel          mCostStats;

    public Step9Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mStaterooms = new ShipComponentSpinner("Staterooms:", ShipComponentBean.STATEROOM_) { @Override
            protected int getMin()
            {
                ShipReportBean report = RuntimeLogic.getInstance().getReport();
                int min = report.getCrewTotal();
                return min;
            }
        };
        mBerths = new ShipComponentSpinner("Low Berths:", ShipComponentBean.BERTH_LOWBERTH);
        mEmergency = new ShipComponentSpinner("Emergency Berths:", ShipComponentBean.BERTH_EMERGENCY_LOWBERTH);
        mCostStats = new DesignStatPanel("Cost:", new DesignStatPanel.IDesignStat() {            
            @Override
            public String getStat(ShipDesignBean ship)
            {
                double cost = ShipDesignLogic.priceAllInstances(ship, ShipComponentBean.STATEROOM);
                return FormatUtils.sCurrency(cost*1000000);
            }
        });
        mCrewStats = new ReportStatPanel("Crew:", new ReportStatPanel.IReportStat() {            
            @Override
            public String getStat(ShipReportBean report)
            {
                return String.valueOf(report.getCrewTotal());
            }
        });
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Accomodation"));
        JPanel client = new JPanel();
        client.setLayout(new GridLayout(3, 1));
        client.add(mStaterooms);
        client.add(mBerths);
        client.add(mEmergency);
        
        JPanel footer = new JPanel();
        footer.setLayout(new GridLayout(1, 2));
        footer.add(mCrewStats);
        footer.add(mCostStats);

        setLayout(new BorderLayout());
        add("Center", client);
        add("South", footer);
    }

    private void initLink()
    {
        // UI to Data
        // data to UI
    }
}
