// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EventStatusPanel.java

package org.seamcat.presentation.components;

import javax.swing.JProgressBar;
import org.seamcat.model.Workspace;
import org.seamcat.model.engines.EventCompletionListener;

// Referenced classes of package org.seamcat.presentation.components:
//            AbstractStatusPanel

public class EventStatusPanel extends AbstractStatusPanel
    implements EventCompletionListener
{

    public EventStatusPanel()
    {
        super("Event Generation Not started");
    }

    public void eventCompleted(int eventsCompleted, int totalNumberOfEvents)
    {
        currentProcess.setValue(eventsCompleted);
    }

    public void eventGenerationCompleted(int count)
    {
        currentProcess.setValue(currentProcess.getMaximum());
    }

    public void generationAndEvaluationComplete()
    {
    }

    public void setCurrentProcessCompletionPercentage(int i)
    {
    }

    public void incrementCurrentProcessCompletionPercentage(int i)
    {
    }

    public void startingEventGeneration(Workspace workspace, int eventsToBeCalculated, int eventStartIndex)
    {
        currentProcess.setMaximum(eventsToBeCalculated);
        currentProcessValue = 0;
        currentProcess.setValue(0);
    }

    public void notifyError(String s)
    {
    }
}
