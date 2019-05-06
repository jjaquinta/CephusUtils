package jo.cephus.shipyard.cmd;

import java.io.File;

import jo.cephus.shipyard.logic.RuntimeLogic;
import jo.cephus.shipyard.ui.ShipyardFrame;


public class Shipyard
{
    private String[] mArgs;
    
    public Shipyard(String[] args)
    {
        mArgs = args;
    }
    
    public void run()
    {
        RuntimeLogic.init();
        parseArgs();
        ShipyardFrame app = new ShipyardFrame();
        app.setSize(1024, 768);
        app.setVisible(true);
    }
    
    private void parseArgs()
    {
        if (mArgs.length > 0)
        {
            File f = new File(mArgs[0]);
            if (f.exists())
                RuntimeLogic.loadAs(f);
        }
    }

    public static void main(String[] args)
    {
        Shipyard app = new Shipyard(args);
        app.run();
    }

}
