// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ImportLibraryConflictSolverDialog.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.seamcat.interfaces.ImportLibraryConflictListener;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public class ImportLibraryConflictSolverDialog extends EscapeDialog
    implements ImportLibraryConflictListener
{

    public ImportLibraryConflictSolverDialog(JFrame parent)
    {
        super(parent);
        okSelected = false;
        initComponents();
        setModal(true);
        setLocationRelativeTo(parent);
    }

    public final String resolveConflict(String existingRef, String newRef)
    {
        jLabel1.setText((new StringBuilder()).append("The reference '").append(existingRef).append("' is already in use.       ").toString());
        newReference.setText(newRef);
        pack();
        setVisible(true);
        if(owerwriteExistingEntity.isSelected())
            newRef = existingRef;
        else
            newRef = newReference.getText();
        return newRef;
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
        owerwriteExistingEntity = new JRadioButton();
        jPanel4 = new JPanel();
        renameNewEntity = new JRadioButton();
        newReference = new JTextField();
        setDefaultCloseOperation(0);
        setTitle("Import library conflict");
        setModal(true);
        setResizable(false);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitForm(evt);
            }

            final ImportLibraryConflictSolverDialog this$0;

            
            {
                this$0 = ImportLibraryConflictSolverDialog.this;
                super();
            }
        }
);
        jPanel1.setLayout(new GridLayout(2, 1));
        jLabel1.setText("The reference 'DEFAULT_ANT' is already in use.");
        jPanel1.add(jLabel1);
        jLabel2.setText("Please select one of the following options:");
        jPanel1.add(jLabel2);
        getContentPane().add(jPanel1, "North");
        jButton1.setText("OK");
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                okEventHandler(evt);
            }

            final ImportLibraryConflictSolverDialog this$0;

            
            {
                this$0 = ImportLibraryConflictSolverDialog.this;
                super();
            }
        }
);
        jPanel2.add(jButton1);
        jButton2.setText("Help");
        jPanel2.add(jButton2);
        getContentPane().add(jPanel2, "South");
        jPanel3.setLayout(new GridLayout(2, 1));
        owerwriteExistingEntity.setText("Owerwrite existing entity");
        buttonGroup1.add(owerwriteExistingEntity);
        owerwriteExistingEntity.setName("owerwriteExistingEntry");
        jPanel3.add(owerwriteExistingEntity);
        jPanel4.setLayout(new BorderLayout());
        renameNewEntity.setSelected(true);
        renameNewEntity.setText("Rename new entity:");
        buttonGroup1.add(renameNewEntity);
        jPanel4.add(renameNewEntity, "West");
        newReference.setText("jTextField1");
        jPanel4.add(newReference, "Center");
        jPanel3.add(jPanel4);
        getContentPane().add(jPanel3, "Center");
        pack();
    }

    public synchronized String handleReferenceConflict(String reference, String newReference)
    {
        String newRef = resolveConflict(reference, newReference);
        return newRef;
    }

    private void okEventHandler(ActionEvent evt)
    {
        setVisible(false);
    }

    private void exitForm(WindowEvent windowevent)
    {
    }

    private boolean okSelected;
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
    private JRadioButton owerwriteExistingEntity;
    private JRadioButton renameNewEntity;


}
