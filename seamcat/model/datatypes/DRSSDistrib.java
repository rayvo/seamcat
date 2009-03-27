// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DRSSDistrib.java

package org.seamcat.model.datatypes;

import org.seamcat.distribution.Distribution;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.datatypes:
//            DRSS

public class DRSSDistrib extends DRSS
{

    public DRSSDistrib(String name)
    {
        super(name);
        setDistrib(null);
    }

    public DRSSDistrib(Element element)
    {
        super(element.getAttribute("name"));
        NodeList nl = element.getElementsByTagName("distribution");
        if(nl.getLength() > 0)
            setDistrib(Distribution.create((Element)nl.item(0)));
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("DRSSDistrib");
        element.setAttribute("name", getReference());
        if(getDistrib() != null)
            element.appendChild(getDistrib().toElement(doc));
        return element;
    }

    public Distribution getDistrib()
    {
        return distrib;
    }

    public void setDistrib(Distribution distrib)
    {
        this.distrib = distrib;
    }

    protected void initNodeAttributes()
    {
    }

    private Distribution distrib;
}
