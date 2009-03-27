// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NewDialogVictimLink.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.seamcat.Seamcat;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.cdma.presentation.CDMAPanel;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.*;
import org.seamcat.model.core.VictimSystemLink;
import org.seamcat.presentation.components.AntennaDefinitionPanel;
import org.seamcat.presentation.components.AntennaPointingPanel;
import org.seamcat.presentation.components.CoverageRadiusPanel;
import org.seamcat.presentation.components.IdentificationPanel;
import org.seamcat.presentation.components.InterferenceCriteriaPanel;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.PowerPanel;
import org.seamcat.presentation.components.PropagationSelectPanel;
import org.seamcat.presentation.components.ReceptionCharacteristicsPanel;
import org.seamcat.presentation.components.RelativeLocationPanel;
import org.seamcat.presentation.components.VictimDefinitionPanel;
import org.seamcat.presentation.components.VictimReceiverChangeListner;
import org.seamcat.presentation.components.WantedTransmitterChangeListener;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public class NewDialogVictimLink extends EscapeDialog
{
    public class ControlPanel extends NavigateButtonPanel
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

        final NewDialogVictimLink this$0;

        public ControlPanel()
        {
            this$0 = NewDialogVictimLink.this;
            super();
        }
    }


    public NewDialogVictimLink(Frame parent)
    {
        super(parent, true);
        main = new JTabbedPane();
        generalPanel = new JPanel();
        victimReceiverPanel = new JTabbedPane(3);
        wantedTransmitterPanel = new JTabbedPane(3);
        wtVrPathPanel = new JTabbedPane(3);
        this.parent = parent;
        init();
    }

    public NewDialogVictimLink(JDialog owner)
    {
        super(owner, true);
        main = new JTabbedPane();
        generalPanel = new JPanel();
        victimReceiverPanel = new JTabbedPane(3);
        wantedTransmitterPanel = new JTabbedPane(3);
        wtVrPathPanel = new JTabbedPane(3);
        parent = owner;
        init();
    }

    private void init()
    {
        generalPanel.setLayout(new GridLayout(1, 2));
        generalIdentification = new IdentificationPanel();
        generalDefinition = new VictimDefinitionPanel(this);
        generalPanel.add(generalIdentification);
        generalPanel.add(generalDefinition);
        victimIdentification = new IdentificationPanel();
        victimAntennaPoint = new AntennaPointingPanel(this);
        victimReception = new ReceptionCharacteristicsPanel(this);
        victimInterference = new InterferenceCriteriaPanel(this);
        victimAntenna = new AntennaDefinitionPanel(this);
        generalDefinition.addVictimReceiverChangeListener(new VictimReceiverChangeListner() {

            public void victimReceiverChanged(VictimSystemLink vlk)
            {
                victimIdentification.setComponent(vlk.getVictimReceiver());
                victimAntennaPoint.set(vlk, true);
                victimReception.setReceiver(vlk.getVictimReceiver());
                victimInterference.setReceiver(vlk.getVictimReceiver());
                victimAntenna.setAntenna(vlk.getVictimReceiver().getAntenna(), false);
                victimSystemLink.setVictimReceiver(vlk.getVictimReceiver());
            }

            final NewDialogVictimLink this$0;

            
            {
                this$0 = NewDialogVictimLink.this;
                super();
            }
        }
);
        generalDefinition.addCDMASystemActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                main.setEnabledAt(1, generalDefinition.cdmaSystemSelected());
                main.setEnabledAt(2, !generalDefinition.cdmaSystemSelected());
                main.setEnabledAt(3, !generalDefinition.cdmaSystemSelected());
                main.setEnabledAt(4, !generalDefinition.cdmaSystemSelected());
                victimSystemLink.setIsCDMASystem(generalDefinition.cdmaSystemSelected());
                if(generalDefinition.cdmaSystemSelected())
                    cdma.setCdmasystem(victimSystemLink.getCDMASystem());
            }

            final NewDialogVictimLink this$0;

            
            {
                this$0 = NewDialogVictimLink.this;
                super();
            }
        }
);
        JPanel generalVictim = new JPanel(new GridLayout(2, 2));
        generalVictim.add(victimIdentification);
        generalVictim.add(victimAntennaPoint);
        generalVictim.add(victimReception);
        generalVictim.add(victimInterference);
        victimReceiverPanel.addTab("General", generalVictim);
        victimReceiverPanel.addTab("Antenna", victimAntenna);
        wantedIdentification = new IdentificationPanel();
        wantedAntennaPoint = new AntennaPointingPanel(this);
        wantedPower = new PowerPanel(this);
        wantedAntenna = new AntennaDefinitionPanel(this);
        JPanel generalWanted = new JPanel(new GridLayout(1, 3));
        generalWanted.add(wantedIdentification);
        generalWanted.add(wantedAntennaPoint);
        generalWanted.add(wantedPower);
        wantedTransmitterPanel.addTab("General", generalWanted);
        wantedTransmitterPanel.addTab("Antenna", wantedAntenna);
        generalDefinition.addWantedTransmitteChangeListener(new WantedTransmitterChangeListener() {

            public void wantedTransmitterChanged(VictimSystemLink vlk)
            {
                wantedIdentification.setComponent(vlk.getWantedTransmitter());
                wantedAntennaPoint.set(vlk, false);
                wantedPower.setWantedTransmitter(vlk.getWantedTransmitter());
                wantedAntenna.setAntenna(vlk.getWantedTransmitter().getAntenna(), false);
            }

            final NewDialogVictimLink this$0;

            
            {
                this$0 = NewDialogVictimLink.this;
                super();
            }
        }
);
        location = new RelativeLocationPanel(this);
        coverage = new CoverageRadiusPanel(this);
        location.setCoverageRadiusPanel(coverage);
        JPanel loc = new JPanel(new GridLayout(1, 2));
        loc.add(location);
        loc.add(coverage);
        wtVrPathPanel.addTab("Relative Location", loc);
        propagationModel = new PropagationSelectPanel(this);
        propagationModel.addPropagationChangeListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e)
            {
                coverage.setPropagationModel(propagationModel.getSelectedModelName());
            }

            final NewDialogVictimLink this$0;

            
            {
                this$0 = NewDialogVictimLink.this;
                super();
            }
        }
);
        wtVrPathPanel.addTab("Propagation Model", propagationModel);
        cdma = new CDMAPanel(this);
        main.addTab(stringlist.getString("VICTIM_LINK_PANE_TITLE_GENERAL"), generalPanel);
        main.addTab("CDMA", cdma);
        main.addTab(stringlist.getString("VICTIM_LINK_PANE_TITLE_VR"), victimReceiverPanel);
        main.addTab(stringlist.getString("VICTIM_LINK_PANE_TITLE_WT"), wantedTransmitterPanel);
        main.addTab(stringlist.getString("VICTIM_LINK_PANE_TITLE_WT2VR"), wtVrPathPanel);
        main.setEnabledAt(1, generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(2, !generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(3, !generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(4, !generalDefinition.cdmaSystemSelected());
        if(generalDefinition.cdmaSystemSelected())
            main.setSelectedIndex(1);
        else
            main.setSelectedIndex(0);
        setTitle("Victim Link");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(main, "Center");
        getContentPane().add(new ControlPanel(), "South");
        setSize(new Dimension(850, 650));
        Seamcat.helpBroker.enableHelpKey(getRootPane(), helplist.getString(getClass().getName()), null);
    }

    public void setVictimSystemLink(VictimSystemLink victimSystemLink)
    {
        this.victimSystemLink = victimSystemLink;
        generalIdentification.setComponent(victimSystemLink);
        generalDefinition.setVictimSystemLink(victimSystemLink);
        victimIdentification.setComponent(victimSystemLink.getVictimReceiver());
        victimAntennaPoint.set(victimSystemLink, true);
        victimReception.setReceiver(victimSystemLink.getVictimReceiver());
        victimInterference.setReceiver(victimSystemLink.getVictimReceiver());
        victimAntenna.setAntenna(victimSystemLink.getVictimReceiver().getAntenna(), false);
        wantedIdentification.setComponent(victimSystemLink.getWantedTransmitter());
        wantedAntennaPoint.set(victimSystemLink, false);
        wantedPower.setWantedTransmitter(victimSystemLink.getWantedTransmitter());
        wantedAntenna.setAntenna(victimSystemLink.getWantedTransmitter().getAntenna(), false);
        location.setTransmitterToReceiverPath(victimSystemLink.getWt2VrPath(), false);
        coverage.set(victimSystemLink);
        coverage.setVisible(!victimSystemLink.getWt2VrPath().getUseCorrelationDistance());
        propagationModel.setPropagationModel(victimSystemLink.getWt2VrPath().getPropagationModel());
        main.setEnabledAt(1, generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(2, !generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(3, !generalDefinition.cdmaSystemSelected());
        main.setEnabledAt(4, !generalDefinition.cdmaSystemSelected());
        if(generalDefinition.cdmaSystemSelected())
        {
            cdma.setCdmasystem(victimSystemLink.getCDMASystem());
            main.setSelectedIndex(1);
        } else
        {
            main.setSelectedIndex(0);
        }
    }

    public void setVisible()
    {
        throw new UnsupportedOperationException("Use show(VictimSystemLink)");
    }

    public boolean show(VictimSystemLink victimSystemLink)
    {
        accept = false;
        setVictimSystemLink(victimSystemLink);
        setLocationRelativeTo(parent);
        super.setVisible(true);
        return accept;
    }

    public void updateModel(VictimSystemLink victimSystemLink)
    {
        LOG.debug("Updating victim link model");
        generalIdentification.updateModel(victimSystemLink);
        generalDefinition.updateModel(victimSystemLink);
        victimIdentification.updateModel(victimSystemLink.getVictimReceiver());
        victimReception.updateModel(victimSystemLink.getVictimReceiver());
        victimInterference.updateModel(victimSystemLink.getVictimReceiver());
        victimAntenna.updateModel(victimSystemLink.getVictimReceiver().getAntenna());
        wantedIdentification.updateModel(victimSystemLink.getWantedTransmitter());
        wantedAntenna.updateModel(victimSystemLink.getWantedTransmitter().getAntenna());
        location.updateModel(victimSystemLink.getWt2VrPath());
        coverage.updateModel(victimSystemLink);
        victimAntennaPoint.updateModel(victimSystemLink, true);
        wantedAntennaPoint.updateModel(victimSystemLink, false);
        cdma.updateModel();
        if(victimSystemLink.isCDMASystem())
        {
            victimSystemLink.setCDMASystem(cdma.getCdmasystem());
            victimSystemLink.getCDMASystem().setFrequency(victimSystemLink.getFrequency().trial());
        }
        victimSystemLink.getWt2VrPath().setPropagationModel(propagationModel.getPropagationModel());
        victimSystemLink.updateNodeAttributes();
        victimSystemLink.getWantedTransmitter().updateNodeAttributes();
        victimSystemLink.getVictimReceiver().updateNodeAttributes();
        wantedPower.updateModel(victimSystemLink.getWantedTransmitter());
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/NewDialogVictimLink);
    private static final ResourceBundle helplist;
    private static final ResourceBundle stringlist;
    private JTabbedPane main;
    private JPanel generalPanel;
    private JTabbedPane victimReceiverPanel;
    private JTabbedPane wantedTransmitterPanel;
    private JTabbedPane wtVrPathPanel;
    private IdentificationPanel generalIdentification;
    private VictimDefinitionPanel generalDefinition;
    private IdentificationPanel victimIdentification;
    private AntennaPointingPanel victimAntennaPoint;
    private ReceptionCharacteristicsPanel victimReception;
    private InterferenceCriteriaPanel victimInterference;
    private AntennaDefinitionPanel victimAntenna;
    private IdentificationPanel wantedIdentification;
    private AntennaPointingPanel wantedAntennaPoint;
    private PowerPanel wantedPower;
    private AntennaDefinitionPanel wantedAntenna;
    private RelativeLocationPanel location;
    private CoverageRadiusPanel coverage;
    private PropagationSelectPanel propagationModel;
    private CDMAPanel cdma;
    private VictimSystemLink victimSystemLink;
    private Component parent;
    private boolean accept;

    static 
    {
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
















}
