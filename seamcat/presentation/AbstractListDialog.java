// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AbstractListDialog.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.seamcat.Seamcat;
import org.seamcat.model.Model;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public abstract class AbstractListDialog extends EscapeDialog
{

    public AbstractListDialog(JDialog _owner)
    {
        super(_owner, true);
        jbtnEditButton = new JButton();
        jbtnDuplicateButton = new JButton();
        jbtnDeleteButton = new JButton();
        jbtnLoadButton = new JButton();
        jbtnSaveButton = new JButton();
        jbtnRestoreDefaults = new JButton();
        saveLoadButtons = false;
        owner = _owner;
        init(false, false);
    }

    public AbstractListDialog(Frame owner)
    {
        this(owner, false, false);
    }

    public AbstractListDialog(Frame owner, boolean saveLoad, boolean restoreDefaults)
    {
        super(owner, true);
        jbtnEditButton = new JButton();
        jbtnDuplicateButton = new JButton();
        jbtnDeleteButton = new JButton();
        jbtnLoadButton = new JButton();
        jbtnSaveButton = new JButton();
        jbtnRestoreDefaults = new JButton();
        saveLoadButtons = false;
        init(saveLoad, restoreDefaults);
    }

    private void init(boolean saveLoad, boolean restoreDefaults)
    {
        initListModel();
        jlListDialogList = new JList(listModel);
        JPanel jpListDialogPanel = new JPanel(new GridBagLayout());
        JButton jbtnAddButton = new JButton();
        jbtnEditButton = new JButton();
        jbtnDuplicateButton = new JButton();
        jbtnDeleteButton = new JButton();
        JButton jbtnCloseButton = new JButton();
        JButton jbtnHelpButton = new JButton();
        jlListDialogList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e)
            {
                updateButtonStatus();
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jlListDialogList.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e)
            {
                if(e.getClickCount() > 1)
                    btnEditButtonActionPerformed(new ActionEvent(e.getSource(), -1, ""));
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnAddButton.setText(STRINGLIST.getString("BTN_CAPTION_ADD"));
        jbtnAddButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                btnAddButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnEditButton.setText(STRINGLIST.getString("BTN_CAPTION_EDIT"));
        jbtnEditButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                btnEditButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnDuplicateButton.setText(STRINGLIST.getString("BTN_CAPTION_DUPLICATE"));
        jbtnDuplicateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                btnDuplicateButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnDeleteButton.setText(STRINGLIST.getString("BTN_CAPTION_DELETE"));
        jbtnDeleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                btnDeleteButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnCloseButton.setText(STRINGLIST.getString("BTN_CAPTION_CLOSE"));
        jbtnCloseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                jbtnCloseButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnHelpButton.setText(STRINGLIST.getString("BTN_CAPTION_HELP"));
        Seamcat.helpBroker.enableHelpOnButton(jbtnHelpButton, helplist.getString(getClass().getName()), null);
        jbtnHelpButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                btnHelpButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnLoadButton.setText(STRINGLIST.getString("BTN_CAPTION_LOAD"));
        jbtnLoadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                btnLoadButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnSaveButton.setText(STRINGLIST.getString("BTN_CAPTION_SAVE"));
        jbtnSaveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                btnSaveButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        jbtnRestoreDefaults.setText(STRINGLIST.getString("BTN_CAPTION_RESTORE"));
        jbtnRestoreDefaults.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                btnRestoreDefaultsButtonActionPerformed(evt);
            }

            final AbstractListDialog this$0;

            
            {
                this$0 = AbstractListDialog.this;
                super();
            }
        }
);
        int y = 0;
        jpListDialogPanel.add(jbtnAddButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jpListDialogPanel.add(jbtnEditButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jpListDialogPanel.add(jbtnDuplicateButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jpListDialogPanel.add(jbtnDeleteButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        if(saveLoad)
        {
            jpListDialogPanel.add(jbtnLoadButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
            jpListDialogPanel.add(jbtnSaveButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        }
        if(restoreDefaults)
            jpListDialogPanel.add(jbtnRestoreDefaults, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jpListDialogPanel.add(jbtnCloseButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jpListDialogPanel.add(Box.createVerticalGlue(), new GridBagConstraints(1, y++, 1, 1, 1.0D, 100D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        jpListDialogPanel.add(jbtnHelpButton, new GridBagConstraints(1, y++, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        jpListDialogPanel.add(new JScrollPane(jlListDialogList), new GridBagConstraints(0, 0, 1, y, 2D, 2D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        getContentPane().add(jpListDialogPanel, "Center");
        pack();
        setSize(400, 400);
        setLocationRelativeTo(owner);
        Seamcat.helpBroker.enableHelpKey(getRootPane(), helplist.getString(getClass().getName()), null);
    }

    private boolean existsName(String name)
    {
        boolean exists = false;
        int x = 0;
        int size = jlListDialogList.getModel().getSize();
        do
        {
            if(x >= size)
                break;
            if(getUniqueNamePart(x).equalsIgnoreCase(name))
            {
                exists = true;
                break;
            }
            x++;
        } while(true);
        return exists;
    }

    protected String getDuplicatedName(String original)
    {
        String newName = null;
        if(existsName(original))
        {
            for(int seqnbr = 1; newName == null; seqnbr++)
            {
                newName = (new StringBuilder()).append(original).append(".").append(seqnbr).toString();
                int x = 0;
                int size = jlListDialogList.getModel().getSize();
                do
                {
                    if(x >= size)
                        break;
                    if(getUniqueNamePart(x).equalsIgnoreCase(newName))
                    {
                        newName = null;
                        break;
                    }
                    x++;
                } while(true);
            }

        } else
        {
            newName = original;
        }
        return newName;
    }

    protected String getUniqueNamePart(int elementIndex)
    {
        return jlListDialogList.getModel().getElementAt(elementIndex).toString();
    }

    public void setVisible(boolean show)
    {
        if(show)
        {
            updateButtonStatus();
            if(listModel.getSize() > 0)
                jlListDialogList.setSelectedIndex(0);
        }
        super.setVisible(show);
    }

    protected void updateButtonStatus()
    {
        jbtnEditButton.setEnabled(jlListDialogList.getSelectedValue() != null);
        jbtnDuplicateButton.setEnabled(jlListDialogList.getSelectedValue() != null);
        jbtnDeleteButton.setEnabled(jlListDialogList.getSelectedValue() != null);
    }

    protected abstract void initListModel();

    protected abstract void btnAddButtonActionPerformed(ActionEvent actionevent);

    protected abstract void btnEditButtonActionPerformed(ActionEvent actionevent);

    protected abstract void btnHelpButtonActionPerformed(ActionEvent actionevent);

    protected abstract void btnDuplicateButtonActionPerformed(ActionEvent actionevent);

    protected abstract void btnDeleteButtonActionPerformed(ActionEvent actionevent);

    protected void btnLoadButtonActionPerformed(ActionEvent actionevent)
    {
    }

    protected void btnSaveButtonActionPerformed(ActionEvent actionevent)
    {
    }

    protected void btnRestoreDefaultsButtonActionPerformed(ActionEvent actionevent)
    {
    }

    protected void jbtnCloseButtonActionPerformed(ActionEvent e)
    {
        setVisible(false);
    }

    protected Component getOwnerFrame()
    {
        return owner;
    }

    protected void saveModel()
    {
        try
        {
            Model.getInstance().persist();
        }
        catch(Exception ex1)
        {
            System.out.println("Failed to save SEAMCAT data!");
        }
    }

    protected static final ResourceBundle STRINGLIST;
    protected static final ResourceBundle helplist;
    protected ListModel listModel;
    protected JList jlListDialogList;
    private Component owner;
    protected JButton jbtnEditButton;
    protected JButton jbtnDuplicateButton;
    protected JButton jbtnDeleteButton;
    protected JButton jbtnLoadButton;
    protected JButton jbtnSaveButton;
    protected JButton jbtnRestoreDefaults;
    protected boolean saveLoadButtons;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }
}
