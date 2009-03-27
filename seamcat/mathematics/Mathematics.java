// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Mathematics.java

package org.seamcat.mathematics;


public class Mathematics
{

    private Mathematics()
    {
    }

    public static double cosh(double x)
    {
        return (Math.exp(x) + Math.exp(-x)) / 2D;
    }

    public static double sinh(double x)
    {
        return (Math.exp(x) - Math.exp(-x)) / 2D;
    }

    public static double tanh(double x)
    {
        return (Math.exp(x) - Math.exp(-x)) / (Math.exp(x) + Math.exp(-x));
    }

    public static double acosD(double angle)
    {
        return Math.acos(angle) * 57.295779513082323D;
    }

    public static double asinD(double x)
    {
        return Math.asin(x) * 57.295779513082323D;
    }

    public static double atanD(double angle)
    {
        return 57.295779513082323D * Math.atan(angle);
    }

    public static double atan2D(double x, double y)
    {
        return Math.atan2(y, x) * 57.295779513082323D;
    }

    public static double cosD(double angle)
    {
        return Math.cos(angle * 0.017453292519943295D);
    }

    public static double sinD(double angle)
    {
        return Math.sin(angle * 0.017453292519943295D);
    }

    public static double tanD(double angle)
    {
        return Math.tan(angle * 0.017453292519943295D);
    }

    public static double getAverage(double p[], int length)
    {
        double sum = 0.0D;
        for(int i = 0; i < length && i < p.length; i++)
            sum += p[i];

        return sum / (double)length;
    }

    public static double getAverage(double p[])
    {
        return getAverage(p, p.length);
    }

    public static double getStdDev(double p[], double ave)
    {
        return getStdDev(p, ave, p.length);
    }

    public static double getStdDev(double p[])
    {
        return getStdDev(p, getAverage(p), p.length);
    }

    public static double getStdDev(double p[], double ave, int length)
    {
        double sum = 0.0D;
        for(int i = 0; i < length && i < p.length; i++)
        {
            double dev = ave - p[i];
            sum += dev * dev;
        }

        return Math.sqrt(sum / (double)(length - 1));
    }

    public static double min(double p[])
    {
        double min = 1.7976931348623157E+308D;
        for(int i = 0; i < p.length; i++)
            if(p[i] < min)
                min = p[i];

        return min;
    }

    public static double max(double p[])
    {
        double max = (-1.0D / 0.0D);
        for(int i = 0; i < p.length; i++)
            if(p[i] > max)
                max = p[i];

        return max;
    }

    private static final double RADEG = 57.295779513082323D;
    private static final double DEGRAD = 0.017453292519943295D;
}