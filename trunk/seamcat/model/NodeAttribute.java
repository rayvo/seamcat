// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   NodeAttribute.java

package org.seamcat.model;

import javax.swing.InputVerifier;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class NodeAttribute
{

    public NodeAttribute(String name, String unit, Object value, String type, Object allowableValues[], boolean allowableValuesOnly, boolean enabled, 
            InputVerifier inputVerifier)
    {
        batchParameter = false;
        this.name = name;
        this.value = value;
        this.unit = unit;
        this.type = type;
        this.allowableValues = allowableValues;
        this.allowableValuesOnly = allowableValuesOnly;
        this.enabled = enabled;
        this.inputVerifier = inputVerifier;
        if(inputVerifier == null && (value instanceof Integer))
            inputVerifier = SeamcatTextFieldFormats.INTEGER_JTEXTFIELD_VERIFIER;
    }

    public String getName()
    {
        return name;
    }

    public Object getValue()
    {
        return value;
    }

    public void setValue(Object value)
    {
        this.value = value;
    }

    public String getUnit()
    {
        return unit;
    }

    public String getType()
    {
        return type;
    }

    public Object[] getAllowableValues()
    {
        return allowableValues;
    }

    public boolean getAllowableValuesOnly()
    {
        return allowableValuesOnly;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setBatchParameter(boolean batchParameter)
    {
        this.batchParameter = batchParameter;
    }

    public InputVerifier getInputVerifier()
    {
        return inputVerifier;
    }

    public boolean isBatchParameter()
    {
        return batchParameter;
    }

    private String name;
    private Object value;
    private String unit;
    private String type;
    private Object allowableValues[];
    private boolean allowableValuesOnly;
    private boolean enabled;
    private InputVerifier inputVerifier;
    private boolean batchParameter;
}