// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   InterferingSystemLink.java

package org.seamcat.model.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.*;
import org.seamcat.model.engines.EGE;
import org.seamcat.model.technical.exception.*;
import org.seamcat.propagation.PropagationModel;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.core:
//            SystemLink, InterferingTransmitter, PowerControl

public class InterferingSystemLink extends SystemLink
{

    public InterferingSystemLink(Element element)
    {
        super((Element)element.getElementsByTagName("systemLink").item(0));
        if(element.getElementsByTagName("description").item(0).getFirstChild() != null)
        {
            CDATASection datasection = (CDATASection)element.getElementsByTagName("description").item(0).getFirstChild();
            super.setDescription(datasection.getData());
        }
        setInterferingTransmitter(new InterferingTransmitter((Element)element.getElementsByTagName("InterferingTransmitter").item(0)));
        setWantedReceiver(new WantedReceiver((Element)element.getElementsByTagName("WantedReceiver").item(0).getFirstChild()));
        getInterferingTransmitter().setDens(getWt2VrPath().getDensity());
        getInterferingTransmitter().setNumberOfChannels(getWt2VrPath().getNumberOfChannels());
        getInterferingTransmitter().setNumberOfUsersPerChannel(getWt2VrPath().getNumberOfUsersPerChannel());
        getInterferingTransmitter().setFreqCluster(getWt2VrPath().getFrequencyCluster());
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("InterferingSystemLink");
        element.setAttribute("reference", getReference());
        Element descriptionElement = doc.createElement("description");
        descriptionElement.appendChild(doc.createCDATASection(getDescription()));
        element.appendChild(descriptionElement);
        element.appendChild(getInterferingTransmitter().toElement(doc));
        Element wantedReceiverElement = doc.createElement("WantedReceiver");
        wantedReceiverElement.appendChild(getWantedReceiver().toElement(doc));
        element.appendChild(wantedReceiverElement);
        element.appendChild(super.toElement(doc));
        return element;
    }

    public InterferingSystemLink(String name, String description, InterferingTransmitter _it, WantedReceiver _wr)
    {
        super(name, description);
        interferingTransmitter = _it;
        wantedReceiver = _wr;
    }

    public InterferingSystemLink(String name, InterferingSystemLink link)
    {
        super(name, link.getDescription());
        setInterferingTransmitter(new InterferingTransmitter(link.getInterferingTransmitter()));
        setWantedReceiver(new WantedReceiver(link.getWantedReceiver()));
        setPowerControl(new PowerControl(link.getPowerControl()));
        setWt2VrPath(link.getWt2VrPath().duplicate());
        setRxTxAntennaGain(link.getRxTxAntennaGain());
        setReceiverToTransmitterAzimuth(Distribution.create(link.getReceiverToTransmitterAzimuth()));
        setRxTxAzimuth(link.getRxTxAzimuth());
        setRxTxElevation(link.getRxTxElevation());
        setReceiverToTransmitterElevation(Distribution.create(link.getReceiverToTransmitterElevation()));
        setRxTxHorTol(link.getRxTxHorTol());
        setRxTxTilt(link.getRxTxTilt());
        setRxTxVerTol(link.getRxTxVerTol());
        setTxRxAngle(link.getTxRxAngle());
        setTxRxAntennaGain(link.getTxRxAntennaGain());
        setTransmitterToReceiverAzimuth(Distribution.create(link.getTransmitterToReceiverAzimuth()));
        setTxRxAzimuth(link.getTxRxAzimuth());
        setTxRxDistance(link.getTxRxDistance());
        setTxRxElevation(link.getTxRxElevation());
        setTransmitterToReceiverElevation(Distribution.create(link.getTransmitterToReceiverElevation()));
        setTxRxHorTol(link.getTxRxHorTol());
        setTxRxPathLoss(link.getTxRxPathLoss());
        setTxRxTilt(link.getTxRxTilt());
        setTxRxVerTol(link.getTxRxVerTol());
    }

    public void updateNodeAttributes()
    {
        super.updateNodeAttributes();
        getInterferingTransmitter().updateNodeAttributes();
        getWantedReceiver().updateNodeAttributes();
        getWt2VrPath().updateNodeAttributes();
    }

