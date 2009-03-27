// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Signals.java

package org.seamcat.model.core;

import java.util.Iterator;
import java.util.List;
import org.seamcat.model.NodeAttribute;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.model.datatypes.DRSSDistrib;
import org.seamcat.model.datatypes.DRSSVector;
import org.seamcat.model.datatypes.IRSSDistribList;
import org.seamcat.model.datatypes.IRSSVector;
import org.seamcat.model.datatypes.IRSSVectorList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model.core:
//            InterferingSystemLink, EventVector

public class Signals extends SeamcatComponent
{

    public Signals()
    {
        victimIsCdma = false;
        dRSSDistrib = new DRSSDistrib("dRSS Distribution");
        iRSSDistribListUnwanted = new IRSSDistribList("iRSS Distribution Unwanted");
        iRSSDistribListBlocking = new IRSSDistribList("iRSS Distribution Blocking");
        iRSSDistribListIntermodulation = new IRSSDistribList("iRSS Distribution Intermodulation");
        iRSSVectorListUnwanted = new IRSSVectorList("iRSS Unwanted");
        iRSSVectorListBlocking = new IRSSVectorList("iRSS Blocking");
        iRSSVectorListIntermodulation = new IRSSVectorList("iRSS Intermodulation");
        dRSSVector = new DRSSVector("dRSS Vector (dBm)");
        iRSSVectorListUnwanted.addIRSSVector(new IRSSVector("Summation Vector"));
        iRSSVectorListBlocking.addIRSSVector(new IRSSVector("Summation Vector"));
        iRSSVectorListIntermodulation.addIRSSVector(new IRSSVector("Summation Vector"));
    }

    public Signals(Element element)
    {
        victimIsCdma = false;
        victimIsCdma = element.getAttribute("victimIsCdma").equalsIgnoreCase("true");
        dRSSDistrib = new DRSSDistrib((Element)element.getElementsByTagName("DRSSDistrib").item(0));
        iRSSDistribListUnwanted = new IRSSDistribList((Element)element.getElementsByTagName("iRSSDistribListUnwanted").item(0).getFirstChild());
        iRSSDistribListBlocking = new IRSSDistribList((Element)element.getElementsByTagName("iRSSDistribListBlocking").item(0).getFirstChild());
        iRSSDistribListIntermodulation = new IRSSDistribList((Element)element.getElementsByTagName("iRSSDistribListIntermodulation").item(0).getFirstChild());
        iRSSVectorListUnwanted = new IRSSVectorList((Element)element.getElementsByTagName("iRSSVectorListUnwanted").item(0).getFirstChild());
        iRSSVectorListBlocking = new IRSSVectorList((Element)element.getElementsByTagName("iRSSVectorListBlocking").item(0).getFirstChild());
        iRSSVectorListIntermodulation = new IRSSVectorList((Element)element.getElementsByTagName("iRSSVectorListIntermodulation").item(0).getFirstChild());
        dRSSVector = new DRSSVector((Element)element.getElementsByTagName("DRSSVector").item(0));
    }

    public void generateResultVectors(InterferingSystemLink interferer)
    {
        iRSSVectorListUnwanted.addIRSSVector(new IRSSVector((new StringBuilder()).append("iRSS unwanted - ").append(interferer.getReference()).toString()));
        iRSSVectorListBlocking.addIRSSVector(new IRSSVector((new StringBuilder()).append("iRSS blocking - ").append(interferer.getReference()).toString()));
        iRSSVectorListIntermodulation.addIRSSVector(new IRSSVector((new StringBuilder()).append("iRSS intermodulation - ").append(interferer.getReference()).toString()));
    }

    public void adjustForCDMAVictim()
    {
        victimIsCdma = true;
        IRSSVector unw = (IRSSVector)iRSSVectorListUnwanted.getIRSSVectors().get(0);
        unw.setReference("External Interfernce, Unwanted");
        iRSSVectorListUnwanted.removeAll();
        iRSSVectorListUnwanted.addIRSSVector(unw);
        IRSSVector blo = (IRSSVector)iRSSVectorListBlocking.getIRSSVectors().get(0);
        blo.setReference("External Interference, Selectivity");
        iRSSVectorListBlocking.removeAll();
        iRSSVectorListBlocking.addIRSSVector(blo);
        iRSSVectorListBlocking.setReference("iRSS Selectivity");
    }

