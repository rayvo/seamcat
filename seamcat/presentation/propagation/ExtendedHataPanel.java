// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExtendedHataPanel.java

package org.seamcat.presentation.propagation;

import java.awt.Window;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.propagation.*;

// Referenced classes of package org.seamcat.presentation.propagation:
//            PropagationModelPanel

public class ExtendedHataPanel extends JPanel
    implements PropagationModelPanel
{

    public ExtendedHataPanel(Window parent)
    {
        this(parent, true, true, true, true);
        clear();
    }

    public ExtendedHataPanel(Window parent, boolean hasBorder, boolean showInfoText, boolean generalEnvState, boolean propagationEnvState)
    {
        super(new LabeledPairLayout());
        cmbGeneralEnv = new JComboBox(PropagationModelConstants.ENVIRONMENT);
        cmbLocalEnvVr = new JComboBox(PropagationModelConstants.DOOR);
        cmbLocalEnvWt = new JComboBox(PropagationModelConstants.DOOR);
        cmbProgagationEnv = new JComboBox(PropagationModelConstants.ROOF);
        cbVariations = new JCheckBox(STRINGLIST.getString("PROPAGATION_VARIATIONS"));
        cbMedianLoss = new JCheckBox(STRINGLIST.getString("PROPAGATION_MEDIANLOSS"));
        txtSizeRoom = new FormattedCalculatorField(parent);
        txtHeightFloor = new FormattedCalculatorField(parent);
        txtWallLoss = new FormattedCalculatorField(parent);
        txtWallLossStdDev = new FormattedCalculatorField(parent);
        txtWallLossInOut = new FormattedCalculatorField(parent);
        txtWallLossStdDevInOut = new FormattedCalculatorField(parent);
        txtLossBetween = new FormattedCalculatorField(parent);
        txtEmpirical = new FormattedCalculatorField(parent);
        JLabel lblGeneralEnv = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_GENERALENV"));
        JLabel lblLocalEnvWt = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_LOCALENVWT"));
        JLabel lblLocalEnvVr = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_LOCALENVVR"));
        JLabel lblSizeRoom = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_SIZEROOM"));
        JLabel lblHeightFloor = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_HEIGHTFLOOR"));
        JLabel lblProgagationEnv = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_PROPAGATIONENV"));
        JLabel lblWallLoss = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_WALLLOSS"));
        JLabel lblWallLossStdDev = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_WALLLOSSSTDDEV"));
        JLabel lblWallLossInOut = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_WALLLOSSINOUT"));
        JLabel lblWallLossStdDevInOut = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_WALLLOSSINOUTSTDDEV"));
        JLabel lblLossBetween = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_LOSSBETWEEN"));
        JLabel lblEmpirical = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_EMPIRICAL"));
        cmbGeneralEnv.setEnabled(generalEnvState);
        cmbProgagationEnv.setEnabled(propagationEnvState);
        add(cbVariations, "label");
        add(cbMedianLoss, "field");
        add(lblGeneralEnv, "label");
        add(cmbGeneralEnv, "field");
        add(lblLocalEnvVr, "label");
        add(cmbLocalEnvVr, "field");
        add(lblLocalEnvWt, "label");
        add(cmbLocalEnvWt, "field");
        add(lblProgagationEnv, "label");
        add(cmbProgagationEnv, "field");
        add(lblWallLoss, "label");
        add(txtWallLoss, "field");
        add(lblWallLossStdDev, "label");
        add(txtWallLossStdDev, "field");
        add(lblWallLossInOut, "label");
        add(txtWallLossInOut, "field");
        add(lblWallLossStdDevInOut, "label");
        add(txtWallLossStdDevInOut, "field");
        add(lblLossBetween, "label");
        add(txtLossBetween, "field");
        add(lblEmpirical, "label");
        add(txtEmpirical, "field");
        add(lblSizeRoom, "label");
        add(txtSizeRoom, "field");
        add(lblHeightFloor, "label");
        add(txtHeightFloor, "field");
        if(showInfoText)
        {
            add(new JLabel(" "), "label");
            add(new JLabel(NOTE), "field");
        }
        if(hasBorder)
            setBorder(new TitledBorder(STRINGLIST.getString("PROPAGATION_EXTHATA_CAPTION")));
    }

    public void clear()
    {
        setPropagationable(new HataSE21Model());
    }

    public PropagationModel getPropagationable()
    {
        HataAndSDModel p = new HataSE21Model(cmbLocalEnvWt.getSelectedIndex(), cmbLocalEnvVr.getSelectedIndex(), cmbGeneralEnv.getSelectedIndex(), cmbProgagationEnv.getSelectedIndex(), ((Number)txtWallLoss.getValue()).doubleValue(), ((Number)txtWallLossStdDev.getValue()).doubleValue(), ((Number)txtWallLossInOut.getValue()).doubleValue(), ((Number)txtWallLossStdDevInOut.getValue()).doubleValue(), ((Number)txtLossBetween.getValue()).doubleValue(), ((Number)txtHeightFloor.getValue()).doubleValue(), ((Number)txtSizeRoom.getValue()).doubleValue(), ((Number)txtEmpirical.getValue()).doubleValue());
        p.setVariationsSelected(cbVariations.isSelected());
        p.setMedianSelected(cbMedianLoss.isSelected());
        return p;
    }

    public void setPropagationable(PropagationModel p)
    {
        if(p instanceof HataAndSDModel)
            setPropagationable((HataAndSDModel)p);
        else
            throw new IllegalArgumentException("Object must be an instance of class <HataSE21Model>");
    }

    public void setPropagationable(HataAndSDModel p)
    {
        cmbLocalEnvWt.setSelectedIndex(p.getTxLocalEnv());
        cmbLocalEnvVr.setSelectedIndex(p.getRxLocalEnv());
        cmbGeneralEnv.setSelectedIndex(p.getGeneralEnv());
        cmbProgagationEnv.setSelectedIndex(p.getPropagEnv());
        txtWallLoss.setValue(new Double(p.getWiLoss()));
        txtWallLossStdDev.setValue(new Double(p.getWiStdDev()));
        txtWallLossInOut.setValue(new Double(p.getWeLoss()));
        txtWallLossStdDevInOut.setValue(new Double(p.getWeStdDev()));
        txtLossBetween.setValue(new Double(p.getFloorLoss()));
        txtHeightFloor.setValue(new Double(p.getFloorHeight()));
        txtSizeRoom.setValue(new Double(p.getRoomSize()));
        txtEmpirical.setValue(new Double(p.getEmpiricalParameter()));
        cbVariations.setSelected(p.getVariationsSelected());
        cbMedianLoss.setSelected(p.getMedianSelected());
    }

    protected static final ResourceBundle STRINGLIST;
    private static final String NOTE;
    private JFormattedTextField txtSizeRoom;
    private JFormattedTextField txtHeightFloor;
    private JFormattedTextField txtWallLoss;
    private JFormattedTextField txtWallLossStdDev;
    private JFormattedTextField txtWallLossInOut;
    private JFormattedTextField txtWallLossStdDevInOut;
    private JFormattedTextField txtLossBetween;
    private JFormattedTextField txtEmpirical;
    private JComboBox cmbGeneralEnv;
    private JComboBox cmbLocalEnvVr;
    private JComboBox cmbLocalEnvWt;
    private JComboBox cmbProgagationEnv;
    private JCheckBox cbVariations;
    private JCheckBox cbMedianLoss;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        NOTE = STRINGLIST.getString("PROPAGATION_EXTHATA_INFORMATION");
    }
}
