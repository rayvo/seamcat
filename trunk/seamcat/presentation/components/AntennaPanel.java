// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AntennaPanel.java

package org.seamcat.presentation.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.model.Antenna;
import org.seamcat.model.core.AntennaPattern;
import org.seamcat.presentation.*;

public class AntennaPanel extends JPanel
{

    public AntennaPanel(JDialog parent)
    {
        super(new LabeledPairLayout());
        lbPeakGain = new JLabel(STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_PEAK_CAPTION"));
        cbHPattern = new JCheckBox(STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_HORIZONTAL_CAPTION"));
        cbVPattern = new JCheckBox(STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_VERTICAL_CAPTION"));
        cbSPattern = new JCheckBox(STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_SPHERICAL_CAPTION"));
        btnHPattern = new JButton(STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_CAPTION"));
        btnVPattern = new JButton(STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_CAPTION"));
        btnSPattern = new JButton(STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_CAPTION"));
        tfAntennaPeakGain = new FormattedCalculatorField(parent);
        dialogPattern = new DialogAntennaPattern(parent);
        cbHPattern.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnHPattern.setEnabled(cbHPattern.isSelected());
                btnSPattern.setEnabled(false);
                if(cbHPattern.isSelected())
                    cbSPattern.setSelected(false);
            }

            final AntennaPanel this$0;

            
            {
                this$0 = AntennaPanel.this;
                super();
            }
        }
);
        cbVPattern.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnVPattern.setEnabled(cbVPattern.isSelected());
                btnSPattern.setEnabled(false);
                if(cbVPattern.isSelected())
                    cbSPattern.setSelected(false);
            }

            final AntennaPanel this$0;

            
            {
                this$0 = AntennaPanel.this;
                super();
            }
        }
);
        cbSPattern.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                btnHPattern.setEnabled(false);
                btnVPattern.setEnabled(false);
                btnSPattern.setEnabled(cbSPattern.isSelected());
                if(cbSPattern.isSelected())
                {
                    cbHPattern.setSelected(false);
                    cbVPattern.setSelected(false);
                }
            }

            final AntennaPanel this$0;

            
            {
                this$0 = AntennaPanel.this;
                super();
            }
        }
);
        btnSPattern.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(dialogPattern.show(spherical, AntennaPanel.STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_SPHERICAL_TITLE")))
                    spherical = dialogPattern.getData();
            }

            final AntennaPanel this$0;

            
            {
                this$0 = AntennaPanel.this;
                super();
            }
        }
);
        btnHPattern.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(dialogPattern.show(horizontal, AntennaPanel.STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_HORIZONTAL_TITLE")))
                    horizontal = dialogPattern.getData();
            }

            final AntennaPanel this$0;

            
            {
                this$0 = AntennaPanel.this;
                super();
            }
        }
);
        btnVPattern.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(dialogPattern.show(vertical, AntennaPanel.STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_PATTERN_VERTICAL_TITLE")))
                    vertical = dialogPattern.getData();
            }

            final AntennaPanel this$0;

            
            {
                this$0 = AntennaPanel.this;
                super();
            }
        }
);
        add(lbPeakGain, "label");
        add(tfAntennaPeakGain, "field");
        add(cbHPattern, "label");
        add(btnHPattern, "field");
        add(cbVPattern, "label");
        add(btnVPattern, "field");
        add(cbSPattern, "label");
        add(btnSPattern, "field");
        setBorder(new TitledBorder(STRINGS.getString("LIBRARY_ANTENNA_ADDEDIT_ANTENNAPATTERNS_CAPTION")));
    }

    public void updateModel(Antenna antenna)
    {
        antenna.setPeakGain(getAntennaPeakGain());
        antenna.setUseHorizontalPattern(cbHPattern.isSelected());
        antenna.setUseVerticalPattern(cbVPattern.isSelected());
        antenna.setUseSphericalPattern(cbSPattern.isSelected());
        antenna.setHorizontalPattern(getHorizontal());
        antenna.setVerticalPattern(getVertical());
        antenna.setSphericalPattern(getSpherical());
        antenna.updateNodeAttributes();
    }

    public Antenna getAntenna()
    {
        return antenna;
    }

    public void setAntenna(Antenna _antenna, boolean readonly)
    {
        if(_antenna == null)
        {
            return;
        } else
        {
            antenna = _antenna;
            tfAntennaPeakGain.setValue(new Double(antenna.getPeakGain()));
            cbHPattern.setSelected(antenna.getUseHorizontalPattern());
            cbVPattern.setSelected(antenna.getUseVerticalPattern());
            cbSPattern.setSelected(antenna.getUseSphericalPattern());
            cbHPattern.setEnabled(!readonly);
            cbSPattern.setEnabled(!readonly);
            cbVPattern.setEnabled(!readonly);
            lbPeakGain.setEnabled(!readonly);
            tfAntennaPeakGain.setEnabled(!readonly);
            btnHPattern.setEnabled(antenna.getUseHorizontalPattern());
            btnVPattern.setEnabled(antenna.getUseVerticalPattern());
            btnSPattern.setEnabled(antenna.getUseSphericalPattern());
            horizontal = antenna.getHorizontalPattern();
            vertical = antenna.getVerticalPattern();
            spherical = antenna.getSphericalPattern();
            return;
        }
    }

    public double getAntennaPeakGain()
    {
        return ((Number)tfAntennaPeakGain.getValue()).doubleValue();
    }

    public AntennaPattern getHorizontal()
    {
        return horizontal;
    }

    public AntennaPattern getVertical()
    {
        return vertical;
    }

    public AntennaPattern getSpherical()
    {
        return spherical;
    }

    public void setAntennaPeakGain(double value)
    {
        tfAntennaPeakGain.setValue(new Double(value));
    }

    public void setHorizontal(AntennaPattern horizontal)
    {
        this.horizontal = horizontal;
    }

    public void setVertical(AntennaPattern vertical)
    {
        this.vertical = vertical;
    }

    public void setSpherical(AntennaPattern spherical)
    {
        this.spherical = spherical;
    }

    public void clear()
    {
        antenna = null;
        horizontal = null;
        vertical = null;
        spherical = null;
    }

    private static final ResourceBundle STRINGS;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private JFormattedTextField tfAntennaPeakGain;
    private JLabel lbPeakGain;
    private JCheckBox cbHPattern;
    private JCheckBox cbVPattern;
    private JCheckBox cbSPattern;
    private JButton btnHPattern;
    private JButton btnVPattern;
    private JButton btnSPattern;
    private AntennaPattern horizontal;
    private AntennaPattern vertical;
    private AntennaPattern spherical;
    private DialogAntennaPattern dialogPattern;
    private Antenna antenna;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }














}
