// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAUplink.java

package org.seamcat.cdma;

import org.apache.log4j.Logger;
import org.seamcat.model.technical.exception.ModelException;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.cdma:
//            CDMALink, UserTerminal, CDMASystem, CDMACell, 
//            CDMALinkLevelDataPoint

public class CDMAUplink extends CDMALink
{

    public CDMAUplink(CDMACell _cell, UserTerminal _user, CDMASystem _cdmasystem)
    {
        super(_cell, _user, _cdmasystem);
    }

    public double calculateTotalTransmitPower(double receivePower)
    {
        double eff = getEffectivePathloss();
        totalTransmitPower = (receivePower + eff) - bsAntGain - userAntGain;
        if(totalTransmitPower > user.getMaxTxPower_dBm())
            totalTransmitPower = user.getMaxTxPower_dBm();
        else
        if(totalTransmitPower < user.getMinTxPower_dBm())
            totalTransmitPower = user.getMinTxPower_dBm();
        user.setCurrentTransmitPower_dBm(totalTransmitPower);
        return totalTransmitPower;
    }

    public boolean decreaseTransmitPower()
    {
        boolean value = true;
        totalTransmitPower -= user.getPowerControlStep();
        if(totalTransmitPower < user.getMinTxPower_dBm())
        {
            totalTransmitPower = user.getMinTxPower_dBm();
            value = false;
        }
        return value;
    }

    public double calculateAchivedCI()
    {
        double procGain = cdmasystem.getProcessingGain();
        double Itotal = getCDMACell().calculateTotalInterference_dBm(this);
        double recPower = calculateCurrentReceivePower_dBm();
        double achCI = (procGain + recPower) - Itotal;
        return achCI;
    }

    public CDMACell getCDMACell()
    {
        return cell;
    }

    public double calculateReceivePower()
    {
        double value = 0.0D;
        double ebNoTarget = user.getLinkLevelData().getEbNo();
        double totalInterference = cell.calculateTotalInterference_dBm(this);
        double procGain = cdmasystem.getProcessingGain();
        value = (ebNoTarget + totalInterference) - procGain;
        totalReceivedPower = value;
        return totalReceivedPower;
    }

    public double calculateInitialReceivePower()
    {
        double thermalNoise_dB = cdmasystem.getThermalNoiseIndB();
        double noiseRise_target_dB = cdmasystem.getTargetNetworkNoiseRiseOverThermalNoise_dB();
        double sir_target_db = user.getLinkLevelData().getEbNo();
        double processing_gain_db = cdmasystem.getProcessingGain();
        totalReceivedPower = (thermalNoise_dB + noiseRise_target_dB + sir_target_db) - processing_gain_db;
        return totalReceivedPower;
    }

    public void determinePathLoss()
    {
        try
        {
            setPathLoss(cdmasystem.getPropagationModel().evaluate(cdmasystem.getFrequency(), getDistance(), cdmasystem.trialUserTerminalAntennaHeight(), cdmasystem.trialBaseStationAntennaHeight()));
        }
        catch(ModelException e)
        {
            LOG.error("Error while determining pathloss", e);
        }
    }

    public void determineMinimumCouplingLoss()
    {
        try
        {
            setMinimumCouplingLoss(cdmasystem.getPropagationModel().evaluate(cdmasystem.getFrequency(), getDistance(), cdmasystem.minUserTerminalAntennaHeight(), cdmasystem.minBaseStationAntennaHeight()));
        }
        catch(ModelException e)
        {
            LOG.error("Error while determining MCL", e);
        }
    }

    public double initializePowerLevels()
    {
        calculateInitialReceivePower();
        calculateTotalTransmitPower(totalReceivedPower);
        double achCI = calculateAchivedCI();
        return achCI;
    }

    public String toString()
    {
        return (new StringBuilder()).append("CDMAUpLink: Distance is: ").append((int)(getDistance() * 1000D)).append(" meters PL is: ").append(CDMASystem.round(getEffectivePathloss())).append(" dB,").append(" Tx Power: ").append(getTotalTransmitPowerIndBm()).append(" dBm").append("Rx Power: ").append(getReceivePower_dB()).append(" dBm").toString();
    }
}