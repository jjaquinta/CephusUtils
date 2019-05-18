package jo.clight.core.cmd;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.audio.util.model.data.AudioMessageBean;
import jo.clight.core.data.CharCareerBean;
import jo.clight.core.data.CharPersonBean;
import jo.clight.core.logic.CharCareerLogic;
import jo.clight.core.logic.CharCreateLogic;
import jo.clight.core.logic.text.TextLogic;

public class DumpChars
{
    public static void main(String argv[])
    {
        System.out.println("Careers");
        for(String id : CharCareerLogic.getCareers())
        {
            CharCareerBean career = CharCareerLogic.getCareer(id);
            String name = TextLogic.getString(career.getName());
            if(name == null)
                System.out.println("    No text for "+career.getName().toJSON().toString());
            String desc = TextLogic.getString(career.getDescription());
            if(desc == null)
                System.out.println("    No text for "+career.getDescription().toJSON().toString());
            System.out.println(name+": "+desc);
            if ((career.getMusteringOutCash() == null) || (career.getMusteringOutCash().length != 7))
                System.out.println("    Bad mustering out cash");
            if ((career.getMusteringOutMaterials() == null) || (career.getMusteringOutMaterials().length != 7))
                System.out.println("    Bad mustering out Materials");
            if ((career.getRankBonus() == null) || (career.getRankBonus().length != 7))
                System.out.println("    Bad RankBonus");
            if ((career.getPersonalDevelopment() == null) || (career.getPersonalDevelopment().length != 6))
                System.out.println("    Bad PersonalDevelopment");
            if ((career.getService() == null) || (career.getService().length != 6))
                System.out.println("    Bad Service");
            if ((career.getSpecialist() == null) || (career.getSpecialist().length != 6))
                System.out.println("    Bad Specialist");
            if ((career.getAdvancedEducation() == null) || (career.getAdvancedEducation().length != 6))
                System.out.println("    Bad AdvancedEducation");
        }
        for(String id : CharCareerLogic.getCareers())
            System.out.println("public static final String CAREER_"+id+" = \""+id+"\";");
        System.out.println("public static final Set<String> CAREERS = new HashSet<>();");
        System.out.println("static");
        System.out.println("{");
        for(String id : CharCareerLogic.getCareers())
            System.out.println("\tCAREERS.add(CAREER_"+id+");");
        System.out.println("}");
        // char build
        Random rnd = new Random(0);
        for (int i = 0; i < 10; i++)
        {
            CharPersonBean pc = CharCreateLogic.create(rnd, null, 10);
            System.out.println("--------------------");
            System.out.println("Stats: "+TextLogic.getString(pc.getUPP())
                + ", Credits: "+pc.getMoney());
            CharCareerBean career = CharCareerLogic.getCareer(pc.getCareer());
            System.out.println("Career: "+TextLogic.getString(career.getName())
                +", Rank: "+TextLogic.getString(career.getRankDescription(pc.getRank()))+" (O"+(pc.getRank()+1)+")"
                +", Terms: "+pc.getTerms());
            System.out.println("Skills:");
            for (String skill : pc.getSkillList())
                System.out.println("  "+TextLogic.getString(new AudioMessageBean("SKILL_"+skill+"_NAME"))+": "+pc.getSkill(skill));
        }
        //Map<String,Map<String,Integer>> bestCareersForJuniorSkill = new HashMap<>();
        Map<String,Map<String,Integer>> bestCareersForEliteSkill = new HashMap<>();
        for (int i = 0; i < 1000; i++)
        {
            CharPersonBean pc = CharCreateLogic.create(rnd, null, 10);
            for (String skill : pc.getSkillList())
            {
                add(bestCareersForEliteSkill, pc.getCareer(), skill, pc.getSkill(skill));
                //add(bestCareersForJuniorSkill, pc.getCareer(), skill, 1);
            }
        }
        JSONObject bestCareers = new JSONObject();
        for (String skill : CharPersonBean.SKILLS)
        {
            bestCareers.put(skill, toJSON(bestCareersForEliteSkill.get(skill)));
        }
        System.out.println(bestCareers.toJSONString());
    }

    @SuppressWarnings("unchecked")
    private static JSONArray toJSON(
            Map<String, Integer> bestCareersForSkill)
    {
        JSONObject[] best = new JSONObject[bestCareersForSkill.size()];
        int o = 0;
        for (String career : bestCareersForSkill.keySet())
        {
            Integer val = bestCareersForSkill.get(career);
            best[o] = new JSONObject();
            best[o].put("career", career);
            best[o].put("value", val);
            o++;
        }
        Arrays.sort(best, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2)
            {
                int v1 = ((Number)o1.get("value")).intValue();
                int v2 = ((Number)o2.get("value")).intValue();
                return v2 - v1;
            }
        });
        JSONArray arr = new JSONArray();
        for (JSONObject obj : best)
            arr.add(obj);
        return arr;
    }

    private static void add(Map<String,Map<String,Integer>> bestCareersForSkill, String career, String skill, int val)
    {
        Map<String,Integer> careers = bestCareersForSkill.get(skill);
        if (careers == null)
        {
            careers = new HashMap<>();
            bestCareersForSkill.put(skill, careers);
        }
        if (careers.containsKey(career))
            careers.put(career, careers.get(career) + val);
        else
            careers.put(career, val);
    }
}
