// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogReportOptions.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.Seamcat;
import org.seamcat.interfaces.Reportable;
import org.seamcat.model.Model;
import org.seamcat.presentation.batch.ReportViewerDialog;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, MainWindow

public class DialogReportOptions extends EscapeDialog
{

    public DialogReportOptions(JDialog owner)
    {
        super(owner);
        buttonGroupOutputOptions = new ButtonGroup();
        checkBoxScenarios = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_SCENARIOS"), true);
        checkBoxSimulationControl = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_CONTROLS"), true);
        checkBoxResults = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_RESULTS"), true);
        checkBoxICEResults = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_ICE_RESULTS"), true);
        checkBoxExpandTabularData = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_SIGNALS"), false);
        radioButtonHTMLFile = new JRadioButton("HTML File", true);
        radioButtonXMLFile = new JRadioButton("XML File", false);
        radioButtonUserFile = new JRadioButton("User specified", false);
        radioButtonCSVFile = new JRadioButton("CSV File (EXCEL)", false);
        buttonSaveReport = new JButton(STRINGLIST.getString("REPORT_OPTION_GENERATE_REPORT"));
        buttonClose = new JButton(STRINGLIST.getString("BTN_CAPTION_CLOSE"));
        buttonHelp = new JButton(STRINGLIST.getString("BTN_CAPTION_HELP"));
        userDefinedFile = new JTextField(" ");
        style = Model.getReportHTMLStyleUrl();
        contentType = "text/html";
        init();
        setLocationRelativeTo(owner);
    }

    public DialogReportOptions(JFrame owner)
    {
        super(owner);
        buttonGroupOutputOptions = new ButtonGroup();
        checkBoxScenarios = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_SCENARIOS"), true);
        checkBoxSimulationControl = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_CONTROLS"), true);
        checkBoxResults = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_RESULTS"), true);
        checkBoxICEResults = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_ICE_RESULTS"), true);
        checkBoxExpandTabularData = new JCheckBox(STRINGLIST.getString("REPORT_OPTION_SIGNALS"), false);
        radioButtonHTMLFile = new JRadioButton("HTML File", true);
        radioButtonXMLFile = new JRadioButton("XML File", false);
        radioButtonUserFile = new JRadioButton("User specified", false);
        radioButtonCSVFile = new JRadioButton("CSV File (EXCEL)", false);
        buttonSaveReport = new JButton(STRINGLIST.getString("REPORT_OPTION_GENERATE_REPORT"));
        buttonClose = new JButton(STRINGLIST.getString("BTN_CAPTION_CLOSE"));
        buttonHelp = new JButton(STRINGLIST.getString("BTN_CAPTION_HELP"));
        userDefinedFile = new JTextField(" ");
        style = Model.getReportHTMLStyleUrl();
        contentType = "text/html";
        init();
        setLocationRelativeTo(owner);
    }

    private void init()
    {
        setTitle(STRINGLIST.getString("REPORT_OPTION_OPTIONS_TITLE"));
        setResizable(true);
        setModal(true);
        reportWindow = new ReportViewerDialog(this, STRINGLIST.getString("REPORT_OPTION_REPORTVIEWER_TITLE"));
        buttonGroupOutputOptions.add(radioButtonHTMLFile);
        buttonGroupOutputOptions.add(radioButtonXMLFile);
        buttonGroupOutputOptions.add(radioButtonCSVFile);
        buttonGroupOutputOptions.add(radioButtonUserFile);
        userDefinedFile.setEnabled(false);
        userDefinedFile.setEditable(false);
        JPanel optionsPanel = new JPanel();
        JPanel contentsOptionsPanel = new JPanel();
        JPanel outputOptionsPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        getContentPane().setLayout(new BorderLayout());
        optionsPanel.setLayout(new GridLayout());
        optionsPanel.setFocusable(false);
        contentsOptionsPanel.setLayout(new BoxLayout(contentsOptionsPanel, 1));
        contentsOptionsPanel.setFocusable(false);
        outputOptionsPanel.setLayout(new BoxLayout(outputOptionsPanel, 1));
        outputOptionsPanel.setFocusable(false);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setFocusable(false);
        contentsOptionsPanel.setBorder(new TitledBorder(STRINGLIST.getString("REPORT_OPTION_CONTENT")));
        outputOptionsPanel.setBorder(new TitledBorder(STRINGLIST.getString("REPORT_OPTION_FORMATS")));
        contentsOptionsPanel.add(checkBoxScenarios);
        contentsOptionsPanel.add(checkBoxSimulationControl);
        contentsOptionsPanel.add(checkBoxResults);
        contentsOptionsPanel.add(checkBoxICEResults);
        contentsOptionsPanel.add(new JSeparator());
        contentsOptionsPanel.add(checkBoxExpandTabularData);
        outputOptionsPanel.add(radioButtonHTMLFile);
        outputOptionsPanel.add(radioButtonXMLFile);
        outputOptionsPanel.add(radioButtonCSVFile);
        outputOptionsPanel.add(radioButtonUserFile);
        outputOptionsPanel.add(userDefinedFile);
        optionsPanel.add(contentsOptionsPanel);
        optionsPanel.add(outputOptionsPanel);
        radioButtonHTMLFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                contentType = "text/html";
                style = reportSource.getStyleSheet(contentType);
                userDefinedFile.setEnabled(false);
            }

