package jo.cephus.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;
import jo.cephus.shipyard.ui.ctrl.DesignStatPanel;
import jo.cephus.shipyard.ui.ctrl.ReportStatPanel;
import jo.cephus.shipyard.ui.ctrl.ShipComponentSpinner;

@SuppressWarnings("serial")
public class Step9Panel extends JComponent
{
    private ShipComponentSpinner     mStaterooms;
    private ShipComponentSpinner     mBerths;
    private ShipComponentSpinner     mBarracks;
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
        mStaterooms = new ShipComponentSpinner("Staterooms:", ShipEditLogic.STATEROOM) { @Override
            protected int getMin()
            {
                ShipReportBean report = RuntimeLogic.getInstance().getReport();
                int min = report.getCrewTotal();
                return min;
            }
        };
        mBerths = new ShipComponentSpinner("Low Berths:", ShipEditLogic.LOWBERTH);
        mBarracks = new ShipComponentSpinner("Barracks:", ShipEditLogic.BARRACKS);
        mEmergency = new ShipComponentSpinner("Emergency Berths:", ShipEditLogic.EMERGENCY_LOWBERTH);
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
        client.setLayout(new GridLayout(4, 1));
        client.add(mStaterooms);
        client.add(mBerths);
        client.add(mBarracks);
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
