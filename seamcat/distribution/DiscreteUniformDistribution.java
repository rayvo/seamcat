// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DiscreteUniformDistribution.java

package org.seamcat.distribution;

import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            StandardDistribution, Bounds, UniformDistribution

public class DiscreteUniformDistribution extends StandardDistribution
{

    public DiscreteUniformDistribution()
    {
        super(8);
    }

    public DiscreteUniformDistribution(double min, double max, double step)
    {
        this();
        setMin(min);
        setMax(max);
        setStep(step);
    }

    public String toString()
    {
        return (new StringBuilder()).append("DiscreteUniformDistribution(").append(getMin()).append(", ").append(getMax()).append(", ").append(getStep()).append(")").toString();
    }

    public double trial()
    {
        double rP = u.trial();
        double rMax = getMax();
        double rMin = getMin();
        double rStep = getStep();
        int n = (new Double((rMax - rMin) / rStep)).intValue();
        double rPi = 1.0D / (double)n;
        int i;
        for(i = 0; i < n && rPi < rP; i++)
            rPi += 1.0D / (double)n;

        return rMin + rStep / 2D + (double)i * rStep;
    }

    public Bounds getBounds()
    {
        return new Bounds(getMin(), getMax(), true);
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.DiscreteUniform;
    }

    private static final UniformDistribution u = new UniformDistribution(0.0D, 1.0D);

}