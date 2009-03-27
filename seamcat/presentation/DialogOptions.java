// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogOptions.java

package org.seamcat.presentation;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.log4j.*;
import org.seamcat.Seamcat;
import org.seamcat.model.Model;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.remote.presentation.ServerSettingsPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, LabelAppender, LabeledPairLayout, SeamcatIcons

public final class DialogOptions extends EscapeDialog
{

    public DialogOptions(JFrame owner)
    {
        super(owner);
        logTestLabel = new JLabel("");
        testAppender = new LabelAppender(logTestLabel);
        logTestLayout = new PatternLayout();
        lblFilename = new JLabel(STRINGLIST.getString("DIALOG_OPTIONS_FILENAME"));
        lblLogLevel = new JLabel(STRINGLIST.getString("DIALOG_OPTIONS_LOGLEVEL"));
        lblFormatString = new JLabel(STRINGLIST.getString("DIALOG_OPTIONS_FORMAT"));
        lblTestLogString = new JLabel(STRINGLIST.getString("DIALOG_OPTIONS_TEST"));
        lblDefaultEmail = new JLabel(STRINGLIST.getString("DIALOG_OPTIONS_EMAIL"));
        loglevel = new JComboBox(logLevels);
        formats = new JComboBox();
        filenamePanel = new JPanel(new BorderLayout());
        filename = new JTextField(25);
        email = new JTextField();
        filenameButton = new JButton(STRINGLIST.getString("BTN_CAPTION_BROWSE"));
        usePostProcessing = new JCheckBox(STRINGLIST.getString("DIALOG_OPTIONS_POSTPROCESSING"));
        useHigherPriorityThread = new JCheckBox(STRINGLIST.getString("DIALOG_OPTIONS_EGEPRIORITY"));
        server = new ServerSettingsPanel();
        logger.removeAllAppenders();
        testAppender.setLayout(logTestLayout);
        logger.addAppender(testAppender);
        fc = new JFileChooser();
        filenamePanel.add(filename, "Center");
        filenamePanel.add(filenameButton, "East");
        filenameButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                browseFileActionPerformed();
            }

