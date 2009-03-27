// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Interference.java

package org.seamcat.model.core;

import org.seamcat.model.datatypes.IRSSVector;
import org.w3c.dom.*;

public class Interference
{

    public Interference()
    {
    }

    public Interference(Element element)
    {
        NodeList nl = element.getElementsByTagName("vector");
        if(nl.getLength() > 0)
            iRSSVector = new IRSSVector((Element)nl.item(0).getFirstChild());
    }

    public IRSSVector getIRSSVector()
    {
        return iRSSVector;
    }

    public void setIRSSVector(IRSSVector iRSSVector)
    {
        this.iRSSVector = iRSSVector;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("interference");
        if(getIRSSVector() != null)
        {
            Element iRSSVectorElement = doc.createElement("vector");
            iRSSVectorElement.appendChild(iRSSVector.toElement(doc));
            doc.appendChild(iRSSVectorElement);
        }
        return element;
    }

    private IRSSVector iRSSVector;
}