package jo.ttg.ship.logic.plan;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jo.cephus.core.data.ShipComponentBean;
import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.util.geom3d.Point3i;
import jo.util.geom3d.SparseMatrix;
import jo.util.utils.io.FileUtils;

public class ShipPlanTextLogic
{
    public static String printShipText(ShipPlanBean ship)
    {
        StringBuffer sb = new StringBuffer();
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        ship.getSquares().getBounds(lower, upper);
        for (int z = lower.z; z <= upper.z; z++)
        {
            SparseMatrix<ShipSquareBean> level = new SparseMatrix<ShipSquareBean>();
            for (int y = lower.y; y <= upper.y; y++)
                for (int x = lower.x; x <= upper.x; x++)
                {
                    ShipSquareBean sq = ship.getSquare(x, y, z);
                    if (sq != null)
                        level.set(x, y, z, sq);                       
                }
            printLevelText(sb, level, z);
        }
        return sb.toString();
    }
    
    private static void printLevelText(StringBuffer sb, SparseMatrix<ShipSquareBean> level, int z)
    {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        level.getBounds(lower, upper);
        int width = (upper.x - lower.x + 1)*3/2;
        int length = (upper.y - lower.y + 1)*3/2;
        sb.append("LEVEL "+z+"\n");
        if (width >= length)
        {
            sb.append("Size: "+width+"m x "+length+"m\n");
            sb.append("<--Port     Starboard-->\n");
            sb.append("\n");
            for (int y = lower.y; y <= upper.y; y++)
            {
                for (int x = lower.x; x <= upper.x; x++)
                {
                    ShipSquareBean square = level.get(x, y, z);
                    sb.append(squareToChar(square));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        else
        {
            sb.append("Size: "+length+"m x "+width+"m\n");
            sb.append("<--Fore       Aft-->\n");
            sb.append("\n");
            for (int x = lower.x; x <= upper.x; x++)
            {
                for (int y = lower.y; y <= upper.y; y++)
                {
                    ShipSquareBean square = level.get(x, y, z);
                    sb.append(squareToChar(square));
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
    }
    
    private static char squareToChar(ShipSquareBean square)
    {
        if (square == null)
            return ' ';
        if (square.getType() == null)
            return '.';
        switch (square.getType())
        {
            case ShipComponentBean.HULL:
                return '#';
            case ShipComponentBean.MDRIVE:
                return 'M';
            case ShipComponentBean.JDRIVE:
                return 'J';
            case ShipComponentBean.PPLANT:
                return 'P';
            case ShipComponentBean.FUEL:
                return '~';
            case ShipComponentBean.TURRET:
                return 't';
            case ShipComponentBean.HANGER:
                return 'H';
            case "$cargo_hold":
                return ' ';
        }
        return '?';
    }
    
    public static void main(String[] argv)
    {
        List<PlanItem> items = new ArrayList<PlanItem>();
        items.add(new PlanItem(ShipComponentBean.PPLANT, 1987*6.75, 1, "Main Power"));
        items.add(new PlanItem(ShipComponentBean.PPLANT, 622*6.75, 1, "Aux Power"));
        items.add(new PlanItem(ShipComponentBean.JDRIVE, 10000*6.75));
        items.add(new PlanItem(ShipComponentBean.MDRIVE, 4000*6.75));
        items.add(new PlanItem(ShipComponentBean.TURRET, 13.5, 300, "Missile Turret"));
        items.add(new PlanItem(ShipComponentBean.TURRET, 13.5, 300, "Laser Turret"));
        items.add(new PlanItem(ShipComponentBean.TURRET, 13.5, 300, "Sandcaster Turret"));
        items.add(new PlanItem("$cargo_hold", 26390*13.5));
        items.add(new PlanItem(ShipComponentBean.FUEL, 31008*13.5));
        items.add(new PlanItem(ShipComponentBean.HANGER, 30*13.5*1.1, 300, "Fighter Bay"));
        items.add(new PlanItem(ShipComponentBean.HANGER, 100*13.5*1.1, 10, "Tender Bay"));
        ShipPlanBean ship = ShipPlanLogic.generateFrame(1350000, ShipPlanBean.HULL_CYLINDER, items);
        String text = printShipText(ship);
        text = text.replace("\n", "\r\n");
        System.out.println(text.substring(0, 4096));
        try
        {
            FileUtils.writeFile(text, new File("c:\\temp\\ship\\test.txt"));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String html = ShipPlanHTMLLogic.printShipHTML(ship);
        html = html.replace("\n", "\r\n");
        try
        {
            FileUtils.writeFile(html, new File("c:\\temp\\ship\\test.htm"));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
