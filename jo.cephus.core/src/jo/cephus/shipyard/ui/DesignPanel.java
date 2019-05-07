package jo.cephus.shipyard.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import jo.cephus.shipyard.ui.steps.Step1Panel;
import jo.cephus.shipyard.ui.steps.Step2Panel;
import jo.cephus.shipyard.ui.steps.Step3Panel;
import jo.cephus.shipyard.ui.steps.Step4Panel;
import jo.cephus.shipyard.ui.steps.Step5Panel;
import jo.cephus.shipyard.ui.steps.Step6Panel;

@SuppressWarnings("serial")
public class DesignPanel extends JPanel
{
    private Step1Panel  mStep1;
    private Step2Panel  mStep2;
    private Step3Panel  mStep3;
    private Step4Panel  mStep4;
    private Step5Panel  mStep5;
    private Step6Panel  mStep6;

    public DesignPanel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mStep1 = new Step1Panel();
        mStep2 = new Step2Panel();
        mStep3 = new Step3Panel();
        mStep4 = new Step4Panel();
        mStep5 = new Step5Panel();
        mStep6 = new Step6Panel();
    }

    private void initLayout()
    {
        setLayout(new GridLayout(6, 1));
        add(mStep1);
        add(mStep2);
        add(mStep3);
        add(mStep4);
        add(mStep5);
        add(mStep6);
    }

    private void initLink()
    {
    }
}
