// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VerticalPattern.java

package org.seamcat.model.core;

import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model.core:
//            AntennaPattern

public class VerticalPattern extends AntennaPattern
{

    protected void initializeRange()
    {
        rangeMax = 90D;
        rangeMin = -90D;
        symmetryPoint = 0.0D;
    }

    public VerticalPattern()
    {
    }

    public VerticalPattern(AntennaPattern ver)
    {
        super(ver);
    }

    public VerticalPattern(Element element)
    {
        super(element);
    }
}
