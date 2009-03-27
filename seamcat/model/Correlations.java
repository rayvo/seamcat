// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Correlations.java

package org.seamcat.model;

import java.util.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.presentation.Node;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model:
//            NodeAttribute

public class Correlations
    implements Node, TableModel
{

    public Correlations()
    {
        correlations = new HashMap();
    }

    public Correlations(Element elem)
    {
        this();
        NodeList nl = elem.getElementsByTagName("Correlation");
        int i = 0;
        for(int stop = nl.getLength(); i < stop; i++)
        {
            Element el = (Element)nl.item(i);
            correlations.put(el.getAttribute("name"), Double.valueOf(Double.parseDouble(el.getAttribute("value"))));
        }

    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("Correlations");
        Element elem;
        for(Iterator i$ = correlations.keySet().iterator(); i$.hasNext(); element.appendChild(elem))
        {
            String s = (String)i$.next();
            elem = doc.createElement("Correlation");
            elem.setAttribute("name", s);
            elem.setAttribute("value", Double.toString(((Double)correlations.get(s)).doubleValue()));
        }

        return element;
    }

    public void reset()
    {
        correlations.clear();
    }

    public void addCorrelation(String name, Double value)
    {
        correlations.put(name, value);
    }

    public int getColumnCount()
    {
        return 4;
    }

    public int getRowCount()
    {
        return correlations.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public Class getColumnClass(int columnIndex)
    {
        return java/lang/String;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object value = correlations.keySet().toArray()[rowIndex];
        if(columnIndex == 1)
            value = correlations.get(value);
        return value;
    }

    public void setValueAt(Object obj, int i, int j)
    {
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

    public void addTableModelListener(TableModelListener tablemodellistener)
    {
    }

    public void removeTableModelListener(TableModelListener tablemodellistener)
    {
    }

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

    public NodeAttribute[] getNodeAttributes()
    {
        NodeAttribute nodeAttributes[] = new NodeAttribute[correlations.size()];
        int i = 0;
        for(Iterator it = correlations.keySet().iterator(); it.hasNext();)
        {
            String name = (String)it.next();
            nodeAttributes[i++] = new NodeAttribute(name, "", correlations.get(name), "", null, false, false, null);
        }

        return nodeAttributes;
    }

    public String toString()
    {
        return STRINGLIST.getString("CORRELATIONS");
    }

    private static final ResourceBundle STRINGLIST;
    private Map correlations;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}