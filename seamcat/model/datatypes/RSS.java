// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RSS.java

package org.seamcat.model.datatypes;

import org.seamcat.model.SeamcatComponent;

public abstract class RSS extends SeamcatComponent
{

    public RSS(String name)
    {
        super(name, null);
    }

    public String toString()
    {
        return getReference();
    }
}
