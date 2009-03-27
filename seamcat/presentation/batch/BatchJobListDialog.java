// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchJobListDialog.java

package org.seamcat.presentation.batch;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.seamcat.batch.BatchJobList;
import org.seamcat.model.*;
import org.seamcat.presentation.AbstractListDialog;
import org.seamcat.presentation.MainWindow;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.presentation.batch:
//            BatchJobListEditorDialog

public class BatchJobListDialog extends AbstractListDialog
{

    public BatchJobListDialog(Frame owner)
    {
        super(owner, true, false);
        batchEditor = new BatchJobListEditorDialog(this);
        setTitle("Batch List");
    }

    protected void btnAddButtonActionPerformed(ActionEvent e)
    {
        BatchJobList batch = new BatchJobList();
        String newName = JOptionPane.showInputDialog(STRINGLIST.getString("BATCH_JOB_LIST_INSERT_BATCH_NAME"));
        if(newName != null && !"".equals(newName.trim()))
        {
            batch.setReference(newName);
            Model.getInstance().getLibrary().getBatchjoblists().add(batch);
            batchEditor.show(batch);
            saveModel();
        }
    }

    protected void btnHelpButtonActionPerformed(ActionEvent actionevent)
    {
    }

    protected void btnDeleteButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            if(JOptionPane.showConfirmDialog(null, STRINGLIST.getString("BATCH_JOB_LIST_DELETE_BATCH")) == 0)
                Model.getInstance().getLibrary().getBatchjoblists().remove(jlListDialogList.getSelectedValue());
            saveModel();
        }
    }

    protected void btnDuplicateButtonActionPerformed(ActionEvent e)
    {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = dbf.newDocumentBuilder();
        }
        catch(ParserConfigurationException e1)
        {
            LOG.error("Error creating document", e1);
        }
        doc = db.newDocument();
        BatchJobList bjl = (BatchJobList)jlListDialogList.getSelectedValue();
        BatchJobList newbjl = new BatchJobList(bjl.toElement(doc));
        String newName = JOptionPane.showInputDialog(STRINGLIST.getString("BATCH_JOB_LIST_INSERT_BATCH_NAME"), bjl.getReference());
        if(newName != null && !"".equals(newName.trim()) && !bjl.getReference().equals(newName))
        {
            newbjl.setReference(newName);
            Model.getInstance().getLibrary().getBatchjoblists().add(newbjl);
        }
        saveModel();
    }

    protected void btnEditButtonActionPerformed(ActionEvent e)
    {
        if(jlListDialogList.getSelectedValue() != null)
        {
            batchEditor.show((BatchJobList)jlListDialogList.getSelectedValue());
            saveModel();
        }
    }

    protected void initListModel()
    {
        listModel = Model.getInstance().getLibrary().getBatchjoblists().createComboBoxModel();
    }

    protected void btnLoadButtonActionPerformed(ActionEvent e)
    {
        try
        {
            JFileChooser fc = MainWindow.FILECHOOSER;
            fc.setFileFilter(MainWindow.FILE_FILTER_BATCH);
            if(fc.showOpenDialog(this) == 0)
            {
                File file = fc.getSelectedFile();
                BatchJobList bj = null;
                try
                {
                    DocumentBuilder db = Model.getSeamcatDocumentBuilderFactory().newDocumentBuilder();
                    db.setErrorHandler(new XmlValidationHandler(false));
                    Document doc = db.parse(file);
                    bj = new BatchJobList((Element)doc.getElementsByTagName("BatchJobList").item(0));
                }
                catch(Exception ex)
                {
                    int response = JOptionPane.showConfirmDialog(this, STRINGLIST.getString("OPEN_BATCH_ERROR"), STRINGLIST.getString("OPEN_BATCH_ERROR_TITLE"), 0, 0);
                    if(response == 0)
                        try
                        {
                            setCursor(Cursor.getPredefinedCursor(3));
                            File temp = Model.removeLineBreaks(fc.getSelectedFile());
                            DocumentBuilder db = Model.getSeamcatDocumentBuilderFactory().newDocumentBuilder();
                            db.setErrorHandler(new XmlValidationHandler(false));
                            Document doc = db.parse(temp);
                            bj = new BatchJobList((Element)doc.getElementsByTagName("BatchJobList").item(0));
                            setCursor(Cursor.getDefaultCursor());
                            JOptionPane.showMessageDialog(this, STRINGLIST.getString("OPEN_BATCH_BROKEN_OK"), STRINGLIST.getString("OPEN_BATCH_BROKEN_OK_TITLE"), 1);
                        }
                        catch(Exception ex2)
                        {
                            JOptionPane.showMessageDialog(this, STRINGLIST.getString("OPEN_BATCH_BROKEN_ERROR"), STRINGLIST.getString("OPEN_BATCH_ERROR_TITLE"), 0);
                        }
                }
                if(bj != null)
                {
                    Model.getInstance().getLibrary().getBatchjoblists().add(bj);
                    saveModel();
                    batchEditor.show(bj);
                }
            }
        }
        catch(Exception ex)
        {
            LOG.error("An Error occured", ex);
        }
    }

    protected void btnSaveButtonActionPerformed(ActionEvent e)
    {
        try
        {
            if(jlListDialogList.getSelectedValue() != null)
            {
                BatchJobList bj = (BatchJobList)jlListDialogList.getSelectedValue();
                JFileChooser fc = MainWindow.FILECHOOSER;
                fc.setFileFilter(MainWindow.FILE_FILTER_BATCH);
                if(fc.showSaveDialog(this) == 0)
                {
                    File file = fc.getSelectedFile();
                    if(!file.getName().toLowerCase().endsWith(".sbj"))
                        file = new File(file.getParentFile(), (new StringBuilder()).append(file.getName()).append(".").append("sbj").toString());
                    DocumentBuilder db = Model.getSeamcatDocumentBuilderFactory().newDocumentBuilder();
                    Document doc = db.newDocument();
                    Element element = bj.toElement(doc);
                    doc.appendChild(element);
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new BufferedOutputStream(new FileOutputStream(file)));
                    TransformerFactory transFactory = TransformerFactory.newInstance();
                    Transformer transformer = transFactory.newTransformer();
                    transformer.transform(source, result);
                    result.getOutputStream().close();
                }
            }
        }
        catch(Exception ex)
        {
            LOG.error("An Error occured", ex);
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/batch/BatchJobListDialog);
    private BatchJobListEditorDialog batchEditor;

}
