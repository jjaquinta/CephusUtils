package jo.clight.shipyard.ui.ctrl;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.logic.ShipComponentLogic;
import jo.util.beans.PropChangeSupport;

@SuppressWarnings("serial")
public class ShipComponentChooser extends JComponent
{
    private List<ShipComponentBean> mComponents;
    private ShipComponentBean       mComponent;
    private ShipComponentBean       mNone = null;
    private PropChangeSupport       mPCS = new PropChangeSupport(this);
    
    private DefaultComboBoxModel<ShipComponentBean> mChooserModel;
    private JComboBox<ShipComponentBean>            mChooser;
    
    public ShipComponentChooser(String type)
    {
        this(type, false);
    }
    
    public ShipComponentChooser(String type, boolean withNone)
    {
        List<ShipComponentBean> components = ShipComponentLogic.getComponentsByType(type);
        init(components, withNone);
    }
    
    public ShipComponentChooser(List<ShipComponentBean> components)
    {
        init(components, false);
    }

    public void init(List<ShipComponentBean> components, boolean withNone)
    {
        mComponents = components;
        if (withNone)
        {
            mNone = ShipComponentLogic.getComponent(ShipComponentBean.NULL_ITEM);
            mComponents.add(0, mNone);
        }
        else
            mNone = null;
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mChooserModel = new DefaultComboBoxModel<>(mComponents.toArray(new ShipComponentBean[0]));
        mChooser = new JComboBox<>(mChooserModel);
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", mChooser);
    }

    private void initLink()
    {
        mChooser.addActionListener(new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e)
            {
                setComponent((ShipComponentBean)mChooser.getSelectedItem());
            }
        });
    }

    public ShipComponentBean getComponent()
    {
        if (mNone != null)
        {
            if (mComponent == null)
                return null;
            else if (mComponent.getID().equals(ShipComponentBean.NULL_ITEM))
                return null;
            else
                return mComponent;
        }
        else
            return mComponent;
    }

    public void setComponent(ShipComponentBean component)
    {
        if (mNone != null)
        {
            if (component == null)
                component = mNone;
        }
        else
        {
            if (mComponent == component)
                return;
        }
        mPCS.queuePropertyChange("component", mComponent, component);
        mComponent = component;
        if (mChooser.getSelectedItem() != mComponent)
            mChooser.setSelectedItem(mComponent);
        mPCS.firePropertyChange();
    }
    
    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        mPCS.addPropertyChangeListener(pcl);
    }
    
    public void addPropertyChangeListener(String prop, PropertyChangeListener pcl)
    {
        mPCS.addPropertyChangeListener(prop, pcl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        mPCS.removePropertyChangeListener(pcl);
    }
}
