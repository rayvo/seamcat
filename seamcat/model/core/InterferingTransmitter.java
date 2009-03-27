// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:27 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   InterferingTransmitter.java

package org.seamcat.model.core;

import org.apache.log4j.Logger;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.*;
import org.seamcat.model.Transmitter;
import org.seamcat.model.technical.exception.GeometricException;
import org.w3c.dom.*;

public class InterferingTransmitter extends Transmitter
{

    public InterferingTransmitter(Element element)
    {
        super((Element)element.getElementsByTagName("transmitter").item(0));
        nbActiveTx = 1;
        rsimu = 1.0D;
        frequency = new ConstantDistribution(900D);
        activity = new ConstantFunction(0.0D);
        try
        {
            setTransProb(Double.parseDouble(element.getAttribute("transProb")));
            setDensActiveTx(Double.parseDouble(element.getAttribute("densActiveTx")));
            setItTrialFrequency(Double.parseDouble(element.getAttribute("itTrialFrequency")));
            setRsimu(Double.parseDouble(element.getAttribute("rsimu")));
            setRefBandwidth(Double.parseDouble(element.getAttribute("refBandwidth")));
            setNbActiveTx(Integer.parseInt(element.getAttribute("nbActiveTx")));
            setTime(Double.parseDouble(element.getAttribute("time")));
        }
        catch(NumberFormatException ex)
        {
            LOG.warn("Error loading attributes of Interfering Transmitter");
        }
        try
        {
            NodeList nl = element.getElementsByTagName("activity");
            if(((Element)nl.item(0).getFirstChild().getFirstChild()).getNodeName().equalsIgnoreCase("discretefunction"))
                setActivity(new DiscreteFunction((Element)nl.item(0).getFirstChild()));
            else
                setActivity(new ConstantFunction((Element)nl.item(0).getFirstChild().getFirstChild()));
        }
        catch(Exception ex1)
        {
            LOG.warn("Error loading activity function of Interfering Transmitter");
        }
        try
        {
            setFrequency(Distribution.create((Element)element.getElementsByTagName("frequency").item(0).getFirstChild()));
        }
        catch(Exception ex2)
        {
            LOG.warn("Error loading frequency function of Interfering Transmitter");
        }
    }

    public InterferingTransmitter()
    {
        nbActiveTx = 1;
        rsimu = 1.0D;
        frequency = new ConstantDistribution(900D);
        activity = new ConstantFunction(0.0D);
        setTransProb(0.0D);
        setTime(0.0D);
        setNbActiveTx(0);
        setDensActiveTx(0.0D);
        setItTrialFrequency(0.0D);
        setRsimu(0.0D);
    }

    public InterferingTransmitter(Transmitter t)
    {
        super(t);
        nbActiveTx = 1;
        rsimu = 1.0D;
        frequency = new ConstantDistribution(900D);
        activity = new ConstantFunction(0.0D);
        if(t instanceof InterferingTransmitter)
        {
            InterferingTransmitter it = (InterferingTransmitter)t;
            setFrequency(Distribution.create(it.getFrequency()));
            setTransProb(it.getTransProb());
            setTime(it.getTime());
            setNbActiveTx(it.getNbActiveTx());
            setDensActiveTx(it.getDensActiveTx());
            setRMax(it.getRMax());
            setActivity(Function.create(it.getActivity()));
            setUnwantedEmissions(new DiscreteFunction2((DiscreteFunction2)t.getUnwantedEmissions()));
        }
    }

    public double getTransProb()
    {
        return transProb;
    }

    public void setTransProb(double transProb)
    {
        this.transProb = transProb;
    }

    public double getTime()
    {
        return time;
    }

    public void setTime(double time)
    {
        this.time = time;
    }

    public int getNbActiveTx()
    {
        return nbActiveTx;
    }

    public void setNbActiveTx(int nbActiveTx)
    {
        this.nbActiveTx = nbActiveTx;
    }

