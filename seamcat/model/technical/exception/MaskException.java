// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MaskException.java

package org.seamcat.model.technical.exception;


public class MaskException extends Exception
{

    public MaskException()
    {
    }

    public MaskException(String msg)
    {
        super(msg);
    }

    public MaskException(Exception e)
    {
        super(e);
    }
}
