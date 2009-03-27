// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractStatusPanel.java

package org.seamcat.presentation.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public abstract class AbstractStatusPanel extends JPanel
{

    protected AbstractStatusPanel(String _statusText)
    {
        statusLabel = new JLabel("Current Performing Task:");
        currentProcessLabel = new JLabel("Current process:");
        memoryUsageLabel = new JLabel("Memory usage:");
        freeMemoryLabel = new JLabel("Free memory:");
        status = new JLabel("");
        debug = new JCheckBox("Debug");
        currentProcess = new JProgressBar(0, 100);
        memoryUsage = new JProgressBar(0, 100);
        statusText = new String();
        currentProcessValue = 0;
        totalProcessValue = 0;
        layout = new GridBagLayout();
        setLayout(layout);
        currentProcess.setStringPainted(true);
        memoryUsage.setStringPainted(true);
        status.setFont(new Font(status.getFont().getName(), 1, 12));
        status.setText(_statusText);
        memoryUsageLabel.setText((new StringBuilder()).append("Use of allocated memory (max: ").append((int)(Runtime.getRuntime().maxMemory() / 1024L)).append(" kb / allocated: ").append((int)(Runtime.getRuntime().totalMemory() / 1024L)).append(" kb):").toString());
        GridBagConstraints con = new GridBagConstraints();
        con.weightx = 0.5D;
        con.anchor = 17;
        con.gridheight = 1;
        con.gridwidth = 1;
        con.gridx = 0;
        con.gridy = 0;
        con.fill = 1;
        add(statusLabel, con);
        con.gridwidth = 2;
        con.gridx++;
        con.weightx = 1.0D;
        con.anchor = 10;
        add(status, con);
        con.anchor = 17;
        con.gridx = 0;
        con.weightx = 1.0D;
        con.gridy++;
        con.gridwidth = 3;
        add(currentProcessLabel, con);
        con.gridx = 0;
        con.weightx = 1.0D;
        con.gridy++;
        con.gridwidth = 3;
        add(currentProcess, con);
        con.gridx = 0;
        con.weightx = 1.0D;
        con.gridy++;
        con.gridwidth = 3;
        add(memoryUsageLabel, con);
        con.gridy++;
        con.gridwidth = 3;
        add(memoryUsage, con);
        con.gridy++;
        con.gridx = 0;
        con.weightx = 0.5D;
        con.gridwidth = 1;
        add(Box.createHorizontalGlue(), con);
        con.gridx++;
        con.weightx = 1.0D;
        add(Box.createHorizontalGlue(), con);
        con.gridx++;
        add(Box.createHorizontalGlue(), con);
        setBorder(new TitledBorder("Simulation Status"));
        startUpdater();
    }

    protected void startUpdater()
    {
        running = true;
        worker = new Thread("Abstract status update thread") {

            public void run()
            {
                while(running) 
                    try
                    {
                        updateGuiComponents();
                        Thread.sleep(750L);
                    }
                    catch(Exception e) { }
            }

            final AbstractStatusPanel this$0;

            
            {
                this$0 = AbstractStatusPanel.this;
                super(x0);
            }
        }
;
        worker.start();
    }

    public void updateGuiComponents()
    {
        if(updateCurrentProcess)
        {
            currentProcess.setValue(currentProcessValue);
            updateCurrentProcess = false;
        }
        if(updateStatusText)
        {
            status.setText(statusText);
            updateStatusText = false;
        }
        if(isVisible())
        {
            memoryUsage.setMaximum(memoryMax);
            memoryUsage.setValue(memoryUsageValue);
            memoryUsageLabel.setText(memoryStatusLabel);
        }
    }

    public void updateStatus(String _status)
    {
        statusText = _status;
        updateStatusText = true;
    }

    public void setCurrentProcessCompletionPercentage(int value)
    {
        currentProcessValue = value;
        updateCurrentProcess = true;
    }

    public void setTotalProcessCompletionPercentage(int value)
    {
        totalProcessValue = value;
    }

    public void incrementCurrentProcessCompletionPercentage(int value)
    {
        currentProcessValue += value;
        updateCurrentProcess = true;
    }

    public void incrementTotalProcessCompletionPercentage(int value)
    {
        totalProcessValue += value;
    }

    protected JLabel statusLabel;
    protected JLabel currentProcessLabel;
    protected JLabel memoryUsageLabel;
    protected JLabel freeMemoryLabel;
    protected JLabel status;
    protected JCheckBox debug;
    protected JProgressBar currentProcess;
    protected JProgressBar memoryUsage;
    protected String statusText;
    protected int currentProcessValue;
    protected int totalProcessValue;
    protected boolean updateStatusText;
    protected boolean updateCurrentProcess;
    protected boolean updateMemoryUsage;
    protected LayoutManager layout;
    protected static final int MEMORY_FACTOR = 1024;
    protected static int memoryMax;
    protected static int memoryUsageValue;
    protected static String memoryStatusLabel;
    protected Thread worker;
    protected boolean running;

    static 
    {
        (new Thread("static memory monitor") {

            public void run()
            {
                do
                    try
                    {
                        AbstractStatusPanel.memoryMax = (int)(Runtime.getRuntime().totalMemory() / 1024L);
                        AbstractStatusPanel.memoryUsageValue = AbstractStatusPanel.memoryMax - (int)(Runtime.getRuntime().freeMemory() / 1024L);
                        AbstractStatusPanel.memoryStatusLabel = (new StringBuilder()).append("Use of allocated memory (max: ").append((int)(Runtime.getRuntime().maxMemory() / 1024L)).append(" kb / allocated: ").append((int)(Runtime.getRuntime().totalMemory() / 1024L)).append(" kb (").append((int)(Runtime.getRuntime().totalMemory() / 1024L) / ((int)(Runtime.getRuntime().maxMemory() / 1024L) / 100)).append(" %)):").toString();
                        Thread.sleep(250L);
                    }
                    catch(Exception e) { }
                while(true);
            }

        }
).start();
    }
}
