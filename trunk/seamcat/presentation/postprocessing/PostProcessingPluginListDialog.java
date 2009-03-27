// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PostProcessingPluginListDialog.java

package org.seamcat.presentation.postprocessing;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import javax.swing.JList;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.seamcat.model.*;
import org.seamcat.postprocessing.PostProcessingPluginWrapper;
import org.seamcat.presentation.AbstractListDialog;

// Referenced classes of package org.seamcat.presentation.postprocessing:
//            PostProcessingPluginAddEditDialog

public class PostProcessingPluginListDialog extends AbstractListDialog
{

    public PostProcessingPluginListDialog(Frame _owner)
    {
        super(_owner);
        setTitle(STRINGLIST.getString("LIBRARY_PPP_LIST_WINDOWTITLE"));
        addEditDialog = new PostProcessingPluginAddEditDialog(this);
    }

    protected void initListModel()
    {
        listModel = Model.getInstance().getLibrary().getPostProcessingPlugins().createComboBoxModel();
    }

    protected void btnAddButtonActionPerformed(ActionEvent e)
    {
        PostProcessingPluginWrapper plugin = new PostProcessingPluginWrapper();
        if(addEditDialog.show(plugin))
        {
            addEditDialog.updateModel(plugin);
            saveModel();
            plugin.setReference(getDuplicatedName(plugin.getReference()));
            Model.getInstance().getLibrary().getPostProcessingPlugins().add(plugin);
            if(!plugin.verify())
                JOptionPane.showMessageDialog(getOwnerFrame(), STRINGLIST.getString("LIBRARY_PLUGIN_ADDEDIT_FAILEDVALIDATE"), "Error", 0);
        }
    }

    protected void btnEditButtonActionPerformed(ActionEvent e)
    {
        PostProcessingPluginWrapper plugin = (PostProcessingPluginWrapper)jlListDialogList.getSelectedValue();
        if(plugin != null && addEditDialog.show(plugin))
        {
            addEditDialog.updateModel(plugin);
            saveModel();
            if(!plugin.verify())
                JOptionPane.showMessageDialog(this, STRINGLIST.getString("LIBRARY_PLUGIN_ADDEDIT_FAILEDVALIDATE"), "Error", 0);
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
                PostProcessingPluginWrapper plugin = null;
                try
                {
                    plugin = new PostProcessingPluginWrapper((PostProcessingPluginWrapper)jlListDialogList.getSelectedValue());
                }
                catch(Exception ex)
                {
                    LOG.debug("Could not duplicate item");
                }
                plugin.setReference(getDuplicatedName(plugin.getReference()));
                Model.getInstance().getLibrary().getPostProcessingPlugins().add(plugin);
                saveModel();
            }
            catch(Exception ex)
            {
                LOG.error("Caught exception while dublicating", ex);
            }
    }

    protected void btnDeleteButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            Model.getInstance().getLibrary().getPostProcessingPlugins().remove(jlListDialogList.getSelectedValue());
            saveModel();
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/postprocessing/PostProcessingPluginListDialog);
    private PostProcessingPluginAddEditDialog addEditDialog;

}
