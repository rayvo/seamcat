// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CalculationModePanel.java

package org.seamcat.presentation.components.interferencecalc;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.model.engines.ICEConfiguration;

public class CalculationModePanel extends JPanel
    implements ActionListener
{

    public CalculationModePanel()
    {
        compatibility = new JRadioButton("Compatibility");
        translation = new JRadioButton("Translation");
        group = new ButtonGroup();
        setBorder(new TitledBorder("Calculation Mode"));
        compatibility.addActionListener(this);
        translation.addActionListener(this);
        group.add(compatibility);
        group.add(translation);
        setLayout(new GridLayout(2, 1));
        add(compatibility);
        add(translation);
        compatibility.setSelected(true);
    }

    public void init(ICEConfiguration _iceconf)
    {
        iceconf = _iceconf;
        setModeIsCompability(!iceconf.calculationModeIsTranslation());
    }

    public boolean modeIsCompability()
    {
        return compatibility.isSelected();
    }

    public boolean modeIsTranslation()
    {
        return translation.isSelected();
    }

    public void addModeListener(ActionListener acc)
    {
        compatibility.addActionListener(acc);
        translation.addActionListener(acc);
    }

    public void setModeIsCompability(boolean comp)
    {
        compatibility.setSelected(comp);
        translation.setSelected(!comp);
    }

    public void actionPerformed(ActionEvent e)
    {
        try
        {
            iceconf.setCalculationModeIsTranslation(translation.isSelected());
        }
        catch(NullPointerException ne) { }
    }

    private JRadioButton compatibility;
    private JRadioButton translation;
    private ButtonGroup group;
    private ICEConfiguration iceconf;
}
