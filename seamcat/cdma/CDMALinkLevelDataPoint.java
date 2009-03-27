// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMALinkLevelDataPoint.java

package org.seamcat.cdma;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CDMALinkLevelDataPoint
{

    public CDMALinkLevelDataPoint(double frequency, int path, double geometry, double speed, 
            double ecior)
    {
        this.frequency = frequency;
        this.path = path;
        this.geometry = geometry;
        this.speed = speed;
        this.ecior = ecior;
    }

    public CDMALinkLevelDataPoint(CDMALinkLevelDataPoint pt)
    {
        frequency = pt.frequency;
        path = pt.path;
        geometry = pt.geometry;
        speed = pt.speed;
        ecior = pt.ecior;
    }

    public CDMALinkLevelDataPoint(Element element, double frequency, int path)
    {
        try
        {
            geometry = Double.parseDouble(element.getAttribute("geo"));
        }
        catch(NumberFormatException e) { }
        try
        {
            speed = Double.parseDouble(element.getAttribute("speed"));
        }
        catch(NumberFormatException e) { }
        try
        {
            ecior = Double.parseDouble(element.getAttribute("ecior"));
        }
        catch(NumberFormatException e) { }
        this.frequency = frequency;
        this.path = path;
    }

    public double extrapolateSpeed(double freq)
    {
        return speed * (frequency / freq);
    }

    public boolean equals(Object obj)
    {
        if(!(obj instanceof CDMALinkLevelDataPoint))
        {
            return false;
        } else
        {
            CDMALinkLevelDataPoint p = (CDMALinkLevelDataPoint)obj;
            return getPath() == p.getPath() && (int)getFrequency() == (int)p.getFrequency() && (int)getSpeed() == (int)p.getSpeed() && getGeometry() == p.getGeometry();
        }
    }

    public int getPath()
    {
        return path;
    }

    public double getFrequency()
    {
        return frequency;
    }

    public double getEcIor()
    {
        return ecior;
    }

    public double getEbNo()
    {
        return ecior;
    }

    public double getGeometry()
    {
        double geo = Math.rint(geometry * (double)geometryPrecission) / (double)geometryPrecission;
        return geo;
    }

    public double getSpeed()
    {
        return speed;
    }

    public String toString()
    {
        return (new StringBuilder()).append(frequency).append("MHz, ").append(path).append("-path, ").append((int)geometry).append("dB Geometry, ").append(speed).append("km/h").toString();
    }

    public void setSpeed(double speed)
    {
        this.speed = speed;
    }

    public void setPath(int path)
    {
        this.path = path;
    }

    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }

    public void setEcior(double ecior)
    {
        this.ecior = ecior;
    }

    public void setGeometry(double geometry)
    {
        this.geometry = geometry;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("point");
        element.setAttribute("geo", String.valueOf(geometry));
        element.setAttribute("speed", String.valueOf(speed));
        element.setAttribute("ecior", Double.toString(ecior));
        return element;
    }

    private double frequency;
    private int path;
    private double geometry;
    private double speed;
    private double ecior;
    private static int geometryPrecission = 1;

}