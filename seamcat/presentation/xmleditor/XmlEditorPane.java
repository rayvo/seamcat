// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlEditorPane.java

package org.seamcat.presentation.xmleditor;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JEditorPane;
import javax.swing.text.Document;

// Referenced classes of package org.seamcat.presentation.xmleditor:
//            XmlEditorKit, XmlDocument

public class XmlEditorPane extends JEditorPane
{

    public XmlEditorPane()
    {
        tagCompletion = true;
        spaces = 4;
        kit = null;
        kit = new XmlEditorKit(this);
        setEditorKitForContentType(kit.getContentType(), kit);
        setBackground(Color.white);
        setContentType(kit.getContentType());
        super.setFont(kit.getFont());
        setEditable(true);
    }

    public boolean isTagCompletion()
    {
        return tagCompletion;
    }

    public void setFont(Font font)
    {
        if(kit != null)
            kit.setFont(font);
        super.setFont(font);
    }

    public void setTabSize(int size)
    {
        defaultTabSize = size;
        Document doc = getDocument();
        if(doc != null)
        {
            int old = getTabSize();
            doc.putProperty("tabSize", new Integer(size));
            firePropertyChange("tabSize", old, size);
        }
    }

    public int getTabSize()
    {
        int size = defaultTabSize;
        Document doc = getDocument();
        if(doc != null)
        {
            Integer i = (Integer)doc.getProperty("tabSize");
            if(i != null)
                size = i.intValue();
        }
        return size;
    }

    public void setText(String text)
    {
        XmlDocument doc = (XmlDocument)getDocument();
        doc.setLoading(true);
        super.setText(text);
        doc.setLoading(false);
    }

    public int getSpaces()
    {
        return spaces;
    }

    private boolean tagCompletion;
    private static int defaultTabSize = 4;
    private int spaces;
    private XmlEditorKit kit;

}
