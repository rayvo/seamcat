// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Function2.java

package org.seamcat.function;

import java.util.List;
import org.seamcat.distribution.Bounds;
import org.seamcat.interfaces.Functionable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.function:
//            FunctionException

public abstract class Function2
    implements Functionable
{

    public Function2()
    {
    }

    public abstract List getPoint3DList();

    public abstract double evaluate(double d)
        throws FunctionException;

    public abstract double evaluate(double d, double d1)
        throws FunctionException;

    public abstract double integrate(double d, double d1)
        throws FunctionException;

    public abstract boolean isConstant();

    public abstract Bounds getBounds();

    public abstract double evaluateMin();

    public abstract void offset(double d);

    public abstract void normalize();

    public abstract String toString();

    public abstract Element toElement(Document document);

    public static final int FUNCTION_TYPE_CONSTANT = 0;
    public static final int FUNCTION_TYPE_DISCRETE = 1;
}