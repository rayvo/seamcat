// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PowerPanel.java

package org.seamcat.presentation.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.WantedTransmitter;
import org.seamcat.presentation.DistributionDialog;

public class PowerPanel extends JPanel
{

    public PowerPanel(JDialog parent)
    {
        powerButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        powerLabel = new JLabel(STRINGLIST.getString("POWER_PANEL_POWER"));
        setLayout(new BoxLayout(this, 1));
        powerDistDialog = new DistributionDialog(parent, true);
        powerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = powerDistDialog.showDistributionDialog(dist, PowerPanel.STRINGLIST.getString("POWER_PANEL_POWER_TITLE"));
                if(accept)
                    dist = powerDistDialog.getDistributionable();
                powerLabel.setText((new StringBuilder()).append(PowerPanel.STRINGLIST.getString("POWER_PANEL_POWER")).append(" [").append(dist.toString()).append("] ").toString());
            }

            final PowerPanel this$0;

            
            {
                this$0 = PowerPanel.this;
                super();
            }
        }
);
        add(powerLabel, "label");
        add(powerButton, "field");
        add(Box.createGlue());
        setBorder(new TitledBorder(STRINGLIST.getString("POWER_PANEL_TITLE")));
    }

    public void setWantedTransmitter(WantedTransmitter wantedTransmitter)
    {
        this.wantedTransmitter = wantedTransmitter;
        dist = wantedTransmitter.getPowerSuppliedDistribution();
        powerLabel.setText((new StringBuilder()).append(STRINGLIST.getString("POWER_PANEL_POWER")).append(" [").append(dist.toString()).append("] ").toString());
    }

    public void updateModel(WantedTransmitter wantedTransmitter)
    {
        wantedTransmitter.setPowerSuppliedDistribution(dist);
    }

    private static final ResourceBundle STRINGLIST;
    private WantedTransmitter wantedTransmitter;
    private JButton powerButton;
    private JLabel powerLabel;
    private DistributionDialog powerDistDialog;
    private Distribution dist;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }





}
