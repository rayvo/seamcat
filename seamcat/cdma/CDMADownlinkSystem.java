// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMADownlinkSystem.java

package org.seamcat.cdma;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.cdma.exceptions.PowerSummationException;
import org.seamcat.cdma.exceptions.ScalingException;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.cdma:
//            CDMASystem, UserTerminal, CDMADownlink, DebugDownlinkUserTerminal, 
//            InterferenceGenerator, CDMACell, NonInterferedCapacityListener, CDMALinkLevelDataPoint

public class CDMADownlinkSystem extends CDMASystem
{

    public CDMADownlinkSystem(Element elem)
    {
        super(elem);
        insertDebugProbeUser = false;
    }

    public CDMADownlinkSystem(CDMASystem sys)
    {
        super(sys);
        insertDebugProbeUser = false;
    }

    public CDMADownlinkSystem()
    {
        insertDebugProbeUser = false;
    }

    public void balancePower()
    {
        notifyBalancingStarted();
        calculateProcessingGain();
        int j = 0;
        for(int bStop = cells.length; j < bStop; j++)
        {
            for(int k = 0; k < typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite(); k++)
                cells[j][k].initializeTransmitPowerLevels();

        }

        try
        {
            int i = 0;
            for(int stop = usersPerCell * numberOfCellSitesInPowerControlCluster; i < stop; i++)
            {
                UserTerminal user = newUserTerminal();
                if(user == null)
                    continue;
                if(user.connectToBaseStationsDownlink())
                {
                    user.setAllowedToConnect(true);
                    activeUsers.add(user);
                    notifyUserAdded(user);
                } else
                {
                    dropActiveUser(user);
                    user.setDropReason("Unable to connect");
                    notActivatedUsers.add(user);
                    notifyUserNotActivated(user);
                }
            }

            internalPowerBalance();
        }
        catch(Exception ex)
        {
            LOG.error("Error scaling power", ex);
        }
        notifyBalancingComplete();
    }

    protected int internalPowerBalance()
    {
        boolean powerConverged;
        double maxPower;
        int powerLoopCount;
        powerConverged = false;
        maxPower = CDMASystem.fromWatt2dBm(cells[0][0].getMaximumChannelPower_Watt());
        powerLoopCount = 0;
_L9:
        int i;
        UserTerminal user;
        if(powerConverged)
            break; /* Loop/switch isn't completed */
        powerConverged = true;
        powerLoopCount++;
        for(i = 0; i < activeUsers.size(); i++)
        {
            user = (UserTerminal)activeUsers.get(i);
            user.calculateReceivedPower();
            user.calculateGeometry();
            user.findLinkLevelDataPoint(getLinkLevelData());
        }

        int j = 0;
        for(int bStop = cells.length; j < bStop; j++)
        {
            for(int k = 0; k < cells[j].length; k++)
            {
                CDMACell cell = cells[j][k];
                if(cell.countServedUsers() <= 0)
                    continue;
                double curPower = cell.calculateCurrentChannelPower_dBm();
                if(curPower > maxPower && curPower - maxPower > 0.0001D)
                {
                    cell.scaleChannelPower();
                    powerConverged = false;
                }
            }

        }

        if(!powerConverged)
            continue; /* Loop/switch isn't completed */
        j = 0;
_L7:
        int freePassCount;
        if(j >= activeUsers.size())
            break; /* Loop/switch isn't completed */
        bStop = (UserTerminal)activeUsers.get(j);
        bStop.calculateAchievedEcIor();
        freePassCount = bStop.getLinkQualityExceptions();
        if(bStop.meetsEcIorRequirement(getSuccessThreshold(), true)) goto _L2; else goto _L1
_L1:
        if(bStop.getPowerScaledUpCount() <= 2) goto _L4; else goto _L3
_L3:
        dropActiveUser(bStop);
        bStop.setDropReason("Power scaled up too many times");
        powerConverged = false;
          goto _L5
_L4:
        bStop.calculateReceivedTrafficChannelPowerInWatt();
        bStop.increasePowerScaledUpCount();
        powerConverged = false;
_L2:
        bStop.setLinkQualityExceptions(freePassCount);
_L5:
        j++;
        if(true) goto _L7; else goto _L6
_L6:
        if(true) goto _L9; else goto _L8
_L8:
        for(j = 0; j < activeUsers.size(); j++)
        {
            bStop = (UserTerminal)activeUsers.get(j);
            bStop.calculateReceivedPower();
            bStop.calculateGeometry();
            bStop.findLinkLevelDataPoint(getLinkLevelData());
            bStop.calculateAchievedEcIor();
            if(!bStop.meetsEcIorRequirement(getSuccessThreshold(), true))
            {
                dropActiveUser(bStop);
                bStop.setDropReason("due to success threshold");
                j--;
            }
        }

        break MISSING_BLOCK_LABEL_427;
        ScalingException ex;
        ex;
        LOG.error("Error scaling power", ex);
        break MISSING_BLOCK_LABEL_427;
        ex;
        LOG.error("An Error occured", ex);
        return 0;
    }

