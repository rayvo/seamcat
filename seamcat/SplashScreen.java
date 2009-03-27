// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SplashScreen.java

package org.seamcat;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.seamcat.presentation.LabelAppender;

public class SplashScreen extends JWindow
{

    public SplashScreen(Frame parent)
    {
        super(parent);
        statusLabel = new JProgressBar();
        appender = new LabelAppender(statusLabel);
        statusLabel.setString((new StringBuilder()).append(stringlist.getString("SPLASH_SCREEN_TEXT")).append(" ").append(stringlist.getString("APPLICATION_TITLE")).toString());
        statusLabel.setMaximum(174);
        statusLabel.setStringPainted(true);
        String imageFileName = stringlist.getString("SPLASH_SCREEN_IMAGE");
        JLabel l = new JLabel(new ImageIcon(getClass().getResource(imageFileName)));
        getContentPane().add(l, "Center");
        getContentPane().add(statusLabel, "South");
        getContentPane().setBackground(Color.WHITE);
        pack();
        setLocationRelativeTo(parent);
        Logger.getRootLogger().addAppender(appender);
        setVisible(true);
    }

    public void setProgressMax(int max)
    {
        statusLabel.setMaximum(max);
    }

    public void setProgress(int status)
    {
        statusLabel.setValue(status);
    }

    public void dispose()
    {
        super.dispose();
        Logger.getRootLogger().removeAppender(appender);
    }

    public Appender getAppender()
    {
        return appender;
    }

    private static final ResourceBundle stringlist;
    private JProgressBar statusLabel;
    private Appender appender;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}