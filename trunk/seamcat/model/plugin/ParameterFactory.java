// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParameterFactory.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            PluginDistribution, Function2D, Function3D

public interface ParameterFactory
{

    public transient abstract PluginDistribution createDistribution(PluginDistribution.DistributionType distributiontype, double ad[]);

    public abstract Function2D createFunction2D();

    public abstract Function3D createFunction3D();
}
