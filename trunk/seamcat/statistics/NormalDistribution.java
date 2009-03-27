// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NormalDistribution.java

package org.seamcat.statistics;

import java.io.Serializable;

// Referenced classes of package org.seamcat.statistics:
//            Distribution, Functions

public class NormalDistribution extends Distribution
    implements Serializable
{

    public NormalDistribution(double mean, double s)
    {
        setParameters(mean, s);
    }

    public NormalDistribution()
    {
        this(0.0D, 1.0D);
    }

    public void setParameters(double m, double s)
    {
        if(s < 0.0D)
            s = 1.0D;
        location = m;
        scale = s;
        c = SQRT2PI * scale;
        double upper = location + 4D * scale;
        double lower = location - 4D * scale;
        double width = (upper - lower) / 100D;
        setDomain(lower, upper, width, 1);
    }

    public double getDensity(double x)
    {
        double z = (x - location) / scale;
        return Math.exp((-z * z) / 2D) / c;
    }

    public double getMaxDensity()
    {
        return getDensity(location);
    }

    public double getMedian()
    {
        return location;
    }

    public double getMean()
    {
        return location;
    }

    public double getVariance()
    {
        return scale * scale;
    }

    public double getCentralMoment(int n)
    {
        if(n == 2 * (n / 2))
            return (Functions.factorial(n) * Math.pow(scale, n)) / (Functions.factorial(n / 2) * Math.pow(2D, n / 2));
        else
            return 0.0D;
    }

    public double getMoment(double a, int n)
    {
        double sum = 0.0D;
        for(int k = 0; k <= n; k++)
            sum += Functions.comb(n, k) * getCentralMoment(k) * Math.pow(location - a, n - k);

        return sum;
    }

    public double getMGF(double t)
    {
        return Math.exp(location * t + (scale * scale * t * t) / 2D);
    }

    public double simulate()
    {
        double r = Math.sqrt(-2D * Math.log(Math.random()));
        double theta = 6.2831853071795862D * Math.random();
        return location + scale * r * Math.cos(theta);
    }

    public double getLocation()
    {
        return location;
    }

    public void setLocation(double m)
    {
        setParameters(m, scale);
    }

    public double getScale()
    {
        return scale;
    }

    public void setScale(double s)
    {
        setParameters(location, s);
    }

    public double getCDF(double x)
    {
        double z = (x - location) / scale;
        if(z >= 0.0D)
            return 0.5D + 0.5D * Functions.gammaCDF((z * z) / 2D, 0.5D);
        else
            return 0.5D - 0.5D * Functions.gammaCDF((z * z) / 2D, 0.5D);
    }

    public String toString()
    {
        return (new StringBuilder()).append("Normal distribution [location = ").append(location).append(", scale = ").append(scale).append("]").toString();
    }

    public static final double SQRT2PI = Math.sqrt(6.2831853071795862D);
    private double location;
    private double scale;
    private double c;

}
