// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   UserTerminal.java

package org.seamcat.cdma;

import java.util.*;
import org.apache.log4j.Logger;
import org.seamcat.cdma.exceptions.LinkLevelDataException;
import org.seamcat.cdma.exceptions.PowerSummationException;

// Referenced classes of package org.seamcat.cdma:
//            CDMAElement, CDMALink, CDMAUplink, CDMADownlink, 
//            CDMALinkLevelDataPoint, CDMASystem, CDMACell, CDMALinkLevelData

public class UserTerminal extends CDMAElement
{

    public UserTerminal(double locX, double locY, CDMASystem _system, int _userid, double antGain, double antHeight)
    {
        super(locX, locY, antHeight, _system);
        uplinkMode = false;
        linkLevelData = null;
        linkQualityExceptions = 0;
        downlinkFreePassLimit = 5;
        powerScaledUpCount = 0;
        userid = _userid;
        antennaGain = antGain;
        links = (CDMALink[])new CDMALink[_system.getNumberOfCellSitesInPowerControlCluster()];
        activeList = new ArrayList(2);
    }

    public void resetTerminal()
    {
        activeList.clear();
        links = (CDMALink[])new Object[cdmasystem.getNumberOfCellSitesInPowerControlCluster()];
    }

    public void generateCDMALinks()
    {
        CDMACell cells[][] = cdmasystem.getCDMACells();
        int linkid = 0;
        for(int i = 0; i < cells.length; i++)
        {
            double pathloss = 0.0D;
            for(int j = 0; j < cells[i].length;)
            {
                if(isUplinkMode())
                    links[linkid] = new CDMAUplink(cells[i][j], this, cdmasystem);
                else
                    links[linkid] = new CDMADownlink(cells[i][j], this, cdmasystem);
                if(j == 0)
                {
                    links[linkid].determinePathLoss();
                    pathloss = links[linkid].getPathLoss();
                } else
                {
                    links[linkid].setPathLoss(pathloss);
                }
                j++;
                linkid++;
            }

        }

    }

    public void selectActiveList(double softHandoverMargin)
    {
        sortLinks(CDMALink.CDMALinkPathlossComparator);
        addToActiveList(links[0]);
        if(Math.abs(links[0].getPathLoss() - links[0].getBsAntGain() - links[0].getUserAntGain() - (links[1].getPathLoss() - links[1].getBsAntGain() - links[1].getUserAntGain())) < softHandoverMargin)
        {
            addToActiveList(links[1]);
            isInSoftHandover = true;
        }
    }

    public void addToActiveList(CDMALink link)
    {
        if(link.getUserTerminal() != this)
        {
            throw new IllegalArgumentException("Attempt to add CDMA link to active list of different user");
        } else
        {
            activeList.add(link);
            return;
        }
    }

    public void connectToBaseStationsUplink()
    {
        CDMAUplink firstLink = (CDMAUplink)links[0];
        activeUplink = firstLink;
        double achievedCI_firstLink = firstLink.initializePowerLevels();
        achievedCI = achievedCI_firstLink;
        if(isInSoftHandover)
        {
            CDMAUplink secondLink = (CDMAUplink)links[1];
            double achievedCI_secondLink = secondLink.initializePowerLevels();
            if(achievedCI_secondLink > achievedCI_firstLink)
            {
                activeUplink = secondLink;
                achievedCI = achievedCI_secondLink;
            }
        }
        activeUplink.initializeConnection();
        int i = 0;
        for(int stop = links.length; i < stop; i++)
            if(activeUplink != links[i])
                links[i].connectToInActiveBaseStation();

        connected = true;
    }

