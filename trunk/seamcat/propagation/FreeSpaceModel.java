// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FreeSpaceModel.java

package org.seamcat.propagation;

import java.util.ArrayList;
import java.util.List;
import org.seamcat.distribution.GaussianDistribution;
import org.seamcat.model.NodeAttribute;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.propagation:
//            BuiltInModel

public class FreeSpaceModel extends BuiltInModel
{

    public FreeSpaceModel()
    {
    }

    public String toString()
    {
        return "Free Space";
    }

    public FreeSpaceModel(Element element)
    {
        super((Element)element.getElementsByTagName("BuiltInModel").item(0));
        String stddev;
        if((stddev = element.getAttribute("rWeStdDev")) != null && stddev.length() > 0)
            getVariationsDistrib().setStdDev(Double.parseDouble(stddev));
    }

    public FreeSpaceModel(double rWeStdDev)
    {
        super(1, 1, 2, 0);
        getVariationsDistrib().setStdDev(rWeStdDev);
    }

    public double evaluate(double rFreq, double rDist, double rHTx, double rHRx)
    {
        double rL = 0.0D;
        if(getMedianSelected())
            rL = 32.439999999999998D + 10D * Math.log10(((rHTx - rHRx) * (rHTx - rHRx)) / 1000000D + rDist * rDist) + 20D * Math.log10(rFreq);
        if(getVariationsSelected())
            rL += getVariationsDistrib().trial();
        return rL;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("FreeSpaceModel");
        element.setAttribute("rWeStdDev", Double.toString(getVariationsDistrib().getStdDev()));
        element.appendChild(super.toElement(doc));
        return element;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Model reference", "", this, "Propagation Model", null, false, false, null));
        nodeList.add(new NodeAttribute("Median loss", "", getMedianSelected() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.FALSE, Boolean.TRUE
        }, true, true, null));
        nodeList.add(new NodeAttribute("Variations", "", getVariationsSelected() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.FALSE, Boolean.TRUE
        }, true, true, null));
        nodeList.add(new NodeAttribute("Std. dev.", "", new Double(getVariationsDistrib().getStdDev()), "Double", null, false, true, null));
        nodeAttributes = (NodeAttribute[])(NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
        switch(rowIndex)
        {
        case 1: // '\001'
            setMedianSelected(((Boolean)aValue).booleanValue());
            break;

        case 2: // '\002'
            setVariationsSelected(((Boolean)aValue).booleanValue());
            break;

        case 3: // '\003'
            setStdDev(((Number)aValue).doubleValue());
            break;
        }
    }

    public static final String MODEL_ID = "FreeSpaceModel";
}
