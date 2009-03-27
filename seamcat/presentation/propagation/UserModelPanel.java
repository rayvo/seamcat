// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   UserModelPanel.java

package org.seamcat.presentation.propagation;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.presentation.LabeledPairLayout;

public class UserModelPanel extends JPanel
{

    private UserModelPanel()
    {
        lblGeneralEnv = new JLabel("General Environment  ", 2);
        cmbEnviroment = new JComboBox(environment);
        lblLocalEnvironmentVr = new JLabel("Local Environment (Vr)  ", 2);
        cmbDoorVr = new JComboBox(door);
        lblLocalEnvWt = new JLabel("Local Environment (Wt)  ", 2);
        cmbDoorWt = new JComboBox(door);
        lblPropagationEnv = new JLabel("Propagation Environment  ", 2);
        cmbRoof = new JComboBox(roof);
        setLayout(new LabeledPairLayout());
        add(lblGeneralEnv, "label");
        add(cmbEnviroment, "field");
        add(lblLocalEnvironmentVr, "label");
        add(cmbDoorVr, "field");
        add(lblLocalEnvWt, "label");
        add(cmbDoorWt, "field");
        add(lblPropagationEnv, "label");
        add(cmbRoof, "field");
        setBorder(new TitledBorder("User-defined Model"));
    }

    private String environment[] = {
        "Rural", "Suburban", "Urban"
    };
    private String door[] = {
        "Indoor", "Outdoor"
    };
    private String roof[] = {
        "Above Roof", "Below Roof"
    };
    private JLabel lblGeneralEnv;
    private JComboBox cmbEnviroment;
    private JLabel lblLocalEnvironmentVr;
    private JComboBox cmbDoorVr;
    private JLabel lblLocalEnvWt;
    private JComboBox cmbDoorWt;
    private JLabel lblPropagationEnv;
    private JComboBox cmbRoof;
}
