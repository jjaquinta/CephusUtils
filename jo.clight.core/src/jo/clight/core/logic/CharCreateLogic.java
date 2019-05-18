package jo.clight.core.logic;

import java.util.Random;

import jo.clight.core.data.CharCareerBean;
import jo.clight.core.data.CharPersonBean;

public class CharCreateLogic
{
    public static CharPersonBean create(Random rnd, String preferredCareer, int maxTerms)
    {
        CharPersonBean pc = new CharPersonBean();
        step1Characteristics(pc, rnd);
        step2Homeworld(pc, rnd);
        step3Career(pc, rnd, preferredCareer, maxTerms);
        return pc;
    }

    public static CharPersonBean createForSkill(Random rnd, String skill, int maxTerms)
    {
        String preferredCareer = CharCareerLogic.getBestCareer(skill, rnd);
        return create(rnd, preferredCareer, maxTerms);
    }
        
    public static void step1Characteristics(CharPersonBean pc, Random rnd)
    {
        pc.getSTR().setValue(DiceLogic.D2(rnd));
        pc.getEND().setValue(DiceLogic.D2(rnd));
        pc.getDEX().setValue(DiceLogic.D2(rnd));
        pc.getINT().setValue(DiceLogic.D2(rnd));
        pc.getEDU().setValue(DiceLogic.D2(rnd));
        pc.getSOC().setValue(DiceLogic.D2(rnd));
        pc.setAge(18);
    }
    
    public static void step2Homeworld(CharPersonBean pc, Random rnd)
    {
        pc.setHomeworldType(rnd.nextInt(CharPersonBean.HOMEWORLD_MAX));
        for (String buff : CharPersonBean.HOMEWORLD_SKILLS[pc.getHomeworldType()])
            pc.addSkill(buff);
    }
    
    public static void step3Career(CharPersonBean pc, Random rnd, String preferredCareer, int maxTerms)
    {
        // pick career
        CharCareerBean career;
        if (preferredCareer != null)
            career = CharCareerLogic.getCareer(preferredCareer);
        else
            career = CharCareerLogic.getCareer(rnd);
        if (!pc.rollFor(career.getQualification(), rnd))
            career = CharCareerLogic.getDraftCareer(rnd);
        pc.setCareer(career.getID());
        // progress career
        int terms = 0;
        pc.setRank(0);
        pc.addSkill(career.getRankBonus()[pc.getRank()]);
        for (;;)
        {
            terms++;
            if (!pc.rollFor(career.getSurvival(), rnd))
            {
                // you die
                break;
            }
            int skillRolls = 1;
            if (terms == 1)
                skillRolls++;
            // check for advancement
            if (pc.rollFor(career.getAdvancement(), rnd))
            {
                skillRolls++;
                if (pc.getRank() + 1 < career.getRankBonus().length)
                {
                    pc.setRank(pc.getRank() + 1);
                    pc.addSkill(career.getRankBonus()[pc.getRank()]);
                }
            }
            // add skills
            while (skillRolls-- > 0)
            {
                int table = (pc.getEDU().getValue() >= 8) ? rnd.nextInt(4) : rnd.nextInt(3);
                switch (table)
                {
                    case 0:
                        pc.addSkill(career.getPersonalDevelopment(), rnd);
                        break;
                    case 1:
                        pc.addSkill(career.getService(), rnd);
                        break;
                    case 2:
                        pc.addSkill(career.getSpecialist(), rnd);
                        break;
                    case 3:
                        pc.addSkill(career.getAdvancedEducation(), rnd);
                        break;
                }
            }
            // aging
            if (terms >= 4)
            {
                
            }
            // re-enlistment
            int roll = DiceLogic.D2(rnd);
            if (roll != 12)
            {
                if (terms >= 8)
                    break; // forced retirement 
                if (terms >= maxTerms)
                    break; // chosen retirement
                if (!pc.rollFor(career.getReEnlistment(), roll))
                    break; // can't re-enlist
            }
        }
        // muster out
        pc.setTerms(terms);
        int rolls = terms;
        if (pc.getRank() >= 6)
            rolls += 3;
        else if (pc.getRank() >= 5)
            rolls += 2;
        else if (pc.getRank() >= 4)
            rolls++;
        int mbDM = 0;
        if (pc.getRank() >= 5)
            mbDM++;
        int cashDM = 0;
        if (pc.getSkill(CharPersonBean.SKILL_CAROUSING) > 0)
            cashDM++;
        while (rolls-- > 0)
        {
            if (rnd.nextBoolean())
            {   // material benefits
                int roll = DiceLogic.D(rnd) + mbDM;
                pc.addSkill(career.getMusteringOutMaterials()[roll - 1]);
            }
            else
            {   // cash
                int roll = DiceLogic.D(rnd) + cashDM;
                int cash = career.getMusteringOutCash()[roll - 1];
                pc.setMoney(pc.getMoney() + cash);
            }
        }
    }
}
