// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DebugDownlinkBaseStation.java

package org.seamcat.cdma;

import org.seamcat.cdma.exceptions.ScalingException;
import org.seamcat.model.Antenna;

// Referenced classes of package org.seamcat.cdma:
//            CDMAOmniDirectionalCell, CDMASystem

public class DebugDownlinkBaseStation extends CDMAOmniDirectionalCell
{

    public DebugDownlinkBaseStation(double _locationX, double _locationY, CDMASystem _system, int _cellid, double antHeight, Antenna ant)
    {
        super(_locationX, _locationY, _system, _cellid, antHeight, ant);
    }

    public double calculateCurrentChannelPower_dBm()
    {
        return super.calculateCurrentChannelPower_dBm();
    }

    public double getAvailableTrafficChannelInWatts()
    {
        return super.getAvailableTrafficChannelInWatts();
    }

    public double getCurrentChannelTransmitPower_dBm()
    {
        return super.getCurrentChannelTransmitPower_dBm();
    }

    public double getCurrentChannelTransmitPowerInWatts()
    {
        return super.getCurrentChannelTransmitPowerInWatts();
    }

    public double getCurrentTransmitPower()
    {
        return super.getCurrentTransmitPower();
    }

    public void scaleChannelPower(double scaleFactor)
        throws ScalingException
    {
        super.scaleChannelPower(scaleFactor);
    }
}