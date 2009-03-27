// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PluginListDialog.java

package org.seamcat.presentation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.seamcat.model.*;
import org.seamcat.model.propagation.PluginModelWrapper;

// Referenced classes of package org.seamcat.presentation:
//            AbstractListDialog, PluginAddEditComponent

public class PluginListDialog extends AbstractListDialog
{

    public PluginListDialog(Frame owner)
    {
        super(owner);
        setTitle(STRINGLIST.getString("LIBRARY_PLUGIN_ADDEDIT_CAPTION"));
        addEditDialog = new PluginAddEditComponent(this);
    }

    protected void initListModel()
    {
        listModel = Model.getInstance().getLibrary().getPlugins().createComboBoxModel();
    }

    protected void btnAddButtonActionPerformed(ActionEvent e)
    {
        PluginModelWrapper plugin = new PluginModelWrapper();
        if(addEditDialog.show(plugin))
        {
            addEditDialog.updateModel(plugin);
            saveModel();
            plugin.setReference(getDuplicatedName(plugin.getReference()));
            try
            {
                Model.getInstance().getLibrary().getPlugins().add(plugin);
            }
            catch(Exception ex)
            {
                JOptionPane.showMessageDialog(getOwnerFrame(), STRINGLIST.getString("LIBRARY_PLUGIN_ADDEDIT_FAILEDVALIDATE"), "Error", 0);
            }
            if(!plugin.verify())
                JOptionPane.showMessageDialog(getOwnerFrame(), STRINGLIST.getString("LIBRARY_PLUGIN_ADDEDIT_FAILEDVALIDATE"), "Error", 0);
        }
    }

    protected void btnEditButtonActionPerformed(ActionEvent e)
    {
        PluginModelWrapper plugin = (PluginModelWrapper)jlListDialogList.getSelectedValue();
        if(plugin != null && addEditDialog.show(plugin))
        {
            addEditDialog.updateModel(plugin);
            saveModel();
            if(!plugin.verify())
                JOptionPane.showMessageDialog(getOwnerFrame(), STRINGLIST.getString("LIBRARY_PLUGIN_ADDEDIT_FAILEDVALIDATE"), "Error", 0);
        }
    }

    protected void btnHelpButtonActionPerformed(ActionEvent actionevent)
    {
    }

    protected void btnDuplicateButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
            try
            {
                PluginModelWrapper plugin = null;
                try
                {
                    plugin = new PluginModelWrapper((PluginModelWrapper)jlListDialogList.getSelectedValue());
                }
                catch(Exception ex)
                {
                    LOG.debug("Could not duplicate item because source has illegal classname");
                }
                plugin.setReference(getDuplicatedName(plugin.getReference()));
                Model.getInstance().getLibrary().getPlugins().add(plugin);
                saveModel();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
    }

    protected void btnDeleteButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            Model.getInstance().getLibrary().getPlugins().remove(jlListDialogList.getSelectedValue());
            saveModel();
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/PluginListDialog);
    private PluginAddEditComponent addEditDialog;

}
