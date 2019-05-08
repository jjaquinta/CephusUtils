package jo.cephus.shipyard.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import jo.cephus.shipyard.ui.steps.Step0Panel;
import jo.cephus.shipyard.ui.steps.Step10Panel;
import jo.cephus.shipyard.ui.steps.Step1Panel;
import jo.cephus.shipyard.ui.steps.Step2Panel;
import jo.cephus.shipyard.ui.steps.Step3Panel;
import jo.cephus.shipyard.ui.steps.Step4Panel;
import jo.cephus.shipyard.ui.steps.Step5Panel;
import jo.cephus.shipyard.ui.steps.Step6Panel;
import jo.cephus.shipyard.ui.steps.Step7Panel;
import jo.cephus.shipyard.ui.steps.Step8Panel;
import jo.cephus.shipyard.ui.steps.Step9Panel;

@SuppressWarnings("serial")
public class DesignPanel extends JPanel
{
    private Step0Panel  mStep0;
    private Step1Panel  mStep1;
    private Step2Panel  mStep2;
    private Step3Panel  mStep3;
    private Step4Panel  mStep4;
    private Step5Panel  mStep5;
    private Step6Panel  mStep6;
    private Step7Panel  mStep7;
    private Step8Panel  mStep8;
    private Step9Panel  mStep9;
    private Step10Panel mStep10;

    public DesignPanel()
    {
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initInstantiate()
    {
        mStep0 = new Step0Panel();
        mStep1 = new Step1Panel();
        mStep2 = new Step2Panel();
        mStep3 = new Step3Panel();
        mStep4 = new Step4Panel();
        mStep5 = new Step5Panel();
        mStep6 = new Step6Panel();
        mStep7 = new Step7Panel();
        mStep8 = new Step8Panel();
        mStep9 = new Step9Panel();
        mStep10 = new Step10Panel();
    }

    private void initLayout()
    {
        setLayout(new GridLayout(11, 1));
        add(mStep0);
        add(mStep1);
        add(mStep2);
        add(mStep3);
        add(mStep4);
        add(mStep5);
        add(mStep6);
        add(mStep7);
        add(mStep8);
        add(mStep9);
        add(mStep10);
    }

    private void initLink()
    {
    }
}
