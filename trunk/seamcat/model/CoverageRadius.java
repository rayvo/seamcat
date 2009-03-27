// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CoverageRadius.java

package org.seamcat.model;

import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model:
//            SeamcatComponent, NodeAttribute

public class CoverageRadius extends SeamcatComponent
{

    public CoverageRadius()
    {
        radiusText = new Vector();
        radiusValues = new Vector();
    }

    public CoverageRadius(Element elem)
    {
        this();
        NodeList nl = elem.getElementsByTagName("CoverageRadius");
        int i = 0;
        for(int stop = nl.getLength(); i < stop; i++)
        {
            Element el = (Element)nl.item(i);
            addCoverageRadius(el.getAttribute("name"), new Double(el.getAttribute("value")));
        }

    }

    public Element toElement(Document doc)
    {
        Element elem = doc.createElement("CoverageRadiuses");
        int i = 0;
        for(int stop = radiusText.size(); i < stop; i++)
        {
            Element el = doc.createElement("CoverageRadius");
            el.setAttribute("name", (String)radiusText.get(i));
            el.setAttribute("value", ((Double)radiusValues.get(i)).toString());
            elem.appendChild(el);
        }

        return elem;
    }

    public void reset()
    {
        radiusText.clear();
        radiusValues.clear();
    }

    public void addCoverageRadius(String name, Double value)
    {
        radiusText.add(name);
        radiusValues.add(value);
        nodeAttributeIsDirty = true;
    }

    public int getRowCount()
    {
        return radiusText.size();
    }

    public String toString()
    {
        return "Calculated Radius";
    }

    protected void initNodeAttributes()
    {
        nodeAttributes = new NodeAttribute[radiusText.size()];
        int i = 0;
        for(int stop = radiusText.size(); i < stop; i++)
            nodeAttributes[i] = new NodeAttribute(((String)radiusText.get(i)).toString(), "km", radiusValues.get(i), "Double", null, false, true, null);

    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/CoverageRadius);
    private List radiusText;
    private List radiusValues;

}