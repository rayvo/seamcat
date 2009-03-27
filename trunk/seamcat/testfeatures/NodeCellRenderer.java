// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NodeCellRenderer.java

package org.seamcat.testfeatures;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import org.w3c.dom.Node;

public class NodeCellRenderer extends JLabel
    implements TreeCellRenderer
{

    public NodeCellRenderer()
    {
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        if(value instanceof Node)
        {
            Node n = (Node)value;
            setText((new StringBuilder()).append(n.getLocalName()).append(" [").append(n.getClass().getSimpleName()).append("]").toString());
        } else
        {
            setText(value.toString());
        }
        if(selected)
            setForeground(Color.RED);
        else
            setForeground(Color.BLACK);
        setOpaque(true);
        return this;
    }
}
