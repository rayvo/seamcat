// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   VictimDefinitionPanel.java

package org.seamcat.presentation.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.*;
import org.seamcat.model.core.VictimSystemLink;
import org.seamcat.presentation.*;

// Referenced classes of package org.seamcat.presentation.components:
//            VictimReceiverChangeListner, WantedTransmitterChangeListener

public class VictimDefinitionPanel extends JPanel
{

    public VictimDefinitionPanel(JDialog owner)
    {
        super(new GridBagLayout());
        victimReceivers = new JComboBox();
        wantedTransmitters = new JComboBox();
        userDefineddRSSButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        frequencyButton = new JButton(STRINGLIST.getString("BTN_CAPTION_DISTRIBUTION"));
        victimReceiverLabel = new JLabel(STRINGLIST.getString("VICTIM_LINK_VICTIM_RECEIVER"));
        frequencyLabel = new JLabel(STRINGLIST.getString("VICTIM_LINK_FREQUENCY"));
        wantedTransmitterLabel = new JRadioButton(STRINGLIST.getString("VICTIM_LINK_WANTED_TRANSMITTER"));
        userDefinedRSSLabel = new JRadioButton(STRINGLIST.getString("VICTIM_LINK_DRSS"));
        transmitterGroup = new ButtonGroup();
        isCDMASystem = new JCheckBox(STRINGLIST.getString("VICTIM_LINK_ISCDMA"));
        cdmaReceiverSelectivity = new JButton(STRINGLIST.getString("BTN_CAPTION_FUNCTION"));
        cdmaReceiverSelectivityLabel = new JLabel(STRINGLIST.getString("CDMA_RECEIVER_SELECTIVY_FUNCTION"));
        victimChangeListeners = new Vector();
        wantedTranmittersListeners = new Vector();
        fireListeners = true;
        powerDistDialog = new DistributionDialog(owner, true);
        functionDialog = new DialogFunctionDefine(owner, true);
        JPanel topPanel = new JPanel(new LabeledPairLayout());
        JPanel middlePanel = new JPanel(new LabeledPairLayout());
        JPanel bottomPanel = new JPanel(new LabeledPairLayout());
        topPanel.add(victimReceiverLabel, "label");
        topPanel.add(victimReceivers, "field");
        middlePanel.add(wantedTransmitterLabel, "label");
        middlePanel.add(wantedTransmitters, "field");
        middlePanel.add(userDefinedRSSLabel, "label");
        middlePanel.add(userDefineddRSSButton, "field");
        userDefinedRSSLabel.setSelected(false);
        wantedTransmitterLabel.setSelected(true);
        userDefinedRSSLabel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                userDefineddRSSButton.setEnabled(userDefinedRSSLabel.isSelected());
                wantedTransmitters.setEnabled(!userDefinedRSSLabel.isSelected());
                victimSystemLink.setUseWantedTransmitter(false);
            }

