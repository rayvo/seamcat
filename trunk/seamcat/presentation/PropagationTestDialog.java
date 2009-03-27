// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropagationTestDialog.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.log4j.*;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.technical.exception.ModelException;
import org.seamcat.propagation.HataSE21Model;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, DialogDisplaySignal, DistributionDialog, PropagationSelectDialog, 
//            LabeledPairLayout, SeamcatTextFieldFormats

public class PropagationTestDialog extends EscapeDialog
{

    public PropagationTestDialog(Frame owner)
    {
        super(owner, "Seamcat Propagation Model Test");
        propagationModel = new HataSE21Model();
        freqDistribution = new ConstantDistribution(900D);
        distDistribution = new ConstantDistribution(1.0D);
        txHeightDistribution = new ConstantDistribution(10D);
        rxHeightDistribution = new ConstantDistribution(10D);
        btn_Prop = new JButton("Propagation Model");
        btn_Gen = new JButton("Generate and show samples");
        btn_Freq = new JButton("Distribution");
        btn_Dist = new JButton("Distribution");
        btn_TxH = new JButton("Distribution");
        btn_RxH = new JButton("Distribution");
        btn_Close = new JButton("Close");
        tf_Events = new JFormattedTextField(SeamcatTextFieldFormats.getIntegerFactory(), new Integer(1000));
        lbl_Prop = new JLabel("Click to configure Propagation Model:");
        lbl_Freq = new JLabel((new StringBuilder()).append("Frequency (MHz): ").append(freqDistribution).toString());
        lbl_Dist = new JLabel((new StringBuilder()).append("Distance (km): ").append(distDistribution).toString());
        lbl_TxH = new JLabel((new StringBuilder()).append("TX Height (m): ").append(txHeightDistribution).toString());
        lbl_RxH = new JLabel((new StringBuilder()).append("RX Height (m): ").append(rxHeightDistribution).toString());
        propagationLabel = new JLabel(propagationModel.toString());
        lbl_Events = new JLabel("Specify number of samples:");
        displaySignal = new DialogDisplaySignal(owner, "Event", "Signal Loss, dB");
        powerDistDialog = new DistributionDialog(this, true);
        propDialog = new PropagationSelectDialog(owner);
        getContentPane().setLayout(new BorderLayout());
        JPanel center = new JPanel(new LabeledPairLayout());
        center.add(lbl_Prop, "label");
        center.add(btn_Prop, "field");
        center.add(lbl_Freq, "label");
        center.add(btn_Freq, "field");
        center.add(lbl_Dist, "label");
        center.add(btn_Dist, "field");
        center.add(lbl_TxH, "label");
        center.add(btn_TxH, "field");
        center.add(lbl_RxH, "label");
        center.add(btn_RxH, "field");
        center.add(new JLabel(""), "label");
        center.add(new JLabel("<html><b>Note:</b> Not all Propagation Models use all of the parameters above"), "field");
        center.add(lbl_Events, "label");
        center.add(tf_Events, "field");
        JPanel south = new JPanel();
        south.add(btn_Gen);
        south.add(btn_Close);
        JPanel north = new JPanel();
        north.add(propagationLabel);
        getContentPane().add(north, "North");
        getContentPane().add(center, "Center");
        getContentPane().add(south, "South");
        btn_Freq.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = powerDistDialog.showDistributionDialog(freqDistribution, "Configure Distribution");
                if(accept)
                {
                    freqDistribution = powerDistDialog.getDistributionable();
                    lbl_Freq.setText((new StringBuilder()).append("Frequency (MHz): ").append(freqDistribution).toString());
                }
            }

