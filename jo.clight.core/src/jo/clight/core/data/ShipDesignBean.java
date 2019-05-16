package jo.clight.core.data;import java.util.ArrayList;import java.util.HashSet;import java.util.List;import java.util.Set;import org.json.simple.JSONObject;import jo.audio.util.FromJSONLogic;import jo.audio.util.IJSONAble;import jo.audio.util.ToJSONLogic;public class ShipDesignBean    implements IJSONAble{    public static final String ROLE_MILITARY = "military";    public static final String ROLE_DOUBLEOCCUPANCY = "doubleoccupancy";    public static final String ROLE_PASSENGERS = "passengers";        private String mShipID;    private String mShipName;    private String mShipFunction;    private Set<String> mRoles = new HashSet<>();    private List<ShipComponentInstanceBean> mComponents = new ArrayList<>();    // constructors        public ShipDesignBean()    {    }    public ShipDesignBean(JSONObject json)    {        fromJSON(json);    }    // utility functions    @Override    public String toString()    {        return mShipName;    }        public JSONObject toJSON()    {        return ToJSONLogic.toJSONFromBean(this);    }    public void fromJSON(JSONObject o)    {        FromJSONLogic.fromJSON(this, o);    }    // getters and setters        public String getShipID()    {        return mShipID;    }    public void setShipID(String shipID)    {        mShipID = shipID;    }    public String getShipName()    {        return mShipName;    }    public void setShipName(String shipName)    {        mShipName = shipName;    }    public String getShipFunction()    {        return mShipFunction;    }    public void setShipFunction(String shipFunction)    {        mShipFunction = shipFunction;    }    public List<ShipComponentInstanceBean> getComponents()    {        return mComponents;    }    public void setComponents(List<ShipComponentInstanceBean> components)    {        mComponents = components;    }    public Set<String> getRoles()    {        return mRoles;    }    public void setRoles(Set<String> roles)    {        mRoles = roles;    }}