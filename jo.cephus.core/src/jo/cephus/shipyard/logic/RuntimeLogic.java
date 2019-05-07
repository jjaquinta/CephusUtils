package jo.cephus.shipyard.logic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.audio.util.JSONUtils;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.logic.ShipComponentLogic;
import jo.cephus.core.logic.ShipReportLogic;
import jo.cephus.shipyard.data.RuntimeBean;

public class RuntimeLogic
{
    private static final RuntimeBean    mRuntime = new RuntimeBean();
    
    public static void init()
    {
        PropertyChangeListener pcl = new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                doNewShip();
            }
        };
        mRuntime.addPropertyChangeListener("ship", pcl);
        mRuntime.addPropertyChangeListener("ship.components", pcl);
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
    public static void save()
    {
        JSONObject library = new JSONObject();
        library.put("date", (new Date()).toString());
        JSONArray ships = new JSONArray();
        library.put("ships", ships);
        for (ShipDesignBean ship : mRuntime.getShips())
            ships.add(ship.toJSON());
        if (mRuntime.getShip() != null)
            library.put("selected", mRuntime.getShip().getShipID());
        try
        {
            JSONUtils.writeJSON(mRuntime.getFile(), library);
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
        setStatus("Delted "+ship.getShipName());
    }
    
    public static void newShip()
    {
        ShipDesignBean ship = new ShipDesignBean();
        ship.setShipID(String.valueOf(System.currentTimeMillis()));
        ship.setShipName("Lollipop");
        ship.setShipFunction("is a sweet trip to the candy shop.");
        ship.getComponents().add(ShipComponentLogic.getInstance("hull100", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("configStandard", 1));
        mRuntime.getShips().add(ship);
        mRuntime.fireMonotonicPropertyChange("ships", mRuntime.getShips());
        mRuntime.setShip(ship);
        mRuntime.setAnyChanges(true);
        setStatus("Created "+ship.getShipName());
    }
}
