package jo.clight.core.logic;

import java.io.IOException;
import java.util.*;
import jo.audio.util.JSONUtils;
import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ShipComponentLogic
{
    private static final Map<String,ShipComponentBean> mIndex = new HashMap<>();
    private static final Map<String,List<ShipComponentBean>> mIndexType = new HashMap<>();

    private static void init()
    {
        if(mIndex.size() > 0)
            return;
        try
        {
            JSONObject json = JSONUtils.readJSON("resource://jo/clight/core/data/ShipComponent.json");
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
        if (mIndex.containsKey(id))
            return mIndex.get(id);
        else if (id.startsWith("$"))
            return mIndex.get(id.substring(1));
        return null;
    }

    public static ShipComponentInstanceBean getInstance(String id, int count)
    {
        init();
        if (id.startsWith("$"))
            id = id.substring(1);
        ShipComponentInstanceBean inst = new ShipComponentInstanceBean();
        inst.setComponentID(id);
        inst.setCount(count);
        return inst;
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

    public static List<ShipComponentInstanceBean> expandInstances(List<ShipComponentInstanceBean> compressed)
    {
        List<ShipComponentInstanceBean> expanded = new ArrayList<>();
        for (ShipComponentInstanceBean comp : compressed)
        {
            for (int i = 0; i < comp.getCount(); i++)
                expanded.add(getInstance(comp.getComponentID(), 1));
        }
        return expanded;
    }
}
