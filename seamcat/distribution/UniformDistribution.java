// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   UniformDistribution.java

package org.seamcat.distribution;

import java.util.Random;
import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            StandardDistribution, Bounds

public class UniformDistribution extends StandardDistribution
{

    public UniformDistribution()
    {
        this(0.0D, 1.0D);
    }

    public UniformDistribution(double min, double max)
    {
        super(2);
        if(max < min)
        {
            throw new IllegalArgumentException("Max cannot be < (less than) min");
        } else
        {
            setMin(min);
            setMax(max);
            recalcRange();
            return;
        }
    }

    public String toString()
    {
        return (new StringBuilder()).append("UniformDistribution(").append(getMin()).append(", ").append(getMax()).append(")").toString();
    }

    public double trial()
    {
        return RND.nextDouble() * range + getMin();
    }

    public Bounds getBounds()
    {
        return new Bounds(getMin(), getMax(), true);
    }

    public void setMin(double min)
    {
        super.setMin(min);
        recalcRange();
    }

    public void setMax(double max)
    {
        super.setMax(max);
        recalcRange();
    }

    private void recalcRange()
    {
        range = getMax() - getMin();
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.Uniform;
    }

    private static final Random RND = new Random();
    private double range;

}