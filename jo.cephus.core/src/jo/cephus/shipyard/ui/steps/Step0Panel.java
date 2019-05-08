package jo.cephus.shipyard.ui.steps;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jo.cephus.shipyard.data.RuntimeBean;
import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.logic.ShipEditLogic;

@SuppressWarnings("serial")
public class Step0Panel extends JComponent
{
    private final static RuntimeBean             mRuntime = RuntimeLogic
            .getInstance();

    private JTextField  mName;
    private JTextArea   mDesc;
    
    public Step0Panel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNewName();
        doNewDesc();
    }

    private void initInstantiate()
    {
        mName = new JTextField();
        mDesc = new JTextArea();
        mDesc.setLineWrap(true);
        mDesc.setWrapStyleWord(true);
    }

    private void initLayout()
    {
        JPanel line1 = new JPanel();
        line1.setLayout(new BorderLayout());
        line1.add("West", new JLabel("Name:"));
        line1.add("Center", mName);
        
        JPanel line2 = new JPanel();
        line2.setLayout(new BorderLayout());
        line2.add("West", new JLabel("Purpose:"));
        line2.add("Center", mDesc);
        
        JPanel client = new JPanel();
        client.setLayout(new BorderLayout());
        client.add("North", line1);
        client.add("Center", line2);
        
        setLayout(new BorderLayout());
        JLabel jLabel = new JLabel("Information");
        Font oldFont = jLabel.getFont();
        jLabel.setFont(new Font(oldFont.getName(), Font.BOLD, oldFont.getSize() + 2));
        add("North", jLabel);
        add("Center", client);
    }

    private void initLink()
    {
        // UI to Data
        mName.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e)
            {
                doActionName();
            }
        });
        mDesc.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e)
            {
                doActionDesc();
            }
        });
        // data to UI        
        mRuntime.addPropertyChangeListener("ship.shipName",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewName();
                    }
                });
        mRuntime.addPropertyChangeListener("ship.shipFunction",
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt)
                    {
                        doNewDesc();
                    }
                });
    }
    
    private void doActionName()
    {
        ShipEditLogic.setShipName(mName.getText());
    }
    
    private void doActionDesc()
    {
        ShipEditLogic.setShipFunction(mDesc.getText());
    }
    
    private void doNewName()
    {
        if (mRuntime.getShip() == null)
            mName.setText("");
        else
            mName.setText(mRuntime.getShip().getShipName());
    }
    
    private void doNewDesc()
    {
        if (mRuntime.getShip() == null)
            mDesc.setText("");
        else
            mDesc.setText(mRuntime.getShip().getShipFunction());
    }
}