    public boolean connectToBaseStationsDownlink()
    {
        links[0].initializeConnection();
        boolean powerScaledDown = links[0].isPowerScaledDownToMax();
        int i = 1;
        if(isInSoftHandover)
        {
            links[1].initializeConnection();
            powerScaledDown = powerScaledDown || links[1].isPowerScaledDownToMax();
            i++;
        }
        for(int stop = links.length; i < stop; i++)
            links[i].connectToInActiveBaseStation();

        if(powerScaledDown)
            connected = meetsEcIorRequirement(cdmasystem.getCallDropThreshold());
        else
            connected = true;
        return connected;
    }

    public void sortLinks(Comparator c)
    {
        Arrays.sort(links, c);
    }

    public String toString()
    {
        return (new StringBuilder()).append("MS #").append(getUserid()).append(" at (").append(CDMASystem.round(locationX)).append(", ").append(CDMASystem.round(locationY)).append(")").append(" [Geo = ").append(getGeometry()).append(" dB]").toString();
    }

    public double calculateGeometry()
    {
        double extInt = CDMASystem.fromdBm2Watt(getExternalInterference());
        double interference = totalPowerReceivedFromBaseStationsNotInActiveSet + extInt;
        if(isInSoftHandover && getMobilitySpeed() == 0.0D)
        {
            double c1 = CDMASystem.fromdBm2Watt(((CDMALink)activeList.get(0)).getReceivePower_dB());
            double c2 = CDMASystem.fromdBm2Watt(((CDMALink)activeList.get(1)).getReceivePower_dB());
            double absGeometry = c1 / (c2 + thermalNoise + interference) + c2 / (c1 + thermalNoise + interference);
            geometry = 10D * Math.log10(absGeometry);
        } else
        {
            double absGeometry = totalPowerReceivedFromBaseStationsInActiveSet / (thermalNoise + interference);
            geometry = 10D * Math.log10(absGeometry);
        }
        return geometry;
    }

