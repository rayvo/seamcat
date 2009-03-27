// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PostProcessingPluginWrapper.java

package org.seamcat.postprocessing;

import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.model.plugin.*;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.postprocessing:
//            ParameterFactory_Impl

public class PostProcessingPluginWrapper extends SeamcatComponent
    implements PostProcessingPlugin
{

    public PostProcessingPluginWrapper()
    {
        super("Unknown plugin", null);
    }

    public String toString()
    {
        return (new StringBuilder()).append(getReference()).append(" <").append(className).append(">").toString();
    }

    public PostProcessingPluginWrapper(PostProcessingPluginWrapper p)
    {
        setClassName(p.className);
        setReference(p.getReference());
        for(int i = 1; i <= p.getNumberOfParameters(); i++)
            setParameterValue(i, p.getParameterValue(i));

    }

    public PostProcessingPluginWrapper(Element elem)
    {
        super(elem.getAttribute("reference"), null);
        setClassName(elem.getAttribute("className"));
        NodeList nl = elem.getElementsByTagName("PostProcessingParameter");
        int i = 0;
        for(int stop = nl.getLength(); i < stop; i++)
        {
            Element e = (Element)nl.item(i);
            int index = Integer.parseInt(e.getAttribute("parameterid"));
            plugin.setParameterValue(index, initParameter((Element)nl.item(i)));
        }

    }

    public void setClassName(String className)
    {
        this.className = className;
        try
        {
            plugin = (PostProcessingPlugin)Class.forName(className).newInstance();
            plugin.init(PARAMETER_FACTORY);
        }
        catch(Exception e)
        {
            plugin = null;
            LOG.error((new StringBuilder()).append("Unable to load class: ").append(className).toString());
        }
    }

    public String getDescription()
    {
        if(plugin == null)
            return "Plugin not loaded";
        else
            return plugin.getDescription();
    }

    public PostProcessingPlugin getPluginInstance()
    {
        return plugin;
    }

    public boolean verify()
    {
        return plugin != null;
    }

    public String getClassName()
    {
        return className;
    }

    public void process(ScenarioInfo info)
        throws Exception
    {
        plugin.process(info);
    }

    public int getNumberOfParameters()
    {
        if(plugin == null)
            return 0;
        else
            return plugin.getNumberOfParameters();
    }

    public org.seamcat.model.plugin.PostProcessingPlugin.ParameterType getParameterType(int index)
    {
        if(plugin == null)
            return null;
        else
            return plugin.getParameterType(index);
    }

    public void setParameterValue(int index, Object newValue)
    {
        if(plugin != null)
            plugin.setParameterValue(index, newValue);
    }

    public Object getParameterValue(int index)
    {
        if(plugin == null)
            return null;
        else
            return plugin.getParameterValue(index);
    }

    public String getParameterName(int index)
    {
        if(plugin == null)
            return null;
        else
            return plugin.getParameterName(index);
    }

    private static Object initParameter(Element e)
    {
        Object value = null;
        org.seamcat.model.plugin.PostProcessingPlugin.ParameterType type = org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.valueOf(e.getAttribute("parametertype"));
        static class _cls1
        {

            static final int $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[];

            static 
            {
                $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType = new int[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.values().length];
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Boolean.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Integer.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Double.ordinal()] = 3;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Distribution.ordinal()] = 4;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Function2D.ordinal()] = 5;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.Function3D.ordinal()] = 6;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PostProcessingPlugin$ParameterType[org.seamcat.model.plugin.PostProcessingPlugin.ParameterType.String.ordinal()] = 7;
                }
                catch(NoSuchFieldError ex) { }
            }
        }

        switch(_cls1..SwitchMap.org.seamcat.model.plugin.PostProcessingPlugin.ParameterType[type.ordinal()])
        {
        case 1: // '\001'
            value = Boolean.valueOf(((Element)e.getElementsByTagName("boolean").item(0)).getAttribute("value").equalsIgnoreCase("true"));
            break;

        case 2: // '\002'
            value = Integer.valueOf(Integer.parseInt(((Element)e.getElementsByTagName("integer").item(0)).getAttribute("value")));
            break;

        case 3: // '\003'
            value = Double.valueOf(Double.parseDouble(((Element)e.getElementsByTagName("double").item(0)).getAttribute("value")));
            break;

        case 4: // '\004'
            value = Distribution.create((Element)e.getElementsByTagName("distribution").item(0));
            break;

        case 5: // '\005'
            value = new DiscreteFunction((Element)e.getElementsByTagName("discretefunction").item(0));
            break;

        case 6: // '\006'
            value = new DiscreteFunction2((Element)e.getElementsByTagName("discretefunction2").item(0));
            break;

        case 7: // '\007'
            value = ((Element)e.getElementsByTagName("string").item(0)).getAttribute("value");
            break;
        }
        return value;
    }

    public Element toElement(Document doc)
    {
        Element elem = doc.createElement("PostProcessingPlugin");
        elem.setAttribute("className", className);
        elem.setAttribute("reference", getReference());
        if(plugin != null)
        {
            int i = 1;
            for(int stop = plugin.getNumberOfParameters(); i <= stop; i++)
            {
                Element param = doc.createElement("PostProcessingParameter");
                org.seamcat.model.plugin.PostProcessingPlugin.ParameterType type = plugin.getParameterType(i);
                param.setAttribute("parameterid", Integer.toString(i));
                param.setAttribute("parametertype", type.toString());
                switch(_cls1..SwitchMap.org.seamcat.model.plugin.PostProcessingPlugin.ParameterType[type.ordinal()])
                {
                case 1: // '\001'
                {
                    Element par = doc.createElement("boolean");
                    par.setAttribute("value", plugin.getParameterValue(i).toString());
                    param.appendChild(par);
                    break;
                }

                case 2: // '\002'
                {
                    Element par = doc.createElement("integer");
                    par.setAttribute("value", plugin.getParameterValue(i).toString());
                    param.appendChild(par);
                    break;
                }

                case 3: // '\003'
                {
                    Element par = doc.createElement("double");
                    par.setAttribute("value", plugin.getParameterValue(i).toString());
                    param.appendChild(par);
                    break;
                }

                case 4: // '\004'
                {
                    Element par = ((Distribution)plugin.getParameterValue(i)).toElement(doc);
                    param.appendChild(par);
                    break;
                }

                case 5: // '\005'
                {
                    Element par = ((DiscreteFunction)plugin.getParameterValue(i)).toElement(doc);
                    param.appendChild(par);
                    break;
                }

                case 6: // '\006'
                {
                    Element par = ((DiscreteFunction2)plugin.getParameterValue(i)).toElement(doc);
                    param.appendChild(par);
                    break;
                }

                case 7: // '\007'
                {
                    Element par = doc.createElement("string");
                    par.setAttribute("value", plugin.getParameterValue(i).toString());
                    param.appendChild(par);
                    break;
                }
                }
                elem.appendChild(param);
            }

        }
        return elem;
    }

    protected void initNodeAttributes()
    {
    }

    public void init(ParameterFactory fact)
    {
        plugin.init(fact);
    }

    public void cleanUp()
    {
        try
        {
            plugin.cleanUp();
        }
        catch(Exception e)
        {
            LOG.error((new StringBuilder()).append("Plugin: ").append(getReference()).append(" threw exception during cleanup").toString(), e);
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/postprocessing/PostProcessingPluginWrapper);
    private static final ParameterFactory PARAMETER_FACTORY = new ParameterFactory_Impl();
    private PostProcessingPlugin plugin;
    private String className;

}
