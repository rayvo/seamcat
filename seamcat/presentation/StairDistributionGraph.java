// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StairDistributionGraph.java

package org.seamcat.presentation;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.jfree.chart.*;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.seamcat.distribution.StairDistribution;
import org.seamcat.function.Point2D;
import org.seamcat.presentation.components.StairDistributionTableModelAdapter;

// Referenced classes of package org.seamcat.presentation:
//            DiscreteFunctionGraph

public class StairDistributionGraph extends JPanel
    implements DatasetChangeListener
{

    public StairDistributionGraph(StairDistributionTableModelAdapter dataset)
    {
        super(new GridLayout());
        this.dataset = dataset;
        CategoryDataset dset = dataset.getCategoryDS();
        chart = ChartFactory.createBarChart("Stair Distribution", "Value", "Probability", dset, PlotOrientation.VERTICAL, false, true, false);
        dset.addChangeListener(this);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setPreferredSize(new Dimension(400, 350));
        add(chartPanel);
    }

    public void datasetChanged(DatasetChangeEvent dce)
    {
        LOG.debug("Chart received DatasetChangeEvent");
        chart.getPlot().datasetChanged(dce);
    }

    public void setStairDistribution(StairDistribution dist)
    {
        dataset.setPoints(dist);
    }

    public static void main(String args[])
    {
        StairDistribution s = new StairDistribution();
        s.add(new Point2D(1.0D, 29D));
        s.add(new Point2D(2D, 61D));
        s.add(new Point2D(3D, 15D));
        s.add(new Point2D(4D, 34D));
        s.add(new Point2D(5D, 43D));
        s.add(new Point2D(6D, 54D));
        s.add(new Point2D(7D, 57D));
        s.add(new Point2D(8D, 4D));
        s.add(new Point2D(9D, 39D));
        s.add(new Point2D(10D, 17D));
        s.add(new Point2D(11D, 8D));
        s.add(new Point2D(12D, 23D));
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(3);
        StairDistributionGraph sg = new StairDistributionGraph(new StairDistributionTableModelAdapter());
        sg.setStairDistribution(s);
        f.getContentPane().add(sg);
        f.pack();
        f.setVisible(true);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/DiscreteFunctionGraph);
    private final StairDistributionTableModelAdapter dataset;
    private JFreeChart chart;

}
