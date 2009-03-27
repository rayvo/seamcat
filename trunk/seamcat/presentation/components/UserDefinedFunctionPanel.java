// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserDefinedFunctionPanel.java

package org.seamcat.presentation.components;

import java.awt.*;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.presentation.*;

// Referenced classes of package org.seamcat.presentation.components:
//            DiscreteFunctionTableModelAdapter, SeamcatTable, FunctionButtonPanel

public class UserDefinedFunctionPanel extends JPanel
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
                    int res = JOptionPane.showConfirmDialog(this, (new StringBuilder()).append("The file named <b>").append(fileChooser.getSelectedFile().getName()).append("</b>").append(" already exists.\nDo you wish to override?").toString(), "File already exists", 1);
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
            ((DiscreteFunctionTableModelAdapter)dataTable.getModel()).clear();
        }

        public void btnAddActionPerformed()
        {
            ((DiscreteFunctionTableModelAdapter)dataTable.getModel()).addRow();
        }

        public void btnDeleteActionPerformed()
        {
            model.deleteRow(dataTable.getSelectedRow());
        }

        public void btnSymActionPerformed()
        {
            if(usePropabilitySymmetrizeFunction)
                DialogTableToDataSet.symmetrizeFunction(model.getFunction().getPointsList(), symmetryPoint);
            else
                DialogTableToDataSet.symmetrize(model.getFunction().getPointsList(), symmetryPoint);
            model.sortPoints();
            model.fireChangeListeners();
        }

        final UserDefinedFunctionPanel this$0;

        private DialogFunctionButtonPanel()
        {
            this$0 = UserDefinedFunctionPanel.this;
            super();
        }

    }


    public UserDefinedFunctionPanel(String borderTitle, String xCaption, String yCaption)
    {
        this(borderTitle, xCaption, yCaption, false);
    }

    public UserDefinedFunctionPanel(String borderTitle, String xCaption, String yCaption, boolean polarPlot)
    {
        super(new GridBagLayout());
        model = new DiscreteFunctionTableModelAdapter();
        symmetryPoint = 0.0D;
        usePropabilitySymmetrizeFunction = false;
        functionGraph = new DiscreteFunctionGraph(model, polarPlot);
        if(xCaption != null)
            model.setColumnName(0, xCaption);
        if(yCaption != null)
            model.setColumnName(1, yCaption);
        dataTable = new SeamcatTable(model);
        JScrollPane dataTableScrollPane = new JScrollPane(dataTable);
        Dimension paneDims = new Dimension(150, 350);
        dataTableScrollPane.setMinimumSize(paneDims);
        dataTableScrollPane.setPreferredSize(paneDims);
        dataTableScrollPane.setMaximumSize(paneDims);
        GridBagConstraints constr = new GridBagConstraints(-1, 0, 1, 1, 1.0D, 1.0D, 11, 3, new Insets(0, 0, 0, 0), 0, 0);
        add(dataTableScrollPane, constr);
        add(new DialogFunctionButtonPanel(), constr);
        constr.fill = 1;
        constr.weightx = 50D;
        constr.weighty = 50D;
        add(functionGraph, constr);
        setBorder(new TitledBorder(borderTitle));
    }

    public TableModel getModel()
    {
        return model;
    }

    public void clear()
    {
        setDiscreteFunction(new DiscreteFunction());
    }

    public void setDiscreteFunction(DiscreteFunction d)
    {
        model.setDiscreteFunction(d);
    }

    public void setUsePropabilitySymmetrizeFunction(boolean usePropabilitySymmetrizeFunction)
    {
        this.usePropabilitySymmetrizeFunction = usePropabilitySymmetrizeFunction;
    }

    public DiscreteFunction getDiscreteFunction()
    {
        return model.getDiscreteFunction();
    }

    public double getSymmetryPoint()
    {
        return symmetryPoint;
    }

    public void setSymmetryPoint(double symmetryPoint)
    {
        this.symmetryPoint = symmetryPoint;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/components/UserDefinedFunctionPanel);
    private static final ResourceBundle STRINGS;
    private DiscreteFunctionTableModelAdapter model;
    private JTable dataTable;
    private DiscreteFunctionGraph functionGraph;
    private double symmetryPoint;
    private boolean usePropabilitySymmetrizeFunction;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }




}
