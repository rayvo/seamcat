// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferingLink_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.model.plugin.*;

// Referenced classes of package org.seamcat.postprocessing:
//            Link_Impl, InterferingTransmitter_Impl, Transceiver_Impl

public class InterferingLink_Impl extends Link_Impl
    implements InterferingLink
{

    public InterferingLink_Impl()
    {
        transmitter = new InterferingTransmitter_Impl();
        receiver = new Transceiver_Impl();
    }

    public InterferingTransmitter getTransmitter()
    {
        return (InterferingTransmitter)transmitter;
    }

    public void setTransmitter(InterferingTransmitter tra)
    {
        transmitter = tra;
    }

    public double getIRSSUnwanted()
    {
        return iRSSUnwanted;
    }

    public double getIRSSBlocking()
    {
        return iRSSBlocking;
    }

    public boolean isSublink()
    {
        return sublink;
    }

    public void setIRSSBlocking(double blocking)
    {
        iRSSBlocking = blocking;
    }

    public void setIRSSUnwanted(double unwanted)
    {
        iRSSUnwanted = unwanted;
    }

    public void setSublink(boolean sublink)
    {
        this.sublink = sublink;
    }

    public int getLinkIndex()
    {
        return linkIndex;
    }

    public void setLinkIndex(int linkIndex)
    {
        this.linkIndex = linkIndex;
    }

    public int getOrigLinkIndex()
    {
        return origLinkIndex;
    }

    public void setOrigLinkIndex(int origLinkIndex)
    {
        this.origLinkIndex = origLinkIndex;
    }

    public PropagationModel getPropagationModelVR()
    {
        return propagationModelVR;
    }

    public void setPropagationModelVR(PropagationModel propagationModelVR)
    {
        this.propagationModelVR = propagationModelVR;
    }

    public volatile Transceiver getTransmitter()
    {
        return getTransmitter();
    }

    private int linkIndex;
    private int origLinkIndex;
    private double iRSSUnwanted;
    private double iRSSBlocking;
    private boolean sublink;
    protected PropagationModel propagationModelVR;
}
