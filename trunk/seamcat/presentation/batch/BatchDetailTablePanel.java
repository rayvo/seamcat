// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchDetailTablePanel.java

package org.seamcat.presentation.batch;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.batch.BatchJob;
import org.seamcat.batch.BatchParameter;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.function.Function;
import org.seamcat.function.Function2;
import org.seamcat.presentation.DialogFunction2Define;
import org.seamcat.presentation.DialogFunctionDefine;
import org.seamcat.presentation.DistributionDialog;
import org.seamcat.presentation.components.SeamcatFilteredList;
import org.seamcat.presentation.components.SpringUtilities;

// Referenced classes of package org.seamcat.presentation.batch:
//            BatchParameterListModel

public class BatchDetailTablePanel extends JPanel
{
    private static class BatchJobTableModel
        implements TableModel, ActionListener
    {

        private void setBatchJob(BatchJob job)
        {
            if(this.job != null)
                this.job.removeActionListener(this);
            this.job = job;
            if(job != null)
                job.addActionListener(this);
            notifyTableModelListeners();
        }

        public int getColumnCount()
        {
            return COLUMN_NAMES.length;
        }

        public int getRowCount()
        {
            return job == null ? 0 : job.getParametersCount();
        }

        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return columnIndex == 2 && (job.getParameter(rowIndex).getType().equals("String") || job.getParameter(rowIndex).getType().equals("Double") || job.getParameter(rowIndex).getType().equals("Integer") || job.getParameter(rowIndex).getType().equals("Boolean"));
        }

        public Class getColumnClass(int columnIndex)
        {
            return java/lang/Object;
        }

        public Object getValueAt(int rowIndex, int columnIndex)
        {
            Object value;
            if(job == null)
            {
                value = null;
            } else
            {
                BatchParameter parameter = job.getParameter(rowIndex);
                switch(columnIndex)
                {
                case 0: // '\0'
                    value = useLongParameterNames ? ((Object) (parameter.toString())) : ((Object) (parameter.getName()));
                    break;

                case 1: // '\001'
                    value = parameter.getType();
                    break;

                case 2: // '\002'
                    value = parameter.getNewValue();
                    break;

                case 3: // '\003'
                    value = parameter.getOldValue();
                    break;

                default:
                    throw new IndexOutOfBoundsException((new StringBuilder()).append("ColumnIndex = ").append(columnIndex).append(" is invalid").toString());
                }
            }
            return value;
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            if(job != null)
                job.getParameter(rowIndex).setNewValue(aValue);
        }

        public String getColumnName(int columnIndex)
        {
            return COLUMN_NAMES[columnIndex];
        }

        public void addTableModelListener(TableModelListener l)
        {
            tableModelListenerList.add(l);
        }

        public void removeTableModelListener(TableModelListener l)
        {
            tableModelListenerList.remove(l);
        }

        public void notifyTableModelListeners()
        {
            notifyTableModelListeners(new TableModelEvent(this));
        }

        private void notifyTableModelListeners(TableModelEvent e)
        {
            TableModelListener listener;
            for(Iterator i$ = tableModelListenerList.iterator(); i$.hasNext(); listener.tableChanged(e))
                listener = (TableModelListener)i$.next();

        }

        public void actionPerformed(ActionEvent e)
        {
            notifyTableModelListeners();
        }

        public void setUseLongParameterNames(boolean useLongParameterNames)
        {
            this.useLongParameterNames = useLongParameterNames;
            notifyTableModelListeners();
        }

        public boolean isUsingLongParameternames()
        {
            return useLongParameterNames;
        }

        public BatchParameter getBatchParameter(int index)
        {
            return job.getParameter(index);
        }

        private static final String COLUMN_NAMES[] = {
            BatchDetailTablePanel.STRINGLIST.getString("DetailModel.parameter"), BatchDetailTablePanel.STRINGLIST.getString("DetailModel.type"), BatchDetailTablePanel.STRINGLIST.getString("DetailModel.new_value"), BatchDetailTablePanel.STRINGLIST.getString("DetailModel.old_value")
        };
        private BatchJob job;
        private final java.util.List tableModelListenerList;
        private boolean useLongParameterNames;



