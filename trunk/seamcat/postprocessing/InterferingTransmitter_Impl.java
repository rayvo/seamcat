// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferingTransmitter_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.function.DiscreteFunction2;
import org.seamcat.model.plugin.Function3D;
import org.seamcat.model.plugin.InterferingTransmitter;

// Referenced classes of package org.seamcat.postprocessing:
//            Transceiver_Impl

public class InterferingTransmitter_Impl extends Transceiver_Impl
    implements InterferingTransmitter
{

    public InterferingTransmitter_Impl()
    {
    }

    public Function3D getUnwantedFunction()
    {
        return unwantedFunction;
    }

    public double getAbsoluteUnwantedPower()
    {
        return absoluteUnwantedPower;
    }

    public void setAbsoluteUnwantedPower(double absoluteUnwantedPower)
    {
        this.absoluteUnwantedPower = absoluteUnwantedPower;
    }

    public boolean isUsingPowerControl()
    {
        return usingPowerControl;
    }

    public void setUsingPowerControl(boolean usingPowerControl)
    {
        this.usingPowerControl = usingPowerControl;
    }

    public void setUnwantedFunction(DiscreteFunction2 unwantedFunction)
    {
        this.unwantedFunction = unwantedFunction;
    }

    public double getItVrAzimuth()
    {
        return itVrAzimuth;
    }

    public void setItVrAzimuth(double itVrAzimuth)
    {
        this.itVrAzimuth = itVrAzimuth;
    }

    public double getItVrElevation()
    {
        return itVrElevation;
    }

    public void setItVrElevation(double itVrElevation)
    {
        this.itVrElevation = itVrElevation;
    }

    public double getVrItAzimuth()
    {
        return vrItAzimuth;
    }

    public void setVrItAzimuth(double vrItAzimuth)
    {
        this.vrItAzimuth = vrItAzimuth;
    }

    public double getVrItElevation()
    {
        return vrItElevation;
    }

    public void setVrItElevation(double vrItElevation)
    {
        this.vrItElevation = vrItElevation;
    }

    public double getSuppliedPower()
    {
        return itSuppliedPower;
    }

    public void setSuppliedPower(double val)
    {
        itSuppliedPower = val;
    }

    protected double absoluteUnwantedPower;
    protected boolean usingPowerControl;
    protected DiscreteFunction2 unwantedFunction;
    protected double itVrAzimuth;
    protected double itVrElevation;
    protected double vrItAzimuth;
    protected double vrItElevation;
    protected double itSuppliedPower;
}
