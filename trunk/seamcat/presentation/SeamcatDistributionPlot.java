// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatDistributionPlot.java

package org.seamcat.presentation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.seamcat.cdma.NonInterferedCapacityListener;
import org.seamcat.cdma.presentation.CapacityFindingStatusPanel;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.Workspace;
import org.seamcat.model.engines.EventCompletionListener;
import org.seamcat.model.engines.PositionListener;
import org.seamcat.presentation.components.EventStatisticsPanel;
import org.seamcat.presentation.components.EventStatusPanel;

public class SeamcatDistributionPlot extends JPanel
    implements PositionListener, EventCompletionListener
{
    class PlotElement
    {

        public double getX(PlotElement origin)
        {
            return x - origin.getX();
        }

        public double getY(PlotElement origin)
        {
            return y - origin.getY();
        }

        public double getX()
        {
            return x;
        }

        public double getY()
        {
            return y;
        }

        private double x;
        private double y;
        final SeamcatDistributionPlot this$0;

        PlotElement(double _x, double _y)
        {
            this$0 = SeamcatDistributionPlot.this;
            super();
            x = _x;
            y = _y;
        }
    }


    public void setMaxEventsToPlot(int maxEventsToPlot)
    {
        this.maxEventsToPlot = maxEventsToPlot;
    }

    public int getMaxEventsToPlot()
    {
        return maxEventsToPlot;
    }

    public SeamcatDistributionPlot(String workspaceTitle, String vcrTitle, int total, int shown)
    {
        super(true);
        wrSeries = new XYSeries(STRINGLIST.getString("WANTED_RECEIVER_SERIES_TITLE"));
        wtSeries = new XYSeries(STRINGLIST.getString("WANTED_TRANSMITTER_SERIES_TITLE"));
        ItSeries = new XYSeries(STRINGLIST.getString("INTERFERING_TRANSMITTER_SERIES_TITLE"));
        vrSeries = new XYSeries(STRINGLIST.getString("VICTIM_RECEIVER_SERIES_TITLE"));
        maxEventsStep = 1000;
        plotPerspective = 0;
        updateWrSeries = false;
        updateWtSeries = false;
        updateItSeries = false;
        updateVrSeries = false;
        runClock = true;
        wantedTransmitters = new ArrayList();
        victimReceivers = new ArrayList();
        wantedReceivers = new ArrayList();
        interferingTransmitters = new ArrayList();
        eventStatsPanel = new EventStatisticsPanel();
        eventStatusPanel = new EventStatusPanel();
        capacityPanel = new CapacityFindingStatusPanel();
        initComponents();
        maxEventsToPlot = maxEventsStep;
        lblWorkspaceValue.setText(workspaceTitle);
        lblReferenceValue.setText(vcrTitle);
        lblTotalValue.setText("0");
        lblShownValue.setText("0");
        dataset.addSeries(ItSeries);
        dataset.addSeries(wtSeries);
        dataset.addSeries(wrSeries);
        dataset.addSeries(vrSeries);
        chart = ChartFactory.createScatterPlot(STRINGLIST.getString("SCENARIO_PLOT_TITLE"), STRINGLIST.getString("SCENARIO_PLOT_AXIX_TITLE_X"), STRINGLIST.getString("SCENARIO_PLOT_AXIX_TITLE_Y"), dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot)chart.getPlot();
        xyPlot.getRenderer().setSeriesPaint(3, new Color(255, 204, 51));
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setDomainCrosshairVisible(true);
        NumberAxis domainAxis = (NumberAxis)chart.getXYPlot().getDomainAxis();
        domainAxis.setAutoRangeStickyZero(true);
        domainAxis.setAutoRangeIncludesZero(true);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        chartPanel.setVerticalAxisTrace(false);
        chartPanel.setHorizontalAxisTrace(false);
        chartPanel.setBorder(new TitledBorder(STRINGLIST.getString("SCENARIO_TITLE")));
        capacityPanel = new CapacityFindingStatusPanel();
        cardLayout = new CardLayout();
        charts = new JPanel(cardLayout);
        charts.add(chartPanel, "SDP");
        charts.add(capacityPanel, "CDMA");
        cardLayout.show(charts, "SDP");
        add(charts, "Center");
        add(LabelsPanel, "North");
        add(eventStatusPanel, "South");
    }

    public void showCDMACapacityPanel()
    {
        cardLayout.show(charts, "CDMA");
    }

    public void showSEAMCATOutlinePanel()
    {
        cardLayout.show(charts, "SDP");
    }

    public NonInterferedCapacityListener getNonInterferedCapacityListener()
    {
        return capacityPanel;
    }

    private void initComponents()
    {
        setLayout(new BorderLayout());
        LabelsPanel = new JPanel();
        TitlePanel = new JPanel();
        lblWorkspace = new JLabel();
        lblWorkspaceValue = new JLabel();
        lblReference = new JLabel();
        lblReferenceValue = new JLabel();
        DataPanel = new JPanel();
        lblTotal = new JLabel();
        lblTotalValue = new JLabel();
        lblShown = new JLabel();
        lblShownValue = new JLabel();
        LabelsPanel.setLayout(new GridLayout(1, 2));
        TitlePanel.setLayout(new GridLayout(6, 2));
        lblWorkspace.setText(STRINGLIST.getString("SCENARIO_SUMMARY_WORKSPACE_PREFIX"));
        TitlePanel.add(lblWorkspace);
        TitlePanel.add(lblWorkspaceValue);
        lblReference.setText(STRINGLIST.getString("SCENARIO_SUMMARY_VICTIM_PREFIX"));
        TitlePanel.add(lblReference);
        TitlePanel.add(lblReferenceValue);
        DataPanel.setLayout(new FlowLayout(0));
        lblTotal.setText(STRINGLIST.getString("SCENARIO_SUMMARY_TOTAL_PREFIX"));
        TitlePanel.add(lblTotal);
        TitlePanel.add(lblTotalValue);
        lblShown.setText(STRINGLIST.getString("SCENARIO_SUMMARY_TOTALSHOWN_PREFIX"));
        timeUseTitle = new JLabel(STRINGLIST.getString("SCENARIO_SUMMARY_ELAPSED_PREFIX"));
        timeUseLabel = new JLabel("0h 0m 0s");
        timeRemainingTitle = new JLabel(STRINGLIST.getString("SCENARIO_SUMMARY_REMAINING_PREFIX"));
        timeRemainingLabel = new JLabel("-");
        TitlePanel.add(lblShown);
        TitlePanel.add(lblShownValue);
        TitlePanel.add(timeUseTitle);
        TitlePanel.add(timeUseLabel);
        TitlePanel.add(timeRemainingTitle);
        TitlePanel.add(timeRemainingLabel);
        TitlePanel.setBorder(new TitledBorder(STRINGLIST.getString("SCENARIO_SUMMARY_TITLE")));
        LabelsPanel.add(TitlePanel);
        LabelsPanel.add(eventStatsPanel);
    }

    public void clearAllElements()
    {
        ItSeries.clear();
        dataset.seriesChanged(new SeriesChangeEvent(ItSeries));
        vrSeries.clear();
        dataset.seriesChanged(new SeriesChangeEvent(vrSeries));
        wrSeries.clear();
        dataset.seriesChanged(new SeriesChangeEvent(wrSeries));
        wtSeries.clear();
        dataset.seriesChanged(new SeriesChangeEvent(wtSeries));
        lblShownValue.setText(Integer.toString(0));
        lblTotalValue.setText(Integer.toString(0));
    }

    public void resetStatistics()
    {
        eventStatsPanel.reset();
        lblShownValue.setText("0");
        timeUseLabel.setText("0h 0m 0s");
    }

    public void addInterferingTransmitter(double x, double y)
    {
        if(ItSeries.getItemCount() < maxEventsToPlot)
        {
            ItSeries.add(x, y);
            interferingTransmitters.add(new PlotElement(x, y));
            updateItSeries = true;
        }
    }

    public void addVictimReceiver(double x, double y)
    {
        if(vrSeries.getItemCount() < maxEventsToPlot)
        {
            vrSeries.add(x, y);
            victimReceivers.add(new PlotElement(x, y));
            updateVrSeries = true;
        }
    }

    public void addWantedTransmitter(double x, double y)
    {
        if(wtSeries.getItemCount() < maxEventsToPlot)
        {
            wtSeries.add(x, y);
            wantedTransmitters.add(new PlotElement(x, y));
            updateWtSeries = true;
        }
    }

    public void addWantedReceiver(double x, double y)
    {
        if(wrSeries.getItemCount() < maxEventsToPlot)
        {
            wrSeries.add(x, y);
            wantedReceivers.add(new PlotElement(x, y));
            updateWrSeries = true;
        }
    }

    public void setWorkspaceName(String name)
    {
        lblWorkspaceValue.setText(name);
    }

    public void setVictimSystemLinkName(String name)
    {
        lblReferenceValue.setText(name);
    }

    public void setNumberOfEvents(int eventCount)
    {
        eventsCompleted = eventCount;
    }

    public synchronized void eventCompleted(int eventsCompleted, int totalNumberOfEvents)
    {
        eventStatsPanel.eventCompleted(eventsCompleted, totalNumberOfEvents);
        eventStatusPanel.eventCompleted(eventsCompleted, totalNumberOfEvents);
        setNumberOfEvents(eventsCompleted);
        lastEventCompletedAt = System.currentTimeMillis();
    }

    public void eventGenerationCompleted(int count)
    {
        setNumberOfEvents(count);
        eventStatusPanel.eventGenerationCompleted(count);
        eventStatsPanel.eventGenerationCompleted(count);
        updateGuiComponents();
    }

    public void updateStatus(String _status)
    {
        eventStatusPanel.updateStatus(_status);
    }

    public void startingEventGeneration(Workspace workspace, int _eventsToBeCalculated, int _eventStartIndex)
    {
        startTime = System.currentTimeMillis();
        eventsToBeCalculated = _eventsToBeCalculated;
        egeRunning = true;
        worker = new Thread("Scenario outline plot") {

            public synchronized void run()
            {
                while(egeRunning) 
                    try
                    {
                        updateGuiComponents();
                        wait(500L);
                    }
                    catch(Exception e) { }
            }

            final SeamcatDistributionPlot this$0;

            
            {
                this$0 = SeamcatDistributionPlot.this;
                super(x0);
            }
        }
;
        worker.start();
        eventStatsPanel.startingEventGeneration(workspace, eventsToBeCalculated, _eventStartIndex);
        eventStatusPanel.startingEventGeneration(workspace, eventsToBeCalculated, _eventStartIndex);
    }

    public void updateGuiComponents()
    {
        if(updateWrSeries)
            dataset.seriesChanged(new SeriesChangeEvent(wrSeries));
        if(updateItSeries)
            dataset.seriesChanged(new SeriesChangeEvent(ItSeries));
        if(updateVrSeries)
            dataset.seriesChanged(new SeriesChangeEvent(vrSeries));
        if(updateWtSeries)
            dataset.seriesChanged(new SeriesChangeEvent(wtSeries));
        if(updateWrSeries || updateItSeries || updateVrSeries || updateWtSeries)
            lblShownValue.setText(Integer.toString(wtSeries.getItemCount() + wrSeries.getItemCount() + ItSeries.getItemCount() + vrSeries.getItemCount()));
        lblTotalValue.setText(Integer.toString(eventsCompleted));
        updateWrSeries = false;
        updateWtSeries = false;
        updateItSeries = false;
        updateVrSeries = false;
        try
        {
            if(startTime > 0L)
            {
                elapsedTime = (System.currentTimeMillis() - startTime) / 1000L;
                int hours = (int)(elapsedTime / 3600L);
                elapsedTime -= hours * 3600;
                int minutes = (int)(elapsedTime / 60L);
                elapsedTime -= minutes * 60;
                timeUseLabel.setText((new StringBuilder()).append(hours).append("h ").append(minutes >= 10 ? "" : "0").append(minutes).append("m ").append(elapsedTime >= 10L ? "" : "0").append(elapsedTime).append("s").toString());
                if(eventsCompleted > 1)
                {
                    double timePrEvent = 0.0D;
                    timePrEvent = (lastEventCompletedAt - startTime) / (long)eventsCompleted;
                    double remaining = (double)(eventsToBeCalculated - eventsCompleted) * timePrEvent;
                    remaining -= System.currentTimeMillis() - lastEventCompletedAt;
                    if(remaining > 1000D)
                    {
                        remaining /= 1000D;
                        hours = (int)(remaining / 3600D);
                        remaining -= hours * 3600;
                        minutes = (int)(remaining / 60D);
                        remaining -= minutes * 60;
                        timeRemainingLabel.setText((new StringBuilder()).append(hours).append("h ").append(minutes >= 10 ? "" : "0").append(minutes).append("m ").append((int)remaining >= 10 ? "" : "0").append((int)remaining).append("s").toString());
                    } else
                    {
                        timeRemainingLabel.setText("-");
                    }
                }
            }
        }
        catch(RuntimeException e)
        {
            timeRemainingLabel.setText("-");
            timeUseLabel.setText("-");
        }
    }

    public void generationAndEvaluationComplete()
    {
        egeRunning = false;
        timeRemainingLabel.setText("-");
        eventStatusPanel.setCurrentProcessCompletionPercentage(100);
    }

    public void setCurrentProcessCompletionPercentage(int value)
    {
        eventStatusPanel.setCurrentProcessCompletionPercentage(value);
    }

    public void setTotalProcessCompletionPercentage(int value)
    {
        eventStatusPanel.setTotalProcessCompletionPercentage(value);
    }

    public void incrementCurrentProcessCompletionPercentage(int value)
    {
        eventStatusPanel.incrementCurrentProcessCompletionPercentage(value);
    }

    public void incrementTotalProcessCompletionPercentage(int value)
    {
        eventStatusPanel.incrementTotalProcessCompletionPercentage(value);
    }

    public void notifyError(String error)
    {
        JOptionPane.showMessageDialog(this, error, "EGE Error", 0);
    }

    public void stopThread()
    {
        runClock = false;
    }

    public void startThread()
    {
        clearAllElements();
        double radius = 5D;
        for(int i = 0; i < 360; i++)
            wtSeries.add(5D * Mathematics.cosD(i), 5D * Mathematics.sinD(i));

        runClock = true;
        Thread t = new Thread() {

            public void run()
            {
                TimeZone tz = TimeZone.getDefault();
                GregorianCalendar cal = new GregorianCalendar(tz);
                long hours = 0L;
                long minutes = 0L;
                long seconds = 0L;
                double secAngle = 0.0D;
                double minAngle = 0.0D;
                double hourAngle = 0.0D;
                while(runClock) 
                {
                    cal.setTimeInMillis(System.currentTimeMillis());
                    wrSeries.clear();
                    ItSeries.clear();
                    vrSeries.clear();
                    hours = cal.get(11);
                    minutes = cal.get(12);
                    seconds = cal.get(13);
                    setVictimSystemLinkName((new StringBuilder()).append(hours >= 10L ? "" : "0").append(hours).append(":").append(minutes >= 10L ? "" : "0").append(minutes).append(":").append(seconds >= 10L ? "" : "0").append(seconds).toString());
                    secAngle = 360L - (270L + seconds * 6L);
                    minAngle = (double)(360L - (270L + minutes * 6L)) - 6D * ((double)seconds / 60D);
                    hourAngle = (double)(360L - (270L + hours * 30L)) - 30D * ((double)minutes / 60D + (double)seconds / 3600D);
                    for(double i = 0.0D; i < 5D; i += 0.25D)
                    {
                        wrSeries.add(i * Mathematics.cosD(secAngle), i * Mathematics.sinD(secAngle));
                        ItSeries.add(i * Mathematics.cosD(minAngle), i * Mathematics.sinD(minAngle));
                        if(i < 3.75D)
                            vrSeries.add(i * Mathematics.cosD(hourAngle), i * Mathematics.sinD(hourAngle));
                    }

                    try
                    {
                        Runtime.getRuntime().gc();
                        Thread.sleep(500L);
                    }
                    catch(Exception e)
                    {
                        runClock = false;
                    }
                }
            }

            final SeamcatDistributionPlot this$0;

            
            {
                this$0 = SeamcatDistributionPlot.this;
                super();
            }
        }
;
        t.start();
    }

    private static final ResourceBundle STRINGLIST;
    private static final int PERSPECTIVE_WT = 0;
    private static final int PERSPECTIVE_WR = 1;
    private static final int PERSPECTIVE_IT = 2;
    private static final int PERSPECTIVE_VR = 3;
    private final JFreeChart chart;
    private final XYSeriesCollection dataset = new XYSeriesCollection();
    private final ChartPanel chartPanel;
    private XYSeries wrSeries;
    private XYSeries wtSeries;
    private XYSeries ItSeries;
    private XYSeries vrSeries;
    private int maxEventsStep;
    private int maxEventsToPlot;
    private int plotPerspective;
    private boolean updateWrSeries;
    private boolean updateWtSeries;
    private boolean updateItSeries;
    private boolean updateVrSeries;
    private boolean runClock;
    private int eventsCompleted;
    private int eventsToBeCalculated;
    private JPanel DataPanel;
    private JPanel LabelsPanel;
    private JPanel TitlePanel;
    private JLabel lblReference;
    private JLabel lblReferenceValue;
    private JLabel lblShown;
    private JLabel lblShownValue;
    private JLabel lblTotal;
    private JLabel lblTotalValue;
    private JLabel lblWorkspace;
    private JLabel lblWorkspaceValue;
    private JLabel timeUseTitle;
    private JLabel timeUseLabel;
    private JLabel timeRemainingTitle;
    private JLabel timeRemainingLabel;
    private Thread worker;
    private boolean egeRunning;
    private long startTime;
    private long elapsedTime;
    private long lastEventCompletedAt;
    private EventStatisticsPanel eventStatsPanel;
    private EventStatusPanel eventStatusPanel;
    private CapacityFindingStatusPanel capacityPanel;
    private JPanel charts;
    private CardLayout cardLayout;
    private java.util.List wantedTransmitters;
    private java.util.List victimReceivers;
    private java.util.List wantedReceivers;
    private java.util.List interferingTransmitters;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }






}
