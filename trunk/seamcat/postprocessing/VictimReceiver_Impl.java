// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VictimReceiver_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.model.plugin.*;

// Referenced classes of package org.seamcat.postprocessing:
//            Transceiver_Impl

public class VictimReceiver_Impl extends Transceiver_Impl
    implements VictimReceiver
{

    public VictimReceiver_Impl()
    {
    }

    public Function2D getBlockingFunction()
    {
        return blocking;
    }

    public double getBandwith()
    {
        return bandwith;
    }

    public void setBandwith(double bandwith)
    {
        this.bandwith = bandwith;
    }

    public double getRequiredCIRatio()
    {
        return requiredCIRatio;
    }

    public void setRequiredCIRatio(double requiredCIRatio)
    {
        this.requiredCIRatio = requiredCIRatio;
    }

    public double getSensitivity()
    {
        return sensitivity;
    }

    public void setSensitivity(double sensitivity)
    {
        this.sensitivity = sensitivity;
    }

    public boolean isUsingPowerControl()
    {
        return usingPowerControl;
    }

    public void setUsingPowerControl(boolean usingPowerControl)
    {
        this.usingPowerControl = usingPowerControl;
    }

    public void setBlocking(Function2D blocking)
    {
        this.blocking = blocking;
    }

    public double getRequiredCNIRatio()
    {
        return requiredCNIRatio;
    }

    public void setRequiredCNIRatio(double requiredCNIRatio)
    {
        this.requiredCNIRatio = requiredCNIRatio;
    }

    public double getRequiredINRatio()
    {
        return requiredINRatio;
    }

    public void setRequiredINRatio(double requiredINRatio)
    {
        this.requiredINRatio = requiredINRatio;
    }

    public double getRequiredNINRatio()
    {
        return requiredNINRatio;
    }

    public void setRequiredNINRatio(double requiredNINRatio)
    {
        this.requiredNINRatio = requiredNINRatio;
    }

    public PluginDistribution getNoiseFloor()
    {
        return noiseFloor;
    }

    public void setNoiseFloor(PluginDistribution noiseFloor)
    {
        this.noiseFloor = noiseFloor;
    }

    protected double bandwith;
    protected double sensitivity;
    protected double requiredCIRatio;
    protected double requiredCNIRatio;
    protected double requiredNINRatio;
    protected double requiredINRatio;
    protected boolean usingPowerControl;
    protected Function2D blocking;
    protected PluginDistribution noiseFloor;
}
