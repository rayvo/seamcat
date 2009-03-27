// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClientSettingsDefinitionDialog.java

package org.seamcat.remote.presentation;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.Seamcat;
import org.seamcat.presentation.*;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.remote.util.ClientSetting;

public class ClientSettingsDefinitionDialog extends EscapeDialog
{

    public ClientSettingsDefinitionDialog(Frame parent)
    {
        super(parent);
        lbl_serverUrl = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_SERVERURL"));
        lbl_serverPort = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_SERVERPORT"));
        lbl_username = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_EMAIL"));
        lbl_password = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_PASSWORD"));
        tf_serverUrl = new JTextField();
        tf_serverPort = new JFormattedTextField();
        tf_username = new JTextField();
        tf_password = new JPasswordField();
        init();
        setLocationRelativeTo(parent);
    }

    public ClientSettingsDefinitionDialog(JDialog parent)
    {
        super(parent);
        lbl_serverUrl = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_SERVERURL"));
        lbl_serverPort = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_SERVERPORT"));
        lbl_username = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_EMAIL"));
        lbl_password = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_PASSWORD"));
        tf_serverUrl = new JTextField();
        tf_serverPort = new JFormattedTextField();
        tf_username = new JTextField();
        tf_password = new JPasswordField();
        init();
        setLocationRelativeTo(parent);
    }

    private void init()
    {
        getContentPane().setLayout(new BorderLayout());
        JPanel main = new JPanel(new LabeledPairLayout());
        tf_serverPort.setColumns(5);
        tf_serverPort.setHorizontalAlignment(4);
        tf_serverPort.setFormatterFactory(DIALOG_FORMATS.getIntegerFactory());
        main.add(lbl_serverUrl, "label");
        main.add(tf_serverUrl, "field");
        main.add(lbl_serverPort, "label");
        main.add(tf_serverPort, "field");
        main.add(lbl_username, "label");
        main.add(tf_username, "field");
        main.add(lbl_password, "label");
        main.add(tf_password, "field");
        navigateButtons = new NavigateButtonPanel() {

            public void btnOkActionPerformed()
            {
                res = true;
                setVisible(false);
            }

            public void btnCancelActionPerformed()
            {
                res = false;
                setVisible(false);
            }

            public void btnHelpActionPerformed()
            {
                Seamcat.helpBroker.enableHelp(getRootPane(), helplist.getString("org.seamcat.remote.presentation.ClientSettingsDefinitionDialog"), null);
            }

            final ClientSettingsDefinitionDialog this$0;

            
            {
                this$0 = ClientSettingsDefinitionDialog.this;
                super();
            }
        }
;
        main.setBorder(new TitledBorder(STRINGLIST.getString("REMOTE_CLIENT_SETTINGS_TITLE")));
        getContentPane().add(main, "Center");
        getContentPane().add(navigateButtons, "South");
        setModal(true);
        setTitle(STRINGLIST.getString("REMOTE_CLIENT_TITLE"));
        setSize(400, 300);
    }

    public boolean showAndGenerateClientSetting()
    {
        setClientSettings(new ClientSetting());
        setVisible(true);
        return res;
    }

    public void setClientSettings(ClientSetting _client)
    {
        tf_serverUrl.setText(_client.getServerUrl());
        tf_serverPort.setValue(new Integer(_client.getPort()));
        tf_username.setText(_client.getEmail());
        tf_password.setText(_client.getPassword());
        client = _client;
        navigateButtons.setBtnOkFocus();
    }

    public ClientSetting updateModel()
    {
        client.setServerUrl(tf_serverUrl.getText());
        client.setPort(((Number)tf_serverPort.getValue()).intValue());
        client.setEmail(tf_username.getText());
        client.setPassword(new String(tf_password.getPassword()));
        return client;
    }

    private static final ResourceBundle STRINGLIST;
    private static final SeamcatTextFieldFormats DIALOG_FORMATS = new SeamcatTextFieldFormats();
    private final JLabel lbl_serverUrl;
    private final JLabel lbl_serverPort;
    private final JLabel lbl_username;
    private final JLabel lbl_password;
    private final JTextField tf_serverUrl;
    private final JFormattedTextField tf_serverPort;
    private final JTextField tf_username;
    private final JPasswordField tf_password;
    private NavigateButtonPanel navigateButtons;
    private ClientSetting client;
    private boolean res;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }

}
