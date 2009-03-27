// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EscapeDialog.java

package org.seamcat.presentation;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public abstract class EscapeDialog extends JDialog
{
    private class CloseDialogActionListener
        implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            setVisible(false);
        }

        final EscapeDialog this$0;

        private CloseDialogActionListener()
        {
            this$0 = EscapeDialog.this;
            super();
        }

    }


    public EscapeDialog()
    {
    }

    public EscapeDialog(Frame _owner, boolean modal)
    {
        super(_owner, modal);
        owner = _owner;
    }

    public EscapeDialog(Frame _owner)
    {
        super(_owner);
        owner = _owner;
    }

    public EscapeDialog(Frame _owner, String title)
    {
        super(_owner, title);
        owner = _owner;
    }

    public EscapeDialog(Frame _owner, String title, boolean modal)
    {
        super(_owner, title, modal);
        owner = _owner;
    }

    public EscapeDialog(JDialog _owner, String title, boolean modal)
    {
        super(_owner, title, modal);
        owner = _owner;
    }

    public EscapeDialog(JDialog _owner)
    {
        super(_owner);
        owner = _owner;
    }

    public EscapeDialog(JDialog _owner, boolean modal)
    {
        super(_owner, modal);
        owner = _owner;
    }

    protected JRootPane createRootPane()
    {
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(new CloseDialogActionListener(), stroke, 2);
        return rootPane;
    }

    protected Window owner;
    private static JDialog nextFocus = null;

}
