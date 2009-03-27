// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BatchJob.java

package org.seamcat.batch;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.function.Point2D;
import org.seamcat.model.Workspace;
import org.seamcat.model.core.VictimSystemLink;
import org.seamcat.model.engines.EGE;
import org.seamcat.model.engines.EventCompletionListener;
import org.seamcat.model.engines.ICE;
import org.seamcat.model.engines.ICEConfiguration;
import org.seamcat.model.engines.InterferenceCalculationListener;
import org.seamcat.presentation.batch.BatchParameterListModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.batch:
//            BatchItemControl, BatchParameter, BatchJobListener

public class BatchJob
    implements Runnable, EventCompletionListener, InterferenceCalculationListener
{

    public BatchJob(Workspace workspace)
    {
        parameters = new ArrayList();
        control = new BatchItemControl();
        eventsGenerated = false;
        listeners = new ArrayList();
        actionListenerList = new ArrayList();
        inServerMode = false;
        setWorkspace(workspace);
    }

    public BatchJob(Element element)
    {
        parameters = new ArrayList();
        control = new BatchItemControl();
        eventsGenerated = false;
        listeners = new ArrayList();
        actionListenerList = new ArrayList();
        inServerMode = false;
        Element ws = (Element)element.getElementsByTagName("Workspace").item(0);
        setWorkspace(new Workspace(ws));
        NodeList bic = element.getElementsByTagName("batch-item-control");
        control = new BatchItemControl((Element)bic.item(0));
        BatchParameterListModel bcbm = new BatchParameterListModel();
        bcbm.setBatchJob(this);
        NodeList params = element.getElementsByTagName("batch-parameter");
        int index = 0;
        int i = 0;
        for(int size = params.getLength(); i < size; i++)
        {
            Element param = (Element)params.item(i);
            index = Integer.parseInt(param.getAttribute("index"));
            BatchParameter bp = (BatchParameter)bcbm.getElementAt(index);
            setNewParameter(param, bp);
            parameters.add(bp);
        }

    }

    public void addActionListener(ActionListener a)
    {
        actionListenerList.add(a);
    }

    public void removeActionListener(ActionListener a)
    {
        actionListenerList.remove(a);
    }

    public String generateReport()
    {
        StringBuffer str = new StringBuffer();
        str.append((new StringBuilder()).append("<WorkspaceName>").append(workspace.getReference()).append("</WorkspaceName>\n").toString());
        str.append((new StringBuilder()).append(getControl().toString()).append("\n").toString());
        return str.toString();
    }

    public void reset()
    {
        getControl().setProcessed(false);
        getControl().getIceConfiguration().setPropabilityResult(0.0D);
        getControl().getIceConfiguration().setHasBeenCalculated(false);
        workspace.resetEventGeneration();
    }

    private void setNewParameter(Element param, BatchParameter bp)
    {
        if(bp.getType().equals("Distribution"))
            bp.setNewValue(Distribution.create((Element)param.getFirstChild()));
        else
        if(bp.getType().equals("Function"))
        {
            if(bp.getOldValue() instanceof DiscreteFunction)
                bp.setNewValue(new DiscreteFunction((Element)param.getFirstChild()));
            else
            if(bp.getOldValue() instanceof DiscreteFunction2)
                bp.setNewValue(new DiscreteFunction2((Element)param.getFirstChild()));
        } else
        if(bp.getType().equals("Double"))
            bp.setNewValue(new Double(param.getAttribute("newValue")));
        else
        if(bp.getType().equals("Integer"))
            bp.setNewValue(new Integer(param.getAttribute("newValue")));
        else
        if(bp.getType().equals("String"))
            bp.setNewValue(param.getAttribute("newValue"));
        else
        if(bp.getType().equals("Boolean"))
            bp.setNewValue(new Boolean(param.getAttribute("newValue")));
    }

    public void addBatchJobListener(BatchJobListener bjl)
    {
        listeners.add(bjl);
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("batch-job");
        element.appendChild(control.toElement(doc));
        element.appendChild(workspace.toElement(doc));
        BatchParameter parameter;
        for(Iterator i$ = parameters.iterator(); i$.hasNext(); element.appendChild(parameter.toElement(doc)))
            parameter = (BatchParameter)i$.next();

        return element;
    }

    public void processBatchJob()
    {
        worker = new Thread(this);
        worker.start();
    }

    private void notifyListeners(int status)
    {
        int i = 0;
        for(int stop = listeners.size(); i < stop; i++)
            ((BatchJobListener)listeners.get(i)).eventOccurred(status);

    }

    public void run()
    {
        if(workspace == null || control == null)
            throw new IllegalStateException("BatchJob can not be started if workspace or control is null");
        notifyListeners(0);
        int i = 0;
        for(int stop = parameters.size(); i < stop; i++)
            ((BatchParameter)parameters.get(i)).assignValue();

        workspace.resetEventGeneration();
        workspace.getEge().addEventCompletionListerner(this);
        workspace.getEge().setInServerMode(isInServerMode());
        workspace.startEventGeneration();
        try
        {
            workspace.joinEge();
        }
        catch(InterruptedException ex)
        {
            LOG.error("Interrupted while waiting for EGE", ex);
        }
        if(!workspace.getVictimSystemLink().isCDMASystem())
        {
            ICE ice = new ICE(workspace);
            ice.addIceListener(this);
            ice.calculateInterference(control.getIceConfiguration());
            try
            {
                ice.getIceThread().join();
            }
            catch(InterruptedException ex1)
            {
                LOG.error("Interrupted while waiting for ICE", ex1);
            }
        }
        ice = 0;
        for(int stop = parameters.size(); ice < stop; ice++)
            ((BatchParameter)parameters.get(ice)).resetValue();

        getControl().setProcessed(true);
        notifyListeners(1);
    }

    public void addParameter(BatchParameter p)
    {
        if(!parameters.contains(p))
        {
            parameters.add(p);
            parametersChanged();
        }
    }

    private void parametersChanged()
    {
        ActionListener listener;
        for(Iterator i$ = actionListenerList.iterator(); i$.hasNext(); listener.actionPerformed(null))
            listener = (ActionListener)i$.next();

    }

    public void removeParameter(int index)
    {
        parameters.remove(index);
        parametersChanged();
    }

    public BatchParameter getParameter(int index)
    {
        return (BatchParameter)parameters.get(index);
    }

    public Workspace getWorkspace()
    {
        return workspace;
    }

    public BatchItemControl getControl()
    {
        return control;
    }

    private void setParameters(java.util.List parameters)
    {
        this.parameters.clear();
        this.parameters.addAll(parameters);
    }

    public void setWorkspace(Workspace workspace)
    {
        this.workspace = workspace;
    }

    public void setControl(BatchItemControl control)
    {
        this.control = control;
    }

    public java.util.List getParameters()
    {
        return Collections.unmodifiableList(parameters);
    }

    public int getParametersCount()
    {
        return parameters.size();
    }

    public void eventCompleted(int _eventsCompleted, int _totalNumberOfEvents)
    {
        eventsCompleted = _eventsCompleted;
        totalEvents = _totalNumberOfEvents;
    }

    public void eventGenerationCompleted(int count)
    {
        eventsCompleted = count;
    }

    public void generationAndEvaluationComplete()
    {
        eventsGenerated = true;
    }

    public void incrementCurrentProcessCompletionPercentage(int value)
    {
        currentProcessPercentage += value;
    }

    public void incrementTotalProcessCompletionPercentage(int value)
    {
        totalProcessPercentage += value;
    }

    public void notifyError(String error)
    {
        LOG.warn(error);
    }

    public void setCurrentProcessCompletionPercentage(int value)
    {
        currentProcessPercentage = value;
    }

    public void setTotalProcessCompletionPercentage(int value)
    {
        totalProcessPercentage = value;
    }

    public void startingEventGeneration(Workspace workspace, int eventsToBeCalculated, int eventStartIndex)
    {
        totalEvents = eventsToBeCalculated;
        eventsCompleted = eventStartIndex;
    }

    public void updateStatus(String status)
    {
        currentStatus = status;
    }

    public void addTranslationResult(Point2D point2d)
    {
    }

    public void calculationComplete()
    {
    }

    public void calculationStarted()
    {
    }

    public boolean confirmContinueOnWarning(String warning)
    {
        return true;
    }

    public void propabilityResult(double result)
    {
        getControl().getIceConfiguration().setPropabilityResult(result);
    }

    public void warningMessage(String warning)
    {
        LOG.warn(warning);
    }

    public void infoMessage(String message)
    {
        LOG.debug(message);
    }

    public boolean isInServerMode()
    {
        return inServerMode;
    }

    public void setInServerMode(boolean inServerMode)
    {
        this.inServerMode = inServerMode;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/batch/BatchJob);
    private static final ResourceBundle STRINGLIST;
    public static final int STARTING_JOB = 0;
    public static final int ENDING_JOB = 1;
    private Workspace workspace;
    private java.util.List parameters;
    private BatchItemControl control;
    private int eventsCompleted;
    private int totalEvents;
    private String currentStatus;
    private int currentProcessPercentage;
    private int totalProcessPercentage;
    private boolean eventsGenerated;
    private final java.util.List listeners;
    private final java.util.List actionListenerList;
    private Thread worker;
    private boolean inServerMode;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}