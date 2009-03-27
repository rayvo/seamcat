// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioCheckUtils.java

package org.seamcat.model.scenariocheck;

import java.util.*;
import org.apache.log4j.Logger;
import org.seamcat.model.Workspace;
import org.seamcat.model.plugin.ConsistencyCheck;
import org.seamcat.postprocessing.PostProcessingPluginWrapper;
import org.seamcat.postprocessing.ScenarioInfo_Impl;

// Referenced classes of package org.seamcat.model.scenariocheck:
//            ScenarioCheck, VictimLinkCheck, InterferingLinkCheck, CDMACheck, 
//            GeneralScenarioCheck, ScenarioCheckResult

public final class ScenarioCheckUtils
{

    private ScenarioCheckUtils()
    {
    }

    public static final List checkWorkspace(Workspace workspace)
    {
        LOG.debug("Checking workspace consistency");
        ScenarioCheck checks[] = {
            new VictimLinkCheck(), new InterferingLinkCheck(), new CDMACheck(), new GeneralScenarioCheck()
        };
        List results = new ArrayList();
        ScenarioCheck arr$[] = checks;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            ScenarioCheck sCheck = arr$[i$];
            results.add(sCheck.check(workspace));
        }

        ScenarioInfo_Impl info = null;
        Iterator i$ = workspace.getPostProcessingPlugins().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            PostProcessingPluginWrapper plugin = (PostProcessingPluginWrapper)i$.next();
            if(plugin.getPluginInstance() instanceof ConsistencyCheck)
            {
                ConsistencyCheck cc = (ConsistencyCheck)plugin.getPluginInstance();
                if(info == null)
                    info = new ScenarioInfo_Impl(workspace);
                if(!cc.check(info))
                {
                    results.add(new ScenarioCheckResult(ScenarioCheckResult.Outcome.FAILED, plugin.getReference(), info.getConsistencyWarnings()));
                    info.getConsistencyWarnings().clear();
                }
            }
        } while(true);
        return results;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/Workspace);

}
