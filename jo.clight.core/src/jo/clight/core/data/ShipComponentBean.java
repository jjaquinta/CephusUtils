package jo.clight.core.data;

import jo.audio.util.*;
import jo.audio.util.model.data.AudioMessageBean;
import jo.clight.core.logic.text.TextLogic;

import org.json.simple.JSONObject;

// Referenced classes of package jo.cephus.core.data:
//            CephusBean

public class ShipComponentBean extends CephusBean
    implements IJSONAble
{
    public static final String NULL_ITEM = "none";
    // generated IDS
    public static final String ARMOR = "ARMOR";
    public static final String ARMOR_TITANIUM = "armorTitanium";
    public static final String ARMOR_CRYSTALIRON = "armorCrystaliron";
    public static final String ARMOR_SUPERDENSE = "armorSuperdense";
    public static final String ARMOR_BONDEDSUPERDENSE = "armorBondedSuperdense";
    public static final String ARMOR_STEALTH = "armorStealth";
    public static final String BAY = "BAY";
    public static final String BAY_MISSILE_BANK = "missile_bank";
    public static final String BAY_PARTICLE_BAY = "particle_bay";
    public static final String BAY_MESON_GUN = "meson_gun";
    public static final String BAY_FUSION_GUN = "fusion_gun";
    public static final String BAY_GRAVITIC_LANCE = "gravitic_lance";
    public static final String BERTH = "BERTH";
    public static final String BERTH_LOWBERTH = "lowberth";
    public static final String BERTH_EMERGENCY_LOWBERTH = "emergency_lowberth";
    public static final String BRIDGE = "BRIDGE";
    public static final String BRIDGE_10 = "bridge10";
    public static final String BRIDGE_20 = "bridge20";
    public static final String BRIDGE_40 = "bridge40";
    public static final String BRIDGE_60 = "bridge60";
    public static final String COMPUTER = "COMPUTER";
    public static final String COMPUTER_0 = "computer0";
    public static final String COMPUTER_1 = "computer1";
    public static final String COMPUTER_2 = "computer2";
    public static final String COMPUTER_3 = "computer3";
    public static final String COMPUTER_4 = "computer4";
    public static final String COMPUTER_5 = "computer5";
    public static final String COMPUTER_6 = "computer6";
    public static final String COMPUTER_7 = "computer7";
    public static final String COMPUTER_BIS = "computer_bis";
    public static final String CONFIG = "CONFIG";
    public static final String CONFIG_DISTRIBUTED = "configDistributed";
    public static final String CONFIG_STANDARD = "configStandard";
    public static final String CONFIG_STREAMLINED = "configStreamlined";
    public static final String CREW = "CREW";
    public static final String CREW_CAPTAIN = "crew_captain";
    public static final String CREW_SENSOR = "crew_sensor";
    public static final String CREW_MEDIC = "crew_medic";
    public static final String CREW_ENGINEER = "crew_engineer";
    public static final String CREW_PURSER = "crew_purser";
    public static final String CREW_GUNNER = "crew_gunner";
    public static final String CREW_SECURITY = "crew_security";
    public static final String CREW_STEWARD = "crew_steward";
    public static final String CREW_PILOT = "crew_pilot";
    public static final String ETC = "ETC";
    public static final String ETC_ARMORY = "armory";
    public static final String ETC_CARGO_HOLD = "cargo_hold";
    public static final String ETC_FUEL_SCOOPS = "fuel_scoops";
    public static final String ETC_FUEL_PROCESSOR = "fuel_processor";
    public static final String ETC_LAB = "lab";
    public static final String ETC_MEDLAB = "medlab";
    public static final String FUEL = "FUEL";
    public static final String FUEL_ = "fuel";
    public static final String HANGER = "HANGER";
    public static final String HANGER_ATV = "hanger_atv";
    public static final String HANGER_RAFT = "hanger_raft";
    public static final String HANGER_DROPSHIP = "hanger_dropship";
    public static final String HANGER_ESCAPE = "hanger_escape";
    public static final String HANGER_FIGHTER = "hanger_fighter";
    public static final String HANGER_GIG = "hanger_gig";
    public static final String HANGER_DRONE_MINING = "hanger_drone_mining";
    public static final String HANGER_LAUNCH = "hanger_launch";
    public static final String HANGER_PINNACE = "hanger_pinnace";
    public static final String HANGER_DRONE_PROBE = "hanger_drone_probe";
    public static final String HANGER_DRONE_REPAIR = "hanger_drone_repair";
    public static final String HANGER_BOAT = "hanger_boat";
    public static final String HANGER_SHUTTLE = "hanger_shuttle";
    public static final String HULL = "HULL";
    public static final String HULL_100 = "hull100";
    public static final String HULL_200 = "hull200";
    public static final String HULL_300 = "hull300";
    public static final String HULL_400 = "hull400";
    public static final String HULL_500 = "hull500";
    public static final String HULL_600 = "hull600";
    public static final String HULL_700 = "hull700";
    public static final String HULL_800 = "hull800";
    public static final String HULL_900 = "hull900";
    public static final String HULL_1000 = "hull1000";
    public static final String HULL_1200 = "hull1200";
    public static final String HULL_1400 = "hull1400";
    public static final String HULL_1600 = "hull1600";
    public static final String HULL_1800 = "hull1800";
    public static final String HULL_2000 = "hull2000";
    public static final String HULL_3000 = "hull3000";
    public static final String HULL_4000 = "hull4000";
    public static final String HULL_5000 = "hull5000";
    public static final String JDRIVE = "JDRIVE";
    public static final String JDRIVE_A = "jdriveA";
    public static final String JDRIVE_B = "jdriveB";
    public static final String JDRIVE_C = "jdriveC";
    public static final String JDRIVE_D = "jdriveD";
    public static final String JDRIVE_E = "jdriveE";
    public static final String JDRIVE_F = "jdriveF";
    public static final String JDRIVE_G = "jdriveG";
    public static final String JDRIVE_H = "jdriveH";
    public static final String JDRIVE_J = "jdriveJ";
    public static final String JDRIVE_K = "jdriveK";
    public static final String JDRIVE_L = "jdriveL";
    public static final String JDRIVE_M = "jdriveM";
    public static final String JDRIVE_N = "jdriveN";
    public static final String JDRIVE_P = "jdriveP";
    public static final String JDRIVE_Q = "jdriveQ";
    public static final String JDRIVE_R = "jdriveR";
    public static final String JDRIVE_S = "jdriveS";
    public static final String JDRIVE_T = "jdriveT";
    public static final String JDRIVE_U = "jdriveU";
    public static final String JDRIVE_V = "jdriveV";
    public static final String JDRIVE_W = "jdriveW";
    public static final String JDRIVE_X = "jdriveX";
    public static final String JDRIVE_Y = "jdriveY";
    public static final String JDRIVE_Z = "jdriveZ";
    public static final String MDRIVE = "MDRIVE";
    public static final String MDRIVE_A = "mdriveA";
    public static final String MDRIVE_B = "mdriveB";
    public static final String MDRIVE_C = "mdriveC";
    public static final String MDRIVE_D = "mdriveD";
    public static final String MDRIVE_E = "mdriveE";
    public static final String MDRIVE_F = "mdriveF";
    public static final String MDRIVE_G = "mdriveG";
    public static final String MDRIVE_H = "mdriveH";
    public static final String MDRIVE_J = "mdriveJ";
    public static final String MDRIVE_K = "mdriveK";
    public static final String MDRIVE_L = "mdriveL";
    public static final String MDRIVE_M = "mdriveM";
    public static final String MDRIVE_N = "mdriveN";
    public static final String MDRIVE_P = "mdriveP";
    public static final String MDRIVE_Q = "mdriveQ";
    public static final String MDRIVE_R = "mdriveR";
    public static final String MDRIVE_S = "mdriveS";
    public static final String MDRIVE_T = "mdriveT";
    public static final String MDRIVE_U = "mdriveU";
    public static final String MDRIVE_V = "mdriveV";
    public static final String MDRIVE_W = "mdriveW";
    public static final String MDRIVE_X = "mdriveX";
    public static final String MDRIVE_Y = "mdriveY";
    public static final String MDRIVE_Z = "mdriveZ";
    public static final String NONE = "NONE";
    public static final String NONE_ = "none";
    public static final String PPLANT = "PPLANT";
    public static final String PPLANT_A = "pplantA";
    public static final String PPLANT_B = "pplantB";
    public static final String PPLANT_C = "pplantC";
    public static final String PPLANT_D = "pplantD";
    public static final String PPLANT_E = "pplantE";
    public static final String PPLANT_F = "pplantF";
    public static final String PPLANT_G = "pplantG";
    public static final String PPLANT_H = "pplantH";
    public static final String PPLANT_J = "pplantJ";
    public static final String PPLANT_K = "pplantK";
    public static final String PPLANT_L = "pplantL";
    public static final String PPLANT_M = "pplantM";
    public static final String PPLANT_N = "pplantN";
    public static final String PPLANT_P = "pplantP";
    public static final String PPLANT_Q = "pplantQ";
    public static final String PPLANT_R = "pplantR";
    public static final String PPLANT_S = "pplantS";
    public static final String PPLANT_T = "pplantT";
    public static final String PPLANT_U = "pplantU";
    public static final String PPLANT_V = "pplantV";
    public static final String PPLANT_W = "pplantW";
    public static final String PPLANT_X = "pplantX";
    public static final String PPLANT_Y = "pplantY";
    public static final String PPLANT_Z = "pplantZ";
    public static final String SCREENS = "SCREENS";
    public static final String SCREENS_MESON_SCREEN = "meson_screen";
    public static final String SCREENS_NUCLEAR_DAMPER = "nuclear_damper";
    public static final String STATEROOM = "STATEROOM";
    public static final String STATEROOM_ = "stateroom";
    public static final String TURRET = "TURRET";
    public static final String TURRET_SINGLE_TURRET = "single_turret";
    public static final String TURRET_DOUBLE_TURRET = "double_turret";
    public static final String TURRET_TRIPLE_TURRET = "triple_turret";
    public static final String TURRET_SINGLE_FIXED_TURRET = "single_fixed_turret";
    public static final String WEAPON = "WEAPON";
    public static final String WEAPON_MISSILE_RACK = "missile_rack";
    public static final String WEAPON_PULSE_LASER = "pulse_laser";
    public static final String WEAPON_SANDCASTER_RACK = "sandcaster_rack";
    public static final String WEAPON_PARTICLE_BEAM = "particle_beam";
    public static final String WEAPON_BEAM_LASER = "beam_laser";
    public static final String WEAPON_PLASMA_BEAM = "plasma_beam";
    public static final String WEAPON_FUSION_BEAM = "fusion_beam";
    
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
        return ((Number)get("price", Double.valueOf(mPrice))).doubleValue();
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
