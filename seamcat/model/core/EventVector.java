// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EventVector.java

package org.seamcat.model.core;

import java.util.Arrays;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.distribution.UniformDistribution;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.statistics.NormalDistribution;
import org.w3c.dom.*;

public class EventVector
{

    public EventVector(Element element)
    {
        this(Integer.parseInt(element.getAttribute("size")));
        setEventIndex(Integer.parseInt(element.getAttribute("eventIndex")));
        setShouldBeCalculated(Boolean.valueOf(element.getAttribute("shouldBeCalculated")).booleanValue());
        NodeList nl = element.getElementsByTagName("event");
        int x = 0;
        for(int size = nl.getLength(); x < size; x++)
            events[x] = Double.parseDouble(((Element)nl.item(x)).getAttribute("y"));

    }

    public Element toElement(Document doc)
    {
        return toElement(doc, true);
    }

    public Element toElement(Document doc, boolean expand)
    {
        Element element = doc.createElement("EventVector");
        element.setAttribute("eventIndex", Integer.toString(getEventIndex()));
        element.setAttribute("size", Integer.toString(events.length));
        element.setAttribute("shouldBeCalculated", Boolean.toString(isShouldBeCalculated()));
        double mean = Mathematics.getAverage(events);
        double stddev = Mathematics.getStdDev(events, mean);
        element.setAttribute("mean", Double.toString(mean));
        element.setAttribute("stddev", Double.toString(stddev));
        int step = 1;
        if(expand)
        {
            NormalDistribution normDist = new NormalDistribution(mean, stddev);
            for(int i = 0; i < events.length; i += step)
            {
                Element eventElement = doc.createElement("event");
                eventElement.setAttribute("x", Double.toString(normDist.getCDF(events[i])));
                eventElement.setAttribute("y", Double.toString(events[i]));
                element.appendChild(eventElement);
            }

        }
        return element;
    }

    public EventVector()
    {
        shouldBeCalculated = true;
        modifiedByPlugin = false;
        events = new double[0];
        eventIndex = 0;
    }

    public EventVector(boolean _shouldBeCalculated)
    {
        shouldBeCalculated = true;
        modifiedByPlugin = false;
        shouldBeCalculated = _shouldBeCalculated;
        events = new double[0];
        eventIndex = 0;
    }

    public EventVector(int eventCount)
    {
        shouldBeCalculated = true;
        modifiedByPlugin = false;
        events = new double[eventCount];
        eventIndex = 0;
    }

    public EventVector(EventVector eventVector)
    {
        this(eventVector.isShouldBeCalculated());
        events = eventVector.getEvents();
        eventIndex = eventVector.eventIndex;
    }

    public double[] getEvents()
    {
        return events;
    }

    public int getEventIndex()
    {
        return eventIndex;
    }

    public void setEvents(double events[])
    {
        if(!shouldBeCalculated)
        {
            throw new IllegalStateException("No events should be set on this Event Vector");
        } else
        {
            this.events = events;
            return;
        }
    }

    public boolean getShouldBeCalculated()
    {
        return shouldBeCalculated;
    }

    public boolean isShouldBeCalculated()
    {
        return shouldBeCalculated;
    }

    public UniformDistribution getU()
    {
        return u;
    }

    public void setShouldBeCalculated(boolean shouldBeCalculated)
    {
        this.shouldBeCalculated = shouldBeCalculated;
    }

    public void setEventIndex(int eventIndex)
    {
        this.eventIndex = eventIndex;
    }

    public void setU(UniformDistribution u)
    {
        u = u;
    }

    public int size()
    {
        return events.length;
    }

    public double mean()
    {
        int i = 0;
        int stop = size();
        double rSum = 0.0D;
        for(; i < stop; i++)
            rSum += events[i];

        double rMean = rSum / (double)size();
        return rMean;
    }

    public double variance()
    {
        double rSum2 = 0.0D;
        double rMean = mean();
        int i = 0;
        for(int size = size(); i < size; i++)
            rSum2 += (events[i] - rMean) * (events[i] - rMean);

        rSum2 /= size();
        double rVariance = rSum2;
        return rVariance;
    }

    public static double correlation(EventVector v1, EventVector v2)
    {
        double rVarianceV1 = 0.0D;
        double rVarianceV2 = 0.0D;
        if(v1.size() != 0)
        {
            rVarianceV1 = v1.variance();
            rVarianceV2 = v2.variance();
            double rMeanV1 = v1.mean();
            double rMeanV2 = v2.mean();
            int k = 0;
            double rSumCorr = 0.0D;
            for(; k < v1.size(); k++)
                rSumCorr += (v1.getEvents()[k] - rMeanV1) * (v2.getEvents()[k] - rMeanV2);

            if(rVarianceV1 == 0.0D && rVarianceV2 == 0.0D)
                return 1.0D;
            if(rVarianceV1 != 0.0D && rVarianceV2 == 0.0D || rVarianceV1 == 0.0D && rVarianceV2 != 0.0D)
            {
                return 0.0D;
            } else
            {
                double rSize = v1.size();
                double rCorr = (1.0D / rSize) * (rSumCorr / (Math.sqrt(rVarianceV1) * Math.sqrt(rVarianceV2)));
                return Math.abs(rCorr);
            }
        } else
        {
            return 1.7976931348623157E+308D;
        }
    }

