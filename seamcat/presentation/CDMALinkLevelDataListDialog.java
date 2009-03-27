// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CDMALinkLevelDataListDialog.java

package org.seamcat.presentation;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.seamcat.cdma.CDMALinkLevelData;
import org.seamcat.cdma.presentation.CDMALinkLevelDataEditorDialog;
import org.seamcat.cdma.xml.CDMAXMLUtils;
import org.seamcat.model.*;

// Referenced classes of package org.seamcat.presentation:
//            AbstractListDialog, MainWindow

public class CDMALinkLevelDataListDialog extends AbstractListDialog
{

    public CDMALinkLevelDataListDialog(Frame owner)
    {
        super(owner, true, true);
        setTitle(STRINGLIST.getString("LIBRARY_CDMA_LLD_LIST_WINDOWTITLE"));
        jbtnSaveButton.setText(STRINGLIST.getString("BTN_CAPTION_SAVEALL"));
        jlListDialogList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e)
            {
                updateButtonVisibility();
            }

            final CDMALinkLevelDataListDialog this$0;

            
            {
                this$0 = CDMALinkLevelDataListDialog.this;
                super();
            }
        }
);
        setSize(525, 400);
    }

    private void updateButtonVisibility()
    {
        boolean hasItems = jlListDialogList.getModel().getSize() > 0;
        boolean visible = hasItems && jlListDialogList.getSelectedIndex() >= 0 && jlListDialogList.getSelectedIndex() < jlListDialogList.getModel().getSize();
        jbtnEditButton.setEnabled(visible);
        jbtnDuplicateButton.setEnabled(visible);
        jbtnDeleteButton.setEnabled(visible);
        jbtnSaveButton.setEnabled(hasItems);
    }

    private boolean edit(CDMALinkLevelData data)
    {
        return lldEditor.show(data);
    }

    protected void initListModel()
    {
        listModel = Model.getInstance().getLibrary().getCDMALinkLevelData().createComboBoxModel();
    }

    protected void btnAddButtonActionPerformed(ActionEvent e)
    {
        CDMALinkLevelData newLLD = new CDMALinkLevelData();
        newLLD.setSystem(STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_NEW_SYSTEM"));
        newLLD.setSource(STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_NEW_SOURCE"));
        if(edit(newLLD))
        {
            Model.getInstance().getLibrary().getCDMALinkLevelData().add(newLLD);
            saveModel();
            updateButtonVisibility();
        }
    }

    protected void btnEditButtonActionPerformed(ActionEvent e)
    {
        int index = jlListDialogList.getSelectedIndex();
        if(index >= 0 && index < jlListDialogList.getModel().getSize() && edit((CDMALinkLevelData)jlListDialogList.getSelectedValue()))
            saveModel();
    }

    protected void btnHelpButtonActionPerformed(ActionEvent actionevent)
    {
    }

    protected void btnDuplicateButtonActionPerformed(ActionEvent e)
    {
        int index = jlListDialogList.getSelectedIndex();
        if(index >= 0 && index < jlListDialogList.getModel().getSize())
        {
            CDMALinkLevelData selected = (CDMALinkLevelData)jlListDialogList.getSelectedValue();
            CDMALinkLevelData newlld = new CDMALinkLevelData(selected);
            newlld.setSystem(getDuplicatedName(selected.getSystem()));
            Model.getInstance().getLibrary().getCDMALinkLevelData().add(newlld);
            saveModel();
            jlListDialogList.setSelectedValue(newlld, true);
        }
    }

    protected String getUniqueNamePart(int elementIndex)
    {
        return ((CDMALinkLevelData)jlListDialogList.getModel().getElementAt(elementIndex)).getSystem();
    }

    protected void btnDeleteButtonActionPerformed(ActionEvent e)
    {
        int index = jlListDialogList.getSelectedIndex();
        if(index >= 0 && index < jlListDialogList.getModel().getSize())
        {
            CDMALinkLevelData selected = (CDMALinkLevelData)jlListDialogList.getSelectedValue();
            Model.getInstance().getLibrary().getCDMALinkLevelData().remove(selected);
            saveModel();
            updateButtonVisibility();
        }
    }

    protected void btnLoadButtonActionPerformed(ActionEvent e)
    {
        JFileChooser fc = MainWindow.FILECHOOSER;
        fc.setFileFilter(MainWindow.FILE_FILTER_XML);
        if(fc.showOpenDialog(this) == 0)
        {
            File f = fc.getSelectedFile();
            try
            {
                java.util.List data = CDMAXMLUtils.loadFromXML(f, null);
                Model.getInstance().getLibrary().getCDMALinkLevelData().addAll(data);
            }
            catch(Exception ex)
            {
                LOG.error("An Error occured while loading link level data", ex);
            }
        }
    }

    protected void btnRestoreDefaultsButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getModel().getSize() < 1 || JOptionPane.showConfirmDialog(this, STRINGLIST.getString("CDMA_LIST_CONFIRM_RESTORE"), STRINGLIST.getString("CDMA_LIST_CONFIRM_RESTORE_TITLE"), 0) == 0)
        {
            LOG.debug("Restoring default LLD");
            Model.getInstance().getLibrary().getCDMALinkLevelData().clear();
            Model.getInstance().getLibrary().getCDMALinkLevelData().addAll(Model.getDefaultCDMALinklevelData());
            updateButtonVisibility();
        }
    }

    protected void btnSaveButtonActionPerformed(ActionEvent e)
    {
        JFileChooser fc = MainWindow.FILECHOOSER;
        fc.setFileFilter(MainWindow.FILE_FILTER_XML);
        if(fc.showSaveDialog(this) == 0)
        {
            File f = fc.getSelectedFile();
            if(!f.getName().toUpperCase().endsWith(".XML"))
                f = new File((new StringBuilder()).append(f.getAbsolutePath()).append(".xml").toString());
            if(!f.exists())
                try
                {
                    f.createNewFile();
                }
                catch(IOException e1)
                {
                    LOG.error("An Error Occured", e1);
                }
            try
            {
                CDMAXMLUtils.saveAsXML(Model.getInstance().getLibrary().getCDMALinkLevelData(), f);
            }
            catch(Exception ex)
            {
                LOG.error("An Error occured while saving link level data", ex);
            }
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/CDMALinkLevelDataListDialog);
    private final CDMALinkLevelDataEditorDialog lldEditor = new CDMALinkLevelDataEditorDialog(this);


}
