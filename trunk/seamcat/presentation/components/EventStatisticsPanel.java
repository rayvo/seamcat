// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EventStatisticsPanel.java

package org.seamcat.presentation.components;

import java.awt.Font;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.Workspace;
import org.seamcat.model.core.EventVector;
import org.seamcat.model.core.Signals;
import org.seamcat.model.datatypes.DRSSVector;
import org.seamcat.model.datatypes.IRSSVector;
import org.seamcat.model.datatypes.IRSSVectorList;
import org.seamcat.model.engines.EventCompletionListener;

// Referenced classes of package org.seamcat.presentation.components:
//            SpringUtilities

public class EventStatisticsPanel extends JPanel
    implements EventCompletionListener, Runnable
{

    public EventStatisticsPanel()
    {
        super(new SpringLayout());
        format = NumberFormat.getInstance();
        font = new Font("Dialog", 0, 12);
        format.setMaximumFractionDigits(2);
        dRSSMeanValue = new JLabel("0.0 dBm", 4);
        dRSSStdDValue = new JLabel("0.0", 2);
        unwantedEmissionMeanValue = new JLabel("0.0 dBm", 4);
        unwantedEmissionStdDValue = new JLabel("0.0", 2);
        blockingMeanValue = new JLabel("0.0 dBm", 4);
        blockingStdDValue = new JLabel("0.0", 2);
        eventGenerationThreshold = new JLabel("0 Events per second", 4);
        add(new JLabel(" "));
        add(new JLabel("Mean"));
        add(new JLabel("StdDev", 2));
        add(new JLabel(STRINGLIST.getString("SIMULATION_STATUS_DRSS")));
        add(dRSSMeanValue);
        add(dRSSStdDValue);
        add(new JLabel(STRINGLIST.getString("SIMULATION_STATUS_IRSS_UNWANTED")));
        add(unwantedEmissionMeanValue);
        add(unwantedEmissionStdDValue);
        add(new JLabel(STRINGLIST.getString("SIMULATION_STATUS_IRSS_BLOCKING")));
        add(blockingMeanValue);
        add(blockingStdDValue);
        add(new JLabel(STRINGLIST.getString("SIMULATION_STATUS_THRESHOLD")));
        add(new JLabel(" "));
        add(eventGenerationThreshold);
        SpringUtilities.makeCompactGrid(this, 5, 3, 6, 6, 10, 10);
        setFont(font);
        setBorder(new TitledBorder("Simulation Summary"));
    }

    public void reset()
    {
        dRSSMeanValue.setText("0.0 dBm");
        dRSSStdDValue.setText("0.0");
        unwantedEmissionMeanValue.setText("0.0 dBm");
        unwantedEmissionStdDValue.setText("0.0");
        blockingMeanValue.setText("0.0 dBm");
        blockingStdDValue.setText("0.0");
        eventGenerationThreshold.setText("0 Events per second");
    }

    public void run()
    {
        try
        {
            Signals signals = workspace.getSignals();
            double dRSSData[] = null;
            double unwantedData[] = null;
            double blockingData[] = null;
            for(boolean updateGui = true; updateGui;)
            {
                dRSSData = signals.getDRSSVector().getEventVector().getEvents();
                unwantedData = ((IRSSVector)signals.getIRSSVectorListUnwanted().getIRSSVectors().get(0)).getEventVector().getEvents();
                blockingData = ((IRSSVector)signals.getIRSSVectorListBlocking().getIRSSVectors().get(0)).getEventVector().getEvents();
                if(dRSSData != null)
                {
                    dRSSMean = Mathematics.getAverage(dRSSData, currentEventIndex);
                    dRSSStdD = Mathematics.getStdDev(dRSSData, dRSSMean, currentEventIndex);
                }
                if(unwantedData != null)
                {
                    unwantedEmissionMean = Mathematics.getAverage(unwantedData, currentEventIndex);
                    unwantedEmissionStdD = Mathematics.getStdDev(unwantedData, unwantedEmissionMean, currentEventIndex);
                }
                if(blockingData != null)
                {
                    blockingMean = Mathematics.getAverage(blockingData, currentEventIndex);
                    blockingStdD = Mathematics.getStdDev(blockingData, blockingMean, currentEventIndex);
                }
                try
                {
                    eventsPrSec = (double)(currentEventIndex - eventStartIndex) / ((double)(lastEventCompletedAt - startTime) / 1000D);
                }
                catch(Exception ex2) { }
                boolean drssModifiedByPlugin = signals.getDRSSVector().getEventVector().isModifiedByPlugin();
                boolean irssUModifiedByPlugin = ((IRSSVector)signals.getIRSSVectorListUnwanted().getIRSSVectors().get(0)).getEventVector().isModifiedByPlugin();
                boolean irssBModifiedByPlugin = ((IRSSVector)signals.getIRSSVectorListBlocking().getIRSSVectors().get(0)).getEventVector().isModifiedByPlugin();
                dRSSMeanValue.setText((new StringBuilder()).append(format.format(dRSSMean)).append(" dBm").toString());
                dRSSStdDValue.setText((new StringBuilder()).append(format.format(dRSSStdD)).append(drssModifiedByPlugin ? "*" : "").toString());
                unwantedEmissionMeanValue.setText((new StringBuilder()).append(format.format(unwantedEmissionMean)).append(" dBm").toString());
                unwantedEmissionStdDValue.setText((new StringBuilder()).append(format.format(unwantedEmissionStdD)).append(irssUModifiedByPlugin ? "*" : "").toString());
                blockingMeanValue.setText((new StringBuilder()).append(format.format(blockingMean)).append(" dBm").toString());
                blockingStdDValue.setText((new StringBuilder()).append(format.format(blockingStdD)).append(irssBModifiedByPlugin ? "*" : "").toString());
                eventGenerationThreshold.setText((new StringBuilder()).append(format.format(eventsPrSec)).append(" Events per second").toString());
                try
                {
                    if(egeRunning)
                        Thread.sleep(500L);
                    else
                        updateGui = false;
                }
                catch(Exception e) { }
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void eventGenerationCompleted(int count)
    {
        egeRunning = false;
    }

    public void startingEventGeneration(Workspace _workspace, int eventsToBeCalculated, int _eventStartIndex)
    {
        workspace = _workspace;
        eventStartIndex = _eventStartIndex;
        startTime = System.currentTimeMillis();
        egeRunning = true;
        worker = new Thread(this, "Eventstatistics monitor");
        worker.start();
    }

    public void updateStatus(String s)
    {
    }

    public void eventCompleted(int eventsCompleted, int totalNumberOfEvents)
    {
        currentEventIndex = eventsCompleted;
        lastEventCompletedAt = System.currentTimeMillis();
    }

    public void generationAndEvaluationComplete()
    {
    }

    public void setCurrentProcessCompletionPercentage(int i)
    {
    }

    public void setTotalProcessCompletionPercentage(int i)
    {
    }

    public void incrementCurrentProcessCompletionPercentage(int i)
    {
    }

    public void incrementTotalProcessCompletionPercentage(int i)
    {
    }

    public void notifyError(String s)
    {
    }

    private static final ResourceBundle STRINGLIST;
    private JLabel dRSSMeanValue;
    private JLabel dRSSStdDValue;
    private JLabel unwantedEmissionMeanValue;
    private JLabel unwantedEmissionStdDValue;
    private JLabel blockingMeanValue;
    private JLabel blockingStdDValue;
    private JLabel eventGenerationThreshold;
    private double dRSSMean;
    private double dRSSStdD;
    private double unwantedEmissionMean;
    private double unwantedEmissionStdD;
    private double blockingMean;
    private double blockingStdD;
    private double eventsPrSec;
    private boolean egeRunning;
    private Workspace workspace;
    private Thread worker;
    private NumberFormat format;
    private int eventStartIndex;
    private int currentEventIndex;
    private static final String unit = " dBm";
    private long startTime;
    private long lastEventCompletedAt;
    private Font font;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
