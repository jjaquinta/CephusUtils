package jo.cephus.core.data;

import jo.cephus.core.logic.ParameterizedLogic;
import org.json.simple.JSONObject;

public class CephusBean
{

    public CephusBean()
    {
        mParams = new JSONObject();
    }

    protected Object get(String name, Object hardValue)
    {
        String key = "func$"+name;
        if(mParams.containsKey(key))
        {
            String id = mParams.getString(key);
            Object val = ParameterizedLogic.getParameterizedValue(this, id, hardValue);
            return val;
        } else
        {
            return hardValue;
        }
    }

    public JSONObject getParams()
    {
        return mParams;
    }

    public void setParams(JSONObject params)
    {
        mParams = params;
    }

    private JSONObject mParams;
}
