// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TestGUIRunner.java

package org.seamcat.testfeatures;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.apache.log4j.*;
import org.seamcat.cdma.*;
import org.seamcat.cdma.presentation.*;
import org.seamcat.presentation.AntennaAddEditComponent;

public class TestGUIRunner extends JFrame
{

    public TestGUIRunner()
    {
        super("GUI Tester");
        initialized = false;
        setDefaultCloseOperation(3);
        guiInitializer = new Thread() {

            public void run()
            {
                CDMASystem system = new CDMAUplinkSystem();
                system.setTypeOfCellsInPowerControlCluster(org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna);
                system.generateSystemCells();
                center = system.getCDMACells()[0];
                antennaDiag = new AntennaAddEditComponent(TestGUIRunner.this);
                plot = new JDialog(TestGUIRunner.this);
                plotpanel = new CDMASystemPlotPanel();
                plotpanel.setCDMASystem(system);
                plot.setContentPane(plotpanel);
                plot.setSize(1024, 768);
                capacity = new JDialog(TestGUIRunner.this);
                capacity.setContentPane(new CapacityFindingStatusPanel());
                capacity.setSize(1024, 768);
                selector = new JDialog(TestGUIRunner.this);
                reference_selector = new ReferenceCellSelectionPanel();
                reference_selector.setCdmaSystem(system);
                selector.setContentPane(reference_selector);
                selector.setSize(1024, 768);
                system.repositionSystem(35D, 80D);
                initialized = true;
            }

            final TestGUIRunner this$0;

            
            {
                this$0 = TestGUIRunner.this;
                super();
            }
        }
;
        guiInitializer.start();
        JButton button1 = new JButton("Show 1 sector antenna");
        JButton button2 = new JButton("Show 2 sector antenna");
        JButton button3 = new JButton("Show 3 sector antenna");
        JButton button4 = new JButton("Show CDMA system");
        JButton button5 = new JButton("Show capacity finding panel");
        JButton button6 = new JButton("Show reference cell selection panel");
        button1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!initialized)
                    try
                    {
                        guiInitializer.join();
                    }
                    catch(InterruptedException ex)
                    {
                        TestGUIRunner.LOG.error("An Error occured", ex);
                    }
                antennaDiag.show(((CDMATriSectorCell)center[0]).getAntenna());
            }

            final TestGUIRunner this$0;

            
            {
                this$0 = TestGUIRunner.this;
                super();
            }
        }
);
        button2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!initialized)
                    try
                    {
                        guiInitializer.join();
                    }
                    catch(InterruptedException ex)
                    {
                        TestGUIRunner.LOG.error("An Error occured", ex);
                    }
                antennaDiag.show(((CDMATriSectorCell)center[1]).getAntenna());
            }

            final TestGUIRunner this$0;

            
            {
                this$0 = TestGUIRunner.this;
                super();
            }
        }
);
        button3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!initialized)
                    try
                    {
                        guiInitializer.join();
                    }
                    catch(InterruptedException ex)
                    {
                        TestGUIRunner.LOG.error("An Error occured", ex);
                    }
                antennaDiag.show(((CDMATriSectorCell)center[2]).getAntenna());
            }

            final TestGUIRunner this$0;

            
            {
                this$0 = TestGUIRunner.this;
                super();
            }
        }
);
        button4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!initialized)
                    try
                    {
                        guiInitializer.join();
                    }
                    catch(InterruptedException ex)
                    {
                        TestGUIRunner.LOG.error("An Error occured", ex);
                    }
                plot.setVisible(true);
            }

            final TestGUIRunner this$0;

            
            {
                this$0 = TestGUIRunner.this;
                super();
            }
        }
);
        button5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!initialized)
                    try
                    {
                        guiInitializer.join();
                    }
                    catch(InterruptedException ex)
                    {
                        TestGUIRunner.LOG.error("An Error occured", ex);
                    }
                capacity.setVisible(true);
            }

            final TestGUIRunner this$0;

            
            {
                this$0 = TestGUIRunner.this;
                super();
            }
        }
);
        button6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!initialized)
                    try
                    {
                        guiInitializer.join();
                    }
                    catch(InterruptedException ex)
                    {
                        TestGUIRunner.LOG.error("An Error occured", ex);
                    }
                selector.setVisible(true);
            }

            final TestGUIRunner this$0;

            
            {
                this$0 = TestGUIRunner.this;
                super();
            }
        }
);
        getContentPane().setLayout(new GridLayout(6, 1));
        getContentPane().add(button1);
        getContentPane().add(button2);
        getContentPane().add(button3);
        getContentPane().add(button4);
        getContentPane().add(button5);
        getContentPane().add(button6);
        pack();
        setVisible(true);
    }

    public static void main(String args[])
    {
        BasicConfigurator.configure();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex)
        {
            LOG.error("An Error occured", ex);
        }
        Logger.getRootLogger().setLevel(Level.INFO);
        new TestGUIRunner();
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/testfeatures/TestGUIRunner);
    private AntennaAddEditComponent antennaDiag;
    private CDMASystemPlotPanel plotpanel;
    private ReferenceCellSelectionPanel reference_selector;
    private CDMACell center[];
    private boolean initialized;
    private JDialog plot;
    private JDialog capacity;
    private JDialog selector;
    private Thread guiInitializer;



















}
