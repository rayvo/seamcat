// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XsltExample.java

package org.seamcat.presentation.xmleditor;

import java.awt.Container;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.io.StringReader;
import java.io.StringWriter;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.transform.TransformerException;

// Referenced classes of package org.seamcat.presentation.xmleditor:
//            XslTransformer, DocumentPane, ExtensionFileFilter

public class XsltExample extends JFrame
    implements ChangeListener
{
    class ExitAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent event)
        {
            System.exit(0);
        }

        final XsltExample this$0;

        public ExitAction()
        {
            this$0 = XsltExample.this;
            super("Exit");
        }
    }

    class SaveAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent event)
        {
            updateDocument(1);
        }

        final XsltExample this$0;

        public SaveAction()
        {
            this$0 = XsltExample.this;
            super("Save");
            setEnabled(false);
        }
    }

    class OpenAction extends AbstractAction
    {

        public void actionPerformed(ActionEvent event)
        {
            updateDocument(0);
        }

        final XsltExample this$0;

        public OpenAction()
        {
            this$0 = XsltExample.this;
            super("Open ...");
        }
    }


    public XsltExample()
    {
        super("XSLT Example");
        transformer = new XslTransformer();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex) { }
        Container content = getContentPane();
        content.setBackground(SystemColor.control);
        JMenuBar menubar = new JMenuBar();
        openAction = new OpenAction();
        saveAction = new SaveAction();
        exitAction = new ExitAction();
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(openAction);
        fileMenu.add(saveAction);
        fileMenu.add(exitAction);
        menubar.add(fileMenu);
        setJMenuBar(menubar);
        tabbedPane = new JTabbedPane();
        documents = new DocumentPane[3];
        for(int i = 0; i < 3; i++)
        {
            documents[i] = new DocumentPane();
            JPanel panel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(documents[i]);
            panel.add(scrollPane);
            tabbedPane.add(tabTitles[i], scrollPane);
        }

        documents[2].setContentType("text/html");
        documents[2].loadFile("XSLT-Instructions.html");
        documents[2].setEditable(false);
        tabbedPane.addChangeListener(this);
        content.add(tabbedPane, "Center");
        setDefaultCloseOperation(3);
        setSize(450, 350);
        setVisible(true);
    }

    public void stateChanged(ChangeEvent event)
    {
        int index = tabbedPane.getSelectedIndex();
        switch(index)
        {
        case 2: // '\002'
            if(documents[0].isLoaded() && documents[1].isLoaded())
                doTransform();
            // fall through

        case 0: // '\0'
        case 1: // '\001'
            updateMenuAndTitle(index);
            // fall through

        default:
            return;
        }
    }

    private void doTransform()
    {
        StringWriter strWriter = new StringWriter();
        try
        {
            java.io.Reader xmlInput = new StringReader(documents[0].getText());
            java.io.Reader xslInput = new StringReader(documents[1].getText());
            transformer = new XslTransformer();
            transformer.process(xmlInput, xslInput, strWriter);
        }
        catch(TransformerException te)
        {
            JOptionPane.showMessageDialog(this, (new StringBuilder()).append("Error: ").append(te.getMessage()).toString());
        }
        documents[2].setText(strWriter.toString());
    }

    private void updateMenuAndTitle(int index)
    {
        if(index > -1 && index < documents.length)
        {
            saveAction.setEnabled(documents[index].isLoaded());
            openAction.setEnabled(documents[index].isEditable());
            String title = "XSLT Example";
            String filename = documents[index].getFilename();
            if(filename.length() > 0)
                title = (new StringBuilder()).append(title).append(" - [").append(filename).append("]").toString();
            setTitle(title);
        }
    }

    private void updateDocument(int mode)
    {
        int index = tabbedPane.getSelectedIndex();
        String description = (new StringBuilder()).append(tabTitles[index]).append(" Files").toString();
        String filename = ExtensionFileFilter.getFileName(".", description, extensions[index], mode);
        if(filename != null)
        {
            if(mode == 1)
                documents[index].saveFile(filename);
            else
                documents[index].loadFile(filename);
            updateMenuAndTitle(index);
        }
    }

    public static void main(String args[])
    {
        new XsltExample();
    }

    private static final int XML = 0;
    private static final int XSL = 1;
    private static final int XSLT = 2;
    private static final String DEFAULT_TITLE = "XSLT Example";
    private static final String tabTitles[] = {
        "XML", "XSL", "XSLT"
    };
    private static final String extensions[] = {
        "xml", "xsl", "html"
    };
    private Action openAction;
    private Action saveAction;
    private Action exitAction;
    private JTabbedPane tabbedPane;
    private DocumentPane documents[];
    private XslTransformer transformer;


}
