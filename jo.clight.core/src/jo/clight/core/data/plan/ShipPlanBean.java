package jo.clight.core.data.plan;

import java.util.HashMap;
import java.util.Map;

import jo.util.beans.PCSBean;
import jo.util.geom3d.Point3i;
import jo.util.geom3d.SparseMatrix;

public class ShipPlanBean extends PCSBean
{
    public static final int HULL_SPHERE = 0;
    public static final int HULL_CYLINDER = 1;
    public static final int HULL_CONE = 2;
    public static final int HULL_BOX = 3;
    
    private String                       mURI;
    private Map<Object, Object>          mMetadata;
    private SparseMatrix<ShipSquareBean> mSquares;

    public ShipPlanBean()
    {
        mMetadata = new HashMap<Object, Object>();
        mSquares = new SparseMatrix<ShipSquareBean>();
    }

    public ShipSquareBean getSquare(int x, int y, int z)
    {
        return mSquares.get(x, y, z);
    }

    public void setSquare(int x, int y, int z, ShipSquareBean square)
    {
        mSquares.set(x, y, z, square);
    }

    public ShipSquareBean getSquare(Point3i p)
    {
        return mSquares.get(p);
    }

    public void setSquare(Point3i p, ShipSquareBean square)
    {
        mSquares.set(p, square);
    }

    /**
     * @return Returns the uRI.
     */
    public String getURI()
    {
        return mURI;
    }

    /**
     * @param uri
     *            The uRI to set.
     */
    public void setURI(String uri)
    {
        queuePropertyChange("uri", mURI, uri);
        mURI = uri;
        firePropertyChange();
    }

    /**
     * @return Returns the metadata.
     */
    public Map<Object, Object> getMetadata()
    {
        return mMetadata;
    }

    /**
     * @param metadata
     *            The metadata to set.
     */
    public void setMetadata(Map<Object, Object> metadata)
    {
        mMetadata = metadata;
    }

    public SparseMatrix<ShipSquareBean> getSquares()
    {
        return mSquares;
    }

    public void setSquares(SparseMatrix<ShipSquareBean> squares)
    {
        mSquares = squares;
    }
}
