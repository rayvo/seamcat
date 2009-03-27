// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Icons.java

package org.seamcat.presentation;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Icons
{

    private Icons()
    {
    }

    public static String getString(String key)
    {
        try
        {
            return RESOURCE_BUNDLE.getString(key);
        }
        catch(MissingResourceException e)
        {
            return (new StringBuilder()).append('!').append(key).append('!').toString();
        }
    }

    private static final String BUNDLE_NAME = "org.seamcat.presentation.seamcat_icons";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("org.seamcat.presentation.seamcat_icons");

}
