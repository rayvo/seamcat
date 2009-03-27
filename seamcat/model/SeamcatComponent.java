// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatComponent.java

package org.seamcat.model;

import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.seamcat.interfaces.Describable;
import org.seamcat.interfaces.Referenceable;
import org.seamcat.presentation.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model:
//            NodeAttribute

public abstract class SeamcatComponent
    implements Referenceable, Describable, Node, TableModel
{

    public SeamcatComponent()
    {
        reference = "";
        description = "";
        nodeAttributeIsDirty = false;
        componentCollection = null;
        tableListenerCollection = new ArrayList();
        STD_TABLEMODEL_EVENT = new TableModelEvent(this);
    }

    public SeamcatComponent(String ref, String desc)
    {
        this();
        setReference(ref);
        setDescription(desc);
    }

    protected void setRowNames(String names[])
    {
        rowNames = names;
    }

    protected void setRowClasses(Object types[])
    {
        rowTypes = types;
    }

    final void setComponentCollection(Collection componentCollection)
    {
        this.componentCollection = componentCollection;
    }

    public final Collection getComponentCollection()
    {
        return componentCollection;
    }

    public String getReference()
    {
        return reference;
    }

    public String getDescription()
    {
        return description;
    }

    public void updateNodeAttributes()
    {
        nodeAttributeIsDirty = true;
        fireTableChangedListeners();
    }

    public void setReference(String reference)
    {
        if(reference != null)
            this.reference = reference;
        else
            this.reference = "";
    }

    public final void setDescription(String description)
    {
        if(description != null)
            this.description = description;
        else
            this.description = "";
    }

    public String toString()
    {
        return getReference();
    }

    public abstract Element toElement(Document document);

    public Enumeration children()
    {
        return null;
    }

    public boolean getAllowsChildren()
    {
        return false;
    }

    public Node getChildAt(int childIndex)
    {
        return null;
    }

    public int getChildCount()
    {
        return 0;
    }

    public int getIndex(Node node)
    {
        return 0;
    }

    public Node getParent()
    {
        return null;
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
        return java/lang/Object;
    }

    public int getColumnCount()
    {
        return 4;
    }

    public String getColumnName(int columnIndex)
    {
        if(columnIndex >= 0 && columnIndex < COLUMNS.length)
            return COLUMNS[columnIndex];
        else
            throw new IllegalArgumentException("Columnname out of range");
    }

    public int getRowCount()
    {
        ensureNodeAttributesLoaded();
        return nodeAttributes.length;
    }

    private void validateRowIndex(int rowIndex)
        throws IllegalArgumentException
    {
        if(rowIndex < 0 || rowIndex >= getRowCount())
            throw new IllegalArgumentException((new StringBuilder()).append("RowIndex is out of range <").append(rowIndex).append(">").toString());
        else
            return;
    }

    private void validateColumnIndex(int columnIndex)
        throws IllegalArgumentException
    {
        if(columnIndex < 0 || columnIndex >= COLUMNS.length)
            throw new IllegalArgumentException((new StringBuilder()).append("ColumnIndex is out of range <").append(columnIndex).append(">").toString());
        else
            return;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object valueAt;
        try
        {
            ensureNodeAttributesLoaded();
            validateRowIndex(rowIndex);
            switch(columnIndex)
            {
            case 0: // '\0'
                valueAt = nodeAttributes[rowIndex].getName();
                break;

            case 1: // '\001'
                valueAt = nodeAttributes[rowIndex].getValue();
                break;

            case 2: // '\002'
                valueAt = nodeAttributes[rowIndex].getUnit();
                break;

            case 3: // '\003'
                valueAt = nodeAttributes[rowIndex].getType();
                break;

            default:
                throw new IllegalArgumentException((new StringBuilder()).append("ColumnIndex out of range <").append(columnIndex).append(">").toString());
            }
        }
        catch(Exception e)
        {
            LOG.warn((new StringBuilder()).append("Error getting value for rowIndex ").append(rowIndex).append(" columnIndex=").append(columnIndex).toString(), e);
            valueAt = null;
        }
        return valueAt;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        validateRowIndex(rowIndex);
        validateColumnIndex(columnIndex);
        return columnIndex == 1 && nodeAttributes[rowIndex].isEnabled();
    }

    public void removeTableModelListener(TableModelListener l)
    {
        tableListenerCollection.remove(l);
    }

    protected void fireTableChangedListeners()
    {
        TableModelListener l;
        for(Iterator i$ = tableListenerCollection.iterator(); i$.hasNext(); l.tableChanged(STD_TABLEMODEL_EVENT))
            l = (TableModelListener)i$.next();

    }

    protected final Object setTreeNodeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        ensureNodeAttributesLoaded();
        validateRowIndex(rowIndex);
        validateColumnIndex(columnIndex);
        Object setValue;
        if(columnIndex == 1)
        {
            if(nodeAttributes[rowIndex].getType().equalsIgnoreCase("DOUBLE"))
                setValue = new Double(aValue.toString());
            else
            if(nodeAttributes[rowIndex].getType().equalsIgnoreCase("INTEGER"))
                setValue = new Integer(aValue.toString());
            else
                setValue = aValue;
            nodeAttributes[rowIndex].setValue(setValue);
        } else
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Only column 1 may be updated <").append(columnIndex).append(">").toString());
        }
        return setValue;
    }

    protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        setTreeNodeValueAt(aValue, rowIndex, columnIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        setNodeAttributeValueAt(aValue, rowIndex, columnIndex);
    }

    protected abstract void initNodeAttributes();

    protected void ensureNodeAttributesLoaded()
    {
        if(nodeAttributes == null || nodeAttributeIsDirty)
        {
            initNodeAttributes();
            if(nodeAttributes == null)
                throw new IllegalStateException("NodeAttributes did not load correctly");
            nodeAttributeIsDirty = false;
        }
    }

    public NodeAttribute[] getNodeAttributes()
    {
        ensureNodeAttributesLoaded();
        return nodeAttributes;
    }

    public void clearAllNodeAttributes()
    {
        nodeAttributes = null;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/SeamcatComponent);
    private String reference;
    private String description;
    protected boolean nodeAttributeIsDirty;
    private static final String COLUMNS[] = {
        "Name", "Value", "Unit", "Type", "Batch Parameter"
    };
    protected String rowNames[];
    protected Object rowTypes[];
    protected NodeAttribute nodeAttributes[];
    private Collection componentCollection;
    private final Collection tableListenerCollection;
    private final TableModelEvent STD_TABLEMODEL_EVENT;

}