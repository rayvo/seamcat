// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   R370Model.java

package org.seamcat.propagation;

import java.util.ArrayList;
import java.util.List;
import org.seamcat.distribution.*;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.NodeAttribute;
import org.seamcat.model.technical.exception.ModelException;
import org.seamcat.model.technical.stats.Stats;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.propagation:
//            BuiltInModel

public class R370Model extends BuiltInModel
{

    public R370Model()
    {
        this(2, 0, 50D, 50D);
    }

    public R370Model(Element element)
    {
        super((Element)element.getElementsByTagName("BuiltInModel").item(0));
        useUserSpecifiedLocalClutterHeight = false;
        userSpecifiedLocalClutterHeight = 0.0D;
        setTimePercentage((UniformDistribution)Distribution.create((Element)element.getElementsByTagName("timePercentage").item(0).getFirstChild()));
        setTimePercIndex(Integer.parseInt(element.getAttribute("timePercIndex")));
        setSystemType(Integer.parseInt(element.getAttribute("systemType")));
        try
        {
            setUserSpecifiedLocalClutterHeight(Double.parseDouble(element.getAttribute("clutterHeight")));
            setUseUserSpecifiedLocalClutterHeight(true);
        }
        catch(Exception e) { }
    }

    public String toString()
    {
        return "ITU-R P.1546";
    }

    public R370Model(int generalEnv, int eSystemType, double ptMin, double ptMax)
    {
        super(1, 1, generalEnv, 0);
        useUserSpecifiedLocalClutterHeight = false;
        userSpecifiedLocalClutterHeight = 0.0D;
        setSystemType(eSystemType);
        setTimePercentage(new UniformDistribution(ptMin, ptMax));
    }

    public void setTimePercIndex(int timePercIndex)
    {
        this.timePercIndex = timePercIndex;
    }

    public void setTimePercentage(UniformDistribution timePercentage)
    {
        this.timePercentage = timePercentage;
    }

    public void setTimePercMin(double ptMin)
    {
        timePercentage.setMin(ptMin);
    }

    public void setTimePercMax(double ptMax)
    {
        timePercentage.setMax(ptMax);
    }

    public double getTimePercMin()
    {
        return timePercentage.getMin();
    }

    public double getTimePercMax()
    {
        return timePercentage.getMax();
    }

    public void setSystemType(int _systemType)
    {
        systemType = _systemType;
    }

    public int getTimePercIndex()
    {
        return timePercIndex;
    }

    public Distribution getTimePercentage()
    {
        return timePercentage;
    }

    public int getSystemType()
    {
        return systemType;
    }

    public double evaluate(double rFreq, double rDist, double rHTx, double rHRx)
        throws ModelException
    {
        double rE = 0.0D;
        double rEfs = 0.0D;
        double rL = 0.0D;
        double rPt = 0.0D;
        double rStdDev = 0.0D;
        if(rDist < 1.0D)
        {
            if(1.0D - rDist < 0.0001D)
                rDist = 1.0D;
            System.currentTimeMillis();
        }
        try
        {
            if(getMedianSelected())
            {
                rPt = getTimePercentage().trial();
                rE = e(rFreq, rDist, rHTx, rHRx, rPt);
                rE += clutterCorrection(rFreq, rDist, rHTx, rHRx);
                rEfs = efs(rDist, rHTx, rHRx);
                if(rE > rEfs)
                    rE = rEfs;
                rL = (139D - rE) + 20D * Math.log10(rFreq);
            }
            if(getVariationsSelected())
            {
                int eSystemType = getSystemType();
                int eGeneralEnv = getGeneralEnv();
                if(eSystemType == 0)
                {
                    double rK;
                    if(eGeneralEnv == 0)
                        rK = 2.1000000000000001D;
                    else
                        rK = 3.7999999999999998D;
                    rStdDev = rK + 1.6000000000000001D * Math.log10(rFreq);
                } else
                if(eSystemType == 2)
                {
                    double rK = 5.0999999999999996D;
                    rStdDev = rK + 1.6000000000000001D * Math.log10(rFreq);
                } else
                {
                    rStdDev = 5.5D;
                }
                getVariationsDistrib().setStdDev(rStdDev);
                rL += getVariationsDistrib().trial();
            }
            return rL;
        }
        catch(Exception e)
        {
            throw new ModelException(e.getMessage());
        }
    }

