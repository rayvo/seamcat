// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ContinousDistribution.java

package org.seamcat.distribution;

import java.util.List;
import java.util.Random;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Point2D;
import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            UserDistribution, Bounds

public class ContinousDistribution extends UserDistribution
{

    public ContinousDistribution()
    {
        super(1);
    }

    public ContinousDistribution(DiscreteFunction cdf)
    {
        super(1, cdf);
    }

    public double trial()
    {
        return getCdf().inverse(RND.nextDouble());
    }

    public String toString()
    {
        return "User defined distribution";
    }

    public Bounds getBounds()
    {
        if(getCdf().getPointsList().size() > 0)
            return new Bounds(((Point2D)getCdf().getPointsList().get(0)).getX(), ((Point2D)getCdf().getPointsList().get(getCdf().getPointsList().size() - 1)).getX(), true);
        else
            return new Bounds(0.0D, 0.0D, false);
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.UserDefined;
    }

    private static final Random RND = new Random();

}