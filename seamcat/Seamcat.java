// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Seamcat.java

package org.seamcat;

import java.awt.Frame;
import java.io.File;
import java.security.*;
import javax.help.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.*;
import org.seamcat.batch.BatchJobList;
import org.seamcat.model.Components;
import org.seamcat.model.Library;
import org.seamcat.model.Model;
import org.seamcat.model.XmlValidationHandler;
import org.seamcat.presentation.MainWindow;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat:
//            SplashScreen

public final class Seamcat
{

    private Seamcat()
    {
    }

    public static SplashScreen getSplashScreen()
    {
        return splash;
    }

    public static void main(String args[])
    {
        splash = new SplashScreen(new JFrame());
        Policy.setPolicy(new Policy() {

            public PermissionCollection getPermissions(CodeSource codesource)
            {
                Permissions perms = new Permissions();
                perms.add(new AllPermission());
                return perms;
            }

            public void refresh()
            {
            }

        }
);
        BasicConfigurator.configure(splash.getAppender());
        Logger.getRootLogger().setLevel(Level.DEBUG);
        Model model = Model.getInstance();
        boolean debug = false;
        if(args.length > 0 && args[0].equalsIgnoreCase("debug"))
        {
            LOG.addAppender(new ConsoleAppender(model.getLogFilePattern()));
            debug = true;
        }
        MainWindow mainWindow = MainWindow.getInstance();
        LOG.info("Starting SEAMCAT");
        LOG.debug("Loading Java Help system");
        if(helpBroker == null)
        {
            HelpSet hs = null;
            String helpHS = "seamcat.hs";
            ClassLoader cl = org/seamcat/Seamcat.getClassLoader();
            try
            {
                java.net.URL hsURL = HelpSet.findHelpSet(cl, helpHS);
                hs = new HelpSet(cl, hsURL);
                helpBroker = hs.createHelpBroker();
                DefaultHelpBroker def = (DefaultHelpBroker)helpBroker;
                WindowPresentation win = def.getWindowPresentation();
                win.createHelpWindow();
                java.awt.Window window = win.getHelpWindow();
                Frame frame = (Frame)window;
                frame.setUndecorated(false);
                frame.setExtendedState(6);
            }
            catch(Exception ee)
            {
                LOG.error((new StringBuilder()).append("Unable to load Help from: ").append(helpHS).toString());
                ee.printStackTrace(System.out);
            }
        }
        LOG.debug("Requesting Model singleton");
        LOG.debug("Initializing MainWindow");
        mainWindow.init(model);
        mainWindow.pack();
        mainWindow.setExtendedState(6);
        helpBroker.setSize(mainWindow.getSize());
        mainWindow.setVisible(true);
        String arr$[] = args;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String s = arr$[i$];
            try
            {
                File f = new File(s);
                if(!f.exists())
                    continue;
                if(s.toLowerCase().endsWith("sws"))
                {
                    LOG.info((new StringBuilder()).append("Opening workspace from: ").append(s).toString());
                    model.loadWorkspace(f);
                    continue;
                }
                if(s.toLowerCase().endsWith("sli"))
                {
                    LOG.info((new StringBuilder()).append("Importing Library from: ").append(s).toString());
                    model.getLibrary().importLibrary(f, mainWindow.importLibraryConflictHandler);
                    continue;
                }
                if(s.toLowerCase().endsWith("sbj"))
                {
                    LOG.info((new StringBuilder()).append("Importing Batch Job from: ").append(s).toString());
                    DocumentBuilder db = Model.getSeamcatDocumentBuilderFactory().newDocumentBuilder();
                    db.setErrorHandler(new XmlValidationHandler(false));
                    Document doc = db.parse(f);
                    BatchJobList bj = new BatchJobList((Element)doc.getElementsByTagName("BatchJobList").item(0));
                    model.getLibrary().getBatchjoblists().add(bj);
                    JOptionPane.showMessageDialog(mainWindow, "Succesfully imported Batch Job");
                }
            }
            catch(Exception e)
            {
                LOG.warn((new StringBuilder()).append("Unable to open file: ").append(s).toString(), e);
            }
        }

        if(!debug)
            LOG.addAppender(new ConsoleAppender(model.getLogFilePattern()));
        LOG.addAppender(model.getLogFileAppender());
        ((Frame)((DefaultHelpBroker)helpBroker).getWindowPresentation().getHelpWindow()).setUndecorated(false);
        splash.setVisible(false);
        splash.dispose();
        if(args.length <= 0 || !args[0].equalsIgnoreCase("debug"))
            LOG.setLevel(Level.WARN);
    }

    public static final long ctm = System.currentTimeMillis();
    public static HelpBroker helpBroker = null;
    private static final Logger LOG = Logger.getRootLogger();
    private static SplashScreen splash;

}