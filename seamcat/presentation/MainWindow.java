// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MainWindow.java

package org.seamcat.presentation;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.help.HelpBroker;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.seamcat.Seamcat;
import org.seamcat.calculator.Calculator;
import org.seamcat.cdma.CDMAResults;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.cdma.presentation.ActivatedUsersGraph;
import org.seamcat.cdma.presentation.CDMACapacityPanel;
import org.seamcat.cdma.presentation.CDMASystemPlotPanel;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.DiscreteFunction2;
import org.seamcat.function.Function;
import org.seamcat.function.Function2;
import org.seamcat.importer.ImportedLibrary;
import org.seamcat.importer.ImportedScenario;
import org.seamcat.importer.LegacySeamcatConverter;
import org.seamcat.interfaces.ImportLibraryConflictListener;
import org.seamcat.model.Antenna;
import org.seamcat.model.Components;
import org.seamcat.model.Control;
import org.seamcat.model.EventGenerationData;
import org.seamcat.model.Library;
import org.seamcat.model.Model;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.model.TransmitterToReceiverPath;
import org.seamcat.model.Workspace;
import org.seamcat.model.core.EventVector;
import org.seamcat.model.core.InterferenceLink;
import org.seamcat.model.core.VictimSystemLink;
import org.seamcat.model.datatypes.DRSSVector;
import org.seamcat.model.datatypes.IRSSVector;
import org.seamcat.model.engines.EGE;
import org.seamcat.model.engines.EgeExceptionListener;
import org.seamcat.model.engines.EventCompletionListener;
import org.seamcat.model.scenariocheck.ScenarioCheckResult;
import org.seamcat.model.scenariocheck.ScenarioCheckUtils;
import org.seamcat.presentation.batch.BatchJobListDialog;
import org.seamcat.presentation.components.InterferenceCalculationsPanel;
import org.seamcat.presentation.components.ServerStatusPanel;
import org.seamcat.presentation.postprocessing.PostProcessingPluginAssignmentDialog;
import org.seamcat.presentation.postprocessing.PostProcessingPluginListDialog;
import org.seamcat.propagation.PropagationModel;
import org.seamcat.remote.client.ClientRequest;
import org.seamcat.remote.client.SeamcatClientSocket;
import org.seamcat.remote.presentation.ClientRequestStatusDialog;
import org.seamcat.remote.presentation.ClientSettingsDefinitionDialog;
import org.seamcat.remote.presentation.ClientStatusDialog;
import org.seamcat.remote.server.SeamcatServerSocket;
import org.seamcat.remote.server.WorkspaceProcessor;
import org.seamcat.remote.util.ClientSetting;
import org.seamcat.remote.util.ServerSetting;

// Referenced classes of package org.seamcat.presentation:
//            NodeAttributeTable, DialogOptions, DistributionDialog, DialogFunctionDefine, 
//            DialogFunction2Define, DialogReportOptions, DistributionTestDialog, PropagationTestDialog, 
//            FunctionTestDialog, DialogGenerateMultipleInterferingSystems, SeamcatDistributionPlot, AntennaListDialog, 
//            TransmitterListDialog, ReceiverListDialog, SimulationControlDialog, CDMALinkLevelDataListDialog, 
//            PluginListDialog, InterferingLinkListDialog, ImportLibraryConflictSolverDialog, NewDialogVictimLink, 
//            DialogDisplaySignal, SeamcatIcons, PropagationSelectDialog, LogLineCatcher

public final class MainWindow extends JFrame
{

    public static final MainWindow getInstance()
    {
        return singleton;
    }

    private MainWindow()
    {
        helpBroker = null;
        selectedWorkspace = null;
        splitPane = new JSplitPane(1, leftScrollPane, tabbedPane);
        root = new JPanel(rootLayout);
        statusLabel = new JLabel("", 2);
        usePostProcessing = false;
        eventHandlerPlotFixedElements = new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                Workspace ws = getSelectedWorkspace();
                if(ws != null)
                {
                    sdp.addWantedTransmitter(0.0D, 0.0D);
                    if(ws.getVictimSystemLink().getWt2VrPath().getUseCorrelationDistance())
                        sdp.addVictimReceiver(ws.getVictimSystemLink().getWt2VrPath().getDeltaX(), ws.getVictimSystemLink().getWt2VrPath().getDeltaY());
                    Collection v = ws.getInterferenceLinks();
                    if(sdp.getMaxEventsToPlot() < v.size())
                        sdp.setMaxEventsToPlot(v.size());
                    Iterator i$ = v.iterator();
                    do
                    {
                        if(!i$.hasNext())
                            break;
                        InterferenceLink ifl = (InterferenceLink)i$.next();
                        if(ifl.getCorrelationMode() != 0 && ifl.getCorrelationMode() != 2 && ifl.getCorrelationMode() != 1)
                            sdp.addInterferingTransmitter(ws.getVictimSystemLink().getWt2VrPath().getDeltaX() + ifl.getWt2VrPath().getDeltaX(), ws.getVictimSystemLink().getWt2VrPath().getDeltaY() + ifl.getWt2VrPath().getDeltaY());
                    } while(true);
                    sdp.updateGuiComponents();
                    tabbedPane.setSelectedComponent(sdp);
                } else
                {
                    JOptionPane.showMessageDialog(MainWindow.this, "Unable to plot scenario outline because no workspace has been selected");
                }
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
;
    }

    public void init(Model _model)
    {
        model = _model;
        FILECHOOSER = new JFileChooser(Model.seamcatHome);
        usePostProcessing = Model.usePostProcessingPlugins;
        calculator.setModal(false);
        LOG.debug("Starting MainWindow init");
        dialogConfiguration = new DialogOptions(this);
        LOG.debug("DialogOptions initialized");
        distDialog = new DistributionDialog(this, true);
        LOG.debug("DistributionDialog initialized");
        editFunction = new DialogFunctionDefine(this, true);
        LOG.debug("DialogFunctionDefine initialized");
        editFunction2 = new DialogFunction2Define(this, true);
        LOG.debug("DialogUnwantedEmission initialized");
        report = new DialogReportOptions(this);
        LOG.debug("Report dialog initialized");
        distributionTest = new DistributionTestDialog(this);
        LOG.debug("Distribution Test dialog initialized");
        propagationTest = new PropagationTestDialog(this);
        LOG.debug("Propagation Test dialog initialized");
        functionTest = new FunctionTestDialog(this);
        LOG.debug("Function Test Dialog initialized");
        clientSettingsDialog = new ClientSettingsDefinitionDialog(this);
        if(model.getClientSettings().size() > 0)
            clientSettingsDialog.setClientSettings((ClientSetting)model.getClientSettings().get(model.getClientSettings().size() - 1));
        LOG.debug("ClientSettings dialog initialized");
        clientRequestStatusDialog = new ClientRequestStatusDialog(this);
        LOG.debug("ClientRequestStatusDialog initialized");
        clientStatusDialog = new ClientStatusDialog(this);
        LOG.debug("ClientStatusDialog initialized");
        dlgGenerateInterferingSystems = new DialogGenerateMultipleInterferingSystems(this);
        LOG.debug("Multiple InterferingLink dialog initialized");
        helpBroker = Seamcat.helpBroker;
        statusBar.add(statusLabel);
        LOG.debug("Manipulating ButtonModels");
        buttonModelWorkspace.setMnemonic(87);
        buttonModelNewWorkspace.setMnemonic(78);
        buttonModelNewWorkspace.addActionListener(eventHandlerNewWorkspace);
        buttonModelRunEGE.setMnemonic(82);
        buttonModelRunEGE.addActionListener(eventHandlerRunEGE);
        buttonModelStopEGE.setMnemonic(81);
        buttonModelStopEGE.addActionListener(eventHandlerStopEGE);
        buttonModelOpenWorkspace.setMnemonic(79);
        buttonModelOpenWorkspace.addActionListener(eventHandlerOpenWorkspace);
        buttonModelSaveWorkspace.setMnemonic(83);
        buttonModelSaveWorkspace.addActionListener(eventHandlerSaveWorkspace);
        buttonModelSaveWorkspaceAs.setMnemonic(65);
        buttonModelSaveWorkspaceAs.addActionListener(eventHandlerSaveWorkspaceAs);
        buttonModelSaveAllWorkspaces.setMnemonic(65);
        buttonModelSaveAllWorkspaces.addActionListener(eventHandlerSaveAllWorkspaces);
        buttonModelCloseWorkspace.setMnemonic(67);
        buttonModelCloseWorkspace.addActionListener(eventHandlerCloseWorkspace);
        buttonModelImportLibrary.addActionListener(eventHandlerImportLibrary);
        buttonModelExportLibrary.addActionListener(eventHandlerExportLibrary);
        buttonModelConfiguration.setMnemonic(71);
        buttonModelConfiguration.addActionListener(eventHandlerConfiguration);
        buttonModelExpandTree.setMnemonic(69);
        buttonModelExpandTree.addActionListener(eventHandlerExpandTree);
        buttonModelCollapseTree.setMnemonic(67);
        buttonModelCollapseTree.addActionListener(eventHandlerCollapseTree);
        buttonModelVictimLink.setMnemonic(86);
        buttonModelInterferingLinks.setMnemonic(73);
        buttonModelCheckConsistency.setMnemonic(72);
        buttonModelCheckConsistency.addActionListener(eventHandlerCheckConsistency);
        buttonModelSimulationControl.setMnemonic(83);
        buttonModelInterferenceCalculation.setMnemonic(67);
        buttonModelReport.setMnemonic(82);
        buttonModelReport.addActionListener(eventHandlerReport);
        buttonModelLegacyImportLibrary.addActionListener(eventHandlerLegacyImportLibrary);
        buttonModelLegacyImportScenario.addActionListener(eventHandlerLegacyImportScenario);
        buttonModelTestPropagationModel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                propagationTest.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        buttonModelGenerateMultipleInterferingSystems.setMnemonic(71);
        buttonModelGenerateMultipleInterferingSystems.addActionListener(eventHandlerGenerateMultipleInterferingSystems);
        buttonModelTestDistributions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                distributionTest.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        buttonModelTestFunctions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                functionTest.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        buttonModelTestCalculator.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                calculator.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        buttonModelRemoteCheckJob.addActionListener(eventHandlerRemoteCheckJob);
        buttonModelRemoteSendJob.addActionListener(eventHandlerRemoteSendJob);
        buttonModelStartServerMode.addActionListener(eventHandlerRemoteStartServer);
        buttonModelStopServerMode.addActionListener(eventHandlerRemoteStopServer);
        buttonModelStopServerMode.setEnabled(false);
        setDefaultCloseOperation(0);
        addWindowListener(eventHandlerWindow);
        LOG.debug("Loading icons");
        setIconImage(SeamcatIcons.getImageIcon("SEAMCAT_ICON_SEAMCAT").getImage());
        setTitle((new StringBuilder()).append(STRINGLIST.getString("APPLICATION_TITLE")).append(" - buildtime: ").append(STRINGLIST.getString("APPLICATION_BUILD_TIME")).toString());
        LOG.debug("Creaing MenuBar");
        createMenuBar();
        LOG.debug("Creating Toolbar");
        createToolBar();
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.setLayout(new FlowLayout(0));
        getContentPane().add(statusBar, "South");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(splitPane);
        leftScrollPane.setPreferredSize(new Dimension(300, 300));
        leftScrollPane.getVerticalScrollBar().setFocusable(false);
        leftScrollPane.getHorizontalScrollBar().setFocusable(false);
        rightScrollPane.getVerticalScrollBar().setFocusable(false);
        rightScrollPane.getHorizontalScrollBar().setFocusable(false);
        LOG.debug("Creating Tree");
        createTree();
        LOG.debug("Creating Table");
        createTable();
        splitPane.setOneTouchExpandable(true);
        tabbedPane.add("Simulation Attributes", rightScrollPane);
        LOG.debug("Constructing Seamcat Distribution Plot Panel");
        sdp = new SeamcatDistributionPlot("", "", 0, 100);
        LOG.debug("Constructing Seamcat Distribution Plot Panel");
        LOG.debug("Constructing Interference Calculations Panel");
        itc = new InterferenceCalculationsPanel();
        LOG.debug("Constructing Detailed CDMASystem Plot");
        cdmaPlot = new CDMASystemPlotPanel();
        LOG.debug("Constructing CDMA Capacity panel");
        capacityPanel = new CDMACapacityPanel();
        tabbedPane.add("Simulation Status", sdp);
        interferenceResultsDeck = new CardLayout();
        interferenceResults = new JPanel(interferenceResultsDeck);
        interferenceResults.add(itc, "interference");
        interferenceResults.add(capacityPanel, "cdma");
        LOG.debug("Ordering layout of SDP");
        tabbedPane.add("Interference Calculations", interferenceResults);
        tabbedPane.add("CDMA System Details", cdmaPlot);
        tabbedPane.setEnabledAt(3, false);
        LOG.debug("Constructing Victim CDMA Active users debug panel");
        activityPanel = new ActivatedUsersGraph();
        tabbedPane.add("Victim CDMA System Statistics", activityPanel);
        tabbedPane.setEnabledAt(4, false);
        LOG.debug("Constructing Server Status panel");
        serverStatusPanel = new ServerStatusPanel();
        root.add(panel, "CLIENT_MODE");
        root.add(serverStatusPanel, "SERVER_MODE");
        getContentPane().add(root);
        LOG.debug("Updating button models");
        updateButtonModels();
        LOG.debug("Enabling Tree");
        tree.setEnabled(true);
        LOG.debug("Constructing Antenna Library Manipulation Dialog");
        antennaLibraryDialog = new AntennaListDialog(this);
        LOG.debug("Constructing Transmitter Library Manipulation Dialog");
        transmitterLibraryDialog = new TransmitterListDialog(this);
        LOG.debug("Constructing Receiver Library Manipulation Dialog");
        receiverLibraryDialog = new ReceiverListDialog(this);
        LOG.debug("Constructing Simulation Control Manipulation Dialog");
        simulationControlDialog = new SimulationControlDialog(this);
        LOG.debug("Constructing CDMA Library Manipulation Dialog");
        cdmaLinkLevelDataDialog = new CDMALinkLevelDataListDialog(this);
        LOG.debug("Constructing Plugin Library Manipulation Dialog");
        propagationPluginsDialog = new PluginListDialog(this);
        LOG.debug("Constructing BatchJob List dialog");
        batchLibraryDialog = new BatchJobListDialog(this);
        LOG.debug("Constructing interfering link dialog");
        splitPane.setDividerLocation(300);
        interferingLinkListDialog = new InterferingLinkListDialog(this);
        LOG.debug("Constructing conflict solver dialog");
        importLibraryConflictHandler = new ImportLibraryConflictSolverDialog(this);
        LOG.debug("Constructing victim link dialog");
        dialogVictimLink = new NewDialogVictimLink(this);
        LOG.debug("Constructing DRSS dialog");
        drssDialog = new DialogDisplaySignal(this, STRINGLIST.getString("VECTOR_GRAPH_AXIX_TITLE_X"), STRINGLIST.getString("VECTOR_GRAPH_AXIX_TITLE_Y"));
        if(usePostProcessing)
        {
            LOG.debug("Constructing post processing dialog");
            postProcessingListDialog = new PostProcessingPluginListDialog(this);
            LOG.debug("Constructing post processing assignment dialog");
            postProcessAssignment = new PostProcessingPluginAssignmentDialog(this);
        }
        LOG.debug("Enabling help system");
        helpBroker.enableHelpKey(rootPane, HELPLIST.getString("org.seamcat.presentation.MainWindow"), null);
        statusLabel.setText((new StringBuilder()).append("SEAMCAT startup time in milliseconds: ").append(System.currentTimeMillis() - Seamcat.ctm).toString());
    }

