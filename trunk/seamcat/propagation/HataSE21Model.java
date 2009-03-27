// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HataSE21Model.java

package org.seamcat.propagation;

import java.util.ArrayList;
import java.util.List;
import org.seamcat.distribution.GaussianDistribution;
import org.seamcat.model.NodeAttribute;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.propagation:
//            HataAndSDModel

public class HataSE21Model extends HataAndSDModel
{

    public HataSE21Model()
    {
    }

    public HataSE21Model(HataAndSDModel p)
    {
        super(p);
    }

    public HataSE21Model(Element element)
    {
        super((Element)element.getElementsByTagName("HataAndSDModel").item(0));
    }

    public HataSE21Model(int txLocalEnv, int rxLocalEnv, int generalEnv, int propagEnv, double rWiLoss, double rWiStdDev, double rWeLoss, double rWeStdDev, double rFloorLoss, 
            double rFloorHeight, double rRoomSize, double rB)
    {
        super(txLocalEnv, rxLocalEnv, generalEnv, propagEnv, rWiLoss, rWiStdDev, rWeLoss, rWeStdDev, rFloorLoss, rFloorHeight, rRoomSize, rB);
    }

    public String toString()
    {
        return "Extended Hata";
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
        if(Double.isInfinite(medianLoss))
            medianLoss = 0.0D;
        return medianLoss;
    }

    protected double calculateMedianLoss(double rFreq, double rDist, double rHTx, double rHRx)
    {
        double rL = 0.0D;
        double rCorr = 0.0D;
        double rD1 = 0.040000000000000001D;
        double rD2 = 0.10000000000000001D;
        double rHm = Math.min(rHTx, rHRx);
        double rHb = Math.max(rHTx, rHRx);
        double rL0 = 32.399999999999999D + 20D * Math.log10(rFreq) + 10D * Math.log10(rDist * rDist + ((rHb - rHm) * (rHb - rHm)) / 1000000D);
        double rA = ((1.1000000000000001D * Math.log10(rFreq) - 0.69999999999999996D) * Math.min(10D, rHm) - (1.5600000000000001D * Math.log10(rFreq) - 0.80000000000000004D)) + Math.max(0.0D, 20D * Math.log10(rHm / 10D));
        double rB = Math.min(0.0D, 20D * Math.log10(rHb / 30D));
        if(rDist <= 20D)
        {
            double rAlpha = 1.0D;
            rCorr = Math.log10(rDist);
        } else
        {
            double rAlpha = 1.0D + (0.14000000000000001D + 0.00018699999999999999D * rFreq + 0.00107D * rHb) * Math.pow(Math.log10(rDist / 20D), 0.80000000000000004D);
            rCorr = Math.pow(Math.log10(rDist), rAlpha);
        }
        if(rDist <= 0.040000000000000001D)
            rL = rL0;
        else
        if(rDist >= 0.10000000000000001D)
        {
            if(rFreq > 30D && rFreq <= 150D)
                rL = (((69.599999999999994D + 26.199999999999999D * Math.log10(150D)) - 20D * Math.log10(150D / rFreq) - 13.82D * Math.log10(Math.max(30D, rHb))) + (44.899999999999999D - 6.5499999999999998D * Math.log10(Math.max(30D, rHb))) * rCorr) - rA - rB;
            else
            if(rFreq <= 1500D)
                rL = (((69.599999999999994D + 26.199999999999999D * Math.log10(rFreq)) - 13.82D * Math.log10(Math.max(30D, rHb))) + (44.899999999999999D - 6.5499999999999998D * Math.log10(Math.max(30D, rHb))) * rCorr) - rA - rB;
            else
            if(rFreq <= 2000D)
                rL = (((46.299999999999997D + 33.899999999999999D * Math.log10(rFreq)) - 13.82D * Math.log10(Math.max(30D, rHb))) + (44.899999999999999D - 6.5499999999999998D * Math.log10(Math.max(30D, rHb))) * rCorr) - rA - rB;
            else
                rL = (((46.299999999999997D + 33.899999999999999D * Math.log10(2000D) + 10D * Math.log10(rFreq / 2000D)) - 13.82D * Math.log10(Math.max(30D, rHb))) + (44.899999999999999D - 6.5499999999999998D * Math.log10(Math.max(30D, rHb))) * rCorr) - rA - rB;
        } else
        {
            double rL1 = calculateMedianLoss(rFreq, 0.040000000000000001D, rHTx, rHRx);
            rL1 = generalEnvCorrections(rL1, rFreq, 0.040000000000000001D);
            double rL2 = calculateMedianLoss(rFreq, 0.10000000000000001D, rHTx, rHRx);
            rL2 = generalEnvCorrections(rL2, rFreq, 0.10000000000000001D);
            rL = rL1 + ((Math.log10(rDist) - Math.log10(0.040000000000000001D)) / (Math.log10(0.10000000000000001D) - Math.log10(0.040000000000000001D))) * (rL2 - rL1);
        }
        return rL;
    }

