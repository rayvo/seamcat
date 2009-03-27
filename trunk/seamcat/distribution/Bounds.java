// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Bounds.java

package org.seamcat.distribution;


public class Bounds
{

    public Bounds(double _rXmin, double _rXmax, boolean _bounded)
    {
        rXmax = _rXmax;
        rXmin = _rXmin;
        bounded = _bounded;
    }

    public double getMin()
    {
        return rXmin;
    }

    public double getMax()
    {
        return rXmax;
    }

    public boolean isBounded()
    {
        return bounded;
    }

    private double rXmin;
    private double rXmax;
    private boolean bounded;
}