            final PropagationTestDialog this$0;

            
            {
                this$0 = PropagationTestDialog.this;
                super();
            }
        }
);
        btn_Dist.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = powerDistDialog.showDistributionDialog(distDistribution, "Configure Distribution");
                if(accept)
                {
                    distDistribution = powerDistDialog.getDistributionable();
                    lbl_Dist.setText((new StringBuilder()).append("Distance (km): ").append(distDistribution).toString());
                }
            }

            final PropagationTestDialog this$0;

            
            {
                this$0 = PropagationTestDialog.this;
                super();
            }
        }
);
        btn_TxH.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = powerDistDialog.showDistributionDialog(txHeightDistribution, "Configure Distribution");
                if(accept)
                {
                    txHeightDistribution = powerDistDialog.getDistributionable();
                    lbl_TxH.setText((new StringBuilder()).append("TX Height (m): ").append(txHeightDistribution).toString());
                }
            }

            final PropagationTestDialog this$0;

            
            {
                this$0 = PropagationTestDialog.this;
                super();
            }
        }
);
        btn_RxH.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = powerDistDialog.showDistributionDialog(rxHeightDistribution, "Configure Distribution");
                if(accept)
                {
                    rxHeightDistribution = powerDistDialog.getDistributionable();
                    lbl_RxH.setText((new StringBuilder()).append("RX Height (m): ").append(rxHeightDistribution).toString());
                }
            }

            final PropagationTestDialog this$0;

            
            {
                this$0 = PropagationTestDialog.this;
                super();
            }
        }
);
        btn_Prop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(propDialog.show(propagationModel))
                    propagationModel = propDialog.getPropagationModel();
                propagationLabel.setText(propagationModel.toString());
            }

            final PropagationTestDialog this$0;

            
            {
                this$0 = PropagationTestDialog.this;
                super();
            }
        }
);
        btn_Gen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                int events = ((Number)tf_Events.getValue()).intValue();
                double samples[] = new double[events];
                for(int i = 0; i < samples.length; i++)
                    try
                    {
                        samples[i] = propagationModel.evaluate(freqDistribution.trial(), distDistribution.trial(), txHeightDistribution.trial(), rxHeightDistribution.trial());
                    }
                    catch(ModelException e1)
                    {
                        samples[i] = -1D;
                    }

                displaySignal.show(samples, (new StringBuilder()).append(events).append(" samples from ").append(propagationModel.toString()).toString());
            }

            final PropagationTestDialog this$0;

            
            {
                this$0 = PropagationTestDialog.this;
                super();
            }
        }
);
        btn_Close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }

            final PropagationTestDialog this$0;

            
            {
                this$0 = PropagationTestDialog.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).setBorder(new TitledBorder("Propagation Model parameters"));
        setSize(600, 400);
        setLocationRelativeTo(owner);
    }

    public static void main(String args[])
    {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
        try
        {
            if(UIManager.getSystemLookAndFeelClassName().equalsIgnoreCase("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"))
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            else
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Table.focusCellBackground", Color.LIGHT_GRAY);
        }
        catch(Exception exception)
        {
            System.err.println(exception.getLocalizedMessage());
        }
        JFrame frame = new JFrame("Propagation Test");
        PropagationTestDialog diag = new PropagationTestDialog(frame);
        diag.setModal(true);
        diag.setVisible(true);
        System.exit(0);
    }

    private DialogDisplaySignal displaySignal;
    private DistributionDialog powerDistDialog;
    private PropagationSelectDialog propDialog;
    private static final String TITLE = "Seamcat Propagation Model Test";
    private PropagationModel propagationModel;
    private Distribution freqDistribution;
    private Distribution distDistribution;
    private Distribution txHeightDistribution;
    private Distribution rxHeightDistribution;
    private JButton btn_Prop;
    private JButton btn_Gen;
    private JButton btn_Freq;
    private JButton btn_Dist;
    private JButton btn_TxH;
    private JButton btn_RxH;
    private JButton btn_Close;
    private JFormattedTextField tf_Events;
    private JLabel lbl_Prop;
    private JLabel lbl_Freq;
    private JLabel lbl_Dist;
    private JLabel lbl_TxH;
    private JLabel lbl_RxH;
    private JLabel propagationLabel;
    private JLabel lbl_Events;



















}
