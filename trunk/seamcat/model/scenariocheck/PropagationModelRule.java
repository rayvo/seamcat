// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropagationModelRule.java

package org.seamcat.model.scenariocheck;

import org.seamcat.propagation.*;

public class PropagationModelRule
{

    public PropagationModelRule()
    {
    }

    public static String validate(PropagationModel model, double rFreqMin, double rFreqMax)
    {
        String errorText = "OK";
        if(model instanceof SDModel)
        {
            if(rFreqMax < 1000D)
                errorText = "Spherical diffraction model not applicable below 1 GHz";
        } else
        if(model instanceof R370Model)
        {
            if(rFreqMin < 30D)
                errorText = "ITU-R P.1546 model not applicable below 30 MHz";
            if(rFreqMax > 3000D)
                errorText = "ITU-R P.1546 model not applicable above 3 GHz";
        } else
        if(model instanceof HataAndSDModel)
        {
            if(rFreqMin < 30D)
                errorText = "Hata model not applicable below 30 MHz";
            if(rFreqMax > 3000D)
                errorText = "Hata model not applicable above 3 GHz";
        }
        return errorText;
    }
}
