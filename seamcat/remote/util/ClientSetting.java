// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClientSetting.java

package org.seamcat.remote.util;

import org.seamcat.model.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ClientSetting
{

    public ClientSetting()
    {
        serverUrl = "server.seamcat.org";
        port = 80;
        email = Model.getInstance().getDefaultEmail();
    }

    public ClientSetting(Element element)
    {
        serverUrl = "server.seamcat.org";
        port = 80;
        jobid = Integer.parseInt(element.getAttribute("jobid"));
        serverUrl = element.getAttribute("server-url");
        port = Integer.parseInt(element.getAttribute("server-port"));
        email = element.getAttribute("username");
        password = element.getAttribute("password");
        sendToServer = Long.parseLong(element.getAttribute("sendToServer"));
        lastStatusCheck = Long.parseLong(element.getAttribute("lastStatusCheck"));
        status = Integer.parseInt(element.getAttribute("status"));
        workspaceName = element.getAttribute("workspaceName");
    }

    public Element toElement(Document doc)
    {
        Element client = doc.createElement("client-setting");
        client.setAttribute("jobid", Integer.toString(jobid));
        client.setAttribute("server-url", serverUrl);
        client.setAttribute("server-port", Integer.toString(port));
        client.setAttribute("username", email);
        client.setAttribute("password", password);
        client.setAttribute("sendToServer", Long.toString(sendToServer));
        client.setAttribute("lastStatusCheck", Long.toString(lastStatusCheck));
        client.setAttribute("status", Integer.toString(status));
        client.setAttribute("workspaceName", workspaceName);
        return client;
    }

    public String toString()
    {
        return workspaceName;
    }

    public String getServerUrl()
    {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public long getLastStatusCheck()
    {
        return lastStatusCheck;
    }

    public void setLastStatusCheck(long lastStatusCheck)
    {
        this.lastStatusCheck = lastStatusCheck;
    }

    public long getSendToServer()
    {
        return sendToServer;
    }

    public void setSendToServer(long sendToServer)
    {
        this.sendToServer = sendToServer;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getWorkspaceName()
    {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName)
    {
        this.workspaceName = workspaceName;
    }

    public int getJobid()
    {
        return jobid;
    }

    public void setJobid(int jobid)
    {
        this.jobid = jobid;
    }

    public int getCurrentEvent()
    {
        return currentEvent;
    }

    public void setCurrentEvent(int currentEvent)
    {
        this.currentEvent = currentEvent;
    }

    private String serverUrl;
    private int port;
    private String email;
    private String password;
    private int jobid;
    private long sendToServer;
    private long lastStatusCheck;
    private int status;
    private int currentEvent;
    private String workspaceName;
}
