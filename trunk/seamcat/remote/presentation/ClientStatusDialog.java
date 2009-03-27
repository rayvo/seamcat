// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClientStatusDialog.java

package org.seamcat.remote.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.seamcat.Seamcat;
import org.seamcat.batch.BatchJobList;
import org.seamcat.model.*;
import org.seamcat.presentation.*;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.remote.client.ClientRequest;
import org.seamcat.remote.client.SeamcatClientSocket;
import org.seamcat.remote.server.ServerResponse;
import org.seamcat.remote.util.ClientSetting;
import org.seamcat.remote.util.Status;

// Referenced classes of package org.seamcat.remote.presentation:
//            ClientRequestStatusDialog

public class ClientStatusDialog extends EscapeDialog
{

    public ClientStatusDialog(Frame _parent)
    {
        super(_parent, false);
        lbl_server = new JLabel(STRINGLIST.getString("REMOTE_STATUS_SERVER"));
        server = new JLabel();
        lbl_sendto = new JLabel(STRINGLIST.getString("REMOTE_STATUS_SENDTO"));
        sendto = new JLabel();
        lbl_laststatus = new JLabel(STRINGLIST.getString("REMOTE_STATUS_LASTSTATUS"));
        laststatus = new JLabel();
        lbl_status = new JLabel(STRINGLIST.getString("REMOTE_STATUS_STATUS"));
        status = new JLabel();
        lbl_jobid = new JLabel(STRINGLIST.getString("REMOTE_STATUS_JOBID"));
        jobid = new JLabel();
        deleteButton = new JButton(STRINGLIST.getString("REMOTE_STATUS_DELETE"));
        parent = _parent;
        statusDialog = new ClientRequestStatusDialog(parent);
        JPanel top = new JPanel(new GridLayout(1, 1));
        combo_clientSettings = new JComboBox();
        combo_clientSettings.setEditable(false);
        combo_clientSettings.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                setSelectedClient((ClientSetting)combo_clientSettings.getSelectedItem());
            }

            final ClientStatusDialog this$0;

            
            {
                this$0 = ClientStatusDialog.this;
                super();
            }
        }
);
        deleteButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                int resp = JOptionPane.showConfirmDialog(ClientStatusDialog.this, ClientStatusDialog.STRINGLIST.getString("REMOTE_STATUS_CONFIRM_DELETE"), ClientStatusDialog.STRINGLIST.getString("REMOTE_STATUS_CONFIRM_DELETE_TITLE"), 0);
                if(resp == 0)
                {
                    ClientSetting clientSettings = selectedClient;
                    statusDialog.reset();
                    SeamcatClientSocket cli = new SeamcatClientSocket(clientSettings, statusDialog);
                    ClientRequest req = new ClientRequest(clientSettings, "deljob");
                    cli.sendRequest(req);
                    setCursor(Cursor.getPredefinedCursor(3));
                    statusDialog.setVisible(true);
                    ServerResponse res = cli.waitForResponse();
                    if(res != null)
                        Model.getInstance().getClientSettings().remove(selectedClient);
                    setVisible(false);
                    Model.getInstance().getClientSettings().remove(selectedClient);
                    showDialog(Model.getInstance().getClientSettings());
                }
            }

            final ClientStatusDialog this$0;

            
            {
                this$0 = ClientStatusDialog.this;
                super();
            }
        }
);
        top.add(combo_clientSettings);
        top.setBorder(new TitledBorder("Remote Job"));
        JPanel main = new JPanel(new LabeledPairLayout());
        main.add(lbl_jobid, "label");
        main.add(jobid, "field");
        main.add(lbl_server, "label");
        main.add(server, "field");
        main.add(lbl_sendto, "label");
        main.add(sendto, "field");
        main.add(lbl_laststatus, "label");
        main.add(laststatus, "field");
        main.add(lbl_status, "label");
        main.add(status, "field");
        main.add(deleteButton, "label");
        main.add(new JLabel(""), "field");
        main.setBorder(new TitledBorder("Details"));
        navigateButtons = new NavigateButtonPanel() {

            public void btnOkActionPerformed()
            {
                ClientSetting clientSettings = selectedClient;
                statusDialog.reset();
                if(clientSettings.getStatus() == 3)
                {
                    SeamcatClientSocket cli = new SeamcatClientSocket(clientSettings, statusDialog);
                    ClientRequest req = new ClientRequest(clientSettings, "result");
                    cli.sendRequest(req);
                    setCursor(Cursor.getPredefinedCursor(3));
                    statusDialog.setVisible(true);
                    ServerResponse res = cli.waitForResponse();
                    if(res != null)
                        Model.getInstance().getClientSettings().remove(selectedClient);
                    setVisible(false);
                    if(res.isBatchResponse())
                    {
                        BatchJobList batch = res.getBatch();
                        Library lib = Model.getInstance().getLibrary();
                        lib.getBatchjoblists().remove(batch.getReference());
                        lib.getBatchjoblists().add(batch);
                        try
                        {
                            Model.getInstance().persist();
                        }
                        catch(ParserConfigurationException ex)
                        {
                            ClientStatusDialog.LOG.error("An Error occured", ex);
                        }
                        setCursor(Cursor.getPredefinedCursor(0));
                        JOptionPane.showMessageDialog(parent, ClientStatusDialog.STRINGLIST.getString("REMOTE_STATUS_DOWNLOAD_COMPLETE_BATCH"), ClientStatusDialog.STRINGLIST.getString("REMOTE_STATUS_DOWNLOAD_COMPLETE_TITLE"), 1);
                    } else
                    {
                        File temp = new File((new StringBuilder()).append(Model.seamcatTemp).append(File.separator).toString(), (new StringBuilder()).append(clientSettings.getJobid()).append("_temp.sws").toString());
                        temp.renameTo(new File((new StringBuilder()).append(Model.seamcatTemp).append(File.separator).append(res.getWorkspace().getReference()).append("_jobid_").append(clientSettings.getJobid()).append(".sws").toString()));
                        setCursor(Cursor.getPredefinedCursor(0));
                        res.getWorkspace().setReference((new StringBuilder()).append(res.getWorkspace().getReference()).append("_jobid_").append(clientSettings.getJobid()).toString());
                        int resp = JOptionPane.showConfirmDialog(parent, (new StringBuilder()).append(ClientStatusDialog.STRINGLIST.getString("REMOTE_STATUS_DOWNLOAD_COMPLETE_PREFIX")).append(Model.seamcatTemp).append(File.separator).append(res.getWorkspace().getReference()).append(ClientStatusDialog.STRINGLIST.getString("REMOTE_STATUS_DOWNLOAD_COMPLETE_POSTFIX")).toString(), ClientStatusDialog.STRINGLIST.getString("REMOTE_STATUS_DOWNLOAD_COMPLETE_TITLE"), 0);
                        if(resp == 0)
                            try
                            {
                                res.getWorkspace().setIsSaved(false);
                                Model.getInstance().getWorkspaces().add(res.getWorkspace());
                                MainWindow.getInstance().setCursor(Cursor.getDefaultCursor());
                            }
                            catch(Exception ex)
                            {
                                ClientStatusDialog.LOG.error("An Error occured", ex);
                            }
                    }
                } else
                {
                    SeamcatClientSocket cli = new SeamcatClientSocket(clientSettings, statusDialog);
                    ClientRequest req = new ClientRequest(clientSettings, "status");
                    cli.sendRequest(req);
                    setCursor(Cursor.getPredefinedCursor(3));
                    statusDialog.setVisible(true);
                    ServerResponse res = cli.waitForResponse();
                    if(res != null && res.getStatus() == 2)
                        clientSettings.setCurrentEvent(res.getCurrentEvent());
                    setSelectedClient(clientSettings);
                }
                setCursor(Cursor.getPredefinedCursor(0));
                break MISSING_BLOCK_LABEL_647;
                Exception exception;
                exception;
                setCursor(Cursor.getPredefinedCursor(0));
                throw exception;
            }

            public void btnCancelActionPerformed()
            {
                setVisible(false);
            }

            public void btnHelpActionPerformed()
            {
                Seamcat.helpBroker.enableHelp(getRootPane(), helplist.getString("org.seamcat.remote.presentation.ClientStatusDialog"), null);
            }

            final ClientStatusDialog this$0;

            
            {
                this$0 = ClientStatusDialog.this;
                super();
            }
        }
