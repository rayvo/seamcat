// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FunctionButtonPanel.java

package org.seamcat.presentation.components;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import org.seamcat.presentation.FileDataIO;

public abstract class FunctionButtonPanel extends JPanel
{

    public FunctionButtonPanel()
    {
        fileio = new FileDataIO();
        fileChooser = new JFileChooser();
        JButton btnLoad = new JButton(stringlist.getString("BTN_CAPTION_LOAD"));
        JButton btnSave = new JButton(stringlist.getString("BTN_CAPTION_SAVE"));
        JButton btnClear = new JButton(stringlist.getString("BTN_CAPTION_CLEAR"));
        JButton btnAdd = new JButton(stringlist.getString("BTN_CAPTION_ADD"));
        JButton btnDelete = new JButton(stringlist.getString("BTN_CAPTION_DELETE"));
        JButton btnSym = new JButton(stringlist.getString("BTN_CAPTION_SYM"));
        btnLoad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnLoadActionPerformed();
            }

            final FunctionButtonPanel this$0;

            
            {
                this$0 = FunctionButtonPanel.this;
                super();
            }
        }
);
        btnSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnSaveActionPerformed();
            }

            final FunctionButtonPanel this$0;

            
            {
                this$0 = FunctionButtonPanel.this;
                super();
            }
        }
);
        btnClear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnClearActionPerformed();
            }

            final FunctionButtonPanel this$0;

            
            {
                this$0 = FunctionButtonPanel.this;
                super();
            }
        }
);
        btnAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnAddActionPerformed();
            }

            final FunctionButtonPanel this$0;

            
            {
                this$0 = FunctionButtonPanel.this;
                super();
            }
        }
);
        btnDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnDeleteActionPerformed();
            }

            final FunctionButtonPanel this$0;

            
            {
                this$0 = FunctionButtonPanel.this;
                super();
            }
        }
);
        btnSym.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnSymActionPerformed();
            }

            final FunctionButtonPanel this$0;

            
            {
                this$0 = FunctionButtonPanel.this;
                super();
            }
        }
);
        JPanel stretchPanel = new JPanel(new GridLayout(6, 1));
        stretchPanel.add(btnLoad);
        stretchPanel.add(btnSave);
        stretchPanel.add(btnClear);
        stretchPanel.add(btnAdd);
        stretchPanel.add(btnDelete);
        stretchPanel.add(btnSym);
        add(stretchPanel);
    }

    public abstract void btnLoadActionPerformed();

    public abstract void btnSaveActionPerformed();

    public abstract void btnClearActionPerformed();

    public abstract void btnAddActionPerformed();

    public abstract void btnDeleteActionPerformed();

    public abstract void btnSymActionPerformed();

    protected static final ResourceBundle stringlist;
    protected FileDataIO fileio;
    protected JFileChooser fileChooser;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
