// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GraphTableModelListener.java

package org.seamcat.presentation;

import java.awt.Window;
import java.io.PrintStream;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

// Referenced classes of package org.seamcat.presentation:
//            UserDefinedGraph

public class GraphTableModelListener
    implements TableModelListener
{

    public GraphTableModelListener(UserDefinedGraph graph, TableModel model, Window parent)
    {
        this.graph = graph;
        this.model = model;
        this.parent = parent;
    }

    public void tableChanged(TableModelEvent e)
    {
        System.out.println((new StringBuilder()).append("Tabledata changed (").append(System.currentTimeMillis()).append(")").toString());
        graph.refreshGraph(model);
        if(parent != null)
            parent.pack();
    }

    public TableModel getModel()
    {
        return model;
    }

    public void setGraph(UserDefinedGraph graph)
    {
        this.graph = graph;
    }

    public void setModel(TableModel model)
    {
        this.model = model;
    }

    public UserDefinedGraph getGraph()
    {
        return graph;
    }

    private UserDefinedGraph graph;
    private TableModel model;
    private Window parent;
}
