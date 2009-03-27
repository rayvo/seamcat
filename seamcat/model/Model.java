// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Model.java

package org.seamcat.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.prefs.Preferences;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.seamcat.Seamcat;
import org.seamcat.cdma.xml.CDMAXMLUtils;
import org.seamcat.integration.ClassPathHacker;
import org.seamcat.model.core.InterferenceLink;
import org.seamcat.model.core.Signals;
import org.seamcat.model.datatypes.IRSSVector;
import org.seamcat.model.datatypes.IRSSVectorList;
import org.seamcat.presentation.Node;
import org.seamcat.remote.server.SeamcatServerSocket;
import org.seamcat.remote.server.WorkspaceProcessor;
import org.seamcat.remote.util.ClientSetting;
import org.seamcat.remote.util.ServerSetting;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model:
//            XmlValidationHandler, Library, Components, Workspace, 
//            SeamcatHomeSelector, NodeAttribute

public final class Model
    implements TreeModel, Node
{

    private Model()
    {
        fileAppender = new FileAppender();
        defaultEmail = "";
        useHigherPriorityThreads = true;
        LOG.debug("Constructing new Model instance");
    }

    public static final DocumentBuilderFactory getWorkspaceDocumentBuilderFactory()
    {
        return WORKSPACE_DOCUMENT_BUILDER_FACTORY;
    }

    public static final DocumentBuilderFactory getSeamcatDocumentBuilderFactory()
    {
        return SEAMCAT_DOCUMENT_BUILDER_FACTORY;
    }

    public static final TransformerFactory getTransformerFactory()
    {
        return TRANSFORMER_FACTORY;
    }

    public static final String getWorkspaceSchemaUrl()
    {
        return WORKSPACE_SCHEMA_URL;
    }

    private void init()
    {
        try
        {
            File data = new File(xmlPath);
            if(data.exists())
            {
                LOG.debug("Constructing DocumentBuilder");
                DocumentBuilder db = SEAMCAT_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
                db.setErrorHandler(new XmlValidationHandler(false));
                LOG.debug("Reading seamcat.xml");
                Document doc = db.parse(data);
                Element seamcat = doc.getDocumentElement();
                Element lib = (Element)seamcat.getElementsByTagName("library").item(0);
                library = new Library();
                library.init(lib);
                clientSettings = new Vector();
                try
                {
                    LOG.debug("Reading server settings");
                    serverSettings = new ServerSetting((Element)seamcat.getElementsByTagName("server-settings").item(0));
                }
                catch(Exception ex)
                {
                    serverSettings = new ServerSetting();
                }
                try
                {
                    Element el = (Element)seamcat.getElementsByTagName("client-settings").item(0);
                    try
                    {
                        setDefaultEmail(el.getAttribute("defaultEmail"));
                    }
                    catch(Exception e) { }
                    LOG.debug("Reading client settings");
                    NodeList nl = el.getChildNodes();
                    int i = 0;
                    for(int stop = nl.getLength(); i < stop; i++)
                        clientSettings.add(new ClientSetting((Element)nl.item(i)));

                }
                catch(Exception ex) { }
                try
                {
                    LOG.debug("Reading log patterns");
                    Element logPat = (Element)seamcat.getElementsByTagName("log-patterns").item(0);
                    filename = logPat.getAttribute("filename");
                    NodeList pats = logPat.getElementsByTagName("pattern");
                    int i = 0;
                    for(int stop = pats.getLength(); i < stop; i++)
                    {
                        String pattern = ((Element)pats.item(i)).getAttribute("value");
                        logPatterns.add(pattern);
                        if(((Element)pats.item(i)).getAttribute("selected").equals(Boolean.toString(true)))
                            filePattern.setConversionPattern(pattern);
                    }

                }
                catch(Exception ex)
                {
                    LOG.error("Error reading model", ex);
                }
                workspaces = new Components("Workspaces");
            } else
            {
                LOG.info("Creating default library components");
                library = new Library();
                library.init();
                restoreDefaultLogPatterns();
                workspaces = new Components("Workspaces");
                serverSettings = new ServerSetting();
                clientSettings = new Vector();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            library = new Library();
            library.init();
            restoreDefaultLogPatterns();
            workspaces = new Components("Workspaces");
            serverSettings = new ServerSetting();
            clientSettings = new Vector();
        }
        if(logPatterns.size() == 0 || filePattern == null)
            restoreDefaultLogPatterns();
        fileAppender.setAppend(true);
        fileAppender.setLayout(getLogFilePattern());
        if(filename == null || filename.equals(""))
            filename = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("seamcat.log").toString();
        fileAppender.setFile(filename);
        fileAppender.activateOptions();
        LOG.debug("Model initiated successfully");
    }

    public static List getDefaultCDMALinklevelData()
    {
        List data;
        try
        {
            data = CDMAXMLUtils.loadFromXML(new File(defaultCDMAUrl), SEAMCAT_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder());
        }
        catch(Exception e)
        {
            LOG.error((new StringBuilder()).append("An error occured during load of default CDMA LLD: ").append(e.getMessage()).toString());
            data = Collections.EMPTY_LIST;
        }
        return data;
    }

    private static void ensureXmlStructureFiles()
    {
        ensureFile(basicHTMLStyleUrl, origBasicHTMLStyleUrl);
        ensureFile(batchHTMLStyleUrl, origBatchHTMLStyleUrl);
        ensureFile(reportHTMLStyleUrl, origReportHTMLStyleUrl);
        ensureFile(batchCSVStyleUrl, origBatchCSVStyleUrl);
        ensureFile(seamcatSchemaUrl, origSeamcatSchemaUrl);
        ensureFile(seamcatTypesSchemaUrl, origSeamcatTypesSchemaUrl);
        ensureFile(WORKSPACE_SCHEMA_URL, origworkspaceSchemaUrl);
        ensureFile(basicCSVStyleUrl, origCSVStyleUrl);
        ensureFile(reportCSVStyleUrl, origReportCSVStyleUrl);
        ensureFile(defaultCDMAUrl, origDefaultCDMAUrl);
    }

    private static void ensureFile(String url, String origUrl)
    {
        try
        {
            File file = new File(url);
            InputStream ins = new BufferedInputStream(org/seamcat/model/Model.getResourceAsStream(origUrl));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte buffer[] = new byte[4096];
            int readLength;
            while((readLength = ins.read(buffer)) > -1) 
                out.write(buffer, 0, readLength);
            ins.close();
            out.close();
        }
        catch(IOException ex)
        {
            LOG.error((new StringBuilder()).append("Error copying file: ").append(origUrl).toString(), ex);
            ex.printStackTrace(System.out);
        }
    }

    public void addPattern(String pattern)
    {
        filePattern.setConversionPattern(pattern);
        if(!logPatterns.contains(pattern))
            logPatterns.add(pattern);
        try
        {
            persist();
        }
        catch(ParserConfigurationException ex)
        {
            LOG.error("Error while persisting Model", ex);
        }
    }

    public String getLogFilename()
    {
        return filename;
    }

    public void setLogFilename(String _file)
    {
        filename = _file;
        fileAppender.setFile(filename);
        fileAppender.activateOptions();
    }

    public void restoreDefaultLogPatterns()
    {
        logPatterns.clear();
        logPatterns.add("(%F:%L[%M]) - %m%n");
        logPatterns.add("%-5p [%d]: %m%n");
        logPatterns.add("%-5p: %m%n");
        logPatterns.add("[%t]: %m%n");
        logPatterns.add("%m%n");
        logPatterns.add("%d{dd MMM yyyy HH:mm:ss,SSS} %5p [%t] (%F:%L[%M]) - %m%n");
        filePattern.setConversionPattern(((String)logPatterns.get(0)).toString());
    }

    public static final Model getInstance()
    {
        if(!initialized)
        {
            initialized = true;
            MODEL.init();
            MODEL.workspaces.addListDataListener(new ListDataListener() {

                public void contentsChanged(ListDataEvent e)
                {
                    try
                    {
                        Model.getInstance().updateTreeListeners(e);
                    }
                    catch(Exception ex)
                    {
                        Model.LOG.info("Non Critical error occured", ex);
                    }
                }

                public void intervalAdded(ListDataEvent e)
                {
                    try
                    {
                        Model.getInstance().updateTreeListeners(e);
                    }
                    catch(Exception ex)
                    {
                        Model.LOG.info("Non Critical error occured", ex);
                    }
                }

                public void intervalRemoved(ListDataEvent e)
                {
                    try
                    {
                        Model.getInstance().updateTreeListeners(e);
                    }
                    catch(Exception ex)
                    {
                        Model.LOG.info("Non Critical error occured", ex);
                    }
                }

            }
);
        }
        return MODEL;
    }

    public final Library getLibrary()
    {
        return library;
    }

    public final Components getWorkspaces()
    {
        return workspaces;
    }

    public Element toElement(Document doc)
    {
        Element seamcat = doc.createElement("seamcat");
        seamcat.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        seamcat.setAttribute("xsi:noNamespaceSchemaLocation", seamcatSchemaUrl);
        seamcat.appendChild(getLibrary().toElement(doc));
        seamcat.appendChild(serverSettings.toElement(doc));
        Element el = doc.createElement("client-settings");
        el.setAttribute("defaultEmail", defaultEmail);
        ClientSetting c;
        for(Iterator i$ = clientSettings.iterator(); i$.hasNext(); el.appendChild(c.toElement(doc)))
            c = (ClientSetting)i$.next();

        seamcat.appendChild(el);
        Element logPat = doc.createElement("log-patterns");
        logPat.setAttribute("filename", filename);
        int i = 0;
        for(int stop = logPatterns.size(); i < stop; i++)
        {
            Element pat = doc.createElement("pattern");
            pat.setAttribute("value", ((String)logPatterns.get(i)).toString());
            pat.setAttribute("selected", Boolean.toString(filePattern.getConversionPattern().equals(logPatterns.get(i))));
            logPat.appendChild(pat);
        }

        seamcat.appendChild(logPat);
        return seamcat;
    }

    public Appender getLogFileAppender()
    {
        return fileAppender;
    }

    public PatternLayout getLogFilePattern()
    {
        return filePattern;
    }

    public final synchronized void persist()
        throws ParserConfigurationException
    {
        DocumentBuilder db = SEAMCAT_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        Document doc = db.newDocument();
        doc.appendChild(toElement(doc));
        try
        {
            String outputURL = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("seamcat.xml").toString();
            File file = seamcatHome;
            if(!file.exists())
                file.mkdir();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(outputURL));
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.transform(source, result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void fireTreeNodesChanged(TreeModelEvent treeModelEvent)
    {
        Iterator i$ = treeModelListenerCollection.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            TreeModelListener listener = (TreeModelListener)i$.next();
            if(listener != null)
                listener.treeStructureChanged(treeModelEvent);
        } while(true);
    }

    public void updateTreeListeners(ListDataEvent e)
    {
        TreeModelListener l;
        for(Iterator i$ = treeModelListenerCollection.iterator(); i$.hasNext(); l.treeStructureChanged(new TreeModelEvent(this, new TreePath(e.getSource()))))
            l = (TreeModelListener)i$.next();

    }

    public void addTreeModelListener(TreeModelListener l)
    {
        treeModelListenerCollection.add(l);
    }

    public Object getChild(Object parent, int index)
    {
        Node child = null;
        if(parent instanceof Node)
        {
            Node node = (Node)parent;
            child = node.getChildAt(index);
        }
        return child;
    }

    public int getChildCount(Object parent)
    {
        int childCount = 0;
        if(parent instanceof Node)
        {
            Node node = (Node)parent;
            childCount = node.getChildCount();
        }
        return childCount;
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        int indexOfChild = -1;
        if((parent instanceof Node) && (child instanceof Node))
        {
            Node node = (Node)parent;
            indexOfChild = node.getIndex((Node)child);
        }
        return indexOfChild;
    }

    public Object getRoot()
    {
        return this;
    }

    public boolean isLeaf(Object node)
    {
        boolean isLeaf = false;
        if(node instanceof Node)
        {
            Node n = (Node)node;
            isLeaf = n.isLeaf();
        }
        return isLeaf;
    }

    public void removeTreeModelListener(TreeModelListener l)
    {
        treeModelListenerCollection.remove(l);
    }

    public void valueForPathChanged(TreePath treepath, Object obj)
    {
    }

    public Enumeration children()
    {
        return null;
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public Node getChildAt(int childIndex)
    {
        Node childAt = null;
        if(childIndex == 0)
            childAt = library;
        else
        if(childIndex > 0 && childIndex <= workspaces.size())
            childAt = (Node)workspaces.getElementAt(childIndex - 1);
        return childAt;
    }

    public int getChildCount()
    {
        return 1 + workspaces.size();
    }

    public int getIndex(Node node)
    {
        int index = -1;
        if(node == library)
        {
            index = 0;
        } else
        {
            Object ws[] = workspaces.toArray();
            for(int i = 0; i < ws.length; i++)
                if(ws[i] == node)
                    index = i + 1;

        }
        return index;
    }

    public Node getParent()
    {
        return null;
    }

    public boolean isLeaf()
    {
        return false;
    }

    public NodeAttribute[] getNodeAttributes()
    {
        return null;
    }

    public Vector getLogPatterns()
    {
        return logPatterns;
    }

    public File getSeamcatHome()
    {
        return seamcatHome;
    }

    public static String getReportHTMLStyleUrl()
    {
        return reportHTMLStyleUrl;
    }

    public static String getReportCSVStyleUrl()
    {
        return reportCSVStyleUrl;
    }

    public static String getBatchHTMLStyleUrl()
    {
        return batchHTMLStyleUrl;
    }

    public static String getBatchCSVStyleUrl()
    {
        return batchCSVStyleUrl;
    }

    public static final boolean checkFilename(String filename)
    {
        boolean ok;
label0:
        {
            int x = 0;
label1:
            for(int filechars = filename.length(); x < filechars; x++)
            {
                char filechar = filename.charAt(x);
                int y = 0;
                do
                {
                    if(y >= ILLEGAL_FILENAME_CHARS.length)
                        continue label1;
                    if(filechar == ILLEGAL_FILENAME_CHARS[y])
                    {
                        ok = false;
                        break label0;
                    }
                    y++;
                } while(true);
            }

            ok = true;
        }
        return ok;
    }

    public static final String getDefaultWorkspacePath()
    {
        return (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("workspaces").append(File.separator).append("default-workspace.xml").toString();
    }

    private static final void copyDefaultWorkspace(File destination)
    {
        if(destination.exists())
        {
            String origPath = destination.getAbsolutePath();
            destination.renameTo(new File((new StringBuilder()).append(destination.getParentFile().getAbsolutePath()).append(File.separator).append(destination.getName()).append(".backup").toString()));
            destination = new File(origPath);
            LOG.info("Appended '.backup' to existing default workspace. New version was copied from jar");
        }
        File wsDir = new File((new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("workspaces").toString());
        if(!wsDir.isDirectory())
            wsDir.mkdir();
        try
        {
            InputStream ins = new BufferedInputStream(org/seamcat/model/Workspace.getResourceAsStream("/default-workspace.xml"));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
            byte buffer[] = new byte[4096];
            int readLength;
            while((readLength = ins.read(buffer)) > -1) 
                out.write(buffer, 0, readLength);
            ins.close();
            out.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        workspaceVersionChecked = true;
    }

    public static final Workspace openDefaultWorkspace()
        throws Exception
    {
        File file = new File((new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("workspaces").append(File.separator).append("default-workspace.xml").toString());
        if(!file.exists())
            copyDefaultWorkspace(file);
        else
        if(!workspaceVersionChecked)
            try
            {
                DocumentBuilder db = WORKSPACE_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
                Document doc = db.parse(file);
                String versionAtt = doc.getElementsByTagName("Workspace").item(0).getAttributes().getNamedItem("seamcat_version").getNodeValue();
                if(!versionAtt.equals(STRINGLIST.getString("APPLICATION_TITLE")))
                    copyDefaultWorkspace(file);
            }
            catch(Exception ex)
            {
                copyDefaultWorkspace(file);
            }
        Workspace ws = openWorkspace(file);
        ws.setAbsoluteLocation(null);
        String reference = (new StringBuilder()).append(STRINGLIST.getString("WORKSPACE_NAME_PREFIX")).append(Workspace.untitledCount).toString();
        ws.setReference(reference);
        try
        {
            int i = 1;
            for(Iterator i$ = ws.getInterferenceLinks().iterator(); i$.hasNext();)
            {
                InterferenceLink link = (InterferenceLink)i$.next();
                link.setReference((new StringBuilder()).append(reference).append(STRINGLIST.getString("WORKSPACE_INTERFERENCELINK_POSTFIX")).append(i).toString());
                link.setBlockingVector((IRSSVector)ws.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(i));
                link.setUnwantedVector((IRSSVector)ws.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(i));
                ((IRSSVector)ws.getSignals().getIRSSVectorListBlocking().getIRSSVectors().get(i)).setReference((new StringBuilder()).append(reference).append(" Blocking").toString());
                ((IRSSVector)ws.getSignals().getIRSSVectorListUnwanted().getIRSSVectors().get(i)).setReference((new StringBuilder()).append(reference).append(" Unwanted Emission").toString());
                i++;
            }

        }
        catch(Exception ex) { }
        Workspace.untitledCount++;
        try
        {
            MODEL.persist();
        }
        catch(ParserConfigurationException ex1)
        {
            ex1.printStackTrace();
        }
        return ws;
    }

    public Workspace loadWorkspace(File file)
        throws Exception
    {
        Workspace wks = openWorkspace(file);
        getWorkspaces().add(wks);
        return wks;
    }

    public static final Workspace openWorkspace(File file)
        throws Exception
    {
        Workspace wks = openWorkspace(((InputStream) (new BufferedInputStream(new FileInputStream(file)))));
        wks.setAbsoluteLocation(file.getAbsolutePath());
        return wks;
    }

    public static final Workspace openWorkspace(InputStream file)
        throws Exception
    {
        DocumentBuilder db = WORKSPACE_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        db.setErrorHandler(new XmlValidationHandler(false));
        Document doc = db.parse(file);
        Workspace wks = new Workspace((Element)doc.getElementsByTagName("Workspace").item(0));
        wks.setIsSaved(true);
        return wks;
    }

    public static final Workspace openBrokenWorkspace(File file)
        throws Exception
    {
        File temp = removeLineBreaks(file);
        Workspace work = openWorkspace(temp);
        LOG.info("Deleting temporary file");
        temp.delete();
        return work;
    }

    public static final File removeLineBreaks(File file)
        throws Exception
    {
        long start = System.currentTimeMillis();
        File temp = new File(file.getParentFile(), (new StringBuilder()).append("temp").append(System.currentTimeMillis()).append(".sws").toString());
        temp.createNewFile();
        BufferedReader inp = new BufferedReader(new FileReader(file));
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        String lastLine = null;
        StringBuffer buf = null;
        String curLine;
        while((curLine = inp.readLine()) != null) 
        {
            buf = new StringBuffer(curLine);
            for(int index = 0; (index = buf.indexOf("\t")) > -1;)
                buf.deleteCharAt(index);

            curLine = buf.toString();
            if(lastLine == null || curLine.charAt(0) != ' ' || lastLine.endsWith("\"") || curLine.charAt(1) == '"')
                out.write(curLine);
            else
                out.write(curLine.substring(1));
            lastLine = curLine;
        }
        inp.close();
        out.flush();
        out.close();
        long end = System.currentTimeMillis();
        LOG.info((new StringBuilder()).append("Removed all linebreaks from file in ").append(end - start).append(" milliseconds").toString());
        return temp;
    }

    public static final void saveWorkspace(Workspace wks, File file, boolean saveResults)
    {
        LOG.debug((new StringBuilder()).append("Saving to file:").append(file.getAbsolutePath()).toString());
        wks.setStoreResults(saveResults);
        wks.setStoreSignals(saveResults);
        wks.setExpandSignals(saveResults);
        Thread saveThread = new Thread((new StringBuilder()).append("Saving ").append(wks.getReference()).toString(), wks, file) {

            public void run()
            {
                try
                {
                    long start = System.currentTimeMillis();
                    DocumentBuilder db = Model.WORKSPACE_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
                    Document doc = db.newDocument();
                    Element element = wks.toElement(doc);
                    doc.appendChild(element);
                    long end = System.currentTimeMillis();
                    Model.LOG.info((new StringBuilder()).append("Time to create document: ").append(end - start).append(" ms").toString());
                    start = System.currentTimeMillis();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new BufferedOutputStream(new FileOutputStream(file)));
                    TransformerFactory transFactory = TransformerFactory.newInstance();
                    Transformer transformer = transFactory.newTransformer();
                    transformer.transform(source, result);
                    result.getOutputStream().close();
                    end = System.currentTimeMillis();
                    Model.LOG.info((new StringBuilder()).append("Time to transform document: ").append(end - start).append(" ms").toString());
                    wks.setIsSaved(true);
                }
                catch(Exception e)
                {
                    Model.LOG.error("Workspace save failed", e);
                }
            }

            final Workspace val$wks;
            final File val$file;

            
            {
                wks = workspace;
                file = file1;
                super(x0);
            }
        }
;
        saveThread.start();
    }

    public ServerSetting getServerSettings()
    {
        return serverSettings;
    }

    public void setServerSettings(ServerSetting serverSettings)
    {
        this.serverSettings = serverSettings;
    }

    public Vector getClientSettings()
    {
        return clientSettings;
    }

    public void addClientSetting(ClientSetting cs)
    {
        clientSettings.add(cs);
    }

    public SeamcatServerSocket createServerSocket(WorkspaceProcessor wp)
    {
        seamcatServerSocket = new SeamcatServerSocket(serverSettings, wp);
        return seamcatServerSocket;
    }

    public static SeamcatServerSocket getSeamcatServerSocket()
    {
        return seamcatServerSocket;
    }

    public WorkspaceProcessor createWorkspaceProcessor()
    {
        workspaceProcessor = new WorkspaceProcessor(serverSettings);
        return workspaceProcessor;
    }

    public static WorkspaceProcessor getWorkspaceProcessor()
    {
        return workspaceProcessor;
    }

    public String getDefaultEmail()
    {
        return defaultEmail;
    }

    public void setDefaultEmail(String email)
    {
        defaultEmail = email;
    }

    public boolean isUseHigherPriorityThreads()
    {
        return useHigherPriorityThreads;
    }

    public void setUseHigherPriorityThreads(boolean useHighPriorityThreads)
    {
        useHigherPriorityThreads = useHighPriorityThreads;
    }

    private static final Logger LOG;
    private static final ResourceBundle STRINGLIST;
    public static final DocumentBuilderFactory SEAMCAT_DOCUMENT_BUILDER_FACTORY;
    private static final DocumentBuilderFactory WORKSPACE_DOCUMENT_BUILDER_FACTORY;
    public static final String HOMEDIR_KEY = "Seamcat directory";
    private static final char ILLEGAL_FILENAME_CHARS[] = {
        '\\', '/', ':', '*', '<', '>', '|'
    };
    public static File seamcatHome;
    public static File seamcatTemp;
    private static final Model MODEL = new Model();
    private static boolean initialized = false;
    private final List treeModelListenerCollection = new ArrayList();
    private static File pluginsHome;
    private static String seamcatSchemaUrl;
    private static String origSeamcatSchemaUrl = "/seamcat.xsd";
    private static String seamcatTypesSchemaUrl;
    private static String origSeamcatTypesSchemaUrl = "/seamcat_types.xsd";
    private static String WORKSPACE_SCHEMA_URL;
    private static String origworkspaceSchemaUrl = "/workspace.xsd";
    private static String reportHTMLStyleUrl;
    private static String origReportHTMLStyleUrl = "/scenario_report_html.xsl";
    private static String reportCSVStyleUrl;
    private static String origReportCSVStyleUrl = "/scenario_report_csv.xsl";
    private static String batchHTMLStyleUrl;
    private static String origBatchHTMLStyleUrl = "/batch_report_html.xsl";
    private static String batchCSVStyleUrl;
    private static String origBatchCSVStyleUrl = "/batch_report_csv.xsl";
    private static String basicHTMLStyleUrl;
    private static String origBasicHTMLStyleUrl = "/seamcat_html_base_templates.xsl";
    private static String basicCSVStyleUrl;
    private static String origCSVStyleUrl = "/seamcat_base_templates_csv.xsl";
    private static String pluginFile;
    private static String origPluginFile;
    private static String xmlPath;
    private static String origDefaultCDMAUrl = "/default-link-level-data.xml";
    private static String defaultCDMAUrl;
    private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();
    private final Vector logPatterns = new Vector();
    private final PatternLayout filePattern = new PatternLayout();
    private static boolean workspaceVersionChecked = false;
    private FileAppender fileAppender;
    private String filename;
    private Library library;
    private Components workspaces;
    private ServerSetting serverSettings;
    private Vector clientSettings;
    private String defaultEmail;
    private static WorkspaceProcessor workspaceProcessor;
    private static SeamcatServerSocket seamcatServerSocket;
    private boolean useHigherPriorityThreads;
    public static boolean usePostProcessingPlugins = false;

    static 
    {
        LOG = Logger.getLogger(org/seamcat/model/Model);
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        Preferences pref = Preferences.userNodeForPackage(org/seamcat/model/Model);
        String scdir = pref.get("Seamcat directory", null);
        if(scdir == null)
        {
            SeamcatHomeSelector shs = new SeamcatHomeSelector(Seamcat.getSplashScreen());
            scdir = shs.getDirectory();
            if(shs.store())
                pref.put("Seamcat directory", scdir);
        }
        LOG.info((new StringBuilder()).append("Using home: ").append(scdir).toString());
        seamcatHome = new File(scdir);
        if(!seamcatHome.exists())
            seamcatHome.mkdir();
        seamcatTemp = new File((new StringBuilder()).append(scdir).append(File.separator).append("temp").toString());
        if(!seamcatTemp.exists())
            seamcatTemp.mkdir();
        seamcatSchemaUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("seamcat.xsd").toString();
        seamcatTypesSchemaUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("seamcat_types.xsd").toString();
        WORKSPACE_SCHEMA_URL = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("workspace.xsd").toString();
        reportHTMLStyleUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("scenario_report_html.xsl").toString();
        reportCSVStyleUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("scenario_report_csv.xsl").toString();
        batchHTMLStyleUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("batch_report_html.xsl").toString();
        batchCSVStyleUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("batch_report_csv.xsl").toString();
        basicHTMLStyleUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("seamcat_html_base_templates.xsl").toString();
        basicCSVStyleUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("seamcat_base_templates_csv.xsl").toString();
        defaultCDMAUrl = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("default-link-level-data.xml").toString();
        ensureXmlStructureFiles();
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        SEAMCAT_DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
        SEAMCAT_DOCUMENT_BUILDER_FACTORY.setNamespaceAware(true);
        SEAMCAT_DOCUMENT_BUILDER_FACTORY.setValidating(true);
        SEAMCAT_DOCUMENT_BUILDER_FACTORY.setIgnoringElementContentWhitespace(true);
        SEAMCAT_DOCUMENT_BUILDER_FACTORY.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        SEAMCAT_DOCUMENT_BUILDER_FACTORY.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", seamcatSchemaUrl);
        WORKSPACE_DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
        WORKSPACE_DOCUMENT_BUILDER_FACTORY.setNamespaceAware(true);
        WORKSPACE_DOCUMENT_BUILDER_FACTORY.setValidating(true);
        WORKSPACE_DOCUMENT_BUILDER_FACTORY.setIgnoringElementContentWhitespace(true);
        WORKSPACE_DOCUMENT_BUILDER_FACTORY.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        WORKSPACE_DOCUMENT_BUILDER_FACTORY.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", WORKSPACE_SCHEMA_URL);
        pluginsHome = new File((new StringBuilder()).append(scdir).append(File.separator).append("plugins").toString());
        xmlPath = (new StringBuilder()).append(seamcatHome.getAbsolutePath()).append(File.separator).append("seamcat.xml").toString();
        if(!pluginsHome.exists())
        {
            pluginsHome.mkdirs();
            try
            {
                pluginFile = (new StringBuilder()).append(pluginsHome.getAbsolutePath()).append(File.separator).append("seamcat_needed_for_plugin.jar").toString();
                origPluginFile = "/seamcat_needed_for_plugin.jar";
                ensureFile(pluginFile, origPluginFile);
            }
            catch(Exception e)
            {
                LOG.error("Error restoring plugin jar");
            }
        } else
        {
            FileFilter filter = new FileFilter() {

                public boolean accept(File file)
                {
                    return file.getName().toLowerCase().endsWith(".jar") || file.getName().toLowerCase().endsWith(".class");
                }

            }
;
            File files[] = pluginsHome.listFiles(filter);
            try
            {
                pluginFile = (new StringBuilder()).append(pluginsHome.getAbsolutePath()).append(File.separator).append("seamcat_needed_for_plugin.jar").toString();
                origPluginFile = "/seamcat_needed_for_plugin.jar";
                ensureFile(pluginFile, origPluginFile);
            }
            catch(Exception e)
            {
                LOG.error("Error restoring plugin jar");
            }
            for(int x = 0; x < files.length; x++)
                try
                {
                    LOG.info((new StringBuilder()).append("Loading plugin class: ").append(files[x].getAbsolutePath()).toString());
                    ClassPathHacker.addFile(files[x]);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            try
            {
                ClassPathHacker.addFile(pluginsHome);
            }
            catch(IOException e)
            {
                LOG.debug("Error adding plugin", e);
            }
        }
    }


}