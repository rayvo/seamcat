// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BuiltInModel.java

package org.seamcat.propagation;

import org.seamcat.distribution.GaussianDistribution;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.propagation:
//            PropagationModel

public abstract class BuiltInModel extends PropagationModel
{

    public BuiltInModel()
    {
        variationsDistrib = new GaussianDistribution(0.0D, 1.0D);
        sameBuildingDistrib = new GaussianDistribution(0.0D, 1.0D);
    }

    public BuiltInModel(Element element)
    {
        super((Element)element.getElementsByTagName("GeneralParameters").item(0));
        variationsDistrib = new GaussianDistribution(0.0D, 1.0D);
        sameBuildingDistrib = new GaussianDistribution(0.0D, 1.0D);
    }

    public BuiltInModel(int txLocalEnv, int rxLocalEnv, int generalEnv, int propagEnv)
    {
        super(txLocalEnv, rxLocalEnv, generalEnv, propagEnv);
        variationsDistrib = new GaussianDistribution(0.0D, 1.0D);
        sameBuildingDistrib = new GaussianDistribution(0.0D, 1.0D);
    }

    public GaussianDistribution getVariationsDistrib()
    {
        return variationsDistrib;
    }

    public void setVariationsDistrib(GaussianDistribution variationsDistrib)
    {
        this.variationsDistrib = variationsDistrib;
    }

    public GaussianDistribution getSameBuildingDistrib()
    {
        return sameBuildingDistrib;
    }

    public void setSameBuildingDistrib(GaussianDistribution sameBuildingDistrib)
    {
        this.sameBuildingDistrib = sameBuildingDistrib;
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("BuiltInModel");
        element.appendChild(super.toElement(doc));
        return element;
    }

    public static final String MODEL_ID = "BuiltInModel";
    private GaussianDistribution variationsDistrib;
    private GaussianDistribution sameBuildingDistrib;
}
