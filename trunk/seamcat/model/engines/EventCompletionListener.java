// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EventCompletionListener.java

package org.seamcat.model.engines;

import org.seamcat.model.Workspace;

public interface EventCompletionListener
{

    public abstract void eventCompleted(int i, int j);

    public abstract void updateStatus(String s);

    public abstract void eventGenerationCompleted(int i);

    public abstract void startingEventGeneration(Workspace workspace, int i, int j);

    public abstract void generationAndEvaluationComplete();

    public abstract void setCurrentProcessCompletionPercentage(int i);

    public abstract void incrementCurrentProcessCompletionPercentage(int i);

    public abstract void setTotalProcessCompletionPercentage(int i);

    public abstract void incrementTotalProcessCompletionPercentage(int i);

    public abstract void notifyError(String s);
}
