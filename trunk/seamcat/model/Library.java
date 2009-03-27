// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Library.java

package org.seamcat.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.seamcat.batch.BatchJobList;
import org.seamcat.cdma.xml.CDMAXMLUtils;
import org.seamcat.interfaces.ImportLibraryConflictListener;
import org.seamcat.model.propagation.PluginModelWrapper;
import org.seamcat.postprocessing.PostProcessingPluginWrapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

// Referenced classes of package org.seamcat.model:
//            SeamcatComponent, Components, Antenna, Receiver, 
//            Transmitter, Model, Workspace

public class Library extends SeamcatComponent
{

    public Library()
    {
        tableListenerCollection = new ArrayList();
        setReference("Library");
        antennas.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalAdded(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalRemoved(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        receivers.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalAdded(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalRemoved(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        transmitters.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalAdded(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalRemoved(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        cdmalinklevel.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                try
                {
                    try
                    {
                        Model.getInstance().updateTreeListeners(e);
                    }
                    catch(Exception ex1) { }
                }
                catch(Exception ex) { }
            }

            public void intervalAdded(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalRemoved(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        plugins.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalAdded(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalRemoved(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        batchjoblists.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalAdded(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalRemoved(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        procplugins.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalAdded(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            public void intervalRemoved(ListDataEvent e)
            {
                try
                {
                    Model.getInstance().updateTreeListeners(e);
                }
                catch(Exception ex) { }
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
    }

    public void init()
    {
        antennas.add(new Antenna());
        receivers.add(new Receiver());
        transmitters.add(new Transmitter());
        cdmalinklevel.addAll(Model.getDefaultCDMALinklevelData());
    }

    public void init(Element element)
    {
        try
        {
            Log.debug("Initializing library");
            int untCount = Integer.parseInt(element.getAttribute("workspace_count"));
            Workspace.untitledCount = untCount;
        }
        catch(NumberFormatException ex)
        {
            LOG.error((new StringBuilder()).append("Unable to parse: ").append(element.getAttribute("workspace_count")).toString());
        }
        try
        {
            boolean usePlugins = Boolean.parseBoolean(element.getAttribute("use_postprocessing"));
            Model.usePostProcessingPlugins = usePlugins;
        }
        catch(Exception ex) { }
        try
        {
            String value = element.getAttribute("use_high_priority_threads");
            if(value == null || value.equals(""))
            {
                Model.getInstance().setUseHigherPriorityThreads(true);
            } else
            {
                boolean useLowPriority = Boolean.parseBoolean(value);
                Model.getInstance().setUseHigherPriorityThreads(useLowPriority);
            }
        }
        catch(Exception ex) { }
        Log.debug("Reading Antennas");
        NodeList anten = element.getElementsByTagName("antennas");
        try
        {
            NodeList nl = ((Element)anten.item(0)).getElementsByTagName("antenna");
            int i = 0;
            for(int size = nl.getLength(); i < size; i++)
            {
                Node node = nl.item(i);
                Antenna antenna = new Antenna((Element)node);
                antennas.add(antenna);
            }

        }
        catch(Exception e)
        {
            LOG.debug("Error reading antennas from seamcat.xml", e);
        }
        Log.debug("Reading Receivers");
        NodeList receiv = element.getElementsByTagName("receivers");
        try
        {
            NodeList nl;
            if(receiv.getLength() == 0)
                nl = element.getElementsByTagName("receiver");
            else
                nl = ((Element)receiv.item(0)).getElementsByTagName("receiver");
            int i = 0;
            for(int size = nl.getLength(); i < size; i++)
            {
                Receiver receiver = new Receiver((Element)nl.item(i), antennas);
                receivers.add(receiver);
            }

        }
        catch(Exception e)
        {
            LOG.debug("Error reading receivers from seamcat.xml", e);
        }
        Log.debug("Reading Transmitters");
        NodeList transmit = element.getElementsByTagName("transmitters");
        try
        {
            NodeList nl;
            if(transmit.getLength() == 0)
                nl = element.getElementsByTagName("transmitter");
            else
                nl = ((Element)transmit.item(0)).getElementsByTagName("transmitter");
            int i = 0;
            for(int size = nl.getLength(); i < size; i++)
            {
                Transmitter transmitter = new Transmitter((Element)nl.item(i), antennas);
                transmitters.add(transmitter);
            }

        }
        catch(Exception e)
        {
            LOG.debug("Error reading tranmitters from seamcat.xml", e);
        }
        Log.debug("Propagation Model Plug-ins");
        NodeList plugi = element.getElementsByTagName("plugins");
        try
        {
            NodeList nl;
            if(plugi.getLength() == 0)
                nl = element.getElementsByTagName("PluginModel");
            else
                nl = ((Element)plugi.item(0)).getElementsByTagName("PluginModel");
            int i = 0;
            for(int size = nl.getLength(); i < size; i++)
                try
                {
                    PluginModelWrapper plugin = new PluginModelWrapper((Element)nl.item(i));
                    plugins.add(plugin);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    LOG.debug((new StringBuilder()).append("Could not load plugin propagation model <").append(e.getMessage()).append(">").toString());
                }

        }
        catch(Exception e)
        {
            LOG.debug("Error reading plugins from seamcat.xml", e);
        }
        Log.debug("Reading Link Level Data");
        List llds = CDMAXMLUtils.getDataFromLibrary(element);
        if(llds.size() > 0)
        {
            cdmalinklevel.addAll(llds);
        } else
        {
            LOG.debug("Loading default CDMA link level data");
            cdmalinklevel.addAll(Model.getDefaultCDMALinklevelData());
        }
        Log.debug("Reading Batch Jobs");
        NodeList batchjlist = element.getElementsByTagName("batchjoblists");
        try
        {
            if(batchjlist.getLength() > 0)
            {
                NodeList nl = ((Element)batchjlist.item(0)).getElementsByTagName("BatchJobList");
                int i = 0;
                for(int size = nl.getLength(); i < size; i++)
                {
                    BatchJobList batchjoblist = new BatchJobList((Element)nl.item(i));
                    batchjoblists.add(batchjoblist);
                }

            }
        }
        catch(Exception e)
        {
            LOG.error("Error reading batchjobs from seamcat.xml", e);
            e.printStackTrace();
        }
        Log.debug("Reading Post Processing Plug-ins");
        batchjlist = element.getElementsByTagName("postprocessing-plugins");
        try
        {
            NodeList nl;
            if(batchjlist.getLength() == 0)
                nl = element.getElementsByTagName("PostProcessingPlugin");
            else
                nl = ((Element)batchjlist.item(0)).getElementsByTagName("PostProcessingPlugin");
            int i = 0;
            for(int size = nl.getLength(); i < size; i++)
                try
                {
                    PostProcessingPluginWrapper plugin = new PostProcessingPluginWrapper((Element)nl.item(i));
                    procplugins.add(plugin);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    LOG.error((new StringBuilder()).append("Could not load post processing plugin <").append(e.getMessage()).append(">").toString());
                }

        }
        catch(Exception e)
        {
            LOG.debug("Error reading plugins from seamcat.xml", e);
        }
        Log.debug("Initializing library containers");
        if(antennas.getSize() == 0)
            antennas.add(new Antenna());
        if(receivers.getSize() == 0)
            receivers.add(new Receiver());
        if(transmitters.getSize() == 0)
            transmitters.add(new Transmitter());
        antennas.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalAdded(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalRemoved(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        receivers.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalAdded(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalRemoved(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        transmitters.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalAdded(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalRemoved(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        plugins.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalAdded(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalRemoved(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
        batchjoblists.addListDataListener(new ListDataListener() {

            public void contentsChanged(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalAdded(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            public void intervalRemoved(ListDataEvent e)
            {
                Model.getInstance().updateTreeListeners(e);
            }

            final Library this$0;

            
            {
                this$0 = Library.this;
                super();
            }
        }
);
    }

    public Components getAntennas()
    {
        return antennas;
    }

    public Components getReceivers()
    {
        return receivers;
    }

    public Components getCDMALinkLevelData()
    {
        return cdmalinklevel;
    }

    public Receiver getFirstReceiver()
    {
        return (Receiver)getReceivers().getElementAt(0);
    }

    public Components getTransmitters()
    {
        return transmitters;
    }

    public Components getPlugins()
    {
        return plugins;
    }

    public Components getBatchjoblists()
    {
        return batchjoblists;
    }

    public Transmitter getFirstTransmitter()
    {
        return (Transmitter)getTransmitters().getElementAt(0);
    }

    public synchronized void importLibrary(File importFile, ImportLibraryConflictListener ilcl)
        throws ParserConfigurationException, SAXException, IOException
    {
        if(importFile == null)
            throw new NullPointerException();
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(importFile);
        Element root = doc.getDocumentElement();
        Element lib = (Element)root.getElementsByTagName("library").item(0);
        NodeList antennas = ((Element)lib.getElementsByTagName("antennas").item(0)).getElementsByTagName("antenna");
        for(int i = 0; i < antennas.getLength(); i++)
        {
            Antenna antenna = new Antenna((Element)antennas.item(i));
            boolean hit = false;
            boolean unique = false;
            while(!unique) 
            {
                hit = false;
                Iterator i$ = getAntennas().iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    Antenna a = (Antenna)i$.next();
                    if(a.getReference().equalsIgnoreCase(antenna.getReference()))
                        hit = true;
                } while(true);
                if(hit)
                {
                    int seq = 0;
                    boolean isUnique = true;
                    do
                    {
                        isUnique = true;
                        seq++;
                        String newReference = (new StringBuilder()).append(antenna.getReference()).append(seq).toString();
                        Iterator i$ = getAntennas().iterator();
                        do
                        {
                            if(!i$.hasNext())
                                break;
                            Antenna ant = (Antenna)i$.next();
                            if(newReference.equalsIgnoreCase(ant.getReference()))
                                isUnique = false;
                        } while(true);
                    } while(!isUnique);
                    String newRef = ilcl.handleReferenceConflict(antenna.getReference(), (new StringBuilder()).append(antenna.getReference()).append(seq).toString());
                    if(newRef == null)
                    {
                        antenna.setReference((new StringBuilder()).append(antenna.getReference()).append(seq).toString());
                        getAntennas().add(antenna);
                        unique = true;
                    } else
                    if(antenna.getReference().equalsIgnoreCase(newRef))
                    {
                        unique = true;
                        for(int j = 0; j < getAntennas().size(); j++)
                        {
                            Antenna a = (Antenna)getAntennas().get(j);
                            if(a.getReference().equalsIgnoreCase(antenna.getReference()))
                                getAntennas().remove(a);
                        }

                        getAntennas().add(antenna);
                    } else
                    {
                        antenna.setReference(newRef);
                        unique = false;
                    }
                } else
                {
                    unique = true;
                    getAntennas().add(antenna);
                }
            }
        }

        NodeList receivers = lib.getElementsByTagName("receiver");
        for(int i = 0; i < receivers.getLength(); i++)
        {
            Receiver receiver = new Receiver((Element)receivers.item(i));
            boolean hit = false;
            boolean unique = false;
            while(!unique) 
            {
                hit = false;
                Iterator i$ = getReceivers().iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    Receiver a = (Receiver)i$.next();
                    if(a.getReference().equalsIgnoreCase(receiver.getReference()))
                        hit = true;
                } while(true);
                if(hit)
                {
                    int seq = 0;
                    boolean isUnique = true;
                    do
                    {
                        isUnique = true;
                        seq++;
                        String newReference = (new StringBuilder()).append(receiver.getReference()).append(seq).toString();
                        Iterator i$ = getReceivers().iterator();
                        do
                        {
                            if(!i$.hasNext())
                                break;
                            Receiver ant = (Receiver)i$.next();
                            if(newReference.equalsIgnoreCase(ant.getReference()))
                                isUnique = false;
                        } while(true);
                    } while(!isUnique);
                    String newRef = ilcl.handleReferenceConflict(receiver.getReference(), (new StringBuilder()).append(receiver.getReference()).append(seq).toString());
                    if(newRef == null)
                    {
                        receiver.setReference((new StringBuilder()).append(receiver.getReference()).append(seq).toString());
                        getReceivers().add(receiver);
                        unique = true;
                    } else
                    if(receiver.getReference().equalsIgnoreCase(newRef))
                    {
                        unique = true;
                        Iterator iterator2 = getReceivers().iterator();
                        do
                        {
                            if(!iterator2.hasNext())
                                break;
                            Receiver a = (Receiver)iterator2.next();
                            if(a.getReference().equalsIgnoreCase(receiver.getReference()))
                                iterator2.remove();
                        } while(true);
                        getReceivers().add(receiver);
                    } else
                    {
                        receiver.setReference(newRef);
                        unique = false;
                    }
                } else
                {
                    unique = true;
                    getReceivers().add(receiver);
                }
            }
        }

        NodeList transmitters = lib.getElementsByTagName("transmitter");
        for(int i = 0; i < transmitters.getLength(); i++)
        {
            Transmitter transmitter = new Transmitter((Element)transmitters.item(i));
            boolean hit = false;
            boolean unique = false;
            while(!unique) 
            {
                hit = false;
                Iterator i$ = getTransmitters().iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    Transmitter a = (Transmitter)i$.next();
                    if(a.getReference().equalsIgnoreCase(transmitter.getReference()))
                        hit = true;
                } while(true);
                if(hit)
                {
                    int seq = 0;
                    boolean isUnique = true;
                    do
                    {
                        isUnique = true;
                        seq++;
                        String newReference = (new StringBuilder()).append(transmitter.getReference()).append(seq).toString();
                        Iterator i$ = getTransmitters().iterator();
                        do
                        {
                            if(!i$.hasNext())
                                break;
                            Transmitter ant = (Transmitter)i$.next();
                            if(newReference.equalsIgnoreCase(ant.getReference()))
                                isUnique = false;
                        } while(true);
                    } while(!isUnique);
                    String newRef = ilcl.handleReferenceConflict(transmitter.getReference(), (new StringBuilder()).append(transmitter.getReference()).append(seq).toString());
                    if(newRef == null)
                    {
                        transmitter.setReference((new StringBuilder()).append(transmitter.getReference()).append(seq).toString());
                        getTransmitters().add(transmitter);
                        unique = true;
                    } else
                    if(transmitter.getReference().equalsIgnoreCase(newRef))
                    {
                        unique = true;
                        Iterator iterator2 = getTransmitters().iterator();
                        do
                        {
                            if(!iterator2.hasNext())
                                break;
                            Transmitter a = (Transmitter)iterator2.next();
                            if(a.getReference().equalsIgnoreCase(transmitter.getReference()))
                                iterator2.remove();
                        } while(true);
                        getTransmitters().add(transmitter);
                    } else
                    {
                        transmitter.setReference(newRef);
                        unique = false;
                    }
                } else
                {
                    unique = true;
                    getTransmitters().add(transmitter);
                }
            }
        }

        cdmalinklevel.addAll(CDMAXMLUtils.getDataFromLibrary(lib));
    }

    public void exportLibrary(File file)
        throws ParserConfigurationException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        doc.appendChild(doc.createElement("seamcat")).appendChild(toElement(doc, true));
        try
        {
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileOutputStream(file));
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.transform(source, result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Element toElement(Document doc)
    {
        return toElement(doc, false);
    }

    public Element toElement(Document doc, boolean export)
    {
        Element library = doc.createElement("library");
        library.setAttribute("workspace_count", Integer.toString(Workspace.untitledCount));
        library.setAttribute("use_postprocessing", Boolean.toString(Model.usePostProcessingPlugins));
        library.setAttribute("use_high_priority_threads", Boolean.toString(Model.getInstance().isUseHigherPriorityThreads()));
        Element antennasElement = doc.createElement("antennas");
        Antenna antenna;
        for(Iterator i$ = antennas.iterator(); i$.hasNext(); antennasElement.appendChild(antenna.toElement(doc)))
            antenna = (Antenna)i$.next();

        library.appendChild(antennasElement);
        Element receiversElement = doc.createElement("receivers");
        Receiver receiver;
        for(Iterator i$ = receivers.iterator(); i$.hasNext(); receiversElement.appendChild(receiver.toElement(doc)))
            receiver = (Receiver)i$.next();

        library.appendChild(receiversElement);
        Element transmittersElement = doc.createElement("transmitters");
        Transmitter transmitter;
        for(Iterator i$ = transmitters.iterator(); i$.hasNext(); transmittersElement.appendChild(transmitter.toElement(doc)))
            transmitter = (Transmitter)i$.next();

        library.appendChild(transmittersElement);
        Element pluginsElement = doc.createElement("plugins");
        PluginModelWrapper plugin;
        for(Iterator i$ = plugins.iterator(); i$.hasNext(); pluginsElement.appendChild(plugin.toElement(doc)))
            plugin = (PluginModelWrapper)i$.next();

        library.appendChild(pluginsElement);
        library.appendChild(CDMAXMLUtils.createCDMALibraryElement(doc, cdmalinklevel));
        if(!export)
        {
            Element batchjoblistsElement = doc.createElement("batchjoblists");
            BatchJobList batch;
            for(Iterator i$ = batchjoblists.iterator(); i$.hasNext(); batchjoblistsElement.appendChild(batch.toElement(doc)))
            {
                batch = (BatchJobList)i$.next();
                batch.setStoreControl(true);
                batch.setStoreResults(true);
                batch.setStoreScenario(true);
                batch.setStoreSignals(false);
            }

            library.appendChild(batchjoblistsElement);
        }
        Element postPluginsElement = doc.createElement("postprocessing-plugins");
        PostProcessingPluginWrapper plugin;
        for(Iterator i$ = procplugins.iterator(); i$.hasNext(); postPluginsElement.appendChild(plugin.toElement(doc)))
            plugin = (PostProcessingPluginWrapper)i$.next();

        library.appendChild(postPluginsElement);
        return library;
    }

    public String toString()
    {
        return "Library";
    }

    public void addTableModelListener(TableModelListener l)
    {
        tableListenerCollection.add(l);
    }

    public Class getColumnClass(int columnIndex)
    {
        return java/lang/String;
    }

    public int getColumnCount()
    {
        return 4;
    }

    public String getColumnName(int columnIndex)
    {
        String columnName = "";
        if(columnIndex == 0)
            columnName = "Name";
        else
        if(columnIndex == 1)
            columnName = "Value";
        else
        if(columnIndex == 2)
            columnName = "Unit";
        else
        if(columnIndex == 3)
            columnName = "Type";
        return columnName;
    }

    public int getRowCount()
    {
        return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        String value = "";
        if(columnIndex == 0)
        {
            if(rowIndex == 0)
                value = ((Antenna)antennas.getElementAt(0)).toString();
            else
            if(rowIndex == 1)
                value = ((Transmitter)transmitters.getElementAt(0)).toString();
            else
            if(rowIndex == 2)
                value = ((Receiver)receivers.getElementAt(0)).toString();
        } else
        if(columnIndex == 3)
            value = "Folder";
        return value;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public void removeTableModelListener(TableModelListener l)
    {
        tableListenerCollection.remove(l);
    }

    public void setValueAt(Object obj, int i, int j)
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

    public org.seamcat.presentation.Node getChildAt(int childIndex)
    {
        if(childIndex == 0)
            return antennas;
        if(childIndex == 1)
            return transmitters;
        if(childIndex == 2)
            return receivers;
        else
            return null;
    }

    public int getChildCount()
    {
        return 3;
    }

    public int getIndex(org.seamcat.presentation.Node node)
    {
        if(node == antennas)
            return 0;
        if(node == transmitters)
            return 1;
        return node != receivers ? -1 : 2;
    }

    public org.seamcat.presentation.Node getParent()
    {
        return Model.getInstance();
    }

    public boolean isLeaf()
    {
        return false;
    }

    public Object getValue(int rowIndex)
    {
        return "";
    }

    public Object getUnitValue(int rowIndex)
    {
        return "";
    }

    protected void initNodeAttributes()
    {
    }

    public Components getPostProcessingPlugins()
    {
        return procplugins;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/Library);
    private final Components antennas = new Components("Antennas");
    private final Components receivers = new Components("Receivers");
    private final Components transmitters = new Components("Transmitters");
    private final Components cdmalinklevel = new Components("CDMA Link-level Data");
    private final Components plugins = new Components("Propagation model plugins");
    private final Components procplugins = new Components("Postprocessing model plugins");
    private final Components batchjoblists = new Components("Batch definitions");
    private Collection tableListenerCollection;

}