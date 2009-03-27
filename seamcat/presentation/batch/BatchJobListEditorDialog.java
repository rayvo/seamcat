// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchJobListEditorDialog.java

package org.seamcat.presentation.batch;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.seamcat.Seamcat;
import org.seamcat.batch.BatchItemControl;
import org.seamcat.batch.BatchJob;
import org.seamcat.batch.BatchJobList;
import org.seamcat.model.Model;
import org.seamcat.model.Workspace;
import org.seamcat.presentation.DialogReportOptions;
import org.seamcat.presentation.EscapeDialog;
import org.seamcat.presentation.InterferingLinkListDialog;
import org.seamcat.presentation.MainWindow;
import org.seamcat.presentation.NewDialogVictimLink;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.remote.client.ClientRequest;
import org.seamcat.remote.client.SeamcatClientSocket;
import org.seamcat.remote.presentation.ClientRequestStatusDialog;
import org.seamcat.remote.presentation.ClientSettingsDefinitionDialog;
import org.seamcat.remote.util.ClientSetting;
import org.w3c.dom.Document;

// Referenced classes of package org.seamcat.presentation.batch:
//            BatchMasterTablePanel, BatchDetailTablePanel, BatchControlDialog, AddWorkspacePanel, 
//            AbstractAddEditDeleteButtonPanel

public class BatchJobListEditorDialog extends EscapeDialog
{
    private class BatchControlPanel extends JPanel
    {

        private final Logger LOG = Logger.getLogger(org/seamcat/presentation/batch/BatchJobListEditorDialog$BatchControlPanel);
        private final JButton btnStart = new JButton(BatchJobListEditorDialog.STRINGLIST.getString("BATCH_START"));
        private final JButton btnRemote = new JButton(BatchJobListEditorDialog.STRINGLIST.getString("BATCH_REMOTE"));
        private final JButton btnReset = new JButton(BatchJobListEditorDialog.STRINGLIST.getString("BATCH_RESET"));
        private final JButton btnReport = new JButton(BatchJobListEditorDialog.STRINGLIST.getString("BATCH_REPORT"));
        final BatchJobListEditorDialog this$0;




        public BatchControlPanel()
        {
            this$0 = BatchJobListEditorDialog.this;
            super();
            btnStart.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    worker = new Thread(batchJobList);
                    worker.start();
                    btnReset.setEnabled(true);
                    btnReport.setEnabled(true);
                    controls.show(batchJobList);
                    if(LOG.isDebugEnabled())
                        LOG.debug("Showed control dialog");
                }

                final BatchJobListEditorDialog val$this$0;
                final BatchControlPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnReset.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    batchJobList.resetAll();
                    masterTable.repaint();
                    btnReset.setEnabled(false);
                    btnReport.setEnabled(false);
                }

