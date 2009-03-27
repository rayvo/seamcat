// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ActivatedUsersGraph.java

package org.seamcat.cdma.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.seamcat.cdma.CDMABalancingListener;
import org.seamcat.cdma.UserTerminal;

public class ActivatedUsersGraph extends JPanel
    implements CDMABalancingListener
{

    public ActivatedUsersGraph()
    {
        totalCount = 0;
        activeCount = 0;
        droppedCount = 0;
        notActivatedCount = 0;
        ignoredCount = 0;
        active = new XYSeries("Active Users");
        notactive = new XYSeries("Not Activated Users");
        dropped = new XYSeries("Dropped Users");
        ignored = new XYSeries("Ignored Users");
        dataLoaded = false;
        dataset.addSeries(active);
        dataset.addSeries(notactive);
        dataset.addSeries(ignored);
        dataset.addSeries(dropped);
        chart = ChartFactory.createXYLineChart("CDMA System User status", "# of total generated voice active users", "User summation", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot)chart.getPlot();
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setDomainCrosshairVisible(true);
        NumberAxis domainAxis = (NumberAxis)chart.getXYPlot().getDomainAxis();
        domainAxis.setAutoRangeStickyZero(true);
        domainAxis.setAutoRangeIncludesZero(true);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        chartPanel.setVerticalAxisTrace(false);
        chartPanel.setHorizontalAxisTrace(false);
        chartPanel.setBorder(new TitledBorder(""));
        setLayout(new BorderLayout());
        add(chartPanel, "Center");
    }

    public void addEvent(int totalCount, int activeCount, int notActiveCount, int _dropped, int _ignored)
    {
        try
        {
            values[0][totalCount] = activeCount;
            values[1][totalCount] = notActiveCount;
            values[2][totalCount] = _ignored;
            values[3][totalCount] = _dropped;
        }
        catch(RuntimeException ex) { }
    }

    public void balancingStarted()
    {
        totalCount = 0;
        activeCount = 0;
        droppedCount = 0;
        notActivatedCount = 0;
        ignoredCount = 0;
        active.clear();
        notactive.clear();
        dropped.clear();
        ignored.clear();
        dataset.removeAllSeries();
        dataLoaded = false;
    }

    public void balancingComplete()
    {
    }

    public void setVisible(boolean val)
    {
        if(!dataLoaded && val)
        {
            for(int i = 0; i <= totalCount; i++)
            {
                active.add(i, values[0][i]);
                notactive.add(i, values[1][i]);
                ignored.add(i, values[2][i]);
                dropped.add(i, values[3][i]);
            }

            dataset.addSeries(active);
            dataset.addSeries(notactive);
            dataset.addSeries(ignored);
            dataset.addSeries(dropped);
            dataLoaded = true;
        }
        super.setVisible(val);
    }

    public void voiceActiveUserAdded(UserTerminal user)
    {
        totalCount++;
        activeCount++;
        addEvent(totalCount, activeCount, notActivatedCount, droppedCount, ignoredCount);
    }

    public void voiceActiveUserDropped(UserTerminal user)
    {
        totalCount++;
        droppedCount++;
        addEvent(totalCount, activeCount, notActivatedCount, droppedCount, ignoredCount);
    }

    public void voiceActiveUserNotActivated(UserTerminal user)
    {
        totalCount++;
        notActivatedCount++;
        addEvent(totalCount, activeCount, notActivatedCount, droppedCount, ignoredCount);
    }

    public void voiceInActiveUserAdded()
    {
        totalCount++;
        addEvent(totalCount, activeCount, notActivatedCount, droppedCount, ignoredCount);
    }

    public void voiceActiveUserIgnored()
    {
        totalCount++;
        ignoredCount++;
        addEvent(totalCount, activeCount, notActivatedCount, droppedCount, ignoredCount);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/presentation/ActivatedUsersGraph);
    private final ChartPanel chartPanel;
    private int totalCount;
    private int activeCount;
    private int droppedCount;
    private int notActivatedCount;
    private int ignoredCount;
    private static int values[][] = new int[4][5000];
    private XYSeries active;
    private XYSeries notactive;
    private XYSeries dropped;
    private XYSeries ignored;
    private final JFreeChart chart;
    private final XYSeriesCollection dataset = new XYSeriesCollection();
    private boolean dataLoaded;

}