// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlDocument.java

package org.seamcat.presentation.xmleditor;

import javax.swing.text.*;

// Referenced classes of package org.seamcat.presentation.xmleditor:
//            XmlEditorPane

public class XmlDocument extends PlainDocument
{

    public XmlDocument(XmlEditorPane editor)
    {
        super(new GapContent(1024));
        this.editor = null;
        loading = false;
        this.editor = editor;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    public int getTagEnd(int p)
    {
        int elementEnd = 0;
        if(p > 0)
            try
            {
                int index = 0;
                String s = getText(0, p);
                int commentStart = s.lastIndexOf("<!--");
                int commentEnd = s.lastIndexOf("-->");
                if(commentStart > 0 && commentStart > commentEnd)
                    index = s.lastIndexOf(">", commentStart);
                else
                    index = s.lastIndexOf(">");
                if(index != -1)
                    elementEnd = index;
            }
            catch(BadLocationException bl) { }
        return elementEnd;
    }

    public void insertString(int off, String str, AttributeSet set)
        throws BadLocationException
    {
        if(!loading && str.equals(">"))
        {
            if(editor.isTagCompletion())
            {
                int caretPosition = editor.getCaretPosition();
                StringBuffer endTag = new StringBuffer(str);
                String text = getText(0, off);
                int startTag = text.lastIndexOf('<', off);
                int prefEndTag = text.lastIndexOf('>', off);
                if(startTag > 0 && startTag > prefEndTag && startTag < text.length() - 1)
                {
                    String tag = text.substring(startTag, text.length());
                    char first = tag.charAt(1);
                    if(first != '/' && first != '!' && first != '?' && !Character.isWhitespace(first))
                    {
                        boolean finished = false;
                        char previous = tag.charAt(tag.length() - 1);
                        if(previous != '/' && previous != '-')
                        {
                            endTag.append("</");
                            for(int i = 1; i < tag.length() && !finished; i++)
                            {
                                char ch = tag.charAt(i);
                                if(!Character.isWhitespace(ch))
                                    endTag.append(ch);
                                else
                                    finished = true;
                            }

                            endTag.append(">");
                        }
                    }
                }
                str = endTag.toString();
                super.insertString(off, str, set);
                editor.setCaretPosition(caretPosition);
            } else
            {
                super.insertString(off, str, set);
            }
        } else
        if(!loading && str.equals("\n"))
        {
            StringBuffer newStr = new StringBuffer(str);
            Element elem = getDefaultRootElement().getElement(getDefaultRootElement().getElementIndex(off));
            int start = elem.getStartOffset();
            int end = elem.getEndOffset();
            String line = getText(start, off - start);
            boolean finished = false;
            for(int i = 0; i < line.length() && !finished; i++)
            {
                char ch = line.charAt(i);
                if(ch != '\n' && ch != '\f' && ch != '\r' && Character.isWhitespace(ch))
                    newStr.append(ch);
                else
                    finished = true;
            }

            if(isStartElement(line))
                newStr.append(getTabString());
            str = newStr.toString();
            super.insertString(off, str, set);
        } else
        {
            super.insertString(off, str, set);
        }
    }

    private String getTabString()
    {
        byte tab[] = new byte[editor.getSpaces()];
        for(int i = 0; i < tab.length; i++)
            tab[i] = 32;

        return new String(tab);
    }

    private boolean isStartElement(String line)
    {
        boolean result = false;
        int first = line.lastIndexOf("<");
        int last = line.lastIndexOf(">");
        if(last < first)
        {
            result = true;
        } else
        {
            int firstEnd = line.lastIndexOf("</");
            int lastEnd = line.lastIndexOf("/>");
            if(firstEnd != first && lastEnd + 1 != last)
                result = true;
        }
        return result;
    }

    private XmlEditorPane editor;
    private boolean loading;
}
