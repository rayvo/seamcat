// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropagationModel.java

package org.seamcat.propagation;

import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.model.propagation.PluginModelWrapper;
import org.seamcat.model.technical.exception.ModelException;
import org.seamcat.presentation.MainWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.propagation:
//            R370Model, FreeSpaceModel, SDModel, HataSE24Model, 
//            HataSE21Model, PropagationModelConstants

public abstract class PropagationModel extends SeamcatComponent
    implements PropagationModelConstants
{

    public PropagationModel()
    {
        medianSelected = true;
        variationsSelected = true;
        txLocalEnv = 1;
        rxLocalEnv = 1;
        generalEnv = 2;
        propagEnv = 0;
    }

    public PropagationModel(Element element)
    {
        this(Integer.parseInt(element.getAttribute("txLocalEnv")), Integer.parseInt(element.getAttribute("rxLocalEnv")), Integer.parseInt(element.getAttribute("generalEnv")), Integer.parseInt(element.getAttribute("propagEnv")));
        setVariationsSelected(Boolean.valueOf(element.getAttribute("variationsSelected")).booleanValue());
        setMedianSelected(Boolean.valueOf(element.getAttribute("medianSelected")).booleanValue());
    }

    public PropagationModel(int txLocalEnv, int rxLocalEnv, int generalEnv, int propagEnv)
    {
        medianSelected = true;
        variationsSelected = true;
        this.txLocalEnv = 1;
        this.rxLocalEnv = 1;
        this.generalEnv = 2;
        this.propagEnv = 0;
        this.txLocalEnv = txLocalEnv;
        this.rxLocalEnv = rxLocalEnv;
        this.generalEnv = generalEnv;
        this.propagEnv = propagEnv;
    }

    public double medianLoss(double rFreq, double rDist, double rHTx, double rHRx)
        throws ModelException
    {
        boolean bMedianLoss = getMedianSelected();
        boolean bVariations = getVariationsSelected();
        setMedianSelected(true);
        setVariationsSelected(false);
        double rLoss = evaluate(rFreq, rDist, rHTx, rHRx);
        setMedianSelected(bMedianLoss);
        setVariationsSelected(bVariations);
        return rLoss;
    }

    public double getStdDev()
    {
        return stdDev;
    }

    public void setStdDev(double stdDev)
    {
        this.stdDev = stdDev;
    }

    public boolean getMedianSelected()
    {
        return medianSelected;
    }

    public void setMedianSelected(boolean value)
    {
        medianSelected = value;
    }

    public boolean getVariationsSelected()
    {
        return variationsSelected;
    }

    public void setVariationsSelected(boolean variationsSelected)
    {
        this.variationsSelected = variationsSelected;
    }

    public int getTxLocalEnv()
    {
        return txLocalEnv;
    }

    public void setTxLocalEnv(int txLocalEnv)
    {
        this.txLocalEnv = txLocalEnv;
    }

    public int getRxLocalEnv()
    {
        return rxLocalEnv;
    }

    public void setRxLocalEnv(int rxLocalEnv)
    {
        this.rxLocalEnv = rxLocalEnv;
    }

    public int getGeneralEnv()
    {
        return generalEnv;
    }

    public void setGeneralEnv(int generalEnv)
    {
        this.generalEnv = generalEnv;
    }

    public int getPropagEnv()
    {
        return propagEnv;
    }

    public void setPropagEnv(int propagEnv)
    {
        this.propagEnv = propagEnv;
    }

    public abstract double evaluate(double d, double d1, double d2, double d3)
        throws ModelException;

    public double variations(double rFreq, double rDist, double rHTx, double rHRx)
        throws ModelException
    {
        setMedianSelected(false);
        setVariationsSelected(true);
        return evaluate(rFreq, rDist, rHTx, rHRx);
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("GeneralParameters");
        element.setAttribute("medianSelected", Boolean.toString(getMedianSelected()));
        element.setAttribute("variationsSelected", Boolean.toString(getVariationsSelected()));
        element.setAttribute("generalEnv", Integer.toString(getGeneralEnv()));
        element.setAttribute("propagEnv", Integer.toString(getPropagEnv()));
        element.setAttribute("rxLocalEnv", Integer.toString(getRxLocalEnv()));
        element.setAttribute("txLocalEnv", Integer.toString(getTxLocalEnv()));
        element.setAttribute("stdDev", Double.toString(getStdDev()));
        return element;
    }

    public static PropagationModel create(Element element)
    {
        String propagationName = element.getNodeName();
        LOG.debug((new StringBuilder()).append("Creating propagation model (").append(propagationName).append(")").toString());
        PropagationModel p;
        if(propagationName != null)
        {
            if(propagationName.equals("R370Model"))
                p = new R370Model(element);
            else
            if(propagationName.equals("FreeSpaceModel"))
                p = new FreeSpaceModel(element);
            else
            if(propagationName.equals("SDModel"))
                p = new SDModel(element);
            else
            if(propagationName.equals("HataSE24Model"))
                p = new HataSE24Model(element);
            else
            if(propagationName.equals("HataSE21Model"))
                p = new HataSE21Model(element);
            else
            if(propagationName.equals("PluginModel"))
            {
                try
                {
                    p = new PluginModelWrapper(element);
                }
                catch(Exception e)
                {
                    JOptionPane.showMessageDialog(MainWindow.getInstance(), (new StringBuilder()).append("Could not instantiate plugin model <").append(e.getMessage()).append("> defaulting model to Extended HATA").toString(), "Error loading plugin model", 0);
                    p = new HataSE21Model();
                }
            } else
            {
                LOG.warn("Could not create propagation model");
                p = null;
            }
        } else
        {
            p = null;
        }
        return p;
    }

    public static final int resolvePropagEnv(String propagEnvStr)
    {
        int propagEnv;
        if(propagEnvStr.equals(ROOF[0]))
            propagEnv = 0;
        else
        if(propagEnvStr.equals(ROOF[1]))
            propagEnv = 1;
        else
            throw new IllegalArgumentException("Cannot resolve propagation environment string");
        return propagEnv;
    }

    public static final int resolveLocalEnv(String localEnvStr)
    {
        int localEnv;
        if(localEnvStr.equals(DOOR[0]))
            localEnv = 0;
        else
        if(localEnvStr.equals(DOOR[1]))
            localEnv = 1;
        else
            throw new IllegalArgumentException("Cannot resolve local environment string");
        return localEnv;
    }

    public static final int resolveGenEnv(String genEnvStr)
    {
        int genEnv;
        if(genEnvStr.equals(ENVIRONMENT[0]))
            genEnv = 0;
        else
        if(genEnvStr.equals(ENVIRONMENT[1]))
            genEnv = 1;
        else
        if(genEnvStr.equals(ENVIRONMENT[2]))
            genEnv = 2;
        else
            throw new IllegalArgumentException("Cannot resolve general environment string");
        return genEnv;
    }

    public abstract String toString();

    private static final Logger LOG = Logger.getLogger(org/seamcat/propagation/PropagationModel);
    public static final String MODEL_ID = "GeneralParameters";
    private double stdDev;
    private boolean medianSelected;
    private boolean variationsSelected;
    private int txLocalEnv;
    private int rxLocalEnv;
    private int generalEnv;
    private int propagEnv;

}
