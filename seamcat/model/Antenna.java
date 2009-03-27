// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Antenna.java

package org.seamcat.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.core.AntennaPattern;
import org.seamcat.model.core.HorizontalPattern;
import org.seamcat.model.core.SphericalPattern;
import org.seamcat.model.core.VerticalPattern;
import org.seamcat.model.technical.exception.PatternException;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model:
//            SeamcatComponent, NodeAttribute

public class Antenna extends SeamcatComponent
{

    public Antenna()
    {
        this(11D);
    }

    public Antenna(double _peakGain)
    {
        super("DEFAULT_ANT", new String());
        peakGain = 11D;
        useHorizontalPattern = false;
        useVerticalPattern = false;
        useSphericalPattern = false;
        horizontalPattern = new HorizontalPattern();
        verticalPattern = new VerticalPattern();
        sphericalPattern = new SphericalPattern();
        setRowClasses(rowTypes);
        setRowNames(rowNames);
        peakGain = _peakGain;
    }

    public Antenna(Element element)
    {
        this();
        setReference(element.getAttribute("reference"));
        setPeakGain(Double.parseDouble(element.getAttribute("peak-gain")));
        setUseHorizontalPattern(element.getAttribute("use-horizontal-pattern").equalsIgnoreCase("true"));
        setUseVerticalPattern(element.getAttribute("use-vertical-pattern").equalsIgnoreCase("true"));
        setUseSphericalPattern(element.getAttribute("use-spherical-pattern").equalsIgnoreCase("true"));
        NodeList nlHorizontal = element.getElementsByTagName("horizontal-pattern");
        if(nlHorizontal.getLength() > 0)
            setHorizontalPattern(new HorizontalPattern((Element)nlHorizontal.item(0).getFirstChild()));
        NodeList nlVertical = element.getElementsByTagName("vertical-pattern");
        if(nlVertical.getLength() > 0)
            setVerticalPattern(new VerticalPattern((Element)nlVertical.item(0).getFirstChild()));
        NodeList nlSpherical = element.getElementsByTagName("spherical-pattern");
        if(nlSpherical.getLength() > 0)
            setSphericalPattern(new SphericalPattern((Element)nlSpherical.item(0).getFirstChild()));
        if(element.getElementsByTagName("description").item(0).getFirstChild() != null)
            try
            {
                CDATASection datasection = (CDATASection)element.getElementsByTagName("description").item(0).getFirstChild();
                setDescription(datasection.getData());
            }
            catch(Exception e)
            {
                setDescription(element.getElementsByTagName("description").item(0).getFirstChild().toString());
            }
        else
            setDescription("");
        setRowClasses(rowTypes);
        setRowNames(rowNames);
    }

    public Antenna(Antenna antenna)
    {
        this();
        setReference(antenna.getReference());
        setDescription(antenna.getDescription());
        peakGain = antenna.getPeakGain();
        useHorizontalPattern = antenna.getUseHorizontalPattern();
        useVerticalPattern = antenna.getUseVerticalPattern();
        useSphericalPattern = antenna.getUseSphericalPattern();
        horizontalPattern = new HorizontalPattern(antenna.horizontalPattern);
        verticalPattern = new VerticalPattern(antenna.verticalPattern);
        sphericalPattern = new SphericalPattern(antenna.sphericalPattern);
        setRowClasses(rowTypes);
        setRowNames(rowNames);
    }

