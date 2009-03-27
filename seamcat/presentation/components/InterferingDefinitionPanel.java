// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferingDefinitionPanel.java

package org.seamcat.presentation.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.Function2;
import org.seamcat.model.Components;
import org.seamcat.model.Library;
import org.seamcat.model.Model;
import org.seamcat.model.Receiver;
import org.seamcat.model.Transmitter;
import org.seamcat.model.WantedReceiver;
import org.seamcat.model.core.InterferenceLink;
import org.seamcat.model.core.InterferingSystemLink;
import org.seamcat.model.core.InterferingTransmitter;
import org.seamcat.presentation.DialogFunction2Define;
import org.seamcat.presentation.DistributionDialog;
import org.seamcat.presentation.LabeledPairLayout;

// Referenced classes of package org.seamcat.presentation.components:
//            InterferingTransmitterChangeListener, WantedReceiverChangeListener

public class InterferingDefinitionPanel extends JPanel
{

    public InterferingDefinitionPanel(JDialog owner)
    {
        super(new LabeledPairLayout());
        cbInterferingTransmitters = new JComboBox();
        cbWantedReceivers = new JComboBox();
        xbIsCDMA = new JCheckBox(STRINGS.getString("INTERFERING_LINK_DEFINITION_ISCDMA"));
        interferingTransmitterChangeListeners = new ArrayList();
        wantedReceiverChangeListeners = new ArrayList();
        itActionListner = new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                Transmitter tra = (Transmitter)((JComboBox)e.getSource()).getSelectedItem();
                if(tra instanceof InterferingTransmitter)
                    interferenceLink.getInterferingLink().setInterferingTransmitter((InterferingTransmitter)tra);
                else
                    interferenceLink.getInterferingLink().setInterferingTransmitter(new InterferingTransmitter(tra));
                notifyInterferingTransmitterChangeListeners();
            }

            final InterferingDefinitionPanel this$0;

            
            {
                this$0 = InterferingDefinitionPanel.this;
                super();
            }
        }
;
        wrActionListener = new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                Receiver rec = (Receiver)((JComboBox)e.getSource()).getSelectedItem();
                if(rec instanceof WantedReceiver)
                    interferenceLink.getInterferingLink().setWantedReceiver((WantedReceiver)rec);
                else
                    interferenceLink.getInterferingLink().setWantedReceiver(new WantedReceiver(rec));
                notifyWantedReceiverChangeListeners();
            }

            final InterferingDefinitionPanel this$0;

            
            {
                this$0 = InterferingDefinitionPanel.this;
                super();
            }
        }
