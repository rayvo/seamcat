// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferingLinkCheck.java

package org.seamcat.model.scenariocheck;

import java.util.Iterator;
import org.seamcat.distribution.Bounds;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.Function2;
import org.seamcat.model.*;
import org.seamcat.model.core.*;

// Referenced classes of package org.seamcat.model.scenariocheck:
//            AbstractCheck, ScenarioCheckResult

public class InterferingLinkCheck extends AbstractCheck
{

    public InterferingLinkCheck()
    {
        result.setCheckName("Interfering Link");
    }

    public ScenarioCheckResult check(Workspace workspace)
    {
        VictimSystemLink vsl = workspace.getVictimSystemLink();
        boolean bFreqVrBounded = vsl.getFrequency().getBounds().isBounded();
        double rFreqVrMin = vsl.getFrequency().getBounds().getMin();
        double rFreqVrMax = vsl.getFrequency().getBounds().getMax();
        Iterator i$ = workspace.getInterferenceLinks().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            InterferenceLink il = (InterferenceLink)i$.next();
            InterferingSystemLink isl = il.getInterferingLink();
            InterferingTransmitter it = isl.getInterferingTransmitter();
            WantedReceiver wr = isl.getWantedReceiver();
            TransmitterToReceiverPath wt2vrPath = isl.getWt2VrPath();
            checkDistribution(it.getPowerSuppliedDistribution(), il.getReference(), "Interfering Transmitter: Power supplied");
            checkDistribution(it.getAntennaHeight(), il.getReference(), "Interfering Transmitter: Antenna Height");
            checkDistribution(it.getFrequency(), il.getReference(), "Interfering Transmitter: Frequency");
            checkDistribution(wr.getAntennaHeight(), il.getReference(), "Interfering Transmitter: Antenna Height");
            checkDistribution(wt2vrPath.getPathDistanceFactor(), il.getReference(), "It->Vr: Path Distance");
            checkDistribution(wt2vrPath.getPathAzimuth(), il.getReference(), "It->Vr: Path Azimuth");
            checkDistribution(wt2vrPath.getPathAzimuth(), il.getReference(), "Interference Link: TxRx Angle");
            checkDistribution(wt2vrPath.getPathDistanceFactor(), il.getReference(), "Interference Link: TxRx Distance");
            if(il.getCorrelationMode() == 0)
                checkDistribution(wt2vrPath.getPathDistanceFactor(), il.getReference(), "Undefined");
            double rUnwantedFloorMin = 0.0D;
            double rUnwantedFloorMax = 0.0D;
            boolean bFreqItBounded = false;
            boolean bUnwantedBounded = false;
            boolean bUnwantedFloorBounded = false;
            Bounds bounds = it.getFrequency().getBounds();
            double rFreqItMin = bounds.getMin();
            double rFreqItMax = bounds.getMax();
            bFreqItBounded = bounds.isBounded();
            bounds = it.getUnwantedEmissions().getBounds();
            double rUnwantedMin = bounds.getMin();
            double rUnwantedMax = bounds.getMax();
            bUnwantedBounded = bounds.isBounded();
            if(it.getUseUnwantedEmissionFloor())
            {
                bounds = it.getUnwantedEmissionsFloor().getBounds();
                rUnwantedFloorMin = bounds.getMin();
                rUnwantedFloorMax = bounds.getMax();
                bUnwantedFloorBounded = bounds.isBounded();
            }
            if(bFreqItBounded)
            {
                checkPropagationModel(il.getWt2VrPath().getPropagationModel(), rFreqItMin, rFreqItMax, il.getReference());
                checkPropagationModel(wt2vrPath.getPropagationModel(), rFreqItMin, rFreqItMax, il.getReference());
            } else
            {
                addErrorMsg((new StringBuilder()).append("Unbounded frequency distribution in ").append(il.getReference()).toString());
            }
            double rBlockingMin = 0.0D;
            double rBlockingMax = 0.0D;
            boolean bBlockingBounded = false;
            double rIntermodMin = 0.0D;
            double rIntermodMax = 0.0D;
            boolean bIntermodBounded = false;
            double rBVr = il.getVictimLink().getVictimReceiver().getBandwidth();
            if(bFreqItBounded && bFreqVrBounded)
            {
                if(bBlockingBounded && (rFreqItMax - rFreqVrMin > rBlockingMax || rFreqItMin - rFreqVrMax < rBlockingMin))
                    addErrorMsg((new StringBuilder()).append("Blocking response range (").append(rBlockingMin).append(", ").append(rBlockingMin).append(")does not match interfering transmitter frequency range (").append(rFreqItMin).append(", ").append(rFreqItMax).append(")<br>and victim receiver frequency range (").append(rFreqVrMin).append(", ").append(rFreqVrMax).append(") in ").append("Interfering Link").toString());
                if(bIntermodBounded && (rFreqItMax - rFreqVrMin > rIntermodMax || rFreqItMin - rFreqVrMax < rIntermodMin))
                    addErrorMsg((new StringBuilder()).append("Intermodulation rejection range (").append(rIntermodMin).append(", ").append(rIntermodMax).append(") does not match interfering transmitter frequency range (").append(rFreqItMin).append(", ").append(rFreqItMax).append(") <br>and victim receiver frequency range (").append(rFreqVrMin).append(", ").append(rFreqVrMax).append(") in ").append("Interfering Link").toString());
                if(bUnwantedBounded)
                    if(rUnwantedMax < rUnwantedMin)
                        addErrorMsg((new StringBuilder()).append("Unwanted emissions range is not valid: [").append(rUnwantedMin).append(" to ").append(rUnwantedMax).append("]").toString());
                    else
                    if((rFreqVrMax - rFreqItMin) + rBVr / 2D > rUnwantedMax || rFreqVrMin - rFreqItMax - rBVr / 2D < rUnwantedMin)
                        addErrorMsg((new StringBuilder()).append("Unwanted emissions range (").append(rUnwantedMin).append(", ").append(rUnwantedMax).append(") does not match interfering transmitter frequency range (").append(rFreqItMin).append(", ").append(rFreqItMax).append(")<br>and victim receiver frequency range (").append(rFreqVrMin).append(", ").append(rFreqVrMax).append(") +/- receiver bandwidth (").append(rBVr).append(") in ").append("Interfering Link").toString());
                if(it.getUseUnwantedEmissionFloor() && bUnwantedFloorBounded && ((rFreqVrMax - rFreqItMin) + rBVr / 1000D / 2D > rUnwantedFloorMax || rFreqVrMin - rFreqItMax - rBVr / 1000D / 2D < rUnwantedFloorMin))
                    addErrorMsg((new StringBuilder()).append("Unwanted emissions floor range (").append(rUnwantedMin).append(", ").append(rUnwantedMax).append(") does not match interfering transmitter frequency range (").append(rFreqItMin).append(", ").append(rFreqItMax).append(")<br>and victim receiver frequency range (").append(rFreqVrMin).append(", ").append(rFreqVrMax).append(") +/- receiver bandwidth (").append(rBVr / 1000D).append(") in ").append("Interfering Link").append("</li>").toString());
            }
        } while(true);
        return result;
    }

    public volatile void addErrorMsg(String x0)
    {
        super.addErrorMsg(x0);
    }

    private static final String LINK = "Interfering Link";
}
