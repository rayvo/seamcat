// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PowerControlPanel.java

package org.seamcat.presentation.components;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.model.core.PowerControl;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class PowerControlPanel extends JPanel
{

    public PowerControlPanel(JDialog parent)
    {
        controlStepSizeLabel = new JLabel(STRINGS.getString("POWERCONTROL_CONTROL_STEP"));
        minThresholdLabel = new JLabel(STRINGS.getString("POWERCONTROL_MIN_THRESHOLD"));
        dynamicRangeLabel = new JLabel(STRINGS.getString("POWERCONTROL_DYNAMIC_RANGE"));
        setLayout(new LabeledPairLayout());
        controlStepSizeField = new FormattedCalculatorField(parent);
        minThresholdField = new FormattedCalculatorField(parent);
        dynamicRangeField = new FormattedCalculatorField(parent);
        add(controlStepSizeLabel, "label");
        add(controlStepSizeField, "field");
        add(minThresholdLabel, "label");
        add(minThresholdField, "field");
        add(dynamicRangeLabel, "label");
        add(dynamicRangeField, "field");
        setPowerControlEnabled(false);
        setBorder(new TitledBorder(STRINGS.getString("POWERCONTROL_AT_RECEIVER")));
    }

    public void setPowerControlEnabled(boolean value)
    {
        controlStepSizeLabel.setEnabled(value);
        controlStepSizeField.setEnabled(value);
        minThresholdLabel.setEnabled(value);
        minThresholdField.setEnabled(value);
        dynamicRangeLabel.setEnabled(value);
        dynamicRangeField.setEnabled(value);
    }

    public void setPowerControl(PowerControl pc)
    {
        setControlStepSize(pc.getPowerControlStep());
        setMinThreshold(pc.getPowerControlMinimum());
        setDynamicRange(pc.getPowerControlRange());
    }

    public void updateModel(PowerControl pc)
    {
        pc.setPowerControlStep(getControlStepSize());
        pc.setPowerControlMinimum(getMinThreshold());
        pc.setPowerControlRange(getDynamicRange());
    }

    public double getControlStepSize()
    {
        return ((Number)controlStepSizeField.getValue()).doubleValue();
    }

    public double getMinThreshold()
    {
        return ((Number)minThresholdField.getValue()).doubleValue();
    }

    public double getDynamicRange()
    {
        return ((Number)dynamicRangeField.getValue()).doubleValue();
    }

    public void setDynamicRange(double dynamicRange)
    {
        dynamicRangeField.setValue(new Double(dynamicRange));
    }

    public void setControlStepSize(double controlStepSize)
    {
        controlStepSizeField.setValue(new Double(controlStepSize));
    }

    public void setMinThreshold(double minThreshold)
    {
        minThresholdField.setValue(new Double(minThreshold));
    }

    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private static final ResourceBundle STRINGS;
    private JLabel controlStepSizeLabel;
    private JLabel minThresholdLabel;
    private JLabel dynamicRangeLabel;
    private JFormattedTextField controlStepSizeField;
    private JFormattedTextField minThresholdField;
    private JFormattedTextField dynamicRangeField;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
