// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StairDistributionTableModelAdapter.java

package org.seamcat.presentation.components;

import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.*;
import org.seamcat.distribution.StairDistribution;
import org.seamcat.function.Point2D;

public class StairDistributionTableModelAdapter
    implements TableModel
{
    private class CategoryDatasetImpl
        implements CategoryDataset
    {

        public void addChangeListener(DatasetChangeListener datasetChangeListener)
        {
            if(!datasetChangeListeners.contains(datasetChangeListener))
                datasetChangeListeners.add(datasetChangeListener);
        }

        public void removeChangeListener(DatasetChangeListener datasetChangeListener)
        {
            datasetChangeListeners.remove(datasetChangeListener);
        }

        public void fireChangeListeners()
        {
            updateColumnKeys();
            for(Iterator i = datasetChangeListeners.iterator(); i.hasNext(); ((DatasetChangeListener)i.next()).datasetChanged(stdDatasetChangeEvent));
        }

        private void updateColumnKeys()
        {
            columnKeys.clear();
            int x = 0;
            for(int size = points.size(); x < size; x++)
                columnKeys.add(getColumnKey(x));

        }

        public DatasetGroup getGroup()
        {
            return datasetGroup;
        }

        public void setGroup(DatasetGroup datasetGroup)
        {
            this.datasetGroup = datasetGroup;
        }

        public Number getValue(int row, int column)
        {
            return new Double(((Point2D)points.get(column)).getY());
        }

        public Comparable getRowKey(int row)
        {
            return (Comparable)rowKeys.get(row);
        }

        public int getRowIndex(Comparable key)
        {
            return rowKeys.indexOf(key);
        }

        public List getRowKeys()
        {
            return rowKeys;
        }

        public Comparable getColumnKey(int column)
        {
            return new Double(((Point2D)points.get(column)).getX());
        }

        public int getColumnIndex(Comparable key)
        {
            return columnKeys.indexOf(key);
        }

        public List getColumnKeys()
        {
            return columnKeys;
        }

        public Number getValue(Comparable rowKey, Comparable colKey)
        {
            return getValue(getRowIndex(rowKey), getColumnIndex(colKey));
        }

        public int getRowCount()
        {
            return 1;
        }

        public int getColumnCount()
        {
            return points.size();
        }

        private List datasetChangeListeners;
        private DatasetChangeEvent stdDatasetChangeEvent;
        private DatasetGroup datasetGroup;
        private List rowKeys;
        private List columnKeys;
        final StairDistributionTableModelAdapter this$0;

        public CategoryDatasetImpl()
        {
            this$0 = StairDistributionTableModelAdapter.this;
            super();
            datasetChangeListeners = new ArrayList();
            stdDatasetChangeEvent = new DatasetChangeEvent(this, this);
            datasetGroup = new DatasetGroup();
            rowKeys = new ArrayList(1);
            columnKeys = new ArrayList();
            rowKeys.add(new Integer(0));
            updateColumnKeys();
        }
    }


    public CategoryDataset getCategoryDS()
    {
        return categoryDS;
    }

    public StairDistributionTableModelAdapter()
    {
        this(new Point2D[0]);
    }

    public StairDistributionTableModelAdapter(Point2D _points[])
    {
        tableModelListeners = new ArrayList();
        stdTableModelEvent = new TableModelEvent(this);
        points = new ArrayList();
        categoryDS = new CategoryDatasetImpl();
        for(int x = 0; x < _points.length; x++)
            points.add(_points[x]);

        ensureMinimumRows();
        col1Name = "Value";
        col2Name = "Cum. Prop.";
    }

    public int getColumnCount()
    {
        return 2;
    }

    public int getRowCount()
    {
        return points.size();
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
        switch(columnIndex)
        {
        case 0: // '\0'
            return new Double(((Point2D)points.get(rowIndex)).getX());

        case 1: // '\001'
            return new Double(((Point2D)points.get(rowIndex)).getY());
        }
        throw new IllegalArgumentException("Point2D only has two columns");
    }

    public int getIndex(Double key)
    {
        int index = -1;
        int x = 0;
        int size = points.size();
        do
        {
            if(x >= size)
                break;
            if((new Double(((Point2D)points.get(x)).getX())).compareTo(key) == 0)
            {
                index = x;
                break;
            }
            x++;
        } while(true);
        return index;
    }

    public Comparable getKey(int index)
    {
        return new Double(((Point2D)points.get(index)).getX());
    }

    public List getKeys()
    {
        List l = new ArrayList();
        for(Iterator i = points.iterator(); i.hasNext(); l.add(new Double(((Point2D)i.next()).getX())));
        return l;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if(aValue instanceof Number)
        {
            Number number = (Number)aValue;
            switch(columnIndex)
            {
            case 0: // '\0'
                ((Point2D)points.get(rowIndex)).setX(number.doubleValue());
                break;

            case 1: // '\001'
                ((Point2D)points.get(rowIndex)).setY(number.doubleValue());
                sortPoints();
                break;

            default:
                throw new IllegalArgumentException("Point2D only has two columns");
            }
            fireChangeListeners();
        } else
        {
            throw new IllegalArgumentException("TableModel only accepts instances of class Number");
        }
    }

    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
        case 0: // '\0'
            return col1Name;

        case 1: // '\001'
            return col2Name;
        }
        throw new IllegalArgumentException((new StringBuilder()).append("Point2D only has two columns <").append(columnIndex).append(">").toString());
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
        return points.size();
    }

    public int getItemCount()
    {
        return points.size();
    }

    public Number getValue(int index)
    {
        return new Double(((Point2D)points.get(index)).getY());
    }

    public void fireChangeListeners()
    {
        for(Iterator i = tableModelListeners.iterator(); i.hasNext(); ((TableModelListener)i.next()).tableChanged(stdTableModelEvent));
        categoryDS.fireChangeListeners();
    }

    public void setPoints(StairDistribution stairDist)
    {
        points.clear();
        Point2D _points[] = stairDist.getPoints();
        for(int x = 0; x < _points.length; x++)
            points.add(_points[x]);

        ensureMinimumRows();
        sortPoints();
        fireChangeListeners();
    }

    public void setPoints(List _points)
    {
        points = _points;
        ensureMinimumRows();
        sortPoints();
        fireChangeListeners();
    }

    public Point2D[] getPoints()
    {
        return (Point2D[])(Point2D[])points.toArray(new Point2D[points.size()]);
    }

    public List getPointsList()
    {
        return points;
    }

    public void clear()
    {
        LOG.debug("Clearing model");
        int rowCount = getRowCount();
        points.clear();
        fireChangeListeners();
    }

    public void addRow()
    {
        addRow(true);
    }

    private void addRow(boolean fireChangeListeners)
    {
        points.add(new Point2D(0.0D, 0.0D));
        if(fireChangeListeners)
            fireChangeListeners();
    }

    public void deleteRow(int row)
    {
        if(row >= 0 && row < getRowCount())
        {
            LOG.debug((new StringBuilder()).append("Deleting row no. ").append(row).toString());
            points.remove(row);
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
        Collections.sort(points, Point2D.POINTX_COMPARATOR);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/components/StairDistributionTableModelAdapter);
    private List tableModelListeners;
    private TableModelEvent stdTableModelEvent;
    private List points;
    private CategoryDatasetImpl categoryDS;
    private String col1Name;
    private String col2Name;


}
