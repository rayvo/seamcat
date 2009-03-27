// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Transmitter.java

package org.seamcat.model;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.function.Function2;
import org.seamcat.function.Point3D;
import org.seamcat.model.core.PowerControl;
import org.seamcat.presentation.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model:
//            Transceiver, Antenna, NodeAttribute, Components, 
//            Model, Library, SeamcatComponent

public class Transmitter extends Transceiver
{
    protected class PowerControlTreeElement extends SeamcatComponent
    {

        public String toString()
        {
            return "Power Control";
        }

        protected void initNodeAttributes()
        {
            List nodeList = new ArrayList();
            nodeList.add(new NodeAttribute("Power control step", "dB", new Double(getPowerControlStep()), "Double", null, false, true, null));
            nodeList.add(new NodeAttribute("Minimum received power", "dBm", new Double(getMinReceivedPower()), "Double", null, false, true, null));
            nodeList.add(new NodeAttribute("Power control range", "dBm", new Double(getPowerControlRange()), "Double", null, false, true, null));
            nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
        }

        protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
            switch(rowIndex)
            {
            case 0: // '\0'
                setPowerControlStep(((Number)aValue).doubleValue());
                break;

            case 1: // '\001'
                setPowerControlMinimum(((Number)aValue).doubleValue());
                break;

            case 2: // '\002'
                setPowerControlRange(((Number)aValue).doubleValue());
                break;
            }
        }

        public Element toElement(Document doc)
        {
            return null;
        }

        public boolean isLeaf()
        {
            return false;
        }

        final Transmitter this$0;

