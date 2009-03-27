// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogFunction2Define.java

package org.seamcat.presentation;

import java.awt.Container;
import java.awt.Frame;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.JDialog;
import org.apache.log4j.Logger;
import org.seamcat.Seamcat;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.function.Function2;
import org.seamcat.presentation.components.DiscreteFunction2TableModelAdapter;
import org.seamcat.presentation.components.NavigateButtonPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, DiscreteFunction2Panel, SeamcatTextFieldFormats

public class DialogFunction2Define extends EscapeDialog
{
    private class DialogNavigateButtonPanel extends NavigateButtonPanel
    {

        public void btnOkActionPerformed()
        {
            accept = true;
            setVisible(false);
        }

        public void btnCancelActionPerformed()
        {
            setVisible(false);
        }

        final DialogFunction2Define this$0;

        public DialogNavigateButtonPanel()
        {
            this$0 = DialogFunction2Define.this;
            super();
        }
    }


    public DialogFunction2Define(JDialog parent, boolean modal, boolean thirdColumnEditable)
    {
        this(parent, modal);
        userDefinitionPanel.getTableModel().setThirdColumnIsEditable(thirdColumnEditable);
    }

    public DialogFunction2Define(JDialog parent, boolean modal)
    {
        super(parent, modal);
        userDefinitionPanel = new DiscreteFunction2Panel("User defined function");
        init();
        setLocationRelativeTo(parent);
    }

    public DialogFunction2Define(Frame parent, boolean modal)
    {
        super(parent, modal);
        userDefinitionPanel = new DiscreteFunction2Panel("User defined function");
        init();
        setLocationRelativeTo(parent);
    }

    private void init()
    {
        getContentPane().add(userDefinitionPanel, "Center");
        getContentPane().add(new DialogNavigateButtonPanel(), "South");
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
        pack();
    }

    public Function2 getFunction()
    {
        Function2 f = userDefinitionPanel.getFunctionable();
        return f;
    }

    public void setFunction(Function2 f)
    {
        userDefinitionPanel.setFunctionable((DiscreteFunction2)f);
    }

    public boolean show(String windowtitle)
    {
        return show(((Function2) (new DiscreteFunction2())), windowtitle);
    }

    public boolean show(Function2 function)
    {
        return show(function, "Function definition");
    }

    public boolean show(Function2 function, String windowtitle)
    {
        setFunction(function);
        setTitle(windowtitle);
        accept = false;
        setVisible(true);
        userDefinitionPanel.stopEditing();
        return accept;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/DialogFunction2Define);
    private static final ResourceBundle stringlist;
    private static final ResourceBundle helplist;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private DiscreteFunction2Panel userDefinitionPanel;
    private boolean accept;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }

}
