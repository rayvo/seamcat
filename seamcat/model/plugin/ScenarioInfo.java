// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioInfo.java

package org.seamcat.model.plugin;

import java.util.List;

// Referenced classes of package org.seamcat.model.plugin:
//            InterferingLink, VictimLink, ProcessingResult

public interface ScenarioInfo
{

    public abstract int getNumberOfInterferingLinks();

    public abstract InterferingLink getInterferingLink(int i);

    public abstract List getInterferingLinks();

    public abstract VictimLink getVictimLink();

    public abstract int getCurrentEventNumber();

    public abstract int getTotalNumberOfEvents();

    public abstract ProcessingResult getProcessingResults();

    public abstract double getIRSSIntermodulation(int i, int j);

    public abstract void requestSimulationStop();

    public abstract void addConsistencyWarning(String s);
}
