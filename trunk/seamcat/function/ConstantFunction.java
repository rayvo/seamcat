// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ConstantFunction.java

package org.seamcat.function;

import java.util.List;
import javax.swing.table.TableModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.function:
//            Function

public class ConstantFunction extends Function
{

    public ConstantFunction(double value)
    {
        constant = value;
        old_constant = value;
    }

    public ConstantFunction(ConstantFunction f)
    {
        this(f.getConstant());
        old_constant = f.old_constant;
    }

    public void reset()
    {
        constant = old_constant;
    }

    public ConstantFunction(Element element)
    {
        this(Double.parseDouble(element.getAttribute("value")));
    }

    public String toString()
    {
        return (new StringBuilder()).append("Constant Function(").append(constant).append(")").toString();
    }

    public double evaluate(double rX, double rB)
    {
        double f;
        if(Math.abs(rX) < rB / 2D)
            f = constant;
        else
            f = 1.7976931348623157E+308D;
        return f;
    }

    public double evaluate(double rX)
    {
        return constant;
    }

    public double evaluateMin()
    {
        return constant;
    }

    public double integrate(double rX, double rBvr, double rBit)
    {
        double rA = Math.max(rX - rBvr / 2D, -rBit / 2D);
        double rB = Math.min(rX + rBvr / 2D, rBit / 2D);
        double f;
        if(rA < rB)
            f = 10D * Math.log10(rB - rA) + constant;
        else
        if(rB <= rA)
            f = 4.9406564584124654E-324D;
        else
            f = 0.0D;
        return f;
    }

    public boolean isConstant()
    {
        return true;
    }

    public void offset(double offset)
    {
        old_constant = constant;
        constant += offset;
    }

    public void setConstant(double constant)
    {
        this.constant = constant;
    }

    public double getConstant()
    {
        return constant;
    }

    public TableModel getTableModel()
    {
        throw new UnsupportedOperationException("ConstantFunction does not implement getTableModel()");
    }

    public void setTableModel(TableModel t)
    {
        throw new UnsupportedOperationException("ConstantFunction does not implement setTableModel(TableModel)");
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("ConstantFunction");
        element.setAttribute("value", Double.toString(getConstant()));
        return element;
    }

    public List getPointsList()
    {
        throw new UnsupportedOperationException("Constant Function does not define any points");
    }

    public void addPoint(double x, double y)
    {
        throw new UnsupportedOperationException("Unable to add point to Constant Function");
    }

    public double integrate(double x, double bandwidth)
        throws Exception
    {
        throw new UnsupportedOperationException("Constant Function does not define the integrate mehtod");
    }

    private double constant;
    private double old_constant;
}