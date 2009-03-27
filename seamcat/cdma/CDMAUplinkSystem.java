// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAUplinkSystem.java

package org.seamcat.cdma;

import java.util.*;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.cdma:
//            CDMASystem, UserTerminal, CDMAUplink, InterferenceGenerator, 
//            NonInterferedCapacityListener, CDMALinkLevelDataPoint, CDMACell

public class CDMAUplinkSystem extends CDMASystem
{

    public CDMAUplinkSystem(Element elem)
    {
        super(elem);
    }

    public CDMAUplinkSystem(CDMASystem sys)
    {
        super(sys);
    }

    public CDMAUplinkSystem()
    {
    }

    public void balancePower()
    {
        notifyBalancingStarted();
        calculateMinimumTransmitPower(mobileStationMaximumTransmitPower, mobileStationPowerControlRange);
        calculateProcessingGain();
        addAllUsersToSystem();
        int loopCount = internalPowerBalance();
        if(loopCount >= maximumPeakCount)
            setSnapshotShouldBeIgnored(true);
        if(loopCount > highestPcLoopCount)
            highestPcLoopCount = loopCount;
        calculateAverageNoiseRise_Linear();
        notifyBalancingComplete();
    }

    private void addAllUsersToSystem()
    {
        int i = 0;
        for(int stop = usersPerCell * numberOfCellSitesInPowerControlCluster; i < stop; i++)
        {
            UserTerminal user = newUserTerminal();
            if(user != null)
            {
                user.connectToBaseStationsUplink();
                activeUsers.add(user);
            }
        }

    }

    protected int findNonInterferedCapacity(InterferenceGenerator generator, boolean interferenceMode)
    {
        LOG.info((new StringBuilder()).append("Testing non interfered capacity. [N = ").append(usersPerCell).append(", deltaN = ").append(deltaN).append("]").toString());
        if(nonInterferedCapacityListener != null)
            nonInterferedCapacityListener.startingTest(usersPerCell, deltaN, numberOfTrials, true);
        calculateMinimumTransmitPower(mobileStationMaximumTransmitPower, mobileStationPowerControlRange);
        calculateProcessingGain();
        double networkNoiseRiseSum = 0.0D;
        for(int i = 0; i < numberOfTrials; i++)
        {
            if(useSmartCapacityFindingAlgorithm)
            {
                double currentEndResult = 10D * Math.log10(networkNoiseRiseSum / (double)numberOfTrials);
                if(currentEndResult > maxTargetNoiseRise)
                {
                    double linearyAverage = networkNoiseRiseSum / (double)i;
                    for(; i < numberOfTrials; i++)
                        networkNoiseRiseSum += linearyAverage;

                    continue;
                }
            }
            resetSystem(true);
            if(nonInterferedCapacityListener != null)
                nonInterferedCapacityListener.startingTrial(i);
            addAllUsersToSystem();
            if(interferenceMode)
            {
                generator.generateInterference(this);
                calculateInterference();
            }
            int loopCount = internalPowerBalance();
            if(loopCount > highestPcLoopCount)
                highestPcLoopCount = loopCount;
            double nr = calculateAverageNoiseRise_Linear();
            networkNoiseRiseSum += nr;
            double temp_meanNoiseRise = 10D * Math.log10(networkNoiseRiseSum / (double)(i + 1));
            if(nonInterferedCapacityListener != null)
                nonInterferedCapacityListener.endingTrial(temp_meanNoiseRise, nr <= targetNetworkNoiseRiseOverThermalNoise, i);
        }

        meanNoiseRiseOverTrials = 10D * Math.log10(networkNoiseRiseSum / (double)numberOfTrials);
        LOG.info((new StringBuilder()).append("Mean noise rise was: ").append(meanNoiseRiseOverTrials).append(" dB").toString());
        if(nonInterferedCapacityListener != null)
            nonInterferedCapacityListener.endingTest(usersPerCell, meanNoiseRiseOverTrials, true, numberOfTrials);
        if(meanNoiseRiseOverTrials >= minTargetNoiseRise)
            break MISSING_BLOCK_LABEL_496;
        if(!fineTuning)
            break MISSING_BLOCK_LABEL_476;
        if(deltaN == 1 && finalFineTuning)
        {
            nonInterferedCapacity = usersPerCell * numberOfCellSitesInPowerControlCluster;
            capacitySimulated = true;
            if(nonInterferedCapacityListener != null)
                nonInterferedCapacityListener.capacityFound(usersPerCell, meanNoiseRiseOverTrials, true);
            return usersPerCell;
        }
        deltaN = (int)Math.ceil((double)deltaN / 2D);
        usersPerCell += deltaN;
        return findNonInterferedCapacity(generator, interferenceMode);
        if(meanNoiseRiseOverTrials <= maxTargetNoiseRise)
            break MISSING_BLOCK_LABEL_580;
        fineTuning = true;
        if(deltaN == 1)
            finalFineTuning = true;
        else
            deltaN = (int)Math.ceil((double)deltaN / 2D);
        usersPerCell -= deltaN;
        if(usersPerCell > 0)
            return findNonInterferedCapacity(generator, interferenceMode);
        try
        {
            usersPerCell = 0;
            return 0;
        }
        catch(Exception ex)
        {
            LOG.error("An Error occured", ex);
        }
        break MISSING_BLOCK_LABEL_638;
        nonInterferedCapacity = usersPerCell * numberOfCellSitesInPowerControlCluster;
        capacitySimulated = true;
        if(nonInterferedCapacityListener != null)
            nonInterferedCapacityListener.capacityFound(usersPerCell, meanNoiseRiseOverTrials, true);
        return usersPerCell;
        return -1;
    }

