// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMADownlink.java

package org.seamcat.cdma;

import org.apache.log4j.Logger;
import org.seamcat.cdma.exceptions.PowerSummationException;
import org.seamcat.model.technical.exception.ModelException;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.cdma:
//            CDMALink, CDMASystem, CDMACell, UserTerminal

public class CDMADownlink extends CDMALink
{

    public CDMADownlink(CDMACell _cell, UserTerminal _user, CDMASystem _cdmasystem)
    {
        super(_cell, _user, _cdmasystem);
    }

    public CDMACell getCDMACell()
    {
        return cell;
    }

    public double calculateTotalTransmitPower(double receivedPower)
    {
        double recP = CDMASystem.fromWatt2dBm(receivedPower);
        totalTransmitPower = (recP + getEffectivePathloss()) - userAntGain - bsAntGain;
        return totalTransmitPower;
    }

    public double calculateTransmittedTrafficChannelPowerIndBm()
    {
        try
        {
            double baseAntGain = getBsAntGain();
            double msAntGain = getUserAntGain();
            transmittedTrafficChannelPowerdBm = (getEffectivePathloss() - baseAntGain - msAntGain) + receivedTrafficChannelPowerdBm;
            double pMax = cdmasystem.getMaxTrafficChannelPowerIndBm();
            if(transmittedTrafficChannelPowerdBm > pMax)
            {
                double difference = transmittedTrafficChannelPowerdBm - pMax;
                transmittedTrafficChannelPowerdBm = pMax;
                receivedTrafficChannelPowerdBm -= difference;
                powerScaledDownToMax = true;
                powerScaledBy = "CDMAlink.scaleTransmitPower";
            } else
            {
                powerScaledDownToMax = false;
                powerScaledBy = "power not scaled";
            }
            return transmittedTrafficChannelPowerdBm;
        }
        catch(Exception e)
        {
            return 0.0D;
        }
    }

    public double calculateReceivePower()
        throws PowerSummationException
    {
        double value = 0.0D;
        double eff = getEffectivePathloss();
        double chPower = cell.getCurrentTransmitPower();
        value = (chPower + bsAntGain + userAntGain) - eff;
        totalReceivedPower = value;
        if(Double.isNaN(totalReceivedPower))
            throw new PowerSummationException("Error calculating received power");
        else
            return totalReceivedPower;
    }

    public void determinePathLoss()
    {
        try
        {
            setPathLoss(cdmasystem.getPropagationModel().evaluate(cdmasystem.getFrequency(), getDistance(), cell.getAntennaHeight(), user.getAntennaHeight()));
        }
        catch(ModelException e)
        {
            LOG.error("Error determining pathloss", e);
        }
    }

    public void determineMinimumCouplingLoss()
    {
        try
        {
            setMinimumCouplingLoss(cdmasystem.getPropagationModel().evaluate(cdmasystem.getFrequency(), getDistance(), cdmasystem.minBaseStationAntennaHeight(), cdmasystem.minUserTerminalAntennaHeight()));
        }
        catch(ModelException e)
        {
            LOG.error("Error determining MCL", e);
        }
    }

    public double getTransmittedTrafficChannelPowerdBm()
    {
        return transmittedTrafficChannelPowerdBm;
    }

    public double getTransmittedTrafficChannelPowerWatt()
    {
        return CDMASystem.fromdBm2Watt(transmittedTrafficChannelPowerdBm);
    }

    public double getTransmitPowerInWatt()
    {
        return CDMASystem.fromdBm2Watt(transmittedTrafficChannelPowerdBm);
    }

    public void scaleTransmitPower(double scaleValue)
    {
        transmittedTrafficChannelPowerdBm = CDMASystem.fromWatt2dBm(CDMASystem.fromdBm2Watt(transmittedTrafficChannelPowerdBm) * scaleValue);
        receivedTrafficChannelPowerdBm = (transmittedTrafficChannelPowerdBm - getEffectivePathloss()) + getBsAntGain() + getUserAntGain();
    }

    public String toString()
    {
        return (new StringBuilder()).append("BS #").append(cell.getCellid()).append(" -> MS #").append(user.getUserid()).append(" D= ").append((int)(getDistance() * 1000D)).append(" m; PL = ").append(CDMASystem.round(getPathLoss())).append(" dB; BsGain = ").append(CDMASystem.round(bsAntGain)).append("db; Angle = ").append(CDMASystem.round(angle)).append('\260').append(" ;Ptx = ").append(CDMASystem.round(getTotalTransmitPowerIndBm())).append(" dBm").toString();
    }

    public double getReceivedTrafficChannelPowerdBm()
    {
        return receivedTrafficChannelPowerdBm;
    }

    public void setReceivedTrafficChannelPowerdBm(double receivedTrafficChannelPowerdBm)
    {
        this.receivedTrafficChannelPowerdBm = receivedTrafficChannelPowerdBm;
    }

    protected double transmittedTrafficChannelPowerdBm;
    protected double receivedTrafficChannelPowerdBm;
}