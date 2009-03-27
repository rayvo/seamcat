// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Link.java

package org.seamcat.model.core;

import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.distribution.UniformDistribution;
import org.seamcat.model.SeamcatComponent;
import org.w3c.dom.*;

public abstract class Link extends SeamcatComponent
{

    public Link(String name, String description)
    {
        super(name, description);
        txRxDistDistrib = new UniformDistribution(0.0D, 1.0D);
        txRxAngleDistrib = new UniformDistribution(0.0D, 360D);
    }

    public Link(Element element)
    {
        this(element.getAttribute("reference"), "");
        try
        {
            if(element.getElementsByTagName("description").item(0).getFirstChild() != null)
            {
                CDATASection datasection = (CDATASection)element.getElementsByTagName("description").item(0).getFirstChild();
                setDescription(datasection.getData());
            } else
            {
                setDescription("");
            }
        }
        catch(Exception ex)
        {
            setDescription("Error while reading description");
        }
        setRxTxAntennaGain(Double.parseDouble(element.getAttribute("rxTxAntennaGain")));
        setRxTxAzimuth(Double.parseDouble(element.getAttribute("rxTxAzimuth")));
        setRxTxElevation(Double.parseDouble(element.getAttribute("rxTxElevation")));
        setRxTxTilt(Double.parseDouble(element.getAttribute("rxTxTilt")));
        setTxRxAngle(Double.parseDouble(element.getAttribute("txRxAngle")));
        setTxRxAntennaGain(Double.parseDouble(element.getAttribute("txRxAntennaGain")));
        setTxRxAzimuth(Double.parseDouble(element.getAttribute("txRxAzimuth")));
        setTxRxDistance(Double.parseDouble(element.getAttribute("txRxDistance")));
        setTxRxElevation(Double.parseDouble(element.getAttribute("txRxElevation")));
        setTxRxPathLoss(Double.parseDouble(element.getAttribute("txRxPathLoss")));
        setTxRxTilt(Double.parseDouble(element.getAttribute("txRxTilt")));
    }

    public double getRxTxAzimuth()
    {
        return rxTxAzimuth;
    }

    public void setRxTxAzimuth(double rxTxAzimuth)
    {
        this.rxTxAzimuth = rxTxAzimuth;
    }

    public double getTxRxAzimuth()
    {
        return txRxAzimuth;
    }

    public void setTxRxAzimuth(double txRxAzimuth)
    {
        this.txRxAzimuth = txRxAzimuth;
    }

    public double getRxTxElevation()
    {
        return rxTxElevation;
    }

    public void setRxTxElevation(double rxTxElevation)
    {
        this.rxTxElevation = rxTxElevation;
    }

    public double getTxRxElevation()
    {
        return txRxElevation;
    }

    public void setTxRxElevation(double txRxElevation)
    {
        this.txRxElevation = txRxElevation;
    }

    public double getTxRxPathLoss()
    {
        return txRxPathLoss;
    }

    public void setTxRxPathLoss(double txRxPathLoss)
    {
        this.txRxPathLoss = txRxPathLoss;
    }

    public double getRxTxAntennaGain()
    {
        return rxTxAntennaGain;
    }

    public void setRxTxAntennaGain(double rxTxAntennaGain)
    {
        this.rxTxAntennaGain = rxTxAntennaGain;
    }

    public double getTxRxAntennaGain()
    {
        return txRxAntennaGain;
    }

    public void setTxRxAntennaGain(double txRxAntennaGain)
    {
        this.txRxAntennaGain = txRxAntennaGain;
    }

    public double getTxRxDistance()
    {
        return txRxDistance;
    }

    public void setTxRxDistance(double txRxDistance)
    {
        this.txRxDistance = txRxDistance;
    }

    public double getTxRxAngle()
    {
        return txRxAngle;
    }

    public void setTxRxAngle(double txRxAngle)
    {
        this.txRxAngle = txRxAngle;
    }

    public double getTxRxTilt()
    {
        return txRxTilt;
    }

    public void setTxRxTilt(double txRxTilt)
    {
        this.txRxTilt = txRxTilt;
    }

    public double getRxTxTilt()
    {
        return rxTxTilt;
    }

    public void setRxTxTilt(double rxTxTilt)
    {
        this.rxTxTilt = rxTxTilt;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("link");
        element.setAttribute("reference", getReference());
        element.setAttribute("rxTxAntennaGain", Double.toString(getRxTxAntennaGain()));
        element.setAttribute("rxTxAzimuth", Double.toString(getRxTxAzimuth()));
        element.setAttribute("rxTxElevation", Double.toString(getRxTxElevation()));
        element.setAttribute("rxTxTilt", Double.toString(getRxTxTilt()));
        element.setAttribute("txRxAngle", Double.toString(getTxRxAngle()));
        element.setAttribute("txRxAntennaGain", Double.toString(getTxRxAntennaGain()));
        element.setAttribute("txRxAzimuth", Double.toString(getTxRxAzimuth()));
        element.setAttribute("txRxDistance", Double.toString(getTxRxDistance()));
        element.setAttribute("txRxElevation", Double.toString(getTxRxElevation()));
        element.setAttribute("txRxPathLoss", Double.toString(getTxRxPathLoss()));
        element.setAttribute("txRxTilt", Double.toString(getTxRxTilt()));
        Element description = doc.createElement("description");
        description.appendChild(doc.createCDATASection(getDescription()));
        element.appendChild(description);
        return element;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/core/Link);
    private double rxTxAzimuth;
    private double txRxAzimuth;
    private double rxTxElevation;
    private double txRxElevation;
    private double txRxPathLoss;
    private double rxTxAntennaGain;
    private double txRxAntennaGain;
    private double txRxRelPosX;
    private double txRxRelPosY;
    private double txRxDistance;
    private double txRxAngle;
    private double txRxTilt;
    private double rxTxTilt;
    private boolean txRxTest;
    private Distribution txRxDistDistrib;
    private Distribution txRxAngleDistrib;

}
