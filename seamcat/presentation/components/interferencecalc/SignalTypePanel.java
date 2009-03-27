// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SignalTypePanel.java

package org.seamcat.presentation.components.interferencecalc;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.seamcat.model.engines.ICEConfiguration;

public class SignalTypePanel extends JPanel
{

    public SignalTypePanel()
    {
        unwanted = new JCheckBox("Unwanted");
        blocking = new JCheckBox("Blocking");
        intermodulation = new JCheckBox("Intermodulation");
        unwanted.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    iceconf.setUnwanted(unwanted.isSelected());
                }
                catch(NullPointerException ne) { }
            }

            final SignalTypePanel this$0;

            
            {
                this$0 = SignalTypePanel.this;
                super();
            }
        }
);
        blocking.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    iceconf.setBlocking(blocking.isSelected());
                }
                catch(NullPointerException ne) { }
            }

            final SignalTypePanel this$0;

            
            {
                this$0 = SignalTypePanel.this;
                super();
            }
        }
);
        intermodulation.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    iceconf.setIntermodulation(intermodulation.isSelected());
                }
                catch(NullPointerException ne) { }
            }

            final SignalTypePanel this$0;

            
            {
                this$0 = SignalTypePanel.this;
                super();
            }
        }
);
        setLayout(new GridLayout(3, 1));
        setBorder(new TitledBorder("Signal Type"));
        add(unwanted);
        add(blocking);
        add(intermodulation);
    }

    public void init(ICEConfiguration _iceconf)
    {
        iceconf = _iceconf;
        setUnwantedStatus(iceconf.isUnwanted());
        setBlockingStatus(iceconf.isBlocking());
        setIntermodulationStatus(iceconf.isIntermodulation());
        if(!iceconf.allowIntermodulation())
        {
            intermodulation.setEnabled(false);
            intermodulation.setSelected(false);
        } else
        {
            intermodulation.setEnabled(true);
        }
    }

    public boolean[] getSelectedSignals()
    {
        return (new boolean[] {
            unwanted.isSelected(), blocking.isSelected(), intermodulation.isSelected()
        });
    }

    public void setUnwantedStatus(boolean selected)
    {
        unwanted.setSelected(selected);
    }

    public void setBlockingStatus(boolean selected)
    {
        blocking.setSelected(selected);
    }

    public void setIntermodulationStatus(boolean selected)
    {
        intermodulation.setSelected(selected);
    }

    public boolean getUnwantedStatus()
    {
        return unwanted.isSelected();
    }

    public boolean getBlockingStatus()
    {
        return blocking.isSelected();
    }

    public boolean getIntermodulationStatus()
    {
        return intermodulation.isSelected();
    }

    private JCheckBox unwanted;
    private JCheckBox blocking;
    private JCheckBox intermodulation;
    private ICEConfiguration iceconf;




}
