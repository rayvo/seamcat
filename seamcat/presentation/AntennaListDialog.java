// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AntennaListDialog.java

package org.seamcat.presentation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.seamcat.model.*;

// Referenced classes of package org.seamcat.presentation:
//            AbstractListDialog, AntennaAddEditComponent

public class AntennaListDialog extends AbstractListDialog
{

    public AntennaListDialog(Frame owner)
    {
        super(owner);
        setTitle(STRINGLIST.getString("LIBRARY_ANTENNA_LIST_TITLE"));
        addEditDialog = new AntennaAddEditComponent(this);
    }

    protected void initListModel()
    {
        listModel = Model.getInstance().getLibrary().getAntennas().createComboBoxModel();
    }

    protected void btnAddButtonActionPerformed(ActionEvent e)
    {
        Antenna antenna = new Antenna();
        if(addEditDialog.show(antenna))
        {
            addEditDialog.updateModel(antenna);
            antenna.setReference(getDuplicatedName(antenna.getReference()));
            Model.getInstance().getLibrary().getAntennas().add(antenna);
        }
    }

    protected void btnEditButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null && addEditDialog.show((Antenna)jlListDialogList.getSelectedValue()))
        {
            addEditDialog.updateModel((Antenna)jlListDialogList.getSelectedValue());
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
            Antenna antenna = new Antenna((Antenna)jlListDialogList.getSelectedValue());
            antenna.setReference(getDuplicatedName(antenna.getReference()));
            Model.getInstance().getLibrary().getAntennas().add(antenna);
            saveModel();
        }
    }

    protected void btnDeleteButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            if(Model.getInstance().getLibrary().getAntennas().size() == 1)
                JOptionPane.showMessageDialog(getOwnerFrame(), STRINGLIST.getString("LIBRARY_LIST_ERR_LASTENTRY"), "Error", 0);
            else
                Model.getInstance().getLibrary().getAntennas().remove(jlListDialogList.getSelectedValue());
            saveModel();
        }
    }

    private AntennaAddEditComponent addEditDialog;
}