        private BatchJobTableModel()
        {
            tableModelListenerList = new ArrayList();
            useLongParameterNames = false;
        }

    }

    private class SelectPanel extends JPanel
    {
        private class BatchParameterCellRenderer extends JLabel
            implements ListCellRenderer
        {

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                setText(value.toString());
                if(isSelected)
                {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else
                {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                setEnabled(list.isEnabled() && !modelNotSelected.isExcluded((BatchParameter)value));
                setFont(list.getFont());
                setOpaque(true);
                return this;
            }

            final SelectPanel this$1;

            private BatchParameterCellRenderer()
            {
                this$1 = SelectPanel.this;
                super();
            }

        }


        void setBatchJob(BatchJob batchJob)
        {
            btnAdd.setEnabled(false);
            btnRemove.setEnabled(false);
            boolean enableList = batchJob != null;
            if(batchParameterList.isEnabled() ^ enableList)
                batchParameterList.setEnabled(enableList);
            modelNotSelected.setBatchJob(batchJob);
        }

        private final JCheckBox xbLongParameterNames;
        private final BatchParameterListModel modelNotSelected;
        private final SeamcatFilteredList batchParameterList;
        private final JButton btnAdd;
        private final JButton btnRemove;
        final BatchDetailTablePanel this$0;






        private SelectPanel()
        {
            this$0 = BatchDetailTablePanel.this;
            super(new BorderLayout());
            xbLongParameterNames = new JCheckBox(BatchDetailTablePanel.STRINGLIST.getString("BATCH_LONGNAMES"));
            modelNotSelected = new BatchParameterListModel();
            batchParameterList = new SeamcatFilteredList(modelNotSelected);
            btnAdd = new JButton(BatchDetailTablePanel.STRINGLIST.getString("BTN_CAPTION_ADD_ARROW"));
            btnRemove = new JButton(BatchDetailTablePanel.STRINGLIST.getString("BTN_CAPTION_REMOVE_ARROW"));
            batchParameterList.setCellRenderer(new BatchParameterCellRenderer());
            batchParameterList.addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e)
                {
                    int row = batchParameterList.getSelectedIndex();
                    if(row != -1)
                    {
                        boolean isExcluded = modelNotSelected.isExcluded(row);
                        if((!btnAdd.isEnabled()) ^ isExcluded)
                            btnAdd.setEnabled(!isExcluded);
                    }
                }

                final BatchDetailTablePanel val$this$0;
                final SelectPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            batchParameterList.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e)
                {
                    int row = batchParameterList.getSelectedIndex();
                    if(row != -1)
                        switch(e.getClickCount())
                        {
                        default:
                            break;

                        case 2: // '\002'
                            if(btnAdd.isEnabled())
                                btnAdd.doClick();
                            break;
                        }
                }

                final BatchDetailTablePanel val$this$0;
                final SelectPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnAdd.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    int indices[] = batchParameterList.getSelectedIndices();
                    int arr$[] = indices;
                    int len$ = arr$.length;
                    for(int i$ = 0; i$ < len$; i$++)
                    {
                        int index = arr$[i$];
                        BatchParameter parameter = (BatchParameter)batchParameterList.getModel().getElementAt(index);
                        batchJob.addParameter(parameter);
                        modelNotSelected.addExclude(parameter);
                    }

                    int nextIndex = indices[indices.length - 1] + 1;
                    int maxIndex = Math.max(batchParameterList.getModel().getSize() - 1, 0);
                    boolean atTheEnd = false;
                    boolean indexFound;
                    for(indexFound = false; nextIndex <= maxIndex && !indexFound;)
                        if(!modelNotSelected.isExcluded(nextIndex))
                            indexFound = true;
                        else
                            nextIndex++;

                    if(indexFound)
                    {
                        BatchParameter p = (BatchParameter)batchParameterList.getModel().getElementAt(nextIndex);
                        batchParameterList.setSelectedValue(p, true);
                    } else
                    {
                        btnAdd.setEnabled(false);
                    }
                }

                final BatchDetailTablePanel val$this$0;
                final SelectPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnRemove.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    int row = detailTable.getSelectedRow();
                    if(row != -1)
                    {
                        BatchParameter parameter = detailTableModel.getBatchParameter(row);
                        batchJob.removeParameter(row);
                        modelNotSelected.removeExclude(parameter);
                        btnRemove.setEnabled(false);
                    }
                }

                final BatchDetailTablePanel val$this$0;
                final SelectPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            xbLongParameterNames.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    detailTableModel.setUseLongParameterNames(xbLongParameterNames.isSelected());
                }

                final BatchDetailTablePanel val$this$0;
                final SelectPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            JPanel buttonPanel = new JPanel(new SpringLayout());
            buttonPanel.add(btnAdd);
            buttonPanel.add(btnRemove);
            buttonPanel.add(Box.createVerticalStrut(1500));
            SpringUtilities.makeCompactGrid(buttonPanel, 3, 1, 0, 0, 0, 0);
            JPanel listPanel = new JPanel(new BorderLayout());
            listPanel.add(batchParameterList.getFilterField(), "North");
            listPanel.add(new JScrollPane(batchParameterList), "Center");
            add(listPanel, "Center");
            add(buttonPanel, "East");
            add(xbLongParameterNames, "South");
        }

    }


    public BatchDetailTablePanel(JDialog parent)
    {
        super(new GridLayout());
        detailTable = new JTable(detailTableModel);
        distDialog = new DistributionDialog(parent, true);
        editFunction = new DialogFunctionDefine(parent, true);
        editFunction2 = new DialogFunction2Define(parent, true);
        detailTable.setSelectionMode(0);
        detailTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e)
            {
                int row = detailTable.getSelectedRow();
                if(row != -1)
                    switch(e.getClickCount())
                    {
                    default:
                        break;

                    case 1: // '\001'
                        selectPanel.batchParameterList.setSelectedValue(detailTableModel.getBatchParameter(row), true);
                        break;

                    case 2: // '\002'
                        try
                        {
                            BatchParameter param = detailTableModel.getBatchParameter(row);
                            if(param.getType().equals("Distribution"))
                            {
                                if(distDialog.showDistributionDialog((Distribution)param.getNewValue(), BatchDetailTablePanel.STRINGLIST.getString("GENERAL_DISTRIBUTION_TITLE")))
                                    param.setNewValue(distDialog.getDistributionable());
                                break;
                            }
                            if(!param.getType().equals("Function"))
                                break;
                            if(param.getNewValue() instanceof Function)
                            {
                                if(editFunction.show((Function)param.getNewValue(), BatchDetailTablePanel.STRINGLIST.getString("GENERAL_FUNCTION_TITLE")))
                                    param.setNewValue(editFunction.getFunction());
                                break;
                            }
                            if((param.getNewValue() instanceof DiscreteFunction2) && editFunction2.show((Function2)param.getNewValue(), BatchDetailTablePanel.STRINGLIST.getString("GENERAL_FUNCTION_TITLE")))
                                param.setNewValue(editFunction2.getFunction());
                        }
                        catch(Exception e1) { }
                        break;
                    }
            }

            final BatchDetailTablePanel this$0;

            
            {
                this$0 = BatchDetailTablePanel.this;
                super();
            }
        }
);
        detailTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e)
            {
                int row = detailTable.getSelectedRow();
                boolean enable = row != -1;
                if(selectPanel.btnRemove.isEnabled() ^ enable)
                    selectPanel.btnRemove.setEnabled(enable);
            }

            final BatchDetailTablePanel this$0;

            
            {
                this$0 = BatchDetailTablePanel.this;
                super();
            }
        }
);
        selectPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        JPanel detailPanel = new JPanel(new GridLayout(2, 1));
        detailPanel.add(selectPanel);
        detailPanel.add(new JScrollPane(detailTable));
        setBorder(new TitledBorder(STRINGLIST.getString("BATCH_PARAMETERS")));
        add(detailPanel);
    }

    public void setBatchJob(BatchJob batchJob)
    {
        this.batchJob = batchJob;
        ((BatchJobTableModel)detailTable.getModel()).setBatchJob(batchJob);
        selectPanel.setBatchJob(batchJob);
    }

    public void addBatchParameter(BatchParameter bp)
    {
        batchJob.addParameter(bp);
    }

    public void removeSelectedParameter()
    {
        try
        {
            batchJob.removeParameter(detailTable.getSelectedRow());
        }
        catch(Exception ex) { }
    }

    private static final ResourceBundle STRINGLIST;
    private final BatchJobTableModel detailTableModel = new BatchJobTableModel();
    private final JTable detailTable;
    private final SelectPanel selectPanel = new SelectPanel();
    private BatchJob batchJob;
    private final DistributionDialog distDialog;
    private final DialogFunctionDefine editFunction;
    private final DialogFunction2Define editFunction2;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }








}
