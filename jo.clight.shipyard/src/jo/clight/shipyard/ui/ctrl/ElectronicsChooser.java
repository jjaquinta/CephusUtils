package jo.clight.shipyard.ui.ctrl;

import java.awt.BorderLayout;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import jo.clight.core.data.ShipComponentBean;

@SuppressWarnings("serial")
public class ElectronicsChooser extends JComponent
{
    private ShipComponentChooser mChooser;
    
    public ElectronicsChooser()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mChooser = new ShipComponentChooser(ShipComponentBean.ELECTRONICS);
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", mChooser);
    }

    private void initLink()
    {
    }

    public ShipComponentBean getComponent()
    {
        return mChooser.getComponent();
    }

    public void setComponent(ShipComponentBean component)
    {
        mChooser.setComponent(component);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        mChooser.addPropertyChangeListener(pcl);
    }
    
    public void addPropertyChangeListener(String prop, PropertyChangeListener pcl)
    {
        mChooser.addPropertyChangeListener(prop, pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        mChooser.removePropertyChangeListener(pcl);
    }
}

