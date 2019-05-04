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
        if(mParams.containsKey((new StringBuilder("func$")).append(name).toString()))
        {
            String id = mParams.getString((new StringBuilder("func$")).append(name).toString());
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
