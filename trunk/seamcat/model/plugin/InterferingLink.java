// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferingLink.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            Link, InterferingTransmitter, PropagationModel

public interface InterferingLink
    extends Link
{

    public abstract InterferingTransmitter getTransmitter();

    public abstract boolean isSublink();

    public abstract double getIRSSUnwanted();

    public abstract double getIRSSBlocking();

    public abstract int getLinkIndex();

    public abstract int getOrigLinkIndex();

    public abstract PropagationModel getPropagationModelVR();
}
