// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   UniformPolarDistanceDistribution.java

package org.seamcat.distribution;

import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            StandardDistribution, UniformDistribution, Bounds

public class UniformPolarDistanceDistribution extends StandardDistribution
{

    public UniformPolarDistanceDistribution()
    {
        super(5);
        u = new UniformDistribution(0.0D, 1.0D);
    }

    public UniformPolarDistanceDistribution(double maxdistance)
    {
        this();
        setMaxDistance(maxdistance);
        u = new UniformDistribution(0.0D, 1.0D);
    }

    public String toString()
    {
        return (new StringBuilder()).append("Uniform Polar Dist. Distri(").append(getMaxDistance()).append(")").toString();
    }

    public double trial()
    {
        return Math.sqrt(u.trial()) * getMaxDistance();
    }

    public Bounds getBounds()
    {
        return new Bounds(0.0D, getMaxDistance(), true);
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.PolarDistance;
    }

    private UniformDistribution u;
}