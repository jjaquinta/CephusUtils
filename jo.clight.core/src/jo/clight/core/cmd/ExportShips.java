package jo.clight.core.cmd;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.audio.util.JSONUtils;
import jo.clight.core.data.ShipComponentBean;
import jo.clight.core.data.ShipComponentInstanceBean;
import jo.clight.core.data.ShipDesignBean;
import jo.clight.core.data.ShipReportBean;
import jo.clight.core.data.plan.PlanItem;
import jo.clight.core.data.plan.ShipPlanBean;
import jo.clight.core.logic.ShipDesignLogic;
import jo.clight.core.logic.ShipReportLogic;
import jo.clight.core.logic.plan.ShipPlanHTMLLogic;
import jo.clight.core.logic.plan.ShipPlanLogic;
import jo.clight.core.logic.plan.ShipPlanTextLogic;
import jo.clight.core.logic.text.TextLogic;
import jo.util.logic.CSVLogic;
import jo.util.utils.io.FileUtils;
import jo.util.utils.io.StreamUtils;

public class ExportShips
{
    private String[] mArgs;
    private File     mShipyardFile;
    
    public ExportShips(String[] argv)
    {
        mArgs = argv;
    }
    
    public void run()
    {
        parseArgs();
        try
        {
            JSONObject json = JSONUtils.readJSON(mShipyardFile);
            JSONArray ships = (JSONArray)json.get("ships");
            String yardName = mShipyardFile.getName();
            yardName = yardName.substring(0, yardName.length() - 5);
            File zipFile = new File(mShipyardFile.getParentFile(), yardName+".zip");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            zos.putNextEntry(new ZipEntry(mShipyardFile.getName()));
            FileInputStream fis = new FileInputStream(mShipyardFile);
            StreamUtils.copy(fis, zos);
            fis.close();
            for (int i = 0; i < ships.size(); i++)
            {
                ShipDesignBean ship = new ShipDesignBean((JSONObject)ships.get(i));
                ShipReportBean report = ShipReportLogic.report(ship);
                String fname = toFileName(ship, report);
                System.out.println(fname);
                exportText(report, fname, zos);
                exportTable(ship, fname, zos);
                exportPlans(ship, fname, zos);
            }
            zos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private void exportPlans(ShipDesignBean ship, String fname, ZipOutputStream zos)
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
            File txtFile = new File(mShipyardFile.getParent(), fname+"_plan.txt");
            FileUtils.writeFile(text, txtFile);
            zos.putNextEntry(new ZipEntry(fname+"_plan.txt"));
            zos.write(text.getBytes());
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
            File htmlFile = new File(mShipyardFile.getParent(), fname+".html");
            FileUtils.writeFile(html, htmlFile);
            zos.putNextEntry(new ZipEntry(fname+".html"));
            zos.write(html.getBytes());
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void exportTable(ShipDesignBean ship, String fname, ZipOutputStream zos)
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
        
        File csvFile = new File(mShipyardFile.getParent(), fname+".csv");
        FileUtils.writeFile(table, csvFile);
        zos.putNextEntry(new ZipEntry(fname+".csv"));
        zos.write(table);
    }

    private void exportText(ShipReportBean report, String fname, ZipOutputStream zos)
            throws IOException
    {
        File textFile = new File(mShipyardFile.getParent(), fname+".txt");
        String text = TextLogic.getString(report.getProse());
        FileUtils.writeFile(text, textFile);
        zos.putNextEntry(new ZipEntry(fname+".txt"));
        StreamUtils.copy(new ByteArrayInputStream(text.getBytes()), zos);
    }
    
    private String toFileName(ShipDesignBean ship, ShipReportBean report)
    {
        String fname = "TL"+report.getTechLevel()+"_"+ship.getShipName().replace(" ", "_");
        return fname;
    }
    
    private void parseArgs()
    {
        for (int i = 0; i < mArgs.length; i++)
            if ("-o".equals(mArgs[i]))
                mShipyardFile = new File(mArgs[++i]);
        if (mShipyardFile == null)
        {
            System.out.println("No -o <shipyard.json> given.");
            System.exit(0);
        }
    }
    
    public static void main(String[] argv)
    {
        ExportShips app = new ExportShips(argv);
        app.run();
    }
}
