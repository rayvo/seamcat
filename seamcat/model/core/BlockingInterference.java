// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BlockingInterference.java

package org.seamcat.model.core;

import org.apache.log4j.Logger;
import org.seamcat.function.Function;
import org.seamcat.model.*;
import org.seamcat.model.technical.exception.*;
import org.seamcat.propagation.PropagationModel;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.core:
//            Interference, InterferenceLink, InterferingSystemLink, InterferingTransmitter, 
//            PowerControl, VictimSystemLink

public class BlockingInterference extends Interference
{

    public BlockingInterference(Element element)
    {
        super((Element)element.getElementsByTagName("interference").item(0));
        bStoppedAttenuation = true;
        bStoppedAttenuation = Boolean.valueOf(element.getAttribute("bStoppedAttenuation")).booleanValue();
    }

    public BlockingInterference()
    {
        bStoppedAttenuation = true;
        bStoppedAttenuation = true;
    }

    public InterferenceLink getInterferenceLink()
    {
        return interferenceLink;
    }

    public void setInterferenceLink(InterferenceLink interferenceLink)
    {
        this.interferenceLink = interferenceLink;
    }

    public double iRSSLinkBudgetBlock(boolean debug)
        throws PatternException, ModelException, MaskException
    {
        double rItPower = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getTxTrialPower();
        itVrPathAntGainsBlock(debug);
        double rItVrAntGain = getInterferenceLink().getTxRxAntennaGain();
        double rVrItAntGain = getInterferenceLink().getRxTxAntennaGain();
        double rItVrPathLoss = getInterferenceLink().getTxRxPathLoss();
        double rAttenuation = vrAttenuation(debug);
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Interfering Transmitter power = ").append(rItPower).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter -> Victim Receiver Antenna Gain = ").append(rItVrAntGain).toString());
            LOG.debug((new StringBuilder()).append("Victim Receiver -> Interfering Transmitter Antenna Gain = ").append(rVrItAntGain).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter -> Victim Receiver Path Loss = ").append(rItVrPathLoss).toString());
            LOG.debug((new StringBuilder()).append("Victim Receiver Attenuation = ").append(rAttenuation).toString());
        }
        double riRSSValue;
        if(getInterferenceLink().getInterferingLink().getInterferingTransmitter().getUsePowerControl())
        {
            if(debug)
                LOG.debug("Using power control");
            double rItPowerControl = getInterferenceLink().getInterferingLink().getPowerControl().getInterferingTransmitterPowerControlGain();
            riRSSValue = ((rItPower + rItPowerControl + rItVrAntGain) - rItVrPathLoss - rAttenuation) + rVrItAntGain;
            if(debug)
                LOG.debug((new StringBuilder()).append("iRSSValue = ").append(riRSSValue).append(" = ").append(rItPower).append(" + ").append(rItPowerControl).append(" + ").append(rItVrAntGain).append(" - ").append(rItVrPathLoss).append(" - ").append(rAttenuation).append(" + ").append(rVrItAntGain).toString());
            return riRSSValue;
        }
        if(debug)
            LOG.debug("NOT using power control");
        riRSSValue = ((rItPower + rItVrAntGain) - rItVrPathLoss - rAttenuation) + rVrItAntGain;
        if(debug)
            LOG.debug((new StringBuilder()).append("iRSSValue = ").append(riRSSValue).append(" = ").append(rItPower).append(" + ").append(rItVrAntGain).append(" - ").append(rItVrPathLoss).append(" - ").append(rAttenuation).append(" + ").append(rVrItAntGain).toString());
        return riRSSValue;
    }

    public void iRSSSummationBlock(boolean debug)
    {
        throw new UnsupportedOperationException("iRSSSummationBlock has NOT been implemented");
    }

    public double vrAttenuation(boolean debug)
        throws MaskException
    {
        double rFreqIt = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getItTrialFrequency();
        double rFreqVr = getInterferenceLink().getVictimLink().getVictimReceiver().getVrTrialFrequency();
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Cached Interfering Transmitter Frequency = ").append(rFreqIt).toString());
            LOG.debug((new StringBuilder()).append("Cached Victim Receiver Frequency = ").append(rFreqVr).toString());
        }
        double offset = 0.0D;
        if(bStoppedAttenuation)
        {
            if(getInterferenceLink().getVictimLink().getVictimReceiver().getBlockingAttenuationMode() == 1)
            {
                if(debug)
                    LOG.debug("Attenuation Mode = Protection ratio");
                offset = vrAttenuationProcRatio(debug);
            } else
            if(getInterferenceLink().getVictimLink().getVictimReceiver().getBlockingAttenuationMode() == 2)
            {
                if(debug)
                    LOG.debug("Attenuation Mode = Sensitivity");
                offset = vrAttenuationSens(debug);
            }
            bStoppedAttenuation = false;
        }
        Function blockingAttenuation = getInterferenceLink().getVictimLink().getVictimReceiver().getBlockingResponse();
        blockingAttenuation.offset(offset);
        double rResult;
        try
        {
            rResult = blockingAttenuation.evaluate(rFreqIt - rFreqVr);
            if(debug)
                LOG.debug((new StringBuilder()).append("Victim Receiver Blocking Response.evaluate(FrequencyIT (").append(rFreqIt).append(") - Frequency VR (").append(rFreqVr).append(") ) = ").append(rResult).toString());
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw new MaskException();
        }
        return rResult;
    }

    public void reset()
    {
        double offset = 0.0D;
        if(getInterferenceLink().getVictimLink().getVictimReceiver().getBlockingAttenuationMode() == 1)
            offset = -vrAttenuationProcRatio(false);
        else
        if(getInterferenceLink().getVictimLink().getVictimReceiver().getBlockingAttenuationMode() == 2)
            offset = -vrAttenuationSens(false);
        Function blockingAttenuation = getInterferenceLink().getVictimLink().getVictimReceiver().getBlockingResponse();
        blockingAttenuation.offset(offset);
        bStoppedAttenuation = true;
    }

    public void itVrPropagationLossBlock(boolean debugMode)
        throws ModelException
    {
        PropagationModel model = getInterferenceLink().getWt2VrPath().getPropagationModel();
        double rPropLossResult;
        try
        {
            rPropLossResult = model.evaluate(getInterferenceLink().getInterferingLink().getInterferingTransmitter().getItTrialFrequency(), getInterferenceLink().getTxRxDistance(), getInterferenceLink().getInterferingLink().getInterferingTransmitter().getTxTrialAntHeight(), getInterferenceLink().getVictimLink().getVictimReceiver().getRxTrialAntHeight());
            if(debugMode)
                LOG.debug((new StringBuilder()).append("<").append(getInterferenceLink().getWt2VrPath().getPropagationModel().getClass().getName()).append("> ").append(rPropLossResult).append(" = evaluate(").append(getInterferenceLink().getInterferingLink().getInterferingTransmitter().getItTrialFrequency()).append(",").append(getInterferenceLink().getTxRxDistance()).append(",").append(getInterferenceLink().getInterferingLink().getInterferingTransmitter().getTxTrialAntHeight()).append(",").append(getInterferenceLink().getVictimLink().getVictimReceiver().getRxTrialAntHeight()).append(")").toString());
        }
        catch(Exception e)
        {
            throw new ModelException();
        }
        getInterferenceLink().setTxRxPathLoss(rPropLossResult);
    }

    public void itVrPathAntGainsBlock(boolean debug)
        throws PatternException
    {
        double rFreqIt = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getItTrialFrequency();
        double rRxTxAzi = getInterferenceLink().getRxTxAzimuth();
        double rRxTxElev = getInterferenceLink().getRxTxElevation();
        double rTxRxAzi = getInterferenceLink().getTxRxAzimuth();
        double rTxRxElev = getInterferenceLink().getTxRxElevation();
        double rVrPeakGain = getInterferenceLink().getVictimLink().getVictimReceiver().getAntenna().getPeakGain();
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Interfering Transmitter Frequency = ").append(rFreqIt).toString());
            LOG.debug((new StringBuilder()).append("Interference Link - Rx -> Tx Azimuth = ").append(rRxTxAzi).toString());
            LOG.debug((new StringBuilder()).append("Interference Link - Rx -> Tx Elevation = ").append(rRxTxElev).toString());
            LOG.debug((new StringBuilder()).append("Interference Link - Tx -> Rx Azimuth = ").append(rTxRxAzi).toString());
            LOG.debug((new StringBuilder()).append("Interference Link - Tx -> Rx Elevation = ").append(rTxRxElev).toString());
            LOG.debug((new StringBuilder()).append("Victim Receiver Antenna Peak Gain = ").append(rVrPeakGain).toString());
            LOG.debug("Victim Receiver -> Interfering Transmitter Antenna Gain: ");
        }
        try
        {
            double rVrGainResult = getInterferenceLink().getVictimLink().getVictimReceiver().getAntenna().calculateGain(rRxTxAzi, rRxTxElev);
            getInterferenceLink().setRxTxAntennaGain(rVrGainResult);
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on Vr->It antenna: ").append(e.getMessage()).toString(), e);
        }
        try
        {
            double rItGainResult = getInterferenceLink().getInterferingLink().getInterferingTransmitter().getAntenna().calculateGain(rTxRxAzi, rTxRxElev);
            getInterferenceLink().setTxRxAntennaGain(rItGainResult);
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on It->Vr antenna: ").append(e.getMessage()).toString(), e);
        }
    }

    public double vrAttenuationProcRatio(boolean debug)
    {
        double rCNI = getInterferenceLink().getVictimLink().getVictimReceiver().getCniLevel();
        double rOffset = 3D + rCNI;
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Victim Receiver CNI Level = ").append(rCNI).toString());
            LOG.debug((new StringBuilder()).append("Victim Receiver Blocking Response Offset = ").append(rOffset).append(" = 3 + CNI").toString());
        }
        return rOffset;
    }

    public double vrAttenuationSens(boolean debug)
    {
        double rCNI = getInterferenceLink().getVictimLink().getVictimReceiver().getCniLevel();
        double rSens = getInterferenceLink().getVictimLink().getVictimReceiver().getSensitivity();
        double rOffset = rCNI - rSens;
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Victim Receiver CNI Level = ").append(rCNI).toString());
            LOG.debug((new StringBuilder()).append("Victim Receiver Sensitivity = ").append(rSens).toString());
            LOG.debug((new StringBuilder()).append("Victim Receiver Blocking Response Offset = ").append(rOffset).append(" = CNI - Sensitivity").toString());
        }
        return rOffset;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("BlockingInterference");
        element.setAttribute("bStoppedAttenuation", Boolean.toString(bStoppedAttenuation));
        element.appendChild(super.toElement(doc));
        return element;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/core/BlockingInterference);
    private boolean bStoppedAttenuation;
    private InterferenceLink interferenceLink;

}