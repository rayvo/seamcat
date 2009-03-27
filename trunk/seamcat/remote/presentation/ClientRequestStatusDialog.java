// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClientRequestStatusDialog.java

package org.seamcat.remote.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.presentation.*;
import org.seamcat.remote.client.ClientRequestStatusListener;

public class ClientRequestStatusDialog extends EscapeDialog
    implements ClientRequestStatusListener
{

    public ClientRequestStatusDialog(Frame parent)
    {
        super(parent);
        findingServer = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_SERVER"));
        findingServer_status = new JLabel(WAITING);
        loginProcess = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_LOGIN"));
        loginProcess_status = new JLabel(WAITING);
        serverVersion = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_SERVERVERSION"));
        serverVersion_status = new JLabel(WAITING);
        sendingRequest = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_SENDING"));
        sendingRequest_status = new JLabel(WAITING);
        receivingResponse = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_RECEIVING"));
        receivingResponse_status = new JLabel(WAITING);
        progress = new JProgressBar();
        okButton = new JButton(STRINGLIST.getString("BTN_CAPTION_CLOSE"));
        init();
        setLocationRelativeTo(parent);
        progressDialog.setLocationRelativeTo(parent);
    }

    public ClientRequestStatusDialog(JDialog parent)
    {
        super(parent);
        findingServer = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_SERVER"));
        findingServer_status = new JLabel(WAITING);
        loginProcess = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_LOGIN"));
        loginProcess_status = new JLabel(WAITING);
        serverVersion = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_SERVERVERSION"));
        serverVersion_status = new JLabel(WAITING);
        sendingRequest = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_SENDING"));
        sendingRequest_status = new JLabel(WAITING);
        receivingResponse = new JLabel(STRINGLIST.getString("REMOTE_CLIENT_STATUS_RECEIVING"));
        receivingResponse_status = new JLabel(WAITING);
        progress = new JProgressBar();
        okButton = new JButton(STRINGLIST.getString("BTN_CAPTION_CLOSE"));
        init();
        setLocationRelativeTo(parent);
        progressDialog.setLocationRelativeTo(parent);
    }

    private void init()
    {
        getContentPane().setLayout(new BorderLayout());
        JPanel main = new JPanel(new LabeledPairLayout());
        main.add(findingServer, "label");
        main.add(findingServer_status, "field");
        main.add(serverVersion, "label");
        main.add(serverVersion_status, "field");
        main.add(loginProcess, "label");
        main.add(loginProcess_status, "field");
        main.add(sendingRequest, "label");
        main.add(sendingRequest_status, "field");
        main.add(receivingResponse, "label");
        main.add(receivingResponse_status, "field");
        main.setBorder(new TitledBorder("Connection Process status"));
        okButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                setVisible(false);
            }

            final ClientRequestStatusDialog this$0;

            
            {
                this$0 = ClientRequestStatusDialog.this;
                super();
            }
        }
);
        okButton.setEnabled(false);
        JPanel progressPanel = new JPanel();
        progressPanel.add(okButton);
        progress.setStringPainted(true);
        progressDialog = new JDialog(this);
        progressDialog.getContentPane().setLayout(new GridLayout(1, 1));
        progressDialog.getContentPane().add(progress);
        progressDialog.setTitle(STRINGLIST.getString("REMOTE_CLIENT_DOWNLOADING_RESULTS"));
        progressDialog.setSize(500, 70);
        getContentPane().add(main, "Center");
        getContentPane().add(progressPanel, "South");
        setTitle(STRINGLIST.getString("REMOTE_CLIENT_STATUS_TITLE"));
        setModal(true);
        setSize(300, 300);
    }

    public void reset()
    {
        findingServer_status.setIcon(WAITING);
        loginProcess_status.setIcon(WAITING);
        serverVersion_status.setIcon(WAITING);
        sendingRequest_status.setIcon(WAITING);
        receivingResponse_status.setIcon(WAITING);
        okButton.setEnabled(false);
        repaint();
    }

    public void findingServerStatus(boolean status)
    {
        if(status)
        {
            findingServer_status.setIcon(OK);
        } else
        {
            findingServer_status.setIcon(FAILED);
            okButton.setEnabled(true);
        }
        repaint();
    }

    public void loginStatus(boolean status)
    {
        if(status)
        {
            loginProcess_status.setIcon(OK);
        } else
        {
            loginProcess_status.setIcon(FAILED);
            okButton.setEnabled(true);
        }
        repaint();
    }

    public void seamcatVersionStatus(boolean status)
    {
        if(status)
        {
            serverVersion_status.setIcon(OK);
        } else
        {
            serverVersion_status.setIcon(FAILED);
            okButton.setEnabled(true);
        }
        repaint();
    }

    public void sendingRequestStatus(boolean status)
    {
        if(status)
        {
            sendingRequest_status.setIcon(OK);
        } else
        {
            sendingRequest_status.setIcon(FAILED);
            okButton.setEnabled(true);
        }
        repaint();
    }

    public void receiveingResponseStatus(boolean status)
    {
        if(status)
        {
            receivingResponse_status.setIcon(OK);
        } else
        {
            receivingResponse_status.setIcon(FAILED);
            okButton.setEnabled(true);
        }
        okButton.setEnabled(true);
        repaint();
    }

    public void fetchingResults(int size)
    {
        progress.setMaximum(size);
        progress.setValue(0);
        progressDialog.setVisible(true);
    }

    public void downloadStatus(int totalRead)
    {
        progress.setValue(totalRead);
        progressDialog.repaint();
    }

    public void resultsDownloaded()
    {
        progressDialog.setVisible(false);
    }

    private static final ResourceBundle STRINGLIST;
    private static final ImageIcon WAITING = SeamcatIcons.getImageIcon("SEAMCAT_ICON_REMOTE_WAITING", 1);
    private static final ImageIcon OK = SeamcatIcons.getImageIcon("SEAMCAT_ICON_REMOTE_OK", 1);
    private static final ImageIcon FAILED = SeamcatIcons.getImageIcon("SEAMCAT_ICON_REMOTE_FAILED", 1);
    protected JLabel findingServer;
    protected JLabel findingServer_status;
    protected JLabel loginProcess;
    protected JLabel loginProcess_status;
    protected JLabel serverVersion;
    protected JLabel serverVersion_status;
    protected JLabel sendingRequest;
    protected JLabel sendingRequest_status;
    protected JLabel receivingResponse;
    protected JLabel receivingResponse_status;
    protected JProgressBar progress;
    protected JButton okButton;
    protected JDialog progressDialog;
    private int downloadSize;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
