// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Transceiver_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.function.DiscreteFunction;
import org.seamcat.model.plugin.Function2D;
import org.seamcat.model.plugin.Transceiver;

public class Transceiver_Impl
    implements Transceiver
{

    public Transceiver_Impl()
    {
        useHorizontalPattern = false;
        useVerticalPattern = false;
        useSphericalPattern = false;
    }

    public double getDistanceTo(Transceiver t)
    {
        return Math.sqrt((positionX - t.getPositionX()) * (positionX - t.getPositionX()) + (positionY - t.getPositionY()) * (positionY - t.getPositionY()));
    }

    public Function2D getHorizontalAntennaPattern()
    {
        return horizontalAntennaPattern;
    }

    public Function2D getVerticalAntennaPattern()
    {
        return horizontalAntennaPattern;
    }

    public Function2D getSphericalAntennaPattern()
    {
        return horizontalAntennaPattern;
    }

    public boolean useHorizontalPattern()
    {
        return useHorizontalPattern;
    }

    public boolean useVerticalPattern()
    {
        return useVerticalPattern;
    }

    public boolean useSphericalPattern()
    {
        return useSphericalPattern;
    }

    public double getAntennaElevation()
    {
        return antennaElevation;
    }

    public void setAntennaElevation(double antennaElevation)
    {
        this.antennaElevation = antennaElevation;
    }

    public double getAntennaGain()
    {
        return antennaGain;
    }

    public void setAntennaGain(double antennaGain)
    {
        this.antennaGain = antennaGain;
    }

    public double getAntennaHeight()
    {
        return antennaHeight;
    }

    public void setAntennaHeight(double antennaHeight)
    {
        this.antennaHeight = antennaHeight;
    }

    public double getAntennaAzimuth()
    {
        return antennaAzimuth;
    }

    public void setAntennaAzimuth(double antennaAzimuth)
    {
        this.antennaAzimuth = antennaAzimuth;
    }

    public double getPositionX()
    {
        return positionX;
    }

    public void setPositionX(double positionX)
    {
        this.positionX = positionX;
    }

    public double getPositionY()
    {
        return positionY;
    }

    public void setPositionY(double positionY)
    {
        this.positionY = positionY;
    }

    public void setHorizontalAntennaPattern(DiscreteFunction horizontalAntennaPattern)
    {
        this.horizontalAntennaPattern = horizontalAntennaPattern;
    }

    public void setSphericalAntennaPattern(DiscreteFunction sphericalAntennaPattern)
    {
        this.sphericalAntennaPattern = sphericalAntennaPattern;
    }

    public void setVerticalAntennaPattern(DiscreteFunction verticalAntennaPattern)
    {
        this.verticalAntennaPattern = verticalAntennaPattern;
    }

    public void setUseHorizontalPattern(boolean useHorizontalPattern)
    {
        this.useHorizontalPattern = useHorizontalPattern;
    }

    public void setUseSphericalPattern(boolean useSphericalPattern)
    {
        this.useSphericalPattern = useSphericalPattern;
    }

    public void setUseVerticalPattern(boolean useVerticalPattern)
    {
        this.useVerticalPattern = useVerticalPattern;
    }

    protected double positionX;
    protected double positionY;
    protected double antennaGain;
    protected double antennaHeight;
    protected double antennaElevation;
    protected double antennaAzimuth;
    protected DiscreteFunction horizontalAntennaPattern;
    protected DiscreteFunction verticalAntennaPattern;
    protected DiscreteFunction sphericalAntennaPattern;
    protected boolean useHorizontalPattern;
    protected boolean useVerticalPattern;
    protected boolean useSphericalPattern;
}
