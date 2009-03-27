// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IRSSVectorList.java

package org.seamcat.model.datatypes;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.NodeAttribute;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.model.core.EventVector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model.datatypes:
//            IRSSVector

public class IRSSVectorList extends SeamcatComponent
{

    public IRSSVectorList(String ref)
    {
        super(ref, null);
        iRSSVectors = new Vector();
    }

    public IRSSVectorList(Element element)
    {
        this(element.getAttribute("name"));
        NodeList nl = element.getElementsByTagName("iRSSVectors").item(0).getChildNodes();
        int x = 0;
        for(int size = nl.getLength(); x < size; x++)
            addIRSSVector(new IRSSVector((Element)nl.item(x)));

        try
        {
            NodeList nlCompositeVector = element.getElementsByTagName("iRSSCompositeVector");
            if(nlCompositeVector.getLength() > 0)
                setIRSSCompositeVector(new EventVector((Element)nlCompositeVector.item(0)));
        }
        catch(RuntimeException ex) { }
        try
        {
            NodeList nlCompositeDistrib = element.getElementsByTagName("iRSSCompositeDistrib");
            if(nlCompositeDistrib.getLength() > 0)
                setIRSSCompositeDistrib(Distribution.create((Element)nlCompositeDistrib.item(0).getFirstChild()));
        }
        catch(RuntimeException ex1) { }
    }

    public void addIRSSVector(IRSSVector vector)
    {
        iRSSVectors.add(vector);
    }

    public void addIRSSVector(IRSSVector vector, int index)
    {
        iRSSVectors.add(index, vector);
    }

    public void remove(int index)
    {
        try
        {
            iRSSVectors.remove(index);
        }
        catch(Exception e)
        {
            LOG.debug("Error while removing specified index", e);
        }
    }

    public void removeAll()
    {
        iRSSVectors.removeAllElements();
    }

    public List getIRSSVectors()
    {
        return iRSSVectors;
    }

    public EventVector getIRSSCompositeVector()
    {
        return iRSSCompositeVector;
    }

    public void setIRSSCompositeVector(EventVector iRSSCompositeVector)
    {
        this.iRSSCompositeVector = iRSSCompositeVector;
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
        nodeAttributes = new NodeAttribute[iRSSVectors.size()];
        for(int i = 0; i < nodeAttributes.length; i++)
            nodeAttributes[i] = new NodeAttribute(((IRSSVector)iRSSVectors.get(i)).getReference(), "dBm", iRSSVectors.get(i), "Vector", null, false, false, null);

    }

    public Element toElement(Document doc)
    {
        return toElement(doc, true);
    }

    public Element toElement(Document doc, boolean expand)
    {
        Element element = doc.createElement("iRSSVectorList");
        element.setAttribute("name", getReference());
        Element iRSSVectorsElement = doc.createElement("iRSSVectors");
        for(Iterator i = getIRSSVectors().iterator(); i.hasNext(); iRSSVectorsElement.appendChild(((IRSSVector)i.next()).toElement(doc, expand)));
        element.appendChild(iRSSVectorsElement);
        Element iRSSCompositeVectorElement = doc.createElement("iRSSCompositeVector");
        if(getIRSSCompositeVector() != null)
            iRSSCompositeVectorElement.appendChild(getIRSSCompositeVector().toElement(doc, expand));
        element.appendChild(iRSSCompositeVectorElement);
        Element iRSSCompositeDistribElement = doc.createElement("iRSSCompositeDistrib");
        if(getIRSSCompositeDistrib() != null)
            iRSSCompositeDistribElement.appendChild(getIRSSCompositeDistrib().toElement(doc));
        element.appendChild(iRSSCompositeDistribElement);
        return element;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/datatypes/IRSSVectorList);
    private Vector iRSSVectors;
    private EventVector iRSSCompositeVector;
    private Distribution iRSSCompositeDistrib;

}
