// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogLineCatcher.java

package org.seamcat.presentation;

import java.util.ArrayList;
import org.apache.log4j.*;
import org.apache.log4j.spi.*;

public class LogLineCatcher
    implements Appender
{

    public LogLineCatcher(Level _level)
    {
        changed = false;
        level = _level;
        catchedLines = new ArrayList();
    }

    public ArrayList getCatchedLines()
    {
        return catchedLines;
    }

    public void addFilter(Filter filter)
    {
    }

    public Filter getFilter()
    {
        return null;
    }

    public void clearFilters()
    {
    }

    public void close()
    {
    }

    public void doAppend(LoggingEvent event)
    {
        if(event.getLevel().isGreaterOrEqual(level))
        {
            catchedLines.add(event.getMessage().toString());
            changed = true;
        }
    }

    public String getName()
    {
        return null;
    }

    public void setErrorHandler(ErrorHandler errorhandler)
    {
    }

    public ErrorHandler getErrorHandler()
    {
        return null;
    }

    public void setLayout(Layout layout)
    {
    }

    public Layout getLayout()
    {
        return null;
    }

    public void setName(String s)
    {
    }

    public boolean requiresLayout()
    {
        return false;
    }

    public boolean isChanged()
    {
        return changed;
    }

    public void setChanged(boolean changed)
    {
        this.changed = changed;
    }

    private ArrayList catchedLines;
    private Level level;
    private boolean changed;
}
