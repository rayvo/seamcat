// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   NonInterferedCapacityListener.java

package org.seamcat.cdma;


public interface NonInterferedCapacityListener
{

    public abstract void startingCapacityFinding(int i, int j, double d, int k, boolean flag, double d1);

    public abstract void startingTest(int i, int j, int k, boolean flag);

    public abstract void endingTest(int i, double d, boolean flag, int j);

    public abstract void capacityFound(int i, double d, boolean flag);

    public abstract void startingTrial(int i);

    public abstract void endingTrial(double d, boolean flag, int i);
}