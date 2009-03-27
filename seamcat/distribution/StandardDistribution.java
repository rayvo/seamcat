// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StandardDistribution.java

package org.seamcat.distribution;


// Referenced classes of package org.seamcat.distribution:
//            NonConstantDistribution

public abstract class StandardDistribution extends NonConstantDistribution
{

    public StandardDistribution(int type)
    {
        super(type);
    }

    public StandardDistribution(int type, double mean, double stddev)
    {
        this(type);
        setMean(mean);
        setStdDev(stddev);
    }
}