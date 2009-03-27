// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VictimSystemLink.java

package org.seamcat.model.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.cdma.CDMADownlinkSystem;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.Antenna;
import org.seamcat.model.Components;
import org.seamcat.model.Library;
import org.seamcat.model.Model;
import org.seamcat.model.NodeAttribute;
import org.seamcat.model.Receiver;
import org.seamcat.model.Transmitter;
import org.seamcat.model.TransmitterToReceiverPath;
import org.seamcat.model.VictimReceiver;
import org.seamcat.model.WantedTransmitter;
import org.seamcat.model.technical.exception.GeometricException;
import org.seamcat.model.technical.exception.ModelException;
import org.seamcat.model.technical.exception.PatternException;
import org.seamcat.model.technical.exception.RandomException;
import org.seamcat.model.technical.stats.Stats;
import org.seamcat.presentation.Node;
import org.seamcat.presentation.components.LocalComponent;
import org.seamcat.propagation.PropagationModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model.core:
//            SystemLink

public class VictimSystemLink extends SystemLink
{

    public VictimSystemLink()
    {
        super("VictimSystemLink", (new StringBuilder()).append("Created on:\n ").append(new Date()).toString());
        useWantedTransmitter = true;
        dRSS = new ConstantDistribution(0.0D);
        frequency = new ConstantDistribution(900D);
        isCDMASystem = false;
        cdmasystem = null;
        wantedTransmitter = new WantedTransmitter((Transmitter)Model.getInstance().getLibrary().getTransmitters().getElementAt(0));
        victimReceiver = new VictimReceiver((Receiver)Model.getInstance().getLibrary().getReceivers().getElementAt(0));
    }

    public VictimSystemLink(Element element)
    {
        super((Element)element.getElementsByTagName("systemLink").item(0));
        useWantedTransmitter = true;
        dRSS = new ConstantDistribution(0.0D);
        frequency = new ConstantDistribution(900D);
        isCDMASystem = false;
        cdmasystem = null;
        setUseWantedTransmitter(Boolean.valueOf(element.getAttribute("useWantedTransmitter")).booleanValue());
        setUseCorrelatedDistance(Boolean.valueOf(element.getAttribute("useCorrelatedDistance")).booleanValue());
        try
        {
            setIsCDMASystem(element.getAttribute("isCDMA").equalsIgnoreCase("true"));
        }
        catch(Exception ex) { }
        setDRSS(Distribution.create((Element)element.getElementsByTagName("dRSS").item(0).getFirstChild()));
        Element freqElement = (Element)element.getElementsByTagName("vlk_frequency").item(0);
        if(freqElement != null)
            setFrequency(Distribution.create((Element)freqElement.getFirstChild()));
        setVictimReceiver(new VictimReceiver((Element)element.getElementsByTagName("VictimReceiver").item(0)));
        setWantedTransmitter(new WantedTransmitter((Element)element.getElementsByTagName("WantedTransmitter").item(0)));
        if(isCDMASystem)
        {
            Element cdmaElement = (Element)element.getElementsByTagName("CdmaSystem").item(0);
            cdmasystem = CDMASystem.createCDMASystem(cdmaElement);
            cdmasystem.setVictimSystem(true);
        }
    }

    public void updateNodeAttributes()
    {
        try
        {
            super.updateNodeAttributes();
            victimReceiver.updateNodeAttributes();
            wantedTransmitter.updateNodeAttributes();
            cdmasystem.updateNodeAttributes();
            getWt2VrPath().updateNodeAttributes();
        }
        catch(Exception ex) { }
    }

    public boolean isCDMASystem()
    {
        return isCDMASystem;
    }

    public void setIsCDMASystem(boolean value)
    {
        isCDMASystem = value;
        if(isCDMASystem && cdmasystem == null)
            createCDMASystem();
        updateNodeAttributes();
    }

    public void createCDMASystem()
    {
        cdmasystem = new CDMADownlinkSystem();
        cdmasystem.setVictimSystem(true);
    }

    public CDMASystem getCDMASystem()
    {
        return cdmasystem;
    }

    public WantedTransmitter getWantedTransmitter()
    {
        return wantedTransmitter;
    }

    public void setWantedTransmitter(WantedTransmitter wantedTransmitter)
    {
        this.wantedTransmitter = wantedTransmitter;
    }

    public VictimReceiver getVictimReceiver()
    {
        return victimReceiver;
    }

    public PropagationModel getPropagationModel()
    {
        throw new IllegalStateException("System Link propagation model on Victim Link should not be used.");
    }

