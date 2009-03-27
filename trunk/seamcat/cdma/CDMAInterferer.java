// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAInterferer.java

package org.seamcat.cdma;

import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.function.*;
import org.seamcat.model.Antenna;
import org.seamcat.model.core.AntennaPattern;
import org.seamcat.model.technical.exception.ModelException;
import org.seamcat.model.technical.exception.PatternException;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.cdma:
//            CDMAElement, CDMASystem, CDMALink

public class CDMAInterferer extends CDMAElement
{

    public CDMAInterferer(double x, double y, double power, PropagationModel model, 
            double freq, double antHeight, Antenna _antenna, Function2 _unwantedEmission, Function _select, 
            CDMASystem system)
    {
        super(x, y, antHeight, system);
        propagationModel = null;
        usingFixedGain = false;
        translateAngles = false;
        txPower = power;
        propagationModel = model;
        frequency = freq;
        antennaHeight = antHeight;
        antenna = _antenna;
        unwantedEmission = _unwantedEmission;
        selectivity = _select;
        try
        {
            if(((Point2D)antenna.getHorizontalPattern().getPattern().getPointsList().get(0)).getX() == -180D)
                translateAngles = true;
        }
        catch(Exception e)
        {
            translateAngles = false;
        }
    }

    public CDMAInterferer(double x, double y, double power, PropagationModel model, 
            double freq, double antHeight, double _antennaGain, Function2 _unwantedEmission, 
            Function _select, CDMASystem system)
    {
        super(x, y, antHeight, system);
        propagationModel = null;
        usingFixedGain = false;
        translateAngles = false;
        txPower = power;
        propagationModel = model;
        frequency = freq;
        antennaHeight = antHeight;
        fixedGain = _antennaGain;
        usingFixedGain = true;
        unwantedEmission = _unwantedEmission;
        selectivity = _select;
    }

    public void setVictim(double x, double y, double height, double gain, double _victimFrequency, double referenceBandwith)
    {
        victimX = x;
        victimY = y;
        victimAntGain = gain;
        victimAntHeight = height;
        victimFrequency = _victimFrequency;
        victimRefBand = referenceBandwith;
        distance = Math.sqrt((locationX - x) * (locationX - x) + (locationY - y) * (locationY - y));
        if(usingFixedGain)
        {
            antennaGain = fixedGain;
        } else
        {
            double horiAngle = CDMALink.calculateKartesianAngle(locationX, locationY, x, y);
            double vertiAngle = CDMALink.calculateElevation(locationX, locationY, antennaHeight, x, y, height);
            if(translateAngles)
                horiAngle -= 180D;
            try
            {
                antennaGain = antenna.calculateGain(horiAngle, vertiAngle);
            }
            catch(PatternException e)
            {
                LOG.error("Error when calculating gain", e);
            }
        }
        try
        {
            pathLoss = propagationModel.evaluate(frequency, distance, antennaHeight, victimAntHeight);
            if(LOG.isDebugEnabled())
                LOG.debug((new StringBuilder()).append("PathLoss [").append(propagationModel.getClass().getSimpleName()).append("] = ").append(pathLoss).append(" [").append(frequency).append("MHz;").append(distance).append("km;").append(antennaHeight).append("TxHeight;").append(victimAntHeight).append("RxHeight]").toString());
        }
        catch(ModelException ex)
        {
            LOG.error("An Error occured", ex);
        }
    }

    public String toString()
    {
        return (new StringBuilder()).append("CDMA Interferer at (").append(locationX).append(", ").append(locationY).append(") : Transmit Power = ").append(txPower).append(" dBm; Frequency = ").append(frequency).append("MHz; Antenna Height = ").append(antennaHeight).append("m").toString();
    }

    public double calculateTotalInterference()
    {
        totalInterference = 10D * Math.log10(Math.pow(10D, calculateUnwantedEmission() / 10D) + Math.pow(10D, calculateSelectivity() / 10D));
        return totalInterference;
    }

    public double calculateSelectivity()
    {
        double bl = 0.0D;
        try
        {
            bl = selectivity.evaluate(victimFrequency - frequency);
        }
        catch(FunctionException ex1)
        {
            LOG.error("Error calculating selectivity function for CDMA interferer", ex1);
        }
        externalInterferenceBlocking = ((txPower - pathLoss) + antennaGain + victimAntGain) - bl;
        return externalInterferenceBlocking;
    }

    public double calculateUnwantedEmission()
    {
        double unw = 0.0D;
        try
        {
            unw = unwantedEmission.integrate(victimFrequency - frequency, victimRefBand);
        }
        catch(FunctionException ex1)
        {
            LOG.error("Error calculating unwanted emission for CDMA interferer", ex1);
        }
        externalInterferenceUnwanted = (txPower - pathLoss) + antennaGain + victimAntGain + unw;
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("iRSSUnwanted = ").append(externalInterferenceUnwanted).append(" dBm = txPower - pathLoss + antennaGain + victimAntGain + unw = ").append(txPower).append(" - ").append(pathLoss).append(" + ").append(antennaGain).append(" + ").append(victimAntGain).append(" + ").append(unw).toString());
        return externalInterferenceUnwanted;
    }

    public PropagationModel getPropagationModel()
    {
        return propagationModel;
    }

    public double getTxPower()
    {
        return txPower;
    }

    public void setTxPower(double txPower)
    {
        this.txPower = txPower;
    }

    public void setPropagationModel(PropagationModel propagationModel)
    {
        this.propagationModel = propagationModel;
    }

    public double getAntennaHeight()
    {
        return antennaHeight;
    }

    public void setAntennaHeight(double antennaHeight)
    {
        this.antennaHeight = antennaHeight;
    }

    public double getFrequency()
    {
        return frequency;
    }

    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }

    public double getPathLoss()
    {
        return pathLoss;
    }

    public void setPathLoss(double pathLoss)
    {
        this.pathLoss = pathLoss;
    }

    public double getBlocking()
    {
        return externalInterferenceBlocking;
    }

    public void setBlocking(double blocking)
    {
        externalInterferenceBlocking = blocking;
    }

    public double getUnwanted()
    {
        return externalInterferenceUnwanted;
    }

    public void setUnwanted(double unwanted)
    {
        externalInterferenceUnwanted = unwanted;
    }

    public double getFixedAntennaGain()
    {
        if(usingFixedGain)
            return fixedGain;
        else
            return antenna.getPeakGain();
    }

    public double calculateAntennaGainTo(double angle, double elevation)
    {
        throw new UnsupportedOperationException("Gain should not be calculated for CDMA Interferer");
    }

    public double getReferenceBandwidth()
    {
        throw new UnsupportedOperationException("Reference bandwith should not be used for CDMA Interferer");
    }

    public Antenna getAntenna()
    {
        return antenna;
    }

    public void setAntenna(Antenna antenna)
    {
        this.antenna = antenna;
    }

    public boolean isUsingFixedGain()
    {
        return usingFixedGain;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/CDMAInterferer);
    private PropagationModel propagationModel;
    private double txPower;
    private double frequency;
    private double antennaHeight;
    private Antenna antenna;
    private boolean usingFixedGain;
    private double fixedGain;
    private double antennaGain;
    private Function2 unwantedEmission;
    private Function selectivity;
    private double victimX;
    private double victimY;
    private double victimAntGain;
    private double victimAntHeight;
    private double victimFrequency;
    private double victimRefBand;
    private double pathLoss;
    private double distance;
    private boolean translateAngles;

}