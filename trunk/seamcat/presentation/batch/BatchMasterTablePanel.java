// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchMasterTablePanel.java

package org.seamcat.presentation.batch;

import java.awt.BorderLayout;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import org.seamcat.batch.BatchJob;
import org.seamcat.batch.BatchJobList;
import org.seamcat.model.engines.InterferenceCriterionType;

public class BatchMasterTablePanel extends JPanel
{

    public BatchMasterTablePanel()
    {
        setLayout(new BorderLayout());
        masterLabel = new JLabel(STRINGLIST.getString("BATCH_JOBS"), 0);
        masterTable = new JTable() {

            public TableCellEditor getCellEditor(int row, int column)
            {
                TableCellEditor editor = null;
                switch(column)
                {
                case 2: // '\002'
                    JComboBox comboBox = new JComboBox(InterferenceCriterionType.INTERFERENCE_CRITERION_TYPES);
                    comboBox.setEditable(false);
                    editor = new DefaultCellEditor(comboBox);
                    break;

                default:
                    editor = super.getCellEditor(row, column);
                    break;
                }
                return editor;
            }

            final BatchMasterTablePanel this$0;

            
            {
                this$0 = BatchMasterTablePanel.this;
                super();
            }
        }
;
        add(masterLabel, "North");
        add(new JScrollPane(masterTable), "Center");
        masterTable.setSelectionMode(0);
    }

    public void selectFirstRow()
    {
        ListSelectionModel model = masterTable.getSelectionModel();
        if(batchJobList.getRowCount() > 0)
            model.setSelectionInterval(0, 0);
    }

    public void addListSelectionListener(ListSelectionListener lsl)
    {
        masterTable.getSelectionModel().addListSelectionListener(lsl);
    }

    public void removeSelectedWorkspace()
    {
        batchJobList.removeBatchJob(getSelectedRowIndex());
    }

    public int getSelectedRowIndex()
    {
        return masterTable.getSelectedRow();
    }

    public BatchJob getSelectedBatchJob()
    {
        return batchJobList.getBatchJob(getSelectedRowIndex());
    }

    public void setBatchJobList(BatchJobList newBatchJobList)
    {
        batchJobList = newBatchJobList;
        masterTable.setModel(batchJobList);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/batch/BatchJobList);
    private static final ResourceBundle STRINGLIST;
    private JTable masterTable;
    private BatchJobList batchJobList;
    private JLabel masterLabel;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
