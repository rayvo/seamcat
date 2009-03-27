// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CoverageRadiusPanel.java

package org.seamcat.presentation.components;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.model.*;
import org.seamcat.model.core.SystemLink;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;
import org.seamcat.propagation.PropagationModel;

public class CoverageRadiusPanel extends JPanel
{
    public class TrafficLimitedNetwork extends JPanel
    {

        public void init()
        {
            densityField.setValue(new Double(wt2path.getDensity()));
            numberOfChannelsField.setValue(new Integer(wt2path.getNumberOfChannels()));
            numberOfUsersPrChannelField.setValue(new Integer(wt2path.getNumberOfUsersPerChannel()));
            frequencyClusterField.setValue(new Integer(wt2path.getFrequencyCluster()));
        }

        public void updateModel()
        {
            wt2path.setDensity(getDensity());
            wt2path.setNumberOfChannels(getNumberOfChannels());
            wt2path.setNumberOfUsersPerChannel(getNumberOfUsersPrChannel());
            wt2path.setFrequencyCluster(getFrequencyCluster());
        }

        public double getDensity()
        {
            return ((Number)densityField.getValue()).doubleValue();
        }

        public int getNumberOfUsersPrChannel()
        {
            return ((Number)numberOfUsersPrChannelField.getValue()).intValue();
        }

        public int getFrequencyCluster()
        {
            return ((Number)frequencyClusterField.getValue()).intValue();
        }

        public int getNumberOfChannels()
        {
            return ((Number)numberOfChannelsField.getValue()).intValue();
        }

        public void setNumberOfChannels(int numberOfChannels)
        {
            numberOfChannelsField.setValue(new Integer(numberOfChannels));
        }

        public void setDensity(double density)
        {
            densityField.setValue(new Double(density));
        }

        public void setNumberOfUsersPrChannel(int numberOfUsersPrChannel)
        {
            numberOfUsersPrChannelField.setValue(new Integer(numberOfUsersPrChannel));
        }

        public void setFrequencyCluster(int frequencyCluster)
        {
            frequencyClusterField.setValue(new Integer(frequencyCluster));
        }

        private JFormattedTextField densityField;
        private JFormattedTextField numberOfChannelsField;
        private JFormattedTextField numberOfUsersPrChannelField;
        private JFormattedTextField frequencyClusterField;
        final CoverageRadiusPanel this$0;

        public TrafficLimitedNetwork()
        {
            this$0 = CoverageRadiusPanel.this;
            super();
            densityField = new FormattedCalculatorField(parent);
            numberOfChannelsField = new JFormattedTextField(SeamcatTextFieldFormats.getIntegerFactory());
            numberOfUsersPrChannelField = new JFormattedTextField(SeamcatTextFieldFormats.getIntegerFactory());
            frequencyClusterField = new JFormattedTextField(SeamcatTextFieldFormats.getIntegerFactory());
            setLayout(new LabeledPairLayout());
            numberOfChannelsField.setColumns(15);
            numberOfChannelsField.setHorizontalAlignment(4);
            numberOfChannelsField.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            numberOfUsersPrChannelField.setColumns(15);
            numberOfUsersPrChannelField.setHorizontalAlignment(4);
            numberOfUsersPrChannelField.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            frequencyClusterField.setColumns(15);
            frequencyClusterField.setHorizontalAlignment(4);
            frequencyClusterField.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            JLabel densityLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_DENSITY"));
            JLabel numberOfChannelsLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_NO_CHANNELS"));
            JLabel numberOfUsersPrChannelLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_NO_USERS_CHANNELS"));
            JLabel frequencyClusterLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_FREQ_CLUSTER"));
            add(densityLabel, "label");
            add(densityField, "field");
            add(numberOfChannelsLabel, "label");
            add(numberOfChannelsField, "field");
            add(numberOfUsersPrChannelLabel, "label");
            add(numberOfUsersPrChannelField, "field");
            add(frequencyClusterLabel, "label");
            add(frequencyClusterField, "field");
            setBorder(new TitledBorder("Traffic Parameters"));
        }
    }

    public class NoiseLimitedNetwork extends JPanel
    {

        public void init()
        {
            antennaHeightReceiverField.setValue(new Double(wt2path.getReferenceReceiverAntennaHeight()));
            antennaHeightTransmitterField.setValue(new Double(wt2path.getReferenceTransmitterAntennaHeight()));
            frequencyTransmitterField.setValue(new Double(wt2path.getReferenceTransmitterFrequency()));
            powerTransmitterField.setValue(new Double(wt2path.getReferenceTransmitterPower()));
            minDistanceField.setValue(new Double(wt2path.getMinimumDistance()));
            maxDistanceField.setValue(new Double(wt2path.getMaximumDistance()));
            availabilityField.setValue(new Double(wt2path.getAvailability()));
            fadingField.setValue(new Double(wt2path.getFadingStdDev()));
        }

