package jo.clight.core.logic;

import java.util.Random;

public class DiceLogic
{
    public static int D(Random rnd)
    {
        return rnd.nextInt(6) + 1;
    }
    
    public static int D2(Random rnd)
    {
        return D(rnd) + D(rnd);
    }
}
