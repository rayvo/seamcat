// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ReceptionCharacteristicsPanel.java

package org.seamcat.presentation.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.Receiver;
import org.seamcat.model.VictimReceiver;
import org.seamcat.presentation.*;

public class ReceptionCharacteristicsPanel extends JPanel
{

    public ReceptionCharacteristicsPanel(JDialog owner)
    {
        this(owner, new Receiver());
    }

    public ReceptionCharacteristicsPanel(JDialog owner, Receiver vr)
    {
        super(new LabeledPairLayout());
        powerControlMaxThreshold = 30D;
        sensitivity = -103D;
        receptionBandwidth = 200D;
        noiseFloorButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        intermodulationButton = new JButton(STRINGLIST.getString("BTN_CAPTION_FUNCTION"));
        blockingResponseButton = new JButton(STRINGLIST.getString("BTN_CAPTION_FUNCTION"));
        noiseLabel = new JLabel(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_NOISE_FLOOR"));
        blockingReponseLabel = new JLabel(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_BLOCKING_RESPONSE"));
        intermodulationLabel = new JLabel(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_INTERMODULATION"));
        sensitivityLabel = new JLabel(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_SENSITIVITY"));
        receptionLabel = new JLabel(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_RECEPTION_BANDWITH"));
        blockingModeLabel = new JLabel(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_BLOCKING_ATTENUATION"));
        powerLabel = new JCheckBox(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_POWER_CONTROL_RANGE"));
        blockingMode = new JComboBox(VictimReceiver.ATTENUATION_MODES);
        functionEditor = new DialogFunctionDefine(owner, true);
        distDialog = new DistributionDialog(owner, true);
        powerControlField = new FormattedCalculatorField(owner);
        sensitivityField = new FormattedCalculatorField(owner);
        receptionField = new FormattedCalculatorField(owner);
        noiseFloorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                boolean accept = receiver.getNoiseFloorDistribution() == null ? distDialog.showDistributionDialog(ReceptionCharacteristicsPanel.STRINGLIST.getString("RECEPTION_CHARACTERISTICS_NOISE_FLOOR_TITLE")) : distDialog.showDistributionDialog(receiver.getNoiseFloorDistribution(), ReceptionCharacteristicsPanel.STRINGLIST.getString("RECEPTION_CHARACTERISTICS_NOISE_FLOOR_TITLE"));
                if(accept)
                    receiver.setNoiseFloorDistribution(distDialog.getDistributionable());
                noiseLabel.setText((new StringBuilder()).append(ReceptionCharacteristicsPanel.STRINGLIST.getString("RECEPTION_CHARACTERISTICS_NOISE_FLOOR")).append(" [").append(receiver.getNoiseFloorDistribution().toString()).append("] ").toString());
            }

            final ReceptionCharacteristicsPanel this$0;

            
            {
                this$0 = ReceptionCharacteristicsPanel.this;
                super();
            }
        }
);
        blockingResponseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(functionEditor.show(receiver.getBlockingResponse(), ReceptionCharacteristicsPanel.STRINGLIST.getString("RECEPTION_CHARACTERISTICS_BLOCKING_RESPONSE_TITLE")))
                    receiver.setBlockingResponse(functionEditor.getFunction());
            }

            final ReceptionCharacteristicsPanel this$0;

            
            {
                this$0 = ReceptionCharacteristicsPanel.this;
                super();
            }
        }
);
        intermodulationButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(functionEditor.show(receiver.getIntermodulationRejection(), ReceptionCharacteristicsPanel.STRINGLIST.getString("RECEPTION_CHARACTERISTICS_INTERMODULATION_TITLE")))
                    receiver.setIntermodulationRejection(functionEditor.getFunction());
            }

            final ReceptionCharacteristicsPanel this$0;

            
            {
                this$0 = ReceptionCharacteristicsPanel.this;
                super();
            }
        }
);
        powerControlField.setValue(new Double(powerControlMaxThreshold));
        powerControlField.setEnabled(false);
        powerLabel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                powerControlField.setEnabled(powerLabel.isSelected());
            }

            final ReceptionCharacteristicsPanel this$0;

            
            {
                this$0 = ReceptionCharacteristicsPanel.this;
                super();
            }
        }
);
        sensitivityField.setValue(new Double(sensitivity));
        receptionField.setValue(new Double(receptionBandwidth));
        setReceiver(vr);
        add(noiseLabel, "label");
        add(noiseFloorButton, "field");
        add(blockingReponseLabel, "label");
        add(blockingResponseButton, "field");
        add(blockingModeLabel, "label");
        add(blockingMode, "field");
        add(intermodulationLabel, "label");
        add(intermodulationButton, "field");
        add(powerLabel, "label");
        add(powerControlField, "field");
        add(sensitivityLabel, "label");
        add(sensitivityField, "field");
        add(receptionLabel, "label");
        add(receptionField, "field");
        setBorder(new TitledBorder(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_TITLE")));
    }

    public void setReceiver(Receiver receiver)
    {
        this.receiver = receiver;
        setPowerControlMaxThreshold(receiver.getPowerControlMaxThreshold());
        setSensitivity(receiver.getSensitivity());
        setReceptionBandwidth(receiver.getReceptionBandwith());
        powerLabel.setSelected(receiver.getUsePowerControlThreshold());
        powerControlField.setEditable(receiver.getUsePowerControlThreshold());
        powerControlField.setValue(Double.valueOf(receiver.getPowerControlMaxThreshold()));
        if(receiver instanceof VictimReceiver)
        {
            VictimReceiver vr = (VictimReceiver)receiver;
            blockingMode.setSelectedIndex(vr.getBlockingAttenuationMode());
            blockingModeLabel.setEnabled(true);
            blockingMode.setEnabled(true);
        } else
        {
            blockingModeLabel.setEnabled(false);
            blockingMode.setEnabled(false);
        }
        noiseLabel.setText((new StringBuilder()).append(STRINGLIST.getString("RECEPTION_CHARACTERISTICS_NOISE_FLOOR")).append(" [").append(receiver.getNoiseFloorDistribution().toString()).append("] ").toString());
    }

    public void updateModel(Receiver receiver)
    {
        receiver.setPowerControlMaxThreshold(getPowerControlMaxThreshold());
        receiver.setUsePowerControlThreshold(powerLabel.isSelected());
        receiver.setSensitivity(getSensitivity());
        receiver.setReceptionBandwith(getReceptionBandwidth());
        if(receiver instanceof VictimReceiver)
        {
            VictimReceiver vr = (VictimReceiver)receiver;
            vr.setBlockingAttenuationMode(blockingMode.getSelectedIndex());
        }
    }

    public double getSensitivity()
    {
        return ((Number)sensitivityField.getValue()).doubleValue();
    }

    public double getReceptionBandwidth()
    {
        return ((Number)receptionField.getValue()).doubleValue();
    }

    public void setPowerControlMaxThreshold(double powerControlMaxThreshold)
    {
        powerControlField.setValue(new Double(powerControlMaxThreshold));
    }

    public void setSensitivity(double sensitivity)
    {
        sensitivityField.setValue(new Double(sensitivity));
    }

    public void setReceptionBandwidth(double receptionBandwidth)
    {
        receptionField.setValue(new Double(receptionBandwidth));
    }

    public double getPowerControlMaxThreshold()
    {
        return ((Number)powerControlField.getValue()).doubleValue();
    }

    private static final ResourceBundle STRINGLIST;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private DialogFunctionDefine functionEditor;
    private DistributionDialog distDialog;
    private double powerControlMaxThreshold;
    private double sensitivity;
    private double receptionBandwidth;
    private JButton noiseFloorButton;
    private JButton intermodulationButton;
    private JButton blockingResponseButton;
    private JFormattedTextField powerControlField;
    private JFormattedTextField sensitivityField;
    private JFormattedTextField receptionField;
    private JLabel noiseLabel;
    private JLabel blockingReponseLabel;
    private JLabel intermodulationLabel;
    private JLabel sensitivityLabel;
    private JLabel receptionLabel;
    private JLabel blockingModeLabel;
    private JCheckBox powerLabel;
    private JComboBox blockingMode;
    protected Receiver receiver;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }






}