    public void calculateInterference()
    {
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
                calculateExternalInterference(cells[i][j]);

        }

    }

    public void balanceInterferedSystem()
    {
        calculateInterference();
        boolean noiseRiseAboveLimit = true;
        int pcCounter = 0;
        do
        {
            if(!noiseRiseAboveLimit)
                break;
            pcCounter++;
            int loopCount = internalPowerBalance();
            double currentAverageNoiseRise = calculateAverageNoiseRise_dB();
            noiseRiseAboveLimit = currentAverageNoiseRise > getTargetNetworkNoiseRiseOverThermalNoise_dB();
            if(loopCount >= maximumPeakCount)
                noiseRiseAboveLimit = true;
            if(noiseRiseAboveLimit)
            {
                int absRiseAboveLimit = (int)Math.max(1.0D, Math.ceil(currentAverageNoiseRise - getTargetNetworkNoiseRiseOverThermalNoise_dB()));
                for(int i = 0; i < absRiseAboveLimit && activeUsers.size() > 0; i++)
                {
                    UserTerminal user = (UserTerminal)activeUsers.get(activeUsers.size() - 1);
                    user.setDroppedAsHighest(true);
                    dropActiveUser(user);
                }

                if(activeUsers.size() == 0)
                {
                    noiseRiseAboveLimit = false;
                    calculateAverageNoiseRise_Linear();
                }
            }
        } while(true);
        for(int i = 0; i < activeUsers.size(); i++)
        {
            UserTerminal user = (UserTerminal)activeUsers.get(i);
            double ci = user.calculateAchievedCI();
            double reqci = user.getLinkLevelData().getEbNo();
            double difference = reqci - ci;
            if(difference > callDropThreshold)
            {
                dropActiveUser(user);
                i--;
            }
        }

    }

    protected int internalPowerBalance()
    {
        boolean powerConverged = false;
        int peakCount = 0;
        do
        {
            if(powerConverged)
                break;
            powerConverged = true;
            peakCount++;
            UserTerminal u;
            for(Iterator i$ = activeUsers.iterator(); i$.hasNext(); u.calculateAchievedCI())
                u = (UserTerminal)i$.next();

            for(int i = 0; i < activeUsers.size(); i++)
            {
                UserTerminal u = (UserTerminal)activeUsers.get(i);
                double cToI_last_loop = u.getOldAchievedCI();
                double cToI_loop = u.getAchievedCI();
                double Sir_target = u.getLinkLevelData().getEbNo();
                double tx_required_last_loop = fromWatt2dBm(u.getCurrentTransmitPowerInWatt());
                double tx_required_loop = (tx_required_last_loop + Sir_target) - cToI_loop;
                if(tx_required_last_loop > tx_required_loop && tx_required_last_loop > -50D)
                    System.currentTimeMillis();
                u.setCurrentTransmitPower_dBm(Math.min(Math.max(tx_required_loop, u.getMinTxPower_dBm()), u.getMaxTxPower_dBm()));
                if(Math.abs(cToI_loop - cToI_last_loop) > powerControlConvergenceThreshold)
                    powerConverged = false;
            }

            if(peakCount > maximumPeakCount)
            {
                powerConverged = true;
                LOG.info((new StringBuilder()).append("Terminating CDMA Power Control loop after ").append(maximumPeakCount).append(" cycles").toString());
            }
        } while(true);
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
                cells[i][j].calculateTotalInterference_dBm(null);

        }

        return peakCount;
    }

    protected UserTerminal newUserTerminal()
    {
        UserTerminal user = generateUserTerminal();
        positionUser(user);
        initializeUser(user);
        boolean voice = trialVoiceActivity();
        if(voice)
        {
            user.setVoiceIsActive(true);
            user.setUplinkMode(true);
        } else
        {
            inactiveUserCount++;
            CDMAUplink l;
            for(Iterator i$ = user.getActiveList().iterator(); i$.hasNext(); l.getCDMACell().addVoiceInActiveUser(l))
                l = (CDMAUplink)i$.next();

            notifyInActiveUserAdded();
            return null;
        }
        user.setMultiPathChannel(1 + (int)Math.round(VOICE_ACTIVITY_RANDOM.nextDouble()));
        user.findLinkLevelDataPoint(getLinkLevelData());
        if(!user.linkLevelDataPointFound())
        {
            noLinkLevelFoundUsers.add(user);
            notifyUserIgnored();
            useridcount--;
            return null;
        } else
        {
            return user;
        }
    }

    public double calculateAverageNoiseRise_Linear()
    {
        int cellCounter = 0;
        double noiseRise = 0.0D;
        double temp = 0.0D;
        int j = 0;
        for(int bStop = cells.length; j < bStop; j++)
        {
            for(int k = 0; k < typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite(); k++)
            {
                CDMACell cell = cells[j][k];
                temp = cell.calculateNoiseRiseOverThermalNoise_LinearyFactor();
                noiseRise += temp;
                cellCounter++;
            }

        }

        averageNoiseRise = noiseRise / (double)cellCounter;
        if(averageNoiseRise > 15D)
            System.currentTimeMillis();
        return averageNoiseRise;
    }

    public double calculateAverageNoiseRise_dB()
    {
        return 10D * Math.log10(calculateAverageNoiseRise_Linear());
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/CDMAUplinkSystem);

}