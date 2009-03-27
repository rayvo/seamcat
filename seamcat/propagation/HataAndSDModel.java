// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HataAndSDModel.java

package org.seamcat.propagation;

import org.seamcat.distribution.GaussianDistribution;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.propagation:
//            BuiltInModel

public abstract class HataAndSDModel extends BuiltInModel
{
    protected static final class LocalEnvCorrections
    {

        public double rMedianLoss;
        public double rStdDev;

        public LocalEnvCorrections(double rMedianLoss, double rStdDev)
        {
            this.rMedianLoss = rMedianLoss;
            this.rStdDev = rStdDev;
        }
    }


    protected HataAndSDModel()
    {
        wiLoss = 5D;
        wiStdDev = 10D;
        weLoss = 10D;
        weStdDev = 5D;
        floorLoss = 18.300000000000001D;
        roomSize = 4D;
        floorHeight = 3D;
        empiricalParameter = 0.46000000000000002D;
    }

    protected HataAndSDModel(HataAndSDModel p)
    {
        this(p.getTxLocalEnv(), p.getRxLocalEnv(), p.getGeneralEnv(), p.getPropagEnv(), p.getWiLoss(), p.getWiStdDev(), p.getWeLoss(), p.getWeStdDev(), p.getFloorLoss(), p.getFloorHeight(), p.getRoomSize(), p.getEmpiricalParameter());
        setVariationsSelected(p.getVariationsSelected());
        setMedianSelected(p.getMedianSelected());
    }

    protected HataAndSDModel(int txLocalEnv, int rxLocalEnv, int generalEnv, int propagEnv, double rWiLoss, double rWiStdDev, double rWeLoss, double rWeStdDev, double rFloorLoss, 
            double rFloorHeight, double rRoomSize, double rB)
    {
        super(txLocalEnv, rxLocalEnv, generalEnv, propagEnv);
        wiLoss = 5D;
        wiStdDev = 10D;
        weLoss = 10D;
        weStdDev = 5D;
        floorLoss = 18.300000000000001D;
        roomSize = 4D;
        floorHeight = 3D;
        empiricalParameter = 0.46000000000000002D;
        setWiLoss(rWiLoss);
        setWiStdDev(rWiStdDev);
        setWeLoss(rWeLoss);
        setWeStdDev(rWeStdDev);
        setFloorLoss(rFloorLoss);
        setFloorHeight(rFloorHeight);
        setRoomSize(rRoomSize);
        setEmpiricalParameter(rB);
    }

    protected HataAndSDModel(Element element)
    {
        super((Element)element.getElementsByTagName("BuiltInModel").item(0));
        wiLoss = 5D;
        wiStdDev = 10D;
        weLoss = 10D;
        weStdDev = 5D;
        floorLoss = 18.300000000000001D;
        roomSize = 4D;
        floorHeight = 3D;
        empiricalParameter = 0.46000000000000002D;
        empiricalParameter = Double.parseDouble(element.getAttribute("b"));
        setFloorHeight(Double.parseDouble(element.getAttribute("floorHeight")));
        setFloorLoss(Double.parseDouble(element.getAttribute("floorLoss")));
        setRoomSize(Double.parseDouble(element.getAttribute("roomSize")));
        setWeLoss(Double.parseDouble(element.getAttribute("weLoss")));
        setWeStdDev(Double.parseDouble(element.getAttribute("weStdDev")));
        setWiLoss(Double.parseDouble(element.getAttribute("wiLoss")));
        setWiStdDev(Double.parseDouble(element.getAttribute("wiStdDev")));
    }

    public double getWiLoss()
    {
        return wiLoss;
    }

    public void setWiLoss(double wiLoss)
    {
        this.wiLoss = wiLoss;
    }

    public double getWiStdDev()
    {
        return wiStdDev;
    }

    public void setWiStdDev(double wiStdDev)
    {
        this.wiStdDev = wiStdDev;
    }

    public double getWeLoss()
    {
        return weLoss;
    }

    public void setWeLoss(double weLoss)
    {
        this.weLoss = weLoss;
    }

    public double getWeStdDev()
    {
        return weStdDev;
    }

    public void setWeStdDev(double weStdDev)
    {
        this.weStdDev = weStdDev;
        setStdDev(weStdDev);
    }

    public double getFloorLoss()
    {
        return floorLoss;
    }

    public void setFloorLoss(double floorLoss)
    {
        this.floorLoss = floorLoss;
    }

    public double getRoomSize()
    {
        return roomSize;
    }

    public void setRoomSize(double roomSize)
    {
        this.roomSize = roomSize;
    }

    public double getFloorHeight()
    {
        return floorHeight;
    }

    public void setFloorHeight(double floorHeight)
    {
        this.floorHeight = floorHeight;
    }

    public double getEmpiricalParameter()
    {
        return empiricalParameter;
    }

    public void setEmpiricalParameter(double b)
    {
        empiricalParameter = b;
    }

