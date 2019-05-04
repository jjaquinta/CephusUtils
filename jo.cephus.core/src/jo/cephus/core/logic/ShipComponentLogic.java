package jo.cephus.core.logic;

import java.io.IOException;
import java.util.*;
import jo.audio.util.JSONUtils;
import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipComponentInstanceBean;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ShipComponentLogic
{

    public ShipComponentLogic()
    {
    }

    private static void init()
    {
        if(mIndex.size() > 0)
            return;
        try
        {
            JSONObject json = JSONUtils.readJSON("resource://jo/cephus/core/data/ShipComponent.json");
            JSONArray components = (JSONArray)json.get("shipComponents");
            for(int i = 0; i < components.size(); i++)
            {
                JSONObject component = (JSONObject)components.get(i);
                ShipComponentBean c = new ShipComponentBean(component);
                mIndex.put(c.getID(), c);
                List<ShipComponentBean> types = mIndexType.get(c.getType());
                if(types == null)
                {
                    types = new ArrayList<>();
                    mIndexType.put(c.getType(), types);
                }
                types.add(c);
            }

        }
        catch(IOException e)
        {
            throw new IllegalStateException(e);
        }
    }

    public static ShipComponentBean getComponent(String id)
    {
        init();
        return (ShipComponentBean)mIndex.get(id);
    }

    public static ShipComponentBean getComponent(ShipComponentInstanceBean component)
    {
        return getComponent(component.getComponentID());
    }

    public static List<ShipComponentBean> getComponentsByType(String type)
    {
        init();
        return mIndexType.get(type);
    }

    public static List<String> getTypes()
    {
        init();
        List<String> types = new ArrayList<>();
        types.addAll(mIndexType.keySet());
        Collections.sort(types);
        return types;
    }

    private static final Map<String,ShipComponentBean> mIndex = new HashMap<>();
    private static final Map<String,List<ShipComponentBean>> mIndexType = new HashMap<>();

}
