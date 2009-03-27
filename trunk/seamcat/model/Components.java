// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Components.java

package org.seamcat.model;

import java.util.*;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.seamcat.presentation.Node;

// Referenced classes of package org.seamcat.model:
//            NodeAttribute, SeamcatComponent, Model

public class Components
    implements Collection, Node
{
    private class ComponentsComboBoxModel
        implements ComboBoxModel
    {

        public void addListDataListener(ListDataListener l)
        {
            Components.this.addListDataListener(l);
        }

        public Object getElementAt(int index)
        {
            return Components.this.getElementAt(index);
        }

        public int getSize()
        {
            return Components.this.getSize();
        }

        public void removeListDataListener(ListDataListener l)
        {
            Components.this.removeListDataListener(l);
        }

        public Object getSelectedItem()
        {
            return selectedItem;
        }

        public void setSelectedItem(Object anItem)
        {
            selectedItem = anItem;
        }

        private Object selectedItem;
        final Components this$0;

        private ComponentsComboBoxModel()
        {
            this$0 = Components.this;
            super();
            selectedItem = null;
        }

    }


    public Components(String name)
    {
        nodeAttributes = new NodeAttribute[0];
        this.name = name;
    }

    public String toString()
    {
        return name;
    }

    public Object get(int index)
    {
        return data.get(index);
    }

    public Object get(String reference)
    {
        for(Iterator i$ = data.iterator(); i$.hasNext();)
        {
            Object o = i$.next();
            if((o instanceof SeamcatComponent) && ((SeamcatComponent)o).getReference().equals(reference))
                return o;
        }

        return null;
    }

    public int indexOf(Object o)
    {
        return data.indexOf(o);
    }

    public boolean add(Object o)
    {
        if(o == null)
            throw new NullPointerException();
        if(o instanceof SeamcatComponent)
            ((SeamcatComponent)o).setComponentCollection(this);
        boolean result = data.add(o);
        if(result)
            fireListDataListeners(new ListDataEvent(this, 0, 0, data.size() - 1));
        return result;
    }

    public boolean addAll(Collection c)
    {
        Object o;
        for(Iterator i$ = c.iterator(); i$.hasNext(); add(o))
            o = i$.next();

        return true;
    }

    public void clear()
    {
        if(data.size() > 0)
        {
            data.clear();
            fireListDataListeners(new ListDataEvent(this, 0, 0, 0));
        }
    }

    public boolean containsReference(String ref)
    {
        boolean result = false;
        int i = 0;
        for(int stop = data.size(); i < stop && !result; i++)
            if(((SeamcatComponent)data.get(i)).getReference().equals(ref))
                result = true;

        return result;
    }

    public boolean contains(Object o)
    {
        return data.contains(o);
    }

    public boolean containsAll(Collection c)
    {
        return data.containsAll(c);
    }

    public boolean isEmpty()
    {
        return data.isEmpty();
    }

    public Iterator iterator()
    {
        return data.iterator();
    }

    public boolean remove(String reference)
    {
        try
        {
            return remove(get(reference));
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public boolean remove(Object o)
    {
        boolean result = data.remove(o);
        if(result)
        {
            SeamcatComponent component = (SeamcatComponent)o;
            component.setComponentCollection(null);
        }
        fireListDataListeners(new ListDataEvent(this, 0, 0, data.size() - 1));
        return result;
    }

    public void fireListDataListeners(ListDataEvent e)
    {
        ListDataListener listener;
        for(Iterator i$ = listListeners.iterator(); i$.hasNext(); listener.contentsChanged(e))
            listener = (ListDataListener)i$.next();

    }

    public boolean removeAll(Collection c)
    {
        Object o;
        for(Iterator i$ = c.iterator(); i$.hasNext(); remove(o))
            o = i$.next();

        return true;
    }

    public boolean retainAll(Collection c)
    {
        throw new UnsupportedOperationException();
    }

    public int size()
    {
        return data.size();
    }

    public Object[] toArray()
    {
        return data.toArray();
    }

    public Object[] toArray(Object a[])
    {
        return data.toArray(a);
    }

    public Enumeration children()
    {
        return Collections.enumeration(data);
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public Node getChildAt(int childIndex)
    {
        return (Node)data.get(childIndex);
    }

    public int getChildCount()
    {
        return data.size();
    }

    public int getIndex(Node node)
    {
        return data.indexOf(node);
    }

    public Node getParent()
    {
        return Model.getInstance().getLibrary();
    }

    public boolean isLeaf()
    {
        return false;
    }

    public NodeAttribute[] getNodeAttributes()
    {
        return (NodeAttribute[])nodeAttributes.clone();
    }

    public int hashCode()
    {
        return 0;
    }

    public boolean equals(Object o)
    {
        return false;
    }

    public ComboBoxModel createComboBoxModel()
    {
        return new ComponentsComboBoxModel();
    }

    public void addListDataListener(ListDataListener l)
    {
        listListeners.add(l);
    }

    public Object getElementAt(int index)
    {
        return data.get(index);
    }

    public int getSize()
    {
        return data.size();
    }

    public void removeListDataListener(ListDataListener l)
    {
        listListeners.remove(l);
    }

    private final List data = new ArrayList();
    private final List listListeners = new ArrayList();
    private NodeAttribute nodeAttributes[];
    private final ComponentsComboBoxModel comboBoxModel = new ComponentsComboBoxModel();
    private String name;
}