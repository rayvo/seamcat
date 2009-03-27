// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DebugCDMADownlink.java

package org.seamcat.cdma;

import org.seamcat.cdma.exceptions.PowerSummationException;
import org.seamcat.cdma.test.ValueTrackingDialog;

// Referenced classes of package org.seamcat.cdma:
//            CDMADownlink, CDMACell, UserTerminal, CDMASystem

public class DebugCDMADownlink extends CDMADownlink
{

    public DebugCDMADownlink(CDMACell _cell, UserTerminal _user, CDMASystem _cdmasystem)
    {
        super(_cell, _user, _cdmasystem);
        plotValueTracking = false;
        if(plotValueTracking)
            valueTracker = new ValueTrackingDialog((new StringBuilder()).append("BS").append(cell.getCellid()).append(" -> MS").append(user.getUserid()).toString(), new String[] {
                "Transmitted Traffic Power (dBm)", "BS Tx Total (dBm)"
            });
    }

    public ValueTrackingDialog getValueTracker()
    {
        return valueTracker;
    }

    public double calculateReceivePower()
        throws PowerSummationException
    {
        return super.calculateReceivePower();
    }

    public double calculateTotalTransmitPower(double receivedPower)
    {
        return super.calculateTotalTransmitPower(receivedPower);
    }

    public double calculateTransmittedTrafficChannelPowerIndBm()
    {
        return super.calculateTransmittedTrafficChannelPowerIndBm();
    }

    public void determinePathLoss()
    {
        super.determinePathLoss();
    }

    public double getTransmitPowerInWatt()
    {
        return super.getTransmitPowerInWatt();
    }

    public double getTransmittedTrafficChannelPowerdBm()
    {
        return super.getTransmittedTrafficChannelPowerdBm();
    }

    public double getTransmittedTrafficChannelPowerWatt()
    {
        return super.getTransmittedTrafficChannelPowerWatt();
    }

    public void scaleTransmitPower(double scaleValue)
    {
        super.scaleTransmitPower(scaleValue);
    }

    public void setPowerScaledDownToMax(boolean powerScaledDownToMax)
    {
        super.setPowerScaledDownToMax(powerScaledDownToMax);
    }

    private ValueTrackingDialog valueTracker;
    private boolean plotValueTracking;
}