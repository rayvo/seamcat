// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PostProcessingPluginConfigurationDialog.java

package org.seamcat.presentation.postprocessing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.model.plugin.PostProcessingPlugin;
import org.seamcat.postprocessing.PostProcessingPluginWrapper;
import org.seamcat.presentation.*;
import org.seamcat.presentation.components.NavigateButtonPanel;

// Referenced classes of package org.seamcat.presentation.postprocessing:
//            ParameterTableModel

public class PostProcessingPluginConfigurationDialog extends EscapeDialog
{

    public PostProcessingPluginConfigurationDialog(JDialog owner)
    {
        super(owner, STRINGLIST.getString("PPP_EDIT_TITLE"), true);
        tableModel = new ParameterTableModel();
        accept = false;
        dialogDist = new DistributionDialog(this, true);
        dialogFunction3D = new DialogFunction2Define(this, true, true);
        dialogFunction2D = new DialogFunctionDefine(this, true);
        dialogFunction2D.setConstantEnabled(false);
        getContentPane().setLayout(new BorderLayout());
        reference = new JTextField();
        description = new JLabel("");
        JPanel northPanel = new JPanel(new LabeledPairLayout());
        northPanel.add(new JLabel(STRINGLIST.getString("PPP_EDIT_REFERENCE")), "label");
        northPanel.add(reference, "field");
        northPanel.add(new JLabel(STRINGLIST.getString("PPP_EDIT_DESCRIPTION")), "label");
        northPanel.add(description, "field");
        table = new JTable(tableModel) {

            public TableCellRenderer getCellRenderer(int row, int column)
            {
                TableCellRenderer tcr = super.getCellRenderer(row, column);
                try
                {
                    if(column == 2)
                    {
                        ListSelectionModel lsm = getSelectionModel();
                        static class _cls4
                        {

                            static final int $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[];

                            static 
                            {
                                $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType = new int[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.values().length];
                                try
                                {
                                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Integer.ordinal()] = 1;
                                }
                                catch(NoSuchFieldError ex) { }
                                try
                                {
                                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Double.ordinal()] = 2;
                                }
                                catch(NoSuchFieldError ex) { }
                                try
                                {
                                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Boolean.ordinal()] = 3;
                                }
                                catch(NoSuchFieldError ex) { }
                                try
                                {
                                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.String.ordinal()] = 4;
                                }
                                catch(NoSuchFieldError ex) { }
                                try
                                {
                                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Distribution.ordinal()] = 5;
                                }
                                catch(NoSuchFieldError ex) { }
                                try
                                {
                                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Function2D.ordinal()] = 6;
                                }
                                catch(NoSuchFieldError ex) { }
                                try
                                {
                                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Function3D.ordinal()] = 7;
                                }
                                catch(NoSuchFieldError ex) { }
                            }
                        }

                        switch(_cls4..SwitchMap.org.seamcat.model.plugin.PostProcessingPlugin.ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.valueOf(getModel().getValueAt(row, 1).toString()).ordinal()])
                        {
                        case 1: // '\001'
                            tcr = getDefaultRenderer(java/lang/Integer);
                            break;

                        case 2: // '\002'
                            tcr = getDefaultRenderer(java/lang/Double);
                            break;

                        case 3: // '\003'
                            tcr = getDefaultRenderer(java/lang/Boolean);
                            break;

                        default:
                            tcr = getDefaultRenderer(java/lang/String);
                            break;
                        }
                    }
                }
                catch(Exception e) { }
                return tcr;
            }

            public TableCellEditor getCellEditor(int row, int column)
            {
                ListSelectionModel lsm = getSelectionModel();
                TableCellEditor editor = null;
                if(column == 2)
                {
                    TableColumn valueColumn = getColumnModel().getColumn(1);
                    switch(_cls4..SwitchMap.org.seamcat.model.plugin.PostProcessingPlugin.ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.valueOf(getModel().getValueAt(row, 1).toString()).ordinal()])
                    {
                    case 1: // '\001'
                        editor = getDefaultEditor(java/lang/Integer);
                        break;

                    case 2: // '\002'
                        editor = getDefaultEditor(java/lang/Double);
                        break;

                    case 3: // '\003'
                        editor = getDefaultEditor(java/lang/Boolean);
                        break;

                    case 4: // '\004'
                        editor = getDefaultEditor(java/lang/String);
                        break;

                    default:
                        editor = getDefaultEditor(java/lang/String);
                        break;
                    }
                } else
                {
                    editor = super.getCellEditor(row, column);
                }
                return editor;
            }

            final PostProcessingPluginConfigurationDialog this$0;

            
            {
                this$0 = PostProcessingPluginConfigurationDialog.this;
                super(x0);
            }
        }
