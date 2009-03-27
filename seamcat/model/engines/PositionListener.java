// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PositionListener.java

package org.seamcat.model.engines;


public interface PositionListener
{

    public abstract void addWantedTransmitter(double d, double d1);

    public abstract void addWantedReceiver(double d, double d1);

    public abstract void addVictimReceiver(double d, double d1);

    public abstract void addInterferingTransmitter(double d, double d1);

    public abstract void clearAllElements();
}
