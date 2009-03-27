// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   WantedTransmitter.java

package org.seamcat.model;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.model.technical.exception.GeometricException;
import org.seamcat.presentation.Node;
import org.seamcat.presentation.components.LocalComponent;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model:
//            Transmitter, NodeAttribute, Antenna, SeamcatComponent

public class WantedTransmitter extends Transmitter
{
    private class CoverageRadius extends SeamcatComponent
    {

        public String toString()
        {
            return "Coverage Radius";
        }

        protected void initNodeAttributes()
        {
            List nodeList = new ArrayList();
            nodeList.add(new NodeAttribute("Fixed coverage radius", "km", new Double(getFixedCoverageRadius()), "Double", null, false, true, null));
            nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
        }

        protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
        {
            aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
            switch(rowIndex)
            {
            case 0: // '\0'
                setFixedCoverageRadius(((Number)aValue).doubleValue());
                break;
            }
        }

        public Element toElement(Document doc)
        {
            return null;
        }

        public boolean isLeaf()
        {
            return false;
        }

        final WantedTransmitter this$0;

        private CoverageRadius()
        {
            this$0 = WantedTransmitter.this;
            super();
        }

    }


    public WantedTransmitter(Transmitter transmitter)
    {
        super(transmitter);
    }

    public WantedTransmitter(Element element)
    {
        super((Element)element.getElementsByTagName("transmitter").item(0));
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("WantedTransmitter");
        element.appendChild(super.toElement(doc));
        return element;
    }

    public void coverageRadiusTrafficWt(boolean debug)
        throws GeometricException
    {
        double rDens = getDens();
        double rFreqCluster = getFreqCluster();
        double rNbChannels = getNumberOfChannels();
        double rNbUsers = getNumberOfUsersPerChannel();
        try
        {
            double rRmax = Math.sqrt((rNbChannels * rNbUsers) / (3.1415926535897931D * rDens * rFreqCluster));
            LOG.debug((new StringBuilder()).append("Traffic Limited Calcalculation of Rmax = Math.sqrt((rNbChannels*rNbUsers)/(Math.PI*rDens*rFreqCluster)) = ").append(rRmax).append(" = Math.sqrt((").append(rNbChannels).append(" * ").append(rNbUsers).append(")/(Math.PI * ").append(rDens).append(" * ").append(rFreqCluster).append("))").toString());
            setRMax(rRmax);
        }
        catch(Exception e)
        {
            throw new GeometricException();
        }
    }

    public void coverageRadiusUserWt()
    {
        double rRmax = getCoverageRadius();
        LOG.debug((new StringBuilder()).append("User Defined specification of Rmax = ").append(rRmax).toString());
        setRMax(rRmax);
    }

    public Node getChildAt(int childIndex)
    {
        Node child;
        switch(childIndex)
        {
        case 0: // '\0'
            child = new CoverageRadius();
            break;

        case 1: // '\001'
            child = new LocalComponent(getAntenna(), "Antenna");
            break;

        default:
            throw new IllegalArgumentException((new StringBuilder()).append("ChildIndex out of range <").append(childIndex).append(">").toString());
        }
        return child;
    }

    public int getChildCount()
    {
        return 2;
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Reference", "", getReference(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Description", "", getDescription(), "String", null, false, true, null));
        nodeList.add(new NodeAttribute("Power supplied distribution", "dBm", getPowerSuppliedDistribution(), "Distribution", null, false, false, null));
        nodeList.add(new NodeAttribute("Use power control", "", getUsePowerControl() ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)), "Boolean", new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        }, false, true, null));
        nodeList.add(new NodeAttribute("Antenna Height", "m", getAntennaHeight(), "Distribution", null, false, false, null));
        nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    protected void setNodeAttributeValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        aValue = setTreeNodeValueAt(aValue, rowIndex, columnIndex);
        switch(rowIndex)
        {
        case 0: // '\0'
            setReference((String)aValue);
            break;

        case 1: // '\001'
            setDescription((String)aValue);
            break;

        case 2: // '\002'
            setPowerSuppliedDistribution((Distribution)aValue);
            break;

        case 3: // '\003'
            setUsePowerControl(((Boolean)aValue).booleanValue());
            break;

        case 4: // '\004'
            setUnwantedEmissions((DiscreteFunction2)aValue);
            break;

        case 5: // '\005'
            setUseUnwantedEmission(Boolean.getBoolean(aValue.toString()));
            break;

        case 6: // '\006'
            getAntenna().setReference((String)aValue);
            break;
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/WantedTransmitter);

}