// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AddWorkspacePanel.java

package org.seamcat.presentation.batch;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.presentation.MainWindow;

public class AddWorkspacePanel extends JPanel
{

    public AddWorkspacePanel()
    {
        filename = new JTextField();
        openFile = new JButton("Choose");
        state = 0;
        setLayout(new GridLayout(2, 1));
        buttons = new ButtonGroup();
        filename.setEnabled(false);
        openFile.setEnabled(false);
        newWorkspace = new JRadioButton("New Workspace");
        openWorkspace = new JRadioButton("Open existing workspace");
        newWorkspace.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                state = 0;
                filename.setEnabled(openWorkspace.isSelected());
                openFile.setEnabled(openWorkspace.isSelected());
            }

            final AddWorkspacePanel this$0;

            
            {
                this$0 = AddWorkspacePanel.this;
                super();
            }
        }
);
        openWorkspace.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                state = 1;
                filename.setEnabled(openWorkspace.isSelected());
                openFile.setEnabled(openWorkspace.isSelected());
            }

            final AddWorkspacePanel this$0;

            
            {
                this$0 = AddWorkspacePanel.this;
                super();
            }
        }
);
        openFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                MainWindow.FILECHOOSER.setFileFilter(MainWindow.FILE_FILTER_WORKSPACE);
                if(MainWindow.FILECHOOSER.showOpenDialog(AddWorkspacePanel.this) == 0)
                    filename.setText(MainWindow.FILECHOOSER.getSelectedFile().getAbsolutePath());
            }

            final AddWorkspacePanel this$0;

            
            {
                this$0 = AddWorkspacePanel.this;
                super();
            }
        }
);
        buttons.add(newWorkspace);
        buttons.add(openWorkspace);
        add(newWorkspace);
        JPanel openPanel = new JPanel(new BorderLayout());
        openPanel.add(openWorkspace, "North");
        JPanel filepanel = new JPanel(new BorderLayout());
        filepanel.add(filename, "Center");
        filepanel.add(openFile, "East");
        openPanel.add(filepanel, "Center");
        add(openPanel);
        setBorder(new TitledBorder(STRINGLIST.getString("DEFINE_WORKSPACE_TITLE")));
        newWorkspace.setSelected(true);
    }

    public int getState()
    {
        return state;
    }

    public String getFilename()
    {
        return filename.getText();
    }

    private static final ResourceBundle STRINGLIST;
    public static final int STATE_NEW_WORKSPACE = 0;
    public static final int STATE_OPEN_EXISTING_WORKSPACE = 1;
    private JRadioButton newWorkspace;
    private JRadioButton openWorkspace;
    private JTextField filename;
    private JButton openFile;
    private ButtonGroup buttons;
    private int state;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }




}
