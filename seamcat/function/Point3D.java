// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Point3D.java

package org.seamcat.function;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.function:
//            Point2D

public class Point3D extends Point2D
{

    public Point3D()
    {
        this(0.0D, 0.0D, 0.0D);
    }

    public Point3D(Element element)
    {
        this(Double.parseDouble(element.getAttribute("x")), Double.parseDouble(element.getAttribute("y")), Double.parseDouble(element.getAttribute("z")));
    }

    public Point3D(Point3D p)
    {
        this(p.getX(), p.getY(), p.getRZ());
    }

    public Point3D(double rX, double rY, double rZ)
    {
        super(rX, rY);
        this.rZ = rZ;
    }

    public Point3D(Point2D p2d, double rZ)
    {
        super(p2d);
        this.rZ = rZ;
    }

    public double getRZ()
    {
        return rZ;
    }

    public void setRZ(double rZ)
    {
        this.rZ = rZ;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("point3d");
        element.setAttribute("x", String.valueOf(getX()));
        element.setAttribute("y", String.valueOf(getY()));
        element.setAttribute("z", String.valueOf(getRZ()));
        return element;
    }

    private double rZ;
}