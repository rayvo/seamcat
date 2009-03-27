// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ProcessingResult.java

package org.seamcat.model.plugin;


public interface ProcessingResult
{

    public abstract void setDRSS(double d);

    public abstract double getDRSS();

    public abstract boolean isDRSSChanged();

    public abstract void setIRSSUnwanted(int i, double d);

    public abstract double getIRSSUnwanted(int i);

    public abstract boolean isIRSSUnwantedChanged(int i);

    public abstract void setIRSSBlocking(int i, double d);

    public abstract double getIRSSBlocking(int i);

    public abstract boolean isIRSSBlockingChanged(int i);

    public abstract void setIRSSIntermodulation(int i, int j, double d);

    public abstract double getIRSSIntermodulation(int i, int j);

    public abstract boolean isIRSSIntermodulationChanged(int i, int j);
}
