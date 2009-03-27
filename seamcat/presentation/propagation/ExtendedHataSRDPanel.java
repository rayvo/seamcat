// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExtendedHataSRDPanel.java

package org.seamcat.presentation.propagation;

import java.awt.Window;
import org.seamcat.propagation.*;

// Referenced classes of package org.seamcat.presentation.propagation:
//            ExtendedHataPanel

public final class ExtendedHataSRDPanel extends ExtendedHataPanel
{

    public ExtendedHataSRDPanel(Window parent)
    {
        super(parent);
    }

    public void clear()
    {
        setPropagationable(new HataSE24Model());
    }

    public PropagationModel getPropagationable()
    {
        return new HataSE24Model((HataAndSDModel)super.getPropagationable());
    }

    public void setPropagationable(PropagationModel p)
    {
        if(p instanceof HataSE24Model)
            super.setPropagationable((HataSE24Model)p);
        else
            throw new IllegalArgumentException("Object must be an instance of class <HataSE24Model>");
    }
}
