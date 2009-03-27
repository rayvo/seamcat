// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VictimLinkCheck.java

package org.seamcat.model.scenariocheck;

import java.util.Iterator;
import org.seamcat.distribution.Bounds;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Function;
import org.seamcat.model.*;
import org.seamcat.model.core.*;

// Referenced classes of package org.seamcat.model.scenariocheck:
//            AbstractCheck, ScenarioCheckResult

public class VictimLinkCheck extends AbstractCheck
{

    public VictimLinkCheck()
    {
        result.setCheckName("Victim Link");
    }

    public ScenarioCheckResult check(Workspace workspace)
    {
        VictimSystemLink vsl = workspace.getVictimSystemLink();
        if(!vsl.isCDMASystem())
        {
            Bounds bounds = vsl.getFrequency().getBounds();
            double rFreqVrMin = bounds.getMin();
            double rFreqVrMax = bounds.getMax();
            boolean bFreqVrBounded = bounds.isBounded();
            TransmitterToReceiverPath wt2vrPath = vsl.getWt2VrPath();
            if(bFreqVrBounded)
            {
                if(vsl.getUseWantedTransmitter())
                    checkPropagationModel(wt2vrPath.getPropagationModel(), rFreqVrMin, rFreqVrMax, "Victim Link");
                Function function = vsl.getVictimReceiver().getBlockingResponse();
                if(function instanceof DiscreteFunction)
                {
                    DiscreteFunction func = (DiscreteFunction)function;
                    double min = func.getRangeMin();
                    double max = func.getRangeMax();
                    Iterator i$ = workspace.getInterferenceLinks().iterator();
                    do
                    {
                        if(!i$.hasNext())
                            break;
                        InterferenceLink link = (InterferenceLink)i$.next();
                        Distribution freq = link.getInterferingLink().getInterferingTransmitter().getFrequency();
                        Bounds itbounds = freq.getBounds();
                        if(itbounds.isBounded())
                            if(rFreqVrMin == rFreqVrMax && itbounds.getMax() == itbounds.getMin())
                            {
                                if(min > rFreqVrMax - itbounds.getMax() || Math.abs(rFreqVrMax - itbounds.getMax()) > max)
                                    addErrorMsg((new StringBuilder()).append("Frequency difference between Victim Link and Interfering Link [").append(link.getReference()).append("] does not fall with in defined blocking function").toString());
                            } else
                            {
                                if(min > rFreqVrMax - itbounds.getMin() || Math.abs(rFreqVrMax - itbounds.getMin()) > max)
                                    addErrorMsg((new StringBuilder()).append("Frequency difference between Victim Link and Interfering Link [").append(link.getReference()).append("] does not fall with in defined blocking function").toString());
                                if(min > rFreqVrMin - itbounds.getMax() || Math.abs(rFreqVrMin - itbounds.getMax()) > max)
                                    addErrorMsg((new StringBuilder()).append("Frequency difference between Victim Link and Interfering Link [").append(link.getReference()).append("] does not fall with in defined blocking function").toString());
                            }
                    } while(true);
                }
            } else
            {
                addErrorMsg("Unbounded frequency distribution in victim link");
            }
            VictimReceiver vr = vsl.getVictimReceiver();
            double rCI = vr.getCiLevel();
            double rCNI = vr.getCniLevel();
            if(rCI < rCNI)
                addErrorMsg("C/I cannot be lower than C/N+I");
            double rNIN = vr.getNinLevel();
            if(rCI <= rCNI || Math.abs(rNIN - 10D * Math.log10(1.0D + 1.0D / (Math.pow(10D, (rCI - rCNI) / 10D) - 1.0D))) > 0.5D)
                addErrorMsg("Inconsistency between C/I, C/N+I and N+I/N thresholds");
            double rNI = vr.getInLevel();
            if(rCI <= rCNI || Math.abs(rNI - 10D * Math.log10(1.0D / (Math.pow(10D, (rCI - rCNI) / 10D) - 1.0D))) > 0.5D)
                addErrorMsg("Inconsistency between C/I, C/N+I and I/N thresholds");
            if(Math.abs(rNIN - 10D * Math.log10(1.0D + Math.pow(10D, rNI / 10D))) > 0.5D)
                addErrorMsg("Inconsistency between N+I/N and I/N thresholds");
            checkDistribution(vsl.getFrequency(), "Victim Link", "Victim Reciever: Frequency");
            checkDistribution(vr.getNoiseFloorDistribution(), "Victim Link", "Victim Reciever: Noise Floor");
            checkDistribution(vr.getAntennaHeight(), "Victim Link", "Victim Reciever: Antenna Height");
            checkDistribution(vsl.getReceiverToTransmitterAzimuth(), "Victim Link", "Victim Reciever: Antenna Azimuth");
            checkDistribution(vsl.getReceiverToTransmitterElevation(), "Victim Link", "Victim Reciever: Antenna Elevation");
            if(!vsl.getUseWantedTransmitter())
            {
                checkDistribution(vsl.getDRSS(), "Victim Link", "dRSS");
            } else
            {
                WantedTransmitter wt = vsl.getWantedTransmitter();
                checkDistribution(vsl.getWantedTransmitter().getPowerSuppliedDistribution(), "Victim Link", "Wanted Transmitter Power");
                checkDistribution(wt.getAntennaHeight(), "Victim Link", "Wanted Transmiter: Antenna Height");
                checkDistribution(vsl.getTransmitterToReceiverAzimuth(), "Victim Link", "Wanted Transmiter: Antenna Azimuth");
                checkDistribution(vsl.getTransmitterToReceiverElevation(), "Victim Link", "Wanted Transmiter: Antenna Elevation");
                if(!vsl.getUseCorrelatedDistance())
                {
                    checkDistribution(vsl.getWt2VrPath().getPathDistanceFactor(), "Victim Link", "Wt2Vr: Path distance");
                    checkDistribution(vsl.getWt2VrPath().getPathAzimuth(), "Victim Link", "Wt2Vr: Path Azimuth");
                }
            }
        }
        return result;
    }

    public volatile void addErrorMsg(String x0)
    {
        super.addErrorMsg(x0);
    }

    private static final String LINK = "Victim Link";
}
