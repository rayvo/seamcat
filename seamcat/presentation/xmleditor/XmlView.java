// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlView.java

package org.seamcat.presentation.xmleditor;

import java.awt.*;
import javax.swing.text.*;

// Referenced classes of package org.seamcat.presentation.xmleditor:
//            XmlDocument, XmlScanner, XmlContext

public class XmlView extends PlainView
{

    public XmlView(XmlContext context, Element elem)
    {
        super(elem);
        this.context = null;
        this.context = context;
        XmlDocument doc = (XmlDocument)getDocument();
        try
        {
            lexer = new XmlScanner(doc);
        }
        catch(Exception e)
        {
            lexer = null;
        }
        lexerValid = false;
    }

    public void paint(Graphics g, Shape a)
    {
        super.paint(g, a);
        lexerValid = false;
    }

    protected int drawUnselectedText(Graphics g, int x, int y, int start, int end)
        throws BadLocationException
    {
        Document doc = getDocument();
        Style lastStyle = null;
        int lastToken = 0;
        int mark = start;
        int p;
        for(; start < end; start = p)
        {
            updateScanner(start);
            p = Math.min(lexer.getEndOffset(), end);
            p = p > start ? p : end;
            Style style = context.getStyle(lexer.token);
            if(style != lastStyle && lastStyle != null)
            {
                g.setColor(context.getForeground(lastStyle));
                g.setFont(context.getFont(lastStyle));
                javax.swing.text.Segment text = getLineBuffer();
                doc.getText(mark, start - mark, text);
                x = Utilities.drawTabbedText(text, x, y, g, this, mark);
                mark = start;
            }
            lastToken = lexer.token;
            lastStyle = style;
        }

        g.setColor(context.getForeground(lastStyle));
        g.setFont(context.getFont(lastStyle));
        javax.swing.text.Segment text = getLineBuffer();
        doc.getText(mark, end - mark, text);
        x = Utilities.drawTabbedText(text, x, y, g, this, mark);
        return x;
    }

    protected int drawSelectedText(Graphics g, int x, int y, int start, int end)
        throws BadLocationException
    {
        Document doc = getDocument();
        Style lastStyle = null;
        int lastToken = 0;
        int mark = start;
        g.setColor(Color.black);
        int p;
        for(; start < end; start = p)
        {
            updateScanner(start);
            p = Math.min(lexer.getEndOffset(), end);
            p = p > start ? p : end;
            Style style = context.getStyle(lexer.token);
            if(style != lastStyle && lastStyle != null)
            {
                g.setFont(context.getFont(lastStyle));
                javax.swing.text.Segment text = getLineBuffer();
                doc.getText(mark, start - mark, text);
                x = Utilities.drawTabbedText(text, x, y, g, this, mark);
                mark = start;
            }
            lastToken = lexer.token;
            lastStyle = style;
        }

        g.setFont(context.getFont(lastStyle));
        javax.swing.text.Segment text = getLineBuffer();
        doc.getText(mark, end - mark, text);
        x = Utilities.drawTabbedText(text, x, y, g, this, mark);
        return x;
    }

    private void updateScanner(int p)
    {
        try
        {
            if(!lexerValid)
            {
                XmlDocument doc = (XmlDocument)getDocument();
                lexer.setRange(doc.getTagEnd(p), doc.getLength());
                lexerValid = true;
            }
            for(; lexer.getEndOffset() <= p; lexer.scan());
        }
        catch(Throwable e)
        {
            e.printStackTrace();
        }
    }

    private static final boolean DEBUG = false;
    private XmlScanner lexer;
    private boolean lexerValid;
    private XmlContext context;
}
