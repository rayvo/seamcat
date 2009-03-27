// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SDPanel.java

package org.seamcat.presentation.propagation;

import java.awt.GridLayout;
import java.awt.Window;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.propagation.*;

// Referenced classes of package org.seamcat.presentation.propagation:
//            ExtendedHataPanel, PropagationModelPanel

public class SDPanel extends JPanel
    implements PropagationModelPanel
{

    public SDPanel(Window parent)
    {
        super(new GridLayout(2, 1));
        tfWaterCtr = new FormattedCalculatorField(parent);
        tfEarthSurface = new FormattedCalculatorField(parent);
        tfRefrIndex = new FormattedCalculatorField(parent);
        tfRefrProb = new FormattedCalculatorField(parent);
        tfTimePercMin = new FormattedCalculatorField(parent);
        tfTimePercMax = new FormattedCalculatorField(parent);
        JLabel lblWaterCtr = new JLabel(stringlist.getString("PROPAGATION_SD_WATERCTR"));
        JLabel lblEarthSurface = new JLabel(stringlist.getString("PROPAGATION_SD_EARTHSURFACE"));
        JLabel lblRefrIndex = new JLabel(stringlist.getString("PROPAGATION_SD_REFRINDEX"));
        JLabel lblRefrProb = new JLabel(stringlist.getString("PROPAGATION_SD_REFRPROB"));
        JLabel lblTimePercMin = new JLabel(stringlist.getString("PROPAGATION_SD_TIMEPERCMIN"));
        JLabel lblTimePercMax = new JLabel(stringlist.getString("PROPAGATION_SD_TIMEPERCMAX"));
        JPanel sdPanel = new JPanel(new LabeledPairLayout());
        sdPanel.add(lblWaterCtr, "label");
        sdPanel.add(tfWaterCtr, "field");
        sdPanel.add(lblEarthSurface, "label");
        sdPanel.add(tfEarthSurface, "field");
        sdPanel.add(lblRefrIndex, "label");
        sdPanel.add(tfRefrIndex, "field");
        sdPanel.add(lblRefrProb, "label");
        sdPanel.add(tfRefrProb, "field");
        sdPanel.add(lblTimePercMin, "label");
        sdPanel.add(tfTimePercMin, "field");
        sdPanel.add(lblTimePercMax, "label");
        sdPanel.add(tfTimePercMax, "field");
        ehPanel = new ExtendedHataPanel(parent, false, false, false, false);
        clear();
        add(ehPanel);
        add(sdPanel);
        setBorder(new TitledBorder(stringlist.getString("PROPAGATION_SD_CAPTION")));
    }

    public PropagationModel getPropagationable()
    {
        HataAndSDModel _p = (HataAndSDModel)ehPanel.getPropagationable();
        SDModel p = new SDModel(_p.getTxLocalEnv(), _p.getRxLocalEnv(), _p.getWiLoss(), _p.getWiStdDev(), _p.getWeLoss(), _p.getWeStdDev(), _p.getFloorLoss(), _p.getFloorHeight(), _p.getRoomSize(), _p.getEmpiricalParameter(), ((Number)tfWaterCtr.getValue()).doubleValue(), ((Number)tfEarthSurface.getValue()).doubleValue(), ((Number)tfRefrProb.getValue()).doubleValue(), ((Number)tfRefrIndex.getValue()).doubleValue(), ((Number)tfTimePercMin.getValue()).doubleValue(), ((Number)tfTimePercMax.getValue()).doubleValue());
        return p;
    }

    public void setPropagationable(PropagationModel p)
    {
        if(p instanceof SDModel)
            setPropagationable((SDModel)p);
        else
            throw new IllegalArgumentException("Object must be an instance of class <SDModel>");
    }

    public void setPropagationable(SDModel p)
    {
        ehPanel.setPropagationable(p);
        tfWaterCtr.setValue(new Double(p.getWaterCtr()));
        tfEarthSurface.setValue(new Double(p.getEarthSurfaceAdmittance()));
        tfRefrIndex.setValue(new Double(p.getRefrIndexGradient()));
        tfRefrProb.setValue(new Double(p.getRefrLayerProb()));
        tfTimePercMin.setValue(new Double(p.getTimePercMin()));
        tfTimePercMax.setValue(new Double(p.getTimePercMax()));
    }

    public void clear()
    {
        setPropagationable(new SDModel());
    }

    protected static final ResourceBundle stringlist;
    private ExtendedHataPanel ehPanel;
    private JFormattedTextField tfWaterCtr;
    private JFormattedTextField tfEarthSurface;
    private JFormattedTextField tfRefrIndex;
    private JFormattedTextField tfRefrProb;
    private JFormattedTextField tfTimePercMin;
    private JFormattedTextField tfTimePercMax;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