;
        navigateButtons.setBtnOkText("Update Status");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(top, "North");
        getContentPane().add(main, "Center");
        getContentPane().add(navigateButtons, "South");
        setTitle(STRINGLIST.getString("REMOTE_STATUS_TITLE"));
        setModal(true);
        setSize(500, 300);
        setLocationRelativeTo(parent);
    }

    public void showDialog(Vector clients)
    {
        combo_clientSettings.setModel(new DefaultComboBoxModel(clients));
        if(combo_clientSettings.getItemCount() < 1)
        {
            JOptionPane.showMessageDialog(this, STRINGLIST.getString("REMOTE_STATUS_NO_JOBS"));
            super.setVisible(false);
        } else
        {
            setSelectedClient((ClientSetting)combo_clientSettings.getSelectedItem());
            super.setVisible(true);
        }
    }

    public ClientSetting getSelectedClient()
    {
        return selectedClient;
    }

    public void setSelectedClient(ClientSetting _selectedClient)
    {
        selectedClient = _selectedClient;
        jobid.setText((new StringBuilder()).append("<html><b>").append(selectedClient.getJobid()).append("</b></html>").toString());
        server.setText((new StringBuilder()).append("<html><b>").append(selectedClient.getServerUrl()).append(":").append(selectedClient.getPort()).append("</b></html>").toString());
        status.setText((new StringBuilder()).append("<html><b>").append(Status.getStatusText(selectedClient.getStatus(), selectedClient.getCurrentEvent())).append("</b></html>").toString());
        sendto.setText((new StringBuilder()).append("<html><b>").append(new Timestamp(selectedClient.getSendToServer())).append("</b></html>").toString());
        laststatus.setText((new StringBuilder()).append("<html><b>").append(new Timestamp(selectedClient.getLastStatusCheck())).append("</b></html>").toString());
        if(selectedClient.getStatus() == 3)
            navigateButtons.setBtnOkText("Download Results");
        else
            navigateButtons.setBtnOkText("Update Status");
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/remote/presentation/ClientStatusDialog);
    private static final ResourceBundle helplist;
    private static final ResourceBundle STRINGLIST;
    private JComboBox combo_clientSettings;
    private JLabel lbl_server;
    private JLabel server;
    private JLabel lbl_sendto;
    private JLabel sendto;
    private JLabel lbl_laststatus;
    private JLabel laststatus;
    private JLabel lbl_status;
    private JLabel status;
    private JLabel lbl_jobid;
    private JLabel jobid;
    private JButton deleteButton;
    private NavigateButtonPanel navigateButtons;
    private ClientSetting selectedClient;
    private ClientRequestStatusDialog statusDialog;
    private Frame parent;
    private static final String VALUE_START = "<html><b>";
    private static final String VALUE_END = "</b></html>";

    static 
    {
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }






}
