// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlEditorKit.java

package org.seamcat.presentation.xmleditor;

import java.awt.Font;
import javax.swing.text.*;

// Referenced classes of package org.seamcat.presentation.xmleditor:
//            XmlContext, XmlDocument, XmlEditorPane, XmlView

public class XmlEditorKit extends DefaultEditorKit
{
    class XmlViewFactory
        implements ViewFactory
    {

        public View create(Element elem)
        {
            return new XmlView(context, elem);
        }

        final XmlEditorKit this$0;

        XmlViewFactory()
        {
            this$0 = XmlEditorKit.this;
            super();
        }
    }


    public XmlEditorKit(XmlEditorPane editor)
    {
        context = null;
        factory = null;
        this.editor = null;
        this.editor = editor;
        factory = new XmlViewFactory();
        context = new XmlContext();
    }

    public String getContentType()
    {
        return "text/xml";
    }

    public void setFont(Font font)
    {
        font = font;
        context.setFont(font);
    }

    public Font getFont()
    {
        return font;
    }

    public Document createDefaultDocument()
    {
        return new XmlDocument(editor);
    }

    public final ViewFactory getViewFactory()
    {
        return factory;
    }

    private static Font font = null;
    private XmlContext context;
    private ViewFactory factory;
    private XmlEditorPane editor;


}
