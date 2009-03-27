// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ServerSettingsPanel.java

package org.seamcat.remote.presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;
import org.seamcat.presentation.components.FileInputPanel;
import org.seamcat.remote.util.ServerSetting;

public class ServerSettingsPanel extends JPanel
{

    public ServerSettingsPanel()
    {
        super(new LabeledPairLayout());
        lbl_port = new JLabel(STRINGLIST.getString("REMOTE_SERVER_PORT"));
        lbl_tempFolder = new JLabel(STRINGLIST.getString("REMOTE_SERVER_TEMPFOLDER"));
        lbl_StagingFolder = new JLabel(STRINGLIST.getString("REMOTE_SERVER_STAGINGFOLDER"));
        lbl_ResultFolder = new JLabel(STRINGLIST.getString("REMOTE_SERVER_RESULTFOLDER"));
        lbl_ErrorFolder = new JLabel(STRINGLIST.getString("REMOTE_SERVER_ERRORFOLDER"));
        lbl_ProcFolder = new JLabel(STRINGLIST.getString("REMOTE_SERVER_PROCESSINGFOLDER"));
        lbl_EmailSubj = new JLabel(STRINGLIST.getString("REMOTE_SERVER_EMAIL_SUBJ"));
        lbl_smtpServer = new JLabel(STRINGLIST.getString("REMOTE_SERVER_SMTP"));
        lbl_smtpUser = new JLabel(STRINGLIST.getString("REMOTE_SERVER_SMTP_USER"));
        lbl_smtpPass = new JLabel(STRINGLIST.getString("REMOTE_SERVER_SMTP_PASSWORD"));
        lbl_fromAddress = new JLabel(STRINGLIST.getString("REMOTE_SERVER_FROM_ADDRESS"));
        lbl_body = new JLabel(STRINGLIST.getString("REMOTE_SERVER_EMAIL_BODY"));
        ftf_port = new JFormattedTextField();
        tf_tempFolder = new FileInputPanel("Select");
        tf_stagingFolder = new FileInputPanel("Select");
        tf_resultFolder = new FileInputPanel("Select");
        tf_errorFolder = new FileInputPanel("Select");
        tf_procFolder = new FileInputPanel("Select");
        tf_emailSubj = new JTextField();
        tf_smtpServer = new JTextField();
        cb_useSmtpAuth = new JCheckBox(STRINGLIST.getString("REMOTE_SERVER_SMTP_USEAUTHENTICATION"));
        tf_smtpUser = new JTextField();
        tf_smtpPassword = new JPasswordField();
        tf_fromadd = new JTextField();
        ta_emailBody = new JTextArea(3, 50);
        ftf_port.setColumns(5);
        ftf_port.setHorizontalAlignment(4);
        ftf_port.setFormatterFactory(DIALOG_FORMATS.getIntegerFactory());
        tf_tempFolder.setDirectoriesOnly();
        cb_useSmtpAuth.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                lbl_smtpPass.setEnabled(cb_useSmtpAuth.isSelected());
                tf_smtpPassword.setEnabled(cb_useSmtpAuth.isSelected());
                lbl_smtpUser.setEnabled(cb_useSmtpAuth.isSelected());
                tf_smtpUser.setEnabled(cb_useSmtpAuth.isSelected());
            }

