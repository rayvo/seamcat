// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PatternException.java

package org.seamcat.model.technical.exception;


public class PatternException extends Exception
{

    public PatternException()
    {
    }

    public PatternException(Exception e)
    {
        super(e);
    }

    public PatternException(String msg, Exception e)
    {
        super(msg, e);
    }

    public PatternException(String msg)
    {
        super(msg);
    }
}
