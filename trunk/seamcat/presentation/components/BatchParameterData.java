// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchParameterData.java

package org.seamcat.presentation.components;

import java.util.Locale;
import java.util.ResourceBundle;

public interface BatchParameterData
{

    public abstract String getValueString(int i);

    public static final ResourceBundle stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    public static final int PARAMETER = 0;
    public static final int TYPE = 1;
    public static final int NEW_VALUE = 2;
    public static final int OLD_VALUE = 3;
    public static final String COLUMN_NAMES[] = {
        stringlist.getString("DetailModel.parameter"), stringlist.getString("DetailModel.type"), stringlist.getString("DetailModel.new_value"), stringlist.getString("DetailModel.old_value")
    };

}
