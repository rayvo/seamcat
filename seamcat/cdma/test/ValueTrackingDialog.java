// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ValueTrackingDialog.java

package org.seamcat.cdma.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.seamcat.presentation.components.NavigateButtonPanel;

public class ValueTrackingDialog extends JFrame
{

    public transient ValueTrackingDialog(String title, String seriesTitles[])
    {
        super(title);
        vectorSeries = new ArrayList();
        dataSet = new XYSeriesCollection();
        valueCount = 0;
        String arr$[] = seriesTitles;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String st = arr$[i$];
            XYSeries series = new XYSeries(st);
            vectorSeries.add(series);
            dataSet.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(title, "Event #", "Value", dataSet, PlotOrientation.VERTICAL, true, true, false);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)chart.getXYPlot().getRenderer();
        renderer.setShapesVisible(true);
        renderer.setShapesFilled(true);
        vectorChart = new ChartPanel(chart);
        vectorChart.setMouseZoomable(true, false);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(vectorChart, "Center");
        getContentPane().add(new NavigateButtonPanel() {

            public void btnOkActionPerformed()
            {
                setVisible(false);
            }

            public void btnCancelActionPerformed()
            {
                setVisible(false);
            }

            final ValueTrackingDialog this$0;

            
            {
                this$0 = ValueTrackingDialog.this;
                super();
            }
        }
, "South");
        setSize(800, 600);
    }

    public void addValue(double value, int seriesIndex)
    {
        ((XYSeries)vectorSeries.get(seriesIndex)).add(valueCount, value);
    }

    public void tickEvent()
    {
        valueCount++;
    }

    public void addValue(double value)
    {
        addValue(value, 0);
    }

    public void clearValues()
    {
        vectorSeries.clear();
    }

    private ChartPanel vectorChart;
    private java.util.List vectorSeries;
    private XYSeriesCollection dataSet;
    private int valueCount;
}