// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParameterTableModel.java

package org.seamcat.presentation.postprocessing;

import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.model.plugin.PostProcessingPlugin;

public class ParameterTableModel
    implements TableModel
{

    public ParameterTableModel()
    {
        listeners = new ArrayList();
    }

    public int getRowCount()
    {
        if(plugin == null)
            return 0;
        else
            return plugin.getNumberOfParameters();
    }

    public int getColumnCount()
    {
        return 3;
    }

    public String getColumnName(int columnIndex)
    {
        String colName = "Unknown Column Index";
        switch(columnIndex)
        {
        case 0: // '\0'
            colName = "Name";
            break;

        case 1: // '\001'
            colName = "Type";
            break;

        case 2: // '\002'
            colName = "Value";
            break;
        }
        return colName;
    }

    public Class getColumnClass(int columnIndex)
    {
        Class colClass = java/lang/Object;
        switch(columnIndex)
        {
        case 0: // '\0'
            colClass = java/lang/String;
            break;

        case 1: // '\001'
            colClass = org/seamcat/model/plugin/PostProcessingPlugin$ParameterType;
            break;

        case 2: // '\002'
            colClass = java/lang/Object;
            break;
        }
        return colClass;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        static class _cls1
        {

            static final int $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[];

            static 
            {
                $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType = new int[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.values().length];
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Boolean.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Integer.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Double.ordinal()] = 3;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.String.ordinal()] = 4;
                }
                catch(NoSuchFieldError ex) { }
            }
        }

        if(columnIndex == 2)
            switch(_cls1..SwitchMap.org.seamcat.model.plugin.PostProcessingPlugin.ParameterType[plugin.getParameterType(rowIndex + 1).ordinal()])
            {
            case 1: // '\001'
                return true;

            case 2: // '\002'
                return true;

            case 3: // '\003'
                return true;

            case 4: // '\004'
                return true;
            }
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        switch(columnIndex)
        {
        case 0: // '\0'
            return plugin.getParameterName(rowIndex + 1);

        case 1: // '\001'
            return plugin.getParameterType(rowIndex + 1);

        case 2: // '\002'
            return plugin.getParameterValue(rowIndex + 1);
        }
        return null;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if(columnIndex == 2)
            switch(_cls1..SwitchMap.org.seamcat.model.plugin.PostProcessingPlugin.ParameterType[plugin.getParameterType(rowIndex + 1).ordinal()])
            {
            case 1: // '\001'
                plugin.setParameterValue(rowIndex + 1, Boolean.valueOf(aValue.toString().equalsIgnoreCase("true")));
                break;

            case 2: // '\002'
                plugin.setParameterValue(rowIndex + 1, Integer.valueOf(Integer.parseInt(aValue.toString())));
                break;

            case 3: // '\003'
                plugin.setParameterValue(rowIndex + 1, Double.valueOf(Double.parseDouble(aValue.toString())));
                break;

            case 4: // '\004'
                plugin.setParameterValue(rowIndex + 1, aValue.toString());
                break;
            }
        TableModelListener tml;
        for(Iterator i$ = listeners.iterator(); i$.hasNext(); tml.tableChanged(new TableModelEvent(this)))
            tml = (TableModelListener)i$.next();

    }

    public void addTableModelListener(TableModelListener l)
    {
        listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        listeners.remove(l);
    }

    public PostProcessingPlugin getPlugin()
    {
        return plugin;
    }

    public void setPlugin(PostProcessingPlugin plugin)
    {
        this.plugin = plugin;
        TableModelListener tml;
        for(Iterator i$ = listeners.iterator(); i$.hasNext(); tml.tableChanged(new TableModelEvent(this)))
            tml = (TableModelListener)i$.next();

    }

    private PostProcessingPlugin plugin;
    private List listeners;
}
