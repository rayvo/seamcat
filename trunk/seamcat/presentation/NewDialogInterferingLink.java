// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NewDialogInterferingLink.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import org.seamcat.Seamcat;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.cdma.presentation.CDMAPanel;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.*;
import org.seamcat.model.core.*;
import org.seamcat.presentation.components.AntennaDefinitionPanel;
import org.seamcat.presentation.components.AntennaPointingPanel;
import org.seamcat.presentation.components.CoverageRadiusPanel;
import org.seamcat.presentation.components.EmissionCharacteristicsPanel;
import org.seamcat.presentation.components.IdentificationPanel;
import org.seamcat.presentation.components.InterferersDensityPanel;
import org.seamcat.presentation.components.InterferingDefinitionPanel;
import org.seamcat.presentation.components.InterferingTransmitterChangeListener;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.PowerControlPanel;
import org.seamcat.presentation.components.PropagationSelectPanel;
import org.seamcat.presentation.components.RelativeLocationInterferingPanel;
import org.seamcat.presentation.components.RelativeLocationPanel;
import org.seamcat.presentation.components.SensitivityPanel;
import org.seamcat.presentation.components.WantedReceiverChangeListener;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public class NewDialogInterferingLink extends EscapeDialog
{
    public class ControlPanel extends NavigateButtonPanel
    {

        public void btnOkActionPerformed()
        {
            accept = true;
            hide();
        }

        public void btnCancelActionPerformed()
        {
            hide();
        }

        final NewDialogInterferingLink this$0;

        public ControlPanel()
        {
            this$0 = NewDialogInterferingLink.this;
            super();
        }
    }


    public NewDialogInterferingLink(JDialog owner)
    {
        super(owner, true);
        main = new JTabbedPane();
        generalPane = new JPanel();
        interferingTransmitterPane = new JTabbedPane(3);
        wantedReceiverPane = new JTabbedPane(3);
        itWrPathPane = new JTabbedPane(3);
        itVrPathPane = new JTabbedPane(3);
        controls = new ControlPanel();
        this.owner = owner;
        generalIdentification = new IdentificationPanel();
        generalDefinition = new InterferingDefinitionPanel(this);
        generalDefinition.addCDMASystemActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                main.setEnabledAt(1, generalDefinition.cdmaSystemSelected());
                main.setEnabledAt(2, !generalDefinition.cdmaSystemSelected());
                main.setEnabledAt(3, !generalDefinition.cdmaSystemSelected());
                main.setEnabledAt(4, !generalDefinition.cdmaSystemSelected());
                itVrLocation.setCdmaMode(generalDefinition.cdmaSystemSelected());
                interferenceLink.setIsCDMASystem(generalDefinition.cdmaSystemSelected());
                if(generalDefinition.cdmaSystemSelected())
                    cdma.setCdmasystem(interferenceLink.getCDMASystem());
            }

            final NewDialogInterferingLink this$0;

            
            {
                this$0 = NewDialogInterferingLink.this;
                super();
            }
        }
);
        generalPane.setLayout(new BoxLayout(generalPane, 0));
        generalPane.add(generalIdentification);
        generalPane.add(generalDefinition);
        interferingIdentification = new IdentificationPanel();
        interferingAntennaPoint = new AntennaPointingPanel(this);
        interferingEmission = new EmissionCharacteristicsPanel(this);
        interferingPowerControl = new PowerControlPanel(this);
        interferingAntenna = new AntennaDefinitionPanel(this);
        JPanel interPanel = new JPanel(new GridLayout(2, 2));
        generalDefinition.addInterferingTransmitterChangeListener(new InterferingTransmitterChangeListener() {

            public void interferingTransmitterChanged(InterferingTransmitter newIT)
            {
                interferingIdentification.setComponent(newIT);
                interferingEmission.setTransmitter(newIT);
                interferingAntennaPoint.set(interferenceLink.getInterferingLink(), newIT);
                interferingPowerControl.setPowerControl(newIT.getPowerControl());
                interferingAntenna.setAntenna(newIT.getAntenna(), false);
            }

            final NewDialogInterferingLink this$0;

            
            {
                this$0 = NewDialogInterferingLink.this;
                super();
            }
        }
);
        interPanel.add(interferingIdentification);
        interPanel.add(interferingAntennaPoint);
        interPanel.add(interferingEmission);
        interPanel.add(interferingPowerControl);
        interferingEmission.addPowerListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                interferingPowerControl.setPowerControlEnabled(((JCheckBox)e.getSource()).isSelected());
            }

            final NewDialogInterferingLink this$0;

            
            {
                this$0 = NewDialogInterferingLink.this;
                super();
            }
        }
);
        interferingTransmitterPane.addTab("General", interPanel);
        interferingTransmitterPane.addTab("Antenna", interferingAntenna);
        receiverIdentification = new IdentificationPanel();
        receiverAntennaPoint = new AntennaPointingPanel(this);
        receiverSensitivity = new SensitivityPanel(this);
        receiverAntenna = new AntennaDefinitionPanel(this);
        generalDefinition.addWantedReceiverChangeListener(new WantedReceiverChangeListener() {

            public void wantedReceiverChanged(WantedReceiver newWT)
            {
                receiverIdentification.setComponent(newWT);
                receiverAntennaPoint.set(interferenceLink.getInterferingLink(), newWT);
                receiverSensitivity.setWantedReceiver(newWT);
                receiverAntenna.setAntenna(newWT.getAntenna(), false);
            }

            final NewDialogInterferingLink this$0;

            
            {
                this$0 = NewDialogInterferingLink.this;
                super();
            }
        }
);
        JPanel recPanel = new JPanel(new GridLayout(1, 3));
        recPanel.add(receiverIdentification);
        recPanel.add(receiverAntennaPoint);
        recPanel.add(receiverSensitivity);
        wantedReceiverPane.addTab("General", recPanel);
        wantedReceiverPane.addTab("Antenna", receiverAntenna);
        itWrLocation = new RelativeLocationPanel(this);
        itWrRadius = new CoverageRadiusPanel(this);
        itWrPropagation = new PropagationSelectPanel(this);
        itWrLocation.setCoverageRadiusPanel(itWrRadius);
        itWrPropagation.addPropagationChangeListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e)
            {
                itWrRadius.setPropagationModel(itWrPropagation.getSelectedModelName());
            }

            final NewDialogInterferingLink this$0;

            
            {
                this$0 = NewDialogInterferingLink.this;
                super();
            }
        }
);
        JPanel itWrPanel = new JPanel(new GridLayout(1, 2));
        itWrPanel.add(itWrLocation);
        itWrPanel.add(itWrRadius);
        itWrPathPane.addTab("Relative location", itWrPanel);
        itWrPathPane.addTab("Propagation model", itWrPropagation);
        itVrLocation = new RelativeLocationInterferingPanel(this);
        itVrDensity = new InterferersDensityPanel(this);
        itVrPropagation = new PropagationSelectPanel(this);
        JPanel itVrPanel = new JPanel(new GridLayout(0, 2));
        itVrPanel.add(itVrLocation);
        itVrPanel.add(itVrDensity);
        itVrLocation.setInterferersDensityPanel(itVrDensity);
        itVrPathPane.addTab("Relative location", itVrPanel);
        itVrPathPane.addTab("Propagation Model", itVrPropagation);
        cdma = new CDMAPanel(this);
        main.addTab(stringlist.getString("INTERFERING_LINK_PANE_TITLE_GENERAL"), generalPane);
        main.addTab(stringlist.getString("INTERFERING_LINK_PANE_TITLE_CDMA"), cdma);
        main.addTab(stringlist.getString("INTERFERING_LINK_PANE_TITLE_IT"), interferingTransmitterPane);
        main.addTab(stringlist.getString("INTERFERING_LINK_PANE_TITLE_WR"), wantedReceiverPane);
        main.addTab(stringlist.getString("INTERFERING_LINK_PANE_TITLE_IT2WRPATH"), itWrPathPane);
        main.addTab(stringlist.getString("INTERFERING_LINK_PANE_TITLE_VR2ITPATH"), itVrPathPane);
        main.setEnabledAt(1, generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(2, !generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(3, !generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(4, !generalDefinition.cdmaSystemSelected());
        setTitle(stringlist.getString("INTERFERING_LINK_TITLE"));
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(main, "Center");
        getContentPane().add(controls, "South");
        setSize(800, 600);
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
    }

    public void setVisible()
    {
        throw new UnsupportedOperationException();
    }

    public boolean show(InterferenceLink il, boolean canBeCoLocated, Workspace workspace)
    {
        accept = false;
        setInterferenceLink(il, canBeCoLocated, workspace);
        setLocationRelativeTo(owner);
        super.setVisible(true);
        interferenceLink = null;
        return accept;
    }

    private void setInterferenceLink(InterferenceLink il, boolean canBeCoLocated, Workspace workspace)
    {
        interferenceLink = il;
        generalIdentification.setComponent(il);
        generalDefinition.setInterferenceLink(il);
        interferingIdentification.setComponent(interferenceLink.getInterferingLink().getInterferingTransmitter());
        interferingAntennaPoint.set(il.getInterferingLink(), il.getInterferingLink().getInterferingTransmitter());
        interferingEmission.setTransmitter(il.getInterferingLink().getInterferingTransmitter());
        interferingPowerControl.setPowerControlEnabled(il.getInterferingLink().getInterferingTransmitter().getUsePowerControl());
        interferingPowerControl.setPowerControl(il.getInterferingLink().getInterferingTransmitter().getPowerControl());
        interferingAntenna.setAntenna(il.getInterferingLink().getInterferingTransmitter().getAntenna(), false);
        receiverIdentification.setComponent(il.getInterferingLink().getWantedReceiver());
        receiverAntennaPoint.set(il.getInterferingLink(), il.getInterferingLink().getWantedReceiver());
        receiverSensitivity.setWantedReceiver(il.getInterferingLink().getWantedReceiver());
        receiverAntenna.setAntenna(il.getInterferingLink().getWantedReceiver().getAntenna(), false);
        itWrLocation.setTransmitterToReceiverPath(il.getInterferingLink().getWt2VrPath(), true);
        itWrRadius.set(il.getInterferingLink());
        itWrRadius.setPropagationModel(il.getInterferingLink().getWt2VrPath().getPropagationModel().toString());
        itVrLocation.setInterferenceLink(il, canBeCoLocated, workspace);
        itVrDensity.setInterferenceLink(il);
        itWrPropagation.setPropagationModel(il.getInterferingLink().getWt2VrPath().getPropagationModel());
        itVrPropagation.setPropagationModel(il.getWt2VrPath().getPropagationModel());
        if(il.getCDMASystem() != null)
            cdma.setCdmasystem(il.getCDMASystem());
        main.setEnabledAt(1, generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(2, !generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(3, !generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(4, !generalDefinition.cdmaSystemSelected());
        if(generalDefinition.cdmaSystemSelected())
            main.setSelectedIndex(1);
        else
            main.setSelectedIndex(0);
    }

    public void updateModel(InterferenceLink interferenceLink)
    {
        generalIdentification.updateModel(interferenceLink);
        generalDefinition.updateModel(interferenceLink);
        if(interferenceLink.isCDMASystem())
        {
            itVrLocation.updateModel(interferenceLink);
            itVrDensity.updateModel(interferenceLink);
            interferenceLink.getWt2VrPath().setPropagationModel(itVrPropagation.getPropagationModel());
            cdma.updateModel();
            interferenceLink.setCDMASystem(cdma.getCdmasystem());
            interferenceLink.getCDMASystem().setFrequency(interferenceLink.getInterferingLink().getInterferingTransmitter().getFrequency().trial());
        } else
        {
            interferingIdentification.updateModel(interferenceLink.getInterferingLink().getInterferingTransmitter());
            interferingAntennaPoint.updateModel(interferenceLink.getInterferingLink(), interferenceLink.getInterferingLink().getInterferingTransmitter());
            interferingEmission.updateModel(interferenceLink.getInterferingLink().getInterferingTransmitter());
            interferingPowerControl.updateModel(interferenceLink.getInterferingLink().getInterferingTransmitter().getPowerControl());
            interferingAntenna.updateModel(interferenceLink.getInterferingLink().getInterferingTransmitter());
            receiverIdentification.updateModel(interferenceLink.getInterferingLink().getWantedReceiver());
            receiverAntennaPoint.updateModel(interferenceLink.getInterferingLink(), interferenceLink.getInterferingLink().getWantedReceiver());
            receiverSensitivity.updateModel(interferenceLink.getInterferingLink().getWantedReceiver());
            receiverAntenna.updateModel(interferenceLink.getInterferingLink().getWantedReceiver());
            itWrLocation.updateModel(interferenceLink.getInterferingLink().getWt2VrPath());
            itWrRadius.updateModel(interferenceLink.getInterferingLink());
            interferenceLink.getInterferingLink().getInterferingTransmitter().setDens(interferenceLink.getInterferingLink().getWt2VrPath().getDensity());
            interferenceLink.getInterferingLink().getInterferingTransmitter().setNumberOfChannels(interferenceLink.getInterferingLink().getWt2VrPath().getNumberOfChannels());
            interferenceLink.getInterferingLink().getInterferingTransmitter().setNumberOfUsersPerChannel(interferenceLink.getInterferingLink().getWt2VrPath().getNumberOfUsersPerChannel());
            interferenceLink.getInterferingLink().getInterferingTransmitter().setFreqCluster(interferenceLink.getInterferingLink().getWt2VrPath().getFrequencyCluster());
            PropagationModel pr = itWrPropagation.getPropagationModel();
            if(pr == null)
                JOptionPane.showMessageDialog(this, "No model selected", "Error on IT -> WR Propagation Model", 0);
            interferenceLink.getInterferingLink().getWt2VrPath().setPropagationModel(pr);
            itVrLocation.updateModel(interferenceLink);
            itVrDensity.updateModel(interferenceLink);
            PropagationModel pr2 = itVrPropagation.getPropagationModel();
            if(pr2 == null)
                JOptionPane.showMessageDialog(this, "No model selected", "Error on IT -> VR Propagation Model", 0);
            interferenceLink.getWt2VrPath().setPropagationModel(pr2);
        }
        interferenceLink.updateNodeAttributes();
        interferenceLink.getInterferingLink().updateNodeAttributes();
        interferenceLink.getInterferingLink().getWantedReceiver().updateNodeAttributes();
        interferenceLink.getInterferingLink().getInterferingTransmitter().updateNodeAttributes();
        interferenceLink.getInterferingLink().getWt2VrPath().updateNodeAttributes();
    }

    private static final ResourceBundle stringlist;
    private static final ResourceBundle helplist;
    private boolean accept;
    private JTabbedPane main;
    private JPanel generalPane;
    private JTabbedPane interferingTransmitterPane;
    private JTabbedPane wantedReceiverPane;
    private JTabbedPane itWrPathPane;
    private JTabbedPane itVrPathPane;
    private IdentificationPanel generalIdentification;
    private InterferingDefinitionPanel generalDefinition;
    private IdentificationPanel interferingIdentification;
    private AntennaPointingPanel interferingAntennaPoint;
    private EmissionCharacteristicsPanel interferingEmission;
    private PowerControlPanel interferingPowerControl;
    private AntennaDefinitionPanel interferingAntenna;
    private IdentificationPanel receiverIdentification;
    private AntennaPointingPanel receiverAntennaPoint;
    private SensitivityPanel receiverSensitivity;
    private AntennaDefinitionPanel receiverAntenna;
    private RelativeLocationPanel itWrLocation;
    private CoverageRadiusPanel itWrRadius;
    private PropagationSelectPanel itWrPropagation;
    private RelativeLocationInterferingPanel itVrLocation;
    private InterferersDensityPanel itVrDensity;
    private PropagationSelectPanel itVrPropagation;
    private CDMAPanel cdma;
    private ControlPanel controls;
    private InterferenceLink interferenceLink;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }

















}
