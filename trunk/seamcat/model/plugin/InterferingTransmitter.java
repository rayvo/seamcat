// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferingTransmitter.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            Transceiver, Function3D

public interface InterferingTransmitter
    extends Transceiver
{

    public abstract double getAbsoluteUnwantedPower();

    public abstract Function3D getUnwantedFunction();

    public abstract boolean isUsingPowerControl();

    public abstract double getItVrAzimuth();

    public abstract double getItVrElevation();

    public abstract double getVrItAzimuth();

    public abstract double getVrItElevation();

    public abstract double getSuppliedPower();
}
