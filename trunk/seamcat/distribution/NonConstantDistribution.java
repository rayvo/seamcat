// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   NonConstantDistribution.java

package org.seamcat.distribution;


// Referenced classes of package org.seamcat.distribution:
//            Distribution

public abstract class NonConstantDistribution extends Distribution
{

    public NonConstantDistribution(int type)
    {
        this(type, false);
    }

    public NonConstantDistribution(int type, boolean normalized)
    {
        super(type);
        setNormalized(normalized);
    }

    public void setNormalized(boolean normalized)
    {
        this.normalized = normalized;
    }

    public boolean isNormalized()
    {
        return normalized;
    }

    private boolean normalized;
}