            final DialogReportOptions this$0;

            
            {
                this$0 = DialogReportOptions.this;
                super();
            }
        }
);
        radioButtonCSVFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                contentType = "application/x-csv";
                style = reportSource.getStyleSheet(contentType);
                userDefinedFile.setEnabled(false);
            }

            final DialogReportOptions this$0;

            
            {
                this$0 = DialogReportOptions.this;
                super();
            }
        }
);
        radioButtonXMLFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                contentType = "text/xml";
                style = reportSource.getStyleSheet(contentType);
                userDefinedFile.setEnabled(false);
            }

            final DialogReportOptions this$0;

            
            {
                this$0 = DialogReportOptions.this;
                super();
            }
        }
);
        radioButtonUserFile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                userDefinedFile.setEnabled(false);
                MainWindow.FILECHOOSER.setFileFilter(MainWindow.FILE_FILTER_XSL);
                if(0 == MainWindow.FILECHOOSER.showOpenDialog(DialogReportOptions.this))
                {
                    style = MainWindow.FILECHOOSER.getSelectedFile().getAbsolutePath();
                    userDefinedFile.setText(style);
                    contentType = "text/html";
                } else
                {
                    radioButtonHTMLFile.setSelected(true);
                }
            }

            final DialogReportOptions this$0;

            
            {
                this$0 = DialogReportOptions.this;
                super();
            }
        }
);
        buttonClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }

            final DialogReportOptions this$0;

            
            {
                this$0 = DialogReportOptions.this;
                super();
            }
        }
);
        buttonSaveReport.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                setCursor(Cursor.getPredefinedCursor(3));
                reportSource.setExpandSignals(checkBoxExpandTabularData.isSelected());
                reportSource.setStoreScenario(checkBoxScenarios.isSelected());
                reportSource.setStoreControl(checkBoxSimulationControl.isSelected());
                reportSource.setStoreResults(checkBoxResults.isSelected());
                reportSource.setStoreSignals(checkBoxResults.isSelected());
                reportWindow.showContent(reportSource.getReport(style), contentType);
                setCursor(Cursor.getPredefinedCursor(0));
            }

            final DialogReportOptions this$0;

            
            {
                this$0 = DialogReportOptions.this;
                super();
            }
        }
);
        buttonHelp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                Seamcat.helpBroker.enableHelp(getRootPane(), DialogReportOptions.helplist.getString(getClass().getName()), null);
            }

            final DialogReportOptions this$0;

            
            {
                this$0 = DialogReportOptions.this;
                super();
            }
        }
);
        buttonSaveReport.setMnemonic(83);
        buttonClose.setMnemonic(67);
        buttonHelp.setMnemonic(72);
        getRootPane().setDefaultButton(buttonSaveReport);
        buttonPanel.add(buttonSaveReport);
        buttonPanel.add(buttonClose);
        buttonPanel.add(buttonHelp);
        getContentPane().add(optionsPanel, "Center");
        getContentPane().add(buttonPanel, "South");
        checkBoxExpandTabularData.setSelected(false);
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
        Seamcat.helpBroker.enableHelpOnButton(buttonHelp, helplist.getString(getClass().getName()), null);
        pack();
    }

    public void show(Reportable _rep)
    {
        reportSource = _rep;
        style = reportSource.getStyleSheet(contentType);
        super.setVisible(true);
        getRootPane().setDefaultButton(buttonSaveReport);
    }

    private static final ResourceBundle STRINGLIST;
    private static final ResourceBundle helplist;
    private ReportViewerDialog reportWindow;
    private ButtonGroup buttonGroupOutputOptions;
    private JCheckBox checkBoxScenarios;
    private JCheckBox checkBoxSimulationControl;
    private JCheckBox checkBoxResults;
    private JCheckBox checkBoxICEResults;
    private JCheckBox checkBoxExpandTabularData;
    private JRadioButton radioButtonHTMLFile;
    private JRadioButton radioButtonXMLFile;
    private JRadioButton radioButtonUserFile;
    private JRadioButton radioButtonCSVFile;
    private JButton buttonSaveReport;
    private JButton buttonClose;
    private JButton buttonHelp;
    private JTextField userDefinedFile;
    private Reportable reportSource;
    private String style;
    private String contentType;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }













}
