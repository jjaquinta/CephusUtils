package jo.clight.core.logic.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.plan.PlanItem;
import jo.clight.core.data.plan.ShipPlanBean;
import jo.clight.core.data.plan.ShipSquareBean;
import jo.util.geom3d.Point3i;
import jo.util.utils.MathUtils;
import jo.util.utils.obj.StringUtils;

public class ShipPlanLogic
{
    // volume in cubic meters
    public static ShipPlanBean generateFrame(double volume, int configuration, List<PlanItem> items)
    {
        ShipPlanBean plan = new ShipPlanBean();
        switch (configuration)
        {
            case ShipPlanBean.HULL_SPHERE:
                generateSphere(plan, volume);
                break;
            case ShipPlanBean.HULL_CYLINDER:
                generateCylinder(plan, volume);
                break;
            case ShipPlanBean.HULL_CONE:
                generateCone(plan, volume);
                break;
            case ShipPlanBean.HULL_BOX:
                generateBox(plan, volume);
                break;
        }
        plateShip(plan);
        //double actualVolume = plan.getSquares().size()*sqToMH(1)*sqToMH(1)*sqToMV(1);
        //double usedVolume = plateShip(plan)*sqToMH(1)*sqToMH(1)*sqToMV(1);
        //double residual = actualVolume - volume;
        //System.out.println("Target Volume="+volume+", availableVolume="+actualVolume+", usedVolume="+usedVolume+", Residual="+residual+", "+(residual*100/volume)+"%");
        allocate(ShipComponentBean.MDRIVE, null, items, plan, 
                new int[] { FillStrategy.Y, FillStrategy.X, FillStrategy.Z },
                new int[] { FillStrategy.PLUS, FillStrategy.CENTER, FillStrategy.CENTER});
        allocate(ShipComponentBean.JDRIVE, null, items, plan, 
                new int[] { FillStrategy.X, FillStrategy.Y, FillStrategy.Z },
                new int[] { FillStrategy.BOTHQ, FillStrategy.HIGHQ, FillStrategy.CENTER});
        allocate(ShipComponentBean.PPLANT, null, items, plan, 
                new int[] { FillStrategy.Y, FillStrategy.X, FillStrategy.Z },
                new int[] { FillStrategy.HIGHQ, FillStrategy.CENTER, FillStrategy.CENTER});
        allocate(ShipComponentBean.TURRET, ShipComponentBean.HULL, items, plan, 
                new int[] { FillStrategy.X, FillStrategy.Z, FillStrategy.Y },
                new int[] { FillStrategy.OUTSIDE, FillStrategy.OUTSIDE, FillStrategy.OUTSIDE});
        allocate(ShipComponentBean.HANGER, null, items, plan, 
                new int[] { FillStrategy.Z, FillStrategy.X, FillStrategy.Y },
                new int[] { FillStrategy.MINUS, FillStrategy.CENTER, FillStrategy.LOWQ });
        allocate("$cargo_hold", null, items, plan, 
                new int[] { FillStrategy.Z, FillStrategy.X, FillStrategy.Y },
                new int[] { FillStrategy.PLUS, FillStrategy.CENTER, FillStrategy.HIGHQ});
        allocate(ShipComponentBean.FUEL, null, items, plan, 
                new int[] { FillStrategy.X, FillStrategy.Z, FillStrategy.Y },
                new int[] { FillStrategy.OUTSIDE, FillStrategy.OUTSIDE, FillStrategy.OUTSIDE});
        return plan;
    }
    
    private static void allocate(String type, String freeType, List<PlanItem> items, ShipPlanBean plan,
            int[] axis, int[] strategy)
    {
        int compartment = 1;
        List<Point3i> freeSpace = null;
        for (PlanItem item : items)
            if (item.getType().equals(type))
            {
                if (freeSpace == null)
                {
                    freeSpace = getFreeSpace(plan, freeType);
                    Collections.sort(freeSpace, new FillStrategy(strategy, axis, plan));
                }
                int count = item.getNumber();
                if (count == 0)
                    count = 1;
                for (int i = 0; i < count; i++)
                {
                    if (!allocate(item.getVolume(), type, compartment++, item.getNotes(), plan, freeSpace, axis, strategy))
                        break;
                }
            }
    }
    
    private static boolean allocate(Double volume, String type, int compartment, String notes, ShipPlanBean plan,
            List<Point3i> freeSpace,
            int[] axis, int[] strategy)
    {
        if (volume == null)
            return true;
        int todo = (int)Math.ceil(volume/6.75);
        for (int i = 0; i < todo; i++)
        {
            if (freeSpace.size() == 0)
            {
                System.out.println("Out of space (need "+(todo - i)+" more) allocating #"+type+", compartment "+compartment+" notes="+notes);
                return false;
            }
            Point3i ords = freeSpace.get(0);
            freeSpace.remove(0);
            ShipSquareBean sq = plan.getSquare(ords);
            sq.setType(type);
            sq.setCompartment(compartment);
            sq.setNotes(notes);
        }
        return true;
    }

    private static List<Point3i> getFreeSpace(ShipPlanBean plan, String freeType)
    {
        List<Point3i> freeSpace = new ArrayList<Point3i>();        
        for (Iterator<Point3i> i = plan.getSquares().iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            if (StringUtils.equals(plan.getSquares().get(p).getType(), freeType))
                freeSpace.add(p);
        }
        return freeSpace;
    }

