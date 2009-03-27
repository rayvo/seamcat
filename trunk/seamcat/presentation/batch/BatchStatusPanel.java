// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchStatusPanel.java

package org.seamcat.presentation.batch;

import java.util.List;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import org.seamcat.batch.*;
import org.seamcat.model.Workspace;
import org.seamcat.presentation.components.AbstractStatusPanel;

public class BatchStatusPanel extends AbstractStatusPanel
    implements BatchJobListListener
{

    public BatchStatusPanel()
    {
        super("");
    }

    public void setBatchJobList(BatchJobList bjl)
    {
        jobs = bjl;
        jobs.addBatchJobListListener(this);
        statusLabel.setText("Starting batch");
        currentProcess.setMaximum(jobs.getBatchJobs().size() + 1);
        currentProcess.setValue(0);
    }

    public void startingJob(int index)
    {
        statusLabel.setText((new StringBuilder()).append("Processing: ").append(jobs.getBatchJob(index).getWorkspace().getReference()).toString());
    }

    public void endingJob(int index)
    {
        currentProcess.setValue(index + 1);
    }

    public void entireBatchIsComplete()
    {
        statusLabel.setText("Batch is complete");
        currentProcess.setValue(currentProcess.getMaximum());
    }

    private BatchJobList jobs;
}
