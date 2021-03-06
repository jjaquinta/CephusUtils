package jo.clight.shipyard.logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.audio.util.JSONUtils;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.logic.ParameterizedLogic;
import jo.clight.core.logic.ShipExportLogic;
import jo.clight.core.logic.ShipLibraryLogic;
import jo.clight.core.logic.ShipReportLogic;
import jo.clight.core.logic.eval.ShipEvals;
import jo.clight.shipyard.data.RuntimeBean;

public class RuntimeLogic
{
    private static final RuntimeBean    mRuntime = new RuntimeBean();
    
    public static void init()
    {
        ShipEvals.init();
        PropertyChangeListener pcl = new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doNewShip();
            }
        };
        mRuntime.addPropertyChangeListener("ship", pcl);
        mRuntime.addPropertyChangeListener("ship.components", pcl);
        mRuntime.addPropertyChangeListener("ship.shipName", pcl);
        mRuntime.addPropertyChangeListener("ship.shipFunction", pcl);
        mRuntime.addPropertyChangeListener("ship.shipRoles", pcl);
    }
    
    public static void shutdown()
    {
        
    }
    
    public static RuntimeBean getInstance()
    {
        return mRuntime;
    }
    
    public static void loadAs(File file)
    {
        mRuntime.setFile(file);
        load();
    }
    
    public static void load()
    {
        if (mRuntime.getFile() == null)
        {
            setStatus("No file set to load from.");
            return;
        }
        try
        {
            JSONObject library = JSONUtils.readJSON(mRuntime.getFile());
            mRuntime.getShips().clear();
            JSONArray ships = (JSONArray)library.get("ships");
            String selectedID = library.getString("selected");
            ShipDesignBean selected = null;
            for (int i = 0; i < ships.size(); i++)
            {
                JSONObject shipj = (JSONObject)ships.get(i);
                ShipDesignBean ship = new ShipDesignBean(shipj);
                mRuntime.getShips().add(ship);
                if (ship.getShipID().equals(selectedID))
                    selected = ship;
            }
            mRuntime.fireMonotonicPropertyChange("ships", mRuntime.getShips());
            mRuntime.setShip(selected);
            mRuntime.setAnyChanges(false);
            setStatus("Loaded "+mRuntime.getShips().size()+" ships from "+mRuntime.getFile().getName());
        }
        catch (IOException e)
        {
            setError(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void saveShipsTo(OutputStream os) throws IOException
    {
        JSONObject library = new JSONObject();
        library.put("date", (new Date()).toString());
        JSONArray ships = new JSONArray();
        library.put("ships", ships);
        for (ShipDesignBean ship : mRuntime.getShips())
            ships.add(ship.toJSON());
        if (mRuntime.getShip() != null)
            library.put("selected", mRuntime.getShip().getShipID());
        os.write(library.toJSONString().getBytes());
    }
    
    public static void save()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(mRuntime.getFile());
            saveShipsTo(fos);
            fos.close();
            mRuntime.setAnyChanges(false);
            setStatus("Saved "+mRuntime.getShips().size()+" ships to "+mRuntime.getFile().getName());
        }
        catch (IOException e)
        {
            setError(e);
        }
    }
    
    public static void saveAs(File file)
    {
        mRuntime.setFile(file);
        save();
    }
    
    public static void export()
    {
        File file = mRuntime.getFile();
        String fname = file.getName();
        int o = fname.lastIndexOf('.');
        if (o > 0)
            fname = fname.substring(0, o)+".zip";
        file = new File(file.getParentFile(), fname);
        exportAs(file);
    }
    
    public static void exportAs(File file)
    {
        String baseName = file.getName();
        int o = baseName.lastIndexOf('.');
        if (o > 0)
            baseName = baseName.substring(0, o);
        try
        {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(file));
            zos.putNextEntry(new ZipEntry(baseName+".json"));
            saveShipsTo(zos);
            for (ShipDesignBean ship : mRuntime.getShips())
            {
                try
                {
                    ParameterizedLogic.addContext(ship);
                    ShipReportBean report = ShipReportLogic.report(ship);
                    String fname = ShipExportLogic.toFileName(ship, report);
                    System.out.println(fname);
                    ShipExportLogic.exportSheets(report, fname, null, zos);
                    ShipExportLogic.exportTable(ship, fname, null, zos);
                    ShipExportLogic.exportPlans(ship, fname, null, zos);
                }
                finally
                {
                    ParameterizedLogic.removeContext(ship);
                }
            }
            zos.close();
            setStatus("Saved "+mRuntime.getShips().size()+" ships to "+file.getName());
        }
        catch (IOException e)
        {
            setError(e);
        }
    }
    
    public static void setError(Throwable t)
    {
        t.printStackTrace();
        setStatus(t.toString());
    }
    
    public static void setStatus(String msg)
    {
        mRuntime.setStatus(msg);
        System.out.println(msg);
    }
    
    private static void doNewShip()
    {
        if (mRuntime.getShip() == null)
            mRuntime.setReport(null);
        else
            mRuntime.setReport(ShipReportLogic.report(mRuntime.getShip()));
    }

    public static void setShip(ShipDesignBean ship)
    {
        mRuntime.setShip(ship);
        if (ship == null)
            setStatus("No ship selected");
        else
            setStatus("Selected ship "+ship.getShipName());
    }
    
    public static void deleteShip()
    {
        ShipDesignBean ship = mRuntime.getShip();
        if (ship == null)
            return;
        mRuntime.setShip(null);
        mRuntime.getShips().remove(ship);
        mRuntime.fireMonotonicPropertyChange("ships", mRuntime.getShips());
        mRuntime.setAnyChanges(true);
        setStatus("Deleted "+ship.getShipName());
    }
    
    public static void newShip()
    {
        ShipDesignBean ship = ShipLibraryLogic.constructDesignExample();
        ship.setShipID(String.valueOf(System.currentTimeMillis()));
        ship.setShipName("Lollipop");
        ship.setShipFunction("is a sweet trip to the candy shop.");
        mRuntime.getShips().add(ship);
        mRuntime.fireMonotonicPropertyChange("ships", mRuntime.getShips());
        mRuntime.setShip(ship);
        mRuntime.setAnyChanges(true);
        setStatus("Created "+ship.getShipName());
    }
}
