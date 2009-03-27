// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PluginAddEditComponent.java

package org.seamcat.presentation;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.AutoCompleteDocument;
import org.seamcat.Seamcat;
import org.seamcat.model.propagation.PluginModel;
import org.seamcat.model.propagation.PluginModelWrapper;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.postprocessing.ClassLoadField;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, LabeledPairLayout, PluginClassAdaptor

public class PluginAddEditComponent extends EscapeDialog
{
    private class NavigationButtons extends NavigateButtonPanel
    {

        public void btnCancelActionPerformed()
        {
            setVisible(false);
        }

        public void btnOkActionPerformed()
        {
            accept = true;
            setVisible(false);
        }

        final PluginAddEditComponent this$0;

        private NavigationButtons()
        {
            this$0 = PluginAddEditComponent.this;
            super();
        }

    }


    public PluginAddEditComponent(JDialog owner)
    {
        super(owner, "Plugin propagation model", true);
        tfName = new JTextField();
        tfDescription = new JTextArea();
        tfClassName = new ClassLoadField() {

            public boolean evaluateClassName()
            {
                PluginModel p = (PluginModel)Class.forName(tfClassName.getText()).newInstance();
                if(p != null)
                {
                    tfDescription.setText((new StringBuilder()).append("Plugin ").append(p.getClass().getName()).append(" was loaded").toString());
                    return true;
                }
                try
                {
                    tfDescription.setText("Plugin not loaded");
                    return false;
                }
                catch(Error e)
                {
                    tfDescription.setText("Plugin not loaded");
                    return false;
                }
                catch(Exception e)
                {
                    tfDescription.setText("Plugin not loaded");
                }
                return false;
            }

            final PluginAddEditComponent this$0;

            
            {
                this$0 = PluginAddEditComponent.this;
                super();
            }
        }
;
        JLabel lbName = new JLabel(STRINGS.getString("LIBRARY_PLUGIN_ADDEDIT_NAME"));
        JLabel lbDescription = new JLabel(STRINGS.getString("LIBRARY_PLUGIN_ADDEDIT_DESC"));
        JLabel lbClassName = new JLabel(STRINGS.getString("LIBRARY_PLUGIN_ADDEDIT_CLASSNAME"));
        tfDescription.setRows(4);
        JPanel generalPanel = new JPanel(new LabeledPairLayout());
        generalPanel.add(lbClassName, "label");
        generalPanel.add(tfClassName, "field");
        generalPanel.add(lbName, "label");
        generalPanel.add(tfName, "field");
        generalPanel.add(lbDescription, "label");
        generalPanel.add(new JScrollPane(tfDescription), "field");
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.add(STRINGS.getString("LIBRARY_PLUGIN_ADDEDIT_GENERAL"), generalPanel);
        JPanel centerPanel = new JPanel(new GridLayout());
        centerPanel.add(tabPane);
        centerPanel.setPreferredSize(new Dimension(400, 300));
        getContentPane().add(centerPanel, "Center");
        getContentPane().add(new NavigationButtons(), "South");
        pack();
        setLocationRelativeTo(owner);
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
        PluginClassAdaptor adaptor = new PluginClassAdaptor(tfClassName);
        AutoCompleteDecorator.decorate(tfClassName, new AutoCompleteDocument(adaptor, true), adaptor);
    }

    public void setVisible()
    {
        throw new UnsupportedOperationException("Use show(PluginModelWrapper)");
    }

    public boolean show(PluginModelWrapper plugin)
    {
        tfName.setText(plugin.getReference());
        tfDescription.setText(plugin.getDescription());
        tfClassName.setText(plugin.getClassName());
        accept = false;
        super.setVisible(true);
        return accept;
    }

    public void updateModel(PluginModelWrapper plugin)
    {
        plugin.setReference(tfName.getText());
        plugin.setDescription(tfDescription.getText());
        try
        {
            if(!tfClassName.getText().equals(plugin.getClassName()))
                plugin.setClassName(tfClassName.getText());
        }
        catch(Exception e)
        {
            LOG.warn((new StringBuilder()).append("Could not set propagation model plugin classname <").append(e.getMessage()).append(">").toString());
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/PluginAddEditComponent);
    private static final ResourceBundle STRINGS;
    private static final ResourceBundle helplist;
    private JTextField tfName;
    private JTextArea tfDescription;
    private JTextField tfClassName;
    private boolean accept;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }



}
