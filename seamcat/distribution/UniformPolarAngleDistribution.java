// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   UniformPolarAngleDistribution.java

package org.seamcat.distribution;

import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            StandardDistribution, UniformDistribution, Bounds

public class UniformPolarAngleDistribution extends StandardDistribution
{

    public UniformPolarAngleDistribution()
    {
        super(6);
    }

    public UniformPolarAngleDistribution(double maxangle)
    {
        this();
        setMaxAngle(maxangle);
    }

    public String toString()
    {
        return (new StringBuilder()).append("Uniform Polar Angle distribution(").append(getMaxAngle()).append(")").toString();
    }

    public double trial()
    {
        double rU = (new UniformDistribution(-1D, 1.0D)).trial();
        double rA = 57.295779513082323D * Mathematics.asinD(rU * Mathematics.sinD((getMaxAngle() * 3.1415926535897931D) / 180D));
        return rA;
    }

    public Bounds getBounds()
    {
        return new Bounds(-getMaxAngle(), getMaxAngle(), true);
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.PolarAngle;
    }
}