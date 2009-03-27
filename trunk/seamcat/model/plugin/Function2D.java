// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Function2D.java

package org.seamcat.model.plugin;


public interface Function2D
{

    public abstract void addPoint(double d, double d1);

    public abstract double evaluate(double d)
        throws Exception;

    public abstract double integrate(double d, double d1)
        throws Exception;
}
