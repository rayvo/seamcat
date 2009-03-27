// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BatchItemControl.java

package org.seamcat.batch;

import javax.swing.JOptionPane;
import org.seamcat.model.engines.ICEConfiguration;
import org.seamcat.presentation.MainWindow;
import org.w3c.dom.*;

public class BatchItemControl
{

    public BatchItemControl()
    {
        iceConf = new ICEConfiguration();
    }

    public BatchItemControl(Element element)
    {
        processed = Boolean.valueOf(element.getAttribute("processed")).booleanValue();
        try
        {
            iceConf = new ICEConfiguration((Element)element.getElementsByTagName("ICEConfiguration").item(0));
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(MainWindow.getInstance(), (new StringBuilder()).append("Ignoring ICE Results!\nCause: ").append(e.getMessage()).toString(), "Ignoring stored results", 2);
        }
    }

    public ICEConfiguration getIceConfiguration()
    {
        return iceConf;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("batch-item-control");
        element.setAttribute("processed", Boolean.toString(processed));
        element.appendChild(iceConf.toElement(doc));
        return element;
    }

    public boolean isProcessed()
    {
        return processed;
    }

    public void setSamples(int samples)
    {
        iceConf.setNumberOfSamples(samples);
    }

    public void setProcessed(boolean processed)
    {
        this.processed = processed;
    }

    public void setInterferenceCriterion(int type)
    {
        iceConf.setInterferenceCriterionType(type);
    }

    public int getInterenceCriterionType()
    {
        return iceConf.getInterferenceCriterionType();
    }

    public int getSamples()
    {
        return iceConf.getNumberOfSamples();
    }

    private boolean processed;
    private ICEConfiguration iceConf;
}