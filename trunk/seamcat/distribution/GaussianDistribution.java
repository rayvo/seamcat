// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GaussianDistribution.java

package org.seamcat.distribution;

import java.util.Random;
import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            StandardDistribution, Bounds

public class GaussianDistribution extends StandardDistribution
{

    public GaussianDistribution()
    {
        super(3);
    }

    public GaussianDistribution(double mean, double stddev)
    {
        this();
        setMean(mean);
        setStdDev(stddev);
    }

    public String toString()
    {
        return (new StringBuilder()).append("Gausian Distribution(").append(getMean()).append(", ").append(getStdDev()).append(")").toString();
    }

    public double trial()
    {
        double rU;
        double rS;
        do
        {
            rU = 2D * RND.nextDouble() - 1.0D;
            double rV = 2D * RND.nextDouble() - 1.0D;
            rS = rU * rU + rV * rV;
        } while(rS >= 1.0D || rS == 0.0D);
        double rG = getMean() + getStdDev() * rU * Math.sqrt((-2D * Math.log(rS)) / rS);
        return rG;
    }

    public Bounds getBounds()
    {
        return BOUNDS;
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.Gaussian;
    }

    private static final Bounds BOUNDS = new Bounds(0.0D, 0.0D, false);
    private static final Random RND = new Random(System.currentTimeMillis());

}