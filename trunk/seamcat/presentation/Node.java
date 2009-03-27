// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Node.java

package org.seamcat.presentation;

import java.util.Enumeration;
import org.seamcat.model.NodeAttribute;

public interface Node
{

    public abstract Enumeration children();

    public abstract boolean getAllowsChildren();

    public abstract Node getChildAt(int i);

    public abstract int getChildCount();

    public abstract int getIndex(Node node);

    public abstract Node getParent();

    public abstract boolean isLeaf();

    public abstract NodeAttribute[] getNodeAttributes();
}
