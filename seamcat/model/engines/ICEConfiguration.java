// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ICEConfiguration.java

package org.seamcat.model.engines;

import java.util.*;
import org.seamcat.function.Point2D;
import org.seamcat.model.Workspace;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.engines:
//            InterferenceCriterionType, ICE

public class ICEConfiguration
    implements InterferenceCriterionType
{

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("ICEConfiguration");
        element.setAttribute("unwanted", Boolean.toString(unwanted));
        element.setAttribute("blocking", Boolean.toString(blocking));
        element.setAttribute("intermodulation", Boolean.toString(intermodulation));
        element.setAttribute("allowIntermodulation", Boolean.toString(allowIntermodulation));
        element.setAttribute("numberOfSamples", Integer.toString(numberOfSamples));
        element.setAttribute("algorithm", Integer.toString(0));
        element.setAttribute("translationParameter", Integer.toString(translationParameter));
        element.setAttribute("translationMin", Double.toString(translationMin));
        element.setAttribute("translationMax", Double.toString(translationMax));
        element.setAttribute("translationPoints", Double.toString(translationPoints));
        element.setAttribute("probabilityResult", Double.toString(probabilityResult));
        element.setAttribute("hasBeenCalculated", Boolean.toString(hasBeenCalculated));
        element.setAttribute("signalsModifiedByPlugin", Boolean.toString(signalsModifiedByPlugin));
        element.setAttribute("interference-criterion", Integer.toString(interferenceCriterionType));
        Element translationPointResultsElement = doc.createElement("translationPointResults");
        Point2D p;
        for(Iterator i$ = translationPointResults.iterator(); i$.hasNext(); translationPointResultsElement.appendChild(p.toElement(doc)))
            p = (Point2D)i$.next();

        element.appendChild(translationPointResultsElement);
        return element;
    }

    public ICEConfiguration(Element element)
        throws Exception
    {
        interferenceCriterionType = 1;
        allowIntermodulation = true;
        translationPointResults = new ArrayList();
        unwanted = Boolean.valueOf(element.getAttribute("unwanted")).booleanValue();
        blocking = Boolean.valueOf(element.getAttribute("blocking")).booleanValue();
        intermodulation = Boolean.valueOf(element.getAttribute("intermodulation")).booleanValue();
        allowIntermodulation = Boolean.valueOf(element.getAttribute("allowIntermodulation")).booleanValue();
        numberOfSamples = Integer.parseInt(element.getAttribute("numberOfSamples"));
        try
        {
            interferenceCriterionType = Integer.parseInt(element.getAttribute("interference-criterion"));
        }
        catch(Exception e) { }
        translationParameter = Integer.parseInt(element.getAttribute("translationParameter"));
        translationMin = Double.parseDouble(element.getAttribute("translationMin"));
        translationMax = Double.parseDouble(element.getAttribute("translationMax"));
        translationPoints = Double.parseDouble(element.getAttribute("translationPoints"));
        probabilityResult = Double.parseDouble(element.getAttribute("probabilityResult"));
        hasBeenCalculated = Boolean.valueOf(element.getAttribute("unwanted")).booleanValue();
        signalsModifiedByPlugin = Boolean.valueOf(element.getAttribute("signalsModifiedByPlugin")).booleanValue();
        NodeList nl = element.getElementsByTagName("point2d");
        int x = 0;
        for(int size = nl.getLength(); x < size; x++)
            addTranslationPoint(new Point2D((Element)nl.item(x)));

    }

    public ICEConfiguration()
    {
        interferenceCriterionType = 1;
        allowIntermodulation = true;
        translationPointResults = new ArrayList();
        calculationModeIsTranslation = false;
        unwanted = true;
        blocking = false;
        intermodulation = false;
        interferenceCriterionType = 1;
        numberOfSamples = 20000;
        translationMin = 0.0D;
        translationMax = 100D;
        translationPoints = 100D;
        hasBeenCalculated = false;
        signalsModifiedByPlugin = false;
    }

    public ICEConfiguration(ICEConfiguration _ice)
    {
        interferenceCriterionType = 1;
        allowIntermodulation = true;
        translationPointResults = new ArrayList();
        calculationModeIsTranslation = _ice.calculationModeIsTranslation;
        unwanted = _ice.unwanted;
        blocking = _ice.blocking;
        intermodulation = _ice.intermodulation;
        interferenceCriterionType = _ice.interferenceCriterionType;
        numberOfSamples = _ice.numberOfSamples;
        translationMin = _ice.translationMin;
        translationMax = _ice.translationMax;
        translationPoints = _ice.translationPoints;
        translationParameter = _ice.translationParameter;
        hasBeenCalculated = false;
        signalsModifiedByPlugin = false;
        allowIntermodulation = _ice.allowIntermodulation;
    }

    public int validate(Workspace workspace, ICE ice)
    {
        int val;
        if(!unwanted && !blocking && !intermodulation)
            val = 1;
        else
        if(translationMax < translationMin)
            val = 2;
        else
            val = 0;
        return val;
    }

    public void setAllowIntermodulation(boolean value)
    {
        allowIntermodulation = value;
    }

    public boolean allowIntermodulation()
    {
        return allowIntermodulation;
    }

    public boolean getHasBeenCalculated()
    {
        return hasBeenCalculated;
    }

    public void setHasBeenCalculated(boolean value)
    {
        hasBeenCalculated = value;
    }

    public int getInterferenceCriterionType()
    {
        return interferenceCriterionType;
    }

    public int getTranslationParameter()
    {
        return translationParameter;
    }

    public boolean isUnwanted()
    {
        return unwanted;
    }

    public double getTranslationMax()
    {
        return translationMax;
    }

    public int getNumberOfSamples()
    {
        return numberOfSamples;
    }

    public double getTranslationPoints()
    {
        return translationPoints;
    }

    public boolean isBlocking()
    {
        return blocking;
    }

    public boolean isIntermodulation()
    {
        return intermodulation;
    }

    public boolean calculationModeIsTranslation()
    {
        return calculationModeIsTranslation;
    }

    public void setTranslationMin(double translationMin)
    {
        this.translationMin = translationMin;
    }

    public void setTranslationParameter(int translationParameter)
    {
        this.translationParameter = translationParameter;
    }

    public void setUnwanted(boolean unwanted)
    {
        this.unwanted = unwanted;
    }

    public void setTranslationMax(double translationMax)
    {
        this.translationMax = translationMax;
    }

    public void setNumberOfSamples(int numberOfSamples)
    {
        this.numberOfSamples = numberOfSamples;
    }

    public void setTranslationPoints(double translationPoints)
    {
        this.translationPoints = translationPoints;
    }

    public void setBlocking(boolean blocking)
    {
        this.blocking = blocking;
    }

    public void setInterferenceCriterionType(int interferenceCriterionType)
    {
        this.interferenceCriterionType = interferenceCriterionType;
    }

    public void setIntermodulation(boolean intermodulation)
    {
        this.intermodulation = intermodulation;
    }

    public void setCalculationModeIsTranslation(boolean calculationMode)
    {
        calculationModeIsTranslation = calculationMode;
    }

    public double getTranslationMin()
    {
        return translationMin;
    }

    public void addTranslationPoint(Point2D p)
    {
        translationPointResults.add(p);
    }

    public List getTranslationResults()
    {
        return translationPointResults;
    }

    public void setPropabilityResult(double value)
    {
        probabilityResult = value;
    }

    public double getPropabilityResult()
    {
        return probabilityResult;
    }

    public boolean isSignalsModifiedByPlugin()
    {
        return signalsModifiedByPlugin;
    }

    public void setSignalsModifiedByPlugin(boolean signalsModifiedByPlugin)
    {
        this.signalsModifiedByPlugin = signalsModifiedByPlugin;
    }

    private static final ResourceBundle STRINGLIST;
    public static final String ERROR[];
    public static final int EVERYTHING_OK = 0;
    public static final int NO_SIGNAL_TYPE = 1;
    public static final int MIN_GREATER_THAN_MAX = 2;
    public static final int SIGNAL_IS_CORRELATED = 3;
    public static final int SIGNAL_IS_CORRELATION_NOT_ACCEPTED = 4;
    private boolean calculationModeIsTranslation;
    private int interferenceCriterionType;
    private boolean unwanted;
    private boolean blocking;
    private boolean intermodulation;
    private boolean allowIntermodulation;
    private int numberOfSamples;
    private int translationParameter;
    private double translationMin;
    private double translationMax;
    private double translationPoints;
    private boolean hasBeenCalculated;
    private boolean signalsModifiedByPlugin;
    private double probabilityResult;
    private List translationPointResults;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        ERROR = (new String[] {
            STRINGLIST.getString("ICECONFIG_ERROR_OK"), STRINGLIST.getString("ICECONFIG_ERROR_ONE_SIGNAL"), STRINGLIST.getString("ICECONFIG_ERROR_MIN_LESS_THAN_MAX"), STRINGLIST.getString("ICECONFIG_ERROR_DRSS_IRSS_CORRELATED"), STRINGLIST.getString("ICECONFIG_ERROR_CANCELLED")
        });
    }
}
