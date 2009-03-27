// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BatchJobListListener.java

package org.seamcat.batch;


public interface BatchJobListListener
{

    public abstract void startingJob(int i);

    public abstract void endingJob(int i);

    public abstract void entireBatchIsComplete();
}