    public void resetAllGeneratedSignales()
    {
        try
        {
            iRSSVectorListBlocking.getIRSSCompositeVector().clear();
        }
        catch(Exception ex) { }
        try
        {
            List l = iRSSVectorListBlocking.getIRSSVectors();
            for(int i = 0; i < l.size(); i++)
                ((IRSSVector)l.get(i)).getEventVector().clear();

        }
        catch(Exception ex) { }
        try
        {
            List l = iRSSVectorListUnwanted.getIRSSVectors();
            for(int i = 0; i < l.size(); i++)
                ((IRSSVector)l.get(i)).getEventVector().clear();

        }
        catch(Exception ex) { }
        try
        {
            List l = iRSSVectorListIntermodulation.getIRSSVectors();
            for(int i = 0; i < l.size(); i++)
                ((IRSSVector)l.get(i)).getEventVector().clear();

        }
        catch(Exception ex) { }
        try
        {
            dRSSVector.getEventVector().clear();
        }
        catch(Exception ex) { }
    }

    public void testIRSSDistributions()
    {
        int sum = iRSSDistribListUnwanted.getIRSSDistribs().size() + iRSSDistribListBlocking.getIRSSDistribs().size() + iRSSDistribListIntermodulation.getIRSSDistribs().size();
        setSignalIsIrssDistribution(sum > 0);
    }

    public void setSignalCoverageRadius(double radius)
    {
        signalCoverageRadius = radius;
    }

    public void setSignalIsDrssDistribution(boolean signalIsDrssDistribution)
    {
        this.signalIsDrssDistribution = signalIsDrssDistribution;
    }

    public void setSignalIsIrssDistribution(boolean signalIsIrssDistribution)
    {
        this.signalIsIrssDistribution = signalIsIrssDistribution;
    }

    public double getSignalCoverageRadius()
    {
        return signalCoverageRadius;
    }

    public DRSSDistrib getDRSSDistrib()
    {
        return dRSSDistrib;
    }

    public IRSSDistribList getIRSSDistribListUnwanted()
    {
        return iRSSDistribListUnwanted;
    }

    public IRSSDistribList getIRSSDistribListBlocking()
    {
        return iRSSDistribListBlocking;
    }

    public IRSSDistribList getIRSSDistribListIntermodulation()
    {
        return iRSSDistribListIntermodulation;
    }

    public DRSSVector getDRSSVector()
    {
        return dRSSVector;
    }

    public IRSSVectorList getIRSSVectorListUnwanted()
    {
        return iRSSVectorListUnwanted;
    }

    public IRSSVectorList getIRSSVectorListIntermodulation()
    {
        return iRSSVectorListIntermodulation;
    }

    public IRSSVectorList getIRSSVectorListBlocking()
    {
        return iRSSVectorListBlocking;
    }

    public boolean isSignalIsDrssDistribution()
    {
        return signalIsDrssDistribution;
    }

