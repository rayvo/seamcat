// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ReportViewerDialog.java

package org.seamcat.presentation.batch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import org.apache.log4j.Logger;
import org.seamcat.presentation.MainWindow;
import org.seamcat.presentation.xmleditor.XmlEditorPane;

public class ReportViewerDialog extends JDialog
{

    public ReportViewerDialog(JDialog parent, String title)
    {
        super(parent);
        editor = new XmlEditorPane();
        editor.setEditable(false);
        ok = new JButton(STRINGLIST.getString("BTN_CAPTION_CLOSE"));
        save = new JButton(STRINGLIST.getString("BTN_CAPTION_SAVE"));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(editor), "Center");
        JPanel okP = new JPanel();
        okP.add(ok);
        okP.add(save);
        getContentPane().add(okP, "South");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }

            final ReportViewerDialog this$0;

            
            {
                this$0 = ReportViewerDialog.this;
                super();
            }
        }
);
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    int option = MainWindow.FILECHOOSER.showSaveDialog(ReportViewerDialog.this);
                    if(option == 0)
                    {
                        setCursor(Cursor.getPredefinedCursor(3));
                        java.io.File out = MainWindow.FILECHOOSER.getSelectedFile();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)));
                        writer.write(editor.getText());
                        writer.close();
                        setCursor(Cursor.getPredefinedCursor(0));
                    }
                }
                catch(Exception ex)
                {
                    ReportViewerDialog.LOG.error("Error saving report", ex);
                }
            }

            final ReportViewerDialog this$0;

            
            {
                this$0 = ReportViewerDialog.this;
                super();
            }
        }
);
        setModal(true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setTitle(title);
    }

    public void showContent(String report, String contentType)
    {
        try
        {
            for(int start = report.toLowerCase().indexOf("<meta"); start > -1; start = report.toLowerCase().indexOf("<meta"))
                report = (new StringBuilder()).append(report.substring(0, start)).append(report.substring(report.indexOf(">", start) + 1)).toString();

        }
        catch(Exception e) { }
        try
        {
            editor.setContentType(contentType);
            editor.read(new StringReader(report), contentType.equalsIgnoreCase("text/html") ? ((Object) (new HTMLDocument())) : null);
        }
        catch(Exception ex)
        {
            LOG.error("Error showing report", ex);
        }
        setVisible(true);
        editor.setCaretPosition(0);
    }

    private static final ResourceBundle STRINGLIST;
    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/batch/ReportViewerDialog);
    private JEditorPane editor;
    private JButton ok;
    private JButton save;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }


}
