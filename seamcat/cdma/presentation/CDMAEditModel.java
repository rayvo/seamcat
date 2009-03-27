// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAEditModel.java

package org.seamcat.cdma.presentation;

import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class CDMAEditModel
    implements TableModel
{

    public CDMAEditModel()
    {
    }

    public int getColumnCount()
    {
        return COLUMN_NAMES.length;
    }

    public int getRowCount()
    {
        return data.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    public Class getColumnClass(int columnIndex)
    {
        return java/lang/Double;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return ((Vector)data.get(rowIndex)).get(columnIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Vector row = (Vector)data.get(rowIndex);
        row.set(columnIndex, aValue);
        fireChangeListeners(rowIndex, 0);
    }

    public String getColumnName(int columnIndex)
    {
        return COLUMN_NAMES[columnIndex];
    }

    public void addTableModelListener(TableModelListener l)
    {
        if(!tableModelListeners.contains(l))
            tableModelListeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        tableModelListeners.remove(l);
    }

    public void setData(Vector vector)
    {
        data = vector;
    }

    public XYSeriesCollection getXYSeriesCollection()
    {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int x = 1;
        for(int stop = getColumnCount(); x < stop; x++)
        {
            XYSeries series = new XYSeries(COLUMN_NAMES[x]);
            int y = 0;
            for(int _stop = getRowCount(); y < _stop; y++)
                if(getValueAt(y, x) != null && getValueAt(y, 0) != null)
                {
                    double a = ((Double)getValueAt(y, 0)).doubleValue();
                    double b = ((Double)getValueAt(y, x)).doubleValue();
                    series.add(a, b);
                }

            dataset.addSeries(series);
        }

        return dataset;
    }

    public void addRow()
    {
        Vector row = new Vector();
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        row.add(null);
        data.add(row);
    }

    public void deleteRow(int index)
    {
        if(index > -1 && index < data.size())
        {
            data.remove(index);
            fireChangeListeners(index, -1);
        }
    }

    public void fireChangeListeners(int row, int type)
    {
        TableModelEvent tme = new TableModelEvent(this, row, type);
        for(Iterator i = tableModelListeners.iterator(); i.hasNext(); ((TableModelListener)i.next()).tableChanged(tme));
    }

    public static final String COLUMN_NAMES[] = {
        "Geometry*", "AWGN", "3 km/h", "30 km/h", "100 km/h"
    };
    private Vector data;
    private final List tableModelListeners = new ArrayList();

}