    private static int plateShip(ShipPlanBean ship)
    {
        int volume = 0;
        for (Iterator<Point3i> i = ship.getSquares().iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            if (isEdge(ship, p))
                ship.getSquare(p).setType(ShipComponentBean.HULL);
            volume++;
        }
        return volume;
    }

    private static boolean isEdge(ShipPlanBean ship, Point3i p)
    {
        if (ship.getSquare(p.x - 1, p.y, p.z) == null)
            return true;
        if (ship.getSquare(p.x + 1, p.y, p.z) == null)
            return true;
        if (ship.getSquare(p.x, p.y - 1, p.z) == null)
            return true;
        if (ship.getSquare(p.x, p.y + 1, p.z) == null)
            return true;
        return false;
    }

    // volume in cubic meters
    private static void generateSphere(ShipPlanBean plan, double volume)
    {
        double r = Math.pow(volume*3/4/Math.PI, .33333);
        int decks = (int)Math.ceil(mToSqV(r*2));
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        for (int z = ventral; z < dorsal; z++)
        {
            double elevation = sqToMV(z + .5);
            double deckRadius = Math.sqrt(r*r - elevation*elevation) + 1;
            int deckRadiusSquares = (int)Math.ceil(mToSqH(deckRadius));
            if (deckRadiusSquares <= 1)
                continue;
            for (int x = -deckRadiusSquares; x <= deckRadiusSquares; x++)
                for (int y = -deckRadiusSquares; y <= deckRadiusSquares; y++)
                {
                    double xm = sqToMH(x);
                    double ym = sqToMH(y);
                    double rm = Math.sqrt(xm*xm + ym*ym);
                    if (rm <= deckRadius)
                        plan.setSquare(x, y, z, new ShipSquareBean(null));
                }
        }
    }

    // volume in cubic meters
    private static void generateCylinder(ShipPlanBean plan, double volume)
    {
        double r = Math.pow(volume/8/Math.PI, .33333);
        double h = r*8;
        int decks = (int)Math.floor(mToSqV(r*2));
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        int length = (int)Math.floor(mToSqH(h));
        int fore = -length/2;
        int aft = fore + length;
        for (int z = ventral; z < dorsal; z++)
        {
            double elevation = sqToMV(z + .5);
            double deckRadius = Math.sqrt(r*r - elevation*elevation);
            int deckRadiusSquares = (int)Math.ceil(mToSqH(deckRadius) - .25);
            if (deckRadiusSquares <= 1)
                continue;
            for (int x = -deckRadiusSquares; x <= deckRadiusSquares; x++)
                for (int y = fore; y < aft; y++)
                    plan.setSquare(x, y, z, new ShipSquareBean(null));
        }
    }

    // volume in cubic meters
    private static void generateCone(ShipPlanBean plan, double volume)
    {
        // h = 3 time radius
        // v = PI*R^2*H/3
        // v = PI*R^2*(3R)/3
        // v = PI*R^3
        // cbrt(v/PI) = r
        double r = Math.pow(volume/Math.PI, .33333);
        double h = r*3;
        double slope = h/r;
        int decks = (int)Math.ceil(mToSqV(r*2));
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        int length = (int)Math.ceil(mToSqH(h));
        for (int z = ventral; z < dorsal; z++)
        {
            double elevation = sqToMV(z+.5);
            double deckRadius = Math.sqrt(r*r - elevation*elevation) + 10;
            int deckLengthSquares = (int)Math.ceil(MathUtils.interpolate(Math.abs(z), 0, dorsal, length, 0)) + 5;
            int deckRadiusSquares = (int)Math.ceil(mToSqH(deckRadius));
            if (deckRadiusSquares <= 5)
                continue;
            for (int x = -deckRadiusSquares; x <= deckRadiusSquares; x++)
            {
                int x0 = Math.abs(x);
                int ycut = (int)(x0*slope);
                for (int y = -deckLengthSquares; y < 0; y++)
                {
                    int y0 = y + deckLengthSquares;
                    if (y0 >= ycut)
                        plan.setSquare(x, y, z, new ShipSquareBean(null));
                }
            }
        }
    }

    // volume in cubic meters
    private static void generateBox(ShipPlanBean plan, double volume)
    {
        // proportions = 3x4x5
        // v = 3x * 4x * 5x
        // v = 3*4*5 * x^3
        // v = 60*x^3
        // cbrt(v/60) = r
        double r = Math.pow(volume/60, .33333);
        double height = r*4;
        double width = r*3;
        double length = r*5;
        int decks = (int)Math.ceil(mToSqV(height));
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        int lengthSquares = (int)mToSqH(length);
        int fore = -lengthSquares/2;
        int aft = fore + lengthSquares;
        int widthSquares = (int)mToSqH(width);
        int port = -widthSquares/2;
        int starboard = port + widthSquares;
        for (int z = ventral; z < dorsal; z++)
        {
            for (int x = port; x <= starboard; x++)
                for (int y = fore; y < aft; y++)
                    plan.setSquare(x, y, z, new ShipSquareBean(null));
        }
    }
    
    private static double mToSqH(double m)
    {
        return m/1.5;
    }
    
    private static double mToSqV(double m)
    {
        return m/3;
    }
    
    private static double sqToMH(double m)
    {
        return m*1.5;
    }
    
    private static double sqToMV(double m)
    {
        return m*3;
    }
}
