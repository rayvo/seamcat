// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CoverageTrafficPanel.java

package org.seamcat.presentation.components;

import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.model.Transmitter;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class CoverageTrafficPanel extends JPanel
{

    public CoverageTrafficPanel(JDialog parent)
    {
        super(new GridLayout(2, 1));
        JLabel lblCoverageRad = new JLabel();
        JLabel lblDensity = new JLabel();
        JLabel lblNoChannels = new JLabel();
        JLabel lblNoUserChannel = new JLabel();
        JLabel lblFreqCluster = new JLabel();
        tfCoveradRad = new FormattedCalculatorField(parent);
        tfDensity = new FormattedCalculatorField(parent);
        tfNoChannels = new JFormattedTextField();
        tfNoUserChannel = new JFormattedTextField();
        tfFreqCluster = new JFormattedTextField();
        tfNoChannels.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        tfNoUserChannel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        tfFreqCluster.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        lblCoverageRad.setText(STRINGLIST.getString("LIBRARY_TRANSMITTER_ADDEDIT_COVRADIUS_CAPTION"));
        lblDensity.setText(STRINGLIST.getString("LIBRARY_TRANSMITTER_ADDEDIT_DENSITY_CAPTION"));
        lblNoChannels.setText(STRINGLIST.getString("LIBRARY_TRANSMITTER_ADDEDIT_NOCHANNELS_CAPTION"));
        lblNoUserChannel.setText(STRINGLIST.getString("LIBRARY_TRANSMITTER_ADDEDIT_NOUSERS_CAPTION"));
        lblFreqCluster.setText(STRINGLIST.getString("LIBRARY_TRANSMITTER_ADDEDIT_FREQCLUSTER_CAPTION"));
        tfNoChannels.setFormatterFactory(DFORMATS.getIntegerFactory());
        tfNoUserChannel.setFormatterFactory(DFORMATS.getIntegerFactory());
        tfFreqCluster.setFormatterFactory(DFORMATS.getIntegerFactory());
        tfNoChannels.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
        tfNoUserChannel.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
        tfFreqCluster.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
        JPanel coverageRadiusPanel = new JPanel(new LabeledPairLayout());
        coverageRadiusPanel.add(lblCoverageRad, "label");
        coverageRadiusPanel.add(tfCoveradRad, "field");
        JPanel trafficPanel = new JPanel(new LabeledPairLayout());
        trafficPanel.setBorder(new TitledBorder(STRINGLIST.getString("LIBRARY_TRANSMITTER_ADDEDIT_TRAFFIC_CAPTION")));
        trafficPanel.add(lblDensity, "label");
        trafficPanel.add(tfDensity, "field");
        trafficPanel.add(lblNoChannels, "label");
        trafficPanel.add(tfNoChannels, "field");
        trafficPanel.add(lblNoUserChannel, "label");
        trafficPanel.add(tfNoUserChannel, "field");
        trafficPanel.add(lblFreqCluster, "label");
        trafficPanel.add(tfFreqCluster, "field");
        add(coverageRadiusPanel);
        add(trafficPanel);
    }

    public void setTransmitter(Transmitter transmitter)
    {
        this.transmitter = transmitter;
        tfCoveradRad.setValue(new Double(transmitter.getCoverageRadius()));
        tfDensity.setValue(new Double(transmitter.getDens()));
        tfFreqCluster.setValue(new Integer(transmitter.getFreqCluster()));
        tfNoChannels.setValue(new Integer(transmitter.getNumberOfChannels()));
        tfNoUserChannel.setValue(new Integer(transmitter.getNumberOfUsersPerChannel()));
    }

    public void updateModel()
    {
        transmitter.setCoverageRadius(((Number)tfCoveradRad.getValue()).doubleValue());
        transmitter.setDens(((Number)tfDensity.getValue()).doubleValue());
        transmitter.setFreqCluster(((Number)tfFreqCluster.getValue()).intValue());
        transmitter.setNumberOfChannels(((Number)tfNoChannels.getValue()).intValue());
        transmitter.setNumberOfUsersPerChannel(((Number)tfNoUserChannel.getValue()).intValue());
    }

    private static final ResourceBundle STRINGLIST;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private JFormattedTextField tfCoveradRad;
    private JFormattedTextField tfDensity;
    private JFormattedTextField tfNoChannels;
    private JFormattedTextField tfNoUserChannel;
    private JFormattedTextField tfFreqCluster;
    private Transmitter transmitter;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
