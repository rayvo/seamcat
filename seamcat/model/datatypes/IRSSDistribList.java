// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IRSSDistribList.java

package org.seamcat.model.datatypes;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.NodeAttribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model.datatypes:
//            IRSSList

public class IRSSDistribList extends IRSSList
{

    public IRSSDistribList(Element element)
    {
        this(element.getAttribute("name"));
        NodeList nlCompositeDistrib = element.getElementsByTagName("iRSSCompositeDistrib");
        if(nlCompositeDistrib.getLength() > 0)
            setIRSSCompositeDistrib(Distribution.create((Element)nlCompositeDistrib.item(0).getFirstChild()));
        try
        {
            NodeList nl = element.getElementsByTagName("iRSSDistribs").item(0).getChildNodes();
            int x = 0;
            for(int size = nl.getLength(); x < size; x++)
                iRSSDistribs.add(nl.item(x));

        }
        catch(NullPointerException ex)
        {
            LOG.debug("Error while instanciating IRSSDistribList", ex);
        }
    }

    public IRSSDistribList(String name)
    {
        super(name);
        iRSSDistribs = new Vector();
    }

    public List getIRSSDistribs()
    {
        return iRSSDistribs;
    }

    public void setIRSSDistribs(Vector iRSSDistribs)
    {
        this.iRSSDistribs = iRSSDistribs;
    }

    public Distribution getIRSSCompositeDistrib()
    {
        return iRSSCompositeDistrib;
    }

    public void setIRSSCompositeDistrib(Distribution iRSSCompositeDistrib)
    {
        this.iRSSCompositeDistrib = iRSSCompositeDistrib;
    }

    public Enumeration children()
    {
        return null;
    }

    public boolean getAllowsChildren()
    {
        return false;
    }

    public org.seamcat.presentation.Node getChildAt(int childIndex)
    {
        return null;
    }

    public int getChildCount()
    {
        return 0;
    }

    public int getIndex(org.seamcat.presentation.Node node)
    {
        return 0;
    }

    public org.seamcat.presentation.Node getParent()
    {
        return null;
    }

    public boolean isLeaf()
    {
        return false;
    }

    protected void initNodeAttributes()
    {
        nodeAttributes = new NodeAttribute[iRSSDistribs.size()];
        for(int i = 0; i < nodeAttributes.length; i++)
            nodeAttributes[i] = new NodeAttribute(iRSSDistribs.get(i).toString(), "dBm", iRSSDistribs.get(i), "Vector", null, false, true, null);

    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("iRSSDistribList");
        element.setAttribute("name", getReference());
        if(getIRSSCompositeDistrib() != null)
            element.appendChild(getIRSSCompositeDistrib().toElement(doc));
        return element;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/datatypes/IRSSDistribList);
    private Vector iRSSDistribs;
    private Distribution iRSSCompositeDistrib;

}
