package jo.clight.shipyard.ui.ctrl;

import java.awt.BorderLayout;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;

@SuppressWarnings("serial")
public class ArmorChooser extends JComponent
{
    private ShipComponentChooser mChooser;
    
    public ArmorChooser()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mChooser = new ShipComponentChooser(ShipComponentBean.ARMOR, true);
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

    public void setComponent(ShipComponentInstanceBean component)
    {
        if (component == null)
            mChooser.setComponent(null);
        else
            mChooser.setComponent(component.getComponent());
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
    
    public boolean isSelected(ShipComponentInstanceBean inst)
    {
        ShipComponentBean sel = mChooser.getComponent();
        if (inst == null)
            return sel == null;
        if (sel == null)
            return inst == null;
        return sel.getID().equals(inst.getComponentID());
    }
    
    public boolean isSelected(ShipComponentBean comp)
    {
        ShipComponentBean sel = mChooser.getComponent();
        return comp == sel;
    }
}

