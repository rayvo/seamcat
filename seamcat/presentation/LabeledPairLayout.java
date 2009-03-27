// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LabeledPairLayout.java

package org.seamcat.presentation;

import java.awt.*;
import java.util.*;
import javax.swing.JComponent;

public class LabeledPairLayout
    implements LayoutManager
{

    public LabeledPairLayout()
    {
        labels = new Vector();
        fields = new Vector();
        yGap = 2;
        xGap = 2;
    }

    public void addLayoutComponent(String s, Component c)
    {
        if(!(c instanceof JComponent))
            throw new IllegalArgumentException("Layoutcomponents must be of type JComponent");
        if(s.equals("label"))
            labels.addElement((JComponent)c);
        else
        if(s.equals("field"))
            fields.addElement((JComponent)c);
    }

    public void layoutContainer(Container c)
    {
        Insets insets = c.getInsets();
        int labelWidth = maxWidth(labels);
        int yPos = insets.top;
        Iterator lIt = labels.iterator();
        for(Iterator fIt = fields.iterator(); lIt.hasNext() && fIt.hasNext();)
        {
            JComponent label = (JComponent)lIt.next();
            JComponent field = (JComponent)fIt.next();
            int height = Math.max(label.getPreferredSize().height, field.getPreferredSize().height);
            label.setBounds(insets.left, yPos, labelWidth, height);
            field.setBounds(insets.left + labelWidth + xGap, yPos, c.getSize().width - (labelWidth + xGap + insets.left + insets.right), height);
            yPos += height + yGap;
        }

    }

    public Dimension minimumLayoutSize(Container c)
    {
        Insets insets = c.getInsets();
        int labelWidth = maxWidth(labels);
        int fieldWidth = maxWidth(fields);
        int yPos = insets.top;
        Iterator lIt = labels.iterator();
        for(Iterator fIt = fields.iterator(); lIt.hasNext() && fIt.hasNext();)
        {
            JComponent label = (JComponent)lIt.next();
            JComponent field = (JComponent)fIt.next();
            int height = Math.max(label.getPreferredSize().height, field.getPreferredSize().height);
            yPos += height + yGap;
        }

        yPos += 2 * yGap;
        return new Dimension(labelWidth + fieldWidth, yPos);
    }

    public Dimension preferredLayoutSize(Container c)
    {
        return minimumLayoutSize(c);
    }

    public void removeLayoutComponent(Component c)
    {
        labels.remove(c);
        fields.remove(c);
    }

    public int getYGap()
    {
        return yGap;
    }

    public void setXGap(int xGap)
    {
        this.xGap = xGap;
    }

    public void setYGap(int yGap)
    {
        this.yGap = yGap;
    }

    public int getXGap()
    {
        return xGap;
    }

    protected static final int maxWidth(Collection comps)
    {
        int width = 0;
        for(Iterator i$ = comps.iterator(); i$.hasNext();)
        {
            JComponent comp = (JComponent)i$.next();
            int localWidth = (int)comp.getPreferredSize().getWidth();
            if(localWidth == 0)
                localWidth = comp.getWidth();
            width = Math.max(width, localWidth);
        }

        return width;
    }

    public static final String LABEL = "label";
    public static final String FIELD = "field";
    protected Vector labels;
    protected Vector fields;
    protected int yGap;
    protected int xGap;
}