        protected PowerControlTreeElement()
        {
            this$0 = Transmitter.this;
            super();
        }
    }

    private class CoverageRadius extends SeamcatComponent
    {

        public String toString()
        {
            return "Coverage radius";
        }

        protected void initNodeAttributes()
        {
            List nodeList = new ArrayList();
            nodeList.add(new NodeAttribute("Fixed coverage radius", "km", new Double(getFixedCoverageRadius()), "Double", null, false, true, null));
            nodeList.add(new NodeAttribute("Users density", "users / km\252", new Double(getUserDensity()), "Double", null, false, true, null));
            nodeList.add(new NodeAttribute("Number of channels", "", new Integer(getNumberOfChannels()), "Integer", null, false, true, null));
            nodeList.add(new NodeAttribute("Number of users per channel", "usr / ch", new Integer(getNumberOfUsersPerChannel()), "Integer", null, false, true, null));
            nodeList.add(new NodeAttribute("Frequency re-use", "", new Integer(getFrequencyReUse()), "Integer", null, false, true, null));
            nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
        }

        protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
            switch(rowIndex)
            {
            case 0: // '\0'
                setFixedCoverageRadius(((Number)aValue).doubleValue());
                break;

            case 1: // '\001'
                setUserDensity(((Number)aValue).doubleValue());
                break;

            case 2: // '\002'
                setNumberOfChannels(((Number)aValue).intValue());
                break;

            case 3: // '\003'
                setNumberOfUsersPerChannel(((Number)aValue).intValue());
                break;

            case 4: // '\004'
                setFrequencyReUse(((Number)aValue).intValue());
                break;
            }
        }

        public Element toElement(Document doc)
        {
            return null;
        }

        public boolean isLeaf()
        {
            return false;
        }

        final Transmitter this$0;

        private CoverageRadius()
        {
            this$0 = Transmitter.this;
            super();
        }

    }


    public Transmitter()
    {
        powerSuppliedDistribution = new ConstantDistribution(40D);
        unwantedEmissions = new DiscreteFunction2();
        unwantedEmissionsFloor = new DiscreteFunction2();
        powerControl = new PowerControl();
        usePowerControl = false;
        useUnwantedEmissionFloor = false;
        fixedCoverageRadius = 10D;
        userDensity = 10D;
        numberOfChannels = 64;
        numberOfUsersPerChannel = 7;
        frequencyReUse = 8;
        rMax = 1.0D;
        dens = 10D;
        freqCluster = 8;
        coverageCalculMode = 2;
        setReference("DEFAULT_TX");
        ((DiscreteFunction2)unwantedEmissions).addPoint(new Point3D(-10D, -8.2400000000000002D, 30D));
        ((DiscreteFunction2)unwantedEmissions).addPoint(new Point3D(10D, -8.2400000000000002D, 30D));
        ((DiscreteFunction2)unwantedEmissionsFloor).addPoint(new Point3D(-10D, -8.2400000000000002D, 30D));
        ((DiscreteFunction2)unwantedEmissionsFloor).addPoint(new Point3D(10D, -8.2400000000000002D, 30D));
    }

    public Transmitter(Element element, Components antennas)
    {
        super((Element)element.getElementsByTagName("transceiver").item(0));
        powerSuppliedDistribution = new ConstantDistribution(40D);
        unwantedEmissions = new DiscreteFunction2();
        unwantedEmissionsFloor = new DiscreteFunction2();
        powerControl = new PowerControl();
        usePowerControl = false;
        useUnwantedEmissionFloor = false;
        fixedCoverageRadius = 10D;
        userDensity = 10D;
        numberOfChannels = 64;
        numberOfUsersPerChannel = 7;
        frequencyReUse = 8;
        rMax = 1.0D;
        dens = 10D;
        freqCluster = 8;
        coverageCalculMode = 2;
        try
        {
            powerSuppliedDistribution = Distribution.create((Element)element.getElementsByTagName("power-supplied-distribution").item(0).getFirstChild());
        }
        catch(Exception e)
        {
            LOG.warn("Could not load power supplied distribution");
        }
        String antennaRef = element.getAttribute("antenna");
        if(antennas == null)
            antennas = Model.getInstance().getLibrary().getAntennas();
        Iterator i$ = antennas.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Antenna antenna = (Antenna)i$.next();
            if(antenna.getReference().equalsIgnoreCase(antennaRef))
                setAntenna(antenna);
        } while(true);
        setUsePowerControl(Boolean.valueOf(element.getAttribute("use_power_control")).booleanValue());
        setUseUnwantedEmission(Boolean.valueOf(element.getAttribute("use_unwanted_emission")).booleanValue());
        setFixedCoverageRadius(Double.parseDouble(element.getAttribute("fixed_coverage_radius")));
        setUserDensity(Double.parseDouble(element.getAttribute("user_density")));
        setNumberOfChannels(Integer.parseInt(element.getAttribute("number_of_channels")));
        setNumberOfUsersPerChannel(Integer.parseInt(element.getAttribute("number_of_users_per_channel")));
        setFrequencyReUse(Integer.parseInt(element.getAttribute("frequency_re_use")));
        NodeList nl = element.getElementsByTagName("unwantedemission");
        if(nl.getLength() > 0)
        {
            setUnwantedEmissions(new DiscreteFunction2((Element)nl.item(0).getFirstChild()));
        } else
        {
            setUnwantedEmissions(new DiscreteFunction2());
            ((DiscreteFunction2)unwantedEmissions).addPoint(new Point3D(-0.10000000000000001D, -8.2400000000000002D, 30D));
            ((DiscreteFunction2)unwantedEmissions).addPoint(new Point3D(0.10000000000000001D, -8.2400000000000002D, 30D));
        }
        nl = element.getElementsByTagName("unwantedemissionfloor");
        if(nl.getLength() > 0)
        {
            setUnwantedEmissionsFloor(new DiscreteFunction2((Element)nl.item(0).getFirstChild()));
        } else
        {
            setUnwantedEmissionsFloor(new DiscreteFunction2());
            ((DiscreteFunction2)unwantedEmissionsFloor).addPoint(new Point3D(-0.10000000000000001D, -8.2400000000000002D, 30D));
            ((DiscreteFunction2)unwantedEmissionsFloor).addPoint(new Point3D(0.10000000000000001D, -8.2400000000000002D, 30D));
        }
        nl = element.getElementsByTagName("powercontrol");
        if(nl.getLength() > 0)
            setPowerControl(new PowerControl((Element)nl.item(0)));
    }

    public Transmitter(Element element)
    {
        this(element, null);
    }

    public Transmitter(Transmitter transmitter)
    {
        this();
        if(transmitter != null)
        {
            super.setReference(transmitter.getReference());
            super.setDescription(transmitter.getDescription());
            setPowerSuppliedDistribution(Distribution.create(transmitter.getPowerSuppliedDistribution()));
            setUsePowerControl(transmitter.getUsePowerControl());
            unwantedEmissions.getPoint3DList().clear();
            unwantedEmissions.getPoint3DList().addAll(transmitter.getUnwantedEmissions().getPoint3DList());
            unwantedEmissionsFloor.getPoint3DList().clear();
            unwantedEmissionsFloor.getPoint3DList().addAll(transmitter.getUnwantedEmissionsFloor().getPoint3DList());
            setUseUnwantedEmission(transmitter.getUseUnwantedEmissionFloor());
            setFixedCoverageRadius(transmitter.getFixedCoverageRadius());
            setUserDensity(transmitter.getUserDensity());
            setNumberOfChannels(transmitter.getNumberOfChannels());
            setNumberOfUsersPerChannel(transmitter.getNumberOfUsersPerChannel());
            setFrequencyReUse(transmitter.getFrequencyReUse());
            setPowerControlStep(transmitter.getPowerControlStep());
            setPowerControlMinimum(transmitter.getMinReceivedPower());
            setPowerControlRange(transmitter.getPowerControlRange());
            setAntenna(new Antenna(transmitter.getAntenna()));
            setAntennaHeight(Distribution.create(transmitter.getAntennaHeight()));
            setCoverageRadius(transmitter.getCoverageRadius());
            setCoverageLoss(transmitter.getCoverageLoss());
        }
    }

    public double getCoverageRadius()
    {
        return coverageRadius;
    }

    public void setCoverageRadius(double coverageRadius)
    {
        this.coverageRadius = coverageRadius;
    }

    public double getTxTrialPower()
    {
        return txTrialPower;
    }

    public void setTxTrialPower(double txTrialPower)
    {
        this.txTrialPower = txTrialPower;
    }

    public double getTxTrialAntHeight()
    {
        return txTrialAntHeight;
    }

    public void setTxTrialAntHeight(double txTrialAntHeight)
    {
        this.txTrialAntHeight = txTrialAntHeight;
    }

    public double getRMax()
    {
        return rMax;
    }

    public void setRMax(double rMax)
    {
        this.rMax = rMax;
    }

    public double getCoverageLoss()
    {
        return coverageLoss;
    }

    public void setCoverageLoss(double coverageLoss)
    {
        this.coverageLoss = coverageLoss;
    }

    public double getRefFrequency()
    {
        return refFrequency;
    }

    public void setRefFrequency(double refFrequency)
    {
        this.refFrequency = refFrequency;
    }

    public double getDens()
    {
        return dens;
    }

    public void setDens(double dens)
    {
        this.dens = dens;
    }

    public int getFreqCluster()
    {
        return freqCluster;
    }

    public void setFreqCluster(int freqCluster)
    {
        this.freqCluster = freqCluster;
    }

    public double getRefPower()
    {
        return refPower;
    }

    public void setRefPower(double refPower)
    {
        this.refPower = refPower;
    }

    public double getMaxDist()
    {
        return maxDist;
    }

    public void setMaxDist(double maxDist)
    {
        this.maxDist = maxDist;
    }

    public double getMinDist()
    {
        return minDist;
    }

    public void setMinDist(double minDist)
    {
        this.minDist = minDist;
    }

    public double getAvailability()
    {
        return availability;
    }

    public void setAvailability(double availability)
    {
        this.availability = availability;
    }

    public double getFadingStdDev()
    {
        return fadingStdDev;
    }

    public void setFadingStdDev(double fadingStdDev)
    {
        this.fadingStdDev = fadingStdDev;
    }

    public double getBandwidth()
    {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth)
    {
        this.bandwidth = bandwidth;
    }

    public Distribution getPowerSuppliedDistribution()
    {
        return powerSuppliedDistribution;
    }

    public boolean getUsePowerControl()
    {
        return usePowerControl;
    }

    public Function2 getUnwantedEmissions()
    {
        return unwantedEmissions;
    }

    public boolean getUseUnwantedEmissionFloor()
    {
        return useUnwantedEmissionFloor;
    }

    public double getFixedCoverageRadius()
    {
        return fixedCoverageRadius;
    }

    public double getUserDensity()
    {
        return userDensity;
    }

    public int getNumberOfChannels()
    {
        return numberOfChannels;
    }

    public int getNumberOfUsersPerChannel()
    {
        return numberOfUsersPerChannel;
    }

    public int getFrequencyReUse()
    {
        return frequencyReUse;
    }

    public PowerControl getPowerControl()
    {
        return powerControl;
    }

    public double getPowerControlStep()
    {
        return powerControl.getPowerControlStep();
    }

    public double getMinReceivedPower()
    {
        return powerControl.getPowerControlMinimum();
    }

    public double getPowerControlRange()
    {
        return powerControl.getPowerControlRange();
    }

    public Function2 getUnwantedEmissionsFloor()
    {
        return unwantedEmissionsFloor;
    }

    public void setPowerSuppliedDistribution(Distribution powerSuppliedDistribution)
    {
        this.powerSuppliedDistribution = powerSuppliedDistribution;
        nodeAttributeIsDirty = true;
    }

    public void setUsePowerControl(boolean usePowerControl)
    {
        this.usePowerControl = usePowerControl;
    }

    public void setUnwantedEmissions(Function2 unwantedEmissions)
    {
        this.unwantedEmissions = unwantedEmissions;
        nodeAttributeIsDirty = true;
    }

    public void setUseUnwantedEmission(boolean useUnwantedEmissionFloor)
    {
        this.useUnwantedEmissionFloor = useUnwantedEmissionFloor;
    }

    public void setFixedCoverageRadius(double fixedCoverageRadius)
    {
        this.fixedCoverageRadius = fixedCoverageRadius;
    }

    public void setUserDensity(double userDensity)
    {
        this.userDensity = userDensity;
    }

    public void setNumberOfChannels(int numberOfChannels)
    {
        this.numberOfChannels = numberOfChannels;
    }

    public void setNumberOfUsersPerChannel(int numberOfUsersPerChannel)
    {
        this.numberOfUsersPerChannel = numberOfUsersPerChannel;
    }

    public void setFrequencyReUse(int frequencyReUse)
    {
        this.frequencyReUse = frequencyReUse;
    }

    public void setPowerControl(PowerControl powerControl)
    {
        this.powerControl = powerControl;
    }

    public void setPowerControlStep(double powerControlStep)
    {
        powerControl.setPowerControlStep(powerControlStep);
    }

    public void setPowerControlMinimum(double minReceivedPower)
    {
        powerControl.setPowerControlMinimum(minReceivedPower);
    }

    public void setPowerControlRange(double powerControlRange)
    {
        powerControl.setPowerControlRange(powerControlRange);
    }

    public void setUnwantedEmissionsFloor(Function2 unwantedEmissionsFloor)
    {
        this.unwantedEmissionsFloor = unwantedEmissionsFloor;
        nodeAttributeIsDirty = true;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("transmitter");
        element.setAttribute("use_power_control", String.valueOf(getUsePowerControl()));
        element.setAttribute("use_unwanted_emission", String.valueOf(getUseUnwantedEmissionFloor()));
        element.setAttribute("fixed_coverage_radius", String.valueOf(getFixedCoverageRadius()));
        element.setAttribute("user_density", String.valueOf(getUserDensity()));
        element.setAttribute("number_of_channels", String.valueOf(getNumberOfChannels()));
        element.setAttribute("number_of_users_per_channel", String.valueOf(getNumberOfUsersPerChannel()));
        element.setAttribute("frequency_re_use", String.valueOf(getFrequencyReUse()));
        Element unwantedEmissionsElement = doc.createElement("unwantedemission");
        unwantedEmissionsElement.appendChild(unwantedEmissions.toElement(doc));
        element.appendChild(unwantedEmissionsElement);
        Element unwantedEmissionsFloorElement = doc.createElement("unwantedemissionfloor");
        unwantedEmissionsFloorElement.appendChild(unwantedEmissionsFloor.toElement(doc));
        element.appendChild(unwantedEmissionsFloorElement);
        Element powerSuppliedElement = doc.createElement("power-supplied-distribution");
        powerSuppliedElement.appendChild(powerSuppliedDistribution.toElement(doc));
        element.appendChild(powerSuppliedElement);
        element.appendChild(powerControl.toElement(doc));
        element.appendChild(super.toElement(doc));
        return element;
    }

    public boolean equals(Object o)
    {
        return o != null && (o instanceof Transmitter) && ((Transmitter)o).getReference().equals(getReference());
    }

    public String toString()
    {
        return getReference();
    }

    public boolean isLeaf()
    {
        return false;
    }

    public Enumeration children()
    {
        return null;
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public Node getChildAt(int childIndex)
    {
        return ((Node) (childIndex != 0 ? new PowerControlTreeElement() : new CoverageRadius()));
    }

    public int getChildCount()
    {
        return 2;
    }

    public int getIndex(Node node)
    {
        return 0;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Reference", "", getReference(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Description", "", getDescription(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Power supplied", "dBm", getPowerSuppliedDistribution(), "Distribution", null, false, false, null));
        nodeList.add(new NodeAttribute("Use power control", "", getUsePowerControl() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, false, true, null));
        nodeList.add(new NodeAttribute("Unwanted emission", "X(MHz) / Y(dBc) / Z(kHz)", getUnwantedEmissions(), "Function", null, false, false, null));
        nodeList.add(new NodeAttribute("Use unwanted emission floor", "", getUseUnwantedEmissionFloor() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, false, true, null));
        nodeList.add(new NodeAttribute("Antenna Reference", "", getAntenna() != null ? ((Object) (getAntenna().getReference())) : "No antenna defined", "Antenna", null, false, true, null));
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
            setPowerSuppliedDistribution((Distribution)aValue);
            break;

        case 3: // '\003'
            setUsePowerControl(((Boolean)aValue).booleanValue());
            break;

        case 4: // '\004'
            setUnwantedEmissions((DiscreteFunction2)aValue);
            break;

        case 5: // '\005'
            setUseUnwantedEmission(((Boolean)aValue).booleanValue());
            break;
        }
    }

    public static final int resolveCalculationMode(String calculationModeStr)
    {
        int calculationMode;
        if(calculationModeStr.equals(CALCULATION_MODES[0]))
            calculationMode = 0;
        else
        if(calculationModeStr.equals(CALCULATION_MODES[2]))
            calculationMode = 2;
        else
        if(calculationModeStr.equals(CALCULATION_MODES[1]))
            calculationMode = 1;
        else
            throw new IllegalArgumentException("Cannot resolve calculation mode string");
        return calculationMode;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/Transmitter);
    private Distribution powerSuppliedDistribution;
    private Function2 unwantedEmissions;
    private Function2 unwantedEmissionsFloor;
    private PowerControl powerControl;
    private boolean usePowerControl;
    private boolean useUnwantedEmissionFloor;
    private double fixedCoverageRadius;
    private double userDensity;
    private int numberOfChannels;
    private int numberOfUsersPerChannel;
    private int frequencyReUse;
    private double coverageRadius;
    private double txTrialPower;
    private double txTrialAntHeight;
    private double rMax;
    private double coverageLoss;
    private double refFrequency;
    private double dens;
    private int freqCluster;
    public static final String CALCULATION_MODES[] = {
        "User-defined radius", "Noise-limited network", "Traffic-limited network"
    };
    public static final int USER_DEFINED = 0;
    public static final int NOISE_LIMITED_NETWORK = 1;
    public static final int TRAFFIC_LIMIED_NETWORK = 2;
    private int coverageCalculMode;
    private double refPower;
    private double maxDist;
    private double minDist;
    private double availability;
    private double fadingStdDev;
    private double bandwidth;
    protected static final String DEFAULT_TX_REF = "DEFAULT_TX";

}