// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PluginModelPanel.java

package org.seamcat.presentation.propagation;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.model.*;
import org.seamcat.model.propagation.PluginModelWrapper;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;
import org.seamcat.propagation.PropagationModel;

public class PluginModelPanel extends JPanel
{

    public PluginModelPanel(Window parent)
    {
        super(new LabeledPairLayout());
        cmbPluginClass = new JComboBox(Model.getInstance().getLibrary().getPlugins().createComboBoxModel());
        cmbGeneralEnv = new JComboBox(PropagationModel.ENVIRONMENT);
        tfParam1 = new FormattedCalculatorField(parent);
        tfParam2 = new FormattedCalculatorField(parent);
        tfParam3 = new FormattedCalculatorField(parent);
        JLabel lblPluginClass = new JLabel(STRINGLIST.getString("PROPAGATION_PLUGIN_CLASSNAME"));
        JLabel lblGeneralEnv = new JLabel(STRINGLIST.getString("PROPAGATION_EXTHATA_GENERALENV"));
        JLabel lblParam1 = new JLabel(STRINGLIST.getString("PROPAGATION_PLUGIN_PARAM1"));
        JLabel lblParam2 = new JLabel(STRINGLIST.getString("PROPAGATION_PLUGIN_PARAM2"));
        JLabel lblParam3 = new JLabel(STRINGLIST.getString("PROPAGATION_PLUGIN_PARAM3"));
        cmbPluginClass.setSelectedItem(null);
        cmbPluginClass.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                if(cmbPluginClass.getSelectedItem() != null)
                    try
                    {
                        PluginModelWrapper p = new PluginModelWrapper((PluginModelWrapper)cmbPluginClass.getSelectedItem());
                        cmbGeneralEnv.setSelectedIndex(p.getGeneralEnv());
                        tfParam1.setValue(Double.valueOf(p.getParam1()));
                        tfParam2.setValue(Double.valueOf(p.getParam2()));
                        tfParam3.setValue(Double.valueOf(p.getParam2()));
                    }
                    catch(Exception e)
                    {
                        JOptionPane.showMessageDialog(PluginModelPanel.this, (new StringBuilder()).append("SEAMCAT was unable to load plugin: ").append(e.getMessage()).toString(), "Unable to load plugin", 0);
                    }
            }

            final PluginModelPanel this$0;

            
            {
                this$0 = PluginModelPanel.this;
                super();
            }
        }
);
        add(lblPluginClass, "label");
        add(cmbPluginClass, "field");
        add(lblGeneralEnv, "label");
        add(cmbGeneralEnv, "field");
        add(lblParam1, "label");
        add(tfParam1, "field");
        add(lblParam2, "label");
        add(tfParam2, "field");
        add(lblParam3, "label");
        add(tfParam3, "field");
        setBorder(new TitledBorder(STRINGLIST.getString("PROPAGATION_PLUGIN_CAPTION")));
    }

    public void clear()
    {
    }

    public PropagationModel getPropagationable()
    {
        try
        {
            PluginModelWrapper pluginModel = (PluginModelWrapper)cmbPluginClass.getSelectedItem();
            pluginModel.setGeneralEnv(cmbGeneralEnv.getSelectedIndex());
            pluginModel.setParam1(((Number)tfParam1.getValue()).doubleValue());
            pluginModel.setParam2(((Number)tfParam2.getValue()).doubleValue());
            pluginModel.setParam3(((Number)tfParam3.getValue()).doubleValue());
            return pluginModel;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, "There was an exception when updating the Plugin Propagationmodel\nMost likely no model is selected.", "Error updating plugin model", 0);
        }
        return null;
    }

    public void setPropagationable(PropagationModel p)
    {
        if(p instanceof PluginModelWrapper)
            setPropagationable((PluginModelWrapper)p);
        else
            throw new IllegalArgumentException("Object must be an instance of class <PluginModelWrapper>");
    }

    public void setPropagationable(PluginModelWrapper p)
    {
        LOG.debug("Setting PluginModelWrapper");
        cmbPluginClass.setSelectedItem(p);
        cmbGeneralEnv.setSelectedIndex(p.getGeneralEnv());
        tfParam1.setValue(Double.valueOf(p.getParam1()));
        tfParam2.setValue(Double.valueOf(p.getParam2()));
        tfParam3.setValue(Double.valueOf(p.getParam3()));
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/propagation/PluginModelPanel);
    protected static final ResourceBundle STRINGLIST;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private JComboBox cmbPluginClass;
    private JComboBox cmbGeneralEnv;
    private JFormattedTextField tfParam1;
    private JFormattedTextField tfParam2;
    private JFormattedTextField tfParam3;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }





}
