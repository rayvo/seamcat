// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SDModel.java

package org.seamcat.propagation;

import java.util.ArrayList;
import java.util.List;
import org.seamcat.distribution.*;
import org.seamcat.model.NodeAttribute;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.propagation:
//            HataAndSDModel

public final class SDModel extends HataAndSDModel
{

    public SDModel()
    {
        waterCtr = 3D;
        earthSurfaceAdmittance = 1.0000000000000001E-005D;
        refrIndexGradient = 40D;
        refrLayerProb = 1.0D;
        timePercentDistrib = new UniformDistribution();
        timePercentage = new UniformDistribution(50D, 50D);
    }

    public SDModel(Element element)
    {
        super((Element)element.getElementsByTagName("HataAndSDModel").item(0));
        waterCtr = 3D;
        earthSurfaceAdmittance = 1.0000000000000001E-005D;
        refrIndexGradient = 40D;
        refrLayerProb = 1.0D;
        timePercentDistrib = new UniformDistribution();
        timePercentage = new UniformDistribution(50D, 50D);
        setEarthSurfaceAdmittance(Double.parseDouble(element.getAttribute("earthSurfaceAdmittance")));
        setRefrIndexGradient(Double.parseDouble(element.getAttribute("refrIndexGradient")));
        setRefrLayerProb(Double.parseDouble(element.getAttribute("refrLayerProb")));
        setWaterCtr(Double.parseDouble(element.getAttribute("waterCtr")));
        try
        {
            timePercentDistrib = (UniformDistribution)Distribution.create((Element)element.getElementsByTagName("timePercentDistrib").item(0).getFirstChild());
        }
        catch(Exception e) { }
        try
        {
            timePercentage = (UniformDistribution)Distribution.create((Element)element.getElementsByTagName("timePercentage").item(0).getFirstChild());
        }
        catch(Exception e) { }
    }

    public SDModel(int txLocalEnv, int rxLocalEnv, double rWiLoss, double rWiStdDev, double rWeLoss, double rWeStdDev, double rFloorLoss, double rFloorHeight, 
            double rRoomSize, double rB, double rWaterCtr, double rEarthSurfaceAdmittance, double rRefrLayerProb, double rRefrIndex, double rPtMin, 
            double rPtMax)
    {
        super(txLocalEnv, rxLocalEnv, 2, 0, rWiLoss, rWiStdDev, rWeLoss, rWeStdDev, rFloorLoss, rFloorHeight, rRoomSize, rB);
        waterCtr = 3D;
        earthSurfaceAdmittance = 1.0000000000000001E-005D;
        refrIndexGradient = 40D;
        refrLayerProb = 1.0D;
        timePercentDistrib = new UniformDistribution();
        timePercentage = new UniformDistribution(50D, 50D);
        setWaterCtr(rWaterCtr);
        setEarthSurfaceAdmittance(rEarthSurfaceAdmittance);
        setRefrIndexGradient(rRefrIndex);
        setRefrLayerProb(rRefrLayerProb);
        setTimePercentage(new UniformDistribution(rPtMin, rPtMax));
    }

    public String toString()
    {
        return "Spherical Diffraction";
    }

    public double evaluate(double rFreq, double rDist, double rHTx, double rHRx)
    {
        double rL = 0.0D;
        double rStdDev = 0.0D;
        if(getMedianSelected())
        {
            double rP = getTimePercentage().trial();
            rL = 92.5D + 20D * Math.log10(rFreq / 1000D) + 20D * Math.log10(rDist) + gazLoss(rFreq, rDist) + diffractionLoss(rFreq, rDist, rHTx, rHRx, rP);
            HataAndSDModel.LocalEnvCorrections lec = localEnvCorrections(new HataAndSDModel.LocalEnvCorrections(rL, rStdDev), rFreq, rDist, rHTx, rHRx);
            rL = lec.rMedianLoss;
            rStdDev = lec.rStdDev;
        }
        if(getVariationsSelected())
        {
            getVariationsDistrib().setStdDev(rStdDev);
            rL += getVariationsDistrib().trial();
        }
        return rL;
    }

    private double gazLoss(double rFreq, double rDist)
    {
        double rAg = 0.0D;
        rAg = (oxygenLinearLoss(rFreq) + waterLinearLoss(rFreq)) * rDist;
        return rAg;
    }

