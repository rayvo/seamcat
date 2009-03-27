// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMACapacityPanel.java

package org.seamcat.cdma.presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.xy.XYDataset;

public class CDMACapacityPanel extends JPanel
    implements DatasetChangeListener
{
    private static class OutageDataset extends Dataset
    {

        public Number getX(int series, int item)
        {
            double x;
            switch(series)
            {
            case 0: // '\0'
            case 1: // '\001'
            case 2: // '\002'
                x = item;
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("Series value out of range <").append(series).append(">").toString());
            }
            return Double.valueOf(x);
        }

        public Number getY(int series, int item)
        {
            double y;
            switch(series)
            {
            case 0: // '\0'
                y = noInterferenceCapacity[item];
                break;

            case 1: // '\001'
                y = interferenceCapacity[item];
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("Series value out of range <").append(series).append(">").toString());
            }
            return Double.valueOf(y);
        }

        public double getXValue(int series, int item)
        {
            return getX(series, item).doubleValue();
        }

        public double getYValue(int series, int item)
        {
            return getY(series, item).doubleValue();
        }

        public int getSeriesCount()
        {
            return 2;
        }

        public String getSeriesName(int series)
        {
            String s;
            switch(series)
            {
            case 0: // '\0'
                s = "Initial Outage percentage";
                break;

            case 1: // '\001'
                s = "Interfered Outage percentage";
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("Series value out of range <").append(series).append(">").toString());
            }
            return s;
        }

        public DomainOrder getDomainOrder()
        {
            return DomainOrder.ASCENDING;
        }

        public Comparable getSeriesKey(int arg0)
        {
            return getSeriesName(arg0);
        }

        public int indexOf(Comparable ser)
        {
            return !ser.equals("Initial Outage percentage") ? 1 : 0;
        }

        private static final String SERIES_0 = "Initial Outage percentage";
        private static final String SERIES_1 = "Interfered Outage percentage";
        public double movingAverageLoss[];

        private OutageDataset(double initialOutage[], double interferedOutage[])
        {
            super(initialOutage, interferedOutage);
            movingAverageLoss = CDMACapacityPanel.DUMMYARRAY;
        }

    }

    private static class CDMACapacityDataset extends Dataset
    {

        public Number getX(int series, int item)
        {
            double x;
            switch(series)
            {
            case 0: // '\0'
            case 1: // '\001'
            case 2: // '\002'
                x = item;
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("Series value out of range <").append(series).append(">").toString());
            }
            return Double.valueOf(x);
        }

        public Number getY(int series, int item)
        {
            double y;
            switch(series)
            {
            case 0: // '\0'
                y = noInterferenceCapacity[item];
                break;

            case 1: // '\001'
                y = interferenceCapacity[item];
                break;

            case 2: // '\002'
                y = noInterferenceCapacity[item] - interferenceCapacity[item];
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("Series value out of range <").append(series).append(">").toString());
            }
            return Double.valueOf(y);
        }

        public double getXValue(int series, int item)
        {
            return getX(series, item).doubleValue();
        }

        public double getYValue(int series, int item)
        {
            return getY(series, item).doubleValue();
        }

        public int getSeriesCount()
        {
            return 3;
        }

        public String getSeriesName(int series)
        {
            String s;
            switch(series)
            {
            case 0: // '\0'
                s = "Initial Capacity";
                break;

            case 1: // '\001'
                s = "Interfered Capacity";
                break;

            case 2: // '\002'
                s = "Excess outage, users";
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("Series value out of range <").append(series).append(">").toString());
            }
            return s;
        }

        public DomainOrder getDomainOrder()
        {
            return DomainOrder.ASCENDING;
        }

        public Comparable getSeriesKey(int arg0)
        {
            return getSeriesName(arg0);
        }

        public int indexOf(Comparable ser)
        {
            return !ser.equals("Initial Capacity") ? 1 : 0;
        }

        private static final String SERIES_0 = "Initial Capacity";
        private static final String SERIES_1 = "Interfered Capacity";
        private static final String SERIES_2 = "Excess outage, users";

        private CDMACapacityDataset(double noInterferenceCapacity[], double interferenceCapacity[])
        {
            super(noInterferenceCapacity, interferenceCapacity);
        }

    }

    private static abstract class Dataset
        implements XYDataset
    {

        protected final void setData(double noInterferenceCapacity[], double interferenceCapacity[])
        {
            if(noInterferenceCapacity.length == interferenceCapacity.length)
            {
                this.noInterferenceCapacity = noInterferenceCapacity;
                this.interferenceCapacity = interferenceCapacity;
                size = noInterferenceCapacity.length;
                fireChangeListeners();
            } else
            {
                throw new IllegalArgumentException((new StringBuilder()).append("Capacity array dimensions must be equal (").append(noInterferenceCapacity.length).append(" != ").append(interferenceCapacity.length).append(")").toString());
            }
        }

        public int getItemCount(int series)
        {
            return size;
        }

        public void addChangeListener(DatasetChangeListener listener)
        {
            datasetChangeListeners.add(listener);
        }

        public DatasetGroup getGroup()
        {
            return datasetGroup;
        }

        public void removeChangeListener(DatasetChangeListener listener)
        {
            datasetChangeListeners.remove(listener);
        }

        public void setGroup(DatasetGroup datasetGroup)
        {
            this.datasetGroup = datasetGroup;
        }

        protected void fireChangeListeners()
        {
            for(Iterator i = datasetChangeListeners.iterator(); i.hasNext(); ((DatasetChangeListener)i.next()).datasetChanged(stdDatasetChangeEvent));
        }

        private DatasetGroup datasetGroup;
        private DatasetChangeEvent stdDatasetChangeEvent;
        protected double noInterferenceCapacity[];
        protected double interferenceCapacity[];
        protected int size;
        private java.util.List datasetChangeListeners;

        protected Dataset(double noInterferenceCapacity[], double interferenceCapacity[])
        {
            datasetGroup = new DatasetGroup();
            stdDatasetChangeEvent = new DatasetChangeEvent(this, this);
            datasetChangeListeners = new ArrayList();
            setData(noInterferenceCapacity, interferenceCapacity);
        }
    }


    public CDMACapacityPanel()
    {
        capacityDataset = new CDMACapacityDataset(DUMMYARRAY, DUMMYARRAY);
        outageDataset = new OutageDataset(DUMMYARRAY, DUMMYARRAY);
        tfAvgLoss = new JTextField();
        tfAvgSystemLoss = new JTextField();
        nf = new DecimalFormat("###.00");
        capacityChart = ChartFactory.createXYLineChart("CDMA Reference Cell Capacity", "Events", "Units", capacityDataset, PlotOrientation.VERTICAL, true, true, false);
        XYLineAndShapeRenderer capacityRenderer = (XYLineAndShapeRenderer)capacityChart.getXYPlot().getRenderer();
        capacityRenderer.setShapesVisible(false);
        capacityRenderer.setShapesFilled(true);
        capacityDataset.addChangeListener(this);
        ChartPanel capacityPanel = new ChartPanel(capacityChart);
        capacityPanel.setMouseZoomable(true, false);
        capacityPanel.setPreferredSize(new Dimension(500, 200));
        outageChart = ChartFactory.createXYLineChart("CDMA Reference Cell outage", "Events", "Outage percentage", outageDataset, PlotOrientation.VERTICAL, true, true, false);
        XYLineAndShapeRenderer lossRenderer = (XYLineAndShapeRenderer)outageChart.getXYPlot().getRenderer();
        lossRenderer.setShapesVisible(false);
        lossRenderer.setShapesFilled(true);
        outageDataset.addChangeListener(this);
        ChartPanel outagePanel = new ChartPanel(outageChart);
        outagePanel.setMouseZoomable(true, false);
        outagePanel.setPreferredSize(new Dimension(500, 200));
        JLabel lbAvgLoss = new JLabel("Average capacity loss in reference cell");
        tfAvgLoss.setColumns(4);
        tfAvgLoss.setEditable(false);
        JLabel lbAvgSystemLoss = new JLabel("Average capacity loss in victim CDMA system");
        tfAvgSystemLoss.setColumns(4);
        tfAvgSystemLoss.setEditable(false);
        JPanel avgLossPanel = new JPanel(new FlowLayout(0));
        avgLossPanel.add(lbAvgLoss);
        avgLossPanel.add(tfAvgLoss);
        avgLossPanel.add(lbAvgSystemLoss);
        avgLossPanel.add(tfAvgSystemLoss);
        setLayout(new BorderLayout());
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(capacityPanel);
        centerPanel.add(outagePanel);
        add(centerPanel, "Center");
        add(avgLossPanel, "South");
    }

    public void resetStatistics()
    {
        setData(DUMMYARRAY, DUMMYARRAY, DUMMYARRAY, DUMMYARRAY, DUMMYARRAY, 1.0D);
        tfAvgLoss.setText("-");
        tfAvgSystemLoss.setText("-");
    }

    public void datasetChanged(DatasetChangeEvent dce)
    {
        log.debug("Chart received DatasetChangeEvent");
        capacityChart.getPlot().datasetChanged(dce);
        outageChart.getPlot().datasetChanged(dce);
    }

    public void setData(double initialCapacity[], double interferedCapacity[], double initialOutage[], double inteferedOutage[], double totalDroppedSystem[], double totalCapacitySystem)
    {
        if(initialCapacity.length != interferedCapacity.length)
            throw new IllegalArgumentException((new StringBuilder()).append("Array dimensions must be the same (initial size = ").append(initialCapacity.length).append(" and interfered size = ").append(interferedCapacity.length).append(")").toString());
        double displayNoInt[];
        double displayInt[];
        if(initialCapacity.length >= 1000)
        {
            int factor = initialCapacity.length / 1000;
            displayNoInt = new double[Math.min(initialCapacity.length, 1000)];
            displayInt = new double[Math.min(initialCapacity.length, 1000)];
            int i = 0;
            for(int j = 0; j < displayNoInt.length;)
            {
                displayNoInt[j] = initialCapacity[i];
                displayInt[j] = interferedCapacity[i];
                j++;
                i += factor;
            }

        } else
        {
            displayNoInt = initialCapacity;
            displayInt = interferedCapacity;
        }
        capacityDataset.setData(displayNoInt, displayInt);
        tfAvgLoss.setText(nf.format(calculateLossAvg(displayNoInt, displayInt)));
        double avgLossSystem = 0.0D;
        double arr$[] = totalDroppedSystem;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            double d = arr$[i$];
            avgLossSystem += d;
        }

        avgLossSystem /= totalDroppedSystem.length;
        avgLossSystem /= totalCapacitySystem;
        avgLossSystem *= 100D;
        tfAvgSystemLoss.setText(nf.format(avgLossSystem));
        outageDataset.setData(initialOutage, inteferedOutage);
    }

    private double calculateLossAvg(double noInterferenceCapacity[], double interferenceCapacity[])
    {
        double avg = 0.0D;
        int size = noInterferenceCapacity.length;
        outageDataset.movingAverageLoss = new double[size];
        for(int x = 0; x < size; x++)
        {
            avg += 100D - (interferenceCapacity[x] / noInterferenceCapacity[x]) * 100D;
            outageDataset.movingAverageLoss[x] = avg / (double)(x + 1);
        }

        return avg / (double)size;
    }

    public Dataset getPercentageLossDataset()
    {
        return outageDataset;
    }

    private static final Logger log = Logger.getLogger(org/seamcat/cdma/presentation/CDMACapacityPanel);
    private static final int EVENT_LIMIT = 1000;
    private static final double DUMMYARRAY[] = new double[0];
    private JFreeChart capacityChart;
    private JFreeChart outageChart;
    private CDMACapacityDataset capacityDataset;
    private OutageDataset outageDataset;
    private JTextField tfAvgLoss;
    private JTextField tfAvgSystemLoss;
    private NumberFormat nf;


}