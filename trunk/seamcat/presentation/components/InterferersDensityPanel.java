// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferersDensityPanel.java

package org.seamcat.presentation.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.function.Function;
import org.seamcat.model.core.*;
import org.seamcat.presentation.*;

public class InterferersDensityPanel extends JPanel
{

    public InterferersDensityPanel(JDialog parent)
    {
        super(new LabeledPairLayout());
        btnActivity = new JButton(STRINGS.getString("BTN_CAPTION_FUNCTION"));
        lblDensity = new JLabel(STRINGS.getString("DENSITY_TX"));
        lblProbalility = new JLabel(STRINGS.getString("DENSITY_PROB_TX"));
        lblActivity = new JLabel(STRINGS.getString("DENSITY_ACTIVITY"));
        lblTime = new JLabel(STRINGS.getString("DENSITY_TIME"));
        lblProtection = new JLabel(STRINGS.getString("DENSITY_PROTECTION_DIST"));
        tfDensity = new FormattedCalculatorField(parent);
        tfProbability = new FormattedCalculatorField(parent);
        tfTime = new FormattedCalculatorField(parent);
        tfProtection = new FormattedCalculatorField(parent);
        editActivity = new DialogFunctionDefine(parent, true);
        btnActivity.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(editActivity.show(transmitterActivity, InterferersDensityPanel.STRINGS.getString("DENSITY_TX_ACTIVITY")))
                    transmitterActivity = editActivity.getFunction();
            }

            final InterferersDensityPanel this$0;

            
            {
                this$0 = InterferersDensityPanel.this;
                super();
            }
        }
);
        add(lblDensity, "label");
        add(tfDensity, "field");
        add(lblProbalility, "label");
        add(tfProbability, "field");
        add(lblActivity, "label");
        add(btnActivity, "field");
        add(lblTime, "label");
        add(tfTime, "field");
        add(lblProtection, "label");
        add(tfProtection, "field");
        setBorder(new TitledBorder(STRINGS.getString("DENSITY_CAPTION")));
    }

    public void setInterferenceLink(InterferenceLink il)
    {
        transmitterActivity = il.getInterferingLink().getInterferingTransmitter().getActivity();
        tfDensity.setValue(new Double(il.getInterferingLink().getInterferingTransmitter().getDensActiveTx()));
        tfProbability.setValue(new Double(il.getInterferingLink().getInterferingTransmitter().getTransProb()));
        tfTime.setValue(new Double(il.getInterferingLink().getInterferingTransmitter().getTime()));
        tfProtection.setValue(new Double(il.getProtectionDistance()));
    }

    public void updateModel(InterferenceLink il)
    {
        il.getInterferingLink().getInterferingTransmitter().setActivity(transmitterActivity);
        il.getInterferingLink().getInterferingTransmitter().setDensActiveTx(((Number)tfDensity.getValue()).doubleValue());
        il.getInterferingLink().getInterferingTransmitter().setTransProb(((Number)tfProbability.getValue()).doubleValue());
        il.getInterferingLink().getInterferingTransmitter().setTime(((Number)tfTime.getValue()).doubleValue());
        il.setProtectionDistance(((Number)tfProtection.getValue()).doubleValue());
    }

    private static final ResourceBundle STRINGS;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private Function transmitterActivity;
    private DialogFunctionDefine editActivity;
    private JFormattedTextField tfDensity;
    private JFormattedTextField tfProbability;
    private JFormattedTextField tfTime;
    private JFormattedTextField tfProtection;
    private JButton btnActivity;
    private JLabel lblDensity;
    private JLabel lblProbalility;
    private JLabel lblActivity;
    private JLabel lblTime;
    private JLabel lblProtection;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }




}