            final ServerSettingsPanel this$0;

            
            {
                this$0 = ServerSettingsPanel.this;
                super();
            }
        }
);
        add(lbl_port, "label");
        add(ftf_port, "field");
        add(lbl_tempFolder, "label");
        add(tf_tempFolder, "field");
        add(lbl_StagingFolder, "label");
        add(tf_stagingFolder, "field");
        add(lbl_ResultFolder, "label");
        add(tf_resultFolder, "field");
        add(lbl_ErrorFolder, "label");
        add(tf_errorFolder, "field");
        add(lbl_ProcFolder, "label");
        add(tf_procFolder, "field");
        add(lbl_smtpServer, "label");
        add(tf_smtpServer, "field");
        add(new JLabel(""), "label");
        add(cb_useSmtpAuth, "field");
        add(lbl_smtpUser, "label");
        add(tf_smtpUser, "field");
        add(lbl_smtpPass, "label");
        add(tf_smtpPassword, "field");
        add(lbl_fromAddress, "label");
        add(tf_fromadd, "field");
        add(lbl_EmailSubj, "label");
        add(tf_emailSubj, "field");
        add(lbl_body, "label");
        add(new JScrollPane(ta_emailBody), "field");
        setBorder(new TitledBorder(STRINGLIST.getString("REMOTE_SETTINGS_TITLE")));
    }

    public void setServerSettings(ServerSetting settings)
    {
        ftf_port.setValue(new Integer(settings.getPort()));
        tf_tempFolder.setText(settings.getTempFolder());
        tf_stagingFolder.setText(settings.getStagingFolder());
        tf_resultFolder.setText(settings.getResultFolder());
        tf_errorFolder.setText(settings.getErrorFolder());
        tf_procFolder.setText(settings.getProccessingFolder());
        tf_smtpServer.setText(settings.getSmtpServer());
        cb_useSmtpAuth.setSelected(settings.getSmtpRequireAuthentication());
        tf_smtpUser.setText(settings.getEmailUser());
        tf_smtpPassword.setText(settings.getEmailUserPw());
        tf_emailSubj.setText(settings.getEmailSubject());
        tf_fromadd.setText(settings.getFromAddress());
        ta_emailBody.setText(settings.getEmailBody());
        lbl_smtpPass.setEnabled(cb_useSmtpAuth.isSelected());
        tf_smtpPassword.setEnabled(cb_useSmtpAuth.isSelected());
        lbl_smtpUser.setEnabled(cb_useSmtpAuth.isSelected());
        tf_smtpUser.setEnabled(cb_useSmtpAuth.isSelected());
    }

    public void updateModel(ServerSetting settings)
    {
        settings.setPort(((Number)ftf_port.getValue()).intValue());
        settings.setTempFolder(tf_tempFolder.getText());
        settings.setStagingFolder(tf_stagingFolder.getText());
        settings.setResultFolder(tf_resultFolder.getText());
        settings.setErrorFolder(tf_errorFolder.getText());
        settings.setProccessingFolder(tf_procFolder.getText());
        settings.setSmtpServer(tf_smtpServer.getText());
        settings.setSmtpRequireAuthentication(cb_useSmtpAuth.isSelected());
        settings.setEmailUser(tf_smtpUser.getText());
        settings.setEmailUserPw(new String(tf_smtpPassword.getPassword()));
        settings.setEmailSubject(tf_emailSubj.getText());
        settings.setFromAddress(tf_fromadd.getText());
        settings.setEmailBody(ta_emailBody.getText());
    }

    private static final ResourceBundle STRINGLIST;
    private static final SeamcatTextFieldFormats DIALOG_FORMATS = new SeamcatTextFieldFormats();
    private JLabel lbl_port;
    private JLabel lbl_tempFolder;
    private JLabel lbl_StagingFolder;
    private JLabel lbl_ResultFolder;
    private JLabel lbl_ErrorFolder;
    private JLabel lbl_ProcFolder;
    private JLabel lbl_EmailSubj;
    private JLabel lbl_smtpServer;
    private JLabel lbl_smtpUser;
    private JLabel lbl_smtpPass;
    private JLabel lbl_fromAddress;
    private JLabel lbl_body;
    private JFormattedTextField ftf_port;
    private FileInputPanel tf_tempFolder;
    private FileInputPanel tf_stagingFolder;
    private FileInputPanel tf_resultFolder;
    private FileInputPanel tf_errorFolder;
    private FileInputPanel tf_procFolder;
    private JTextField tf_emailSubj;
    private JTextField tf_smtpServer;
    private JCheckBox cb_useSmtpAuth;
    private JTextField tf_smtpUser;
    private JPasswordField tf_smtpPassword;
    private JTextField tf_fromadd;
    private JTextArea ta_emailBody;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }





}
