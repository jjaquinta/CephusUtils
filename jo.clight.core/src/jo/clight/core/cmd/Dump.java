package jo.clight.core.cmd;

import java.util.ArrayList;
import java.util.List;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.data.plan.PlanItem;
import jo.clight.core.data.plan.ShipPlanBean;
import jo.clight.core.logic.ShipComponentLogic;
import jo.clight.core.logic.ShipLibraryLogic;
import jo.clight.core.logic.ShipReportLogic;
import jo.clight.core.logic.ShipTableLogic;
import jo.clight.core.logic.plan.ShipPlanLogic;
import jo.clight.core.logic.text.TextLogic;

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
            System.out.println("public static final String "+type.toUpperCase()+" = \""+type+"\";");
            for(ShipComponentBean comp : comps)
            {
                String name = TextLogic.getString(comp.getName());
                if(name == null)
                    System.out.println((new StringBuilder("    No text for ")).append(comp.getName().toJSON()).toString());
                String desc = TextLogic.getString(comp.getDescription());
                if(desc == null)
                    System.out.println((new StringBuilder("    No text for ")).append(comp.getDescription().toJSON()).toString());
                String ident = comp.getID();
                if (ident.toUpperCase().startsWith(type.toUpperCase()))
                {
                    ident = ident.substring(type.length());
                    if (ident.startsWith("_"))
                        ident = ident.substring(1);
                }
                System.out.println("public static final String "+comp.getType().toUpperCase()+"_"+ident.toUpperCase()+" = \""+comp.getID()+"\";");
            }
        }
        // ship build
        ShipDesignBean ship = ShipLibraryLogic.constructDesignExample();
        ShipReportBean report = ShipReportLogic.report(ship);
        for (String e : report.getAllErrors())
            System.out.println("  "+e);
        List<List<Object>> designSheet = ShipTableLogic.toDesignSheet(ship);
        List<List<Object>> shipSheet = ShipTableLogic.toShipSheet(report);
        System.out.println(ShipTableLogic.formatTable(designSheet, "0,40|wrap,0|right,0.2,0.2"));
        System.out.println(ShipTableLogic.formatTable(shipSheet, "20,20,20,20"));
        /*
        String prose = TextLogic.getString(report.getProse());
        prose = StringUtils.wrap(prose, 80, "\r\n");
        System.out.println(prose);
        */
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

}
