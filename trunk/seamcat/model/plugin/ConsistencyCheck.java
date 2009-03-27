// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ConsistencyCheck.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            ScenarioInfo

public interface ConsistencyCheck
{

    public abstract boolean check(ScenarioInfo scenarioinfo);
}
