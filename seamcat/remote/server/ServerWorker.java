// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServerWorker.java

package org.seamcat.remote.server;

import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.seamcat.model.Model;
import org.seamcat.model.Workspace;
import org.seamcat.model.engines.EGE;
import org.seamcat.remote.util.*;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.remote.server:
//            ServerResponse, WorkspaceProcessor

public class ServerWorker
    implements Runnable
{

    public ServerWorker(Socket socket, ServerSetting serverSetting, WorkspaceProcessor wks)
    {
        this.socket = socket;
        this.serverSetting = serverSetting;
        wksProc = wks;
        LOG.info((new StringBuilder()).append("Client connected from ").append(socket.getRemoteSocketAddress()).toString());
    }

    private int createXmlFile(String fileName, String xmlStr)
    {
        PrintWriter out;
        int errCode;
        out = null;
        errCode = 0;
        out = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName))));
        out.println(xmlStr);
        if(out != null)
            out.close();
        break MISSING_BLOCK_LABEL_108;
        IOException ioe;
        ioe;
        LOG.error((new StringBuilder()).append("Error saving file: ").append(fileName).toString(), ioe);
        errCode = 7;
        if(out != null)
            out.close();
        break MISSING_BLOCK_LABEL_108;
        Exception exception;
        exception;
        if(out != null)
            out.close();
        throw exception;
        return errCode;
    }

    private Element createResponseXML(Document doc, int jobId, Status status, int errorCode, int curEvent)
    {
        Element element = (new ServerResponse(userName, requestTypeStr, jobId, status, errorCode, curEvent)).toElement(doc);
        return element;
    }

    private int moveFile(String fileName, String newFileName)
    {
        int errCode = 0;
        File file = new File(fileName);
        File newFile = new File(newFileName);
        if(!file.renameTo(newFile))
            errCode = 7;
        return errCode;
    }

    private void deleteFile(String fileName)
    {
        File file = new File(fileName);
        file.delete();
    }

    private Status getJobStatus(int jobId)
    {
        Status status = new Status();
        String fileName = null;
        boolean jobExist = false;
        String folderName[] = {
            serverSetting.getStagingFolder(), serverSetting.getTempFolder(), serverSetting.getResultFolder(), serverSetting.getErrorFolder(), serverSetting.getProccessingFolder()
        };
        status.setJobId(jobId);
        fileName = (new StringBuilder()).append("ws").append(jobId).append(".xml").toString();
        int i = 0;
        do
        {
            if(i >= folderName.length)
                break;
            File file = new File((new StringBuilder()).append(folderName[i]).append(fileName).toString());
            if(file.exists())
            {
                if(i + 1 == 1)
                {
                    File folder = new File(folderName[i]);
                    File files[] = folder.listFiles();
                    int j = 0;
                    do
                    {
                        if(j >= files.length)
                            break;
                        if(files[j].getName().equalsIgnoreCase(fileName))
                        {
                            status.setQueueNumber(j + 1);
                            break;
                        }
                        j++;
                    } while(true);
                }
                jobExist = true;
                switch(i)
                {
                case 0: // '\0'
                    status.setStatus(1);
                    break;

                case 1: // '\001'
                    status.setStatus(1);
                    break;

                case 2: // '\002'
                    status.setStatus(3);
                    break;

                case 3: // '\003'
                    status.setStatus(4);
                    break;

                case 4: // '\004'
                    status.setStatus(2);
                    break;

                default:
                    status.setStatus(4);
                    break;
                }
                status.setUploadTime(Long.toString(file.lastModified()));
                break;
            }
            i++;
        } while(true);
        if(!jobExist)
            status.setStatus(4);
        return status;
    }

    private Status getPendingQueue()
    {
        Status status = new Status();
        File stagingFolder = new File(serverSetting.getStagingFolder());
        String files[] = stagingFolder.list();
        status.setQueueNumber(files.length);
        return status;
    }

    private int parseRequestXml(Document document)
    {
        int errCode = 0;
        try
        {
            userName = document.getElementsByTagName("RemoteSeamcatRequest").item(0).getAttributes().getNamedItem("username").getNodeValue();
            if(!validEmailAddress(userName))
                errCode = 3;
            if(serverSetting.getUserMustLogin() && !authenticateUser())
                errCode = 4;
            requestTypeStr = document.getElementsByTagName("RemoteSeamcatRequest").item(0).getAttributes().getNamedItem("requesttype").getNodeValue();
            if(requestTypeStr.equalsIgnoreCase("upload"))
                requestType = new RequestType(1);
            else
            if(requestTypeStr.equalsIgnoreCase("status"))
                requestType = new RequestType(2);
            else
            if(requestTypeStr.equalsIgnoreCase("result"))
                requestType = new RequestType(3);
            else
            if(requestTypeStr.equalsIgnoreCase("admstatus"))
                requestType = new RequestType(6);
            else
            if(requestTypeStr.equalsIgnoreCase("deljob"))
                requestType = new RequestType(4);
            else
            if(requestTypeStr.equalsIgnoreCase("queue"))
            {
                requestType = new RequestType(5);
            } else
            {
                requestType = new RequestType(0);
                errCode = 8;
            }
            try
            {
                jobId = Integer.parseInt(document.getElementsByTagName("RemoteSeamcatRequest").item(0).getAttributes().getNamedItem("jobid").getNodeValue());
            }
            catch(NumberFormatException nfe)
            {
                jobId = 0;
            }
        }
        catch(Exception e)
        {
            errCode = 1;
            LOG.error("Error parsing document", e);
        }
        return errCode;
    }

    private boolean authenticateUser()
    {
        return true;
    }

    private boolean validEmailAddress(String emailAddress)
    {
        boolean result = true;
        if(emailAddress == null)
            return false;
        try
        {
            InternetAddress emailAddr = new InternetAddress(emailAddress);
            emailAddr.validate();
        }
        catch(AddressException ex)
        {
            result = false;
        }
        return result;
    }

    private int createJobId()
    {
        Random generator = new Random();
        return Math.abs(generator.nextInt());
    }

    private String getResultXml(String fileName)
    {
        BufferedReader file;
        String xmlDoc;
        file = null;
        String tmpStr = null;
        xmlDoc = "";
        try
        {
            file = new BufferedReader(new FileReader(fileName));
            for(String tmpStr = file.readLine(); tmpStr != null; tmpStr = file.readLine())
                xmlDoc = (new StringBuilder()).append(xmlDoc).append(tmpStr).toString();

        }
        catch(FileNotFoundException fnfe)
        {
            try
            {
                file.close();
            }
            catch(Exception e) { }
            break MISSING_BLOCK_LABEL_116;
        }
        catch(IOException ioe)
        {
            try
            {
                file.close();
            }
            catch(Exception e) { }
            break MISSING_BLOCK_LABEL_116;
        }
        try
        {
            file.close();
        }
        catch(Exception e) { }
        break MISSING_BLOCK_LABEL_116;
        Exception exception;
        exception;
        try
        {
            file.close();
        }
        catch(Exception e) { }
        throw exception;
        return xmlDoc.length() != 0 ? xmlDoc : null;
    }

    private int handleRequest(OutputStream out)
    {
        int errCode = 0;
        boolean writeDocument = true;
        Document doc = null;
        try
        {
            DocumentBuilder db = Model.getWorkspaceDocumentBuilderFactory().newDocumentBuilder();
            doc = db.newDocument();
        }
        catch(ParserConfigurationException ex2)
        {
            LOG.error("Error creating XML document", ex2);
            return 1;
        }
        switch(requestType.getRequestType())
        {
        case 1: // '\001'
        {
            int jobId = createJobId();
            Status status = null;
            String newFileName = (new StringBuilder()).append("ws").append(jobId).append(".xml").toString();
            moveFile((new StringBuilder()).append(serverSetting.getTempFolder()).append(requestFileName).toString(), (new StringBuilder()).append(serverSetting.getStagingFolder()).append(newFileName).toString());
            status = getJobStatus(jobId);
            doc.appendChild(createResponseXML(doc, jobId, status, errCode, 0));
            break;
        }

        case 2: // '\002'
        {
            Status status = getJobStatus(this.jobId);
            int event = 0;
            if(status.getStatus() == 2)
            {
                Workspace w = wksProc.getCurrentWorkspace();
                event = w.getEge().getTotalNumberOfEventsGenerated();
                LOG.info((new StringBuilder()).append("EGE reports that ").append(event).append(" events have been generated so far.").toString());
            }
            doc.appendChild(createResponseXML(doc, this.jobId, status, errCode, event));
            deleteFile((new StringBuilder()).append(serverSetting.getTempFolder()).append(requestFileName).toString());
            break;
        }

        case 3: // '\003'
        {
            String fileName = null;
            Status status = null;
            status = getJobStatus(this.jobId);
            if(this.jobId > 0 && status.getStatus() == 3)
            {
                File file = new File((new StringBuilder()).append(serverSetting.getResultFolder()).append("ws").append(this.jobId).append(".xml").toString());
                if(file.exists())
                    try
                    {
                        BufferedOutputStream bout = new BufferedOutputStream(out);
                        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));
                        bout.write(Long.toString(file.length()).getBytes());
                        bout.write("\n".getBytes());
                        byte buffer[] = new byte[4096];
                        int read;
                        while((read = bin.read(buffer)) > -1) 
                            bout.write(buffer, 0, read);
                        bin.close();
                        bout.flush();
                        writeDocument = false;
                        LOG.info((new StringBuilder()).append("Succesfully send results of job with id = ").append(this.jobId).append(" to client").toString());
                    }
                    catch(IOException ex1)
                    {
                        LOG.error("Error sending results to client");
                    }
                else
                    doc.appendChild(createResponseXML(doc, this.jobId, status, 9, 0));
            } else
            if(status.getStatus() == 1)
                doc.appendChild(createResponseXML(doc, this.jobId, status, 11, 0));
            else
                doc.appendChild(createResponseXML(doc, this.jobId, status, 9, 0));
            deleteFile((new StringBuilder()).append(serverSetting.getTempFolder()).append(requestFileName).toString());
            break;
        }

        case 6: // '\006'
        {
            deleteFile((new StringBuilder()).append(serverSetting.getTempFolder()).append(requestFileName).toString());
            break;
        }

        case 4: // '\004'
        {
            String fileName = null;
            String folderName = null;
            Status status = null;
            status = getJobStatus(this.jobId);
            if(this.jobId > 0)
            {
                fileName = (new StringBuilder()).append("ws").append(this.jobId).append(".xml").toString();
                boolean running = false;
                switch(status.getStatus())
                {
                case 1: // '\001'
                    folderName = serverSetting.getStagingFolder();
                    break;

                case 2: // '\002'
                    folderName = serverSetting.getTempFolder();
                    running = true;
                    break;

                case 3: // '\003'
                    folderName = serverSetting.getResultFolder();
                    break;
                }
                if(running)
                {
                    Workspace w = wksProc.getCurrentWorkspace();
                    if(w != null)
                    {
                        w.getEge().stop();
                        LOG.info((new StringBuilder()).append("Stopped EGE for workspace: ").append(w.toString()).toString());
                    }
                }
                if(folderName != null)
                {
                    deleteFile((new StringBuilder()).append(folderName).append(fileName).toString());
                    LOG.info((new StringBuilder()).append("Deleted job: ").append(folderName).append(fileName).toString());
                    doc.appendChild(createResponseXML(doc, this.jobId, status, 0, 0));
                } else
                {
                    doc.appendChild(createResponseXML(doc, this.jobId, status, 10, 0));
                }
            }
            deleteFile((new StringBuilder()).append(serverSetting.getTempFolder()).append(requestFileName).toString());
            break;
        }

        case 5: // '\005'
        {
            Status status = getPendingQueue();
            doc.appendChild(createResponseXML(doc, this.jobId, status, 0, 0));
            deleteFile((new StringBuilder()).append(serverSetting.getTempFolder()).append(requestFileName).toString());
            break;
        }
        }
        try
        {
            if(writeDocument)
            {
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(out);
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                transformer.transform(source, result);
                out.flush();
            }
        }
        catch(Exception ex)
        {
            LOG.error("Error writing XML to client", ex);
        }
        return errCode;
    }

    public void run()
    {
        BufferedInputStream in;
        BufferedOutputStream out;
        PrintWriter pout;
        in = null;
        out = null;
        pout = null;
        int errCode = 0;
        Random generator = new Random();
        requestFileName = (new StringBuilder()).append("REQ").append(generator.nextInt()).append(".xml").toString();
        try
        {
            in = new BufferedInputStream(socket.getInputStream());
            out = new BufferedOutputStream(socket.getOutputStream());
            pout = new PrintWriter(out, true);
        }
        catch(IOException e)
        {
            LOG.error("run() - in or out failed", e);
        }
        pout.println(STRINGLIST.getString("APPLICATION_TITLE"));
        Document doc = null;
        LOG.info("Reading input");
        int errCode;
        try
        {
            StringBuffer str = new StringBuffer();
            LOG.info("Reading from stream");
            byte buffer[] = new byte[4096];
            int read;
            for(; str.indexOf("</RemoteSeamcatRequest>") < 0; str.append(new String(buffer, 0, read)))
                read = in.read(buffer);

            LOG.info("Reading complete");
            DocumentBuilder db = Model.getWorkspaceDocumentBuilderFactory().newDocumentBuilder();
            Document doc = db.parse(new ByteArrayInputStream(str.toString().getBytes()));
            LOG.info("Input Read");
            createXmlFile((new StringBuilder()).append(serverSetting.getTempFolder()).append(requestFileName).toString(), str.toString());
            errCode = parseRequestXml(doc);
        }
        catch(Exception ex2)
        {
            LOG.error("Error reading XML document", ex2);
            errCode = 1;
        }
        if(errCode == 0)
        {
            LOG.info((new StringBuilder()).append("Stored Received request in ").append(serverSetting.getTempFolder()).append(requestFileName).toString());
            errCode = handleRequest(out);
        } else
        {
            LOG.info((new StringBuilder()).append("Error receiving Request. \"").append(ErrorCode.getErrorDescription(errCode)).append("\"").toString());
            deleteFile((new StringBuilder()).append(serverSetting.getTempFolder()).append(requestFileName).toString());
            pout.println("</RemoteSeamcatResponse>");
        }
        try
        {
            in.close();
            pout.close();
        }
        catch(IOException ioe) { }
        break MISSING_BLOCK_LABEL_493;
        Exception e;
        e;
        if(LOG.isDebugEnabled())
            LOG.debug("run() - Read failed");
        try
        {
            in.close();
            pout.close();
        }
        catch(IOException ioe) { }
        break MISSING_BLOCK_LABEL_493;
        Exception exception;
        exception;
        try
        {
            in.close();
            pout.close();
        }
        catch(IOException ioe) { }
        throw exception;
        LOG.info((new StringBuilder()).append("Disconnecting client from ").append(socket.getRemoteSocketAddress()).toString());
        return;
    }

    public static void setAppender(Appender app)
    {
        LOG.removeAllAppenders();
        LOG.addAppender(app);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/remote/server/ServerWorker);
    private static final ResourceBundle STRINGLIST;
    private Socket socket;
    private ServerSetting serverSetting;
    private String userName;
    private String userPw;
    private RequestType requestType;
    private String requestTypeStr;
    private int jobId;
    private String requestFileName;
    private WorkspaceProcessor wksProc;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
