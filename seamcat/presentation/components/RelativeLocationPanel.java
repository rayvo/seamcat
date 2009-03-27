// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RelativeLocationPanel.java

package org.seamcat.presentation.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.TransmitterToReceiverPath;
import org.seamcat.presentation.*;

public class RelativeLocationPanel extends JPanel
{

    public RelativeLocationPanel(JDialog parent)
    {
        super(new BorderLayout());
        correlateDistance = new JCheckBox();
        azimuthButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        factorButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        deltaXLabel = new JLabel(STRINGLIST.getString("RELATIVE_LOCATION_DELTA_X"));
        deltaYLabel = new JLabel(STRINGLIST.getString("RELATIVE_LOCATION_DELTA_Y"));
        azimuthLabel = new JLabel(STRINGLIST.getString("RELATIVE_LOCATION_PATH_AZIMUTH"));
        factorLabel = new JLabel(STRINGLIST.getString("RELATIVE_LOCATION_PATH_DISTANCE"));
        this.parent = parent;
        deltaXField = new FormattedCalculatorField(parent);
        deltaYField = new FormattedCalculatorField(parent);
        powerDistDialog = new DistributionDialog(parent, true);
        correlateDistance.addActionListener(correlatedDistanceActionListener);
        azimuthButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = azimuth == null ? powerDistDialog.showDistributionDialog(RelativeLocationPanel.STRINGLIST.getString("RELATIVE_LOCATION_PATH_AZIMUTH")) : powerDistDialog.showDistributionDialog(azimuth, RelativeLocationPanel.STRINGLIST.getString("RELATIVE_LOCATION_PATH_AZIMUTH"));
                if(accept)
                    azimuth = powerDistDialog.getDistributionable();
                azimuthLabel.setText((new StringBuilder()).append(RelativeLocationPanel.STRINGLIST.getString("RELATIVE_LOCATION_PATH_AZIMUTH")).append(" [").append(azimuth.toString()).append("] ").toString());
            }

