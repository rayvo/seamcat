// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DocumentTreeModel.java

package org.seamcat.testfeatures;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.w3c.dom.*;

public class DocumentTreeModel
    implements TreeModel
{

    public DocumentTreeModel(Document _doc)
    {
        doc = _doc;
        listeners = new ArrayList();
    }

    public Object getRoot()
    {
        return doc;
    }

    public Object getChild(Object parent, int index)
    {
        Node p = (Node)parent;
        int childs = p.getChildNodes().getLength();
        Node n;
        if(index < childs)
            n = p.getChildNodes().item(index);
        else
            n = p.getAttributes().item(index - childs);
        return n;
    }

    public int getChildCount(Object parent)
    {
        int childs = 0;
        int att = 0;
        Node p = (Node)parent;
        if(p.hasChildNodes())
            childs = p.getChildNodes().getLength();
        if(p.hasAttributes())
            att = p.getAttributes().getLength();
        return childs + att;
    }

    public boolean isLeaf(Object node)
    {
        return ((Node)node).getChildNodes().getLength() < 1;
    }

    public void valueForPathChanged(TreePath treepath, Object obj)
    {
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        NodeList nl = ((Node)parent).getChildNodes();
        for(int i = 0; i < nl.getLength(); i++)
            if(nl.item(i).equals(child))
                return i;

        return -1;
    }

    public void addTreeModelListener(TreeModelListener l)
    {
        listeners.add(l);
    }

    public void removeTreeModelListener(TreeModelListener l)
    {
        listeners.remove(l);
    }

    public Document doc;
    private List listeners;
}
