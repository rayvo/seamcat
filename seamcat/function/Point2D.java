// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Point2D.java

package org.seamcat.function;

import java.util.Comparator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Point2D
    implements Comparable
{
    static final class PointComparator
        implements Comparator
    {

        public final int compare(Point2D p1, Point2D p2)
        {
            int result;
            switch(compareOn)
            {
            case 0: // '\0'
                if(p2.getX() == 0.0D && p2.getY() == 0.0D)
                    return -1;
                if(p1.getX() == p2.getX())
                {
                    result = 0;
                    break;
                }
                if(p1.getX() > p2.getX())
                    result = 1;
                else
                    result = -1;
                break;

            case 1: // '\001'
                if(p1.getY() == p2.getY())
                {
                    result = 0;
                    break;
                }
                if(p1.getY() > p2.getY())
                    result = 1;
                else
                    result = -1;
                break;

            default:
                throw new IllegalStateException("compareOn value out of range");
            }
            return result;
        }

        public volatile int compare(Object x0, Object x1)
        {
            return compare((Point2D)x0, (Point2D)x1);
        }

        public static final int X = 0;
        public static final int Y = 1;
        private int compareOn;

        public PointComparator(int compareOn)
        {
            this.compareOn = compareOn;
        }
    }


    public Point2D(double rX, double rY)
    {
        setX(rX);
        setY(rY);
    }

    public Point2D(Point2D p)
    {
        this(p.getX(), p.getY());
    }

    public Point2D(Element element)
    {
        this(Double.parseDouble(element.getAttribute("x")), Double.parseDouble(element.getAttribute("y")));
    }

    public double getX()
    {
        return rX;
    }

    public void setX(double _rX)
    {
        rX = _rX;
    }

    public double getY()
    {
        return rY;
    }

    public void setY(double rY)
    {
        this.rY = rY;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append('(');
        sb.append(rX);
        sb.append(", ");
        sb.append(rY);
        sb.append(')');
        return sb.toString();
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("point2d");
        element.setAttribute("x", String.valueOf(getX()));
        element.setAttribute("y", String.valueOf(getY()));
        return element;
    }

    public int compareTo(Point2D pt)
    {
        return POINTX_COMPARATOR.compare(this, pt);
    }

    public volatile int compareTo(Object x0)
    {
        return compareTo((Point2D)x0);
    }

    public static final Comparator POINTX_COMPARATOR = new PointComparator(0);
    public static final Comparator POINTY_COMPARATOR = new PointComparator(1);
    private double rX;
    private double rY;

}