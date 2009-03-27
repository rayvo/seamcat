// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserDefinedGraph.java

package org.seamcat.presentation;

import javax.swing.JPanel;
import javax.swing.table.TableModel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DatasetGroup;

public abstract class UserDefinedGraph extends JPanel
{

    public UserDefinedGraph()
    {
    }

    public abstract void refreshGraph();

    public abstract JFreeChart createChart();

    public abstract void refreshGraph(TableModel tablemodel);

    public abstract DatasetGroup getDataset();

    public abstract void setDataset(DatasetGroup datasetgroup);
}
