// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchParameterListModel.java

package org.seamcat.presentation.batch;

import java.util.*;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.apache.log4j.Logger;
import org.seamcat.batch.BatchJob;
import org.seamcat.batch.BatchParameter;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.model.Workspace;
import org.seamcat.presentation.Node;

public class BatchParameterListModel
    implements ListModel
{

    public BatchParameterListModel()
    {
    }

    public BatchParameter getBatchParameterAt(int index)
    {
        checkParameterIndex(index);
        return (BatchParameter)parameters.get(index);
    }

    public void addExclude(BatchParameter b)
    {
        excludeSet.add(b);
        fireContentsChanged();
    }

    public void removeExclude(BatchParameter b)
    {
        excludeSet.remove(b);
        fireContentsChanged();
    }

    public boolean isExcluded(BatchParameter b)
    {
        return excludeSet.contains(b);
    }

    public boolean isExcluded(int index)
    {
        checkParameterIndex(index);
        return isExcluded((BatchParameter)parameters.get(index));
    }

    private void checkParameterIndex(int index)
    {
        if(index >= getSize())
            throw new ArrayIndexOutOfBoundsException((new StringBuilder()).append("BatchParameterIndex does not exist (size=").append(parameters.size()).append(")").toString());
        else
            return;
    }

    public void setBatchJob(BatchJob bj)
    {
        parameters.clear();
        excludeSet.clear();
        if(bj != null)
        {
            Workspace wks = bj.getWorkspace();
            if(wks != null)
            {
                Node scenario = wks.getScenario();
                parameters.addAll(initParameters(0, scenario, new StringBuffer()));
                excludeSet.addAll(bj.getParameters());
            }
        }
        fireContentsChanged();
    }

    private void fireContentsChanged()
    {
        ListDataEvent event = new ListDataEvent(this, 0, 0, Math.max(parameters.size() - 1, 0));
        ListDataListener l;
        for(Iterator i$ = listDataListeners.iterator(); i$.hasNext(); l.contentsChanged(event))
            l = (ListDataListener)i$.next();

    }

    private static List initParameters(int parameterIndex, Node node, StringBuffer path)
    {
        List parameters = new ArrayList();
        path.append(" / ").append(node.toString());
        if(node instanceof SeamcatComponent)
        {
            org.seamcat.model.NodeAttribute att[] = node.getNodeAttributes();
            for(int cnt = 0; cnt < att.length; cnt++)
                parameters.add(new BatchParameter((SeamcatComponent)node, cnt, parameterIndex++, path.toString()));

        }
        for(int x = 0; x < node.getChildCount(); x++)
        {
            List l = initParameters(parameterIndex, node.getChildAt(x), new StringBuffer(path));
            parameters.addAll(l);
            parameterIndex += l.size();
        }

        return parameters;
    }

    public int getSize()
    {
        return parameters.size();
    }

    public Object getElementAt(int index)
    {
        return getBatchParameterAt(index);
    }

    public void addListDataListener(ListDataListener l)
    {
        if(!listDataListeners.contains(l))
            listDataListeners.add(l);
    }

    public void clear()
    {
        parameters.clear();
    }

    public void removeListDataListener(ListDataListener l)
    {
        listDataListeners.remove(l);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/batch/BatchParameterListModel);
    private static final String PATH_DELIMITER = " / ";
    private final List parameters = new ArrayList();
    private final List listDataListeners = new ArrayList();
    private final Set excludeSet = new HashSet();

}