    private double e(double rFreq, double rDist, double rHTx, double rHRx, double rTimePerc)
        throws ModelException
    {
        double rEfs = 0.0D;
        try
        {
            double rE;
            if(rHTx >= 10D)
            {
                int eFreqInf;
                int eFreqSup;
                double rFreqInf;
                double rFreqSup;
                if(rFreq < 600D)
                {
                    eFreqInf = 0;
                    rFreqInf = 100D;
                    eFreqSup = 1;
                    rFreqSup = 600D;
                } else
                {
                    eFreqInf = 1;
                    rFreqInf = 600D;
                    eFreqSup = 2;
                    rFreqSup = 2000D;
                }
                int eTimePercInf;
                int eTimePercSup;
                double rTimePercInf;
                double rTimePercSup;
                if(rTimePerc < 10D)
                {
                    eTimePercInf = 2;
                    rTimePercInf = 1.0D;
                    eTimePercSup = 1;
                    rTimePercSup = 10D;
                } else
                {
                    eTimePercInf = 1;
                    rTimePercInf = 10D;
                    eTimePercSup = 0;
                    rTimePercSup = 50D;
                }
                rEfs = efs(rDist, rHTx, rHRx);
                double rEInf = eb(eFreqInf, rDist, rHTx, rHRx, eTimePercInf);
                double rESup = eb(eFreqSup, rDist, rHTx, rHRx, eTimePercInf);
                double rE1 = rEInf + ((rESup - rEInf) * Math.log10(rFreq / rFreqInf)) / Math.log10(rFreqSup / rFreqInf);
                if(rE1 > rEfs)
                    rE1 = rEfs;
                rEInf = eb(eFreqInf, rDist, rHTx, rHRx, eTimePercSup);
                rESup = eb(eFreqSup, rDist, rHTx, rHRx, eTimePercSup);
                double rE2 = rEInf + ((rESup - rEInf) * Math.log10(rFreq / rFreqInf)) / Math.log10(rFreqSup / rFreqInf);
                if(rE1 > rEfs)
                    rE1 = rEfs;
                double rQ = Stats.qi(rTimePerc / 100D);
                double rQ1 = Stats.qi(rTimePercInf / 100D);
                double rQ2 = Stats.qi(rTimePercSup / 100D);
                rE = (rE2 * (rQ1 - rQ) + rE1 * (rQ - rQ2)) / (rQ1 - rQ2);
            } else
            if(rDist < dh(rHTx))
                rE = (e(rFreq, dh(10D), 10D, rHRx, rTimePerc) + e(rFreq, rDist, 10D, rHRx, rTimePerc)) - e(rFreq, dh(rHTx), 10D, rHRx, rTimePerc);
            else
                rE = e(rFreq, (dh(10D) + rDist) - dh(rHTx), 10D, rHRx, rTimePerc);
            return rE;
        }
        catch(Exception e)
        {
            throw new ModelException(e.getMessage());
        }
    }

