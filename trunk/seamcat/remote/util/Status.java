// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Status.java

package org.seamcat.remote.util;

import java.util.Locale;
import java.util.ResourceBundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Status
{

    public Status()
    {
    }

    public Status(int _status)
    {
        status = _status;
    }

    public Status(Element elem)
    {
        status = Integer.parseInt(elem.getAttribute("status"));
        if(status == 1)
            queueNumber = Integer.parseInt(elem.getAttribute("queuenumber"));
    }

    public Element toElement(Document doc)
    {
        Element elem = doc.createElement("JobStatus");
        elem.setAttribute("status", Integer.toString(status));
        if(status == 1)
            elem.setAttribute("queuenumber", Integer.toString(queueNumber));
        return elem;
    }

    public static String getStatusText(int status, int event)
    {
        String statusText;
        switch(status)
        {
        case 4: // '\004'
            statusText = STATUS_TEXT_ERROR;
            break;

        case 3: // '\003'
            statusText = STATUS_TEXT_FINISHED;
            break;

        case 1: // '\001'
            statusText = STATUS_TEXT_PENDING;
            break;

        case 2: // '\002'
            statusText = (new StringBuilder()).append(STATUS_TEXT_RUNNING).append(event).toString();
            break;

        default:
            statusText = STATUS_TEXT_UNKNOWN;
            break;
        }
        return statusText;
    }

    public int getJobId()
    {
        return jobId;
    }

    public int getStatus()
    {
        return status;
    }

    public String getUploadTime()
    {
        return uploadTime;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setJobId(int i)
    {
        jobId = i;
    }

    public void setStatus(int i)
    {
        status = i;
    }

    public void setUploadTime(String string)
    {
        uploadTime = string;
    }

    public void setUserName(String string)
    {
        userName = string;
    }

    public int getQueueNumber()
    {
        return queueNumber;
    }

    public void setQueueNumber(int i)
    {
        queueNumber = i;
    }

    public static final String STATUS_ROOT = "JobStatus";
    private static final ResourceBundle STRINGLIST;
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_RUNNING = 2;
    public static final int STATUS_FINISHED = 3;
    public static final int STATUS_ERROR = 4;
    private static final String STATUS_TEXT_PENDING;
    private static final String STATUS_TEXT_RUNNING;
    private static final String STATUS_TEXT_FINISHED;
    private static final String STATUS_TEXT_ERROR;
    private static final String STATUS_TEXT_UNKNOWN;
    private int jobId;
    private int status;
    private String userName;
    private String uploadTime;
    private int queueNumber;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        STATUS_TEXT_PENDING = STRINGLIST.getString("REMOTE_STATUSTEXT_PENDING");
        STATUS_TEXT_RUNNING = STRINGLIST.getString("REMOTE_STATUSTEXT_RUNNING");
        STATUS_TEXT_FINISHED = STRINGLIST.getString("REMOTE_STATUSTEXT_FINISHED");
        STATUS_TEXT_ERROR = STRINGLIST.getString("REMOTE_STATUSTEXT_ERROR");
        STATUS_TEXT_UNKNOWN = STRINGLIST.getString("REMOTE_STATUSTEXT_UNKNOWN");
    }
}
