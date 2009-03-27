// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StringedObject.java

package org.seamcat.presentation;


public class StringedObject
{

    public StringedObject(Object obj, String text)
    {
        this.text = text;
        object = obj;
    }

    public StringedObject(Object obj)
    {
        this(obj, "");
    }

    public StringedObject(String text)
    {
        this(null, text);
    }

    public StringedObject()
    {
        this((Object)null);
    }

    public Object getObject()
    {
        return object;
    }

    public void setObject(Object obj)
    {
        object = obj;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public String toString()
    {
        return text;
    }

    private String text;
    private Object object;
}
