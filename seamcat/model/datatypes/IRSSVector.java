// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IRSSVector.java

package org.seamcat.model.datatypes;

import org.seamcat.model.core.EventVector;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.datatypes:
//            IRSS

public class IRSSVector extends IRSS
{

    public IRSSVector(String name)
    {
        super(name);
        shouldBeCalculated = true;
        eventVector = new EventVector(shouldBeCalculated);
    }

    public IRSSVector(String name, boolean _shouldBeCalculated)
    {
        super(name);
        shouldBeCalculated = true;
        shouldBeCalculated = _shouldBeCalculated;
        eventVector = new EventVector(shouldBeCalculated);
    }

    public IRSSVector(Element element)
    {
        super(element.getAttribute("name"));
        shouldBeCalculated = true;
        setShouldBeCalculated(Boolean.valueOf(element.getAttribute("shouldBeCalculated")).booleanValue());
        setEventVector(new EventVector((Element)element.getElementsByTagName("eventVector").item(0).getFirstChild()));
    }

    public Element toElement(Document doc)
    {
        return toElement(doc, true);
    }

    public Element toElement(Document doc, boolean expand)
    {
        Element element = doc.createElement("IRSSVector");
        element.setAttribute("name", getReference());
        element.setAttribute("shouldBeCalculated", Boolean.toString(isShouldBeCalculated()));
        Element eventVectorElement = doc.createElement("eventVector");
        eventVectorElement.appendChild(eventVector.toElement(doc, expand));
        element.appendChild(eventVectorElement);
        return element;
    }

    public EventVector getEventVector()
    {
        return eventVector;
    }

    protected boolean isShouldBeCalculated()
    {
        return shouldBeCalculated;
    }

    public void setEventVector(EventVector eventVector)
    {
        this.eventVector = eventVector;
    }

    protected void setShouldBeCalculated(boolean shouldBeCalculated)
    {
        this.shouldBeCalculated = shouldBeCalculated;
    }

    public String toString()
    {
        return (new StringBuilder()).append("Array[").append(eventVector.size()).append("]").toString();
    }

    public String getReference()
    {
        return (new StringBuilder()).append(super.getReference()).append(eventVector.isModifiedByPlugin() ? " - modified by plugin" : "").toString();
    }

    protected void initNodeAttributes()
    {
    }

    private EventVector eventVector;
    private boolean shouldBeCalculated;
}
