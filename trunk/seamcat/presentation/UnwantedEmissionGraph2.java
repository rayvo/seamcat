// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UnwantedEmissionGraph2.java

package org.seamcat.presentation;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.presentation.components.DiscreteFunction2TableModelAdapter;

public class UnwantedEmissionGraph2 extends JPanel
    implements DatasetChangeListener
{

    public UnwantedEmissionGraph2(DiscreteFunction2TableModelAdapter dataset)
    {
        super(new GridLayout());
        this.dataset = dataset;
        chart = ChartFactory.createXYLineChart(STRINGLIST.getString("EMISSION_GRAPH_TITLE"), STRINGLIST.getString("EMISSION_GRAPH_AXIX_TITLE_X"), STRINGLIST.getString("EMISSION_GRAPH_AXIX_TITLE_Y"), dataset, PlotOrientation.VERTICAL, true, true, false);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)chart.getXYPlot().getRenderer();
        renderer.setShapesVisible(true);
        renderer.setShapesFilled(true);
        dataset.addChangeListener(this);
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setPreferredSize(new Dimension(400, 350));
        add(chartPanel);
    }

    public void datasetChanged(DatasetChangeEvent dce)
    {
        log.debug("Chart received DatasetChangeEvent");
        double max = dataset.getMaxValue();
        double min = dataset.getMinValue();
        if(max != min)
        {
            double buf = (max - min) / 10D;
            ((NumberAxis)chartPanel.getChart().getXYPlot().getRangeAxis()).setAutoRange(false);
            ((NumberAxis)chartPanel.getChart().getXYPlot().getRangeAxis()).setRange(new Range(min - buf, max + buf));
        }
        chart.getPlot().datasetChanged(dce);
    }

    public void setDiscreteFunction2(DiscreteFunction2 function)
    {
        dataset.setDiscreteFunction2(function);
    }

    public DiscreteFunction2 getDiscreteFunction2()
    {
        return dataset.getDiscreteFunction2();
    }

    private static final Logger log = Logger.getLogger(org/seamcat/presentation/UnwantedEmissionGraph2);
    private static final ResourceBundle STRINGLIST;
    private DiscreteFunction2TableModelAdapter dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
