// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DiscreteFunction2TableModelAdapter.java

package org.seamcat.presentation.components;

import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jfree.data.DomainOrder;
import org.jfree.data.general.*;
import org.jfree.data.xy.XYDataset;
import org.seamcat.function.*;
import org.seamcat.interfaces.Functionable;

public class DiscreteFunction2TableModelAdapter
    implements TableModel, XYDataset
{

    public DiscreteFunction2TableModelAdapter()
    {
        this(new DiscreteFunction2());
    }

    public DiscreteFunction2TableModelAdapter(DiscreteFunction2 function)
    {
        tableModelListeners = new ArrayList();
        datasetChangeListeners = new ArrayList();
        stdTableModelEvent = new TableModelEvent(this);
        stdDatasetChangeEvent = new DatasetChangeEvent(this, this);
        datasetGroup = new DatasetGroup();
        thirdColumnIsEditable = true;
        this.function = function;
        ensureMinimumRows();
    }

    public DiscreteFunction2 getFunction()
    {
        sortPoints();
        return function;
    }

    public int getColumnCount()
    {
        return 3;
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
            value = Double.valueOf(((Point3D)function.getPoint3DList().get(rowIndex)).getX());
            break;

        case 1: // '\001'
            value = Double.valueOf(((Point3D)function.getPoint3DList().get(rowIndex)).getY());
            break;

        case 2: // '\002'
            value = Double.valueOf(((Point3D)function.getPoint3DList().get(rowIndex)).getRZ());
            break;

        default:
            throw new IllegalArgumentException("Point3D only has three columns");
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
                ((Point3D)function.getPoint3DList().get(rowIndex)).setX(number.doubleValue());
                break;

            case 1: // '\001'
                ((Point3D)function.getPoint3DList().get(rowIndex)).setY(number.doubleValue());
                break;

            case 2: // '\002'
                ((Point3D)function.getPoint3DList().get(rowIndex)).setRZ(number.doubleValue());
                break;

            default:
                throw new IllegalArgumentException("Point3D only has three columns");
            }
            sortPoints();
            fireChangeListeners();
        } else
        if(aValue != null)
            throw new IllegalArgumentException((new StringBuilder()).append("TableModel only accepts instances of class Number [Passed argument was of type: ").append(aValue.getClass()).append("]").toString());
    }

    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
        case 0: // '\0'
            return COL1_NAME;

        case 1: // '\001'
            return COL2_NAME;

        case 2: // '\002'
            return COL3_NAME;
        }
        throw new IllegalArgumentException((new StringBuilder()).append("Point3D only has three columns <").append(columnIndex).append(">").toString());
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
        if(series == 0 || series == 1)
            return ((Point3D)function.getPoint3DList().get(item)).getX();
        else
            throw new IllegalArgumentException("Illegal series");
    }

    public double getYValue(int series, int item)
    {
        switch(series)
        {
        case 0: // '\0'
            return ((Point3D)function.getPoint3DList().get(item)).getY();

        case 1: // '\001'
            return getValueInReferenceBandwith((Point3D)function.getPoint3DList().get(item));
        }
        throw new IllegalArgumentException("Illegal series");
    }

    public double getValueInReferenceBandwith(Point3D p)
    {
        return p.getY() + 10D * Math.log10(1000D / p.getRZ());
    }

    public int getSeriesCount()
    {
        return 2;
    }

    public String getSeriesName(int series)
    {
        switch(series)
        {
        case 0: // '\0'
            return SERIES1_NAME;

        case 1: // '\001'
            return SERIES2_NAME;
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

    public DiscreteFunction2 getDiscreteFunction2()
    {
        return function;
    }

    public void setFunctionable(Functionable func)
    {
        if(func instanceof DiscreteFunction2)
        {
            setDiscreteFunction2((DiscreteFunction2)func);
        } else
        {
            DiscreteFunction2 dis = new DiscreteFunction2();
            Point2D p;
            for(Iterator i$ = func.getPointsList().iterator(); i$.hasNext(); dis.addPoint(new Point3D(p, 0.0D)))
                p = (Point2D)i$.next();

            setDiscreteFunction2(dis);
        }
    }

    public void setDiscreteFunction2(DiscreteFunction2 function)
    {
        this.function = new DiscreteFunction2(function);
        ensureMinimumRows();
        sortPoints();
        fireChangeListeners();
    }

    public void clear()
    {
        log.debug("Clearing model");
        function.getPointsList().clear();
        fireChangeListeners();
    }

    public void addRow()
    {
        addRow(true);
    }

    public double getMaxValue()
    {
        double val = 0.0D;
        if(function.getPointsList().size() > 0)
        {
            double max = -1.7976931348623157E+308D;
            Iterator i$ = function.getPoint3DList().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                Point3D p = (Point3D)i$.next();
                if(p.getY() > max)
                    max = p.getY();
                val = getValueInReferenceBandwith(p);
                if(val > max)
                    max = val;
            } while(true);
            val = max;
        }
        return val;
    }

    public double getMinValue()
    {
        double val = 0.0D;
        if(function.getPointsList().size() > 0)
        {
            for(Iterator i$ = function.getPoint3DList().iterator(); i$.hasNext();)
            {
                Point3D p = (Point3D)i$.next();
                double min = 1.7976931348623157E+308D;
                if(p.getY() < min)
                    min = p.getY();
                val = getValueInReferenceBandwith(p);
                if(val < min)
                    min = val;
                val = min;
            }

        }
        return val;
    }

    private void addRow(boolean fireChangeListeners)
    {
        if(function.getPointsList().size() < 1)
            function.addPoint(new Point3D(0.0D, 0.0D, 1000D));
        else
            function.addPoint(new Point3D(0.0D, 0.0D, ((Point3D)function.getPoint3DList().get(0)).getRZ()));
        sortPoints();
        if(fireChangeListeners)
            fireChangeListeners();
    }

    public void deleteRow(int row)
    {
        if(row >= 0 && row < getRowCount())
        {
            log.debug((new StringBuilder()).append("Deleting row no. ").append(row).toString());
            function.getPoint3DList().remove(row);
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
        log.debug("Sorting Point3D-collection");
        Collections.sort(function.getPoint3DList());
    }

    public boolean isThirdColumnIsEditable()
    {
        return thirdColumnIsEditable;
    }

    public void setThirdColumnIsEditable(boolean thirdColumnIsEditable)
    {
        this.thirdColumnIsEditable = thirdColumnIsEditable;
    }

    public DomainOrder getDomainOrder()
    {
        return DomainOrder.NONE;
    }

    public Number getX(int arg0, int arg1)
    {
        return Double.valueOf(getXValue(arg0, arg1));
    }

    public Number getY(int arg0, int arg1)
    {
        return Double.valueOf(getYValue(arg0, arg1));
    }

    public Comparable getSeriesKey(int arg0)
    {
        return getSeriesName(arg0);
    }

    public int indexOf(Comparable ser)
    {
        return !ser.equals(SERIES1_NAME) ? 1 : 0;
    }

    private static final Logger log = Logger.getLogger(org/seamcat/presentation/components/DiscreteFunction2TableModelAdapter);
    private static final ResourceBundle STRINGLIST;
    private final List tableModelListeners;
    private final List datasetChangeListeners;
    private final TableModelEvent stdTableModelEvent;
    private final DatasetChangeEvent stdDatasetChangeEvent;
    private DatasetGroup datasetGroup;
    private DiscreteFunction2 function;
    private static final String COL1_NAME;
    private static final String COL2_NAME;
    private static final String COL3_NAME;
    private static final String SERIES1_NAME;
    private static final String SERIES2_NAME;
    private static final int SERIES1 = 0;
    private static final int SERIES2 = 1;
    private boolean thirdColumnIsEditable;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        COL1_NAME = STRINGLIST.getString("FIRST_COLUMN_TITLE");
        COL2_NAME = STRINGLIST.getString("SECOND_COLUMN_TITLE");
        COL3_NAME = STRINGLIST.getString("THIRD_COLUMN_TITLE");
        SERIES1_NAME = STRINGLIST.getString("FIRST_SERIES_TITLE");
        SERIES2_NAME = STRINGLIST.getString("SECOND_SERIES_TITLE");
    }
}
