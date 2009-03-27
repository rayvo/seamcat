// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Constants.java

package org.seamcat.presentation.xmleditor;


public interface Constants
{

    public static final long MAXFILESIZE = 0xffffffffL;
    public static final long MAXLINENUMBER = 0xffffffffL;
    public static final int ELEMENT_NAME = 1;
    public static final int ELEMENT_PREFIX = 2;
    public static final int ELEMENT_VALUE = 3;
    public static final int ATTRIBUTE_NAME = 5;
    public static final int ATTRIBUTE_PREFIX = 6;
    public static final int ATTRIBUTE_VALUE = 7;
    public static final int NAMESPACE_NAME = 10;
    public static final int NAMESPACE_PREFIX = 11;
    public static final int NAMESPACE_VALUE = 12;
    public static final int ENTITY = 15;
    public static final int COMMENT = 16;
    public static final int DECLARATION = 17;
    public static final int SPECIAL = 20;
    public static final int STRING = 21;
    public static final String TEXT_VALUES[] = {
        null, "element-name", "element-prefix", "element-value", null, "attribute-name", "attribute-prefix", "attribute-value", null, null, 
        "namespace-name", "namespace-prefix", "namespace-value", null, null, "entity", "comment", "declaration", null, null, 
        "special", "string"
    };
    public static final int MAX_TOKENS = TEXT_VALUES.length;

}
