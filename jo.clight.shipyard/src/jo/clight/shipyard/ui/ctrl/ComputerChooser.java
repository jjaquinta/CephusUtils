package jo.clight.shipyard.ui.ctrl;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.shipyard.data.RuntimeBean;
import jo.clight.shipyard.logic.RuntimeLogic;
import jo.clight.shipyard.logic.ShipEditLogic;

@SuppressWarnings("serial")
public class ComputerChooser extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private ShipComponentChooser mChooser;
    
    public ComputerChooser()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mChooser = new ShipComponentChooser(ShipEditLogic.COMPUTERS);
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", mChooser);
    }

    private void initLink()
    {
        // UI to Data
        mChooser.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doActionChooser();
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
    
    private void doActionChooser()
    {
        ShipComponentBean comp = mChooser.getComponent();
        if (comp != ShipEditLogic.getSingletonType(ShipEditLogic.COMPUTER_IDS))
            ShipEditLogic.setSingletonType(comp, ShipEditLogic.COMPUTER_IDS);
    }
    
    private void doNewComponents()
    {
        if (mRuntime.getShip() == null)
        {
            mChooser.setComponent(null);
        }
        else
        {
            ShipComponentBean comp = ShipEditLogic.getSingletonType(ShipEditLogic.COMPUTER_IDS);
            if (comp != mChooser.getComponent())
                mChooser.setComponent(comp);
        }
    }
}

