// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Workspace.java

package org.seamcat.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.event.TreeModelEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.seamcat.batch.BatchParameter;
import org.seamcat.cdma.CDMAResults;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.interfaces.Reportable;
import org.seamcat.model.core.EventVector;
import org.seamcat.model.core.InterferenceLink;
import org.seamcat.model.core.Signals;
import org.seamcat.model.core.VictimSystemLink;
import org.seamcat.model.datatypes.DRSSVector;
import org.seamcat.model.datatypes.IRSSVector;
import org.seamcat.model.datatypes.IRSSVectorList;
import org.seamcat.model.engines.EGE;
import org.seamcat.model.engines.ICEConfiguration;
import org.seamcat.postprocessing.PostProcessingPluginWrapper;
import org.seamcat.presentation.MainWindow;
import org.seamcat.presentation.Node;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model:
//            SeamcatComponent, Components, Correlations, CoverageRadius, 
//            Scenario, Results, Control, Model, 
//            EventGenerationData, DistributionEvaluationData

public class Workspace extends SeamcatComponent
    implements Reportable
{

    public Workspace(Element element)
    {
        super(element.getAttribute("workspace_reference"), "");
        hasBeenCalculated = false;
        storeSignals = false;
        storeScenario = true;
        storeControl = true;
        storeResults = false;
        expandSignals = false;
        interferenceLinks = new Components("Interference Links");
        signals = new Signals();
        cdmaResults = new CDMAResults();
        correlations = new Correlations();
        radius = new CoverageRadius();
        iceConfigurations = new ArrayList();
        batchParameters = new ArrayList();
        scenario = new Scenario(this);
        results = new Results(this);
        ege = new EGE(this);
        egeThread = null;
        postProcessingPlugins = new ArrayList();
        isSaved = false;
        file = null;
        hasBeenCalculated = Boolean.valueOf(element.getAttribute("hasBeenCalculated")).booleanValue();
        victimSystemLink = new VictimSystemLink((Element)element.getElementsByTagName("VictimSystemLink").item(0));
        NodeList nlInterferenceLinks = element.getElementsByTagName("InterferenceLink");
        int x = 0;
        for(int size = nlInterferenceLinks.getLength(); x < size; x++)
        {
            InterferenceLink interferenceLink = new InterferenceLink((Element)nlInterferenceLinks.item(x), this);
            addInterferingSystemLink(interferenceLink);
        }

        NodeList nlIceConfigurations = element.getElementsByTagName("ICEConfiguration");
        int x = 0;
        for(int size = nlIceConfigurations.getLength(); x < size; x++)
            try
            {
                iceConfigurations.add(new ICEConfiguration((Element)nlIceConfigurations.item(x)));
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(MainWindow.getInstance(), (new StringBuilder()).append("Ignoring ICE Results!\nCause: ").append(e.getMessage()).toString(), "Ignoring stored results", 2);
            }

        try
        {
            cdmaResults = new CDMAResults((Element)element.getElementsByTagName("CDMAResults").item(0));
        }
        catch(RuntimeException ex) { }
        NodeList nl = element.getElementsByTagName("Signals");
        if(nl != null && nl.getLength() > 0)
        {
            signals = new Signals((Element)nl.item(0));
            ege.setTotalNumberOfEventsGenerated(signals.getDRSSVector().getEventVector().size());
        }
        nl = element.getElementsByTagName("Correlations");
        if(nl != null && nl.getLength() > 0)
            correlations = new Correlations((Element)nl.item(0));
        nl = element.getElementsByTagName("CoverageRadiuses");
        if(nl != null && nl.getLength() > 0)
            radius = new CoverageRadius((Element)nl.item(0));
        control = new Control(this, (Element)element.getElementsByTagName("Control").item(0));
        NodeList plugi = element.getElementsByTagName("postprocessing-plugins");
        try
        {
            if(plugi.getLength() == 0)
                nl = element.getElementsByTagName("PostProcessingPlugin");
            else
                nl = ((Element)plugi.item(0)).getElementsByTagName("PostProcessingPlugin");
            int i = 0;
            for(int size = nl.getLength(); i < size; i++)
                try
                {
                    PostProcessingPluginWrapper plugin = new PostProcessingPluginWrapper((Element)nl.item(i));
                    postProcessingPlugins.add(plugin);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    LOG.error((new StringBuilder()).append("Could not load post processing plugin <").append(e.getMessage()).append(">").toString());
                }

        }
        catch(Exception e)
        {
            LOG.debug("Error reading plugins from workspace", e);
        }
        consolidateSignals();
    }

    private void consolidateSignals()
    {
        if(signals.getIRSSVectorListUnwanted().getIRSSVectors().size() != getInterferenceLinks().size() + 1 && !victimSystemLink.isCDMASystem())
        {
            LOG.debug((new StringBuilder()).append("iRSSUnwanted not in sync (was ").append(signals.getIRSSVectorListUnwanted().getIRSSVectors().size()).append(" instead of ").append(getInterferenceLinks().size() + 1).append(")").toString());
            List vectors = signals.getIRSSVectorListUnwanted().getIRSSVectors();
            vectors.clear();
            vectors.add(new IRSSVector("Unwanted Summation Vector"));
            InterferenceLink link;
            for(Iterator i$ = getInterferenceLinks().iterator(); i$.hasNext(); vectors.add(new IRSSVector((new StringBuilder()).append("iRSS Unwanted - ").append(link.getReference()).toString())))
                link = (InterferenceLink)i$.next();

        }
        if(signals.getIRSSVectorListBlocking().getIRSSVectors().size() != getInterferenceLinks().size() + 1 && !victimSystemLink.isCDMASystem())
        {
            LOG.debug((new StringBuilder()).append("iRSSBlocking not in sync (was ").append(signals.getIRSSVectorListBlocking().getIRSSVectors().size()).append(" instead of ").append(getInterferenceLinks().size() + 1).append(")").toString());
            List vectors = signals.getIRSSVectorListBlocking().getIRSSVectors();
            vectors.clear();
            vectors.add(new IRSSVector("Blocking Summation Vector"));
            InterferenceLink link;
            for(Iterator i$ = getInterferenceLinks().iterator(); i$.hasNext(); vectors.add(new IRSSVector((new StringBuilder()).append("iRSS Blocking - ").append(link.getReference()).toString())))
                link = (InterferenceLink)i$.next();

        }
        if(signals.getIRSSVectorListIntermodulation().getIRSSVectors().size() != getInterferenceLinks().size() * (getInterferenceLinks().size() - 1) + 1)
        {
            LOG.debug((new StringBuilder()).append("iRSSIntermodulation not in sync (was ").append(signals.getIRSSVectorListIntermodulation().getIRSSVectors().size()).append(" instead of ").append(getInterferenceLinks().size() * (getInterferenceLinks().size() - 1) + 1).append(")").toString());
            List vectors = signals.getIRSSVectorListIntermodulation().getIRSSVectors();
            vectors.clear();
            if(interferenceLinks.size() < 2)
            {
                vectors.add(new IRSSVector("Intermodulation Summation Vector", true));
            } else
            {
                vectors.add(new IRSSVector("Intermodulation Summation Vector"));
                for(int j = 0; j < interferenceLinks.size(); j++)
                {
                    for(int i = 0; i < interferenceLinks.size(); i++)
                        if(i != j)
                            signals.getIRSSVectorListIntermodulation().addIRSSVector(new IRSSVector((new StringBuilder()).append("iRSS Intermodulation ").append(j + 1).append(" / ").append(i + 1).toString(), true));

                }

            }
        }
    }

    public Scenario getScenario()
    {
        return scenario;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("Workspace");
        element.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        element.setAttribute("xsi:noNamespaceSchemaLocation", Model.getWorkspaceSchemaUrl());
        element.setAttribute("seamcat_version", STRINGLIST.getString("APPLICATION_TITLE"));
        element.setAttribute("workspace_reference", getReference());
        element.setAttribute("hasBeenCalculated", Boolean.toString(hasBeenCalculated));
        if(storeScenario)
        {
            InterferenceLink il;
            for(Iterator i$ = interferenceLinks.iterator(); i$.hasNext(); element.appendChild(il.toElement(doc)))
                il = (InterferenceLink)i$.next();

        }
        if(storeResults)
        {
            Element iceConfigurationsElement = doc.createElement("iceConfigurations");
            ICEConfiguration ice;
            for(Iterator i$ = iceConfigurations.iterator(); i$.hasNext(); iceConfigurationsElement.appendChild(ice.toElement(doc)))
                ice = (ICEConfiguration)i$.next();

            element.appendChild(iceConfigurationsElement);
            if(victimSystemLink.isCDMASystem())
                element.appendChild(cdmaResults.toElement(doc));
        }
        if(storeSignals)
        {
            element.appendChild(signals.toElement(doc, expandSignals));
            element.appendChild(correlations.toElement(doc));
            element.appendChild(radius.toElement(doc));
        }
        if(storeScenario)
            element.appendChild(victimSystemLink.toElement(doc));
        if(storeControl)
            element.appendChild(control.toElement(doc));
        Element postPluginsElement = doc.createElement("postprocessing-plugins");
        org.seamcat.model.plugin.PostProcessingPlugin plugin;
        for(Iterator i$ = postProcessingPlugins.iterator(); i$.hasNext(); postPluginsElement.appendChild(((PostProcessingPluginWrapper)plugin).toElement(doc)))
            plugin = (PostProcessingPluginWrapper)i$.next();

        element.appendChild(postPluginsElement);
        return element;
    }

    public Workspace()
    {
        super((new StringBuilder()).append("Untitled workspace - ").append(++untitledCount).toString(), "");
        hasBeenCalculated = false;
        storeSignals = false;
        storeScenario = true;
        storeControl = true;
        storeResults = false;
        expandSignals = false;
        interferenceLinks = new Components("Interference Links");
        signals = new Signals();
        cdmaResults = new CDMAResults();
        correlations = new Correlations();
        radius = new CoverageRadius();
        iceConfigurations = new ArrayList();
        batchParameters = new ArrayList();
        scenario = new Scenario(this);
        results = new Results(this);
        ege = new EGE(this);
        egeThread = null;
        postProcessingPlugins = new ArrayList();
        isSaved = false;
        file = null;
        victimSystemLink = new VictimSystemLink();
        addInterferingSystemLink(new InterferenceLink(STRINGLIST.getString("WORKSPACE_INTERFERENCELINK_POSTFIX"), victimSystemLink));
        control = new Control(this);
    }

    public boolean hasCDMASubSystem()
    {
        boolean value = getVictimSystemLink().isCDMASystem();
        if(interferenceLinks != null)
        {
            for(Iterator i = interferenceLinks.iterator(); i.hasNext() && !value; value = value || ((InterferenceLink)i.next()).isCDMASystem());
        }
        return value;
    }

    public List getBatchParameters()
    {
        return batchParameters;
    }

    public void addBatchParameter(BatchParameter b)
    {
        batchParameters.add(b);
    }

    public List getPostProcessingPlugins()
    {
        return postProcessingPlugins;
    }

    public void addPostProcessingPlugin(PostProcessingPluginWrapper p)
    {
        postProcessingPlugins.add(p);
    }

    public boolean hasPostProcessingPlugins()
    {
        return postProcessingPlugins.size() > 0;
    }

    public void resetEventGeneration()
    {
        hasBeenCalculated = false;
        storeSignals = false;
        storeResults = false;
        radius.reset();
        correlations.reset();
        signals.resetAllGeneratedSignales();
        cdmaResults.resetAllGeneratedResults();
        iceConfigurations.clear();
        addIceConfiguration(new ICEConfiguration());
        if(hasCDMASubSystem())
        {
            if(victimSystemLink.isCDMASystem())
                victimSystemLink.getCDMASystem().setCapacitySimulated(false);
            Iterator i$ = getInterferenceLinks().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                InterferenceLink link = (InterferenceLink)i$.next();
                if(link.isCDMASystem())
                    link.getCDMASystem().setCapacitySimulated(false);
            } while(true);
        }
        getEge().reset();
    }

    public void setReference(String ref)
    {
        super.setReference(ref);
    }

    public String getReport(String reportStyleUrl)
    {
        String report;
        try
        {
            DocumentBuilder db = Model.getWorkspaceDocumentBuilderFactory().newDocumentBuilder();
            Document doc = db.newDocument();
            doc.appendChild(toElement(doc));
            DOMSource source = new DOMSource(doc);
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(bout);
            Transformer transformer;
            if(reportStyleUrl != null && !reportStyleUrl.equals(""))
            {
                StreamSource style = new StreamSource(reportStyleUrl);
                transformer = Model.getTransformerFactory().newTransformer(style);
            } else
            {
                transformer = Model.getTransformerFactory().newTransformer();
            }
            transformer.setOutputProperty("indent", "yes");
            transformer.transform(source, result);
            report = bout.toString();
        }
        catch(Exception ex)
        {
            LOG.error("Error generating report", ex);
            report = "Error generating report";
        }
        return report;
    }

    public Object getValue(int rowIndex)
    {
        return "";
    }

    public Object getUnitValue(int rowIndex)
    {
        return "";
    }

    public int getRowCount()
    {
        return 0;
    }

    public boolean isLeaf()
    {
        return false;
    }

    public final boolean getIsSaved()
    {
        return isSaved;
    }

    public final File getFile()
    {
        return file;
    }

    public void addInterferingSystemLink(InterferenceLink _int)
    {
        addInterferingSystemLink(_int, true);
    }

    public void addInterferingSystemLink(InterferenceLink _int, boolean generateIntermodulation)
    {
        interferenceLinks.add(_int);
        IRSSVector blo = new IRSSVector((new StringBuilder()).append(_int.getReference()).append(" Blocking").toString());
        IRSSVector unw = new IRSSVector((new StringBuilder()).append(_int.getReference()).append(" Unwanted Emission").toString());
        signals.getIRSSVectorListBlocking().addIRSSVector(blo);
        signals.getIRSSVectorListUnwanted().addIRSSVector(unw);
        _int.setBlockingVector(blo);
        _int.setUnwantedVector(unw);
        for(int i = 0; i < interferenceLinks.size() - 1; i++)
        {
            signals.getIRSSVectorListIntermodulation().addIRSSVector(new IRSSVector((new StringBuilder()).append("iRSS Intermodulation ").append(i + 1).append(" / ").append(interferenceLinks.size()).toString(), generateIntermodulation), i * (interferenceLinks.size() - 1) + (interferenceLinks.size() - 1));
            signals.getIRSSVectorListIntermodulation().addIRSSVector(new IRSSVector((new StringBuilder()).append("iRSS Intermodulation ").append(interferenceLinks.size()).append(" / ").append(i + 1).toString(), generateIntermodulation));
        }

    }

    public void removeInterferingSystemLink(InterferenceLink _int)
    {
        int index = interferenceLinks.indexOf(_int);
        interferenceLinks.remove(_int);
        signals.getIRSSVectorListBlocking().remove(index + 1);
        signals.getIRSSVectorListUnwanted().remove(index + 1);
    }

    final void setParent(Model parent)
    {
        this.parent = parent;
    }

    public void close()
    {
        signals = null;
        scenario = null;
        interferenceLinks = null;
        ege = null;
    }

    public final void startEventGeneration()
    {
        hasBeenCalculated = true;
        try
        {
            if(parent == null)
                parent = Model.getInstance();
            TreeModelEvent tme = new TreeModelEvent(this, new Object[] {
                parent
            }, new int[] {
                parent.getIndex(this)
            }, new Object[] {
                this
            });
            parent.fireTreeNodesChanged(tme);
        }
        catch(Exception e)
        {
            LOG.error("Error starting EGE thread", e);
        }
        if(victimSystemLink.isCDMASystem() && !signals.isVictimIsCdma())
            signals.adjustForCDMAVictim();
        egeThread = new Thread(ege, (new StringBuilder()).append("EGE - workspace: ").append(toString()).toString());
        ege.setNumberOfEvents(control.getEgData().getNumberOfEvents());
        ege.setDebugMode(control.getEgData().isInDebugMode());
        ege.setTimeLimited(control.getEgData().isTimeLimited());
        ege.setTimeLimit(control.getEgData().getTimeLeft());
        ege.setUseTestCDMAAlgorithm(control.getEgData().getUseTestCDMAAlgoritm());
        egeThread.start();
    }

    public void joinEge()
        throws InterruptedException
    {
        egeThread.join();
    }

    public boolean isEgeRunning()
    {
        try
        {
            return egeThread.isAlive();
        }
        catch(Exception e)
        {
            return false;
        }
    }

    public final void stopEventGeneration()
    {
        if(egeThread != null && egeThread.isAlive())
            ege.stop();
    }

    public boolean isHasBeenCalculated()
    {
        return hasBeenCalculated;
    }

    public final boolean isEventGenerationRunning()
    {
        return egeThread != null && egeThread.isAlive();
    }

    public EGE getEge()
    {
        return ege;
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
            childAt = scenario;
        else
        if(childIndex == 1)
            childAt = control;
        else
        if(childIndex == 2)
            childAt = results;
        return childAt;
    }

    public int getChildCount()
    {
        return !hasBeenCalculated ? 2 : 3;
    }

    public int getIndex(Node node)
    {
        int index = -1;
        if(node == scenario)
            index = 0;
        else
        if(node == control)
            index = 1;
        else
        if(node == results)
            index = 2;
        return index;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object value = null;
        switch(columnIndex)
        {
        case 0: // '\0'
            switch(rowIndex)
            {
            case 0: // '\0'
                value = scenario;
                break;

            case 1: // '\001'
                value = control;
                break;

            case 2: // '\002'
                value = results;
                break;
            }
            break;

        case 3: // '\003'
            value = "Folder";
            break;
        }
        return value == null ? "" : value;
    }

    public Signals getSignals()
    {
        return signals;
    }

    public void setSignals(Signals signals)
    {
        this.signals = signals;
    }

    public VictimSystemLink getVictimSystemLink()
    {
        return victimSystemLink;
    }

    public void setVictimSystemLink(VictimSystemLink victimSystemLink)
    {
        this.victimSystemLink = victimSystemLink;
    }

    public Components getInterferenceLinks()
    {
        return interferenceLinks;
    }

    public boolean testForCorrelation()
    {
        boolean result = false;
        if(EventVector.correlation(signals.getDRSSVector().getEventVector(), ((IRSSVector)signals.getIRSSVectorListBlocking().getIRSSVectors().get(0)).getEventVector()) > getControl().getDeData().getCorrelationThreshold())
            result = true;
        if(EventVector.correlation(signals.getDRSSVector().getEventVector(), ((IRSSVector)signals.getIRSSVectorListUnwanted().getIRSSVectors().get(0)).getEventVector()) > getControl().getDeData().getCorrelationThreshold())
            result = true;
        if(EventVector.correlation(signals.getDRSSVector().getEventVector(), ((IRSSVector)signals.getIRSSVectorListIntermodulation().getIRSSVectors().get(0)).getEventVector()) > getControl().getDeData().getCorrelationThreshold())
            result = true;
        return result;
    }

    public void setInterferenceLinks(Vector interferenceLinks)
    {
        interferenceLinks.clear();
        interferenceLinks.addAll(interferenceLinks);
    }

    public void addIceConfiguration(ICEConfiguration iceconf)
    {
        iceconf.setAllowIntermodulation(getSignals().getIRSSVectorListIntermodulation().getIRSSVectors().size() > 1);
        iceConfigurations.add(iceconf);
    }

    public void removeIceConfiguration(ICEConfiguration iceconf)
    {
        iceConfigurations.remove(iceconf);
    }

    public int getIceConfigurationCount()
    {
        return iceConfigurations.size();
    }

    public ICEConfiguration getIceConfiguration(int i)
    {
        return (ICEConfiguration)iceConfigurations.get(i);
    }

    protected void initNodeAttributes()
    {
    }

    public Control getControl()
    {
        return control;
    }

    public Results getResults()
    {
        return results;
    }

    public Correlations getCorrelations()
    {
        return correlations;
    }

    public CoverageRadius getRadius()
    {
        return radius;
    }

    public CDMAResults getCdmaResults()
    {
        return cdmaResults;
    }

    public void setIsSaved(boolean b)
    {
        isSaved = b;
    }

    public void setStoreSignals(boolean storeSignals)
    {
        this.storeSignals = storeSignals;
    }

    public void setStoreScenario(boolean storeScenario)
    {
        this.storeScenario = storeScenario;
    }

    public void setStoreControl(boolean storeControl)
    {
        this.storeControl = storeControl;
    }

    public void setStoreResults(boolean storeResults)
    {
        this.storeResults = storeResults;
    }

    public void setExpandSignals(boolean expandSignals)
    {
        this.expandSignals = expandSignals;
    }

    public String getStyleSheet(String contentType)
    {
        if(contentType.equalsIgnoreCase("text/html"))
            return Model.getReportHTMLStyleUrl();
        if(contentType.equalsIgnoreCase("application/x-csv"))
            return Model.getReportCSVStyleUrl();
        else
            return "";
    }

    public String getAbsoluteLocation()
    {
        return absoluteLocation;
    }

    public void setAbsoluteLocation(String absoluteLocation)
    {
        this.absoluteLocation = absoluteLocation;
    }

    public void setPostProcessingPlugins(List postProcessingPlugins)
    {
        this.postProcessingPlugins = postProcessingPlugins;
    }

    private static final ResourceBundle STRINGLIST;
    private static final Logger LOG = Logger.getLogger(org/seamcat/model/Workspace);
    public static final String CONSISTENCY_OK = "OK";
    public static int untitledCount = 1;
    private Model parent;
    private boolean hasBeenCalculated;
    private boolean storeSignals;
    private boolean storeScenario;
    private boolean storeControl;
    private boolean storeResults;
    private boolean expandSignals;
    private Components interferenceLinks;
    private Signals signals;
    private CDMAResults cdmaResults;
    private Correlations correlations;
    private CoverageRadius radius;
    private VictimSystemLink victimSystemLink;
    private Control control;
    private List iceConfigurations;
    private List batchParameters;
    private Scenario scenario;
    private Results results;
    private EGE ege;
    private Thread egeThread;
    private String absoluteLocation;
    private List postProcessingPlugins;
    private boolean isSaved;
    private File file;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}