;
        frequencyDistributionDialog = new DistributionDialog(owner, true);
        functionDialog = new DialogFunction2Define(owner, true);
        final JLabel lblInterferingTransmitter = new JLabel(STRINGS.getString("INTERFERING_LINK_DEFINITION_IT"));
        lblFrequency = new JLabel(STRINGS.getString("INTERFERING_LINK_DEFINITION_FREQUENCY"));
        final JLabel lblWantedReceiver = new JLabel(STRINGS.getString("INTERFERING_LINK_DEFINITION_WR"));
        JButton frequencyButton = new JButton(STRINGS.getString("BTN_CAPTION_DISTRIBUTION"));
        frequencyButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                boolean accept = frequency == null ? frequencyDistributionDialog.showDistributionDialog(InterferingDefinitionPanel.STRINGS.getString(InterferingDefinitionPanel.STRINGS.getString("INTERFERING_LINK_DEFINITION_FREQUENCY_TITLE"))) : frequencyDistributionDialog.showDistributionDialog(frequency, InterferingDefinitionPanel.STRINGS.getString("INTERFERING_LINK_DEFINITION_FREQUENCY_TITLE"));
                if(accept)
                    frequency = frequencyDistributionDialog.getDistributionable();
                lblFrequency.setText((new StringBuilder()).append(InterferingDefinitionPanel.STRINGS.getString("INTERFERING_LINK_DEFINITION_FREQUENCY")).append(" [").append(frequency.toString()).append("] ").toString());
            }

            final InterferingDefinitionPanel this$0;

            
            {
                this$0 = InterferingDefinitionPanel.this;
                super();
            }
        }
);
        final JLabel lblCdmaUnwanted = new JLabel(STRINGS.getString("CDMA_EMISSION_CHARACTERISTICS_UNWANTED"));
        final JButton cdmaUnwanted = new JButton(STRINGS.getString("BTN_CAPTION_FUNCTION"));
        lblCdmaUnwanted.setEnabled(false);
        cdmaUnwanted.setEnabled(false);
        cdmaUnwanted.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent arg0)
            {
                boolean accept = functionDialog.show(getCdmaUnwanted(), InterferingDefinitionPanel.STRINGS.getString("EMISSION_CHARACTERISTICS_UNWANTED"));
                if(accept)
                    setCdmaUnwanted(functionDialog.getFunction());
            }

            final InterferingDefinitionPanel this$0;

            
            {
                this$0 = InterferingDefinitionPanel.this;
                super();
            }
        }
);
        xbIsCDMA.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e)
            {
                cbInterferingTransmitters.setEnabled(!xbIsCDMA.isSelected());
                cbWantedReceivers.setEnabled(!xbIsCDMA.isSelected());
                lblInterferingTransmitter.setEnabled(!xbIsCDMA.isSelected());
                lblWantedReceiver.setEnabled(!xbIsCDMA.isSelected());
                lblCdmaUnwanted.setEnabled(xbIsCDMA.isSelected());
                cdmaUnwanted.setEnabled(xbIsCDMA.isSelected());
            }

            final JLabel val$lblInterferingTransmitter;
            final JLabel val$lblWantedReceiver;
            final JLabel val$lblCdmaUnwanted;
            final JButton val$cdmaUnwanted;
            final InterferingDefinitionPanel this$0;

            
            {
                this$0 = InterferingDefinitionPanel.this;
                lblInterferingTransmitter = jlabel;
                lblWantedReceiver = jlabel1;
                lblCdmaUnwanted = jlabel2;
                cdmaUnwanted = jbutton;
                super();
            }
        }
);
        cbInterferingTransmitters.addActionListener(itActionListner);
        cbWantedReceivers.addActionListener(wrActionListener);
        add(lblInterferingTransmitter, "label");
        add(cbInterferingTransmitters, "field");
        add(lblWantedReceiver, "label");
        add(cbWantedReceivers, "field");
        add(lblFrequency, "label");
        add(frequencyButton, "field");
        add(xbIsCDMA, "label");
        add(new JLabel(), "field");
        add(lblCdmaUnwanted, "label");
        add(cdmaUnwanted, "field");
        setBorder(new TitledBorder(STRINGS.getString("INTERFERING_LINK_DEFINITION_TITLE")));
    }

    public void addInterferingTransmitterChangeListener(InterferingTransmitterChangeListener itcl)
    {
        interferingTransmitterChangeListeners.add(itcl);
    }

    public void addWantedReceiverChangeListener(WantedReceiverChangeListener wtcl)
    {
        wantedReceiverChangeListeners.add(wtcl);
    }

    private void notifyInterferingTransmitterChangeListeners()
    {
        for(Iterator i = interferingTransmitterChangeListeners.iterator(); i.hasNext(); ((InterferingTransmitterChangeListener)i.next()).interferingTransmitterChanged(interferenceLink.getInterferingLink().getInterferingTransmitter()));
    }

    private void notifyWantedReceiverChangeListeners()
    {
        for(Iterator i = wantedReceiverChangeListeners.iterator(); i.hasNext(); ((WantedReceiverChangeListener)i.next()).wantedReceiverChanged(interferenceLink.getInterferingLink().getWantedReceiver()));
    }

    public boolean cdmaSystemSelected()
    {
        return xbIsCDMA.isSelected();
    }

    public void setInterferenceLink(InterferenceLink interferenceLink)
    {
        this.interferenceLink = interferenceLink;
        setInterferingTransmitters(Model.getInstance().getLibrary().getTransmitters().createComboBoxModel());
        setWantedReceivers(Model.getInstance().getLibrary().getReceivers().createComboBoxModel());
        setFrequency(interferenceLink.getInterferingLink().getInterferingTransmitter().getFrequency());
        setCdmaUnwanted(interferenceLink.getInterferingLink().getInterferingTransmitter().getUnwantedEmissions());
        cbInterferingTransmitters.removeActionListener(itActionListner);
        cbWantedReceivers.removeActionListener(wrActionListener);
        cbInterferingTransmitters.setEditable(true);
        cbWantedReceivers.setEditable(true);
        cbInterferingTransmitters.setSelectedItem(interferenceLink.getInterferingLink().getInterferingTransmitter());
        cbWantedReceivers.setSelectedItem(interferenceLink.getInterferingLink().getWantedReceiver());
        cbInterferingTransmitters.addActionListener(itActionListner);
        cbWantedReceivers.addActionListener(wrActionListener);
        cbInterferingTransmitters.setEditable(false);
        cbWantedReceivers.setEditable(false);
        xbIsCDMA.setSelected(interferenceLink.isCDMASystem());
        lblFrequency.setText((new StringBuilder()).append(STRINGS.getString("INTERFERING_LINK_DEFINITION_FREQUENCY")).append(" [").append(frequency.toString()).append("] ").toString());
    }

    public void addCDMASystemActionListener(java.awt.event.ActionListener al)
    {
        xbIsCDMA.addActionListener(al);
    }

    public InterferenceLink updateModel(InterferenceLink il)
    {
        il.getInterferingLink().getInterferingTransmitter().setFrequency(getFrequency());
        il.getInterferingLink().getInterferingTransmitter().setUnwantedEmissions(getCdmaUnwanted());
        return il;
    }

    public Distribution getFrequency()
    {
        return frequency;
    }

    public void setFrequency(Distribution frequency)
    {
        this.frequency = frequency;
    }

    public void setInterferingTransmitters(ComboBoxModel transmitters)
    {
        cbInterferingTransmitters.setModel(transmitters);
    }

    public void setWantedReceivers(ComboBoxModel receivers)
    {
        cbWantedReceivers.setModel(receivers);
    }

    public WantedReceiver getWantedReceiver()
    {
        return (WantedReceiver)cbWantedReceivers.getSelectedItem();
    }

    public InterferingTransmitter getInteferingTransmitter()
    {
        return (InterferingTransmitter)cbInterferingTransmitters.getSelectedItem();
    }

    public void clear()
    {
        frequency = null;
        frequencyDistributionDialog.clear();
        interferenceLink = null;
    }

    public Function2 getCdmaUnwanted()
    {
        return cdmaUnwanted;
    }

    public void setCdmaUnwanted(Function2 cdmaUnwanted)
    {
        this.cdmaUnwanted = cdmaUnwanted;
    }

    private static final ResourceBundle STRINGS;
    private JComboBox cbInterferingTransmitters;
    private JComboBox cbWantedReceivers;
    private JCheckBox xbIsCDMA;
    private Distribution frequency;
    private Function2 cdmaUnwanted;
    private DistributionDialog frequencyDistributionDialog;
    private DialogFunction2Define functionDialog;
    private List interferingTransmitterChangeListeners;
    private List wantedReceiverChangeListeners;
    private JLabel lblFrequency;
    private InterferenceLink interferenceLink;
    private java.awt.event.ActionListener itActionListner;
    private java.awt.event.ActionListener wrActionListener;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }












}
