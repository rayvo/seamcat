// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PluginModel.java

package org.seamcat.model.propagation;

import org.seamcat.propagation.PropagationModelConstants;

public interface PluginModel
    extends PropagationModelConstants
{

    public abstract double evaluate(double d, double d1, double d2, double d3, int i, double d4, double d5, double d6);
}