    public InterferingTransmitter getInterferingTransmitter()
    {
        return interferingTransmitter;
    }

    public void setInterferingTransmitter(InterferingTransmitter interferingTransmitter)
    {
        this.interferingTransmitter = interferingTransmitter;
    }

    public WantedReceiver getWantedReceiver()
    {
        return wantedReceiver;
    }

    public void setWantedReceiver(WantedReceiver wantedReceiver)
    {
        this.wantedReceiver = wantedReceiver;
    }

    public PowerControl getPowerControl()
    {
        return interferingTransmitter.getPowerControl();
    }

    public void setPowerControl(PowerControl powerControl)
    {
        interferingTransmitter.setPowerControl(powerControl);
    }

    public void powerControlGain(boolean debugMode)
    {
        double rPowerSupplied = getInterferingTransmitter().getTxTrialPower();
        double rGainItWr = getTxRxAntennaGain();
        double rGainWrIt = getRxTxAntennaGain();
        double rPathLossItWr = getTxRxPathLoss();
        double rStep = getPowerControl().getPowerControlStep();
        double rPmin = getPowerControl().getPowerControlMinimum();
        double rRange = getPowerControl().getPowerControlRange();
        double rPinit = ((rPowerSupplied + rGainItWr) - rPathLossItWr) + rGainWrIt;
        double rResultPowerGain;
        if(rPinit > rPmin && rPinit < rPmin + rRange)
            rResultPowerGain = -rStep * Math.floor((rPinit - rPmin) / rStep);
        else
        if(rPinit <= rPmin)
            rResultPowerGain = 0.0D;
        else
            rResultPowerGain = -rRange;
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("IT Power control initial Wr received power = ").append(rPinit).toString());
            LOG.debug((new StringBuilder()).append("IT Power control calculated gain = ").append(rResultPowerGain).toString());
        }
        getPowerControl().setInterferingTransmitterPowerControlGain(rResultPowerGain);
    }

    public void iSLPathAntGains(boolean debugMode)
        throws PatternException
    {
        double rFreqIt = getInterferingTransmitter().getItTrialFrequency();
        double rRxTxAzi = getRxTxAzimuth();
        double rRxTxElev = getRxTxElevation();
        double rTxRxAzi = getTxRxAzimuth();
        double rTxRxElev = getTxRxElevation();
        double rWrPeakGain = getWantedReceiver().getAntenna().getPeakGain();
        try
        {
            double rWrGainResult = getWantedReceiver().getAntenna().calculateGain(rRxTxAzi, rRxTxElev);
            setRxTxAntennaGain(rWrGainResult);
            if(debugMode)
                LOG.debug((new StringBuilder()).append("Wanted Receiver - Interfering Transmitter gain = ").append(rWrGainResult).toString());
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on Wr->It antenna: ").append(e.getMessage()).toString(), e);
        }
        try
        {
            double rItGainResult = getInterferingTransmitter().getAntenna().calculateGain(rTxRxAzi, rTxRxElev);
            if(debugMode)
                LOG.debug((new StringBuilder()).append("Interfering Transmitter - Wanted Receiver gain = ").append(rItGainResult).toString());
            setTxRxAntennaGain(rItGainResult);
        }
        catch(PatternException e)
        {
            throw new PatternException((new StringBuilder()).append("Error on It->Wr antenna: ").append(e.getMessage()).toString(), e);
        }
    }

    public void wrTrial(boolean debugMode)
        throws RandomException
    {
        Distribution antHeight = getWantedReceiver().getAntennaHeight();
        double rResultAntHeight;
        try
        {
            rResultAntHeight = antHeight.trial();
        }
        catch(Exception exception)
        {
            throw new RandomException();
        }
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Wanted Receiver antenna height = ").append(rResultAntHeight).toString());
        getWantedReceiver().setRxTrialAntHeight(rResultAntHeight);
    }

    public void itTrial(boolean debugMode)
        throws RandomException
    {
        double antHeight = getInterferingTransmitter().getAntennaHeight().trial();
        getInterferingTransmitter().setTxTrialAntHeight(antHeight);
        Distribution power = getInterferingTransmitter().getPowerSuppliedDistribution();
        double rResultPower;
        try
        {
            rResultPower = power.trial();
        }
        catch(Exception exception)
        {
            throw new RandomException();
        }
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Interfering Transmitter supplied power = ").append(rResultPower).toString());
        getInterferingTransmitter().setTxTrialPower(rResultPower);
        Distribution frequency = getInterferingTransmitter().getFrequency();
        double rResultFrequency;
        try
        {
            rResultFrequency = frequency.trial();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
            throw new RandomException();
        }
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Interfering Transmitter frequency = ").append(rResultFrequency).toString());
        getInterferingTransmitter().setItTrialFrequency(rResultFrequency);
    }

    public void iSLPathAntAziElev(boolean debugMode)
        throws RandomException, GeometricException
    {
        double rItAntHeight = getInterferingTransmitter().getTxTrialAntHeight();
        double rWrAntHeight = getWantedReceiver().getRxTrialAntHeight();
        double rTxRxDistance = getTxRxDistance() * 1000D;
        Distribution itWrAzi = getTransmitterToReceiverAzimuth();
        Distribution itWrElev = getTransmitterToReceiverElevation();
        Distribution wrItAzi = getReceiverToTransmitterAzimuth();
        Distribution wrItElev = getReceiverToTransmitterElevation();
        double rItWrAziTrial;
        double rWrItAziTrial;
        double rItWrElevTrial;
        double rWrItElevTrial;
        try
        {
            rItWrAziTrial = itWrAzi.trial();
            rItWrElevTrial = itWrElev.trial();
            rWrItAziTrial = wrItAzi.trial();
            rWrItElevTrial = wrItElev.trial();
        }
        catch(Exception exception)
        {
            throw new RandomException();
        }
        double rWrItAziResult = rWrItAziTrial;
        setRxTxAzimuth(rWrItAziResult);
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Wanted Receiver -> Interfering Transmitter azimuth = ").append(rWrItAziResult).toString());
        double rWrItTilt = rWrItElevTrial;
        double rWrItAlpha;
        double rWrItPhi;
        try
        {
            rWrItPhi = (Math.atan2(rItAntHeight - rWrAntHeight, rTxRxDistance) * 180D) / 3.1415926535897931D;
            rWrItAlpha = rWrItTilt * Mathematics.cosD(rWrItAziResult);
        }
        catch(Exception exception)
        {
            throw new GeometricException();
        }
        double rWrItElevResult = rWrItPhi - rWrItAlpha;
        setRxTxElevation(rWrItElevResult);
        setRxTxTilt(rWrItTilt);
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Wanted Receiver -> Interfering Transmitter elevation = ").append(rWrItElevResult).toString());
            LOG.debug((new StringBuilder()).append("Wanted Receiver -> Interfering Transmitter tilt = ").append(rWrItTilt).toString());
        }
        double rItWrAziResult = rItWrAziTrial;
        setTxRxAzimuth(rItWrAziResult);
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Interfering Transmitter -> Wanted Receiver azimuth = ").append(rItWrAziResult).toString());
        double rItWrTilt = rItWrElevTrial;
        double rItWrAlpha;
        double rItWrPhi;
        try
        {
            rItWrPhi = (Math.atan2(rWrAntHeight - rItAntHeight, rTxRxDistance) * 180D) / 3.1415926535897931D;
            rItWrAlpha = rItWrTilt * Mathematics.cosD(rItWrAziResult);
        }
        catch(Exception exception)
        {
            throw new GeometricException();
        }
        double rItWrElevResult = rItWrPhi - rItWrAlpha;
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Interfering Transmitter -> Wanted Receiver elevation = ").append(rItWrElevResult).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter -> Wanted Receiver tilt = ").append(rItWrTilt).toString());
        }
        setTxRxElevation(rItWrElevResult);
        setTxRxTilt(rItWrTilt);
    }

    public void iSLPropagationLoss(boolean debugMode)
        throws ModelException
    {
        PropagationModel pathlossModel = getWt2VrPath().getPropagationModel();
        double rPropLossResult;
        try
        {
            rPropLossResult = pathlossModel.evaluate(getInterferingTransmitter().getItTrialFrequency(), getTxRxDistance(), getInterferingTransmitter().getTxTrialAntHeight(), getWantedReceiver().getRxTrialAntHeight());
        }
        catch(Exception exception)
        {
            throw new ModelException();
        }
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Propagation Loss from Interfering transmitter to Wanted Receiver = ").append(rPropLossResult).append(" dB = <").append(pathlossModel.getClass().getName()).append("> ").append(" = evaluate(").append(getInterferingTransmitter().getItTrialFrequency()).append(",").append(getTxRxDistance()).append(",").append(getInterferingTransmitter().getTxTrialAntHeight()).append(",").append(getWantedReceiver().getRxTrialAntHeight()).append(")").toString());
        setTxRxPathLoss(rPropLossResult);
    }

    public void calculateRelativeTransmitterReceiverLocation(boolean debugMode)
        throws RandomException, GeometricException
    {
        if(getWt2VrPath().getUseCorrelationDistance())
        {
            if(debugMode)
                LOG.debug("Interfering Transmitter -> Wanted Reciever distance is correlated");
            double rResultPosX = getWt2VrPath().getDeltaX();
            double rResultPosY = getWt2VrPath().getDeltaY();
            double rResultItWrDistance;
            double rResultItWrAngle;
            try
            {
                rResultItWrAngle = (Math.atan2(rResultPosY, rResultPosX) * 180D) / 3.1415926535897931D;
                rResultItWrDistance = Math.sqrt(rResultPosX * rResultPosX + rResultPosY * rResultPosY);
            }
            catch(Exception exception)
            {
                throw new GeometricException();
            }
            if(debugMode)
            {
                LOG.debug((new StringBuilder()).append("Interfering Transmitter - Wanted Receiver angle = ").append(rResultItWrAngle).toString());
                LOG.debug((new StringBuilder()).append("Interfering Transmitter - Wanted Receiver distance = ").append(rResultItWrDistance).toString());
            }
            setTxRxAngle(rResultItWrAngle);
            setTxRxDistance(rResultItWrDistance);
            getWantedReceiver().setX(rResultPosX);
            getWantedReceiver().setY(rResultPosY);
        } else
        {
            if(debugMode)
                LOG.debug("Interfering Transmitter -> Wanted Reciever distance is NOT correlated");
            Distribution itWrDistance = getWt2VrPath().getPathDistanceFactor();
            Distribution itWrAngle = getWt2VrPath().getPathAzimuth();
            double rRmax = getInterferingTransmitter().getRMax();
            double rItWrDistTrial;
            double rItWrAngleTrial;
            try
            {
                rItWrDistTrial = itWrDistance.trial();
                rItWrAngleTrial = itWrAngle.trial();
            }
            catch(Exception exception)
            {
                throw new RandomException();
            }
            double rResultItWrDistance = rRmax * rItWrDistTrial;
            double rResultPosX;
            double rResultPosY;
            try
            {
                rResultPosX = rResultItWrDistance * Mathematics.cosD(rItWrAngleTrial);
                rResultPosY = rResultItWrDistance * Mathematics.sinD(rItWrAngleTrial);
            }
            catch(Exception e)
            {
                throw new GeometricException();
            }
            if(debugMode)
            {
                LOG.debug((new StringBuilder()).append("Interfering Transmitter - Wanted Receiver angle = ").append(rItWrAngleTrial).toString());
                LOG.debug((new StringBuilder()).append("Interfering Transmitter - Wanted Receiver distance = rMax * distanceTrial = ").append(rResultItWrDistance).append(" = ").append(rRmax).append(" * ").append(rItWrDistTrial).toString());
                LOG.debug((new StringBuilder()).append("Interfering Transmitter - Wanted Receiver Relative Position X = ").append(rResultPosX).toString());
                LOG.debug((new StringBuilder()).append("Interfering Transmitter - Wanted Receiver Relative Position Y = ").append(rResultPosY).toString());
            }
            setTxRxAngle(rItWrAngleTrial);
            setTxRxDistance(rResultItWrDistance);
            getWantedReceiver().setX(rResultPosX);
            getWantedReceiver().setY(rResultPosY);
            getWt2VrPath().setDeltaX(rResultPosX);
            getWt2VrPath().setDeltaY(rResultPosY);
        }
    }

    public void coverageRadiusNoiseInterferingTransmitter()
    {
        double rMinDist = getInterferingTransmitter().getMinDist() * 1000D;
        double rMaxDist = getInterferingTransmitter().getMaxDist() * 1000D;
        double rRefPower = getInterferingTransmitter().getRefPower();
        double rR = 0.0D;
        double rDR = 0.0D;
        double rR1 = rMinDist;
        double rR2 = rMaxDist;
        double rLogR1 = Math.log10(rR1);
        double rLogR2 = Math.log10(rR2);
        double rL1 = rRefPower;
        double rL2 = rRefPower;
        if(rL1 * rL2 >= 0.0D)
            getInterferingTransmitter().setRMax(rMaxDist);
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
        int i = 0;
        do
        {
            if(i >= 1000)
                break;
            double rLogR = rLogR1 + ((rLogR2 - rLogR1) * rL1) / (rL1 - rL2);
            rR = Math.pow(10D, rLogR);
            double rL = rRefPower;
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
        getInterferingTransmitter().setRMax(rR / 1000D);
    }

    public void coverageRadiusInterferingTransmitter()
        throws GeometricException
    {
        switch(getWt2VrPath().getCoverageRadiusCalculationMode())
        {
        case 0: // '\0'
            getInterferingTransmitter().setCoverageRadius(getWt2VrPath().getCoverageRadius());
            getInterferingTransmitter().coverageRadiusUserIt();
            break;

        case 1: // '\001'
            coverageRadiusNoiseInterferingTransmitter();
            break;

        case 2: // '\002'
            getInterferingTransmitter().coverageRadiusTrafficIt();
            break;
        }
    }

    public TransmitterToReceiverPath getWt2VrPath()
    {
        return wt2VrPath;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Correlated", "", getWt2VrPath().getUseCorrelationDistance() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, true, true, null));
        if(getWt2VrPath().getUseCorrelationDistance())
        {
            nodeList.add(new NodeAttribute("Fixed distance (X)", "", new Double(getWt2VrPath().getDeltaX()), "Double", null, false, true, null));
            nodeList.add(new NodeAttribute("Fixed distance (Y)", "", new Double(getWt2VrPath().getDeltaY()), "Double", null, false, true, null));
        } else
        {
            nodeList.add(new NodeAttribute("Path distance factor", "", getWt2VrPath().getPathDistanceFactor(), "Distribution", null, false, false, null));
            nodeList.add(new NodeAttribute("Path azimuth", "", getWt2VrPath().getPathAzimuth(), "Distribution", null, false, false, null));
        }
        nodeAttributes = (NodeAttribute[])(NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
        if(rowIndex == 0)
            getWt2VrPath().setUseCorrelatedDistance(((Boolean)aValue).booleanValue());
        else
        if(getWt2VrPath().getUseCorrelationDistance())
            switch(rowIndex)
            {
            case 1: // '\001'
                getWt2VrPath().setDeltaX(((Number)aValue).doubleValue());
                break;

            case 2: // '\002'
                getWt2VrPath().setDeltaY(((Number)aValue).doubleValue());
                break;
            }
        else
            switch(rowIndex)
            {
            case 1: // '\001'
                getWt2VrPath().setPathDistanceFactor((Distribution)aValue);
                break;

            case 2: // '\002'
                getWt2VrPath().setPathAzimuth((Distribution)aValue);
                break;
            }
    }

    public String toString()
    {
        return "Relative location";
    }

    public PropagationModel getPropagationModel()
    {
        throw new UnsupportedOperationException("Use Transmitter to receiver path");
    }

    private final int KMTOM = 1000;
    private final int PID = 180;
    private InterferingTransmitter interferingTransmitter;
    private WantedReceiver wantedReceiver;
    private static final Logger LOG = Logger.getLogger(org/seamcat/model/engines/EGE);

}