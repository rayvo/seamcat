// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DiscreteFunction2Panel.java

package org.seamcat.presentation;

import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import org.apache.log4j.Logger;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.presentation.components.DiscreteFunction2TableModelAdapter;
import org.seamcat.presentation.components.FunctionButtonPanel;
import org.seamcat.presentation.components.SeamcatTable;

// Referenced classes of package org.seamcat.presentation:
//            UnwantedEmissionGraph2, FileDataIO, DialogTableToDataSet

public class DiscreteFunction2Panel extends JPanel
{
    private class DialogFunctionButtonPanel extends FunctionButtonPanel
    {

        public void btnLoadActionPerformed()
        {
            if(fileChooser.showOpenDialog(this) == 0)
            {
                fileio.setFile(fileChooser.getSelectedFile());
                model.setFunctionable(fileio.loadTableData());
            }
        }

        public void btnSaveActionPerformed()
        {
            if(fileChooser.showSaveDialog(this) == 0)
            {
                if(fileChooser.getSelectedFile().exists())
                {
                    int res = JOptionPane.showConfirmDialog(this, (new StringBuilder()).append("<html>The file named <b>").append(fileChooser.getSelectedFile().getName()).append("</b>").append(" already exists.<br>Do you wish to override?</html>").toString(), "File already exists", 1);
                    if(res == 1)
                    {
                        btnSaveActionPerformed();
                        return;
                    }
                    if(res == 2)
                        return;
                }
                fileio.setFile(fileChooser.getSelectedFile());
                fileio.saveTableData(model.getFunction());
            }
        }

        public void btnClearActionPerformed()
        {
            ((DiscreteFunction2TableModelAdapter)dataTable.getModel()).clear();
        }

        public void btnAddActionPerformed()
        {
            ((DiscreteFunction2TableModelAdapter)dataTable.getModel()).addRow();
        }

        public void btnDeleteActionPerformed()
        {
            model.deleteRow(dataTable.getSelectedRow());
        }

        public void btnSymActionPerformed()
        {
            DialogTableToDataSet.symmetrizeFunction(model.getFunction().getPointsList(), 0.0D);
            model.sortPoints();
            model.fireChangeListeners();
        }

        final DiscreteFunction2Panel this$0;

        public DialogFunctionButtonPanel()
        {
            this$0 = DiscreteFunction2Panel.this;
            super();
        }
    }


    public DiscreteFunction2Panel(String title)
    {
        super(new BorderLayout());
        model = new DiscreteFunction2TableModelAdapter();
        userDefinitionPanel = new UnwantedEmissionGraph2(model);
        LOG.debug("Constructing Panel for 3D Functions");
        dataTable = new SeamcatTable(model);
        JScrollPane dataTableScrollPane = new JScrollPane(dataTable);
        Dimension paneDims = new Dimension(225, 350);
        dataTableScrollPane.setMinimumSize(paneDims);
        dataTableScrollPane.setPreferredSize(paneDims);
        dataTableScrollPane.setMaximumSize(paneDims);
        GridBagConstraints constr = new GridBagConstraints(-1, 0, 1, 1, 1.0D, 1.0D, 11, 3, new Insets(0, 0, 0, 0), 0, 0);
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(dataTableScrollPane, constr);
        centerPanel.add(new DialogFunctionButtonPanel(), constr);
        constr.fill = 1;
        constr.weightx = 50D;
        constr.weighty = 50D;
        centerPanel.add(userDefinitionPanel, constr);
        add(centerPanel, "Center");
    }

    public DiscreteFunction2TableModelAdapter getTableModel()
    {
        return model;
    }

    public boolean isLastCell()
    {
        int rows = dataTable.getRowCount();
        int cols = dataTable.getColumnCount();
        int selectedRow = dataTable.getSelectedRow();
        int selectedCol = dataTable.getSelectedColumn();
        return rows == selectedRow + 1 && cols == selectedCol + 1;
    }

    public void setFunctionable(DiscreteFunction2 f)
    {
        model.setDiscreteFunction2(f);
    }

    public DiscreteFunction2 getFunctionable()
    {
        return model.getDiscreteFunction2();
    }

    public void stopEditing()
    {
        if(dataTable.isEditing())
            dataTable.editingStopped(new ChangeEvent(dataTable));
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/DiscreteFunction2Panel);
    private static final ResourceBundle helplist;
    private static final ResourceBundle STRINGLIST;
    private DiscreteFunction2TableModelAdapter model;
    private JTable dataTable;
    private UnwantedEmissionGraph2 userDefinitionPanel;

    static 
    {
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }


}
