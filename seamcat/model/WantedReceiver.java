// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   WantedReceiver.java

package org.seamcat.model;

import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model:
//            Receiver

public class WantedReceiver extends Receiver
{

    public WantedReceiver(Receiver r)
    {
        super(r);
    }

    public WantedReceiver(Element e)
    {
        super(e);
    }

    protected void initNodeAttributes()
    {
    }
}