// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ReceiverListDialog.java

package org.seamcat.presentation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.seamcat.model.*;

// Referenced classes of package org.seamcat.presentation:
//            AbstractListDialog, ReceiverAddEditComponent

public class ReceiverListDialog extends AbstractListDialog
{

    public ReceiverListDialog(Frame owner)
    {
        super(owner);
        setTitle("Receiver library");
        receiverEditor = new ReceiverAddEditComponent(this);
    }

    protected void initListModel()
    {
        listModel = Model.getInstance().getLibrary().getReceivers().createComboBoxModel();
    }

    protected void btnAddButtonActionPerformed(ActionEvent e)
    {
        Receiver r = new Receiver();
        if(receiverEditor.show(r))
        {
            r.setReference(getDuplicatedName(r.getReference()));
            Model.getInstance().getLibrary().getReceivers().add(r);
        }
    }

    protected void btnEditButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            receiverEditor.show((Receiver)jlListDialogList.getSelectedValue());
            saveModel();
        }
    }

    protected void btnHelpButtonActionPerformed(ActionEvent actionevent)
    {
    }

    protected void btnDuplicateButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            Receiver receiver = new Receiver((Receiver)jlListDialogList.getSelectedValue());
            receiver.setReference(getDuplicatedName(receiver.getReference()));
            Model.getInstance().getLibrary().getReceivers().add(receiver);
            saveModel();
        }
    }

    protected void btnDeleteButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            if(Model.getInstance().getLibrary().getReceivers().size() == 1)
                JOptionPane.showMessageDialog(getOwnerFrame(), "Cannot delete last entry", "Error", 0);
            else
                Model.getInstance().getLibrary().getReceivers().remove(jlListDialogList.getSelectedValue());
            saveModel();
        }
    }

    private ReceiverAddEditComponent receiverEditor;
}
