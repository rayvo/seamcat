// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogResolveLibraryImportConflict.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public final class DialogResolveLibraryImportConflict extends JFrame
{

    public DialogResolveLibraryImportConflict()
    {
        initComponents();
    }

    private void initComponents()
    {
        buttonGroup1 = new ButtonGroup();
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jPanel2 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jPanel3 = new JPanel();
        owerwriteExistingEntry = new JRadioButton();
        jPanel4 = new JPanel();
        renameNewEntity = new JRadioButton();
        newReference = new JTextField();
        setTitle("Resolve Library Import Conflict");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitForm(evt);
            }

            final DialogResolveLibraryImportConflict this$0;

            
            {
                this$0 = DialogResolveLibraryImportConflict.this;
                super();
            }
        }
);
        jPanel1.setLayout(new BorderLayout());
        jLabel1.setText("The reference 'DEFAULT_ANT' is already in use.");
        jLabel1.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        jPanel1.add(jLabel1, "North");
        jLabel2.setText("Please select one of the following options:");
        jLabel2.setBorder(new EmptyBorder(new Insets(2, 2, 2, 2)));
        jPanel1.add(jLabel2, "Center");
        getContentPane().add(jPanel1, "North");
        jButton1.setText("OK");
        jPanel2.add(jButton1);
        jButton2.setText("Help");
        jPanel2.add(jButton2);
        getContentPane().add(jPanel2, "South");
        jPanel3.setLayout(new GridLayout(2, 1));
        owerwriteExistingEntry.setText("Overwrite existing entity");
        owerwriteExistingEntry.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                owerwriteExistingEntryActionPerformed(evt);
            }

            final DialogResolveLibraryImportConflict this$0;

            
            {
                this$0 = DialogResolveLibraryImportConflict.this;
                super();
            }
        }
);
        jPanel3.add(owerwriteExistingEntry);
        jPanel4.setLayout(new FlowLayout(0));
        renameNewEntity.setText("Rename new entity:");
        jPanel4.add(renameNewEntity);
        newReference.setText("DEFAULT_ANT1                     ");
        jPanel4.add(newReference);
        jPanel3.add(jPanel4);
        getContentPane().add(jPanel3, "Center");
        pack();
    }

    private void owerwriteExistingEntryActionPerformed(ActionEvent actionevent)
    {
    }

    private void exitForm(WindowEvent evt)
    {
        System.exit(0);
    }

    public static void main(String args[])
    {
        (new DialogResolveLibraryImportConflict()).show();
    }

    private ButtonGroup buttonGroup1;
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JTextField newReference;
    private JRadioButton owerwriteExistingEntry;
    private JRadioButton renameNewEntity;


}
