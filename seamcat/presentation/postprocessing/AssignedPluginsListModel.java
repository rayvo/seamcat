// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AssignedPluginsListModel.java

package org.seamcat.presentation.postprocessing;

import java.util.*;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.seamcat.postprocessing.PostProcessingPluginWrapper;

public class AssignedPluginsListModel
    implements ListModel
{

    public AssignedPluginsListModel()
    {
        plugins = new ArrayList();
        listeners = new ArrayList();
    }

    public int getSize()
    {
        if(plugins == null)
            return 0;
        else
            return plugins.size();
    }

    public Object getElementAt(int index)
    {
        if(plugins == null)
            return null;
        else
            return plugins.get(index);
    }

    public void addListDataListener(ListDataListener l)
    {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l)
    {
        listeners.remove(l);
    }

    public void addPlugin(PostProcessingPluginWrapper p)
    {
        plugins.add(p);
        fireListeners();
    }

    public void removePlugin(PostProcessingPluginWrapper p)
    {
        plugins.remove(p);
        fireListeners();
    }

    public void moveUp(int index)
    {
        plugins.add(index - 1, plugins.remove(index));
        fireListeners();
    }

    public void moveDown(int index)
    {
        plugins.add(index + 1, plugins.remove(index));
        fireListeners();
    }

    public List getPlugins()
    {
        List _plugins = new ArrayList();
        PostProcessingPluginWrapper p;
        for(Iterator i$ = plugins.iterator(); i$.hasNext(); _plugins.add(p))
            p = (PostProcessingPluginWrapper)i$.next();

        return _plugins;
    }

    public void removePlugin(int index)
    {
        plugins.remove(index);
        fireListeners();
    }

    public void fireListeners()
    {
        ListDataListener ldl;
        for(Iterator i$ = listeners.iterator(); i$.hasNext(); ldl.contentsChanged(new ListDataEvent(this, 0, 0, getSize())))
            ldl = (ListDataListener)i$.next();

    }

    public void setPlugins(List _plugins)
    {
        plugins.clear();
        PostProcessingPluginWrapper p;
        for(Iterator i$ = _plugins.iterator(); i$.hasNext(); plugins.add(p))
            p = (PostProcessingPluginWrapper)i$.next();

        fireListeners();
    }

    private List plugins;
    private List listeners;
}