    private double diffractionLoss(double rFreq, double rDist, double rHTx, double rHRx, double rTimePercentage)
    {
        double rRe = 6375D;
        double rK = getEarthSurfaceAdmittance();
        double rK2 = rK * rK;
        double rK4 = rK2 * rK2;
        double rKe50 = 157D / (157D - getRefrIndexGradient());
        double rKe;
        if(rTimePercentage < 50D)
            rKe = rKe50 + ((5D - rKe50) * (1.7D - Math.log10(rTimePercentage))) / (1.7D - Math.log10(getRefrLayerProb()));
        else
            rKe = rKe50;
        double rAe = rKe * rRe;
        double rBeta;
        if(rFreq < 20D)
            rBeta = (1.0D + 1.6000000000000001D * rK2 + 0.75D * rK4) / (1.0D + 4.5D * rK2 + 1.3500000000000001D * rK4);
        else
            rBeta = 1.0D;
        double rX = 2.2000000000000002D * rBeta * Math.pow(rFreq, 0.33333333333333331D) * Math.pow(rAe, -0.66666666666666663D) * rDist;
        double rYTx = ((0.0095999999999999992D * rBeta * Math.pow(rFreq, 0.66666666666666663D)) / Math.pow(rAe, 0.33333333333333331D)) * rHTx;
        double rYRx = ((0.0095999999999999992D * rBeta * Math.pow(rFreq, 0.66666666666666663D)) / Math.pow(rAe, 0.33333333333333331D)) * rHRx;
        double rDiffractionLoss = -(f(rX) + g(rYTx) + g(rYRx));
        return rDiffractionLoss;
    }

    private static double oxygenLinearLoss(double rFreq)
    {
        double rLoss = 0.0D;
        double rFreq2 = 0.0D;
        rFreq /= 1000D;
        rFreq2 = rFreq * rFreq;
        if(rFreq <= 57D)
            rLoss = rFreq2 * 0.001D * (0.0071900000000000002D + 6.0899999999999999D / (rFreq * rFreq + 0.22700000000000001D) + 4.8099999999999996D / ((rFreq - 57D) * (rFreq - 57D) + 1.5D));
        else
        if(rFreq <= 60D)
            rLoss = 10.5D + 1.5D * (rFreq - 57D);
        else
        if(rFreq <= 63D)
            rLoss = 15D - 1.2D * (rFreq - 60D);
        else
        if(rFreq < 350D)
            rLoss = (rFreq + 198D) * (rFreq + 198D) * 0.001D * (3.7899999999999999E-007D * rFreq + 0.26500000000000001D / ((rFreq - 63D) * (rFreq - 63D) + 1.5900000000000001D) + 0.028000000000000001D / ((rFreq - 118D) * (rFreq - 118D) + 1.47D));
        return rLoss;
    }

    private double waterLinearLoss(double rFreq)
    {
        double rLoss = 0.0D;
        double rWaterCtr = getWaterCtr();
        rFreq /= 1000D;
        if(rFreq < 350D)
            rLoss = rFreq * rFreq * 0.0001D * rWaterCtr * (0.050000000000000003D + 0.0020999999999999999D * rWaterCtr + 3.6000000000000001D / ((rFreq - 22.199999999999999D) * (rFreq - 22.199999999999999D) + 8.5D) + 10.6D / ((rFreq - 183.30000000000001D) * (rFreq - 183.30000000000001D) + 9D) + 8.9000000000000004D / ((rFreq - 325.39999999999998D) * (rFreq - 325.39999999999998D) + 26.300000000000001D));
        return rLoss;
    }

    private static double f(double rX)
    {
        return (11D + 10D * Math.log10(rX)) - 17.600000000000001D * rX;
    }

    private double g(double rY)
    {
        double rK = getEarthSurfaceAdmittance();
        double rK1 = rK * 10D;
        double rK2 = rK / 10D;
        double rG;
        if(rY > 2D)
            rG = 17.600000000000001D * Math.sqrt(rY - 1.1000000000000001D) - 5D * Math.log10(rY - 1.1000000000000001D) - 8D;
        else
        if(rY > rK1)
            rG = 20D * Math.log10(rY + 0.10000000000000001D * rY * rY * rY);
        else
        if(rY > rK2)
            rG = 2D + 20D * Math.log10(rK) + 9D * Math.log10(rY / rK) * (1.0D + Math.log10(rY / rK));
        else
            rG = 2D + 20D * Math.log10(rK);
        return rG;
    }

