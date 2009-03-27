// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PostProcessingPluginAssignmentDialog.java

package org.seamcat.presentation.postprocessing;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.seamcat.model.*;
import org.seamcat.postprocessing.PostProcessingPluginWrapper;
import org.seamcat.presentation.EscapeDialog;
import org.seamcat.presentation.components.NavigateButtonPanel;

// Referenced classes of package org.seamcat.presentation.postprocessing:
//            AssignedPluginsListModel, PostProcessingPluginConfigurationDialog

public class PostProcessingPluginAssignmentDialog extends EscapeDialog
{
    private class IndexedListCellRenderer extends DefaultListCellRenderer
    {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            String newValue = (new StringBuilder()).append(index + 1).append(": ").append(value.toString()).toString();
            return super.getListCellRendererComponent(list, newValue, index, isSelected, cellHasFocus);
        }

        final PostProcessingPluginAssignmentDialog this$0;

        private IndexedListCellRenderer()
        {
            this$0 = PostProcessingPluginAssignmentDialog.this;
            super();
        }

    }


    public PostProcessingPluginAssignmentDialog(Frame owner)
    {
        super(owner, STRINGLIST.getString("PPP_WORKSPACE_TITLE"), true);
        listModel = new AssignedPluginsListModel();
        accept = false;
        editDialog = new PostProcessingPluginConfigurationDialog(this);
        libraryPlugins = new JComboBox(Model.getInstance().getLibrary().getPostProcessingPlugins().createComboBoxModel());
        libraryPlugins.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e)
            {
                if(libraryPlugins.getSelectedItem() == null)
                    addButton.setEnabled(false);
                else
                    addButton.setEnabled(true);
            }

            final PostProcessingPluginAssignmentDialog this$0;

            
            {
                this$0 = PostProcessingPluginAssignmentDialog.this;
                super();
            }
        }
);
        plugins = new JList(listModel);
        plugins.setSelectionMode(0);
        plugins.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e)
            {
                if(plugins.getSelectedValue() == null)
                {
                    upButton.setEnabled(false);
                    downButton.setEnabled(false);
                    removeButton.setEnabled(false);
                    editButton.setEnabled(false);
                } else
                {
                    int selectedIndex = plugins.getSelectedIndex();
                    if(selectedIndex < 1)
                        upButton.setEnabled(false);
                    else
                        upButton.setEnabled(true);
                    if(selectedIndex > listModel.getSize() - 2)
                        downButton.setEnabled(false);
                    else
                        downButton.setEnabled(true);
                    removeButton.setEnabled(true);
                    editButton.setEnabled(true);
                }
            }

            final PostProcessingPluginAssignmentDialog this$0;

            
            {
                this$0 = PostProcessingPluginAssignmentDialog.this;
                super();
            }
        }
);
        plugins.setCellRenderer(new IndexedListCellRenderer());
        addButton = new JButton(STRINGLIST.getString("PPP_WORKSPACE_ADD"));
        removeButton = new JButton(STRINGLIST.getString("PPP_WORKSPACE_REMOVE"));
        editButton = new JButton(STRINGLIST.getString("PPP_WORKSPACE_EDIT"));
        upButton = new JButton(STRINGLIST.getString("PPP_WORKSPACE_UP"));
        downButton = new JButton(STRINGLIST.getString("PPP_WORKSPACE_DOWN"));
        addButton.setEnabled(false);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
        removeButton.setEnabled(false);
        editButton.setEnabled(false);
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

            final PostProcessingPluginAssignmentDialog this$0;

            
            {
                this$0 = PostProcessingPluginAssignmentDialog.this;
                super();
            }
        }
;
        upButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                Object s = plugins.getSelectedValue();
                listModel.moveUp(plugins.getSelectedIndex());
                plugins.setSelectedValue(s, true);
            }

            final PostProcessingPluginAssignmentDialog this$0;

            
            {
                this$0 = PostProcessingPluginAssignmentDialog.this;
                super();
            }
        }
);
        downButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                Object s = plugins.getSelectedValue();
                listModel.moveDown(plugins.getSelectedIndex());
                plugins.setSelectedValue(s, true);
            }

            final PostProcessingPluginAssignmentDialog this$0;

            
            {
                this$0 = PostProcessingPluginAssignmentDialog.this;
                super();
            }
        }
);
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                listModel.addPlugin(new PostProcessingPluginWrapper((PostProcessingPluginWrapper)libraryPlugins.getSelectedItem()));
            }

            final PostProcessingPluginAssignmentDialog this$0;

            
            {
                this$0 = PostProcessingPluginAssignmentDialog.this;
                super();
            }
        }
);
        removeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                listModel.removePlugin(plugins.getSelectedIndex());
            }

            final PostProcessingPluginAssignmentDialog this$0;

            
            {
                this$0 = PostProcessingPluginAssignmentDialog.this;
                super();
            }
        }
);
        editButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                editDialog.show((PostProcessingPluginWrapper)plugins.getSelectedValue());
            }

            final PostProcessingPluginAssignmentDialog this$0;

            
            {
                this$0 = PostProcessingPluginAssignmentDialog.this;
                super();
            }
        }
);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(plugins), "Center");
        getContentPane().add(libraryPlugins, "North");
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        int y = 0;
        buttonPanel.add(addButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(editButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(removeButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(upButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(downButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        buttonPanel.add(Box.createVerticalGlue(), new GridBagConstraints(1, y++, 1, 1, 1.0D, 100D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        getContentPane().add(buttonPanel, "East");
        getContentPane().add(navigation, "South");
        ((JPanel)getContentPane()).setBorder(new TitledBorder(STRINGLIST.getString("PPP_WORKSPACE_BORDER_TITLE")));
        pack();
        setSize(400, 300);
        setLocationRelativeTo(owner);
    }

    public void show(Workspace ws)
    {
        accept = false;
        listModel.setPlugins(ws.getPostProcessingPlugins());
        setVisible(true);
        if(accept)
            ws.setPostProcessingPlugins(listModel.getPlugins());
    }

    private static final ResourceBundle STRINGLIST;
    private JList plugins;
    private JComboBox libraryPlugins;
    private JButton addButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton upButton;
    private JButton downButton;
    private NavigateButtonPanel navigation;
    private AssignedPluginsListModel listModel;
    private PostProcessingPluginConfigurationDialog editDialog;
    private boolean accept;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }










}
