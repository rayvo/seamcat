// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlInputStream.java

package org.seamcat.presentation.xmleditor;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.text.*;

class XmlInputStream extends InputStream
{

    public XmlInputStream(Document doc)
    {
        segment = null;
        document = null;
        end = 0;
        pos = 0;
        index = 0;
        segment = new Segment();
        document = doc;
        end = document.getLength();
        pos = 0;
        try
        {
            loadSegment();
        }
        catch(IOException ioe)
        {
            throw new Error((new StringBuilder()).append("unexpected: ").append(ioe).toString());
        }
    }

    public void setRange(int start, int end)
    {
        this.end = end;
        pos = start;
        try
        {
            loadSegment();
        }
        catch(IOException ioe)
        {
            throw new Error((new StringBuilder()).append("unexpected: ").append(ioe).toString());
        }
    }

    public int read()
        throws IOException
    {
        if(index >= segment.offset + segment.count)
        {
            if(pos >= end)
                return -1;
            loadSegment();
        }
        return segment.array[index++];
    }

    private void loadSegment()
        throws IOException
    {
        try
        {
            int n = Math.min(1024, end - pos);
            document.getText(pos, n, segment);
            pos += n;
            index = segment.offset;
        }
        catch(BadLocationException e)
        {
            throw new IOException("Bad location");
        }
    }

    private Segment segment;
    private Document document;
    private int end;
    private int pos;
    private int index;
}
