// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TransmitterAddEditComponent.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.Seamcat;
import org.seamcat.model.Transmitter;
import org.seamcat.presentation.components.AntennaDefinitionPanel;
import org.seamcat.presentation.components.CoverageTrafficPanel;
import org.seamcat.presentation.components.EmissionCharacteristicsPanel;
import org.seamcat.presentation.components.IdentificationPanel;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.PowerControlPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public class TransmitterAddEditComponent extends EscapeDialog
{
    private class DialogNavigateButtonPanel extends NavigateButtonPanel
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

        final TransmitterAddEditComponent this$0;

        private DialogNavigateButtonPanel()
        {
            this$0 = TransmitterAddEditComponent.this;
            super();
        }

    }


    public TransmitterAddEditComponent(JDialog owner)
    {
        super(owner, true);
        setTitle(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_TITLE"));
        idPanel = new IdentificationPanel();
        antDefPanel = new AntennaDefinitionPanel(this);
        antDefPanel.setReadonly(true);
        emiCharPanel = new EmissionCharacteristicsPanel(this);
        powerControlPanel = new PowerControlPanel(owner);
        coverageTrafPanel = new CoverageTrafficPanel(owner);
        idPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_ID_CAPTION")));
        antDefPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_ANTENNA_CAPTION")));
        emiCharPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_EMISSION_CAPTION")));
        powerControlPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_POWER_CAPTION")));
        coverageTrafPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_COVERAGE_CAPTION")));
        JPanel powerTab = new JPanel();
        powerTab.setLayout(new GridLayout(2, 1));
        powerTab.add(emiCharPanel);
        powerTab.add(powerControlPanel);
        emiCharPanel.addPowerListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                powerControlPanel.setPowerControlEnabled(emiCharPanel.isPowerControlEnabled());
            }

            final TransmitterAddEditComponent this$0;

            
            {
                this$0 = TransmitterAddEditComponent.this;
                super();
            }
        }
);
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_GENERAL_TABCAPTION"), idPanel);
        tabbedPane.addTab(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_ANTENNA_TABCAPTION"), antDefPanel);
        tabbedPane.addTab(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_POWER_TABCAPTION"), powerTab);
        tabbedPane.addTab(stringlist.getString("LIBRARY_TRANSMITTER_ADDEDIT_COVERAGE_TABCAPTION"), coverageTrafPanel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, "Center");
        getContentPane().add(new DialogNavigateButtonPanel(), "South");
        pack();
        setLocationRelativeTo(owner);
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
    }

    public void updateModel(Transmitter t)
    {
        idPanel.updateModel(t);
        antDefPanel.updateModel(t);
        emiCharPanel.updateModel(t);
        transmitter.setPowerControlStep(powerControlPanel.getControlStepSize());
        transmitter.setPowerControlMinimum(powerControlPanel.getMinThreshold());
        transmitter.setPowerControlRange(powerControlPanel.getDynamicRange());
        coverageTrafPanel.updateModel();
    }

    public void setVisible()
    {
        throw new IllegalArgumentException("Cannot show() TransmitterAddEditComponent - use show(Transmitter)");
    }

    public boolean show(Transmitter transmitter)
    {
        setTransmitter(transmitter);
        accept = false;
        tabbedPane.setSelectedIndex(0);
        super.setVisible(true);
        if(accept)
            updateModel(transmitter);
        return accept;
    }

    public void setTransmitter(Transmitter transmitter)
    {
        this.transmitter = transmitter;
        idPanel.setComponent(transmitter);
        antDefPanel.setAntenna(transmitter.getAntenna(), true);
        emiCharPanel.setTransmitter(transmitter);
        powerControlPanel.setPowerControl(transmitter.getPowerControl());
        coverageTrafPanel.setTransmitter(transmitter);
    }

    private static final ResourceBundle stringlist;
    private static final ResourceBundle helplist;
    private JTabbedPane tabbedPane;
    private IdentificationPanel idPanel;
    private AntennaDefinitionPanel antDefPanel;
    private EmissionCharacteristicsPanel emiCharPanel;
    private PowerControlPanel powerControlPanel;
    private CoverageTrafficPanel coverageTrafPanel;
    private Transmitter transmitter;
    private boolean accept;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }



}
