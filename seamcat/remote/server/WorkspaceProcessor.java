// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WorkspaceProcessor.java

package org.seamcat.remote.server;

import java.io.*;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.seamcat.batch.BatchJobList;
import org.seamcat.model.*;
import org.seamcat.model.engines.EGE;
import org.seamcat.remote.client.ClientRequest;
import org.seamcat.remote.notification.Email;
import org.seamcat.remote.notification.EmailMessage;
import org.seamcat.remote.util.*;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.remote.server:
//            ServerResponse

public class WorkspaceProcessor
    implements Runnable
{

    public WorkspaceProcessor(ServerSetting _serverSettings)
    {
        monitorFolder = true;
        serverSettings = _serverSettings;
    }

    public Workspace getCurrentWorkspace()
    {
        return currentWorkspace;
    }

    public BatchJobList getCurrentBatch()
    {
        return currentBatch;
    }

    public void run()
    {
        File stagingFolder = new File(serverSettings.getStagingFolder());
        File resultsFolder = new File(serverSettings.getResultFolder());
        File errorFolder = new File(serverSettings.getErrorFolder());
        File tempFolder = new File(serverSettings.getTempFolder());
        File processingFolder = new File(serverSettings.getProccessingFolder());
        if(!stagingFolder.exists() || !stagingFolder.isDirectory())
            stagingFolder.mkdirs();
        if(!resultsFolder.exists() || !resultsFolder.isDirectory())
            resultsFolder.mkdirs();
        if(!errorFolder.exists() || !errorFolder.isDirectory())
            errorFolder.mkdirs();
        if(!tempFolder.exists() || !tempFolder.isDirectory())
            tempFolder.mkdirs();
        if(!processingFolder.exists() || !processingFolder.isDirectory())
            processingFolder.mkdirs();
        do
        {
            if(!monitorFolder)
                break;
            try
            {
                File files[] = stagingFolder.listFiles();
                if(files != null && files.length > 0)
                    try
                    {
                        File file = new File(processingFolder, files[0].getName());
                        files[0].renameTo(file);
                        DocumentBuilder db = Model.getWorkspaceDocumentBuilderFactory().newDocumentBuilder();
                        db.setErrorHandler(new XmlValidationHandler(true));
                        Document doc = db.parse(file);
                        ClientRequest req = new ClientRequest((Element)doc.getElementsByTagName("RemoteSeamcatRequest").item(0));
                        try
                        {
                            int jobid = Integer.parseInt(file.getName().substring(3, file.getName().indexOf(".xml")));
                            req.getSettings().setJobid(jobid);
                        }
                        catch(Exception e) { }
                        ServerResponse res = new ServerResponse(req.getSettings().getEmail(), "result", req.getSettings().getJobid(), new Status(3), 0, 0);
                        LOG.info((new StringBuilder()).append("Succesfully read request from ").append(file.getAbsolutePath()).toString());
                        LOG.info("Starting Simulation");
                        long start = System.currentTimeMillis();
                        boolean wasInterupted = false;
                        if(req.isBatchRequest())
                        {
                            currentBatch = req.getBatch();
                            currentBatch.setInServerMode(true);
                            Thread worker = new Thread(currentBatch);
                            worker.start();
                            worker.join();
                            long end = System.currentTimeMillis();
                            LOG.info((new StringBuilder()).append("Succesfully finished ").append(currentBatch.getBatchJobs().size()).append(" batch entries in ").append(currentBatch.toString()).append(" in ").append(end - start).append(" milliseconds").toString());
                            res.setBatch(currentBatch);
                            currentBatch = null;
                        } else
                        {
                            currentWorkspace = req.getWorkspace();
                            currentWorkspace.getEge().setInServerMode(true);
                            currentWorkspace.startEventGeneration();
                            currentWorkspace.joinEge();
                            long end = System.currentTimeMillis();
                            if(currentWorkspace.getEge().isStopped())
                            {
                                LOG.info("Removing results from stopped job");
                                currentWorkspace.resetEventGeneration();
                                wasInterupted = true;
                            } else
                            {
                                LOG.info((new StringBuilder()).append("Succesfully generated ").append(currentWorkspace.getEge().getTotalNumberOfEventsGenerated()).append(" events from workspace ").append(currentWorkspace.getReference()).append(" in ").append(end - start).append(" milliseconds (").append((double)currentWorkspace.getEge().getTotalNumberOfEventsGenerated() / ((double)(end - start) / 1000D)).append(" Events / Second)").toString());
                                currentWorkspace.setStoreResults(true);
                                currentWorkspace.setStoreScenario(true);
                                currentWorkspace.setStoreSignals(true);
                                currentWorkspace.setExpandSignals(true);
                                res.setWorkspace(currentWorkspace);
                                currentWorkspace = null;
                            }
                        }
                        start = System.currentTimeMillis();
                        if(!wasInterupted)
                        {
                            doc = db.newDocument();
                            doc.appendChild(res.toElement(doc));
                            DOMSource source = new DOMSource(doc);
                            StreamResult result = new StreamResult(new BufferedOutputStream(new FileOutputStream(file)));
                            TransformerFactory transFactory = TransformerFactory.newInstance();
                            Transformer transformer = transFactory.newTransformer();
                            transformer.transform(source, result);
                            result.getOutputStream().close();
                            file.renameTo(new File(resultsFolder, file.getName()));
                            long end = System.currentTimeMillis();
                            LOG.info((new StringBuilder()).append("Time to save results: ").append(end - start).append(" ms").toString());
                            try
                            {
                                String emailText = serverSettings.getEmailBody().replaceAll("%%jobid%%", Integer.toString(req.getSettings().getJobid()));
                                EmailMessage emailMessage = new EmailMessage(req.getSettings().getEmail(), serverSettings.getFromAddress(), serverSettings.getEmailSubject(), emailText);
                                Email email = new Email();
                                email.sendMessage(serverSettings, emailMessage);
                                LOG.info((new StringBuilder()).append("Notified ").append(req.getSettings().getEmail()).append(" of job completion").toString());
                            }
                            catch(Exception e)
                            {
                                LOG.error((new StringBuilder()).append("Error sending notification email to ").append(req.getSettings().getEmail()).toString(), e);
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        LOG.error((new StringBuilder()).append("Error processing workspace from ").append(files[0].getAbsolutePath()).toString(), e);
                        files[0].renameTo(new File(errorFolder, files[0].getName()));
                    }
                else
                    Thread.sleep(1000L);
            }
            catch(Exception ex)
            {
                LOG.error("Error monitoring folder", ex);
            }
        } while(true);
        LOG.info("Monitoring of staging folder stops");
    }

    public void startProcessor()
    {
        worker = new Thread(this);
        worker.start();
    }

    public void stopProcessor()
    {
        monitorFolder = false;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/remote/server/WorkspaceProcessor);
    private ServerSetting serverSettings;
    private boolean monitorFolder;
    private Thread worker;
    private Workspace currentWorkspace;
    private BatchJobList currentBatch;

}
