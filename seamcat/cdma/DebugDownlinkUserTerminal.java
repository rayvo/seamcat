// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DebugDownlinkUserTerminal.java

package org.seamcat.cdma;

import java.util.Iterator;
import java.util.List;
import org.seamcat.cdma.exceptions.LinkLevelDataException;
import org.seamcat.cdma.exceptions.PowerSummationException;
import org.seamcat.cdma.test.ValueTrackingDialog;

// Referenced classes of package org.seamcat.cdma:
//            UserTerminal, DebugCDMADownlink, CDMADownlink, CDMASystem, 
//            CDMALink, CDMACell, CDMALinkLevelData, CDMALinkLevelDataPoint

public class DebugDownlinkUserTerminal extends UserTerminal
{

    public DebugDownlinkUserTerminal(double locX, double locY, CDMASystem _system, int _userid, double antGain, double antHeight)
    {
        super(locX, locY, _system, _userid, antGain, antHeight);
        plotValueTracking = false;
        if(plotValueTracking)
        {
            valueTracker = new ValueTrackingDialog("Ec/Ior", new String[] {
                "Achieved", "Required", "Geometry", "Pactive (dBm)", "Ptraffic (dBm)", "Pinactive (dBm)", "Power Scale Up Count", "Itotal"
            });
            valueTracker.setVisible(plotValueTracking);
        }
    }

    public void generateCDMALinks()
    {
        CDMACell cells[][] = cdmasystem.getCDMACells();
        int linkid = 0;
        CDMALink links[] = super.getCDMALinks();
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length;)
            {
                links[linkid] = new DebugCDMADownlink(cells[i][j], this, cdmasystem);
                links[linkid].determinePathLoss();
                j++;
                linkid++;
            }

        }

    }

    public void addToActiveList(CDMADownlink link)
    {
        super.addToActiveList(link);
    }

    public void calculateAchievedEcIor()
    {
        super.calculateAchievedEcIor();
    }

    public void calculateDownlinkTransmitPower()
    {
        super.calculateDownlinkTransmitPower();
    }

    public double calculateGeometry()
    {
        double geo = super.calculateGeometry();
        return geo;
    }

    public double calculateInitialReceivedTrafficChannelPowerInWatt()
        throws LinkLevelDataException
    {
        return super.calculateInitialReceivedTrafficChannelPowerInWatt();
    }

    public void calculateReceivedPower()
        throws PowerSummationException
    {
        super.calculateReceivedPower();
        if(plotValueTracking)
        {
            valueTracker.addValue(getAchievedEcIor(), 0);
            valueTracker.addValue(getRequiredEcIor(), 1);
            valueTracker.addValue(getGeometry(), 2);
            valueTracker.addValue(getTotalPowerReceivedFromBaseStationsActiveSetIndBm(), 3);
            valueTracker.addValue(getReceivedTrafficChannelPowerdBm(), 4);
            valueTracker.addValue(getTotalPowerReceivedFromBaseStationsNotInActiveSetdBm(), 5);
            valueTracker.addValue(getPowerScaledUpCount(), 6);
            valueTracker.addValue(getExternalInterference(), 7);
            valueTracker.tickEvent();
            DebugCDMADownlink link;
            for(Iterator i$ = getActiveList().iterator(); i$.hasNext(); link.getValueTracker().tickEvent())
            {
                CDMADownlink l = (CDMADownlink)i$.next();
                link = (DebugCDMADownlink)l;
                if(!link.getValueTracker().isVisible())
                    link.getValueTracker().setVisible(true);
                link.getValueTracker().addValue(link.getTransmittedTrafficChannelPowerdBm(), 0);
                link.getValueTracker().addValue(link.getCDMACell().getCurrentTransmitPower(), 1);
            }

        }
    }

    public double calculateReceivedTrafficChannelPowerInWatt()
    {
        return super.calculateReceivedTrafficChannelPowerInWatt();
    }

    public double calculateTotalInterference()
    {
        return super.calculateTotalInterference();
    }

    public boolean connectToBaseStationsDownlink()
    {
        return super.connectToBaseStationsDownlink();
    }

    public CDMALinkLevelDataPoint findLinkLevelDataPoint(CDMALinkLevelData data)
    {
        return super.findLinkLevelDataPoint(data);
    }

    public boolean meetsEcIorRequirement(double callDropThreshold, boolean finalLoop)
    {
        return super.meetsEcIorRequirement(callDropThreshold, finalLoop);
    }

    public void dropCall()
    {
        super.dropCall();
    }

    public void setReceivedTrafficChannelPowerWatt(double trafficChannelPower)
    {
        super.setReceivedTrafficChannelPowerWatt(trafficChannelPower);
    }

    public void setTotalPowerReceivedFromBaseStationsInActiveSet(double totalPowerReceivedFromBaseStationsInActiveSet)
    {
        super.setTotalPowerReceivedFromBaseStationsInActiveSet(totalPowerReceivedFromBaseStationsInActiveSet);
    }

    public void setTotalPowerReceivedFromBaseStationsNotInActiveSet(double totalPowerReceivedFromBaseStationsNotInActiveSet)
    {
        super.setTotalPowerReceivedFromBaseStationsNotInActiveSet(totalPowerReceivedFromBaseStationsNotInActiveSet);
    }

    public volatile void addToActiveList(CDMALink x0)
    {
        addToActiveList((CDMADownlink)x0);
    }

    private ValueTrackingDialog valueTracker;
    private boolean plotValueTracking;
}