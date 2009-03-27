// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Distribution.java

package org.seamcat.distribution;

import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Point2D;
import org.seamcat.model.plugin.PluginDistribution;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.distribution:
//            ContinousDistribution, StairDistribution, ConstantDistribution, UniformDistribution, 
//            GaussianDistribution, RayleighDistribution, DiscreteUniformDistribution, UniformPolarAngleDistribution, 
//            UniformPolarDistanceDistribution, Bounds

public abstract class Distribution
    implements PluginDistribution
{
    public static class DummyDistribution extends Distribution
    {

        public String toString()
        {
            return "Dummy Distribution";
        }

        public double trial()
        {
            throw new UnsupportedOperationException("Cannot run trial() on a DummyDistribution");
        }

        public Bounds getBounds()
        {
            return new Bounds(0.0D, 0.0D, true);
        }

        public org.seamcat.model.plugin.PluginDistribution.DistributionType getDistributionType()
        {
            return null;
        }

        public DummyDistribution()
        {
        }
    }


    private Distribution()
    {
        type = 0;
        constant = 33D;
        mean = 0.0D;
        stdDev = 0.0D;
        min = 0.0D;
        max = 1.0D;
        maxDistance = 1.0D;
        maxAngle = 360D;
        step = 0.20000000000000001D;
    }

    public abstract Bounds getBounds();

    public abstract double trial();

    public abstract String toString();

    public Distribution(int type)
    {
        this.type = 0;
        constant = 33D;
        mean = 0.0D;
        stdDev = 0.0D;
        min = 0.0D;
        max = 1.0D;
        maxDistance = 1.0D;
        maxAngle = 360D;
        step = 0.20000000000000001D;
        setType(type);
    }

    public double getConstant()
    {
        return constant;
    }

    public double getMax()
    {
        return max;
    }

    public double getMaxAngle()
    {
        return maxAngle;
    }

    public double getMaxDistance()
    {
        return maxDistance;
    }

    public double getMean()
    {
        return mean;
    }

    public double getMin()
    {
        return min;
    }

    public double getStdDev()
    {
        return stdDev;
    }

    public double getStep()
    {
        return step;
    }

    public int getType()
    {
        return type;
    }

    public void setConstant(double constant)
    {
        this.constant = constant;
    }

    public void setMax(double max)
    {
        this.max = max;
    }

    public void setMaxAngle(double maxAngle)
    {
        this.maxAngle = maxAngle;
    }

    public void setMaxDistance(double maxDistance)
    {
        this.maxDistance = maxDistance;
    }

    public void setMean(double mean)
    {
        this.mean = mean;
    }

    public void setMin(double min)
    {
        this.min = min;
    }

    public void setStdDev(double stdDev)
    {
        this.stdDev = stdDev;
    }

    public void setStep(double step)
    {
        this.step = step;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("distribution");
        element.setAttribute("type", String.valueOf(getType()));
        element.setAttribute("constant", String.valueOf(getConstant()));
        element.setAttribute("mean", String.valueOf(getMean()));
        element.setAttribute("std-dev", String.valueOf(getStdDev()));
        element.setAttribute("min", String.valueOf(getMin()));
        element.setAttribute("max", String.valueOf(getMax()));
        element.setAttribute("max-distance", String.valueOf(getMaxDistance()));
        element.setAttribute("max-angle", String.valueOf(getMaxAngle()));
        element.setAttribute("step", String.valueOf(getStep()));
        switch(getType())
        {
        default:
            break;

        case 1: // '\001'
            Element userDefined = doc.createElement("user-defined");
            userDefined.appendChild(((ContinousDistribution)this).getCdf().toElement(doc));
            element.appendChild(userDefined);
            break;

        case 7: // '\007'
            Element userDefinedStair = doc.createElement("user-defined-stair");
            StairDistribution sd = (StairDistribution)this;
            Point2D points[] = sd.getPoints();
            for(int x = 0; x < points.length; x++)
                userDefinedStair.appendChild(points[x].toElement(doc));

            element.appendChild(userDefinedStair);
            break;
        }
        Element name = doc.createElement("description");
        name.appendChild(doc.createCDATASection(toString()));
        element.appendChild(name);
        return element;
    }

    public static Distribution create(Distribution d)
    {
        return create(d, null);
    }

    public static Distribution create(Distribution d, Element element)
    {
        Distribution newObj;
        switch(d.getType())
        {
        case 0: // '\0'
            newObj = new ConstantDistribution();
            break;

        case 2: // '\002'
            newObj = new UniformDistribution();
            break;

        case 3: // '\003'
            newObj = new GaussianDistribution();
            break;

        case 4: // '\004'
            newObj = new RayleighDistribution();
            break;

        case 8: // '\b'
            newObj = new DiscreteUniformDistribution();
            break;

        case 6: // '\006'
            newObj = new UniformPolarAngleDistribution();
            break;

        case 5: // '\005'
            newObj = new UniformPolarDistanceDistribution();
            break;

        case 1: // '\001'
            if(element != null)
                newObj = new ContinousDistribution(new DiscreteFunction(element));
            else
                newObj = new ContinousDistribution();
            break;

        case 7: // '\007'
            newObj = new StairDistribution();
            if(element != null)
            {
                NodeList nlStair = element.getElementsByTagName("user-defined-stair");
                if(nlStair == null || nlStair.getLength() <= 0)
                    break;
                StairDistribution sd = (StairDistribution)newObj;
                NodeList nl = ((Element)nlStair.item(0)).getElementsByTagName("point2d");
                int x = 0;
                for(int size = nl.getLength(); x < size; x++)
                    sd.add(new Point2D((Element)nl.item(x)));

                break;
            }
            StairDistribution sd = (StairDistribution)newObj;
            Point2D arr$[] = ((StairDistribution)d).getPoints();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                Point2D p = arr$[i$];
                sd.add(new Point2D(p.getX(), p.getY()));
            }

            break;

        default:
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown Distribution class type: ").append(d.getType()).toString());
        }
        newObj.setConstant(d.getConstant());
        newObj.setMean(d.getMean());
        newObj.setStdDev(d.getStdDev());
        newObj.setMin(d.getMin());
        newObj.setMax(d.getMax());
        newObj.setMaxDistance(d.getMaxDistance());
        newObj.setMaxAngle(d.getMaxAngle());
        newObj.setStep(d.getStep());
        return newObj;
    }

    public static Distribution create(Element element)
    {
        Distribution newObj = new DummyDistribution();
        newObj.setConstant(Double.parseDouble(element.getAttribute("constant")));
        newObj.setType(Integer.parseInt(element.getAttribute("type").trim()));
        newObj.setMean(Double.parseDouble(element.getAttribute("mean")));
        newObj.setStdDev(Double.parseDouble(element.getAttribute("std-dev")));
        newObj.setMin(Double.parseDouble(element.getAttribute("min")));
        newObj.setMax(Double.parseDouble(element.getAttribute("max")));
        newObj.setMaxDistance(Double.parseDouble(element.getAttribute("max-distance")));
        newObj.setMaxAngle(Double.parseDouble(element.getAttribute("max-angle")));
        newObj.setStep(Double.parseDouble(element.getAttribute("step")));
        return create(newObj, element);
    }


    public static final int TYPE_CONSTANT = 0;
    public static final int TYPE_USER_DEFINED = 1;
    public static final int TYPE_UNIFORM = 2;
    public static final int TYPE_GAUSSIAN = 3;
    public static final int TYPE_RAYLEIGH = 4;
    public static final int TYPE_UNIFORM_POLAR_DISTANCE = 5;
    public static final int TYPE_UNIFORM_POLAR_ANGLE = 6;
    public static final int TYPE_USER_DEFINED_STAIR = 7;
    public static final int TYPE_DISCRETE_UNIFORM = 8;
    private int type;
    protected double constant;
    protected double mean;
    protected double stdDev;
    protected double min;
    protected double max;
    protected double maxDistance;
    protected double maxAngle;
    protected double step;
}