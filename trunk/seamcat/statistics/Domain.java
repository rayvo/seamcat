// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Domain.java

package org.seamcat.statistics;

import java.io.Serializable;

public class Domain
    implements Serializable
{

    public Domain(double a, double b, double w, int t)
    {
        if(w <= 0.0D)
            w = 1.0D;
        width = w;
        if(t < 0)
            t = 0;
        else
        if(t > 1)
            t = 1;
        type = t;
        if(type == 0)
        {
            if(b < a)
                b = a;
            lowerBound = a - 0.5D * width;
            upperBound = b + 0.5D * width;
        } else
        {
            if(b < a + w)
                b = a + w;
            lowerBound = a;
            upperBound = b;
        }
        lowerValue = lowerBound + 0.5D * width;
        upperValue = upperBound - 0.5D * width;
        size = (int)Math.rint((upperBound - lowerBound) / width);
    }

    public Domain()
    {
        this(0.0D, 1.0D, 0.10000000000000001D, 1);
    }

    public int getType()
    {
        return type;
    }

    public int getIndex(double x)
    {
        if(x < lowerBound)
            return -1;
        if(x > upperBound)
            return size;
        else
            return (int)Math.rint((x - lowerValue) / width);
    }

    public double getBound(int i)
    {
        return lowerBound + (double)i * width;
    }

    public double getValue(int i)
    {
        return lowerValue + (double)i * width;
    }

    public double getLowerBound()
    {
        return lowerBound;
    }

    public double getUpperBound()
    {
        return upperBound;
    }

    public double getLowerValue()
    {
        return lowerValue;
    }

    public double getUpperValue()
    {
        return upperValue;
    }

    public double getWidth()
    {
        return width;
    }

    public int getSize()
    {
        return size;
    }

    public double getNearestValue(double x)
    {
        return getValue(getIndex(x));
    }

    private double lowerBound;
    private double upperBound;
    private double width;
    private double lowerValue;
    private double upperValue;
    private int size;
    private int type;
    public static final int DISCRETE = 0;
    public static final int CONTINUOUS = 1;
}