    public final Model getModel()
    {
        return model;
    }

    public final Workspace getSelectedWorkspace()
    {
        if(selectedWorkspace == null && getModel().getWorkspaces().size() == 1)
            selectedWorkspace = (Workspace)getModel().getWorkspaces().get(0);
        return selectedWorkspace;
    }

    private void resetTree()
    {
        int count = tree.getModel().getChildCount(tree.getModel().getRoot());
        for(int i = 0; i < count; i++)
            if(tree.getModel().getChild(tree.getModel().getRoot(), i) instanceof SeamcatComponent)
                ((SeamcatComponent)tree.getModel().getChild(tree.getModel().getRoot(), i)).updateNodeAttributes();

    }

    private final void exit()
    {
        Iterator i$ = model.getWorkspaces().iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Workspace w = (Workspace)i$.next();
            if(!w.getIsSaved())
            {
                int shouldBeSaved = JOptionPane.showConfirmDialog(this, replaceVariables(STRINGLIST.getString("SAVE_WORKSPACE_ON_EXIT"), w.getReference(), 1), STRINGLIST.getString("SAVE_WORKSPACE_ON_EXIT_TITLE"), 1, 1);
                if(shouldBeSaved == 2)
                    return;
                if(shouldBeSaved == 0)
                {
                    selectedWorkspace = w;
                    eventHandlerSaveWorkspace.actionPerformed(new ActionEvent(this, -1, ""));
                }
            }
        } while(true);
        try
        {
            model.persist();
            LOG.debug("Saved model");
        }
        catch(ParserConfigurationException ex)
        {
            JOptionPane.showMessageDialog(this, "Unable to save model to disk", "Save error", 2);
        }
        System.exit(0);
    }

    private final void createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        createMenuFile(menuBar);
        createMenuEdit(menuBar);
        createMenuView(menuBar);
        createMenuLibrary(menuBar);
        createMenuWorkspace(menuBar);
        createMenuTools(menuBar);
        createMenuHelp(menuBar);
        setJMenuBar(menuBar);
    }

    private final void createMenuFile(JMenuBar menuBar)
    {
        JMenu menu = new JMenu();
        menu.setText(STRINGLIST.getString("FILE_MENU_TEXT"));
        menu.setMnemonic(70);
        createMenuItemNewWorkspace(menu);
        createMenuItemOpenWorkspace(menu);
        menu.addSeparator();
        createMenuItemSaveWorkspace(menu);
        createMenuItemSaveWorkspaceAs(menu);
        createMenuItemSaveAllWorkspaces(menu);
        menu.addSeparator();
        createMenuItemCloseWorkspace(menu);
        createMenuItemCloseAllWorkspaces(menu);
        menu.addSeparator();
        createMenuItemImportLibrary(menu);
        createMenuItemExportLibrary(menu);
        menu.addSeparator();
        createLegacyImportMenu(menu);
        menu.addSeparator();
        createMenuItemConfiguration(menu);
        menu.addSeparator();
        createMenuItemExit(menu);
        menuBar.add(menu);
    }

    private final void createLegacyImportMenu(JMenu menu)
    {
        JMenu subMenu = new JMenu("Legacy Import");
        subMenu.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_BLANK"));
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelLegacyImportLibrary);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_IMPORT_LIBRARY"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_LEGACY_IMPORT_LIBRARY"));
        subMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setModel(buttonModelLegacyImportScenario);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_IMPORT_LIBRARY"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_LEGACY_IMPORT_SCENARIO"));
        subMenu.add(menuItem);
        menu.add(subMenu);
    }

    private final void createMenuItemNewWorkspace(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelNewWorkspace);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_NEW"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_NEW_WORKSPACE"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(78, 2));
        menu.add(menuItem);
    }

    private final void createMenuEdit(JMenuBar menuBar)
    {
        JMenu menu = new JMenu();
        menu.setText(STRINGLIST.getString("EDIT_MENU_TEXT"));
        menu.setMnemonic(69);
        createMenuItemCut(menu);
        createMenuItemCopy(menu);
        createMenuItemPaste(menu);
        menuBar.add(menu);
    }

    private final void createMenuView(JMenuBar menuBar)
    {
        JMenu menu = new JMenu();
        menu.setText(STRINGLIST.getString("VIEW_MENU_TEXT"));
        menu.setMnemonic(86);
        createMenuItemViewToolBar(menu);
        createMenuItemViewStatusBar(menu);
        menu.addSeparator();
        createMenuItemExpandTree(menu);
        createMenuItemCollapseTree(menu);
        menuBar.add(menu);
    }

    private final void createMenuLibrary(JMenuBar menuBar)
    {
        JMenu menu = new JMenu();
        JMenuItem transmitter = new JMenuItem("Transmitters");
        JMenuItem antenna = new JMenuItem("Antennas");
        JMenuItem receiver = new JMenuItem("Receivers");
        JMenuItem cdmaLinkLevel = new JMenuItem("CDMA link level data");
        JMenuItem plugins = new JMenuItem("Propagation model plugins");
        JMenuItem postplugins = new JMenuItem("Post processing plugins");
        transmitter.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                transmitterLibraryDialog.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        antenna.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                antennaLibraryDialog.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        receiver.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                receiverLibraryDialog.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        cdmaLinkLevel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                cdmaLinkLevelDataDialog.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        plugins.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                propagationPluginsDialog.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        postplugins.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                postProcessingListDialog.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        menu.setText(STRINGLIST.getString("LIBRARY_MENU_TEXT"));
        menu.setMnemonic(76);
        menu.add(antenna);
        menu.add(receiver);
        menu.add(transmitter);
        menu.add(cdmaLinkLevel);
        menu.add(plugins);
        if(usePostProcessing)
            menu.add(postplugins);
        menuBar.add(menu);
    }

    private final void createMenuWorkspace(JMenuBar menuBar)
    {
        JMenu menu = new JMenu();
        menu.setModel(buttonModelWorkspace);
        menu.setText(STRINGLIST.getString("MENU_TEXT_WORKSPACE"));
        createMenuItemVictimLink(menu);
        createMenuItemInterferingLinks(menu);
        menu.addSeparator();
        if(usePostProcessing)
        {
            createMenuItemPostProcessing(menu);
            menu.addSeparator();
        }
        createMenuItemCheckConsistency(menu);
        menu.addSeparator();
        createMenuItemSimulationControl(menu);
        menu.addSeparator();
        createMenuItemInterferenceCalculation(menu);
        menu.addSeparator();
        createMenuItemReport(menu);
        menuBar.add(menu);
    }

    private final void createMenuTools(JMenuBar menuBar)
    {
        JMenu menu = new JMenu();
        menu.setText(STRINGLIST.getString("TOOLS_MENU_TEXT"));
        menu.setMnemonic(84);
        createMenuItemBatchOperation(menu);
        menu.addSeparator();
        createMenuItemGenerateMultipleInterferingSystems(menu);
        menu.addSeparator();
        createMenuTestFunctions(menu);
        menu.addSeparator();
        createMenuRemoteComputing(menu);
        menuBar.add(menu);
    }

    private final void createMenuRemoteComputing(JMenu menu)
    {
        JMenu subMenu = new JMenu(STRINGLIST.getString("REMOTE_SUBMENU_TEXT"));
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelRemoteSendJob);
        menuItem.setText(STRINGLIST.getString("REMOTE_SEND_JOB_MENU_ITEM_TEXT"));
        subMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setModel(buttonModelRemoteCheckJob);
        menuItem.setText(STRINGLIST.getString("REMOTE_CHECK_JOB_MENU_ITEM_TEXT"));
        subMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setModel(buttonModelStartServerMode);
        menuItem.setText(STRINGLIST.getString("REMOTE_START_SERVER_MENU_ITEM_TEXT"));
        subMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setModel(buttonModelStopServerMode);
        menuItem.setText(STRINGLIST.getString("REMOTE_STOP_SERVER_MENU_ITEM_TEXT"));
        subMenu.add(menuItem);
        menu.add(subMenu);
    }

    private final void createMenuTestFunctions(JMenu menu)
    {
        JMenu subMenu = new JMenu(STRINGLIST.getString("TEST_SUBMENU_TEXT"));
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelTestDistributions);
        menuItem.setText(STRINGLIST.getString("TEST_DISTRIBUTIONS_MENU_ITEM_TEXT"));
        subMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setModel(buttonModelTestPropagationModel);
        menuItem.setText(STRINGLIST.getString("TEST_PROPAGATIONS_MENU_ITEM_TEXT"));
        subMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setModel(buttonModelTestFunctions);
        menuItem.setText(STRINGLIST.getString("TEST_FUNCTIONS_MENU_ITEM_TEXT"));
        subMenu.add(menuItem);
        menuItem = new JMenuItem();
        menuItem.setModel(buttonModelTestCalculator);
        menuItem.setText(STRINGLIST.getString("TEST_CALCULATOR_MENU_ITEM_TEXT"));
        subMenu.add(menuItem);
        menu.add(subMenu);
    }

    private final void createMenuHelp(JMenuBar menuBar)
    {
        JMenu menu = new JMenu();
        menu.setText(STRINGLIST.getString("HELP_MENU_TEXT"));
        menu.setMnemonic(72);
        createMenuItemHelp(menu);
        menuBar.add(menu);
    }

    private final void createMenuItemOpenWorkspace(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelOpenWorkspace);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_OPEN"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_OPEN_WORKSPACE"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(79, 2));
        menu.add(menuItem);
    }

    private final void createMenuItemSaveWorkspace(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelSaveWorkspace);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_SAVE"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_SAVE_WORKSPACE"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(83, 2));
        menu.add(menuItem);
    }

    private final void createMenuItemSaveWorkspaceAs(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelSaveWorkspaceAs);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_SAVEAS"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_SAVE_WORKSPACE_AS"));
        menuItem.setMnemonic(65);
        menu.add(menuItem);
    }

    private final void createMenuItemSaveAllWorkspaces(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelSaveAllWorkspaces);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_SAVEALL"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_SAVE_ALL_WORKSPACES"));
        menuItem.setMnemonic(65);
        menu.add(menuItem);
    }

    private final void createMenuItemCloseWorkspace(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelCloseWorkspace);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_CLOSE"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_CLOSE_WORKSPACE"));
        menu.add(menuItem);
    }

    private final void createMenuItemCloseAllWorkspaces(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_CLOSEALL"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_CLOSE_ALL_WORKSPACES"));
        menuItem.setMnemonic(76);
        menuItem.setEnabled(false);
        menu.add(menuItem);
    }

    private final void createMenuItemImportLibrary(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelImportLibrary);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_IMPORT_LIBRARY"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_IMPORT_LIBRARY"));
        menuItem.setMnemonic(73);
        menu.add(menuItem);
    }

    private final void createMenuItemExportLibrary(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelExportLibrary);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_EXPORT_LIBRARY"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_EXPORT_LIBRARY"));
        menuItem.setMnemonic(69);
        menu.add(menuItem);
    }

    private final void createMenuItemConfiguration(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelConfiguration);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CONFIGURATION"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_CONFIGURATION"));
        menu.add(menuItem);
    }

    private final void createMenuItemExit(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerExit);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_EXIT"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_EXIT"));
        menuItem.setMnemonic(88);
        menu.add(menuItem);
    }

    private final void createMenuItemCut(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CUT"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_CUT"));
        menuItem.setMnemonic(84);
        menuItem.setEnabled(false);
        menu.add(menuItem);
    }

    private final void createMenuItemCopy(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_COPY"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_COPY"));
        menuItem.setMnemonic(67);
        menuItem.setEnabled(false);
        menu.add(menuItem);
    }

    private final void createMenuItemPaste(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_PASTE"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_PASTE"));
        menuItem.setMnemonic(80);
        menuItem.setEnabled(false);
        menu.add(menuItem);
    }

    private final void createMenuItemViewToolBar(JMenu menu)
    {
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
        menuItem.addActionListener(eventHandlerViewToolBar);
        menuItem.setText("Tool Bar");
        menuItem.setMnemonic(84);
        menuItem.setSelected(true);
        menu.add(menuItem);
    }

    private final void createMenuItemViewStatusBar(JMenu menu)
    {
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
        menuItem.addActionListener(eventHandlerViewStatusBar);
        menuItem.setText("Status Bar");
        menuItem.setMnemonic(83);
        menuItem.setSelected(true);
        menu.add(menuItem);
    }

    private final void createMenuItemExpandTree(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerExpandTree);
        menuItem.setText("Expand Tree");
        menuItem.setMnemonic(69);
        menu.add(menuItem);
    }

    private final void createMenuItemCollapseTree(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerCollapseTree);
        menuItem.setText("Collapse Tree");
        menuItem.setMnemonic(67);
        menu.add(menuItem);
    }

    private final void createMenuItemVictimLink(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerVictimLink);
        menuItem.setModel(buttonModelVictimLink);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_BLANK"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_VICTIM_LINK"));
        menu.add(menuItem);
    }

    private final void createMenuItemPostProcessing(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerPostProcessing);
        menuItem.setModel(buttonModelPostProcessing);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_BLANK"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_POST_PROCESSING"));
        menu.add(menuItem);
    }

    private final void createMenuItemInterferingLinks(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerInterferingLink);
        menuItem.setModel(buttonModelInterferingLinks);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_BLANK"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_INTERFERING_LINKS"));
        menu.add(menuItem);
    }

    private final void createMenuItemCheckConsistency(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelCheckConsistency);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CHECK_CONSISTENCY"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_CHECK_CONSISTENCY"));
        menu.add(menuItem);
    }

    private final void createMenuItemSimulationControl(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerSimulationControl);
        menuItem.setModel(buttonModelSimulationControl);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_SIMULATION_CONTROL"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_SIMULATION_CONTROL"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(32, 2));
        menu.add(menuItem);
    }

    private final void createMenuItemInterferenceCalculation(JMenu menu)
    {
        interferenceCalculationMenuItem = new JMenuItem();
        interferenceCalculationMenuItem.addActionListener(eventHandlerInterferenceCalculation);
        interferenceCalculationMenuItem.setModel(buttonModelInterferenceCalculation);
        interferenceCalculationMenuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_INTERFERENCE_CALCULATIONS"));
        interferenceCalculationMenuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_INTERFERENCE_CALCULATION"));
        interferenceCalculationMenuItem.setEnabled(false);
        menu.add(interferenceCalculationMenuItem);
    }

    private final void createMenuItemReport(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelReport);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_GENERATE_REPORT"));
        menuItem.setText(STRINGLIST.getString("MENU_ITEM_TEXT_REPORT"));
        menu.add(menuItem);
    }

    private final void createMenuItemBatchOperation(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerBatch);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_BATCH"));
        menuItem.setText("Batch Operation");
        menuItem.setMnemonic(66);
        menuItem.setEnabled(true);
        menu.add(menuItem);
    }

    private final void createMenuItemGenerateMultipleInterferingSystems(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setModel(buttonModelGenerateMultipleInterferingSystems);
        menuItem.setText("Generate Multiple Interfering Systems");
        menuItem.setMnemonic(71);
        menu.add(menuItem);
    }

    private final void createMenuItemHelp(JMenu menu)
    {
        JMenuItem menuItem = new JMenuItem();
        menuItem.addActionListener(eventHandlerHelp);
        menuItem.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_HELP"));
        menuItem.setText(STRINGLIST.getString("HELP_CONTENTS_MENU_ITEM_TEXT"));
        menuItem.setMnemonic(72);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(112, 0));
        menu.add(menuItem);
        helpBroker.enableHelpOnButton(menuItem, HELPLIST.getString("org.seamcat.presentation.MainWindow_HelpMenu.HelpItem"), null);
    }

    private final void createToolBar()
    {
        toolBar.setName(STRINGLIST.getString("TOOLBAR_TITLE_TEXT"));
        toolBar.setFocusable(false);
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        createButtonNewWorkspace();
        createButtonOpenWorkspace();
        createButtonSaveWorkspace();
        createButtonCloseWorkspace();
        toolBar.addSeparator();
        createButtonImportLibrary();
        createButtonExportLibrary();
        toolBar.addSeparator();
        createButtonCut();
        createButtonCopy();
        createButtonPaste();
        toolBar.addSeparator();
        createButtonRunEGE();
        createButtonStopEGE();
        toolBar.addSeparator();
        createButtonCheckConsistency();
        createButtonInterferenceCalculation();
        createButtonReport();
        toolBar.addSeparator();
        createButtonHelp();
        toolBar.addSeparator();
        createButtonPlotFixedElements();
        toolBar.addSeparator();
        createButtonCalculator();
        getContentPane().add(toolBar, "North");
    }

    private final void createButtonRunEGE()
    {
        JButton button = new JButton();
        button.setModel(buttonModelRunEGE);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_SIMULATION_START", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_RUN_EGE");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        button.setMnemonic(82);
        toolBar.add(button);
    }

    private final void createButtonStopEGE()
    {
        JButton button = new JButton();
        button.setModel(buttonModelStopEGE);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_SIMULATION_STOP", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_STOP_EGE");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonResetEGE()
    {
        JButton button = new JButton();
        button.setModel(buttonModelResetEGE);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_SIMULATION_RESET", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_RESET_EGE");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonNewWorkspace()
    {
        JButton button = new JButton();
        button.setModel(buttonModelNewWorkspace);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_NEW", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_NEW_WORKSPACE");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonOpenWorkspace()
    {
        JButton button = new JButton();
        button.setModel(buttonModelOpenWorkspace);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_OPEN", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_OPEN_WORKSPACE");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonSaveWorkspace()
    {
        JButton button = new JButton();
        button.setModel(buttonModelSaveWorkspace);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_SAVE", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_SAVE_WORKSPACE");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonCloseWorkspace()
    {
        JButton button = new JButton();
        button.setModel(buttonModelCloseWorkspace);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_WORKSPACE_CLOSE", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_CLOSE_WORKSPACE");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonImportLibrary()
    {
        JButton button = new JButton();
        button.setModel(buttonModelImportLibrary);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_IMPORT_LIBRARY", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_IMPORT_LIBRARY");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonExportLibrary()
    {
        JButton button = new JButton();
        button.setModel(buttonModelExportLibrary);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_EXPORT_LIBRARY", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_EXPORT_LIBRARY");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonCut()
    {
        JButton button = new JButton();
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CUT", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_CUT");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        button.setEnabled(false);
        toolBar.add(button);
    }

    private final void createButtonCopy()
    {
        JButton button = new JButton();
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_COPY", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_COPY");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        button.setEnabled(false);
        toolBar.add(button);
    }

    private final void createButtonPaste()
    {
        JButton button = new JButton();
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_PASTE", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_PASTE");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        button.setEnabled(false);
        toolBar.add(button);
    }

    private final void createButtonCheckConsistency()
    {
        JButton button = new JButton();
        button.setModel(buttonModelCheckConsistency);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CHECK_CONSISTENCY", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_CHECK_CONSISTENCY");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonCalculator()
    {
        JButton button = new JButton();
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                calculator.setVisible(true);
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CALCULATOR", 0));
        String toolTip = STRINGLIST.getString("TEST_CALCULATOR_MENU_ITEM_TEXT");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonPlotFixedElements()
    {
        JButton button = new JButton();
        button.addActionListener(eventHandlerPlotFixedElements);
        button.setModel(buttonModelPlotFixedElements);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_PLOT_FIXED_ELEMENTS", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_PLOT_FIXED_ELEMENTS");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonInterferenceCalculation()
    {
        JButton button = new JButton();
        button.addActionListener(eventHandlerInterferenceCalculation);
        button.setModel(buttonModelInterferenceCalculation);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_INTERFERENCE_CALCULATIONS", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_INTERFERENCE_CALCULATION");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonReport()
    {
        JButton button = new JButton();
        button.setModel(buttonModelReport);
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_GENERATE_REPORT", 0));
        String toolTip = STRINGLIST.getString("MENU_ITEM_TEXT_REPORT");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        toolBar.add(button);
    }

    private final void createButtonHelp()
    {
        JButton button = new JButton();
        button.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_HELP", 0));
        String toolTip = STRINGLIST.getString("HELP_CONTENTS_MENU_ITEM_TEXT");
        button.setToolTipText(toolTip);
        button.setFocusable(false);
        button.addActionListener(eventHandlerHelp);
        toolBar.add(button);
        helpBroker.enableHelpOnButton(button, HELPLIST.getString("org.seamcat.presentation.MainWindow_Toolbar.HelpButton"), null);
    }

    private final void createTree()
    {
        tree = new JTree() {

            public boolean isPathEditable(TreePath path)
            {
                if(path == null)
                    return false;
                else
                    return path.getLastPathComponent() instanceof Workspace;
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
;
        tree.setModel(model);
        tree.getSelectionModel().setSelectionMode(1);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setSelectionRow(0);
        tree.addTreeSelectionListener(eventHandlerTreeSelection);
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(renderer.getDefaultLeafIcon());
        tree.setCellRenderer(renderer);
        leftScrollPane.setViewportView(tree);
        tree.setEditable(true);
        editor = new DefaultTreeCellEditor(tree, renderer);
        editor.addCellEditorListener(new CellEditorListener() {

            public void editingStopped(ChangeEvent e)
            {
                String name = editor.getCellEditorValue().toString();
                if(name.equals("What's the time mister wolf?"))
                {
                    sdp.startThread();
                    tabbedPane.setSelectedComponent(sdp);
                } else
                {
                    sdp.stopThread();
                }
                getSelectedWorkspace().setReference(name);
                model.fireTreeNodesChanged(new TreeModelEvent(getSelectedWorkspace(), tree.getSelectionPath()));
            }

            public void editingCanceled(ChangeEvent changeevent)
            {
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
        tree.setCellEditor(editor);
    }

    private final void createTable()
    {
        table.setModel(model.getLibrary());
        table.setSelectionMode(0);
        ListSelectionModel lsm = table.getSelectionModel();
        lsm.addListSelectionListener(eventHandlerTableRowSelection);
        rightScrollPane.setViewportView(table);
        table.doLayout();
        table.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e)
            {
                try
                {
                    if(e.getClickCount() > 1)
                    {
                        Object value = table.getModel().getValueAt(table.getSelectedRow(), 1);
                        if(value instanceof IRSSVector)
                        {
                            IRSSVector r = (IRSSVector)table.getModel().getValueAt(table.getSelectedRow(), 1);
                            drssDialog.show(r.getEventVector().getEvents(), r.getReference());
                        } else
                        if(table.getModel().getValueAt(table.getSelectedRow(), 1) instanceof DRSSVector)
                        {
                            DRSSVector r = (DRSSVector)table.getModel().getValueAt(table.getSelectedRow(), 1);
                            drssDialog.show(r.getEventVector().getEvents(), r.getReference());
                        } else
                        if(table.getModel().getValueAt(table.getSelectedRow(), 1) instanceof Distribution)
                        {
                            Distribution dist = (Distribution)table.getModel().getValueAt(table.getSelectedRow(), 1);
                            boolean accept = dist == null ? distDialog.showDistributionDialog(MainWindow.STRINGLIST.getString("GENERAL_DISTRIBUTION_TITLE")) : distDialog.showDistributionDialog(dist, MainWindow.STRINGLIST.getString("GENERAL_DISTRIBUTION_TITLE"));
                            if(accept)
                                table.getModel().setValueAt(distDialog.getDistributionable(), table.getSelectedRow(), 1);
                        } else
                        if(table.getModel().getValueAt(table.getSelectedRow(), 1) instanceof Function)
                        {
                            if(editFunction.show((Function)table.getModel().getValueAt(table.getSelectedRow(), 1), (new StringBuilder()).append(MainWindow.STRINGLIST.getString("GENERAL_FUNCTION_TITLE")).append(table.getModel().getValueAt(table.getSelectedRow(), 0)).append(" [").append(table.getModel().getValueAt(table.getSelectedRow(), 2)).append("]").toString()))
                                table.getModel().setValueAt(editFunction.getFunction(), table.getSelectedRow(), 1);
                        } else
                        if(table.getModel().getValueAt(table.getSelectedRow(), 1) instanceof DiscreteFunction2)
                        {
                            if(editFunction2.show((Function2)table.getModel().getValueAt(table.getSelectedRow(), 1), (new StringBuilder()).append(MainWindow.STRINGLIST.getString("GENERAL_FUNCTION_TITLE")).append(table.getModel().getValueAt(table.getSelectedRow(), 0)).append(" [").append(table.getModel().getValueAt(table.getSelectedRow(), 2)).append("]").toString()))
                                table.getModel().setValueAt(editFunction2.getFunction(), table.getSelectedRow(), 1);
                        } else
                        if(table.getModel().getValueAt(table.getSelectedRow(), 1) instanceof PropagationModel)
                        {
                            if(propDialog.show((PropagationModel)table.getModel().getValueAt(table.getSelectedRow(), 1)))
                                table.getModel().setValueAt(propDialog.getPropagationModel(), table.getSelectedRow(), 1);
                        } else
                        if(table.getModel().getValueAt(table.getSelectedRow(), 1) instanceof CDMAResults)
                        {
                            capacityPanel.setData(selectedWorkspace.getCdmaResults().getCdmaInitialCapacity().getEvents(), selectedWorkspace.getCdmaResults().getCdmaInterferedCapacity().getEvents(), selectedWorkspace.getCdmaResults().getCdmaInitialOutage().getEvents(), selectedWorkspace.getCdmaResults().getCdmaInterferedOutage().getEvents(), selectedWorkspace.getCdmaResults().getCdmaTotalDroppedUsers().getEvents(), selectedWorkspace.getVictimSystemLink().getCDMASystem().getNumberOfCellSitesInPowerControlCluster() * selectedWorkspace.getVictimSystemLink().getCDMASystem().getUsersPerCell());
                            interferenceResultsDeck.show(interferenceResults, "cdma");
                            tabbedPane.setSelectedIndex(2);
                            tabbedPane.setTitleAt(2, "CDMA Capacity Results");
                        }
                    }
                }
                catch(Exception e1)
                {
                    MainWindow.LOG.error("An Error occured", e1);
                }
            }

            final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
        }
);
    }

    private static final String replaceVariables(String org, String txt, int no)
    {
        StringBuffer sb = new StringBuffer(org);
        String variable = (new StringBuilder()).append("%").append(no).toString();
        boolean end = false;
        while(!end) 
        {
            int idx = sb.toString().indexOf(variable);
            if(idx != -1)
            {
                sb.delete(idx, idx + variable.length());
                sb.insert(idx, txt);
            } else
            {
                end = true;
            }
        }
        return sb.toString();
    }

    public final void setAllButtonModelsEnabled(boolean value)
    {
        buttonModelWorkspace.setEnabled(value);
        buttonModelNewWorkspace.setEnabled(value);
        buttonModelRunEGE.setEnabled(value);
        buttonModelStopEGE.setEnabled(value);
        buttonModelResetEGE.setEnabled(value);
        buttonModelOpenWorkspace.setEnabled(value);
        buttonModelSaveWorkspace.setEnabled(value);
        buttonModelSaveWorkspaceAs.setEnabled(value);
        buttonModelCloseWorkspace.setEnabled(value);
        buttonModelImportLibrary.setEnabled(value);
        buttonModelExportLibrary.setEnabled(value);
        buttonModelConfiguration.setEnabled(value);
        buttonModelExpandTree.setEnabled(value);
        buttonModelCollapseTree.setEnabled(value);
        buttonModelVictimLink.setEnabled(value);
        buttonModelInterferingLinks.setEnabled(value);
        buttonModelCheckConsistency.setEnabled(value);
        buttonModelSimulationControl.setEnabled(value);
        buttonModelInterferenceCalculation.setEnabled(value);
        buttonModelReport.setEnabled(value);
        buttonModelGenerateMultipleInterferingSystems.setEnabled(value);
    }

    private final void updateButtonModels()
    {
        boolean workspaceIsSelected = getSelectedWorkspace() != null;
        boolean temp = model.getChildCount() > 1 && model.getChildCount() < 3;
        buttonModelNewWorkspace.setEnabled(true);
        buttonModelWorkspace.setEnabled(workspaceIsSelected || temp);
        buttonModelVictimLink.setEnabled(workspaceIsSelected || temp);
        buttonModelPostProcessing.setEnabled(workspaceIsSelected || temp);
        buttonModelInterferingLinks.setEnabled(workspaceIsSelected || temp);
        buttonModelCheckConsistency.setEnabled(workspaceIsSelected || temp);
        buttonModelSimulationControl.setEnabled(workspaceIsSelected || temp);
        buttonModelInterferenceCalculation.setEnabled(workspaceIsSelected || temp);
        buttonModelResetEGE.setEnabled(workspaceIsSelected || temp);
        buttonModelReport.setEnabled(workspaceIsSelected || temp);
        buttonModelRunEGE.setEnabled(workspaceIsSelected || temp);
        buttonModelStopEGE.setEnabled(workspaceIsSelected && getSelectedWorkspace().isEgeRunning());
        buttonModelTestPlotCDMAElements.setEnabled(getSelectedWorkspace() != null && getSelectedWorkspace().getVictimSystemLink().isCDMASystem());
        buttonModelSaveWorkspace.setEnabled(workspaceIsSelected || temp);
        buttonModelSaveWorkspaceAs.setEnabled(workspaceIsSelected || temp);
        if(!workspaceIsSelected && !temp)
            buttonModelCloseWorkspace.setRollover(false);
        buttonModelCloseWorkspace.setEnabled(workspaceIsSelected || temp);
        interferenceCalculationMenuItem.setEnabled(workspaceIsSelected && getSelectedWorkspace().isHasBeenCalculated());
        buttonModelResetEGE.setEnabled(workspaceIsSelected && getSelectedWorkspace().isHasBeenCalculated());
        buttonModelPlotFixedElements.setEnabled(workspaceIsSelected && !getSelectedWorkspace().isHasBeenCalculated());
        if(workspaceIsSelected && selectedWorkspace.getVictimSystemLink().isCDMASystem())
        {
            interferenceResultsDeck.show(interferenceResults, "cdma");
            tabbedPane.setTitleAt(2, "CDMA Capacity Results");
            if(selectedWorkspace.getEge().isInDebugMode())
                tabbedPane.setEnabledAt(4, true);
        } else
        {
            interferenceResultsDeck.show(interferenceResults, "interference");
            tabbedPane.setTitleAt(2, "Interference Calculations");
            tabbedPane.setEnabledAt(4, false);
        }
        tabbedPane.setEnabledAt(3, workspaceIsSelected && selectedWorkspace.hasCDMASubSystem());
        buttonModelRemoteCheckJob.setEnabled(true);
        buttonModelRemoteSendJob.setEnabled(workspaceIsSelected);
    }

    public static boolean displayScenarioCheckResults(java.util.List results, boolean displayIfOk, boolean askEge, boolean giveRemoteError, Component component)
    {
        boolean outcomeOk = true;
        StringBuffer messages = new StringBuffer();
        Iterator i = results.iterator();
        do
        {
            if(!i.hasNext())
                break;
            ScenarioCheckResult result = (ScenarioCheckResult)i.next();
            if(result.getOutcome() != org.seamcat.model.scenariocheck.ScenarioCheckResult.Outcome.OK)
            {
                outcomeOk &= false;
                Iterator i$ = result.getMessages().iterator();
                while(i$.hasNext()) 
                {
                    String message = (String)i$.next();
                    messages.append("<li><u>");
                    messages.append(result.getCheckName());
                    messages.append(":</u> ");
                    messages.append(message);
                    messages.append("</li>");
                }
            }
        } while(true);
        boolean runEge;
        if(!outcomeOk)
        {
            if(askEge)
            {
                int ans = JOptionPane.showOptionDialog(component, (new StringBuilder()).append(STRINGLIST.getString("CONSISTENCY_ERROR_PRE")).append(messages.toString()).append(STRINGLIST.getString("CONSISTENCY_ERROR_POST")).toString(), STRINGLIST.getString("CONSISTENCY_ERROR_TITLE"), 0, 2, null, new String[] {
                    STRINGLIST.getString("CONSISTENCY_YES_OPTION"), STRINGLIST.getString("CONSISTENCY_NO_OPTION")
                }, null);
                runEge = ans == 0;
            } else
            {
                JOptionPane.showMessageDialog(component, (new StringBuilder()).append(STRINGLIST.getString("CONSISTENCY_ERROR_PRE")).append(messages.toString()).append(giveRemoteError ? STRINGLIST.getString("REMOTE_CLIENT_CONSISTENCY_FAILED") : "").toString(), STRINGLIST.getString("CONSISTENCY_ERROR_TITLE"), 2);
                runEge = false;
            }
        } else
        {
            if(displayIfOk)
                JOptionPane.showMessageDialog(component, STRINGLIST.getString("CONSISTENCY_OK"), STRINGLIST.getString("CONSISTENCY_OK_TITLE"), 1);
            runEge = true;
        }
        return runEge;
    }

    public void setUsePostProcessing(boolean usePostProcessing)
    {
        this.usePostProcessing = usePostProcessing;
    }

    public SeamcatDistributionPlot getChartWindow()
    {
        return sdp;
    }

    public static final String ROOTID = "CLIENT_MODE";
    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/MainWindow);
    private HelpBroker helpBroker;
    private static final ResourceBundle STRINGLIST;
    private static final ResourceBundle HELPLIST;
    private DistributionDialog distDialog;
    private DialogFunctionDefine editFunction;
    private DialogFunction2Define editFunction2;
    private PropagationSelectDialog propDialog;
    private DistributionTestDialog distributionTest;
    private PropagationTestDialog propagationTest;
    private FunctionTestDialog functionTest;
    private Model model;
    private Workspace selectedWorkspace;
    private DefaultTreeCellEditor editor;
    private final JToolBar toolBar = new JToolBar();
    private final JPanel statusBar = new JPanel();
    private final JTabbedPane tabbedPane = new JTabbedPane(3);
    private final JScrollPane leftScrollPane = new JScrollPane();
    private final JScrollPane rightScrollPane = new JScrollPane();
    private JTree tree;
    private final NodeAttributeTable table = new NodeAttributeTable();
    private final JSplitPane splitPane;
    public static JFileChooser FILECHOOSER;
    private JPanel interferenceResults;
    private CardLayout interferenceResultsDeck;
    private ActivatedUsersGraph activityPanel;
    private static final String INTERFERENCE_CALCULATIONS = "interference";
    private static final String CDMA_CAPACITY = "cdma";
    private SeamcatDistributionPlot sdp;
    private CDMASystemPlotPanel cdmaPlot;
    private InterferenceCalculationsPanel itc;
    private CDMACapacityPanel capacityPanel;
    private JMenuItem interferenceCalculationMenuItem;
    private ServerStatusPanel serverStatusPanel;
    private static DialogReportOptions report;
    private DialogGenerateMultipleInterferingSystems dlgGenerateInterferingSystems;
    private DialogOptions dialogConfiguration;
    private SimulationControlDialog simulationControlDialog;
    public ImportLibraryConflictListener importLibraryConflictHandler;
    private final ButtonModel buttonModelWorkspace = new DefaultButtonModel();
    private final ButtonModel buttonModelNewWorkspace = new DefaultButtonModel();
    private final ButtonModel buttonModelRunEGE = new DefaultButtonModel();
    private final ButtonModel buttonModelStopEGE = new DefaultButtonModel();
    private final ButtonModel buttonModelResetEGE = new DefaultButtonModel();
    private final ButtonModel buttonModelOpenWorkspace = new DefaultButtonModel();
    private final ButtonModel buttonModelSaveWorkspace = new DefaultButtonModel();
    private final ButtonModel buttonModelSaveAllWorkspaces = new DefaultButtonModel();
    private final ButtonModel buttonModelSaveWorkspaceAs = new DefaultButtonModel();
    private final ButtonModel buttonModelCloseWorkspace = new DefaultButtonModel();
    private final ButtonModel buttonModelImportLibrary = new DefaultButtonModel();
    private final ButtonModel buttonModelExportLibrary = new DefaultButtonModel();
    private final ButtonModel buttonModelConfiguration = new DefaultButtonModel();
    private final ButtonModel buttonModelExpandTree = new DefaultButtonModel();
    private final ButtonModel buttonModelCollapseTree = new DefaultButtonModel();
    private final ButtonModel buttonModelVictimLink = new DefaultButtonModel();
    private final ButtonModel buttonModelPostProcessing = new DefaultButtonModel();
    private final ButtonModel buttonModelInterferingLinks = new DefaultButtonModel();
    private final ButtonModel buttonModelCheckConsistency = new DefaultButtonModel();
    private final ButtonModel buttonModelSimulationControl = new DefaultButtonModel();
    private final ButtonModel buttonModelInterferenceCalculation = new DefaultButtonModel();
    private final ButtonModel buttonModelReport = new DefaultButtonModel();
    private final ButtonModel buttonModelGenerateMultipleInterferingSystems = new DefaultButtonModel();
    private final ButtonModel buttonModelPlotFixedElements = new DefaultButtonModel();
    private final ButtonModel buttonModelTestPlotCDMAElements = new DefaultButtonModel();
    private final ButtonModel buttonModelLegacyImportLibrary = new DefaultButtonModel();
    private final ButtonModel buttonModelLegacyImportScenario = new DefaultButtonModel();
    private final ButtonModel buttonModelTestPropagationModel = new DefaultButtonModel();
    private final ButtonModel buttonModelTestDistributions = new DefaultButtonModel();
    private final ButtonModel buttonModelTestFunctions = new DefaultButtonModel();
    private final ButtonModel buttonModelTestCalculator = new DefaultButtonModel();
    private final ButtonModel buttonModelRemoteSendJob = new DefaultButtonModel();
    private final ButtonModel buttonModelRemoteCheckJob = new DefaultButtonModel();
    private final ButtonModel buttonModelStartServerMode = new DefaultButtonModel();
    private final ButtonModel buttonModelStopServerMode = new DefaultButtonModel();
    private JDialog antennaLibraryDialog;
    private JDialog transmitterLibraryDialog;
    private JDialog receiverLibraryDialog;
    private JDialog cdmaLinkLevelDataDialog;
    private JDialog propagationPluginsDialog;
    private JDialog batchLibraryDialog;
    private final Calculator calculator = new Calculator(this);
    private ClientSettingsDefinitionDialog clientSettingsDialog;
    private ClientRequestStatusDialog clientRequestStatusDialog;
    private ClientStatusDialog clientStatusDialog;
    private final CardLayout rootLayout = new CardLayout();
    private final JPanel root;
    private DialogDisplaySignal drssDialog;
    private InterferingLinkListDialog interferingLinkListDialog;
    private NewDialogVictimLink dialogVictimLink;
    private JLabel statusLabel;
    private boolean usePostProcessing;
    private PostProcessingPluginListDialog postProcessingListDialog;
    private PostProcessingPluginAssignmentDialog postProcessAssignment;
    private static final MainWindow singleton = new MainWindow();
    private final WindowListener eventHandlerWindow = new WindowListener() {

        public void windowActivated(WindowEvent windowevent)
        {
        }

        public void windowClosed(WindowEvent windowevent)
        {
        }

        public void windowClosing(WindowEvent e)
        {
            exit();
        }

        public void windowDeactivated(WindowEvent windowevent)
        {
        }

        public void windowDeiconified(WindowEvent windowevent)
        {
        }

        public void windowIconified(WindowEvent windowevent)
        {
        }

        public void windowOpened(WindowEvent windowevent)
        {
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final TreeSelectionListener eventHandlerTreeSelection = new TreeSelectionListener() {

        public void valueChanged(TreeSelectionEvent e)
        {
            table.setAutoCreateColumnsFromModel(true);
            TreePath treePath = e.getNewLeadSelectionPath();
            try
            {
                Object obj = treePath.getLastPathComponent();
                if(obj instanceof TableModel)
                {
                    table.setModel((TableModel)obj);
                    tabbedPane.setSelectedIndex(0);
                }
                obj = treePath.getPath()[1];
                if(obj instanceof Workspace)
                {
                    if(obj != selectedWorkspace)
                    {
                        sdp.resetStatistics();
                        sdp.clearAllElements();
                    }
                    selectedWorkspace = (Workspace)obj;
                    interferingLinkListDialog.setSelectedWorkspace(selectedWorkspace);
                } else
                {
                    selectedWorkspace = null;
                    interferingLinkListDialog.setSelectedWorkspace(null);
                }
            }
            catch(Exception e1)
            {
                selectedWorkspace = null;
            }
            updateButtonModels();
            if(selectedWorkspace != null)
            {
                itc.setWorkspace(selectedWorkspace);
                sdp.setWorkspaceName(selectedWorkspace.toString());
                sdp.setVictimSystemLinkName(selectedWorkspace.getVictimSystemLink().getReference());
                sdp.setNumberOfEvents(selectedWorkspace.getEge().getTotalNumberOfEventsGenerated());
                sdp.updateGuiComponents();
            }
            table.setAutoResizeMode(3);
            table.getColumnModel().getColumn(0).sizeWidthToFit();
            table.getColumnModel().getColumn(1).sizeWidthToFit();
            table.getColumnModel().getColumn(2).sizeWidthToFit();
            table.invalidate();
            table.doLayout();
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ListSelectionListener eventHandlerTableRowSelection = new ListSelectionListener() {

        public void valueChanged(ListSelectionEvent listselectionevent)
        {
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerRemoteSendJob = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            Workspace wks = getSelectedWorkspace();
            if(wks != null)
            {
                java.util.List results = ScenarioCheckUtils.checkWorkspace(wks);
                if(MainWindow.displayScenarioCheckResults(results, false, false, true, MainWindow.this) && clientSettingsDialog.showAndGenerateClientSetting())
                {
                    clientRequestStatusDialog.reset();
                    ClientSetting clientSettings = clientSettingsDialog.updateModel();
                    SeamcatClientSocket client = new SeamcatClientSocket(clientSettings, clientRequestStatusDialog);
                    ClientRequest req = new ClientRequest(clientSettings, "upload");
                    req.setWorkspace(wks);
                    client.sendRequest(req);
                    setCursor(Cursor.getPredefinedCursor(3));
                    clientRequestStatusDialog.setVisible(true);
                    clientSettings.setSendToServer(System.currentTimeMillis());
                    model.addClientSetting(clientSettings);
                    setCursor(Cursor.getPredefinedCursor(0));
                    int close = JOptionPane.showConfirmDialog(MainWindow.this, MainWindow.STRINGLIST.getString("REMOTE_CLOSE_WORKSPACE"), MainWindow.STRINGLIST.getString("REMOTE_CLOSE_WORKSPACE_TITLE"), 0, 1);
                    if(close == 0)
                        eventHandlerCloseWorkspace.actionPerformed(e);
                }
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerRemoteCheckJob = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            clientStatusDialog.showDialog(model.getClientSettings());
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerRemoteStartServer = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            model.getServerSettings().setUserMustLogin(false);
            WorkspaceProcessor wp = model.createWorkspaceProcessor();
            SeamcatServerSocket server = model.createServerSocket(wp);
            Appender app = serverStatusPanel.getAppender();
            app.setLayout(model.getLogFilePattern());
            Logger.getRootLogger().addAppender(app);
            Logger.getRootLogger().setLevel(Level.INFO);
            rootLayout.show(root, "SERVER_MODE");
            server.listenSocket();
            wp.startProcessor();
            buttonModelStopServerMode.setEnabled(true);
            buttonModelStartServerMode.setEnabled(false);
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerRemoteStopServer = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            Model.getSeamcatServerSocket().stopListening();
            Model.getWorkspaceProcessor().stopProcessor();
            rootLayout.show(root, "CLIENT_MODE");
            buttonModelStopServerMode.setEnabled(false);
            buttonModelStartServerMode.setEnabled(true);
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerClearBatchParameter = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            ListSelectionModel lsm = table.getSelectionModel();
            int selectedRowIndex;
            if(!lsm.isSelectionEmpty() && getSelectedWorkspace() != null)
                selectedRowIndex = lsm.getMinSelectionIndex();
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerNewWorkspace = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                Workspace workspace = Model.openDefaultWorkspace();
                model.getWorkspaces().add(workspace);
                TreePath treePath = new TreePath(new Object[] {
                    model, workspace
                });
                tree.grabFocus();
                tree.setSelectionPath(treePath);
                tree.scrollPathToVisible(treePath);
            }
            catch(Exception ex)
            {
                MainWindow.LOG.error("Error opening default workspace", ex);
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("NEW_WORKSPACE_DEFAULT_ERROR"), Model.getDefaultWorkspacePath(), 1), MainWindow.STRINGLIST.getString("NEW_WORKSPACE_DEFAULT_ERROR_TITLE"), 0);
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerRunEGE = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            (new Thread(new Runnable() {

                public void run()
                {
                    MainWindow.LOG.debug("Run EGE Button clicked - starting launch thread");
                    buttonModelRunEGE.setRollover(false);
                    Workspace wks = getSelectedWorkspace();
                    if(wks != null)
                    {
                        eventHandlerResetEGE.actionPerformed(new ActionEvent(this, -1, ""));
                        java.util.List results = ScenarioCheckUtils.checkWorkspace(wks);
                        if(MainWindow.displayScenarioCheckResults(results, false, true, false, _fld0))
                        {
                            if(wks.hasCDMASubSystem() && wks.getControl().getEgData().getNumberOfEvents() > 1000 && !wks.getControl().getEgData().isTimeLimited())
                            {
                                int ans = JOptionPane.showOptionDialog(_fld0, MainWindow.STRINGLIST.getString("CONSISTENCY_CDMA_EVENTS"), MainWindow.STRINGLIST.getString("CONSISTENCY_CDMA_EVENTS_TITLE"), 0, 2, null, null, null);
                                if(ans != 0)
                                    return;
                            }
                            MainWindow.LOG.debug("Changing cursor to Cursor.WAIT_CURSOR");
                            setCursor(Cursor.getPredefinedCursor(3));
                            sdp.clearAllElements();
                            tabbedPane.setSelectedIndex(1);
                            wks.getEge().addPositionListerner(sdp);
                            wks.getEge().addEventCompletionListerner(sdp);
                            wks.getEge().addEventCompletionListerner(new EventCompletionListener() {

                                public void eventCompleted(int i, int j)
                                {
                                }

                                public void updateStatus(String s)
                                {
                                }

                                public void eventGenerationCompleted(int count)
                                {
                                    generationAndEvaluationComplete();
                                }

                                public void startingEventGeneration(Workspace workspace, int eventsToBeCalculated, int eventStartIndex)
                                {
                                    selectedWorkspace = workspace;
                                    sdp.setCurrentProcessCompletionPercentage(0);
                                }

                                public void generationAndEvaluationComplete()
                                {
                                    itc.setWorkspace(selectedWorkspace);
                                    sdp.setCurrentProcessCompletionPercentage(100);
                                    if(selectedWorkspace.getEge().isInDebugMode())
                                        JOptionPane.showMessageDialog(_fld0, (new StringBuilder()).append(MainWindow.STRINGLIST.getString("EGE_LOGGING")).append(selectedWorkspace.getEge().getLogfile().getAbsolutePath()).toString());
                                    if(selectedWorkspace.getVictimSystemLink().isCDMASystem())
                                    {
                                        capacityPanel.setData(selectedWorkspace.getCdmaResults().getCdmaInitialCapacity().getEvents(), selectedWorkspace.getCdmaResults().getCdmaInterferedCapacity().getEvents(), selectedWorkspace.getCdmaResults().getCdmaInitialOutage().getEvents(), selectedWorkspace.getCdmaResults().getCdmaInterferedOutage().getEvents(), selectedWorkspace.getCdmaResults().getCdmaTotalDroppedUsers().getEvents(), selectedWorkspace.getVictimSystemLink().getCDMASystem().getNumberOfCellSitesInPowerControlCluster() * selectedWorkspace.getVictimSystemLink().getCDMASystem().getUsersPerCell());
                                        interferenceResultsDeck.show(interferenceResults, "cdma");
                                        tabbedPane.setSelectedIndex(2);
                                        tabbedPane.setTitleAt(2, "CDMA Capacity Results");
                                    } else
                                    {
                                        interferenceResultsDeck.show(interferenceResults, "interference");
                                        tabbedPane.setTitleAt(2, "Interference Calculations");
                                    }
                                    if(selectedWorkspace.hasCDMASubSystem())
                                    {
                                        tabbedPane.setEnabledAt(3, true);
                                        cdmaPlot.setWorkspace(selectedWorkspace);
                                        if(selectedWorkspace.getEge().isInDebugMode())
                                            tabbedPane.setEnabledAt(4, true);
                                    }
                                    MainWindow.LOG.debug("EGE reports that Generation and Evaluation is complete");
                                    setAllButtonModelsEnabled(true);
                                    interferenceCalculationMenuItem.setEnabled(true);
                                    MainWindow.LOG.debug("Changing Cursor to Cursor.DEFAULT_CURSOR");
                                    setCursor(Cursor.getPredefinedCursor(0));
                                    MainWindow.LOG.debug("Opening result branch of tree");
                                    TreePath tp = new TreePath(new Object[] {
                                        model, selectedWorkspace, selectedWorkspace.getResults(), selectedWorkspace.getSignals()
                                    });
                                    tree.expandPath(tp);
                                    tree.makeVisible(tp);
                                    tree.scrollPathToVisible(new TreePath(selectedWorkspace.getSignals()));
                                }

                                public void setCurrentProcessCompletionPercentage(int i)
                                {
                                }

                                public void setTotalProcessCompletionPercentage(int i)
                                {
                                }

                                public void incrementCurrentProcessCompletionPercentage(int i)
                                {
                                }

                                public void incrementTotalProcessCompletionPercentage(int i)
                                {
                                }

                                public void notifyError(String err)
                                {
                                    MainWindow.LOG.error(err);
                                }

                                final _cls1 this$2;

                        
                        {
                            this$2 = _cls1.this;
                            super();
                        }
                            }
);
                            wks.getEge().addExceptionListerner(new EgeExceptionListener() {

                                public boolean notifyException(Exception ex, int eventId)
                                {
                                    int response = JOptionPane.showConfirmDialog(_fld0, (new StringBuilder()).append("<html><b>Exception message:</b> ").append(ex.getMessage()).append("\n").append("Do you want to abort EGE?").toString(), (new StringBuilder()).append("Exception occurred in EGE on event #").append(eventId).toString(), 0, 0);
                                    return response == 0;
                                }

                                final _cls1 this$2;

                        
                        {
                            this$2 = _cls1.this;
                            super();
                        }
                            }
);
                            MainWindow.LOG.debug("Calling workspace to start EGE");
                            wks.getEge().setUseHigherPriorityThreads(model.isUseHigherPriorityThreads());
                            wks.startEventGeneration();
                            MainWindow.LOG.debug("Disabling all button models");
                            setAllButtonModelsEnabled(false);
                            buttonModelStopEGE.setEnabled(true);
                        }
                    } else
                    {
                        MainWindow.LOG.debug("No workspace is selected -> EGE NOT started");
                        JOptionPane.showMessageDialog(_fld0, "Specific workspace MUST be selected before starting EGE");
                    }
                    MainWindow.LOG.debug("Launch thread has finished");
                }

                final _cls24 this$1;

                    
                    {
                        this$1 = _cls24.this;
                        super();
                    }
            }
)).start();
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerStopEGE = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            if(getSelectedWorkspace() != null)
            {
                if(!getSelectedWorkspace().getEge().isStopped())
                {
                    getSelectedWorkspace().stopEventGeneration();
                    JOptionPane.showMessageDialog(MainWindow.this, "Stopping EGE after completion of the current event");
                    setCursor(Cursor.getDefaultCursor());
                }
            } else
            {
                JOptionPane.showMessageDialog(MainWindow.this, "Specific workspace MUST be selected before EGE can be stopped");
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerResetEGE = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            try
            {
                if(selectedWorkspace != null)
                {
                    Workspace ws = selectedWorkspace;
                    selectedWorkspace.resetEventGeneration();
                    Model.getInstance().updateTreeListeners(new ListDataEvent(tree.getModel(), 0, 0, 0));
                    sdp.clearAllElements();
                    sdp.setCurrentProcessCompletionPercentage(0);
                    sdp.setTotalProcessCompletionPercentage(0);
                    sdp.updateStatus("Event Generation Not started");
                    sdp.resetStatistics();
                    capacityPanel.resetStatistics();
                    cdmaPlot.reset();
                    tabbedPane.setSelectedIndex(0);
                    interferenceResultsDeck.show(interferenceResults, "interference");
                    tabbedPane.setTitleAt(2, "Interference Calculations");
                    tree.setSelectionRow(tree.getModel().getIndexOfChild(tree.getModel().getRoot(), ws));
                } else
                {
                    JOptionPane.showMessageDialog(MainWindow.this, "Select a specific workspace before reseting generated events");
                }
            }
            catch(Exception ex)
            {
                MainWindow.LOG.error("Error reseting simulation", ex);
            }
            updateButtonModels();
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerInterferenceCalculation = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            tabbedPane.setSelectedIndex(2);
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerOpenWorkspace = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            MainWindow.FILECHOOSER.setFileFilter(MainWindow.FILE_FILTER_WORKSPACE);
            MainWindow.FILECHOOSER.setMultiSelectionEnabled(false);
            MainWindow.FILECHOOSER.setDialogTitle("Open Workspace");
            int returnValue = MainWindow.FILECHOOSER.showOpenDialog(MainWindow.this);
            if(returnValue == 0)
                try
                {
                    setCursor(Cursor.getPredefinedCursor(3));
                    Workspace workspace = Model.openWorkspace(MainWindow.FILECHOOSER.getSelectedFile());
                    model.getWorkspaces().add(workspace);
                    TreePath treePath = new TreePath(new Object[] {
                        model, workspace
                    });
                    tree.grabFocus();
                    tree.setSelectionPath(treePath);
                    tree.scrollPathToVisible(treePath);
                    setCursor(Cursor.getDefaultCursor());
                    if(workspace.isHasBeenCalculated())
                        JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("OPEN_WORKSPACE_HAS_RESULTS"), MainWindow.STRINGLIST.getString("OPEN_WORKSPACE_HAS_RESULTS_TITLE"), 1);
                }
                catch(Exception iex1)
                {
                    setCursor(Cursor.getDefaultCursor());
                    int response = JOptionPane.showConfirmDialog(MainWindow.this, MainWindow.STRINGLIST.getString("OPEN_WORKSPACE_ERROR"), MainWindow.STRINGLIST.getString("OPEN_WORKSPACE_ERROR_TITLE"), 0, 0);
                    if(response == 0)
                        try
                        {
                            setCursor(Cursor.getPredefinedCursor(3));
                            Workspace workspace = Model.openBrokenWorkspace(MainWindow.FILECHOOSER.getSelectedFile());
                            model.getWorkspaces().add(workspace);
                            TreePath treePath = new TreePath(new Object[] {
                                model, workspace
                            });
                            tree.grabFocus();
                            tree.setSelectionPath(treePath);
                            tree.scrollPathToVisible(treePath);
                            setCursor(Cursor.getDefaultCursor());
                            JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("OPEN_WORKSPACE_BROKEN_OK"), MainWindow.STRINGLIST.getString("OPEN_WORKSPACE_BROKEN_OK_TITLE"), 1);
                        }
                        catch(Exception ex)
                        {
                            JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("OPEN_WORKSPACE_BROKEN_ERROR"), MainWindow.STRINGLIST.getString("OPEN_WORKSPACE_ERROR_TITLE"), 0);
                        }
                    setCursor(Cursor.getDefaultCursor());
                    MainWindow.LOG.error("Error opening workspace", iex1);
                }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerImportLibrary = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            MainWindow.FILECHOOSER.setFileFilter(MainWindow.FILE_FILTER_LIBRARY);
            MainWindow.FILECHOOSER.setMultiSelectionEnabled(false);
            MainWindow.FILECHOOSER.setDialogTitle("Import library");
            int returnValue = MainWindow.FILECHOOSER.showOpenDialog(MainWindow.this);
            if(returnValue == 0)
                try
                {
                    Model.getInstance().getLibrary().importLibrary(MainWindow.FILECHOOSER.getSelectedFile(), importLibraryConflictHandler);
                    Model.getInstance().persist();
                    JOptionPane.showMessageDialog(MainWindow.this, MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("IMPORT_LIBRARY_OK"), MainWindow.FILECHOOSER.getSelectedFile().getAbsolutePath(), 1), MainWindow.STRINGLIST.getString("IMPORT_LIBRARY_OK_TITLE"), 1);
                }
                catch(Exception exception)
                {
                    exception.printStackTrace(System.out);
                    JOptionPane.showMessageDialog(MainWindow.this, "Invalid file", "Error", 0);
                }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerLegacyImportLibrary = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            MainWindow.FILECHOOSER.setDialogTitle(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_LIBRARY_FILE_TITLE"));
            MainWindow.FILECHOOSER.removeChoosableFileFilter(MainWindow.FILE_FILTER_LIBRARY);
            MainWindow.FILECHOOSER.removeChoosableFileFilter(MainWindow.FILE_FILTER_WORKSPACE);
            MainWindow.FILECHOOSER.addChoosableFileFilter(MainWindow.FILE_FILTER_LEGACY);
            int response = MainWindow.FILECHOOSER.showOpenDialog(MainWindow.this);
            if(response == 0)
            {
                setCursor(Cursor.getPredefinedCursor(3));
                LogLineCatcher logcatcher = new LogLineCatcher(Level.WARN);
                Logger.getLogger("org.seamcat.importer").addAppender(logcatcher);
                try
                {
                    ImportedLibrary impLibrary = LegacySeamcatConverter.importLibrary(new BufferedReader(new FileReader(MainWindow.FILECHOOSER.getSelectedFile())));
                    Library lib = model.getLibrary();
                    Antenna ant[] = impLibrary.getAntennas();
                    for(int i = 0; i < ant.length; i++)
                    {
                        if(!lib.getAntennas().containsReference(ant[i].getReference()));
                        lib.getAntennas().add(ant[i]);
                    }

                    org.seamcat.model.Receiver rec[] = impLibrary.getReceivers();
                    for(int i = 0; i < rec.length; i++)
                        model.getLibrary().getReceivers().add(rec[i]);

                    org.seamcat.model.Transmitter tra[] = impLibrary.getTransmitters();
                    for(int i = 0; i < tra.length; i++)
                        model.getLibrary().getTransmitters().add(tra[i]);

                    Logger.getLogger("org.seamcat.importer").removeAppender(logcatcher);
                    ArrayList warnings = logcatcher.getCatchedLines();
                    if(warnings.size() > 0)
                    {
                        StringBuffer str = new StringBuffer();
                        str.append(MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_LIBRARY_WARNING_PREFIX"), MainWindow.FILECHOOSER.getSelectedFile().getName(), 1));
                        String s;
                        for(Iterator i$ = warnings.iterator(); i$.hasNext(); str.append((new StringBuilder()).append("<li>").append(s).append("</li>").toString()))
                            s = (String)i$.next();

                        str.append(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_LIBRARY_WARNING_POSTFIX"));
                        JOptionPane.showMessageDialog(MainWindow.this, str.toString(), MainWindow.STRINGLIST.getString("IMPORT_LEGACY_LIBRARY_TITLE_WARNING"), 2);
                    } else
                    {
                        JOptionPane.showMessageDialog(MainWindow.this, (new StringBuilder()).append(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_LIBRARY")).append(MainWindow.FILECHOOSER.getSelectedFile().getName()).toString(), MainWindow.STRINGLIST.getString("IMPORT_LEGACY_LIBRARY_TITLE"), 1);
                    }
                }
                catch(Exception ex)
                {
                    MainWindow.LOG.error("Error during import", ex);
                    JOptionPane.showMessageDialog(MainWindow.this, (new StringBuilder()).append(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_LIBRARY_ERROR")).append(MainWindow.FILECHOOSER.getSelectedFile().getName()).toString(), MainWindow.STRINGLIST.getString("IMPORT_LEGACY_LIBRARY_ERROR_TITLE"), 0);
                }
            }
            setCursor(Cursor.getDefaultCursor());
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerLegacyImportScenario = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            MainWindow.FILECHOOSER.setDialogTitle(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_SCENARIO_FILE_TITLE"));
            MainWindow.FILECHOOSER.removeChoosableFileFilter(MainWindow.FILE_FILTER_LIBRARY);
            MainWindow.FILECHOOSER.removeChoosableFileFilter(MainWindow.FILE_FILTER_WORKSPACE);
            MainWindow.FILECHOOSER.addChoosableFileFilter(MainWindow.FILE_FILTER_LEGACY);
            int response = MainWindow.FILECHOOSER.showOpenDialog(MainWindow.this);
            if(response == 0)
            {
                try
                {
                    setCursor(Cursor.getPredefinedCursor(3));
                    LogLineCatcher logcatcher = new LogLineCatcher(Level.WARN);
                    Logger.getLogger("org.seamcat.importer").addAppender(logcatcher);
                    ImportedScenario impScenario = LegacySeamcatConverter.importScenario(new BufferedReader(new FileReader(MainWindow.FILECHOOSER.getSelectedFile())));
                    Workspace ws = new Workspace();
                    ws.removeInterferingSystemLink((InterferenceLink)ws.getInterferenceLinks().get(0));
                    ws.setReference((new StringBuilder()).append("Workspace from ").append(MainWindow.FILECHOOSER.getSelectedFile().getName()).toString());
                    ws.setVictimSystemLink(impScenario.getVictimSystemLink());
                    ws.getVictimSystemLink().updateNodeAttributes();
                    InterferenceLink ilks[] = impScenario.getInterferenceLinks();
                    int i = 0;
                    for(int stop = ilks.length; i < stop; i++)
                    {
                        ws.addInterferingSystemLink(ilks[i]);
                        ilks[i].updateNodeAttributes();
                    }

                    model.getWorkspaces().add(ws);
                    resetTree();
                    TreePath tp = new TreePath(new Object[] {
                        model, ws, ws.getScenario()
                    });
                    tree.expandPath(tp);
                    tree.makeVisible(tp);
                    Logger.getLogger("org.seamcat.importer").removeAppender(logcatcher);
                    ArrayList warnings = logcatcher.getCatchedLines();
                    if(warnings.size() > 0)
                    {
                        StringBuffer str = new StringBuffer();
                        str.append(MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_SCENARIO_WARNING_PREFIX"), MainWindow.FILECHOOSER.getSelectedFile().getName(), 1));
                        String s;
                        for(Iterator i$ = warnings.iterator(); i$.hasNext(); str.append((new StringBuilder()).append("<li>").append(s).append("</li>").toString()))
                            s = (String)i$.next();

                        str.append(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_SCENARIO_WARNING_POSTFIX"));
                        JOptionPane.showMessageDialog(MainWindow.this, str.toString(), MainWindow.STRINGLIST.getString("IMPORT_LEGACY_SCENARIO_TITLE_WARNING"), 2);
                    } else
                    {
                        JOptionPane.showMessageDialog(MainWindow.this, (new StringBuilder()).append(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_SCENARIO")).append(MainWindow.FILECHOOSER.getSelectedFile().getName()).toString(), MainWindow.STRINGLIST.getString("IMPORT_LEGACY_SCENARIO_TITLE"), 1);
                    }
                }
                catch(Exception ex)
                {
                    MainWindow.LOG.error("Error during import", ex);
                    JOptionPane.showMessageDialog(MainWindow.this, (new StringBuilder()).append(MainWindow.STRINGLIST.getString("IMPORT_LEGACY_SCENARIO_ERROR")).append(MainWindow.FILECHOOSER.getSelectedFile().getName()).toString(), MainWindow.STRINGLIST.getString("IMPORT_LEGACY_SCENARIO_ERROR_TITLE"), 0);
                }
                setCursor(Cursor.getDefaultCursor());
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerExportLibrary = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            MainWindow.FILECHOOSER.setFileFilter(MainWindow.FILE_FILTER_LIBRARY);
            MainWindow.FILECHOOSER.setMultiSelectionEnabled(false);
            MainWindow.FILECHOOSER.setDialogTitle("Export library");
            int returnValue = MainWindow.FILECHOOSER.showSaveDialog(MainWindow.this);
            if(returnValue == 0)
                try
                {
                    File f = MainWindow.FILECHOOSER.getSelectedFile();
                    if(!f.getName().toUpperCase().endsWith(".SLI"))
                        f = new File((new StringBuilder()).append(f.getAbsolutePath()).append(".sli").toString());
                    Model.getInstance().getLibrary().exportLibrary(f);
                    JOptionPane.showMessageDialog(MainWindow.this, MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("SAVE_LIBRARY_DONE"), f.getAbsolutePath(), 1), MainWindow.STRINGLIST.getString("SAVE_LIBRARY_DONE_TITLE"), 1);
                }
                catch(Exception exception)
                {
                    JOptionPane.showMessageDialog(MainWindow.this, "File error", "Error", 0);
                }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerSaveAllWorkspaces = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            Iterator i$ = model.getWorkspaces().iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                Workspace w = (Workspace)i$.next();
                if(!w.getIsSaved())
                {
                    selectedWorkspace = w;
                    eventHandlerSaveWorkspace.actionPerformed(new ActionEvent(this, -1, ""));
                }
            } while(true);
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerSaveWorkspace = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            if(getSelectedWorkspace() == null)
            {
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("SAVE_NO_WORKSPACE_SELECTED"), MainWindow.STRINGLIST.getString("SAVE_NO_WORKSPACE_SELECTED_TITLE"), 0);
                return;
            }
            Workspace ws = getSelectedWorkspace();
            String filename = ws.getReference();
            if(getSelectedWorkspace() != null)
                if(!Model.checkFilename(filename))
                {
                    JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILENAME_ERROR"), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILENAME_ERROR_TITLE"), 0);
                } else
                {
                    boolean override = false;
                    File outputURL = new File((new StringBuilder()).append(Model.getInstance().getSeamcatHome().getAbsolutePath()).append(File.separator).append("workspaces").append(File.separator).toString());
                    if(!outputURL.exists())
                        outputURL.mkdirs();
                    File file = new File(outputURL, (new StringBuilder()).append(filename).append(filename.toLowerCase().endsWith(".sws") ? "" : ".sws").toString());
                    if(ws.getAbsoluteLocation() != null)
                    {
                        file = new File(ws.getAbsoluteLocation());
                        override = true;
                    }
                    if(file.exists() && !override)
                    {
                        int response = JOptionPane.showOptionDialog(MainWindow.this, MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS"), file.getAbsolutePath(), 1), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_TITLE"), 1, 2, null, new String[] {
                            MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_OVERRIDE"), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_RENAME"), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_CANCEL")
                        }, MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_RENAME"));
                        if(response == 1)
                        {
                            eventHandlerSaveWorkspaceAs.actionPerformed(e);
                            return;
                        }
                        if(response == 2)
                            return;
                    }
                    boolean saveResults = true;
                    if(ws.getEge().getTotalNumberOfEventsGenerated() > 0)
                    {
                        int response = JOptionPane.showConfirmDialog(MainWindow.this, "The workspace contains results - do you wish to save these?", "Results exist in workspace", 0, 1);
                        saveResults = response == 0;
                    }
                    setCursor(Cursor.getPredefinedCursor(3));
                    Model.saveWorkspace(ws, file, saveResults);
                    setCursor(Cursor.getDefaultCursor());
                    JOptionPane.showMessageDialog(MainWindow.this, MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_DONE"), file.getAbsolutePath(), 1), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_DONE_TITLE"), 1);
                }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerSaveWorkspaceAs = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            if(getSelectedWorkspace() == null)
            {
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("SAVE_NO_WORKSPACE_SELECTED"), MainWindow.STRINGLIST.getString("SAVE_NO_WORKSPACE_SELECTED_TITLE"), 0);
                return;
            }
            MainWindow.FILECHOOSER.setFileFilter(MainWindow.FILE_FILTER_DEFAULT_WORKSPACE);
            MainWindow.FILECHOOSER.addChoosableFileFilter(MainWindow.FILE_FILTER_WORKSPACE);
            MainWindow.FILECHOOSER.setDialogTitle("Save Workspace As");
            int returnValue = MainWindow.FILECHOOSER.showSaveDialog(MainWindow.this);
            if(returnValue == 0)
            {
                File file = MainWindow.FILECHOOSER.getSelectedFile();
                if(MainWindow.FILECHOOSER.getFileFilter() == MainWindow.FILE_FILTER_DEFAULT_WORKSPACE)
                {
                    int response = JOptionPane.showConfirmDialog(MainWindow.this, "Overide current default workspace? All new workspaces will be created on this basis.", "Override?", 0, 1);
                    if(response != 0)
                        return;
                } else
                if(!file.getName().toLowerCase().endsWith(".sws") && MainWindow.FILECHOOSER.getFileFilter() == MainWindow.FILE_FILTER_WORKSPACE)
                    file = new File(file.getParentFile(), (new StringBuilder()).append(file.getName()).append(".sws").toString());
                if(file.exists())
                {
                    int response = JOptionPane.showOptionDialog(MainWindow.this, MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS"), file.getAbsolutePath(), 1), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_TITLE"), 1, 2, null, new String[] {
                        MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_OVERRIDE"), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_RENAME"), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_CANCEL")
                    }, MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_FILE_EXISTS_RENAME"));
                    if(response == 1)
                    {
                        actionPerformed(e);
                        return;
                    }
                    if(response == 2)
                        return;
                }
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                getSelectedWorkspace().setReference(name);
                boolean saveResults = true;
                if(getSelectedWorkspace().getEge().getTotalNumberOfEventsGenerated() > 0)
                {
                    int response = JOptionPane.showConfirmDialog(MainWindow.this, "The workspace contains results - do you wish to save these?", "Results exist in workspace", 0, 1);
                    saveResults = response == 0;
                }
                Model.saveWorkspace(getSelectedWorkspace(), file, saveResults);
                getSelectedWorkspace().setAbsoluteLocation(file.getAbsolutePath());
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_DONE"), file.getAbsolutePath(), 1), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_DONE_TITLE"), 1);
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerCloseWorkspace = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            Workspace ws = null;
            if(getSelectedWorkspace() == null)
            {
                if(model.getChildCount() > 1)
                {
                    tree.setSelectionRow(0);
                    ws = (Workspace)model.getWorkspaces().getElementAt(0);
                }
            } else
            {
                ws = getSelectedWorkspace();
            }
            if(ws != null && !ws.getIsSaved())
            {
                int shouldBeSaved = JOptionPane.showConfirmDialog(MainWindow.this, MainWindow.replaceVariables(MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_ON_EXIT"), ws.getReference(), 1), MainWindow.STRINGLIST.getString("SAVE_WORKSPACE_ON_EXIT_TITLE"), 1, 1);
                if(shouldBeSaved == 2)
                    return;
                if(shouldBeSaved == 0)
                {
                    selectedWorkspace = ws;
                    eventHandlerSaveWorkspace.actionPerformed(new ActionEvent(this, -1, ""));
                }
            }
            tree.setSelectionRow(tree.getRowForPath(new TreePath(new Object[] {
                model, ws
            })) - 1);
            model.getWorkspaces().remove(ws);
            sdp.clearAllElements();
            sdp.setWorkspaceName("No workspace selected");
            tabbedPane.setSelectedIndex(0);
            updateButtonModels();
            ws.close();
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerConfiguration = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            dialogConfiguration.setVisible(true);
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerExit = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            exit();
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerExpandTree = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            int oldCount = 0;
            for(int count = tree.getRowCount(); oldCount != count; count = tree.getRowCount())
            {
                for(int i = count; i >= 0; i--)
                    try
                    {
                        if(!tree.isExpanded(i))
                            tree.expandRow(i);
                    }
                    catch(Exception exception) { }

                oldCount = count;
            }

        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerCollapseTree = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            int oldCount = 0;
            for(int count = tree.getRowCount(); oldCount != count;)
            {
                for(int i = count; i >= 0; i--)
                    try
                    {
                        if(tree.isExpanded(i))
                            tree.collapseRow(i);
                    }
                    catch(Exception e1) { }

                oldCount = count;
                count = tree.getRowCount();
                resetTree();
            }

        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerHelp = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            helpBroker.setViewDisplayed(true);
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerBatch = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            batchLibraryDialog.setVisible(true);
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerSimulationControl = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            Workspace ws = getSelectedWorkspace();
            if(ws != null)
            {
                if(simulationControlDialog.show(ws.getControl().getEgData()))
                    simulationControlDialog.updateModel(ws.getControl().getEgData());
            } else
            {
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("NO_WORKSPACE_SELECTED"));
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerInterferingLink = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            if(getSelectedWorkspace() != null)
            {
                interferingLinkListDialog.setVisible(true);
                getSelectedWorkspace().setIsSaved(false);
                resetTree();
            } else
            {
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("NO_WORKSPACE_SELECTED"));
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerVictimLink = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            if(getSelectedWorkspace() != null)
            {
                if(dialogVictimLink.show(getSelectedWorkspace().getVictimSystemLink()))
                    dialogVictimLink.updateModel(getSelectedWorkspace().getVictimSystemLink());
                getSelectedWorkspace().setIsSaved(false);
                resetTree();
            } else
            {
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("NO_WORKSPACE_SELECTED"));
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerPostProcessing = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            if(getSelectedWorkspace() != null)
            {
                postProcessAssignment.show(getSelectedWorkspace());
                getSelectedWorkspace().setIsSaved(false);
                resetTree();
            } else
            {
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("NO_WORKSPACE_SELECTED"));
            }
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerViewToolBar = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)e.getSource();
            toolBar.setVisible(menuItem.isSelected());
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerViewStatusBar = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem)e.getSource();
            statusBar.setVisible(menuItem.isSelected());
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerCheckConsistency = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            Workspace wks = getSelectedWorkspace();
            if(wks != null)
                MainWindow.displayScenarioCheckResults(ScenarioCheckUtils.checkWorkspace(wks), true, false, false, MainWindow.this);
            else
                JOptionPane.showMessageDialog(MainWindow.this, MainWindow.STRINGLIST.getString("NO_WORKSPACE_SELECTED"), "Warning", 2);
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private ActionListener eventHandlerPlotFixedElements;
    private final ActionListener eventHandlerReport = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            MainWindow.report.show(getSelectedWorkspace());
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private final ActionListener eventHandlerGenerateMultipleInterferingSystems = new ActionListener() {

        public void actionPerformed(ActionEvent e)
        {
            dlgGenerateInterferingSystems.setLocationRelativeTo(MainWindow.this);
            dlgGenerateInterferingSystems.setVisible(true);
            dlgGenerateInterferingSystems.setWorkspace(getSelectedWorkspace());
        }

        final MainWindow this$0;

            
            {
                this$0 = MainWindow.this;
                super();
            }
    }
;
    private static final FileFilter FILE_FILTER_LEGACY = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String extension = "";
                    String name = file.getName();
                    int i = name.lastIndexOf('.');
                    if(i > 0 && i < name.length() - 1)
                        extension = name.substring(i + 1).toLowerCase();
                    returnValue = extension.equalsIgnoreCase("TXT");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "SEAMCAT 2 Files";
        }

    }
;
    public static final FileFilter FILE_FILTER_WORKSPACE = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String extension = "";
                    String name = file.getName();
                    int i = name.lastIndexOf('.');
                    if(i > 0 && i < name.length() - 1)
                        extension = name.substring(i + 1).toLowerCase();
                    returnValue = extension.equalsIgnoreCase("SWS");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "SEAMCAT Workspace Files";
        }

    }
;
    public static final FileFilter FILE_FILTER_BATCH = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String extension = "";
                    String name = file.getName();
                    int i = name.lastIndexOf('.');
                    if(i > 0 && i < name.length() - 1)
                        extension = name.substring(i + 1).toLowerCase();
                    returnValue = extension.equalsIgnoreCase("SBJ");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "SEAMCAT Batch Files";
        }

    }
