// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DistributionEvaluationData.java

package org.seamcat.model;

import java.util.*;
import javax.swing.InputVerifier;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.presentation.Node;
import org.seamcat.presentation.SeamcatTextFieldFormats;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model:
//            NodeAttribute

public class DistributionEvaluationData
    implements Node, TableModel
{

    public DistributionEvaluationData(Node parent)
    {
        tableListenerCollection = new ArrayList();
        this.parent = parent;
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute(STRINGLIST.getString("EGE_CONTROL_SIGN_STABILITY"), "", new Double(0.80000000000000004D), "Float", null, false, true, VERIFIER_SIGN_STABILITY));
        nodeList.add(new NodeAttribute(STRINGLIST.getString("EGE_CONTROL_ADDITIONAL_NB_EVENTS"), "", new Integer(2000), "Integer", null, false, true, VERIFIER_ADDITIONAL_NB_EVENTS));
        nodeList.add(new NodeAttribute(STRINGLIST.getString("EGE_CONTROL_SIGN_DISTRIBUTION"), "", new Double(0.20000000000000001D), "Float", null, false, true, VERIFIER_SIGN_DISTRIBUTION));
        nodeList.add(new NodeAttribute(STRINGLIST.getString("EGE_CONTROL_CORRELATION_THRESHOLD"), "", new Double(0.80000000000000004D), "Float", null, false, true, VERIFIER_CORRELATION_THRESHOLD));
        nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    public DistributionEvaluationData(Node parent, Element element)
    {
        this(parent);
        setAdditionalNbEvents(Integer.parseInt(element.getAttribute("additionalNbEvents")));
        setSignDistribution(Double.parseDouble(element.getAttribute("signDistribution")));
        setSignStability(Double.parseDouble(element.getAttribute("signStability")));
        setCorrelationThreshold(Double.parseDouble(element.getAttribute("correlationThreshold")));
    }

    public final double getCorrelationThreshold()
    {
        return ((Number)nodeAttributes[3].getValue()).doubleValue();
    }

    public final void setCorrelationThreshold(double signDistribution)
    {
        nodeAttributes[3].setValue(new Double(signDistribution));
        fireTableChanged(new TableModelEvent(this, 3));
    }

    public final double getSignDistribution()
    {
        return ((Number)nodeAttributes[2].getValue()).doubleValue();
    }

    public final void setSignDistribution(double signDistribution)
    {
        nodeAttributes[2].setValue(new Double(signDistribution));
        fireTableChanged(new TableModelEvent(this, 2));
    }

    public final int getAdditionalNbEvents()
    {
        return ((Number)nodeAttributes[1].getValue()).intValue();
    }

    public final void setAdditionalNbEvents(int additionalNbEvents)
    {
        nodeAttributes[1].setValue(new Integer(additionalNbEvents));
        fireTableChanged(new TableModelEvent(this, 1));
    }

    public final double getSignStability()
    {
        return ((Number)nodeAttributes[0].getValue()).doubleValue();
    }

    public final void setSignStability(double signStability)
    {
        nodeAttributes[0].setValue(new Double(signStability));
        fireTableChanged(new TableModelEvent(this, 0));
    }

    private final void fireTableChanged(TableModelEvent event)
    {
        TableModelListener listener;
        for(Iterator i = tableListenerCollection.iterator(); i.hasNext(); listener.tableChanged(event))
            listener = (TableModelListener)i.next();

    }

    public final String toString()
    {
        return STRINGLIST.getString("NODE_TEXT_DISTRIBUTION_EVALUATION");
    }

    public final Enumeration children()
    {
        return null;
    }

    public final boolean getAllowsChildren()
    {
        return true;
    }

    public final Node getChildAt(int childIndex)
    {
        return null;
    }

    public final int getChildCount()
    {
        return 0;
    }

    public final int getIndex(Node node)
    {
        return -1;
    }

    public final boolean isLeaf()
    {
        return true;
    }

    public final NodeAttribute[] getNodeAttributes()
    {
        return (NodeAttribute[])nodeAttributes.clone();
    }

    public final void addTableModelListener(TableModelListener l)
    {
        tableListenerCollection.add(l);
    }

    public final Class getColumnClass(int columnIndex)
    {
        Class columnClass = java/lang/String;
        return columnClass;
    }

    public final int getColumnCount()
    {
        return 4;
    }

    public final String getColumnName(int columnIndex)
    {
        String columnName;
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
        else
            throw new IllegalArgumentException("Illegal column index requested");
        return columnName;
    }

    public final int getRowCount()
    {
        return nodeAttributes.length;
    }

    public final Object getValueAt(int rowIndex, int columnIndex)
    {
        Object valueAt;
        if(columnIndex == 0)
            valueAt = nodeAttributes[rowIndex].getName();
        else
        if(columnIndex == 1)
            valueAt = nodeAttributes[rowIndex].getValue();
        else
        if(columnIndex == 2)
            valueAt = nodeAttributes[rowIndex].getUnit();
        else
        if(columnIndex == 3)
            valueAt = nodeAttributes[rowIndex].getType();
        else
            throw new IllegalArgumentException("Illegal row index requested");
        return valueAt;
    }

    public final boolean isCellEditable(int rowIndex, int columnIndex)
    {
        boolean cellEditable = false;
        if(columnIndex == 1 && nodeAttributes[rowIndex].isEnabled())
            cellEditable = true;
        return cellEditable;
    }

    public final void removeTableModelListener(TableModelListener l)
    {
        tableListenerCollection.remove(l);
    }

    public final void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if(columnIndex == 1)
            if(nodeAttributes[rowIndex].getType().equalsIgnoreCase("FLOAT"))
                nodeAttributes[rowIndex].setValue(new Double((String)aValue));
            else
            if(nodeAttributes[rowIndex].getType().equalsIgnoreCase("INTEGER"))
                nodeAttributes[rowIndex].setValue(new Integer((String)aValue));
            else
                nodeAttributes[rowIndex].setValue(aValue);
    }

    public final Node getParent()
    {
        return parent;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("DistributionEvaluationData");
        element.setAttribute("additionalNbEvents", Integer.toString(getAdditionalNbEvents()));
        element.setAttribute("signDistribution", Double.toString(getSignDistribution()));
        element.setAttribute("signStability", Double.toString(getSignStability()));
        element.setAttribute("correlationThreshold", Double.toString(getCorrelationThreshold()));
        return element;
    }

    private static final ResourceBundle STRINGLIST;
    private static final InputVerifier VERIFIER_SIGN_STABILITY;
    private static final InputVerifier VERIFIER_ADDITIONAL_NB_EVENTS;
    private static final InputVerifier VERIFIER_SIGN_DISTRIBUTION;
    private static final InputVerifier VERIFIER_CORRELATION_THRESHOLD;
    private static final String COL1_NAME = "Name";
    private static final String COL2_NAME = "Value";
    private static final String COL3_NAME = "Unit";
    private static final String COL4_NAME = "Type";
    private final List tableListenerCollection;
    private final NodeAttribute nodeAttributes[];
    private final Node parent;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        VERIFIER_SIGN_STABILITY = SeamcatTextFieldFormats.DOUBLE_JTEXTFIELD_VERIFIER;
        VERIFIER_ADDITIONAL_NB_EVENTS = SeamcatTextFieldFormats.INTEGER_JTEXTFIELD_VERIFIER;
        VERIFIER_SIGN_DISTRIBUTION = SeamcatTextFieldFormats.DOUBLE_JTEXTFIELD_VERIFIER;
        VERIFIER_CORRELATION_THRESHOLD = SeamcatTextFieldFormats.DOUBLE_JTEXTFIELD_VERIFIER;
    }
}