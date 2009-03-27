// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioCheck.java

package org.seamcat.model.scenariocheck;

import org.seamcat.model.Workspace;

// Referenced classes of package org.seamcat.model.scenariocheck:
//            ScenarioCheckResult

public interface ScenarioCheck
{

    public abstract ScenarioCheckResult check(Workspace workspace);
}
