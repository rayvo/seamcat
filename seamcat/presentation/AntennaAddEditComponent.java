// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AntennaAddEditComponent.java

package org.seamcat.presentation;

import java.awt.Container;
import java.awt.Frame;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.seamcat.Seamcat;
import org.seamcat.model.Antenna;
import org.seamcat.presentation.components.AntennaDefinitionPanel;
import org.seamcat.presentation.components.NavigateButtonPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public class AntennaAddEditComponent extends EscapeDialog
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

        final AntennaAddEditComponent this$0;

        private DialogNavigateButtonPanel()
        {
            this$0 = AntennaAddEditComponent.this;
            super();
        }

    }


    private AntennaAddEditComponent(Frame owner, boolean modal)
    {
        super(owner, modal);
        setTitle(stringlist.getString("LIBRARY_ANTENNA_ADDEDIT_TITLE"));
        antDefPanel = new AntennaDefinitionPanel(this);
        getContentPane().add(antDefPanel, "Center");
        getContentPane().add(new DialogNavigateButtonPanel(), "South");
        setSize(500, 400);
        setLocationRelativeTo(owner);
        try
        {
            Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
        }
        catch(Exception e) { }
    }

    public AntennaAddEditComponent(JFrame owner)
    {
        this(((Frame) (owner)), true);
    }

    public AntennaAddEditComponent(JDialog owner)
    {
        super(owner, true);
        setTitle(stringlist.getString("LIBRARY_ANTENNA_ADDEDIT_TITLE"));
        antDefPanel = new AntennaDefinitionPanel(this);
        getContentPane().add(antDefPanel, "Center");
        getContentPane().add(new DialogNavigateButtonPanel(), "South");
        setSize(500, 400);
        setLocationRelativeTo(owner);
        try
        {
            Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
        }
        catch(Exception e) { }
    }

    public void setVisible()
    {
        throw new IllegalArgumentException("Cannot show() AntennaAddEditComponent - use show(Antenna)");
    }

    public boolean show(Antenna antenna)
    {
        antDefPanel.setAntenna(antenna, false);
        accept = false;
        super.setVisible(true);
        return accept;
    }

    public void updateModel(Antenna a)
    {
        antDefPanel.updateModel(a);
    }

    private static final ResourceBundle stringlist;
    private static final ResourceBundle helplist;
    private AntennaDefinitionPanel antDefPanel;
    public boolean accept;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }
}