    protected double variationsStdDev(double rDist)
    {
        double rStdDev = 0.0D;
        int propagEnv = getPropagEnv();
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
        } else
        if(rDist <= 0.20000000000000001D)
        {
            if(propagEnv == 0)
                rStdDev = 12D;
            else
            if(propagEnv == 1)
                rStdDev = 17D;
        } else
        if(rDist <= 0.59999999999999998D)
        {
            if(propagEnv == 0)
                rStdDev = 12D + -7.5000000000000009D * (rDist - 0.20000000000000001D);
            else
            if(propagEnv == 1)
                rStdDev = 17D + -20D * (rDist - 0.20000000000000001D);
        } else
        {
            rStdDev = 9D;
        }
        return rStdDev;
    }

    protected double generalEnvCorrections(double rMedianLoss, double frequency, double rDist)
    {
        if(rDist >= 0.10000000000000001D)
            if(getGeneralEnv() == 1)
                rMedianLoss += -2D * Math.pow(Math.log10(Math.min(Math.max(150D, frequency), 2000D) / 28D), 2D) - 5.4000000000000004D;
            else
            if(getGeneralEnv() == 2)
                rMedianLoss += (-4.7800000000000002D * Math.pow(Math.log10(Math.min(Math.max(150D, frequency), 2000D)), 2D) + 18.329999999999998D * Math.log10(Math.min(Math.max(150D, frequency), 2000D))) - 40.939999999999998D;
        return rMedianLoss;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("HataSE21Model");
        element.appendChild(super.toElement(doc));
        return element;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Model reference", "", this, "Propagation Model", null, false, false, null));
        nodeList.add(new NodeAttribute("Median loss", "", getMedianSelected() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.FALSE, Boolean.TRUE
        }, true, true, null));
        nodeList.add(new NodeAttribute("Variations", "", getVariationsSelected() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.FALSE, Boolean.TRUE
        }, true, true, null));
        nodeList.add(new NodeAttribute("General environment", "", ENVIRONMENT[getGeneralEnv()], "String", ENVIRONMENT, true, true, null));
        nodeList.add(new NodeAttribute("Transmitter environment", "", DOOR[getTxLocalEnv()], "String", DOOR, true, true, null));
        nodeList.add(new NodeAttribute("Receiver environment", "", DOOR[getRxLocalEnv()], "String", DOOR, true, true, null));
        nodeList.add(new NodeAttribute("Propagation environment", "", ROOF[getPropagEnv()], "String", ROOF, true, true, null));
        nodeList.add(new NodeAttribute("Floor loss", "dB", new Double(getFloorLoss()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Empirical parameter B", "", new Double(getEmpiricalParameter()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Room size", "m", new Double(getRoomSize()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Floor height", "m", new Double(getFloorHeight()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Wall loss (indoor-indoor)", "dB", new Double(getWiLoss()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Wall loss (indoor-outdoor)", "dB", new Double(getWeLoss()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Wall loss std. dev. (indoor-indoor)", "dB", new Double(getWiStdDev()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Wall loss std. dev. (indoor-outdoor)", "dB", new Double(getWeStdDev()), "Double", null, false, true, null));
        nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
        switch(rowIndex)
        {
        case 1: // '\001'
            setMedianSelected(((Boolean)aValue).booleanValue());
            break;

        case 2: // '\002'
            setVariationsSelected(((Boolean)aValue).booleanValue());
            break;

        case 3: // '\003'
            setGeneralEnv(resolveGenEnv((String)aValue));
            break;

        case 4: // '\004'
            setTxLocalEnv(resolveLocalEnv((String)aValue));
            break;

        case 5: // '\005'
            setRxLocalEnv(resolveLocalEnv((String)aValue));
            break;

        case 6: // '\006'
            setPropagEnv(resolvePropagEnv((String)aValue));
            break;

        case 7: // '\007'
            setFloorLoss(((Number)aValue).doubleValue());
            break;

        case 8: // '\b'
            setEmpiricalParameter(((Number)aValue).doubleValue());
            break;

        case 9: // '\t'
            setEmpiricalParameter(((Number)aValue).doubleValue());
            break;

        case 10: // '\n'
            setRoomSize(((Number)aValue).doubleValue());
            break;

        case 11: // '\013'
            setFloorHeight(((Number)aValue).doubleValue());
            break;

        case 12: // '\f'
            setWiLoss(((Number)aValue).doubleValue());
            break;

        case 13: // '\r'
            setWeLoss(((Number)aValue).doubleValue());
            break;

        case 14: // '\016'
            setWiStdDev(((Number)aValue).doubleValue());
            break;

        case 15: // '\017'
            setWeStdDev(((Number)aValue).doubleValue());
            break;
        }
    }

    public static final String MODEL_ID = "HataSE21Model";
}
