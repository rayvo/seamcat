// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SystemLink.java

package org.seamcat.model.core;

import org.apache.log4j.Logger;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.TransmitterToReceiverPath;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.core:
//            Link

public abstract class SystemLink extends Link
{

    public SystemLink(Element element)
    {
        super((Element)element.getElementsByTagName("link").item(0));
        transmitterToReceiverElevation = new ConstantDistribution(0.0D);
        transmitterToReceiverAzimuth = new ConstantDistribution(0.0D);
        receiverToTransmitterAzimuth = new ConstantDistribution(0.0D);
        receiverToTransmitterElevation = new ConstantDistribution(0.0D);
        wt2VrPath = new TransmitterToReceiverPath();
        setTxRxHorTol(Double.parseDouble(element.getAttribute("txRxHorTol")));
        setRxTxHorTol(Double.parseDouble(element.getAttribute("rxTxHorTol")));
        setTxRxVerTol(Double.parseDouble(element.getAttribute("txRxVerTol")));
        setRxTxVerTol(Double.parseDouble(element.getAttribute("rxTxVerTol")));
        NodeList nodes = element.getElementsByTagName("distribution");
        setTransmitterToReceiverElevation(Distribution.create((Element)nodes.item(0)));
        setTransmitterToReceiverAzimuth(Distribution.create((Element)nodes.item(1)));
        setReceiverToTransmitterAzimuth(Distribution.create((Element)nodes.item(2)));
        setReceiverToTransmitterElevation(Distribution.create((Element)nodes.item(3)));
        try
        {
            wt2VrPath = new TransmitterToReceiverPath((Element)element.getElementsByTagName("TransmitterToReceiverPath").item(0));
        }
        catch(Exception e)
        {
            LOG.warn("Could not load wt2vr path", e);
        }
    }

    public SystemLink(String name, String description)
    {
        super(name, description);
        transmitterToReceiverElevation = new ConstantDistribution(0.0D);
        transmitterToReceiverAzimuth = new ConstantDistribution(0.0D);
        receiverToTransmitterAzimuth = new ConstantDistribution(0.0D);
        receiverToTransmitterElevation = new ConstantDistribution(0.0D);
        wt2VrPath = new TransmitterToReceiverPath();
    }

    public TransmitterToReceiverPath getWt2VrPath()
    {
        return wt2VrPath;
    }

    public double getTxRxHorTol()
    {
        return txRxHorTol;
    }

    public void setTxRxHorTol(double txRxHorTol)
    {
        this.txRxHorTol = txRxHorTol;
    }

    public double getRxTxHorTol()
    {
        return rxTxHorTol;
    }

    public void setRxTxHorTol(double rxTxHorTol)
    {
        this.rxTxHorTol = rxTxHorTol;
    }

    public double getTxRxVerTol()
    {
        return txRxVerTol;
    }

    public void setTxRxVerTol(double txRxVerTol)
    {
        this.txRxVerTol = txRxVerTol;
    }

    public double getRxTxVerTol()
    {
        return rxTxVerTol;
    }

    public void setRxTxVerTol(double rxTxVerTol)
    {
        this.rxTxVerTol = rxTxVerTol;
    }

    public Distribution getTransmitterToReceiverElevation()
    {
        return transmitterToReceiverElevation;
    }

    public void setTransmitterToReceiverElevation(Distribution txRxElevDistrib)
    {
        transmitterToReceiverElevation = txRxElevDistrib;
    }

    public Distribution getTransmitterToReceiverAzimuth()
    {
        return transmitterToReceiverAzimuth;
    }

    public void setTransmitterToReceiverAzimuth(Distribution txRxAzDistrib)
    {
        transmitterToReceiverAzimuth = txRxAzDistrib;
    }

    public Distribution getReceiverToTransmitterAzimuth()
    {
        return receiverToTransmitterAzimuth;
    }

    public void setReceiverToTransmitterAzimuth(Distribution rxTxAzDistrib)
    {
        receiverToTransmitterAzimuth = rxTxAzDistrib;
    }

    public Distribution getReceiverToTransmitterElevation()
    {
        return receiverToTransmitterElevation;
    }

    public void setReceiverToTransmitterElevation(Distribution rxTxElevDistrib)
    {
        receiverToTransmitterElevation = rxTxElevDistrib;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("systemLink");
        element.setAttribute("txRxHorTol", Double.toString(getTxRxHorTol()));
        element.setAttribute("rxTxHorTol", Double.toString(getRxTxHorTol()));
        element.setAttribute("txRxVerTol", Double.toString(getTxRxVerTol()));
        element.setAttribute("rxTxVerTol", Double.toString(getRxTxVerTol()));
        Element txRxElevationElement = doc.createElement("txRxElevation");
        txRxElevationElement.appendChild(transmitterToReceiverElevation.toElement(doc));
        element.appendChild(txRxElevationElement);
        Element txRxAzimuthElement = doc.createElement("txRxAzimuth");
        txRxAzimuthElement.appendChild(transmitterToReceiverAzimuth.toElement(doc));
        element.appendChild(txRxAzimuthElement);
        Element rxTxAzimuthElement = doc.createElement("rxTxAzimuth");
        rxTxAzimuthElement.appendChild(receiverToTransmitterAzimuth.toElement(doc));
        element.appendChild(rxTxAzimuthElement);
        Element rxTxElevationElement = doc.createElement("rxTxElevation");
        rxTxElevationElement.appendChild(receiverToTransmitterElevation.toElement(doc));
        element.appendChild(rxTxElevationElement);
        element.appendChild(wt2VrPath.toElement(doc));
        element.appendChild(super.toElement(doc));
        return element;
    }

    public void setWt2VrPath(TransmitterToReceiverPath wt2VrPath)
    {
        this.wt2VrPath = wt2VrPath;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/core/SystemLink);
    private double txRxHorTol;
    private double rxTxHorTol;
    private double txRxVerTol;
    private double rxTxVerTol;
    private Distribution transmitterToReceiverElevation;
    private Distribution transmitterToReceiverAzimuth;
    private Distribution receiverToTransmitterAzimuth;
    private Distribution receiverToTransmitterElevation;
    protected TransmitterToReceiverPath wt2VrPath;

}
