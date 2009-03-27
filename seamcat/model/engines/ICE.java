// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ICE.java

package org.seamcat.model.engines;

import java.util.*;
import org.apache.log4j.Logger;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.*;
import org.seamcat.model.*;
import org.seamcat.model.core.*;
import org.seamcat.model.datatypes.*;
import org.seamcat.model.technical.exception.SummationException;

// Referenced classes of package org.seamcat.model.engines:
//            InterferenceCalculationListener, InterferenceCriterionType, ICEConfiguration

public class ICE
    implements Runnable
{

    public ICE(Workspace workspace)
    {
        this.workspace = workspace;
    }

    public void calculateInterference(ICEConfiguration iceconf)
    {
        this.iceconf = iceconf;
        stopped = false;
        if(iceconf.getNumberOfSamples() > workspace.getControl().getEgData().getNumberOfEvents())
            iceconf.setNumberOfSamples(workspace.getControl().getEgData().getNumberOfEvents());
        int validationResult = iceconf.validate(workspace, this);
        if(validationResult == 0)
        {
            iceThread = new Thread(this, THREAD_NAME);
            iceThread.start();
        } else
        {
            if(LOG.isDebugEnabled())
                LOG.error("configuration validation not OK - notify listeners");
            notifyListenersWarningMessage(ICEConfiguration.ERROR[validationResult]);
        }
    }

    public void stop()
    {
        stopped = true;
    }

    public void run()
    {
        if(LOG.isDebugEnabled())
            LOG.debug("Begin ICE run()");
        notifyListenersCalculationStarted();
        if(iceconf == null)
        {
            LOG.error("Ice configuration is null -> throws IllegalStateException");
            throw new IllegalStateException("Unable to start ICE when no ICEConfiguration is specified!");
        }
        if(iceconf.isUnwanted() && ((IRSSVector)workspace.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(0)).getEventVector().isModifiedByPlugin())
            iceconf.setSignalsModifiedByPlugin(true);
        if(iceconf.isBlocking() && ((IRSSVector)workspace.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(0)).getEventVector().isModifiedByPlugin())
            iceconf.setSignalsModifiedByPlugin(true);
        if(iceconf.isIntermodulation() && ((IRSSVector)workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors().get(0)).getEventVector().isModifiedByPlugin())
            iceconf.setSignalsModifiedByPlugin(true);
        if(!iceconf.calculationModeIsTranslation())
        {
            if(LOG.isDebugEnabled())
                LOG.debug("ALGORITHM_COMPLETE1 && !Translation");
            try
            {
                iceComplete1Compatibility(iceconf.isUnwanted(), iceconf.isBlocking(), iceconf.isIntermodulation(), iceconf.getInterferenceCriterionType(), iceconf.getNumberOfSamples());
            }
            catch(SummationException e)
            {
                LOG.error("Exception - notify listeners", e);
                notifyListenersWarningMessage("Interference calculation failed");
            }
        } else
        if(iceconf.calculationModeIsTranslation())
        {
            if(LOG.isDebugEnabled())
                LOG.debug("ALGORITHM_COMPLETE1 && Translation");
            try
            {
                iceComplete1Derivation(iceconf.isUnwanted(), iceconf.isBlocking(), iceconf.isIntermodulation(), iceconf.getInterferenceCriterionType(), iceconf.getNumberOfSamples());
            }
            catch(Exception e)
            {
                LOG.error("Exception - notify listeners", e);
                notifyListenersWarningMessage(ERRORS[0]);
            }
        }
        iceconf.setHasBeenCalculated(true);
        notifyListenersCalculationComplete();
        if(LOG.isDebugEnabled())
            LOG.debug("End ICE run()");
    }

    public boolean isStopped()
    {
        return stopped;
    }

    public Thread getIceThread()
    {
        return iceThread;
    }

    public boolean testProbQuick(double dRSS, double iRSS, double NoiseFloor, InterferenceCriterionType InterferenceCriterion)
        throws SummationException
    {
        double rSens;
        double rCI;
        double rCNI;
        double rINI;
        double rIN;
        rSens = workspace.getVictimSystemLink().getVictimReceiver().getSensitivity();
        rCI = workspace.getVictimSystemLink().getVictimReceiver().getCiLevel();
        rCNI = workspace.getVictimSystemLink().getVictimReceiver().getCniLevel();
        rINI = workspace.getVictimSystemLink().getVictimReceiver().getNinLevel();
        rIN = workspace.getVictimSystemLink().getVictimReceiver().getInLevel();
        InterferenceCriterion.getInterferenceCriterionType();
        JVM INSTR tableswitch 1 4: default 344
    //                   1 112
    //                   2 132
    //                   3 224
    //                   4 303;
           goto _L1 _L2 _L3 _L4 _L5
_L2:
        return dRSS - iRSS > rCI && dRSS > rSens;
_L3:
        double rA = Math.pow(10D, dRSS / 10D);
        double rB = Math.pow(10D, iRSS / 10D);
        double rC = Math.pow(10D, NoiseFloor / 10D);
        if(10D * Math.log10(rA / (rB + rC)) > rCNI && dRSS > rSens)
            return true;
        try
        {
            return false;
        }
        catch(Exception e)
        {
            LOG.error("Exception - throw new SummationException()", e);
        }
        throw new SummationException();
_L4:
        double rB = Math.pow(10D, iRSS / 10D);
        double rC = Math.pow(10D, NoiseFloor / 10D);
        if(10D * Math.log10((rC + rB) / rC) < rINI && dRSS > rSens)
            return true;
        try
        {
            return false;
        }
        catch(Exception e)
        {
            LOG.error("Exception - throw new SummationException()", e);
        }
        throw new SummationException();
_L5:
        if(iRSS - NoiseFloor < rIN && dRSS > rSens)
            return true;
        try
        {
            return false;
        }
        catch(Exception e)
        {
            LOG.error("Exception - throw new SummationException()", e);
        }
        throw new SummationException();
_L1:
        return false;
    }

    public EventVector cfcCompositeIRSSVector(boolean unwant, boolean blocking, boolean intermod)
        throws SummationException
    {
        EventVector pResult = new EventVector();
        int iSize = workspace.getSignals().getDRSSVector().getEventVector().size();
        int iCount = workspace.getInterferenceLinks().size();
        pResult.reserve(iSize);
        for(int j = 0; j < iSize && !stopped; j++)
        {
            double rSum = 0.0D;
            if(unwant)
            {
                IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListUnwanted();
                for(int i = 1; i <= iCount; i++)
                {
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                    double rValue = iRSSVector.getEventVector().getEvents()[j];
                    try
                    {
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                }

            }
            if(blocking)
            {
                IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListBlocking();
                for(int i = 1; i <= iCount; i++)
                {
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                    double rValue = iRSSVector.getEventVector().getEvents()[j];
                    try
                    {
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                }

            }
            if(intermod)
            {
                IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListIntermodulation();
                for(int i = 0; i < iCount * (iCount - 1); i++)
                {
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                    double rValue = iRSSVector.getEventVector().getEvents()[j];
                    try
                    {
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                }

            }
            try
            {
                pResult.addEvent(10D * Math.log10(rSum));
            }
            catch(Exception e)
            {
                LOG.error("Exception - throw new SummationException()", e);
                throw new SummationException();
            }
        }

        return pResult;
    }

    public EventVector cfcCritVector(EventVector dRSS, EventVector iRSSComp, EventVector NoiseFloor, InterferenceCriterionType InterferenceCriterion)
        throws SummationException
    {
        EventVector pResult = new EventVector();
        int iSize = dRSS.size();
        pResult.reserve(iSize);
        switch(InterferenceCriterion.getInterferenceCriterionType())
        {
        case 1: // '\001'
            for(int i = 0; i < iSize; i++)
            {
                double rdRSS = dRSS.getEvents()[i];
                double riRSS = iRSSComp.getEvents()[i];
                double rCrit = rdRSS - riRSS;
                pResult.addEvent(rCrit);
            }

            return pResult;

        case 2: // '\002'
            for(int i = 0; i < iSize; i++)
            {
                double rdRSS = dRSS.getEvents()[i];
                double riRSS = iRSSComp.getEvents()[i];
                double rNoiseFloor = NoiseFloor.getEvents()[i];
                double rCrit;
                try
                {
                    double rA = Math.pow(10D, rdRSS / 10D);
                    double rB = Math.pow(10D, riRSS / 10D);
                    double rC = Math.pow(10D, rNoiseFloor / 10D);
                    rCrit = 10D * Math.log10(rA / (rB + rC));
                }
                catch(Exception e)
                {
                    LOG.error("Exception - throw new SummationException()", e);
                    throw new SummationException();
                }
                pResult.addEvent(rCrit);
            }

            return pResult;

        case 3: // '\003'
            for(int i = 0; i < iSize; i++)
            {
                double riRSS = iRSSComp.getEvents()[i];
                double rNoiseFloor = NoiseFloor.getEvents()[i];
                double rCrit;
                try
                {
                    double rB = Math.pow(10D, riRSS / 10D);
                    double rC = Math.pow(10D, rNoiseFloor / 10D);
                    rCrit = 10D * Math.log10((rC + rB) / rC);
                }
                catch(Exception e)
                {
                    LOG.error("Exception - throw new SummationException()", e);
                    throw new SummationException();
                }
                pResult.addEvent(rCrit);
            }

            return pResult;

        case 4: // '\004'
            for(int i = 0; i < iSize; i++)
            {
                double riRSS = iRSSComp.getEvents()[i];
                double rNoiseFloor = NoiseFloor.getEvents()[i];
                double rCrit = riRSS - rNoiseFloor;
                pResult.addEvent(rCrit);
            }

            return pResult;
        }
        throw new IllegalArgumentException((new StringBuilder()).append("Invalid Interference Criterion: ").append(InterferenceCriterion.getInterferenceCriterionType()).toString());
    }

    public boolean testProbComplete(double dRSS, double crit, InterferenceCriterionType interferenceCriterion)
    {
        VictimReceiver vr = workspace.getVictimSystemLink().getVictimReceiver();
        double rSens = vr.getSensitivity();
        double rCI = vr.getCiLevel();
        double rCNI = vr.getCniLevel();
        double rINI = vr.getNinLevel();
        double rIN = vr.getInLevel();
        boolean test;
        switch(interferenceCriterion.getInterferenceCriterionType())
        {
        case 1: // '\001'
            test = crit > rCI && dRSS > rSens;
            break;

        case 2: // '\002'
            test = crit > rCNI && dRSS > rSens;
            break;

        case 3: // '\003'
            test = crit < rINI && dRSS > rSens;
            break;

        case 4: // '\004'
            test = crit < rIN && dRSS > rSens;
            break;

        default:
            test = false;
            break;
        }
        return test;
    }

    public EventVector cfcCompositeiRSSDistribution(boolean unwant, boolean blocking, boolean intermod, int nbrTrial)
        throws SummationException
    {
        EventVector pResult = new EventVector();
        int iCount = workspace.getInterferenceLinks().size();
        pResult.reserve(nbrTrial);
        for(int j = 0; j < nbrTrial && !stopped; j++)
        {
            double rSum = 0.0D;
            if(unwant)
            {
                IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListUnwanted();
                for(int i = 1; i <= iCount; i++)
                {
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                    try
                    {
                        double rValue = iRSSVector.getEventVector().getEvents()[j];
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                }

            }
            if(blocking)
            {
                IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListBlocking();
                for(int i = 1; i <= iCount; i++)
                {
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                    try
                    {
                        double rValue = iRSSVector.getEventVector().getEvents()[j];
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                }

            }
            if(intermod)
            {
                IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListIntermodulation();
                for(int i = 0; i < iCount * (iCount - 1); i++)
                {
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                    try
                    {
                        double rValue = iRSSVector.getEventVector().getEvents()[j];
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                }

            }
            try
            {
                pResult.addEvent(10D * Math.log10(rSum));
            }
            catch(Exception e)
            {
                LOG.error("Exception - throw new SummationException()", e);
                throw new SummationException();
            }
        }

        return pResult;
    }

    public EventVector cfcCompositeiRSSDistributionDerivation(boolean unwant, boolean blocking, boolean intermod, int nbrTrial, double ref, int choice)
        throws SummationException, FunctionException
    {
        EventVector pResult = new EventVector();
        int iCount = workspace.getInterferenceLinks().size();
        Function pBlockingResponse = workspace.getVictimSystemLink().getVictimReceiver().getBlockingResponse();
        Function pIntermod = workspace.getVictimSystemLink().getVictimReceiver().getIntermodResponse();
        pResult.reserve(nbrTrial);
        for(int j = 0; j < nbrTrial && !stopped; j++)
        {
            double rSum = 0.0D;
label0:
            switch(choice)
            {
            case 0: // '\0'
            {
                double rRefInit = pBlockingResponse.evaluate(0.0D);
                if(blocking)
                {
                    IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListBlocking();
                    for(int i = 1; i <= iCount; i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        try
                        {
                            double riRSSTrial = iRSSVector.getEventVector().getEvents()[j];
                            double rValue = riRSSTrial - (ref - rRefInit);
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                if(unwant)
                {
                    IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListUnwanted();
                    for(int i = 1; i <= iCount; i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        try
                        {
                            double rValue = iRSSVector.getEventVector().getEvents()[j];
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                if(intermod)
                {
                    IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListIntermodulation();
                    for(int i = 0; i < iCount * (iCount - 1); i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        try
                        {
                            double rValue = iRSSVector.getEventVector().getEvents()[j];
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                break;
            }

            case 1: // '\001'
            {
                double rRefInit = pIntermod.evaluate(0.0D);
                IRSSVectorList iRSSVectorList;
                int i;
                if(intermod)
                {
                    iRSSVectorList = workspace.getSignals().getIRSSVectorListIntermodulation();
                    for(i = 0; i < iCount * (iCount - 1); i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        try
                        {
                            double riRSSTrial = iRSSVector.getEventVector().getEvents()[j];
                            double rValue = riRSSTrial - 3D * (ref - rRefInit);
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                if(unwant)
                {
                    iRSSVectorList = workspace.getSignals().getIRSSVectorListUnwanted();
                    for(i = 1; i <= iCount; i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        try
                        {
                            double rValue = iRSSVector.getEventVector().getEvents()[j];
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                if(!blocking)
                    break;
                iRSSVectorList = workspace.getSignals().getIRSSVectorListBlocking();
                i = 1;
                do
                {
                    if(i > iCount)
                        break label0;
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                    try
                    {
                        double rValue = iRSSVector.getEventVector().getEvents()[j];
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                    i++;
                } while(true);
            }

            default:
            {
                InterferingSystemLink isl = ((InterferenceLink)workspace.getInterferenceLinks().get(choice - 2)).getInterferingLink();
                ConstantDistribution pUnwantedPower = (ConstantDistribution)isl.getInterferingTransmitter().getPowerSuppliedDistribution();
                double rUnwantedPower = pUnwantedPower.trial();
                double rRefInit = rUnwantedPower;
                IRSSVectorList iRSSVectorList;
                if(unwant)
                {
                    iRSSVectorList = workspace.getSignals().getIRSSVectorListUnwanted();
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(choice - 2);
                    try
                    {
                        double riRSSTrial = iRSSVector.getEventVector().getEvents()[j];
                        double rValue = riRSSTrial + (ref - rRefInit);
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                }
                if(unwant || blocking)
                {
                    IRSSVectorList iRSSVectorListU = workspace.getSignals().getIRSSVectorListUnwanted();
                    IRSSVectorList iRSSVectorListB = workspace.getSignals().getIRSSVectorListBlocking();
                    for(int i = 0; i < iCount; i++)
                    {
                        IRSSVector iRSSVector;
                        if(unwant && i != choice - 2)
                        {
                            iRSSVector = (IRSSVector)iRSSVectorListU.getIRSSVectors().get(i);
                            try
                            {
                                double rValue = iRSSVector.getEventVector().getEvents()[j];
                                rSum += Math.pow(10D, rValue / 10D);
                            }
                            catch(Exception e)
                            {
                                LOG.error("Exception - throw new SummationException()", e);
                                throw new SummationException();
                            }
                        }
                        if(!blocking)
                            continue;
                        iRSSVector = (IRSSVector)iRSSVectorListB.getIRSSVectors().get(i);
                        try
                        {
                            double rValue = iRSSVector.getEventVector().getEvents()[j];
                            rSum += Math.pow(10D, rValue / 10D);
                            continue;
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                        }
                        throw new SummationException();
                    }

                }
                if(!intermod)
                    break;
                iRSSVectorListU = workspace.getSignals().getIRSSVectorListIntermodulation();
                int i = 0;
                do
                {
                    if(i >= iCount * (iCount - 1))
                        break label0;
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorListU.getIRSSVectors().get(i);
                    try
                    {
                        double rValue = iRSSVector.getEventVector().getEvents()[j];
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                    i++;
                } while(true);
            }
            }
            try
            {
                pResult.addEvent(10D * Math.log10(rSum));
            }
            catch(Exception e)
            {
                LOG.error("Exception - throw new SummationException()", e);
                throw new SummationException();
            }
        }

        return pResult;
    }

    public EventVector cfcCompositeIRSSVectorDerivation(boolean unwant, boolean blocking, boolean intermod, double ref, int choice)
        throws SummationException, FunctionException
    {
        EventVector pResult = new EventVector();
        int iCount = workspace.getInterferenceLinks().size();
        Function pBlockingResponse = workspace.getVictimSystemLink().getVictimReceiver().getBlockingResponse();
        Function pIntermod = workspace.getVictimSystemLink().getVictimReceiver().getIntermodResponse();
        int iSize = workspace.getSignals().getDRSSVector().getEventVector().size();
        pResult.reserve(iSize);
        for(int j = 0; j < iSize; j++)
        {
            double rSum = 0.0D;
label0:
            switch(choice)
            {
            case 0: // '\0'
            {
                double rRefInit = pBlockingResponse.evaluate(0.0D);
                if(blocking)
                {
                    IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListBlocking();
                    for(int i = 1; i <= iCount; i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        double riRSS = iRSSVector.getEventVector().getEvents()[j];
                        double rValue = riRSS - (ref - rRefInit);
                        try
                        {
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                if(unwant)
                {
                    IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListUnwanted();
                    for(int i = 1; i <= iCount; i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        double rValue = iRSSVector.getEventVector().getEvents()[j];
                        try
                        {
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                if(intermod)
                {
                    IRSSVectorList iRSSVectorList = workspace.getSignals().getIRSSVectorListIntermodulation();
                    for(int i = 0; i < iCount * (iCount - 1); i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        double rValue = iRSSVector.getEventVector().getEvents()[j];
                        try
                        {
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                break;
            }

            case 1: // '\001'
            {
                double rRefInit = pIntermod.evaluate(0.0D);
                IRSSVectorList iRSSVectorList;
                int i;
                if(intermod)
                {
                    iRSSVectorList = workspace.getSignals().getIRSSVectorListIntermodulation();
                    for(i = 1; i <= iCount; i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        double riRSS = iRSSVector.getEventVector().getEvents()[j];
                        double rValue = riRSS - 3D * (ref - rRefInit);
                        try
                        {
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                    }

                }
                if(unwant)
                {
                    iRSSVectorList = workspace.getSignals().getIRSSVectorListUnwanted();
                    for(i = 1; i <= iCount; i++)
                    {
                        IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                        double rValue = iRSSVector.getEventVector().getEvents()[j];
                        try
                        {
                            rSum += Math.pow(10D, rValue / 10D);
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                            throw new SummationException();
                        }
                        i++;
                    }

                }
                if(!blocking)
                    break;
                iRSSVectorList = workspace.getSignals().getIRSSVectorListBlocking();
                i = 1;
                do
                {
                    if(i > iCount)
                        break label0;
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(i);
                    double rValue = iRSSVector.getEventVector().getEvents()[j];
                    try
                    {
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                    i++;
                    i++;
                } while(true);
            }

            default:
            {
                InterferingSystemLink interferingSystemLink = ((InterferenceLink)workspace.getInterferenceLinks().get(choice - 2)).getInterferingLink();
                ConstantDistribution pUnwantedPower = (ConstantDistribution)interferingSystemLink.getInterferingTransmitter().getPowerSuppliedDistribution();
                double rUnwantedPower = pUnwantedPower.trial();
                double rRefInit = rUnwantedPower;
                IRSSVectorList iRSSVectorList;
                if(unwant)
                {
                    iRSSVectorList = workspace.getSignals().getIRSSVectorListUnwanted();
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorList.getIRSSVectors().get(choice - 1);
                    double riRSS = iRSSVector.getEventVector().getEvents()[j];
                    double rValue = riRSS + (ref - rRefInit);
                    try
                    {
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                }
                if(!unwant && !blocking)
                {
                    IRSSVectorList iRSSVectorListU = workspace.getSignals().getIRSSVectorListUnwanted();
                    IRSSVectorList iRSSVectorListB = workspace.getSignals().getIRSSVectorListUnwanted();
                    for(int i = 0; i < iCount; i++)
                    {
                        if(i == choice - 2)
                            continue;
                        IRSSVector iRSSVector;
                        double rValue;
                        if(unwant)
                        {
                            iRSSVector = (IRSSVector)iRSSVectorListU.getIRSSVectors().get(i);
                            rValue = iRSSVector.getEventVector().getEvents()[j];
                            try
                            {
                                rSum += Math.pow(10D, rValue / 10D);
                            }
                            catch(Exception e)
                            {
                                LOG.error("Exception - throw new SummationException()", e);
                                throw new SummationException();
                            }
                        }
                        if(!blocking)
                            continue;
                        iRSSVector = (IRSSVector)iRSSVectorListB.getIRSSVectors().get(i);
                        rValue = iRSSVector.getEventVector().getEvents()[j];
                        try
                        {
                            rSum += Math.pow(10D, rValue / 10D);
                            continue;
                        }
                        catch(Exception e)
                        {
                            LOG.error("Exception - throw new SummationException()", e);
                        }
                        throw new SummationException();
                    }

                }
                if(!intermod)
                    break;
                iRSSVectorListU = workspace.getSignals().getIRSSVectorListIntermodulation();
                int i = 0;
                do
                {
                    if(i >= iCount * (iCount - 1))
                        break label0;
                    IRSSVector iRSSVector = (IRSSVector)iRSSVectorListU.getIRSSVectors().get(i);
                    double rValue = iRSSVector.getEventVector().getEvents()[j];
                    try
                    {
                        rSum += Math.pow(10D, rValue / 10D);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Exception - throw new SummationException()", e);
                        throw new SummationException();
                    }
                    i++;
                } while(true);
            }
            }
            try
            {
                pResult.addEvent(10D * Math.log10(rSum));
            }
            catch(Exception e)
            {
                LOG.error("Exception - throw new SummationException()", e);
                throw new SummationException();
            }
        }

        return pResult;
    }

    private double iceComplete1Compatibility(boolean bUnwant, boolean bBlocking, boolean bIntermod, int iInterferenceCriterion, int iNbEvents)
        throws SummationException
    {
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("ICEComplete1Compatibility(bUnwant: ").append(bUnwant).append(", bBlocking: ").append(bBlocking).append(", bIntermod: ").append(bIntermod).append(",iInterferenceCriterion: ").append(iInterferenceCriterion).append(", iNbEvents: ").append(iNbEvents).append(")").toString());
        notifyListenersSetCurrentProcessCompletionPercentage(0);
        double rProbD = 0.0D;
        double rProb = 0.0D;
        int iSize = workspace.getSignals().getDRSSVector().getEventVector().size();
        double rSensVr = workspace.getVictimSystemLink().getVictimReceiver().getSensitivity();
        EventVector critVector = null;
        EventVector dRSS = workspace.getSignals().getDRSSVector().getEventVector();
        EventVector iRSSVectorComposite = null;
        EventVector noiseFloor = new EventVector();
        Distribution pNoiseFloor = workspace.getVictimSystemLink().getVictimReceiver().getNoiseFloorDistribution();
        noiseFloor.generateFromDistrib(pNoiseFloor, iSize);
        double rProbN = 0.0D;
        iRSSVectorComposite = cfcCompositeIRSSVector(bUnwant, bBlocking, bIntermod);
        if(!isStopped())
        {
            critVector = cfcCritVector(dRSS, iRSSVectorComposite, noiseFloor, iceconf);
            for(int i = 0; i < iNbEvents; i++)
            {
                double rdRSSTrial = workspace.getSignals().getDRSSVector().getEventVector().getEvents()[i];
                if(rdRSSTrial > rSensVr)
                    rProbD++;
                else
                    System.currentTimeMillis();
                double rCrit = critVector.getEvents()[i];
                boolean bResultTest = testProbComplete(rdRSSTrial, rCrit, iceconf);
                if(bResultTest)
                    rProbN++;
            }

        }
        if(rProbD == 0.0D)
        {
            rProb = 0.0D;
            notifyListenersWarningMessage(WARNINGS[0]);
        } else
        {
            rProb = rProbN / rProbD;
        }
        double probResult = 1.0D - rProb;
        notifyListenersProbabilityResult(probResult);
        notifyListenersInfoMessage((new StringBuilder()).append("Interference probability result (Complete / Compatibility mode) :").append(probResult).append(iceconf.isSignalsModifiedByPlugin() ? " *" : "").toString());
        return probResult;
    }

    private void iceComplete1Derivation(boolean bUnwant, boolean bBlocking, boolean bIntermod, int iInterferenceCriterion, int iNbEvents)
        throws SummationException, FunctionException, SummationException
    {
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("ICEComplete1Derivation(bUnwant: ").append(bUnwant).append(", bBlocking: ").append(bBlocking).append(", bIntermod: ").append(bIntermod).append(",iInterferenceCriterion: ").append(iInterferenceCriterion).append(", iNbEvents: ").append(iNbEvents).append(")").toString());
        double rProbD = 0.0D;
        int iChoice = iceconf.getTranslationParameter();
        int iSize = workspace.getSignals().getDRSSVector().getEventVector().size();
        double rSensVr = workspace.getVictimSystemLink().getVictimReceiver().getSensitivity();
        double rRefMin = iceconf.getTranslationMin();
        double rRefMax = iceconf.getTranslationMax();
        int iRefNbr = (int)iceconf.getTranslationPoints();
        double rRefStep = (rRefMax - rRefMin) / (double)iRefNbr;
        double rProduct[] = new double[iRefNbr + 1];
        EventVector dRSS = workspace.getSignals().getDRSSVector().getEventVector();
        int k = 0;
        double rRef = rRefMin;
        Distribution pNoiseFloor;
        pNoiseFloor = pNoiseFloor = workspace.getVictimSystemLink().getVictimReceiver().getNoiseFloorDistribution();
        for(int j = 0; j < iRefNbr && !isStopped();)
        {
            rProbD = 0.0D;
            EventVector iRSSVectorComposite = null;
            iRSSVectorComposite = cfcCompositeIRSSVectorDerivation(bUnwant, bBlocking, bIntermod, rRef, iChoice);
            EventVector critVector = null;
            EventVector noiseFloor = new EventVector();
            noiseFloor.generateFromDistrib(pNoiseFloor, iSize);
            critVector = cfcCritVector(dRSS, iRSSVectorComposite, noiseFloor, iceconf);
            for(int i = 0; i < iNbEvents; i++)
            {
                double rdRSSTrial = dRSS.getEvents()[i];
                if(rdRSSTrial > rSensVr)
                    rProbD++;
                double rCrit = critVector.getEvents()[i];
                boolean bResultTest = testProbComplete(rdRSSTrial, rCrit, iceconf);
                if(bResultTest)
                    rProduct[j]++;
            }

            if(rProbD == 0.0D)
            {
                rProduct[j] = 0.0D;
                k++;
            } else
            {
                rProduct[j] = rProduct[j] / rProbD;
            }
            notifyListenersIncrementCurrentProcessCompletionPercentage((j * 100) / iRefNbr);
            j++;
            rRef += rRefStep;
        }

        if(k == iRefNbr)
            notifyListenersWarningMessage(WARNINGS[0]);
        rRef = rRefMin;
        for(int j = 0; j < iRefNbr; j++)
        {
            Point2D pt = new Point2D(rRef, 1.0D - rProduct[j]);
            notifyListenersTranslationResult(pt);
            rRef += rRefStep;
        }

    }

    public void addIceListener(InterferenceCalculationListener iceListener)
    {
        iceListeners.add(iceListener);
    }

    public void remocveIceListener(InterferenceCalculationListener iceListener)
    {
        iceListeners.remove(iceListener);
    }

    void notifyListenersWarningMessage(String warning)
    {
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.warningMessage(warning))
            l = (InterferenceCalculationListener)i$.next();

    }

    void notifyListenersInfoMessage(String warning)
    {
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.infoMessage(warning))
            l = (InterferenceCalculationListener)i$.next();

    }

    private void notifyListenersCalculationStarted()
    {
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.calculationStarted())
            l = (InterferenceCalculationListener)i$.next();

    }

    private void notifyListenersCalculationComplete()
    {
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.calculationComplete())
            l = (InterferenceCalculationListener)i$.next();

    }

    void notifyListenersSetCurrentProcessCompletionPercentage(int value)
    {
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.setCurrentProcessCompletionPercentage(value))
            l = (InterferenceCalculationListener)i$.next();

    }

    void notifyListenersSetTotalProcessCompletionPercentage(int value)
    {
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.setTotalProcessCompletionPercentage(value))
            l = (InterferenceCalculationListener)i$.next();

    }

    void notifyListenersIncrementCurrentProcessCompletionPercentage(int value)
    {
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.incrementCurrentProcessCompletionPercentage(value))
            l = (InterferenceCalculationListener)i$.next();

    }

    void notifyListenersIncrementTotalProcessCompletionPercentage(int value)
    {
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.incrementTotalProcessCompletionPercentage(value))
            l = (InterferenceCalculationListener)i$.next();

    }

    void notifyListenersProbabilityResult(double value)
    {
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("ProbabilityResult: ").append(value).toString());
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.propabilityResult(value))
            l = (InterferenceCalculationListener)i$.next();

    }

    void notifyListenersTranslationResult(Point2D value)
    {
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("TranslationResult: x=").append(value.getX()).append(" y=").append(value.getY()).toString());
        InterferenceCalculationListener l;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext(); l.addTranslationResult(value))
            l = (InterferenceCalculationListener)i$.next();

    }

    boolean confirmListenersWarningMessage(String message)
    {
        boolean result = true;
        for(Iterator i$ = iceListeners.iterator(); i$.hasNext();)
        {
            InterferenceCalculationListener l = (InterferenceCalculationListener)i$.next();
            result &= l.confirmContinueOnWarning(message);
        }

        return result;
    }

    private static final ResourceBundle STRINGLIST;
    private static final Logger LOG = Logger.getLogger(org/seamcat/model/engines/ICE);
    private static final String THREAD_NAME;
    private static final String WARNINGS[];
    private static final String ERRORS[];
    private Workspace workspace;
    private ICEConfiguration iceconf;
    private Thread iceThread;
    private boolean stopped;
    private final List iceListeners = new ArrayList();

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        THREAD_NAME = STRINGLIST.getString("ICE_THREAD");
        WARNINGS = (new String[] {
            STRINGLIST.getString("ICE_WARNING_PROB")
        });
        ERRORS = (new String[] {
            STRINGLIST.getString("ICE_ERROR")
        });
    }
}