;
    public static final FileFilter FILE_FILTER_XML = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String extension = "";
                    String name = file.getName();
                    int i = name.lastIndexOf('.');
                    if(i > 0 && i < name.length() - 1)
                        extension = name.substring(i + 1).toLowerCase();
                    returnValue = extension.equalsIgnoreCase("XML");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "XML Files";
        }

    }
;
    public static final FileFilter FILE_FILTER_XSL = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String extension = "";
                    String name = file.getName();
                    int i = name.lastIndexOf('.');
                    if(i > 0 && i < name.length() - 1)
                        extension = name.substring(i + 1).toLowerCase();
                    returnValue = extension.equalsIgnoreCase("XSL");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "Stylesheet Files (.xsl)";
        }

    }
;
    public static final FileFilter FILE_FILTER_DEFAULT_WORKSPACE = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String name = file.getName();
                    returnValue = name.equalsIgnoreCase("default-workspace.xml");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "Default SEAMCAT Workspace";
        }

    }
;
    public static final FileFilter FILE_FILTER_LIBRARY = new FileFilter() {

        public boolean accept(File file)
        {
            boolean returnValue = false;
            if(file != null)
                if(file.isDirectory())
                {
                    returnValue = true;
                } else
                {
                    String extension = "";
                    String name = file.getName();
                    int i = name.lastIndexOf('.');
                    if(i > 0 && i < name.length() - 1)
                        extension = name.substring(i + 1).toLowerCase();
                    returnValue = extension.equalsIgnoreCase("SLI");
                }
            return returnValue;
        }

        public String getDescription()
        {
            return "SEAMCAT Library Files";
        }

    }
;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        HELPLIST = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }





























































}
