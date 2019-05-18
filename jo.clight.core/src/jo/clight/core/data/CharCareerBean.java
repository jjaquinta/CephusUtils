package jo.clight.core.data;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import jo.audio.util.FromJSONLogic;
import jo.audio.util.IJSONAble;
import jo.audio.util.ToJSONLogic;
import jo.audio.util.model.data.AudioMessageBean;

public class CharCareerBean extends CephusBean implements IJSONAble
{
    // generated IDs
    public static final String CAREER_AGENT = "AGENT";
    public static final String CAREER_COLONIST = "COLONIST";
    public static final String CAREER_NAVY = "NAVY";
    public static final String CAREER_ROGUE = "ROGUE";
    public static final String CAREER_ARMY = "ARMY";
    public static final String CAREER_SCHOLAR = "SCHOLAR";
    public static final String CAREER_BELTER = "BELTER";
    public static final String CAREER_PIRATE = "PIRATE";
    public static final String CAREER_ELITE = "ELITE";
    public static final String CAREER_MARINE = "MARINE";
    public static final String CAREER_MERCHANT = "MERCHANT";
    public static final String CAREER_SCOUT = "SCOUT";
    
    public static final String[] DRAFT = {
            CAREER_COLONIST,
            CAREER_ROGUE,
            CAREER_MARINE,
            CAREER_MERCHANT,
            CAREER_NAVY,
            CAREER_SCOUT
    };
    public static final Set<String> CAREERS = new HashSet<>();
    static
    {
        CAREERS.add(CAREER_AGENT);
        CAREERS.add(CAREER_COLONIST);
        CAREERS.add(CAREER_NAVY);
        CAREERS.add(CAREER_ROGUE);
        CAREERS.add(CAREER_ARMY);
        CAREERS.add(CAREER_SCHOLAR);
        CAREERS.add(CAREER_BELTER);
        CAREERS.add(CAREER_PIRATE);
        CAREERS.add(CAREER_ELITE);
        CAREERS.add(CAREER_MARINE);
        CAREERS.add(CAREER_MERCHANT);
        CAREERS.add(CAREER_SCOUT);
    }
    
    private String mID;
    private String mQualification;
    private String mSurvival;
    private String mAdvancement;
    private String mReEnlistment;
    private int[] mMusteringOutCash;
    private String[] mMusteringOutMaterials;
    private String[] mRankBonus;
    private String[] mPersonalDevelopment;
    private String[] mService;
    private String[] mSpecialist;
    private String[] mAdvancedEducation;

    // constructors

    public CharCareerBean()
    {
    }

    public CharCareerBean(JSONObject json)
    {
        fromJSON(json);
    }

    // utilities
    @Override
    public String toString()
    {
        return mID;
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
        return new AudioMessageBean("CAREER_"+mID+"_NAME");
    }
    
    public AudioMessageBean getDescription()
    {
        return new AudioMessageBean("CAREER_"+mID+"_DESC");
    }
    
    public AudioMessageBean getRankDescription(int rank)
    {
        return new AudioMessageBean("CAREER_"+mID+"_RANK_"+rank+"_NAME");
    }
    
    public boolean isCareer(String id)
    {
        return CAREERS.contains(id);
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

    public String getQualification()
    {
        return mQualification;
    }

    public void setQualification(String qualification)
    {
        mQualification = qualification;
    }

    public String getSurvival()
    {
        return mSurvival;
    }

    public void setSurvival(String survival)
    {
        mSurvival = survival;
    }

    public String getAdvancement()
    {
        return mAdvancement;
    }

    public void setAdvancement(String advancement)
    {
        mAdvancement = advancement;
    }

    public String getReEnlistment()
    {
        return mReEnlistment;
    }

    public void setReEnlistment(String reEnlistment)
    {
        mReEnlistment = reEnlistment;
    }

    public int[] getMusteringOutCash()
    {
        return mMusteringOutCash;
    }

    public void setMusteringOutCash(int[] musteringOutCash)
    {
        mMusteringOutCash = musteringOutCash;
    }

    public String[] getMusteringOutMaterials()
    {
        return mMusteringOutMaterials;
    }

    public void setMusteringOutMaterials(String[] musteringOutMaterials)
    {
        mMusteringOutMaterials = musteringOutMaterials;
    }

    public String[] getRankBonus()
    {
        return mRankBonus;
    }

    public void setRankBonus(String[] rankBonus)
    {
        mRankBonus = rankBonus;
    }

    public String[] getPersonalDevelopment()
    {
        return mPersonalDevelopment;
    }

    public void setPersonalDevelopment(String[] personalDevelopment)
    {
        mPersonalDevelopment = personalDevelopment;
    }

    public String[] getService()
    {
        return mService;
    }

    public void setService(String[] service)
    {
        mService = service;
    }

    public String[] getSpecialist()
    {
        return mSpecialist;
    }

    public void setSpecialist(String[] specialist)
    {
        mSpecialist = specialist;
    }

    public String[] getAdvancedEducation()
    {
        return mAdvancedEducation;
    }

    public void setAdvancedEducation(String[] advancedEducation)
    {
        mAdvancedEducation = advancedEducation;
    }

}
