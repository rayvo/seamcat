// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractAddEditDeleteButtonPanel.java

package org.seamcat.presentation.batch;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractAddEditDeleteButtonPanel extends JPanel
{

    public AbstractAddEditDeleteButtonPanel(String title, boolean duplicate)
    {
        addButton = new JButton(STRINGLIST.getString("BTN_CAPTION_ADD"));
        deleteButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DELETE"));
        dublicateButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DUPLICATE"));
        c = new GridBagConstraints();
        setLayout(new GridBagLayout());
        c.fill = 1;
        c.gridwidth = 0;
        add(addButton, c);
        if(duplicate)
            add(dublicateButton, c);
        add(deleteButton, c);
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                addButtonActionPerformed(e);
            }

            final AbstractAddEditDeleteButtonPanel this$0;

            
            {
                this$0 = AbstractAddEditDeleteButtonPanel.this;
                super();
            }
        }
);
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                deleteButtonActionPerformed(e);
            }

            final AbstractAddEditDeleteButtonPanel this$0;

            
            {
                this$0 = AbstractAddEditDeleteButtonPanel.this;
                super();
            }
        }
);
        dublicateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                dublicateButtonActionPerformed(e);
            }

            final AbstractAddEditDeleteButtonPanel this$0;

            
            {
                this$0 = AbstractAddEditDeleteButtonPanel.this;
                super();
            }
        }
);
    }

    protected abstract void addButtonActionPerformed(ActionEvent actionevent);

    protected abstract void deleteButtonActionPerformed(ActionEvent actionevent);

    protected abstract void dublicateButtonActionPerformed(ActionEvent actionevent);

    private static final ResourceBundle STRINGLIST;
    private JButton addButton;
    private JButton deleteButton;
    private JButton dublicateButton;
    protected GridBagConstraints c;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
