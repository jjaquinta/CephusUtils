package jo.cephus.core.cmd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.audio.util.JSONUtils;
import jo.cephus.core.data.ShipComponentInstanceBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.ShipReportLogic;
import jo.cephus.core.logic.text.TextLogic;
import jo.util.logic.CSVLogic;
import jo.util.utils.io.FileUtils;

public class ExportShips
{
    private String[] mArgs;
    private File     mShipyardFile;
    
    public ExportShips(String[] argv)
    {
        mArgs = argv;
    }
    
    public void run()
    {
        parseArgs();
        try
        {
            JSONObject json = JSONUtils.readJSON(mShipyardFile);
            JSONArray ships = (JSONArray)json.get("ships");
            for (int i = 0; i < ships.size(); i++)
            {
                ShipDesignBean ship = new ShipDesignBean((JSONObject)ships.get(i));
                ShipReportBean report = ShipReportLogic.report(ship);
                String fname = toFileName(ship, report);
                System.out.println(fname);
                File textFile = new File(mShipyardFile.getParent(), fname+".txt");
                FileUtils.writeFile(TextLogic.getString(report.getProse()), textFile);
                File csvFile = new File(mShipyardFile.getParent(), fname+".csv");
                BufferedWriter out = new BufferedWriter(new FileWriter(csvFile));
                out.write(CSVLogic.toCSVHeader(new String[] { "Name", "Type", "Count", "Price", "Volume" }));
                out.newLine();
                for (ShipComponentInstanceBean comp : ship.getComponents())
                {
                    List<Object> outbuf = new ArrayList<>();
                    outbuf.add(comp.getComponentID());
                    outbuf.add(comp.getType());
                    outbuf.add(comp.getCount());
                    outbuf.add(comp.getPrice());
                    outbuf.add(comp.getVolume());
                    out.write(CSVLogic.toCSVLine(outbuf));
                    out.newLine();
                }
                out.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private String toFileName(ShipDesignBean ship, ShipReportBean report)
    {
        String fname = "TL"+report.getTechLevel()+"_"+ship.getShipName().replace(" ", "_");
        return fname;
    }
    
    private void parseArgs()
    {
        for (int i = 0; i < mArgs.length; i++)
            if ("-o".equals(mArgs[i]))
                mShipyardFile = new File(mArgs[++i]);
        if (mShipyardFile == null)
        {
            System.out.println("No -o <shipyard.json> given.");
            System.exit(0);
        }
    }
    
    public static void main(String[] argv)
    {
        ExportShips app = new ExportShips(argv);
        app.run();
    }
}
