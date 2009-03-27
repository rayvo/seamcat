// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AntennaPattern.java

package org.seamcat.model.core;

import javax.swing.event.TableModelListener;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.interfaces.Patternable;
import org.seamcat.presentation.components.DiscreteFunctionTableModelAdapter;
import org.w3c.dom.*;

public abstract class AntennaPattern
    implements Patternable
{

    public AntennaPattern()
    {
        rangeMin = 0.0D;
        rangeMax = 360D;
        symmetryPoint = 0.0D;
        model = new DiscreteFunctionTableModelAdapter();
        initializeRange();
    }

    public AntennaPattern(AntennaPattern ant)
    {
        this();
        setPattern(new DiscreteFunction(ant.getPattern()));
        rangeMin = ant.getRangeMin();
        rangeMax = ant.getRangeMax();
    }

    public AntennaPattern(Element element)
    {
        this();
        if(element != null)
        {
            try
            {
                rangeMin = Double.parseDouble(element.getAttribute("rangeMin"));
                rangeMax = Double.parseDouble(element.getAttribute("rangeMax"));
            }
            catch(Exception e) { }
            NodeList nl = element.getElementsByTagName("function");
            if(nl.getLength() > 0)
                setPattern(new DiscreteFunction((Element)nl.item(0).getFirstChild()));
        }
        initializeRange();
    }

    protected abstract void initializeRange();

    public DiscreteFunction getPattern()
    {
        return model.getDiscreteFunction();
    }

    public void setPattern(DiscreteFunction pattern)
    {
        model.setDiscreteFunction(pattern);
    }

    public int getColumnCount()
    {
        return model.getColumnCount();
    }

    public int getRowCount()
    {
        return model.getRowCount();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }

    public Class getColumnClass(int columnIndex)
    {
        return model.getColumnClass(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return model.getValueAt(rowIndex, columnIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        model.setValueAt(aValue, rowIndex, columnIndex);
    }

    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
        case 0: // '\0'
            return "Angle";

        case 1: // '\001'
            return "Attenuation";
        }
        throw new IllegalArgumentException((new StringBuilder()).append("Antenna pattern only has two columns <").append(columnIndex).append(">").toString());
    }

    public void addTableModelListener(TableModelListener l)
    {
        model.addTableModelListener(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        model.removeTableModelListener(l);
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("AntennaPattern");
        element.setAttribute("rangeMin", Double.toString(rangeMin));
        element.setAttribute("rangeMax", Double.toString(rangeMax));
        Element functionElement = doc.createElement("function");
        functionElement.appendChild(getPattern().toElement(doc));
        element.appendChild(functionElement);
        return element;
    }

    public double getRangeMax()
    {
        return rangeMax;
    }

    public void setRangeMax(double rangeMax)
    {
        this.rangeMax = rangeMax;
    }

    public double getRangeMin()
    {
        return rangeMin;
    }

    public void setRangeMin(double rangeMin)
    {
        this.rangeMin = rangeMin;
    }

    public double getSymmetryPoint()
    {
        return symmetryPoint;
    }

    public void setSymmetryPoint(double symmetryPoint)
    {
        this.symmetryPoint = symmetryPoint;
    }

    private DiscreteFunctionTableModelAdapter model;
    private static final String COL1_NAME = "Angle";
    private static final String COL2_NAME = "Attenuation";
    protected double rangeMin;
    protected double rangeMax;
    protected double symmetryPoint;
}