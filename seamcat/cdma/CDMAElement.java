// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAElement.java

package org.seamcat.cdma;


// Referenced classes of package org.seamcat.cdma:
//            CDMASystem

public abstract class CDMAElement
{

    public CDMAElement(double _posX, double _posY, double _antHeight, CDMASystem _system)
    {
        externalInterferenceUnwanted = -1000D;
        externalInterferenceBlocking = -1000D;
        locationX = _posX;
        locationY = _posY;
        antennaHeight = _antHeight;
        cdmasystem = _system;
    }

    public abstract double calculateAntennaGainTo(double d, double d1);

    public double getFrequency()
    {
        return cdmasystem.getFrequency();
    }

    public double getReferenceBandwidth()
    {
        return cdmasystem.getSystemBandwidth();
    }

    public final double getExternalInterference()
    {
        return 10D * Math.log10(Math.pow(10D, externalInterferenceUnwanted / 10D) + Math.pow(10D, externalInterferenceBlocking / 10D));
    }

    public final CDMASystem getCdmasystem()
    {
        return cdmasystem;
    }

    public final void setCdmasystem(CDMASystem cdmasystem)
    {
        this.cdmasystem = cdmasystem;
    }

    public final double getInterSystemInterference()
    {
        return interSystemInterference;
    }

    public final void setInterSystemInterference(double interSystemInterference)
    {
        this.interSystemInterference = interSystemInterference;
    }

    public double getLocationX()
    {
        return locationX;
    }

    public void setLocationX(double locationX)
    {
        this.locationX = locationX;
    }

    public double getLocationY()
    {
        return locationY;
    }

    public void setLocationY(double locationY)
    {
        this.locationY = locationY;
    }

    public final double getTotalInterference()
    {
        return totalInterference;
    }

    public final void setTotalInterference(double totalInterference)
    {
        this.totalInterference = totalInterference;
    }

    public double getAntennaHeight()
    {
        return antennaHeight;
    }

    public void setAntennaHeight(double antennaHeight)
    {
        this.antennaHeight = antennaHeight;
    }

    public double getExternalInterferenceBlocking()
    {
        return externalInterferenceBlocking;
    }

    public void setExternalInterferenceBlocking(double externalInterferenceBlocking)
    {
        this.externalInterferenceBlocking = externalInterferenceBlocking;
    }

    public double getExternalInterferenceUnwanted()
    {
        return externalInterferenceUnwanted;
    }

    public void setExternalInterferenceUnwanted(double externalInterferenceUnwanted)
    {
        this.externalInterferenceUnwanted = externalInterferenceUnwanted;
    }

    protected double locationX;
    protected double locationY;
    protected CDMASystem cdmasystem;
    protected double antennaHeight;
    protected double totalInterference;
    protected double interSystemInterference;
    protected double externalInterferenceUnwanted;
    protected double externalInterferenceBlocking;
}