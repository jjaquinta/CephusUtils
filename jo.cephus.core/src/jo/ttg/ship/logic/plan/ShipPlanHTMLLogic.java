package jo.ttg.ship.logic.plan;

import jo.cephus.core.data.ShipComponentBean;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.util.geom3d.Point3i;
import jo.util.geom3d.SparseMatrix;

public class ShipPlanHTMLLogic
{
    public static String printShipHTML(ShipPlanBean ship)
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
        sb.append("<h2>LEVEL "+z+"</h2>\n");
        int firstLow, firstHigh;
        int secondLow, secondHigh;
        if (width >= length)
        {
            sb.append("Size: "+width+"m x "+length+"m<br/>\n");
            sb.append("&lt;--Port      Starboard--&gt;<br/>\n");
            firstLow = lower.y; firstHigh = upper.y;                
            secondLow = lower.x; secondHigh = upper.x;                
        }
        else
        {
            sb.append("Size: "+length+"m x "+width+"m<br/>\n");
            sb.append("<--Fore            Aft-->\n");
            firstLow = lower.x; firstHigh = upper.x;                
            secondLow = lower.y; secondHigh = upper.y;                
        }
        sb.append("\n");
        sb.append("<table cellpadding=\"0\" cellspacing=\"0\">\n");
        for (int first = firstLow; first <= firstHigh; first++)
        {
            sb.append(" <tr>\n");
            for (int second = secondLow; second <= secondHigh; second++)
            {
                sb.append("  <td>");
                ShipSquareBean square;
                if (width >= length)
                    square = level.get(second, first, z);
                else
                    square = level.get(first, second, z);
                String image = squareToImage(square);
                if (image != null)
                {
                    String title = squareToTitle(square);
                    sb.append("<img src=\"");
                    sb.append(image);
                    sb.append("\"");
                    if (title != null)
                    {
                        sb.append(" title=\"");
                        sb.append(title);
                        sb.append("\"");
                    }
                    sb.append("/>");
                }
                sb.append("</td>\n");
            }
            sb.append(" </tr>\n");
        }
        sb.append("</table>\n");
        sb.append("\n");
    }
    
    private static String squareToImage(ShipSquareBean square)
    {
        if (square == null)
            return null;
        if (square.getType() == null)
            return "unset.png";
        switch (square.getType())
        {
            case ShipComponentBean.HULL:
                return "hull.png";
            case ShipComponentBean.MDRIVE:
                return "maneuver.png";
            case ShipComponentBean.JDRIVE:
                return "jump.png";
            case ShipComponentBean.PPLANT:
                return "power.png";
            case ShipComponentBean.FUEL:
                return "fuel.png";
            case ShipComponentBean.TURRET:
                return "turret.png";
            case ShipComponentBean.HANGER:
                return "hanger.png";
            case "$cargo_hold":
                return "cargo.png";
        }
        return null;
    }
    
    private static String squareToTitle(ShipSquareBean square)
    {
        if (square == null)
            return null;
        if (square.getNotes() != null)
            return square.getNotes();
        if (square.getType() == null)
            return null;
        switch (square.getType())
        {
            case ShipComponentBean.HULL:
                return "Hull";
            case ShipComponentBean.MDRIVE:
                return "Maneuver Drive";
            case ShipComponentBean.JDRIVE:
                return "Jump Drive";
            case ShipComponentBean.PPLANT:
                return "Power Plan";
            case ShipComponentBean.FUEL:
                return "Fuel Tankage";
            case ShipComponentBean.TURRET:
                return "Turret";
            case ShipComponentBean.HANGER:
                return "Hanger";
            case "$cargo_hold":
                return "Cargo Bay";
        }
        return null;
    }
}