    public boolean isSameBuilding(double rDist)
    {
        double rSameBuildingProb = 0.0D;
        boolean bSameBuilding;
        if(rDist < 0.02D)
            bSameBuilding = true;
        else
        if(rDist < 0.050000000000000003D)
        {
            rSameBuildingProb = (0.050000000000000003D - rDist) / 0.029999999999999999D;
            bSameBuilding = getSameBuildingDistrib().trial() < rSameBuildingProb;
        } else
        {
            bSameBuilding = false;
        }
        return bSameBuilding;
    }

    protected double generalEnvCorrections(double rMedianLoss, double rFreq, double rDist)
    {
        if(rDist >= 0.10000000000000001D)
            if(getGeneralEnv() == 1)
                rMedianLoss += -2D * Math.pow(Math.log10(Math.min(Math.max(150D, rFreq), 2000D) / 28D), 2D) - 5.4000000000000004D;
            else
            if(getGeneralEnv() == 2)
                rMedianLoss += (-4.7800000000000002D * Math.pow(Math.log10(Math.min(Math.max(150D, rFreq), 2000D)), 2D) + 18.329999999999998D * Math.log10(Math.min(Math.max(150D, rFreq), 2000D))) - 40.939999999999998D;
        return rMedianLoss;
    }

    protected double variationsStdDev(double rDist)
    {
        int propagEnv = getPropagEnv();
        double rStdDev;
        if(rDist <= 0.040000000000000001D)
            rStdDev = 3.5D;
        else
        if(rDist <= 0.10000000000000001D)
        {
            if(propagEnv == 0)
                rStdDev = 3.5D + 141.66666666666666D * (rDist - 0.040000000000000001D);
            else
            if(propagEnv == 1)
                rStdDev = 3.5D + 224.99999999999997D * (rDist - 0.040000000000000001D);
            else
                throw new IllegalArgumentException("Unsupported propagation environment.");
        } else
        if(rDist <= 0.20000000000000001D)
        {
            if(propagEnv == 0)
                rStdDev = 12D;
            else
            if(propagEnv == 1)
                rStdDev = 17D;
            else
                throw new IllegalArgumentException("Unsupported propagation environment.");
        } else
        if(rDist <= 0.59999999999999998D)
        {
            if(propagEnv == 0)
                rStdDev = 12D + -7.5000000000000009D * (rDist - 0.20000000000000001D);
            else
            if(propagEnv == 1)
                rStdDev = 17D + -20D * (rDist - 0.20000000000000001D);
            else
                throw new IllegalArgumentException("Unsupported propagation environment.");
        } else
        {
            rStdDev = 9D;
        }
        return rStdDev;
    }

    protected LocalEnvCorrections localEnvCorrections(LocalEnvCorrections localEnvironmentCorrections, double frequency, double distance, double heightOfTransmitter, 
            double heightOfReceiver)
    {
        double rWeStdDev = getWeStdDev();
        int TxLocalEnv = getTxLocalEnv();
        int RxLocalEnv = getRxLocalEnv();
        if(RxLocalEnv == 0 && TxLocalEnv == 0)
        {
            if(isSameBuilding(distance))
            {
                double rK = Math.floor(Math.abs(heightOfTransmitter - heightOfReceiver) / getFloorHeight());
                localEnvironmentCorrections.rMedianLoss = -27.600000000000001D + 20D * Math.log10(1000D * distance) + 20D * Math.log10(frequency) + Math.floor((1000D * distance) / getRoomSize()) * getWiLoss() + Math.pow(rK, (rK + 2D) / (rK + 1.0D) - getEmpiricalParameter()) * getFloorLoss();
                localEnvironmentCorrections.rStdDev = getWiStdDev();
            } else
            {
                localEnvironmentCorrections.rMedianLoss += 2D * getWeLoss();
                localEnvironmentCorrections.rStdDev = Math.sqrt(localEnvironmentCorrections.rStdDev * localEnvironmentCorrections.rStdDev + 4D * rWeStdDev * rWeStdDev);
            }
        } else
        if(RxLocalEnv == 0 || TxLocalEnv == 0)
        {
            localEnvironmentCorrections.rMedianLoss += getWeLoss();
            localEnvironmentCorrections.rStdDev = Math.sqrt(localEnvironmentCorrections.rStdDev * localEnvironmentCorrections.rStdDev + rWeStdDev * rWeStdDev);
        }
        return localEnvironmentCorrections;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("HataAndSDModel");
        element.setAttribute("b", Double.toString(empiricalParameter));
        element.setAttribute("floorHeight", Double.toString(getFloorHeight()));
        element.setAttribute("floorLoss", Double.toString(getFloorLoss()));
        element.setAttribute("roomSize", Double.toString(getRoomSize()));
        element.setAttribute("weLoss", Double.toString(getWeLoss()));
        element.setAttribute("weStdDev", Double.toString(getWeStdDev()));
        element.setAttribute("wiLoss", Double.toString(getWiLoss()));
        element.setAttribute("wiStdDev", Double.toString(getWiStdDev()));
        element.appendChild(super.toElement(doc));
        return element;
    }

    public static final String MODEL_ID = "HataAndSDModel";
    private double wiLoss;
    private double wiStdDev;
    private double weLoss;
    private double weStdDev;
    private LocalEnvCorrections lec;
    private double floorLoss;
    private double roomSize;
    private double floorHeight;
    private double empiricalParameter;
}