    public void clear()
    {
        if(!shouldBeCalculated)
        {
            throw new IllegalStateException("No events exists for this Event Vector");
        } else
        {
            events = new double[0];
            modifiedByPlugin = false;
            eventIndex = 0;
            return;
        }
    }

    public double kSTest(EventVector v1, EventVector v2)
    {
        int j1 = 0;
        int j2 = 0;
        int iN1 = v1.size();
        int iN2 = v2.size();
        double rEn1 = iN1;
        double rEn2 = iN2;
        double rD = 0.0D;
        double rFn1 = 0.0D;
        double rFn2 = 0.0D;
        v1.sort();
        v2.sort();
        do
        {
            if(j1 >= iN1 || j2 >= iN2)
                break;
            double rD1;
            double rD2;
            if((rD1 = v1.getEvents()[j1]) <= (rD2 = v2.getEvents()[j2]))
                rFn1 = (double)(j1++) / rEn1;
            if(rD2 <= rD1)
                rFn2 = (double)(j2++) / rEn2;
            double rDt;
            if((rDt = Math.abs(rFn1 - rFn2)) > rD)
                rD = rDt;
        } while(true);
        double rEn = Math.sqrt((rEn1 * rEn2) / (rEn1 + rEn2));
        double rProb = kSProb((rEn + 0.12D + 0.11D / rEn) * rD);
        v1.reset();
        v2.reset();
        return rProb;
    }

    public double kSProb(double rLambda)
    {
        double rEps1 = 0.001D;
        double rEps2 = 1E-008D;
        double rFac = 2D;
        double rSum = 0.0D;
        double rTermBf = 0.0D;
        double rA2 = -2D * rLambda * rLambda;
        for(int i = 1; i <= 100; i++)
        {
            double rTerm = rFac * Math.exp(rA2 * (double)i * (double)i);
            rSum += rTerm;
            if(Math.abs(rTerm) <= 0.001D * rTermBf || Math.abs(rTerm) <= 1E-008D * rSum)
                return rSum;
            rFac = -rFac;
            rTermBf = Math.abs(rTerm);
        }

        return 1.0D;
    }

    public void reset()
    {
        events = orig_events;
    }

    public void sort()
    {
        orig_events = (double[])events.clone();
        Arrays.sort(events);
    }

    public double max()
    {
        double max = 4.9406564584124654E-324D;
        for(int i = 0; i < events.length; i++)
            if(events[i] > max)
                max = events[i];

        return max;
    }

    public double min()
    {
        double min = 1.7976931348623157E+308D;
        for(int i = 0; i < events.length; i++)
            if(events[i] < min)
                min = events[i];

        return min;
    }

    public void addEvent(double event)
    {
        if(!shouldBeCalculated)
        {
            throw new IllegalStateException("No events should be calculated for this Event Vector");
        } else
        {
            events[eventIndex++] = event;
            return;
        }
    }

    public double checkStability(int maxEvents, int dN)
    {
        EventVector v = new EventVector();
        int iSize = size() - dN;
        for(int k = 0; k < iSize; k++)
            v.addEvent(getEvents()[k]);

        double rResulKSTest = kSTest(this, v);
        return rResulKSTest;
    }

    public void reserve(int size)
    {
        if(!shouldBeCalculated)
            throw new IllegalStateException("No events should exist for this Event Vector");
        if(events == null)
            events = new double[size];
        else
        if(events.length < size)
        {
            double newEvents[] = new double[size];
            System.arraycopy(events, 0, newEvents, 0, events.length);
            events = newEvents;
        } else
        if(events.length > size)
        {
            double newEvents[] = new double[size];
            System.arraycopy(events, 0, newEvents, 0, size);
            events = newEvents;
        }
    }

    public void trimToSize()
    {
        if(events.length != eventIndex)
        {
            double trimmedEvents[] = new double[eventIndex];
            System.arraycopy(events, 0, trimmedEvents, 0, eventIndex);
            events = trimmedEvents;
        }
    }

    public void generateFromVector(EventVector v, int NbEvents)
    {
        UniformDistribution u = new UniformDistribution(0.0D, 1.0D);
        int i = 0;
        int iMaxSamples = v.size();
        for(i = 0; i < NbEvents; i++)
        {
            int iProb = (int)u.trial() * iMaxSamples;
            double rEvent = v.getEvents()[iProb];
            addEvent(rEvent);
        }

    }

    public void generateFromDistrib(Distribution distrib, int NbEvents)
    {
        double rEvent = 0.0D;
        int i = 0;
        reserve(NbEvents);
        for(i = 0; i < NbEvents; i++)
        {
            rEvent = distrib.trial();
            addEvent(rEvent);
        }

    }

    public boolean isModifiedByPlugin()
    {
        return modifiedByPlugin;
    }

    public void setModifiedByPlugin(boolean modifiedByPlugin)
    {
        this.modifiedByPlugin = modifiedByPlugin;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/core/EventVector);
    private double events[];
    private double orig_events[];
    private int eventIndex;
    private boolean shouldBeCalculated;
    private boolean modifiedByPlugin;
    private static UniformDistribution u = new UniformDistribution(0.0D, 1.0D);

}