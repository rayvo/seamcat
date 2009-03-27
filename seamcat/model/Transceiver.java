// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Transceiver.java

package org.seamcat.model;

import org.apache.log4j.Logger;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model:
//            SeamcatComponent, Antenna

public abstract class Transceiver extends SeamcatComponent
{

    public Transceiver()
    {
        x = 0.0D;
        y = 0.0D;
        refAntennaHeight = 0.0D;
        antennaHeight = new ConstantDistribution(10D);
        antenna = new Antenna();
    }

    public Transceiver(Element element)
    {
        x = 0.0D;
        y = 0.0D;
        refAntennaHeight = 0.0D;
        antennaHeight = new ConstantDistribution(10D);
        antenna = new Antenna();
        if(element != null)
        {
            if(element.getElementsByTagName("description").item(0).getFirstChild() != null)
            {
                CDATASection datasection = (CDATASection)element.getElementsByTagName("description").item(0).getFirstChild();
                setDescription(datasection.getData());
            } else
            {
                setDescription("");
            }
            if(element.getElementsByTagName("antenna-height").item(0).getFirstChild() != null)
            {
                setAntennaHeight(Distribution.create((Element)element.getElementsByTagName("antenna-height").item(0).getFirstChild()));
            } else
            {
                LOG.warn("Could not load antenna height distribution");
                setAntennaHeight(new ConstantDistribution());
            }
            try
            {
                setAntenna(new Antenna((Element)element.getElementsByTagName("antenna").item(0)));
            }
            catch(Exception e)
            {
                setAntenna(new Antenna());
                LOG.warn("Antenna could not be loaded");
            }
            try
            {
                setReference(element.getAttribute("reference"));
                setRefAntennaHeight(Double.parseDouble(element.getAttribute("refAntennaHeight")));
                setX(Double.parseDouble(element.getAttribute("x")));
                setY(Double.parseDouble(element.getAttribute("y")));
            }
            catch(Exception e)
            {
                LOG.warn("One or more attributes could be loaded");
            }
        } else
        {
            LOG.warn("Missing transceiver tag in element");
        }
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void translateX(double x)
    {
        this.x += x;
    }

    public void translateY(double y)
    {
        this.y += y;
    }

    public double getRefAntennaHeight()
    {
        return refAntennaHeight;
    }

    public void setRefAntennaHeight(double refAntennaHeight)
    {
        this.refAntennaHeight = refAntennaHeight;
    }

    public Distribution getAntennaHeight()
    {
        return antennaHeight;
    }

    public void setAntennaHeight(Distribution antennaHeight)
    {
        this.antennaHeight = antennaHeight;
        nodeAttributeIsDirty = true;
    }

    public Antenna getAntenna()
    {
        return antenna;
    }

    public void setAntenna(Antenna value)
    {
        antenna = value;
    }

    protected abstract void initNodeAttributes();

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("transceiver");
        element.setAttribute("reference", getReference());
        element.setAttribute("refAntennaHeight", Double.toString(refAntennaHeight));
        element.setAttribute("x", Double.toString(x));
        element.setAttribute("y", Double.toString(y));
        Element description = doc.createElement("description");
        description.appendChild(doc.createCDATASection(getDescription()));
        element.appendChild(description);
        Element antennaHeightElement = doc.createElement("antenna-height");
        antennaHeightElement.appendChild(antennaHeight.toElement(doc));
        element.appendChild(antennaHeightElement);
        element.appendChild(antenna.toElement(doc));
        return element;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/Transceiver);
    private double x;
    private double y;
    private double refAntennaHeight;
    private Distribution antennaHeight;
    private Antenna antenna;

}