// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FunctionTestDialog.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.distribution.Distribution;
import org.seamcat.function.*;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, DialogDisplaySignal, DialogFunction2Define, DistributionDialog, 
//            LabeledPairLayout, SeamcatTextFieldFormats

public class FunctionTestDialog extends EscapeDialog
{

    public FunctionTestDialog(Frame owner)
    {
        super(owner, "Rel. Unwanted Function");
        difference = new ConstantDistribution(-0.025000000000000001D);
        function = new DiscreteFunction2();
        btn_function = new JButton("Function");
        btn_diff = new JButton("Distribution");
        tf_refband = new JFormattedTextField(new Double(25D));
        rbtn_integrate = new JRadioButton("Integrate");
        type = new ButtonGroup();
        btn_Gen = new JButton("Generate and show samples");
        btn_Close = new JButton("Close");
        tf_Events = new JFormattedTextField(dialogFormats.getIntegerFactory(), new Integer(1000));
        lbl_Dist = new JLabel("Frequency Difference (MHz)");
        lbl_refband = new JLabel("VR Bandwidth (KHz)");
        lbl_func = new JLabel("Define Unwanted Function");
        lbl_Events = new JLabel("Specify number of samples:");
        displaySignal = new DialogDisplaySignal(this, "Event", "Function Value");
        functionDialog = new DialogFunction2Define(this, true);
        distDialog = new DistributionDialog(this, true);
        getContentPane().setLayout(new BorderLayout());
        JPanel center = new JPanel(new LabeledPairLayout());
        center.add(lbl_Dist, "label");
        center.add(btn_diff, "field");
        center.add(lbl_refband, "label");
        center.add(tf_refband, "field");
        center.add(lbl_func, "label");
        center.add(btn_function, "field");
        type.add(rbtn_integrate);
        rbtn_integrate.setSelected(true);
        center.add(new JLabel(""), "label");
        center.add(rbtn_integrate, "field");
        center.add(new JLabel(""), "label");
        center.add(lbl_Events, "label");
        center.add(tf_Events, "field");
        JPanel south = new JPanel();
        south.add(btn_Gen);
        south.add(btn_Close);
        getContentPane().add(center, "Center");
        getContentPane().add(south, "South");
        btn_diff.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = distDialog.showDistributionDialog(difference, "Configure Distribution");
                if(accept)
                    difference = distDialog.getDistributionable();
            }

            final FunctionTestDialog this$0;

            
            {
                this$0 = FunctionTestDialog.this;
                super();
            }
        }
);
        btn_function.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                boolean accept = functionDialog.show(function, "Configure Function");
                if(accept)
                    function = functionDialog.getFunction();
            }

            final FunctionTestDialog this$0;

            
            {
                this$0 = FunctionTestDialog.this;
                super();
            }
        }
);
        btn_Gen.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                int events = ((Number)tf_Events.getValue()).intValue();
                if(function instanceof DiscreteFunction2)
                    ((DiscreteFunction2)function).storePoints();
                function.normalize();
                double ref = ((Number)tf_refband.getValue()).doubleValue() / 1000D;
                double samples[] = new double[events];
                for(int i = 0; i < samples.length; i++)
                {
                    if(rbtn_integrate.isSelected())
                    {
                        try
                        {
                            samples[i] = function.integrate(difference.trial(), ref);
                        }
                        catch(FunctionException ex)
                        {
                            FunctionTestDialog.LOG.error("An Error occured", ex);
                        }
                        continue;
                    }
                    try
                    {
                        samples[i] = function.evaluate(difference.trial(), ref);
                    }
                    catch(FunctionException ex)
                    {
                        FunctionTestDialog.LOG.error("An Error occured", ex);
                    }
                }

                if(function instanceof DiscreteFunction2)
                    ((DiscreteFunction2)function).resetPoints();
                displaySignal.show(samples, (new StringBuilder()).append(events).append(" samples from specified function").toString());
            }

            final FunctionTestDialog this$0;

            
            {
                this$0 = FunctionTestDialog.this;
                super();
            }
        }
);
        btn_Close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                setVisible(false);
            }

            final FunctionTestDialog this$0;

            
            {
                this$0 = FunctionTestDialog.this;
                super();
            }
        }
);
        ((JComponent)getContentPane()).setBorder(new TitledBorder("Configure Function Test"));
        pack();
        setLocationRelativeTo(owner);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/FunctionTestDialog);
    private DialogDisplaySignal displaySignal;
    private DialogFunction2Define functionDialog;
    private DistributionDialog distDialog;
    private static final SeamcatTextFieldFormats dialogFormats = new SeamcatTextFieldFormats();
    private static final String TITLE = "Rel. Unwanted Function";
    private Distribution difference;
    private Function2 function;
    private JButton btn_function;
    private JButton btn_diff;
    private JFormattedTextField tf_refband;
    private JRadioButton rbtn_integrate;
    private ButtonGroup type;
    private JButton btn_Gen;
    private JButton btn_Close;
    private JFormattedTextField tf_Events;
    private JLabel lbl_Dist;
    private JLabel lbl_refband;
    private JLabel lbl_func;
    private JLabel lbl_Events;












}
