// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatTable.java

package org.seamcat.presentation.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.seamcat.presentation.SeamcatTextFieldFormats;

// Referenced classes of package org.seamcat.presentation.components:
//            DiscreteFunctionTableModelAdapter, DiscreteFunction2TableModelAdapter, StairDistributionTableModelAdapter

public class SeamcatTable extends JTable
{
    private static interface SeamcatTableLastCellAction
    {

        public abstract void tabActionPerformedOnLastCell();
    }


    public SeamcatTable()
    {
        init();
    }

    public SeamcatTable(int numRows, int numColumns)
    {
        super(numRows, numColumns);
        init();
    }

    public SeamcatTable(TableModel dm)
    {
        super(dm);
        init();
        if(dm instanceof DiscreteFunctionTableModelAdapter)
            addSeamcatTableLastCellAction(new SeamcatTableLastCellAction() {

                public void tabActionPerformedOnLastCell()
                {
                    ((DiscreteFunctionTableModelAdapter)getModel()).addRow();
                }

                final SeamcatTable this$0;

            
            {
                this$0 = SeamcatTable.this;
                super();
            }
            }
);
        else
        if(dm instanceof DiscreteFunction2TableModelAdapter)
            addSeamcatTableLastCellAction(new SeamcatTableLastCellAction() {

                public void tabActionPerformedOnLastCell()
                {
                    ((DiscreteFunction2TableModelAdapter)getModel()).addRow();
                }

                final SeamcatTable this$0;

            
            {
                this$0 = SeamcatTable.this;
                super();
            }
            }
);
        else
        if(dm instanceof StairDistributionTableModelAdapter)
            addSeamcatTableLastCellAction(new SeamcatTableLastCellAction() {

                public void tabActionPerformedOnLastCell()
                {
                    ((StairDistributionTableModelAdapter)getModel()).addRow();
                }

                final SeamcatTable this$0;

            
            {
                this$0 = SeamcatTable.this;
                super();
            }
            }
);
    }

    public SeamcatTable(Object rowData[][], Object columnNames[])
    {
        super(rowData, columnNames);
        init();
    }

    public SeamcatTable(Vector rowData, Vector columnNames)
    {
        super(rowData, columnNames);
        init();
    }

    public SeamcatTable(TableModel dm, TableColumnModel cm)
    {
        super(dm, cm);
        init();
    }

    public SeamcatTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
    {
        super(dm, cm, sm);
        init();
    }

    public void addSeamcatTableLastCellAction(SeamcatTableLastCellAction stca)
    {
        lastCellAction = stca;
    }

    private void init()
    {
        String actionName = "selectNextColumnCell";
        final Action tabAction = getActionMap().get(actionName);
        Action myAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e)
            {
                if(isLastCell())
                    try
                    {
                        lastCellAction.tabActionPerformedOnLastCell();
                    }
                    catch(Exception ex) { }
                tabAction.actionPerformed(e);
            }

            final Action val$tabAction;
            final SeamcatTable this$0;

            
            {
                this$0 = SeamcatTable.this;
                tabAction = action;
                super();
            }
        }
;
        getActionMap().put(actionName, myAction);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(false);
        setDefaultRenderer(java/lang/Double, SeamcatTextFieldFormats.DOUBLE_RENDERER);
        setDefaultEditor(java/lang/Double, SeamcatTextFieldFormats.DOUBLE_EDITOR);
    }

    public boolean editCellAt(int row, int column, EventObject e)
    {
        boolean res = super.editCellAt(row, column, e);
        Component c = getEditorComponent();
        if(c instanceof JTextComponent)
        {
            ((JTextComponent)c).selectAll();
            if(getColumnClass(column) == java/lang/Double && (c instanceof JFormattedTextField))
                ((JFormattedTextField)c).setFocusLostBehavior(0);
        }
        return res;
    }

    public boolean isLastCell()
    {
        int rows = getRowCount();
        int cols = getColumnCount();
        int selectedRow = getSelectedRow();
        int selectedCol = getSelectedColumn();
        return rows == selectedRow + 1 && cols == selectedCol + 1;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/components/SeamcatTable);
    private SeamcatTableLastCellAction lastCellAction;
    private static final SeamcatTextFieldFormats dialogFormats = new SeamcatTextFieldFormats();


}
