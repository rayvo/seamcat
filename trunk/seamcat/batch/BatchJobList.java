// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BatchJobList.java

package org.seamcat.batch;

import java.io.ByteArrayOutputStream;
import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.seamcat.cdma.CDMAResults;
import org.seamcat.interfaces.Reportable;
import org.seamcat.model.*;
import org.seamcat.model.core.VictimSystemLink;
import org.seamcat.model.engines.ICEConfiguration;
import org.seamcat.model.engines.InterferenceCriterionType;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.batch:
//            BatchJob, BatchJobListListener, BatchItemControl

public class BatchJobList extends SeamcatComponent
    implements TableModel, Runnable, Reportable
{

    public BatchJobList()
    {
        super("Batch", "");
        batchItems = new ArrayList();
        tablelisteners = new ArrayList();
        listeners = new ArrayList();
        inServerMode = false;
        currentIndex = 0;
    }

    public BatchJobList(Element element)
    {
        super(element.getAttribute("batch_reference"), "");
        batchItems = new ArrayList();
        tablelisteners = new ArrayList();
        listeners = new ArrayList();
        inServerMode = false;
        currentIndex = 0;
        NodeList batchjobs = element.getElementsByTagName("batch-job");
        int i = 0;
        for(int size = batchjobs.getLength(); i < size; i++)
        {
            BatchJob bj = new BatchJob((Element)batchjobs.item(i));
            addBatchJob(bj);
        }

    }

    public String getReport(String reportStyleUrl)
    {
        try
        {
            DocumentBuilder db = Model.getSeamcatDocumentBuilderFactory().newDocumentBuilder();
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
            return bout.toString();
        }
        catch(Exception ex)
        {
            LOG.error("Error generating report", ex);
        }
        return "Error generating report";
    }

    public void resetAll()
    {
        currentIndex = 0;
        BatchJob l;
        for(Iterator i$ = batchItems.iterator(); i$.hasNext(); l.reset())
            l = (BatchJob)i$.next();

    }

    public void addBatchJobListListener(BatchJobListListener bl)
    {
        listeners.add(bl);
    }

    public void removeBatchJobListListeners(BatchJobListListener bl)
    {
        listeners.remove(bl);
    }

    private void notifyListenersStart(int bj)
    {
        BatchJobListListener l;
        for(Iterator i$ = listeners.iterator(); i$.hasNext(); l.startingJob(bj))
            l = (BatchJobListListener)i$.next();

    }

    private void notifyListenersComplete()
    {
        BatchJobListListener l;
        for(Iterator i$ = listeners.iterator(); i$.hasNext(); l.entireBatchIsComplete())
            l = (BatchJobListListener)i$.next();

    }

    private void notifyListenersEnd(int bj)
    {
        BatchJobListListener l;
        for(Iterator i$ = listeners.iterator(); i$.hasNext(); l.endingJob(bj))
            l = (BatchJobListListener)i$.next();

    }

    protected void initNodeAttributes()
    {
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("BatchJobList");
        element.setAttribute("batch_reference", getReference());
        BatchJob bj;
        for(Iterator i$ = batchItems.iterator(); i$.hasNext(); element.appendChild(bj.toElement(doc)))
            bj = (BatchJob)i$.next();

        return element;
    }

    public void stopBatch()
    {
        stopped = true;
    }

    public void addBatchJob(BatchJob bj)
    {
        batchItems.add(bj);
        notifyTableListeners(new TableModelEvent(this));
    }

    public BatchJob getBatchJob(int index)
    {
        return index < 0 || index >= batchItems.size() ? null : (BatchJob)batchItems.get(index);
    }

    public List getBatchJobs()
    {
        return batchItems;
    }

    public void removeBatchJob(int index)
    {
        if(index >= 0)
        {
            batchItems.remove(index);
            notifyTableListeners(new TableModelEvent(this));
        }
    }

    public void run()
    {
        for(stopped = false; currentIndex < batchItems.size() && !stopped; currentIndex++)
        {
            LOG.debug((new StringBuilder()).append("Processing batch job #").append(currentIndex).toString());
            BatchJob currentJob = (BatchJob)batchItems.get(currentIndex);
            currentJob.setInServerMode(isInServerMode());
            notifyListenersStart(currentIndex);
            Thread worker = new Thread(currentJob, (new StringBuilder()).append("BatchJob #").append(currentIndex).toString());
            worker.start();
            try
            {
                worker.join();
            }
            catch(InterruptedException ex) { }
            notifyListenersEnd(currentIndex);
            notifyTableListeners(new TableModelEvent(this, currentIndex));
        }

        notifyListenersComplete();
    }

    public int getColumnCount()
    {
        return 8;
    }

    public int getRowCount()
    {
        return batchItems.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        BatchJob bj = (BatchJob)batchItems.get(rowIndex);
        boolean isEditable;
        switch(columnIndex)
        {
        case 0: // '\0'
        case 8: // '\b'
            isEditable = true;
            break;

        case 1: // '\001'
            isEditable = false;
            break;

        case 2: // '\002'
            isEditable = true;
            break;

        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 6: // '\006'
        case 7: // '\007'
            isEditable = !bj.getWorkspace().getVictimSystemLink().isCDMASystem();
            break;

        default:
            throw new IllegalStateException((new StringBuilder()).append("rowIndex ").append(rowIndex).append(" is unknown").toString());
        }
        return isEditable;
    }

    public Class getColumnClass(int columnIndex)
    {
        Class c;
        switch(columnIndex)
        {
        case 0: // '\0'
        case 1: // '\001'
        case 2: // '\002'
            c = java/lang/String;
            break;

        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
            c = java/lang/Boolean;
            break;

        case 6: // '\006'
        case 7: // '\007'
            c = java/lang/Integer;
            break;

        default:
            throw new IndexOutOfBoundsException((new StringBuilder()).append("Column index = ").append(columnIndex).append(" is invalid").toString());
        }
        return c;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        BatchJob bj = (BatchJob)batchItems.get(rowIndex);
        switch(columnIndex)
        {
        case 0: // '\0'
            return getBatchJob(rowIndex).getWorkspace();

        case 1: // '\001'
            if(!bj.getControl().isProcessed())
                return "Not yet processed";
            if(bj.getWorkspace().getVictimSystemLink().isCDMASystem())
                return (new StringBuilder()).append(bj.getWorkspace().getCdmaResults().getAverageLoss()).append(" %").toString();
            else
                return new Double(getBatchJob(rowIndex).getControl().getIceConfiguration().getPropabilityResult());

        case 2: // '\002'
            if(bj.getWorkspace().getVictimSystemLink().isCDMASystem())
                return "Not available for CDMA";
            else
                return InterferenceCriterionType.INTERFERENCE_CRITERION_TYPES[getBatchJob(rowIndex).getControl().getInterenceCriterionType() - 1];

        case 3: // '\003'
            if(bj.getWorkspace().getVictimSystemLink().isCDMASystem())
                return Boolean.valueOf(false);
            else
                return new Boolean(getBatchJob(rowIndex).getControl().getIceConfiguration().isUnwanted());

        case 4: // '\004'
            if(bj.getWorkspace().getVictimSystemLink().isCDMASystem())
                return Boolean.valueOf(false);
            else
                return new Boolean(getBatchJob(rowIndex).getControl().getIceConfiguration().isBlocking());

        case 5: // '\005'
            if(bj.getWorkspace().getVictimSystemLink().isCDMASystem())
                return Boolean.valueOf(false);
            else
                return new Boolean(getBatchJob(rowIndex).getControl().getIceConfiguration().isIntermodulation());

        case 6: // '\006'
            if(bj.getWorkspace().getVictimSystemLink().isCDMASystem())
                return Integer.valueOf(-1);
            else
                return new Integer(getBatchJob(rowIndex).getControl().getSamples());

        case 7: // '\007'
            return Integer.valueOf(bj.getWorkspace().getControl().getEgData().getNumberOfEvents());
        }
        throw new IndexOutOfBoundsException((new StringBuilder()).append("Column index = ").append(columnIndex).append(" is invalid").toString());
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        switch(columnIndex)
        {
        case 0: // '\0'
            getBatchJob(rowIndex).getWorkspace().setReference(aValue.toString());
            return;

        case 1: // '\001'
            return;

        case 2: // '\002'
            String values[] = InterferenceCriterionType.INTERFERENCE_CRITERION_TYPES;
            int i = 0;
            for(int stop = values.length; i < stop; i++)
                if(values[i].equalsIgnoreCase((String)aValue))
                {
                    getBatchJob(rowIndex).getControl().setInterferenceCriterion(i + 1);
                    return;
                }

            throw new IllegalArgumentException((new StringBuilder()).append(aValue.toString()).append(" does not appear to be a valid choice for Interference Criterion").toString());

        case 3: // '\003'
            getBatchJob(rowIndex).getControl().getIceConfiguration().setUnwanted(((Boolean)aValue).booleanValue());
            return;

        case 4: // '\004'
            getBatchJob(rowIndex).getControl().getIceConfiguration().setBlocking(((Boolean)aValue).booleanValue());
            return;

        case 5: // '\005'
            getBatchJob(rowIndex).getControl().getIceConfiguration().setIntermodulation(((Boolean)aValue).booleanValue());
            return;

        case 6: // '\006'
            getBatchJob(rowIndex).getControl().setSamples(((Number)aValue).intValue());
            return;

        case 7: // '\007'
            getBatchJob(rowIndex).getWorkspace().getControl().getEgData().setNumberOfEvents(((Number)aValue).intValue());
            return;
        }
        throw new IndexOutOfBoundsException((new StringBuilder()).append("Column index = ").append(columnIndex).append(" is invalid").toString());
    }

    public String getColumnName(int columnIndex)
    {
        return COLUMN_NAMES[columnIndex];
    }

    private void notifyTableListeners(TableModelEvent e)
    {
        TableModelListener l;
        for(Iterator i$ = tablelisteners.iterator(); i$.hasNext(); l.tableChanged(e))
            l = (TableModelListener)i$.next();

    }

    public void addTableModelListener(TableModelListener l)
    {
        tablelisteners.add(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        tablelisteners.remove(l);
    }

    public String getStyleSheet(String contentType)
    {
        String ss;
        if(contentType.equalsIgnoreCase("text/html"))
            ss = Model.getBatchHTMLStyleUrl();
        else
        if(contentType.equalsIgnoreCase("application/x-csv"))
            ss = Model.getBatchCSVStyleUrl();
        else
            ss = "";
        return ss;
    }

    public void setStoreSignals(boolean value)
    {
        BatchJob b;
        for(Iterator i$ = batchItems.iterator(); i$.hasNext(); b.getWorkspace().setStoreSignals(value))
            b = (BatchJob)i$.next();

    }

    public void setStoreControl(boolean value)
    {
        BatchJob b;
        for(Iterator i$ = batchItems.iterator(); i$.hasNext(); b.getWorkspace().setStoreControl(value))
            b = (BatchJob)i$.next();

    }

    public void setStoreScenario(boolean value)
    {
        BatchJob b;
        for(Iterator i$ = batchItems.iterator(); i$.hasNext(); b.getWorkspace().setStoreScenario(value))
            b = (BatchJob)i$.next();

    }

    public void setStoreResults(boolean value)
    {
        BatchJob b;
        for(Iterator i$ = batchItems.iterator(); i$.hasNext(); b.getWorkspace().setStoreResults(value))
            b = (BatchJob)i$.next();

    }

    public void setExpandSignals(boolean value)
    {
        BatchJob b;
        for(Iterator i$ = batchItems.iterator(); i$.hasNext(); b.getWorkspace().setExpandSignals(value))
            b = (BatchJob)i$.next();

    }

    public boolean isInServerMode()
    {
        return inServerMode;
    }

    public void setInServerMode(boolean inServerMode)
    {
        this.inServerMode = inServerMode;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/batch/BatchJobList);
    private static final ResourceBundle STRINGLIST;
    private boolean stopped;
    private static final String COLUMN_NAMES[];
    private final List batchItems;
    private final List tablelisteners;
    private final List listeners;
    private boolean inServerMode;
    private int currentIndex;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        COLUMN_NAMES = (new String[] {
            STRINGLIST.getString("BATCHDATA_WORKSPACE"), STRINGLIST.getString("BATCHDATA_PROBABILITY"), STRINGLIST.getString("BATCHDATA_CRITERION"), STRINGLIST.getString("BATCHDATA_UNWANTED"), STRINGLIST.getString("BATCHDATA_BLOCKING"), STRINGLIST.getString("BATCHDATA_INTERMOD"), STRINGLIST.getString("BATCHDATA_SAMPLES"), STRINGLIST.getString("BATCHDATA_NUMBEROFEVENTS")
        });
    }
}