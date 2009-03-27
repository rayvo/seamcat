// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractTestCase.java

package org.seamcat.test;

import junit.framework.TestCase;

public class AbstractTestCase extends TestCase
{

    public AbstractTestCase()
    {
    }

    protected void testIt(Runnable r)
    {
        testIt(r, 1);
    }

    protected void testIt(Runnable r, int tests)
    {
        for(int x = 0; x < tests; x++)
            r.run();

    }

    protected static int rndInt()
    {
        return rndBoolean() ? rndNegativeInt() : rndPositiveInt();
    }

    protected static int rndPositiveInt()
    {
        return rndInt(0, 0x7fffffff);
    }

    protected static int rndNegativeInt()
    {
        return rndInt(0x80000000, 0);
    }

    protected static int rndInt(int min, int max)
    {
        return (int)(Math.random() * (double)(max - min) + (double)min);
    }

    protected static long rndPositiveLong()
    {
        return rndLong(0L, 0x7fffffffffffffffL);
    }

    protected static long rndNegativeLong()
    {
        return rndLong(0x8000000000000000L, 0L);
    }

    protected static long rndLong(long min, long max)
    {
        return (long)(Math.random() * (double)(max - min) + (double)min);
    }

    protected static long rndLong()
    {
        return rndBoolean() ? rndNegativeLong() : rndPositiveLong();
    }

    protected static double rndPositiveDouble()
    {
        return rndDouble(0.0D, 1.7976931348623157E+308D);
    }

    protected static double rndNegativeDouble()
    {
        return rndDouble(4.9406564584124654E-324D, 0.0D);
    }

    protected static double rndDouble(double min, double max)
    {
        return Math.random() * (max - min) + min;
    }

    protected static double rndDouble()
    {
        return rndBoolean() ? rndNegativeDouble() : rndPositiveDouble();
    }

    protected static boolean rndBoolean(double pos)
    {
        return Math.random() > pos;
    }

    protected static boolean rndBoolean()
    {
        return rndBoolean(0.5D);
    }

    protected static char rndChar()
    {
        return (char)(int)(Math.random() * 65535D);
    }

    protected static final int TESTS = 1;
}
