// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IRSSDistrib.java

package org.seamcat.model.datatypes;

import org.seamcat.distribution.Distribution;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model.datatypes:
//            IRSS

public class IRSSDistrib extends IRSS
{

    public IRSSDistrib(String name)
    {
        super(name);
    }

    public void setDistribution(Distribution distrib)
    {
        this.distrib = distrib;
    }

    public Distribution getDistribution()
    {
        return distrib;
    }

    protected void initNodeAttributes()
    {
    }

    public Element toElement(Document doc)
    {
        return null;
    }

    private Distribution distrib;
}
