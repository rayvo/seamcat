// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PluginClassAdaptor.java

package org.seamcat.presentation;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.jdesktop.swingx.autocomplete.AbstractAutoCompleteAdaptor;
import org.seamcat.model.Model;

public class PluginClassAdaptor extends AbstractAutoCompleteAdaptor
{

    public PluginClassAdaptor(JTextComponent comp)
    {
        files = new ArrayList();
        component = comp;
        try
        {
            File all_files[] = (new File(Model.seamcatHome, "plugins")).listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".class");
                }

                final PluginClassAdaptor this$0;

            
            {
                this$0 = PluginClassAdaptor.this;
                super();
            }
            }
);
            File arr$[] = all_files;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                File file = arr$[i$];
                String name = file.getName();
                files.add(name.substring(0, name.indexOf(".class")));
            }

        }
        catch(Exception e) { }
    }

    public Object getItem(int index)
    {
        return files.get(index);
    }

    public int getItemCount()
    {
        return files.size();
    }

    public Object getSelectedItem()
    {
        return selectedItem;
    }

    public JTextComponent getTextComponent()
    {
        return component;
    }

    public void setSelectedItem(Object selected)
    {
        selectedItem = selected.toString();
    }

    List files;
    private String selectedItem;
    private JTextComponent component;
}
