// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DRSSVector.java

package org.seamcat.model.datatypes;

import java.util.Enumeration;
import org.seamcat.model.NodeAttribute;
import org.seamcat.model.core.EventVector;
import org.seamcat.presentation.Node;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.datatypes:
//            DRSS

public class DRSSVector extends DRSS
{

    public DRSSVector(String name)
    {
        super(name);
        eventVector = new EventVector();
    }

    public DRSSVector(Element element)
    {
        super(element.getAttribute("name"));
        setEventVector(new EventVector((Element)element.getElementsByTagName("EventVector").item(0)));
    }

    public EventVector getEventVector()
    {
        return eventVector;
    }

    public void setEventVector(EventVector eventVector)
    {
        this.eventVector = eventVector;
    }

    public Enumeration children()
    {
        return null;
    }

    public boolean getAllowsChildren()
    {
        return false;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public Node getChildAt(int childIndex)
    {
        return null;
    }

    public int getChildCount()
    {
        return 0;
    }

    public int getIndex(Node node)
    {
        return 0;
    }

    public Node getParent()
    {
        return null;
    }

    public boolean isLeaf()
    {
        return false;
    }

    public String toString()
    {
        return "dRSS";
    }

    public String getReference()
    {
        return (new StringBuilder()).append(super.getReference()).append(eventVector.isModifiedByPlugin() ? " - modified by plugin" : "").toString();
    }

    protected void initNodeAttributes()
    {
        nodeAttributes = new NodeAttribute[1];
        nodeAttributes[0] = new NodeAttribute((new StringBuilder()).append(getReference()).append(" [").append(eventVector.size()).append("]").toString(), "dBm", this, (new StringBuilder()).append("Vector [").append(eventVector.size()).append("]").toString(), null, true, true, null);
    }

    public Element toElement(Document doc)
    {
        return toElement(doc, true);
    }

    public Element toElement(Document doc, boolean expand)
    {
        Element element = doc.createElement("DRSSVector");
        element.setAttribute("name", getReference());
        element.appendChild(eventVector.toElement(doc, expand));
        return element;
    }

    private EventVector eventVector;
}
