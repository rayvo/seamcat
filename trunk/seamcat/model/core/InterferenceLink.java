// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   InterferenceLink.java

package org.seamcat.model.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.cdma.CDMADownlinkSystem;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.distribution.Distribution;
import org.seamcat.distribution.UniformPolarDistanceDistribution;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.*;
import org.seamcat.model.datatypes.IRSSVector;
import org.seamcat.model.technical.exception.GeometricException;
import org.seamcat.model.technical.exception.RandomException;
import org.seamcat.presentation.Node;
import org.seamcat.presentation.components.LocalComponent;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.core:
//            Link, UnwantedInterference, BlockingInterference, InterferingSystemLink, 
//            InterferingTransmitter, VictimSystemLink

public class InterferenceLink extends Link
{
    private class WantedReceiverComponent extends SeamcatComponent
    {

        public int getChildCount()
        {
            return 1;
        }

        public Node getChildAt(int childIndex)
        {
            Node child;
            switch(childIndex)
            {
            case 0: // '\0'
                child = new LocalComponent(getInterferingLink().getWantedReceiver().getAntenna(), "Antenna");
                break;

            default:
                throw new IllegalArgumentException("ChildIndex out of range");
            }
            return child;
        }

        protected void initNodeAttributes()
        {
            List nodeList = new ArrayList();
            nodeList.add(new NodeAttribute("Reference", "", getInterferingLink().getWantedReceiver().getReference(), "String", null, false, true, null));
            nodeList.add(new NodeAttribute("Description", "", getInterferingLink().getWantedReceiver().getDescription(), "String", null, false, true, null));
            nodeList.add(new NodeAttribute("Antenna height", "kHz", getInterferingLink().getWantedReceiver().getAntennaHeight(), "Distribution", null, false, false, null));
            nodeList.add(new NodeAttribute("Antenna azimuth", "kHz", getInterferingLink().getReceiverToTransmitterAzimuth(), "Distribution", null, false, false, null));
            nodeList.add(new NodeAttribute("Antenna elevation", "kHz", getInterferingLink().getReceiverToTransmitterElevation(), "Distribution", null, false, false, null));
            nodeList.add(new NodeAttribute("Sensitivity", "dBm", new Double(getInterferingLink().getWantedReceiver().getSensitivity()), "Double", null, false, true, null));
            nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
        }

        protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
            switch(rowIndex)
            {
            case 0: // '\0'
                getInterferingLink().getWantedReceiver().setReference((String)aValue);
                break;

            case 1: // '\001'
                getInterferingLink().getWantedReceiver().setDescription((String)aValue);
                break;

            case 2: // '\002'
                getInterferingLink().getWantedReceiver().setAntennaHeight((Distribution)aValue);
                break;

            case 3: // '\003'
                getInterferingLink().setReceiverToTransmitterAzimuth((Distribution)aValue);
                break;

            case 4: // '\004'
                getInterferingLink().setReceiverToTransmitterElevation((Distribution)aValue);
                break;

            case 5: // '\005'
                getInterferingLink().getWantedReceiver().setSensitivity(((Number)aValue).doubleValue());
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

        private final Logger LOG;
        final InterferenceLink this$0;

        private WantedReceiverComponent()
        {
            this$0 = InterferenceLink.this;
            super();
            LOG = Logger.getLogger(org/seamcat/model/core/InterferenceLink$WantedReceiverComponent);
        }

    }

    private class InterferingTransmitterComponent extends SeamcatComponent
    {
        private class CoverageRadiusComponent extends SeamcatComponent
        {

            public String toString()
            {
                return "Coverage Radius";
            }

            protected void initNodeAttributes()
            {
                List nodeList = new ArrayList();
                nodeList.add(new NodeAttribute("Coverage radius", "Km", new Double(getInterferingLink().getWt2VrPath().getCoverageRadius()), "Double", null, false, true, null));
                nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
            }

            protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
            {
                aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
                switch(rowIndex)
                {
                case 0: // '\0'
                    getInterferingLink().getWt2VrPath().setCoverageRadius(((Number)aValue).doubleValue());
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

            private final Logger LOG;
            final InterferingTransmitterComponent this$1;

            private CoverageRadiusComponent()
            {
                this$1 = InterferingTransmitterComponent.this;
                super();
                LOG = Logger.getLogger(org/seamcat/model/core/InterferenceLink$InterferingTransmitterComponent$CoverageRadiusComponent);
            }

        }


        protected void initNodeAttributes()
        {
            List nodeList = new ArrayList();
            nodeList.add(new NodeAttribute("Reference", "", getReference(), "String", null, false, true, null));
            nodeList.add(new NodeAttribute("Description", "", getDescription(), "String", null, false, true, null));
            nodeList.add(new NodeAttribute("Power supplied", "dBm", getInterferingLink().getInterferingTransmitter().getPowerSuppliedDistribution(), "Distribution", null, false, false, null));
            nodeList.add(new NodeAttribute("Use power control", "", getInterferingLink().getInterferingTransmitter().getUsePowerControl() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
                Boolean.TRUE, Boolean.FALSE
            }, false, true, null));
            nodeList.add(new NodeAttribute("Unwanted emission", "X(MHz) / Y(dBc) / Z(kHz)", getInterferingLink().getInterferingTransmitter().getUnwantedEmissions(), "Function", null, true, false, null));
            nodeList.add(new NodeAttribute("Use unwanted emission floor", "", getInterferingLink().getInterferingTransmitter().getUseUnwantedEmissionFloor() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
                Boolean.TRUE, Boolean.FALSE
            }, false, true, null));
            nodeList.add(new NodeAttribute("Antenna height", "", getInterferingLink().getInterferingTransmitter().getAntennaHeight(), "Distribution", null, true, false, null));
            nodeList.add(new NodeAttribute("Antenna azimuth", "", getInterferingLink().getTransmitterToReceiverAzimuth(), "Distribution", null, true, false, null));
            nodeList.add(new NodeAttribute("Antenna tilt", "", getInterferingLink().getTransmitterToReceiverElevation(), "Distribution", null, true, false, null));
            nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
        }

        protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
            switch(rowIndex)
            {
            case 0: // '\0'
                getInterferingLink().getInterferingTransmitter().setReference((String)aValue);
                break;

            case 1: // '\001'
                getInterferingLink().getInterferingTransmitter().setDescription("Description");
                break;

            case 2: // '\002'
                getInterferingLink().getInterferingTransmitter().setPowerSuppliedDistribution((Distribution)aValue);
                break;

            case 3: // '\003'
                getInterferingLink().getInterferingTransmitter().setUsePowerControl(((Boolean)aValue).booleanValue());
                break;

            case 4: // '\004'
                getInterferingLink().getInterferingTransmitter().setUnwantedEmissions((DiscreteFunction2)aValue);
                break;

            case 5: // '\005'
                getInterferingLink().getInterferingTransmitter().setUseUnwantedEmission(((Boolean)aValue).booleanValue());
                break;

            case 6: // '\006'
                getInterferingLink().getInterferingTransmitter().setAntennaHeight((Distribution)aValue);
                break;

            case 7: // '\007'
                getInterferingLink().setTransmitterToReceiverAzimuth((Distribution)aValue);
                break;

            case 8: // '\b'
                getInterferingLink().setTransmitterToReceiverElevation((Distribution)aValue);
                break;
            }
        }

        public Node getChildAt(int childIndex)
        {
            Node child;
            switch(childIndex)
            {
            case 0: // '\0'
                if(covComp == null)
                    covComp = new CoverageRadiusComponent();
                child = covComp;
                break;

            case 1: // '\001'
                child = new LocalComponent(getInterferingLink().getInterferingTransmitter().getAntenna(), "Antenna");
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("ChildIndex out of range <").append(childIndex).append(">").toString());
            }
            return child;
        }

        public int getChildCount()
        {
            return 2;
        }

        public void updateNodeAttributes()
        {
            try
            {
                covComp.updateNodeAttributes();
                super.updateNodeAttributes();
            }
            catch(Exception e) { }
        }

        public Element toElement(Document doc)
        {
            return null;
        }

        private final Logger LOG;
        private CoverageRadiusComponent covComp;
        final InterferenceLink this$0;

