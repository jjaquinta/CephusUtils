package jo.clight.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.simple.JSONObject;

import jo.audio.util.FromJSONLogic;
import jo.audio.util.IJSONAble;
import jo.audio.util.ToJSONLogic;
import jo.audio.util.model.data.AudioMessageBean;
import jo.clight.core.logic.DiceLogic;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class CharPersonBean extends CephusBean implements IJSONAble
{
    public static final String STAT_STR            = "STR";
    public static final String STAT_DEX            = "DEX";
    public static final String STAT_END            = "END";
    public static final String STAT_INT            = "INT";
    public static final String STAT_EDU            = "EDU";
    public static final String STAT_SOC            = "SOC";
    
    public static final int HOMEWORLD_HIGHTECH = 0;
    public static final int HOMEWORLD_FRONTIER = 1;
    public static final int HOMEWORLD_INHOSPITABLE = 2;
    public static final int HOMEWORLD_BACKWATER = 3;
    public static final int HOMEWORLD_MAX = 4;
    public static final String[][] HOMEWORLD_SKILLS = {
            { "COMPUTER-1", "GRAV VEHICLES-1", "STREETWISE-1" },
            { "DRIVING-1", "WATERCRAFT-1", "SURVIVAL-1" },
            { "REPAIR-1", "SCIENCE-1", "ZERO_G-1" },
            { "ANIMALS-1", "RECON-1", "SURVIVAL-1" },            
    };
    
    // skill IDS
    public static final String SKILL_ADMIN         = "ADMIN";
    public static final String SKILL_AIRCRAFT      = "AIRCRAFT";
    public static final String SKILL_ANIMALS       = "ANIMALS";
    public static final String SKILL_ATHLETICS     = "ATHLETICS";
    public static final String SKILL_CAROUSING     = "CAROUSING";
    public static final String SKILL_COMPUTER      = "COMPUTER";
    public static final String SKILL_DECEPTION     = "DECEPTION";
    public static final String SKILL_DEMOLITIONS   = "DEMOLITIONS";
    public static final String SKILL_DRIVING       = "DRIVING";
    public static final String SKILL_ENGINEERING   = "ENGINEERING";
    public static final String SKILL_GRAV_VEHICLE  = "GRAV_VEHICLE";
    public static final String SKILL_GUN_COMBAT    = "GUN_COMBAT";
    public static final String SKILL_GUNNERY       = "GUNNERY";
    public static final String SKILL_HEAVY_WEAPONS = "HEAVY_WEAPONS";
    public static final String SKILL_INVESTIGATION = "INVESTIGATION";
    public static final String SKILL_JACK_O_TRADES = "JACK_O_TRADES";
    public static final String SKILL_LEADERSHIP    = "LEADERSHIP";
    public static final String SKILL_LIAISON       = "LIAISON";
    public static final String SKILL_MEDICINE      = "MEDICINE";
    public static final String SKILL_MELEE_COMBAT  = "MELEE_COMBAT";
    public static final String SKILL_PILOTING      = "PILOTING";
    public static final String SKILL_SCIENCE       = "SCIENCE";
    public static final String SKILL_STEALTH       = "STEALTH";
    public static final String SKILL_STEWARD       = "STEWARD";
    public static final String SKILL_STREETWISE    = "STREETWISE";
    public static final String SKILL_SURVIVAL      = "SURVIVAL";
    public static final String SKILL_TACTICS       = "TACTICS";
    public static final String SKILL_RECON         = "RECON";
    public static final String SKILL_REPAIR        = "REPAIR";
    public static final String SKILL_WATERCRAFT    = "WATERCRAFT";
    public static final String SKILL_ZERO_G        = "ZERO_G";
    public static final Set<String> SKILLS = new HashSet<>();
    static
    {
        SKILLS.add(SKILL_ADMIN);
        SKILLS.add(SKILL_AIRCRAFT);
        SKILLS.add(SKILL_ANIMALS);
        SKILLS.add(SKILL_ATHLETICS);
        SKILLS.add(SKILL_CAROUSING);
        SKILLS.add(SKILL_COMPUTER);
        SKILLS.add(SKILL_DECEPTION);
        SKILLS.add(SKILL_DEMOLITIONS);
        SKILLS.add(SKILL_DRIVING);
        SKILLS.add(SKILL_ENGINEERING);
        SKILLS.add(SKILL_GRAV_VEHICLE);
        SKILLS.add(SKILL_GUN_COMBAT);
        SKILLS.add(SKILL_GUNNERY);
        SKILLS.add(SKILL_HEAVY_WEAPONS);
        SKILLS.add(SKILL_INVESTIGATION);
        SKILLS.add(SKILL_JACK_O_TRADES);
        SKILLS.add(SKILL_LEADERSHIP);
        SKILLS.add(SKILL_LIAISON);
        SKILLS.add(SKILL_MEDICINE);
        SKILLS.add(SKILL_MELEE_COMBAT);
        SKILLS.add(SKILL_PILOTING);
        SKILLS.add(SKILL_SCIENCE);
        SKILLS.add(SKILL_STEALTH);
        SKILLS.add(SKILL_STEWARD);
        SKILLS.add(SKILL_STREETWISE);
        SKILLS.add(SKILL_SURVIVAL);
        SKILLS.add(SKILL_TACTICS);
        SKILLS.add(SKILL_RECON);
        SKILLS.add(SKILL_REPAIR);
        SKILLS.add(SKILL_WATERCRAFT);
        SKILLS.add(SKILL_ZERO_G);
    }

    private String             mID;
    private String             mName;
    private CharStatBean       mSTR                = new CharStatBean(STAT_STR);
    private CharStatBean       mDEX                = new CharStatBean(STAT_DEX);
    private CharStatBean       mEND                = new CharStatBean(STAT_END);
    private CharStatBean       mINT                = new CharStatBean(STAT_INT);
    private CharStatBean       mEDU                = new CharStatBean(STAT_EDU);
    private CharStatBean       mSOC                = new CharStatBean(STAT_SOC);
    private int                mMoney;
    private int                mAge;
    private int                mRank;
    private int                mHomeworldType;
    private String             mCareer;
    private int                mTerms;
    private Map<String,Integer> mSkills = new HashMap<>();

    // constructors

    public CharPersonBean()
    {
    }

    public CharPersonBean(JSONObject json)
    {
        fromJSON(json);
    }

    // utilities
    @Override
    public String toString()
    {
        return mName;
    }

    public JSONObject toJSON()
    {
        return ToJSONLogic.toJSONFromBean(this);
    }

    public void fromJSON(JSONObject o)
    {
        FromJSONLogic.fromJSON(this, o);
    }
    
    public AudioMessageBean getHomeworldTypeName()
    {
        return new AudioMessageBean("HOMEWORLD_"+mHomeworldType+"_NAME");
    }
    
    public int getSkill(String skill)
    {
        if (mSkills.containsKey(skill))
            return mSkills.get(skill);
        else
            return 0;
    }
    
    public void addSkill(String def)
    {
        if (def.startsWith("+"))
        {
            int o = def.indexOf(' ');
            int n = IntegerUtils.parseInt(def.substring(1, o));
            CharStatBean stat = getStat(def.substring(o + 1));
            stat.setValue(stat.getValue() + n);
            return;
        }
        if (def.startsWith("-"))
        {
            int o = def.indexOf(' ');
            int n = IntegerUtils.parseInt(def.substring(1, o));
            CharStatBean stat = getStat(def.substring(o + 1));
            stat.setValue(stat.getValue() - n);
            return;
        }
        int o = def.lastIndexOf('-');
        if (o < 0)
            addSkill(def, 1);
        else
            addSkill(def.substring(0, o), IntegerUtils.parseInt(def.substring(o + 1)));
    }
    
    public void addSkill(String id, int amnt)
    {
        if (StringUtils.isTrivial(id))
            return;
        if (mSkills.containsKey(id))
            mSkills.put(id, mSkills.get(id) + amnt);
        else
            mSkills.put(id, amnt);
    }
    
    public void addSkill(String[] ids, Random rnd)
    {
        int idx = rnd.nextInt(ids.length);
        addSkill(ids[idx]);
    }
    
    public boolean isSkill(String id)
    {
        return SKILLS.contains(id);
    }
    
    public List<String> getSkillList()
    {
        List<String> skills = new ArrayList<>();
        for (String skill : mSkills.keySet())
            if (isSkill(skill))
                skills.add(skill);
        Collections.sort(skills);
        return skills;
    }
    
    public boolean rollFor(String spec, Random rnd)
    {
        int roll = DiceLogic.D2(rnd);
        return rollFor(spec, roll);
    }
    
    public boolean rollFor(String spec, int roll)
    {
        int o = spec.indexOf('=');
        if (o > 0)
        {
            CharStatBean stat = getStat(spec.substring(0, o));
            return stat.getValue() >= IntegerUtils.parseInt(spec.substring(o + 1));            
        }
        o = spec.indexOf(' ');
        if (o >= 0)
        {
            CharStatBean stat = getStat(spec.substring(0, o));
            spec = spec.substring(o + 1);
            roll += stat.getMod();
        }
        if (spec.endsWith("+"))
            return roll >= IntegerUtils.parseInt(spec.substring(0, spec.length() - 1));
        else if (spec.endsWith("-"))
            return roll <= IntegerUtils.parseInt(spec.substring(0, spec.length() - 1));
        return roll >= IntegerUtils.parseInt(spec);
    }
    
    public CharStatBean getStat(String id)
    {
        switch (id)
        {
            case STAT_STR:
                return mSTR;
            case STAT_DEX:
                return mDEX;
            case STAT_END:
                return mEND;
            case STAT_EDU:
                return mEDU;
            case STAT_INT:
                return mINT;
            case STAT_SOC:
                return mSOC;
        }
        throw new IllegalStateException("Unknown stat '"+id+"'");
    }
    
    public AudioMessageBean getUPP()
    {
        return new AudioMessageBean(AudioMessageBean.GROUP,
                mSTR.getHex(),
                mDEX.getHex(),
                mEND.getHex(),
                mINT.getHex(),
                mEDU.getHex(),
                mSOC.getHex()
                );
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

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public CharStatBean getSTR()
    {
        return mSTR;
    }

    public void setSTR(CharStatBean sTR)
    {
        mSTR = sTR;
    }

    public CharStatBean getDEX()
    {
        return mDEX;
    }

    public void setDEX(CharStatBean dEX)
    {
        mDEX = dEX;
    }

    public CharStatBean getEND()
    {
        return mEND;
    }

    public void setEND(CharStatBean eND)
    {
        mEND = eND;
    }

    public CharStatBean getINT()
    {
        return mINT;
    }

    public void setINT(CharStatBean iNT)
    {
        mINT = iNT;
    }

    public CharStatBean getEDU()
    {
        return mEDU;
    }

    public void setEDU(CharStatBean eDU)
    {
        mEDU = eDU;
    }

    public CharStatBean getSOC()
    {
        return mSOC;
    }

    public void setSOC(CharStatBean sOC)
    {
        mSOC = sOC;
    }

    public int getMoney()
    {
        return mMoney;
    }

    public void setMoney(int money)
    {
        mMoney = money;
    }

    public int getHomeworldType()
    {
        return mHomeworldType;
    }

    public void setHomeworldType(int homeworldType)
    {
        mHomeworldType = homeworldType;
    }

    public Map<String, Integer> getSkills()
    {
        return mSkills;
    }

    public void setSkills(Map<String, Integer> skills)
    {
        mSkills = skills;
    }

    public int getAge()
    {
        return mAge;
    }

    public void setAge(int age)
    {
        mAge = age;
    }

    public int getRank()
    {
        return mRank;
    }

    public void setRank(int rank)
    {
        mRank = rank;
    }

    public String getCareer()
    {
        return mCareer;
    }

    public void setCareer(String career)
    {
        mCareer = career;
    }

    public int getTerms()
    {
        return mTerms;
    }

    public void setTerms(int terms)
    {
        mTerms = terms;
    }
}