    public boolean isSignalIsIrssDistribution()
    {
        return signalIsIrssDistribution;
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public org.seamcat.presentation.Node getChildAt(int childIndex)
    {
        if(victimIsCdma)
            switch(childIndex)
            {
            case 0: // '\0'
                return iRSSVectorListUnwanted;

            case 1: // '\001'
                return iRSSVectorListBlocking;
            }
        else
            switch(childIndex)
            {
            case 0: // '\0'
                return dRSSVector;

            case 1: // '\001'
                return iRSSVectorListUnwanted;

            case 2: // '\002'
                return iRSSVectorListBlocking;

            case 3: // '\003'
                return iRSSVectorListIntermodulation;
            }
        return null;
    }

    public int getChildCount()
    {
        return !victimIsCdma ? 4 : 2;
    }

    public boolean isLeaf()
    {
        return false;
    }

    public String toString()
    {
        return "Generated Signals";
    }

    protected void initNodeAttributes()
    {
        nodeAttributes = new NodeAttribute[1];
        nodeAttributes[0] = new NodeAttribute("Reference", "", getReference(), "String", null, false, true, null);
    }

    public Element toElement(Document doc)
    {
        return toElement(doc, true);
    }

    public Element toElement(Document doc, boolean expand)
    {
        Element element = doc.createElement("Signals");
        element.setAttribute("victimIsCdma", Boolean.toString(victimIsCdma));
        element.appendChild(getDRSSDistrib().toElement(doc));
        element.appendChild(getDRSSVector().toElement(doc, expand));
        Element iRSSDistribListBlockingElement = doc.createElement("iRSSDistribListBlocking");
        iRSSDistribListBlockingElement.appendChild(getIRSSDistribListBlocking().toElement(doc));
        element.appendChild(iRSSDistribListBlockingElement);
        Element iRSSDistribListIntermodulationElement = doc.createElement("iRSSDistribListIntermodulation");
        iRSSDistribListIntermodulationElement.appendChild(getIRSSDistribListIntermodulation().toElement(doc));
        element.appendChild(iRSSDistribListIntermodulationElement);
        Element iRSSDistribListUnwantedElement = doc.createElement("iRSSDistribListUnwanted");
        iRSSDistribListUnwantedElement.appendChild(getIRSSDistribListUnwanted().toElement(doc));
        element.appendChild(iRSSDistribListUnwantedElement);
        Element iRSSVectorListBlockingElement = doc.createElement("iRSSVectorListBlocking");
        iRSSVectorListBlockingElement.appendChild(getIRSSVectorListBlocking().toElement(doc, expand));
        element.appendChild(iRSSVectorListBlockingElement);
        Element iRSSVectorListIntermodulationElement = doc.createElement("iRSSVectorListIntermodulation");
        iRSSVectorListIntermodulationElement.appendChild(getIRSSVectorListIntermodulation().toElement(doc, expand));
        element.appendChild(iRSSVectorListIntermodulationElement);
        Element iRSSVectorListUnwantedElement = doc.createElement("iRSSVectorListUnwanted");
        iRSSVectorListUnwantedElement.appendChild(getIRSSVectorListUnwanted().toElement(doc, expand));
        element.appendChild(iRSSVectorListUnwantedElement);
        return element;
    }

    public boolean isVictimIsCdma()
    {
        return victimIsCdma;
    }

    public void trim()
    {
        IRSSVector vector;
        for(Iterator i$ = iRSSVectorListUnwanted.getIRSSVectors().iterator(); i$.hasNext(); vector.getEventVector().trimToSize())
            vector = (IRSSVector)i$.next();

        IRSSVector vector;
        for(Iterator i$ = iRSSVectorListBlocking.getIRSSVectors().iterator(); i$.hasNext(); vector.getEventVector().trimToSize())
            vector = (IRSSVector)i$.next();

        IRSSVector vector;
        for(Iterator i$ = iRSSVectorListIntermodulation.getIRSSVectors().iterator(); i$.hasNext(); vector.getEventVector().trimToSize())
            vector = (IRSSVector)i$.next();

        dRSSVector.getEventVector().trimToSize();
    }

    private DRSSDistrib dRSSDistrib;
    private final IRSSDistribList iRSSDistribListUnwanted;
    private final IRSSDistribList iRSSDistribListBlocking;
    private final IRSSDistribList iRSSDistribListIntermodulation;
    private final IRSSVectorList iRSSVectorListUnwanted;
    private final IRSSVectorList iRSSVectorListBlocking;
    private final IRSSVectorList iRSSVectorListIntermodulation;
    private final DRSSVector dRSSVector;
    private double signalCoverageRadius;
    private boolean signalIsDrssDistribution;
    private boolean signalIsIrssDistribution;
    private boolean victimIsCdma;
}
