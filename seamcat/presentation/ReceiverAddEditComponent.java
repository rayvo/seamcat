// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ReceiverAddEditComponent.java

package org.seamcat.presentation;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.Seamcat;
import org.seamcat.model.Receiver;
import org.seamcat.presentation.components.AntennaDefinitionPanel;
import org.seamcat.presentation.components.IdentificationPanel;
import org.seamcat.presentation.components.InterferenceCriteriaPanel;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.ReceptionCharacteristicsPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public class ReceiverAddEditComponent extends EscapeDialog
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

        final ReceiverAddEditComponent this$0;

        private DialogNavigateButtonPanel()
        {
            this$0 = ReceiverAddEditComponent.this;
            super();
        }

    }


    public ReceiverAddEditComponent(JDialog owner)
    {
        super(owner, true);
        setTitle(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_TITLE"));
        idPanel = new IdentificationPanel();
        receptionCharPanel = new ReceptionCharacteristicsPanel(this);
        antDefPanel = new AntennaDefinitionPanel(this);
        antDefPanel.setReadonly(true);
        intfCriterieaPanel = new InterferenceCriteriaPanel(this);
        idPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_ID_CAPTION")));
        receptionCharPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_RECEPTION_CAPTION")));
        antDefPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_ANTENNA_CAPTION")));
        intfCriterieaPanel.setBorder(new TitledBorder(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_INTFCRITERIA_CAPTION")));
        JPanel generalPanel = new JPanel(new GridLayout(3, 1));
        generalPanel.add(idPanel);
        generalPanel.add(receptionCharPanel);
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_GENERAL_TABCAPTION"), generalPanel);
        tabbedPane.addTab(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_ANTENNA_TABCAPTION"), antDefPanel);
        tabbedPane.addTab(stringlist.getString("LIBRARY_RECEIVER_ADDEDIT_INTFCRITERIA_TABCAPTION"), intfCriterieaPanel);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, "Center");
        getContentPane().add(new DialogNavigateButtonPanel(), "South");
        pack();
        setLocationRelativeTo(owner);
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
    }

    public void setVisible()
    {
        throw new IllegalArgumentException("Cannot show() ReceiverAddEditComponent - use show(Receiver)");
    }

    public boolean show(Receiver receiver)
    {
        setReceiver(receiver);
        accept = false;
        tabbedPane.setSelectedIndex(0);
        super.setVisible(true);
        if(accept)
            updateModel(receiver);
        return accept;
    }

    public void setReceiver(Receiver receiver)
    {
        idPanel.setComponent(receiver);
        receptionCharPanel.setReceiver(receiver);
        antDefPanel.setAntenna(receiver.getAntenna(), true);
        intfCriterieaPanel.setReceiver(receiver);
    }

    public void updateModel(Receiver r)
    {
        idPanel.updateModel(r);
        antDefPanel.updateModel(r);
        receptionCharPanel.updateModel(r);
        intfCriterieaPanel.updateModel(r);
    }

    private static final ResourceBundle stringlist;
    private static final ResourceBundle helplist;
    private JTabbedPane tabbedPane;
    private IdentificationPanel idPanel;
    private ReceptionCharacteristicsPanel receptionCharPanel;
    private AntennaDefinitionPanel antDefPanel;
    private InterferenceCriteriaPanel intfCriterieaPanel;
    private boolean accept;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }

}