    public double getWaterCtr()
    {
        return waterCtr;
    }

    public double getRefrIndexGradient()
    {
        return refrIndexGradient;
    }

    public UniformDistribution getTimePercentage()
    {
        return timePercentage;
    }

    public double getEarthSurfaceAdmittance()
    {
        return earthSurfaceAdmittance;
    }

    public double getRefrLayerProb()
    {
        return refrLayerProb;
    }

    public void setTimePercentDistrib(UniformDistribution timePercentDistrib)
    {
        this.timePercentDistrib = timePercentDistrib;
    }

    public void setWaterCtr(double waterCtr)
    {
        this.waterCtr = waterCtr;
    }

    public void setRefrIndexGradient(double refrIndexGradient)
    {
        this.refrIndexGradient = refrIndexGradient;
    }

    public void setTimePercentage(UniformDistribution timePercentage)
    {
        this.timePercentage = timePercentage;
    }

    public void setEarthSurfaceAdmittance(double earthSurfaceAdmittance)
    {
        this.earthSurfaceAdmittance = earthSurfaceAdmittance;
    }

    public void setRefrLayerProb(double refrLayerProb)
    {
        this.refrLayerProb = refrLayerProb;
    }

    public UniformDistribution getTimePercentDistrib()
    {
        return timePercentDistrib;
    }

    public double getTimePercMin()
    {
        return timePercentage.getMin();
    }

    public double getTimePercMax()
    {
        return timePercentage.getMax();
    }

    public void setTimePercMin(double ptMin)
    {
        timePercentage.setMin(ptMin);
    }

    public void setTimePercMax(double ptMax)
    {
        timePercentage.setMax(ptMax);
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("SDModel");
        element.setAttribute("earthSurfaceAdmittance", Double.toString(getEarthSurfaceAdmittance()));
        element.setAttribute("refrIndexGradient", Double.toString(getRefrIndexGradient()));
        element.setAttribute("refrLayerProb", Double.toString(getRefrLayerProb()));
        element.setAttribute("waterCtr", Double.toString(getWaterCtr()));
        Element timePercElement = doc.createElement("timePercentage");
        timePercElement.appendChild(timePercentage.toElement(doc));
        element.appendChild(timePercElement);
        Element timePercDistribElement = doc.createElement("timePercentDistrib");
        timePercDistribElement.appendChild(timePercentDistrib.toElement(doc));
        element.appendChild(timePercDistribElement);
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
        nodeList.add(new NodeAttribute("Water concentration", "g/m3", new Double(getWaterCtr()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Earth surface admittance", "", new Double(getEarthSurfaceAdmittance()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Refraction index gradient", "1/km", new Double(getRefrIndexGradient()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Refraction layer probability", "%", new Double(getRefrLayerProb()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Min time percentage", "%", new Double(getTimePercMin()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Max time percentage", "%", new Double(getTimePercMax()), "Double", null, false, true, null));
        nodeAttributes = (NodeAttribute[])(NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
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

        case 16: // '\020'
            setWaterCtr(((Number)aValue).doubleValue());
            break;

        case 17: // '\021'
            setEarthSurfaceAdmittance(((Number)aValue).doubleValue());
            break;

        case 18: // '\022'
            setRefrIndexGradient(((Number)aValue).doubleValue());
            break;

        case 19: // '\023'
            setRefrLayerProb(((Number)aValue).doubleValue());
            break;

        case 20: // '\024'
            setTimePercMin(((Number)aValue).doubleValue());
            break;

        case 21: // '\025'
            setTimePercMax(((Number)aValue).doubleValue());
            break;
        }
    }

    public static final String MODEL_ID = "SDModel";
    private static final double EARTHRADIUS_IN_KM = 6375D;
    private double waterCtr;
    private double earthSurfaceAdmittance;
    private double refrIndexGradient;
    private double refrLayerProb;
    private UniformDistribution timePercentDistrib;
    private UniformDistribution timePercentage;
}
