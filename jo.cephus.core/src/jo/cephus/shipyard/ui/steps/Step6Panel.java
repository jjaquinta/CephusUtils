package jo.cephus.shipyard.ui.steps;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.border.TitledBorder;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ShipDesignLogic;
import jo.cephus.shipyard.logic.FormatUtils;
import jo.cephus.shipyard.ui.ctrl.DesignStatPanel;

@SuppressWarnings("serial")
public class Step6Panel extends JComponent
{
    private DesignStatPanel          mBridgeStats;

    public Step6Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mBridgeStats = new DesignStatPanel("Bridge:", new DesignStatPanel.IDesignStat() {            
            @Override
            public String getStat(ShipDesignBean ship)
            {
                ShipComponentBean bridge = ShipDesignLogic.findHighestNumberParam(ship, ShipComponentBean.BRIDGE, "tonsSupported");
                if (bridge == null)
                    return "-";
                else
                    return FormatUtils.sTons(bridge.getVolume())+" / "+FormatUtils.sCurrency(bridge.getPrice()*1000000);
            }
        });
    }

    private void initLayout()
    {
        setBorder(new TitledBorder("Bridge"));
        setLayout(new BorderLayout());
        add("Center", mBridgeStats);
    }

    private void initLink()
    {
        // UI to Data
        // data to UI
    }
}
