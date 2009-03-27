// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SummationException.java

package org.seamcat.model.technical.exception;


public class SummationException extends Exception
{

    public SummationException()
    {
    }

    public SummationException(Exception cause)
    {
        super(cause);
    }

    public SummationException(String msg)
    {
        super(msg);
    }
}