    public double calculateGain(double horisontalAngle, double verticalAngle)
        throws PatternException
    {
        double gain = 0.0D;
        boolean debug = LOG.isDebugEnabled();
        if(getUseSphericalPattern())
        {
            try
            {
                double sphericalAngle = Mathematics.acosD(Mathematics.cosD(verticalAngle) * Mathematics.cosD(horisontalAngle));
                double sphericalGain = gainS(sphericalAngle);
                gain = getPeakGain() + sphericalGain;
                if(debug)
                {
                    LOG.debug("Using spherical pattern");
                    LOG.debug((new StringBuilder()).append("Spherical angle (rSAng) = ").append(sphericalAngle).append(" = acosD(cosD(RxTXElev [").append(verticalAngle).append("]) * cosD(RxTxAzi [").append(horisontalAngle).append("]))").toString());
                    LOG.debug((new StringBuilder()).append("VrSResult = getVictimReceiver().getAntenna().gainS(").append(sphericalGain).append(") = ").append(sphericalGain).toString());
                }
            }
            catch(Exception e)
            {
                throw new PatternException((new StringBuilder()).append("Unable to calculate spherical gain, due to: \n").append(e.getMessage()).toString(), e);
            }
            if(debug)
                LOG.debug((new StringBuilder()).append("Antenna Gain = ").append(gain).toString());
        } else
        {
            double horiGain = 0.0D;
            double vertiGain = 0.0D;
            if(getUseHorizontalPattern())
                try
                {
                    horiGain = gainH(horisontalAngle);
                    if(debug)
                    {
                        LOG.debug("Using horizontal pattern:");
                        LOG.debug((new StringBuilder()).append("horizontailGain = gainH(horisontalAngle) -> ").append(horiGain).append(" = gainH(").append(horisontalAngle).append(")").toString());
                    }
                }
                catch(Exception e)
                {
                    throw new PatternException((new StringBuilder()).append("Unable to calculate horizontal gain, due to: \n").append(e.getMessage()).toString(), e);
                }
            if(getUseVerticalPattern())
                try
                {
                    vertiGain = gainV(verticalAngle);
                    if(debug)
                    {
                        LOG.debug("Using vertical pattern:");
                        LOG.debug((new StringBuilder()).append("verticalGain = gainV(verticalAngle) -> ").append(vertiGain).append(" = gainV(").append(verticalAngle).append(")").toString());
                    }
                }
                catch(Exception e)
                {
                    throw new PatternException((new StringBuilder()).append("Unable to calculate vertical gain, due to: \n").append(e.getMessage()).toString(), e);
                }
            gain = getPeakGain() + horiGain + vertiGain;
        }
        return gain;
    }

    public double gainV(double verticalAngle)
        throws PatternException
    {
        double rResult = 0.0D;
        try
        {
            rResult = getVerticalPattern().getPattern().evaluate(verticalAngle);
        }
        catch(Exception e)
        {
            throw new PatternException(e);
        }
        return rResult;
    }

    public double gainH(double horizontalAngle)
        throws PatternException
    {
        double rResult = 0.0D;
        try
        {
            rResult = getHorizontalPattern().getPattern().evaluate(horizontalAngle);
        }
        catch(Exception e)
        {
            throw new PatternException(e);
        }
        return rResult;
    }

    public double gainS(double sphericalAngle)
        throws PatternException
    {
        double rResult = 0.0D;
        try
        {
            rResult = getSphericalPattern().getPattern().evaluate(sphericalAngle);
        }
        catch(Exception e)
        {
            throw new PatternException();
        }
        return rResult;
    }

