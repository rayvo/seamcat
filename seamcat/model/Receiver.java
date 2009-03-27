// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Receiver.java

package org.seamcat.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.*;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model:
//            Transceiver, Antenna, NodeAttribute, Components

public class Receiver extends Transceiver
{

    public Receiver()
    {
        protectionRatio = 19D;
        extendedProtectionRatio = 16D;
        noiseAugmentation = 3D;
        interferenceToNoiseRatio = 0.0D;
        noiseFloorDistribution = new ConstantDistribution(-110D);
        blockingResponse = new ConstantFunction(0.0D);
        intermodulationRejection = new DiscreteFunction();
        sensitivity = -103D;
        receptionBandwith = 200D;
        usePowerControlThreshold = false;
        powerControlMaxThreshold = 30D;
        setReference("DEFAULT_RX");
        setDescription("");
    }

    public Receiver(Element element, Components antennas)
    {
        super((Element)element.getElementsByTagName("transceiver").item(0));
        protectionRatio = 19D;
        extendedProtectionRatio = 16D;
        noiseAugmentation = 3D;
        interferenceToNoiseRatio = 0.0D;
        noiseFloorDistribution = new ConstantDistribution(-110D);
        blockingResponse = new ConstantFunction(0.0D);
        intermodulationRejection = new DiscreteFunction();
        sensitivity = -103D;
        receptionBandwith = 200D;
        usePowerControlThreshold = false;
        powerControlMaxThreshold = 30D;
        setProtectionRatio(Double.parseDouble(element.getAttribute("protection_ratio")));
        setExtendedProtectionRatio(Double.parseDouble(element.getAttribute("extended_protection_ratio")));
        setNoiseAugmentation(Double.parseDouble(element.getAttribute("noise_augmentation")));
        setInterferenceToNoiseRatio(Double.parseDouble(element.getAttribute("interference_to_noise_ratio")));
        setSensitivity(Double.parseDouble(element.getAttribute("sensitivity")));
        setReceptionBandwith(Double.parseDouble(element.getAttribute("reception_bandwith")));
        setUsePowerControlThreshold(Boolean.valueOf(element.getAttribute("use_power_control_threshold")).booleanValue());
        setPowerControlMaxThreshold(Double.parseDouble(element.getAttribute("power_control_max_threshold")));
        try
        {
            setNoiseFloorDistribution(Distribution.create((Element)element.getElementsByTagName("noise-floor-distribution").item(0).getFirstChild()));
        }
        catch(Exception e)
        {
            LOG.warn((new StringBuilder()).append("Noise-floor distribution could not be loaded <").append(e.getMessage()).append(">").toString());
        }
        Function f = null;
        Element e = (Element)element.getElementsByTagName("intermodulation_rejection").item(0);
        NodeList nl = e.getElementsByTagName("ConstantFunction");
        if(nl.getLength() > 0)
            f = new ConstantFunction((Element)nl.item(0));
        else
        if((nl = e.getElementsByTagName("discretefunction")).getLength() > 0)
            f = new DiscreteFunction((Element)nl.item(0));
        if(f != null)
            setIntermodulationRejection(f);
        else
            LOG.warn("Intermodulation rejection could not be loaded");
        f = null;
        e = (Element)element.getElementsByTagName("blocking_response").item(0);
        nl = e.getElementsByTagName("ConstantFunction");
        if(nl.getLength() > 0)
            f = new ConstantFunction((Element)nl.item(0));
        else
        if((nl = e.getElementsByTagName("discretefunction")).getLength() > 0)
            f = new DiscreteFunction((Element)nl.item(0));
        if(f != null)
            setBlockingResponse(f);
        else
            LOG.warn("Blocking response could not be loaded");
    }

    public Receiver(Element element)
    {
        this(element, null);
    }

