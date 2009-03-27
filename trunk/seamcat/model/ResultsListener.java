// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ResultsListener.java

package org.seamcat.model;


// Referenced classes of package org.seamcat.model:
//            ResultsEvent

public interface ResultsListener
{

    public abstract void ResultsAdded(ResultsEvent resultsevent);

    public abstract void ResultsFlushed(ResultsEvent resultsevent);

    public abstract void ResultsCompleted(ResultsEvent resultsevent);
}