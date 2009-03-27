// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServerResponse.java

package org.seamcat.remote.server;

import org.seamcat.batch.BatchJobList;
import org.seamcat.model.Workspace;
import org.seamcat.remote.util.Status;
import org.w3c.dom.*;

public class ServerResponse
{

    public ServerResponse(String _username, String _requestType, int _jobid, Status _status, int _error, int curEvent)
    {
        username = _username;
        requestType = _requestType;
        jobid = _jobid;
        status = _status;
        errorCode = _error;
        currentEvent = curEvent;
    }

    public ServerResponse(Element elem)
    {
        username = elem.getAttribute("username");
        requestType = elem.getAttribute("requesttype");
        jobid = Integer.parseInt(elem.getAttribute("jobid"));
        batchResponse = elem.getAttribute("batchResponse").equalsIgnoreCase("true");
        try
        {
            currentEvent = Integer.parseInt(elem.getAttribute("current-event"));
        }
        catch(Exception e)
        {
            currentEvent = 0;
        }
        try
        {
            errorCode = Integer.parseInt(((Element)elem.getElementsByTagName("JobError").item(0)).getAttribute("errorcode"));
        }
        catch(Exception e)
        {
            errorCode = 0;
            status = new Status((Element)elem.getElementsByTagName("JobStatus").item(0));
        }
        if(requestType.equals("result"))
            if(batchResponse)
                batch = new BatchJobList((Element)elem.getElementsByTagName("BatchJobList").item(0));
            else
                workspace = new Workspace((Element)elem.getElementsByTagName("Workspace").item(0));
    }

    public Element toElement(Document doc)
    {
        Element elem = doc.createElement("RemoteSeamcatResponse");
        elem.setAttribute("username", username);
        elem.setAttribute("requesttype", requestType);
        elem.setAttribute("jobid", Integer.toString(jobid));
        elem.setAttribute("batchResponse", Boolean.toString(batchResponse));
        elem.setAttribute("current-event", Integer.toString(currentEvent));
        if(errorCode != 0)
        {
            Element error = doc.createElement("JobError");
            error.setAttribute("errorcode", Integer.toString(errorCode));
            elem.appendChild(error);
        } else
        {
            elem.appendChild(status.toElement(doc));
            if(requestType.equals("result"))
                if(batchResponse)
                    elem.appendChild(batch.toElement(doc));
                else
                    elem.appendChild(workspace.toElement(doc));
        }
        return elem;
    }

    public int getJobid()
    {
        return jobid;
    }

    public void setJobid(int jobid)
    {
        this.jobid = jobid;
    }

    public String getRequestType()
    {
        return requestType;
    }

    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public Status getStatusObject()
    {
        return status;
    }

    public int getStatus()
    {
        return status.getStatus();
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public Workspace getWorkspace()
    {
        return workspace;
    }

    public void setWorkspace(Workspace workspace)
    {
        this.workspace = workspace;
    }

    public BatchJobList getBatch()
    {
        return batch;
    }

    public void setBatch(BatchJobList batch)
    {
        this.batch = batch;
        batchResponse = true;
    }

    public boolean isBatchResponse()
    {
        return batchResponse;
    }

    public void setBatchResponse(boolean batchResponse)
    {
        this.batchResponse = batchResponse;
    }

    public int getCurrentEvent()
    {
        return currentEvent;
    }

    public void setCurrentEvent(int currentEvent)
    {
        this.currentEvent = currentEvent;
    }

    public static final String RESPONSE_ROOT = "RemoteSeamcatResponse";
    public static final String RESPONSE_END = "</RemoteSeamcatResponse>";
    private String username;
    private int jobid;
    private String requestType;
    private Status status;
    private int errorCode;
    private Workspace workspace;
    private BatchJobList batch;
    private boolean batchResponse;
    private int currentEvent;
}
