// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   HorizontalPattern.java

package org.seamcat.model.core;

import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model.core:
//            AntennaPattern

public class HorizontalPattern extends AntennaPattern
{

    protected void initializeRange()
    {
        rangeMax = 360D;
        rangeMin = 0.0D;
        symmetryPoint = 180D;
    }

    public HorizontalPattern()
    {
    }

    public HorizontalPattern(AntennaPattern hoz)
    {
        super(hoz);
    }

    public HorizontalPattern(Element element)
    {
        super(element);
    }
}