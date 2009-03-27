// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TranslationParametersPanel.java

package org.seamcat.presentation.components.interferencecalc;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.model.Components;
import org.seamcat.model.Workspace;
import org.seamcat.model.engines.ICEConfiguration;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class TranslationParametersPanel extends JPanel
{

    public TranslationParametersPanel()
    {
        listModel = new DefaultListModel();
        parameters = new JList(listModel);
        minValue = new JFormattedTextField();
        maxValue = new JFormattedTextField();
        pointsValue = new JFormattedTextField();
        minLabel = new JLabel("Min (dBm)");
        maxLabel = new JLabel("Max (dBm)");
        pointsLabel = new JLabel("# Points");
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Translation Parameters"));
        parameters.setSelectionMode(1);
        add(new JScrollPane(parameters), "Center");
        minValue.setColumns(5);
        minValue.setHorizontalAlignment(4);
        minValue.setFormatterFactory(dialogFormats.getIntegerFactory());
        minValue.setValue(new Integer(0));
        maxValue.setColumns(5);
        maxValue.setHorizontalAlignment(4);
        maxValue.setFormatterFactory(dialogFormats.getIntegerFactory());
        maxValue.setValue(new Integer(100));
        pointsValue.setColumns(5);
        pointsValue.setHorizontalAlignment(4);
        pointsValue.setFormatterFactory(dialogFormats.getIntegerFactory());
        pointsValue.setValue(new Integer(100));
        JPanel bottom = new JPanel(new LabeledPairLayout());
        bottom.add(minLabel, "label");
        bottom.add(minValue, "field");
        bottom.add(maxLabel, "label");
        bottom.add(maxValue, "field");
        bottom.add(pointsLabel, "label");
        bottom.add(pointsValue, "field");
        add(bottom, "South");
    }

    public void init(ICEConfiguration _iceconf, Workspace workspace)
    {
        iceconf = _iceconf;
        listModel.clear();
        listModel.addElement("Blocking response level / Victim link");
        listModel.addElement("Intermodulation response level / Victim link");
        int count = workspace.getInterferenceLinks().size();
        for(int i = 0; i < count; i++)
            listModel.addElement((new StringBuilder()).append("Power supplied / Interfering link ").append(i + 1).toString());

        parameters.setSelectedIndex(iceconf.getTranslationParameter());
        minValue.setValue(new Double(iceconf.getTranslationMin()));
        maxValue.setValue(new Double(iceconf.getTranslationMax()));
        pointsValue.setValue(new Double(iceconf.getTranslationPoints()));
    }

    public void updateModel()
    {
        try
        {
            iceconf.setTranslationParameter(parameters.getSelectedIndex());
            iceconf.setTranslationMin(((Number)minValue.getValue()).doubleValue());
            iceconf.setTranslationMax(((Number)maxValue.getValue()).doubleValue());
            iceconf.setTranslationPoints(((Number)pointsValue.getValue()).doubleValue());
        }
        catch(NullPointerException ne) { }
    }

    public void setElementStatusEnabled(boolean value)
    {
        parameters.setEnabled(value);
        minValue.setEnabled(value);
        minLabel.setEnabled(value);
        maxValue.setEnabled(value);
        maxLabel.setEnabled(value);
        pointsValue.setEnabled(value);
        pointsLabel.setEnabled(value);
    }

    private DefaultListModel listModel;
    private JList parameters;
    private JFormattedTextField minValue;
    private JFormattedTextField maxValue;
    private JFormattedTextField pointsValue;
    private JLabel minLabel;
    private JLabel maxLabel;
    private JLabel pointsLabel;
    private ICEConfiguration iceconf;
    private static SeamcatTextFieldFormats dialogFormats = new SeamcatTextFieldFormats();

}
