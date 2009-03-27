// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferenceCriteriaPanel.java

package org.seamcat.presentation.components;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.model.Receiver;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class InterferenceCriteriaPanel extends JPanel
{

    public InterferenceCriteriaPanel(JDialog parent)
    {
        tfProtectionRatio = new FormattedCalculatorField(parent);
        tfExtendedProtectionRatio = new FormattedCalculatorField(parent);
        tfNoiseAugmentation = new FormattedCalculatorField(parent);
        tfInterferenceToNoiseRatio = new FormattedCalculatorField(parent);
        JLabel protectionRatioLabel = new JLabel(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_INTFCRITERIA_PROTRATIO"));
        JLabel extendedProtectionRatioLabel = new JLabel(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_INTFCRITERIA_EXTPROTECTION_CAPTION"));
        JLabel noiseArgumentationLabel = new JLabel(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_INTFCRITERIA_NOISEARG_CAPTION"));
        JLabel interferenceToNoiseRatioLabel = new JLabel(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_INTFCRITERIA_INTFTONOISE_CAPTION"));
        JLabel info = new JLabel(stringlist.getString("INTERFERENCE_CRITERION_WARNING"));
        info.setHorizontalAlignment(2);
        setLayout(new LabeledPairLayout());
        add(protectionRatioLabel, "label");
        add(tfProtectionRatio, "field");
        add(extendedProtectionRatioLabel, "label");
        add(tfExtendedProtectionRatio, "field");
        add(noiseArgumentationLabel, "label");
        add(tfNoiseAugmentation, "field");
        add(interferenceToNoiseRatioLabel, "label");
        add(tfInterferenceToNoiseRatio, "field");
        add(new JLabel(""), "label");
        add(info, "field");
        setBorder(new TitledBorder(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_INTFCRITERIA_CAPTION")));
    }

    public void setReceiver(Receiver receiver)
    {
        this.receiver = receiver;
        tfProtectionRatio.setValue(new Double(receiver.getProtectionRatio()));
        tfExtendedProtectionRatio.setValue(new Double(receiver.getExtendedProtectionRatio()));
        tfNoiseAugmentation.setValue(new Double(receiver.getNoiseAugmentation()));
        tfInterferenceToNoiseRatio.setValue(new Double(receiver.getInterferenceToNoiseRatio()));
    }

    public void updateModel(Receiver receiver)
    {
        receiver.setProtectionRatio(getProtectionRatio());
        receiver.setExtendedProtectionRatio(getExtendedProtectionRatio());
        receiver.setNoiseAugmentation(getNoiseAugmentation());
        receiver.setInterferenceToNoiseRatio(getInterferenceToNoiseRatio());
    }

    public double getInterferenceToNoiseRatio()
    {
        return ((Number)tfInterferenceToNoiseRatio.getValue()).doubleValue();
    }

    public double getExtendedProtectionRatio()
    {
        return ((Number)tfExtendedProtectionRatio.getValue()).doubleValue();
    }

    public double getProtectionRatio()
    {
        return ((Number)tfProtectionRatio.getValue()).doubleValue();
    }

    public double getNoiseAugmentation()
    {
        return ((Number)tfNoiseAugmentation.getValue()).doubleValue();
    }

    private static final ResourceBundle stringlist;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private JFormattedTextField tfProtectionRatio;
    private JFormattedTextField tfExtendedProtectionRatio;
    private JFormattedTextField tfNoiseAugmentation;
    private JFormattedTextField tfInterferenceToNoiseRatio;
    private Receiver receiver;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
