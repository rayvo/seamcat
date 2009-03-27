// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ICEResultPanel.java

package org.seamcat.presentation.components.interferencecalc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Iterator;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.SeriesChangeEvent;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.seamcat.function.Point2D;
import org.seamcat.model.engines.ICEConfiguration;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class ICEResultPanel extends JPanel
{

    public ICEResultPanel()
    {
        super(new BorderLayout());
        setBorder(new TitledBorder("Results"));
        probability.setColumns(15);
        probability.setHorizontalAlignment(4);
        probability.setFormatterFactory(SeamcatTextFieldFormats.getDoubleFactory());
        JPanel top = new JPanel(new FlowLayout(0));
        top.setBorder(new TitledBorder("Compatibility (single result)"));
        top.add(new JLabel("Probability"));
        top.add(probability);
        top.add(modified);
        dataset.addSeries(resultSeries);
        chart = ChartFactory.createXYLineChart("", "", "Probability", dataset, PlotOrientation.VERTICAL, true, true, false);
        XYPlot xyPlot = (XYPlot)chart.getPlot();
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setDomainCrosshairVisible(true);
        NumberAxis domainAxis = (NumberAxis)chart.getXYPlot().getDomainAxis();
        domainAxis.setAutoRangeStickyZero(true);
        domainAxis.setAutoRangeIncludesZero(true);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 170));
        chartPanel.setVerticalAxisTrace(false);
        chartPanel.setHorizontalAxisTrace(false);
        chartPanel.setBorder(new TitledBorder("Translation (probability function of translation parameter)"));
        add(top, "North");
        add(chartPanel, "Center");
    }

    public void setProbabilityResult(double result)
    {
        probability.setValue(new Double(result));
        repaint();
    }

    public void addTranslationResult(Point2D point)
    {
        resultSeries.add(point.getX(), point.getY());
        dataset.seriesChanged(new SeriesChangeEvent(resultSeries));
    }

    public void init(ICEConfiguration iceconf)
    {
        if(iceconf.getHasBeenCalculated())
        {
            chartPanel.setEnabled(true);
            probability.setEnabled(true);
            probability.setValue(new Double(iceconf.getPropabilityResult()));
            java.util.List l = iceconf.getTranslationResults();
            resultSeries.clear();
            Point2D p;
            for(Iterator i$ = l.iterator(); i$.hasNext(); addTranslationResult(p))
                p = (Point2D)i$.next();

            if(iceconf.isSignalsModifiedByPlugin())
                modified.setText("Signals used in calculation has been modified by plugin");
            else
                modified.setText("");
        } else
        {
            resultSeries.clear();
            probability.setValue(null);
            chartPanel.setEnabled(false);
            probability.setEnabled(false);
            modified.setText("");
        }
    }

    private final JFormattedTextField probability = new JFormattedTextField(new Double(0.0D));
    private final JLabel modified = new JLabel("");
    private final JFreeChart chart;
    private ChartPanel chartPanel;
    private final XYSeriesCollection dataset = new XYSeriesCollection();
    private final XYSeries resultSeries = new XYSeries("dBm");
    private static final SeamcatTextFieldFormats dialogFormats = new SeamcatTextFieldFormats();

}
