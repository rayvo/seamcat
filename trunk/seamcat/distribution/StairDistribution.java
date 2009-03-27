// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StairDistribution.java

package org.seamcat.distribution;

import java.util.*;
import org.seamcat.function.Point2D;
import org.seamcat.model.plugin.PluginDistribution;

// Referenced classes of package org.seamcat.distribution:
//            UserDistribution, Bounds

public class StairDistribution extends UserDistribution
{

    public StairDistribution()
    {
        super(7);
        points = new LinkedList();
        sum = 0.0D;
    }

    public StairDistribution(List points)
    {
        this((Point2D[])points.toArray(new Point2D[points.size()]));
    }

    public String toString()
    {
        return "User defined stair distribution";
    }

    public StairDistribution(Point2D points[])
    {
        this();
        for(int x = 0; x < points.length; x++)
            add(points[x]);

    }

    public boolean validate()
    {
        double min = 1.7976931348623157E+308D;
        double max = 4.9406564584124654E-324D;
        Iterator i$ = points.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Point2D point = (Point2D)i$.next();
            if(point.getY() < min)
                min = point.getY();
            if(point.getY() > max)
                max = point.getY();
        } while(true);
        return min >= 0.0D && max == 1.0D;
    }

    public double trial()
    {
        double rndNo = RND.nextDouble();
        double n = (0.0D / 0.0D);
        int i;
        for(i = 0; i < points.size() && ((Point2D)points.get(i)).getY() <= rndNo; i++);
        if(i >= points.size())
            i = points.size() - 1;
        n = ((Point2D)points.get(i)).getX();
        if(n == (0.0D / 0.0D))
            throw new IllegalStateException("Consistency problem in StairDistributon");
        else
            return n;
    }

    public Bounds getBounds()
    {
        if(points.size() > 0)
            return new Bounds(((Point2D)points.get(0)).getX(), ((Point2D)points.get(points.size() - 1)).getX(), true);
        else
            return new Bounds(0.0D, 0.0D, false);
    }

    public void add(Point2D point)
    {
        points.add(new Point2D(point));
        sum += point.getY();
    }

    public void add(List pointsList)
    {
        for(Iterator i = pointsList.iterator(); i.hasNext(); add((Point2D)i.next()));
    }

    public Point2D[] getPoints()
    {
        Point2D points[] = new Point2D[this.points.size()];
        int x = 0;
        for(Iterator i = this.points.iterator(); i.hasNext();)
            points[x++] = new Point2D((Point2D)i.next());

        return points;
    }

    public void clearPoints()
    {
        points.clear();
        sum = 0.0D;
    }

    public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
    {
        return org.seamcat.model.plugin.PluginDistribution.DistributionType.UserDefinedStair;
    }

    private static final Random RND = new Random();
    private final List points;
    private double sum;

}