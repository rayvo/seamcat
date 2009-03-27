// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LabeledPairLayoutWithUnit.java

package org.seamcat.presentation;

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JComponent;

// Referenced classes of package org.seamcat.presentation:
//            LabeledPairLayout

public class LabeledPairLayoutWithUnit extends LabeledPairLayout
{

    public LabeledPairLayoutWithUnit()
    {
        units = new Vector();
    }

    public void addLayoutComponent(String s, Component c)
    {
        super.addLayoutComponent(s, c);
        if(s.equals("unit"))
            units.addElement((JComponent)c);
    }

    public void layoutContainer(Container c)
    {
        int labelWidth = maxWidth(labels);
        int unitWidth = maxWidth(units);
        Insets insets = c.getInsets();
        int yPos = insets.top;
        Iterator lIt = labels.iterator();
        Iterator fIt = fields.iterator();
        for(Iterator uIt = units.iterator(); lIt.hasNext() && fIt.hasNext() && uIt.hasNext();)
        {
            JComponent label = (JComponent)lIt.next();
            JComponent field = (JComponent)fIt.next();
            JComponent unit = (JComponent)uIt.next();
            int height = Math.max(label.getPreferredSize().height, field.getPreferredSize().height);
            label.setBounds(insets.left, yPos, labelWidth, height);
            field.setBounds(insets.left + labelWidth + xGap, yPos, c.getSize().width - (labelWidth + 2 * xGap + insets.left + insets.right + unitWidth), height);
            unit.setBounds(insets.left + labelWidth + (c.getSize().width - (labelWidth + 2 * xGap + insets.left + insets.right + unitWidth)) + 2 * xGap, yPos, unitWidth, height);
            yPos += height + yGap;
        }

    }

    public Dimension minimumLayoutSize(Container c)
    {
        Dimension d = super.minimumLayoutSize(c);
        d.width += maxWidth(units) + 4 * xGap;
        return d;
    }

    public void removeLayoutComponent(Component c)
    {
        super.removeLayoutComponent(c);
        units.remove(c);
    }

    public static final String UNIT = "unit";
    protected Vector units;
}
