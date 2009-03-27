// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LocalComponent.java

package org.seamcat.presentation.components;

import java.util.Enumeration;
import javax.swing.event.TableModelListener;
import org.apache.log4j.Logger;
import org.seamcat.model.NodeAttribute;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.presentation.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LocalComponent extends SeamcatComponent
{

    public LocalComponent(SeamcatComponent refComponent)
    {
        this(refComponent, null);
    }

    public LocalComponent(SeamcatComponent refComponent, String name)
    {
        this.refComponent = refComponent;
        this.name = name;
    }

    public Enumeration children()
    {
        return refComponent.children();
    }

    public boolean getAllowsChildren()
    {
        return refComponent.getAllowsChildren();
    }

    public Node getChildAt(int childIndex)
    {
        return refComponent.getChildAt(childIndex);
    }

    public int getChildCount()
    {
        return refComponent.getChildCount();
    }

    public int getIndex(Node node)
    {
        return refComponent.getIndex(node);
    }

    public NodeAttribute[] getNodeAttributes()
    {
        return refComponent.getNodeAttributes();
    }

    public Node getParent()
    {
        return refComponent.getParent();
    }

    public boolean isLeaf()
    {
        return refComponent.isLeaf();
    }

    public int getColumnCount()
    {
        return refComponent.getColumnCount();
    }

    public int getRowCount()
    {
        return refComponent.getRowCount();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return refComponent.isCellEditable(rowIndex, columnIndex);
    }

    public Class getColumnClass(int columnIndex)
    {
        return refComponent.getColumnClass(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return refComponent.getValueAt(rowIndex, columnIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        refComponent.setValueAt(aValue, rowIndex, columnIndex);
    }

    public String getColumnName(int columnIndex)
    {
        return refComponent.getColumnName(columnIndex);
    }

    public void addTableModelListener(TableModelListener l)
    {
        refComponent.addTableModelListener(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        refComponent.removeTableModelListener(l);
    }

    protected void initNodeAttributes()
    {
        throw new IllegalStateException("initNodeAttributes() should not be called on LocalComponent");
    }

    public Element toElement(Document doc)
    {
        throw new UnsupportedOperationException("toElement(Document) should not be called on this class");
    }

    public String toString()
    {
        String name;
        if(this.name != null)
            name = this.name;
        else
            name = refComponent.toString();
        return name;
    }

    public void setRefComponent(SeamcatComponent refComponent)
    {
        this.refComponent = refComponent;
    }

    public SeamcatComponent getRefComponent()
    {
        return refComponent;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/components/LocalComponent);
    private SeamcatComponent refComponent;
    private String name;

}
