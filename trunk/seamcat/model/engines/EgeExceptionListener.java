// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EgeExceptionListener.java

package org.seamcat.model.engines;


public interface EgeExceptionListener
{

    public abstract boolean notifyException(Exception exception, int i);
}
