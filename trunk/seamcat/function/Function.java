// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Function.java

package org.seamcat.function;

import java.util.List;
import org.seamcat.interfaces.Functionable;
import org.seamcat.model.plugin.Function2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.function:
//            DiscreteFunction, Point2D, ConstantFunction, FunctionException

public abstract class Function
    implements Functionable, Function2D
{

    public Function()
    {
    }

    public abstract double evaluate(double d)
        throws FunctionException;

    public abstract double evaluate(double d, double d1)
        throws FunctionException;

    public abstract double integrate(double d, double d1, double d2)
        throws FunctionException;

    public abstract boolean isConstant();

    public abstract double evaluateMin();

    public boolean getBounds(double rxMin, double rxMax)
    {
        return true;
    }

    public abstract void offset(double d);

    public abstract void reset();

    public abstract Element toElement(Document document);

    public abstract String toString();

    public static Function create(Function f)
    {
        Function newFunc = null;
        if(f instanceof DiscreteFunction)
        {
            newFunc = new DiscreteFunction();
            List points = f.getPointsList();
            int i = 0;
            for(int stop = points.size(); i < stop; i++)
                ((DiscreteFunction)newFunc).addPoint(new Point2D((Point2D)points.get(i)));

        } else
        if(f instanceof ConstantFunction)
            newFunc = new ConstantFunction(((ConstantFunction)f).getConstant());
        return newFunc;
    }

    public static final int FUNCTION_TYPE_CONSTANT = 0;
    public static final int FUNCTION_TYPE_DISCRETE = 1;
}