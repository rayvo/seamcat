// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DocumentPane.java

package org.seamcat.presentation.xmleditor;

import java.io.*;
import java.net.URL;
import javax.swing.JEditorPane;

public class DocumentPane extends JEditorPane
{

    public DocumentPane()
    {
        loaded = false;
        filename = "";
    }

    public void setPage(URL url)
    {
        loaded = false;
        try
        {
            super.setPage(url);
            File file = new File(getPage().toString());
            setFilename(file.getName());
            loaded = true;
        }
        catch(IOException ioe)
        {
            System.err.println((new StringBuilder()).append("Unable to set page: ").append(url).toString());
        }
    }

    public void setText(String text)
    {
        super.setText(text);
        setFilename("");
        loaded = true;
    }

    public void loadFile(String filename)
    {
        try
        {
            File file = new File(filename);
            setPage(file.toURL());
        }
        catch(IOException mue)
        {
            System.err.println((new StringBuilder()).append("Unable to load file: ").append(filename).toString());
        }
    }

    public void saveFile(String filename)
    {
        try
        {
            File file = new File(filename);
            FileWriter writer = new FileWriter(file);
            writer.write(getText());
            writer.close();
            setFilename(file.getName());
        }
        catch(IOException ioe)
        {
            System.err.println((new StringBuilder()).append("Unable to save file: ").append(filename).toString());
        }
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public static final String TEXT = "text/plain";
    public static final String HTML = "text/html";
    private boolean loaded;
    private String filename;
}
