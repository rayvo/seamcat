// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProcessingResult_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.model.plugin.ProcessingResult;

public class ProcessingResult_Impl
    implements ProcessingResult
{

    public ProcessingResult_Impl(int absLinkCount, int origCount)
    {
        linkCount = origCount;
        unwanted = new double[absLinkCount];
        blocking = new double[absLinkCount];
        unwantedChanges = new boolean[absLinkCount];
        blockingChanges = new boolean[absLinkCount];
        intermodulation = new double[origCount * (origCount - 1) + 1];
        intermodulationChanges = new boolean[origCount * (origCount - 1) + 1];
        stopRequested = false;
    }

    public void reset()
    {
        drss = 0.0D;
        drssChanged = false;
        for(int i = 0; i < unwanted.length; i++)
        {
            unwanted[i] = 0.0D;
            unwantedChanges[i] = false;
            blocking[i] = 0.0D;
            blockingChanges[i] = false;
        }

        for(int i = 0; i < intermodulation.length; i++)
        {
            intermodulation[i] = 0.0D;
            intermodulationChanges[i] = false;
        }

        stopRequested = false;
    }

    public void setDRSS(double value)
    {
        drss = value;
        drssChanged = true;
    }

    public double getDRSS()
    {
        return drss;
    }

    public void setIRSSUnwanted(int linkIndex, double irss)
    {
        unwanted[linkIndex - 1] = irss;
        unwantedChanges[linkIndex - 1] = true;
    }

    public void setIRSSBlocking(int linkIndex, double irss)
    {
        blocking[linkIndex - 1] = irss;
        blockingChanges[linkIndex - 1] = true;
    }

    public void setIRSSIntermodulation(int firstLinkIndex, int secondLinkIndex, double irss)
    {
        firstLinkIndex--;
        secondLinkIndex--;
        intermodulation[firstLinkIndex] = irss;
        intermodulationChanges[secondLinkIndex] = true;
    }

    public boolean isDRSSChanged()
    {
        return drssChanged;
    }

    public boolean isIRSSUnwantedChanged(int linkIndex)
    {
        return unwantedChanges[linkIndex - 1];
    }

    public boolean isIRSSBlockingChanged(int linkIndex)
    {
        return blockingChanges[linkIndex - 1];
    }

    public boolean isIRSSIntermodulationChanged(int firstLinkIndex, int secondLinkIndex)
    {
        if(linkCount < 2)
            return false;
        int index;
        if(firstLinkIndex < secondLinkIndex)
            index = firstLinkIndex * secondLinkIndex - (firstLinkIndex - (linkCount - secondLinkIndex));
        else
            index = linkCount * firstLinkIndex - ((firstLinkIndex - 1) + (linkCount - secondLinkIndex));
        return intermodulationChanges[index];
    }

    public double getIRSSUnwanted(int linkIndex)
    {
        return unwanted[linkIndex - 1];
    }

    public double getIRSSBlocking(int linkIndex)
    {
        return blocking[linkIndex - 1];
    }

    public double getIRSSIntermodulation(int firstLinkIndex, int secondLinkIndex)
    {
        int index;
        if(firstLinkIndex < secondLinkIndex)
            index = firstLinkIndex * secondLinkIndex - (firstLinkIndex - (linkCount - secondLinkIndex));
        else
            index = linkCount * firstLinkIndex - ((firstLinkIndex - 1) + (linkCount - secondLinkIndex));
        return intermodulation[index];
    }

    public boolean isStopRequested()
    {
        return stopRequested;
    }

    public void setStopRequested(boolean stopRequested)
    {
        this.stopRequested = stopRequested;
    }

    protected double drss;
    protected boolean drssChanged;
    protected final double unwanted[];
    protected final boolean unwantedChanges[];
    protected final double blocking[];
    protected final boolean blockingChanges[];
    protected final double intermodulation[];
    protected final boolean intermodulationChanges[];
    protected boolean stopRequested;
    private final int linkCount;
}