        public void updateModel()
        {
            wt2path.setReferenceReceiverAntennaHeight(getAntennaHeightReceiver());
            wt2path.setReferenceTransmitterAntennaHeight(getAntennaHeightTransmitter());
            wt2path.setReferenceTransmitterFrequency(getFrequencyTransmitter());
            wt2path.setReferenceTransmitterPower(getPowerTransmitter());
            wt2path.setMinimumDistance(getMinDistance());
            wt2path.setMaximumDistance(getMaxDistance());
            wt2path.setAvailability(getAvailability());
            wt2path.setFadingStdDev(getFading());
        }

        public double getFrequencyTransmitter()
        {
            return ((Number)frequencyTransmitterField.getValue()).doubleValue();
        }

        public double getMaxDistance()
        {
            return ((Number)maxDistanceField.getValue()).doubleValue();
        }

        public double getPowerTransmitter()
        {
            return ((Number)powerTransmitterField.getValue()).doubleValue();
        }

        public double getAntennaHeightReceiver()
        {
            return ((Number)antennaHeightReceiverField.getValue()).doubleValue();
        }

        public double getAntennaHeightTransmitter()
        {
            return ((Number)antennaHeightTransmitterField.getValue()).doubleValue();
        }

        public double getAvailability()
        {
            return ((Number)availabilityField.getValue()).doubleValue();
        }

        public double getMinDistance()
        {
            return ((Number)minDistanceField.getValue()).doubleValue();
        }

        public void setFading(double fading)
        {
            fadingField.setValue(new Double(fading));
        }

        public void setPropagationModel(String propagationModel)
        {
            propagationLabel.setText(propagationModel);
        }

        public void setFrequencyTransmitter(double frequencyTransmitter)
        {
            frequencyTransmitterField.setValue(new Double(frequencyTransmitter));
        }

        public void setMaxDistance(double maxDistance)
        {
            maxDistanceField.setValue(new Double(maxDistance));
        }

        public void setPowerTransmitter(double powerTransmitter)
        {
            powerTransmitterField.setValue(new Double(powerTransmitter));
        }

        public void setAntennaHeightReceiver(double antennaHeightReceiver)
        {
            antennaHeightReceiverField.setValue(new Double(antennaHeightReceiver));
        }

        public void setAntennaHeightTransmitter(double antennaHeightTransmitter)
        {
            antennaHeightTransmitterField.setValue(new Double(antennaHeightTransmitter));
        }

        public void setAvailability(double availability)
        {
            availabilityField.setValue(new Double(availability));
        }

        public void setMinDistance(double minDistance)
        {
            minDistanceField.setValue(new Double(minDistance));
        }

        public double getFading()
        {
            return ((Number)fadingField.getValue()).doubleValue();
        }

        private JLabel propagationLabel;
        private JFormattedTextField antennaHeightReceiverField;
        private JFormattedTextField antennaHeightTransmitterField;
        private JFormattedTextField frequencyTransmitterField;
        private JFormattedTextField powerTransmitterField;
        private JFormattedTextField minDistanceField;
        private JFormattedTextField maxDistanceField;
        private JFormattedTextField availabilityField;
        private JFormattedTextField fadingField;
        final CoverageRadiusPanel this$0;

        public NoiseLimitedNetwork()
        {
            this$0 = CoverageRadiusPanel.this;
            super();
            propagationLabel = new JLabel("HataSE21Model");
            antennaHeightReceiverField = new FormattedCalculatorField(parent);
            antennaHeightTransmitterField = new FormattedCalculatorField(parent);
            frequencyTransmitterField = new FormattedCalculatorField(parent);
            powerTransmitterField = new FormattedCalculatorField(parent);
            minDistanceField = new FormattedCalculatorField(parent);
            maxDistanceField = new FormattedCalculatorField(parent);
            availabilityField = new FormattedCalculatorField(parent);
            fadingField = new FormattedCalculatorField(parent);
            setLayout(new LabeledPairLayout());
            JLabel propagationModelLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_PROPAGATION_MODEL"));
            JLabel antennaHeightReceiverLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_REF_RX_ANTENNA_HEIGHT"));
            JLabel antennaHeightTransmitterLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_REF_TX_ANTENNA_HEIGHT"));
            JLabel frequencyTransmitterLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_REF_FREQ"));
            JLabel powerTransmitterLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_REF_TX_POWER"));
            JLabel minDistanceLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_MIN_DIST"));
            JLabel maxDistanceLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_MAX_DIST"));
            JLabel availabilityLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_AVAILABILITY"));
            JLabel fadingLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_FADING_STDDEV"));
            add(propagationModelLabel, "label");
            add(propagationLabel, "field");
            add(antennaHeightReceiverLabel, "label");
            add(antennaHeightReceiverField, "field");
            add(antennaHeightTransmitterLabel, "label");
            add(antennaHeightTransmitterField, "field");
            add(frequencyTransmitterLabel, "label");
            add(frequencyTransmitterField, "field");
            add(powerTransmitterLabel, "label");
            add(powerTransmitterField, "field");
            add(minDistanceLabel, "label");
            add(minDistanceField, "field");
            add(maxDistanceLabel, "label");
            add(maxDistanceField, "field");
            add(availabilityLabel, "label");
            add(availabilityField, "field");
            add(fadingLabel, "label");
            add(fadingField, "field");
            setBorder(new TitledBorder("Noise-limited Parameters"));
        }
    }

