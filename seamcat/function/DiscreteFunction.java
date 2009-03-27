// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DiscreteFunction.java

package org.seamcat.function;

import java.util.*;
import javax.swing.table.TableModel;
import org.seamcat.model.plugin.Function2D;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.function:
//            Function, Point2D, FunctionException

public class DiscreteFunction extends Function
    implements Function2D
{

    public DiscreteFunction()
    {
        points = new ArrayList();
    }

    public DiscreteFunction(Element element)
    {
        this();
        NodeList nl = element.getElementsByTagName("point2d");
        int x = 0;
        for(int size = nl.getLength(); x < size; x++)
            addPoint(new Point2D((Element)nl.item(x)));

    }

    public DiscreteFunction(Point2D points[])
    {
        this();
        for(int x = 0; x < points.length; x++)
            addPoint(points[x]);

    }

    public String toString()
    {
        return "Discrete Function (2 Dimensions)";
    }

    public DiscreteFunction(List _points)
    {
        this();
        points = _points;
    }

    public DiscreteFunction(DiscreteFunction d)
    {
        this();
        Point2D p;
        for(Iterator i$ = d.getPointsList().iterator(); i$.hasNext(); addPoint(new Point2D(p)))
            p = (Point2D)i$.next();

        setNumber(d.getNumber());
    }

    public void reset()
    {
    }

    public double evaluate(double rX)
        throws FunctionException
    {
        int size = points.size();
        if(size == 0)
            return 0.0D;
        double rLast = ((Point2D)points.get(size - 1)).getX();
        double rFirst = ((Point2D)points.get(0)).getX();
        if(rX > rLast || rX < rFirst)
            throw new FunctionException((new StringBuilder()).append("Specified value (").append(rX).append(") is outside bounds [").append(rFirst).append(" to ").append(rLast).append("]").toString());
        int i;
        for(i = 0; rX > ((Point2D)points.get(i)).getX() && i < size; i++);
        if(i == size || i == 0)
            return ((Point2D)points.get(0)).getY();
        if(rX == ((Point2D)points.get(i)).getX())
            return ((Point2D)points.get(i)).getY();
        double rX1 = ((Point2D)points.get(i - 1)).getX();
        double rY1 = ((Point2D)points.get(i - 1)).getY();
        double rX2 = ((Point2D)points.get(i)).getX();
        double rY2 = ((Point2D)points.get(i)).getY();
        if(rX2 == rX1)
        {
            return rY2;
        } else
        {
            double rT = (rX - rX1) / (rX2 - rX1);
            return rT * rY2 + (1.0D - rT) * rY1;
        }
    }

    public double evaluate(double rX, double rB)
        throws FunctionException
    {
        return evaluate(rX);
    }

    public double evaluateMin()
    {
        double rFinalY = ((Point2D)points.get(0)).getY();
        Iterator i$ = points.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Point2D p = (Point2D)i$.next();
            double rY = p.getY();
            if(rY < rFinalY)
                rFinalY = rY;
        } while(true);
        return rFinalY;
    }

    public double integrate(double rX, double rBvr, double rBit)
        throws FunctionException
    {
        int size = points.size();
        double rSum = 0.0D;
        int i;
        for(i = 0; i < size && rX - rBvr * 0.5D >= ((Point2D)points.get(i)).getX(); i++);
        if(i == 0)
            throw new FunctionException();
        double rXa = rX - rBvr * 0.5D;
        double rYa = ((Point2D)points.get(i - 1)).getY() + ((((Point2D)points.get(i)).getY() - ((Point2D)points.get(i - 1)).getY()) / (((Point2D)points.get(i)).getX() - ((Point2D)points.get(i - 1)).getX())) * (rXa - ((Point2D)points.get(i - 1)).getX());
        double rYaLin = Math.pow(10D, rYa / 10D);
        int j;
        double rXb;
        double rYbLin;
        double rYb;
        for(j = i; j < size && rX + rBvr * 0.5D >= ((Point2D)points.get(j)).getX(); j++)
        {
            rXb = ((Point2D)points.get(j)).getX();
            rYb = ((Point2D)points.get(j)).getY();
            rYbLin = Math.pow(10D, rYb / 10D);
            if(rYb == rYa)
                rSum += (rXb - rXa) * rYaLin;
            else
                rSum += (((10D / Math.log(10D)) * (rXb - rXa)) / (rYb - rYa)) * (rYbLin - rYaLin);
            rYa = rYb;
            rXa = rXb;
            rYaLin = rYbLin;
        }

        if(j == size)
            throw new FunctionException();
        rXb = rX + rBvr * 0.5D;
        rYb = ((Point2D)points.get(j - 1)).getY() + ((((Point2D)points.get(i)).getY() - ((Point2D)points.get(i - 1)).getY()) / (((Point2D)points.get(i)).getX() - ((Point2D)points.get(i - 1)).getX())) * (rXb - ((Point2D)points.get(i - 1)).getX());
        rYbLin = Math.pow(10D, rYb / 10D);
        if(rYb == rYa)
            rSum += (rXb - rXa) * rYaLin;
        else
            rSum += (((10D / Math.log(10D)) * (rXb - rXa)) / (rYb - rYa)) * (rYbLin - rYaLin);
        return 10D * Math.log10(rSum);
    }

    public boolean isConstant()
    {
        return false;
    }

    public void offset(double offset)
    {
        Point2D p;
        for(Iterator i$ = points.iterator(); i$.hasNext(); p.setY(p.getY() + offset))
            p = (Point2D)i$.next();

    }

    public double inverse(double rY)
    {
        double rX;
        if(points.size() == 0)
            return rX = 0.0D;
        Point2D p = null;
        int i = 0;
        int size = points.size();
        Point2D _p;
        do
        {
            _p = p;
            p = (Point2D)points.get(i);
        } while(++i < size && rY > p.getY());
        if(rY == p.getY())
            rX = p.getX();
        else
        if(_p.getY() == p.getY())
            rX = 0.0D;
        else
            rX = _p.getX() + ((rY - _p.getY()) / (p.getY() - _p.getY())) * (p.getX() - _p.getX());
        return rX;
    }

    public double getRangeMin()
    {
        return ((Point2D)points.get(0)).getX();
    }

    public double getRangeMax()
    {
        return ((Point2D)points.get(points.size() - 1)).getX();
    }

    public double evaluateMax()
    {
        double rFinalY = ((Point2D)points.get(0)).getY();
        Iterator i$ = points.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Point2D p = (Point2D)i$.next();
            double rY = p.getY();
            if(rY > rFinalY)
                rFinalY = rY;
        } while(true);
        return rFinalY;
    }

    public List getPointsList()
    {
        return points;
    }

    public final void addPoint(Point2D point)
    {
        points.add(point);
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public TableModel getTableModel()
    {
        throw new UnsupportedOperationException("DiscreteFunction does not implement getTableModel()");
    }

    public void setTableModel(TableModel t)
    {
        throw new UnsupportedOperationException("DiscreteFunction does not implement setTableModel(TableModel)");
    }

    public void sortPoints()
    {
        Collections.sort(getPointsList(), Point2D.POINTX_COMPARATOR);
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("discretefunction");
        Point2D p;
        for(Iterator i$ = points.iterator(); i$.hasNext(); element.appendChild(p.toElement(doc)))
            p = (Point2D)i$.next();

        return element;
    }

    public void addPoint(double x, double y)
    {
        addPoint(new Point2D(x, y));
    }

    public double integrate(double x, double bandwidth)
        throws Exception
    {
        return integrate(x, bandwidth, 0.0D);
    }

    private List points;
    private int number;
}