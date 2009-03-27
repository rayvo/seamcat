// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractCheck.java

package org.seamcat.model.scenariocheck;

import org.seamcat.distribution.ContinousDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.propagation.*;

// Referenced classes of package org.seamcat.model.scenariocheck:
//            ScenarioCheckResult, ScenarioCheck

abstract class AbstractCheck
    implements ScenarioCheck
{

    public AbstractCheck()
    {
        result = new ScenarioCheckResult();
    }

    public void addErrorMsg(String msg)
    {
        if(result.getOutcome() != ScenarioCheckResult.Outcome.FAILED)
            result.setOutcome(ScenarioCheckResult.Outcome.FAILED);
        result.addMessage(msg);
    }

    protected void checkDistribution(Distribution dist, String linkName, String fieldName)
    {
        switch(dist.getType())
        {
        default:
            break;

        case 1: // '\001'
            double rMin = ((ContinousDistribution)dist).getCdf().evaluateMin();
            double rMax = ((ContinousDistribution)dist).getCdf().evaluateMax();
            if(rMin != 0.0D || rMax != 1.0D)
                addErrorMsg((new StringBuilder()).append("User-defined distribution doesn't include 0 and/or 1 value(s) for parameter ").append(fieldName).append(" in ").append(linkName).toString());
            break;

        case 8: // '\b'
            double rRatio = (dist.getMax() - dist.getMin()) / dist.getStep();
            if((rRatio - Math.rint(rRatio)) / rRatio > 0.01D)
                addErrorMsg((new StringBuilder()).append("Range is not multiple of the step in the discrete uniform distribution used for parameter ").append(fieldName).append(" in ").append(linkName).toString());
            break;
        }
    }

    protected void checkPropagationModel(PropagationModel model, double rFreqMin, double rFreqMax, String link)
    {
        if(model instanceof SDModel)
        {
            if(rFreqMax < 1000D)
                addErrorMsg((new StringBuilder()).append("Spherical diffraction model not applicable below 1 GHz in ").append(link).toString());
            return;
        }
        if(model instanceof R370Model)
        {
            if(rFreqMin < 30D)
                addErrorMsg((new StringBuilder()).append("ITU-R P.1546 model not applicable below 30 MHz in ").append(link).toString());
            if(rFreqMax > 3000D)
                addErrorMsg((new StringBuilder()).append("ITU-R P.1546 model not applicable above 3 GHz in ").append(link).toString());
        }
        if(model instanceof HataAndSDModel)
        {
            if(rFreqMin < 30D)
                addErrorMsg((new StringBuilder()).append("Hata model not applicable below 30 MHz in ").append(link).toString());
            if(rFreqMax > 3000D)
                addErrorMsg((new StringBuilder()).append("Hata model not applicable above 3 GHz in ").append(link).toString());
        }
    }

    protected ScenarioCheckResult result;
}
