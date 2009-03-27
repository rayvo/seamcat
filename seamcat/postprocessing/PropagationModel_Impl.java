// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropagationModel_Impl.java

package org.seamcat.postprocessing;

import org.seamcat.model.plugin.PropagationModel;
import org.seamcat.model.propagation.PluginModelWrapper;
import org.seamcat.propagation.FreeSpaceModel;
import org.seamcat.propagation.HataSE21Model;
import org.seamcat.propagation.HataSE24Model;
import org.seamcat.propagation.R370Model;
import org.seamcat.propagation.SDModel;

public class PropagationModel_Impl
    implements PropagationModel
{

    public PropagationModel_Impl()
    {
    }

    public PropagationModel_Impl(org.seamcat.model.plugin.PropagationModel.AssumedModel as)
    {
        assumedModel = as;
    }

    public int getEnviroment()
    {
        return enviroment;
    }

    public org.seamcat.model.plugin.PropagationModel.AssumedModel getAssumedModel()
    {
        return assumedModel;
    }

    public void setAssumedModel(org.seamcat.model.plugin.PropagationModel.AssumedModel assumedModel)
    {
        this.assumedModel = assumedModel;
    }

    public void setEnviroment(int enviroment)
    {
        this.enviroment = enviroment;
    }

    public static PropagationModel create(org.seamcat.propagation.PropagationModel prop)
    {
        PropagationModel_Impl p = new PropagationModel_Impl();
        p.setEnviroment(prop.getGeneralEnv());
        if(prop instanceof R370Model)
            p.setAssumedModel(org.seamcat.model.plugin.PropagationModel.AssumedModel.ITU_P1546);
        else
        if(prop instanceof FreeSpaceModel)
            p.setAssumedModel(org.seamcat.model.plugin.PropagationModel.AssumedModel.FreeSpace);
        else
        if(prop instanceof SDModel)
            p.setAssumedModel(org.seamcat.model.plugin.PropagationModel.AssumedModel.SphericalDiffraction);
        else
        if(prop instanceof HataSE24Model)
            p.setAssumedModel(org.seamcat.model.plugin.PropagationModel.AssumedModel.Hata_SRD);
        else
        if(prop instanceof HataSE21Model)
            p.setAssumedModel(org.seamcat.model.plugin.PropagationModel.AssumedModel.Hata);
        else
        if(prop instanceof PluginModelWrapper)
            p.setAssumedModel(org.seamcat.model.plugin.PropagationModel.AssumedModel.UserDefinedPlugin);
        else
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown model type: ").append(prop.getClass().getName()).toString());
        return p;
    }

    protected int enviroment;
    protected org.seamcat.model.plugin.PropagationModel.AssumedModel assumedModel;
}
