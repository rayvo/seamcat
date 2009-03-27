// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EmissionCharacteristicsPanel.java

package org.seamcat.presentation.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.Function2;
import org.seamcat.model.Transmitter;
import org.seamcat.presentation.*;

public class EmissionCharacteristicsPanel extends JPanel
{

    public EmissionCharacteristicsPanel(JDialog parent)
    {
        super(new LabeledPairLayout());
        xbUnwantedEmissionFloor = new JCheckBox(STRINGS.getString("EMISSION_CHARACTERISTICS_UNWANTED_FLOOR"));
        xbPowerControl = new JCheckBox(STRINGS.getString("EMISSION_CHARACTERISTICS_POWER_CONTROL"));
        lblPower = new JLabel(STRINGS.getString("EMISSION_CHARACTERISTICS_POWER"));
        diagMask = new DialogFunction2Define(parent, true, false);
        powerDistDialog = new DistributionDialog(parent, true);
        JButton powerButton = new JButton(STRINGS.getString("BTN_CAPTION_DISTRIBUTION"));
        JButton maskButton = new JButton(STRINGS.getString("BTN_CAPTION_FUNCTION"));
        final JButton floorButton = new JButton(STRINGS.getString("BTN_CAPTION_FUNCTION"));
        floorButton.setEnabled(false);
        powerButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(powerDistDialog.showDistributionDialog(powerDist, EmissionCharacteristicsPanel.STRINGS.getString("EMISSION_CHARACTERISTICS_POWER_TITLE")))
                    powerDist = powerDistDialog.getDistributionable();
                lblPower.setText((new StringBuilder()).append(EmissionCharacteristicsPanel.STRINGS.getString("EMISSION_CHARACTERISTICS_POWER")).append(" [").append(powerDist.toString()).append("] ").toString());
            }

            final EmissionCharacteristicsPanel this$0;

            
            {
                this$0 = EmissionCharacteristicsPanel.this;
                super();
            }
        }
);
        maskButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(diagMask.show(unwantedEmissionMask, EmissionCharacteristicsPanel.STRINGS.getString("EMISSION_CHARACTERISTICS_UNWANTED_TITLE")))
                    unwantedEmissionMask = diagMask.getFunction();
            }

            final EmissionCharacteristicsPanel this$0;

            
            {
                this$0 = EmissionCharacteristicsPanel.this;
                super();
            }
        }
);
        floorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(diagMask.show(unwantedEmissionFloor, EmissionCharacteristicsPanel.STRINGS.getString("EMISSION_CHARACTERISTICS_UNWANTED_FLOOR_TITLE")))
                    unwantedEmissionFloor = diagMask.getFunction();
            }

            final EmissionCharacteristicsPanel this$0;

            
            {
                this$0 = EmissionCharacteristicsPanel.this;
                super();
            }
        }
);
        xbUnwantedEmissionFloor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                floorButton.setEnabled(xbUnwantedEmissionFloor.isSelected());
            }

            final JButton val$floorButton;
            final EmissionCharacteristicsPanel this$0;

            
            {
                this$0 = EmissionCharacteristicsPanel.this;
                floorButton = jbutton;
                super();
            }
        }
);
        JLabel lblUnwantedEmissionMask = new JLabel(STRINGS.getString("EMISSION_CHARACTERISTICS_UNWANTED"));
        add(lblPower, "label");
        add(powerButton, "field");
        add(lblUnwantedEmissionMask, "label");
        add(maskButton, "field");
        add(xbUnwantedEmissionFloor, "label");
        add(floorButton, "field");
        add(xbPowerControl, "label");
        add(Box.createHorizontalGlue(), "field");
        setBorder(new TitledBorder(STRINGS.getString("EMISSION_CHARACTERISTICS_TITLE")));
    }

    public boolean isPowerControlEnabled()
    {
        return xbPowerControl.isSelected();
    }

    public void addPowerListener(ActionListener act)
    {
        xbPowerControl.addActionListener(act);
    }

    public Transmitter updateModel(Transmitter it)
    {
        if(xbPowerControl.isSelected())
            it.setUsePowerControl(true);
        else
            it.setUsePowerControl(false);
        if(xbUnwantedEmissionFloor.isSelected())
        {
            it.setUseUnwantedEmission(true);
            it.setUnwantedEmissionsFloor(unwantedEmissionFloor);
        } else
        {
            it.setUseUnwantedEmission(false);
        }
        it.setUnwantedEmissions(unwantedEmissionMask);
        it.setPowerSuppliedDistribution(powerDist);
        return it;
    }

    public void setTransmitter(Transmitter it)
    {
        if(it.getUsePowerControl() ^ xbPowerControl.isSelected())
            xbPowerControl.doClick();
        if(it.getUseUnwantedEmissionFloor() ^ xbUnwantedEmissionFloor.isSelected())
            xbUnwantedEmissionFloor.doClick();
        powerDist = it.getPowerSuppliedDistribution();
        unwantedEmissionFloor = it.getUnwantedEmissionsFloor();
        unwantedEmissionMask = it.getUnwantedEmissions();
        lblPower.setText((new StringBuilder()).append(STRINGS.getString("EMISSION_CHARACTERISTICS_POWER")).append(" [").append(powerDist.toString()).append("] ").toString());
    }

    public void clear()
    {
        unwantedEmissionMask = null;
        unwantedEmissionFloor = null;
        powerDist = null;
        powerDistDialog.clear();
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/components/EmissionCharacteristicsPanel);
    private static final ResourceBundle STRINGS;
    private DialogFunction2Define diagMask;
    private DistributionDialog powerDistDialog;
    private JCheckBox xbUnwantedEmissionFloor;
    private JCheckBox xbPowerControl;
    private Function2 unwantedEmissionMask;
    private Function2 unwantedEmissionFloor;
    private Distribution powerDist;
    private JLabel lblPower;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }











}