    public boolean equals(Object o)
    {
        if(o instanceof Antenna)
            return getReference().equals(((Antenna)o).getReference());
        else
            return false;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Reference", "", getReference(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Description", "", getDescription(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Peak Gain", "dB", new Double(getPeakGain()), "Double", null, false, true, null));
        nodeList.add(new NodeAttribute("Use horizontal pattern", "", getUseHorizontalPattern() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, true, true, null));
        nodeList.add(new NodeAttribute("Use vertical pattern", "", getUseVerticalPattern() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, true, true, null));
        nodeList.add(new NodeAttribute("Use spherical pattern", "", getUseSphericalPattern() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, true, true, null));
        nodeAttributes = (NodeAttribute[])(NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
        switch(rowIndex)
        {
        default:
            break;

        case 0: // '\0'
            setReference((String)aValue);
            fireTableChangedListeners();
            break;

        case 1: // '\001'
            setDescription((String)aValue);
            break;

        case 2: // '\002'
            setPeakGain(((Number)aValue).doubleValue());
            break;

        case 3: // '\003'
            boolean useHPattern = Boolean.getBoolean(aValue.toString());
            if(useHPattern && getUseSphericalPattern())
                nodeAttributes[5].setValue(Boolean.FALSE);
            setUseHorizontalPattern(useHPattern);
            break;

        case 4: // '\004'
            boolean useVPattern = Boolean.getBoolean(aValue.toString());
            if(useVPattern && getUseSphericalPattern())
                nodeAttributes[5].setValue(Boolean.FALSE);
            setUseVerticalPattern(useVPattern);
            break;

        case 5: // '\005'
            boolean useSPattern = Boolean.getBoolean(aValue.toString());
            if(useSPattern)
            {
                if(getUseVerticalPattern())
                    nodeAttributes[4].setValue(Boolean.FALSE);
                if(getUseHorizontalPattern())
                    nodeAttributes[3].setValue(Boolean.FALSE);
            }
            setUseSphericalPattern(useSPattern);
            break;
        }
    }

    public double getPeakGain()
    {
        return peakGain;
    }

    public boolean getUseHorizontalPattern()
    {
        return useHorizontalPattern;
    }

    public boolean getUseVerticalPattern()
    {
        return useVerticalPattern;
    }

    public boolean getUseSphericalPattern()
    {
        return useSphericalPattern;
    }

    public AntennaPattern getHorizontalPattern()
    {
        return horizontalPattern;
    }

    public AntennaPattern getVerticalPattern()
    {
        return verticalPattern;
    }

    public AntennaPattern getSphericalPattern()
    {
        return sphericalPattern;
    }

    public void setPeakGain(double peakGain)
    {
        this.peakGain = peakGain;
    }

    public void setUseHorizontalPattern(boolean useHorizontalPattern)
    {
        this.useHorizontalPattern = useHorizontalPattern;
        if(useHorizontalPattern)
            useSphericalPattern = false;
    }

    public void setUseVerticalPattern(boolean useVerticalPattern)
    {
        this.useVerticalPattern = useVerticalPattern;
        if(useVerticalPattern)
            useSphericalPattern = false;
    }

    public void setUseSphericalPattern(boolean useSphericalPattern)
    {
        this.useSphericalPattern = useSphericalPattern;
        if(useSphericalPattern)
        {
            useHorizontalPattern = false;
            useVerticalPattern = false;
        }
    }

    public Element toElement(Document doc)
    {
        Element antenna = doc.createElement("antenna");
        antenna.setAttribute("reference", super.getReference());
        antenna.setAttribute("peak-gain", String.valueOf(getPeakGain()));
        antenna.setAttribute("use-horizontal-pattern", String.valueOf(getUseHorizontalPattern()));
        antenna.setAttribute("use-vertical-pattern", String.valueOf(getUseVerticalPattern()));
        antenna.setAttribute("use-spherical-pattern", String.valueOf(getUseSphericalPattern()));
        Element description = doc.createElement("description");
        description.appendChild(doc.createCDATASection(super.getDescription()));
        antenna.appendChild(description);
        Element horizontalPattern_elem = doc.createElement("horizontal-pattern");
        horizontalPattern_elem.appendChild(getHorizontalPattern().toElement(doc));
        antenna.appendChild(horizontalPattern_elem);
        Element verticalPattern_elem = doc.createElement("vertical-pattern");
        verticalPattern_elem.appendChild(getVerticalPattern().toElement(doc));
        antenna.appendChild(verticalPattern_elem);
        Element sphericalPattern_elem = doc.createElement("spherical-pattern");
        sphericalPattern_elem.appendChild(getSphericalPattern().toElement(doc));
        antenna.appendChild(sphericalPattern_elem);
        return antenna;
    }

    public double getFreqMin()
    {
        return freqMin;
    }

    public void setFreqMin(double freqMin)
    {
        this.freqMin = freqMin;
    }

    public void setSphericalPattern(AntennaPattern sphericalPattern)
    {
        this.sphericalPattern = sphericalPattern;
        nodeAttributeIsDirty = true;
    }

    public void setVerticalPattern(AntennaPattern verticalPattern)
    {
        this.verticalPattern = verticalPattern;
        nodeAttributeIsDirty = true;
    }

    public void setHorizontalPattern(AntennaPattern horizontalPattern)
    {
        this.horizontalPattern = horizontalPattern;
        nodeAttributeIsDirty = true;
    }

    public double getFreqMax()
    {
        return freqMax;
    }

    public void setFreqMax(double _freqMax)
    {
        freqMax = _freqMax;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/Antenna);
    private double peakGain;
    private boolean useHorizontalPattern;
    private boolean useVerticalPattern;
    private boolean useSphericalPattern;
    private AntennaPattern horizontalPattern;
    private AntennaPattern verticalPattern;
    private AntennaPattern sphericalPattern;
    private double freqMin;
    private double freqMax;

}