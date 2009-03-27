// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClientRequestStatusListener.java

package org.seamcat.remote.client;


public interface ClientRequestStatusListener
{

    public abstract void findingServerStatus(boolean flag);

    public abstract void loginStatus(boolean flag);

    public abstract void seamcatVersionStatus(boolean flag);

    public abstract void sendingRequestStatus(boolean flag);

    public abstract void receiveingResponseStatus(boolean flag);

    public abstract void fetchingResults(int i);

    public abstract void downloadStatus(int i);

    public abstract void resultsDownloaded();
}
