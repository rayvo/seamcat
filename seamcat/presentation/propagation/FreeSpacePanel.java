// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FreeSpacePanel.java

package org.seamcat.presentation.propagation;

import java.awt.Window;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.distribution.GaussianDistribution;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;
import org.seamcat.propagation.FreeSpaceModel;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.presentation.propagation:
//            PropagationModelPanel

public class FreeSpacePanel extends JPanel
    implements PropagationModelPanel
{

    public FreeSpacePanel(Window parent)
    {
        super(new LabeledPairLayout());
        JLabel lblVarStdDev = new JLabel(stringlist.getString("PROPAGATION_FREESPACE_VARSTDDEV"));
        tfVarStdDev = new FormattedCalculatorField(parent);
        xbVariations = new JCheckBox(stringlist.getString("PROPAGATION_VARIATIONS"));
        xbMedianLoss = new JCheckBox(stringlist.getString("PROPAGATION_MEDIANLOSS"));
        add(xbVariations, "label");
        add(xbMedianLoss, "field");
        add(lblVarStdDev, "label");
        add(tfVarStdDev, "field");
        setBorder(new TitledBorder(stringlist.getString("PROPAGATION_FREESPACE_CAPTION")));
        clear();
    }

    public PropagationModel getPropagationable()
    {
        FreeSpaceModel p = new FreeSpaceModel(((Number)tfVarStdDev.getValue()).doubleValue());
        p.setMedianSelected(xbMedianLoss.isSelected());
        p.setVariationsSelected(xbVariations.isSelected());
        return p;
    }

    public void setPropagationable(PropagationModel p)
    {
        if(p instanceof FreeSpaceModel)
            setPropagationable((FreeSpaceModel)p);
        else
            throw new IllegalArgumentException("Object must be an instance of class <FreeSpaceModel>");
    }

    public void setPropagationable(FreeSpaceModel p)
    {
        xbVariations.setSelected(p.getVariationsSelected());
        xbMedianLoss.setSelected(p.getMedianSelected());
        tfVarStdDev.setValue(new Double(p.getVariationsDistrib().getStdDev()));
    }

    public void clear()
    {
        setPropagationable(new FreeSpaceModel());
    }

    protected static final ResourceBundle stringlist;
    private JCheckBox xbVariations;
    private JCheckBox xbMedianLoss;
    private JFormattedTextField tfVarStdDev;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
