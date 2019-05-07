package jo.cephus.shipyard.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import jo.cephus.shipyard.ui.steps.Step1Panel;

@SuppressWarnings("serial")
public class DesignPanel extends JPanel
{
    private Step1Panel  mStep1;

    public DesignPanel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mStep1 = new Step1Panel();
    }

    private void initLayout()
    {
        setLayout(new GridLayout(1, 1));
        add(mStep1);
    }

    private void initLink()
    {
    }
}
