// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMASystem.java

package org.seamcat.cdma;

import java.util.*;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.seamcat.cdma.xml.CDMASystemXmlTags;
import org.seamcat.distribution.*;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Point2D;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.*;
import org.seamcat.model.core.AntennaPattern;
import org.seamcat.presentation.MainWindow;
import org.seamcat.propagation.HataSE21Model;
import org.seamcat.propagation.PropagationModel;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.cdma:
//            CDMALinkLevelData, CDMADownlinkSystem, CDMAUplinkSystem, CDMAInterferer, 
//            CDMAElement, UserTerminal, CDMATriSectorCell, CDMAOmniDirectionalCell, 
//            CDMABalancingListener, CDMACell, CDMALink, NonInterferedCapacityListener, 
//            InterferenceGenerator

public abstract class CDMASystem extends SeamcatComponent
    implements CDMASystemXmlTags
{
    public static final class CellType extends Enum
    {

        public static CellType[] values()
        {
            return (CellType[])$VALUES.clone();
        }

        public static CellType valueOf(String name)
        {
            return (CellType)Enum.valueOf(org/seamcat/cdma/CDMASystem$CellType, name);
        }

        public int getNumberOfCellsPerSite()
        {
            return numberOfCellsPerSite;
        }

        public static final CellType OmniDirectionalAntenna;
        public static final CellType TriSectorAntenna;
        private int numberOfCellsPerSite;
        private static final CellType $VALUES[];

        static 
        {
            OmniDirectionalAntenna = new CellType("OmniDirectionalAntenna", 0, 1);
            TriSectorAntenna = new CellType("TriSectorAntenna", 1, 3);
            $VALUES = (new CellType[] {
                OmniDirectionalAntenna, TriSectorAntenna
            });
        }

        private CellType(String s, int i, int cellsPerSite)
        {
            super(s, i);
            numberOfCellsPerSite = cellsPerSite;
        }
    }

    public static final class LinkDirection extends Enum
    {

        public static LinkDirection[] values()
        {
            return (LinkDirection[])$VALUES.clone();
        }

        public static LinkDirection valueOf(String name)
        {
            return (LinkDirection)Enum.valueOf(org/seamcat/cdma/CDMASystem$LinkDirection, name);
        }

        public boolean isUplink()
        {
            return uplink;
        }

        public boolean isDownlink()
        {
            return !isUplink();
        }

        public int getIntegerRepresentation()
        {
            return !uplink ? 1 : 0;
        }

        public static final LinkDirection Uplink;
        public static final LinkDirection Downlink;
        private boolean uplink;
        private static final LinkDirection $VALUES[];

        static 
        {
            Uplink = new LinkDirection("Uplink", 0, true);
            Downlink = new LinkDirection("Downlink", 1, false);
            $VALUES = (new LinkDirection[] {
                Uplink, Downlink
            });
        }

        private LinkDirection(String s, int i, boolean isuplink)
        {
            super(s, i);
            uplink = isuplink;
        }
    }


    public CDMASystem()
    {
        this(5D);
    }

    protected CDMASystem(double _cellRadius)
    {
        cdmaLinkComponent = LinkDirection.Downlink;
        receiverNoiseFigure = 4D;
        handoverMargin = 4D;
        callDropThreshold = 3D;
        successThreshold = 0.5D;
        powerControlConvergenceThreshold = 0.001D;
        voiceBitRate = 9.5999999999999996D;
        frequency = 0.0D;
        systemBandwidth = 1.25D;
        voiceActivityFactor = 0.5D;
        numberOfCellSitesInPowerControlCluster = 19;
        typeOfCellsInPowerControlCluster = CellType.OmniDirectionalAntenna;
        cellRadius = 5D;
        baseStationAntennaHeight = new ConstantDistribution(30D);
        omniDirectionalAntenna = new Antenna(9D);
        triSectorAntenna = new Antenna();
        mobileStationAntennaHeight = new ConstantDistribution(1.5D);
        mobileStationAntennaGain = new ConstantDistribution(0.0D);
        mobileStationMobilityDistribution = new StairDistribution(new Point2D[] {
            new Point2D(0.0D, 0.25D), new Point2D(3D, 0.5D), new Point2D(30D, 0.75D), new Point2D(100D, 1.0D)
        });
        propagationModel = new HataSE21Model();
        baseStationPilotFraction = 0.14999999999999999D;
        baseStationOverheadFraction = 0.050000000000000003D;
        baseStationMaximumBroadcastPower = 40D;
        baseStationMaxChannelPowerFraction = 0.14999999999999999D;
        baseStationPowerControlStep = 1.0D;
        targetNetworkNoiseRiseOverThermalNoise = 5.5D;
        targetOutagePercentage = 5D;
        mobileStationMaximumTransmitPower = 25D;
        mobileStationPowerControlRange = 75D;
        mobileStationPowerControlStep = 1.0D;
        cellStructure = 6;
        numberOfTiers = 2;
        initialBaseStationBroadcastPower = 0.69999999999999996D;
        minimumCouplingLoss = 70D;
        useridcount = 1;
        sigmaShadow = 1.0D;
        userLocation = new UniformPolarDistanceDistribution(1.0D);
        userAngle = new UniformPolarAngleDistribution(360D);
        activeUsers = new ArrayList();
        inactiveUsers = new ArrayList();
        droppedUsers = new ArrayList();
        notActivatedUsers = new ArrayList();
        noLinkLevelFoundUsers = new ArrayList();
        externalInterferers = new ArrayList();
        listeners = new ArrayList(3);
        inactiveUserCount = 0;
        peakPowerConvergenceIterations = 0;
        triSectorReferenceCellSelection = 0;
        usersPerCell = 20;
        deltaN = 20;
        numberOfTrials = 20;
        systemTolerance = 0.050000000000000003D;
        external_usersPerCell = 20;
        external_deltaN = 20;
        maximumPeakCount = 150;
        succesCriteria = 0.80000000000000004D;
        simulateCapacity = true;
        capacitySimulated = false;
        fineTuning = false;
        simulateNetworkEdge = false;
        networkEdgeLeftSide = true;
        victimSystem = false;
        referenceCellId = 0;
        measureInterferenceFromCluster = false;
        stopped = false;
        snapshotShouldBeIgnored = false;
        useSmartCapacityFindingAlgorithm = true;
        highestPcLoopCount = 0;
        finalFineTuning = false;
        LOG = Logger.getLogger(org/seamcat/cdma/CDMASystem);
        setReference("DEFAULT_CDMA_SYSTEM");
        setDescription("");
        setCellRadius(_cellRadius);
        linkLevelData = new CDMALinkLevelData((CDMALinkLevelData)Model.getInstance().getLibrary().getCDMALinkLevelData().get(1));
        setTypeOfCellsInPowerControlCluster(CellType.OmniDirectionalAntenna);
        configureDefaultTriSectorAntenna();
        omniDirectionalAntenna.getHorizontalPattern().setRangeMin(-180D);
        omniDirectionalAntenna.getHorizontalPattern().setRangeMax(180D);
        generateSystemCells();
        repositionSystem(getLocationX(), getLocationY());
    }

    protected CDMASystem(CDMASystem cdma)
    {
        cdmaLinkComponent = LinkDirection.Downlink;
        receiverNoiseFigure = 4D;
        handoverMargin = 4D;
        callDropThreshold = 3D;
        successThreshold = 0.5D;
        powerControlConvergenceThreshold = 0.001D;
        voiceBitRate = 9.5999999999999996D;
        frequency = 0.0D;
        systemBandwidth = 1.25D;
        voiceActivityFactor = 0.5D;
        numberOfCellSitesInPowerControlCluster = 19;
        typeOfCellsInPowerControlCluster = CellType.OmniDirectionalAntenna;
        cellRadius = 5D;
        baseStationAntennaHeight = new ConstantDistribution(30D);
        omniDirectionalAntenna = new Antenna(9D);
        triSectorAntenna = new Antenna();
        mobileStationAntennaHeight = new ConstantDistribution(1.5D);
        mobileStationAntennaGain = new ConstantDistribution(0.0D);
        mobileStationMobilityDistribution = new StairDistribution(new Point2D[] {
            new Point2D(0.0D, 0.25D), new Point2D(3D, 0.5D), new Point2D(30D, 0.75D), new Point2D(100D, 1.0D)
        });
        propagationModel = new HataSE21Model();
        baseStationPilotFraction = 0.14999999999999999D;
        baseStationOverheadFraction = 0.050000000000000003D;
        baseStationMaximumBroadcastPower = 40D;
        baseStationMaxChannelPowerFraction = 0.14999999999999999D;
        baseStationPowerControlStep = 1.0D;
        targetNetworkNoiseRiseOverThermalNoise = 5.5D;
        targetOutagePercentage = 5D;
        mobileStationMaximumTransmitPower = 25D;
        mobileStationPowerControlRange = 75D;
        mobileStationPowerControlStep = 1.0D;
        cellStructure = 6;
        numberOfTiers = 2;
        initialBaseStationBroadcastPower = 0.69999999999999996D;
        minimumCouplingLoss = 70D;
        useridcount = 1;
        sigmaShadow = 1.0D;
        userLocation = new UniformPolarDistanceDistribution(1.0D);
        userAngle = new UniformPolarAngleDistribution(360D);
        activeUsers = new ArrayList();
        inactiveUsers = new ArrayList();
        droppedUsers = new ArrayList();
        notActivatedUsers = new ArrayList();
        noLinkLevelFoundUsers = new ArrayList();
        externalInterferers = new ArrayList();
        listeners = new ArrayList(3);
        inactiveUserCount = 0;
        peakPowerConvergenceIterations = 0;
        triSectorReferenceCellSelection = 0;
        usersPerCell = 20;
        deltaN = 20;
        numberOfTrials = 20;
        systemTolerance = 0.050000000000000003D;
        external_usersPerCell = 20;
        external_deltaN = 20;
        maximumPeakCount = 150;
        succesCriteria = 0.80000000000000004D;
        simulateCapacity = true;
        capacitySimulated = false;
        fineTuning = false;
        simulateNetworkEdge = false;
        networkEdgeLeftSide = true;
        victimSystem = false;
        referenceCellId = 0;
        measureInterferenceFromCluster = false;
        stopped = false;
        snapshotShouldBeIgnored = false;
        useSmartCapacityFindingAlgorithm = true;
        highestPcLoopCount = 0;
        finalFineTuning = false;
        setBaseStationAntennaHeight(Distribution.create(cdma.getBaseStationAntennaHeight()));
        setBaseStationMaxChannelPowerFraction(cdma.getBaseStationMaxChannelPowerFraction());
        setBaseStationMaximumBroadcastPower(cdma.getBaseStationMaximumBroadcastPowerdBm());
        setBaseStationOverheadFraction(cdma.getBaseStationOverheadFraction());
        setBaseStationPilotFraction(cdma.getBaseStationPilotFraction());
        setBaseStationPowerControlStep(cdma.getBaseStationPowerControlStep());
        setCallDropThreshold(cdma.getCallDropThreshold());
        setSuccessThreshold(cdma.getSuccessThreshold());
        setSystemTolerance(cdma.getSystemTolerance());
        setCDMALinkComponent(cdma.getCDMALinkComponent());
        setCellRadius(cdma.getCellRadius());
        setCellStructure(cdma.getCellStructure());
        setDeltaN(cdma.getDeltaN());
        setDescription(cdma.getDescription());
        setDisplacementAngle(cdma.getDisplacementAngle());
        setFrequency(cdma.getFrequency());
        setHandoverMargin(cdma.getHandoverMargin());
        setInitialBaseStationBroadcastPower(cdma.getInitialBaseStationBroadcastPower());
        setInterCellDistance(cdma.getInterCellDistance());
        setLinkLevelData(new CDMALinkLevelData(cdma.getLinkLevelData()));
        setLocation(cdma.getLocationX(), cdma.getLocationY());
        setMinimumCouplingLoss(cdma.getMinimumCouplingLoss());
        setMobileStationAntennaGain(Distribution.create(cdma.getMobileStationAntennaGain()));
        setMobileStationAntennaHeight(Distribution.create(cdma.getMobileStationAntennaHeight()));
        setMobileStationMaximumTransmitPower(cdma.getMobileStationMaximumTransmitPower());
        setMobileStationMobilityDistribution(Distribution.create(cdma.getMobileStationMobilityDistribution()));
        setMobileStationPowerControlRange(cdma.getMobileStationPowerControlRange());
        setMobileStationPowerControlStep(cdma.getMobileStationPowerControlStep());
        setNonInterferedCapacity(cdma.getNonInterferedCapacity());
        setNumberOfCellSitesInPowerControlCluster(cdma.getNumberOfCellSitesInPowerControlCluster());
        setNumberOfTiers(cdma.getNumberOfTiers());
        setNumberOfTrials(cdma.getNumberOfTrials());
        setOmniDirectionalAntenna(new Antenna(cdma.getOmniDirectionalAntenna()));
        setPowerControlConvergenceThreshold(cdma.getPowerControlConvergenceThreshold());
        setProcessingGain(cdma.calculateProcessingGain());
        setPropagationModel(cdma.getPropagationModel());
        setReceiverNoiseFigure(cdma.getReceiverNoiseFigure());
        setSimulateCapacity(cdma.isSimulateCapacity());
        setSimulateNetworkEdge(cdma.isSimulateNetworkEdge());
        setNetworkEdgeLeftSide(cdma.isNetworkEdgeLeftSide());
        setSuccesCriteria(cdma.getSuccesCriteria());
        setSuccesRate(cdma.getSuccesRate());
        setSystemBandwidth(cdma.getSystemBandwidth());
        setTargetNetworkNoiseRiseOverThermalNoise(cdma.getTargetNetworkNoiseRiseOverThermalNoise_dB());
        setTargetOutagePercentage(cdma.getTargetOutagePercentage());
        setThermalNoise(cdma.getThermalNoiseInWatt());
        setTriSectorAntenna(new Antenna(cdma.getTriSectorAntenna()));
        setTypeOfCellsInPowerControlCluster(cdma.getTypeOfCellsInPowerControlCluster());
        setUsersPerCell(cdma.getUsersPerCell());
        setVoiceActivityFactor(cdma.getVoiceActivityFactor());
        setVoiceBitRate(cdma.getVoiceBitRate());
        setTriSectorReferenceCellSelection(cdma.getTriSectorReferenceCellSelection());
        calculateThermalNoise();
        generateSystemCells();
        setReferenceCellId(cdma.referenceCellId);
        setVictimSystem(cdma.isVictimSystem());
        setMeasureInterferenceFromCluster(cdma.isMeasureInterferenceFromCluster());
        repositionSystem(getLocationX(), getLocationY());
    }

    protected CDMASystem(Element element)
    {
        cdmaLinkComponent = LinkDirection.Downlink;
        receiverNoiseFigure = 4D;
        handoverMargin = 4D;
        callDropThreshold = 3D;
        successThreshold = 0.5D;
        powerControlConvergenceThreshold = 0.001D;
        voiceBitRate = 9.5999999999999996D;
        frequency = 0.0D;
        systemBandwidth = 1.25D;
        voiceActivityFactor = 0.5D;
        numberOfCellSitesInPowerControlCluster = 19;
        typeOfCellsInPowerControlCluster = CellType.OmniDirectionalAntenna;
        cellRadius = 5D;
        baseStationAntennaHeight = new ConstantDistribution(30D);
        omniDirectionalAntenna = new Antenna(9D);
        triSectorAntenna = new Antenna();
        mobileStationAntennaHeight = new ConstantDistribution(1.5D);
        mobileStationAntennaGain = new ConstantDistribution(0.0D);
        mobileStationMobilityDistribution = new StairDistribution(new Point2D[] {
            new Point2D(0.0D, 0.25D), new Point2D(3D, 0.5D), new Point2D(30D, 0.75D), new Point2D(100D, 1.0D)
        });
        propagationModel = new HataSE21Model();
        baseStationPilotFraction = 0.14999999999999999D;
        baseStationOverheadFraction = 0.050000000000000003D;
        baseStationMaximumBroadcastPower = 40D;
        baseStationMaxChannelPowerFraction = 0.14999999999999999D;
        baseStationPowerControlStep = 1.0D;
        targetNetworkNoiseRiseOverThermalNoise = 5.5D;
        targetOutagePercentage = 5D;
        mobileStationMaximumTransmitPower = 25D;
        mobileStationPowerControlRange = 75D;
        mobileStationPowerControlStep = 1.0D;
        cellStructure = 6;
        numberOfTiers = 2;
        initialBaseStationBroadcastPower = 0.69999999999999996D;
        minimumCouplingLoss = 70D;
        useridcount = 1;
        sigmaShadow = 1.0D;
        userLocation = new UniformPolarDistanceDistribution(1.0D);
        userAngle = new UniformPolarAngleDistribution(360D);
        activeUsers = new ArrayList();
        inactiveUsers = new ArrayList();
        droppedUsers = new ArrayList();
        notActivatedUsers = new ArrayList();
        noLinkLevelFoundUsers = new ArrayList();
        externalInterferers = new ArrayList();
        listeners = new ArrayList(3);
        inactiveUserCount = 0;
        peakPowerConvergenceIterations = 0;
        triSectorReferenceCellSelection = 0;
        usersPerCell = 20;
        deltaN = 20;
        numberOfTrials = 20;
        systemTolerance = 0.050000000000000003D;
        external_usersPerCell = 20;
        external_deltaN = 20;
        maximumPeakCount = 150;
        succesCriteria = 0.80000000000000004D;
        simulateCapacity = true;
        capacitySimulated = false;
        fineTuning = false;
        simulateNetworkEdge = false;
        networkEdgeLeftSide = true;
        victimSystem = false;
        referenceCellId = 0;
        measureInterferenceFromCluster = false;
        stopped = false;
        snapshotShouldBeIgnored = false;
        useSmartCapacityFindingAlgorithm = true;
        highestPcLoopCount = 0;
        finalFineTuning = false;
        if(element == null)
        {
            setTypeOfCellsInPowerControlCluster(CellType.OmniDirectionalAntenna);
            setInterCellDistance(5D);
            linkLevelData = new CDMALinkLevelData((CDMALinkLevelData)Model.getInstance().getLibrary().getCDMALinkLevelData().get(1));
            configureDefaultTriSectorAntenna();
            calculateThermalNoise();
            generateSystemCells();
            return;
        }
        if(Integer.parseInt(element.getAttribute("linkComponentDownlink")) == 0)
            setCDMALinkComponent(LinkDirection.Uplink);
        else
        if(Integer.parseInt(element.getAttribute("linkComponentDownlink")) == 1)
            setCDMALinkComponent(LinkDirection.Downlink);
        try
        {
            setMinimumCouplingLoss(Double.parseDouble(element.getAttribute("minimum_coupling_loss")));
        }
        catch(Exception e) { }
        setReceiverNoiseFigure(Double.parseDouble(element.getAttribute("receiverNoiseFigure")));
        setHandoverMargin(Double.parseDouble(element.getAttribute("handover")));
        setCallDropThreshold(Double.parseDouble(element.getAttribute("callDropThreshold")));
        try
        {
            setSuccessThreshold(Double.parseDouble(element.getAttribute("successThreshold")));
        }
        catch(Exception e) { }
        try
        {
            setSystemTolerance(Double.parseDouble(element.getAttribute("allowable-initial-outage")));
        }
        catch(Exception e) { }
        setPowerControlConvergenceThreshold(Double.parseDouble(element.getAttribute("powerControlThreshold")));
        setVoiceBitRate(Double.parseDouble(element.getAttribute("voiceBitrate")));
        setSystemBandwidth(Double.parseDouble(element.getAttribute("systemBandwith")));
        setCellStructure(Integer.parseInt(element.getAttribute("cellStructure")));
        setNumberOfTiers(Integer.parseInt(element.getAttribute("number_of_tiers")));
        setDisplacementAngle(Double.parseDouble(element.getAttribute("displacementAngle")));
        setLocationX(Double.parseDouble(element.getAttribute("location_x")));
        setLocationY(Double.parseDouble(element.getAttribute("location_y")));
        try
        {
            setCellRadius(Double.parseDouble(element.getAttribute("cell_radius")));
        }
        catch(Exception e)
        {
            setCellRadius(getInterCellDistance());
        }
        setVoiceActivityFactor(Double.parseDouble(element.getAttribute("voice_activity_factor")));
        setNumberOfCellSitesInPowerControlCluster(Integer.parseInt(element.getAttribute("number_of_cells")));
        setTargetNetworkNoiseRiseOverThermalNoise(Double.parseDouble(element.getAttribute("target_noise")));
        setFrequency(Double.parseDouble(element.getAttribute("frequency")));
        try
        {
            setUsersPerCell(Integer.parseInt(element.getAttribute("users_per_cell")));
            setDeltaN(Integer.parseInt(element.getAttribute("delta_users_per_cell")));
            setNumberOfTrials(Integer.parseInt(element.getAttribute("number_of_trials")));
            setSimulateCapacity("true".equalsIgnoreCase(element.getAttribute("simulate_capacity")));
        }
        catch(Exception e) { }
        try
        {
            setSimulateNetworkEdge("true".equalsIgnoreCase(element.getAttribute("simulate_network_edge")));
            setNetworkEdgeLeftSide("true".equalsIgnoreCase(element.getAttribute("simulate_left_network_edge")));
        }
        catch(Exception e)
        {
            setNetworkEdgeLeftSide(true);
        }
        Element bselement = (Element)element.getElementsByTagName("BaseStation").item(0);
        setBaseStationPilotFraction(Double.parseDouble(bselement.getAttribute("pilotFraction")));
        setBaseStationOverheadFraction(Double.parseDouble(bselement.getAttribute("overheadFraction")));
        setBaseStationMaxChannelPowerFraction(Double.parseDouble(bselement.getAttribute("maxPowerFraction")));
        setBaseStationMaximumBroadcastPower(Double.parseDouble(bselement.getAttribute("maxPower")));
        setBaseStationPowerControlStep(Double.parseDouble(bselement.getAttribute("powerStep")));
        setInitialBaseStationBroadcastPower(Double.parseDouble(bselement.getAttribute("initialPower")));
        setBaseStationAntennaHeight(Distribution.create((Element)bselement.getElementsByTagName("antenna-height").item(0).getFirstChild()));
        try
        {
            setOmniDirectionalAntenna(new Antenna((Element)bselement.getElementsByTagName("Omni-Antenna").item(0).getFirstChild()));
        }
        catch(Exception ignored)
        {
            omniDirectionalAntenna.getHorizontalPattern().setRangeMin(-180D);
            omniDirectionalAntenna.getHorizontalPattern().setRangeMax(180D);
        }
        try
        {
            setTriSectorAntenna(new Antenna((Element)bselement.getElementsByTagName("Tri-Sector-Antenna").item(0).getFirstChild()));
            if(triSectorAntenna.getUseHorizontalPattern())
            {
                triSectorAntenna.getHorizontalPattern().setRangeMin(-180D);
                triSectorAntenna.getHorizontalPattern().setRangeMax(180D);
            }
        }
        catch(Exception e)
        {
            configureDefaultTriSectorAntenna();
        }
        if(Integer.parseInt(element.getAttribute("cellType")) == 1)
            setTypeOfCellsInPowerControlCluster(CellType.OmniDirectionalAntenna);
        else
            setTypeOfCellsInPowerControlCluster(CellType.TriSectorAntenna);
        try
        {
            setTriSectorReferenceCellSelection(Integer.parseInt(element.getAttribute("referenceSector")));
        }
        catch(Exception e) { }
        Element mselement = (Element)element.getElementsByTagName("MobileStation").item(0);
        setMobileStationMaximumTransmitPower(Double.parseDouble(mselement.getAttribute("maxPower")));
        setMobileStationPowerControlRange(Double.parseDouble(mselement.getAttribute("powerRange")));
        setMobileStationPowerControlStep(Double.parseDouble(mselement.getAttribute("powerStep")));
        setMobileStationAntennaGain(Distribution.create((Element)mselement.getElementsByTagName("AntennaGain").item(0).getFirstChild()));
        setMobileStationAntennaHeight(Distribution.create((Element)mselement.getElementsByTagName("antenna-height").item(0).getFirstChild()));
        setMobileStationMobilityDistribution(Distribution.create((Element)mselement.getElementsByTagName("UserMobility").item(0).getFirstChild()));
        userAngle = Distribution.create((Element)element.getElementsByTagName("UserAngle").item(0).getFirstChild());
        userLocation = Distribution.create((Element)element.getElementsByTagName("UserDistance").item(0).getFirstChild());
        propagationModel = PropagationModel.create((Element)element.getElementsByTagName("PropagationModel").item(0).getFirstChild());
        try
        {
            Element lldElement = (Element)element.getElementsByTagName("CDMA-Link-level-data").item(0);
            String _frequency = lldElement.getAttribute("frequency");
            String _source = lldElement.getAttribute("source");
            String _system = lldElement.getAttribute("system");
            if(_frequency != null && _frequency.length() > 0 || _source != null && _source.length() > 0 || _system != null && _system.length() > 0)
            {
                linkLevelData = new CDMALinkLevelData(lldElement);
                if(!Model.getInstance().getLibrary().getCDMALinkLevelData().contains(linkLevelData))
                {
                    Model.getInstance().getLibrary().getCDMALinkLevelData().add(linkLevelData);
                    JOptionPane.showMessageDialog(MainWindow.getInstance(), STRINGLIST.getString("CDMA_LLD_IMPORT"), STRINGLIST.getString("CDMA_LLD_IMPORT_TITLE"), 1);
                }
            } else
            {
                LOG.warn("Old format LLD detected");
                JOptionPane.showMessageDialog(MainWindow.getInstance(), STRINGLIST.getString("CDMA_OLD_LLD_WARN"), "XML Parsing Warning", 2);
            }
        }
        catch(Exception e) { }
        try
        {
            referenceCellId = Integer.parseInt(element.getAttribute("reference_cell_id"));
        }
        catch(Exception e)
        {
            referenceCellId = 0;
        }
        try
        {
            victimSystem = Boolean.parseBoolean(element.getAttribute("is_victim_system"));
        }
        catch(Exception e)
        {
            victimSystem = false;
        }
        try
        {
            measureInterferenceFromCluster = Boolean.parseBoolean(element.getAttribute("interference_from_cluster"));
        }
        catch(Exception e)
        {
            measureInterferenceFromCluster = false;
        }
        calculateThermalNoise();
        generateSystemCells();
        referenceCell = cells[referenceCellId][triSectorReferenceCellSelection];
        repositionSystem(getLocationX(), getLocationX());
    }

    public static CDMASystem createCDMASystem(Element elem)
    {
        if(Integer.valueOf(elem.getAttribute("linkComponentDownlink")).intValue() == LinkDirection.Downlink.getIntegerRepresentation())
            return new CDMADownlinkSystem(elem);
        else
            return new CDMAUplinkSystem(elem);
    }

    public static CDMASystem createCDMASystem(CDMASystem sys)
    {
        if(sys.getCDMALinkComponent() == LinkDirection.Downlink)
            return new CDMADownlinkSystem(sys);
        else
            return new CDMAUplinkSystem(sys);
    }

    public String toString()
    {
        return getReference();
    }

    protected abstract int findNonInterferedCapacity(InterferenceGenerator interferencegenerator, boolean flag);

    public abstract void balanceInterferedSystem();

    protected abstract UserTerminal newUserTerminal();

    protected abstract int internalPowerBalance();

    public abstract void balancePower();

    public abstract void calculateInterference();

    public double getCenterCellTx()
    {
        double result = 0.0D;
        for(int i = 0; i < typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite(); i++)
            result += cells[0][i].getCurrentChannelTransmitPower_dBm();

        return result;
    }

    public int getSimulatedUsersPerCell()
    {
        return usersPerCell;
    }

    public void resetSystem(boolean clearInterferers)
    {
        droppedUsers.clear();
        activeUsers.clear();
        inactiveUsers.clear();
        notActivatedUsers.clear();
        noLinkLevelFoundUsers.clear();
        if(clearInterferers)
            externalInterferers.clear();
        peakPowerConvergenceIterations = 0;
        useridcount = 0;
        inactiveUserCount = 0;
        int j = 0;
        for(int bStop = cells.length; j < bStop; j++)
        {
            for(int k = 0; k < typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite(); k++)
            {
                cells[j][k].resetCell();
                cells[j][k].setUplinkMode(cdmaLinkComponent.isUplink());
            }

        }

        setSnapshotShouldBeIgnored(false);
    }

    public void addExternalInterferer(CDMAInterferer i)
    {
        externalInterferers.add(i);
    }

    public int countTotalNumberOfUsers()
    {
        return countActiveUsers() + countDroppedUsers() + countNotActivatedUsers();
    }

    protected void calculateExternalInterference(CDMAElement elem)
    {
        double extIntUnw = 0.0D;
        double extIntBlo = 0.0D;
        int i = 0;
        for(int stop = externalInterferers.size(); i < stop; i++)
        {
            ((CDMAInterferer)externalInterferers.get(i)).setVictim(elem.getLocationX(), elem.getLocationY(), elem.getAntennaHeight(), elem.calculateAntennaGainTo(CDMALink.calculateKartesianAngle((CDMAElement)externalInterferers.get(i), elem), CDMALink.calculateElevation((CDMAElement)externalInterferers.get(i), elem)), elem.getFrequency(), elem.getReferenceBandwidth());
            extIntUnw += delogaritmize(((CDMAInterferer)externalInterferers.get(i)).calculateUnwantedEmission());
            extIntBlo += delogaritmize(((CDMAInterferer)externalInterferers.get(i)).calculateSelectivity());
        }

        elem.setExternalInterferenceUnwanted(10D * Math.log10(extIntUnw));
        elem.setExternalInterferenceBlocking(10D * Math.log10(extIntBlo));
    }

    protected void initializeUser(UserTerminal user)
    {
        user.setMobilitySpeed(mobileStationMobilityDistribution.trial());
        user.generateCDMALinks();
        user.selectActiveList(getHandoverMargin());
    }

    protected void positionUser(UserTerminal user)
    {
        double userAng = userAngle.trial();
        double userDist = ((double)getNumberOfTiers() + 0.5D) * getInterCellDistance() * userLocation.trial();
        double x = userDist * Mathematics.cosD(userAng);
        double y = userDist * Mathematics.sinD(userAng);
        user.setLocationX(locationX + x);
        user.setLocationY(locationY + y);
        for(int i = 0; i < cells.length; i++)
            if(cells[i][0].isInsideCell(user.getLocationX(), user.getLocationY()))
                return;

        positionUser(user);
    }

    public int getReferenceCellCapacity()
    {
        return referenceCell.countServedUsers();
    }

    public double getReferenceCellOutagePercentage()
    {
        return referenceCell.getOutagePercentage();
    }

    public void findNonInterferedCapacity(NonInterferedCapacityListener listener, InterferenceGenerator generator, boolean interferenceMode)
    {
        setNonInterferedCapacityListener(listener);
        if(simulateCapacity && !capacitySimulated)
        {
            fineTuning = false;
            capacitySimulated = false;
            setStopped(false);
            usersPerCell = external_usersPerCell;
            deltaN = external_deltaN;
            if(listener != null)
                if(isUplink())
                {
                    minTargetNoiseRise = getTargetNetworkNoiseRiseOverThermalNoise_dB() - getSystemTolerance();
                    maxTargetNoiseRise = getTargetNetworkNoiseRiseOverThermalNoise_dB();
                    listener.startingCapacityFinding(usersPerCell, deltaN, getSystemTolerance(), numberOfTrials, true, getTargetNetworkNoiseRiseOverThermalNoise_dB());
                } else
                {
                    listener.startingCapacityFinding(usersPerCell, deltaN, getSystemTolerance(), numberOfTrials, false, succesCriteria);
                }
            findNonInterferedCapacity(generator, interferenceMode);
            LOG.debug((new StringBuilder()).append("Initial Capacity found: ").append(usersPerCell).append(" users per cell").toString());
        } else
        if(!capacitySimulated)
            usersPerCell = external_usersPerCell;
    }

    protected void waitALittle()
    {
        if(activeUsers.size() % 10 == 0)
            try
            {
                Thread.sleep(1L);
            }
            catch(InterruptedException ex) { }
    }

    public int countDroppedUsers()
    {
        int dropped = 0;
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
                dropped += cells[i][j].countDroppedUsers();

        }

        return droppedUsers.size();
    }

    public int countActiveUsers()
    {
        return activeUsers.size() + inactiveUserCount;
    }

    public static final double fromdBm2Watt(double dbm)
    {
        if(last_dbm != dbm)
        {
            last_res = Math.pow(10D, (dbm - 30D) / 10D);
            last_dbm = dbm;
        }
        return last_res;
    }

    public static final double fromWatt2dBm(double watt)
    {
        if(watt == 0.0D)
            return -1000D;
        else
            return 10D * Math.log10(watt) + 30D;
    }

    public static final double delogaritmize(double value)
    {
        if(value == 0.0D)
        {
            LOG.warn("Delog - Returned -1000 instead of NaN");
            return -1000D;
        } else
        {
            return Math.pow(10D, value / 10D);
        }
    }

    public static final double round(double d)
    {
        if(d == 0.0D)
            return 0.0D;
        else
            return Math.rint(d * 1000D) / 1000D;
    }

    public static final transient double powerSummation(double powers[])
    {
        double total = 0.0D;
        double arr$[] = powers;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            double power = arr$[i$];
            if(power != 0.0D)
                total += Math.pow(10D, (power - 30D) / 10D);
        }

        return 10D * Math.log10(total) + 30D;
    }

    public int countNotActivatedUsers()
    {
        return notActivatedUsers.size();
    }

    public List getNotActivatedUsers()
    {
        return notActivatedUsers;
    }

    public void dropActiveUser(UserTerminal user)
    {
        user.dropCall();
        activeUsers.remove(user);
        droppedUsers.add(user);
    }

    public UserTerminal generateUserTerminal()
    {
        UserTerminal user = new UserTerminal(0.0D, 0.0D, this, useridcount++, trialUserTerminalAntennaGain(), trialUserTerminalAntennaHeight());
        user.setThermalNoise(getThermalNoiseInWatt());
        user.setUplinkMode(cdmaLinkComponent.isUplink());
        user.setMaxTxPower_dBm(mobileStationMaximumTransmitPower);
        user.setMinTxPower_dBm(mobileStationMinmumTransmitPower);
        user.setPowerControlStep(mobileStationPowerControlStep);
        return user;
    }

    public double getMaxTrafficChannelPowerInWatt()
    {
        double maxBroadcastPower = getBaseStationMaximumBroadcastPowerdBm();
        double maxPowerChannelFraction = getBaseStationMaxChannelPowerFraction();
        double value = fromdBm2Watt(maxBroadcastPower) * maxPowerChannelFraction;
        return value;
    }

    public double getMaxTrafficChannelPowerIndBm()
    {
        return fromWatt2dBm(getMaxTrafficChannelPowerInWatt());
    }

    public boolean trialVoiceActivity()
    {
        return VOICE_ACTIVITY_RANDOM.nextDouble() <= voiceActivityFactor;
    }

    public int getNumberOfNoLinkLevelDataUsers()
    {
        return noLinkLevelFoundUsers.size();
    }

    public void generateSystemCells()
    {
        double angle = 360 / cellStructure;
        cells = (CDMACell[][])new CDMACell[19][typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite()];
        boolean triSectorCells = typeOfCellsInPowerControlCluster == CellType.TriSectorAntenna;
        generateCells(cells[0], locationX, locationY, 0, triSectorCells);
        for(int i = 0; i < cells.length; i++)
            generateCells(cells[i], 0.0D, 0.0D, i, triSectorCells);

        repositionSystem(getLocationX(), getLocationX());
        try
        {
            referenceCell = cells[referenceCellId][0];
        }
        catch(Exception e) { }
    }

    protected void generateCells(CDMACell _cells[], double x, double y, int _cellid, boolean triSector)
    {
        for(int i = 0; i < _cells.length; i++)
        {
            if(_cells[i] != null)
                continue;
            if(triSector)
            {
                int cellid = _cellid * typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite() + i;
                _cells[i] = new CDMATriSectorCell(x, y, this, cellid, trialBaseStationAntennaHeight(), new Antenna(triSectorAntenna), i + 1);
            } else
            {
                _cells[i] = new CDMAOmniDirectionalCell(x, y, this, _cellid, trialBaseStationAntennaHeight(), new Antenna(omniDirectionalAntenna));
            }
            _cells[i].setCellLocationId(_cellid);
            _cells[i].setMaximumChannelPowerFraction(getBaseStationMaxChannelPowerFraction());
            _cells[i].setMaximumTransmitPower_dBm(getBaseStationMaximumBroadcastPowerdBm());
            _cells[i].setPilotFraction(getBaseStationPilotFraction());
            _cells[i].setOverheadFraction(getBaseStationOverheadFraction());
            _cells[i].initializeTransmitPowerLevels();
        }

    }

    public void addInitialUsers(int numberOfInitialUsers)
    {
        for(int i = 0; i < numberOfInitialUsers; i++)
            generateUserTerminal();

    }

    public CDMACell[][] getCDMACells()
    {
        return cells;
    }

    public void calculateThermalNoise()
    {
        double bandw = getSystemBandwidth();
        double recNoise = getReceiverNoiseFigure();
        double noise = -173.977D + 10D * Math.log10(bandw * 1000000D) + recNoise;
        setThermalNoise(fromdBm2Watt(noise));
    }

    public double calculateProcessingGain()
    {
        processingGain = (systemBandwidth / voiceBitRate) * 1000D;
        processingGain = 10D * Math.log10(processingGain);
        return processingGain;
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2)
    {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void translateCellCoordinates(double x, double y)
    {
        double diffX = (int)x - (int)locationX;
        double diffY = (int)y - (int)locationY;
        int i;
        for(i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
            {
                cells[i][j].translateLocationX(diffX);
                cells[i][j].translateLocationY(diffY);
            }

        }

        i = 0;
        for(int stop = activeUsers.size(); i < stop; i++)
        {
            ((UserTerminal)activeUsers.get(i)).translateLocationX(diffX);
            ((UserTerminal)activeUsers.get(i)).translateLocationY(diffY);
        }

        locationX = cells[0][0].getLocationX();
        locationY = cells[0][0].getLocationY();
    }

    public static int calculateNumberOfCells(int numberOfCellsInRing, int tiers)
    {
        if(tiers == 0)
            return 1;
        else
            return numberOfCellsInRing * tiers + calculateNumberOfCells(numberOfCellsInRing, tiers - 1);
    }

    public double getFadingEffect()
    {
        return 0.0D;
    }

    public String getStatusString()
    {
        StringBuffer str = new StringBuffer();
        str.append((new StringBuilder()).append(getReference()).append(" Contains: \n\t").append(cells.length * typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite()).append(" cells\n").toString());
        str.append((new StringBuilder()).append("\t").append(activeUsers.size()).append(" active users\n\t").toString());
        str.append((new StringBuilder()).append(inactiveUsers.size()).append(" inactive users\n\t").toString());
        str.append((new StringBuilder()).append(droppedUsers.size()).append(" dropped users\n").toString());
        return str.toString();
    }

    public int getUserIdCount()
    {
        return useridcount;
    }

    public double trialBaseStationAntennaHeight()
    {
        return baseStationAntennaHeight.trial();
    }

    public double trialUserTerminalAntennaHeight()
    {
        return mobileStationAntennaHeight.trial();
    }

    public double trialUserTerminalAntennaGain()
    {
        return mobileStationAntennaGain.trial();
    }

    public double minBaseStationAntennaHeight()
    {
        return baseStationAntennaHeight.getBounds().getMin();
    }

    public double minUserTerminalAntennaHeight()
    {
        return mobileStationAntennaHeight.getBounds().getMin();
    }

    public double minUserTerminalAntennaGain()
    {
        return mobileStationAntennaGain.getBounds().getMin();
    }

    public boolean isUplink()
    {
        return cdmaLinkComponent.isUplink();
    }

    public void updateTriSectorAntenna()
    {
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
                if(cells[i][j] != null)
                    cells[i][j].setAntenna(new Antenna(triSectorAntenna));

        }

    }

    public void updateOmniAntenna()
    {
        for(int i = 0; i < cells.length; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
                if(cells[i][j] != null)
                    cells[i][j].setAntenna(new Antenna(omniDirectionalAntenna));

        }

    }

    public List getCDMACellsAsList()
    {
        List list = new ArrayList();
        CDMACell arr$[][] = cells;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            CDMACell cs[] = arr$[i$];
            list.addAll(Arrays.asList(cs));
        }

        return list;
    }

    public void repositionSystem(double x, double y)
    {
        double d = getInterCellDistance();
        setLocationX(x);
        setLocationY(y);
        if(referenceCell == null)
            referenceCell = cells[0][0];
        switch(referenceCell.getCellLocationId())
        {
        case 0: // '\0'
            setLocationX(x);
            setLocationY(y);
            break;

        case 1: // '\001'
            setLocationX(x - (1.5D * d) / SQRT3);
            setLocationY(y - d / 2D);
            break;

        case 2: // '\002'
            setLocationX(x);
            setLocationY(y - d);
            break;

        case 3: // '\003'
            setLocationX(x + (1.5D * d) / SQRT3);
            setLocationY(y - d / 2D);
            break;

        case 4: // '\004'
            setLocationX(x + (1.5D * d) / SQRT3);
            setLocationY(y + d / 2D);
            break;

        case 5: // '\005'
            setLocationX(x);
            setLocationY(y + d);
            break;

        case 6: // '\006'
            setLocationX(x - (1.5D * d) / SQRT3);
            setLocationY(y + d / 2D);
            break;

        case 7: // '\007'
            setLocationX(x - (3D * d) / SQRT3);
            setLocationY(y);
            break;

        case 8: // '\b'
            setLocationX(x - (3D * d) / SQRT3);
            setLocationY(y - d);
            break;

        case 9: // '\t'
            setLocationX(x - (1.5D * d) / SQRT3);
            setLocationY(y - 1.5D * d);
            break;

        case 10: // '\n'
            setLocationX(x);
            setLocationY(y - 2D * d);
            break;

        case 11: // '\013'
            setLocationX(x + (1.5D * d) / SQRT3);
            setLocationY(y - 1.5D * d);
            break;

        case 12: // '\f'
            setLocationX(x + (3D * d) / SQRT3);
            setLocationY(y - d);
            break;

        case 13: // '\r'
            setLocationX(x + (3D * d) / SQRT3);
            setLocationY(y);
            break;

        case 14: // '\016'
            setLocationX(x + (3D * d) / SQRT3);
            setLocationY(y + d);
            break;

        case 15: // '\017'
            setLocationX(x + (1.5D * d) / SQRT3);
            setLocationY(y + 1.5D * d);
            break;

        case 16: // '\020'
            setLocationX(x);
            setLocationY(y + 2D * d);
            break;

        case 17: // '\021'
            setLocationX(x - (1.5D * d) / SQRT3);
            setLocationY(y + 1.5D * d);
            break;

        case 18: // '\022'
            setLocationX(x - (3D * d) / SQRT3);
            setLocationY(y + d);
            break;
        }
        x = getLocationX();
        y = getLocationY();
        for(int i = 0; i < cells[0].length; i++)
        {
            cells[0][i].setLocationX(x);
            cells[0][i].setLocationY(y);
        }

        for(int i = 0; i < cells[1].length; i++)
        {
            cells[1][i].setLocationX(x + (1.5D * d) / SQRT3);
            cells[1][i].setLocationY(y + d / 2D);
        }

        for(int i = 0; i < cells[2].length; i++)
        {
            cells[2][i].setLocationX(x);
            cells[2][i].setLocationY(y + d);
        }

        for(int i = 0; i < cells[3].length; i++)
        {
            cells[3][i].setLocationX(x - (1.5D * d) / SQRT3);
            cells[3][i].setLocationY(y + d / 2D);
        }

        for(int i = 0; i < cells[4].length; i++)
        {
            cells[4][i].setLocationX(x - (1.5D * d) / SQRT3);
            cells[4][i].setLocationY(y - d / 2D);
        }

        for(int i = 0; i < cells[5].length; i++)
        {
            cells[5][i].setLocationX(x);
            cells[5][i].setLocationY(y - d);
        }

        for(int i = 0; i < cells[6].length; i++)
        {
            cells[6][i].setLocationX(x + (1.5D * d) / SQRT3);
            cells[6][i].setLocationY(y - d / 2D);
        }

        for(int i = 0; i < cells[7].length; i++)
        {
            cells[7][i].setLocationX(x + (3D * d) / SQRT3);
            cells[7][i].setLocationY(y);
        }

        for(int i = 0; i < cells[8].length; i++)
        {
            cells[8][i].setLocationX(x + (3D * d) / SQRT3);
            cells[8][i].setLocationY(y + d);
        }

        for(int i = 0; i < cells[9].length; i++)
        {
            cells[9][i].setLocationX(x + (1.5D * d) / SQRT3);
            cells[9][i].setLocationY(y + 1.5D * d);
        }

        for(int i = 0; i < cells[10].length; i++)
        {
            cells[10][i].setLocationX(x);
            cells[10][i].setLocationY(y + 2D * d);
        }

        for(int i = 0; i < cells[11].length; i++)
        {
            cells[11][i].setLocationX(x - (1.5D * d) / SQRT3);
            cells[11][i].setLocationY(y + 1.5D * d);
        }

        for(int i = 0; i < cells[12].length; i++)
        {
            cells[12][i].setLocationX(x - (3D * d) / SQRT3);
            cells[12][i].setLocationY(y + d);
        }

        for(int i = 0; i < cells[13].length; i++)
        {
            cells[13][i].setLocationX(x - (3D * d) / SQRT3);
            cells[13][i].setLocationY(y);
        }

        for(int i = 0; i < cells[14].length; i++)
        {
            cells[14][i].setLocationX(x - (3D * d) / SQRT3);
            cells[14][i].setLocationY(y - d);
        }

        for(int i = 0; i < cells[15].length; i++)
        {
            cells[15][i].setLocationX(x - (1.5D * d) / SQRT3);
            cells[15][i].setLocationY(y - 1.5D * d);
        }

        for(int i = 0; i < cells[16].length; i++)
        {
            cells[16][i].setLocationX(x);
            cells[16][i].setLocationY(y - 2D * d);
        }

        for(int i = 0; i < cells[17].length; i++)
        {
            cells[17][i].setLocationX(x + (1.5D * d) / SQRT3);
            cells[17][i].setLocationY(y - 1.5D * d);
        }

        for(int i = 0; i < cells[18].length; i++)
        {
            cells[18][i].setLocationX(x + (3D * d) / SQRT3);
            cells[18][i].setLocationY(y - d);
        }

    }

    public double calculateMinimumTransmitPower(double maxTxPower, double powerControlRange)
    {
        mobileStationMinmumTransmitPower = maxTxPower - powerControlRange;
        return mobileStationMinmumTransmitPower;
    }

    public LinkDirection getCDMALinkComponent()
    {
        return cdmaLinkComponent;
    }

    public final double getReceiverNoiseFigure()
    {
        return receiverNoiseFigure;
    }

    public final double getHandoverMargin()
    {
        return handoverMargin;
    }

    public final double getCallDropThreshold()
    {
        return callDropThreshold;
    }

    public final double getPowerControlConvergenceThreshold()
    {
        return powerControlConvergenceThreshold;
    }

    public final double getVoiceBitRate()
    {
        return voiceBitRate;
    }

    public final double getSystemBandwidth()
    {
        return systemBandwidth;
    }

    public final double getVoiceActivityFactor()
    {
        return voiceActivityFactor;
    }

    public final int getNumberOfCellSitesInPowerControlCluster()
    {
        return numberOfCellSitesInPowerControlCluster;
    }

    public CellType getTypeOfCellsInPowerControlCluster()
    {
        return typeOfCellsInPowerControlCluster;
    }

    public final double getInterCellDistance()
    {
        return cellRadius * SQRT3;
    }

    public final Distribution getBaseStationAntennaHeight()
    {
        return baseStationAntennaHeight;
    }

    public final Distribution getMobileStationAntennaHeight()
    {
        return mobileStationAntennaHeight;
    }

    public final Distribution getMobileStationAntennaGain()
    {
        return mobileStationAntennaGain;
    }

    public final Distribution getMobileStationMobilityDistribution()
    {
        return mobileStationMobilityDistribution;
    }

    public final PropagationModel getPropagationModel()
    {
        return propagationModel;
    }

    public final double getBaseStationPilotFraction()
    {
        return baseStationPilotFraction;
    }

    public final double getBaseStationOverheadFraction()
    {
        return baseStationOverheadFraction;
    }

    public final double getBaseStationMaximumBroadcastPowerdBm()
    {
        return baseStationMaximumBroadcastPower;
    }

    public final double getBaseStationMaxChannelPowerFraction()
    {
        return baseStationMaxChannelPowerFraction;
    }

    public final CDMALinkLevelData getLinkLevelData()
    {
        return linkLevelData;
    }

    public final double getTargetNetworkNoiseRiseOverThermalNoise_dB()
    {
        return targetNetworkNoiseRiseOverThermalNoise;
    }

    public final double getMobileStationMaximumTransmitPower()
    {
        return mobileStationMaximumTransmitPower;
    }

    public final double getMobileStationPowerControlRange()
    {
        return mobileStationPowerControlRange;
    }

    public final double getMobileStationPowerControlStep()
    {
        return mobileStationPowerControlStep;
    }

    public int getCellStructure()
    {
        return cellStructure;
    }

    public double getDisplacementAngle()
    {
        return displacementAngle;
    }

    public double getLocationY()
    {
        return locationY;
    }

    public double getLocationX()
    {
        return locationX;
    }

    public int getNumberOfTiers()
    {
        return numberOfTiers;
    }

    public List getActiveUsers()
    {
        return activeUsers;
    }

    public double getThermalNoiseInWatt()
    {
        return thermalNoise;
    }

    public double getThermalNoiseIndB()
    {
        return fromWatt2dBm(thermalNoise);
    }

    public double getBaseStationPowerControlStep()
    {
        return baseStationPowerControlStep;
    }

    public List getDroppedUsers()
    {
        return droppedUsers;
    }

    public double getFrequency()
    {
        return frequency;
    }

    public double getInitialBaseStationBroadcastPower()
    {
        return initialBaseStationBroadcastPower;
    }

    public int getInactiveUserCount()
    {
        return inactiveUserCount;
    }

    public Random getFADING_EFFECT_RANDOM()
    {
        return FADING_EFFECT_RANDOM;
    }

    public void setCDMALinkComponent(LinkDirection _cdmaLinkComponent)
    {
        cdmaLinkComponent = _cdmaLinkComponent;
    }

    public final void setReceiverNoiseFigure(double receiverNoiseFigure)
    {
        this.receiverNoiseFigure = receiverNoiseFigure;
    }

    public final void setHandoverMargin(double handoverMargin)
    {
        this.handoverMargin = handoverMargin;
    }

    public final void setCallDropThreshold(double callDropThreshold)
    {
        this.callDropThreshold = callDropThreshold;
    }

    public final void setPowerControlConvergenceThreshold(double powerControlConvergenceThreshold)
    {
        this.powerControlConvergenceThreshold = powerControlConvergenceThreshold;
    }

    public final void setVoiceBitRate(double voiceBitRate)
    {
        this.voiceBitRate = voiceBitRate;
    }

    public final void setSystemBandwidth(double systemBandwidth)
    {
        this.systemBandwidth = systemBandwidth;
    }

    public final void setVoiceActivityFactor(double voiceActivityFactor)
    {
        this.voiceActivityFactor = voiceActivityFactor;
    }

    public final void setNumberOfCellSitesInPowerControlCluster(int numberOfCellSitesInPowerControlCluster)
    {
        this.numberOfCellSitesInPowerControlCluster = numberOfCellSitesInPowerControlCluster;
    }

    public final void setTypeOfCellsInPowerControlCluster(CellType _typeOfCellsInPowerControlCluster)
    {
        if(typeOfCellsInPowerControlCluster != _typeOfCellsInPowerControlCluster)
        {
            typeOfCellsInPowerControlCluster = _typeOfCellsInPowerControlCluster;
            generateSystemCells();
        }
        typeOfCellsInPowerControlCluster = _typeOfCellsInPowerControlCluster;
    }

    public final void setInterCellDistance(double interCellDistance)
    {
        this.interCellDistance = interCellDistance;
    }

    public final void setBaseStationAntennaHeight(Distribution baseStationAntennaHeight)
    {
        this.baseStationAntennaHeight = baseStationAntennaHeight;
    }

    public final void setMobileStationAntennaHeight(Distribution mobileStationAntennaHeight)
    {
        this.mobileStationAntennaHeight = mobileStationAntennaHeight;
    }

    public final void setMobileStationAntennaGain(Distribution mobileStationAntennaGain)
    {
        this.mobileStationAntennaGain = mobileStationAntennaGain;
    }

    public final void setMobileStationMobilityDistribution(Distribution mobileStationMobilityDistribution)
    {
        this.mobileStationMobilityDistribution = mobileStationMobilityDistribution;
    }

    public final void setPropagationModel(PropagationModel propagationModel)
    {
        this.propagationModel = propagationModel;
    }

    public final void setBaseStationPilotFraction(double baseStationPilotFraction)
    {
        this.baseStationPilotFraction = baseStationPilotFraction;
    }

    public final void setBaseStationOverheadFraction(double baseStationOverheadFraction)
    {
        this.baseStationOverheadFraction = baseStationOverheadFraction;
    }

    public final void setBaseStationMaximumBroadcastPower(double baseStationMaximumBroadcastPower)
    {
        this.baseStationMaximumBroadcastPower = baseStationMaximumBroadcastPower;
        if(cells != null)
            try
            {
                int j = 0;
                for(int bStop = cells.length; j < bStop; j++)
                {
                    for(int k = 0; k < typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite(); k++)
                    {
                        cells[j][k].setMaximumTransmitPower_dBm(baseStationMaximumBroadcastPower);
                        cells[j][k].resetCell();
                    }

                }

            }
            catch(Exception ex)
            {
                generateSystemCells();
            }
    }

    public final void setBaseStationMaxChannelPowerFraction(double baseStationMaxChannelPowerFraction)
    {
        this.baseStationMaxChannelPowerFraction = baseStationMaxChannelPowerFraction;
    }

    public final void setLinkLevelData(CDMALinkLevelData linkLevelData)
    {
        this.linkLevelData = linkLevelData;
    }

    public final void setTargetNetworkNoiseRiseOverThermalNoise(double targetNetworkNoiseRiseOverThermalNoise)
    {
        this.targetNetworkNoiseRiseOverThermalNoise = targetNetworkNoiseRiseOverThermalNoise;
    }

    public final void setMobileStationMaximumTransmitPower(double mobileStationMaximumTransmitPower)
    {
        this.mobileStationMaximumTransmitPower = mobileStationMaximumTransmitPower;
        calculateMinimumTransmitPower(mobileStationMaximumTransmitPower, mobileStationPowerControlRange);
    }

    public final void setMobileStationPowerControlRange(double mobileStationPowerControlRange)
    {
        this.mobileStationPowerControlRange = mobileStationPowerControlRange;
        calculateMinimumTransmitPower(mobileStationMaximumTransmitPower, mobileStationPowerControlRange);
    }

    public final void setMobileStationPowerControlStep(double mobileStationPowerControlStep)
    {
        this.mobileStationPowerControlStep = mobileStationPowerControlStep;
    }

    public void setCellStructure(int cellStructure)
    {
        this.cellStructure = cellStructure;
        setDisplacementAngle(360 / cellStructure / 2);
    }

    public void setDisplacementAngle(double displacementAngle)
    {
        this.displacementAngle = displacementAngle;
    }

    public void setLocation(double x, double y)
    {
        setLocationX(x);
        setLocationY(y);
    }

    public void setLocationY(double locationY)
    {
        this.locationY = locationY;
    }

    public void setLocationX(double locationX)
    {
        this.locationX = locationX;
    }

    public void setNumberOfTiers(int numberOfTiers)
    {
        this.numberOfTiers = numberOfTiers;
        setNumberOfCellSitesInPowerControlCluster(calculateNumberOfCells(getCellStructure(), getNumberOfTiers()));
    }

    public void setThermalNoise(double thermalNoise)
    {
        this.thermalNoise = thermalNoise;
    }

    public void setBaseStationPowerControlStep(double baseStationPowerControlStep)
    {
        this.baseStationPowerControlStep = baseStationPowerControlStep;
    }

    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }

    public void setInitialBaseStationBroadcastPower(double initialBaseStationBroadcastPower)
    {
        this.initialBaseStationBroadcastPower = initialBaseStationBroadcastPower;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("CdmaSystem");
        element.setAttribute("linkComponentDownlink", Integer.toString(cdmaLinkComponent.getIntegerRepresentation()));
        element.setAttribute("receiverNoiseFigure", Double.toString(receiverNoiseFigure));
        element.setAttribute("handover", Double.toString(handoverMargin));
        element.setAttribute("callDropThreshold", Double.toString(callDropThreshold));
        element.setAttribute("successThreshold", Double.toString(successThreshold));
        element.setAttribute("powerControlThreshold", Double.toString(powerControlConvergenceThreshold));
        element.setAttribute("voiceBitrate", Double.toString(voiceBitRate));
        element.setAttribute("systemBandwith", Double.toString(systemBandwidth));
        element.setAttribute("cellStructure", Integer.toString(cellStructure));
        element.setAttribute("number_of_tiers", Integer.toString(numberOfTiers));
        element.setAttribute("displacementAngle", Double.toString(displacementAngle));
        element.setAttribute("location_x", Double.toString(locationX));
        element.setAttribute("location_y", Double.toString(locationY));
        element.setAttribute("intercell_distance", Double.toString(interCellDistance));
        element.setAttribute("cell_radius", Double.toString(cellRadius));
        element.setAttribute("voice_activity_factor", Double.toString(voiceActivityFactor));
        element.setAttribute("cellType", Integer.toString(typeOfCellsInPowerControlCluster.getNumberOfCellsPerSite()));
        element.setAttribute("referenceSector", Integer.toString(triSectorReferenceCellSelection));
        element.setAttribute("number_of_cells", Integer.toString(numberOfCellSitesInPowerControlCluster));
        element.setAttribute("target_noise", Double.toString(targetNetworkNoiseRiseOverThermalNoise));
        element.setAttribute("frequency", Double.toString(frequency));
        element.setAttribute("reference_cell_id", Integer.toString(referenceCellId));
        element.setAttribute("users_per_cell", Integer.toString(getUsersPerCell()));
        element.setAttribute("delta_users_per_cell", Integer.toString(getDeltaN()));
        element.setAttribute("number_of_trials", Integer.toString(numberOfTrials));
        element.setAttribute("allowable-initial-outage", Double.toString(systemTolerance));
        element.setAttribute("simulate_capacity", Boolean.toString(simulateCapacity));
        element.setAttribute("simulate_network_edge", Boolean.toString(simulateNetworkEdge));
        element.setAttribute("simulate_left_network_edge", Boolean.toString(networkEdgeLeftSide));
        element.setAttribute("interference_from_cluster", Boolean.toString(measureInterferenceFromCluster));
        element.setAttribute("is_victim_system", Boolean.toString(victimSystem));
        element.setAttribute("minimum_coupling_loss", Double.toString(minimumCouplingLoss));
        Element bsElement = doc.createElement("BaseStation");
        bsElement.setAttribute("pilotFraction", Double.toString(baseStationPilotFraction));
        bsElement.setAttribute("overheadFraction", Double.toString(baseStationOverheadFraction));
        bsElement.setAttribute("maxPowerFraction", Double.toString(baseStationMaxChannelPowerFraction));
        bsElement.setAttribute("maxPower", Double.toString(baseStationMaximumBroadcastPower));
        bsElement.setAttribute("powerStep", Double.toString(baseStationPowerControlStep));
        bsElement.setAttribute("initialPower", Double.toString(initialBaseStationBroadcastPower));
        Element bsAntHeight = doc.createElement("antenna-height");
        bsAntHeight.appendChild(baseStationAntennaHeight.toElement(doc));
        bsElement.appendChild(bsAntHeight);
        Element bsAnt = doc.createElement("Tri-Sector-Antenna");
        bsAnt.appendChild(triSectorAntenna.toElement(doc));
        bsElement.appendChild(bsAnt);
        Element bsOAnt = doc.createElement("Omni-Antenna");
        bsOAnt.appendChild(omniDirectionalAntenna.toElement(doc));
        bsElement.appendChild(bsOAnt);
        element.appendChild(bsElement);
        Element msElement = doc.createElement("MobileStation");
        msElement.setAttribute("powerRange", Double.toString(mobileStationPowerControlRange));
        msElement.setAttribute("maxPower", Double.toString(mobileStationMaximumTransmitPower));
        msElement.setAttribute("powerStep", Double.toString(mobileStationPowerControlStep));
        Element msAntHeight = doc.createElement("antenna-height");
        msAntHeight.appendChild(mobileStationAntennaHeight.toElement(doc));
        msElement.appendChild(msAntHeight);
        Element msAntGain = doc.createElement("AntennaGain");
        msAntGain.appendChild(mobileStationAntennaGain.toElement(doc));
        msElement.appendChild(msAntGain);
        Element msMobility = doc.createElement("UserMobility");
        msMobility.appendChild(mobileStationMobilityDistribution.toElement(doc));
        msElement.appendChild(msMobility);
        element.appendChild(msElement);
        Element userDist = doc.createElement("UserDistance");
        userDist.appendChild(userLocation.toElement(doc));
        element.appendChild(userDist);
        Element userAng = doc.createElement("UserAngle");
        userAng.appendChild(userAngle.toElement(doc));
        element.appendChild(userAng);
        Element prop = doc.createElement("PropagationModel");
        prop.appendChild(propagationModel.toElement(doc));
        element.appendChild(prop);
        if(linkLevelData != null)
            element.appendChild(linkLevelData.toElement(doc));
        return element;
    }

    public int getRowCount()
    {
        if(nodeAttributes == null)
            initNodeAttributes();
        return nodeAttributes.length;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Receiver Noise Figure", "dB", Double.valueOf(getReceiverNoiseFigure()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Handover Margin", "dB", Double.valueOf(getHandoverMargin()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Call Drop threshold", "dB", Double.valueOf(getCallDropThreshold()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Power Control Convergence Threshold", "dB", Double.valueOf(getPowerControlConvergenceThreshold()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Voice Bit Rate", "kbps", Double.valueOf(getVoiceBitRate()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("System Bandwidth", "MHz", Double.valueOf(getSystemBandwidth()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Voice Activity Factor", "", Double.valueOf(getVoiceActivityFactor()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Cell Radius", "km", Double.valueOf(getCellRadius()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("BS Antenna Height", "m", getBaseStationAntennaHeight(), "Distribution", null, false, true, null));
        nodeList.add(new NodeAttribute("BS Omni Antenna Gain", "dB", Double.valueOf(getOmniDirectionalAntenna().getPeakGain()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("MS Antenna Height", "m", getMobileStationAntennaHeight(), "Distribution", null, false, true, null));
        nodeList.add(new NodeAttribute("MS Antenna Gain", "dB", getMobileStationAntennaGain(), "Distribution", null, false, true, null));
        nodeList.add(new NodeAttribute("BS Pilot fraction", "", Double.valueOf(getBaseStationPilotFraction()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("BS Overhead fraction", "", Double.valueOf(getBaseStationOverheadFraction()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("BS Max channel fraction", "", Double.valueOf(getBaseStationMaxChannelPowerFraction()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("BS Max broadcast power", "dBm", Double.valueOf(getBaseStationMaximumBroadcastPowerdBm()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Target Noise Rise", "dB", Double.valueOf(getTargetNetworkNoiseRiseOverThermalNoise_dB()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("MS Max transmit power", "dBm", Double.valueOf(getMobileStationMaximumTransmitPower()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("MS Power Control range", "dB", Double.valueOf(getMobileStationPowerControlRange()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Minimum Coupling Loss", "dB", Double.valueOf(getMinimumCouplingLoss()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Users per Cell", "", Integer.valueOf(getUsersPerCell()), "Integer", null, false, true, null));
        nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
        switch(rowIndex)
        {
        case 0: // '\0'
            setReceiverNoiseFigure(((Number)aValue).doubleValue());
            break;

        case 1: // '\001'
            setHandoverMargin(((Number)aValue).doubleValue());
            break;

        case 2: // '\002'
            setCallDropThreshold(((Number)aValue).doubleValue());
            break;

        case 3: // '\003'
            setPowerControlConvergenceThreshold(((Number)aValue).doubleValue());
            break;

        case 4: // '\004'
            setVoiceBitRate(((Number)aValue).doubleValue());
            break;

        case 5: // '\005'
            setSystemBandwidth(((Number)aValue).doubleValue());
            break;

        case 6: // '\006'
            setVoiceActivityFactor(((Number)aValue).doubleValue());
            break;

        case 7: // '\007'
            setCellRadius(((Number)aValue).doubleValue());
            break;

        case 8: // '\b'
            setBaseStationAntennaHeight((Distribution)aValue);
            break;

        case 9: // '\t'
            omniDirectionalAntenna.setPeakGain(((Number)aValue).doubleValue());
            break;

        case 10: // '\n'
            setMobileStationAntennaHeight((Distribution)aValue);
            break;

        case 11: // '\013'
            setMobileStationAntennaGain((Distribution)aValue);
            break;

        case 12: // '\f'
            setBaseStationPilotFraction(((Number)aValue).doubleValue());
            break;

        case 13: // '\r'
            setBaseStationOverheadFraction(((Number)aValue).doubleValue());
            break;

        case 14: // '\016'
            setBaseStationMaxChannelPowerFraction(((Number)aValue).doubleValue());
            break;

        case 15: // '\017'
            setBaseStationMaximumBroadcastPower(((Number)aValue).doubleValue());
            break;

        case 16: // '\020'
            setTargetNetworkNoiseRiseOverThermalNoise(((Number)aValue).doubleValue());
            break;

        case 17: // '\021'
            setMobileStationMaximumTransmitPower(((Number)aValue).doubleValue());
            break;

        case 18: // '\022'
            setMobileStationPowerControlRange(((Number)aValue).doubleValue());
            break;

        case 19: // '\023'
            setMinimumCouplingLoss(((Number)aValue).doubleValue());
            break;

        case 20: // '\024'
            setUsersPerCell(((Number)aValue).intValue());
            break;
        }
    }

    public List getExternalInterferers()
    {
        return externalInterferers;
    }

    public double getProcessingGain()
    {
        return processingGain;
    }

    public void setProcessingGain(double processingGain)
    {
        this.processingGain = processingGain;
    }

    public double getTargetOutagePercentage()
    {
        return targetOutagePercentage;
    }

    public void setTargetOutagePercentage(double targetOutagePercentage)
    {
        this.targetOutagePercentage = targetOutagePercentage;
    }

    public double getMinimumCouplingLoss()
    {
        return minimumCouplingLoss;
    }

    public void setMinimumCouplingLoss(double minimumCouplingLoss)
    {
        this.minimumCouplingLoss = minimumCouplingLoss;
    }

    public int getDeltaN()
    {
        return external_deltaN;
    }

    public void setDeltaN(int deltaN)
    {
        external_deltaN = deltaN;
    }

    public int getNonInterferedCapacity()
    {
        return nonInterferedCapacity;
    }

    public void setNonInterferedCapacity(int nonInterferedCapacity)
    {
        this.nonInterferedCapacity = nonInterferedCapacity;
    }

    public int getNumberOfTrials()
    {
        return numberOfTrials;
    }

    public void setNumberOfTrials(int numberOfTrials)
    {
        this.numberOfTrials = numberOfTrials;
    }

    public double getSuccesCriteria()
    {
        return succesCriteria;
    }

    public void setSuccesCriteria(double succesCriteria)
    {
        this.succesCriteria = succesCriteria;
    }

    public double getSuccesRate()
    {
        return succesRate;
    }

    public void setSuccesRate(double succesRate)
    {
        this.succesRate = succesRate;
    }

    public int getUsersPerCell()
    {
        return external_usersPerCell;
    }

    public void setUsersPerCell(int usersPerCell)
    {
        external_usersPerCell = usersPerCell;
    }

    public boolean isSimulateCapacity()
    {
        return simulateCapacity;
    }

    public void setSimulateCapacity(boolean simulateCapacity)
    {
        this.simulateCapacity = simulateCapacity;
    }

    public double getAvgNoiseRise()
    {
        return meanNoiseRiseOverTrials;
    }

    public double getCellRadius()
    {
        return cellRadius;
    }

    public void setCellRadius(double cellRadius)
    {
        this.cellRadius = cellRadius;
    }

    public CDMACell getReferenceCell()
    {
        if(referenceCell == null)
            referenceCell = cells[0][triSectorReferenceCellSelection];
        return referenceCell;
    }

    public void setReferenceCellId(int id)
    {
        referenceCellId = id;
        referenceCell = cells[referenceCellId][triSectorReferenceCellSelection];
    }

    public boolean isSimulateNetworkEdge()
    {
        return simulateNetworkEdge;
    }

    public void setSimulateNetworkEdge(boolean simulateNetworkEdge)
    {
        this.simulateNetworkEdge = simulateNetworkEdge;
    }

    public Antenna getTriSectorAntenna()
    {
        return triSectorAntenna;
    }

    public void setTriSectorAntenna(Antenna triSectorAntenna)
    {
        this.triSectorAntenna = triSectorAntenna;
    }

    protected void configureDefaultTriSectorAntenna()
    {
        triSectorAntenna.setReference("Default Tri-Sector Antenna");
        triSectorAntenna.setDescription("3GPP 3-sector");
        triSectorAntenna.setPeakGain(15D);
        triSectorAntenna.setUseHorizontalPattern(true);
        DiscreteFunction func = triSectorAntenna.getHorizontalPattern().getPattern();
        func.addPoint(new Point2D(-180D, -23.18D));
        func.addPoint(new Point2D(-170D, -24.27D));
        func.addPoint(new Point2D(-160D, -23D));
        func.addPoint(new Point2D(-150D, -20.364000000000001D));
        func.addPoint(new Point2D(-140D, -18.640000000000001D));
        func.addPoint(new Point2D(-130D, -17.359999999999999D));
        func.addPoint(new Point2D(-120D, -15.550000000000001D));
        func.addPoint(new Point2D(-110D, -13.73D));
        func.addPoint(new Point2D(-100D, -11.359999999999999D));
        func.addPoint(new Point2D(-90D, -9.3599999999999994D));
        func.addPoint(new Point2D(-80D, -7.1799999999999997D));
        func.addPoint(new Point2D(-70D, -5.2699999999999996D));
        func.addPoint(new Point2D(-60D, -3.8199999999999998D));
        func.addPoint(new Point2D(-50D, -2.73D));
        func.addPoint(new Point2D(-40D, -1.3700000000000001D));
        func.addPoint(new Point2D(-30D, -0.36399999999999999D));
        func.addPoint(new Point2D(-20D, -0.182D));
        func.addPoint(new Point2D(-10D, 0.0D));
        func.addPoint(new Point2D(0.0D, 0.0D));
        func.addPoint(new Point2D(10D, 0.0D));
        func.addPoint(new Point2D(20D, -0.182D));
        func.addPoint(new Point2D(30D, -0.36399999999999999D));
        func.addPoint(new Point2D(40D, -1.3700000000000001D));
        func.addPoint(new Point2D(50D, -2.73D));
        func.addPoint(new Point2D(60D, -3.8199999999999998D));
        func.addPoint(new Point2D(70D, -5.2699999999999996D));
        func.addPoint(new Point2D(80D, -7.1799999999999997D));
        func.addPoint(new Point2D(90D, -9.3599999999999994D));
        func.addPoint(new Point2D(100D, -11.359999999999999D));
        func.addPoint(new Point2D(110D, -13.73D));
        func.addPoint(new Point2D(120D, -15.550000000000001D));
        func.addPoint(new Point2D(130D, -17.359999999999999D));
        func.addPoint(new Point2D(140D, -18.640000000000001D));
        func.addPoint(new Point2D(150D, -20.364000000000001D));
        func.addPoint(new Point2D(160D, -23D));
        func.addPoint(new Point2D(170D, -24.27D));
        func.addPoint(new Point2D(180D, -23.18D));
        triSectorAntenna.getHorizontalPattern().setRangeMin(-180D);
        triSectorAntenna.getHorizontalPattern().setRangeMax(180D);
    }

    public NonInterferedCapacityListener getNonInterferedCapacityListener()
    {
        return nonInterferedCapacityListener;
    }

    public void setNonInterferedCapacityListener(NonInterferedCapacityListener nonInterferedCapacityListener)
    {
        this.nonInterferedCapacityListener = nonInterferedCapacityListener;
    }

    public int getTriSectorReferenceCellSelection()
    {
        return triSectorReferenceCellSelection;
    }

    public void setTriSectorReferenceCellSelection(int triSectorReferenceCellSelection)
    {
        this.triSectorReferenceCellSelection = triSectorReferenceCellSelection;
    }

    public void addPowerBalancingListener(CDMABalancingListener _listener)
    {
        listeners.add(_listener);
    }

    public void removePowerBalancingListener(CDMABalancingListener _listener)
    {
        listeners.remove(_listener);
    }

    public void notifyBalancingStarted()
    {
        int i = 0;
        for(int stop = listeners.size(); i < stop; i++)
            ((CDMABalancingListener)listeners.get(i)).balancingStarted();

    }

    public void notifyBalancingComplete()
    {
        for(int i = 0; i < listeners.size(); i++)
            ((CDMABalancingListener)listeners.get(i)).balancingComplete();

    }

    public void notifyUserAdded(UserTerminal user)
    {
        int i = 0;
        for(int stop = listeners.size(); i < stop; i++)
            ((CDMABalancingListener)listeners.get(i)).voiceActiveUserAdded(user);

    }

    public void notifyUserDropped(UserTerminal user)
    {
        int i = 0;
        for(int stop = listeners.size(); i < stop; i++)
            ((CDMABalancingListener)listeners.get(i)).voiceActiveUserDropped(user);

    }

    public void notifyUserNotActivated(UserTerminal user)
    {
        int i = 0;
        for(int stop = listeners.size(); i < stop; i++)
            ((CDMABalancingListener)listeners.get(i)).voiceActiveUserNotActivated(user);

    }

    public void notifyInActiveUserAdded()
    {
        int i = 0;
        for(int stop = listeners.size(); i < stop; i++)
            ((CDMABalancingListener)listeners.get(i)).voiceInActiveUserAdded();

    }

    public void notifyUserIgnored()
    {
        int i = 0;
        for(int stop = listeners.size(); i < stop; i++)
            ((CDMABalancingListener)listeners.get(i)).voiceActiveUserIgnored();

    }

    public boolean isNetworkEdgeLeftSide()
    {
        return networkEdgeLeftSide;
    }

    public void setNetworkEdgeLeftSide(boolean networkEdgeLeftSide)
    {
        this.networkEdgeLeftSide = networkEdgeLeftSide;
    }

    public double getSuccessThreshold()
    {
        return successThreshold;
    }

    public void setSuccessThreshold(double successThreshold)
    {
        this.successThreshold = successThreshold;
    }

    public double getSystemTolerance()
    {
        return systemTolerance;
    }

    public void setSystemTolerance(double allowableInitialOutage)
    {
        systemTolerance = allowableInitialOutage;
    }

    public boolean isCapacitySimulated()
    {
        return capacitySimulated;
    }

    public void setCapacitySimulated(boolean capacitySimulated)
    {
        this.capacitySimulated = capacitySimulated;
    }

    public int getMaximumPeakCount()
    {
        return maximumPeakCount;
    }

    public void setMaximumPeakCount(int maximumPeakCount)
    {
        this.maximumPeakCount = maximumPeakCount;
    }

    public Antenna getOmniDirectionalAntenna()
    {
        return omniDirectionalAntenna;
    }

    public void setOmniDirectionalAntenna(Antenna omniDirectionalAntenna)
    {
        this.omniDirectionalAntenna = omniDirectionalAntenna;
    }

    public int getInternalUserPerCell()
    {
        return usersPerCell;
    }

    public boolean isMeasureInterferenceFromCluster()
    {
        return measureInterferenceFromCluster;
    }

    public void setMeasureInterferenceFromCluster(boolean measureInterferenceFromCluster)
    {
        this.measureInterferenceFromCluster = measureInterferenceFromCluster;
    }

    public boolean isVictimSystem()
    {
        return victimSystem;
    }

    public void setVictimSystem(boolean victimSystem)
    {
        this.victimSystem = victimSystem;
    }

    public int getDroppedBeforeInterference()
    {
        return droppedBeforeInterference;
    }

    public void setDroppedBeforeInterference(int droppedBeforeInterference)
    {
        this.droppedBeforeInterference = droppedBeforeInterference;
    }

    public boolean isStopped()
    {
        return stopped;
    }

    public void setStopped(boolean stopped)
    {
        this.stopped = stopped;
    }

    public double getAverageNoiseRise()
    {
        return averageNoiseRise;
    }

    public void setAverageNoiseRise(double averageNoiseRise)
    {
        this.averageNoiseRise = averageNoiseRise;
    }

    public boolean isSnapshotShouldBeIgnored()
    {
        return snapshotShouldBeIgnored;
    }

    public void setSnapshotShouldBeIgnored(boolean snapshotShouldBeIgnored)
    {
        this.snapshotShouldBeIgnored = snapshotShouldBeIgnored;
    }

    public int getHighestPcLoopCount()
    {
        return highestPcLoopCount;
    }

    private static Logger LOG = Logger.getLogger(org/seamcat/cdma/CDMASystem);
    protected static final ResourceBundle STRINGLIST;
    public static final double SQRT3 = Math.sqrt(3D);
    public static final double SQRT2 = Math.sqrt(2D);
    public static final int HEXAGONAL = 6;
    protected LinkDirection cdmaLinkComponent;
    protected double receiverNoiseFigure;
    protected double handoverMargin;
    protected double callDropThreshold;
    protected double successThreshold;
    protected double powerControlConvergenceThreshold;
    protected double voiceBitRate;
    protected double frequency;
    protected double systemBandwidth;
    protected double voiceActivityFactor;
    protected int numberOfCellSitesInPowerControlCluster;
    protected CellType typeOfCellsInPowerControlCluster;
    protected double cellRadius;
    protected double interCellDistance;
    protected Distribution baseStationAntennaHeight;
    protected Antenna omniDirectionalAntenna;
    protected Antenna triSectorAntenna;
    protected Distribution mobileStationAntennaHeight;
    protected Distribution mobileStationAntennaGain;
    protected Distribution mobileStationMobilityDistribution;
    protected PropagationModel propagationModel;
    protected double baseStationPilotFraction;
    protected double baseStationOverheadFraction;
    protected double baseStationMaximumBroadcastPower;
    protected double baseStationMaxChannelPowerFraction;
    protected double baseStationPowerControlStep;
    protected CDMALinkLevelData linkLevelData;
    protected double targetNetworkNoiseRiseOverThermalNoise;
    protected double targetOutagePercentage;
    protected double mobileStationMaximumTransmitPower;
    protected double mobileStationPowerControlRange;
    protected double mobileStationMinmumTransmitPower;
    protected double mobileStationPowerControlStep;
    protected int cellStructure;
    protected int numberOfTiers;
    protected double displacementAngle;
    protected double thermalNoise;
    protected double processingGain;
    protected double initialBaseStationBroadcastPower;
    protected double locationX;
    protected double locationY;
    protected double minimumCouplingLoss;
    protected int useridcount;
    protected double sigmaShadow;
    protected static final Random FADING_EFFECT_RANDOM = new Random(System.currentTimeMillis());
    protected CDMACell cells[][];
    public Distribution userLocation;
    public Distribution userAngle;
    protected List activeUsers;
    protected List inactiveUsers;
    protected List droppedUsers;
    protected List notActivatedUsers;
    protected List noLinkLevelFoundUsers;
    protected List externalInterferers;
    protected List listeners;
    protected int inactiveUserCount;
    protected CDMACell referenceCell;
    protected static final Random VOICE_ACTIVITY_RANDOM = new Random();
    protected static final int DEFAULT_INTERCELL_DISTANCE = 5;
    protected int peakPowerConvergenceIterations;
    public static final double DEFAULT_DELOG = -1000D;
    protected NonInterferedCapacityListener nonInterferedCapacityListener;
    protected int triSectorReferenceCellSelection;
    protected int usersPerCell;
    protected int deltaN;
    protected int numberOfTrials;
    protected double systemTolerance;
    protected int external_usersPerCell;
    protected int external_deltaN;
    protected int maximumPeakCount;
    protected double succesCriteria;
    public boolean simulateCapacity;
    protected boolean capacitySimulated;
    protected int nonInterferedCapacity;
    protected double succesRate;
    protected double meanNoiseRiseOverTrials;
    protected double minTargetNoiseRise;
    protected double maxTargetNoiseRise;
    protected double averageNoiseRise;
    protected boolean fineTuning;
    protected boolean simulateNetworkEdge;
    protected boolean networkEdgeLeftSide;
    protected boolean victimSystem;
    protected int referenceCellId;
    protected boolean measureInterferenceFromCluster;
    protected int droppedBeforeInterference;
    protected boolean stopped;
    protected boolean snapshotShouldBeIgnored;
    protected boolean useSmartCapacityFindingAlgorithm;
    protected int highestPcLoopCount;
    protected boolean finalFineTuning;
    private static double last_dbm;
    private static double last_res;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}