// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IRSSList.java

package org.seamcat.model.datatypes;

import org.seamcat.model.SeamcatComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class IRSSList extends SeamcatComponent
{

    public IRSSList(String ref)
    {
        super(ref, "");
    }

    public String toString()
    {
        return getReference();
    }

    public Element toElement(Document doc)
    {
        return null;
    }
}
