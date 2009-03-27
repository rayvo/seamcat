// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   IntermodulationInterference.java

package org.seamcat.model.core;

import org.seamcat.function.Function;
import org.seamcat.model.VictimReceiver;
import org.seamcat.model.technical.exception.MaskException;

// Referenced classes of package org.seamcat.model.core:
//            Interference, InterferenceLink, BlockingInterference, VictimSystemLink, 
//            InterferingSystemLink, InterferingTransmitter, PowerControl

public class IntermodulationInterference extends Interference
{

    public IntermodulationInterference()
    {
    }

    public BlockingInterference getBlockingInterference1()
    {
        return blockingInterference1;
    }

    public void setBlockingInterference1(BlockingInterference blockingInterference1)
    {
        this.blockingInterference1 = blockingInterference1;
    }

    public BlockingInterference getBlockingInterference2()
    {
        return blockingInterference2;
    }

    public void setBlockingInterference2(BlockingInterference blockingInterference2)
    {
        this.blockingInterference2 = blockingInterference2;
    }

    public void detectionInterMod()
    {
    }

    public double vrInterModResponse()
        throws MaskException
    {
        InterferenceLink if1 = getBlockingInterference1().getInterferenceLink();
        InterferenceLink if2 = getBlockingInterference2().getInterferenceLink();
        double rVrBandWidth = if2.getVictimLink().getVictimReceiver().getBandwidth();
        Function IntermodResponse = if2.getVictimLink().getVictimReceiver().getIntermodulationRejection();
        double rFreqVr = if2.getVictimLink().getVictimReceiver().getVrTrialFrequency();
        double rFreq1 = if1.getInterferingLink().getInterferingTransmitter().getItTrialFrequency();
        double rFreq2 = if2.getInterferingLink().getInterferingTransmitter().getItTrialFrequency();
        double rFreq0 = 2D * rFreq1 - rFreq2;
        double rResultIntermod;
        try
        {
            rResultIntermod = IntermodResponse.evaluate(rFreq0 - rFreqVr, rVrBandWidth);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new MaskException();
        }
        return rResultIntermod;
    }

    public double iRSSLinkBudgetInterMod()
        throws MaskException
    {
        InterferenceLink if1 = getBlockingInterference1().getInterferenceLink();
        InterferenceLink if2 = getBlockingInterference2().getInterferenceLink();
        double rItPower1 = if1.getInterferingLink().getInterferingTransmitter().getTxTrialPower();
        double rItPower2 = if2.getInterferingLink().getInterferingTransmitter().getTxTrialPower();
        double rItVrAntGain1 = if1.getTxRxAntennaGain();
        double rVrItAntGain1 = if1.getRxTxAntennaGain();
        double rItVrAntGain2 = if2.getTxRxAntennaGain();
        double rVrItAntGain2 = if2.getRxTxAntennaGain();
        double rItVrPathLoss1 = if1.getTxRxPathLoss();
        double rItVrPathLoss2 = if2.getTxRxPathLoss();
        double riRSSValue1;
        if(if1.getInterferingLink().getPowerControl() != null)
        {
            double rItPowerControl1 = if1.getInterferingLink().getPowerControl().getInterferingTransmitterPowerControlGain();
            riRSSValue1 = ((rItPower1 + rItPowerControl1 + rItVrAntGain1) - rItVrPathLoss1) + rVrItAntGain1;
        } else
        {
            riRSSValue1 = ((rItPower1 + rItVrAntGain1) - rItVrPathLoss1) + rVrItAntGain1;
        }
        double riRSSValue2;
        if(if2.getInterferingLink().getPowerControl() != null)
        {
            double rItPowerControl2 = if2.getInterferingLink().getPowerControl().getInterferingTransmitterPowerControlGain();
            riRSSValue2 = ((rItPower2 + rItPowerControl2 + rItVrAntGain2) - rItVrPathLoss2) + rVrItAntGain2;
        } else
        {
            riRSSValue2 = ((rItPower2 + rItVrAntGain2) - rItVrPathLoss2) + rVrItAntGain2;
        }
        double rIntermodResponse = vrInterModResponse();
        double rSens = if2.getVictimLink().getVictimReceiver().getSensitivity();
        double rIntermodValue;
        if(rIntermodResponse >= 1.7976931348623157E+308D)
            rIntermodValue = -1000D;
        else
            rIntermodValue = (2D * riRSSValue1 + riRSSValue2) - 3D * rIntermodResponse - 3D * rSens - 9D;
        return rIntermodValue;
    }

    public void iRSSSummationInterMod()
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    private BlockingInterference blockingInterference1;
    private BlockingInterference blockingInterference2;
}