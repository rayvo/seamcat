// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CapacityFindingStatusPanel.java

package org.seamcat.cdma.presentation;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.*;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;
import org.seamcat.cdma.NonInterferedCapacityListener;

public class CapacityFindingStatusPanel extends JPanel
    implements NonInterferedCapacityListener
{

    public CapacityFindingStatusPanel()
    {
        testCounter = 0;
        FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:NONE", "CENTER:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0),FILL:DEFAULT:GROW(1.0),CENTER:DEFAULT:NONE");
        CellConstraints cc = new CellConstraints();
        setLayout(formlayout1);
        numberOfTrialsData = new DefaultValueDataset(0.0D);
        successRateData = new DefaultValueDataset(0.0D);
        usersPerCellData = new DefaultValueDataset(0.0D);
        overallStatusData = new DefaultCategoryDataset();
        numberOfTrialsPlot = new MeterPlot(numberOfTrialsData);
        numberOfTrialsPlot.setUnits("Trials completed");
        numberOfTrialsPlot.setBackgroundPaint(Color.WHITE);
        numberOfTrialsPlot.setNeedlePaint(Color.BLACK);
        numberOfTrialsPlot.setOutlinePaint(Color.BLACK);
        numberOfTrialsPlot.setValuePaint(Color.BLUE);
        numberOfTrialsPlot.setTickPaint(Color.BLUE);
        numberOfTrialsPlot.setRange(new Range(0.0D, 20D));
        numberOfTrialsPlot.setDialShape(DialShape.CIRCLE);
        numberOfTrialsPlot.setDialBackgroundPaint(getBackground());
        numberOfTrialsPlot.setTickSize(2D);
        numberOfTrialsPanel = new ChartPanel(new JFreeChart(numberOfTrialsPlot));
        usersPerCellPlot = new MeterPlot(usersPerCellData);
        usersPerCellPlot.setUnits("Users per Cell");
        usersPerCellPlot.setBackgroundPaint(Color.WHITE);
        usersPerCellPlot.setNeedlePaint(Color.BLACK);
        usersPerCellPlot.setOutlinePaint(Color.BLACK);
        usersPerCellPlot.setValuePaint(Color.BLUE);
        usersPerCellPlot.setTickPaint(Color.BLUE);
        usersPerCellPlot.setRange(new Range(0.0D, 120D));
        usersPerCellPlot.setDialShape(DialShape.CIRCLE);
        usersPerCellPlot.setDialBackgroundPaint(getBackground());
        usersPerCellPlot.setTickSize(10D);
        usersPerCellPanel = new ChartPanel(new JFreeChart(usersPerCellPlot));
        successRatePlot = new MeterPlot(successRateData);
        successRatePlot.setRange(new Range(0.0D, 20D));
        successRatePlot.setBackgroundPaint(Color.WHITE);
        successRatePlot.setDialBackgroundPaint(getBackground());
        successRatePlot.setUnits("Successful trials");
        successRatePlot.setDialShape(DialShape.CIRCLE);
        successRatePlot.setNeedlePaint(Color.BLACK);
        successRatePlot.setValuePaint(Color.BLUE);
        successRatePlot.setTickSize(2D);
        successRatePlot.setTickPaint(Color.BLUE);
        successRatePanel = new ChartPanel(new JFreeChart(successRatePlot));
        successRatePanel.getChart().removeLegend();
        overallStatusChart = ChartFactory.createStackedBarChart("Non interfered capacity finding status", "Users per cell", "Successful Trials", overallStatusData, PlotOrientation.VERTICAL, false, true, false);
        overallStatusPanel = new ChartPanel(overallStatusChart);
        add(numberOfTrialsPanel, cc.xy(2, 2));
        add(usersPerCellPanel, cc.xy(3, 2));
        add(successRatePanel, cc.xy(4, 2));
        add(overallStatusPanel, cc.xywh(2, 3, 3, 1));
        addFillComponents(this, new int[] {
            1, 2, 3, 4
        }, new int[] {
            1, 2, 3, 4
        });
        setBorder(new TitledBorder("CDMA Non-Interfered Capacity status"));
    }

    private void addFillComponents(Container panel, int cols[], int rows[])
    {
        Dimension filler = new Dimension(10, 10);
        boolean filled_cell_11 = false;
        CellConstraints cc = new CellConstraints();
        if(cols.length > 0 && rows.length > 0 && cols[0] == 1 && rows[0] == 1)
        {
            panel.add(Box.createRigidArea(filler), cc.xy(1, 1));
            filled_cell_11 = true;
        }
        for(int index = 0; index < cols.length; index++)
            if(cols[index] != 1 || !filled_cell_11)
                panel.add(Box.createRigidArea(filler), cc.xy(cols[index], 1));

        for(int index = 0; index < rows.length; index++)
            if(rows[index] != 1 || !filled_cell_11)
                panel.add(Box.createRigidArea(filler), cc.xy(1, rows[index]));

    }

    public void startingTest(int usersPrCell, int deltaUsers, int trials, boolean uplink)
    {
        numberOfTrialsData.setValue(Integer.valueOf(0));
        numberOfTrialsPlot.setRange(new Range(0.0D, trials));
        numberOfTrialsPlot.setTickSize(trials / 10);
        numberOfTrialsPlot.setTickLabelsVisible(true);
        usersPerCellData.setValue(Integer.valueOf(usersPrCell));
        successRateData.setValue(Integer.valueOf(0));
    }

    public void endingTest(int usersPerCell, double successRate, boolean uplink, int trialsPerformed)
    {
        overallStatusData.setValue(successRate, "Row", (new StringBuilder()).append(++testCounter).append("#Users: ").append(usersPerCell).toString());
    }

    public void capacityFound(int i, double d, boolean flag)
    {
    }

    public void startingTrial(int i)
    {
    }

    public void endingTrial(double outage, boolean success, int trialid)
    {
        if(uplink)
            successRateData.setValue(Double.valueOf(outage));
        else
        if(success)
            successRateData.setValue(Integer.valueOf(successRateData.getValue().intValue() + 1));
        numberOfTrialsData.setValue(Integer.valueOf(trialid + 1));
    }

    public void startingCapacityFinding(int usersPrCell, int delta, double allowableOutage, int trials, boolean _uplink, double target)
    {
        overallStatusChart.getCategoryPlot().clearRangeMarkers();
        successRatePlot.clearIntervals();
        uplink = _uplink;
        if(uplink)
        {
            overallStatusChart.getCategoryPlot().getRangeAxis().setLabel("Average Noiserise (dB)");
            overallStatusChart.getCategoryPlot().getRangeAxis().setRange(0.0D, 10D);
            successRatePlot.setRange(new Range(0.0D, 20D));
            successRatePlot.setUnits("dB of Average Noise-rise");
            successRatePlot.setTickSize(2D);
            Marker marker;
            if(allowableOutage != 0.0D)
            {
                double min = target - allowableOutage;
                double max = target;
                marker = new IntervalMarker(min, max);
                successRatePlot.addInterval(new MeterInterval("Try with more users per cell", new Range(0.0D, min), Color.BLACK, new BasicStroke(1.0F), new Color(255, 140, 140)));
                successRatePlot.addInterval(new MeterInterval("Non-interfered capacity found", new Range(min, max), Color.BLACK, new BasicStroke(1.0F), new Color(140, 255, 140)));
                successRatePlot.addInterval(new MeterInterval("Try with fewer users per cell", new Range(max, 20D), Color.BLACK, new BasicStroke(1.0F), new Color(255, 255, 140)));
            } else
            {
                marker = new ValueMarker(target);
                successRatePlot.addInterval(new MeterInterval("Try with more users per cell", new Range(0.0D, target), Color.BLACK, new BasicStroke(1.0F), new Color(255, 140, 140)));
                successRatePlot.addInterval(new MeterInterval("Try with fewer users per cell", new Range(target, 20D), Color.BLACK, new BasicStroke(1.0F), new Color(255, 255, 140)));
            }
            marker.setPaint(new Color(180, 255, 255));
            overallStatusChart.getCategoryPlot().addRangeMarker(marker);
        } else
        {
            overallStatusChart.getCategoryPlot().getRangeAxis().setRange(0.0D, trials);
            overallStatusChart.getCategoryPlot().getRangeAxis().setLabel("Successful Trials");
            overallStatusChart.getCategoryPlot().addRangeMarker(new ValueMarker((double)trials * target));
            successRatePlot.setRange(new Range(0.0D, trials));
            successRatePlot.setUnits("Successful trials");
            successRatePlot.setTickSize(trials / 10);
            successRatePlot.addInterval(new MeterInterval("Try with fewer users per cell", new Range(0.0D, (int)((double)trials * 0.80000000000000004D - 1.0D)), Color.BLACK, new BasicStroke(1.0F), new Color(255, 140, 140)));
            successRatePlot.addInterval(new MeterInterval("Non-interfered capacity found", new Range((int)((double)trials * 0.80000000000000004D - 1.0D), (int)((double)trials * 0.80000000000000004D + 1.0D)), Color.BLACK, new BasicStroke(1.0F), new Color(140, 255, 140)));
            successRatePlot.addInterval(new MeterInterval("Try with more users per cell", new Range((int)((double)trials * 0.80000000000000004D + 1.0D), trials), Color.BLACK, new BasicStroke(1.0F), new Color(255, 255, 140)));
        }
        overallStatusData.clear();
        testCounter = 0;
    }

    private MeterPlot numberOfTrialsPlot;
    private ChartPanel numberOfTrialsPanel;
    private MeterPlot usersPerCellPlot;
    private ChartPanel usersPerCellPanel;
    private MeterPlot successRatePlot;
    private ChartPanel successRatePanel;
    private JFreeChart overallStatusChart;
    private ChartPanel overallStatusPanel;
    private DefaultValueDataset numberOfTrialsData;
    private DefaultValueDataset usersPerCellData;
    private DefaultValueDataset successRateData;
    private DefaultCategoryDataset overallStatusData;
    private int testCounter;
    private boolean uplink;
}