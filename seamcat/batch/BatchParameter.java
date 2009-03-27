// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BatchParameter.java

package org.seamcat.batch;

import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.function.Function;
import org.seamcat.model.NodeAttribute;
import org.seamcat.model.SeamcatComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BatchParameter
{

    public BatchParameter(SeamcatComponent parent, int localIndex, int index, String path)
    {
        this.parent = parent;
        this.index = index;
        this.path = path;
        this.localIndex = localIndex;
        node = parent.getNodeAttributes()[localIndex];
        displayname = (new StringBuilder()).append(path).append(" / ").append(node.getName()).toString();
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("batch-parameter");
        element.setAttribute("index", Integer.toString(index));
        element.setAttribute("newValue", getNewValue().toString());
        element.setAttribute("type", node.getType());
        element.setAttribute("name", node.getName());
        element.setAttribute("path", path);
        if(node.getType().equals("Distribution"))
            element.appendChild(((Distribution)(Distribution)getNewValue()).toElement(doc));
        else
        if(node.getType().equals("Function"))
            if(getNewValue() instanceof Function)
                element.appendChild(((Function)(Function)getNewValue()).toElement(doc));
            else
            if(getNewValue() instanceof DiscreteFunction2)
                element.appendChild(((DiscreteFunction2)(DiscreteFunction2)getNewValue()).toElement(doc));
        return element;
    }

    public String toString()
    {
        return displayname;
    }

    public void assignValue()
    {
        try
        {
            oldValue = node.getValue();
            node.setValue(newValue);
            parent.setValueAt(getNewValue(), localIndex, 1);
            if(oldValue == null)
                oldValue = newValue;
        }
        catch(Exception ex)
        {
            LOG.error((new StringBuilder()).append("Error assigning new value to ").append(node.getName()).toString(), ex);
        }
    }

    public void resetValue()
    {
        try
        {
            node.setValue(oldValue);
            parent.setValueAt(oldValue, localIndex, 1);
        }
        catch(Exception ex)
        {
            LOG.error((new StringBuilder()).append("Error resetting value of ").append(node.getName()).toString(), ex);
        }
    }

    public Object getType()
    {
        return node.getType();
    }

    public String getName()
    {
        return node.getName();
    }

    public Object getOldValue()
    {
        if(oldValue == null)
            oldValue = node.getValue();
        return oldValue;
    }

    public Object getNewValue()
    {
        if(newValue == null)
            return getOldValue();
        else
            return newValue;
    }

    public void setOldValue(Object oldValue)
    {
        this.oldValue = oldValue;
    }

    public void setNewValue(Object newValue)
    {
        this.newValue = newValue;
    }

    public boolean equals(Object o)
    {
        return (o instanceof BatchParameter) ? equals((BatchParameter)o) : false;
    }

    public boolean equals(BatchParameter b)
    {
        return displayname == null ? false : displayname.equals(b.displayname);
    }

    public int hashCode()
    {
        return displayname == null ? 0 : displayname.hashCode();
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/batch/BatchParameter);
    private Object oldValue;
    private Object newValue;
    private NodeAttribute node;
    private SeamcatComponent parent;
    private int index;
    private int localIndex;
    private String displayname;
    private String path;

}