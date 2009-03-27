// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DiscreteFunctionGraph.java

package org.seamcat.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.PolarChartPanel;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Point2D;
import org.seamcat.presentation.components.DiscreteFunctionTableModelAdapter;

public class DiscreteFunctionGraph extends JTabbedPane
    implements DatasetChangeListener
{

    public DiscreteFunctionGraph(DiscreteFunctionTableModelAdapter dataset, boolean enablePolar)
    {
        super(3);
        clickEnabled = false;
        enablePolarPlot = false;
        enablePolarPlot = enablePolar;
        this.dataset = dataset;
        xyChart = ChartFactory.createXYLineChart("Function", "", "", dataset, PlotOrientation.VERTICAL, true, true, false);
        polarChart = ChartFactory.createPolarChart("Function", dataset, true, true, false);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)xyChart.getXYPlot().getRenderer();
        renderer.setShapesVisible(true);
        renderer.setShapesFilled(true);
        dataset.addChangeListener(this);
        xyChartPanel = new ChartPanel(xyChart);
        xyChartPanel.setMouseZoomable(true, false);
        xyChartPanel.setPreferredSize(new Dimension(800, 600));
        xyChartPanel.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e)
            {
                if(clickEnabled)
                {
                    PlotRenderingInfo info = xyChartPanel.getChartRenderingInfo().getPlotInfo();
                    XYPlot plot = (XYPlot)xyChart.getPlot();
                    Rectangle2D dataArea = info.getDataArea();
                    double x = 0.0D;
                    double y = 0.0D;
                    if(dataArea.contains(e.getX(), e.getY()))
                    {
                        ValueAxis da = plot.getDomainAxis();
                        if(da != null)
                            x = da.java2DToValue(e.getX(), info.getDataArea(), plot.getDomainAxisEdge());
                        ValueAxis ra = plot.getRangeAxis();
                        if(ra != null)
                            y = ra.java2DToValue(e.getY(), info.getDataArea(), plot.getRangeAxisEdge());
                    }
                    double oldX = x;
                    double oldY = y;
                    x = (int)x;
                    y = (double)(int)(y * 100D) / 100D;
                    dataset.getDiscreteFunction().sortPoints();
                    for(Iterator i$ = dataset.getDiscreteFunction().getPointsList().iterator(); i$.hasNext();)
                    {
                        Point2D point = (Point2D)i$.next();
                        if(point.getX() == x)
                        {
                            point.setY(y);
                            return;
                        }
                    }

                    dataset.getDiscreteFunction().addPoint(new Point2D(x, y));
                    dataset.getDiscreteFunction().sortPoints();
                    dataset.fireChangeListeners();
                    datasetChanged(new DatasetChangeEvent(this, dataset));
                }
            }

            public void mousePressed(MouseEvent mouseevent)
            {
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
            }

            public void mouseEntered(MouseEvent mouseevent)
            {
            }

            public void mouseExited(MouseEvent mouseevent)
            {
            }

            final DiscreteFunctionGraph this$0;

            
            {
                this$0 = DiscreteFunctionGraph.this;
                super();
            }
        }
);
        if(enablePolarPlot)
        {
            polarChartPanel = new PolarChartPanel(polarChart);
            polarChartPanel.setMouseZoomable(true, true);
            polarChartPanel.setPreferredSize(new Dimension(800, 600));
        }
        final JCheckBox enableClickModify = new JCheckBox("Enable \"Click to insert point\"");
        enableClickModify.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                clickEnabled = enableClickModify.isSelected();
            }

            final JCheckBox val$enableClickModify;
            final DiscreteFunctionGraph this$0;

            
            {
                this$0 = DiscreteFunctionGraph.this;
                enableClickModify = jcheckbox;
                super();
            }
        }
);
        JPanel first = new JPanel(new BorderLayout());
        first.add(xyChartPanel, "Center");
        first.add(enableClickModify, "South");
        addTab("X & Y", first);
        if(enablePolarPlot)
            addTab("Polar", polarChartPanel);
    }

    public void datasetChanged(DatasetChangeEvent dce)
    {
        log.debug("Chart received DatasetChangeEvent");
        xyChart.getPlot().datasetChanged(dce);
        polarChart.getPlot().datasetChanged(dce);
    }

    public void setDiscreteFunction(DiscreteFunction function)
    {
        dataset.setDiscreteFunction(function);
        ((PolarPlot)polarChart.getPlot()).getAxis().setRange(function.getRangeMin(), function.getRangeMax());
    }

    public DiscreteFunction getDiscreteFunction()
    {
        return dataset.getDiscreteFunction();
    }

    private static final Logger log = Logger.getLogger(org/seamcat/presentation/DiscreteFunctionGraph);
    private DiscreteFunctionTableModelAdapter dataset;
    private JFreeChart xyChart;
    private ChartPanel xyChartPanel;
    private JFreeChart polarChart;
    private ChartPanel polarChartPanel;
    private boolean clickEnabled;
    private boolean enablePolarPlot;






}
