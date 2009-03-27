// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LabelAppender.java

package org.seamcat.presentation;

import java.io.PrintStream;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import org.apache.log4j.*;
import org.apache.log4j.spi.*;

public class LabelAppender
    implements Appender
{

    public LabelAppender(JLabel _label)
    {
        label = _label;
    }

    public LabelAppender(JProgressBar _label)
    {
        label = _label;
    }

    public void addFilter(Filter filter)
    {
        throw new UnsupportedOperationException("addFilter has not been implemented");
    }

    public Filter getFilter()
    {
        throw new UnsupportedOperationException("getFilter has not been implemented");
    }

    public void clearFilters()
    {
        throw new UnsupportedOperationException("clearFilters has not been implemented");
    }

    public void close()
    {
    }

    public void doAppend(LoggingEvent loggingEvent)
    {
        if(label != null)
            if(layout != null)
            {
                if(label instanceof JTextComponent)
                    ((JTextComponent)label).setText(layout.format(loggingEvent));
                else
                if(label instanceof JProgressBar)
                {
                    JProgressBar jp = (JProgressBar)label;
                    if(jp.getValue() == jp.getMaximum())
                    {
                        jp.setMaximum(jp.getMaximum() + 1);
                        System.out.println((new StringBuilder()).append("Increased maximum to ").append(jp.getMaximum()).toString());
                    }
                    jp.setValue(jp.getValue() + 1);
                    jp.setString(layout.format(loggingEvent));
                }
            } else
            if(label instanceof JTextComponent)
                ((JTextComponent)label).setText(loggingEvent.getMessage().toString());
            else
            if(label instanceof JProgressBar)
            {
                JProgressBar jp = (JProgressBar)label;
                if(jp.getValue() == jp.getMaximum())
                {
                    jp.setMaximum(jp.getMaximum() + 1);
                    System.out.println((new StringBuilder()).append("Increased maximum to ").append(jp.getMaximum()).toString());
                }
                jp.setValue(jp.getValue() + 1);
                jp.setString(loggingEvent.getMessage().toString());
            }
        if(forwardToSystemOut && loggingEvent.getLevel().isGreaterOrEqual(Level.INFO))
        {
            if(layout != null)
                System.out.println(layout.format(loggingEvent));
            else
                System.out.println(loggingEvent.getMessage().toString());
            if(loggingEvent.getLevel().isGreaterOrEqual(Level.WARN))
                loggingEvent.getThrowableInformation().getThrowable().printStackTrace(System.out);
        }
    }

    public String getName()
    {
        return name;
    }

    public void setErrorHandler(ErrorHandler errorHandler)
    {
        throw new UnsupportedOperationException("setErrorHandler has not been implemented");
    }

    public ErrorHandler getErrorHandler()
    {
        throw new UnsupportedOperationException("getErrorHandler has not been implemented");
    }

    public void setLayout(Layout _layout)
    {
        layout = _layout;
    }

    public Layout getLayout()
    {
        return layout;
    }

    public void setName(String string)
    {
        name = string;
    }

    public boolean requiresLayout()
    {
        return false;
    }

    private JComponent label;
    private Layout layout;
    private String name;
    private boolean forwardToSystemOut;
}
