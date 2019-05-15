package jo.clight.core.data;import jo.audio.util.*;import jo.clight.core.logic.ParameterizedLogic;import jo.clight.core.logic.ShipComponentLogic;import org.json.simple.JSONObject;// Referenced classes of package jo.cephus.core.data://            ShipComponentBeanpublic class ShipComponentInstanceBean    implements IJSONAble{    public ShipComponentInstanceBean()    {    }    public ShipComponentInstanceBean(JSONObject json)    {        fromJSON(json);    }    public double getVolume()    {        try        {            ParameterizedLogic.addContext(this);            return getComponent().getVolume();        }        finally        {            ParameterizedLogic.removeContext(this);        }    }    public double getPrice()    {        try        {            ParameterizedLogic.addContext(this);            return getComponent().getPrice();        }        finally        {            ParameterizedLogic.removeContext(this);        }    }    public int getMaxCopies()    {        try        {            ParameterizedLogic.addContext(this);            return getComponent().getMaxCopies();        }        finally        {            ParameterizedLogic.removeContext(this);        }    }    public int getTechLevel()    {        try        {            ParameterizedLogic.addContext(this);            return getComponent().getTechLevel();        }        finally        {            ParameterizedLogic.removeContext(this);        }    }    public String getType()    {        return getComponent().getType();    }    public JSONObject toJSON()    {        return ToJSONLogic.toJSONFromBean(this);    }    public void fromJSON(JSONObject o)    {        FromJSONLogic.fromJSON(this, o);    }    public ShipComponentBean getComponent()    {        ShipComponentBean component = ShipComponentLogic.getComponent(this);        if (component == null)            System.err.println("Unknown component: "+mComponentID);        return component;    }    public String getComponentID()    {        return mComponentID;    }    public void setComponentID(String componentID)    {        mComponentID = componentID;    }    public int getCount()    {        return mCount;    }    public void setCount(int count)    {        mCount = count;    }    private String mComponentID;    private int mCount;}