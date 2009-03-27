// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NodeAttributeTable.java

package org.seamcat.presentation;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;
import org.seamcat.model.NodeAttribute;

// Referenced classes of package org.seamcat.presentation:
//            Node

public class NodeAttributeTable extends JTable
{

    public NodeAttributeTable()
    {
    }

    public TableCellRenderer getCellRenderer(int row, int column)
    {
        TableCellRenderer tcr = super.getCellRenderer(row, column);
        try
        {
            if(column == 1)
            {
                ListSelectionModel lsm = getSelectionModel();
                NodeAttribute nodeAttr = ((Node)getModel()).getNodeAttributes()[row];
                if(nodeAttr.getType().equalsIgnoreCase("Integer"))
                    tcr = getDefaultRenderer(java/lang/Integer);
                else
                if(nodeAttr.getType().equalsIgnoreCase("Float"))
                    tcr = getDefaultRenderer(java/lang/Double);
                else
                    tcr = getDefaultRenderer(java/lang/String);
            }
        }
        catch(Exception e) { }
        return tcr;
    }

    public TableCellEditor getCellEditor(int row, int column)
    {
        ListSelectionModel lsm = getSelectionModel();
        TableCellEditor editor = null;
        if(column == 1)
        {
            NodeAttribute nodeAttr = ((Node)getModel()).getNodeAttributes()[row];
            TableColumn valueColumn = getColumnModel().getColumn(1);
            if(nodeAttr.getAllowableValues() != null)
            {
                JComboBox comboBox = new JComboBox();
                Object allowableValues[] = nodeAttr.getAllowableValues();
                for(int i = 0; i < allowableValues.length; i++)
                    comboBox.addItem(allowableValues[i]);

                comboBox.setEditable(!nodeAttr.getAllowableValuesOnly());
                editor = new DefaultCellEditor(comboBox);
            } else
            if(nodeAttr.getType().equalsIgnoreCase("Integer"))
                editor = getDefaultEditor(java/lang/Integer);
            else
            if(nodeAttr.getType().equalsIgnoreCase("Float"))
                editor = getDefaultEditor(java/lang/Double);
            else
            if(nodeAttr.getType().equalsIgnoreCase("Double"))
                editor = getDefaultEditor(java/lang/Double);
            else
                editor = getDefaultEditor(java/lang/String);
        } else
        {
            editor = super.getCellEditor(row, column);
        }
        return editor;
    }

    public Component prepareEditor(TableCellEditor editor, int row, int column)
    {
        return super.prepareEditor(editor, row, column);
    }
}