    public void setVictimReceiver(VictimReceiver victimReceiver)
    {
        this.victimReceiver = victimReceiver;
    }

    public boolean getUseCorrelatedDistance()
    {
        return useCorrelatedDistance;
    }

    public void setUseCorrelatedDistance(boolean _value)
    {
        useCorrelatedDistance = _value;
    }

    public Distribution getDRSS()
    {
        return dRSS;
    }

    public void setDRSS(Distribution dRSS)
    {
        this.dRSS = dRSS;
    }

    public double dRSSLinkBudgetDef(boolean debug)
        throws RandomException, GeometricException, PatternException, ModelException
    {
        wtTrial(debug);
        vrTrial(debug);
        vLTxRxLoc(debug);
        vLPathAntAziElev(debug);
        vLPathAntGains(debug);
        vLPropagationLoss(debug);
        double rWtPower = getWantedTransmitter().getTxTrialPower();
        double rWtGain = getTxRxAntennaGain();
        double rVrGain = getRxTxAntennaGain();
        double rPcmax = getVictimReceiver().getPowerControlMaxThreshold();
        double rSens = getVictimReceiver().getSensitivity();
        double rPathLoss = getTxRxPathLoss();
        double rdRSSValue = (rWtPower + rWtGain + rVrGain) - rPathLoss;
        if(debug)
            LOG.debug((new StringBuilder()).append("dRSS Value = ").append(rdRSSValue).append(" [(wtPower = ").append(rWtPower).append(") + (wtGain = ").append(rWtGain).append(") + (VrGain = ").append(rVrGain).append(") - (pattloss = ").append(rPathLoss).append(")]").toString());
        if(getVictimReceiver().getUsePowerControlThreshold() && rdRSSValue > rSens + rPcmax)
        {
            rdRSSValue = rSens + rPcmax;
            if(debug)
                LOG.debug((new StringBuilder()).append("(getVictimReceiver().getCheckPcMax()) && (rdRSSValue > (Sensitivity + Pcmax)) is true -> dRSS Value [").append(rdRSSValue).append("] = Sensitivity [").append(rSens).append("] + Pcmax [").append(rPcmax).append("]").toString());
        }
        return rdRSSValue;
    }

    public double dRSSLinkBudgetFixed(boolean debug)
        throws RandomException, PatternException, GeometricException
    {
        double rdRSSValue = 0.0D;
        vrTrial(debug);
        vrAntAziElev(debug);
        vrAntGain(debug);
        rdRSSValue = getDRSS().trial();
        if(debug)
            LOG.debug((new StringBuilder()).append("dRSS trialed as = ").append(rdRSSValue).toString());
        return rdRSSValue;
    }

    public void vLPathAntGains(boolean debug)
        throws PatternException
    {
        double rFreqVr = getVictimReceiver().getVrTrialFrequency();
        double rRxTxAzi = getRxTxAzimuth();
        double rRxTxElev = getRxTxElevation();
        double rTxRxAzi = getTxRxAzimuth();
        double rTxRxElev = getTxRxElevation();
        double rVrPeakGain = getVictimReceiver().getAntenna().getPeakGain();
        if(debug)
        {
            LOG.debug("*********         Vr->Wt Antenna Gain     *******");
            LOG.debug((new StringBuilder()).append("Rx->Tx Azimuth (RxTxAzi) = ").append(rRxTxAzi).toString());
            LOG.debug((new StringBuilder()).append("Rx->Tx Elevation (RxTxElev) = ").append(rRxTxElev).toString());
            LOG.debug((new StringBuilder()).append("Tx->Rx Azimuth (TxRxAzi) = ").append(rTxRxAzi).toString());
            LOG.debug((new StringBuilder()).append("Tx->Rx Elevation (TxRxElev) = ").append(rTxRxElev).toString());
            LOG.debug((new StringBuilder()).append("Vr frequency = ").append(rFreqVr).toString());
            LOG.debug((new StringBuilder()).append("Vr peak gain = ").append(rVrPeakGain).toString());
        }
        try
        {
            double rVrGainResult = getVictimReceiver().getAntenna().calculateGain(rRxTxAzi, rRxTxElev);
            setRxTxAntennaGain(rVrGainResult);
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on Vr->Wt antenna: ").append(e.getMessage()).toString(), e);
        }
        try
        {
            double rWtGainResult = getWantedTransmitter().getAntenna().calculateGain(rTxRxAzi, rTxRxElev);
            setTxRxAntennaGain(rWtGainResult);
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on Wt->Vr antenna: ").append(e.getMessage()).toString(), e);
        }
    }

