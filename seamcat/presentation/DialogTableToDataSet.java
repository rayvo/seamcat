// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogTableToDataSet.java

package org.seamcat.presentation;

import java.util.*;
import org.seamcat.function.Point2D;
import org.seamcat.function.Point3D;

public class DialogTableToDataSet
{

    public DialogTableToDataSet()
    {
    }

    public static void symmetrize(List data, double symmetryPoint)
    {
        List newPoints = new ArrayList();
        Iterator i = data.iterator();
        do
        {
            if(!i.hasNext())
                break;
            Point2D point = (Point2D)i.next();
            double x = symmetryPoint + (symmetryPoint - point.getX());
            if(!pointXExistsInList(data, x))
                if(point instanceof Point3D)
                    newPoints.add(new Point3D(x, point.getY(), ((Point3D)point).getRZ()));
                else
                    newPoints.add(new Point2D(x, point.getY()));
        } while(true);
        if(newPoints.size() > 0)
            data.addAll(newPoints);
    }

    public static void symmetrizeFunction(List data, double symmetryPoint)
    {
        List newPoints = new ArrayList();
        Iterator i = data.iterator();
        do
        {
            if(!i.hasNext())
                break;
            Point2D point = (Point2D)i.next();
            double x = symmetryPoint + (symmetryPoint - point.getX());
            if(!pointXExistsInList(data, x))
                if(point instanceof Point3D)
                    newPoints.add(new Point3D(x, point.getY(), ((Point3D)point).getRZ()));
                else
                    newPoints.add(new Point2D(x, point.getY()));
        } while(true);
        if(newPoints.size() > 0)
            data.addAll(newPoints);
    }

    private static boolean pointXExistsInList(List data, double x)
    {
        boolean exists = false;
        Iterator i = data.iterator();
        do
        {
            if(!i.hasNext() || exists)
                break;
            Point2D point = (Point2D)i.next();
            if(point.getX() == x)
                exists = true;
        } while(true);
        return exists;
    }
}
