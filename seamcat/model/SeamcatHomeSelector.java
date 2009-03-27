// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatHomeSelector.java

package org.seamcat.model;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.SplashScreen;
import org.seamcat.presentation.components.FileInputPanel;

public class SeamcatHomeSelector extends JDialog
{

    public SeamcatHomeSelector(SplashScreen parent)
    {
        super((Frame)parent.getParent(), true);
        fileInput = new FileInputPanel("Select Seamcat Directory");
        okButton = new JButton("OK");
        storeHome = new JCheckBox("Store Home");
        fileInput.setDirectoriesOnly();
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                setVisible(false);
            }

            final SeamcatHomeSelector this$0;

            
            {
                this$0 = SeamcatHomeSelector.this;
                super();
            }
        }
);
        storeHome.setSelected(true);
        JPanel center = new JPanel();
        center.setBorder(new TitledBorder("Select SEAMCAT Home directory"));
        center.add(fileInput);
        center.add(storeHome);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(center, "Center");
        JPanel south = new JPanel();
        south.add(okButton);
        getContentPane().add(south, "South");
        setTitle("Select SEAMCAT home");
        setSize(350, 150);
        setLocationRelativeTo(parent);
    }

    public String getDirectory()
    {
        fileInput.setString((new StringBuilder()).append(System.getProperty("user.home")).append(File.separator).append("seamcat").toString());
        setVisible(true);
        return fileInput.getFileString();
    }

    public boolean store()
    {
        return storeHome.isSelected();
    }

    private FileInputPanel fileInput;
    private JButton okButton;
    private JCheckBox storeHome;
}