            final RelativeLocationPanel this$0;

            
            {
                this$0 = RelativeLocationPanel.this;
                super();
            }
        }
);
        factorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = factor == null ? powerDistDialog.showDistributionDialog(RelativeLocationPanel.STRINGLIST.getString("RELATIVE_LOCATION_PATH_DISTANCE")) : powerDistDialog.showDistributionDialog(factor, RelativeLocationPanel.STRINGLIST.getString("RELATIVE_LOCATION_PATH_DISTANCE"));
                if(accept)
                    factor = powerDistDialog.getDistributionable();
                factorLabel.setText((new StringBuilder()).append(RelativeLocationPanel.STRINGLIST.getString("RELATIVE_LOCATION_PATH_DISTANCE")).append(" [").append(factor.toString()).append("] ").toString());
            }

            final RelativeLocationPanel this$0;

            
            {
                this$0 = RelativeLocationPanel.this;
                super();
            }
        }
);
        deltaXField.setEnabled(false);
        deltaXLabel.setEnabled(false);
        deltaYField.setEnabled(false);
        deltaYLabel.setEnabled(false);
        deltaXField.setEnabled(correlateDistance.isSelected());
        deltaYField.setEnabled(correlateDistance.isSelected());
        deltaXLabel.setEnabled(correlateDistance.isSelected());
        deltaYLabel.setEnabled(correlateDistance.isSelected());
        azimuthLabel.setEnabled(!correlateDistance.isSelected());
        factorLabel.setEnabled(!correlateDistance.isSelected());
        azimuthButton.setEnabled(!correlateDistance.isSelected());
        factorButton.setEnabled(!correlateDistance.isSelected());
        try
        {
            idpanel.setVisible(!correlateDistance.isSelected());
        }
        catch(Exception ex) { }
        JPanel fields = new JPanel(new LabeledPairLayout());
        fields.add(deltaXLabel, "label");
        fields.add(deltaXField, "field");
        fields.add(deltaYLabel, "label");
        fields.add(deltaYField, "field");
        fields.add(azimuthLabel, "label");
        fields.add(azimuthButton, "field");
        fields.add(factorLabel, "label");
        fields.add(factorButton, "field");
        add(correlateDistance, "North");
        add(fields, "Center");
        setBorder(new TitledBorder(STRINGLIST.getString("RELATIVE_LOCATION_TITLE")));
    }

    public void setTransmitterToReceiverPath(TransmitterToReceiverPath tvPath, boolean interferer)
    {
        if(interferer)
            correlateDistance.setText(STRINGLIST.getString("RELATIVE_LOCATION_CORRELATED_DISTANCE_INTERFERER"));
        else
            correlateDistance.setText(STRINGLIST.getString("RELATIVE_LOCATION_CORRELATED_DISTANCE_VICTIM"));
        setDeltaX(tvPath.getDeltaX());
        setDeltaY(tvPath.getDeltaY());
        correlateDistance.setSelected(tvPath.getUseCorrelationDistance());
        correlatedDistanceActionListener.actionPerformed(new ActionEvent(correlateDistance, -1, ""));
        azimuth = tvPath.getPathAzimuth();
        factor = tvPath.getPathDistanceFactor();
        azimuthLabel.setText((new StringBuilder()).append(STRINGLIST.getString("RELATIVE_LOCATION_PATH_AZIMUTH")).append(" [").append(azimuth.toString()).append("] ").toString());
        factorLabel.setText((new StringBuilder()).append(STRINGLIST.getString("RELATIVE_LOCATION_PATH_DISTANCE")).append(" [").append(factor.toString()).append("] ").toString());
    }

    public void updateModel(TransmitterToReceiverPath tvPath)
    {
        tvPath.setUseCorrelatedDistance(correlateDistance.isSelected());
        tvPath.setDeltaX(getDeltaX());
        tvPath.setDeltaY(getDeltaY());
        tvPath.setPathAzimuth(azimuth);
        tvPath.setPathDistanceFactor(factor);
    }

    public void setCoverageRadiusPanel(JPanel _panel)
    {
        idpanel = _panel;
        try
        {
            idpanel.setVisible(!correlateDistance.isSelected());
        }
        catch(Exception ex) { }
    }

    public double getDeltaY()
    {
        return ((Number)deltaYField.getValue()).doubleValue();
    }

    public Distribution getFactor()
    {
        return factor;
    }

    public double getDeltaX()
    {
        return ((Number)deltaXField.getValue()).doubleValue();
    }

    public void setAzimuth(Distribution azimuth)
    {
        this.azimuth = azimuth;
    }

    public void setDeltaY(double deltaY)
    {
        deltaYField.setValue(new Double(deltaY));
    }

    public void setFactor(Distribution factor)
    {
        this.factor = factor;
    }

    public void setDeltaX(double deltaX)
    {
        deltaXField.setValue(new Double(deltaX));
    }

    public Distribution getAzimuth()
    {
        return azimuth;
    }

    private static final ResourceBundle STRINGLIST;
    private Distribution azimuth;
    private Distribution factor;
    private DistributionDialog powerDistDialog;
    private JCheckBox correlateDistance;
    private JFormattedTextField deltaXField;
    private JFormattedTextField deltaYField;
    private JButton azimuthButton;
    private JButton factorButton;
    private JLabel deltaXLabel;
    private JLabel deltaYLabel;
    private JLabel azimuthLabel;
    private JLabel factorLabel;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private JPanel idpanel;
    private JDialog parent;
    private final ActionListener correlatedDistanceActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            deltaXField.setEditable(correlateDistance.isSelected());
            deltaYField.setEditable(correlateDistance.isSelected());
            deltaXField.setEnabled(correlateDistance.isSelected());
            deltaYField.setEnabled(correlateDistance.isSelected());
            deltaXLabel.setEnabled(correlateDistance.isSelected());
            deltaYLabel.setEnabled(correlateDistance.isSelected());
            azimuthLabel.setEnabled(!correlateDistance.isSelected());
            factorLabel.setEnabled(!correlateDistance.isSelected());
            azimuthButton.setEnabled(!correlateDistance.isSelected());
            factorButton.setEnabled(!correlateDistance.isSelected());
            try
            {
                idpanel.setVisible(!correlateDistance.isSelected());
            }
            catch(Exception ex) { }
        }

        final RelativeLocationPanel this$0;

            
            {
                this$0 = RelativeLocationPanel.this;
                super();
            }
    }
;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
















}
