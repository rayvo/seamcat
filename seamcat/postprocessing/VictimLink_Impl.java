// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VictimLink_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.model.plugin.*;

// Referenced classes of package org.seamcat.postprocessing:
//            Link_Impl, Transceiver_Impl, VictimReceiver_Impl

public class VictimLink_Impl extends Link_Impl
    implements VictimLink
{

    public VictimLink_Impl(boolean uw)
    {
        useWantedTransmitter = true;
        transmitter = new Transceiver_Impl();
        receiver = new VictimReceiver_Impl();
        useWantedTransmitter = uw;
    }

    public VictimReceiver getReceiver()
    {
        return (VictimReceiver)receiver;
    }

    public void setReceiver(VictimReceiver rec)
    {
        receiver = rec;
    }

    public double getDRSSValue()
    {
        return dRSSValue;
    }

    public void setDRSSValue(double value)
    {
        dRSSValue = value;
    }

    public boolean useWantedTransmitter()
    {
        return useWantedTransmitter;
    }

    public volatile Transceiver getReceiver()
    {
        return getReceiver();
    }

    protected double dRSSValue;
    protected boolean useWantedTransmitter;
}
