// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   RayleighDistribution.java

package org.seamcat.distribution;

import java.util.Random;
import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            StandardDistribution, Bounds

public class RayleighDistribution extends StandardDistribution
{

    public RayleighDistribution()
    {
        super(4);
    }

    public RayleighDistribution(double min, double stddev)
    {
        this();
        setMin(min);
        setStdDev(stddev);
    }

    public String toString()
    {
        return (new StringBuilder()).append("RayleighDistribution(").append(getMin()).append(", ").append(getStdDev()).append(")").toString();
    }

    public double trial()
    {
        double rS;
        do
        {
            double rU = 2D * RND.nextDouble() - 1.0D;
            double rV = 2D * RND.nextDouble() - 1.0D;
            rS = rU * rU + rV * rV;
        } while(rS >= 1.0D || rS == 0.0D);
        double rR0 = Math.sqrt(-2D * Math.log(rS));
        double rSigma = getStdDev() / Math.sqrt(0.42920367320510344D);
        double rR = getMin() + rSigma * rR0;
        return rR;
    }

    public Bounds getBounds()
    {
        return new Bounds(0.0D, 0.0D, false);
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.Rayleigh;
    }

    private static final Random RND = new Random();

}