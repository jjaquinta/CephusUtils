package jo.ttg.ship.beans.plan;

public class PlanItem
{
    private String mType;
    private int mNumber;
    private double mVolume; // in cubic meters
    private String  mNotes;
    
    public PlanItem()
    {    
        mNumber = 1;
    }
    
    public PlanItem(String type, double volume)
    {
        mType = type;
        mVolume = volume;
    }
    
    public PlanItem(String type, double volume, int number, String notes)
    {
        mType = type;
        mVolume = volume;
        mNumber = number;
        mNotes = notes;
    }
    
    public String getType()
    {
        return mType;
    }
    public void setType(String type)
    {
        mType = type;
    }
    public double getVolume()
    {
        return mVolume;
    }
    public void setVolume(double volume)
    {
        mVolume = volume;
    }

    public String getNotes()
    {
        return mNotes;
    }

    public void setNotes(String notes)
    {
        mNotes = notes;
    }

    public int getNumber()
    {
        return mNumber;
    }

    public void setNumber(int number)
    {
        mNumber = number;
    }
}
