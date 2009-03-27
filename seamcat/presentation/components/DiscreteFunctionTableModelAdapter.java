// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DiscreteFunctionTableModelAdapter.java

package org.seamcat.presentation.components;

import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.*;
import org.jfree.data.xy.XYDataset;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Point2D;
import org.seamcat.interfaces.Functionable;

public class DiscreteFunctionTableModelAdapter
    implements TableModel, XYDataset
{

    public DiscreteFunctionTableModelAdapter()
    {
        this(new DiscreteFunction());
    }

    public DiscreteFunctionTableModelAdapter(DiscreteFunction function)
    {
        tableModelListeners = new ArrayList();
        datasetChangeListeners = new ArrayList();
        stdTableModelEvent = new TableModelEvent(this);
        stdDatasetChangeEvent = new DatasetChangeEvent(this, this);
        datasetGroup = new DatasetGroup();
        col1Name = "X";
        col2Name = "Y";
        this.function = function;
        ensureMinimumRows();
    }

    public DiscreteFunction getFunction()
    {
        return function;
    }

    public int getColumnCount()
    {
        return 2;
    }

    public int getRowCount()
    {
        return function.getPointsList().size();
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
        Double value;
        switch(columnIndex)
        {
        case 0: // '\0'
            value = new Double(((Point2D)function.getPointsList().get(rowIndex)).getX());
            break;

        case 1: // '\001'
            value = new Double(((Point2D)function.getPointsList().get(rowIndex)).getY());
            break;

        default:
            throw new IllegalArgumentException("Point2D only has two columns");
        }
        return value;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if(aValue instanceof Number)
        {
            Number number = (Number)aValue;
            switch(columnIndex)
            {
            case 0: // '\0'
                ((Point2D)function.getPointsList().get(rowIndex)).setX(number.doubleValue());
                break;

            case 1: // '\001'
                ((Point2D)function.getPointsList().get(rowIndex)).setY(number.doubleValue());
                sortPoints();
                break;

            default:
                throw new IllegalArgumentException("Point2D only has two columns");
            }
            fireChangeListeners();
        } else
        if(aValue != null)
            throw new IllegalArgumentException((new StringBuilder()).append("TableModel only accepts instances of class Number (Input was: ").append(aValue.getClass().getName()).append(")").toString());
    }

    public String getColumnName(int columnIndex)
    {
        String name;
        switch(columnIndex)
        {
        case 0: // '\0'
            name = col1Name;
            break;

        case 1: // '\001'
            name = col2Name;
            break;

        default:
            throw new IllegalArgumentException((new StringBuilder()).append("Point2D only has two columns <").append(columnIndex).append(">").toString());
        }
        return name;
    }

    public void setColumnName(int columnIndex, String name)
    {
        switch(columnIndex)
        {
        case 0: // '\0'
            col1Name = name;
            break;

        case 1: // '\001'
            col2Name = name;
            break;

        default:
            throw new IllegalArgumentException((new StringBuilder()).append("Point2D only has two columns <").append(columnIndex).append(">").toString());
        }
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

    public int getItemCount(int series)
    {
        return function.getPointsList().size();
    }

    public double getXValue(int series, int item)
    {
        if(series == 0)
            return ((Point2D)function.getPointsList().get(item)).getX();
        else
            throw new IllegalArgumentException("Illegal series");
    }

    public double getYValue(int series, int item)
    {
        switch(series)
        {
        case 0: // '\0'
            return ((Point2D)function.getPointsList().get(item)).getY();
        }
        throw new IllegalArgumentException("Illegal series");
    }

    public int getSeriesCount()
    {
        return 1;
    }

    public String getSeriesName(int series)
    {
        switch(series)
        {
        case 0: // '\0'
            return "";
        }
        throw new IllegalArgumentException("Illegal series index");
    }

    public void addChangeListener(DatasetChangeListener datasetChangeListener)
    {
        if(!datasetChangeListeners.contains(datasetChangeListener))
            datasetChangeListeners.add(datasetChangeListener);
    }

    public void removeChangeListener(DatasetChangeListener datasetChangeListener)
    {
        datasetChangeListeners.remove(datasetChangeListener);
    }

    public DatasetGroup getGroup()
    {
        return datasetGroup;
    }

    public void setGroup(DatasetGroup datasetGroup)
    {
        this.datasetGroup = datasetGroup;
    }

    public void fireChangeListeners()
    {
        TableModelListener l;
        for(Iterator i$ = tableModelListeners.iterator(); i$.hasNext(); l.tableChanged(stdTableModelEvent))
            l = (TableModelListener)i$.next();

        DatasetChangeListener l;
        for(Iterator i$ = datasetChangeListeners.iterator(); i$.hasNext(); l.datasetChanged(stdDatasetChangeEvent))
            l = (DatasetChangeListener)i$.next();

    }

    public DiscreteFunction getDiscreteFunction()
    {
        return function;
    }

    public void setFunctionable(Functionable func)
    {
        if(func instanceof DiscreteFunction)
        {
            setDiscreteFunction((DiscreteFunction)func);
        } else
        {
            DiscreteFunction dis = new DiscreteFunction();
            Point2D point;
            for(Iterator i$ = func.getPointsList().iterator(); i$.hasNext(); dis.addPoint(new Point2D(point.getX(), point.getY())))
                point = (Point2D)i$.next();

            setDiscreteFunction(dis);
        }
    }

    public void setDiscreteFunction(DiscreteFunction _function)
    {
        function = new DiscreteFunction(_function);
        ensureMinimumRows();
        sortPoints();
        fireChangeListeners();
    }

    public void clear()
    {
        function.getPointsList().clear();
        fireChangeListeners();
    }

    public void addRow()
    {
        addRow(true);
    }

    private void addRow(boolean fireChangeListeners)
    {
        function.addPoint(new Point2D(0.0D, 0.0D));
        if(fireChangeListeners)
            fireChangeListeners();
    }

    public void deleteRow(int row)
    {
        if(row >= 0 && row < getRowCount())
        {
            function.getPointsList().remove(row);
            ensureMinimumRows();
            fireChangeListeners();
        }
    }

    private void ensureMinimumRows()
    {
        if(getRowCount() == 0)
            addRow(false);
    }

    public void sortPoints()
    {
        Collections.sort(function.getPointsList(), Point2D.POINTX_COMPARATOR);
    }

    public Number getX(int series, int item)
    {
        return Double.valueOf(getXValue(series, item));
    }

    public Number getY(int series, int item)
    {
        return Double.valueOf(getYValue(series, item));
    }

    public DomainOrder getDomainOrder()
    {
        return DomainOrder.ASCENDING;
    }

    public Comparable getSeriesKey(int arg0)
    {
        return getSeriesName(arg0);
    }

    public int indexOf(Comparable arg0)
    {
        return 0;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/components/DiscreteFunctionTableModelAdapter);
    private static final int SERIES1 = 0;
    private static final String SERIES1_NAME = "";
    private final List tableModelListeners;
    private final List datasetChangeListeners;
    private final TableModelEvent stdTableModelEvent;
    private final DatasetChangeEvent stdDatasetChangeEvent;
    private DatasetGroup datasetGroup;
    private DiscreteFunction function;
    private String col1Name;
    private String col2Name;

}
