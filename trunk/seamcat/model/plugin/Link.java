// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Link.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            Transceiver, PropagationModel

public interface Link
{

    public abstract double getPathloss();

    public abstract double getFrequency();

    public abstract double getFrequencyMax();

    public abstract double getFrequencyMin();

    public abstract Transceiver getReceiver();

    public abstract Transceiver getTransmitter();

    public abstract PropagationModel getPropagationModel();
}
