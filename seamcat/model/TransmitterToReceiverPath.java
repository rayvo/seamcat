// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   TransmitterToReceiverPath.java

package org.seamcat.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.distribution.UniformDistribution;
import org.seamcat.distribution.UniformPolarDistanceDistribution;
import org.seamcat.presentation.Node;
import org.seamcat.presentation.components.LocalComponent;
import org.seamcat.propagation.HataSE21Model;
import org.seamcat.propagation.PropagationModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model:
//            SeamcatComponent, NodeAttribute, Model

public class TransmitterToReceiverPath extends SeamcatComponent
{
    private class RelativeLocationDisplay extends SeamcatComponent
    {

        public Element toElement(Document doc)
        {
            return null;
        }

        public void initNodeAttributes()
        {
            List nodeList = new ArrayList();
            nodeList.add(new NodeAttribute("Correlated", "", getUseCorrelationDistance() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
                Boolean.TRUE, Boolean.FALSE
            }, true, true, null));
            if(getUseCorrelationDistance())
            {
                nodeList.add(new NodeAttribute("Fixed distance (X)", "", new Double(getDeltaX()), "Double", null, false, true, null));
                nodeList.add(new NodeAttribute("Fixed distance (Y)", "", new Double(getDeltaY()), "Double", null, false, true, null));
            } else
            {
                nodeList.add(new NodeAttribute("Path distance factor", "", getPathDistanceFactor(), "Distribution", null, false, false, null));
                nodeList.add(new NodeAttribute("Path azimuth", "", getPathAzimuth(), "Distribution", null, false, false, null));
            }
            nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
        }

        protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
            if(rowIndex == 0)
                setUseCorrelatedDistance(((Boolean)aValue).booleanValue());
            else
            if(getUseCorrelationDistance())
                switch(rowIndex)
                {
                case 1: // '\001'
                    setDeltaX(((Number)aValue).doubleValue());
                    break;

                case 2: // '\002'
                    setDeltaY(((Number)aValue).doubleValue());
                    break;
                }
            else
                switch(rowIndex)
                {
                case 1: // '\001'
                    setPathDistanceFactor((Distribution)aValue);
                    break;

                case 2: // '\002'
                    setPathAzimuth((Distribution)aValue);
                    break;
                }
        }

        public String toString()
        {
            return "Relative location";
        }

        final TransmitterToReceiverPath this$0;

        private RelativeLocationDisplay()
        {
            this$0 = TransmitterToReceiverPath.this;
            super();
        }

    }


    public TransmitterToReceiverPath(Element element)
    {
        displayCoverage = new RelativeLocationDisplay();
        pathAzimuth = new UniformDistribution(0.0D, 360D);
        pathDistanceFactor = new UniformPolarDistanceDistribution(10D);
        propagationModel = new HataSE21Model();
        try
        {
            pathAzimuth = Distribution.create((Element)element.getElementsByTagName("pathAzimuth").item(0).getFirstChild());
        }
        catch(Exception e)
        {
            LOG.warn("Could not load path azimuth distribution");
        }
        try
        {
            pathDistanceFactor = Distribution.create((Element)element.getElementsByTagName("pathDistanceFactor").item(0).getFirstChild());
        }
        catch(Exception e)
        {
            LOG.warn("Could not load path distance factor distribution");
        }
        try
        {
            propagationModel = PropagationModel.create((Element)element.getElementsByTagName("PropagationModel").item(0).getFirstChild());
        }
        catch(Exception e)
        {
            LOG.warn("Could not load propagation model");
            e.printStackTrace();
        }
        try
        {
            useCorrelatedDistance = Boolean.valueOf(element.getAttribute("useCorrelatedDistance")).booleanValue();
            deltaX = Double.parseDouble(element.getAttribute("deltaX"));
            deltaY = Double.parseDouble(element.getAttribute("deltaY"));
            coverageRadius = Double.parseDouble(element.getAttribute("coverageRadius"));
            referenceReceiverAntennaHeight = Double.parseDouble(element.getAttribute("referenceReceiverAntennaHeight"));
            referenceTransmitterAntennaHeight = Double.parseDouble(element.getAttribute("referenceTransmitterAntennaHeight"));
            referenceTransmitterFrequency = Double.parseDouble(element.getAttribute("referenceTransmitterFrequency"));
            referenceTransmitterPower = Double.parseDouble(element.getAttribute("referenceTransmitterPower"));
            minimumDistance = Double.parseDouble(element.getAttribute("minimumDistance"));
            maximumDistance = Double.parseDouble(element.getAttribute("maximumDistance"));
            availability = Double.parseDouble(element.getAttribute("availability"));
            fadingStdDev = Double.parseDouble(element.getAttribute("fadingStdDev"));
            density = Double.parseDouble(element.getAttribute("density"));
            coverageRadiusCalculationMode = Integer.parseInt(element.getAttribute("coverageRadiusCalculatinMode"));
            numberOfChannels = Integer.parseInt(element.getAttribute("numberOfChannels"));
            numberOfUsersPerChannel = Integer.parseInt(element.getAttribute("numberOfUsersPerChannel"));
            frequencyCluster = Integer.parseInt(element.getAttribute("frequencyCluster"));
        }
        catch(Exception e)
        {
            LOG.warn("One or more parameters could not be loaded");
        }
    }

    public TransmitterToReceiverPath()
    {
        displayCoverage = new RelativeLocationDisplay();
        pathAzimuth = new UniformDistribution(0.0D, 360D);
        pathDistanceFactor = new UniformPolarDistanceDistribution(10D);
        propagationModel = new HataSE21Model();
    }

    public TransmitterToReceiverPath duplicate()
    {
        TransmitterToReceiverPath it = new TransmitterToReceiverPath();
        it.setDeltaX(getDeltaX());
        it.setDeltaY(getDeltaY());
        it.setPathAzimuth(Distribution.create(getPathAzimuth()));
        it.setPathDistanceFactor(Distribution.create(getPathDistanceFactor()));
        try
        {
            it.setPropagationModel(PropagationModel.create(getPropagationModel().toElement(Model.getSeamcatDocumentBuilderFactory().newDocumentBuilder().newDocument())));
        }
        catch(Exception ex) { }
        return it;
    }

    public boolean getUseCorrelationDistance()
    {
        return useCorrelatedDistance;
    }

    public double getDeltaX()
    {
        return deltaX;
    }

    public double getDeltaY()
    {
        return deltaY;
    }

    public Distribution getPathAzimuth()
    {
        return pathAzimuth;
    }

    public Distribution getPathDistanceFactor()
    {
        return pathDistanceFactor;
    }

    public int getCoverageRadiusCalculationMode()
    {
        return coverageRadiusCalculationMode;
    }

    public double getCoverageRadius()
    {
        return coverageRadius;
    }

    public double getReferenceReceiverAntennaHeight()
    {
        return referenceReceiverAntennaHeight;
    }

    public double getReferenceTransmitterAntennaHeight()
    {
        return referenceTransmitterAntennaHeight;
    }

    public double getReferenceTransmitterFrequency()
    {
        return referenceTransmitterFrequency;
    }

    public double getReferenceTransmitterPower()
    {
        return referenceTransmitterPower;
    }

    public double getMinimumDistance()
    {
        return minimumDistance;
    }

    public double getMaximumDistance()
    {
        return maximumDistance;
    }

    public double getAvailability()
    {
        return availability;
    }

    public double getFadingStdDev()
    {
        return fadingStdDev;
    }

    public double getDensity()
    {
        return density;
    }

    public int getNumberOfChannels()
    {
        return numberOfChannels;
    }

    public int getNumberOfUsersPerChannel()
    {
        return numberOfUsersPerChannel;
    }

    public int getFrequencyCluster()
    {
        return frequencyCluster;
    }

    public PropagationModel getPropagationModel()
    {
        return propagationModel;
    }

    public void setUseCorrelatedDistance(boolean useCorrelatedDistance)
    {
        this.useCorrelatedDistance = useCorrelatedDistance;
        displayCoverage.initNodeAttributes();
    }

    public void setDeltaX(double deltaX)
    {
        this.deltaX = deltaX;
    }

    public void setDeltaY(double deltaY)
    {
        this.deltaY = deltaY;
    }

    public void setCoverageRadiusCalculationMode(int coverageRadiusCalculatinMode)
    {
        coverageRadiusCalculationMode = coverageRadiusCalculatinMode;
    }

    public void setCoverageRadius(double coverageRadius)
    {
        this.coverageRadius = coverageRadius;
    }

    public void setReferenceReceiverAntennaHeight(double referenceReceiverAntennaHeight)
    {
        this.referenceReceiverAntennaHeight = referenceReceiverAntennaHeight;
    }

    public void setReferenceTransmitterAntennaHeight(double referenceTransmitterAntennaHeight)
    {
        this.referenceTransmitterAntennaHeight = referenceTransmitterAntennaHeight;
    }

    public void setReferenceTransmitterFrequency(double referenceTransmitterFrequency)
    {
        this.referenceTransmitterFrequency = referenceTransmitterFrequency;
    }

    public void setReferenceTransmitterPower(double referenceTransmitterPower)
    {
        this.referenceTransmitterPower = referenceTransmitterPower;
    }

    public void setMinimumDistance(double minimumDistance)
    {
        this.minimumDistance = minimumDistance;
    }

    public void setMaximumDistance(double maximumDistance)
    {
        this.maximumDistance = maximumDistance;
    }

    public void setAvailability(double availability)
    {
        this.availability = availability;
    }

    public void setFadingStdDev(double fadingStdDev)
    {
        this.fadingStdDev = fadingStdDev;
    }

    public void setDensity(double density)
    {
        this.density = density;
    }

    public void setNumberOfChannels(int numberOfChannels)
    {
        this.numberOfChannels = numberOfChannels;
    }

    public void setNumberOfUsersPerChannel(int numberOfUsersPerChannel)
    {
        this.numberOfUsersPerChannel = numberOfUsersPerChannel;
    }

    public void setFrequencyCluster(int frequencyCluster)
    {
        this.frequencyCluster = frequencyCluster;
    }

    public void setPathAzimuth(Distribution pathAzimuth)
    {
        this.pathAzimuth = pathAzimuth;
        nodeAttributeIsDirty = true;
    }

    public void setPathDistanceFactor(Distribution pathDistanceFactor)
    {
        this.pathDistanceFactor = pathDistanceFactor;
        nodeAttributeIsDirty = true;
    }

    public void setPropagationModel(PropagationModel propagationModel)
    {
        this.propagationModel = propagationModel;
        nodeAttributeIsDirty = true;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("TransmitterToReceiverPath");
        element.setAttribute("useCorrelatedDistance", Boolean.toString(useCorrelatedDistance));
        element.setAttribute("deltaX", Double.toString(deltaX));
        element.setAttribute("deltaY", Double.toString(deltaY));
        element.setAttribute("coverageRadius", Double.toString(coverageRadius));
        element.setAttribute("referenceReceiverAntennaHeight", Double.toString(referenceReceiverAntennaHeight));
        element.setAttribute("referenceTransmitterAntennaHeight", Double.toString(referenceTransmitterAntennaHeight));
        element.setAttribute("referenceTransmitterFrequency", Double.toString(referenceTransmitterFrequency));
        element.setAttribute("referenceTransmitterPower", Double.toString(referenceTransmitterPower));
        element.setAttribute("minimumDistance", Double.toString(minimumDistance));
        element.setAttribute("maximumDistance", Double.toString(maximumDistance));
        element.setAttribute("availability", Double.toString(availability));
        element.setAttribute("fadingStdDev", Double.toString(fadingStdDev));
        element.setAttribute("density", Double.toString(density));
        element.setAttribute("coverageRadiusCalculatinMode", Integer.toString(coverageRadiusCalculationMode));
        element.setAttribute("numberOfChannels", Integer.toString(numberOfChannels));
        element.setAttribute("numberOfUsersPerChannel", Integer.toString(numberOfUsersPerChannel));
        element.setAttribute("frequencyCluster", Integer.toString(frequencyCluster));
        Element pathAzimuthElement = doc.createElement("pathAzimuth");
        pathAzimuthElement.appendChild(pathAzimuth.toElement(doc));
        element.appendChild(pathAzimuthElement);
        Element pathDistanceFactorElement = doc.createElement("pathDistanceFactor");
        pathDistanceFactorElement.appendChild(pathDistanceFactor.toElement(doc));
        element.appendChild(pathDistanceFactorElement);
        Element propagationModelElement = doc.createElement("PropagationModel");
        propagationModelElement.appendChild(propagationModel.toElement(doc));
        element.appendChild(propagationModelElement);
        return element;
    }

    public void updateNodeAttributes()
    {
        displayCoverage.updateNodeAttributes();
    }

    protected void initNodeAttributes()
    {
        nodeAttributes = new NodeAttribute[0];
    }

    public int getChildCount()
    {
        return 2;
    }

    public Node getChildAt(int childIndex)
    {
        Node value;
        switch(childIndex)
        {
        case 0: // '\0'
            value = displayCoverage;
            break;

        case 1: // '\001'
            value = new LocalComponent(propagationModel, "Propagation model");
            break;

        default:
            throw new IllegalArgumentException((new StringBuilder()).append("ChildIndex is out of range <").append(childIndex).append(">").toString());
        }
        return value;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/TransmitterToReceiverPath);
    public static final int COVERAGE_RADIUS_CALCULATION_MODE_USER_DEFINED = 0;
    public static final int COVERAGE_RADIUS_CALCULATION_MODE_NOISE_LIMITED_NETWORK = 1;
    public static final int COVERAGE_RADIUS_CALCULATION_MODE_TRAFFIC_LIMITED_NETWORK = 2;
    public static final UniformPolarDistanceDistribution DEFAULT_DISTANCE_FACTOR = new UniformPolarDistanceDistribution(1.0D);
    private RelativeLocationDisplay displayCoverage;
    private boolean useCorrelatedDistance;
    private double deltaX;
    private double deltaY;
    private Distribution pathAzimuth;
    private Distribution pathDistanceFactor;
    private int coverageRadiusCalculationMode;
    private double coverageRadius;
    private double referenceReceiverAntennaHeight;
    private double referenceTransmitterAntennaHeight;
    private double referenceTransmitterFrequency;
    private double referenceTransmitterPower;
    private double minimumDistance;
    private double maximumDistance;
    private double availability;
    private double fadingStdDev;
    private double density;
    private int numberOfChannels;
    private int numberOfUsersPerChannel;
    private int frequencyCluster;
    private PropagationModel propagationModel;

}