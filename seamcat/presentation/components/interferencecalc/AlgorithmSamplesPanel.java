// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AlgorithmSamplesPanel.java

package org.seamcat.presentation.components.interferencecalc;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.model.engines.ICEConfiguration;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class AlgorithmSamplesPanel extends JPanel
{

    public AlgorithmSamplesPanel()
    {
        samples = new JFormattedTextField();
        setLayout(new LabeledPairLayout());
        setBorder(new TitledBorder("General"));
        samples.setColumns(15);
        samples.setHorizontalAlignment(4);
        samples.setFormatterFactory(SeamcatTextFieldFormats.getIntegerFactory());
        samples.setEditable(false);
        samples.setEnabled(false);
        add(new JLabel("Algorithm: "), "label");
        add(new JLabel("Complete"), "field");
        add(new JLabel("Samples"), "label");
        add(samples, "field");
    }

    public void init(ICEConfiguration _iceconf)
    {
        iceconf = _iceconf;
        samples.setValue(Integer.valueOf(iceconf.getNumberOfSamples()));
    }

    public void updateModel()
    {
    }

    private static SeamcatTextFieldFormats dialogFormats = new SeamcatTextFieldFormats();
    private JFormattedTextField samples;
    private ICEConfiguration iceconf;

}
