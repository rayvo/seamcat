// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PostProcessingPluginAddEditDialog.java

package org.seamcat.presentation.postprocessing;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.AutoCompleteDocument;
import org.seamcat.Seamcat;
import org.seamcat.model.Model;
import org.seamcat.model.plugin.PostProcessingPlugin;
import org.seamcat.postprocessing.PostProcessingPluginWrapper;
import org.seamcat.presentation.*;
import org.seamcat.presentation.components.NavigateButtonPanel;

// Referenced classes of package org.seamcat.presentation.postprocessing:
//            ClassLoadField

public class PostProcessingPluginAddEditDialog extends EscapeDialog
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
            try
            {
                Model.getInstance().persist();
            }
            catch(ParserConfigurationException ex)
            {
                PostProcessingPluginAddEditDialog.LOG.error("Unable to save model", ex);
            }
            setVisible(false);
        }

        final PostProcessingPluginAddEditDialog this$0;

        private NavigationButtons()
        {
            this$0 = PostProcessingPluginAddEditDialog.this;
            super();
        }

    }


    public PostProcessingPluginAddEditDialog(JDialog owner)
    {
        super(owner, "Post Processing Plugin", true);
        tfName = new JTextField();
        tfDescription = new JTextArea();
        tfClassName = new ClassLoadField() {

            public boolean evaluateClassName()
            {
                PostProcessingPlugin p = (PostProcessingPlugin)Class.forName(tfClassName.getText()).newInstance();
                if(p != null)
                {
                    tfDescription.setText(p.getDescription());
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

            final PostProcessingPluginAddEditDialog this$0;

            
            {
                this$0 = PostProcessingPluginAddEditDialog.this;
                super();
            }
        }
;
        JLabel lbName = new JLabel(STRINGS.getString("LIBRARY_PLUGIN_ADDEDIT_NAME"));
        JLabel lbDescription = new JLabel(STRINGS.getString("LIBRARY_PLUGIN_ADDEDIT_DESC"));
        JLabel lbClassName = new JLabel(STRINGS.getString("LIBRARY_PLUGIN_ADDEDIT_CLASSNAME"));
        tfDescription.setRows(4);
        tfDescription.setLineWrap(true);
        tfDescription.setWrapStyleWord(true);
        tfDescription.setEditable(false);
        tfDescription.setEnabled(false);
        JPanel generalPanel = new JPanel(new LabeledPairLayout());
        generalPanel.add(lbClassName, "label");
        generalPanel.add(tfClassName, "field");
        generalPanel.add(lbName, "label");
        generalPanel.add(tfName, "field");
        generalPanel.add(lbDescription, "label");
        generalPanel.add(new JScrollPane(tfDescription), "field");
        generalPanel.setBorder(new TitledBorder(STRINGS.getString("LIBRARY_PLUGIN_ADDEDIT_GENERAL")));
        JPanel centerPanel = new JPanel(new GridLayout());
        centerPanel.add(generalPanel);
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

    public boolean show(PostProcessingPluginWrapper plugin)
    {
        tfName.setText(plugin.getReference());
        tfDescription.setText(plugin.getDescription());
        tfClassName.setText(plugin.getClassName());
        accept = false;
        tfClassName.grabFocus();
        super.setVisible(true);
        return accept;
    }

    public void updateModel(PostProcessingPluginWrapper plugin)
    {
        plugin.setReference(tfName.getText());
        try
        {
            if(!tfClassName.getText().equals(plugin.getClassName()))
                plugin.setClassName(tfClassName.getText());
        }
        catch(Exception e)
        {
            LOG.warn((new StringBuilder()).append("Could not set post processing plugin classname <").append(e.getMessage()).append(">").toString());
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
