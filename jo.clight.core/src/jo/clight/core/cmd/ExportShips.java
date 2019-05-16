package jo.clight.core.cmd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.audio.util.JSONUtils;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.ShipExportLogic;
import jo.clight.core.logic.ShipReportLogic;
import jo.util.utils.io.StreamUtils;

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
            String yardName = mShipyardFile.getName();
            yardName = yardName.substring(0, yardName.length() - 5);
            File zipFile = new File(mShipyardFile.getParentFile(), yardName+".zip");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            zos.putNextEntry(new ZipEntry(mShipyardFile.getName()));
            FileInputStream fis = new FileInputStream(mShipyardFile);
            StreamUtils.copy(fis, zos);
            fis.close();
            for (int i = 0; i < ships.size(); i++)
            {
                ShipDesignBean ship = new ShipDesignBean((JSONObject)ships.get(i));
                ShipReportBean report = ShipReportLogic.report(ship);
                String fname = ShipExportLogic.toFileName(ship, report);
                System.out.println(fname);
                ShipExportLogic.exportTable(ship, fname, mShipyardFile.getParentFile(), zos);
                ShipExportLogic.exportPlans(ship, fname, mShipyardFile.getParentFile(), zos);
            }
            zos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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
