package parsebibl;

public class DFR_old extends BM_old
{
    public String nameshop;
    public double[][] matrix;
    private double cashcass;
    private int TE;

    public DFR_old()
    {
    }

    public void setTypeEvent(int te)
    {
        this.TE = te;
    }

    public int getTypeEvent()
    {
        return this.TE;
    }

    public void setCash(double d)
    {
        this.cashcass = d;
    }

    public double getCash()
    {
        return this.cashcass;
    }

    public void addData(DFR_old DFR, boolean fl)
    {
        int sign = 1;
        if (!fl)
        {
            sign *= -1;
        }
        int len = this.matrix.length;
        for (int i = 0; i < len; i++)
        {
            int len2 = this.matrix[i].length;
            for (int j = 0; j < len2; j++)
            {
                this.matrix[i][j] += DFR.matrix[i][j] * sign;
            }
        }
    }
}