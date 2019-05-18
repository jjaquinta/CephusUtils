package jo.clight.core.logic;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.audio.util.JSONUtils;
import jo.clight.core.data.CharCareerBean;

public class CharCareerLogic
{
    private static final Map<String,CharCareerBean> mIndex = new HashMap<>();
    private static final Map<String,Map<String,Integer>> mBestCareerIndex = new HashMap<>();
    private static final Map<String,Integer> mBestCareerTotals = new HashMap<>();

    private static void init()
    {
        if(mIndex.size() > 0)
            return;
        try
        {
            JSONObject json = JSONUtils.readJSON("resource://jo/clight/core/data/CharCareer.json");
            JSONArray careers = (JSONArray)json.get("careers");
            for(int i = 0; i < careers.size(); i++)
            {
                JSONObject career = (JSONObject)careers.get(i);
                CharCareerBean c = new CharCareerBean(career);
                mIndex.put(c.getID(), c);
            }
            JSONObject best = JSONUtils.readJSON("resource://jo/clight/core/data/CharCareerBest.json");
            for (String skill : best.keySet())
            {
                Map<String,Integer> ranks = new HashMap<>();
                int total = 0;
                JSONArray list = (JSONArray)best.get(skill);
                for (int i = 0; i < list.size(); i++)
                {
                    JSONObject kv = (JSONObject)list.get(i);
                    String career = kv.getString("career");
                    int val = kv.getInt("value");
                    ranks.put(career, val);
                    total += val;
                }
                mBestCareerIndex.put(skill, ranks);
                mBestCareerTotals.put(skill, total);
            }
        }
        catch(IOException e)
        {
            throw new IllegalStateException(e);
        }
    }
    
    public static Collection<String> getCareers()
    {
        init();
        return mIndex.keySet();
    }

    public static CharCareerBean getCareer(String id)
    {
        init();
        if (mIndex.containsKey(id))
            return mIndex.get(id);
        return null;
    }

    public static CharCareerBean getCareer(Random rnd)
    {
        init();
        int idx = rnd.nextInt(mIndex.keySet().size());
        for (String id : mIndex.keySet())
            if (idx-- == 0)
                return getCareer(id);
        return null;
    }

    public static CharCareerBean getDraftCareer(Random rnd)
    {
        init();
        int idx = rnd.nextInt(CharCareerBean.DRAFT.length);
        return getCareer(CharCareerBean.DRAFT[idx]);
    }
    
    public static String getBestCareer(String skill, Random rnd)
    {
        Map<String, Integer> ranks = mBestCareerIndex.get(skill);
        int total = mBestCareerTotals.get(skill);
        int roll = rnd.nextInt(total);
        for (String career : ranks.keySet())
        {
            int val = ranks.get(career);
            roll -= val;
            if (roll < 0)
                return career;
        }
        return null;
    }
}