    public void vrTrial(boolean debug)
        throws RandomException
    {
        double rResultAntHeight;
        try
        {
            rResultAntHeight = getVictimReceiver().getAntennaHeight().trial();
            if(debug)
                LOG.debug((new StringBuilder()).append("Trialed VR antenna height = ").append(rResultAntHeight).toString());
        }
        catch(Exception e)
        {
            throw new RandomException();
        }
        getVictimReceiver().setRxTrialAntHeight(rResultAntHeight);
        double rResultFrequency;
        try
        {
            rResultFrequency = getFrequency().trial();
            if(debug)
                LOG.debug((new StringBuilder()).append("Trialed VR frequency = ").append(rResultFrequency).toString());
        }
        catch(Exception e)
        {
            throw new RandomException();
        }
        getVictimReceiver().setVrTrialFrequency(rResultFrequency);
    }

    public void wtTrial(boolean debug)
        throws RandomException
    {
        Distribution antHeight = getWantedTransmitter().getAntennaHeight();
        Distribution power = getWantedTransmitter().getPowerSuppliedDistribution();
        double rResultAntHeight;
        double rResultPower;
        try
        {
            rResultAntHeight = antHeight.trial();
            rResultPower = power.trial();
        }
        catch(Exception e)
        {
            throw new RandomException();
        }
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("WT power trial = ").append(rResultPower).toString());
            LOG.debug((new StringBuilder()).append("WT Antenna height trial = ").append(rResultAntHeight).toString());
        }
        getWantedTransmitter().setTxTrialAntHeight(rResultAntHeight);
        getWantedTransmitter().setTxTrialPower(rResultPower);
    }

    public void vLPathAntAziElev(boolean debug)
        throws RandomException, GeometricException
    {
        double rWtAntHeight = getWantedTransmitter().getTxTrialAntHeight();
        double rVrAntHeight = getVictimReceiver().getRxTrialAntHeight();
        double rTxRxDistance = getTxRxDistance() * 1000D;
        Distribution wtVrAzi = getTransmitterToReceiverAzimuth();
        Distribution wtVrElev = getTransmitterToReceiverElevation();
        Distribution vrWtAzi = getReceiverToTransmitterAzimuth();
        Distribution vrWtElev = getReceiverToTransmitterElevation();
        double rWtVrAziTrial;
        double rVrWtAziTrial;
        double rWtVrElevTrial;
        double rVrWtElevTrial;
        try
        {
            rWtVrAziTrial = wtVrAzi.trial();
            rWtVrElevTrial = wtVrElev.trial();
            rVrWtAziTrial = vrWtAzi.trial();
            rVrWtElevTrial = vrWtElev.trial();
        }
        catch(Exception e)
        {
            throw new RandomException();
        }
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Wt antenna height trial = ").append(rWtAntHeight).toString());
            LOG.debug((new StringBuilder()).append("Vr antenna height trial = ").append(rVrAntHeight).toString());
            LOG.debug((new StringBuilder()).append("TxRx distance trial = ").append(rTxRxDistance).toString());
            LOG.debug((new StringBuilder()).append("WtVr Azimuth trial = ").append(rWtVrAziTrial).toString());
            LOG.debug((new StringBuilder()).append("WtVR Elevation trial = ").append(rWtVrElevTrial).toString());
            LOG.debug((new StringBuilder()).append("VrWt Azimuth trial = ").append(rVrWtAziTrial).toString());
            LOG.debug((new StringBuilder()).append("VrWt Elevation trial = ").append(rVrWtElevTrial).toString());
        }
        double rVrWtAziResult = rVrWtAziTrial;
        setRxTxAzimuth(rVrWtAziResult);
        double rVrWtTilt = rVrWtElevTrial;
        if(debug)
            LOG.debug((new StringBuilder()).append("VrWtTilt = VrWt Elevation trial = ").append(rVrWtTilt).toString());
        double rVrWtAlpha;
        double rVrWtPhi;
        try
        {
            rVrWtPhi = (Math.atan2(rWtAntHeight - rVrAntHeight, rTxRxDistance) * 180D) / 3.1415926535897931D;
            rVrWtAlpha = rVrWtTilt * Mathematics.cosD(rVrWtAziResult);
            if(LOG.isDebugEnabled())
            {
                LOG.debug((new StringBuilder()).append("VrWtPhi = Math.atan2((WtAntHeight - VrAntHeight), (TxRxDistance)) * PID / Math.PI) = ").append(rVrWtPhi).append(" = Math.atan2((").append(rWtAntHeight).append(" - ").append(rVrAntHeight).append("), (").append(rTxRxDistance).append(")) * ").append(180).append(" / ").append(3.1415926535897931D).append(")").toString());
                LOG.debug((new StringBuilder()).append("VrWtAlpha = VrWtTilt * Mathematics.cosD(rVrWtAziResult) = ").append(rVrWtAlpha).append(" = ").append(rVrWtTilt).append(" * Mathematics.cosD(").append(rVrWtAziResult).append(")").toString());
            }
        }
        catch(Exception e)
        {
            throw new GeometricException();
        }
        double rVrWtElevResult = rVrWtPhi - rVrWtAlpha;
        if(rVrWtElevResult < -90D)
            rVrWtElevResult += 180D;
        else
        if(rVrWtElevResult > 90D)
            rVrWtElevResult -= 180D;
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("VrWt Elevation Result = rVrWtPhi - rVrWtAlpha = ").append(rVrWtElevResult).append(" = ").append(rVrWtPhi).append(" - ").append(rVrWtAlpha).toString());
        setRxTxElevation(rVrWtElevResult);
        setRxTxTilt(rVrWtTilt);
        double rWtVrAziResult = rWtVrAziTrial;
        setTxRxAzimuth(rWtVrAziResult);
        double rWtVrTilt = rWtVrElevTrial;
        double rWtVrAlpha;
        double rWtVrPhi;
        try
        {
            rWtVrPhi = (Math.atan2(rVrAntHeight - rWtAntHeight, rTxRxDistance) * 180D) / 3.1415926535897931D;
            rWtVrAlpha = rWtVrTilt * Mathematics.cosD(rWtVrAziResult);
            if(LOG.isDebugEnabled())
                LOG.debug((new StringBuilder()).append("WtVrPhi=Math.atan2((rVrAntHeight - rWtAntHeight), (rTxRxDistance)) * PID / Math.PI = ").append(rWtVrPhi).append("=Math.atan2((").append(rVrAntHeight).append(" - ").append(rWtAntHeight).append("), (").append(rTxRxDistance).append(")) * ").append(180).append(" / ").append(3.1415926535897931D).toString());
        }
        catch(Exception exception)
        {
            throw new GeometricException();
        }
        double rWtVrElevResult = rWtVrPhi - rWtVrAlpha;
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("WtVr Elevation Result = WtVrPhi - WtVrAlpha = ").append(rWtVrElevResult).append(" = ").append(rWtVrPhi).append(" - ").append(rWtVrAlpha).toString());
        setTxRxElevation(rWtVrElevResult);
        setTxRxTilt(rWtVrTilt);
    }

    public void vLPropagationLoss(boolean debug)
        throws ModelException
    {
        PropagationModel propagationModel = getWt2VrPath().getPropagationModel();
        double rPropLossResult;
        try
        {
            rPropLossResult = propagationModel.evaluate(getVictimReceiver().getVrTrialFrequency(), getTxRxDistance(), getWantedTransmitter().getTxTrialAntHeight(), getVictimReceiver().getRxTrialAntHeight());
            if(LOG.isDebugEnabled())
                LOG.debug((new StringBuilder()).append("Propagation Loss = ").append(propagationModel.getClass()).append(".evaluate(getVictimReceiver().getVrTrialFrequency(), ").append("getTxRxDistance(),getWantedTransmitter().getTxTrialAntHeight(), ").append(" getVictimReceiver().getRxTrialAntHeight(), getRxTxAntennaGain(),getTxRxAntennaGain()) = ").append(rPropLossResult).append(" = ").append(propagationModel.getClass()).append(".evaluate(").append(getVictimReceiver().getVrTrialFrequency()).append(", ").append(getTxRxDistance()).append(",").append(getWantedTransmitter().getTxTrialAntHeight()).append(",").append(getVictimReceiver().getRxTrialAntHeight()).append(")").toString());
        }
        catch(Exception exception)
        {
            throw new ModelException();
        }
        setTxRxPathLoss(rPropLossResult);
    }

    public void vLTxRxLoc(boolean debug)
        throws RandomException, GeometricException
    {
        if(!getWt2VrPath().getUseCorrelationDistance())
        {
            Distribution wtVrDistance = getWt2VrPath().getPathDistanceFactor();
            Distribution wtVrAngle = getWt2VrPath().getPathAzimuth();
            double rRmax = getWt2VrPath().getCoverageRadius();
            double rWtVrDistTrial;
            double rWtVrAngleTrial;
            try
            {
                rWtVrDistTrial = wtVrDistance.trial();
                rWtVrAngleTrial = wtVrAngle.trial();
            }
            catch(Exception exception)
            {
                throw new RandomException();
            }
            double rResultWtVrDistance = rRmax * rWtVrDistTrial;
            double rResultPosX;
            double rResultPosY;
            try
            {
                rResultPosX = rResultWtVrDistance * Math.cos(rWtVrAngleTrial * 0.017453292519943295D);
                rResultPosY = rResultWtVrDistance * Math.sin(rWtVrAngleTrial * 0.017453292519943295D);
            }
            catch(Exception exception)
            {
                throw new GeometricException();
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Non correlated case");
                LOG.debug((new StringBuilder()).append("WtVrDistance trial = ").append(rWtVrDistTrial).toString());
                LOG.debug((new StringBuilder()).append("WtVrAngle trial = ").append(rWtVrAngleTrial).toString());
                LOG.debug((new StringBuilder()).append("Rmax = ").append(rRmax).toString());
                LOG.debug((new StringBuilder()).append("WtVr Distance = WrVrDistance trial * Rmax = ").append(rResultWtVrDistance).toString());
                LOG.debug((new StringBuilder()).append("Position X = WtVr Distance * Math.cos(WtVrAngle trial * (Math.PI / PID)) = ").append(rResultPosX).toString());
                LOG.debug((new StringBuilder()).append("Position Y = WtVr Distance * Math.sin(WtVrAngle trial * (Math.PI / PID)) = ").append(rResultPosY).toString());
            }
            setTxRxAngle(rWtVrAngleTrial);
            setTxRxDistance(rResultWtVrDistance);
            getVictimReceiver().setX(rResultPosX);
            getVictimReceiver().setY(rResultPosY);
        } else
        if(getWt2VrPath().getUseCorrelationDistance())
        {
            double rResultPosX = getWt2VrPath().getDeltaX();
            double rResultPosY = getWt2VrPath().getDeltaY();
            double rResultWtVrDistance;
            double rResultWtVrAngle;
            try
            {
                rResultWtVrAngle = (Math.atan2(rResultPosY, rResultPosX) * 180D) / 3.1415926535897931D;
                rResultWtVrDistance = Math.sqrt(rResultPosX * rResultPosX + rResultPosY * rResultPosY);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Correlated case");
                    LOG.debug((new StringBuilder()).append("WtVrAngle = Math.atan2(PosY, PosX) * PID / Math.PI = ").append(rResultWtVrAngle).append(" = Math.atan2(").append(rResultPosY).append(", ").append(rResultPosX).append(") * ").append(180).append(" / ").append(3.1415926535897931D).append(")").toString());
                    LOG.debug((new StringBuilder()).append("WtVrDistance = Math.sqrt((PosX * PosX) + (PosY * PosY)) = ").append(rResultWtVrDistance).append("=Math.sqrt((").append(rResultPosX).append(" * ").append(rResultPosX).append(") + (").append(rResultPosY).append(" * ").append(rResultPosY).append("))").toString());
                }
            }
            catch(Exception exception)
            {
                throw new GeometricException();
            }
            setTxRxAngle(rResultWtVrAngle);
            setTxRxDistance(rResultWtVrDistance);
            getVictimReceiver().setX(rResultPosX);
            getVictimReceiver().setY(rResultPosY);
        }
    }

    public void coverageRadiusNoiseWt(boolean debug)
        throws ModelException
    {
        PropagationModel propagationModel = getWt2VrPath().getPropagationModel();
        double rFadingStdDev = getWt2VrPath().getFadingStdDev();
        double rAvailability = getWt2VrPath().getAvailability() / 100D;
        double rRefAntHeightWt = getWt2VrPath().getReferenceTransmitterAntennaHeight();
        double rRefAntHeightVr = getWt2VrPath().getReferenceReceiverAntennaHeight();
        double rFrequencyVr = getWt2VrPath().getReferenceTransmitterFrequency();
        double rCoverageLoss = getWantedTransmitter().getCoverageLoss();
        double rSens = getVictimReceiver().getSensitivity();
        double rPeakGainVr = getVictimReceiver().getAntenna().getPeakGain();
        double rPeakGainWt = getWantedTransmitter().getAntenna().getPeakGain();
        double rMaxDist = getWt2VrPath().getMaximumDistance() * 1000D;
        double rMinDist = getWt2VrPath().getMinimumDistance() * 1000D;
        double rRefPower = getWt2VrPath().getReferenceTransmitterPower();
        double rFadingLoss = -Stats.qi(rAvailability) * rFadingStdDev;
        if(LOG.isDebugEnabled())
        {
            LOG.debug((new StringBuilder()).append("FadingStdDev = ").append(rFadingStdDev).toString());
            LOG.debug((new StringBuilder()).append("Availability = ").append(rAvailability).toString());
            LOG.debug((new StringBuilder()).append("RefAntHeightWt = ").append(rRefAntHeightWt).toString());
            LOG.debug((new StringBuilder()).append("RefAntHeightVr = ").append(rRefAntHeightVr).toString());
            LOG.debug((new StringBuilder()).append("FrequencyVr = ").append(rFrequencyVr).toString());
            LOG.debug((new StringBuilder()).append("CoverageLoss = ").append(rCoverageLoss).toString());
            LOG.debug((new StringBuilder()).append("Sensitivity = ").append(rSens).toString());
            LOG.debug((new StringBuilder()).append("PeakGainVr = ").append(rPeakGainVr).toString());
            LOG.debug((new StringBuilder()).append("PeakGainWt = ").append(rPeakGainWt).toString());
            LOG.debug((new StringBuilder()).append("MaxDist = ").append(rMaxDist).toString());
            LOG.debug((new StringBuilder()).append("MinDist = ").append(rMinDist).toString());
            LOG.debug((new StringBuilder()).append("RefPower = ").append(rRefPower).toString());
            LOG.debug((new StringBuilder()).append("FadingLoss = -Stats.qi(Availability) * FadingStdDev = ").append(rFadingLoss).append(" = ").append(-Stats.qi(rAvailability)).append(" * ").append(rFadingStdDev).toString());
        }
        double rR = 0.0D;
        double rDR = 0.0D;
        int i = 0;
        double rR1 = rMinDist;
        double rR2 = rMaxDist;
        double rLogR1 = Math.log10(rR1);
        double rLogR2 = Math.log10(rR2);
        double rL1 = (rRefPower + rPeakGainWt + rPeakGainVr) - rSens - rFadingLoss - propagationModel.medianLoss(rFrequencyVr, rR1 / 1000D, rRefAntHeightWt, rRefAntHeightVr);
        double rL2 = (rRefPower + rPeakGainWt + rPeakGainVr) - rSens - rFadingLoss - propagationModel.medianLoss(rFrequencyVr, rR2 / 1000D, rRefAntHeightWt, rRefAntHeightVr);
        if(LOG.isDebugEnabled())
        {
            LOG.debug((new StringBuilder()).append("R1 = MinDist = ").append(rMinDist).toString());
            LOG.debug((new StringBuilder()).append("R2 = MaxDist = ").append(rMaxDist).toString());
            LOG.debug((new StringBuilder()).append("L1 = rRefPower + rPeakGainWt + rPeakGainVr - rSens - rFadingLoss - ").append(propagationModel.getClass()).append(".medianLoss(rFrequencyVr,rR1 / KMTOM, rRefAntHeightWt, ").append("rRefAntHeightVr, rPeakGainWt, rPeakGainVr) -> ").append(rL1).append(" = ").append(rRefPower).append(" + ").append(rPeakGainWt).append(" + ").append(rPeakGainVr).append(" - ").append(rSens).append(" - ").append(rFadingLoss).append(" - ").append(propagationModel.getClass()).append(".medianLoss(").append(rFrequencyVr).append(",").append(rR1 / 1000D).append(",").append(rRefAntHeightWt).append(",").append(rRefAntHeightVr).append(")").toString());
            LOG.debug((new StringBuilder()).append("L2 = rRefPower + rPeakGainWt + rPeakGainVr - rSens - rFadingLoss - ").append(propagationModel.getClass()).append(".medianLoss(rFrequencyVr,rR2 / KMTOM, rRefAntHeightWt, ").append("rRefAntHeightVr, rPeakGainWt, rPeakGainVr) -> ").append(rL2).append(" = ").append(rRefPower).append(" + ").append(rPeakGainWt).append(" + ").append(rPeakGainVr).append(" - ").append(rSens).append(" - ").append(rFadingLoss).append(" - ").append(propagationModel.getClass()).append(".medianLoss(").append(rFrequencyVr).append(",").append(rR2 / 1000D).append(",").append(rRefAntHeightWt).append(",").append(rRefAntHeightVr).append(")").toString());
        }
        if(rL1 * rL2 >= 0.0D)
            getWantedTransmitter().setRMax(rMaxDist);
        else
        if(rL1 > 0.0D)
        {
            double rL = rL1;
            rL1 = rL2;
            rL2 = rL;
            double rLogR = rLogR1;
            rLogR1 = rLogR2;
            rLogR2 = rLogR;
            rR = rR1;
            rR1 = rR2;
            rR2 = rR;
        }
        i = 0;
        do
        {
            if(i >= 1000)
                break;
            double rLogR = rLogR1 + ((rLogR2 - rLogR1) * rL1) / (rL1 - rL2);
            rR = Math.pow(10D, rLogR);
            double rL = (rRefPower + rPeakGainWt + rPeakGainVr) - rSens - rFadingLoss - propagationModel.medianLoss(rFrequencyVr, rR / 1000D, rRefAntHeightWt, rRefAntHeightVr);
            if(rL < 0.0D)
            {
                rDR = rL1 - rL;
                rL1 = rL;
                rR1 = rR;
                rLogR1 = rLogR;
            } else
            {
                rDR = rL2 - rL;
                rL2 = rL;
                rR2 = rR;
                rLogR2 = rLogR;
            }
            if(Math.abs(rDR) < 0.10000000000000001D || rL == 0.0D)
                break;
            i++;
        } while(true);
        getWantedTransmitter().setRMax(rR / 1000D);
    }

    public void coverageRadiusWt(boolean debug)
        throws GeometricException, ModelException
    {
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("Coverage Radius Mode Wanted Transmitter = ").append(Transmitter.CALCULATION_MODES[getWt2VrPath().getCoverageRadiusCalculationMode()]).toString());
        switch(getWt2VrPath().getCoverageRadiusCalculationMode())
        {
        case 0: // '\0'
            getWantedTransmitter().setCoverageRadius(getWt2VrPath().getCoverageRadius());
            getWantedTransmitter().coverageRadiusUserWt();
            break;

        case 2: // '\002'
            getWantedTransmitter().coverageRadiusTrafficWt(debug);
            getWt2VrPath().setCoverageRadius(getWantedTransmitter().getRMax());
            getWantedTransmitter().setCoverageRadius(getWt2VrPath().getCoverageRadius());
            break;

        case 1: // '\001'
            coverageRadiusNoiseWt(debug);
            getWt2VrPath().setCoverageRadius(getWantedTransmitter().getRMax());
            getWantedTransmitter().setCoverageRadius(getWt2VrPath().getCoverageRadius());
            break;

        default:
            throw new IllegalStateException((new StringBuilder()).append("Unknown coverage radius calculation mode <").append(getWt2VrPath().getCoverageRadiusCalculationMode()).append(">").toString());
        }
    }

    public void vrAntAziElev(boolean debug)
        throws RandomException, GeometricException
    {
        double rVrPhi = 0.0D;
        double rVrAntHeight = getVictimReceiver().getRxTrialAntHeight();
        Distribution vrAzi = getReceiverToTransmitterAzimuth();
        Distribution vrElev = getReceiverToTransmitterElevation();
        double rVrAziTrial;
        double rVrElevTrial;
        try
        {
            rVrAziTrial = vrAzi.trial();
            rVrElevTrial = vrElev.trial();
            if(debug)
            {
                LOG.debug((new StringBuilder()).append("VR Azimuth trial = ").append(rVrAziTrial).toString());
                LOG.debug((new StringBuilder()).append("VR Elevation trial = ").append(rVrElevTrial).toString());
            }
        }
        catch(Exception e)
        {
            throw new RandomException();
        }
        double rVrAziResult = rVrAziTrial;
        setRxTxAzimuth(rVrAziResult);
        double rVrTilt = rVrElevTrial;
        if(debug)
            LOG.debug((new StringBuilder()).append("Vr -> Wt elevation [VrTilt] = (Vr Elevation Trial) = ").append(rVrElevTrial).toString());
        double rVrAlpha;
        try
        {
            rVrAlpha = rVrTilt * Mathematics.cosD(rVrAziResult);
            if(debug)
                LOG.debug((new StringBuilder()).append("Vr alpha = (VrTilt * cosD(VrAzimuth) = ").append(rVrTilt).append(" * cosD(").append(rVrAziResult).append(")").toString());
        }
        catch(Exception exception)
        {
            throw new GeometricException();
        }
        double rVrElevResult = rVrPhi - rVrAlpha;
        if(debug)
            LOG.debug((new StringBuilder()).append("Vr Elevation = 0 - ").append(rVrAlpha).append(" [Vr alpha]").toString());
        setRxTxElevation(rVrElevResult);
        setRxTxTilt(rVrTilt);
    }

    public void vrAntGain(boolean debug)
        throws PatternException
    {
        double rFreqVr = getVictimReceiver().getVrTrialFrequency();
        double rRxTxAzi = getRxTxAzimuth();
        double rRxTxElev = getRxTxElevation();
        double rVrPeakGain = getVictimReceiver().getAntenna().getPeakGain();
        if(debug)
        {
            LOG.debug((new StringBuilder()).append("Rx->Tx Azimuth (RxTxAzi) = ").append(rRxTxAzi).toString());
            LOG.debug((new StringBuilder()).append("Rx->Tx Elevation (RxTxElev) = ").append(rRxTxElev).toString());
            LOG.debug((new StringBuilder()).append("Vr frequency = ").append(rFreqVr).toString());
            LOG.debug((new StringBuilder()).append("Vr peak gain = ").append(rVrPeakGain).toString());
        }
        double rVrGainResult;
        try
        {
            rVrGainResult = getVictimReceiver().getAntenna().calculateGain(rRxTxAzi, rRxTxElev);
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on Vr->Wt antenna: ").append(e.getMessage()).toString(), e);
        }
        setRxTxAntennaGain(rVrGainResult);
    }

    public boolean getUseWantedTransmitter()
    {
        return useWantedTransmitter;
    }

    public Distribution getFrequency()
    {
        return frequency;
    }

    public void setUseWantedTransmitter(boolean value)
    {
        useWantedTransmitter = value;
        updateNodeAttributes();
    }

    public void setFrequency(Distribution _frequency)
    {
        LOG.debug((new StringBuilder()).append("VictimSystemLink Frequency set to ").append(frequency).toString());
        frequency = _frequency;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("VictimSystemLink");
        element.setAttribute("useWantedTransmitter", Boolean.toString(getUseWantedTransmitter()));
        element.setAttribute("useCorrelatedDistance", Boolean.toString(getUseCorrelatedDistance()));
        element.setAttribute("isCDMA", Boolean.toString(isCDMASystem));
        Element dRSSElement = doc.createElement("dRSS");
        dRSSElement.appendChild(getDRSS().toElement(doc));
        element.appendChild(dRSSElement);
        Element frequencyElement = doc.createElement("vlk_frequency");
        frequencyElement.appendChild(getFrequency().toElement(doc));
        element.appendChild(frequencyElement);
        element.appendChild(getVictimReceiver().toElement(doc));
        element.appendChild(getWantedTransmitter().toElement(doc));
        element.appendChild(super.toElement(doc));
        if(isCDMASystem)
            element.appendChild(cdmasystem.toElement(doc));
        return element;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Reference", "", getReference(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Description", "", getDescription(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Frequency", "MHz", getFrequency(), "Distribution", null, false, false, null));
        if(!isCDMASystem)
        {
            nodeList.add(new NodeAttribute("Use wanted transmitter", "", getUseWantedTransmitter() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
                Boolean.FALSE, Boolean.TRUE
            }, true, true, null));
            if(!getUseWantedTransmitter())
                nodeList.add(new NodeAttribute("User defined dRSS", "dBm", getDRSS(), "Distribution", null, false, false, null));
        }
        nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
        switch(rowIndex)
        {
        case 0: // '\0'
            setReference((String)aValue);
            break;

        case 1: // '\001'
            setDescription((String)aValue);
            break;

        case 2: // '\002'
            setFrequency((Distribution)aValue);
            break;

        case 3: // '\003'
            setUseWantedTransmitter(((Boolean)aValue).booleanValue());
            break;

        case 4: // '\004'
            setDRSS((Distribution)aValue);
            break;
        }
    }

    public boolean isLeaf()
    {
        return false;
    }

    public int getChildCount()
    {
        return !isCDMASystem() ? 3 : 1;
    }

    public Node getChildAt(int childIndex)
    {
        Node child;
        if(isCDMASystem)
            child = new LocalComponent(cdmasystem, "Victim CDMA");
        else
            switch(childIndex)
            {
            case 0: // '\0'
                child = new LocalComponent(getVictimReceiver(), "Victim receiver");
                break;

            case 1: // '\001'
                child = new LocalComponent(getWantedTransmitter(), "Wanted transmitter");
                break;

            case 2: // '\002'
                child = new LocalComponent(getWt2VrPath(), "WTx-VRx path");
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("ChildIndex out of range <").append(childIndex).append(">").toString());
            }
        return child;
    }

    public void setCDMASystem(CDMASystem cdma)
    {
        cdmasystem = cdma;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/core/VictimSystemLink);
    private static final int KM_TO_METERS = 1000;
    private static final int PID = 180;
    private boolean useCorrelatedDistance;
    private boolean useWantedTransmitter;
    private WantedTransmitter wantedTransmitter;
    private VictimReceiver victimReceiver;
    private Distribution dRSS;
    private Distribution frequency;
    private boolean isCDMASystem;
    private CDMASystem cdmasystem;

}
