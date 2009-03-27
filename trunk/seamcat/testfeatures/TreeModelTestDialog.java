// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TreeModelTestDialog.java

package org.seamcat.testfeatures;

import java.awt.Container;
import java.io.IOException;
import javax.swing.*;
import javax.xml.parsers.*;
import org.seamcat.model.Model;
import org.seamcat.model.XmlValidationHandler;
import org.xml.sax.SAXException;

// Referenced classes of package org.seamcat.testfeatures:
//            DocumentTreeModel, NodeCellRenderer

public class TreeModelTestDialog extends JDialog
{

    public TreeModelTestDialog()
        throws SAXException, IOException, ParserConfigurationException
    {
        JFileChooser ch = new JFileChooser();
        ch.showOpenDialog(this);
        java.io.File f = ch.getSelectedFile();
        DocumentBuilder db = Model.getWorkspaceDocumentBuilderFactory().newDocumentBuilder();
        db.setErrorHandler(new XmlValidationHandler(false));
        org.w3c.dom.Document doc = db.parse(f);
        DocumentTreeModel model = new DocumentTreeModel(doc);
        JTree tree = new JTree(model);
        tree.setCellRenderer(new NodeCellRenderer());
        getContentPane().add(new JScrollPane(tree));
        setSize(800, 300);
        setVisible(true);
    }

    public static void main(String args[])
    {
        try
        {
            new TreeModelTestDialog();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
