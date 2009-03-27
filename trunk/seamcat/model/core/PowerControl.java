// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PowerControl.java

package org.seamcat.model.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PowerControl
{

    public PowerControl()
    {
        this(2D, -103D, 6D, 0.0D);
    }

    public PowerControl(Element element)
    {
        this(Double.parseDouble(element.getAttribute("pcstep")), Double.parseDouble(element.getAttribute("pcmin")), Double.parseDouble(element.getAttribute("pcrange")), Double.parseDouble(element.getAttribute("itpcgain")));
    }

    public PowerControl(PowerControl pc)
    {
        setPowerControlMinimum(pc.getPowerControlMinimum());
        setPowerControlRange(pc.getPowerControlRange());
        setPowerControlStep(pc.getPowerControlStep());
        setInterferingTransmitterPowerControlGain(pc.getInterferingTransmitterPowerControlGain());
    }

    public PowerControl(double pcstep, double pcmin, double pcrange, double itpcgain)
    {
        setPowerControlStep(pcstep);
        setPowerControlMinimum(pcmin);
        setPowerControlRange(pcrange);
        setInterferingTransmitterPowerControlGain(itpcgain);
    }

    public double getPowerControlStep()
    {
        return pcStep;
    }

    public void setPowerControlStep(double pcStep)
    {
        this.pcStep = pcStep;
    }

    public double getPowerControlMinimum()
    {
        return pcMin;
    }

    public void setPowerControlMinimum(double pcMin)
    {
        this.pcMin = pcMin;
    }

    public double getPowerControlRange()
    {
        return pcRange;
    }

    public void setPowerControlRange(double pcRange)
    {
        this.pcRange = pcRange;
    }

    public double getInterferingTransmitterPowerControlGain()
    {
        return itPCGain;
    }

    public void setInterferingTransmitterPowerControlGain(double itPCGain)
    {
        this.itPCGain = itPCGain;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("powercontrol");
        element.setAttribute("pcstep", Double.toString(pcStep));
        element.setAttribute("pcmin", Double.toString(pcMin));
        element.setAttribute("pcrange", Double.toString(pcRange));
        element.setAttribute("itpcgain", Double.toString(itPCGain));
        return element;
    }

    private double pcStep;
    private double pcMin;
    private double pcRange;
    private double itPCGain;
}
