// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   UserDistribution.java

package org.seamcat.distribution;

import org.seamcat.function.DiscreteFunction;

// Referenced classes of package org.seamcat.distribution:
//            NonConstantDistribution

public abstract class UserDistribution extends NonConstantDistribution
{

    public UserDistribution(int type)
    {
        this(type, new DiscreteFunction());
    }

    public UserDistribution(int type, DiscreteFunction cdf)
    {
        super(type);
        setCdf(cdf);
    }

    public void setCdf(DiscreteFunction cdf)
    {
        this.cdf = cdf;
    }

    public DiscreteFunction getCdf()
    {
        return cdf;
    }

    private DiscreteFunction cdf;
}