                final BatchJobListEditorDialog val$this$0;
                final BatchControlPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnReport.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    reportDialog.show(batchJobList);
                }

                final BatchJobListEditorDialog val$this$0;
                final BatchControlPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnRemote.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    if(clientSettingsDialog.showAndGenerateClientSetting())
                    {
                        clientRequestStatusDialog.reset();
                        ClientSetting clientSettings = clientSettingsDialog.updateModel();
                        clientSettings.setWorkspaceName(batchJobList.getReference());
                        SeamcatClientSocket client = new SeamcatClientSocket(clientSettings, clientRequestStatusDialog);
                        ClientRequest req = new ClientRequest(clientSettings, "upload");
                        req.setBatch(batchJobList);
                        client.sendRequest(req);
                        MainWindow.getInstance().setCursor(Cursor.getPredefinedCursor(3));
                        clientRequestStatusDialog.setVisible(true);
                        clientSettings.setSendToServer(System.currentTimeMillis());
                        Model.getInstance().addClientSetting(clientSettings);
                        MainWindow.getInstance().setCursor(Cursor.getPredefinedCursor(0));
                        JOptionPane.showMessageDialog(_fld0, BatchJobListEditorDialog.STRINGLIST.getString("REMOTE_CLOSE_BATCH"), BatchJobListEditorDialog.STRINGLIST.getString("REMOTE_CLOSE_BATCH_TITLE"), 1);
                        setVisible(false);
                    }
                }

                final BatchJobListEditorDialog val$this$0;
                final BatchControlPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            add(btnStart);
            add(btnRemote);
            add(btnReset);
            add(btnReport);
        }
    }

    private class DialogAddNewWorkspace extends JDialog
    {

        public void setVisible(boolean modal)
        {
            super.setVisible(modal);
            ok.requestFocusInWindow();
        }

        private final Logger LOG = Logger.getLogger(org/seamcat/presentation/batch/BatchJobListEditorDialog$DialogAddNewWorkspace);
        private AddWorkspacePanel wsPanel;
        private JButton ok;
        final BatchJobListEditorDialog this$0;


        public DialogAddNewWorkspace(Dialog parent)
        {
            this$0 = BatchJobListEditorDialog.this;
            super(parent);
            wsPanel = new AddWorkspacePanel();
            ok = new JButton(BatchJobListEditorDialog.STRINGLIST.getString("BTN_CAPTION_OK"));
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(wsPanel, "Center");
            JPanel buttons = new JPanel();
            ok.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    switch(wsPanel.getState())
                    {
                    default:
                        break;

                    case 0: // '\0'
                        batchJobList.addBatchJob(new BatchJob(new Workspace()));
                        break;

                    case 1: // '\001'
                        try
                        {
                            batchJobList.addBatchJob(new BatchJob(Model.openWorkspace(new File(wsPanel.getFilename()))));
                        }
                        catch(Exception ex)
                        {
                            JOptionPane.showMessageDialog(DialogAddNewWorkspace.this, BatchJobListEditorDialog.STRINGLIST.getString("OPEN_WORKSPACE_ERROR"), BatchJobListEditorDialog.STRINGLIST.getString("OPEN_WORKSPACE_ERROR_TITLE"), 0);
                        }
                        break;
                    }
                    setVisible(false);
                }

                final BatchJobListEditorDialog val$this$0;
                final DialogAddNewWorkspace this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            JButton cancel = new JButton(BatchJobListEditorDialog.STRINGLIST.getString("BTN_CAPTION_CANCEL"));
            cancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setVisible(false);
                }

                final BatchJobListEditorDialog val$this$0;
                final DialogAddNewWorkspace this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            buttons.add(ok);
            buttons.add(cancel);
            getContentPane().add(buttons, "South");
            setSize(400, 200);
            setTitle(BatchJobListEditorDialog.STRINGLIST.getString("ADD_WORKSPACE_TITLE"));
            setLocationRelativeTo(parent);
        }
    }

    private class DialogNavigateButtonPanel extends NavigateButtonPanel
    {

        public void btnOkActionPerformed()
        {
            accept = true;
            setVisible(false);
        }

        public void btnCancelActionPerformed()
        {
            setVisible(false);
        }

        public void btnHelpActionPerformed()
        {
            Seamcat.helpBroker.enableHelp(getRootPane(), helplist.getString(getClass().getName()), null);
        }

        private final Logger LOG;
        final BatchJobListEditorDialog this$0;

        private DialogNavigateButtonPanel()
        {
            this$0 = BatchJobListEditorDialog.this;
            super();
            LOG = Logger.getLogger(org/seamcat/presentation/batch/BatchJobListEditorDialog$DialogNavigateButtonPanel);
        }

    }

    private class MasterButtonPanel extends AbstractAddEditDeleteButtonPanel
    {

        protected void addButtonActionPerformed(ActionEvent e)
        {
            addWsDiag.setVisible(true);
        }

        protected void deleteButtonActionPerformed(ActionEvent e)
        {
            masterTable.removeSelectedWorkspace();
            detailTable.setBatchJob(null);
        }

        protected void dublicateButtonActionPerformed(ActionEvent e)
        {
            BatchJob bj = masterTable.getSelectedBatchJob();
            if(bj != null)
            {
                Document doc = null;
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = null;
                try
                {
                    db = dbf.newDocumentBuilder();
                }
                catch(ParserConfigurationException e1)
                {
                    LOG.error("Error creating document", e1);
                }
                doc = db.newDocument();
                BatchJob newBatchJob = new BatchJob(bj.toElement(doc));
                batchJobList.addBatchJob(newBatchJob);
            }
        }

        private final Logger LOG = Logger.getLogger(org/seamcat/presentation/batch/BatchJobListEditorDialog$MasterButtonPanel);
        private final JButton modify = new JButton(BatchJobListEditorDialog.STRINGLIST.getString("BATCH_MODIFY"));
        final BatchJobListEditorDialog this$0;

        public MasterButtonPanel()
        {
            this$0 = BatchJobListEditorDialog.this;
            super("Master", true);
            final JRadioButton victim = new JRadioButton(BatchJobListEditorDialog.STRINGLIST.getString("BATCH_VICTIM"));
            JRadioButton interferer = new JRadioButton(BatchJobListEditorDialog.STRINGLIST.getString("BATCH_INTERFERER"));
            ButtonGroup bg = new ButtonGroup();
            bg.add(victim);
            bg.add(interferer);
            victim.setSelected(true);
            modify.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    BatchJob bj = masterTable.getSelectedBatchJob();
                    if(bj == null)
                    {
                        JOptionPane.showMessageDialog(_fld0, BatchJobListEditorDialog.STRINGLIST.getString("BATCH_NO_JOB_SELECTED"));
                    } else
                    {
                        if(victim.isSelected())
                        {
                            if(victimLink.show(bj.getWorkspace().getVictimSystemLink()))
                                victimLink.updateModel(bj.getWorkspace().getVictimSystemLink());
                        } else
                        {
                            interferingLinks.setSelectedWorkspace(bj.getWorkspace());
                            interferingLinks.setVisible(true);
                        }
                        detailTable.setBatchJob(bj);
                    }
                }

                final BatchJobListEditorDialog val$this$0;
                final JRadioButton val$victim;
                final MasterButtonPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            add(modify, c);
            add(victim, c);
            add(interferer, c);
        }
    }


    public BatchJobListEditorDialog(JDialog _owner)
    {
        super(_owner, true);
        owner = _owner;
        interferingLinks = new InterferingLinkListDialog(this);
        victimLink = new NewDialogVictimLink(this);
        clientSettingsDialog = new ClientSettingsDefinitionDialog(this);
        clientRequestStatusDialog = new ClientRequestStatusDialog(this);
        JPanel master = new JPanel(new BorderLayout());
        master.add(masterTable, "Center");
        master.add(new MasterButtonPanel(), "East");
        master.setBorder(new TitledBorder("Edit Batch Members"));
        JPanel mainCenter = new JPanel(new GridLayout(2, 1));
        mainCenter.add(master);
        mainCenter.add(detailTable);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(controlPanel, "Center");
        bottomPanel.add(new DialogNavigateButtonPanel(), "South");
        masterTable.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e)
            {
                if(!e.getValueIsAdjusting())
                {
                    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                    if(!lsm.isSelectionEmpty())
                    {
                        int selectedRow = lsm.getMinSelectionIndex();
                        detailTable.setBatchJob(batchJobList.getBatchJob(selectedRow));
                    }
                }
            }

            final BatchJobListEditorDialog this$0;

            
            {
                this$0 = BatchJobListEditorDialog.this;
                super();
            }
        }
);
        setTitle(STRINGLIST.getString("BATCH_EDIT"));
        setSize(800, 600);
        setLocationRelativeTo(owner);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(mainCenter, "Center");
        getContentPane().add(bottomPanel, "South");
        try
        {
            Seamcat.helpBroker.enableHelpKey(getRootPane(), helplist.getString(getClass().getName()), null);
        }
        catch(Exception ex) { }
    }

    public void show(BatchJobList batchJobList)
    {
        this.batchJobList = batchJobList;
        masterTable.setBatchJobList(batchJobList);
        if(masterTable.getSelectedBatchJob() != null)
            detailTable.setBatchJob(masterTable.getSelectedBatchJob());
        else
            detailTable.setBatchJob(new BatchJob((Workspace)null));
        try
        {
            controlPanel.btnReset.setEnabled(((BatchJob)batchJobList.getBatchJobs().get(0)).getControl().isProcessed());
            controlPanel.btnReport.setEnabled(((BatchJob)batchJobList.getBatchJobs().get(0)).getControl().isProcessed());
        }
        catch(Exception ex)
        {
            controlPanel.btnReset.setEnabled(false);
            controlPanel.btnReport.setEnabled(false);
        }
        masterTable.selectFirstRow();
        LOG.debug("Showing BatchJobList");
        setVisible(true);
        LOG.debug("Showed BatchJobList");
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/batch/BatchJobListEditorDialog);
    private static final ResourceBundle STRINGLIST;
    private static final ResourceBundle helplist;
    private final DialogAddNewWorkspace addWsDiag = new DialogAddNewWorkspace(this);
    private final BatchMasterTablePanel masterTable = new BatchMasterTablePanel();
    private final BatchDetailTablePanel detailTable = new BatchDetailTablePanel(this);
    private final BatchControlPanel controlPanel = new BatchControlPanel();
    private final BatchControlDialog controls = new BatchControlDialog(this);
    private final DialogReportOptions reportDialog = new DialogReportOptions(this);
    private boolean accept;
    private Thread worker;
    private BatchJobList batchJobList;
    private InterferingLinkListDialog interferingLinks;
    private NewDialogVictimLink victimLink;
    private ClientSettingsDefinitionDialog clientSettingsDialog;
    private ClientRequestStatusDialog clientRequestStatusDialog;
    private JDialog owner;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }














}
