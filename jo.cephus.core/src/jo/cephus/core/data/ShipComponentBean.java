package jo.cephus.core.data;

import jo.audio.util.*;
import jo.audio.util.model.data.AudioMessageBean;
import jo.cephus.core.logic.text.TextLogic;

import org.json.simple.JSONObject;

// Referenced classes of package jo.cephus.core.data:
//            CephusBean

public class ShipComponentBean extends CephusBean
    implements IJSONAble
{
    public static final String NULL_ITEM = "none";
    public static final String ARMOR = "ARMOR";
    public static final String BAY = "BAY";
    public static final String BERTH = "BERTH";
    public static final String BRIDGE = "BRIDGE";
    public static final String COMPUTER = "COMPUTER";
    public static final String CONFIG = "CONFIG";
    public static final String ELECTRONICS = "ELECTRONICS";
    public static final String ETC = "ETC";
    public static final String FUEL = "FUEL";
    public static final String HULL = "HULL";
    public static final String JDRIVE = "JDRIVE";
    public static final String MDRIVE = "MDRIVE";
    public static final String PPLANT = "PPLANT";
    public static final String SCREENS = "SCREENS";
    public static final String STATEROOM = "STATEROOM";
    public static final String TURRET = "TURRET";
    public static final String WEAPON = "WEAPON";
    public static final String HANGER = "HANGER";
    
    private String mID;
    private AudioMessageBean mName;
    private AudioMessageBean mDescription;
    private double mVolume;
    private double mPrice;
    private int mMaxCopies;
    private String mType;
    private boolean mValidToAdd;
    private int mTechLevel;

    // constructors

    public ShipComponentBean()
    {
        mName = new AudioMessageBean();
        mDescription = new AudioMessageBean();
    }

    public ShipComponentBean(JSONObject json)
    {
        mName = new AudioMessageBean();
        mDescription = new AudioMessageBean();
        fromJSON(json);
    }
    
    // utilities
    @Override
    public String toString()
    {
        return TextLogic.getString(mName);
    }
    
    public JSONObject toJSON()
    {
        return ToJSONLogic.toJSONFromBean(this);
    }

    public void fromJSON(JSONObject o)
    {
        FromJSONLogic.fromJSON(this, o);
    }
    
    // getters and setters

    public String getID()
    {
        return mID;
    }

    public void setID(String iD)
    {
        mID = iD;
    }

    public AudioMessageBean getName()
    {
        return (AudioMessageBean)get("name", mName);
    }

    public void setName(AudioMessageBean name)
    {
        mName = name;
    }

    public AudioMessageBean getDescription()
    {
        return (AudioMessageBean)get("description", mDescription);
    }

    public void setDescription(AudioMessageBean description)
    {
        mDescription = description;
    }

    public double getVolume()
    {
        return ((Double)get("volume", Double.valueOf(mVolume))).doubleValue();
    }

    public void setVolume(double volume)
    {
        mVolume = volume;
    }

    public double getPrice()
    {
        return ((Double)get("price", Double.valueOf(mPrice))).doubleValue();
    }

    public void setPrice(double price)
    {
        mPrice = price;
    }

    public String getType()
    {
        return mType;
    }

    public void setType(String type)
    {
        mType = type;
    }

    public boolean isValidToAdd()
    {
        return ((Boolean)get("validToAdd", Boolean.valueOf(mValidToAdd))).booleanValue();
    }

    public void setValidToAdd(boolean validToAdd)
    {
        mValidToAdd = validToAdd;
    }

    public int getTechLevel()
    {
        return ((Integer)get("techLevel", Integer.valueOf(mTechLevel))).intValue();
    }

    public void setTechLevel(int techLevel)
    {
        mTechLevel = techLevel;
    }

    public int getMaxCopies()
    {
        return ((Integer)get("maxCopies", Integer.valueOf(mMaxCopies))).intValue();
    }

    public void setMaxCopies(int maxCopies)
    {
        mMaxCopies = maxCopies;
    }

}
