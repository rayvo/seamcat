// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HataSE24Model.java

package org.seamcat.propagation;

import org.seamcat.distribution.GaussianDistribution;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.propagation:
//            HataSE21Model, HataAndSDModel

public class HataSE24Model extends HataSE21Model
{

    public HataSE24Model()
    {
    }

    public HataSE24Model(HataAndSDModel p)
    {
        super(p);
    }

    public HataSE24Model(Element element)
    {
        super((Element)element.getElementsByTagName("HataSE21Model").item(0));
    }

    public HataSE24Model(int txLocalEnv, int rxLocalEnv, int generalEnv, int propagEnv, double rWiLoss, double rWiStdDev, double rWeLoss, double rWeStdDev, double rFloorLoss, 
            double rFloorHeight, double rRoomSize, double rB)
    {
        super(txLocalEnv, rxLocalEnv, generalEnv, propagEnv, rWiLoss, rWiStdDev, rWeLoss, rWeStdDev, rFloorLoss, rFloorHeight, rRoomSize, rB);
    }

    public String toString()
    {
        return "Extended Hata - SRD";
    }

    protected double calculateMedianLoss(double frequency, double distance, double heightOfTransmitter, double heightOfReceiver)
    {
        double medianLoss = 0.0D;
        double rCorr = 0.0D;
        double firstLimitDistance = 0.040000000000000001D;
        double secondLimitDistance = 0.10000000000000001D;
        double rHm = Math.min(heightOfTransmitter, heightOfReceiver);
        double rHb = Math.max(heightOfTransmitter, heightOfReceiver);
        double freeSpaceAttenuation = 32.399999999999999D + 20D * Math.log10(frequency) + 10D * Math.log10(distance * distance + ((rHb - rHm) * (rHb - rHm)) / 1000000D);
        double rA = ((1.1000000000000001D * Math.log10(frequency) - 0.69999999999999996D) * Math.min(10D, rHm) - (1.5600000000000001D * Math.log10(frequency) - 0.80000000000000004D)) + Math.max(0.0D, 20D * Math.log10(rHm / 10D));
        double rB = ((1.1000000000000001D * Math.log10(frequency) - 0.69999999999999996D) * Math.min(10D, rHb) - (1.5600000000000001D * Math.log10(frequency) - 0.80000000000000004D)) + Math.max(0.0D, 20D * Math.log10(rHb / 10D));
        if(distance <= 20D)
        {
            double rAlpha = 1.0D;
            rCorr = Math.log10(distance);
        } else
        {
            double rAlpha = 1.0D + (0.14000000000000001D + 0.00018699999999999999D * frequency + 0.00107D * rHb) * Math.pow(Math.log10(distance / 20D), 0.80000000000000004D);
            rCorr = Math.pow(Math.log10(distance), rAlpha);
        }
        if(distance <= 0.040000000000000001D)
            medianLoss = freeSpaceAttenuation;
        else
        if(distance >= 0.10000000000000001D)
        {
            if(frequency > 30D && frequency <= 150D)
                medianLoss = (((69.599999999999994D + 26.199999999999999D * Math.log10(150D)) - 20D * Math.log10(150D / frequency) - 13.82D * Math.log10(Math.max(30D, rHb))) + (44.899999999999999D - 6.5499999999999998D * Math.log10(Math.max(30D, rHb))) * rCorr) - rA - rB;
            else
            if(frequency <= 1500D)
                medianLoss = (((69.599999999999994D + 26.199999999999999D * Math.log10(frequency)) - 13.82D * Math.log10(Math.max(30D, rHb))) + (44.899999999999999D - 6.5499999999999998D * Math.log10(Math.max(30D, rHb))) * rCorr) - rA - rB;
            else
            if(frequency <= 2000D)
                medianLoss = (((46.299999999999997D + 33.899999999999999D * Math.log10(frequency)) - 13.82D * Math.log10(Math.max(30D, rHb))) + (44.899999999999999D - 6.5499999999999998D * Math.log10(Math.max(30D, rHb))) * rCorr) - rA - rB;
            else
            if(frequency <= 3000D)
                medianLoss = (((46.299999999999997D + 33.899999999999999D * Math.log10(2000D) + 10D * Math.log10(frequency / 2000D)) - 13.82D * Math.log10(Math.max(30D, rHb))) + (44.899999999999999D - 6.5499999999999998D * Math.log10(Math.max(30D, rHb))) * rCorr) - rA - rB;
        } else
        {
            double lossForFirstLimitDistance = calculateMedianLoss(frequency, 0.040000000000000001D, heightOfTransmitter, heightOfReceiver);
            generalEnvCorrections(lossForFirstLimitDistance, frequency, 0.040000000000000001D);
            double lossForSecondLimitDistance = calculateMedianLoss(frequency, 0.10000000000000001D, heightOfTransmitter, heightOfReceiver);
            generalEnvCorrections(lossForSecondLimitDistance, frequency, 0.10000000000000001D);
            medianLoss = lossForFirstLimitDistance + ((Math.log10(distance) - Math.log10(0.040000000000000001D)) / (Math.log10(0.040000000000000001D) - Math.log10(0.10000000000000001D))) * (lossForFirstLimitDistance - lossForSecondLimitDistance);
        }
        return medianLoss;
    }

    public double evaluate(double rFreq, double rDist, double rHTx, double rHRx)
    {
        double medianLoss = 0.0D;
        double standardDeviation = 0.0D;
        double freeSpaceAttenuation = 32.399999999999999D + 20D * Math.log10(rFreq) + 10D * Math.log10(rDist * rDist + ((rHTx - rHRx) * (rHTx - rHRx)) / 1000000D);
        if(getMedianSelected())
        {
            medianLoss = calculateMedianLoss(rFreq, rDist, rHTx, rHRx);
            standardDeviation = variationsStdDev(rDist);
            medianLoss = generalEnvCorrections(medianLoss, rFreq, rDist);
            if(medianLoss < freeSpaceAttenuation)
                medianLoss = freeSpaceAttenuation;
            HataAndSDModel.LocalEnvCorrections lec = localEnvCorrections(new HataAndSDModel.LocalEnvCorrections(medianLoss, standardDeviation), rFreq, rDist, rHTx, rHRx);
            medianLoss = lec.rMedianLoss;
            standardDeviation = lec.rStdDev;
        }
        if(getVariationsSelected())
        {
            getVariationsDistrib().setStdDev(standardDeviation);
            medianLoss += getVariationsDistrib().trial();
        }
        return medianLoss;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("HataSE24Model");
        element.appendChild(super.toElement(doc));
        return element;
    }

    public static final String MODEL_ID = "HataSE24Model";
}
