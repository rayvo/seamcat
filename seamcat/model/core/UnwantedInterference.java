// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UnwantedInterference.java

package org.seamcat.model.core;

import org.apache.log4j.Logger;
import org.seamcat.function.Function2;
import org.seamcat.function.FunctionException;
import org.seamcat.model.*;
import org.seamcat.model.technical.exception.*;
import org.seamcat.propagation.PropagationModel;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.core:
//            Interference, InterferenceLink, InterferingSystemLink, InterferingTransmitter, 
//            PowerControl, VictimSystemLink

public class UnwantedInterference extends Interference
{

    public UnwantedInterference()
    {
    }

    public UnwantedInterference(Element element)
    {
        super((Element)element.getElementsByTagName("interference").item(0));
    }

    public InterferenceLink getInterferenceLink()
    {
        return interferenceLink;
    }

    public void setInterferenceLink(InterferenceLink interferenceLink)
    {
        this.interferenceLink = interferenceLink;
    }

    public double iRSSLinkBudgetUnwanted(boolean debug)
        throws ModelException, PatternException, MaskException
    {
        itVrPathAntGainsUnwanted(debug);
        double rItVrAntGain = getInterferenceLink().getTxRxAntennaGain();
        double rVrItAntGain = getInterferenceLink().getRxTxAntennaGain();
        itVrPropagationLossUnwanted(debug);
        double rItVrPathLoss = getInterferenceLink().getTxRxPathLoss();
        double rItPower = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getTxTrialPower();
        double rUnwantedEmissionRel = itUnwantedEmissions(debug);
        double rItPowerControlGain = 0.0D;
        if(getInterferenceLink().getInterferingLink().getInterferingTransmitter().getUsePowerControl())
        {
            rItPowerControlGain = getInterferenceLink().getInterferingLink().getPowerControl().getInterferingTransmitterPowerControlGain();
            if(debug)
                LOG.debug((new StringBuilder()).append("Using power control on Interfering Transmitter - power gain = ").append(rItPowerControlGain).toString());
        } else
        if(debug)
            LOG.debug("Not using power control -> Interfering Transmitter power gain = 0");
        double rUnwantedEmissionAbs = rItPower + rUnwantedEmissionRel + rItPowerControlGain;
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Interfering Transmitter Tx Power trial = ").append(rItPower).toString());
            LOG.debug((new StringBuilder()).append("Absolute Unwanted Emission = ItPower + RelUnwanted + ItPower Gain = ").append(rUnwantedEmissionAbs).toString());
        }
        if(getInterferenceLink().getInterferingLink().getInterferingTransmitter().getUseUnwantedEmissionFloor())
        {
            double rUnwantedReference = itUnwantedReference(debug);
            rUnwantedEmissionAbs = Math.max(rUnwantedEmissionAbs, rUnwantedReference);
            if(debug)
            {
                LOG.debug("Using unwanted emission floor");
                LOG.debug((new StringBuilder()).append("Absolute Unwanted Emission = MAX(Abs Unwanted, Ref Unwanted) = ").append(rUnwantedEmissionAbs).toString());
            }
        }
        unwantedPower = rUnwantedEmissionAbs;
        double riRSSValue = ((rUnwantedEmissionAbs + rItVrAntGain) - rItVrPathLoss) + rVrItAntGain;
        if(debug)
            LOG.debug((new StringBuilder()).append("iRSSValue = Abs UnwantedEmission + ItVrAntGain - ItVrPathLoss + VrItAntGain = ").append(riRSSValue).append(" = ").append(rUnwantedEmissionAbs).append(" + ").append(rItVrAntGain).append(" - ").append(rItVrPathLoss).append(" + ").append(rVrItAntGain).toString());
        return riRSSValue;
    }

    public void itVrPropagationLossUnwanted(boolean debug)
        throws ModelException
    {
        PropagationModel model = getInterferenceLink().getWt2VrPath().getPropagationModel();
        double rFreq = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getItTrialFrequency();
        double rDist = getInterferenceLink().getTxRxDistance();
        double rRxAntHeight = getInterferenceLink().getVictimLink().getVictimReceiver().getRxTrialAntHeight();
        double rTxAntHeight = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getTxTrialAntHeight();
        double rRxAntGain = getInterferenceLink().getRxTxAntennaGain();
        double rTxAntGain = getInterferenceLink().getTxRxAntennaGain();
        double rPropLossResult;
        try
        {
            rPropLossResult = model.evaluate(rFreq, rDist, rTxAntHeight, rRxAntHeight);
            if(debug)
                LOG.debug((new StringBuilder()).append("<").append(model.getClass().getName()).append("> ").append(rPropLossResult).append(" = evaluate(").append(rFreq).append(",").append(rDist).append(",").append(rRxAntHeight).append(",").append(rTxAntHeight).append(")").toString());
        }
        catch(Exception exception)
        {
            throw new ModelException();
        }
        getInterferenceLink().setTxRxPathLoss(rPropLossResult);
    }

    public void itVrPathAntGainsUnwanted(boolean debug)
        throws PatternException
    {
        double rFreqVr = getInterferenceLink().getVictimLink().getVictimReceiver().getVrTrialFrequency();
        double rRxTxAzi = getInterferenceLink().getRxTxAzimuth();
        double rRxTxElev = getInterferenceLink().getRxTxElevation();
        double rTxRxAzi = getInterferenceLink().getTxRxAzimuth();
        double rTxRxElev = getInterferenceLink().getTxRxElevation();
        double rVrPeakGain = getInterferenceLink().getVictimLink().getVictimReceiver().getAntenna().getPeakGain();
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Victim Receiver Frequency = ").append(rFreqVr).toString());
            LOG.debug((new StringBuilder()).append("Interference Link Rx -> Tx Azimuth = ").append(rRxTxAzi).toString());
            LOG.debug((new StringBuilder()).append("Interference Link Rx -> Tx Elevation = ").append(rRxTxElev).toString());
            LOG.debug((new StringBuilder()).append("Interference Link Tx -> Rx Azimuth = ").append(rTxRxAzi).toString());
            LOG.debug((new StringBuilder()).append("Interference Link Tx -> Rx Elevation = ").append(rTxRxElev).toString());
            LOG.debug((new StringBuilder()).append("Victim Receiver Antenna Peak Gain = ").append(rVrPeakGain).toString());
            LOG.debug("Victim Receiver -> Interfering Transmitter Antenna Gain calculation:");
        }
        try
        {
            double rVrGainResult = getInterferenceLink().getVictimLink().getVictimReceiver().getAntenna().calculateGain(rRxTxAzi, rRxTxElev);
            if(debug)
                LOG.debug((new StringBuilder()).append("Interference Link - Rx -> Tx Antenna Gain = ").append(rVrGainResult).toString());
            getInterferenceLink().setRxTxAntennaGain(rVrGainResult);
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on Vr->It antenna: ").append(e.getMessage()).toString(), e);
        }
        double rItPeakGain = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getAntenna().getPeakGain();
        if(debug)
        {
            LOG.debug("Interfering Transmitter Antenna Peak Gain");
            LOG.debug("Interfering Transmitter -> Victim Receiver Antenna Gain calculation:");
        }
        try
        {
            double rItGainResult = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getAntenna().calculateGain(rTxRxAzi, rTxRxElev);
            if(debug)
                LOG.debug((new StringBuilder()).append("Interference Link - Tx -> Rx Antenna Gain = ").append(rItGainResult).toString());
            getInterferenceLink().setTxRxAntennaGain(rItGainResult);
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on It->Vr antenna: ").append(e.getMessage()).toString(), e);
        }
    }

    public double itUnwantedEmissions(boolean debug)
        throws MaskException
    {
        double rFreqIt = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getItTrialFrequency();
        double rFreqVr = getInterferenceLink().getVictimLink().getVictimReceiver().getVrTrialFrequency();
        Function2 unWantEmission = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getUnwantedEmissions();
        double rVrBandwidth = getInterferenceLink().getVictimLink().getVictimReceiver().getBandwidth();
        double rResult;
        try
        {
            rResult = unWantEmission.integrate(rFreqVr - rFreqIt, rVrBandwidth);
            if(debug)
            {
                LOG.debug((new StringBuilder()).append("Interfering Transmitter Frequency = ").append(rFreqIt).toString());
                LOG.debug((new StringBuilder()).append("Victim Receiver Frequency = ").append(rFreqVr).toString());
                LOG.debug((new StringBuilder()).append("Victim Receiver Bandwith = ").append(rVrBandwidth).toString());
                LOG.debug((new StringBuilder()).append("Relative Unwanted Emission = .integrate((VrFreq - ItFreq), VrBandwith) = ").append(rResult).append(" dBc").toString());
            }
        }
        catch(FunctionException e)
        {
            LOG.debug((new StringBuilder()).append("integrate(VrFreq - ItFreq, VrBandwidth) = integrate(").append(rFreqVr).append(" - ").append(rFreqIt).append(", ").append(rVrBandwidth).append(") threw FunctionException").toString());
            throw new MaskException(e.getMessage());
        }
        return rResult;
    }

    public double itUnwantedReference(boolean debug)
        throws MaskException
    {
        double rFreqIt = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getItTrialFrequency();
        double rFreqVr = getInterferenceLink().getVictimLink().getVictimReceiver().getVrTrialFrequency();
        double rVrBandwidth = getInterferenceLink().getVictimLink().getVictimReceiver().getBandwidth();
        Function2 unWantReference = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getUnwantedEmissionsFloor();
        double rResult;
        try
        {
            rResult = unWantReference.integrate(rFreqVr - rFreqIt, rVrBandwidth);
            if(debug)
            {
                LOG.debug((new StringBuilder()).append("Interfering Transmitter Frequency = ").append(rFreqIt).toString());
                LOG.debug((new StringBuilder()).append("Victim Receiver Frequency = ").append(rFreqVr).toString());
                LOG.debug((new StringBuilder()).append("Victim Receiver Bandwith = ").append(rVrBandwidth).toString());
                LOG.debug((new StringBuilder()).append("Reference Unwanted Emission = .integrate((VrFreq - ItFreq), VrBandwith) = ").append(rResult).append(" dBc").toString());
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw new MaskException();
        }
        return rResult;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("UnwantedInterference");
        element.appendChild(super.toElement(doc));
        return element;
    }

    public double getUnwantedPower()
    {
        return unwantedPower;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/core/UnwantedInterference);
    private InterferenceLink interferenceLink;
    private double unwantedPower;

}
