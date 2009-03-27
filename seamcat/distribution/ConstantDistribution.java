// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ConstantDistribution.java

package org.seamcat.distribution;

import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            Distribution, Bounds

public class ConstantDistribution extends Distribution
{

    public ConstantDistribution()
    {
        super(0);
    }

    public ConstantDistribution(double constant)
    {
        this();
        setConstant(constant);
    }

    public double trial()
    {
        return constant;
    }

    public Bounds getBounds()
    {
        return new Bounds(constant, constant, true);
    }

    public String toString()
    {
        return (new StringBuilder()).append("Constant(").append(constant).append(")").toString();
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.Constant;
    }
}