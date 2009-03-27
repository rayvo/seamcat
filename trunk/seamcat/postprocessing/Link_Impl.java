// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Link_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.model.plugin.*;

public abstract class Link_Impl
    implements Link
{

    public Link_Impl()
    {
    }

    public double getFrequency()
    {
        return frequency;
    }

    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }

    public double getPathloss()
    {
        return pathloss;
    }

    public void setPathloss(double pathloss)
    {
        this.pathloss = pathloss;
    }

    public PropagationModel getPropagationModel()
    {
        return propagationModel;
    }

    public void setPropagationModel(PropagationModel propagationModel)
    {
        this.propagationModel = propagationModel;
    }

    public Transceiver getReceiver()
    {
        return receiver;
    }

    public void setReceiver(Transceiver receiver)
    {
        this.receiver = receiver;
    }

    public Transceiver getTransmitter()
    {
        return transmitter;
    }

    public void setTransmitter(Transceiver transmitter)
    {
        this.transmitter = transmitter;
    }

    public double getFrequencyMax()
    {
        return frequencyMax;
    }

    public double getFrequencyMin()
    {
        return frequencyMin;
    }

    public void setFrequencyMax(double frequencyMax)
    {
        this.frequencyMax = frequencyMax;
    }

    public void setFrequencyMin(double frequencyMin)
    {
        this.frequencyMin = frequencyMin;
    }

    protected Transceiver transmitter;
    protected Transceiver receiver;
    protected PropagationModel propagationModel;
    protected double pathloss;
    protected double frequency;
    protected double frequencyMax;
    protected double frequencyMin;
}
