package jo.cephus.shipyard.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jo.cephus.core.data.ShipDesignBean;
import jo.cephus.core.data.ShipReportBean;
import jo.util.beans.PCSBean;

public class RuntimeBean extends PCSBean
{
    private File                    mFile;
    private List<ShipDesignBean>    mShips = new ArrayList<>();
    private ShipDesignBean          mShip;
    private ShipReportBean          mReport;
    private String                  mStatus;
    private boolean                 mAnyChanges;
    
    public File getFile()
    {
        return mFile;
    }
    public void setFile(File file)
    {
        queuePropertyChange("file", mFile, file);
        mFile = file;
        firePropertyChange();
    }
    public List<ShipDesignBean> getShips()
    {
        return mShips;
    }
    public void setShips(List<ShipDesignBean> ships)
    {
        queuePropertyChange("ships", mShips, ships);
        mShips = ships;
        firePropertyChange();
    }
    public ShipDesignBean getShip()
    {
        return mShip;
    }
    public void setShip(ShipDesignBean ship)
    {
        queuePropertyChange("ship", mShip, ship);
        mShip = ship;
        firePropertyChange();
    }
    public ShipReportBean getReport()
    {
        return mReport;
    }
    public void setReport(ShipReportBean report)
    {
        queuePropertyChange("report", mReport, report);
        mReport = report;
        firePropertyChange();
    }
    public String getStatus()
    {
        return mStatus;
    }
    public void setStatus(String status)
    {
        queuePropertyChange("status", mStatus, status);
        mStatus = status;
        firePropertyChange();
    }
    public boolean isAnyChanges()
    {
        return mAnyChanges;
    }
    public void setAnyChanges(boolean anyChanges)
    {
        queuePropertyChange("anyChanges", mAnyChanges, anyChanges);
        mAnyChanges = anyChanges;
        firePropertyChange();
    }
}
