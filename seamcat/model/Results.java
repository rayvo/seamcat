// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Results.java

package org.seamcat.model;

import java.io.File;
import java.util.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.model.core.VictimSystemLink;
import org.seamcat.presentation.Node;

// Referenced classes of package org.seamcat.model:
//            NodeAttribute, Workspace, ResultsListener

public final class Results
    implements Node, TableModel
{

    Results(Workspace parent)
    {
        this.parent = parent;
    }

    public final synchronized Collection getTransceiverLocationSamples()
    {
        return null;
    }

    public final synchronized void add(Collection collection)
    {
    }

    public final synchronized void add(Results results1)
    {
    }

    public final synchronized void flush()
    {
    }

    public final void addResultsListener(ResultsListener resultsListener)
    {
        listenerCollection.add(resultsListener);
    }

    public final synchronized File getResultsXMLFile()
    {
        return null;
    }

    public final String toString()
    {
        return "Results";
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
        Node node;
        switch(childIndex)
        {
        case 0: // '\0'
            node = parent.getRadius();
            break;

        case 1: // '\001'
            node = parent.getCorrelations();
            break;

        case 2: // '\002'
            node = parent.getSignals();
            break;

        case 3: // '\003'
            node = parent.getCdmaResults();
            break;

        default:
            node = null;
            break;
        }
        return node;
    }

    public int getChildCount()
    {
        return 3 + (parent.getVictimSystemLink().isCDMASystem() ? 1 : 0);
    }

    public int getIndex(Node node)
    {
        return -1;
    }

    public Node getParent()
    {
        return parent;
    }

    public boolean isLeaf()
    {
        return false;
    }

    public NodeAttribute[] getNodeAttributes()
    {
        return (NodeAttribute[])nodeAttributes.clone();
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
        String columnName;
        switch(columnIndex)
        {
        case 0: // '\0'
            columnName = "Name";
            break;

        case 1: // '\001'
            columnName = "Value";
            break;

        case 2: // '\002'
            columnName = "Unit";
            break;

        case 3: // '\003'
            columnName = "Type";
            break;

        default:
            columnName = "";
            break;
        }
        return columnName;
    }

    public int getRowCount()
    {
        return 0;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return null;
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

    private final Workspace parent;
    private final Collection tableListenerCollection = new ArrayList();
    private final Collection listenerCollection = new ArrayList();
    private final NodeAttribute nodeAttributes[] = new NodeAttribute[0];
}