// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NavigateButtonPanel.java

package org.seamcat.presentation.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.seamcat.Seamcat;

public abstract class NavigateButtonPanel extends JPanel
{

    public NavigateButtonPanel()
    {
        btnOk = new JButton(stringlist.getString("BTN_CAPTION_OK"));
        btnCancel = new JButton(stringlist.getString("BTN_CAPTION_CANCEL"));
        btnHelp = new JButton(stringlist.getString("BTN_CAPTION_HELP"));
        try
        {
            Seamcat.helpBroker.enableHelpOnButton(btnHelp, helplist.getString(getClass().getName()), null);
        }
        catch(Exception ex) { }
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnOkActionPerformed();
            }

            final NavigateButtonPanel this$0;

            
            {
                this$0 = NavigateButtonPanel.this;
                super();
            }
        }
);
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnCancelActionPerformed();
            }

            final NavigateButtonPanel this$0;

            
            {
                this$0 = NavigateButtonPanel.this;
                super();
            }
        }
);
        btnHelp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnHelpActionPerformed();
            }

            final NavigateButtonPanel this$0;

            
            {
                this$0 = NavigateButtonPanel.this;
                super();
            }
        }
);
        try
        {
            Seamcat.helpBroker.enableHelpOnButton(btnHelp, helplist.getString(getHelpClassName()), null);
        }
        catch(Exception e) { }
        add(btnOk);
        add(btnCancel);
        add(btnHelp);
    }

    protected String getHelpClassName()
    {
        return getClass().getName();
    }

    public abstract void btnOkActionPerformed();

    public abstract void btnCancelActionPerformed();

    public void btnHelpActionPerformed()
    {
    }

    public void setBtnOkFocus()
    {
        btnOk.grabFocus();
    }

    public void setBtnOkText(String text)
    {
        btnOk.setText(text);
    }

    protected static final ResourceBundle stringlist;
    protected static final ResourceBundle helplist;
    protected final JButton btnOk;
    protected final JButton btnCancel;
    protected final JButton btnHelp;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }
}
