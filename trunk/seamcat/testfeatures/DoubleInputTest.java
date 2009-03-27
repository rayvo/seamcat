// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoubleInputTest.java

package org.seamcat.testfeatures;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import org.seamcat.presentation.LabeledPairLayout;
import org.seamcat.presentation.SeamcatTextFieldFormats;

public class DoubleInputTest extends JPanel
{

    private DoubleInputTest()
    {
        super(new LabeledPairLayout());
        add(new JLabel("Standard JTextField with postprocessing"), "label");
        tf1 = new JTextField();
        tf1.setText(Integer.toString(0));
        tf1.setHorizontalAlignment(4);
        add(tf1, "field");
        add(new JLabel("Standard JFormattedTextField"), "label");
        tf2 = new JFormattedTextField(Double.valueOf(0.0D));
        tf2.setHorizontalAlignment(4);
        add(tf2, "field");
        add(new JLabel("Seamcat Formatted Textfield"), "label");
        tf3 = new JFormattedTextField();
        tf3.setHorizontalAlignment(4);
        DefaultFormatterFactory f = new DefaultFormatterFactory();
        DefaultFormatter f1 = new DefaultFormatter();
        f1.setValueClass(java/lang/Double);
        f.setEditFormatter(f1);
        f1.install(tf3);
        tf3.setValue(Double.valueOf(0.0D));
        add(tf3, "field");
    }

    public static void main(String args1[])
    {
    }

    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private JTextField tf1;
    private JFormattedTextField tf2;
    private JFormattedTextField tf3;

}
