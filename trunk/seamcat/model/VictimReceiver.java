// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   VictimReceiver.java

package org.seamcat.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Function;
import org.seamcat.presentation.Node;
import org.seamcat.presentation.components.LocalComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model:
//            Receiver, NodeAttribute, Antenna

public class VictimReceiver extends Receiver
{

    public VictimReceiver(Receiver receiver)
    {
        super(receiver);
        intermodResponse = new DiscreteFunction();
        blockingAttenuationMode = 0;
        refAntennaHeight = 0.0D;
        criterionCalcMode = 0;
        checkPcMax = false;
        vrTrialFrequency = 0.0D;
        blockingAttenuationMode = 0;
        if(receiver instanceof VictimReceiver)
        {
            VictimReceiver vr = (VictimReceiver)receiver;
            setIntermodResponse(new DiscreteFunction((DiscreteFunction)vr.getIntermodResponse()));
            setRefAntennaHeight(vr.getRefAntennaHeight());
        }
    }

    public VictimReceiver(Element element)
    {
        super((Element)element.getElementsByTagName("receiver").item(0));
        intermodResponse = new DiscreteFunction();
        blockingAttenuationMode = 0;
        refAntennaHeight = 0.0D;
        criterionCalcMode = 0;
        checkPcMax = false;
        vrTrialFrequency = 0.0D;
        setVrTrialFrequency(Double.parseDouble(element.getAttribute("vrTrialFrequency")));
        setCheckPcMax(Boolean.valueOf(element.getAttribute("checkPcMax")).booleanValue());
        setCriterionCalcMode(Integer.parseInt(element.getAttribute("criterionCalcMode")));
        setBlockingAttenuationMode(Integer.parseInt(element.getAttribute("blocking-attenuation-mode")));
        try
        {
            setIntermodResponse(new DiscreteFunction((Element)element.getElementsByTagName("intermodResponse").item(0).getFirstChild()));
        }
        catch(Exception e)
        {
            LOG.warn("Could not load intermodulation response");
        }
    }

    public void setBlockingAttenuationMode(String name)
    {
        setBlockingAttenuationMode(resolveBlockingAttenuationMode(name));
    }

    public static int resolveBlockingAttenuationMode(String name)
    {
        for(int i = 0; i < ATTENUATION_MODES.length; i++)
            if(name.equals(ATTENUATION_MODES[i]))
                return i;

        return -1;
    }

    public void setBlockingAttenuationMode(int blockingAttenuationMode)
    {
        this.blockingAttenuationMode = blockingAttenuationMode;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("VictimReceiver");
        element.setAttribute("vrTrialFrequency", Double.toString(getVrTrialFrequency()));
        element.setAttribute("checkPcMax", Boolean.toString(getCheckPcMax()));
        element.setAttribute("pcMax", Double.toString(getPowerControlMaxThreshold()));
        element.setAttribute("criterionCalcMode", Integer.toString(getCriterionCalcMode()));
        element.setAttribute("refAntennaHeight", Double.toString(getRefAntennaHeight()));
        element.setAttribute("blocking-attenuation-mode", String.valueOf(blockingAttenuationMode));
        Element intermodResponseElement = doc.createElement("intermodResponse");
        Element functionElement = doc.createElement("function");
        functionElement.appendChild(getIntermodResponse().toElement(doc));
        intermodResponseElement.appendChild(functionElement);
        element.appendChild(intermodResponseElement);
        element.appendChild(super.toElement(doc));
        return element;
    }

    public int getBlockingAttenuationMode()
    {
        return blockingAttenuationMode;
    }

    public double getCiLevel()
    {
        return getProtectionRatio();
    }

    public double getCniLevel()
    {
        return getExtendedProtectionRatio();
    }

    public double getNinLevel()
    {
        return getNoiseAugmentation();
    }

    public double getInLevel()
    {
        return getInterferenceToNoiseRatio();
    }

    public int getCriterionCalcMode()
    {
        return criterionCalcMode;
    }

    public void setCriterionCalcMode(int criterionCalcMode)
    {
        this.criterionCalcMode = criterionCalcMode;
    }

    public boolean getCheckPcMax()
    {
        return checkPcMax;
    }

    public void setCheckPcMax(boolean checkPcMax)
    {
        this.checkPcMax = checkPcMax;
    }

    public double getBandwidth()
    {
        return getReceptionBandwith() / 1000D;
    }

    public double getVrTrialFrequency()
    {
        return vrTrialFrequency;
    }

    public void setVrTrialFrequency(double vrTrialFrequency)
    {
        this.vrTrialFrequency = vrTrialFrequency;
    }

    public double getRefAntennaHeight()
    {
        return refAntennaHeight;
    }

    public void setRefAntennaHeight(double value)
    {
        refAntennaHeight = value;
    }

    public Function getIntermodResponse()
    {
        return intermodResponse;
    }

    public void setIntermodResponse(Function value)
    {
        intermodResponse = value;
    }

    public boolean equals(Receiver r)
    {
        return r != null && r.getReference().equals(getReference());
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
        nodeList.add(new NodeAttribute("Blocking Attenuation mode", "", ATTENUATION_MODES[getBlockingAttenuationMode()], "String", ATTENUATION_MODES, true, true, null));
        nodeList.add(new NodeAttribute("Sensitivity", "dBm", new Double(getSensitivity()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Reception bandwith", "kHz", new Double(getReceptionBandwith()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Intermodulation rejection", "X(MHz) / Y(dB)", getIntermodulationRejection(), "Function", null, false, false, null));
        nodeList.add(new NodeAttribute("Use power control threshold", "", getUsePowerControlThreshold() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, true, true, null));
        nodeList.add(new NodeAttribute("Antenna height", "m", getAntennaHeight(), "Distribution", null, false, false, null));
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
            setBlockingAttenuationMode((String)aValue);
            break;

        case 10: // '\n'
            setSensitivity(((Number)aValue).doubleValue());
            break;

        case 11: // '\013'
            setReceptionBandwith(((Number)aValue).doubleValue());
            break;

        case 12: // '\f'
            setIntermodulationRejection((Function)aValue);
            break;

        case 13: // '\r'
            setUsePowerControlThreshold(((Boolean)aValue).booleanValue());
            break;

        case 14: // '\016'
            setAntennaHeight((Distribution)aValue);
            break;
        }
    }

    public int getChildCount()
    {
        return 1;
    }

    public Node getChildAt(int childIndex)
    {
        Node child;
        if(childIndex == 0)
            child = new LocalComponent(getAntenna(), "Antenna");
        else
            throw new IllegalArgumentException("ChildIndex out of range");
        return child;
    }

    public boolean isLeaf()
    {
        return false;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/VictimReceiver);
    public static final int BLOCKING_ATTENUATION_MODE_USER_DEFINED = 0;
    public static final int BLOCKING_ATTENUATION_MODE_PROTECTION_RATIO = 1;
    public static final int BLOCKING_ATTENUATION_MODE_SENSITIVITY = 2;
    public static final String ATTENUATION_MODES[] = {
        "User Defined", "Protection Ratio", "Sensitivity"
    };
    private Function intermodResponse;
    private int blockingAttenuationMode;
    private double refAntennaHeight;
    private int criterionCalcMode;
    private boolean checkPcMax;
    private double vrTrialFrequency;

}