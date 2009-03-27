// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ConstantFunction2.java

package org.seamcat.function;

import java.util.List;
import javax.swing.table.TableModel;
import org.seamcat.distribution.Bounds;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.function:
//            Function2, ConstantFunction

public class ConstantFunction2 extends Function2
{

    public ConstantFunction2(double value)
    {
        constant = value;
    }

    public ConstantFunction2(ConstantFunction f)
    {
        this(f.getConstant());
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

    public String toString()
    {
        return (new StringBuilder()).append("Constant Function (").append(constant).append(")").toString();
    }

    public double evaluate(double rX)
    {
        return constant;
    }

    public double evaluateMin()
    {
        return constant;
    }

    public double integrate(double rX, double rBvr)
    {
        return 0.0D;
    }

    public boolean isConstant()
    {
        return true;
    }

    public void offset(double offset)
    {
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

    public void normalize()
    {
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
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean getBounds(double rxMin, double rxMax)
    {
        return false;
    }

    public Bounds getBounds()
    {
        return new Bounds(constant, constant, true);
    }

    public List getPointsList()
    {
        throw new UnsupportedOperationException("Constant Function does not define any points");
    }

    public List getPoint3DList()
    {
        throw new UnsupportedOperationException("Constant Function does not define any points");
    }

    private double constant;
}