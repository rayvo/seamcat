// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchData.java

package org.seamcat.presentation.components;

import java.util.*;

public interface BatchData
{

    public abstract String getValueString(int i);

    public abstract List getParameterData();

    public static final ResourceBundle STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    public static final int WORKSPACE = 0;
    public static final int PROBABILITY = 1;
    public static final int ERRORS = 2;
    public static final int WARNINGS = 3;
    public static final int ALGORITHM = 4;
    public static final int CRITERION = 5;
    public static final int UNWANTED = 6;
    public static final int BLOCKING = 7;
    public static final int INTERMOD = 8;
    public static final int SAMPLES = 9;
    public static final String MASTER_COLUMN_NAMES[] = {
        STRINGLIST.getString("BATCHDATA_WORKSPACE"), STRINGLIST.getString("BATCHDATA_PROBABILITY"), STRINGLIST.getString("BATCHDATA_ERRORS"), STRINGLIST.getString("BATCHDATA_WARNINGS"), STRINGLIST.getString("BATCHDATA_ALGORITHM"), STRINGLIST.getString("BATCHDATA_CRITERION"), STRINGLIST.getString("BATCHDATA_UNWANTED"), STRINGLIST.getString("BATCHDATA_BLOCKING"), STRINGLIST.getString("BATCHDATA_INTERMOD"), STRINGLIST.getString("BATCHDATA_SAMPLES")
    };

}
