// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropagationModelConstants.java

package org.seamcat.propagation;


public interface PropagationModelConstants
{

    public static final int INDOOR = 0;
    public static final int OUTDOOR = 1;
    public static final int URBAN = 0;
    public static final int SUBURBAN = 1;
    public static final int RURAL = 2;
    public static final int ABOVE_ROOF = 0;
    public static final int BELOW_ROOF = 1;
    public static final int BELOW_1MHZ = 0;
    public static final int ABOVE_1MHZ = 1;
    public static final int ANALOGUE = 2;
    public static final String ENVIRONMENT[] = {
        "Urban", "Suburban", "Rural"
    };
    public static final String DOOR[] = {
        "Indoor", "Outdoor"
    };
    public static final String ROOF[] = {
        "Above Roof", "Below Roof"
    };
    public static final String SYSTEM[] = {
        "Digital (Bw < 1MHz)", "Digital (Bw > 1MHz)", "Analogue"
    };

}
