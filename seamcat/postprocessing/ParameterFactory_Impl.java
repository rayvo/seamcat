// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParameterFactory_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.distribution.*;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.model.plugin.*;

public class ParameterFactory_Impl
    implements ParameterFactory
{

    public ParameterFactory_Impl()
    {
    }

    public transient PluginDistribution createDistribution(org.seamcat.model.plugin.PluginDistribution.DistributionType type, double parameters[])
    {
        static class _cls1
        {

            static final int $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[];

            static 
            {
                $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType = new int[org.seamcat.model.plugin.PluginDistribution.DistributionType.values().length];
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.Constant.ordinal()] = 1;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.Uniform.ordinal()] = 2;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.Gaussian.ordinal()] = 3;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.Rayleigh.ordinal()] = 4;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.DiscreteUniform.ordinal()] = 5;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.PolarAngle.ordinal()] = 6;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.PolarDistance.ordinal()] = 7;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.UserDefined.ordinal()] = 8;
                }
                catch(NoSuchFieldError ex) { }
                try
                {
                    $SwitchMap$org$seamcat$model$plugin$PluginDistribution$DistributionType[org.seamcat.model.plugin.PluginDistribution.DistributionType.UserDefinedStair.ordinal()] = 9;
                }
                catch(NoSuchFieldError ex) { }
            }
        }

        Distribution newObj;
        switch(_cls1..SwitchMap.org.seamcat.model.plugin.PluginDistribution.DistributionType[type.ordinal()])
        {
        case 1: // '\001'
            if(parameters.length > 0)
                newObj = new ConstantDistribution(parameters[0]);
            else
                newObj = new ConstantDistribution();
            break;

        case 2: // '\002'
            if(parameters.length > 1)
                newObj = new UniformDistribution(parameters[0], parameters[1]);
            else
                newObj = new UniformDistribution();
            break;

        case 3: // '\003'
            if(parameters.length > 1)
                newObj = new GaussianDistribution(parameters[0], parameters[1]);
            else
                newObj = new GaussianDistribution();
            break;

        case 4: // '\004'
            if(parameters.length > 1)
                newObj = new RayleighDistribution(parameters[0], parameters[1]);
            else
                newObj = new RayleighDistribution();
            break;

        case 5: // '\005'
            if(parameters.length > 2)
                newObj = new DiscreteUniformDistribution(parameters[0], parameters[1], parameters[2]);
            else
                newObj = new DiscreteUniformDistribution();
            break;

        case 6: // '\006'
            if(parameters.length > 0)
                newObj = new UniformPolarAngleDistribution(parameters[0]);
            else
                newObj = new UniformPolarAngleDistribution();
            break;

        case 7: // '\007'
            if(parameters.length > 0)
                newObj = new UniformPolarDistanceDistribution(parameters[0]);
            else
                newObj = new UniformPolarDistanceDistribution();
            break;

        case 8: // '\b'
            newObj = new ContinousDistribution();
            break;

        case 9: // '\t'
            newObj = new StairDistribution();
            break;

        default:
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown Distribution class type: ").append(type).toString());
        }
        return newObj;
    }

    public Function2D createFunction2D()
    {
        return new DiscreteFunction();
    }

    public Function3D createFunction3D()
    {
        return new DiscreteFunction2();
    }
}