    public class UserDefinedCoverageRadiusPanel extends JPanel
    {

        public void init()
        {
            radiusField.setValue(new Double(wt2path.getCoverageRadius()));
        }

        public void updateModel()
        {
            wt2path.setCoverageRadius(getCoverageRadius());
        }

        public void setCoverageRadius(double coverageRadius)
        {
            radiusField.setValue(new Double(coverageRadius));
        }

        public double getCoverageRadius()
        {
            return ((Number)radiusField.getValue()).doubleValue();
        }

        private JFormattedTextField radiusField;
        final CoverageRadiusPanel this$0;

        public UserDefinedCoverageRadiusPanel()
        {
            this$0 = CoverageRadiusPanel.this;
            super();
            radiusField = new FormattedCalculatorField(parent);
            setLayout(new LabeledPairLayout());
            JLabel radiusLabel = new JLabel(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_KM"));
            add(radiusLabel, "label");
            add(radiusField, "field");
            setBorder(new TitledBorder(CoverageRadiusPanel.STRINGS.getString("COVERAGE_RADIUS_USER_DEFINED_CAPTION")));
        }
    }


    public CoverageRadiusPanel(JDialog _parent)
    {
        super(new BorderLayout());
        calculationModeBox = new JComboBox(WantedTransmitter.CALCULATION_MODES);
        userDefined = new UserDefinedCoverageRadiusPanel();
        noiseLimited = new NoiseLimitedNetwork();
        trafficLimited = new TrafficLimitedNetwork();
        configLayout = new CardLayout();
        configurationPanel = new JPanel(configLayout);
        JLabel calculationModeLabel = new JLabel(STRINGS.getString("COVERAGE_RADIUS_CALCULATION_MODE"));
        parent = _parent;
        JPanel selectPanel = new JPanel();
        selectPanel.add(calculationModeLabel);
        selectPanel.add(calculationModeBox);
        JScrollPane ud = new JScrollPane(userDefined);
        ud.setBorder(null);
        JScrollPane nl = new JScrollPane(noiseLimited);
        nl.setBorder(null);
        JScrollPane tl = new JScrollPane(trafficLimited);
        tl.setBorder(null);
        configurationPanel.add(Transmitter.CALCULATION_MODES[0], ud);
        configurationPanel.add(Transmitter.CALCULATION_MODES[1], nl);
        configurationPanel.add(Transmitter.CALCULATION_MODES[2], tl);
        configurationPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        calculationModeBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                configLayout.show(configurationPanel, (String)calculationModeBox.getSelectedItem());
                wt2path.setCoverageRadiusCalculationMode(calculationModeBox.getSelectedIndex());
            }

            final CoverageRadiusPanel this$0;

            
            {
                this$0 = CoverageRadiusPanel.this;
                super();
            }
        }
);
        add(selectPanel, "North");
        add(configurationPanel, "Center");
        setBorder(new TitledBorder(STRINGS.getString("COVERAGE_RADIUS_CAPTION")));
    }

    public void updateModel(SystemLink vl)
    {
        wt2path = vl.getWt2VrPath();
        userDefined.updateModel();
        noiseLimited.updateModel();
        trafficLimited.updateModel();
    }

    public void set(SystemLink vl)
    {
        wt2path = vl.getWt2VrPath();
        userDefined.init();
        noiseLimited.init();
        trafficLimited.init();
        calculationModeBox.setSelectedIndex(wt2path.getCoverageRadiusCalculationMode());
        setPropagationModel(wt2path.getPropagationModel().toString());
    }

    public void setPropagationModel(String propagationModel)
    {
        noiseLimited.setPropagationModel(propagationModel);
    }

    private static final ResourceBundle STRINGS;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private JComboBox calculationModeBox;
    private UserDefinedCoverageRadiusPanel userDefined;
    private NoiseLimitedNetwork noiseLimited;
    private TrafficLimitedNetwork trafficLimited;
    private CardLayout configLayout;
    private JPanel configurationPanel;
    protected TransmitterToReceiverPath wt2path;
    private JDialog parent;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }





}
