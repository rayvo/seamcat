// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioInfo_Impl.java

package org.seamcat.postprocessing;

import java.util.*;
import org.seamcat.distribution.Bounds;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.model.*;
import org.seamcat.model.core.*;
import org.seamcat.model.plugin.*;

// Referenced classes of package org.seamcat.postprocessing:
//            VictimLink_Impl, Transceiver_Impl, PropagationModel_Impl, VictimReceiver_Impl, 
//            InterferingLink_Impl, InterferingTransmitter_Impl, ProcessingResult_Impl

public class ScenarioInfo_Impl
    implements ScenarioInfo
{

    public ScenarioInfo_Impl(Workspace workspace)
    {
        interferingLinks = new ArrayList();
        vl = new VictimLink_Impl(workspace.getVictimSystemLink().getUseWantedTransmitter());
        if(vl.useWantedTransmitter())
        {
            vl.setPropagationModel(PropagationModel_Impl.create(workspace.getVictimSystemLink().getWt2VrPath().getPropagationModel()));
            Transceiver_Impl transmitter = (Transceiver_Impl)getVictimLink().getTransmitter();
            transmitter.setUseHorizontalPattern(workspace.getVictimSystemLink().getWantedTransmitter().getAntenna().getUseHorizontalPattern());
            transmitter.setHorizontalAntennaPattern(workspace.getVictimSystemLink().getWantedTransmitter().getAntenna().getHorizontalPattern().getPattern());
            transmitter.setUseVerticalPattern(workspace.getVictimSystemLink().getWantedTransmitter().getAntenna().getUseVerticalPattern());
            transmitter.setVerticalAntennaPattern(workspace.getVictimSystemLink().getWantedTransmitter().getAntenna().getVerticalPattern().getPattern());
            transmitter.setUseSphericalPattern(workspace.getVictimSystemLink().getWantedTransmitter().getAntenna().getUseSphericalPattern());
            transmitter.setSphericalAntennaPattern(workspace.getVictimSystemLink().getWantedTransmitter().getAntenna().getSphericalPattern().getPattern());
            transmitter.setAntennaGain(workspace.getVictimSystemLink().getWantedTransmitter().getAntenna().getPeakGain());
        } else
        {
            vl.setPropagationModel(new PropagationModel_Impl(org.seamcat.model.plugin.PropagationModel.AssumedModel.NoModelDefined));
        }
        try
        {
            vl.setFrequencyMax(workspace.getVictimSystemLink().getFrequency().getBounds().getMax());
            vl.setFrequencyMin(workspace.getVictimSystemLink().getFrequency().getBounds().getMin());
        }
        catch(RuntimeException ex)
        {
            vl.setFrequencyMax(-1D);
            vl.setFrequencyMin(-1D);
        }
        VictimReceiver_Impl receiver = (VictimReceiver_Impl)getVictimLink().getReceiver();
        receiver.setUseHorizontalPattern(workspace.getVictimSystemLink().getVictimReceiver().getAntenna().getUseHorizontalPattern());
        receiver.setHorizontalAntennaPattern(workspace.getVictimSystemLink().getVictimReceiver().getAntenna().getHorizontalPattern().getPattern());
        receiver.setUseVerticalPattern(workspace.getVictimSystemLink().getVictimReceiver().getAntenna().getUseVerticalPattern());
        receiver.setVerticalAntennaPattern(workspace.getVictimSystemLink().getVictimReceiver().getAntenna().getVerticalPattern().getPattern());
        receiver.setUseSphericalPattern(workspace.getVictimSystemLink().getVictimReceiver().getAntenna().getUseSphericalPattern());
        receiver.setSphericalAntennaPattern(workspace.getVictimSystemLink().getVictimReceiver().getAntenna().getSphericalPattern().getPattern());
        receiver.setAntennaGain(workspace.getVictimSystemLink().getVictimReceiver().getAntenna().getPeakGain());
        receiver.setBandwith(workspace.getVictimSystemLink().getVictimReceiver().getBandwidth());
        receiver.setSensitivity(workspace.getVictimSystemLink().getVictimReceiver().getSensitivity());
        receiver.setRequiredCIRatio(workspace.getVictimSystemLink().getVictimReceiver().getCiLevel());
        receiver.setRequiredNINRatio(workspace.getVictimSystemLink().getVictimReceiver().getNinLevel());
        receiver.setRequiredCNIRatio(workspace.getVictimSystemLink().getVictimReceiver().getCniLevel());
        receiver.setRequiredINRatio(workspace.getVictimSystemLink().getVictimReceiver().getInLevel());
        receiver.setUsingPowerControl(workspace.getVictimSystemLink().getVictimReceiver().getUsePowerControlThreshold());
        receiver.setBlocking(workspace.getVictimSystemLink().getVictimReceiver().getBlockingResponse());
        int linkCount = 0;
        for(Iterator i$ = workspace.getInterferenceLinks().iterator(); i$.hasNext();)
        {
            InterferenceLink il = (InterferenceLink)i$.next();
            linkCount++;
            int i = 0;
            int stop = il.getInterferingLink().getInterferingTransmitter().getNbActiveTx();
            while(i < stop) 
            {
                InterferingLink_Impl l = new InterferingLink_Impl();
                l.setSublink(i > 0);
                l.setLinkIndex(linkCount + i);
                l.setOrigLinkIndex(linkCount);
                l.setPropagationModel(PropagationModel_Impl.create(il.getInterferingLink().getWt2VrPath().getPropagationModel()));
                l.setPropagationModelVR(PropagationModel_Impl.create(il.getWt2VrPath().getPropagationModel()));
                try
                {
                    l.setFrequencyMax(il.getInterferingLink().getInterferingTransmitter().getFrequency().getBounds().getMax());
                    l.setFrequencyMin(il.getInterferingLink().getInterferingTransmitter().getFrequency().getBounds().getMin());
                }
                catch(RuntimeException ex)
                {
                    l.setFrequencyMax(-1D);
                    l.setFrequencyMin(-1D);
                }
                Transceiver_Impl rec = (Transceiver_Impl)l.getReceiver();
                rec.setUseHorizontalPattern(il.getInterferingLink().getWantedReceiver().getAntenna().getUseHorizontalPattern());
                rec.setHorizontalAntennaPattern(il.getInterferingLink().getWantedReceiver().getAntenna().getHorizontalPattern().getPattern());
                rec.setUseVerticalPattern(il.getInterferingLink().getWantedReceiver().getAntenna().getUseVerticalPattern());
                rec.setVerticalAntennaPattern(il.getInterferingLink().getWantedReceiver().getAntenna().getVerticalPattern().getPattern());
                rec.setUseSphericalPattern(il.getInterferingLink().getWantedReceiver().getAntenna().getUseSphericalPattern());
                rec.setSphericalAntennaPattern(il.getInterferingLink().getWantedReceiver().getAntenna().getSphericalPattern().getPattern());
                rec.setAntennaGain(il.getInterferingLink().getWantedReceiver().getAntenna().getPeakGain());
                InterferingTransmitter_Impl trans = (InterferingTransmitter_Impl)l.getTransmitter();
                trans.setUnwantedFunction((DiscreteFunction2)il.getInterferingLink().getInterferingTransmitter().getUnwantedEmissions());
                trans.setUseHorizontalPattern(il.getInterferingLink().getInterferingTransmitter().getAntenna().getUseHorizontalPattern());
                trans.setHorizontalAntennaPattern(il.getInterferingLink().getInterferingTransmitter().getAntenna().getHorizontalPattern().getPattern());
                trans.setUseVerticalPattern(il.getInterferingLink().getInterferingTransmitter().getAntenna().getUseVerticalPattern());
                trans.setVerticalAntennaPattern(il.getInterferingLink().getInterferingTransmitter().getAntenna().getVerticalPattern().getPattern());
                trans.setUseSphericalPattern(il.getInterferingLink().getInterferingTransmitter().getAntenna().getUseSphericalPattern());
                trans.setSphericalAntennaPattern(il.getInterferingLink().getInterferingTransmitter().getAntenna().getSphericalPattern().getPattern());
                trans.setAntennaGain(il.getInterferingLink().getInterferingTransmitter().getAntenna().getPeakGain());
                interferingLinks.add(l);
                i++;
            }
        }

        results = new ProcessingResult_Impl(interferingLinks.size(), linkCount);
        intermodulation = new double[linkCount * (linkCount - 1) + 1];
        totalEventCount = workspace.getControl().getEgData().getNumberOfEvents();
    }

    public int getNumberOfInterferingLinks()
    {
        return interferingLinks.size();
    }

    public List getInterferingLinks()
    {
        return Collections.unmodifiableList(interferingLinks);
    }

    public InterferingLink getInterferingLink(int index)
    {
        return (InterferingLink)interferingLinks.get(index - 1);
    }

    public VictimLink getVictimLink()
    {
        return vl;
    }

    public int getCurrentEventNumber()
    {
        return currentEvent;
    }

    public ProcessingResult getProcessingResults()
    {
        return results;
    }

    public void setCurrentEvent(int currentEvent)
    {
        this.currentEvent = currentEvent + 1;
    }

    public double getIRSSIntermodulation(int firstLinkIndex, int secondLinkIndex)
    {
        int index;
        if(firstLinkIndex < secondLinkIndex)
            index = firstLinkIndex * secondLinkIndex - (firstLinkIndex - (linkCount - secondLinkIndex));
        else
            index = linkCount * firstLinkIndex - ((firstLinkIndex - 1) + (linkCount - secondLinkIndex));
        return intermodulation[index];
    }

    public double[] getIntermodulation()
    {
        return intermodulation;
    }

    public int getTotalNumberOfEvents()
    {
        return totalEventCount;
    }

    public void requestSimulationStop()
    {
        results.setStopRequested(true);
    }

    public void addConsistencyWarning(String description)
    {
        if(consistencyWarnings == null)
            consistencyWarnings = new ArrayList();
        consistencyWarnings.add(description);
    }

    public List getConsistencyWarnings()
    {
        return consistencyWarnings;
    }

    private VictimLink_Impl vl;
    private List interferingLinks;
    private int currentEvent;
    private double intermodulation[];
    private int linkCount;
    private int totalEventCount;
    private List consistencyWarnings;
    private ProcessingResult_Impl results;
}