            final VictimDefinitionPanel this$0;

            
            {
                this$0 = VictimDefinitionPanel.this;
                super();
            }
        }
);
        wantedTransmitterLabel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                wantedTransmitters.setEnabled(wantedTransmitterLabel.isSelected());
                userDefineddRSSButton.setEnabled(!wantedTransmitterLabel.isSelected());
                victimSystemLink.setUseWantedTransmitter(true);
            }

            final VictimDefinitionPanel this$0;

            
            {
                this$0 = VictimDefinitionPanel.this;
                super();
            }
        }
);
        userDefineddRSSButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                boolean accept = userDefined == null ? powerDistDialog.showDistributionDialog(VictimDefinitionPanel.STRINGLIST.getString("VICTIM_LINK_DRSS_TITLE")) : powerDistDialog.showDistributionDialog(userDefined, VictimDefinitionPanel.STRINGLIST.getString("VICTIM_LINK_DRSS_TITLE"));
                if(accept)
                    userDefined = powerDistDialog.getDistributionable();
            }

            final VictimDefinitionPanel this$0;

            
            {
                this$0 = VictimDefinitionPanel.this;
                super();
            }
        }
);
        cdmaReceiverSelectivity.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                boolean accept = functionDialog.show(victimSystemLink.getVictimReceiver().getBlockingResponse(), VictimDefinitionPanel.STRINGLIST.getString("CDMA_RECEIVER_SELECTIVY_FUNCTION_TITLE"));
                if(accept)
                    victimSystemLink.getVictimReceiver().setBlockingResponse(functionDialog.getFunction());
            }

            final VictimDefinitionPanel this$0;

            
            {
                this$0 = VictimDefinitionPanel.this;
                super();
            }
        }
);
        bottomPanel.add(new JLabel("  "), "label");
        bottomPanel.add(new JLabel("  "), "field");
        bottomPanel.add(frequencyLabel, "label");
        bottomPanel.add(frequencyButton, "field");
        bottomPanel.add(isCDMASystem, "label");
        bottomPanel.add(new JLabel(""), "field");
        bottomPanel.add(cdmaReceiverSelectivityLabel, "label");
        bottomPanel.add(cdmaReceiverSelectivity, "field");
        frequencyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                boolean accept = frequency == null ? powerDistDialog.showDistributionDialog(VictimDefinitionPanel.STRINGLIST.getString("VICTIM_LINK_FREQUENCY_TITLE")) : powerDistDialog.showDistributionDialog(frequency, VictimDefinitionPanel.STRINGLIST.getString("VICTIM_LINK_FREQUENCY_TITLE"));
                if(accept)
                    frequency = powerDistDialog.getDistributionable();
                frequencyLabel.setText((new StringBuilder()).append(VictimDefinitionPanel.STRINGLIST.getString("VICTIM_LINK_FREQUENCY")).append(": ").append(frequency.toString()).toString());
            }

            final VictimDefinitionPanel this$0;

            
            {
                this$0 = VictimDefinitionPanel.this;
                super();
            }
        }
);
        isCDMASystem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                victimReceivers.setEnabled(!isCDMASystem.isSelected());
                victimReceiverLabel.setEnabled(!isCDMASystem.isSelected());
                wantedTransmitterLabel.setEnabled(!isCDMASystem.isSelected());
                wantedTransmitters.setEnabled(!isCDMASystem.isSelected());
                userDefinedRSSLabel.setEnabled(!isCDMASystem.isSelected());
                cdmaReceiverSelectivity.setEnabled(isCDMASystem.isSelected());
                cdmaReceiverSelectivityLabel.setEnabled(isCDMASystem.isSelected());
            }

            final VictimDefinitionPanel this$0;

            
            {
                this$0 = VictimDefinitionPanel.this;
                super();
            }
        }
);
        transmitterGroup.add(wantedTransmitterLabel);
        transmitterGroup.add(userDefinedRSSLabel);
        setVictimReceivers(Model.getInstance().getLibrary().getReceivers().createComboBoxModel());
        setWantedTransmitters(Model.getInstance().getLibrary().getTransmitters().createComboBoxModel());
        wantedTransmitters.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    if(fireListeners)
                    {
                        victimSystemLink.setWantedTransmitter((wantedTransmitters.getSelectedItem() instanceof WantedTransmitter) ? (WantedTransmitter)wantedTransmitters.getSelectedItem() : new WantedTransmitter((Transmitter)wantedTransmitters.getSelectedItem()));
                        notifyWantedTransmitterChangeListeners();
                    }
                }
                catch(Exception ex) { }
            }

            final VictimDefinitionPanel this$0;

            
            {
                this$0 = VictimDefinitionPanel.this;
                super();
            }
        }
);
        victimReceivers.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    if(fireListeners)
                    {
                        victimSystemLink.setVictimReceiver((victimReceivers.getSelectedItem() instanceof VictimReceiver) ? (VictimReceiver)victimReceivers.getSelectedItem() : new VictimReceiver((Receiver)victimReceivers.getSelectedItem()));
                        notifyVictimReceiverChangeListeners();
                    }
                }
                catch(NullPointerException e1) { }
            }

            final VictimDefinitionPanel this$0;

            
            {
                this$0 = VictimDefinitionPanel.this;
                super();
            }
        }
);
        GridBagConstraints gconst = new GridBagConstraints(0, -1, 1, 1, 1.0D, 1.0D, 11, 2, new Insets(0, 0, 0, 0), 0, 0);
        add(topPanel, gconst);
        add(middlePanel, gconst);
        add(bottomPanel, gconst);
        gconst.weighty = 50D;
        add(Box.createVerticalGlue(), gconst);
        setBorder(new TitledBorder(STRINGLIST.getString("VICTIM_LINK_DEFINITION_TITLE")));
    }

    public void setVictimSystemLink(VictimSystemLink victimSystemLink)
    {
        this.victimSystemLink = victimSystemLink;
        userDefineddRSSButton.setEnabled(!victimSystemLink.getUseWantedTransmitter());
        wantedTransmitters.setEnabled(victimSystemLink.getUseWantedTransmitter());
        wantedTransmitters.setEditable(true);
        victimReceivers.setEditable(true);
        fireListeners = false;
        wantedTransmitters.setSelectedItem(victimSystemLink.getWantedTransmitter());
        victimReceivers.setSelectedItem(victimSystemLink.getVictimReceiver());
        wantedTransmitters.setEditable(false);
        victimReceivers.setEditable(false);
        fireListeners = true;
        userDefinedRSSLabel.setSelected(!victimSystemLink.getUseWantedTransmitter());
        wantedTransmitterLabel.setSelected(victimSystemLink.getUseWantedTransmitter());
        setUserDefinedRSS(victimSystemLink.getDRSS());
        frequency = victimSystemLink.getFrequency();
        isCDMASystem.setSelected(victimSystemLink.isCDMASystem());
        victimReceivers.setEnabled(!isCDMASystem.isSelected());
        victimReceiverLabel.setEnabled(!isCDMASystem.isSelected());
        wantedTransmitterLabel.setEnabled(!isCDMASystem.isSelected());
        wantedTransmitters.setEnabled(!isCDMASystem.isSelected());
        userDefinedRSSLabel.setEnabled(!isCDMASystem.isSelected());
        cdmaReceiverSelectivity.setEnabled(isCDMASystem.isSelected());
        cdmaReceiverSelectivityLabel.setEnabled(isCDMASystem.isSelected());
        frequencyLabel.setText((new StringBuilder()).append(STRINGLIST.getString("VICTIM_LINK_FREQUENCY")).append(": [").append(frequency.toString()).append("]").toString());
    }

    public boolean cdmaSystemSelected()
    {
        return isCDMASystem.isSelected();
    }

    public boolean useWantedTransmitterSelected()
    {
        return userDefinedRSSLabel.isSelected();
    }

    public void addCDMASystemActionListener(ActionListener al)
    {
        isCDMASystem.addActionListener(al);
    }

    public void addUseWantedTransmitterActionListener(ActionListener al)
    {
        userDefinedRSSLabel.addActionListener(al);
        wantedTransmitterLabel.addActionListener(al);
    }

    public void notifyVictimReceiverChangeListeners()
    {
        for(int i = 0; i < victimChangeListeners.size(); i++)
            ((VictimReceiverChangeListner)victimChangeListeners.get(i)).victimReceiverChanged(victimSystemLink);

    }

    public void notifyWantedTransmitterChangeListeners()
    {
        for(int i = 0; i < wantedTranmittersListeners.size(); i++)
            ((WantedTransmitterChangeListener)wantedTranmittersListeners.get(i)).wantedTransmitterChanged(victimSystemLink);

    }

    public void addVictimReceiverChangeListener(VictimReceiverChangeListner vct)
    {
        victimChangeListeners.add(vct);
    }

    public void addWantedTransmitteChangeListener(WantedTransmitterChangeListener wtl)
    {
        wantedTranmittersListeners.add(wtl);
    }

    public Distribution getFrequency()
    {
        return frequency;
    }

    public Distribution getUserDefinedRSS()
    {
        return userDefined;
    }

    public void setUserDefinedRSS(Distribution _rss)
    {
        userDefined = _rss;
    }

    public void setVictimReceivers(ComboBoxModel _receivers)
    {
        victimReceivers.setModel(_receivers);
    }

    public void setWantedTransmitters(ComboBoxModel _transmitters)
    {
        wantedTransmitters.setModel(_transmitters);
    }

    public VictimReceiver getVictimReceiver()
    {
        return (VictimReceiver)victimReceivers.getSelectedItem();
    }

    public WantedTransmitter getWantedTransmitter()
    {
        return (WantedTransmitter)wantedTransmitters.getSelectedItem();
    }

    public void updateModel(VictimSystemLink vl)
    {
        vl.setFrequency(frequency);
        vl.setDRSS(userDefined);
    }

    private static final ResourceBundle STRINGLIST;
    private JComboBox victimReceivers;
    private JComboBox wantedTransmitters;
    private JButton userDefineddRSSButton;
    private JButton frequencyButton;
    private JLabel victimReceiverLabel;
    private JLabel frequencyLabel;
    private JRadioButton wantedTransmitterLabel;
    private JRadioButton userDefinedRSSLabel;
    private ButtonGroup transmitterGroup;
    private JCheckBox isCDMASystem;
    private JButton cdmaReceiverSelectivity;
    private JLabel cdmaReceiverSelectivityLabel;
    private Distribution userDefined;
    private Distribution frequency;
    private DistributionDialog powerDistDialog;
    private DialogFunctionDefine functionDialog;
    private VictimSystemLink victimSystemLink;
    private Vector victimChangeListeners;
    private Vector wantedTranmittersListeners;
    private boolean fireListeners;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }



















}
