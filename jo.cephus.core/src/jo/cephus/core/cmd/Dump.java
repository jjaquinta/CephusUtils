package jo.cephus.core.cmd;

import java.util.ArrayList;
import java.util.List;

import jo.cephus.core.data.ShipComponentBean;
import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.data.ShipReportBean;
import jo.cephus.core.logic.ShipComponentLogic;
import jo.cephus.core.logic.ShipReportLogic;
import jo.cephus.core.logic.text.TextLogic;
import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.logic.plan.ShipPlanLogic;
import jo.util.utils.obj.StringUtils;

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
        // ship build
        ShipDesignBean ship = constructCorvette();
        ShipReportBean report = ShipReportLogic.report(ship);
        for (String e : report.getErrors())
            System.out.println("  "+e);
        String prose = TextLogic.getString(report.getProse());
        prose = StringUtils.wrap(prose, 80, "\r\n");
        System.out.println(prose);
        // test residuals
        ArrayList<PlanItem> contents = new ArrayList<PlanItem>();
        for (int configuration = ShipPlanBean.HULL_SPHERE; configuration <= ShipPlanBean.HULL_BOX; configuration++)
        {
            int count = 0;
            double total = 0;
            for (ShipComponentBean hull : ShipComponentLogic.getComponentsByType(ShipComponentBean.HULL))
            {
                double volume = -hull.getVolume()*13.5;
                ShipPlanBean plan = ShipPlanLogic.generateFrame(volume, configuration, contents);
                double designVolume = plan.getSquares().size()*6.25;
                double pc = designVolume/volume;
                total += pc;
                count++;
            }
            int average = (int)(100*total/count);
            System.out.println("Config #"+configuration+", average = "+average+"%");
        }
    }

    public static ShipDesignBean constructAsteroidMiner()
    {
        ShipDesignBean ship = new ShipDesignBean();
        ship.setShipName("Asteroid Miner");
        ship.setShipFunction("frequently used to exploit the abundant riches found in planetoid belts");
        ship.getComponents().add(ShipComponentLogic.getInstance("hull200", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("jdriveA", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("mdriveA", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("pplantA", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("fuel", 44));
        ship.getComponents().add(ShipComponentLogic.getInstance("computer2", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("electronics_civilian", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("stateroom", 3));
        ship.getComponents().add(ShipComponentLogic.getInstance("lowberth", 5));
        ship.getComponents().add(ShipComponentLogic.getInstance("configStandard", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("armorTitanium", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("hanger_escape", 3));
        ship.getComponents().add(ShipComponentLogic.getInstance("fuel_processor", 3));
        return ship;
    }

    public static ShipDesignBean constructCorvette()
    {
        ShipDesignBean ship = new ShipDesignBean();
        ship.setShipName("Corvette");
        ship.setShipFunction("an example of a frigate commonly found in operation within an interstellar polity");
        ship.getComponents().add(ShipComponentLogic.getInstance("hull300", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("jdriveC", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("mdriveJ", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("pplantJ", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("fuel", 96));
        ship.getComponents().add(ShipComponentLogic.getInstance("computer3", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("computer_fib", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("electronics_advanced", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("stateroom", 5));
        ship.getComponents().add(ShipComponentLogic.getInstance("emergency_lowberth", 5));
        ship.getComponents().add(ShipComponentLogic.getInstance("triple_turret", 3));
        ship.getComponents().add(ShipComponentLogic.getInstance("missile_rack", 6));
        ship.getComponents().add(ShipComponentLogic.getInstance("beam_laser", 3));
        ship.getComponents().add(ShipComponentLogic.getInstance("configStandard", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("armorCrystaliron", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("armory", 1));
        ship.getComponents().add(ShipComponentLogic.getInstance("detention_cell", 4));
        ship.getComponents().add(ShipComponentLogic.getInstance("hanger_escape", 3));
        ship.getComponents().add(ShipComponentLogic.getInstance("fuel_processor", 5));
        return ship;
    }
}
