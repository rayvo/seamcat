// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServerStatusPanel.java

package org.seamcat.presentation.components;

import java.awt.BorderLayout;
import javax.swing.*;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.*;

public class ServerStatusPanel extends JPanel
{

    public ServerStatusPanel()
    {
        super(new BorderLayout());
        logfield.setEditable(false);
        add(new JScrollPane(logfield), "Center");
    }

    public JTextArea getLogField()
    {
        return logfield;
    }

    public void append(String text)
    {
        logfield.append(text);
        logfield.setCaretPosition(logfield.getText().length());
    }

    public void clear()
    {
        logfield.setText("");
    }

    public void setText(String text)
    {
        logfield.setText(text);
    }

    public Appender getAppender()
    {
        return appender;
    }

    private final JTextArea logfield = new JTextArea();
    public static final String ROOTID = "SERVER_MODE";
    private final Appender appender = new Appender() {

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

        public void doAppend(LoggingEvent loggingEvent)
        {
            if(layout != null)
                logfield.append(layout.format(loggingEvent));
            else
                logfield.append(loggingEvent.getMessage().toString());
        }

        public void setErrorHandler(ErrorHandler errorhandler)
        {
        }

        public ErrorHandler getErrorHandler()
        {
            return null;
        }

        public void setLayout(Layout _layout)
        {
            layout = _layout;
        }

        public boolean requiresLayout()
        {
            return false;
        }

        public String getName()
        {
            return null;
        }

        public Layout getLayout()
        {
            return layout;
        }

        public void setName(String s)
        {
        }

        private Layout layout;
        final ServerStatusPanel this$0;

            
            {
                this$0 = ServerStatusPanel.this;
                super();
            }
    }
;

}
