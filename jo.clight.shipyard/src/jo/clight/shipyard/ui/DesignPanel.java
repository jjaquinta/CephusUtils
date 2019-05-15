package jo.clight.shipyard.ui;

import javax.swing.JPanel;

import jo.audio.tools.TableLayout;
import jo.clight.shipyard.ui.steps.Step0Panel;
import jo.clight.shipyard.ui.steps.Step10Panel;
import jo.clight.shipyard.ui.steps.Step11Panel;
import jo.clight.shipyard.ui.steps.Step12Panel;
import jo.clight.shipyard.ui.steps.Step13Panel;
import jo.clight.shipyard.ui.steps.Step14Panel;
import jo.clight.shipyard.ui.steps.Step15Panel;
import jo.clight.shipyard.ui.steps.Step16Panel;
import jo.clight.shipyard.ui.steps.Step1Panel;
import jo.clight.shipyard.ui.steps.Step2Panel;
import jo.clight.shipyard.ui.steps.Step3Panel;
import jo.clight.shipyard.ui.steps.Step4Panel;
import jo.clight.shipyard.ui.steps.Step5Panel;
import jo.clight.shipyard.ui.steps.Step6Panel;
import jo.clight.shipyard.ui.steps.Step7Panel;
import jo.clight.shipyard.ui.steps.Step9Panel;

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
    private Step9Panel  mStep9;
    private Step10Panel mStep10;
    private Step11Panel mStep11;
    private Step12Panel mStep12;
    private Step13Panel mStep13;
    private Step14Panel mStep14;
    private Step15Panel mStep15;
    private Step16Panel mStep16;

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
        mStep9 = new Step9Panel();
        mStep10 = new Step10Panel();
        mStep11 = new Step11Panel();
        mStep12 = new Step12Panel();
        mStep13 = new Step13Panel();
        mStep14 = new Step14Panel();
        mStep15 = new Step15Panel();
        mStep16 = new Step16Panel();
    }

    private void initLayout()
    {
        setLayout(new TableLayout("fill=h ipadx=20"));
        add("1,+", mStep0);
        add("1,+", mStep1);
        add("1,+", mStep2);
        add("1,+", mStep3);
        add("1,+", mStep4);
        add("1,+", mStep5);
        add("1,+", mStep6);
        add("1,+", mStep7);
        add("1,+", mStep9);
        add("1,+", mStep10);
        add("1,+", mStep11);
        add("1,+", mStep12);
        add("1,+", mStep13);
        add("1,+", mStep14);
        add("1,+", mStep15);
        add("1,+", mStep16);
    }

    private void initLink()
    {
    }
}
