package parsebibl;

import java.io.Serializable;
import java.util.Date;

public class BM_old
        implements Serializable
{
    private String version;
    private Date date;
    private double placex;
    private double placey;
    protected int UI;

    public void SetDate(Date d)
    {
        date = d;
    }

    public Date GetDate()
    {
        return date;
    }

    public void SetPlace(double x, double y)
    {
        placex = x;
        placey = y;
    }

    public double GetX()
    {
        return placex;
    }

    public double GetY()
    {
        return placey;
    }

    public BM_old()
    {
        this.date = new Date();
        this.UI = this.hashCode();
    }

    public String GetVersion()
    {
        return this.version;
    }

    public void SetVersion(String v)
    {
        this.version = v;
    }
}