;
        table.setDefaultRenderer(java/lang/Double, SeamcatTextFieldFormats.DOUBLE_RENDERER);
        table.setDefaultEditor(java/lang/Double, SeamcatTextFieldFormats.DOUBLE_EDITOR);
        table.getColumnModel().getColumn(0).sizeWidthToFit();
        table.getColumnModel().getColumn(1).sizeWidthToFit();
        table.getColumnModel().getColumn(2).sizeWidthToFit();
        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e)
            {
                try
                {
                    if(e.getClickCount() > 1)
                        switch(_cls4..SwitchMap.org.seamcat.model.plugin.PostProcessingPlugin.ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.valueOf(tableModel.getValueAt(table.getSelectedRow(), 1).toString()).ordinal()])
                        {
                        default:
                            break;

                        case 5: // '\005'
                        {
                            if(dialogDist.showDistributionDialog((Distribution)plugin.getParameterValue(table.getSelectedRow() + 1)))
                                plugin.setParameterValue(table.getSelectedRow() + 1, dialogDist.getDistributionable());
                            break;
                        }

                        case 6: // '\006'
                        {
                            DiscreteFunction func = (DiscreteFunction)plugin.getParameterValue(table.getSelectedRow() + 1);
                            if(dialogFunction2D.show(func))
                            {
                                func = (DiscreteFunction)dialogFunction2D.getFunction();
                                plugin.setParameterValue(table.getSelectedRow() + 1, func);
                            }
                            break;
                        }

                        case 7: // '\007'
                        {
                            DiscreteFunction2 func = (DiscreteFunction2)plugin.getParameterValue(table.getSelectedRow() + 1);
                            if(dialogFunction3D.show(func))
                            {
                                func = (DiscreteFunction2)dialogFunction3D.getFunction();
                                plugin.setParameterValue(table.getSelectedRow() + 1, func);
                            }
                            break;
                        }
                        }
                }
                catch(Exception e1)
                {
                    PostProcessingPluginConfigurationDialog.LOG.error("An Error occured", e1);
                }
            }

            final PostProcessingPluginConfigurationDialog this$0;

            
            {
                this$0 = PostProcessingPluginConfigurationDialog.this;
                super();
            }
        }
);
        navigation = new NavigateButtonPanel() {

            public void btnOkActionPerformed()
            {
                accept = true;
                setVisible(false);
            }

            public void btnCancelActionPerformed()
            {
                setVisible(false);
            }

            final PostProcessingPluginConfigurationDialog this$0;

            
            {
                this$0 = PostProcessingPluginConfigurationDialog.this;
                super();
            }
        }
;
        getContentPane().add(northPanel, "North");
        getContentPane().add(new JScrollPane(table), "Center");
        getContentPane().add(navigation, "South");
        ((JPanel)getContentPane()).setBorder(new TitledBorder(STRINGLIST.getString("PPP_EDIT_BORDER")));
        pack();
        setSize(700, 500);
        setLocationRelativeTo(owner);
    }

    public void show(PostProcessingPluginWrapper p)
    {
        accept = false;
        plugin = new PostProcessingPluginWrapper(p);
        reference.setText(plugin.getReference());
        description.setText(plugin.getDescription());
        tableModel.setPlugin(plugin);
        setVisible(true);
        if(accept)
        {
            p.setReference(reference.getText());
            for(int i = 1; i <= p.getNumberOfParameters(); i++)
                p.setParameterValue(i, plugin.getParameterValue(i));

        }
        plugin = null;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/postprocessing/PostProcessingPluginConfigurationDialog);
    private static final ResourceBundle STRINGLIST;
    private JTextField reference;
    private JLabel description;
    private JTable table;
    private ParameterTableModel tableModel;
    private NavigateButtonPanel navigation;
    private DistributionDialog dialogDist;
    private DialogFunction2Define dialogFunction3D;
    private DialogFunctionDefine dialogFunction2D;
    private PostProcessingPluginWrapper plugin;
    private boolean accept;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }








}
