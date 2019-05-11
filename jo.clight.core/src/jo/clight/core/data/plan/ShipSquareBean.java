package jo.clight.core.data.plan;

public class ShipSquareBean
{
    private String mType;
    private int mCompartment;
    private String  mNotes;
    
    public ShipSquareBean(String type)
    {
        mType = type;
    }

    public String getType()
    {
        return mType;
    }

    public void setType(String type)
    {
        mType = type;
    }

    public int getCompartment()
    {
        return mCompartment;
    }

    public void setCompartment(int compartment)
    {
        mCompartment = compartment;
    }

    public String getNotes()
    {
        return mNotes;
    }

    public void setNotes(String notes)
    {
        mNotes = notes;
    }
    
}
