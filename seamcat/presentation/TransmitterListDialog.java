// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TransmitterListDialog.java

package org.seamcat.presentation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.seamcat.model.*;

// Referenced classes of package org.seamcat.presentation:
//            AbstractListDialog, TransmitterAddEditComponent

public class TransmitterListDialog extends AbstractListDialog
{

    public TransmitterListDialog(Frame owner)
    {
        super(owner);
        setTitle("Transmitter library");
        transmitterEditor = new TransmitterAddEditComponent(this);
    }

    protected void initListModel()
    {
        listModel = Model.getInstance().getLibrary().getTransmitters().createComboBoxModel();
    }

    protected void btnAddButtonActionPerformed(ActionEvent e)
    {
        Transmitter t = new Transmitter();
        if(transmitterEditor.show(t))
        {
            t.setReference(getDuplicatedName(t.getReference()));
            Model.getInstance().getLibrary().getTransmitters().add(t);
        }
    }

    protected void btnEditButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            transmitterEditor.show((Transmitter)jlListDialogList.getSelectedValue());
            saveModel();
            jlListDialogList.repaint();
        }
    }

    protected void btnHelpButtonActionPerformed(ActionEvent actionevent)
    {
    }

    protected void btnDuplicateButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedIndex() >= 0)
        {
            Transmitter transmitter = new Transmitter((Transmitter)jlListDialogList.getSelectedValue());
            transmitter.setReference(getDuplicatedName(transmitter.getReference()));
            Model.getInstance().getLibrary().getTransmitters().add(transmitter);
            saveModel();
        }
    }

    protected void btnDeleteButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            if(Model.getInstance().getLibrary().getTransmitters().size() == 1)
                JOptionPane.showMessageDialog(getOwnerFrame(), "Cannot delete last entry", "Error", 0);
            else
                Model.getInstance().getLibrary().getTransmitters().remove(jlListDialogList.getSelectedValue());
            saveModel();
        }
    }

    private TransmitterAddEditComponent transmitterEditor;
}
