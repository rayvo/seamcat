// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AntennaPointingPanel.java

package org.seamcat.presentation.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.*;
import org.seamcat.model.core.*;
import org.seamcat.presentation.DistributionDialog;

public class AntennaPointingPanel extends JPanel
{

    public AntennaPointingPanel(JDialog owner)
    {
        setLayout(new BoxLayout(this, 1));
        powerDistDialog = new DistributionDialog(owner, true);
        heightLabel = new JLabel(STRINGLIST.getString("ANTENNA_POINTING_HEIGHT"));
        azimuthLabel = new JLabel(STRINGLIST.getString("ANTENNA_POINTING_AZIMUTH"));
        elevationLabel = new JLabel(STRINGLIST.getString("ANTENNA_POINTING_ELEVATION"));
        JButton heightButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        JButton azimuthButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        JButton elevationButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        heightButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = height == null ? powerDistDialog.showDistributionDialog(AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_HEIGHT")) : powerDistDialog.showDistributionDialog(height, AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_HEIGHT"));
                if(accept)
                    height = powerDistDialog.getDistributionable();
                heightLabel.setText((new StringBuilder()).append(AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_HEIGHT")).append(" [").append(height.toString()).append("] ").toString());
            }

            final AntennaPointingPanel this$0;

            
            {
                this$0 = AntennaPointingPanel.this;
                super();
            }
        }
);
        azimuthButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = azimuth == null ? powerDistDialog.showDistributionDialog(AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_AZIMUTH_TITLE")) : powerDistDialog.showDistributionDialog(azimuth, AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_AZIMUTH_TITLE"));
                if(accept)
                    azimuth = powerDistDialog.getDistributionable();
                azimuthLabel.setText((new StringBuilder()).append(AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_AZIMUTH")).append(" [").append(azimuth.toString()).append("] ").toString());
            }

            final AntennaPointingPanel this$0;

            
            {
                this$0 = AntennaPointingPanel.this;
                super();
            }
        }
);
        elevationButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = elevation == null ? powerDistDialog.showDistributionDialog(AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_ELEVATION_TITLE")) : powerDistDialog.showDistributionDialog(elevation, AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_ELEVATION_TITLE"));
                if(accept)
                    elevation = powerDistDialog.getDistributionable();
                elevationLabel.setText((new StringBuilder()).append(AntennaPointingPanel.STRINGLIST.getString("ANTENNA_POINTING_ELEVATION")).append(" [").append(elevation.toString()).append("] ").toString());
            }

            final AntennaPointingPanel this$0;

            
            {
                this$0 = AntennaPointingPanel.this;
                super();
            }
        }
);
        add(heightLabel);
        add(heightButton);
        add(new JLabel(" "));
        add(azimuthLabel);
        add(azimuthButton);
        add(new JLabel(" "));
        add(elevationLabel);
        add(elevationButton);
        add(new JLabel(" "));
        add(new JLabel(STRINGLIST.getString("ANTENNA_POINTING_HEIGHT_INFORMATION")));
        add(Box.createGlue());
        setBorder(new TitledBorder(STRINGLIST.getString("ANTENNA_POINTING_TITLE")));
    }

    public void set(InterferingSystemLink isl, WantedReceiver wr)
    {
        setAzimuth(isl.getReceiverToTransmitterAzimuth());
        setElevation(isl.getReceiverToTransmitterElevation());
        setHeight(wr.getAntennaHeight());
        heightLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_HEIGHT")).append(" [").append(height.toString()).append("] ").toString());
        azimuthLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_AZIMUTH")).append("[ ").append(azimuth.toString()).append("] ").toString());
        elevationLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_ELEVATION")).append(" [").append(elevation.toString()).append("] ").toString());
    }

    public void set(InterferingSystemLink isl, InterferingTransmitter it)
    {
        setAzimuth(isl.getTransmitterToReceiverAzimuth());
        setElevation(isl.getTransmitterToReceiverElevation());
        setHeight(it.getAntennaHeight());
        heightLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_HEIGHT")).append(" [").append(height.toString()).append("] ").toString());
        azimuthLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_AZIMUTH")).append("[ ").append(azimuth.toString()).append("] ").toString());
        elevationLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_ELEVATION")).append(" [").append(elevation.toString()).append("] ").toString());
    }

    public void set(VictimSystemLink vlk, boolean victimReceiver)
    {
        if(victimReceiver)
        {
            setAzimuth(vlk.getReceiverToTransmitterAzimuth());
            setHeight(vlk.getVictimReceiver().getAntennaHeight());
            setElevation(vlk.getReceiverToTransmitterElevation());
        } else
        {
            setAzimuth(vlk.getTransmitterToReceiverAzimuth());
            setHeight(vlk.getWantedTransmitter().getAntennaHeight());
            setElevation(vlk.getTransmitterToReceiverElevation());
        }
        heightLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_HEIGHT")).append(" [").append(height.toString()).append("]").toString());
        azimuthLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_AZIMUTH")).append(" [").append(azimuth.toString()).append("]").toString());
        elevationLabel.setText((new StringBuilder()).append(STRINGLIST.getString("ANTENNA_POINTING_ELEVATION")).append(" [").append(elevation.toString()).append("]").toString());
    }

    public void updateModel(VictimSystemLink vlk, boolean victimReceiver)
    {
        if(victimReceiver)
        {
            vlk.setReceiverToTransmitterAzimuth(getAzimuthDistribution());
            vlk.getVictimReceiver().setAntennaHeight(getHeightDistribution());
            vlk.setReceiverToTransmitterElevation(getElevationDistribution());
        } else
        {
            vlk.setTransmitterToReceiverAzimuth(getAzimuthDistribution());
            vlk.getWantedTransmitter().setAntennaHeight(getHeightDistribution());
            vlk.setTransmitterToReceiverElevation(getElevationDistribution());
        }
    }

    public void updateModel(InterferingSystemLink isl, WantedReceiver wr)
    {
        isl.setReceiverToTransmitterAzimuth(getAzimuthDistribution());
        isl.setReceiverToTransmitterElevation(getElevationDistribution());
        wr.setAntennaHeight(getHeightDistribution());
    }

    public void updateModel(InterferingSystemLink isl, InterferingTransmitter it)
    {
        isl.setTransmitterToReceiverAzimuth(getAzimuthDistribution());
        isl.setTransmitterToReceiverElevation(getElevationDistribution());
        it.setAntennaHeight(getHeightDistribution());
    }

    public Distribution getElevationDistribution()
    {
        return elevation;
    }

    public Distribution getHeightDistribution()
    {
        return height;
    }

    public void setAzimuth(Distribution azimuth)
    {
        this.azimuth = azimuth;
    }

    public void setElevation(Distribution elevation)
    {
        this.elevation = elevation;
    }

    public void setHeight(Distribution height)
    {
        this.height = height;
    }

    public Distribution getAzimuthDistribution()
    {
        return azimuth;
    }

    public void clear()
    {
        height = null;
        elevation = null;
        azimuth = null;
    }

    private static final ResourceBundle STRINGLIST;
    private Distribution height;
    private Distribution azimuth;
    private Distribution elevation;
    private final JLabel heightLabel;
    private final JLabel azimuthLabel;
    private final JLabel elevationLabel;
    private DistributionDialog powerDistDialog;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }











}