        private InterferingTransmitterComponent()
        {
            this$0 = InterferenceLink.this;
            super();
            LOG = Logger.getLogger(org/seamcat/model/core/InterferenceLink$InterferingTransmitterComponent);
        }

    }

    private class RelativeLocationComponent extends SeamcatComponent
    {

        protected void initNodeAttributes()
        {
            List nodeList = new ArrayList();
            nodeList.add(new NodeAttribute("Correlation mode", "", InterferenceLink.RELATIVE_LOCATION_MODES[getCorrelationMode()], "String", InterferenceLink.RELATIVE_LOCATION_MODES, true, true, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][0])
                nodeList.add(new NodeAttribute("Delta X", "", new Double(getWt2VrPath().getDeltaX()), "Double", null, false, true, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][1])
                nodeList.add(new NodeAttribute("Delta Y", "", new Double(getWt2VrPath().getDeltaY()), "Double", null, false, true, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][2])
                nodeList.add(new NodeAttribute("Path azimuth", "", getWt2VrPath().getPathAzimuth(), "Distribution", null, false, false, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][3])
                nodeList.add(new NodeAttribute("Path distance", "", getWt2VrPath().getPathDistanceFactor(), "Distribution", null, false, false, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][4])
                nodeList.add(new NodeAttribute("Simulation Radius", "", new Double(getInterferingLink().getInterferingTransmitter().getRsimu()), "Double", null, false, true, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][5])
                nodeList.add(new NodeAttribute("Number of active transmitters", "", new Integer(getInterferingLink().getInterferingTransmitter().getNbActiveTx()), "Integer", null, false, true, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][6])
                nodeList.add(new NodeAttribute("Transmission probability", "", new Double(getInterferingLink().getInterferingTransmitter().getTransProb()), "Double", null, false, true, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][6])
                nodeList.add(new NodeAttribute("Density of transmitters", "", new Double(getInterferingLink().getInterferingTransmitter().getDensActiveTx()), "Double", null, false, true, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][6])
                nodeList.add(new NodeAttribute("Activity", "", getInterferingLink().getInterferingTransmitter().getActivity(), "Function", null, false, false, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][6])
                nodeList.add(new NodeAttribute("Time", "", new Double(getInterferingLink().getInterferingTransmitter().getTime()), "Double", null, false, true, null));
            if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][6])
                nodeList.add(new NodeAttribute("Protection distance", "", new Double(getProtectionDistance()), "Distribution", null, false, true, null));
            nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
        }

        protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
            switch(rowIndex)
            {
            case 5: // '\005'
            default:
                break;

            case 0: // '\0'
                setCorrelationMode(InterferenceLink.resolveRelativeLocation((String)aValue));
                break;

            case 1: // '\001'
                if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][0])
                    getWt2VrPath().setDeltaX(((Number)aValue).doubleValue());
                break;

            case 2: // '\002'
                if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][1])
                {
                    getWt2VrPath().setDeltaY(((Number)aValue).doubleValue());
                    break;
                }
                if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][2])
                    getWt2VrPath().setPathDistanceFactor((Distribution)aValue);
                else
                    getInterferingLink().getInterferingTransmitter().setNbActiveTx(((Number)aValue).intValue());
                break;

            case 3: // '\003'
                if(InterferenceLink.MODE_ENABLE_STATUS[getCorrelationMode()][4])
                    getInterferingLink().getInterferingTransmitter().setRsimu(((Number)aValue).doubleValue());
                else
                    getInterferingLink().getInterferingTransmitter().setDensActiveTx(((Number)aValue).intValue());
                break;

            case 4: // '\004'
                getInterferingLink().getInterferingTransmitter().setTransProb(((Number)aValue).intValue());
                break;

            case 6: // '\006'
                getInterferingLink().getInterferingTransmitter().setTime(((Number)aValue).intValue());
                break;

            case 7: // '\007'
                setProtectionDistance(((Number)aValue).intValue());
                break;
            }
        }

        public Element toElement(Document doc)
        {
            return null;
        }

        public String toString()
        {
            return "Relative location";
        }

        private final Logger LOG;
        final InterferenceLink this$0;

        private RelativeLocationComponent()
        {
            this$0 = InterferenceLink.this;
            super();
            LOG = Logger.getLogger(org/seamcat/model/core/InterferenceLink$RelativeLocationComponent);
        }

    }

    private class ITx2WRxPathComponent extends SeamcatComponent
    {

        public void updateNodeAttributes()
        {
            initNodeAttributes();
            if(location != null)
                location.updateNodeAttributes();
        }

        public int getChildCount()
        {
            return 2;
        }

        public Node getChildAt(int childIndex)
        {
            Node child;
            switch(childIndex)
            {
            case 0: // '\0'
                if(location == null)
                    location = new RelativeLocationComponent();
                child = location;
                break;

            case 1: // '\001'
                child = new LocalComponent(getWt2VrPath().getPropagationModel(), "Propagation model");
                break;

            default:
                throw new IllegalArgumentException("ChildIndex out of range");
            }
            return child;
        }

        protected void initNodeAttributes()
        {
            nodeAttributes = new NodeAttribute[0];
        }

        public Element toElement(Document doc)
        {
            return null;
        }

        private final Logger LOG;
        private RelativeLocationComponent location;
        final InterferenceLink this$0;

        private ITx2WRxPathComponent()
        {
            this$0 = InterferenceLink.this;
            super();
            LOG = Logger.getLogger(org/seamcat/model/core/InterferenceLink$ITx2WRxPathComponent);
        }

    }


    public InterferenceLink(Element element, Workspace workspace)
    {
        super((Element)element.getElementsByTagName("link").item(0));
        correlationMode = 2;
        protectionDistance = 1.0D;
        unwantedInterference = new UnwantedInterference();
        wt2vrPath = new TransmitterToReceiverPath();
        distanceFactor = new UniformPolarDistanceDistribution(1.0D);
        colocated = false;
        isCDMASystem = false;
        cdmasystem = null;
        setCorrelationMode(Integer.parseInt(element.getAttribute("correlationMode")));
        setProtectionDistance(Double.parseDouble(element.getAttribute("protectionDistance")));
        try
        {
            setIsCDMASystem(element.getAttribute("isCDMA").equalsIgnoreCase("true"));
        }
        catch(Exception ex) { }
        try
        {
            setColocated(element.getAttribute("colocated").equalsIgnoreCase("true"));
        }
        catch(Exception e) { }
        try
        {
            String colocationRef = element.getAttribute("colocation_link");
            if(colocationRef != null)
                setColocationLink((InterferenceLink)workspace.getInterferenceLinks().get(colocationRef));
        }
        catch(Exception e) { }
        setWt2vrPath(new TransmitterToReceiverPath((Element)element.getElementsByTagName("TransmitterToReceiverPath").item(0)));
        setBlockingInterference(new BlockingInterference((Element)element.getElementsByTagName("BlockingInterference").item(0)));
        setUnwantedInterference(new UnwantedInterference((Element)element.getElementsByTagName("UnwantedInterference").item(0)));
        setVictimLink(workspace.getVictimSystemLink());
        setInterferingLink(new InterferingSystemLink((Element)element.getElementsByTagName("InterferingSystemLink").item(0)));
        if(isCDMASystem)
            cdmasystem = CDMASystem.createCDMASystem((Element)element.getElementsByTagName("CdmaSystem").item(0));
    }

    public InterferenceLink(String _name, VictimSystemLink _link)
    {
        super(_name, "");
        correlationMode = 2;
        protectionDistance = 1.0D;
        unwantedInterference = new UnwantedInterference();
        wt2vrPath = new TransmitterToReceiverPath();
        distanceFactor = new UniformPolarDistanceDistribution(1.0D);
        colocated = false;
        isCDMASystem = false;
        cdmasystem = null;
        victimLink = _link;
        interferingLink = new InterferingSystemLink((new StringBuilder()).append(toString()).append(" - InterferingSystemLink").toString(), "Auto Generated by InterferenceLink constructor", new InterferingTransmitter((Transmitter)Model.getInstance().getLibrary().getTransmitters().getElementAt(0)), new WantedReceiver((Receiver)Model.getInstance().getLibrary().getReceivers().getElementAt(0)));
        blockingInterference1 = new BlockingInterference();
        blockingInterference1.setInterferenceLink(this);
    }

    public InterferenceLink(String name, InterferenceLink link)
    {
        super(name, link.getDescription());
        correlationMode = 2;
        protectionDistance = 1.0D;
        unwantedInterference = new UnwantedInterference();
        wt2vrPath = new TransmitterToReceiverPath();
        distanceFactor = new UniformPolarDistanceDistribution(1.0D);
        colocated = false;
        isCDMASystem = false;
        cdmasystem = null;
        victimLink = link.getVictimLink();
        blockingInterference1 = new BlockingInterference();
        blockingInterference1.setInterferenceLink(this);
        unwantedInterference.setInterferenceLink(this);
        setBlockingInterference(link.getBlockingInterference());
        setCorrelationMode(link.getCorrelationMode());
        setDescription(link.getDescription());
        setInterferingLink(new InterferingSystemLink((new StringBuilder()).append(name).append(" - InteferingSystemLink").toString(), link.getInterferingLink()));
        setProtectionDistance(link.getProtectionDistance());
        setRxTxAntennaGain(link.getRxTxAntennaGain());
        setRxTxAzimuth(link.getRxTxAzimuth());
        setRxTxElevation(link.getRxTxElevation());
        setRxTxTilt(link.getRxTxTilt());
        setTxRxAngle(link.getTxRxAngle());
        setTxRxAntennaGain(link.getTxRxAntennaGain());
        setTxRxAzimuth(link.getTxRxAzimuth());
        setTxRxDistance(link.getTxRxDistance());
        setTxRxElevation(link.getTxRxElevation());
        setTxRxPathLoss(link.getTxRxPathLoss());
        setTxRxTilt(link.getTxRxTilt());
        setWt2vrPath(link.getWt2VrPath().duplicate());
        if(link.isCDMASystem())
        {
            setIsCDMASystem(true);
            cdmasystem = CDMASystem.createCDMASystem(link.getCDMASystem());
        }
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
    }

    public CDMASystem getCDMASystem()
    {
        return cdmasystem;
    }

    public void setCDMASystem(CDMASystem cdma)
    {
        cdmasystem = cdma;
    }

    public void setReference(String ref)
    {
        super.setReference(ref);
        try
        {
            blockingVector.setReference((new StringBuilder()).append(ref).append(" Blocking").toString());
            unwantedVector.setReference((new StringBuilder()).append(ref).append(" Unwanted Emission").toString());
        }
        catch(RuntimeException ex) { }
        nodeAttributeIsDirty = true;
    }

    public int getCorrelationMode()
    {
        return correlationMode;
    }

    public void setCorrelationMode(int correlationMode)
    {
        this.correlationMode = correlationMode;
        if(it2WrPathComponent != null)
            it2WrPathComponent.updateNodeAttributes();
    }

    public double getProtectionDistance()
    {
        return protectionDistance;
    }

    public TransmitterToReceiverPath getWt2VrPath()
    {
        return wt2vrPath;
    }

    public void setProtectionDistance(double protectionDistance)
    {
        this.protectionDistance = protectionDistance;
    }

    public BlockingInterference getBlockingInterference()
    {
        return blockingInterference1;
    }

    public void setBlockingInterference(BlockingInterference blockingInterference1)
    {
        this.blockingInterference1 = blockingInterference1;
        this.blockingInterference1.setInterferenceLink(this);
        nodeAttributeIsDirty = true;
    }

    public VictimSystemLink getVictimLink()
    {
        return victimLink;
    }

    public void setVictimLink(VictimSystemLink victimLink)
    {
        this.victimLink = victimLink;
    }

    public InterferingSystemLink getInterferingLink()
    {
        return interferingLink;
    }

    public void setInterferingLink(InterferingSystemLink interferingLink)
    {
        this.interferingLink = interferingLink;
    }

    public void itVrLocCorrelated(boolean debugMode)
        throws GeometricException
    {
        double rXit = 0.0D;
        double rYit = 0.0D;
        double rXvr = 0.0D;
        double rYvr = 0.0D;
        double rXwr = 0.0D;
        double rYwr = 0.0D;
        double rDistanceItVr = 0.0D;
        double rAngleItVr = 0.0D;
        double rDeltaXItVr = 0.0D;
        double rDeltaYItVr = 0.0D;
        double rDeltaXItWr = getInterferingLink().getWt2VrPath().getDeltaX();
        double rDeltaYItWr = getInterferingLink().getWt2VrPath().getDeltaY();
        double rDeltaXWtVr = getVictimLink().getVictimReceiver().getX();
        double rDeltaYWtVr = getVictimLink().getVictimReceiver().getY();
        double rDeltaXIL = getWt2VrPath().getDeltaX();
        double rDeltaYIL = getWt2VrPath().getDeltaY();
        int sCorrelationMode = getCorrelationMode();
        if(sCorrelationMode == 4)
        {
            rXvr = rDeltaXWtVr;
            rYvr = rDeltaYWtVr;
            rXit = rDeltaXIL;
            rYit = rDeltaYIL;
            try
            {
                rDistanceItVr = Math.sqrt((rXit - rXvr) * (rXit - rXvr) + (rYit - rYvr) * (rYit - rYvr));
                rAngleItVr = (Math.atan2(rYit - rYvr, rXit - rXvr) * 180D) / 3.1415926535897931D;
            }
            catch(Exception exception)
            {
                throw new GeometricException();
            }
            rDeltaXItVr = rXit - rXvr;
            rDeltaYItVr = rYit - rYvr;
            rXwr = rXit + getInterferingLink().getWantedReceiver().getX();
            rYwr = rYit + getInterferingLink().getWantedReceiver().getY();
        } else
        if(sCorrelationMode == 3)
        {
            rXvr = rDeltaXWtVr;
            rYvr = rDeltaYWtVr;
            rXit = rDeltaXWtVr + rDeltaXIL;
            rYit = rDeltaYWtVr + rDeltaYIL;
            try
            {
                rDistanceItVr = StrictMath.sqrt(StrictMath.pow(rXit - rXvr, 2D) + StrictMath.pow(rYit - rYvr, 2D));
                rAngleItVr = (Math.atan2(rYit - rYvr, rXit - rXvr) * 180D) / 3.1415926535897931D;
            }
            catch(Exception exception)
            {
                throw new GeometricException();
            }
            rDeltaXItVr = rXit;
            rDeltaYItVr = rYit;
            rXwr = rXit + getInterferingLink().getWantedReceiver().getX();
            rYwr = rYit + getInterferingLink().getWantedReceiver().getY();
        } else
        if(sCorrelationMode == 5)
        {
            rXvr = rDeltaXWtVr;
            rYvr = rDeltaYWtVr;
            rXit = rDeltaXIL - rDeltaXItWr;
            rYit = rDeltaYIL - rDeltaYItWr;
            try
            {
                rDistanceItVr = Math.sqrt((rXit - rXvr) * (rXit - rXvr) + (rYit - rYvr) * (rYit - rYvr));
                rAngleItVr = (Math.atan2(rYit - rYvr, rXit - rXvr) * 180D) / 3.1415926535897931D;
            }
            catch(Exception exception)
            {
                throw new GeometricException();
            }
            rDeltaXItVr = rXit - rXvr;
            rDeltaYItVr = rYit - rYvr;
            rXwr = rXit + getInterferingLink().getWantedReceiver().getX();
            rYwr = rYit + getInterferingLink().getWantedReceiver().getY();
            System.currentTimeMillis();
        } else
        if(sCorrelationMode == 6)
        {
            rXvr = getVictimLink().getVictimReceiver().getX();
            rYvr = getVictimLink().getVictimReceiver().getY();
            rXit = -rDeltaXItWr + rDeltaXIL;
            rYit = -rDeltaYItWr + rDeltaYIL;
            try
            {
                rDistanceItVr = Math.sqrt(rXit * rXit + rYit * rYit);
                rAngleItVr = (Math.atan2(rYit, rXit) * 180D) / 3.1415926535897931D;
            }
            catch(Exception e)
            {
                throw new GeometricException();
            }
            rDeltaXItVr = rXit;
            rDeltaYItVr = rYit;
            rXwr = rXvr + rDeltaXIL;
            rYwr = rYvr + rDeltaYIL;
        }
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver angle (correlated case) = ").append(rAngleItVr).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver distance (correlated case) = ").append(rDistanceItVr).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver Relative Position X (correlated case) = ").append(rXvr).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver Relative Position Y (correlated case) = ").append(rYvr).toString());
        }
        getInterferingLink().getInterferingTransmitter().setX(rXit);
        getInterferingLink().getInterferingTransmitter().setY(rYit);
        getInterferingLink().getWantedReceiver().setX(rXwr);
        getInterferingLink().getWantedReceiver().setY(rYwr);
        setTxRxDistance(rDistanceItVr);
        setTxRxAngle(rAngleItVr);
    }

    public void itVrLocNonCorrelated(boolean debugMode)
        throws RandomException, GeometricException
    {
        Distribution itVrDistance = getWt2VrPath().getPathDistanceFactor();
        Distribution itVrAngle = getWt2VrPath().getPathAzimuth();
        double rItVrDistTrial;
        double rItVrAngleTrial;
        try
        {
            rItVrDistTrial = itVrDistance.trial();
            rItVrAngleTrial = itVrAngle.trial();
        }
        catch(Exception e)
        {
            throw new RandomException();
        }
        double rRsimu = getInterferingLink().getInterferingTransmitter().getRsimu();
        double rResultItVrDistance = rRsimu * rItVrDistTrial;
        double resultPosX;
        double resultPosY;
        try
        {
            resultPosX = rResultItVrDistance * Math.cos(rItVrAngleTrial * 0.017453292519943295D);
            resultPosY = rResultItVrDistance * Math.sin(rItVrAngleTrial * 0.017453292519943295D);
            resultPosX += getVictimLink().getVictimReceiver().getX();
            resultPosY += getVictimLink().getVictimReceiver().getY();
        }
        catch(Exception exception)
        {
            throw new GeometricException();
        }
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Intefering Transmitter - Victim Receiver angle (NON correlated case) = ").append(rItVrAngleTrial).toString());
            LOG.debug((new StringBuilder()).append("Intefering Transmitter - Victim Receiver distance (NON correlated case) = ").append(rResultItVrDistance).toString());
            LOG.debug((new StringBuilder()).append("Intefering Transmitter - Victim Receiver Relative Position X (NON correlated case) = ").append(resultPosX).toString());
            LOG.debug((new StringBuilder()).append("Intefering Transmitter - Victim Receiver Relative Position Y (NON correlated case) = ").append(resultPosY).toString());
        }
        setTxRxAngle(rItVrAngleTrial);
        setTxRxDistance(rResultItVrDistance);
        getInterferingLink().getInterferingTransmitter().setX(resultPosX);
        getInterferingLink().getInterferingTransmitter().setY(resultPosY);
        getInterferingLink().getWantedReceiver().translateX(resultPosX);
        getInterferingLink().getWantedReceiver().translateY(resultPosY);
    }

    public void itVrColocated(boolean debugMode)
    {
        setTxRxAngle(colocationLink.getTxRxAngle());
        setTxRxDistance(colocationLink.getTxRxDistance());
        getInterferingLink().getInterferingTransmitter().setX(colocationLink.getInterferingLink().getInterferingTransmitter().getX());
        getInterferingLink().getInterferingTransmitter().setY(colocationLink.getInterferingLink().getInterferingTransmitter().getY());
        getInterferingLink().getWantedReceiver().translateX(colocationLink.getInterferingLink().getInterferingTransmitter().getX());
        getInterferingLink().getWantedReceiver().translateY(colocationLink.getInterferingLink().getInterferingTransmitter().getY());
        if(debugMode)
        {
            LOG.debug("Colocating IT");
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver angle = ").append(getTxRxAngle()).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver distance = ").append(getTxRxDistance()).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Position X  = ").append(getInterferingLink().getInterferingTransmitter().getX()).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Position Y  = ").append(getInterferingLink().getInterferingTransmitter().getY()).toString());
        }
    }

    public void itVrLoc(boolean debugMode)
        throws RandomException, GeometricException
    {
        if(colocated)
            itVrColocated(debugMode);
        else
        if(getCorrelationMode() == 0)
            itVrLocNonCorrelated(debugMode);
        else
        if(getCorrelationMode() == 2)
        {
            itVrLocClosest(debugMode);
            getInterferingLink().getInterferingTransmitter().setNbActiveTx(1);
        } else
        if(getCorrelationMode() == 1)
        {
            itVrLocUniform(debugMode);
        } else
        {
            itVrLocCorrelated(debugMode);
            getInterferingLink().getInterferingTransmitter().setNbActiveTx(1);
        }
    }

    public void iLPathAntAziElev(boolean debugMode)
        throws GeometricException
    {
        double rVrAntHeight = getVictimLink().getVictimReceiver().getRxTrialAntHeight();
        double rItAntHeight = getInterferingLink().getInterferingTransmitter().getTxTrialAntHeight();
        double rItAntTilt = getInterferingLink().getTxRxTilt();
        double rVrAntTilt = getVictimLink().getRxTxTilt();
        double rVrItDist = getTxRxDistance() * 1000D;
        double rILangle = getInterferingLink().getTxRxAngle();
        double rILAzi = getInterferingLink().getTxRxAzimuth();
        double rVLangle = getVictimLink().getTxRxAngle();
        double rVLAzi = getVictimLink().getRxTxAzimuth();
        double rItVrangle = getTxRxAngle();
        double rResultVrAzi = -rVLangle + rVLAzi + 180D + rItVrangle;
        double rResultItAzi = -rILangle + rILAzi + 180D + rItVrangle;
        if(rResultVrAzi < 0.0D)
            rResultVrAzi += 360 * ((int)Math.abs(rResultVrAzi / 360D) + 1);
        else
        if(rResultVrAzi > 360D)
            rResultVrAzi -= 360 * (int)(rResultVrAzi / 360D);
        if(rResultItAzi < 0.0D)
            rResultItAzi += 360 * ((int)Math.abs(rResultItAzi / 360D) + 1);
        else
        if(rResultItAzi > 360D)
            rResultItAzi -= 360 * (int)(rResultItAzi / 360D);
        double rVrPhi;
        double rVrAlpha;
        try
        {
            rVrPhi = Math.atan2(rItAntHeight - rVrAntHeight, rVrItDist) * 57.295779513082323D;
            rVrAlpha = rVrAntTilt * Mathematics.cosD(rResultVrAzi);
        }
        catch(Exception e)
        {
            throw new GeometricException(e);
        }
        double rResultVrElev = rVrPhi - rVrAlpha;
        double rItPhi;
        double rItAlpha;
        try
        {
            rItPhi = Math.atan2(rVrAntHeight - rItAntHeight, rVrItDist) * 57.295779513082323D;
            rItAlpha = rItAntTilt * Mathematics.cosD(rResultItAzi);
        }
        catch(Exception e)
        {
            throw new GeometricException();
        }
        double rResultItElev = rItPhi - rItAlpha;
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Intefering Transmitter -> Victim Receiver IT azimuth = ").append(rResultItAzi).toString());
            LOG.debug((new StringBuilder()).append("Intefering Transmitter -> Victim Receiver IT elevation = ").append(rResultItElev).toString());
            LOG.debug((new StringBuilder()).append("Intefering Transmitter -> Victim Receiver VR azimuth = ").append(rResultVrAzi).toString());
            LOG.debug((new StringBuilder()).append("Intefering Transmitter -> Victim Receiver VR elevation = ").append(rResultVrElev).toString());
        }
        if(rResultVrElev < -90D || rResultVrElev > 90D)
            System.currentTimeMillis();
        setTxRxAzimuth(rResultItAzi);
        setTxRxElevation(rResultItElev);
        setRxTxAzimuth(rResultVrAzi);
        setRxTxElevation(rResultVrElev);
    }

    public void itVrLocClosest(boolean debugMode)
        throws RandomException, GeometricException
    {
        Distribution wtVrAngle = getWt2VrPath().getPathAzimuth();
        Distribution wtVrDistance = getWt2VrPath().getPathDistanceFactor();
        double rRsimu = getInterferingLink().getInterferingTransmitter().getRsimu();
        double rWtVrDistTrial;
        double rWtVrAngleTrial;
        try
        {
            int i = 0;
            do
            {
                rWtVrDistTrial = wtVrDistance.trial();
                i++;
            } while(rWtVrDistTrial * rRsimu < protectionDistance && i < 10);
            if(rWtVrDistTrial * rRsimu < protectionDistance)
                rWtVrDistTrial = protectionDistance;
            else
                rWtVrDistTrial *= rRsimu;
            rWtVrAngleTrial = wtVrAngle.trial();
        }
        catch(Exception e)
        {
            throw new RandomException();
        }
        double rResultWtVrDistance = rWtVrDistTrial;
        double rResultPosX;
        double rResultPosY;
        try
        {
            rResultPosX = rResultWtVrDistance * Math.cos(rWtVrAngleTrial * 0.017453292519943295D);
            rResultPosY = rResultWtVrDistance * Math.sin(rWtVrAngleTrial * 0.017453292519943295D);
        }
        catch(Exception e)
        {
            throw new GeometricException();
        }
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver angle (location closest mode) = ").append(rWtVrAngleTrial).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver distance (location closest mode) = ").append(rResultWtVrDistance).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver Pos X = (location closest mode) ").append(rResultPosX).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver Pos Y = (location closest mode) ").append(rResultPosY).toString());
        }
        setTxRxAngle(rWtVrAngleTrial);
        setTxRxDistance(rResultWtVrDistance);
        getWt2VrPath().setDeltaX(rResultPosX);
        getWt2VrPath().setDeltaY(rResultPosY);
        double vrX = getVictimLink().getVictimReceiver().getX();
        double vrY = getVictimLink().getVictimReceiver().getY();
        double itX = vrX + rResultPosX;
        double itY = vrY + rResultPosY;
        getInterferingLink().getInterferingTransmitter().setX(itX);
        getInterferingLink().getInterferingTransmitter().setY(itY);
        getInterferingLink().getWantedReceiver().translateX(itX);
        getInterferingLink().getWantedReceiver().translateY(itY);
    }

    public void itVrLocUniform(boolean debugMode)
        throws RandomException, GeometricException
    {
        Distribution wtVrDistance = distanceFactor;
        Distribution wtVrAngle = getWt2VrPath().getPathAzimuth();
        double rWtVrDistTrial;
        double rWtVrAngleTrial;
        try
        {
            rWtVrDistTrial = wtVrDistance.trial();
            rWtVrAngleTrial = wtVrAngle.trial();
        }
        catch(Exception e)
        {
            throw new RandomException();
        }
        double rRsimu = getInterferingLink().getInterferingTransmitter().getRsimu();
        double rResultWtVrDistance = (rRsimu - protectionDistance) * rWtVrDistTrial + protectionDistance;
        double rResultPosX;
        double rResultPosY;
        try
        {
            rResultPosX = rResultWtVrDistance * Math.cos(rWtVrAngleTrial * 0.017453292519943295D);
            rResultPosY = rResultWtVrDistance * Math.sin(rWtVrAngleTrial * 0.017453292519943295D);
        }
        catch(Exception e)
        {
            throw new GeometricException();
        }
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver angle (location uniform mode) = ").append(rWtVrAngleTrial).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver distance (location uniform mode) = ").append(rResultWtVrDistance).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver Pos X = (location uniform mode) ").append(rResultPosX).toString());
            LOG.debug((new StringBuilder()).append("Interfering Transmitter - Victim Receiver Pos Y = (location uniform mode) ").append(rResultPosY).toString());
        }
        setTxRxAngle(rWtVrAngleTrial);
        setTxRxDistance(rResultWtVrDistance);
        double vrX = getVictimLink().getVictimReceiver().getX();
        double vrY = getVictimLink().getVictimReceiver().getY();
        double itX = vrX + rResultPosX;
        double itY = vrY + rResultPosY;
        getInterferingLink().getInterferingTransmitter().setX(itX);
        getInterferingLink().getInterferingTransmitter().setY(itY);
        getInterferingLink().getWantedReceiver().translateX(itX);
        getInterferingLink().getWantedReceiver().translateY(itY);
    }

    public UnwantedInterference getUnwantedInterference()
    {
        return unwantedInterference;
    }

    public String toString()
    {
        return getReference();
    }

    public void updateNodeAttributes()
    {
        super.updateNodeAttributes();
        getInterferingLink().updateNodeAttributes();
        getWt2VrPath().updateNodeAttributes();
        try
        {
            it2WrPathComponent.updateNodeAttributes();
            itComp.updateNodeAttributes();
            cdmasystem.updateNodeAttributes();
        }
        catch(Exception e) { }
    }

    public void setUnwantedInterference(UnwantedInterference unwantedInterference)
    {
        this.unwantedInterference = unwantedInterference;
        this.unwantedInterference.setInterferenceLink(this);
        nodeAttributeIsDirty = false;
    }

    public void setWt2vrPath(TransmitterToReceiverPath wt2vrPath)
    {
        this.wt2vrPath = wt2vrPath;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("InterferenceLink");
        element.setAttribute("correlationMode", Integer.toString(getCorrelationMode()));
        element.setAttribute("protectionDistance", Double.toString(protectionDistance));
        element.setAttribute("isCDMA", Boolean.toString(isCDMASystem));
        element.setAttribute("colocated", Boolean.toString(colocated));
        try
        {
            element.setAttribute("colocation_link", colocationLink.getReference());
        }
        catch(Exception e) { }
        element.appendChild(getWt2VrPath().toElement(doc));
        element.appendChild(getBlockingInterference().toElement(doc));
        element.appendChild(getUnwantedInterference().toElement(doc));
        element.appendChild(super.toElement(doc));
        element.appendChild(getInterferingLink().toElement(doc));
        if(isCDMASystem)
            element.appendChild(cdmasystem.toElement(doc));
        return element;
    }

    public Node getChildAt(int childIndex)
    {
        Node child;
        if(isCDMASystem)
            switch(childIndex)
            {
            case 0: // '\0'
                child = new LocalComponent(cdmasystem, "Interfering CDMA");
                break;

            case 1: // '\001'
                if(it2WrPathComponent == null)
                    it2WrPathComponent = new ITx2WRxPathComponent();
                child = new LocalComponent(it2WrPathComponent, "IT - VR path");
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("ChildIndex out of range <").append(childIndex).append(">").toString());
            }
        else
            switch(childIndex)
            {
            case 0: // '\0'
                if(itComp == null)
                    itComp = new InterferingTransmitterComponent();
                child = new LocalComponent(itComp, "Interfering transmitter");
                break;

            case 1: // '\001'
                child = new LocalComponent(new WantedReceiverComponent(), "Wanted receiver");
                break;

            case 2: // '\002'
                if(it2WrPathComponent == null)
                    it2WrPathComponent = new ITx2WRxPathComponent();
                child = new LocalComponent(it2WrPathComponent, "IT - VR path");
                break;

            case 3: // '\003'
                child = new LocalComponent(getInterferingLink().getWt2VrPath(), "IT - WR path");
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("ChildIndex out of range <").append(childIndex).append(">").toString());
            }
        return child;
    }

    public int getChildCount()
    {
        return !isCDMASystem ? 4 : 2;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Reference", "", getReference(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Description", "", getDescription(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Frequency distribution", "", getInterferingLink().getInterferingTransmitter().getFrequency(), "Distribution", null, false, false, null));
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
            getInterferingLink().getInterferingTransmitter().setFrequency((Distribution)aValue);
            break;
        }
    }

    public static final int resolveRelativeLocation(String genEnvStr)
    {
        int relLoc;
        if(genEnvStr.equals(RELATIVE_LOCATION_MODES[0]))
            relLoc = 0;
        else
        if(genEnvStr.equals(RELATIVE_LOCATION_MODES[1]))
            relLoc = 1;
        else
        if(genEnvStr.equals(RELATIVE_LOCATION_MODES[2]))
            relLoc = 2;
        else
        if(genEnvStr.equals(RELATIVE_LOCATION_MODES[3]))
            relLoc = 3;
        else
        if(genEnvStr.equals(RELATIVE_LOCATION_MODES[4]))
            relLoc = 4;
        else
        if(genEnvStr.equals(RELATIVE_LOCATION_MODES[5]))
            relLoc = 5;
        else
        if(genEnvStr.equals(RELATIVE_LOCATION_MODES[6]))
            relLoc = 6;
        else
            throw new IllegalArgumentException("Cannot resolve general environment string");
        return relLoc;
    }

    public boolean isColocated()
    {
        return colocated;
    }

    public void setColocated(boolean colocated)
    {
        this.colocated = colocated;
    }

    public InterferenceLink getColocationLink()
    {
        return colocationLink;
    }

    public void setColocationLink(InterferenceLink colocationLink)
    {
        this.colocationLink = colocationLink;
    }

    public IRSSVector getBlockingVector()
    {
        return blockingVector;
    }

    public void setBlockingVector(IRSSVector blockingVector)
    {
        this.blockingVector = blockingVector;
    }

    public IRSSVector getUnwantedVector()
    {
        return unwantedVector;
    }

    public void setUnwantedVector(IRSSVector unwantedVector)
    {
        this.unwantedVector = unwantedVector;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/core/InterferenceLink);
    public static final String RELATIVE_LOCATION_MODES[] = {
        "None", "Uniform density", "Closest interferer", "VRx->ITx", "WTx->ITx", "WTx->WRx", "VRx->WRx"
    };
    private static final boolean MODE_ENABLE_STATUS[][] = {
        {
            false, false, true, true, true, true, false
        }, {
            false, false, true, false, false, true, true
        }, {
            false, false, true, false, false, false, true
        }, {
            true, true, false, false, false, false, false
        }, {
            true, true, false, false, false, false, false
        }, {
            true, true, false, false, false, false, false
        }, {
            true, true, false, false, false, false, false
        }
    };
    public static final int RELATIVE_LOCATION_NONE = 0;
    public static final int RELATIVE_LOCATION_UNIFORM = 1;
    public static final int RELATIVE_LOCATION_CLOSEST = 2;
    public static final int RELATIVE_LOCATION_IT_VR = 3;
    public static final int RELATIVE_LOCATION_IT_WT = 4;
    public static final int RELATIVE_LOCATION_WR_WT = 5;
    public static final int RELATIVE_LOCATION_WR_VR = 6;
    private static final int PID = 180;
    private static final int KM_TO_METERS = 1000;
    private ITx2WRxPathComponent it2WrPathComponent;
    private InterferingTransmitterComponent itComp;
    private int correlationMode;
    private double protectionDistance;
    private BlockingInterference blockingInterference1;
    private UnwantedInterference unwantedInterference;
    private VictimSystemLink victimLink;
    private InterferingSystemLink interferingLink;
    private TransmitterToReceiverPath wt2vrPath;
    private Distribution distanceFactor;
    private boolean colocated;
    private InterferenceLink colocationLink;
    private boolean isCDMASystem;
    private CDMASystem cdmasystem;
    private IRSSVector blockingVector;
    private IRSSVector unwantedVector;


}