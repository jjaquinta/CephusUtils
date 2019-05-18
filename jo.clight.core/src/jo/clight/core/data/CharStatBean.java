package jo.clight.core.data;

import org.json.simple.JSONObject;

import jo.audio.util.FromJSONLogic;
import jo.audio.util.IJSONAble;
import jo.audio.util.ToJSONLogic;
import jo.audio.util.model.data.AudioMessageBean;

// Referenced classes of package jo.cephus.core.data:
//            CephusBean

public class CharStatBean extends CephusBean
    implements IJSONAble
{
    private String  mID;
    private int     mValue;

    // constructors

    public CharStatBean(String id)
    {
        mID = id;
    }

    public CharStatBean(JSONObject json)
    {
        fromJSON(json);
    }
    
    // utilities
    @Override
    public String toString()
    {
        return mID+":"+mValue;
    }
    
    public JSONObject toJSON()
    {
        return ToJSONLogic.toJSONFromBean(this);
    }

    public void fromJSON(JSONObject o)
    {
        FromJSONLogic.fromJSON(this, o);
    }
    
    public AudioMessageBean getName()
    {
        return new AudioMessageBean(mID+"_NAME");
    }
    
    public AudioMessageBean getDescription()
    {
        return new AudioMessageBean(mID+"_DESC");
    }
    
    public AudioMessageBean getHex()
    {
        return new AudioMessageBean("HEX_"+mValue);
    }
    
    public int getMod()
    {
        if (mValue <= 2)
            return -2;
        else if (mValue <= 5)
            return -1;
        else if (mValue <= 8)
            return 0;
        else if (mValue <= 11)
            return 1;
        else if (mValue <= 14)
            return 2;
        else if (mValue <= 17)
            return 3;
        else if (mValue <= 20)
            return 4;
        return 4 + (mValue - 20)/3;
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

    public int getValue()
    {
        return mValue;
    }

    public void setValue(int value)
    {
        mValue = value;
    }
}
