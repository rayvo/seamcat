// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CDMACheck.java

package org.seamcat.model.scenariocheck;

import java.util.Iterator;
import org.seamcat.cdma.CDMALinkLevelData;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.distribution.Bounds;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.Components;
import org.seamcat.model.Workspace;
import org.seamcat.model.core.*;

// Referenced classes of package org.seamcat.model.scenariocheck:
//            AbstractCheck, ScenarioCheckResult

public class CDMACheck extends AbstractCheck
{

    public CDMACheck()
    {
        result.setCheckName("cdma system check");
    }

    public ScenarioCheckResult check(Workspace workspace)
    {
        checkVictimCDMA(workspace);
        checkInterferingCDMA(workspace);
        return result;
    }

    private void checkVictimCDMA(Workspace workspace)
    {
        VictimSystemLink vsl = workspace.getVictimSystemLink();
        if(vsl.isCDMASystem())
        {
            CDMASystem cdma = vsl.getCDMASystem();
            Distribution freq = vsl.getFrequency();
            Bounds bounds = vsl.getFrequency().getBounds();
            double rFreqVrMin = bounds.getMin();
            double rFreqVrMax = bounds.getMax();
            if(!freq.getBounds().isBounded())
                addErrorMsg("Frequency of CDMA System is unbounded for: cdma system");
            else
                checkPropagationModel(cdma.getPropagationModel(), rFreqVrMin, rFreqVrMax, "cdma system");
            if(cdma.getLinkLevelData() == null)
            {
                addErrorMsg("No Link Level Data selected for CDMA System: cdma system");
            } else
            {
                double deltaFreq = Math.abs(freq.trial() - cdma.getLinkLevelData().getFrequency());
                if(deltaFreq > 300D)
                    addErrorMsg((new StringBuilder()).append("Frequency difference between system and link level data for CDMA System:cdma system is ").append(deltaFreq).append(" Mhz. Link level data may not be applicable at this frequency.").toString());
                if((cdma.getLinkLevelData().getLinkType() == org.seamcat.cdma.CDMALinkLevelData.LinkType.UPLINK) != cdma.isUplink())
                    addErrorMsg("Link direction mismatch between link level data and Victim CDMA System");
            }
            if(cdma.isSimulateCapacity())
                if(cdma.getSystemTolerance() < 0.0D)
                {
                    if(cdma.isUplink())
                        addErrorMsg("Target Noise Rise precision is negative!");
                    else
                        addErrorMsg("Initial allowable outage percentage is negative!");
                } else
                if(!cdma.isUplink() && cdma.getSystemTolerance() >= 1.0D)
                    addErrorMsg("Initial Allowed Outage is to high!");
            if(cdma.isUplink())
                if(cdma.getPowerControlConvergenceThreshold() < 0.0D)
                    addErrorMsg("Power Control Convergence Precision is negative!");
                else
                if(cdma.getPowerControlConvergenceThreshold() == 0.0D)
                    addErrorMsg("Power Control Convergence Precision is 0 - this might cause EGE to enter never ending loop");
                else
                if(cdma.getPowerControlConvergenceThreshold() > 0.10000000000000001D)
                    addErrorMsg("Power Control Convergence Precision is more than 0.1 - this is not recommended");
        } else
        if(workspace.getSignals().isVictimIsCdma())
            addErrorMsg("Unable to simulate non CDMA victim on this workspace");
    }

    private void checkInterferingCDMA(Workspace workspace)
    {
        Iterator i$ = workspace.getInterferenceLinks().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            InterferenceLink il = (InterferenceLink)i$.next();
            if(il.isCDMASystem())
            {
                CDMASystem cdma = il.getCDMASystem();
                Distribution freq = il.getInterferingLink().getInterferingTransmitter().getFrequency();
                if(cdma.getLinkLevelData() == null)
                    addErrorMsg((new StringBuilder()).append("No Link Level Data selected for CDMA System: ").append(il.getReference()).toString());
                if(!freq.getBounds().isBounded())
                    addErrorMsg((new StringBuilder()).append("Frequency of CDMA System is unbounded for: ").append(il.getReference()).toString());
                double deltaFreq = Math.abs(freq.trial() - cdma.getLinkLevelData().getFrequency());
                if(deltaFreq > 300D)
                    addErrorMsg((new StringBuilder()).append("Frequency difference between system and link level data for CDMA System:").append(il.getReference()).append(" is ").append(deltaFreq).append(" Mhz. Link level data may not be applicable at this frequency.").toString());
                if((cdma.getLinkLevelData().getLinkType() == org.seamcat.cdma.CDMALinkLevelData.LinkType.UPLINK) != cdma.isUplink())
                    addErrorMsg((new StringBuilder()).append("Link direction mismatch between link level data and CDMA System: ").append(il.getReference()).toString());
            }
        } while(true);
    }

    public volatile void addErrorMsg(String x0)
    {
        super.addErrorMsg(x0);
    }

    private static final String LINK = "cdma system";
}
