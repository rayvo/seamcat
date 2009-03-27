// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Control.java

package org.seamcat.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.presentation.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// Referenced classes of package org.seamcat.model:
//            EventGenerationData, DistributionEvaluationData, Workspace, NodeAttribute

public class Control
    implements Node, TableModel
{

    public Control(Workspace parent)
    {
        tableListenerCollection = new ArrayList();
        this.parent = parent;
        egData = new EventGenerationData(this);
        deData = new DistributionEvaluationData(this);
    }

    public Control(Workspace parent, Element element)
    {
        tableListenerCollection = new ArrayList();
        this.parent = parent;
        egData = new EventGenerationData(this, (Element)element.getElementsByTagName("EventGeneration").item(0).getFirstChild());
        deData = new DistributionEvaluationData(this, (Element)element.getElementsByTagName("DistributionEvaluationData").item(0).getFirstChild());
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("Control");
        Element eventGenerationElement = doc.createElement("EventGeneration");
        eventGenerationElement.appendChild(egData.toElement(doc));
        element.appendChild(eventGenerationElement);
        Element distributionEvaluationData = doc.createElement("DistributionEvaluationData");
        distributionEvaluationData.appendChild(deData.toElement(doc));
        element.appendChild(distributionEvaluationData);
        return element;
    }

    public NodeAttribute[] getNodeAttributes()
    {
        return nodeAttributes;
    }

    public EventGenerationData getEgData()
    {
        return egData;
    }

    public void setDeData(DistributionEvaluationData deData)
    {
        this.deData = deData;
    }

    public void setEgData(EventGenerationData egData)
    {
        this.egData = egData;
    }

    public DistributionEvaluationData getDeData()
    {
        return deData;
    }

    public String toString()
    {
        return "Control";
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
        Node childAt;
        switch(childIndex)
        {
        case 0: // '\0'
            childAt = egData;
            break;

        case 1: // '\001'
            childAt = deData;
            break;

        default:
            throw new IllegalArgumentException("Illegal child index requested");
        }
        return childAt;
    }

    public int getChildCount()
    {
        return 2;
    }

    public int getIndex(Node node)
    {
        int index = -1;
        if(node == egData)
            index = 0;
        else
        if(node == deData)
            index = 1;
        return index;
    }

    public Node getParent()
    {
        return parent;
    }

    public boolean isLeaf()
    {
        return false;
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
            throw new IllegalArgumentException("Illegal column index requested");
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

    private Workspace parent;
    private final Collection tableListenerCollection;
    private EventGenerationData egData;
    private DistributionEvaluationData deData;
    private NodeAttribute nodeAttributes[];
    private static final String COL1_NAME = "Name";
    private static final String COL2_NAME = "Value";
    private static final String COL3_NAME = "Unit";
    private static final String COL4_NAME = "Type";
}