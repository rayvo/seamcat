// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatServerSocket.java

package org.seamcat.remote.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.seamcat.remote.util.ServerSetting;

// Referenced classes of package org.seamcat.remote.server:
//            ServerWorker, WorkspaceProcessor

public class SeamcatServerSocket
    implements Runnable
{

    public SeamcatServerSocket(ServerSetting _serverSetting, WorkspaceProcessor prc)
    {
        serverSetting = _serverSetting;
        wksProc = prc;
    }

    public void listenSocket()
    {
        worker = new Thread(this, "Seamcat Server Listening Thread");
        worker.start();
    }

    public void run()
    {
        listen = true;
        try
        {
            serverSetting.ensureDirectories();
            server = new ServerSocket(serverSetting.getPort());
            LOG.info((new StringBuilder()).append("SEAMCAT Server is listening on ").append(server.getInetAddress().getHostAddress()).append(":").append(server.getLocalPort()).toString());
            LOG.info("");
            LOG.info((new StringBuilder()).append("Temp folder is: ").append(serverSetting.getTempFolder()).toString());
            LOG.info((new StringBuilder()).append("Result folder is: ").append(serverSetting.getResultFolder()).toString());
            LOG.info((new StringBuilder()).append("Staging folder is: ").append(serverSetting.getStagingFolder()).toString());
            LOG.info((new StringBuilder()).append("Error folder is: ").append(serverSetting.getErrorFolder()).toString());
            LOG.info((new StringBuilder()).append("Processing folder is: ").append(serverSetting.getProccessingFolder()).toString());
            LOG.info("*****************************************************************************");
        }
        catch(IOException e)
        {
            LOG.error((new StringBuilder()).append("listenSocket() - Could not listen on port: ").append(serverSetting.getPort()).toString(), e);
        }
        while(listen) 
            try
            {
                ServerWorker w = new ServerWorker(server.accept(), serverSetting, wksProc);
                Thread t = new Thread(w);
                t.start();
            }
            catch(IOException e) { }
            catch(Exception e)
            {
                LOG.error("Unknown error", e);
                listen = false;
            }
    }

    public void setAppender(Appender app)
    {
        LOG.removeAllAppenders();
        LOG.addAppender(app);
        appender = app;
        ServerWorker.setAppender(app);
    }

    protected void finalize()
    {
        try
        {
            server.close();
        }
        catch(IOException e)
        {
            LOG.info("finalize() - Could not close socket");
        }
    }

    public void stopListening()
    {
        listen = false;
        try
        {
            LOG.info("Stopping server");
            server.close();
            LOG.info("Server stopped");
        }
        catch(Exception ex)
        {
            LOG.error("Error stoping server", ex);
        }
    }

    private final Logger LOG = Logger.getLogger(org/seamcat/remote/server/SeamcatServerSocket);
    private Appender appender;
    private ServerSocket server;
    private ServerSetting serverSetting;
    private Thread worker;
    private boolean listen;
    private WorkspaceProcessor wksProc;
}
