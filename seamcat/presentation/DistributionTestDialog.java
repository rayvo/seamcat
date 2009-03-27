// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DistributionTestDialog.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.distribution.Distribution;
import org.seamcat.distribution.UniformDistribution;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, DialogDisplaySignal, DistributionDialog, LabeledPairLayout, 
//            SeamcatTextFieldFormats

public class DistributionTestDialog extends EscapeDialog
{

    public DistributionTestDialog(Frame owner)
    {
        super(owner, "Seamcat Distribution Test");
        random = new Random();
        distribution = new UniformDistribution(0.0D, 1.0D);
        btn_Dist = new JButton("Distribution");
        btn_Java_Random = new JCheckBox("Test Java random");
        btn_Gen = new JButton("Generate and show samples");
        btn_Close = new JButton("Close");
        tf_Events = new JFormattedTextField(dialogFormats.getIntegerFactory(), new Integer(1000));
        lbl_Dist = new JLabel("Click to configure distribution:");
        lbl_Events = new JLabel("Specify number of samples:");
        displaySignal = new DialogDisplaySignal(owner, "Trial Number", "Trialed Value");
        powerDistDialog = new DistributionDialog(this, true);
        getContentPane().setLayout(new BorderLayout());
        JPanel center = new JPanel(new LabeledPairLayout());
        center.add(lbl_Dist, "label");
        center.add(btn_Dist, "field");
        center.add(new JLabel(""), "label");
        center.add(btn_Java_Random, "field");
        center.add(lbl_Events, "label");
        center.add(tf_Events, "field");
        JPanel south = new JPanel();
        south.add(btn_Gen);
        south.add(btn_Close);
        getContentPane().add(center, "Center");
        getContentPane().add(south, "South");
        btn_Dist.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = powerDistDialog.showDistributionDialog(distribution, "Configure Distribution");
                if(accept)
                    distribution = powerDistDialog.getDistributionable();
            }

            final DistributionTestDialog this$0;

            
            {
                this$0 = DistributionTestDialog.this;
                super();
            }
        }
);
        btn_Java_Random.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                btn_Dist.setEnabled(!btn_Java_Random.isSelected());
            }

            final DistributionTestDialog this$0;

            
            {
                this$0 = DistributionTestDialog.this;
                super();
            }
        }
);
        btn_Gen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                int events = ((Number)tf_Events.getValue()).intValue();
                double samples[] = new double[events];
                boolean intJava = btn_Java_Random.isSelected();
                for(int i = 0; i < samples.length; i++)
                    if(intJava)
                        samples[i] = random.nextDouble();
                    else
                        samples[i] = distribution.trial();

                displaySignal.show(samples, (new StringBuilder()).append(events).append(" samples from ").append(intJava ? "internal java random" : distribution.toString()).toString());
            }

            final DistributionTestDialog this$0;

            
            {
                this$0 = DistributionTestDialog.this;
                super();
            }
        }
);
        btn_Close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }

            final DistributionTestDialog this$0;

            
            {
                this$0 = DistributionTestDialog.this;
                super();
            }
        }
);
        ((JComponent)getContentPane()).setBorder(new TitledBorder("Configure Distribution"));
        pack();
        setLocationRelativeTo(owner);
    }

    private DialogDisplaySignal displaySignal;
    private DistributionDialog powerDistDialog;
    private static final SeamcatTextFieldFormats dialogFormats = new SeamcatTextFieldFormats();
    private static final String TITLE = "Seamcat Distribution Test";
    private Random random;
    private Distribution distribution;
    private JButton btn_Dist;
    private JCheckBox btn_Java_Random;
    private JButton btn_Gen;
    private JButton btn_Close;
    private JFormattedTextField tf_Events;
    private JLabel lbl_Dist;
    private JLabel lbl_Events;









}
