// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Reportable.java

package org.seamcat.interfaces;


public interface Reportable
{

    public abstract String getReport(String s);

    public abstract String getStyleSheet(String s);

    public abstract void setStoreSignals(boolean flag);

    public abstract void setStoreControl(boolean flag);

    public abstract void setStoreScenario(boolean flag);

    public abstract void setStoreResults(boolean flag);

    public abstract void setExpandSignals(boolean flag);

    public static final String CONTENT_TYPE_HTML = "text/html";
    public static final String CONTENT_TYPE_XML = "text/xml";
    public static final String CONTENT_TYPE_CSV = "application/x-csv";
}