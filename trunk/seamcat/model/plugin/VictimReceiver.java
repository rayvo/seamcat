// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VictimReceiver.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            Transceiver, Function2D, PluginDistribution

public interface VictimReceiver
    extends Transceiver
{

    public abstract double getBandwith();

    public abstract double getSensitivity();

    public abstract double getRequiredCIRatio();

    public abstract double getRequiredCNIRatio();

    public abstract double getRequiredNINRatio();

    public abstract double getRequiredINRatio();

    public abstract Function2D getBlockingFunction();

    public abstract boolean isUsingPowerControl();

    public abstract PluginDistribution getNoiseFloor();
}
