// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ICEControlPanel.java

package org.seamcat.presentation.components.interferencecalc;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.model.*;
import org.seamcat.model.core.EventVector;
import org.seamcat.model.core.Signals;
import org.seamcat.model.datatypes.DRSSVector;
import org.seamcat.model.engines.ICEConfiguration;
import org.seamcat.presentation.components.InterferenceCalculationsPanel;

public class ICEControlPanel extends JPanel
{

    public ICEControlPanel(InterferenceCalculationsPanel _parent)
    {
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        firstButton = new JButton("First");
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        lastButton = new JButton("Last");
        deleteButton = new JButton("Delete");
        statusText = new JLabel("No stored calculations");
        parent = _parent;
        setLayout(new FlowLayout(0));
        setBorder(new TitledBorder("Interference Calculation Engine Control"));
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        startButton.setIcon(new ImageIcon(getClass().getResource("/org/seamcat/presentation/resources/Play16.gif")));
        stopButton.setIcon(new ImageIcon(getClass().getResource("/org/seamcat/presentation/resources/Stop.gif")));
        firstButton.setIcon(new ImageIcon(getClass().getResource("/org/seamcat/presentation/resources/First.gif")));
        previousButton.setIcon(new ImageIcon(getClass().getResource("/org/seamcat/presentation/resources/Previous.gif")));
        nextButton.setIcon(new ImageIcon(getClass().getResource("/org/seamcat/presentation/resources/Next.gif")));
        lastButton.setIcon(new ImageIcon(getClass().getResource("/org/seamcat/presentation/resources/Last.gif")));
        deleteButton.setIcon(new ImageIcon(getClass().getResource("/org/seamcat/presentation/resources/Delete.gif")));
        add(startButton);
        add(stopButton);
        add(new JSeparator(1));
        JPanel p = new JPanel(new FlowLayout(0));
        p.add(firstButton);
        p.add(previousButton);
        p.add(nextButton);
        p.add(lastButton);
        p.add(deleteButton);
        add(p);
        add(new JSeparator(1));
        add(statusText);
        addActionListenerFirst(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                currentIndex = 0;
                updateIceConf();
            }

            final ICEControlPanel this$0;

            
            {
                this$0 = ICEControlPanel.this;
                super();
            }
        }
);
        addActionListenerLast(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                currentIndex = workspace.getIceConfigurationCount() - 1;
                updateIceConf();
            }

            final ICEControlPanel this$0;

            
            {
                this$0 = ICEControlPanel.this;
                super();
            }
        }
);
        addActionListenerNext(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                currentIndex++;
                if(currentIndex == workspace.getIceConfigurationCount())
                {
                    parent.updateModel();
                    ICEConfiguration iceconf = workspace.getIceConfiguration(currentIndex - 1);
                    ICEConfiguration ic = new ICEConfiguration(iceconf);
                    ic.setNumberOfSamples(workspace.getSignals().getDRSSVector().getEventVector().size());
                    workspace.addIceConfiguration(ic);
                }
                updateIceConf();
            }

            final ICEControlPanel this$0;

            
            {
                this$0 = ICEControlPanel.this;
                super();
            }
        }
);
        addActionListenerPrevious(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                currentIndex--;
                updateIceConf();
            }

            final ICEControlPanel this$0;

            
            {
                this$0 = ICEControlPanel.this;
                super();
            }
        }
);
        addActionListenerDelete(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                workspace.removeIceConfiguration(workspace.getIceConfiguration(currentIndex));
                if(workspace.getIceConfigurationCount() < 1)
                {
                    workspace.addIceConfiguration(new ICEConfiguration());
                    currentIndex = 0;
                }
                updateIceConf();
            }

            final ICEControlPanel this$0;

            
            {
                this$0 = ICEControlPanel.this;
                super();
            }
        }
);
    }

    public void updateIceConf()
    {
        try
        {
            parent.updateModel();
            if(currentIndex == workspace.getIceConfigurationCount())
                currentIndex--;
            ICEConfiguration ice = workspace.getIceConfiguration(currentIndex);
            int samples = workspace.getControl().getEgData().getNumberOfEvents();
            if(samples != ice.getNumberOfSamples())
                ice.setNumberOfSamples(samples);
            parent.init(ice);
            previousButton.setEnabled(currentIndex > 0);
            firstButton.setEnabled(currentIndex > 0);
            lastButton.setEnabled(currentIndex < workspace.getIceConfigurationCount() - 1);
            deleteButton.setEnabled(workspace.getIceConfiguration(currentIndex) != null);
            startButton.setEnabled(!workspace.getIceConfiguration(currentIndex).getHasBeenCalculated());
            statusText.setText((new StringBuilder()).append("ICEConfiguration ").append(currentIndex + 1).append(" of ").append(workspace.getIceConfigurationCount()).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void init(Workspace _workspace)
    {
        workspace = _workspace;
        currentIndex = 0;
        updateIceConf();
    }

    public void addActionListenerStart(ActionListener acc)
    {
        startButton.addActionListener(acc);
    }

    public void addActionListenerStop(ActionListener acc)
    {
        stopButton.addActionListener(acc);
    }

    public void addActionListenerFirst(ActionListener acc)
    {
        firstButton.addActionListener(acc);
    }

    public void addActionListenerPrevious(ActionListener acc)
    {
        previousButton.addActionListener(acc);
    }

    public void addActionListenerNext(ActionListener acc)
    {
        nextButton.addActionListener(acc);
    }

    public void addActionListenerLast(ActionListener acc)
    {
        lastButton.addActionListener(acc);
    }

    public void addActionListenerDelete(ActionListener acc)
    {
        deleteButton.addActionListener(acc);
    }

    public void setStatusText(String text)
    {
        statusText.setText(text);
    }

    private static final String imageUrlPrefix = "/org/seamcat/presentation/resources/";
    private JButton startButton;
    private JButton stopButton;
    private JButton firstButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton lastButton;
    private JButton deleteButton;
    private JLabel statusText;
    private Workspace workspace;
    private InterferenceCalculationsPanel parent;
    private int currentIndex;






}