    protected int findNonInterferedCapacity(InterferenceGenerator generator, boolean interferenceMode)
    {
        int trialThreshold;
        LOG.info((new StringBuilder()).append("Finding non interfered capacity. [N = ").append(usersPerCell).append(", deltaN = ").append(deltaN).append("]").toString());
        if(nonInterferedCapacityListener != null)
            nonInterferedCapacityListener.startingTest(usersPerCell, deltaN, numberOfTrials, false);
        trialThreshold = (int)Math.ceil((double)(usersPerCell * numberOfCellSitesInPowerControlCluster) * systemTolerance);
        int dropped[];
        int smartStopIndex;
        int i;
        calculateProcessingGain();
        dropped = new int[numberOfTrials];
        smartStopIndex = 0;
        i = 0;
_L1:
        if(i >= dropped.length)
            break MISSING_BLOCK_LABEL_657;
        if(isStopped())
            return -1;
label0:
        {
            int j;
            if(useSmartCapacityFindingAlgorithm)
            {
                int currentSuccess = 0;
                for(j = 0; j < i; j++)
                    if(dropped[j] <= trialThreshold)
                        currentSuccess++;

                if((double)currentSuccess > (double)numberOfTrials * succesCriteria)
                {
                    smartStopIndex = i;
                    for(; i < dropped.length; i++)
                        dropped[i] = -1;

                    break label0;
                }
                if((double)(currentSuccess + (dropped.length - i)) < (double)numberOfTrials * succesCriteria)
                {
                    smartStopIndex = i;
                    for(; i < dropped.length; i++)
                        dropped[i] = trialThreshold + 1;

                    break label0;
                }
            }
            resetSystem(true);
            if(nonInterferedCapacityListener != null)
                nonInterferedCapacityListener.startingTrial(i);
            int j = 0;
            for(int bStop = cells.length; j < bStop; j++)
            {
                for(int k = 0; k < typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite(); k++)
                    cells[j][k].initializeTransmitPowerLevels();

            }

            bStop = 0;
label1:
            for(int stop = usersPerCell * numberOfCellSitesInPowerControlCluster; bStop < stop; bStop++)
            {
                UserTerminal user = newUserTerminal();
                if(user == null)
                    continue;
                if(user.connectToBaseStationsDownlink())
                {
                    user.setAllowedToConnect(true);
                    activeUsers.add(user);
                    notifyUserAdded(user);
                    Iterator i$ = user.getActiveList().iterator();
                    do
                    {
                        if(!i$.hasNext())
                            continue label1;
                        CDMADownlink link = (CDMADownlink)i$.next();
                        CDMACell cell = link.getCDMACell();
                        if(cell.getCurrentTransmitPower() > cell.getMaximumTransmitPower_dBm())
                            cell.scaleChannelPower();
                        cell.calculateCurrentChannelPower_dBm();
                    } while(true);
                }
                dropActiveUser(user);
                user.setDropReason("Unable to connect");
                notActivatedUsers.add(user);
                notifyUserNotActivated(user);
            }

            bStop = 0;
            for(int bStop = cells.length; bStop < bStop; bStop++)
            {
                for(int k = 0; k < cells[bStop].length; k++)
                {
                    CDMACell cell = cells[bStop][k];
                    cell.calculateCurrentChannelPower_dBm();
                }

            }

            if(interferenceMode)
            {
                generator.generateInterference(this);
                calculateInterference();
            }
            internalPowerBalance();
            dropped[i] = droppedUsers.size();
            if(nonInterferedCapacityListener != null)
                nonInterferedCapacityListener.endingTrial(dropped[i], dropped[i] <= trialThreshold, i);
            waitALittle();
        }
        i++;
          goto _L1
        int succes;
        succes = 0;
        int stop = dropped.length;
        if(smartStopIndex > 0)
            stop = smartStopIndex;
        for(int i = 0; i < stop; i++)
            if(dropped[i] <= trialThreshold)
                succes++;

        if(nonInterferedCapacityListener != null)
            nonInterferedCapacityListener.endingTest(usersPerCell, succes, false, smartStopIndex);
        LOG.info((new StringBuilder()).append("Number of succesfull trials was: ").append(succes).append(" out of ").append(numberOfTrials).toString());
        if((double)succes <= (double)numberOfTrials * succesCriteria)
            break MISSING_BLOCK_LABEL_896;
        if(!fineTuning)
            break MISSING_BLOCK_LABEL_876;
        if(deltaN == 1 && finalFineTuning)
        {
            nonInterferedCapacity = usersPerCell * numberOfCellSitesInPowerControlCluster;
            succesRate = succes;
            capacitySimulated = true;
            if(nonInterferedCapacityListener != null)
                nonInterferedCapacityListener.capacityFound(usersPerCell, succes, false);
            return usersPerCell;
        }
        deltaN = (int)Math.ceil((double)deltaN / 2D);
        usersPerCell += deltaN;
        return findNonInterferedCapacity(generator, interferenceMode);
        if((double)succes < (double)numberOfTrials * succesCriteria)
        {
            fineTuning = true;
            if(deltaN == 1)
                finalFineTuning = true;
            else
                deltaN = (int)Math.ceil((double)deltaN / 2D);
            usersPerCell -= deltaN;
            return findNonInterferedCapacity(generator, interferenceMode);
        }
        try
        {
            nonInterferedCapacity = usersPerCell * numberOfCellSitesInPowerControlCluster;
            succesRate = succes;
            capacitySimulated = true;
            if(nonInterferedCapacityListener != null)
                nonInterferedCapacityListener.capacityFound(usersPerCell, succes, false);
            return usersPerCell;
        }
        catch(Exception ex)
        {
            LOG.error("An Error occured", ex);
        }
        return -1;
    }

