// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   R370Panel.java

package org.seamcat.presentation.propagation;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;
import org.seamcat.propagation.PropagationModel;
import org.seamcat.propagation.R370Model;

// Referenced classes of package org.seamcat.presentation.propagation:
//            PropagationModelPanel

public class R370Panel extends JPanel
    implements PropagationModelPanel
{

    public R370Panel(Window parent)
    {
        super(new LabeledPairLayout());
        xbVariations = new JCheckBox(STRINGLIST.getString("PROPAGATION_VARIATIONS"));
        xbMedianLoss = new JCheckBox(STRINGLIST.getString("PROPAGATION_MEDIANLOSS"));
        cbGeneralEnv = new JComboBox(PropagationModel.ENVIRONMENT);
        cbSystem = new JComboBox(R370Model.SYSTEM);
        tfTimePercMin = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
        tfTimePercMax = new JSpinner(new SpinnerNumberModel(0, 0, 50, 1));
        useLocalClutter = new JCheckBox(STRINGLIST.getString("PROPAGATION_R370_USE_LOCAL_CLUTTER"));
        informationLabel = new JLabel(STRINGLIST.getString("PROPAGATION_R370_INFORMATION"));
        localClutter = new FormattedCalculatorField(parent);
        useLocalClutter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                localClutter.setEnabled(useLocalClutter.isSelected());
            }

            final R370Panel this$0;

            
            {
                this$0 = R370Panel.this;
                super();
            }
        }
);
        JLabel lblGeneralEnv = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_GENERALENV"));
        JLabel lblSystem = new JLabel(STRINGLIST.getString("PROPAGATION_R370_SYSTEM"));
        JLabel lblTimePercMin = new JLabel(STRINGLIST.getString("PROPAGATION_R370_TIMEPERCMIN"));
        JLabel lblTimePercMax = new JLabel(STRINGLIST.getString("PROPAGATION_R370_TIMEPERCMAX"));
        JLabel lblLocalClutter = new JLabel(STRINGLIST.getString("PROPAGATION_R370_LOCALCLUTTER"));
        tfTimePercMin.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
        tfTimePercMax.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
        add(xbVariations, "label");
        add(xbMedianLoss, "field");
        add(lblGeneralEnv, "label");
        add(cbGeneralEnv, "field");
        add(lblSystem, "label");
        add(cbSystem, "field");
        add(lblTimePercMin, "label");
        add(tfTimePercMin, "field");
        add(lblTimePercMax, "label");
        add(tfTimePercMax, "field");
        add(useLocalClutter, "label");
        add(localClutter, "field");
        add(new JLabel(""), "label");
        add(informationLabel, "field");
        setBorder(new TitledBorder(STRINGLIST.getString("PROPAGATION_R370_CAPTION")));
        clear();
    }

    public PropagationModel getPropagationable()
    {
        R370Model p = new R370Model(cbGeneralEnv.getSelectedIndex(), cbSystem.getSelectedIndex(), ((Number)tfTimePercMin.getValue()).doubleValue(), ((Number)tfTimePercMax.getValue()).doubleValue());
        p.setMedianSelected(xbMedianLoss.isSelected());
        p.setVariationsSelected(xbVariations.isSelected());
        p.setUserSpecifiedLocalClutterHeight(((Number)localClutter.getValue()).doubleValue());
        p.setUseUserSpecifiedLocalClutterHeight(useLocalClutter.isSelected());
        return p;
    }

    public void setPropagationable(PropagationModel p)
    {
        if(p instanceof R370Model)
            setPropagationable((R370Model)p);
        else
            throw new IllegalArgumentException("Object must be an instance of class <R370Model>");
    }

    public void setPropagationable(R370Model p)
    {
        xbVariations.setSelected(p.getVariationsSelected());
        xbMedianLoss.setSelected(p.getMedianSelected());
        cbGeneralEnv.setSelectedIndex(p.getGeneralEnv());
        cbSystem.setSelectedIndex(p.getSystemType());
        tfTimePercMin.setValue(new Double(p.getTimePercMin()));
        tfTimePercMax.setValue(new Double(p.getTimePercMax()));
        localClutter.setValue(Double.valueOf(p.getUserSpecifiedLocalClutterHeight()));
        useLocalClutter.setSelected(p.useUserSpecifiedLocalClutterHeight());
        localClutter.setEnabled(useLocalClutter.isSelected());
    }

    public void clear()
    {
        setPropagationable(new R370Model());
    }

    protected static final ResourceBundle STRINGLIST;
    private JCheckBox xbVariations;
    private JCheckBox xbMedianLoss;
    private JComboBox cbGeneralEnv;
    private JComboBox cbSystem;
    private JSpinner tfTimePercMin;
    private JSpinner tfTimePercMax;
    private JFormattedTextField localClutter;
    private JCheckBox useLocalClutter;
    private JLabel informationLabel;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }


}