    private double eb(int eFreqIndex, double rDist, double rHTx, double rHRx, 
            int eTimePercIndex)
        throws ModelException
    {
        double rK = 0.0D;
        double rEu = 0.0D;
        double rEref = 0.0D;
        double rEoff = 0.0D;
        double rEfs = 0.0D;
        double rE1 = 0.0D;
        double rE2 = 0.0D;
        double rA0 = a0[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rA1 = a1[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rA2 = a2[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rA3 = a3[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rB0 = b0[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rB1 = b1[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rB2 = b2[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rB3 = b3[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rB4 = b4[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rB5 = b5[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rB6 = b6[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rB7 = b7[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rC0 = c0[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rC1 = c1[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rC2 = c2[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rC3 = c3[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rC4 = c4[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rC5 = c5[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rC6 = c6[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rD0 = d0[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        double rD1 = d1[(int)((double)eTimePercIndex + 3D * (double)eFreqIndex)];
        try
        {
            rK = Math.log10(rHTx / 9.375D) / Math.log10(2D);
            rE1 = (rA0 * rK * rK + rA1 * rK + rA2) * Math.log10(rDist) + 0.19950000000000001D * rK * rK + 1.8671D * rK + rA3;
            double rDzeta = Math.pow(Math.log10(rDist), rB5);
            rEref = ((rB0 * (Math.exp(-rB4 * Math.pow(10D, rDzeta)) - 1.0D) + rB1 * Math.exp((-(Math.log10(rDist) - rB2) * (Math.log10(rDist) - rB2)) / (rB3 * rB3))) - rB6 * Math.log10(rDist)) + rB7;
            rEoff = (rC0 / 2D) * rK * (1.0D - Mathematics.tanh(rC1 * (Math.log10(rDist) - rC2 - Math.pow(rC3, rK) / rC4))) + rC5 * Math.pow(rK, rC6);
            rE2 = rEref + rEoff;
            double rPb = rD0 + rD1 * Math.sqrt(rK);
            rEu = rPb * Math.log10(Math.pow(10D, (rE1 + rE2) / rPb) / (Math.pow(10D, rE1 / rPb) + Math.pow(10D, rE2 / rPb)));
            double rPbb = 8D;
            rEfs = efs(rDist, rHTx, rHRx);
            double rEb = rPbb * Math.log10(Math.pow(10D, (rEu + rEfs) / rPbb) / (Math.pow(10D, rEu / rPbb) + Math.pow(10D, rEfs / rPbb)));
            return rEb;
        }
        catch(Exception e)
        {
            throw new ModelException(e.getMessage());
        }
    }

    private double dh(double rHTx)
        throws ModelException
    {
        double rDH = 0.0D;
        try
        {
            rDH = 4.0999999999999996D * Math.sqrt(rHTx);
        }
        catch(Exception e)
        {
            throw new ModelException(e.getMessage());
        }
        return rDH;
    }

    private double efs(double rDist, double rHTx, double rHRx)
        throws ModelException
    {
        double rEfs = 0.0D;
        try
        {
            rEfs = 106.90000000000001D - 20D * Math.log10(rDist);
            return rEfs;
        }
        catch(Exception e)
        {
            throw new ModelException(e.getMessage());
        }
    }

    public double j(double v)
        throws ModelException
    {
        double rJ = 0.0D;
        try
        {
            rJ = 6.9000000000000004D + 20D * Math.log10((Math.sqrt((v - 0.10000000000000001D) * (v - 0.10000000000000001D) + 1.0D) + v) - 0.10000000000000001D);
            return rJ;
        }
        catch(Exception e)
        {
            throw new ModelException(e.getMessage());
        }
    }

    private double d06(double rFreq, double rHTx, double rHRx)
        throws ModelException
    {
        double rD06 = 0.0D;
        double rDf = 0.0D;
        double rDh = 0.0D;
        try
        {
            rDf = 3.8899999999999997E-005D * rFreq * rHTx * rHRx;
            rDh = 4.0999999999999996D * (Math.sqrt(rHTx) + Math.sqrt(rHRx));
            rD06 = (rDf * rDh) / (rDf + rDh);
        }
        catch(Exception e)
        {
            throw new ModelException(e.getMessage());
        }
        return rD06;
    }

    private double clutterCorrection(double rFreq, double rDist, double rHTx, double rHRx)
        throws ModelException
    {
        int eGeneralEnv = getGeneralEnv();
        if(rHRx < 1.0D)
            throw new ModelException("rHRx < 1.0");
        double rCorr;
        try
        {
            double rR0;
            if(useUserSpecifiedLocalClutterHeight)
                rR0 = userSpecifiedLocalClutterHeight;
            else
                rR0 = eGeneralEnv != 0 ? 10D : 20D;
            double rR = (1000D * rDist * rR0 - 15D * rHTx) / (1000D * rDist - 15D);
            if(rR < 1.0D)
                rR = 1.0D;
            double rKHRx = 3.2000000000000002D + 6.2000000000000002D * Math.log10(rFreq);
            switch(eGeneralEnv)
            {
            case 0: // '\0'
            case 1: // '\001'
                if(rHRx < rR)
                {
                    double rHdif = rR - rHRx;
                    double rKnu = 0.010800000000000001D * Math.sqrt(rFreq);
                    double rTheta = Mathematics.atanD(rHdif / 27D);
                    double rNu = rKnu * Math.sqrt(rHdif * rTheta);
                    rCorr = 6.0300000000000002D - j(rNu);
                } else
                {
                    rCorr = rKHRx * Math.log10(rHRx / rR);
                }
                break;

            default:
                rCorr = rKHRx * Math.log10(rHRx / rR);
                break;
            }
            if((eGeneralEnv == 0 || eGeneralEnv == 1) && rDist < 15D && rHTx - rR0 < 150D && rHTx > rR0)
                rCorr += -3.2999999999999998D * Math.log10(rFreq) * (1.0D - 0.84999999999999998D * Math.log10(rDist)) * (1.0D - 0.46000000000000002D * Math.log10((1.0D + rHTx) - rR0));
        }
        catch(Exception e)
        {
            throw new ModelException(e.getMessage());
        }
        return rCorr;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("R370Model");
        element.setAttribute("timePercIndex", Integer.toString(getTimePercIndex()));
        element.setAttribute("systemType", Integer.toString(getSystemType()));
        if(useUserSpecifiedLocalClutterHeight)
            element.setAttribute("clutterHeight", Double.toString(userSpecifiedLocalClutterHeight));
        Element timePercentage = doc.createElement("timePercentage");
        timePercentage.appendChild(getTimePercentage().toElement(doc));
        element.appendChild(timePercentage);
        element.appendChild(super.toElement(doc));
        return element;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Model reference", "", "R370Model", "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Median loss", "", getMedianSelected() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.FALSE, Boolean.TRUE
        }, true, true, null));
        nodeList.add(new NodeAttribute("Variations", "", getVariationsSelected() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.FALSE, Boolean.TRUE
        }, true, true, null));
        nodeList.add(new NodeAttribute("General environment", "", ENVIRONMENT[getGeneralEnv()], "String", ENVIRONMENT, true, true, null));
        nodeList.add(new NodeAttribute("System", "", SYSTEM[getSystemType()], "String", SYSTEM, true, true, null));
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
            setSystemType(resolveSystem((String)aValue));
            break;

        case 5: // '\005'
            setTimePercMin(((Number)aValue).doubleValue());
            break;

        case 6: // '\006'
            setTimePercMax(((Number)aValue).doubleValue());
            break;
        }
    }

    public static final int resolveSystem(String systemStr)
    {
        int system;
        if(systemStr.equals(SYSTEM[0]))
            system = 0;
        else
        if(systemStr.equals(SYSTEM[1]))
            system = 1;
        else
        if(systemStr.equals(SYSTEM[2]))
            system = 2;
        else
            throw new IllegalArgumentException("Cannot resolve general environment string");
        return system;
    }

    public double getUserSpecifiedLocalClutterHeight()
    {
        return userSpecifiedLocalClutterHeight;
    }

    public void setUserSpecifiedLocalClutterHeight(double userSpecifiedLocalClutterHeight)
    {
        this.userSpecifiedLocalClutterHeight = userSpecifiedLocalClutterHeight;
    }

    public boolean useUserSpecifiedLocalClutterHeight()
    {
        return useUserSpecifiedLocalClutterHeight;
    }

    public void setUseUserSpecifiedLocalClutterHeight(boolean useUserSpecifiedLocalClutterHeight)
    {
        this.useUserSpecifiedLocalClutterHeight = useUserSpecifiedLocalClutterHeight;
    }

    public static final String MODEL_ID = "R370Model";
    private static final double NB_FREQ_INDEX = 3D;
    private static final double NB_TIME_PERC_INDEX = 3D;
    private static final int F100 = 0;
    private static final int F600 = 1;
    private static final int F2000 = 2;
    private static final int P50 = 0;
    private static final int P10 = 1;
    private static final int P1 = 2;
    private static double a0[] = {
        0.0814D, 0.0814D, 0.077600000000000002D, 0.094600000000000004D, 0.091300000000000006D, 0.086999999999999994D, 0.094600000000000004D, 0.094100000000000003D, 0.091800000000000007D
    };
    private static double a1[] = {
        0.76100000000000001D, 0.76100000000000001D, 0.72599999999999998D, 0.88490000000000002D, 0.85389999999999999D, 0.81410000000000005D, 0.88490000000000002D, 0.88049999999999995D, 0.85840000000000005D
    };
    private static double a2[] = {
        -30.443999999999999D, -30.443999999999999D, -29.027999999999999D, -35.399000000000001D, -34.159999999999997D, -32.567D, -35.399000000000001D, -35.222000000000001D, -34.337000000000003D
    };
    private static double a3[] = {
        90.225999999999999D, 90.225999999999999D, 90.225999999999999D, 92.778000000000006D, 92.778000000000006D, 92.778000000000006D, 94.492999999999995D, 94.492999999999995D, 94.492999999999995D
    };
    private static double b0[] = {
        33.623800000000003D, 40.455399999999997D, 45.576999999999998D, 51.638599999999997D, 35.345300000000002D, 36.883600000000001D, 30.005099999999999D, 25.0641D, 31.387799999999999D
    };
    private static double b1[] = {
        10.8917D, 12.820600000000001D, 14.6752D, 10.9877D, 15.759499999999999D, 13.8843D, 15.420199999999999D, 22.101099999999999D, 15.6683D
    };
    private static double b2[] = {
        2.3311000000000002D, 2.2048000000000001D, 2.2332999999999998D, 2.2113D, 2.2252000000000001D, 2.3469000000000002D, 2.2978000000000001D, 2.3182999999999998D, 2.3940999999999999D
    };
    private static double b3[] = {
        0.44269999999999998D, 0.47610000000000002D, 0.54390000000000005D, 0.53839999999999999D, 0.52849999999999997D, 0.52459999999999996D, 0.49709999999999999D, 0.56359999999999999D, 0.56330000000000002D
    };
    private static double b4[] = {
        1.2560000000000001E-007D, 7.7879999999999996E-007D, 1.0499999999999999E-006D, 4.3229999999999999E-006D, 1.7039999999999999E-007D, 5.1689999999999998E-007D, 1.677E-007D, 3.1260000000000003E-008D, 1.4390000000000001E-007D
    };
    private static double b5[] = {
        1.7749999999999999D, 1.6799999999999999D, 1.6499999999999999D, 1.52D, 1.76D, 1.6899999999999999D, 1.762D, 1.8600000000000001D, 1.77D
    };
    private static double b6[] = {
        49.390000000000001D, 41.780000000000001D, 38.020000000000003D, 49.520000000000003D, 49.060000000000002D, 46.5D, 55.210000000000001D, 54.390000000000001D, 49.18D
    };
    private static double b7[] = {
        103.01000000000001D, 94.299999999999997D, 91.769999999999996D, 97.280000000000001D, 98.930000000000007D, 101.59D, 101.89D, 101.39D, 100.39D
    };
    private static double c0[] = {
        5.4419000000000004D, 5.4877000000000002D, 4.7697000000000003D, 6.4701000000000004D, 5.8635999999999999D, 4.7453000000000003D, 6.9657D, 6.5808999999999997D, 6.0397999999999996D
    };
    private static double c1[] = {
        3.7364000000000002D, 2.4672999999999998D, 2.7486999999999999D, 2.9820000000000002D, 3.0122D, 2.9581D, 3.6532D, 3.5470000000000002D, 2.5951D
    };
    private static double c2[] = {
        1.9457D, 1.7565999999999999D, 1.6797D, 1.7604D, 1.7335D, 1.9286000000000001D, 1.7658D, 1.7749999999999999D, 1.9153D
    };
    private static double c3[] = {
        1.845D, 1.9104000000000001D, 1.8793D, 1.7507999999999999D, 1.7452000000000001D, 1.7378D, 1.6268D, 1.7321D, 1.6541999999999999D
    };
    private static double c4[] = {
        415.91000000000003D, 510.07999999999998D, 343.24000000000001D, 198.33000000000001D, 216.91D, 247.68000000000001D, 114.39D, 219.53999999999999D, 186.66999999999999D
    };
    private static double c5[] = {
        0.1128D, 0.16220000000000001D, 0.26419999999999999D, 0.14319999999999999D, 0.16900000000000001D, 0.1842D, 0.13089999999999999D, 0.1704D, 0.1019D
    };
    private static double c6[] = {
        2.3538000000000001D, 2.1962999999999999D, 1.9549000000000001D, 2.2690000000000001D, 2.1985000000000001D, 2.0872999999999999D, 2.3285999999999998D, 2.1977000000000002D, 2.3954D
    };
    private static double d0[] = {
        10D, 5.5D, 3D, 5D, 5D, 8D, 8D, 8D, 8D
    };
    private static double d1[] = {
        -1D, 1.0D, 2D, 1.2D, 1.2D, 0.0D, 0.0D, 0.0D, 0.0D
    };
    private int timePercIndex;
    private UniformDistribution timePercentage;
    private int systemType;
    private boolean useUserSpecifiedLocalClutterHeight;
    private double userSpecifiedLocalClutterHeight;

}