    public double calculateAchievedCI()
    {
        oldAchievedCI = achievedCI;
        CDMAUplink link = activeUplink;
        achievedCI = link.calculateAchivedCI();
        if(isInSoftHandover)
        {
            Iterator i$ = activeList.iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                CDMALink l = (CDMALink)i$.next();
                if(l != link)
                {
                    double achievedCI_inactiveLink = ((CDMAUplink)l).calculateAchivedCI();
                    if(l.getCDMACell().getCellLocationId() == link.getCDMACell().getCellLocationId())
                        achievedCI = CDMASystem.powerSummation(new double[] {
                            achievedCI, achievedCI_inactiveLink
                        });
                    else
                    if(achievedCI_inactiveLink > achievedCI)
                    {
                        achievedCI = achievedCI_inactiveLink;
                        activeUplink.deinitilizeConnection();
                        activeUplink = (CDMAUplink)l;
                        activeUplink.initializeConnection();
                    }
                }
            } while(true);
        }
        return achievedCI;
    }

    public boolean meetsEcIorRequirement(double callDropThreshold)
    {
        return meetsEcIorRequirement(callDropThreshold, false);
    }

    public void calculateAchievedEcIor()
    {
        double pTraf_W = getReceivedTrafficChannelPowerWatt();
        double pTot_W = getTotalPowerReceivedFromBaseStationsActiveSetInWatt();
        double pTraf = CDMASystem.fromWatt2dBm(pTraf_W);
        double pTot = CDMASystem.fromWatt2dBm(pTot_W);
        achievedEcIor = pTraf - pTot;
        requiredEcIor = linkLevelData.getEcIor();
    }

    public boolean meetsEcIorRequirement(double callDropThreshold, boolean finalLoop)
    {
        boolean value = achievedEcIor >= requiredEcIor - callDropThreshold;
        if(finalLoop || value)
            linkQualityExceptions = 0;
        else
        if(!value && linkQualityExceptions < downlinkFreePassLimit)
        {
            linkQualityExceptions++;
            value = true;
        }
        return value;
    }

    public double calculateTotalInterference()
    {
        totalInterference = CDMASystem.fromWatt2dBm(CDMASystem.fromdBm2Watt(getExternalInterference()) + cdmasystem.getThermalNoiseInWatt());
        return totalInterference;
    }

    public void dropCall()
    {
        int i = 0;
        for(int stop = links.length; i < stop; i++)
            links[i].disconnect();

        connected = false;
        dropped = true;
    }

    public double calculateTransmitPower()
        throws PowerSummationException
    {
        double powerSum = 0.0D;
        int i = 0;
        for(int stop = activeList.size(); i < stop; i++)
        {
            double rec = links[i].calculateReceivePower();
            powerSum += CDMASystem.fromdBm2Watt(links[i].calculateTotalTransmitPower(rec));
        }

        currentTransmitPower = CDMASystem.fromWatt2dBm(powerSum);
        return currentTransmitPower;
    }

    public void calculateReceivedPower()
        throws PowerSummationException
    {
        double resultActive_W = 0.0D;
        double resultInActive_W = 0.0D;
        for(int x = 0; x < links.length; x++)
            if(activeList.contains(links[x]))
                resultActive_W += CDMASystem.fromdBm2Watt(links[x].calculateReceivePower());
            else
                resultInActive_W += CDMASystem.fromdBm2Watt(links[x].calculateReceivePower());

        setTotalPowerReceivedFromBaseStationsInActiveSet(resultActive_W);
        setTotalPowerReceivedFromBaseStationsNotInActiveSet(resultInActive_W);
    }

    public void calculateInitialReceivedPower()
        throws PowerSummationException
    {
        double resultActive_W = 0.0D;
        double resultInActive_W = 0.0D;
        double initialTransmitPower_dbm = CDMASystem.fromWatt2dBm(CDMASystem.fromdBm2Watt(cdmasystem.getBaseStationMaximumBroadcastPowerdBm()) * 0.69999999999999996D);
        for(int x = 0; x < links.length; x++)
            if(activeList.contains(links[x]))
                resultActive_W += CDMASystem.fromdBm2Watt((initialTransmitPower_dbm + links[x].getBsAntGain() + links[x].getUserAntGain()) - links[x].getEffectivePathloss());
            else
                resultInActive_W += CDMASystem.fromdBm2Watt((initialTransmitPower_dbm + links[x].getBsAntGain() + links[x].getUserAntGain()) - links[x].getEffectivePathloss());

        setTotalPowerReceivedFromBaseStationsInActiveSet(resultActive_W);
        setTotalPowerReceivedFromBaseStationsNotInActiveSet(resultInActive_W);
    }

    public double calculateDistance(UserTerminal u)
    {
        return Math.sqrt((getLocationX() - u.getLocationX()) * (getLocationX() - u.getLocationX()) + (getLocationY() - u.getLocationY()) * (getLocationY() - u.getLocationY()));
    }

    public CDMALinkLevelDataPoint findLinkLevelDataPoint(CDMALinkLevelData data)
    {
        if(data == null)
            throw new IllegalArgumentException("Supplied CDMALinkLevelData is null");
        try
        {
            if(uplinkMode)
                linkLevelData = data.getLinkLevelDataPoint(new CDMALinkLevelDataPoint(cdmasystem.getFrequency(), multiPathChannel, 0.0D, getMobilitySpeed(), 0.0D));
            else
                linkLevelData = data.getLinkLevelDataPoint(new CDMALinkLevelDataPoint(cdmasystem.getFrequency(), activeList.size(), getGeometry(), getMobilitySpeed(), 0.0D));
            linkLevelDataPointFound = true;
        }
        catch(IllegalStateException ex)
        {
            linkLevelDataPointFound = false;
        }
        if(linkLevelData == null)
            linkLevelDataPointFound = false;
        return linkLevelData;
    }

    public boolean linkLevelDataPointFound()
    {
        return linkLevelDataPointFound;
    }

    public CDMALinkLevelDataPoint getLinkLevelData()
    {
        if(linkLevelData == null)
            findLinkLevelDataPoint(cdmasystem.getLinkLevelData());
        return linkLevelData;
    }

    public double calculateInitialReceivedTrafficChannelPowerInWatt()
        throws LinkLevelDataException
    {
        if(!linkLevelDataPointFound)
            throw new LinkLevelDataException((new StringBuilder()).append("No link level data was found [Geo: ").append(getGeometry()).append(" dB; Speed: ").append(getMobilitySpeed()).append(" km/h; ").append(activeList.size()).append("-path]").toString());
        double EcIor = CDMASystem.delogaritmize(linkLevelData.getEcIor());
        double P_init_rx = CDMASystem.fromdBm2Watt(cdmasystem.getBaseStationMaximumBroadcastPowerdBm()) * 0.69999999999999996D;
        receivedTrafficChannelPowerWatt = P_init_rx * EcIor;
        if(isInSoftHandover())
            receivedTrafficChannelPowerWatt /= 2D;
        CDMADownlink link;
        for(Iterator i$ = activeList.iterator(); i$.hasNext(); link.calculateTransmittedTrafficChannelPowerIndBm())
            link = (CDMADownlink)i$.next();

        if(isInSoftHandover())
            receivedTrafficChannelPowerWatt *= 2D;
        return receivedTrafficChannelPowerWatt;
    }

    public double calculateReceivedTrafficChannelPowerInWatt()
    {
        double EcIor = CDMASystem.delogaritmize(linkLevelData.getEcIor());
        receivedTrafficChannelPowerWatt = totalPowerReceivedFromBaseStationsInActiveSet * EcIor;
        double receivedPerLink = receivedTrafficChannelPowerWatt;
        if(isInSoftHandover())
            receivedPerLink /= 2D;
        CDMADownlink link;
        for(Iterator i$ = activeList.iterator(); i$.hasNext(); link.calculateTransmittedTrafficChannelPowerIndBm())
        {
            link = (CDMADownlink)i$.next();
            link.setReceivedTrafficChannelPowerdBm(CDMASystem.fromWatt2dBm(receivedPerLink));
        }

        return getReceivedTrafficChannelPowerWatt();
    }

    public double getReceivedTrafficChannelPowerWatt()
    {
        double received = 0.0D;
        for(Iterator i$ = activeList.iterator(); i$.hasNext();)
        {
            CDMADownlink link = (CDMADownlink)i$.next();
            received += CDMASystem.fromdBm2Watt(link.getReceivedTrafficChannelPowerdBm());
        }

        receivedTrafficChannelPowerWatt = received;
        return receivedTrafficChannelPowerWatt;
    }

    public void calculateDownlinkTransmitPower()
    {
        CDMADownlink link;
        for(Iterator i$ = activeList.iterator(); i$.hasNext(); link.calculateTotalTransmitPower(totalPowerReceivedFromBaseStationsInActiveSet))
        {
            link = (CDMADownlink)i$.next();
            link.calculateTransmittedTrafficChannelPowerIndBm();
        }

    }

    public void setPowerControlRange(double pcRange)
    {
        powerControlRange = pcRange;
    }

    public double calculateMinimumTransmitPower()
    {
        minTxPower = 10D * Math.log10(CDMASystem.delogaritmize(maxTxPower) - CDMASystem.delogaritmize(powerControlRange));
        return minTxPower;
    }

    public CDMALink[] getCDMALinks()
    {
        return links;
    }

    public List getActiveList()
    {
        return activeList;
    }

    public void setMobilitySpeed(double mobilitySpeed)
    {
        this.mobilitySpeed = mobilitySpeed;
    }

    public void setVoiceIsActive(boolean _voiceIsActive)
    {
        voiceIsActive = _voiceIsActive;
    }

    public void setAntennaGain(double antennaGain)
    {
        this.antennaGain = antennaGain;
    }

    public void setTotalPowerReceivedFromBaseStationsNotInActiveSet(double totalPowerReceivedFromBaseStationsNotInActiveSet)
    {
        this.totalPowerReceivedFromBaseStationsNotInActiveSet = totalPowerReceivedFromBaseStationsNotInActiveSet;
    }

    public void setThermalNoise(double thermalNoise)
    {
        this.thermalNoise = thermalNoise;
    }

    public void setTotalPowerReceivedFromBaseStationsInActiveSet(double totalPowerReceivedFromBaseStationsInActiveSet)
    {
        this.totalPowerReceivedFromBaseStationsInActiveSet = totalPowerReceivedFromBaseStationsInActiveSet;
    }

    public void setReceivedTrafficChannelPowerWatt(double trafficChannelPower)
    {
        if(trafficChannelPower > cdmasystem.getMaxTrafficChannelPowerInWatt())
            System.currentTimeMillis();
        receivedTrafficChannelPowerWatt = trafficChannelPower;
    }

    public void setUserid(int userid)
    {
        this.userid = userid;
    }

    public void setAllowedToConnect(boolean allowedToConnect)
    {
        this.allowedToConnect = allowedToConnect;
    }

    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }

    public void translateLocationX(double factor)
    {
        locationX += factor;
    }

    public void translateLocationY(double factor)
    {
        locationY += factor;
    }

    public double getMobilitySpeed()
    {
        return mobilitySpeed;
    }

    public double getTotalPowerReceivedFromBaseStationsNotInActiveSetWatt()
    {
        return totalPowerReceivedFromBaseStationsNotInActiveSet;
    }

    public double getTotalPowerReceivedFromBaseStationsNotInActiveSetdBm()
    {
        return CDMASystem.fromWatt2dBm(totalPowerReceivedFromBaseStationsNotInActiveSet);
    }

    public double getThermalNoise()
    {
        return thermalNoise;
    }

    public double getTotalPowerReceivedFromBaseStationsActiveSetInWatt()
    {
        return totalPowerReceivedFromBaseStationsInActiveSet;
    }

    public double getTotalPowerReceivedFromBaseStationsActiveSetIndBm()
    {
        return CDMASystem.fromWatt2dBm(getTotalPowerReceivedFromBaseStationsActiveSetInWatt());
    }

    public double getGeometry()
    {
        return geometry;
    }

    public double getReceivedTrafficChannelPowerdBm()
    {
        return CDMASystem.fromWatt2dBm(receivedTrafficChannelPowerWatt);
    }

    public int getUserid()
    {
        return userid;
    }

    public double getAchievedEcIor()
    {
        return achievedEcIor;
    }

    public double getRequiredEcIor()
    {
        return requiredEcIor;
    }

    public boolean isAllowedToConnect()
    {
        return allowedToConnect;
    }

    public boolean isConnected()
    {
        return connected;
    }

    public boolean voiceIsActive()
    {
        return voiceIsActive;
    }

    public boolean isInSoftHandover()
    {
        return activeList.size() > 1;
    }

    public boolean isInSofterHandover()
    {
        return isInSoftHandover() && ((CDMALink)activeList.get(0)).getCDMACell().getCellLocationId() == ((CDMALink)activeList.get(1)).getCDMACell().getCellLocationId();
    }

    public double getSoftHandoverState()
    {
        return 0.0D;
    }

    public boolean isUplinkMode()
    {
        return uplinkMode;
    }

    public void setUplinkMode(boolean uplinkMode)
    {
        this.uplinkMode = uplinkMode;
    }

    public int getMultiPathChannel()
    {
        return multiPathChannel;
    }

    public void setMultiPathChannel(int multiPathChannel)
    {
        this.multiPathChannel = multiPathChannel;
    }

    public double getCurrentTransmitPowerInWatt()
    {
        return currentTransmitPower;
    }

    public double getCurrentTransmitPowerIndBm()
    {
        return CDMASystem.fromWatt2dBm(currentTransmitPower);
    }

    public void setCurrentTransmitPowerWatt(double currentTransmitPower)
    {
        this.currentTransmitPower = currentTransmitPower;
    }

    public void setCurrentTransmitPower_dBm(double currentTransmitPower)
    {
        this.currentTransmitPower = CDMASystem.fromdBm2Watt(currentTransmitPower);
    }

    public double getMaxTxPower_dBm()
    {
        return maxTxPower;
    }

    public void setMaxTxPower_dBm(double maxTxPower)
    {
        this.maxTxPower = maxTxPower;
    }

    public double getMinTxPower_dBm()
    {
        return minTxPower;
    }

    public void setMinTxPower_dBm(double minTxPower)
    {
        this.minTxPower = minTxPower;
    }

    public double getConvergenceValue()
    {
        return convergenceValue;
    }

    public void setConvergenceValue(double convergenceValue)
    {
        this.convergenceValue = convergenceValue;
    }

    public double getAchievedCI()
    {
        return achievedCI;
    }

    public double getPowerControlStep()
    {
        return powerControlStep;
    }

    public void setPowerControlStep(double powerControlStep)
    {
        this.powerControlStep = powerControlStep;
    }

    public boolean isDropped()
    {
        return dropped;
    }

    public boolean isDroppedAsHighest()
    {
        return droppedAsHighest;
    }

    public void setDroppedAsHighest(boolean droppedAsHighest)
    {
        this.droppedAsHighest = droppedAsHighest;
    }

    public double calculateAntennaGainTo(double angle, double elevation)
    {
        return antennaGain;
    }

    public String getDropReason()
    {
        return dropReason;
    }

    public void setDropReason(String dropReason)
    {
        this.dropReason = dropReason;
    }

    public int getLinkQualityExceptions()
    {
        return linkQualityExceptions;
    }

    public void setLinkQualityExceptions(int linkQualityExceptions)
    {
        this.linkQualityExceptions = linkQualityExceptions;
    }

    public int getPowerScaledUpCount()
    {
        return powerScaledUpCount;
    }

    public void increasePowerScaledUpCount()
    {
        powerScaledUpCount++;
    }

    public void resetPowerScaledUpCount()
    {
        powerScaledUpCount = 0;
    }

    public double getOldAchievedCI()
    {
        return oldAchievedCI;
    }

    public void setOldAchievedCI(double oldAchievedCI)
    {
        this.oldAchievedCI = oldAchievedCI;
    }

    public CDMAUplink getActiveUplink()
    {
        if(!isUplinkMode())
            throw new IllegalStateException("This method should NOT be called in Downlink mode");
        else
            return activeUplink;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/UserTerminal);
    private boolean uplinkMode;
    private int userid;
    private double mobilitySpeed;
    private double achievedEcIor;
    private double requiredEcIor;
    private double maxTxPower;
    private double minTxPower;
    private double powerControlRange;
    private double powerControlStep;
    private double achievedCI;
    private double oldAchievedCI;
    private double geometry;
    private double totalPowerReceivedFromBaseStationsInActiveSet;
    private double totalPowerReceivedFromBaseStationsNotInActiveSet;
    private double receivedTrafficChannelPowerWatt;
    private double thermalNoise;
    private int multiPathChannel;
    private double currentTransmitPower;
    private double convergenceValue;
    private boolean voiceIsActive;
    private boolean allowedToConnect;
    private boolean connected;
    private boolean isInSoftHandover;
    private boolean dropped;
    private boolean droppedAsHighest;
    private CDMALinkLevelDataPoint linkLevelData;
    private CDMALink links[];
    private CDMAUplink activeUplink;
    private List activeList;
    private boolean linkLevelDataPointFound;
    private double antennaGain;
    private int linkQualityExceptions;
    private String dropReason;
    private int downlinkFreePassLimit;
    private int powerScaledUpCount;

}