            final DialogOptions this$0;

            
            {
                this$0 = DialogOptions.this;
                super();
            }
        }
);
        JTabbedPane tabbed = new JTabbedPane(2, 1);
        clearButton = new JButton(STRINGLIST.getString("DIALOG_OPTIONS_CLEARBTN"));
        clearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                Preferences pref = Preferences.userNodeForPackage(org/seamcat/model/Model);
                pref.remove("Seamcat directory");
                JOptionPane.showMessageDialog(DialogOptions.this, DialogOptions.STRINGLIST.getString("DIALOG_OPTIONS_CLEAR_MESSAGE"), DialogOptions.STRINGLIST.getString("DIALOG_OPTIONS_CLEAR_TITLE"), 1);
            }

            final DialogOptions this$0;

            
            {
                this$0 = DialogOptions.this;
                super();
            }
        }
);
        getContentPane().setLayout(new BorderLayout());
        JPanel options = new JPanel(new LabeledPairLayout());
        options.add("label", lblFilename);
        options.add("field", filenamePanel);
        options.add("label", lblLogLevel);
        options.add("field", loglevel);
        options.add("label", lblFormatString);
        options.add("field", formats);
        options.add("label", lblTestLogString);
        options.add("field", logTestLabel);
        options.add("label", lblDefaultEmail);
        options.add("field", email);
        options.add("label", new JLabel(""));
        options.add("field", clearButton);
        options.add("label", new JLabel(""));
        options.add("field", usePostProcessing);
        options.add("label", new JLabel(""));
        options.add("field", useHigherPriorityThread);
        options.setBorder(new TitledBorder(STRINGLIST.getString("DIALOG_OPTIONS_LOG_TITLE")));
        tabbed.addTab("General", SeamcatIcons.getImageIcon("SEAMCAT_ICON_SETTINGS_GENERAL", 1), options);
        tabbed.addTab("Server Settings", SeamcatIcons.getImageIcon("SEAMCAT_ICON_SETTINGS_SERVER", 1), server);
        getContentPane().add(tabbed, "Center");
        getContentPane().add(new NavigateButtonPanel() {

            public void btnCancelActionPerformed()
            {
                setVisible(false);
            }

            public void btnOkActionPerformed()
            {
                okButtonActionPerformed();
            }

            final DialogOptions this$0;

            
            {
                this$0 = DialogOptions.this;
                super();
            }
        }
, "South");
        setTitle(STRINGLIST.getString("DIALOG_OPTIONS_TITLE"));
        loglevel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                logLevelActionPerformed();
            }

            final DialogOptions this$0;

            
            {
                this$0 = DialogOptions.this;
                super();
            }
        }
);
        formats.setModel(new DefaultComboBoxModel(Model.getInstance().getLogPatterns()));
        formats.setEditable(true);
        formats.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                formatStringActionPerformed();
            }

            final DialogOptions this$0;

            
            {
                this$0 = DialogOptions.this;
                super();
            }
        }
);
        try
        {
            Seamcat.helpBroker.enableHelpKey(getRootPane(), helplist.getString(getClass().getName()), null);
        }
        catch(Exception ex) { }
        setSize(800, 500);
        setLocationRelativeTo(owner);
    }

    public void setVisible(boolean value)
    {
        filename.setText(Model.getInstance().getLogFilename());
        loglevel.setSelectedItem(Logger.getRootLogger().getLevel());
        formats.setSelectedItem(Model.getInstance().getLogFilePattern().getConversionPattern());
        logTestLayout.setConversionPattern(formats.getSelectedItem().toString());
        usePostProcessing.setSelected(Model.usePostProcessingPlugins);
        useHigherPriorityThread.setSelected(!Model.getInstance().isUseHigherPriorityThreads());
        server.setServerSettings(Model.getInstance().getServerSettings());
        email.setText(Model.getInstance().getDefaultEmail());
        super.setVisible(value);
    }

    private void okButtonActionPerformed()
    {
        String pattern = formats.getSelectedItem().toString();
        Model.getInstance().addPattern(pattern);
        server.updateModel(Model.getInstance().getServerSettings());
        Model.usePostProcessingPlugins = usePostProcessing.isSelected();
        Model.getInstance().setUseHigherPriorityThreads(!useHigherPriorityThread.isSelected());
        Model.getInstance().setDefaultEmail(email.getText());
        File file = new File(filename.getText());
        LOG.debug((new StringBuilder()).append("Logfile name is: |").append(file.getAbsolutePath()).append("|").toString());
        if(file.exists() && !file.getAbsolutePath().equals(Model.getInstance().getLogFilename()))
        {
            int choice = JOptionPane.showConfirmDialog(this, STRINGLIST.getString("FILE_EXIST_TEXT"), STRINGLIST.getString("FILE_EXIST_TITLE"), 2, 2);
            if(choice == 2)
                return;
        } else
        if(!filename.getText().equals(""))
        {
            try
            {
                file.createNewFile();
            }
            catch(IOException e)
            {
                LOG.error((new StringBuilder()).append("Could not open file:").append(filename.getText()).toString());
                LOG.error((new StringBuilder()).append("Error is: ").append(e).toString());
            }
            if(!file.canRead() || !file.canWrite())
            {
                JOptionPane.showMessageDialog(this, STRINGLIST.getString("FILE_ERROR_TEXT"), STRINGLIST.getString("FILE_ERROR_TITLE"), 0);
                return;
            }
        }
        Model.getInstance().setLogFilename(file.getAbsolutePath());
        super.setVisible(false);
    }

    private void logLevelActionPerformed()
    {
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getRootLogger().info((new StringBuilder()).append("Setting logging level to: ").append(logLevels[loglevel.getSelectedIndex()]).toString());
        Logger.getRootLogger().setLevel(logLevels[loglevel.getSelectedIndex()]);
    }

    private void formatStringActionPerformed()
    {
        logTestLayout.setConversionPattern(formats.getSelectedItem().toString());
        logger.info(STRINGLIST.getString("DIALOG_OPTIONS_DEMOSTRING"));
    }

    private void browseFileActionPerformed()
    {
        fc.setSelectedFile(new File(Model.getInstance().getLogFilename()));
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == 0)
            filename.setText(fc.getSelectedFile().getAbsolutePath());
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/DialogOptions);
    private static Logger logger = Logger.getLogger("TEST LOGGER USED BY PREVIEW LABEL");
    private static final ResourceBundle helplist;
    private static final Level logLevels[];
    private JFileChooser fc;
    private JLabel logTestLabel;
    private LabelAppender testAppender;
    private PatternLayout logTestLayout;
    private static final ResourceBundle STRINGLIST;
    private JLabel lblFilename;
    private JLabel lblLogLevel;
    private JLabel lblFormatString;
    private JLabel lblTestLogString;
    private JLabel lblDefaultEmail;
    private JComboBox loglevel;
    private JComboBox formats;
    private JPanel filenamePanel;
    private JTextField filename;
    private JTextField email;
    private JButton filenameButton;
    private JButton clearButton;
    private JCheckBox usePostProcessing;
    private JCheckBox useHigherPriorityThread;
    private ServerSettingsPanel server;

    static 
    {
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
        logLevels = (new Level[] {
            Level.DEBUG, Level.INFO, Level.WARN, Level.ERROR, Level.FATAL, Level.OFF
        });
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }





}
