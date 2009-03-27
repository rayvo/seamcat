// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferingLinkListDialog.java

package org.seamcat.presentation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.seamcat.model.Components;
import org.seamcat.model.Workspace;
import org.seamcat.model.core.InterferenceLink;

// Referenced classes of package org.seamcat.presentation:
//            AbstractListDialog, NewDialogInterferingLink, EscapeDialog

public class InterferingLinkListDialog extends AbstractListDialog
{

    public InterferingLinkListDialog(JDialog owner)
    {
        super(owner);
        init();
    }

    public InterferingLinkListDialog(Frame owner)
    {
        super(owner);
        init();
    }

    private void init()
    {
        setTitle("Interfering Links");
        addEditDialog = new NewDialogInterferingLink(this);
    }

    protected void initListModel()
    {
        listModel = new DefaultListModel();
    }

    public void setSelectedWorkspace(Workspace workspace)
    {
        this.workspace = workspace;
        if(workspace != null)
            listModel = workspace.getInterferenceLinks().createComboBoxModel();
        else
            listModel = new DefaultListModel();
        jlListDialogList.setModel(listModel);
    }

    protected void btnAddButtonActionPerformed(ActionEvent e)
    {
        InterferenceLink i = new InterferenceLink(getDuplicatedName("New interfering link"), workspace.getVictimSystemLink());
        boolean canBeCoLocated = workspace.getInterferenceLinks().size() > 0;
        if(addEditDialog.show(i, canBeCoLocated, workspace))
        {
            addEditDialog.updateModel(i);
            i.setReference(getDuplicatedName(i.getReference()));
            workspace.addInterferingSystemLink(i);
        }
    }

    protected void btnDeleteButtonActionPerformed(ActionEvent e)
    {
        Object v[] = jlListDialogList.getSelectedValues();
        if(v != null && v.length > 0)
        {
            int res = JOptionPane.showConfirmDialog(this, STRINGLIST.getString("INTERFERING_LINK_CONFIRM_DELETE"));
            if(res == 0)
            {
                for(int i = 0; i < v.length; i++)
                    workspace.removeInterferingSystemLink((InterferenceLink)v[i]);

            }
        }
    }

    protected void btnDuplicateButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            InterferenceLink srcObj = (InterferenceLink)jlListDialogList.getSelectedValue();
            InterferenceLink newObj = new InterferenceLink(getDuplicatedName(srcObj.getReference()), srcObj);
            workspace.addInterferingSystemLink(newObj);
        }
    }

    protected void btnEditButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            InterferenceLink i = (InterferenceLink)jlListDialogList.getSelectedValue();
            boolean canBeCoLocated = jlListDialogList.getSelectedIndex() > 0;
            if(addEditDialog.show(i, canBeCoLocated, workspace))
                addEditDialog.updateModel(i);
        }
    }

    protected void btnHelpButtonActionPerformed(ActionEvent actionevent)
    {
    }

    public static void setNextFocus(EscapeDialog nextFocus)
    {
        nextFocus = nextFocus;
        firstFocus = true;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/InterferingLinkListDialog);
    private static final ResourceBundle helplist;
    private static final ResourceBundle STRINGLIST;
    private static EscapeDialog nextFocus;
    private static boolean firstFocus;
    private NewDialogInterferingLink addEditDialog;
    private Workspace workspace;

    static 
    {
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
