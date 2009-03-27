// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DiscreteFunction2.java

package org.seamcat.function;

import java.util.*;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Bounds;
import org.seamcat.model.plugin.Function3D;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.function:
//            Function2, Point3D, Point2D, FunctionException

public class DiscreteFunction2 extends Function2
    implements Function3D
{

    public DiscreteFunction2()
    {
        points = new ArrayList();
        origPoints = new ArrayList();
    }

    public DiscreteFunction2(Element element)
    {
        this();
        NodeList nl = element.getElementsByTagName("point3d");
        int x = 0;
        for(int size = nl.getLength(); x < size; x++)
            addPoint(new Point3D((Element)nl.item(x)));

    }

    public DiscreteFunction2(DiscreteFunction2 d)
    {
        this();
        Point3D p;
        for(Iterator i$ = d.getPoint3DList().iterator(); i$.hasNext(); addPoint(new Point3D(p)))
            p = (Point3D)i$.next();

    }

    public DiscreteFunction2(List points)
    {
        this.points = new ArrayList();
        origPoints = new ArrayList();
        Point2D p;
        for(Iterator i$ = points.iterator(); i$.hasNext(); addPoint((p instanceof Point3D) ? (Point3D)p : new Point3D(p, 0.0D)))
            p = (Point2D)i$.next();

    }

    public String toString()
    {
        return "Discrete Function (3 Dimensions)";
    }

    public DiscreteFunction2(Point3D points[])
    {
        this();
        for(int x = 0; x < points.length; x++)
            addPoint(points[x]);

    }

    public final void addPoint(Point3D point3D)
    {
        points.add(point3D);
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
        if(i == size || i - 1 <= 1)
            rX = 0.0D;
        else
        if(_p.getY() == p.getY())
            rX = 0.0D;
        else
            rX = _p.getX() + ((rY - _p.getY()) / (p.getY() - _p.getY())) * (p.getX() - _p.getX());
        return rX;
    }

    public void storePoints()
    {
        origPoints.clear();
        Point3D p;
        for(Iterator i$ = points.iterator(); i$.hasNext(); origPoints.add(new Point3D(p)))
            p = (Point3D)i$.next();

    }

    public void resetPoints()
    {
        points.clear();
        points.addAll(origPoints);
    }

    public void normalize()
    {
        Point3D point;
        for(Iterator i$ = points.iterator(); i$.hasNext(); point.setRZ(1000D))
        {
            point = (Point3D)i$.next();
            double rZ = point.getRZ();
            double rY = point.getY();
            rY -= 10D * Math.log10(rZ / 1000D);
            point.setY(rY);
        }

    }

    public boolean isConstant()
    {
        return false;
    }

    public double evaluate(double rX)
        throws FunctionException
    {
        int size = points.size();
        double rLast = ((Point3D)points.get(size - 1)).getX();
        double rFirst = ((Point3D)points.get(0)).getX();
        if(rX > rLast || rX < rFirst)
            throw new FunctionException();
        int i;
        for(i = 0; rX > ((Point3D)points.get(i)).getX() && i < size; i++);
        if(i - 1 == size || i == 0)
            return ((Point3D)points.get(0)).getY();
        if(rX == ((Point3D)points.get(i)).getX())
            return ((Point3D)points.get(i)).getY();
        double rX1 = ((Point3D)points.get(i - 1)).getX();
        double rY1 = ((Point3D)points.get(i - 1)).getY();
        double rX2 = ((Point3D)points.get(i)).getX();
        double rY2 = ((Point3D)points.get(i)).getY();
        if(rX2 == rX1)
        {
            return ((Point3D)points.get(i)).getY();
        } else
        {
            double rT = (rX - rX1) / (rX2 - rX1);
            double rY = rT * rY2 + (1.0D - rT) * rY1;
            return rY;
        }
    }

    public double integrate(double bandwithDifference, double referenceBandwith)
        throws FunctionException
    {
        int i = 0;
        int j = 0;
        int size = points.size();
        double rXa = 0.0D;
        double rXb = 0.0D;
        double rYaLin = 0.0D;
        double rYbLin = 0.0D;
        double rYa = 0.0D;
        double rYb = 0.0D;
        double rSum = 0.0D;
        bandwithDifference = (double)(long)(bandwithDifference * 1000000D) / 1000000D;
        if(size < 2)
            return 0.0D;
        if((bandwithDifference - referenceBandwith) * 0.5D < ((Point3D)points.get(0)).getX() || (bandwithDifference + referenceBandwith) * 0.5D > ((Point3D)points.get(size - 1)).getX())
        {
            LOG.error("Emission mask is undefined at the begining  or at the end of the reception bandwidth:");
            LOG.error((new StringBuilder()).append("(").append(bandwithDifference).append(" - ").append(referenceBandwith).append(" * 0.5 < (").append(((Point3D)points.get(0)).getX()).append(")) ||").append(" (").append(bandwithDifference).append(" + ").append(referenceBandwith).append(" * 0.5 > (").append(((Point3D)points.get(size - 1)).getX()).append(")) -> ").append(bandwithDifference - referenceBandwith * 0.5D < ((Point3D)points.get(0)).getX()).append(" || ").append(bandwithDifference + referenceBandwith * 0.5D > ((Point3D)points.get(size - 1)).getX()).toString());
            throw new FunctionException("Emission mask is undefined at the begining or at the end of the reception bandwidth");
        }
        for(i = 0; i < size && ((Point3D)points.get(i)).getX() <= bandwithDifference - referenceBandwith * 0.5D; i++);
        rXa = bandwithDifference - referenceBandwith * 0.5D;
        rYa = ((Point3D)points.get(i - 1)).getY() + ((((Point3D)points.get(i)).getY() - ((Point3D)points.get(i - 1)).getY()) / (((Point3D)points.get(i)).getX() - ((Point3D)points.get(i - 1)).getX())) * (rXa - ((Point3D)points.get(i - 1)).getX());
        rYaLin = Math.pow(10D, rYa / 10D);
        boolean wasInsideForLoop = false;
        for(j = i; j < size && bandwithDifference + referenceBandwith * 0.5D >= ((Point3D)points.get(j)).getX(); j++)
        {
            rXb = ((Point3D)points.get(j)).getX();
            rYb = ((Point3D)points.get(j)).getY();
            rYbLin = Math.pow(10D, rYb / 10D);
            if(rYb == rYa)
                rSum += (rXb - rXa) * rYaLin;
            else
                rSum += (((10D / Math.log(10D)) * (rXb - rXa)) / (rYb - rYa)) * (rYbLin - rYaLin);
            rYa = rYb;
            rXa = rXb;
            rYaLin = rYbLin;
            wasInsideForLoop = true;
        }

        if(j == size)
            j--;
        if(bandwithDifference + referenceBandwith * 0.5D < ((Point3D)points.get(j)).getX() || !wasInsideForLoop)
        {
            rXb = bandwithDifference + referenceBandwith * 0.5D;
            rYb = ((Point3D)points.get(j - 1)).getY() + ((((Point3D)points.get(j)).getY() - ((Point3D)points.get(j - 1)).getY()) / (((Point3D)points.get(j)).getX() - ((Point3D)points.get(j - 1)).getX())) * (rXb - ((Point3D)points.get(j - 1)).getX());
            rYbLin = Math.pow(10D, rYb / 10D);
            if(rYb == rYa)
                rSum += (rXb - rXa) * rYaLin;
            else
                rSum += (((10D / Math.log(10D)) * (rXb - rXa)) / (rYb - rYa)) * (rYbLin - rYaLin);
        }
        if(rSum != 0.0D)
            return 10D * Math.log10(rSum);
        else
            return rSum;
    }

    public void offset(double offset)
    {
        Point3D thisPoint;
        for(Iterator i$ = points.iterator(); i$.hasNext(); thisPoint.setY(thisPoint.getY() + offset))
            thisPoint = (Point3D)i$.next();

    }

    public double evaluate(double rX, double rB)
        throws FunctionException
    {
        return evaluate(rX);
    }

    public double evaluateMin()
    {
        double rFinalY = ((Point3D)points.get(0)).getY();
        Iterator i$ = points.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Point3D p = (Point3D)i$.next();
            double rY = p.getY();
            if(rY < rFinalY)
                rFinalY = rY;
        } while(true);
        return rFinalY;
    }

    public List getPointsList()
    {
        return getPoint3DList();
    }

    public List getPoint3DList()
    {
        return points;
    }

    public TableModel getTableModel()
    {
        throw new UnsupportedOperationException("DiscreteFunction2 does not implement getTableModel()");
    }

    public void setTableModel(TableModel t)
    {
        throw new UnsupportedOperationException("DiscreteFunction2 does not implement setTableModel(TableModel)");
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("discretefunction2");
        Point3D point;
        for(Iterator i$ = points.iterator(); i$.hasNext(); element.appendChild(point.toElement(doc)))
            point = (Point3D)i$.next();

        return element;
    }

    public Bounds getBounds()
    {
        Collections.sort(getPoint3DList());
        Bounds b;
        if(points.size() > 0)
        {
            double min = ((Point3D)points.get(0)).getX();
            double max = ((Point3D)points.get(points.size() - 1)).getX();
            b = new Bounds(min, max, true);
        } else
        {
            b = new Bounds(0.0D, 0.0D, false);
        }
        return b;
    }

    public void addPoint(double x, double y, double z)
    {
        addPoint(new Point3D(x, y, z));
    }

    public void addPoint(double x, double y)
    {
        addPoint(new Point3D(x, y, 0.0D));
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/function/DiscreteFunction2);
    private List points;
    private final List origPoints;

}