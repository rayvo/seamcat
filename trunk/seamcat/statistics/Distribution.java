// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Distribution.java

package org.seamcat.statistics;


// Referenced classes of package org.seamcat.statistics:
//            Domain

public abstract class Distribution
{

    public Distribution()
    {
    }

    public abstract double getDensity(double d);

    protected void setDomain(Domain d)
    {
        domain = d;
        type = domain.getType();
    }

    protected void setDomain(double a, double b, double w, int t)
    {
        setDomain(new Domain(a, b, w, t));
    }

    public Domain getDomain()
    {
        return domain;
    }

    public final int getType()
    {
        return type;
    }

    public double getMaxDensity()
    {
        double max = 0.0D;
        for(int i = 0; i < domain.getSize(); i++)
        {
            double d = getDensity(domain.getValue(i));
            if((d > max) & (d < (1.0D / 0.0D)))
                max = d;
        }

        return max;
    }

    public double getMoment(double a, int n)
    {
        double sum = 0.0D;
        double w;
        if(type == 0)
            w = 1.0D;
        else
            w = domain.getWidth();
        for(int i = 0; i < domain.getSize(); i++)
        {
            double x = domain.getValue(i);
            sum += Math.pow(x - a, n) * getDensity(x) * w;
        }

        return sum;
    }

    public double getMoment(int n)
    {
        return getMoment(0.0D, n);
    }

    public double getMean()
    {
        return getMoment(1);
    }

    public double getVariance()
    {
        return getMoment(getMean(), 2);
    }

    public double getSD()
    {
        return Math.sqrt(getVariance());
    }

    public double getCDF(double x)
    {
        double sum = 0.0D;
        double w;
        if(type == 0)
            w = 1.0D;
        else
            w = domain.getWidth();
        int j = domain.getIndex(x);
        if(j < 0)
            return 0.0D;
        if(j >= domain.getSize())
            return 1.0D;
        for(int i = 0; i <= j; i++)
            sum += getDensity(domain.getValue(i)) * w;

        if(type == 1)
        {
            double y = domain.getValue(j) - 0.5D * w;
            sum += getDensity((x + y) / 2D) * (x - y);
        }
        return sum;
    }

    public double getQuantile(double p)
    {
        double x;
        double q;
        if(type == 0)
        {
            if(p <= 0.0D)
                return domain.getLowerValue();
            if(p >= 1.0D)
                return domain.getUpperValue();
            int n = domain.getSize();
            int i = 0;
            x = domain.getValue(i);
            for(q = getDensity(x); (q < p) & (i < n); q += getDensity(x))
            {
                i++;
                x = domain.getValue(i);
            }

            return x;
        }
        if(p <= 0.0D)
            return domain.getLowerBound();
        if(p >= 1.0D)
            return domain.getUpperBound();
        double x1 = domain.getLowerBound();
        double x2 = domain.getUpperBound();
        x = (x1 + x2) / 2D;
        q = getCDF(x);
        double error = Math.abs(q - p);
        for(int n = 1; (error > 0.0001D) & (n < 100); error = Math.abs(q - p))
        {
            n++;
            if(q < p)
                x1 = x;
            else
                x2 = x;
            x = (x1 + x2) / 2D;
            q = getCDF(x);
        }

        return x;
    }

    public double simulate()
    {
        return getQuantile(Math.random());
    }

    public double getMedian()
    {
        return getQuantile(0.5D);
    }

    public double getFailureRate(double x)
    {
        return getDensity(x) / (1.0D - getCDF(x));
    }

    public double getMGF(double t)
    {
        double sum = 0.0D;
        double w;
        if(type == 0)
            w = 1.0D;
        else
            w = domain.getWidth();
        for(int i = 0; i < domain.getSize(); i++)
        {
            double x = domain.getValue(i);
            sum += Math.exp(t * x) * getDensity(x) * w;
        }

        return sum;
    }

    public double getPGF(double t)
    {
        return getMGF(Math.log(t));
    }

    public String toString()
    {
        return (new StringBuilder()).append("Distribution distribution [type = ").append(type).append(", domain = ").append(domain).append("]").toString();
    }

    public static final int DISCRETE = 0;
    public static final int CONTINUOUS = 1;
    public static final int MIXED = 2;
    private int type;
    private Domain domain;
}
