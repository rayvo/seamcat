// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PluginModelWrapper.java

package org.seamcat.model.propagation;

import org.apache.log4j.Logger;
import org.seamcat.propagation.PropagationModel;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.model.propagation:
//            PluginModel

public class PluginModelWrapper extends PropagationModel
{

    public PluginModelWrapper()
    {
        param1 = 0.0D;
        param2 = 0.0D;
        param3 = 0.0D;
        setReference("USER_PLUGIN");
        try
        {
            setClassName("");
        }
        catch(Exception e) { }
    }

    public String toString()
    {
        return getReference();
    }

    public PluginModelWrapper(PluginModelWrapper plugin)
        throws ClassNotFoundException
    {
        param1 = 0.0D;
        param2 = 0.0D;
        param3 = 0.0D;
        if(plugin == null)
        {
            throw new IllegalArgumentException("Plugin can not be null");
        } else
        {
            setReference(plugin.getReference());
            setDescription(plugin.getDescription());
            setClassName(plugin.getPluginClassName());
            param1 = plugin.param1;
            param2 = plugin.param2;
            param3 = plugin.param3;
            return;
        }
    }

    public PluginModelWrapper(String className)
        throws ClassNotFoundException
    {
        this();
        setClassName(className);
    }

    public PluginModelWrapper(Element element)
        throws ClassNotFoundException
    {
        super((Element)element.getElementsByTagName("GeneralParameters").item(0));
        param1 = 0.0D;
        param2 = 0.0D;
        param3 = 0.0D;
        setReference(element.getAttribute("reference"));
        setClassName(element.getAttribute("class"));
        try
        {
            setParam1(Double.parseDouble(element.getAttribute("param1")));
            setParam2(Double.parseDouble(element.getAttribute("param2")));
            setParam3(Double.parseDouble(element.getAttribute("param3")));
        }
        catch(Exception ex1) { }
        try
        {
            CDATASection datasection = (CDATASection)element.getElementsByTagName("description").item(0).getFirstChild();
            setDescription(datasection.getData());
        }
        catch(Exception ex)
        {
            setDescription("");
        }
    }

    public String getPluginClassName()
    {
        return className;
    }

    public void setPlugin(PluginModel plugin)
        throws ClassNotFoundException
    {
        this.plugin = plugin;
        if(plugin != null)
            setClassName(plugin.getClass().getName());
    }

    public void setClassName(String className)
        throws ClassNotFoundException
    {
        this.className = className;
        try
        {
            plugin = (PluginModel)Class.forName(className).newInstance();
        }
        catch(Throwable e)
        {
            plugin = null;
            LOG.error((new StringBuilder()).append("Unable to load class: ").append(className).toString());
            throw new ClassNotFoundException(className, e);
        }
    }

    public PluginModel getPlugin()
    {
        return plugin;
    }

    public String getClassName()
    {
        return className;
    }

    protected void initNodeAttributes()
    {
    }

    public boolean verify()
    {
        return plugin != null;
    }

    public double evaluate(double rFreq, double rDist, double rHTx, double rHRx)
    {
        return plugin.evaluate(rFreq, rDist, rHTx, rHRx, getGeneralEnv(), getParam1(), getParam2(), getParam3());
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("PluginModel");
        element.setAttribute("reference", getReference());
        if(plugin != null)
            element.setAttribute("class", plugin.getClass().getName());
        else
            element.setAttribute("class", className);
        element.setAttribute("param1", Double.toString(getParam1()));
        element.setAttribute("param2", Double.toString(getParam2()));
        element.setAttribute("param3", Double.toString(getParam3()));
        Element descriptionElement = doc.createElement("description");
        descriptionElement.appendChild(doc.createCDATASection(getDescription()));
        element.appendChild(descriptionElement);
        element.appendChild(super.toElement(doc));
        return element;
    }

    public boolean equals(Object o)
    {
        return (o instanceof PluginModelWrapper) && ((PluginModelWrapper)o).getReference().equals(getReference());
    }

    public double getParam1()
    {
        return param1;
    }

    public void setParam1(double param1)
    {
        this.param1 = param1;
    }

    public double getParam2()
    {
        return param2;
    }

    public void setParam2(double param2)
    {
        this.param2 = param2;
    }

    public double getParam3()
    {
        return param3;
    }

    public void setParam3(double param3)
    {
        this.param3 = param3;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/propagation/PluginModelWrapper);
    public static final String MODEL_ID = "PluginModel";
    private PluginModel plugin;
    private String className;
    private double param1;
    private double param2;
    private double param3;

}