    public double getDensActiveTx()
    {
        return densActiveTx;
    }

    public void setDensActiveTx(double densActiveTx)
    {
        this.densActiveTx = densActiveTx;
    }

    public double getItTrialFrequency()
    {
        return itTrialFrequency;
    }

    public void setItTrialFrequency(double itTrialFrequency)
    {
        this.itTrialFrequency = itTrialFrequency;
    }

    public double getRsimu()
    {
        return rsimu;
    }

    public void setRsimu(double _rsimu)
    {
        rsimu = _rsimu;
    }

    public double getRefBandwidth()
    {
        return refBandwidth;
    }

    public void setRefBandwidth(double refBandwidth)
    {
        this.refBandwidth = refBandwidth;
    }

    public Distribution getFrequency()
    {
        return frequency;
    }

    public void setFrequency(Distribution frequency)
    {
        this.frequency = frequency;
        nodeAttributeIsDirty = true;
    }

    public Function getActivity()
    {
        return activity;
    }

    public void setActivity(Function activity)
    {
        this.activity = activity;
        nodeAttributeIsDirty = true;
    }

    public void itSimulationRadius(double protectionDistance)
        throws GeometricException
    {
        double rNbActive = getNbActiveTx();
        double rDensIt = getDensActiveTx();
        double rTransProb = getTransProb();
        double rTime = getTime();
        double rActivity;
        try
        {
            rActivity = getActivity().evaluate(rTime);
        }
        catch(Exception e)
        {
            throw new GeometricException();
        }
        double rDensActive = rDensIt * rTransProb * rActivity;
        double rRsimu;
        try
        {
            rRsimu = Math.sqrt(rNbActive / (3.1415926535897931D * rDensActive) + protectionDistance * protectionDistance);
        }
        catch(Exception e)
        {
            throw new GeometricException();
        }
        setRsimu(rRsimu);
    }

    public void coverageRadiusTrafficIt()
        throws GeometricException
    {
        double rDens = getDens();
        double rFreqCluster = getFreqCluster();
        double rNbChannels = getNumberOfChannels();
        double rNbUsers = getNumberOfUsersPerChannel();
        try
        {
            double rRmax = Math.sqrt((rNbChannels * rNbUsers) / (3.1415926535897931D * rDens * rFreqCluster));
            setRMax(rRmax);
            setCoverageRadius(rRmax);
        }
        catch(Exception e)
        {
            throw new GeometricException();
        }
    }

    public void coverageRadiusUserIt()
    {
        setRMax(getCoverageRadius());
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("InterferingTransmitter");
        element.setAttribute("transProb", Double.toString(getTransProb()));
        element.setAttribute("densActiveTx", Double.toString(getDensActiveTx()));
        element.setAttribute("itTrialFrequency", Double.toString(getItTrialFrequency()));
        element.setAttribute("rsimu", Double.toString(getRsimu()));
        element.setAttribute("refBandwidth", Double.toString(getRefBandwidth()));
        element.setAttribute("nbActiveTx", Integer.toString(getNbActiveTx()));
        element.setAttribute("time", Double.toString(getTime()));
        Element activityElement = doc.createElement("activity");
        Element functionElement = doc.createElement("function");
        functionElement.appendChild(getActivity().toElement(doc));
        activityElement.appendChild(functionElement);
        element.appendChild(activityElement);
        Element frequencyElement = doc.createElement("frequency");
        frequencyElement.appendChild(getFrequency().toElement(doc));
        element.appendChild(frequencyElement);
        element.appendChild(super.toElement(doc));
        return element;
    }

    protected void initNodeAttributes()
    {
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/core/InterferingTransmitter);
    private double transProb;
    private double time;
    private int nbActiveTx;
    private double densActiveTx;
    private double itTrialFrequency;
    private double rsimu;
    private double refBandwidth;
    private Distribution frequency;
    private Function activity;

}