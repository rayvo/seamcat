// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RequestType.java

package org.seamcat.remote.util;


public class RequestType
{

    public RequestType()
    {
    }

    public RequestType(int requestType)
    {
        this.requestType = requestType;
    }

    public int getRequestType()
    {
        return requestType;
    }

    public void setRequestType(int requestType)
    {
        this.requestType = requestType;
    }

    public static final int WORKSPACE_UPLOAD_REQUEST = 1;
    public static final int WORKSPACE_STATUS_REQUEST = 2;
    public static final int WORKSPACE_RESULT_REQUEST = 3;
    public static final int WORKSPACE_DELETE_REQUEST = 4;
    public static final int QUEUE_STATUS_REQUEST = 5;
    public static final int ADMIN_STATUS_REQUEST = 6;
    private int requestType;
}
