package jo.cephus.core.cmd;

import java.util.List;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.logic.ShipComponentLogic;
import jo.cephus.core.logic.text.TextLogic;

public class Dump
{

    public Dump()
    {
    }

    public static void main(String argv[])
    {
        System.out.println("Ship Components");
        System.out.println((new StringBuilder(String.valueOf(ShipComponentLogic.getTypes().size()))).append(" types").toString());
        for(String type : ShipComponentLogic.getTypes())
        {
            List<ShipComponentBean> comps = ShipComponentLogic.getComponentsByType(type);
            System.out.println((new StringBuilder("  ")).append(type).append(" x ").append(comps.size()).toString());
            for(ShipComponentBean comp : comps)
            {
                String name = TextLogic.getString(comp.getName());
                if(name == null)
                    System.out.println((new StringBuilder("    No text for ")).append(comp.getName().toJSON()).toString());
                String desc = TextLogic.getString(comp.getDescription());
                if(desc == null)
                    System.out.println((new StringBuilder("    No text for ")).append(comp.getDescription().toJSON()).toString());
            }

        }

    }
}
