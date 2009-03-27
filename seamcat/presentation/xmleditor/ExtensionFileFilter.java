// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExtensionFileFilter.java

package org.seamcat.presentation.xmleditor;

import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter
{

    public ExtensionFileFilter(boolean allowDirectories)
    {
        extensionsTable = new Hashtable();
        allowAll = false;
        this.allowDirectories = allowDirectories;
    }

    public ExtensionFileFilter()
    {
        this(true);
    }

    public static String getFileName(String initialDirectory, String description, String extension)
    {
        String extensions[] = {
            extension
        };
        return getFileName(initialDirectory, description, extensions, 0);
    }

    public static String getFileName(String initialDirectory, String description, String extension, int mode)
    {
        String extensions[] = {
            extension
        };
        return getFileName(initialDirectory, description, extensions, mode);
    }

    public static String getFileName(String initialDirectory, String description, String extensions[])
    {
        return getFileName(initialDirectory, description, extensions, 0);
    }

    public static String getFileName(String initialDirectory, String description, String extensions[], int mode)
    {
        ExtensionFileFilter filter = new ExtensionFileFilter();
        filter.setDescription(description);
        for(int i = 0; i < extensions.length; i++)
        {
            String extension = extensions[i];
            filter.addExtension(extension, true);
        }

        JFileChooser chooser = new JFileChooser(initialDirectory);
        chooser.setFileFilter(filter);
        int selectVal = mode != 1 ? chooser.showOpenDialog(null) : chooser.showSaveDialog(null);
        if(selectVal == 0)
        {
            String path = chooser.getSelectedFile().getAbsolutePath();
            return path;
        } else
        {
            JOptionPane.showMessageDialog(null, "No file selected.");
            return null;
        }
    }

    public void addExtension(String extension, boolean caseInsensitive)
    {
        if(caseInsensitive)
            extension = extension.toLowerCase();
        if(!extensionsTable.containsKey(extension))
        {
            extensionsTable.put(extension, new Boolean(caseInsensitive));
            if(extension.equals("*") || extension.equals("*.*") || extension.equals(".*"))
                allowAll = true;
        }
    }

    public boolean accept(File file)
    {
        if(file.isDirectory())
            return allowDirectories;
        if(allowAll)
            return true;
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if(dotIndex == -1 || dotIndex == name.length() - 1)
            return false;
        String extension = name.substring(dotIndex + 1);
        if(extensionsTable.containsKey(extension))
            return true;
        for(Enumeration keys = extensionsTable.keys(); keys.hasMoreElements();)
        {
            String possibleExtension = (String)keys.nextElement();
            Boolean caseFlag = (Boolean)extensionsTable.get(possibleExtension);
            if(caseFlag != null && caseFlag.equals(Boolean.FALSE) && possibleExtension.equalsIgnoreCase(extension))
                return true;
        }

        return false;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public static final int LOAD = 0;
    public static final int SAVE = 1;
    private String description;
    private boolean allowDirectories;
    private Hashtable extensionsTable;
    private boolean allowAll;
}
