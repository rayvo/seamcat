// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SensitivityPanel.java

package org.seamcat.presentation.components;

import java.awt.Window;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.model.WantedReceiver;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class SensitivityPanel extends JPanel
{

    public SensitivityPanel(Window parent)
    {
        sensitivityLabel = new JLabel(STRINGS.getString("SENSITIVITY_CAPTION"));
        setLayout(new LabeledPairLayout());
        sensitivityField = new FormattedCalculatorField(parent);
        add(sensitivityLabel, "label");
        add(sensitivityField, "field");
    }

    private double getSensitivity()
    {
        return ((Number)sensitivityField.getValue()).doubleValue();
    }

    private void setSensitivity(double n)
    {
        sensitivityField.setValue(new Double(n));
    }

    public void setWantedReceiver(WantedReceiver wr)
    {
        setSensitivity(wr.getSensitivity());
    }

    public void updateModel(WantedReceiver wr)
    {
        wr.setSensitivity(getSensitivity());
    }

    public void clear()
    {
        setSensitivity(-103D);
    }

    private static final ResourceBundle STRINGS;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private JLabel sensitivityLabel;
    private JFormattedTextField sensitivityField;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
