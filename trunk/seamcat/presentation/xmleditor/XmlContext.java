// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlContext.java

package org.seamcat.presentation.xmleditor;

import java.awt.Color;
import java.awt.Font;
import javax.swing.text.*;

// Referenced classes of package org.seamcat.presentation.xmleditor:
//            Constants

public class XmlContext extends StyleContext
{

    public XmlContext()
    {
        styles = null;
        italicFont = null;
        plainFont = null;
        boldFont = null;
        styles = new Style[Constants.MAX_TOKENS + 1];
        for(int i = 0; i < styles.length; i++)
            styles[i] = new javax.swing.text.StyleContext.NamedStyle(this);

        setDefaultValues();
    }

    public void setFont(Font font)
    {
        plainFont = font.deriveFont(0);
        italicFont = font.deriveFont(2);
        boldFont = font.deriveFont(1);
    }

    public void setDefaultValues()
    {
        setAttributes(1, new Color(0, 51, 102), 0);
        setAttributes(3, Color.black, 0);
        setAttributes(2, new Color(0, 102, 102), 0);
        setAttributes(5, new Color(153, 51, 51), 0);
        setAttributes(7, new Color(102, 0, 0), 0);
        setAttributes(6, new Color(0, 102, 102), 0);
        setAttributes(10, new Color(102, 102, 102), 0);
        setAttributes(12, new Color(0, 51, 51), 0);
        setAttributes(11, new Color(0, 102, 102), 0);
        setAttributes(15, new Color(102, 102, 102), 0);
        setAttributes(16, new Color(153, 153, 153), 0);
        setAttributes(20, new Color(102, 102, 102), 0);
    }

    public void setAttributes(int token, Color foreground, int style)
    {
        setForeground(token, foreground);
        setFontStyle(token, style);
    }

    public void setFontStyle(int token, int style)
    {
        Style s = getStyle(token);
        StyleConstants.setItalic(s, (style & 2) > 0);
        StyleConstants.setBold(s, (style & 1) > 0);
    }

    public void setForeground(int token, Color color)
    {
        Style s = getStyle(token);
        StyleConstants.setForeground(s, color);
    }

    public Color getForeground(int token)
    {
        if(token >= 0 && token < styles.length)
        {
            Style s = styles[token];
            return super.getForeground(s);
        } else
        {
            return Color.black;
        }
    }

    public Font getFont(int token)
    {
        if(token >= 0 && token < styles.length)
        {
            Style s = styles[token];
            return getFont(s);
        } else
        {
            return null;
        }
    }

    public Font getFont(Style style)
    {
        Font font = plainFont;
        if(style != null)
            if(StyleConstants.isItalic(style))
                font = italicFont;
            else
            if(StyleConstants.isBold(style))
                font = boldFont;
        return font;
    }

    public Color getForeground(Style style)
    {
        if(style != null)
            return super.getForeground(style);
        else
            return null;
    }

    public Style getStyle(int token)
    {
        if(token >= 0 && token < styles.length)
            return styles[token];
        else
            return null;
    }

    private Style styles[];
    private Font italicFont;
    private Font plainFont;
    private Font boldFont;
}
