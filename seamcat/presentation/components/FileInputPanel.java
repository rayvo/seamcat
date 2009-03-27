// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileInputPanel.java

package org.seamcat.presentation.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

public class FileInputPanel extends JPanel
{

    public FileInputPanel(String text)
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        selectionText = "Select Seamcat Directory";
        selectionText = text;
        fileButton = new JButton(STRINGLIST.getString("BTN_CAPTION_BROWSE"));
        fileName = new JTextField();
        fileChooser.setMultiSelectionEnabled(false);
        fileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                FileInputPanel.fileChooser.setSelectedFile(new File(fileName.getText()));
                int res = FileInputPanel.fileChooser.showDialog(FileInputPanel.this, selectionText);
                if(res == 0)
                    fileName.setText(FileInputPanel.fileChooser.getSelectedFile().getAbsolutePath());
            }

            final FileInputPanel this$0;

            
            {
                this$0 = FileInputPanel.this;
                super();
            }
        }
);
        setLayout(new BorderLayout());
        add(fileName, "Center");
        add(fileButton, "East");
    }

    public String getFileString()
    {
        return fileName.getText();
    }

    public void setString(String str)
    {
        fileName.setText(str);
    }

    public void setText(String str)
    {
        setString(str);
    }

    public String getText()
    {
        return getFileString();
    }

    public void setDirectoriesOnly()
    {
        fileChooser.setFileSelectionMode(1);
    }

    protected ResourceBundle STRINGLIST;
    protected JButton fileButton;
    protected JTextField fileName;
    protected static final JFileChooser fileChooser = new JFileChooser();
    protected String selectionText;
    private final FileFilter fileFilterLegacy = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String extension = "";
                    String name = file.getName();
                    int i = name.lastIndexOf('.');
                    if(i > 0 && i < name.length() - 1)
                        extension = name.substring(i + 1).toLowerCase();
                    returnValue = extension.equalsIgnoreCase("TXT");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "SEAMCAT 2 Files";
        }

        final FileInputPanel this$0;

            
            {
                this$0 = FileInputPanel.this;
                super();
            }
    }
;
    private final FileFilter fileFilterWorkspace = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String extension = "";
                    String name = file.getName();
                    int i = name.lastIndexOf('.');
                    if(i > 0 && i < name.length() - 1)
                        extension = name.substring(i + 1).toLowerCase();
                    returnValue = extension.equalsIgnoreCase("SWS");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "SEAMCAT Workspace Files";
        }

        final FileInputPanel this$0;

            
            {
                this$0 = FileInputPanel.this;
                super();
            }
    }
;
    private final FileFilter fileFilterDefaultWorkspace = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String name = file.getName();
                    returnValue = name.equalsIgnoreCase("default-workspace.xml");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "Default SEAMCAT Workspace";
        }

        final FileInputPanel this$0;

            
            {
                this$0 = FileInputPanel.this;
                super();
            }
    }
;
    private final FileFilter fileFilterDirectoriesOnly = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null && file.isDirectory())
                returnValue = true;
            return returnValue;
        }

        public String getDescription()
        {
            return "Directories";
        }

        final FileInputPanel this$0;

            
            {
                this$0 = FileInputPanel.this;
                super();
            }
    }
;

}
