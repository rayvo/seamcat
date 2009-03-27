// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServerSetting.java

package org.seamcat.remote.util;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import org.seamcat.model.Model;
import org.w3c.dom.*;

public class ServerSetting
{

    public ServerSetting()
    {
        port = 55014;
        smtpServer = "";
        smtpRequireAuthentication = false;
        emailSubject = "SEAMCAT Distributed Job Notification";
        emailBody = "Your Job has been accepted, and was assigned JobID = %%jobid%% and you are currently in queue as no = %%queueno%%";
        adminUser = new Hashtable();
        setTempFolder((new StringBuilder()).append(Model.getInstance().getSeamcatHome().getAbsolutePath()).append(File.separator).append("server").append(File.separator).append("temp").append(File.separator).toString());
        setResultFolder((new StringBuilder()).append(Model.getInstance().getSeamcatHome().getAbsolutePath()).append(File.separator).append("server").append(File.separator).append("results").append(File.separator).toString());
        setStagingFolder((new StringBuilder()).append(Model.getInstance().getSeamcatHome().getAbsolutePath()).append(File.separator).append("server").append(File.separator).append("staging").append(File.separator).toString());
        setErrorFolder((new StringBuilder()).append(Model.getInstance().getSeamcatHome().getAbsolutePath()).append(File.separator).append("server").append(File.separator).append("error").append(File.separator).toString());
        setProccessingFolder((new StringBuilder()).append(Model.getInstance().getSeamcatHome().getAbsolutePath()).append(File.separator).append("server").append(File.separator).append("proc").append(File.separator).toString());
    }

    public ServerSetting(Element element)
    {
        port = 55014;
        smtpServer = "";
        smtpRequireAuthentication = false;
        emailSubject = "SEAMCAT Distributed Job Notification";
        emailBody = "Your Job has been accepted, and was assigned JobID = %%jobid%% and you are currently in queue as no = %%queueno%%";
        adminUser = new Hashtable();
        port = Integer.parseInt(element.getAttribute("port"));
        smtpServer = element.getAttribute("smtp-server");
        smtpRequireAuthentication = element.getAttribute("smtp-require-authentication").equalsIgnoreCase("true");
        emailUser = element.getAttribute("smtp-user");
        emailUserPw = element.getAttribute("smtp-password");
        emailSubject = element.getAttribute("email-subject");
        userMustLogin = element.getAttribute("require-authentication").equalsIgnoreCase("true");
        stagingFolder = element.getAttribute("staging-folder");
        resultFolder = element.getAttribute("results-folder");
        tempFolder = element.getAttribute("temp-folder");
        errorFolder = element.getAttribute("error-folder");
        proccessingFolder = element.getAttribute("proccessing-folder");
        fromAddress = element.getAttribute("from");
        emailBody = element.getFirstChild().getNodeValue();
    }

    public Element toElement(Document doc)
    {
        Element server = doc.createElement("server-settings");
        server.setAttribute("port", Integer.toString(port));
        server.setAttribute("smtp-server", smtpServer);
        server.setAttribute("smtp-require-authentication", Boolean.toString(smtpRequireAuthentication));
        server.setAttribute("smtp-user", emailUser);
        server.setAttribute("smtp-password", emailUserPw);
        server.setAttribute("email-subject", emailSubject);
        server.setAttribute("require-authentication", Boolean.toString(userMustLogin));
        server.setAttribute("staging-folder", stagingFolder);
        server.setAttribute("results-folder", resultFolder);
        server.setAttribute("temp-folder", tempFolder);
        server.setAttribute("error-folder", errorFolder);
        server.setAttribute("proccessing-folder", getProccessingFolder());
        server.setAttribute("from", fromAddress);
        server.appendChild(doc.createCDATASection(emailBody));
        return server;
    }

    public void ensureDirectories()
        throws IOException
    {
        File dir = new File(stagingFolder);
        if(!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
        dir = new File(resultFolder);
        if(!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
        dir = new File(errorFolder);
        if(!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
        dir = new File(tempFolder);
        if(!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
        dir = new File(proccessingFolder);
        if(!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getSmtpServer()
    {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer)
    {
        this.smtpServer = smtpServer;
    }

    public boolean getSmtpRequireAuthentication()
    {
        return smtpRequireAuthentication;
    }

    public void setSmtpRequireAuthentication(boolean b)
    {
        smtpRequireAuthentication = b;
    }

    public String getEmailUser()
    {
        return emailUser;
    }

    public void setEmailUser(String emailUser)
    {
        this.emailUser = emailUser;
    }

    public String getEmailUserPw()
    {
        return emailUserPw;
    }

    public void setEmailUserPw(String emailUserPw)
    {
        this.emailUserPw = emailUserPw;
    }

    public String getEmailSubject()
    {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject)
    {
        this.emailSubject = emailSubject;
    }

    public String getEmailBody()
    {
        return emailBody;
    }

    public void setEmailBody(String string)
    {
        emailBody = string;
    }

    public boolean getEmailDebug()
    {
        return emailDebug;
    }

    public void setEmailDebug(boolean emailDebug)
    {
        this.emailDebug = emailDebug;
    }

    public boolean getUserMustLogin()
    {
        return userMustLogin;
    }

    public void setUserMustLogin(boolean userMustLogin)
    {
        this.userMustLogin = userMustLogin;
    }

    public String getStagingFolder()
    {
        return stagingFolder;
    }

    public void setStagingFolder(String stagingFolder)
    {
        this.stagingFolder = stagingFolder;
    }

    public String getTempFolder()
    {
        return tempFolder;
    }

    public void setTempFolder(String tempFolder)
    {
        this.tempFolder = tempFolder;
    }

    public String getResultFolder()
    {
        return resultFolder;
    }

    public void setResultFolder(String resultFolder)
    {
        this.resultFolder = resultFolder;
    }

    public String getErrorFolder()
    {
        return errorFolder;
    }

    public void setErrorFolder(String errorFolder)
    {
        this.errorFolder = errorFolder;
    }

    public void addAdminUser(String userName, String password)
    {
        if(userName == null || password == null)
        {
            return;
        } else
        {
            adminUser.put(userName, password);
            return;
        }
    }

    public boolean validateAdminUser(String userName, String password)
    {
        boolean validate = false;
        if(userName == null || password == null)
            return false;
        if(((String)adminUser.get("userName")).equals(password))
            validate = true;
        return validate;
    }

    public String getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress)
    {
        this.fromAddress = fromAddress;
    }

    public String getProccessingFolder()
    {
        return proccessingFolder;
    }

    public void setProccessingFolder(String proccessingFolder)
    {
        this.proccessingFolder = proccessingFolder;
    }

    private int port;
    private String smtpServer;
    private boolean smtpRequireAuthentication;
    private String emailUser;
    private String emailUserPw;
    private String emailSubject;
    private String emailBody;
    private boolean emailDebug;
    private boolean userMustLogin;
    private String stagingFolder;
    private String tempFolder;
    private String resultFolder;
    private String proccessingFolder;
    private String fromAddress;
    private String errorFolder;
    private Hashtable adminUser;
}
