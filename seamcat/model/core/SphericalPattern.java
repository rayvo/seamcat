// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SphericalPattern.java

package org.seamcat.model.core;

import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model.core:
//            AntennaPattern

public class SphericalPattern extends AntennaPattern
{

    protected void initializeRange()
    {
        rangeMax = 180D;
        rangeMin = 0.0D;
        symmetryPoint = 90D;
    }

    public SphericalPattern()
    {
    }

    public SphericalPattern(AntennaPattern sph)
    {
        super(sph);
    }

    public SphericalPattern(Element element)
    {
        super(element);
    }
}
