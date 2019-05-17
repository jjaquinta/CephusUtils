package jo.clight.core.logic;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.data.plan.PlanItem;
import jo.clight.core.data.plan.ShipPlanBean;
import jo.clight.core.logic.plan.ShipPlanHTMLLogic;
import jo.clight.core.logic.plan.ShipPlanLogic;
import jo.clight.core.logic.plan.ShipPlanTextLogic;
import jo.clight.core.logic.text.TextLogic;
import jo.util.logic.CSVLogic;
import jo.util.utils.io.FileUtils;

public class ShipExportLogic
{
    
    public static void exportPlans(ShipDesignBean ship, String fname, File dir, ZipOutputStream zos)
            throws IOException
    {
        List<PlanItem> items = new ArrayList<PlanItem>();
        for (ShipComponentInstanceBean inst : ship.getComponents())
        {
            String type = inst.getType();
            if (ShipComponentBean.HULL.equals(type))
                continue;
            if (ShipComponentBean.ETC.equals(type))
                type = "$"+inst.getComponentID();
            items.add(new PlanItem(type, inst.getVolume()*13.5, inst.getCount(), TextLogic.getString(inst.getComponent().getName())));
        }
        int disp = ShipDesignLogic.getHullDisplacement(ship);
        ShipPlanBean shipPlan = ShipPlanLogic.generateFrame(disp*13.5, ShipPlanBean.HULL_BOX, items);
        String text = ShipPlanTextLogic.printShipText(shipPlan);
        text = text.replace("\n", "\r\n");
        System.out.println(text.substring(0, Math.min(text.length(), 4096)));
        try
        {
            if (dir != null)
            {
                File txtFile = new File(dir, fname+"_plan.txt");
                FileUtils.writeFile(text, txtFile);
            }
            if (zos != null)
            {
                zos.putNextEntry(new ZipEntry(fname+"_plan.txt"));
                zos.write(text.getBytes());
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String html = ShipPlanHTMLLogic.printShipHTML(shipPlan);
        html = html.replace("\n", "\r\n");
        try
        {
            if (dir != null)
            {
                File htmlFile = new File(dir, fname+"_plan.html");
                FileUtils.writeFile(html, htmlFile);
            }
            if (zos != null)
            {
                zos.putNextEntry(new ZipEntry(fname+"_plan.html"));
                zos.write(html.getBytes());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public static void exportSheets(ShipReportBean report, String fname, File dir, ZipOutputStream zos)
            throws IOException
    {
        exportShipSheet(report, fname, dir, zos);
        exportDesignSheet(report, fname, dir, zos);
    }

    public static void exportShipSheet(ShipReportBean report, String fname, File dir, ZipOutputStream zos)
            throws IOException
    {
        List<List<Object>> shipSheet = ShipTableLogic.toShipSheet(report);
        String text = ShipTableLogic.formatTextTable(shipSheet, "20,20,20,20");
        String html = "<html>\r\n<body>\r\n>"+ShipTableLogic.formatHTMLTable(shipSheet)+"</body>\r\n</html>\r\n";
        if (dir != null)
        {
            File txtFile = new File(dir, fname+"_sheet_ship.txt");
            FileUtils.writeFile(text, txtFile);
            File htmlFile = new File(dir, fname+"_sheet_ship.html");
            FileUtils.writeFile(html, htmlFile);
        }
        if (zos != null)
        {
            zos.putNextEntry(new ZipEntry(fname+"_sheet_ship.txt"));
            zos.write(text.getBytes());
            zos.putNextEntry(new ZipEntry(fname+"_sheet_ship.html"));
            zos.write(html.getBytes());
        }
    }

    public static void exportDesignSheet(ShipReportBean report, String fname, File dir, ZipOutputStream zos)
            throws IOException
    {
        List<List<Object>> designSheet = ShipTableLogic.toDesignSheet(report.getShip());
        String text = ShipTableLogic.formatTextTable(designSheet, "0,40|wrap,0|right,0.2,0.2");
        String html = "<html>\r\n<body>\r\n>"+ShipTableLogic.formatHTMLTable(designSheet)+"</body>\r\n</html>\r\n";
        if (dir != null)
        {
            File txtFile = new File(dir, fname+"_sheet_design.txt");
            FileUtils.writeFile(text, txtFile);
            File htmlFile = new File(dir, fname+"_sheet_design.html");
            FileUtils.writeFile(html, htmlFile);
        }
        if (zos != null)
        {
            zos.putNextEntry(new ZipEntry(fname+"_sheet_design.txt"));
            zos.write(text.getBytes());
            zos.putNextEntry(new ZipEntry(fname+"_sheet_design.html"));
            zos.write(html.getBytes());
        }
    }

    public static void exportTable(ShipDesignBean ship, String fname, File dir, ZipOutputStream zos)
            throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(baos));
        out.write(CSVLogic.toCSVHeader(new String[] { "Name", "Type", "Count", "Price", "Volume" }));
        out.newLine();
        for (ShipComponentInstanceBean comp : ship.getComponents())
        {
            List<Object> outbuf = new ArrayList<>();
            outbuf.add(comp.getComponentID());
            outbuf.add(comp.getType());
            outbuf.add(comp.getCount());
            outbuf.add(comp.getPrice());
            outbuf.add(comp.getVolume());
            out.write(CSVLogic.toCSVLine(outbuf));
            out.newLine();
        }
        out.close();
        byte[] table = baos.toByteArray();
        
        if (dir != null)
        {
            File csvFile = new File(dir, fname+".csv");
            FileUtils.writeFile(table, csvFile);
        }
        if (zos != null)
        {
            zos.putNextEntry(new ZipEntry(fname+".csv"));
            zos.write(table);
        }
    }
    
    public static String toFileName(ShipDesignBean ship, ShipReportBean report)
    {
        String fname = "TL"+report.getTechLevel()+"_"+ship.getShipName().replace(" ", "_");
        return fname;
    }

}
