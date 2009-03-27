// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Stats.java

package org.seamcat.model.technical.stats;


public class Stats
{

    public Stats()
    {
    }

    private static final double getrC0()
    {
        return rC0;
    }

    private static void setrC0(double value)
    {
        rC0 = value;
    }

    private static final double getrC1()
    {
        return rC1;
    }

    private static void setrC1(double value)
    {
        rC1 = value;
    }

    private static final double getrC2()
    {
        return rC2;
    }

    private static void setrC2(double value)
    {
        rC2 = value;
    }

    private static final double getrD1()
    {
        return rD1;
    }

    private static void setrD1(double value)
    {
        rD1 = value;
    }

    private static final double getrD2()
    {
        return rD2;
    }

    private static void setrD2(double value)
    {
        rD2 = value;
    }

    private static final double getrD3()
    {
        return rD3;
    }

    private static void setrD3(double value)
    {
        rD3 = value;
    }

    private static double t(double rX)
    {
        double rT = 0.0D;
        rT = Math.sqrt(-2D * Math.log(rX));
        return rT;
    }

    private static double dZeta(double rX)
    {
        double rDzeta = 0.0D;
        double rT = 0.0D;
        rT = t(rX);
        rDzeta = ((rC2 * rT + rC1) * rT + rC0) / (((rD3 * rT + rD2) * rT + rD1) * rT + 1.0D);
        return rDzeta;
    }

    public static void init()
    {
        seed0 = 1L;
    }

    public static double qi(double rX)
    {
        double rQ = 0.0D;
        if(rX < 0.5D)
            rQ = t(rX) - dZeta(rX);
        else
            rQ = -(t(1.0D - rX) - dZeta(1.0D - rX));
        return rQ;
    }

    public static final long getSeed0()
    {
        seed0++;
        return seed0;
    }

    private static long seed0 = 1L;
    private static double rC0 = 2.515517D;
    private static double rC1 = 0.80285300000000004D;
    private static double rC2 = 0.010328D;
    private static double rD1 = 1.432788D;
    private static double rD2 = 0.18926899999999999D;
    private static double rD3 = 0.0013079999999999999D;

}