    public void calculateInterference()
    {
        for(int i = 0; i < activeUsers.size(); i++)
        {
            UserTerminal user = (UserTerminal)activeUsers.get(i);
            calculateExternalInterference(user);
            user.calculateTotalInterference();
            user.calculateGeometry();
            user.findLinkLevelDataPoint(getLinkLevelData());
            if(user.getLinkLevelData().getEcIor() > 0.0D)
            {
                LOG.debug((new StringBuilder()).append("Dropping user due to high requirement: ").append(user).toString());
                dropActiveUser(user);
                user.setDropReason("Ec/Ior requirement to high");
                i--;
            }
        }

    }

    public void balanceInterferedSystem()
    {
        calculateInterference();
        internalPowerBalance();
    }

    protected UserTerminal newUserTerminal()
    {
        UserTerminal user = generateUserTerminal();
        if(insertDebugProbeUser)
        {
            user = new DebugDownlinkUserTerminal(0.0D, 0.0D, this, useridcount++, trialUserTerminalAntennaGain(), trialUserTerminalAntennaHeight());
            user.setThermalNoise(getThermalNoiseInWatt());
            insertDebugProbeUser = false;
        }
        positionUser(user);
        initializeUser(user);
        boolean voice = trialVoiceActivity();
        if(voice)
        {
            user.setVoiceIsActive(true);
        } else
        {
            CDMADownlink l;
            for(Iterator i$ = user.getActiveList().iterator(); i$.hasNext(); l.getCDMACell().addVoiceInActiveUser(l))
                l = (CDMADownlink)i$.next();

            inactiveUserCount++;
            notifyInActiveUserAdded();
            return null;
        }
        try
        {
            user.calculateInitialReceivedPower();
            user.calculateGeometry();
            user.findLinkLevelDataPoint(getLinkLevelData());
            user.calculateReceivedTrafficChannelPowerInWatt();
        }
        catch(Exception e)
        {
            if(!user.linkLevelDataPointFound())
            {
                noLinkLevelFoundUsers.add(user);
                notifyUserIgnored();
                useridcount--;
                return null;
            } else
            {
                LOG.warn("Error calulating Ptraf", e);
                return null;
            }
        }
        return user;
    }

    public double getSuccessThreshold()
    {
        return successThreshold;
    }

    public void setSuccessThreshold(double successThreshold)
    {
        this.successThreshold = successThreshold;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/CDMADownlinkSystem);
    protected boolean insertDebugProbeUser;

}