// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferenceCriterionType.java

package org.seamcat.model.engines;

import java.util.Locale;
import java.util.ResourceBundle;

public interface InterferenceCriterionType
{

    public abstract int getInterferenceCriterionType();

    public static final ResourceBundle STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    public static final int CI = 1;
    public static final int CNI = 2;
    public static final int INI = 3;
    public static final int IN = 4;
    public static final String INTERFERENCE_CRITERION_TYPES[] = {
        STRINGLIST.getString("INTERFERENCE_CRITERION_TYPE_CI"), STRINGLIST.getString("INTERFERENCE_CRITERION_TYPE_CNI"), STRINGLIST.getString("INTERFERENCE_CRITERION_TYPE_INI"), STRINGLIST.getString("INTERFERENCE_CRITERION_TYPE_IN")
    };

}
