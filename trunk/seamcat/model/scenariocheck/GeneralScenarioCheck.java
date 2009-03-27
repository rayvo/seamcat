// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GeneralScenarioCheck.java

package org.seamcat.model.scenariocheck;

import java.util.Iterator;
import java.util.List;
import org.seamcat.model.*;
import org.seamcat.model.core.EventVector;
import org.seamcat.model.core.Signals;
import org.seamcat.model.datatypes.IRSSVector;
import org.seamcat.model.datatypes.IRSSVectorList;

// Referenced classes of package org.seamcat.model.scenariocheck:
//            AbstractCheck, ScenarioCheckResult

public class GeneralScenarioCheck extends AbstractCheck
{

    public GeneralScenarioCheck()
    {
        result.setCheckName("General Scenario Check");
    }

    public ScenarioCheckResult check(Workspace workspace)
    {
        if(!checkMemory(workspace))
            addErrorMsg((new StringBuilder()).append("SEAMCAT does not appear to have enough memory available to complete the requested simulation.<br>SEAMCAT estimates that it can only do around ").append(calculateMaxEvents(workspace)).append(" events of your scenario with the current ").append(" available memory.").toString());
        return result;
    }

    private boolean checkMemory(Workspace workspace)
    {
        int total = workspace.getInterferenceLinks().size();
        int vectorCount = (int)(3D * Math.pow(total, 2D) + (double)(3 * total) + 4D);
        Iterator i$ = workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            IRSSVector vector = (IRSSVector)i$.next();
            if(!vector.getEventVector().isShouldBeCalculated())
                vectorCount--;
        } while(true);
        double byteConversionValue = 1048576D;
        double memoryNeeded = (double)vectorCount * ((double)(workspace.getControl().getEgData().getNumberOfEvents() * 8) / 1048576D);
        Runtime r = Runtime.getRuntime();
        r.gc();
        double memoryAvailable = ((double)(r.maxMemory() - (r.totalMemory() - r.freeMemory())) / 1048576D) * 0.84999999999999998D;
        return memoryNeeded <= memoryAvailable;
    }

    private int calculateMaxEvents(Workspace workspace)
    {
        int total = workspace.getInterferenceLinks().size();
        int vectorCount = (int)(3D * Math.pow(total, 2D) + (double)(3 * total) + 4D);
        Iterator i$ = workspace.getSignals().getIRSSVectorListIntermodulation().getIRSSVectors().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            IRSSVector vector = (IRSSVector)i$.next();
            if(!vector.getEventVector().isShouldBeCalculated())
                vectorCount--;
        } while(true);
        double byteConversionValue = 1048576D;
        Runtime r = Runtime.getRuntime();
        r.gc();
        double memoryAvailable = ((double)(r.maxMemory() - (r.totalMemory() - r.freeMemory())) / 1048576D) * 0.84999999999999998D;
        return (int)(memoryAvailable / (((double)vectorCount * 8D) / 1048576D));
    }

    public volatile void addErrorMsg(String x0)
    {
        super.addErrorMsg(x0);
    }
}
