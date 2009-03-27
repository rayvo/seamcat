// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatClientSocket.java

package org.seamcat.remote.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.seamcat.model.Model;
import org.seamcat.model.XmlValidationHandler;
import org.seamcat.remote.server.ServerResponse;
import org.seamcat.remote.util.ClientSetting;
import org.seamcat.remote.util.Status;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

// Referenced classes of package org.seamcat.remote.client:
//            ClientRequest, ClientRequestStatusListener

public class SeamcatClientSocket
    implements Runnable
{

    public SeamcatClientSocket(ClientSetting _clientSetting, ClientRequestStatusListener _listener)
    {
        socket = null;
        out = null;
        pin = null;
        in = null;
        clientSetting = _clientSetting;
        listener = _listener;
    }

    public void run()
    {
        try
        {
            if(connect())
            {
                LOG.debug("Creating and writing output");
                DocumentBuilder db = Model.getWorkspaceDocumentBuilderFactory().newDocumentBuilder();
                Document doc = db.newDocument();
                Element element = clientRequest.toElement(doc);
                doc.appendChild(element);
                DOMSource source = new DOMSource(doc);
                if(listener != null)
                    listener.loginStatus(true);
                StreamResult result = new StreamResult(out);
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                transformer.transform(source, result);
                out.flush();
                if(listener != null)
                    listener.sendingRequestStatus(true);
                LOG.debug("Input written");
            } else
            if(listener != null)
            {
                listener.loginStatus(false);
                listener.seamcatVersionStatus(false);
                listener.sendingRequestStatus(false);
                listener.receiveingResponseStatus(false);
                return;
            }
        }
        catch(Exception ioe)
        {
            LOG.error("Exception", ioe);
            if(listener != null)
                listener.sendingRequestStatus(false);
        }
        try
        {
            serverResponse = readServerResponse(in);
            LOG.debug((new StringBuilder()).append("Server said: \"").append(Status.getStatusText(serverResponse.getStatus(), serverResponse.getCurrentEvent())).append("\"").toString());
            if(listener != null)
                listener.receiveingResponseStatus(true);
        }
        catch(Exception ex1)
        {
            LOG.error("uploadWorkspace(Workspace)", ex1);
            if(listener != null)
                listener.receiveingResponseStatus(false);
            requestCompleted = false;
            return;
        }
        clientSetting.setJobid(serverResponse.getJobid());
        clientSetting.setStatus(serverResponse.getStatus());
        clientSetting.setLastStatusCheck(System.currentTimeMillis());
        requestCompleted = true;
    }

    public void sendRequest(ClientRequest clientReq)
    {
        clientRequest = clientReq;
        requestCompleted = false;
        worker = new Thread(this, "Seamcat Client Worker");
        worker.start();
    }

    public ServerResponse waitForResponse()
    {
        if(!requestCompleted && worker.isAlive())
            try
            {
                worker.join();
            }
            catch(InterruptedException ex)
            {
                LOG.error("An Error occured while waiting for response", ex);
            }
        return serverResponse;
    }

    public boolean requestCompleted()
    {
        return requestCompleted;
    }

    private ServerResponse readServerResponse(InputStream in)
        throws Exception
    {
        StringBuffer str = new StringBuffer();
        char buffer[] = new char[4096];
        int read = 0;
        try
        {
            Document doc = null;
            if(clientRequest.getRequestType().equals("result"))
            {
                try
                {
                    long size = Long.parseLong(pin.readLine());
                    LOG.debug((new StringBuilder()).append("File size = ").append(size).append(" bytes").toString());
                    if(listener != null)
                        listener.fetchingResults((int)(size / 1024L));
                    File temp = new File((new StringBuilder()).append(Model.seamcatTemp).append(File.separator).toString(), (new StringBuilder()).append(clientSetting.getJobid()).append("_temp.sws").toString());
                    BufferedWriter bout = new BufferedWriter(new FileWriter(temp));
                    int totalRead = 0;
                    do
                    {
                        if((read = pin.read(buffer)) <= -1)
                            break;
                        bout.write(buffer, 0, read);
                        str.append(new String(buffer, 0, read));
                        totalRead += read;
                        if(listener != null)
                            listener.downloadStatus(totalRead / 1024);
                    } while(true);
                    bout.flush();
                    bout.close();
                    if(listener != null)
                        listener.resultsDownloaded();
                    doc = db.parse(new ByteArrayInputStream(str.toString().getBytes()));
                    LOG.debug((new StringBuilder()).append("Stored ").append(totalRead).append(" bytes of results in: ").append(temp.getAbsolutePath()).toString());
                }
                catch(Exception ex1)
                {
                    LOG.error("Error receiving results", ex1);
                }
            } else
            {
                LOG.debug("Reading from stream");
                for(; str.indexOf("</RemoteSeamcatResponse>") < 0; str.append(new String(buffer, 0, read)))
                    read = pin.read(buffer);

                LOG.debug("Parsing response from server");
                doc = db.parse(new ByteArrayInputStream(str.toString().getBytes()));
            }
            return new ServerResponse((Element)doc.getElementsByTagName("RemoteSeamcatResponse").item(0));
        }
        catch(IOException ex)
        {
            LOG.error("An Error occured", ex);
        }
        catch(SAXException ex)
        {
            LOG.error((new StringBuilder()).append("An Error occured while parsing. \n").append(str.toString()).toString());
            LOG.error("StackTrace:", ex);
        }
        return null;
    }

    public void disconnect()
    {
        try
        {
            socket.close();
        }
        catch(Exception e)
        {
            LOG.debug("Error while attempting socket close");
        }
    }

    public boolean connect()
    {
        boolean connected = false;
        try
        {
            socket = new Socket(clientSetting.getServerUrl(), clientSetting.getPort());
            if(listener != null)
                listener.findingServerStatus(true);
            out = new BufferedOutputStream(socket.getOutputStream());
            in = new BufferedInputStream(socket.getInputStream());
            pin = new BufferedReader(new InputStreamReader(in));
            connected = true;
            String seamcatVersion = pin.readLine();
            if(listener != null)
                listener.seamcatVersionStatus(seamcatVersion.equalsIgnoreCase(STRINGLIST.getString("APPLICATION_TITLE")));
        }
        catch(UnknownHostException e)
        {
            if(listener != null)
                listener.findingServerStatus(false);
            LOG.error("Unknown HOST");
        }
        catch(IOException e)
        {
            LOG.info("I/O Error during connect", e);
            if(listener != null)
                listener.findingServerStatus(false);
        }
        return connected;
    }

    public ClientRequestStatusListener getListener()
    {
        return listener;
    }

    public void setListener(ClientRequestStatusListener listener)
    {
        this.listener = listener;
    }

    private static final ResourceBundle STRINGLIST;
    private static final Logger LOG;
    private Socket socket;
    private OutputStream out;
    private BufferedReader pin;
    private InputStream in;
    private ClientSetting clientSetting;
    private ClientRequestStatusListener listener;
    private Thread worker;
    private ServerResponse serverResponse;
    private ClientRequest clientRequest;
    private boolean requestCompleted;
    private static DocumentBuilder db;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        LOG = Logger.getLogger(org/seamcat/remote/client/SeamcatClientSocket);
        try
        {
            db = Model.getWorkspaceDocumentBuilderFactory().newDocumentBuilder();
            db.setErrorHandler(new XmlValidationHandler(true));
        }
        catch(ParserConfigurationException ex)
        {
            LOG.error("An Error occured", ex);
        }
    }
}
