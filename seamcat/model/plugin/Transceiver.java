// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Transceiver.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            Function2D

public interface Transceiver
{

    public abstract double getPositionX();

    public abstract double getPositionY();

    public abstract double getDistanceTo(Transceiver transceiver);

    public abstract double getAntennaGain();

    public abstract Function2D getHorizontalAntennaPattern();

    public abstract Function2D getVerticalAntennaPattern();

    public abstract Function2D getSphericalAntennaPattern();

    public abstract boolean useHorizontalPattern();

    public abstract boolean useVerticalPattern();

    public abstract boolean useSphericalPattern();

    public abstract double getAntennaHeight();

    public abstract double getAntennaElevation();

    public abstract double getAntennaAzimuth();
}
