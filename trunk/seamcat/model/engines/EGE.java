// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EGE.java

package org.seamcat.model.engines;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import org.apache.log4j.*;
import org.seamcat.cdma.*;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.*;
import org.seamcat.model.*;
import org.seamcat.model.core.*;
import org.seamcat.model.datatypes.*;
import org.seamcat.model.plugin.*;
import org.seamcat.model.technical.exception.*;
import org.seamcat.postprocessing.*;
import org.seamcat.presentation.MainWindow;
import org.seamcat.presentation.SeamcatDistributionPlot;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.model.engines:
//            CdmaEngine, EventCompletionListener, EgeExceptionListener, PositionListener

public class EGE
    implements Runnable, NonInterferedCapacityListener
{

    public void setNumberOfEvents(int numberOfEvents)
    {
        this.numberOfEvents = numberOfEvents;
    }

    public int getNumberOfEvents()
    {
        return numberOfEvents;
    }

    public void reset()
    {
        totalNumberOfEventsGenerated = 0;
        eventCompletionListeners.clear();
        positionListeners.clear();
        exceptionListeners.clear();
        initialized = false;
    }

    public EGE(Workspace _workspace)
    {
        initialized = false;
        generateEvents = true;
        totalNumberOfEventsGenerated = 0;
        timeLimited = false;
        stoppedByTimeLimit = false;
        useHigherPriorityThreads = false;
        inServerMode = false;
        useTestCDMAAlgorithm = false;
        workspace = _workspace;
        eventCompletionListeners = new ArrayList();
        positionListeners = new ArrayList();
        exceptionListeners = new ArrayList();
    }

    public void addEventCompletionListerner(EventCompletionListener ecl)
    {
        LOG.debug((new StringBuilder()).append("Adding EventCompletionListener: ").append(ecl.getClass().getName()).toString());
        if(!eventCompletionListeners.contains(ecl))
            eventCompletionListeners.add(ecl);
    }

    public void removeEventCompletionListerner(EventCompletionListener ecl)
    {
        LOG.debug((new StringBuilder()).append("Removing EventCompletionListener: ").append(ecl.getClass().getName()).toString());
        eventCompletionListeners.remove(ecl);
    }

    public void addPositionListerner(PositionListener ecl)
    {
        LOG.debug((new StringBuilder()).append("Adding PositionListener: ").append(ecl.getClass().getName()).toString());
        if(!positionListeners.contains(ecl))
            positionListeners.add(ecl);
    }

    public void removePositionListerner(PositionListener ecl)
    {
        LOG.debug((new StringBuilder()).append("Removing PositionListener: ").append(ecl.getClass().getName()).toString());
        positionListeners.remove(ecl);
    }

    public void addExceptionListerner(EgeExceptionListener ecl)
    {
        LOG.debug((new StringBuilder()).append("Adding ExceptionListener: ").append(ecl.getClass().getName()).toString());
        if(!exceptionListeners.contains(ecl))
            exceptionListeners.add(ecl);
    }

    public void removeExceptionListerner(EgeExceptionListener ecl)
    {
        LOG.debug((new StringBuilder()).append("Removing ExceptionListener: ").append(ecl.getClass().getName()).toString());
        exceptionListeners.remove(ecl);
    }

    public void startGeneration(EventCompletionListener l)
        throws GeometricException, ModelException
    {
        addEventCompletionListerner(l);
        startGeneration();
        removeEventCompletionListerner(l);
    }

    public void ensureCapacities(int capacity)
    {
        LOG.debug("Ensuring capacities of event vectors");
        notifyListenersSetCurrentProcessCompletionPercentage(0);
        if(workspace.getVictimSystemLink().isCDMASystem())
        {
            workspace.getCdmaResults().getCdmaInitialCapacity().setShouldBeCalculated(true);
            workspace.getCdmaResults().getCdmaInitialCapacity().reserve(capacity);
            workspace.getCdmaResults().getCdmaInterferedCapacity().setShouldBeCalculated(true);
            workspace.getCdmaResults().getCdmaInterferedCapacity().reserve(capacity);
            workspace.getCdmaResults().getCdmaInitialOutage().setShouldBeCalculated(true);
            workspace.getCdmaResults().getCdmaInitialOutage().reserve(capacity);
            workspace.getCdmaResults().getCdmaInterferedOutage().setShouldBeCalculated(true);
            workspace.getCdmaResults().getCdmaInterferedOutage().reserve(capacity);
            workspace.getCdmaResults().getCdmaTotalDroppedUsers().setShouldBeCalculated(true);
            workspace.getCdmaResults().getCdmaTotalDroppedUsers().reserve(capacity);
        } else
        {
            workspace.getSignals().getDRSSVector().getEventVector().reserve(capacity);
        }
        notifyListenersUpdateStatus("Allocating memory for Unwanted Emission EventVectors");
        List v = workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors();
        int completionFactor = 25 / v.size();
        int updateFactor = 1;
        if(completionFactor < 1)
        {
            completionFactor = 1;
            updateFactor = v.size() / 25;
        }
        int i = 0;
        for(int stop = v.size(); i < stop; i++)
        {
            ((IRSSVector)v.get(i)).getEventVector().reserve(capacity);
            if(i % updateFactor == 0)
                notifyListenersIncrementCurrentProcessCompletionPercentage(completionFactor);
        }

        notifyListenersSetCurrentProcessCompletionPercentage(25);
        notifyListenersUpdateStatus("Allocating memory for Blocking EventVectors");
        v = workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors();
        completionFactor = 25 / v.size();
        updateFactor = 1;
        if(completionFactor < 1)
        {
            completionFactor = 1;
            updateFactor = v.size() / 25;
        }
        i = 0;
        for(int stop = v.size(); i < stop; i++)
        {
            ((IRSSVector)v.get(i)).getEventVector().reserve(capacity);
            if(i % updateFactor == 0)
                notifyListenersIncrementCurrentProcessCompletionPercentage(completionFactor);
        }

        notifyListenersSetCurrentProcessCompletionPercentage(50);
        notifyListenersUpdateStatus("Allocating memory for Intermodulation EventVectors");
        v = workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors();
        completionFactor = 25 / v.size();
        updateFactor = 1;
        if(completionFactor < 1)
        {
            completionFactor = 1;
            updateFactor = v.size() / 25;
        }
        i = 0;
        for(int stop = v.size(); i < stop; i++)
        {
            if(!((IRSSVector)v.get(i)).getEventVector().isShouldBeCalculated())
                continue;
            ((IRSSVector)v.get(i)).getEventVector().reserve(capacity);
            if(i % updateFactor == 0)
                notifyListenersIncrementCurrentProcessCompletionPercentage(completionFactor);
        }

    }

    public void startGeneration()
        throws GeometricException, ModelException
    {
        if(debugMode)
        {
            try
            {
                logfile = new File((new StringBuilder()).append(Model.seamcatHome).append(File.separator).append("logfiles").append(File.separator).toString());
                if(!logfile.exists())
                    logfile.mkdirs();
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                cal.setTimeInMillis(System.currentTimeMillis());
                logfile = new File(logfile, (new StringBuilder()).append(workspace.getReference()).append(" - ").append(cal.get(1)).append(cal.get(2) + 1 >= 10 ? "" : "0").append(cal.get(2) + 1).append(cal.get(5)).append("_").append(cal.get(11)).append(".").append(cal.get(12)).append(".log").toString());
                logfile.createNewFile();
                logfileAppender = new FileAppender(Model.getInstance().getLogFilePattern(), logfile.getAbsolutePath(), true);
                Logger.getLogger("org.seamcat").addAppender(logfileAppender);
                Logger.getLogger("org.seamcat").setLevel(Level.DEBUG);
            }
            catch(IOException ex)
            {
                LOG.warn("Unable to create EGE log file", ex);
            }
            LOG.debug((new StringBuilder()).append("\nStarting new event generation at: ").append(new Timestamp(System.currentTimeMillis())).append("\n").toString());
        }
        LOG.debug("Starting Event Generation");
        notifyListenersSetCurrentProcessCompletionPercentage(0);
        LOG.debug("Reseting Position Listeners");
        resetPositionListeners();
        notifyListenersSetCurrentProcessCompletionPercentage(5);
        if(workspace.getVictimSystemLink().getUseWantedTransmitter() && !workspace.getVictimSystemLink().getWt2VrPath().getUseCorrelationDistance())
            workspace.getVictimSystemLink().coverageRadiusWt(debugMode);
        notifyListenersSetCurrentProcessCompletionPercentage(10);
        Iterator interferenceLinkIterator = workspace.getInterferenceLinks().iterator();
        int percentageFactor = 90 / workspace.getInterferenceLinks().size();
        LOG.debug("Iterating through Interfering Links");
        do
        {
            if(!interferenceLinkIterator.hasNext())
                break;
            InterferenceLink interferenceLink = (InterferenceLink)interferenceLinkIterator.next();
            Function2 unWantEmission = interferenceLink.getInterferingLink().getInterferingTransmitter().getUnwantedEmissions();
            if(LOG.isDebugEnabled())
                LOG.debug((new StringBuilder()).append("Correlation mode = ").append(interferenceLink.getCorrelationMode()).toString());
            if(interferenceLink.getCorrelationMode() == 1)
                interferenceLink.getInterferingLink().getInterferingTransmitter().itSimulationRadius(interferenceLink.getProtectionDistance());
            if(!interferenceLink.getInterferingLink().getWt2VrPath().getUseCorrelationDistance())
                interferenceLink.getInterferingLink().coverageRadiusInterferingTransmitter();
            if(unWantEmission instanceof DiscreteFunction2)
            {
                if(LOG.isDebugEnabled())
                    LOG.debug("Storing original UnwantedEmission function");
                ((DiscreteFunction2)unWantEmission).storePoints();
            }
            if(LOG.isDebugEnabled())
                LOG.debug("Normalizing UnwantedEmission");
            unWantEmission.normalize();
            if(interferenceLink.getInterferingLink().getInterferingTransmitter().getUseUnwantedEmissionFloor())
                interferenceLink.getInterferingLink().getInterferingTransmitter().getUnwantedEmissionsFloor().normalize();
            initialized = true;
            notifyListenersIncrementCurrentProcessCompletionPercentage(percentageFactor);
            if(LOG.isDebugEnabled())
                LOG.debug("Starting of EGE complete");
        } while(true);
        notifyListenersSetCurrentProcessCompletionPercentage(100);
        if(workspace.hasCDMASubSystem())
        {
            notifyListenersUpdateStatus("Determining non interfered capacity of CDMA systems");
            if(workspace.getVictimSystemLink().isCDMASystem())
            {
                CDMASystem cdma = workspace.getVictimSystemLink().getCDMASystem();
                NonInterferedCapacityListener list = null;
                if(cdma.isSimulateCapacity() && !cdma.isCapacitySimulated() && !isInServerMode())
                {
                    MainWindow.getInstance().getChartWindow().showCDMACapacityPanel();
                    list = MainWindow.getInstance().getChartWindow().getNonInterferedCapacityListener();
                }
                cdma.findNonInterferedCapacity(list, null, false);
                if(isStopped())
                {
                    workspace.resetEventGeneration();
                    return;
                }
            }
            for(int i = 0; i < workspace.getInterferenceLinks().size(); i++)
            {
                InterferenceLink il = (InterferenceLink)workspace.getInterferenceLinks().get(i);
                if(!il.isCDMASystem())
                    continue;
                CDMASystem cdma = il.getCDMASystem();
                if(cdma.isSimulateCapacity() && !cdma.isCapacitySimulated() && !isInServerMode())
                    MainWindow.getInstance().getChartWindow().showCDMACapacityPanel();
                cdma.findNonInterferedCapacity(MainWindow.getInstance().getChartWindow().getNonInterferedCapacityListener(), null, false);
                if(isStopped())
                {
                    workspace.resetEventGeneration();
                    return;
                }
            }

            try
            {
                MainWindow.getInstance().getChartWindow().showSEAMCATOutlinePanel();
            }
            catch(NullPointerException npe) { }
        } else
        {
            try
            {
                MainWindow.getInstance().getChartWindow().showSEAMCATOutlinePanel();
            }
            catch(NullPointerException npe) { }
        }
    }

    public void eventGeneration(int events)
    {
        int eventCount;
        int i;
        int iCount;
        double rdRSSValue;
        double iRSSUnwanted;
        double iRSSBlocking;
        IRSSVector iRSSVectorSumU;
        IRSSVector iRSSVectorSumB;
        IRSSVector iRSSVectorSumI;
        boolean victimIsCdma;
        int percentageFactor;
        int totalPercentageFactor;
        double riRSSUnwantedValue[];
        double riRSSBlockingValue[];
        double riRSSIntermodulationValue[];
        boolean updatePositionListeners;
        double initialVictimCapacity;
        double initialVictimOutage;
        CDMASystem victim;
        long startTime;
        boolean doPostProcessing;
        ScenarioInfo_Impl info;
        long eventStart;
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("Generating ").append(events).append(" events").toString());
        eventCount = events;
        events += totalNumberOfEventsGenerated;
        ensureCapacities(events);
        i = 0;
        iCount = workspace.getInterferenceLinks().size();
        rdRSSValue = 0.0D;
        iRSSUnwanted = 0.0D;
        iRSSBlocking = 0.0D;
        double vrX = 0.0D;
        double vrY = 0.0D;
        iRSSVectorSumU = (IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(0);
        iRSSVectorSumB = (IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(0);
        iRSSVectorSumI = (IRSSVector)workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors().get(0);
        notifyListenersSetCurrentProcessCompletionPercentage(0);
        boolean victimReceiverFixed = workspace.getVictimSystemLink().getWt2VrPath().getUseCorrelationDistance();
        victimIsCdma = workspace.getVictimSystemLink().isCDMASystem();
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("Position of Victim Receiver is ").append(victimReceiverFixed ? "fixed" : "dynamic").toString());
        if(victimIsCdma)
        {
            workspace.getVictimSystemLink().getCDMASystem().repositionSystem(0.0D, 0.0D);
            boolean uplink = workspace.getVictimSystemLink().getCDMASystem().isUplink();
            CDMACell cells[][] = workspace.getVictimSystemLink().getCDMASystem().getCDMACells();
            int t = 0;
            for(int stop = cells.length; t < stop; t++)
                if(uplink)
                    notifyPositionListenersVictimReceiver(cells[t][0].getLocationX(), cells[t][0].getLocationY());
                else
                    notifyPositionListenersWantedTransmitter(cells[t][0].getLocationX(), cells[t][0].getLocationY());

        } else
        {
            notifyPositionListenersWantedTransmitter(0.0D, 0.0D);
            if(victimReceiverFixed)
            {
                vrX = workspace.getVictimSystemLink().getWt2VrPath().getDeltaX();
                vrY = workspace.getVictimSystemLink().getWt2VrPath().getDeltaY();
                notifyPositionListenersVictimReceiver(vrX, vrY);
            }
        }
        percentageFactor = (events - totalNumberOfEventsGenerated) / 100;
        totalPercentageFactor = (events - totalNumberOfEventsGenerated) / 40;
        if(percentageFactor == 0)
        {
            percentageFactor = 100 / (events - totalNumberOfEventsGenerated);
            totalPercentageFactor = 40 / (events - totalNumberOfEventsGenerated);
        }
        riRSSUnwantedValue = null;
        riRSSBlockingValue = null;
        riRSSIntermodulationValue = null;
        updatePositionListeners = true;
        initialVictimCapacity = 0.0D;
        initialVictimOutage = 0.0D;
        double interferedVictimCapacity = 0.0D;
        double interferedOutage = 0.0D;
        victim = null;
        notifyListenersUpdateStatus("Generating Events");
        startTime = System.currentTimeMillis();
        long endTime = startTime;
        doPostProcessing = workspace.hasPostProcessingPlugins();
        info = null;
        if(doPostProcessing)
            info = new ScenarioInfo_Impl(workspace);
        stoppedByTimeLimit = false;
        eventStart = System.currentTimeMillis();
        i = totalNumberOfEventsGenerated;
_L3:
        if(i >= events || isStopped()) goto _L2; else goto _L1
_L1:
        startTime = System.currentTimeMillis();
        if(doPostProcessing)
        {
            ((ProcessingResult_Impl)info.getProcessingResults()).reset();
            info.setCurrentEvent(i);
        }
        if(debugMode)
            LOG.debug((new StringBuilder()).append("-------------------Generating event #").append(i).append("---------------------").toString());
        if(eventCount < 100)
            updatePositionListeners = true;
        else
            updatePositionListeners = i % (eventCount / 100) == 0;
        long start;
        long end;
        riRSSUnwantedValue = new double[iCount];
        riRSSBlockingValue = new double[iCount];
        riRSSIntermodulationValue = new double[iCount * (iCount - 1)];
        if(debugMode)
            LOG.debug("Calculating dRSS values");
        if(!victimIsCdma)
            break MISSING_BLOCK_LABEL_900;
        victim = workspace.getVictimSystemLink().getCDMASystem();
        victim.resetSystem(true);
        victim.repositionSystem(0.0D, 0.0D);
        if(debugMode)
            LOG.debug("Balancing Victim CDMA System");
        start = System.currentTimeMillis();
        victim.balancePower();
        end = System.currentTimeMillis();
        if(victim.isSnapshotShouldBeIgnored())
        {
            i--;
            continue; /* Loop/switch isn't completed */
        }
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Balanced Victim CDMA System in ").append(end - start).append(" millis").toString());
        initialVictimCapacity = victim.getReferenceCellCapacity();
        initialVictimOutage = victim.getReferenceCellOutagePercentage();
        victim.setDroppedBeforeInterference(victim.countDroppedUsers());
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Initial Victim Capacity = ").append(initialVictimCapacity).append(" users in reference cell").toString());
            LOG.debug((new StringBuilder()).append("Initial Victim Outage = ").append(initialVictimOutage).append("% in reference cell").toString());
        }
        break MISSING_BLOCK_LABEL_1324;
        rdRSSValue = workspace.getVictimSystemLink().dRSSLinkBudgetDef(debugMode);
        if(!workspace.getVictimSystemLink().getUseWantedTransmitter())
        {
            rdRSSValue = workspace.getVictimSystemLink().getDRSS().trial();
            if(debugMode)
                LOG.debug((new StringBuilder()).append("Overriding calculated dRSS with value from user-defined distribution, trialed as = ").append(rdRSSValue).append(" dBm").toString());
        }
        if(doPostProcessing)
        {
            ((VictimLink_Impl)info.getVictimLink()).setDRSSValue(rdRSSValue);
            ((VictimLink_Impl)info.getVictimLink()).setFrequency(workspace.getVictimSystemLink().getVictimReceiver().getVrTrialFrequency());
            ((VictimLink_Impl)info.getVictimLink()).setPathloss(workspace.getVictimSystemLink().getTxRxPathLoss());
            Transceiver_Impl transmitter = (Transceiver_Impl)info.getVictimLink().getTransmitter();
            transmitter.setPositionX(workspace.getVictimSystemLink().getWantedTransmitter().getX());
            transmitter.setPositionY(workspace.getVictimSystemLink().getWantedTransmitter().getY());
            transmitter.setAntennaAzimuth(workspace.getVictimSystemLink().getTxRxAzimuth());
            transmitter.setAntennaElevation(workspace.getVictimSystemLink().getTxRxElevation());
            transmitter.setAntennaGain(workspace.getVictimSystemLink().getTxRxAntennaGain());
            transmitter.setAntennaHeight(workspace.getVictimSystemLink().getWantedTransmitter().getTxTrialAntHeight());
            VictimReceiver_Impl receiver = (VictimReceiver_Impl)info.getVictimLink().getReceiver();
            receiver.setPositionX(workspace.getVictimSystemLink().getVictimReceiver().getX());
            receiver.setPositionY(workspace.getVictimSystemLink().getVictimReceiver().getY());
            receiver.setAntennaAzimuth(workspace.getVictimSystemLink().getRxTxAzimuth());
            receiver.setAntennaElevation(workspace.getVictimSystemLink().getRxTxElevation());
            receiver.setAntennaGain(workspace.getVictimSystemLink().getRxTxAntennaGain());
            receiver.setAntennaHeight(workspace.getVictimSystemLink().getVictimReceiver().getRxTrialAntHeight());
            receiver.setNoiseFloor(workspace.getVictimSystemLink().getVictimReceiver().getNoiseFloorDistribution());
        }
        if(debugMode)
            LOG.debug((new StringBuilder()).append("dRSSValue = ").append(rdRSSValue).toString());
        double rSumU;
        double rSumB;
        double rSumI;
        rSumU = 0.0D;
        rSumB = 0.0D;
        rSumI = 0.0D;
        int j = 0;
        int id = 0;
        Iterator i$ = workspace.getInterferenceLinks().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            InterferenceLink interferenceLink = (InterferenceLink)i$.next();
            id++;
            double rSumUnwantedInt = 0.0D;
            double rSumBlockingInt = 0.0D;
            interferenceLink.setInterferingLink(interferenceLink.getInterferingLink());
            interferenceLink.getUnwantedInterference().setInterferenceLink(interferenceLink);
            interferenceLink.getBlockingInterference().setInterferenceLink(interferenceLink);
            if(interferenceLink.isCDMASystem())
            {
                if(debugMode)
                    LOG.debug("Balancing interfering CDMA System");
                interferenceLink.itVrLoc(debugMode);
                CDMASystem cdma = interferenceLink.getCDMASystem();
                cdma.resetSystem(true);
                cdma.repositionSystem(interferenceLink.getInterferingLink().getInterferingTransmitter().getX(), interferenceLink.getInterferingLink().getInterferingTransmitter().getY());
                long start = System.currentTimeMillis();
                cdma.balancePower();
                long end = System.currentTimeMillis();
                if(cdma.isSnapshotShouldBeIgnored())
                {
                    i--;
                    continue;
                }
                if(debugMode)
                    LOG.debug((new StringBuilder()).append("Balanced CDMA System in ").append(end - start).append(" millis").toString());
                PropagationModel model = interferenceLink.getWt2VrPath().getPropagationModel();
                Function2 unwanted = interferenceLink.getInterferingLink().getInterferingTransmitter().getUnwantedEmissions();
                Function blocking = workspace.getVictimSystemLink().getVictimReceiver().getBlockingResponse();
                double victimX = workspace.getVictimSystemLink().getWt2VrPath().getDeltaX();
                double victimY = workspace.getVictimSystemLink().getWt2VrPath().getDeltaY();
                double victimFreq = workspace.getVictimSystemLink().getVictimReceiver().getVrTrialFrequency();
                double victimHeight = workspace.getVictimSystemLink().getVictimReceiver().getRxTrialAntHeight();
                double victimGain = workspace.getVictimSystemLink().getRxTxAntennaGain();
                double victimRef = workspace.getVictimSystemLink().getVictimReceiver().getReceptionBandwith() / 1000D;
                boolean interferenceFromCluster = cdma.isMeasureInterferenceFromCluster();
                if(cdma.isUplink())
                {
                    double tempUw = 0.0D;
                    double tempBl = 0.0D;
                    if(debugMode)
                        LOG.debug("Interfering CDMA system is uplink - IT's are MS's connected to reference cell:");
                    List list = new ArrayList();
                    Iterator i$;
                    if(interferenceFromCluster)
                    {
                        list.addAll(cdma.getActiveUsers());
                    } else
                    {
                        CDMALink link;
                        for(i$ = cdma.getReferenceCell().getActiveConnections().iterator(); i$.hasNext(); list.add(link.getUserTerminal()))
                            link = (CDMALink)i$.next();

                    }
                    i$ = list.iterator();
                    do
                    {
                        if(!i$.hasNext())
                            break;
                        UserTerminal user = (UserTerminal)i$.next();
                        double TxPower = user.getCurrentTransmitPowerInWatt();
                        double TxGain = user.calculateAntennaGainTo(0.0D, 0.0D);
                        double TxHeight = user.getAntennaHeight();
                        CDMAInterferer interferer = new CDMAInterferer(user.getLocationX(), user.getLocationY(), TxPower, model, cdma.getFrequency(), TxHeight, TxGain, unwanted, blocking, cdma);
                        if(victimIsCdma)
                        {
                            victim.addExternalInterferer(interferer);
                        } else
                        {
                            interferer.setVictim(victimX, victimY, victimHeight, victimGain, victimFreq, victimRef);
                            double uwi = interferer.calculateUnwantedEmission();
                            tempUw += Math.pow(10D, uwi / 10D);
                            double blocki = interferer.calculateSelectivity();
                            tempBl += Math.pow(10D, blocki / 10D);
                        }
                        if(debugMode)
                            LOG.debug(interferer.toString());
                    } while(true);
                    iRSSUnwanted = 10D * Math.log10(tempUw);
                    iRSSBlocking = 10D * Math.log10(tempBl);
                } else
                {
                    double tempUw = 0.0D;
                    double tempBl = 0.0D;
                    if(debugMode)
                        LOG.debug("Interfering CDMA system is downlink - IT is reference cell BS:");
                    List itCells = new ArrayList();
                    if(interferenceFromCluster)
                        itCells.addAll(cdma.getCDMACellsAsList());
                    else
                        itCells.add(cdma.getReferenceCell());
                    Iterator i$ = itCells.iterator();
                    do
                    {
                        if(!i$.hasNext())
                            break;
                        CDMACell cell = (CDMACell)i$.next();
                        double TxPower = cell.getCurrentTransmitPower();
                        double TxHeight = cell.getAntennaHeight();
                        CDMAInterferer interferer = new CDMAInterferer(cell.getLocationX(), cell.getLocationY(), TxPower, model, cdma.getFrequency(), TxHeight, cell.getAntenna(), unwanted, blocking, cdma);
                        if(victimIsCdma)
                        {
                            victim.addExternalInterferer(interferer);
                        } else
                        {
                            interferer.setVictim(victimX, victimY, victimHeight, victimGain, victimFreq, victimRef);
                            double uwi = interferer.calculateUnwantedEmission();
                            tempUw += Math.pow(10D, uwi / 10D);
                            double blocki = interferer.calculateSelectivity();
                            tempBl += Math.pow(10D, blocki / 10D);
                        }
                        if(debugMode)
                            LOG.debug(interferer.toString());
                    } while(true);
                    iRSSUnwanted = 10D * Math.log10(tempUw);
                    iRSSBlocking = 10D * Math.log10(tempBl);
                }
                if(debugMode)
                {
                    LOG.debug((new StringBuilder()).append("iRSS Unwanted = ").append(iRSSUnwanted).toString());
                    LOG.debug((new StringBuilder()).append("iRSS Blocking = ").append(iRSSBlocking).toString());
                }
                try
                {
                    rSumUnwantedInt += Math.pow(10D, iRSSUnwanted / 10D);
                    rSumBlockingInt += Math.pow(10D, iRSSBlocking / 10D);
                    if(debugMode)
                    {
                        LOG.debug((new StringBuilder()).append("SumUnwantedInt = ").append(rSumUnwantedInt).toString());
                        LOG.debug((new StringBuilder()).append("SumBlockingInt = ").append(rSumBlockingInt).toString());
                    }
                }
                catch(Exception exception)
                {
                    throw new SummationException();
                }
                if(updatePositionListeners)
                {
                    boolean uplink = cdma.isUplink();
                    CDMACell cells[][] = cdma.getCDMACells();
                    int t = 0;
                    for(int stop = cells.length; t < stop; t++)
                        if(uplink)
                            notifyPositionListenersWantedReceiver(cells[t][0].getLocationX(), cells[t][0].getLocationY());
                        else
                            notifyPositionListenersInterferingTransmitter(cells[t][0].getLocationX(), cells[t][0].getLocationY());

                }
            } else
            {
                if(debugMode)
                    LOG.debug((new StringBuilder()).append("Starting loop on ").append(interferenceLink.getInterferingLink().getInterferingTransmitter().getNbActiveTx()).append(" active interferers").toString());
                for(int z = 0; z < interferenceLink.getInterferingLink().getInterferingTransmitter().getNbActiveTx(); z++)
                {
                    try
                    {
                        if(debugMode)
                            LOG.debug((new StringBuilder()).append("************* Processing interferer #").append(z).append("*****************").toString());
                        interferenceLink.getInterferingLink().itTrial(debugMode);
                        interferenceLink.getInterferingLink().wrTrial(debugMode);
                        interferenceLink.getInterferingLink().calculateRelativeTransmitterReceiverLocation(debugMode);
                        interferenceLink.itVrLoc(debugMode);
                        interferenceLink.getInterferingLink().iSLPathAntAziElev(debugMode);
                        interferenceLink.getInterferingLink().iSLPathAntGains(debugMode);
                        if(interferenceLink.getInterferingLink().getInterferingTransmitter().getUsePowerControl())
                        {
                            if(debugMode)
                                LOG.debug("Using Power Control");
                            interferenceLink.getInterferingLink().iSLPropagationLoss(debugMode);
                            interferenceLink.getInterferingLink().powerControlGain(debugMode);
                        }
                        interferenceLink.iLPathAntAziElev(debugMode);
                        if(victimIsCdma)
                        {
                            double x = workspace.getVictimSystemLink().getCDMASystem().getReferenceCell().getLocationX() + interferenceLink.getInterferingLink().getInterferingTransmitter().getX();
                            double y = workspace.getVictimSystemLink().getCDMASystem().getReferenceCell().getLocationY() + interferenceLink.getInterferingLink().getInterferingTransmitter().getY();
                            double power = interferenceLink.getInterferingLink().getInterferingTransmitter().getTxTrialPower();
                            if(interferenceLink.getInterferingLink().getInterferingTransmitter().getUsePowerControl())
                                power += interferenceLink.getInterferingLink().getInterferingTransmitter().getPowerControl().getInterferingTransmitterPowerControlGain();
                            CDMAInterferer interferer = new CDMAInterferer(x, y, power, interferenceLink.getWt2VrPath().getPropagationModel(), interferenceLink.getInterferingLink().getInterferingTransmitter().getItTrialFrequency(), interferenceLink.getInterferingLink().getInterferingTransmitter().getTxTrialAntHeight(), interferenceLink.getInterferingLink().getInterferingTransmitter().getAntenna(), interferenceLink.getInterferingLink().getInterferingTransmitter().getUnwantedEmissions(), workspace.getVictimSystemLink().getVictimReceiver().getBlockingResponse(), victim);
                            victim.addExternalInterferer(interferer);
                            if(debugMode)
                                LOG.debug((new StringBuilder()).append("Adding external interferer to victim cdma: ").append(interferer).toString());
                        } else
                        {
                            iRSSUnwanted = interferenceLink.getUnwantedInterference().iRSSLinkBudgetUnwanted(debugMode);
                            iRSSBlocking = interferenceLink.getBlockingInterference().iRSSLinkBudgetBlock(debugMode);
                            if(debugMode)
                            {
                                LOG.debug((new StringBuilder()).append("iRSS Unwanted = ").append(iRSSUnwanted).toString());
                                LOG.debug((new StringBuilder()).append("iRSS Blocking = ").append(iRSSBlocking).toString());
                            }
                            try
                            {
                                rSumUnwantedInt += Math.pow(10D, iRSSUnwanted / 10D);
                                rSumBlockingInt += Math.pow(10D, iRSSBlocking / 10D);
                            }
                            catch(Exception exception)
                            {
                                throw new SummationException();
                            }
                        }
                        if(updatePositionListeners)
                        {
                            double itX = interferenceLink.getInterferingLink().getInterferingTransmitter().getX();
                            double itY = interferenceLink.getInterferingLink().getInterferingTransmitter().getY();
                            double wrX = interferenceLink.getInterferingLink().getWantedReceiver().getX();
                            double wrY = interferenceLink.getInterferingLink().getWantedReceiver().getY();
                            notifyPositionListenersInterferingTransmitter(itX, itY);
                            notifyPositionListenersWantedReceiver(wrX, wrY);
                        }
                        if(doPostProcessing)
                        {
                            int linkIndex = id + z;
                            InterferingLink_Impl link = (InterferingLink_Impl)info.getInterferingLink(linkIndex);
                            link.setFrequency(interferenceLink.getInterferingLink().getInterferingTransmitter().getItTrialFrequency());
                            link.setPathloss(interferenceLink.getTxRxPathLoss());
                            link.setIRSSUnwanted(iRSSUnwanted);
                            link.setIRSSBlocking(iRSSBlocking);
                            Transceiver_Impl receiver = (Transceiver_Impl)link.getReceiver();
                            receiver.setPositionX(interferenceLink.getInterferingLink().getWantedReceiver().getX());
                            receiver.setPositionY(interferenceLink.getInterferingLink().getWantedReceiver().getY());
                            receiver.setAntennaAzimuth(interferenceLink.getInterferingLink().getRxTxAzimuth());
                            receiver.setAntennaElevation(interferenceLink.getInterferingLink().getRxTxElevation());
                            receiver.setAntennaGain(interferenceLink.getInterferingLink().getRxTxAntennaGain());
                            receiver.setAntennaHeight(interferenceLink.getInterferingLink().getWantedReceiver().getRxTrialAntHeight());
                            InterferingTransmitter_Impl transmitter = (InterferingTransmitter_Impl)link.getTransmitter();
                            transmitter.setPositionX(interferenceLink.getInterferingLink().getInterferingTransmitter().getX());
                            transmitter.setPositionY(interferenceLink.getInterferingLink().getInterferingTransmitter().getY());
                            transmitter.setVrItAzimuth(interferenceLink.getRxTxAzimuth());
                            transmitter.setVrItElevation(interferenceLink.getRxTxElevation());
                            transmitter.setItVrAzimuth(interferenceLink.getTxRxAzimuth());
                            transmitter.setItVrElevation(interferenceLink.getTxRxElevation());
                            transmitter.setAntennaAzimuth(interferenceLink.getInterferingLink().getTxRxAzimuth());
                            transmitter.setAntennaElevation(interferenceLink.getInterferingLink().getTxRxElevation());
                            transmitter.setAntennaHeight(interferenceLink.getInterferingLink().getInterferingTransmitter().getTxTrialAntHeight());
                            transmitter.setUsingPowerControl(interferenceLink.getInterferingLink().getInterferingTransmitter().getUsePowerControl());
                            transmitter.setAbsoluteUnwantedPower(interferenceLink.getUnwantedInterference().getUnwantedPower());
                            transmitter.setSuppliedPower(interferenceLink.getInterferingLink().getInterferingTransmitter().getTxTrialPower());
                        }
                    }
                    catch(Exception ex)
                    {
                        throw new Exception((new StringBuilder()).append("Exception on IT,WR pair #").append(z).append("\nCaused by: ").append(ex.getMessage()).append("\n").append("[").append(ex.getStackTrace()[0].getFileName()).append(":").append(ex.getStackTrace()[0].getLineNumber()).append("]").toString(), ex);
                    }
                    if(debugMode)
                    {
                        LOG.debug((new StringBuilder()).append("Processed interferer #").append(z).toString());
                        LOG.debug("*******************************************");
                    }
                }

            }
            try
            {
                if(rSumUnwantedInt > 0.0D)
                    riRSSUnwantedValue[j] = 10D * Math.log10(rSumUnwantedInt);
                else
                    riRSSUnwantedValue[j] = -1000D;
                if(rSumBlockingInt > 0.0D)
                    riRSSBlockingValue[j] = 10D * Math.log10(rSumBlockingInt);
                else
                    riRSSBlockingValue[j] = -1000D;
            }
            catch(Exception exception)
            {
                throw new SummationException();
            }
            j++;
        } while(true);
        if(!victimIsCdma)
            break MISSING_BLOCK_LABEL_4384;
        victim.balanceInterferedSystem();
        if(victim.isSnapshotShouldBeIgnored())
        {
            i--;
            continue; /* Loop/switch isn't completed */
        }
        double interferedVictimCapacity = victim.getReferenceCellCapacity();
        double interferedOutage = victim.getReferenceCellOutagePercentage();
        workspace.getCdmaResults().getCdmaInitialCapacity().addEvent(initialVictimCapacity);
        workspace.getCdmaResults().getCdmaInterferedCapacity().addEvent(interferedVictimCapacity);
        workspace.getCdmaResults().getCdmaInitialOutage().addEvent(initialVictimOutage);
        workspace.getCdmaResults().getCdmaInterferedOutage().addEvent(interferedOutage);
        workspace.getCdmaResults().getCdmaTotalDroppedUsers().addEvent(victim.countDroppedUsers());
        EventVector unw = ((IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(0)).getEventVector();
        EventVector blo = ((IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(0)).getEventVector();
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("Victim CDMA interfered capacity = ").append(interferedVictimCapacity).append(" users in reference cell").toString());
            LOG.debug((new StringBuilder()).append("Victim CDMA interfered outage = ").append(interferedOutage).append("% outage in reference cell").toString());
        }
        if(victim.isUplink())
        {
            unw.addEvent(victim.getReferenceCell().getExternalInterferenceUnwanted());
            blo.addEvent(victim.getReferenceCell().getExternalInterferenceBlocking());
            if(debugMode)
            {
                LOG.debug("Victim CDMA is uplink - VR is reference cell BS");
                LOG.debug((new StringBuilder()).append("Reference Cell External Unwanted = ").append(victim.getReferenceCell().getExternalInterferenceUnwanted()).append(" dBm").toString());
                LOG.debug((new StringBuilder()).append("Reference Cell External Blocking = ").append(victim.getReferenceCell().getExternalInterferenceBlocking()).append(" dBm").toString());
            }
        } else
        {
            double sumUnw = 0.0D;
            double sumBlo = 0.0D;
            for(Iterator i$ = victim.getReferenceCell().getActiveConnections().iterator(); i$.hasNext();)
            {
                CDMALink link = (CDMALink)i$.next();
                sumUnw += link.getUserTerminal().getExternalInterferenceUnwanted();
                sumBlo += link.getUserTerminal().getExternalInterferenceBlocking();
            }

            int size = victim.getReferenceCell().getActiveConnections().size();
            unw.addEvent(sumUnw / (double)size);
            blo.addEvent(sumBlo / (double)size);
            if(debugMode)
            {
                LOG.debug("Victim CDMA is downlink - VR's are MS's connected to reference cell");
                LOG.debug((new StringBuilder()).append("Average Unwanted = ").append(sumUnw / (double)size).append(" dBm").toString());
                LOG.debug((new StringBuilder()).append("Avereage Blocking = ").append(sumBlo / (double)size).append(" dBm").toString());
            }
        }
        break MISSING_BLOCK_LABEL_6827;
        List iRSSVectorIteratorIntermod = workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors();
        if(iCount > 1)
        {
            if(debugMode)
                LOG.debug("Calculation of iRSS - Intermodulation values");
            int l = 0;
            IntermodulationInterference intermod = new IntermodulationInterference();
            Components interferenceIntermod = workspace.getInterferenceLinks();
            double noiseFloor = workspace.getVictimSystemLink().getVictimReceiver().getNoiseFloorDistribution().trial();
            for(int m = 0; m < interferenceIntermod.size(); m++)
            {
                InterferenceLink interferenceLinkM = (InterferenceLink)interferenceIntermod.get(m);
                intermod.setBlockingInterference1(interferenceLinkM.getBlockingInterference());
                for(int k = 0; k < interferenceIntermod.size(); k++)
                {
                    if(m == k || !((IRSSVector)iRSSVectorIteratorIntermod.get(l)).getEventVector().isShouldBeCalculated())
                        continue;
                    InterferenceLink interferenceLinkK = (InterferenceLink)interferenceIntermod.get(k);
                    intermod.setBlockingInterference2(interferenceLinkK.getBlockingInterference());
                    riRSSIntermodulationValue[l] = intermod.iRSSLinkBudgetInterMod();
                    riRSSIntermodulationValue[l] += noiseFloor;
                    if(doPostProcessing)
                        info.getIntermodulation()[l] = riRSSIntermodulationValue[l];
                    if(debugMode)
                        LOG.debug((new StringBuilder()).append("iRSS Intermodulation = ").append(riRSSIntermodulationValue[1]).toString());
                    try
                    {
                        rSumI += Math.pow(10D, riRSSIntermodulationValue[l] / 10D);
                        if(LOG.isDebugEnabled() && i % 100 == 0)
                            LOG.debug((new StringBuilder()).append("rSumI = ").append(rSumI).toString());
                    }
                    catch(Exception exception)
                    {
                        throw new SummationException();
                    }
                    l++;
                }

            }

        }
        if(doPostProcessing)
        {
            if(LOG.isDebugEnabled())
                LOG.debug((new StringBuilder()).append("Running ").append(workspace.getPostProcessingPlugins().size()).append(" post processing plugins").toString());
            boolean chainedPlugins = workspace.getPostProcessingPlugins().size() > 1;
            int ii = 1;
            Iterator i$ = workspace.getPostProcessingPlugins().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                PostProcessingPluginWrapper plugin = (PostProcessingPluginWrapper)i$.next();
                if(LOG.isDebugEnabled())
                    LOG.debug((new StringBuilder()).append("Running plugin #").append(ii++).append(" (").append(plugin.getReference()).append(" ) Plugin class: ").append(plugin.getClass().getName()).toString());
                try
                {
                    long start = System.currentTimeMillis();
                    plugin.process(info);
                    long end = System.currentTimeMillis();
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug((new StringBuilder()).append("Plugin completed in ").append(end - start).append(" milliseconds").toString());
                        LOG.debug("Storing results");
                    }
                    ProcessingResult result = info.getProcessingResults();
                    if(result.isDRSSChanged())
                    {
                        rdRSSValue = result.getDRSS();
                        if(chainedPlugins)
                            ((VictimLink_Impl)info.getVictimLink()).setDRSSValue(rdRSSValue);
                        if(LOG.isDebugEnabled())
                            LOG.debug((new StringBuilder()).append("dRSS was changed to: ").append(rdRSSValue).append(" dBm").toString());
                        workspace.getSignals().getDRSSVector().getEventVector().setModifiedByPlugin(true);
                    }
                    List links = info.getInterferingLinks();
                    int q = 0;
                    for(int stop = links.size(); q < stop; q++)
                    {
                        InterferingLink link = (InterferingLink)links.get(q);
                        if(link.isSublink())
                        {
                            double iUnw = 0.0D;
                            double iBlo = 0.0D;
                            int origIndex = link.getOrigLinkIndex();
                            boolean moreSublinks = true;
                            do
                            {
                                if(!moreSublinks)
                                    break;
                                moreSublinks = false;
                                if(result.isIRSSUnwantedChanged(link.getLinkIndex()))
                                {
                                    iUnw += Math.pow(10D, result.getIRSSUnwanted(link.getLinkIndex()) / 10D);
                                    if(chainedPlugins)
                                        ((InterferingLink_Impl)link).setIRSSUnwanted(result.getIRSSUnwanted(link.getLinkIndex()));
                                }
                                if(result.isIRSSBlockingChanged(link.getLinkIndex()))
                                {
                                    iBlo += Math.pow(10D, result.getIRSSBlocking(link.getLinkIndex()) / 10D);
                                    if(chainedPlugins)
                                        ((InterferingLink_Impl)link).setIRSSBlocking(result.getIRSSBlocking(link.getLinkIndex()));
                                }
                                if(q + 1 < stop && ((InterferingLink)links.get(q + 1)).isSublink() && ((InterferingLink)links.get(q + 1)).getOrigLinkIndex() == origIndex)
                                {
                                    link = (InterferingLink)links.get(++q);
                                    moreSublinks = true;
                                }
                            } while(true);
                            if(iUnw != 0.0D)
                            {
                                riRSSUnwantedValue[link.getOrigLinkIndex() - 1] = 10D * Math.log10(iUnw);
                                if(LOG.isDebugEnabled())
                                    LOG.debug((new StringBuilder()).append("iRSSUnwanted for link ").append(link.getOrigLinkIndex()).append(" was changed to: ").append(riRSSUnwantedValue[link.getOrigLinkIndex() - 1]).append(" dBm").toString());
                                ((IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(link.getOrigLinkIndex())).getEventVector().setModifiedByPlugin(true);
                                ((IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(0)).getEventVector().setModifiedByPlugin(true);
                            }
                            if(iBlo != 0.0D)
                            {
                                riRSSBlockingValue[link.getOrigLinkIndex() - 1] = 10D * Math.log10(iBlo);
                                if(LOG.isDebugEnabled())
                                    LOG.debug((new StringBuilder()).append("iRSSBlocking for link ").append(link.getOrigLinkIndex()).append(" was changed to: ").append(riRSSBlockingValue[link.getOrigLinkIndex() - 1]).append(" dBm").toString());
                                ((IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(link.getOrigLinkIndex())).getEventVector().setModifiedByPlugin(true);
                                ((IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(0)).getEventVector().setModifiedByPlugin(true);
                            }
                        } else
                        {
                            if(result.isIRSSUnwantedChanged(link.getLinkIndex()))
                            {
                                riRSSUnwantedValue[link.getOrigLinkIndex() - 1] = result.getIRSSUnwanted(link.getLinkIndex());
                                if(chainedPlugins)
                                    ((InterferingLink_Impl)link).setIRSSUnwanted(riRSSUnwantedValue[link.getOrigLinkIndex() - 1]);
                                if(LOG.isDebugEnabled())
                                    LOG.debug((new StringBuilder()).append("iRSSUnwanted for link ").append(link.getOrigLinkIndex()).append(" was changed to: ").append(riRSSUnwantedValue[link.getOrigLinkIndex() - 1]).append(" dBm").toString());
                                ((IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(link.getOrigLinkIndex())).getEventVector().setModifiedByPlugin(true);
                                ((IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(0)).getEventVector().setModifiedByPlugin(true);
                            }
                            if(result.isIRSSBlockingChanged(link.getLinkIndex()))
                            {
                                riRSSBlockingValue[link.getOrigLinkIndex() - 1] = result.getIRSSBlocking(link.getLinkIndex());
                                if(chainedPlugins)
                                    ((InterferingLink_Impl)link).setIRSSBlocking(riRSSBlockingValue[link.getOrigLinkIndex() - 1]);
                                if(LOG.isDebugEnabled())
                                    LOG.debug((new StringBuilder()).append("iRSSBlocking for link ").append(link.getOrigLinkIndex()).append(" was changed to: ").append(riRSSBlockingValue[link.getOrigLinkIndex() - 1]).append(" dBm").toString());
                                ((IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(link.getOrigLinkIndex())).getEventVector().setModifiedByPlugin(true);
                                ((IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(0)).getEventVector().setModifiedByPlugin(true);
                            }
                        }
                        int stopCondition = links.size();
                        int l = 0;
                        for(int rc = 1; rc <= stopCondition; rc++)
                        {
                            for(int cr = 1; cr <= stopCondition; cr++)
                            {
                                if(rc != cr && result.isIRSSIntermodulationChanged(rc, cr))
                                {
                                    riRSSIntermodulationValue[l] = result.getIRSSIntermodulation(rc, cr);
                                    if(chainedPlugins)
                                        info.getIntermodulation()[l] = riRSSIntermodulationValue[l];
                                    if(LOG.isDebugEnabled())
                                        LOG.debug((new StringBuilder()).append("iRSSIntermodulation between link ").append(rc).append(" and link ").append(cr).append(" was changed to: ").append(riRSSIntermodulationValue[link.getOrigLinkIndex() - 1]).append(" dBm").toString());
                                    ((IRSSVector)workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors().get(l)).getEventVector().setModifiedByPlugin(true);
                                    ((IRSSVector)workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors().get(0)).getEventVector().setModifiedByPlugin(true);
                                }
                                l++;
                            }

                        }

                    }

                    if(((ProcessingResult_Impl)result).isStopRequested())
                    {
                        generateEvents = false;
                        notifyListenersUpdateStatus((new StringBuilder()).append("Plugin ").append(plugin.getReference()).append(" stopped simulation at event #").append(i + 1).toString());
                    }
                }
                catch(Exception e)
                {
                    LOG.error((new StringBuilder()).append("Error running plugin for event #").append(i + 1).append(": ").append(plugin.getClass().getName()).toString(), e);
                }
            } while(true);
        }
        DRSSVector dRSSResult = workspace.getSignals().getDRSSVector();
        dRSSResult.getEventVector().addEvent(rdRSSValue);
        for(int k = 0; k < iCount; k++)
        {
            IRSSVector iRSSVectorU = (IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(k + 1);
            iRSSVectorU.getEventVector().addEvent(riRSSUnwantedValue[k]);
            IRSSVector iRSSVectorB = (IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(k + 1);
            iRSSVectorB.getEventVector().addEvent(riRSSBlockingValue[k]);
            try
            {
                rSumU += Math.pow(10D, riRSSUnwantedValue[k] / 10D);
            }
            catch(Exception exception)
            {
                throw new SummationException();
            }
            try
            {
                rSumB += Math.pow(10D, riRSSBlockingValue[k] / 10D);
            }
            catch(Exception exception)
            {
                throw new SummationException();
            }
        }

        for(int l = 1; l < iRSSVectorIteratorIntermod.size(); l++)
            try
            {
                if(((IRSSVector)iRSSVectorIteratorIntermod.get(l)).getEventVector().isShouldBeCalculated())
                    ((IRSSVector)iRSSVectorIteratorIntermod.get(l)).getEventVector().addEvent(riRSSIntermodulationValue[l - 1]);
            }
            catch(Exception ex)
            {
                LOG.error((new StringBuilder()).append("Error assigning value: l = ").append(l).toString(), ex);
            }

        try
        {
            iRSSVectorSumU.getEventVector().addEvent(10D * Math.log10(rSumU));
            iRSSVectorSumB.getEventVector().addEvent(10D * Math.log10(rSumB));
        }
        catch(Exception exception)
        {
            throw new SummationException(exception);
        }
        if(iCount > 1)
            try
            {
                iRSSVectorSumI.getEventVector().addEvent(10D * Math.log10(rSumI));
            }
            catch(Exception exception)
            {
                throw new SummationException(exception);
            }
        totalNumberOfEventsGenerated++;
        try
        {
            if((events - totalNumberOfEventsGenerated) % percentageFactor == 0)
            {
                notifyListenersEventCompleted(i, events);
                notifyListenersIncrementCurrentProcessCompletionPercentage(1);
                Thread.yield();
            } else
            if(events < 100)
            {
                notifyListenersEventCompleted(i, events);
                notifyListenersIncrementCurrentProcessCompletionPercentage(percentageFactor);
                Thread.yield();
            }
            if((events - totalNumberOfEventsGenerated) % totalPercentageFactor == 0)
            {
                notifyListenersIncrementTotalProcessCompletionPercentage(1);
                Thread.yield();
            }
        }
        catch(Exception ex2)
        {
            LOG.debug("A non critical error occurred in EGE while updating GUI", ex2);
        }
        break MISSING_BLOCK_LABEL_6998;
        Exception e;
        e;
        LOG.error((new StringBuilder()).append("Error while processing event #").append(i).toString(), e);
        if(notifyListenersOfException(e, i))
        {
            LOG.info("Stopping EGE");
            notifyListenersError("Aborting EGE due to unhandled exception");
            stop();
        } else
        {
            LOG.info("EGE continues");
        }
        if(updatePositionListeners)
        {
            if(!victimIsCdma)
            {
                double vrX = workspace.getVictimSystemLink().getVictimReceiver().getX();
                double vrY = workspace.getVictimSystemLink().getVictimReceiver().getY();
                notifyPositionListenersVictimReceiver(vrX, vrY);
                notifyPositionListenersWantedTransmitter(0.0D, 0.0D);
            }
            try
            {
                Thread.sleep(1L);
            }
            catch(InterruptedException ex)
            {
                LOG.error("An Error occured", ex);
            }
        }
        resetPositions();
        long endTime = System.currentTimeMillis();
        if(debugMode)
        {
            LOG.debug((new StringBuilder()).append("-------------------Generated event #").append(i).append(" in ").append(endTime - startTime).append(" ms---------------------").toString());
            LOG.debug("################################################################################################");
        }
        if(timeLimited && System.currentTimeMillis() - (eventStart + (endTime - startTime)) > timeLimit)
        {
            stop();
            stoppedByTimeLimit = true;
            LOG.debug("Stopping Event Generation due to specified time limit");
        }
        i++;
          goto _L3
_L2:
        if(doPostProcessing)
        {
            PostProcessingPlugin p;
            for(Iterator i$ = workspace.getPostProcessingPlugins().iterator(); i$.hasNext(); p.cleanUp())
                p = (PostProcessingPluginWrapper)i$.next();

        }
        InterferenceLink link;
        for(Iterator i$ = workspace.getInterferenceLinks().iterator(); i$.hasNext(); link.getBlockingInterference().reset())
        {
            link = (InterferenceLink)i$.next();
            Function2 unWantEmission = link.getInterferingLink().getInterferingTransmitter().getUnwantedEmissions();
            if(unWantEmission instanceof DiscreteFunction2)
            {
                LOG.debug((new StringBuilder()).append("Reseting unwanted emission function of interfering transmitter on interfering link # ").append(i).toString());
                ((DiscreteFunction2)unWantEmission).resetPoints();
            }
        }

        initialized = false;
        notifyListenersSetCurrentProcessCompletionPercentage(100);
        notifyListenersEventGenerationCompleted(totalNumberOfEventsGenerated);
        return;
    }

    private void resetPositions()
    {
        workspace.getVictimSystemLink().getWantedTransmitter().setX(0.0D);
        workspace.getVictimSystemLink().getWantedTransmitter().setY(0.0D);
        workspace.getVictimSystemLink().getVictimReceiver().setX(0.0D);
        workspace.getVictimSystemLink().getVictimReceiver().setY(0.0D);
        for(int i = 0; i < workspace.getInterferenceLinks().size(); i++)
        {
            InterferenceLink li = (InterferenceLink)workspace.getInterferenceLinks().get(i);
            li.getInterferingLink().getWantedReceiver().setX(0.0D);
            li.getInterferingLink().getWantedReceiver().setY(0.0D);
            li.getInterferingLink().getInterferingTransmitter().setX(0.0D);
            li.getInterferingLink().getInterferingTransmitter().setY(0.0D);
        }

    }

    public int getTotalNumberOfEventsGenerated()
    {
        return totalNumberOfEventsGenerated;
    }

    public File getLogfile()
    {
        return logfile;
    }

    public void run()
    {
        try
        {
            notifyListenersSetTotalProcessCompletionPercentage(0);
            if(!useHigherPriorityThreads)
                Thread.currentThread().setPriority(1);
            notifyListenersUpdateStatus("Initializing Event Generation Engine");
            if(useTestCDMAAlgorithm)
            {
                notifyListenersStartingEventGeneration(workspace, numberOfEvents, getTotalNumberOfEventsGenerated());
                CdmaEngine cdmaEngine = new CdmaEngine(workspace);
                cdmaEngine.startSimulation();
            } else
            {
                generateEvents = true;
                if(!initialized)
                    startGeneration();
                Thread.yield();
                notifyListenersIncrementTotalProcessCompletionPercentage(10);
                notifyListenersUpdateStatus("Generating Events");
                notifyListenersStartingEventGeneration(workspace, numberOfEvents, getTotalNumberOfEventsGenerated());
                if(!isStopped())
                    eventGeneration(numberOfEvents);
            }
            Thread.yield();
            if(!generateEvents)
            {
                workspace.getSignals().trim();
                if(workspace.hasCDMASubSystem())
                    workspace.getCdmaResults().trim();
                notifyListenersSetCurrentProcessCompletionPercentage(100);
                notifyListenersSetTotalProcessCompletionPercentage(100);
                if(stoppedByTimeLimit)
                    notifyListenersUpdateStatus("Eventgeneration was stopped by time limit!");
                else
                    notifyListenersUpdateStatus("Event Generation was stopped");
                notifyListenersGenerationAndEvaluationComplete();
            } else
            {
                notifyListenersUpdateStatus("Checking Stability");
                try
                {
                    stability();
                }
                catch(Exception e) { }
                notifyListenersIncrementTotalProcessCompletionPercentage(10);
                try
                {
                    if(!workspace.getVictimSystemLink().isCDMASystem())
                        correlate();
                }
                catch(Exception e)
                {
                    LOG.error("Error correlating", e);
                }
                notifyListenersSetCurrentProcessCompletionPercentage(100);
                notifyListenersSetTotalProcessCompletionPercentage(100);
                notifyListenersUpdateStatus("Event Generation Complete");
                notifyListenersGenerationAndEvaluationComplete();
            }
            try
            {
                if(debugMode)
                {
                    Logger.getLogger("org.seamcat").removeAppender(logfileAppender);
                    Logger.getLogger("org.seamcat").setLevel(Logger.getRootLogger().getLevel());
                    logfileAppender.close();
                }
            }
            catch(Exception e)
            {
                LOG.error("Error reseting logfile appender.", e);
            }
        }
        catch(Exception e)
        {
            LOG.error("An unknown error occurred", e);
        }
    }

    private void stability()
    {
        int iCount = workspace.getInterferenceLinks().size();
        int idN = workspace.getControl().getDeData().getAdditionalNbEvents();
        int iMaxEvents = workspace.getControl().getEgData().getNumberOfEvents();
        double rStability = workspace.getControl().getDeData().getSignDistribution();
        double rResultStability = 0.0D;
        int i = 0;
        boolean bTestDEE = true;
        boolean bError = false;
        notifyListenersUpdateStatus("Stability evaluation");
        if(getNumberOfEvents() <= idN && workspace.getControl().getEgData().getTerminationConditionInt() == 1)
        {
            notifyListenersError("Number of generated events must be greater than DEE additional number of events");
            bTestDEE = false;
        }
        if(workspace.getControl().getEgData().getTerminationConditionInt() == 1 && workspace.getSignals().getDRSSVector().getEventVector().size() > 0 && bTestDEE)
        {
            DRSSVector dRSSResult = workspace.getSignals().getDRSSVector();
            List tempSignalVector = new ArrayList(workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors());
            tempSignalVector.addAll(workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors());
            tempSignalVector.addAll(workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors());
            try
            {
                rResultStability = dRSSResult.getEventVector().checkStability(iMaxEvents, idN);
            }
            catch(Exception e)
            {
                bError = true;
            }
            while(rResultStability < rStability && workspace.getSignals().getDRSSVector().getEventVector().size() < 2 * iMaxEvents && !bError) 
            {
                notifyListenersSetCurrentProcessCompletionPercentage(0);
                try
                {
                    eventGeneration(idN);
                }
                catch(Exception e)
                {
                    LOG.error("An error occurred", e);
                }
                DRSSVector dRSSResultBis = workspace.getSignals().getDRSSVector();
                try
                {
                    rResultStability = dRSSResultBis.getEventVector().checkStability(iMaxEvents, idN);
                }
                catch(Exception e)
                {
                    bError = true;
                }
            }
            for(i = 0; i < 2 * iCount + iCount * (iCount - 1) && !bError; i++)
            {
                IRSSVector iRSSVector = (IRSSVector)tempSignalVector.get(i);
                try
                {
                    rResultStability = iRSSVector.getEventVector().checkStability(iMaxEvents, idN);
                }
                catch(Exception e)
                {
                    bError = true;
                }
                while(rResultStability < rStability && workspace.getSignals().getDRSSVector().getEventVector().size() < 2 * iMaxEvents && !bError) 
                {
                    notifyListenersSetCurrentProcessCompletionPercentage(0);
                    try
                    {
                        eventGeneration(idN);
                    }
                    catch(Exception e) { }
                    IRSSVector iRSSVectorBis = (IRSSVector)tempSignalVector.get(i);
                    try
                    {
                        rResultStability = iRSSVectorBis.getEventVector().checkStability(iMaxEvents, idN);
                    }
                    catch(Exception e)
                    {
                        bError = true;
                    }
                }
            }

        }
    }

    private void correlate()
    {
        notifyListenersUpdateStatus("Performing correlation evaluation and distribution identification");
        DRSSVector dRSSResult = workspace.getSignals().getDRSSVector();
        IRSSVectorList iRSSVectorListUnwanted = workspace.getSignals().getIRSSVectorListUnwanted();
        IRSSVectorList iRSSVectorListBlocking = workspace.getSignals().getIRSSVectorListBlocking();
        IRSSVectorList iRSSVectorListIntermodulation = workspace.getSignals().getIRSSVectorListIntermodulation();
        double rResult = 0.0D;
        int iCount = workspace.getInterferenceLinks().size();
        int iNbrSig = 2 * iCount + iCount * (iCount - 1);
        double rCorrUnwanted[] = new double[iRSSVectorListUnwanted.getIRSSVectors().size()];
        double rCorrBlocking[] = new double[iRSSVectorListBlocking.getIRSSVectors().size()];
        double rCorrIntermodular[] = new double[iRSSVectorListIntermodulation.getIRSSVectors().size()];
        double rThreshold = workspace.getControl().getDeData().getCorrelationThreshold();
        int idN = workspace.getControl().getDeData().getAdditionalNbEvents();
        int iMaxEvents = workspace.getControl().getEgData().getNumberOfEvents();
        double rStability = workspace.getControl().getDeData().getSignDistribution();
        int j = 0;
        boolean bCorr = true;
        int k = 0;
        int stop = iRSSVectorListUnwanted.getIRSSVectors().size();
        for(int i = 0; i < stop && !isStopped();)
        {
            IRSSVector iRSSVector = (IRSSVector)iRSSVectorListUnwanted.getIRSSVectors().get(i);
            rResult = EventVector.correlation(dRSSResult.getEventVector(), iRSSVector.getEventVector());
            if(rResult > rThreshold)
                bCorr = false;
            rCorrUnwanted[k] = rResult;
            i++;
            k++;
        }

        stop = iRSSVectorListBlocking.getIRSSVectors().size();
        k = 0;
        for(int i = 0; i < stop && !isStopped();)
        {
            IRSSVector iRSSVector = (IRSSVector)iRSSVectorListBlocking.getIRSSVectors().get(i);
            rResult = EventVector.correlation(dRSSResult.getEventVector(), iRSSVector.getEventVector());
            if(rResult > rThreshold)
                bCorr = false;
            rCorrBlocking[k] = rResult;
            i++;
            k++;
        }

        stop = iRSSVectorListIntermodulation.getIRSSVectors().size();
        k = 0;
        for(int i = 0; i < stop && !isStopped(); i++)
        {
            IRSSVector iRSSVector = (IRSSVector)iRSSVectorListIntermodulation.getIRSSVectors().get(i);
            if(!dRSSResult.getEventVector().isShouldBeCalculated() || !iRSSVector.getEventVector().isShouldBeCalculated())
                continue;
            rResult = EventVector.correlation(dRSSResult.getEventVector(), iRSSVector.getEventVector());
            if(rResult > rThreshold)
                bCorr = false;
            rCorrIntermodular[k++] = rResult;
        }

        stop = iRSSVectorListUnwanted.getIRSSVectors().size();
        List tempSignalVector = new ArrayList(iRSSVectorListUnwanted.getIRSSVectors());
        tempSignalVector.addAll(iRSSVectorListBlocking.getIRSSVectors());
        tempSignalVector.addAll(iRSSVectorListIntermodulation.getIRSSVectors());
        stop = tempSignalVector.size();
        for(int i = 0; i < stop && !isStopped(); i++)
        {
            IRSSVector iRSSVectori = (IRSSVector)tempSignalVector.get(i);
            for(j = 0; j < 2 * iCount + iCount * (iCount - 1) && !isStopped(); j++)
            {
                if(i >= j)
                    continue;
                IRSSVector iRSSVectorj = (IRSSVector)tempSignalVector.get(j);
                rResult = EventVector.correlation(iRSSVectori.getEventVector(), iRSSVectorj.getEventVector());
                if(rResult > rThreshold)
                    bCorr = false;
                k++;
            }

        }

        EventVector dRSSEventVector = workspace.getSignals().getDRSSVector().getEventVector();
        DRSSDistrib dRSSDistrib = workspace.getSignals().getDRSSDistrib();
        IRSSVector iRSSVectorSumU = (IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(0);
        IRSSVector iRSSVectorSumB = (IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(0);
        IRSSVector iRSSVectorSumI = (IRSSVector)workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors().get(0);
        if(workspace.getVictimSystemLink().getUseWantedTransmitter())
            workspace.getRadius().addCoverageRadius("Wanted Transmitter - Coverage radius", new Double(workspace.getVictimSystemLink().getWantedTransmitter().getCoverageRadius()));
        int ii = 0;
        for(int stopi = workspace.getInterferenceLinks().getSize(); ii < stopi; ii++)
        {
            workspace.getRadius().addCoverageRadius((new StringBuilder()).append("Interfering Transmitter ").append(ii + 1).append(" - Coverage radius").toString(), new Double(((InterferenceLink)workspace.getInterferenceLinks().get(ii)).getInterferingLink().getInterferingTransmitter().getCoverageRadius()));
            workspace.getRadius().addCoverageRadius((new StringBuilder()).append("Interfering Transmitter ").append(ii + 1).append(" - Simulation radius").toString(), new Double(((InterferenceLink)workspace.getInterferenceLinks().get(ii)).getInterferingLink().getInterferingTransmitter().getRsimu()));
        }

        if(!isStopped())
        {
            workspace.getSignals().setSignalIsDrssDistribution(dRSSDistrib.getDistrib() != null);
            workspace.getSignals().testIRSSDistributions();
        } else
        {
            workspace.getSignals().setSignalIsDrssDistribution(true);
            workspace.getSignals().setSignalIsIrssDistribution(true);
        }
        if(!isStopped())
        {
            int i = 0;
            k = 0;
            for(j = 1; i < rCorrUnwanted.length; j++)
            {
                if(rCorrUnwanted[k] > rThreshold)
                    workspace.getCorrelations().addCorrelation((new StringBuilder()).append("dRSS with Unwanted ").append(Integer.toString(j)).toString(), new Double(rCorrUnwanted[k]));
                i++;
                k++;
            }

            i = 0;
            k = 0;
            for(j = 1; i < rCorrBlocking.length; j++)
            {
                if(rCorrBlocking[k] > rThreshold)
                    workspace.getCorrelations().addCorrelation((new StringBuilder()).append("dRSS with Blocking ").append(Integer.toString(j)).toString(), new Double(rCorrBlocking[k]));
                i++;
                k++;
            }

            i = 0;
            for(k = 0; i < rCorrIntermodular.length; k++)
            {
                for(j = 1; j <= iCount; j++)
                    if(rCorrIntermodular[k] > rThreshold && i != j)
                        workspace.getCorrelations().addCorrelation((new StringBuilder()).append("dRSS with Intermodulation(").append(Integer.toString(i)).append("/").append(Integer.toString(j)).append(")").toString(), new Double(rCorrIntermodular[k]));

                i++;
            }

            k = 0;
            for(i = 0; i < 2 * iCount + iCount * (iCount - 1); i++)
            {
                String sFirst;
                if(i < 2 * iCount)
                {
                    if(i % 2 == 0)
                        sFirst = (new StringBuilder()).append("Unwanted").append(Integer.toString(i / 2 + 1)).toString();
                    else
                        sFirst = (new StringBuilder()).append("Blocking").append(Integer.toString(i / 2 + 1)).toString();
                } else
                {
                    int m = i - 2 * iCount;
                    int n = m / (iCount - 1) + 1;
                    int l = m % (iCount - 1) + 1;
                    if(l >= n)
                        l++;
                    sFirst = (new StringBuilder()).append("Intermodulation (").append(Integer.toString(n)).append("/").append(Integer.toString(l)).append(")").toString();
                }
                for(j = 0; j < 2 * iCount + iCount * (iCount - 1); j++)
                {
                    String sSecond;
                    if(j < 2 * iCount)
                    {
                        if(j % 2 == 0)
                            sSecond = (new StringBuilder()).append("Unwanted").append(Integer.toString(j / 2 + 1)).toString();
                        else
                            sSecond = (new StringBuilder()).append("Blocking").append(Integer.toString(j / 2 + 1)).toString();
                    } else
                    {
                        int m = j - 2 * iCount;
                        int n = m / (iCount - 1) + 1;
                        int l = m % (iCount - 1) + 1;
                        if(l >= n)
                            l++;
                        sSecond = (new StringBuilder()).append("Intermodulation (").append(Integer.toString(n)).append("/").append(Integer.toString(l)).append(")").toString();
                    }
                    if(i >= j)
                        continue;
                    if(k < rCorrIntermodular.length && rCorrIntermodular[k] > rThreshold)
                        workspace.getCorrelations().addCorrelation((new StringBuilder()).append(sFirst).append(" with ").append(sSecond).toString(), new Double(rCorrIntermodular[k]));
                    k++;
                }

            }

        }
    }

    public boolean isStopped()
    {
        return !generateEvents;
    }

    public void stop()
    {
        generateEvents = false;
        if(workspace.hasCDMASubSystem())
        {
            if(workspace.getVictimSystemLink().isCDMASystem())
                workspace.getVictimSystemLink().getCDMASystem().setStopped(true);
            Iterator i$ = workspace.getInterferenceLinks().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                InterferenceLink link = (InterferenceLink)i$.next();
                if(link.isCDMASystem())
                    link.getCDMASystem().setStopped(true);
            } while(true);
        }
    }

    private void notifyListenersEventCompleted(int event, int total)
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            ((EventCompletionListener)eventCompletionListeners.get(i)).eventCompleted(event, total);

    }

    private void notifyListenersUpdateStatus(String status)
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            ((EventCompletionListener)eventCompletionListeners.get(i)).updateStatus(status);

    }

    private void notifyListenersGenerationAndEvaluationComplete()
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            ((EventCompletionListener)eventCompletionListeners.get(i)).generationAndEvaluationComplete();

    }

    private void notifyListenersEventGenerationCompleted(int count)
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            try
            {
                ((EventCompletionListener)eventCompletionListeners.get(i)).eventGenerationCompleted(count);
            }
            catch(Exception e)
            {
                LOG.error(e);
            }

    }

    private void notifyListenersStartingEventGeneration(Workspace workspace, int eventsToBeCalculated, int eventStartIndex)
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            ((EventCompletionListener)eventCompletionListeners.get(i)).startingEventGeneration(workspace, eventsToBeCalculated, eventStartIndex);

    }

    void notifyListenersSetCurrentProcessCompletionPercentage(int value)
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            ((EventCompletionListener)eventCompletionListeners.get(i)).setCurrentProcessCompletionPercentage(value);

    }

    void notifyListenersSetTotalProcessCompletionPercentage(int value)
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            ((EventCompletionListener)eventCompletionListeners.get(i)).setTotalProcessCompletionPercentage(value);

    }

    void notifyListenersIncrementCurrentProcessCompletionPercentage(int value)
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            ((EventCompletionListener)eventCompletionListeners.get(i)).incrementCurrentProcessCompletionPercentage(value);

    }

    void notifyListenersIncrementTotalProcessCompletionPercentage(int value)
    {
        int i = 0;
        for(int size = eventCompletionListeners.size(); i < size; i++)
            ((EventCompletionListener)eventCompletionListeners.get(i)).incrementTotalProcessCompletionPercentage(value);

    }

    void notifyListenersError(String value)
    {
        EventCompletionListener listener;
        for(Iterator i$ = eventCompletionListeners.iterator(); i$.hasNext(); listener.notifyError(value))
            listener = (EventCompletionListener)i$.next();

    }

    boolean notifyListenersOfException(Exception e, int eventId)
    {
        boolean abort = false;
        for(Iterator i$ = exceptionListeners.iterator(); i$.hasNext();)
        {
            EgeExceptionListener listener = (EgeExceptionListener)i$.next();
            abort = abort || listener.notifyException(e, eventId);
        }

        return abort;
    }

    private void notifyPositionListenersWantedReceiver(double x, double y)
    {
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Wanted Receiver position: (").append(x).append(",").append(y).append(")").toString());
        int i = 0;
        for(int size = positionListeners.size(); i < size; i++)
            ((PositionListener)positionListeners.get(i)).addWantedReceiver(x, y);

    }

    private void notifyPositionListenersWantedTransmitter(double x, double y)
    {
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Wanted Transmitter position: (").append(x).append(",").append(y).append(")").toString());
        int i = 0;
        for(int size = positionListeners.size(); i < size; i++)
            ((PositionListener)positionListeners.get(i)).addWantedTransmitter(x, y);

    }

    private void notifyPositionListenersVictimReceiver(double x, double y)
    {
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Victim Receiver position: (").append(x).append(",").append(y).append(")").toString());
        int i = 0;
        for(int size = positionListeners.size(); i < size; i++)
            ((PositionListener)positionListeners.get(i)).addVictimReceiver(x, y);

    }

    private void notifyPositionListenersInterferingTransmitter(double x, double y)
    {
        if(debugMode)
            LOG.debug((new StringBuilder()).append("Interfering Transmitter position: (").append(x).append(",").append(y).append(")").toString());
        int i = 0;
        for(int size = positionListeners.size(); i < size; i++)
            ((PositionListener)positionListeners.get(i)).addInterferingTransmitter(x, y);

    }

    public void resetPositionListeners()
    {
        int i = 0;
        for(int size = positionListeners.size(); i < size; i++)
            ((PositionListener)positionListeners.get(i)).clearAllElements();

    }

    public void setDebugMode(boolean value)
    {
        debugMode = value;
    }

    public boolean isInDebugMode()
    {
        return debugMode;
    }

    public void setTotalNumberOfEventsGenerated(int totalNumberOfEventsGenerated)
    {
        this.totalNumberOfEventsGenerated = totalNumberOfEventsGenerated;
    }

    public void startingTest(int usersPrCell, int deltaUsers, int trials, boolean uplink)
    {
        if(uplink)
            notifyListenersUpdateStatus((new StringBuilder()).append("Testing with ").append(usersPrCell).append(" users pr cell for ").append(trials).append(" trials (last avg. noise rise was ").append(lastSuccessRate).append(" dB)").toString());
        else
            notifyListenersUpdateStatus((new StringBuilder()).append("Testing with ").append(usersPrCell).append(" users pr cell for ").append(trials).append(" trials (last number of successful trials was ").append((int)lastSuccessRate).append(")").toString());
    }

    public void capacityFound(int usersPrCell, double successRate, boolean uplink)
    {
        if(uplink)
            notifyListenersUpdateStatus((new StringBuilder()).append("Found non interfered capacity to be ").append(usersPrCell).append(" users pr cell ").append("(last avg. noise rise was ").append(successRate).append(" dB)").toString());
        else
            notifyListenersUpdateStatus((new StringBuilder()).append("Found non interfered capacity to be ").append(usersPrCell).append(" users pr cell (last number of successful trials was ").append((int)successRate).append(")").toString());
    }

    public double getTimeLimit()
    {
        return (double)timeLimit;
    }

    public void setTimeLimit(double timeLimit)
    {
        this.timeLimit = (long)(timeLimit * 60D * 1000D);
    }

    public boolean isTimeLimited()
    {
        return timeLimited;
    }

    public void setTimeLimited(boolean timeLimited)
    {
        this.timeLimited = timeLimited;
    }

    public int getCurrentNumbeOfEvents()
    {
        return currentNumbeOfEvents;
    }

    public void startingTrial(int i)
    {
    }

    public void endingTest(int usersPerCell, double successRate, boolean uplink, int trialsPerformed)
    {
        lastSuccessRate = successRate;
    }

    public void endingTrial(double d, boolean flag, int i)
    {
    }

    public void startingCapacityFinding(int i, int j, double d, int k, boolean flag, double d1)
    {
    }

    public boolean isUseHigherPriorityThreads()
    {
        return useHigherPriorityThreads;
    }

    public void setUseHigherPriorityThreads(boolean useLowPriorityThreads)
    {
        useHigherPriorityThreads = useLowPriorityThreads;
    }

    public boolean isInServerMode()
    {
        return inServerMode;
    }

    public void setInServerMode(boolean inServerMode)
    {
        this.inServerMode = inServerMode;
    }

    public boolean isUseTestCDMAAlgorithm()
    {
        return useTestCDMAAlgorithm;
    }

    public void setUseTestCDMAAlgorithm(boolean useTestCDMAAlgorithm)
    {
        this.useTestCDMAAlgorithm = useTestCDMAAlgorithm;
    }

    private static Logger LOG = Logger.getLogger(org/seamcat/model/engines/EGE);
    private File logfile;
    private Appender logfileAppender;
    private Workspace workspace;
    private List eventCompletionListeners;
    private List positionListeners;
    private List exceptionListeners;
    private boolean initialized;
    private boolean generateEvents;
    private int numberOfEvents;
    private int totalNumberOfEventsGenerated;
    private int currentNumbeOfEvents;
    private boolean debugMode;
    private double lastSuccessRate;
    private boolean timeLimited;
    private long timeLimit;
    private boolean stoppedByTimeLimit;
    private boolean useHigherPriorityThreads;
    private boolean inServerMode;
    private boolean useTestCDMAAlgorithm;

}
