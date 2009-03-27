// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VictimLink.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            Link, VictimReceiver

public interface VictimLink
    extends Link
{

    public abstract VictimReceiver getReceiver();

    public abstract double getDRSSValue();

    public abstract boolean useWantedTransmitter();
}