    public Receiver(Receiver receiver)
    {
        protectionRatio = 19D;
        extendedProtectionRatio = 16D;
        noiseAugmentation = 3D;
        interferenceToNoiseRatio = 0.0D;
        noiseFloorDistribution = new ConstantDistribution(-110D);
        blockingResponse = new ConstantFunction(0.0D);
        intermodulationRejection = new DiscreteFunction();
        sensitivity = -103D;
        receptionBandwith = 200D;
        usePowerControlThreshold = false;
        powerControlMaxThreshold = 30D;
        if(receiver == null)
        {
            new Receiver();
        } else
        {
            setReference(receiver.getReference());
            setDescription(receiver.getDescription());
            setProtectionRatio(receiver.getProtectionRatio());
            setExtendedProtectionRatio(receiver.getExtendedProtectionRatio());
            setNoiseAugmentation(receiver.getNoiseAugmentation());
            setInterferenceToNoiseRatio(receiver.getInterferenceToNoiseRatio());
            setNoiseFloorDistribution(Distribution.create(receiver.getNoiseFloorDistribution()));
            setBlockingResponse(receiver.getBlockingResponse());
            setSensitivity(receiver.getSensitivity());
            setReceptionBandwith(receiver.getReceptionBandwith());
            setIntermodulationRejection(receiver.getIntermodulationRejection());
            setUsePowerControlThreshold(receiver.getUsePowerControlThreshold());
            setPowerControlMaxThreshold(receiver.getPowerControlMaxThreshold());
            setAntennaHeight(Distribution.create(receiver.getAntennaHeight()));
            setAntenna(new Antenna(receiver.getAntenna()));
            setRowClasses(rowTypes);
            setRowNames(rowNames);
        }
    }

    public double getProtectionRatio()
    {
        return protectionRatio;
    }

    public double getExtendedProtectionRatio()
    {
        return extendedProtectionRatio;
    }

    public double getNoiseAugmentation()
    {
        return noiseAugmentation;
    }

    public double getInterferenceToNoiseRatio()
    {
        return interferenceToNoiseRatio;
    }

    public Distribution getNoiseFloorDistribution()
    {
        return noiseFloorDistribution;
    }

    public Function getBlockingResponse()
    {
        return blockingResponse;
    }

    public double getSensitivity()
    {
        return sensitivity;
    }

    public double getReceptionBandwith()
    {
        return receptionBandwith;
    }

    public Function getIntermodulationRejection()
    {
        return intermodulationRejection;
    }

    public boolean getUsePowerControlThreshold()
    {
        return usePowerControlThreshold;
    }

    public double getPowerControlMaxThreshold()
    {
        return powerControlMaxThreshold;
    }

    public double getRxTrialAntHeight()
    {
        return rxTrialAntHeight;
    }

    public void setProtectionRatio(double protectionRatio)
    {
        this.protectionRatio = protectionRatio;
    }

    public void setExtendedProtectionRatio(double extendedProtectionRatio)
    {
        this.extendedProtectionRatio = extendedProtectionRatio;
    }

    public void setNoiseAugmentation(double noiseAugmentation)
    {
        this.noiseAugmentation = noiseAugmentation;
    }

    public void setInterferenceToNoiseRatio(double interferenceToNoiseRatio)
    {
        this.interferenceToNoiseRatio = interferenceToNoiseRatio;
    }

    public void setNoiseFloorDistribution(Distribution noiseFloorDistribution)
    {
        this.noiseFloorDistribution = noiseFloorDistribution;
        nodeAttributeIsDirty = true;
    }

    public void setSensitivity(double sensitivity)
    {
        this.sensitivity = sensitivity;
    }

    public void setReceptionBandwith(double receptionBandwith)
    {
        this.receptionBandwith = receptionBandwith;
    }

    public void setUsePowerControlThreshold(boolean usePowerControlThreshold)
    {
        this.usePowerControlThreshold = usePowerControlThreshold;
    }

    public void setPowerControlMaxThreshold(double powerControlMaxThreshold)
    {
        this.powerControlMaxThreshold = powerControlMaxThreshold;
    }

    public void setRxTrialAntHeight(double rxTrialAntHeight)
    {
        this.rxTrialAntHeight = rxTrialAntHeight;
    }

    public void setIntermodulationRejection(Function intermodulationRejection)
    {
        this.intermodulationRejection = intermodulationRejection;
        nodeAttributeIsDirty = true;
    }

