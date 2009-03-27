// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CdmaEngine.java

package org.seamcat.model.engines;

import java.util.Iterator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.seamcat.cdma.*;
import org.seamcat.model.*;
import org.seamcat.model.core.*;
import org.seamcat.presentation.MainWindow;
import org.seamcat.presentation.SeamcatDistributionPlot;

// Referenced classes of package org.seamcat.model.engines:
//            EGE

public class CdmaEngine
    implements NonInterferedCapacityListener, InterferenceGenerator
{

    public CdmaEngine(Workspace _wks)
    {
        listener = MainWindow.getInstance().getChartWindow().getNonInterferedCapacityListener();
        workspace = _wks;
    }

    public void startSimulation()
        throws Exception
    {
        if(workspace.getVictimSystemLink().isCDMASystem())
        {
            CDMASystem victim = workspace.getVictimSystemLink().getCDMASystem();
            victim.setSimulateCapacity(false);
            LOG.setLevel(Level.INFO);
            workspace.getEge().startGeneration();
            MainWindow.getInstance().getChartWindow().showCDMACapacityPanel();
            victim.setSimulateCapacity(true);
            victim.setNumberOfTrials(workspace.getEge().getNumberOfEvents());
            victim.findNonInterferedCapacity(this, null, false);
            int n_baseline = victim.getInternalUserPerCell();
            LOG.info((new StringBuilder()).append("N_Baseline = ").append(n_baseline).append(" (success = ").append(victim.getSuccesRate() / (double)victim.getNumberOfTrials()).append(")").toString());
            victim.setCapacitySimulated(false);
            victim.setUsersPerCell(n_baseline);
            victim.findNonInterferedCapacity(this, this, true);
            int n_interfered = victim.getInternalUserPerCell();
            LOG.info((new StringBuilder()).append("N_Interfered = ").append(n_interfered).append(" (success = ").append(victim.getSuccesRate() / (double)victim.getNumberOfTrials()).append(")").toString());
            LOG.info((new StringBuilder()).append("Capacity loss: ").append(((double)(n_baseline - n_interfered) / (double)n_baseline) * 100D).append(" %").toString());
        }
    }

    public void startingCapacityFinding(int usersPrCell, int deltaUsers, double allowableOutage, int trials, boolean uplink, double target)
    {
        LOG.info((new StringBuilder()).append("Starting capacity finding [N=").append(usersPrCell).append(",deltaN=").append(deltaUsers).append(",NumberOfTrials=").append(trials).append("]").toString());
        listener.startingCapacityFinding(usersPrCell, deltaUsers, allowableOutage, trials, uplink, target);
    }

    public void startingTest(int usersPrCell, int deltaUsers, int trials, boolean uplink)
    {
        LOG.info((new StringBuilder()).append("Starting test [N=").append(usersPrCell).append(",deltaN=").append(deltaUsers).append(",NumberOfTrials=").append(trials).append("]").toString());
        listener.startingTest(usersPrCell, deltaUsers, trials, uplink);
    }

    public void endingTest(int usersPerCell, double successRate, boolean uplink, int numberOfTrials)
    {
        listener.endingTest(usersPerCell, successRate, uplink, numberOfTrials);
    }

    public void capacityFound(int usersPrCell, double successRate, boolean uplink)
    {
        listener.capacityFound(usersPrCell, successRate, uplink);
    }

    public void startingTrial(int trialid)
    {
        listener.startingTrial(trialid);
    }

    public void endingTrial(double outage, boolean success, int trialid)
    {
        listener.endingTrial(outage, success, trialid);
    }

    public void generateInterference(CDMASystem cdma)
    {
        try
        {
            for(Iterator i$ = workspace.getInterferenceLinks().iterator(); i$.hasNext();)
            {
                InterferenceLink interferenceLink = (InterferenceLink)i$.next();
                boolean debugMode = false;
                interferenceLink.setInterferingLink(interferenceLink.getInterferingLink());
                interferenceLink.getUnwantedInterference().setInterferenceLink(interferenceLink);
                interferenceLink.getBlockingInterference().setInterferenceLink(interferenceLink);
                int z = 0;
                while(z < interferenceLink.getInterferingLink().getInterferingTransmitter().getNbActiveTx()) 
                {
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
                    double x = workspace.getVictimSystemLink().getCDMASystem().getReferenceCell().getLocationX() + interferenceLink.getInterferingLink().getInterferingTransmitter().getX();
                    double y = workspace.getVictimSystemLink().getCDMASystem().getReferenceCell().getLocationY() + interferenceLink.getInterferingLink().getInterferingTransmitter().getY();
                    double power = interferenceLink.getInterferingLink().getInterferingTransmitter().getTxTrialPower();
                    if(interferenceLink.getInterferingLink().getInterferingTransmitter().getUsePowerControl())
                        power += interferenceLink.getInterferingLink().getInterferingTransmitter().getPowerControl().getInterferingTransmitterPowerControlGain();
                    CDMAInterferer interferer = new CDMAInterferer(x, y, power, interferenceLink.getWt2VrPath().getPropagationModel(), interferenceLink.getInterferingLink().getInterferingTransmitter().getItTrialFrequency(), interferenceLink.getInterferingLink().getInterferingTransmitter().getTxTrialAntHeight(), interferenceLink.getInterferingLink().getInterferingTransmitter().getAntenna(), interferenceLink.getInterferingLink().getInterferingTransmitter().getUnwantedEmissions(), workspace.getVictimSystemLink().getVictimReceiver().getBlockingResponse(), cdma);
                    cdma.addExternalInterferer(interferer);
                    z++;
                }
            }

        }
        catch(Exception e)
        {
            LOG.info("Error on CDMA callback", e);
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/engines/CdmaEngine);
    private Workspace workspace;
    private NonInterferedCapacityListener listener;

}
