// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClientRequest.java

package org.seamcat.remote.client;

import org.seamcat.batch.BatchJobList;
import org.seamcat.model.Workspace;
import org.seamcat.remote.util.ClientSetting;
import org.w3c.dom.*;

public class ClientRequest
{

    public ClientRequest(ClientSetting _settings, String _requestType)
    {
        batchRequest = false;
        settings = _settings;
        requestType = _requestType;
    }

    public ClientRequest(Element element)
    {
        batchRequest = false;
        settings = new ClientSetting();
        settings.setEmail(element.getAttribute("username"));
        settings.setPassword(element.getAttribute("password"));
        requestType = element.getAttribute("requesttype");
        batchRequest = element.getAttribute("batchRequest").equalsIgnoreCase("true");
        settings.setJobid(Integer.parseInt(element.getAttribute("jobid")));
        if(requestType.equals("upload"))
            if(batchRequest)
                batch = new BatchJobList((Element)element.getElementsByTagName("BatchJobList").item(0));
            else
                workspace = new Workspace((Element)element.getElementsByTagName("Workspace").item(0));
    }

    public Element toElement(Document doc)
    {
        Element elem = doc.createElement("RemoteSeamcatRequest");
        elem.setAttribute("username", settings.getEmail());
        if(settings.getPassword() != null && !settings.getPassword().equals(""))
            elem.setAttribute("password", settings.getPassword());
        elem.setAttribute("requesttype", requestType);
        elem.setAttribute("jobid", Integer.toString(settings.getJobid()));
        elem.setAttribute("batchRequest", Boolean.toString(batchRequest));
        if(requestType.equals("upload"))
        {
            if(batchRequest)
            {
                elem.appendChild(batch.toElement(doc));
            } else
            {
                workspace.setStoreResults(false);
                workspace.setStoreSignals(false);
                workspace.setStoreScenario(true);
                workspace.setStoreControl(true);
                elem.appendChild(workspace.toElement(doc));
            }
        } else
        {
            Element description = doc.createElement("description");
            description.appendChild(doc.createCDATASection(""));
            elem.appendChild(description);
        }
        return elem;
    }

    public Workspace getWorkspace()
    {
        return workspace;
    }

    public void setWorkspace(Workspace workspace)
    {
        this.workspace = workspace;
        settings.setWorkspaceName(workspace.getReference());
    }

    public String getRequestType()
    {
        return requestType;
    }

    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }

    public ClientSetting getSettings()
    {
        return settings;
    }

    public void setSettings(ClientSetting settings)
    {
        this.settings = settings;
    }

    public BatchJobList getBatch()
    {
        return batch;
    }

    public void setBatch(BatchJobList batch)
    {
        this.batch = batch;
        batchRequest = true;
    }

    public boolean isBatchRequest()
    {
        return batchRequest;
    }

    public void setBatchRequest(boolean batchRequest)
    {
        this.batchRequest = batchRequest;
    }

    public static final String REQUEST_ROOT = "RemoteSeamcatRequest";
    public static final String REQ_TYPE_UPLOAD = "upload";
    public static final String REQ_TYPE_STATUS = "status";
    public static final String REQ_TYPE_RESULT = "result";
    public static final String REQ_TYPE_QUEUE = "queue";
    public static final String REQ_TYPE_ADMINSTATUS = "adminstatus";
    public static final String REQ_TYPE_DELETE = "deljob";
    private ClientSetting settings;
    private String requestType;
    private Workspace workspace;
    private BatchJobList batch;
    private boolean batchRequest;
    public static final String REQUEST_ELEMENT_END = "</RemoteSeamcatRequest>";
}
