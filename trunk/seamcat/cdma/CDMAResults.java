// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAResults.java

package org.seamcat.cdma;

import org.seamcat.model.NodeAttribute;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.model.core.EventVector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CDMAResults extends SeamcatComponent
{

    public CDMAResults()
    {
        cdmaInitialCapacity = new EventVector(true);
        cdmaInterferedCapacity = new EventVector(true);
        cdmaInitialOutage = new EventVector(true);
        cdmaInterferedOutage = new EventVector(true);
        cdmaTotalDroppedUsers = new EventVector(true);
    }

    public CDMAResults(Element element)
    {
        cdmaInitialCapacity = new EventVector((Element)element.getElementsByTagName("initial-capacity").item(0).getFirstChild());
        cdmaInterferedCapacity = new EventVector((Element)element.getElementsByTagName("interfered-capacity").item(0).getFirstChild());
        cdmaInitialOutage = new EventVector((Element)element.getElementsByTagName("initial-outage").item(0).getFirstChild());
        cdmaInterferedOutage = new EventVector((Element)element.getElementsByTagName("interfered-outage").item(0).getFirstChild());
        cdmaTotalDroppedUsers = new EventVector((Element)element.getElementsByTagName("total-outage").item(0).getFirstChild());
    }

    public Element toElement(Document doc)
    {
        Element elem = doc.createElement("CDMAResults");
        elem.setAttribute("average_loss", Double.toString(getAverageLoss()));
        Element initial = doc.createElement("initial-capacity");
        initial.appendChild(cdmaInitialCapacity.toElement(doc));
        elem.appendChild(initial);
        Element interfered = doc.createElement("interfered-capacity");
        interfered.appendChild(cdmaInterferedCapacity.toElement(doc));
        elem.appendChild(interfered);
        Element initOut = doc.createElement("initial-outage");
        initOut.appendChild(cdmaInitialOutage.toElement(doc));
        elem.appendChild(initOut);
        Element interOut = doc.createElement("interfered-outage");
        interOut.appendChild(cdmaInterferedOutage.toElement(doc));
        elem.appendChild(interOut);
        Element totalOut = doc.createElement("total-outage");
        totalOut.appendChild(cdmaTotalDroppedUsers.toElement(doc));
        elem.appendChild(totalOut);
        return elem;
    }

    public void trim()
    {
        cdmaInitialCapacity.trimToSize();
        cdmaInterferedCapacity.trimToSize();
        cdmaInitialOutage.trimToSize();
        cdmaInterferedOutage.trimToSize();
    }

    public EventVector getCdmaInitialCapacity()
    {
        return cdmaInitialCapacity;
    }

    public EventVector getCdmaInterferedCapacity()
    {
        return cdmaInterferedCapacity;
    }

    public void resetAllGeneratedResults()
    {
        try
        {
            cdmaInitialCapacity.clear();
            cdmaInterferedCapacity.clear();
            cdmaInitialOutage.clear();
            cdmaInterferedOutage.clear();
            cdmaTotalDroppedUsers.clear();
        }
        catch(IllegalStateException ile) { }
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public org.seamcat.presentation.Node getChildAt(int childIndex)
    {
        return null;
    }

    public int getChildCount()
    {
        return 0;
    }

    public boolean isLeaf()
    {
        return false;
    }

    public String toString()
    {
        return "CDMA Results";
    }

    protected void initNodeAttributes()
    {
        nodeAttributes = new NodeAttribute[1];
        nodeAttributes[0] = new NodeAttribute("CDMA System Capacity", "", this, "String", null, false, true, null);
    }

    public double getAverageLoss()
    {
        double avg = 0.0D;
        double noInterferenceCapacity[] = cdmaInitialCapacity.getEvents();
        double interferenceCapacity[] = cdmaInterferedCapacity.getEvents();
        int size = noInterferenceCapacity.length;
        if(size < 1)
            return 0.0D;
        for(int x = 0; x < size; x++)
            avg += 100D - (interferenceCapacity[x] / noInterferenceCapacity[x]) * 100D;

        return avg / (double)size;
    }

    public EventVector getCdmaInitialOutage()
    {
        return cdmaInitialOutage;
    }

    public EventVector getCdmaInterferedOutage()
    {
        return cdmaInterferedOutage;
    }

    public EventVector getCdmaTotalDroppedUsers()
    {
        return cdmaTotalDroppedUsers;
    }

    private EventVector cdmaInitialCapacity;
    private EventVector cdmaInterferedCapacity;
    private EventVector cdmaInitialOutage;
    private EventVector cdmaInterferedOutage;
    private EventVector cdmaTotalDroppedUsers;
}