    public void setBlockingResponse(Function blockingResponse)
    {
        this.blockingResponse = blockingResponse;
        nodeAttributeIsDirty = true;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("receiver");
        element.setAttribute("protection_ratio", String.valueOf(getProtectionRatio()));
        element.setAttribute("extended_protection_ratio", String.valueOf(getExtendedProtectionRatio()));
        element.setAttribute("noise_augmentation", String.valueOf(getNoiseAugmentation()));
        element.setAttribute("interference_to_noise_ratio", String.valueOf(getInterferenceToNoiseRatio()));
        element.setAttribute("sensitivity", String.valueOf(getSensitivity()));
        element.setAttribute("reception_bandwith", String.valueOf(getReceptionBandwith()));
        element.setAttribute("use_power_control_threshold", String.valueOf(getUsePowerControlThreshold()));
        element.setAttribute("power_control_max_threshold", String.valueOf(getPowerControlMaxThreshold()));
        Element noiseFloorDistributionElement = doc.createElement("noise-floor-distribution");
        noiseFloorDistributionElement.appendChild(noiseFloorDistribution.toElement(doc));
        element.appendChild(noiseFloorDistributionElement);
        Element blockingResponseElement = doc.createElement("blocking_response");
        Element bfunctionElement = doc.createElement("function");
        bfunctionElement.appendChild(blockingResponse.toElement(doc));
        blockingResponseElement.appendChild(bfunctionElement);
        element.appendChild(blockingResponseElement);
        Element intermodulationRejectionElement = doc.createElement("intermodulation_rejection");
        Element functionElement = doc.createElement("function");
        functionElement.appendChild(intermodulationRejection.toElement(doc));
        intermodulationRejectionElement.appendChild(functionElement);
        element.appendChild(intermodulationRejectionElement);
        element.appendChild(super.toElement(doc));
        return element;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Reference", "", getReference(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Description", "", getDescription(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("C/I", "dB", new Double(getProtectionRatio()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Antenna Reference", "", getAntenna() != null ? ((Object) (getAntenna().getReference())) : "No antenna defined", "Antenna", null, false, true, null));
        nodeList.add(new NodeAttribute("C / (N + I)", "dB", new Double(getExtendedProtectionRatio()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("(N + I) / N", "dB", new Double(getNoiseAugmentation()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("I / N", "dB", new Double(getInterferenceToNoiseRatio()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Noise floor", "dBm", getNoiseFloorDistribution(), "Distribution", null, false, false, null));
        nodeList.add(new NodeAttribute("Blocking Response", "X(MHz) / Y(dBm or dB)", getBlockingResponse(), "Function", null, false, false, null));
        nodeList.add(new NodeAttribute("Sensitivity", "dBm", new Double(getSensitivity()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Reception bandwith", "kHz", new Double(getReceptionBandwith()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Intermodulation rejection", "X(MHz) / Y(dB)", getIntermodulationRejection(), "Function", null, false, false, null));
        nodeList.add(new NodeAttribute("Use power control threshold", "", getUsePowerControlThreshold() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, true, true, null));
        nodeAttributes = (NodeAttribute[])(NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
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
            setProtectionRatio(((Number)aValue).doubleValue());
            break;

        case 3: // '\003'
            getAntenna().setReference((String)aValue);
            break;

        case 4: // '\004'
            setExtendedProtectionRatio(((Number)aValue).doubleValue());
            break;

        case 5: // '\005'
            setNoiseAugmentation(((Number)aValue).doubleValue());
            break;

        case 6: // '\006'
            setInterferenceToNoiseRatio(((Number)aValue).doubleValue());
            break;

        case 7: // '\007'
            setNoiseFloorDistribution((Distribution)aValue);
            break;

        case 8: // '\b'
            setBlockingResponse((Function)aValue);
            break;

        case 9: // '\t'
            setSensitivity(((Number)aValue).doubleValue());
            break;

        case 10: // '\n'
            setReceptionBandwith(((Number)aValue).doubleValue());
            break;

        case 11: // '\013'
            setIntermodulationRejection((Function)aValue);
            break;

        case 12: // '\f'
            setUsePowerControlThreshold(((Boolean)aValue).booleanValue());
            break;
        }
    }

    public boolean equals(Object o)
    {
        return o != null && (o instanceof Receiver) && ((Receiver)o).getReference().equals(getReference());
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/Receiver);
    private double protectionRatio;
    private double extendedProtectionRatio;
    private double noiseAugmentation;
    private double interferenceToNoiseRatio;
    private Distribution noiseFloorDistribution;
    private Function blockingResponse;
    private Function intermodulationRejection;
    private double sensitivity;
    private double receptionBandwith;
    private boolean usePowerControlThreshold;
    private double powerControlMaxThreshold;
    private double rxTrialAntHeight;

}