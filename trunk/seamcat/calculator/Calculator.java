// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Calculator.java

package org.seamcat.calculator;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import org.apache.log4j.*;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.presentation.LabelAppender;
import org.seamcat.presentation.SeamcatIcons;

// Referenced classes of package org.seamcat.calculator:
//            CalculatorButton, MemoryButton, FormattedCalculatorField

public class Calculator extends JDialog
{
    private static final class BinaryOperation extends Enum
    {

        public static BinaryOperation[] values()
        {
            return (BinaryOperation[])$VALUES.clone();
        }

        public static BinaryOperation valueOf(String name)
        {
            return (BinaryOperation)Enum.valueOf(org/seamcat/calculator/Calculator$BinaryOperation, name);
        }

        public static final BinaryOperation Add;
        public static final BinaryOperation Substract;
        public static final BinaryOperation Multiply;
        public static final BinaryOperation Divide;
        private static final BinaryOperation $VALUES[];

        static 
        {
            Add = new BinaryOperation("Add", 0);
            Substract = new BinaryOperation("Substract", 1);
            Multiply = new BinaryOperation("Multiply", 2);
            Divide = new BinaryOperation("Divide", 3);
            $VALUES = (new BinaryOperation[] {
                Add, Substract, Multiply, Divide
            });
        }

        private BinaryOperation(String s, int i)
        {
            super(s, i);
        }
    }


    public Calculator(Frame owner)
    {
        super(owner, true);
        statusLabel = new JLabel("SEAMCAT Calculator");
        display = new JFormattedTextField();
        comma = new CalculatorButton(null);
        change_sign = new CalculatorButton(null);
        tan = new CalculatorButton("Y");
        log10 = new CalculatorButton("U");
        aCosD = new CalculatorButton("I");
        aSinD = new CalculatorButton("X");
        aTanD = new CalculatorButton("P");
        sinH = new CalculatorButton("S");
        cosH = new CalculatorButton("D");
        tanH = new CalculatorButton("F");
        ee = new CalculatorButton("J");
        min = new CalculatorButton("K");
        stdDev = new CalculatorButton("L");
        average = new CalculatorButton("Z");
        pow = new CalculatorButton("L");
        powerSum = new CalculatorButton("V");
        floor = new CalculatorButton("N");
        ceil = new CalculatorButton(null);
        clear = new CalculatorButton(null);
        clearAll = new CalculatorButton(null);
        list_separate = new CalculatorButton(null);
        dbm2watt = new CalculatorButton("Q");
        watt2dbm = new CalculatorButton("W");
        sin = new CalculatorButton("M");
        cos = new CalculatorButton("R");
        sqrt = new CalculatorButton("T");
        cbrt = new CalculatorButton("O");
        dgr2rad = new CalculatorButton(null);
        rad2dgr = new CalculatorButton(null);
        random = new CalculatorButton("G");
        minus = new CalculatorButton(null);
        add = new CalculatorButton(null);
        multiply = new CalculatorButton(null);
        divide = new CalculatorButton(null);
        equals = new CalculatorButton(null);
        transfer = new CalculatorButton(null);
        constant_pi = new CalculatorButton(null);
        constant_e = new CalculatorButton(null);
        constant_sqrt2 = new CalculatorButton(null);
        constant_sqrt3 = new CalculatorButton(null);
        constant_boltzmann = new CalculatorButton(null);
        tempEqualsValue = 0.0D;
        lastActionWasEquals = false;
        tempBinaryOperatorValue = 0.0D;
        insideBinaryOperation = false;
        firstEntryOfBinaryOperation = false;
        transferValue = false;
        numberAction = new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(firstEntryOfBinaryOperation)
                {
                    firstEntryOfBinaryOperation = false;
                    lastActionWasEquals = false;
                    removeLastEntry();
                }
                numberActionPerformed(e.getActionCommand());
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
;
        unique = new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(display.getText().indexOf(e.getActionCommand()) <= -1)
                    display.setText((new StringBuilder()).append(display.getText()).append(e.getActionCommand()).toString());
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
;
        init();
        setLocationRelativeTo(owner);
    }

    public Calculator(JDialog owner)
    {
        super(owner, true);
        statusLabel = new JLabel("SEAMCAT Calculator");
        display = new JFormattedTextField();
        comma = new CalculatorButton(null);
        change_sign = new CalculatorButton(null);
        tan = new CalculatorButton("Y");
        log10 = new CalculatorButton("U");
        aCosD = new CalculatorButton("I");
        aSinD = new CalculatorButton("X");
        aTanD = new CalculatorButton("P");
        sinH = new CalculatorButton("S");
        cosH = new CalculatorButton("D");
        tanH = new CalculatorButton("F");
        ee = new CalculatorButton("J");
        min = new CalculatorButton("K");
        stdDev = new CalculatorButton("L");
        average = new CalculatorButton("Z");
        pow = new CalculatorButton("L");
        powerSum = new CalculatorButton("V");
        floor = new CalculatorButton("N");
        ceil = new CalculatorButton(null);
        clear = new CalculatorButton(null);
        clearAll = new CalculatorButton(null);
        list_separate = new CalculatorButton(null);
        dbm2watt = new CalculatorButton("Q");
        watt2dbm = new CalculatorButton("W");
        sin = new CalculatorButton("M");
        cos = new CalculatorButton("R");
        sqrt = new CalculatorButton("T");
        cbrt = new CalculatorButton("O");
        dgr2rad = new CalculatorButton(null);
        rad2dgr = new CalculatorButton(null);
        random = new CalculatorButton("G");
        minus = new CalculatorButton(null);
        add = new CalculatorButton(null);
        multiply = new CalculatorButton(null);
        divide = new CalculatorButton(null);
        equals = new CalculatorButton(null);
        transfer = new CalculatorButton(null);
        constant_pi = new CalculatorButton(null);
        constant_e = new CalculatorButton(null);
        constant_sqrt2 = new CalculatorButton(null);
        constant_sqrt3 = new CalculatorButton(null);
        constant_boltzmann = new CalculatorButton(null);
        tempEqualsValue = 0.0D;
        lastActionWasEquals = false;
        tempBinaryOperatorValue = 0.0D;
        insideBinaryOperation = false;
        firstEntryOfBinaryOperation = false;
        transferValue = false;
        numberAction = new _cls1();
        unique = new _cls2();
        init();
        setLocationRelativeTo(owner);
    }

    private void init()
    {
        setTitle("SEAMCAT Calculator");
        initializePanel();
        pack();
        transfer.setVisible(false);
        setResizable(false);
        setLocation(100, 100);
        registerKeyListeners();
        LOG.addAppender(new LabelAppender(statusLabel));
        LOG.setLevel(Level.INFO);
    }

    private void registerKeyListeners()
    {
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(27, 0), "cancel-dialog");
        ((JPanel)getContentPane()).getActionMap().put("cancel-dialog", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                setVisible(false);
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(109, 0), "-");
        ((JPanel)getContentPane()).getActionMap().put("-", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                minus.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(107, 0), "+");
        ((JPanel)getContentPane()).getActionMap().put("+", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                add.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(106, 0), "*");
        ((JPanel)getContentPane()).getActionMap().put("*", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                multiply.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(111, 0), "/");
        ((JPanel)getContentPane()).getActionMap().put("/", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                divide.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(59, 0), ";");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(107, 1), ";");
        ((JPanel)getContentPane()).getActionMap().put(";", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                list_separate.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(109, 1), "--");
        ((JPanel)getContentPane()).getActionMap().put("--", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                change_sign.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(66, 0), "constant-B");
        ((JPanel)getContentPane()).getActionMap().put("constant-B", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                constant_boltzmann.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(69, 0), "constant-E");
        ((JPanel)getContentPane()).getActionMap().put("constant-E", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                constant_e.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(110, 0), ",");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(44, 0), ",");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(46, 0), ",");
        ((JPanel)getContentPane()).getActionMap().put(",", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                if(firstEntryOfBinaryOperation)
                {
                    firstEntryOfBinaryOperation = false;
                    lastActionWasEquals = false;
                    removeLastEntry();
                }
                comma.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        display.getInputMap(0).put(KeyStroke.getKeyStroke(8, 0), "C");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(8, 0), "C");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(67, 0), "C");
        ((JPanel)getContentPane()).getActionMap().put("C", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                clear.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(127, 0), "CA");
        display.getInputMap(0).put(KeyStroke.getKeyStroke(127, 0), "CA");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(65, 0), "CA");
        ((JPanel)getContentPane()).getActionMap().put("CA", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                clearAll.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(35, 0), "==");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(61, 0), "==");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(10, 0), "==");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(32, 0), "==");
        ((JPanel)getContentPane()).getActionMap().put("==", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                equals.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(10, 2), "transfer");
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(155, 0), "transfer");
        ((JPanel)getContentPane()).getActionMap().put("transfer", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                transfer.doClick();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        display.getInputMap(0).put(KeyStroke.getKeyStroke(10, 0), "==");
    }

    public void show(FormattedCalculatorField field)
    {
        clearDisplay();
        lastActionWasEquals = false;
        transferValue = false;
        try
        {
            setValue(((Number)field.getValue()).doubleValue());
        }
        catch(Exception e) { }
        transfer.setVisible(true);
        setVisible(true);
        statusLabel.setText("");
        try
        {
            if(transferValue && !getValueString().equals(""))
                field.setValue(Double.valueOf(getValue()));
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, "Unable to transfer result to field!", "Field error", 2);
        }
        transfer.setVisible(false);
    }

    void addFillComponents(Container panel, int cols[], int rows[])
    {
        Dimension filler = new Dimension(10, 10);
        boolean filled_cell_11 = false;
        CellConstraints cc = new CellConstraints();
        if(cols.length > 0 && rows.length > 0 && cols[0] == 1 && rows[0] == 1)
        {
            panel.add(Box.createRigidArea(filler), cc.xy(1, 1));
            filled_cell_11 = true;
        }
        for(int index = 0; index < cols.length; index++)
            if(cols[index] != 1 || !filled_cell_11)
                panel.add(Box.createRigidArea(filler), cc.xy(cols[index], 1));

        for(int index = 0; index < rows.length; index++)
            if(rows[index] != 1 || !filled_cell_11)
                panel.add(Box.createRigidArea(filler), cc.xy(1, rows[index]));

    }

    public JPanel createPanel()
    {
        JPanel mainPanel = new JPanel();
        FormLayout formlayout = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE", "CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
        CellConstraints cc = new CellConstraints();
        mainPanel.setLayout(formlayout);
        mainPanel.add(generateNumericButton("7"), cc.xy(13, 7));
        mainPanel.add(generateNumericButton("8"), cc.xy(14, 7));
        mainPanel.add(generateNumericButton("9"), cc.xy(15, 7));
        mainPanel.add(generateNumericButton("4"), cc.xy(13, 9));
        mainPanel.add(generateNumericButton("5"), cc.xy(14, 9));
        mainPanel.add(generateNumericButton("6"), cc.xy(15, 9));
        mainPanel.add(generateNumericButton("1"), cc.xy(13, 11));
        mainPanel.add(generateNumericButton("2"), cc.xy(14, 11));
        mainPanel.add(generateNumericButton("3"), cc.xy(15, 11));
        mainPanel.add(generateNumericButton("0"), cc.xy(13, 13));
        comma.setActionCommand(".");
        comma.setText(".");
        comma.setMargin(new Insets(2, 5, 2, 5));
        comma.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                String str = display.getText();
                if(display.getText().indexOf(";") > -1)
                    str = str.substring(str.lastIndexOf(";"));
                if(str.indexOf(".") < 0)
                    if(str.length() == 0)
                        display.setText((new StringBuilder()).append(display.getText()).append("0.").toString());
                    else
                        display.setText((new StringBuilder()).append(display.getText()).append(".").toString());
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(comma, cc.xy(14, 13));
        transfer.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_TRANSFER_RESULT"));
        transfer.setToolTipText("Close and copy result to SEAMCAT [INSERT]");
        transfer.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                transferValue = true;
                setVisible(false);
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(transfer, cc.xy(18, 15));
        change_sign.setActionCommand("+ / -");
        change_sign.setText("+ / -");
        change_sign.setToolTipText("Change sign of current input [SHIFT + MINUS]");
        change_sign.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(display.getText().indexOf(";") > -1)
                {
                    String str = display.getText();
                    String nmb = str.substring(str.lastIndexOf(";") + 1);
                    if(nmb.indexOf("-") > -1)
                        nmb = nmb.substring(1);
                    else
                        nmb = (new StringBuilder()).append("-").append(nmb).toString();
                    display.setText((new StringBuilder()).append(str.substring(0, str.lastIndexOf(";") + 1)).append(nmb).toString());
                } else
                {
                    String str = display.getText();
                    if(str.length() > 0 && str.charAt(0) == '-')
                        str = str.substring(1);
                    else
                        str = (new StringBuilder()).append("-").append(str).toString();
                    display.setText(str);
                }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(change_sign, cc.xy(18, 5));
        display.setFocusLostBehavior(0);
        display.setHorizontalAlignment(4);
        display.setEditable(false);
        display.setEnabled(true);
        mainPanel.add(display, cc.xywh(2, 3, 21, 1));
        tan.setActionCommand("tanD");
        tan.setText("tanD");
        tan.setToolTipText("Internal SEAMCAT function: Mathematics.tanD(double)");
        tan.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.tanD(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.tanD (").append(value).append(") = ").append(result).toString());
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(tan, cc.xy(2, 7));
        log10.setActionCommand("Log10");
        log10.setText("Log10");
        log10.setToolTipText("Internal java function: Math.log10(double)");
        log10.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Math.log10(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.log10 (").append(value).append(") = ").append(result).toString());
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(log10, cc.xy(4, 7));
        aCosD.setActionCommand("acosD");
        aCosD.setText("acosD");
        aCosD.setToolTipText("Internal SEAMCAT function: Mathematics.acosD(double)");
        aCosD.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.acosD(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.acosD (").append(value).append(") = ").append(result).toString());
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(aCosD, cc.xy(6, 7));
        aSinD.setActionCommand("asinD");
        aSinD.setText("asinD");
        aSinD.setToolTipText("Internal SEAMCAT function: Mathematics.asinD(double)");
        aSinD.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.asinD(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.asinD (").append(value).append(") = ").append(result).toString());
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(aSinD, cc.xy(8, 7));
        aTanD.setActionCommand("atanD");
        aTanD.setText("atanD");
        aTanD.setToolTipText("Internal SEAMCAT function: Mathematics.atanD(double)");
        aTanD.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.atanD(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.atanD (").append(value).append(") = ").append(result).toString());
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(aTanD, cc.xy(2, 9));
        sinH.setActionCommand("sinh");
        sinH.setText("sinh");
        sinH.setToolTipText("Internal SEAMCAT function: Mathematics.sinh(double)");
        sinH.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.sinh(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.sinh (").append(value).append(") = ").append(result).toString());
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(sinH, cc.xy(4, 9));
        cosH.setActionCommand("cosh");
        cosH.setText("cosh");
        cosH.setToolTipText("Internal SEAMCAT function: Mathematics.cosh(double)");
        cosH.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.cosh(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.cosh (").append(value).append(") = ").append(result).toString());
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(cosH, cc.xy(6, 9));
        tanH.setActionCommand("tanh");
        tanH.setText("tanh");
        tanH.setToolTipText("Internal SEAMCAT function: Mathematics.tanhD(double)");
        tanH.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.tanh(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.tanh (").append(value).append(") = ").append(result).toString());
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(tanH, cc.xy(8, 9));
        ee.setActionCommand("Max");
        ee.setText("<html>X * 10<sup>y</sup>");
        ee.setToolTipText("Internal SEAMCAT function: X * 10^Y");
        ee.setForeground(Color.RED);
        ee.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                double val[] = getValues();
                if(val.length == 2)
                {
                    double result = val[0] * Math.pow(10D, val[1]);
                    clearDisplay();
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of ").append(val[0]).append(" * 10 ^ ").append(val[1]).append(" = ").append(result).toString());
                } else
                {
                    JOptionPane.showMessageDialog(Calculator.this, "XeY is only applicable to lists with 2 elements");
                }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(ee, cc.xy(2, 11));
        min.setActionCommand("Min");
        min.setText("<html>X<sup>y</sup>");
        min.setToolTipText("Internal SEAMCAT function: Math.pow(double, double)");
        min.setForeground(Color.RED);
        min.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                double values[] = getValues();
                if(values.length == 2)
                {
                    double result = Math.pow(values[0], values[1]);
                    clearDisplay();
                    setValue(result);
                    StringBuffer val = new StringBuffer("Result of Math.pow ({");
                    for(int i = 0; i < values.length; i++)
                    {
                        val.append(values[i]);
                        if(i + 1 < values.length)
                            val.append(";");
                    }

                    val.append((new StringBuilder()).append("}) = ").append(result).toString());
                    Calculator.LOG.debug(val.toString());
                } else
                {
                    JOptionPane.showMessageDialog(Calculator.this, "Pow (x;y) is only applicable to lists with 2 elements");
                }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(min, cc.xy(4, 11));
        stdDev.setActionCommand("Std. Dev.");
        stdDev.setText("Std. Dev.");
        stdDev.setToolTipText("Internal SEAMCAT function: Mathematics.getStdDev(double[])");
        stdDev.setForeground(Color.RED);
        stdDev.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                double values[] = getValues();
                double result = Mathematics.getStdDev(values);
                clearDisplay();
                setValue(result);
                StringBuffer val = new StringBuffer("Result of Mathematics.getStdDev ({");
                for(int i = 0; i < values.length; i++)
                {
                    val.append(values[i]);
                    if(i + 1 < values.length)
                        val.append(";");
                }

                val.append((new StringBuilder()).append("}) = ").append(result).toString());
                Calculator.LOG.debug(val.toString());
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(stdDev, cc.xy(6, 11));
        average.setActionCommand("Average");
        average.setText("Average");
        average.setToolTipText("Internal SEAMCAT function: Mathematics.getAverage(double[])");
        average.setForeground(Color.RED);
        average.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                double values[] = getValues();
                double result = Mathematics.getAverage(values);
                clearDisplay();
                setValue(result);
                StringBuffer val = new StringBuffer("Result of Mathematics.getAverage ({");
                for(int i = 0; i < values.length; i++)
                {
                    val.append(values[i]);
                    if(i + 1 < values.length)
                        val.append(";");
                }

                val.append((new StringBuilder()).append("}) = ").append(result).toString());
                Calculator.LOG.debug(val.toString());
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(average, cc.xy(8, 11));
        pow.setActionCommand("Pow (x;y)");
        pow.setText("Pow (x;y)");
        pow.setToolTipText("Internal Java function: Math.pow(x, y)");
        pow.setForeground(Color.RED);
        pow.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                double val[] = getValues();
                if(val.length == 2)
                {
                    double result = Math.pow(val[0], val[1]);
                    clearDisplay();
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.pow (").append(val[0]).append(", ").append(val[1]).append(") = ").append(result).toString());
                } else
                {
                    JOptionPane.showMessageDialog(Calculator.this, "Pow (x;y) is only applicable to lists with 2 elements");
                }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(pow, cc.xy(2, 13));
        powerSum.setActionCommand("\u03A3 Power");
        powerSum.setText("\u03A3 Power");
        powerSum.setToolTipText("Internal SEAMCAT function: CDMASystem.powerSummation(double[])");
        powerSum.setForeground(Color.RED);
        powerSum.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                double values[] = getValues();
                double result = Calculator.powerSummation(values);
                clearDisplay();
                setValue(result);
                StringBuffer val = new StringBuffer("Result of CDMASystem.powerSummation ({");
                for(int i = 0; i < values.length; i++)
                {
                    val.append(values[i]);
                    if(i + 1 < values.length)
                        val.append(";");
                }

                val.append((new StringBuilder()).append("}) = ").append(result).toString());
                Calculator.LOG.debug(val.toString());
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(powerSum, cc.xy(4, 13));
        floor.setActionCommand("Floor");
        floor.setText("Floor");
        floor.setToolTipText("Internal Java function: Math.floor(double)");
        floor.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Math.floor(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.floor (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(floor, cc.xy(6, 13));
        ceil.setActionCommand("Ceil");
        ceil.setText("Ceil");
        ceil.setToolTipText("Internal Java function: Math.ceil(double)");
        ceil.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Math.ceil(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.ceil (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(ceil, cc.xy(8, 13));
        sqrt.setActionCommand("sqrt");
        sqrt.setText("SQRT");
        sqrt.setToolTipText("Squareroot");
        sqrt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Math.sqrt(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.sqrt (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(sqrt, cc.xy(10, 5));
        cbrt.setActionCommand("cbrt");
        cbrt.setText("CBRT");
        cbrt.setToolTipText("Cubicroot");
        cbrt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Math.cbrt(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.cbrt (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(cbrt, cc.xy(10, 7));
        random.setActionCommand("random");
        random.setText("Random");
        random.setToolTipText("Generates a random number between 0 and 1. Number is inserted at current cursor location.");
        random.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double result = Math.random();
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.random () = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(random, cc.xy(10, 9));
        dgr2rad.setActionCommand("Dgr->Rad");
        dgr2rad.setText("Dgr\u21D2Rad");
        dgr2rad.setToolTipText("Convert input from Degrees to Radians");
        dgr2rad.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Math.toRadians(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.toRadians (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(dgr2rad, cc.xy(10, 11));
        rad2dgr.setActionCommand("rad2dgr");
        rad2dgr.setText("Rad\u21D2Dgr");
        rad2dgr.setToolTipText("Convert input from Radians to Degrees");
        rad2dgr.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Math.toDegrees(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Math.toDegrees (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(rad2dgr, cc.xy(10, 13));
        clear.setActionCommand("Clear");
        clear.setText("C");
        clear.setToolTipText("Clear last entry [BACKSPACE]");
        clear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    if(firstEntryOfBinaryOperation)
                    {
                        removeLastEntry();
                        firstEntryOfBinaryOperation = false;
                    } else
                    {
                        display.setText(display.getText().substring(0, display.getText().length() - 1));
                        statusLabel.setText("");
                    }
                    equals.requestFocusInWindow();
                }
                catch(Exception ex) { }
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(clear, cc.xy(15, 5));
        clearAll.setActionCommand("CA");
        clearAll.setText("A");
        clearAll.setToolTipText("Clear All [DELETE]");
        clearAll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                clearDisplay();
                insideBinaryOperation = false;
                statusLabel.setText("");
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(clearAll, cc.xy(14, 5));
        list_separate.setActionCommand(";");
        list_separate.setName("List separate");
        list_separate.setText(";");
        list_separate.setForeground(Color.RED);
        list_separate.setToolTipText("Separate list entries [SHIFT + ADD]");
        list_separate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                firstEntryOfBinaryOperation = false;
                numberActionPerformed(e.getActionCommand());
                insideBinaryOperation = false;
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(list_separate, cc.xy(13, 5));
        dbm2watt.setActionCommand("dBm\u21D2Watt");
        dbm2watt.setText("dBm\u21D2Watt");
        dbm2watt.setToolTipText("Internal SEAMCAT function: CDMASystem.fromdBm2Watt(double)");
        dbm2watt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Calculator.fromdBm2Watt(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of CDMASystem.fromdBm2Watt (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(dbm2watt, cc.xy(2, 5));
        watt2dbm.setActionCommand("Watt\u21D2dBm");
        watt2dbm.setText("Watt\u21D2dBm");
        watt2dbm.setToolTipText("Internal SEAMCAT function: CDMASystem.fromWatt2dBm(double)");
        watt2dbm.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Calculator.fromWatt2dBm(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of CDMASystem.fromWatt2dBm (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(watt2dbm, cc.xy(4, 5));
        sin.setActionCommand("sinD");
        sin.setText("sinD");
        sin.setToolTipText("Internal SEAMCAT function: Mathematics.sinD(double)");
        sin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.sinD(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.sinD (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(sin, cc.xy(6, 5));
        cos.setActionCommand("cosD");
        cos.setText("cosD");
        cos.setToolTipText("Internal SEAMCAT function: Mathematics.cosD(double)");
        cos.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double value = getValue();
                    double result = Mathematics.cosD(value);
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Result of Mathematics.cosD (").append(value).append(") = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(cos, cc.xy(8, 5));
        minus.setText("-");
        minus.setToolTipText("Substraction");
        minus.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!insideBinaryOperation)
                    break MISSING_BLOCK_LABEL_57;
                if(getValueString().equals("") || firstEntryOfBinaryOperation)
                {
                    currentOperation = BinaryOperation.Substract;
                    return;
                }
                completeBinaryOperation();
                break MISSING_BLOCK_LABEL_72;
                tempBinaryOperatorValue = getValue();
                currentOperation = BinaryOperation.Substract;
                insideBinaryOperation = true;
                firstEntryOfBinaryOperation = true;
                setValue(tempBinaryOperatorValue);
                Calculator.LOG.debug((new StringBuilder()).append("Enter value to substract from ").append(tempBinaryOperatorValue).toString());
                break MISSING_BLOCK_LABEL_150;
                Exception ex;
                ex;
                equals.requestFocusInWindow();
                return;
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(minus, cc.xy(18, 7));
        add.setText("+");
        add.setToolTipText("Addition");
        add.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!insideBinaryOperation)
                    break MISSING_BLOCK_LABEL_57;
                if(getValueString().equals("") || firstEntryOfBinaryOperation)
                {
                    currentOperation = BinaryOperation.Add;
                    return;
                }
                completeBinaryOperation();
                break MISSING_BLOCK_LABEL_72;
                tempBinaryOperatorValue = getValue();
                currentOperation = BinaryOperation.Add;
                insideBinaryOperation = true;
                setValue(tempBinaryOperatorValue);
                firstEntryOfBinaryOperation = true;
                Calculator.LOG.debug((new StringBuilder()).append("Enter value to add to ").append(tempBinaryOperatorValue).toString());
                break MISSING_BLOCK_LABEL_150;
                Exception ex;
                ex;
                equals.requestFocusInWindow();
                return;
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(add, cc.xy(18, 9));
        multiply.setText("*");
        multiply.setToolTipText("Multiplication");
        multiply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!insideBinaryOperation)
                    break MISSING_BLOCK_LABEL_57;
                if(getValueString().equals("") || firstEntryOfBinaryOperation)
                {
                    currentOperation = BinaryOperation.Multiply;
                    return;
                }
                completeBinaryOperation();
                break MISSING_BLOCK_LABEL_72;
                tempBinaryOperatorValue = getValue();
                currentOperation = BinaryOperation.Multiply;
                insideBinaryOperation = true;
                firstEntryOfBinaryOperation = true;
                setValue(tempBinaryOperatorValue);
                Calculator.LOG.debug((new StringBuilder()).append("Enter value to multiply with ").append(tempBinaryOperatorValue).toString());
                break MISSING_BLOCK_LABEL_150;
                Exception ex;
                ex;
                equals.requestFocusInWindow();
                return;
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(multiply, cc.xy(18, 11));
        divide.setText("/");
        divide.setToolTipText("Division");
        divide.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!insideBinaryOperation)
                    break MISSING_BLOCK_LABEL_57;
                if(getValueString().equals("") || firstEntryOfBinaryOperation)
                {
                    currentOperation = BinaryOperation.Divide;
                    return;
                }
                completeBinaryOperation();
                break MISSING_BLOCK_LABEL_72;
                tempBinaryOperatorValue = getValue();
                currentOperation = BinaryOperation.Divide;
                insideBinaryOperation = true;
                firstEntryOfBinaryOperation = true;
                setValue(tempBinaryOperatorValue);
                Calculator.LOG.debug((new StringBuilder()).append("Enter value to divide ").append(tempBinaryOperatorValue).append(" with").toString());
                break MISSING_BLOCK_LABEL_155;
                Exception ex;
                ex;
                equals.requestFocusInWindow();
                return;
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(divide, cc.xy(18, 13));
        constant_pi.setText("pi");
        constant_pi.setToolTipText("The double value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter.");
        constant_pi.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double result = 3.1415926535897931D;
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Inserted Math.PI = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(constant_pi, cc.xy(20, 5));
        constant_e.setText("e");
        constant_e.setToolTipText("The double value that is closer than any other to e, the base of the natural logarithms.");
        constant_e.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double result = 2.7182818284590451D;
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Inserted Math.E = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(constant_e, cc.xy(20, 7));
        constant_sqrt2.setText("SQRT2");
        constant_sqrt2.setToolTipText("The double value that is closer than any other to sqrt(2)");
        constant_sqrt2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double result = Calculator.SQRT2;
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Inserted CDMASystem.SQRT2 = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(constant_sqrt2, cc.xy(20, 9));
        constant_sqrt3.setText("SQRT3");
        constant_sqrt3.setToolTipText("The double value that is closer than any other to sqrt(3)");
        constant_sqrt3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double result = Calculator.SQRT3;
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Inserted CDMASystem.SQRT3 = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(constant_sqrt3, cc.xy(20, 11));
        constant_boltzmann.setText("B");
        constant_boltzmann.setToolTipText("Boltzmann's constant, k = 1.38x10^-23 (Joules/K)");
        constant_boltzmann.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    double result = 1.3800000000000001E-023D;
                    setValue(result);
                    Calculator.LOG.debug((new StringBuilder()).append("Inserted 1.38e-23d = ").append(result).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(constant_boltzmann, cc.xy(20, 13));
        equals.setText("=");
        equals.setToolTipText("Equals [ENTER]");
        equals.setMargin(new Insets(2, 5, 2, 5));
        equals.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!insideBinaryOperation)
                    break MISSING_BLOCK_LABEL_51;
                if(getValueString().equals(""))
                    return;
                tempEqualsValue = getValue();
                completeBinaryOperation();
                break MISSING_BLOCK_LABEL_206;
                static class _cls60
                {

                    static final int $SwitchMap$org$seamcat$calculator$Calculator$BinaryOperation[];

                    static 
                    {
                        $SwitchMap$org$seamcat$calculator$Calculator$BinaryOperation = new int[BinaryOperation.values().length];
                        try
                        {
                            $SwitchMap$org$seamcat$calculator$Calculator$BinaryOperation[BinaryOperation.Add.ordinal()] = 1;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$calculator$Calculator$BinaryOperation[BinaryOperation.Substract.ordinal()] = 2;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$calculator$Calculator$BinaryOperation[BinaryOperation.Multiply.ordinal()] = 3;
                        }
                        catch(NoSuchFieldError ex) { }
                        try
                        {
                            $SwitchMap$org$seamcat$calculator$Calculator$BinaryOperation[BinaryOperation.Divide.ordinal()] = 4;
                        }
                        catch(NoSuchFieldError ex) { }
                    }
                }

                if(lastActionWasEquals)
                {
                    switch(_cls60..SwitchMap.org.seamcat.calculator.Calculator.BinaryOperation[currentOperation.ordinal()])
                    {
                    case 1: // '\001'
                        tempBinaryOperatorValue+= = tempEqualsValue;
                        break;

                    case 2: // '\002'
                        tempBinaryOperatorValue-= = tempEqualsValue;
                        break;

                    case 3: // '\003'
                        tempBinaryOperatorValue*= = tempEqualsValue;
                        break;

                    case 4: // '\004'
                        tempBinaryOperatorValue/= = tempEqualsValue;
                        break;
                    }
                } else
                {
                    currentOperation = BinaryOperation.Add;
                    tempEqualsValue = 0.0D;
                    tempBinaryOperatorValue = 0.0D;
                    return;
                }
                try
                {
                    setValue(tempBinaryOperatorValue);
                    insideBinaryOperation = false;
                    lastActionWasEquals = true;
                    Calculator.LOG.debug((new StringBuilder()).append("Result is ").append(tempBinaryOperatorValue).toString());
                }
                catch(Exception ex) { }
                equals.requestFocusInWindow();
                return;
            }

            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                super();
            }
        }
);
        mainPanel.add(equals, cc.xy(15, 13));
        mainPanel.add(new MemoryButton(1, this, 112), cc.xy(22, 5));
        mainPanel.add(new MemoryButton(2, this, 113), cc.xy(22, 7));
        mainPanel.add(new MemoryButton(3, this, 114), cc.xy(22, 9));
        mainPanel.add(new MemoryButton(4, this, 115), cc.xy(22, 11));
        mainPanel.add(new MemoryButton(5, this, 116), cc.xy(22, 13));
        mainPanel.add(statusLabel, cc.xywh(2, 15, 17, 1));
        addFillComponents(mainPanel, new int[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 
            21, 23
        }, new int[] {
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
            11, 12, 13, 14, 15
        });
        return mainPanel;
    }

    protected void initializePanel()
    {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(createPanel(), "Center");
    }

    private void removeLastEntry()
    {
        String str = null;
        str = display.getText();
        if(display.getText().indexOf(";") > -1)
        {
            str = str.substring(0, str.lastIndexOf(";") + 1);
            display.setText(str);
        } else
        {
            display.setText("");
        }
    }

    private String getValueString()
    {
        String str = null;
        str = display.getText();
        if(display.getText().indexOf(";") > -1)
            str = str.substring(str.lastIndexOf(";") + 1);
        return str;
    }

    public double getValue()
        throws Exception
    {
        try
        {
            double value = Double.parseDouble(getValueString());
            return value;
        }
        catch(NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Input does not appear to be a valid number!", "Input error", 2);
            throw ex;
        }
    }

    private double[] getValues()
    {
        String str = display.getText();
        if(str.charAt(str.length() - 1) == ';')
            str = str.substring(0, str.length() - 1);
        double values[];
        if(str.indexOf(';') > -1)
        {
            Scanner scanner = new Scanner(str.replace(".", ","));
            scanner.useDelimiter(";");
            ArrayList list = new ArrayList();
            while(scanner.hasNextDouble()) 
                try
                {
                    list.add(Double.valueOf(scanner.nextDouble()));
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            values = new double[list.size()];
            for(int i = 0; i < list.size(); i++)
                values[i] = ((Double)list.get(i)).doubleValue();

        } else
        {
            values = new double[1];
            try
            {
                values[0] = getValue();
            }
            catch(Exception ex)
            {
                LOG.error(ex);
            }
        }
        return values;
    }

    public void setValue(double result)
    {
        String str = Double.toString(result);
        if(display.getText().indexOf(";") > -1)
            display.setText((new StringBuilder()).append(display.getText().substring(0, display.getText().lastIndexOf(";") + 1)).append(str).toString());
        else
            display.setText(str);
        firstEntryOfBinaryOperation = true;
        lastActionWasEquals = false;
    }

    protected JButton generateNumericButton(String str)
    {
        final JButton but = new CalculatorButton(null);
        but.setActionCommand(str);
        but.setText(str);
        but.setForeground(Color.BLUE);
        but.addActionListener(numberAction);
        but.setMargin(new Insets(2, 5, 2, 5));
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke(str), str);
        ((JPanel)getContentPane()).getInputMap(2).put(KeyStroke.getKeyStroke((new StringBuilder()).append("NUMPAD").append(str).toString()), str);
        ((JPanel)getContentPane()).getActionMap().put(str, new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                but.doClick();
            }

            final JButton val$but;
            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                but = jbutton;
                super();
            }
        }
);
        return but;
    }

    private void completeBinaryOperation()
        throws Exception
    {
        switch(_cls60..SwitchMap.org.seamcat.calculator.Calculator.BinaryOperation[currentOperation.ordinal()])
        {
        case 1: // '\001'
            tempBinaryOperatorValue += getValue();
            break;

        case 2: // '\002'
            tempBinaryOperatorValue -= getValue();
            break;

        case 3: // '\003'
            tempBinaryOperatorValue *= getValue();
            break;

        case 4: // '\004'
            tempBinaryOperatorValue /= getValue();
            break;
        }
    }

    private void numberActionPerformed(String s)
    {
        display.setText((new StringBuilder()).append(display.getText()).append(s).toString());
    }

    private void clearDisplay()
    {
        display.setText("");
    }

    protected JButton generateModifyButton(final String value)
    {
        JButton button = new JButton(value);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                display.setText((new StringBuilder()).append(display.getText()).append(value).toString());
                equals.requestFocusInWindow();
            }

            final String val$value;
            final Calculator this$0;

            
            {
                this$0 = Calculator.this;
                value = s;
                super();
            }
        }
);
        return button;
    }

    private static final double fromdBm2Watt(double dbm)
    {
        return Math.pow(10D, (dbm - 30D) / 10D);
    }

    private static final double fromWatt2dBm(double watt)
    {
        if(watt == 0.0D)
        {
            LOG.warn("fromWatt2dBm - Returned -1000 instead of NaN");
            return -1000D;
        } else
        {
            return 10D * Math.log10(watt) + 30D;
        }
    }

    private static final double delogaritmize(double value)
    {
        if(value == 0.0D)
        {
            LOG.warn("Delog - Returned -1000 instead of NaN");
            return -1000D;
        } else
        {
            return Math.pow(10D, value / 10D);
        }
    }

    private static final double round(double d)
    {
        if(d == 0.0D)
            return 0.0D;
        else
            return StrictMath.rint(d * 1000D) / 1000D;
    }

    private static final transient double powerSummation(double powers[])
    {
        double total = 0.0D;
        double arr$[] = powers;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            double power = arr$[i$];
            total += Math.pow(10D, (power - 30D) / 10D);
        }

        return 10D * Math.log10(total) + 30D;
    }

    public void resetFocus()
    {
        equals.requestFocusInWindow();
    }

    public static void main(String args[])
    {
        try
        {
            if(UIManager.getSystemLookAndFeelClassName().equalsIgnoreCase("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"))
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            else
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception exception)
        {
            System.err.println(exception.getLocalizedMessage());
        }
        BasicConfigurator.configure();
        LOG.setLevel(Level.ERROR);
        JFrame main = new JFrame("Calculator");
        main.setIconImage(SeamcatIcons.getImage("SEAMCAT_ICON_CALCULATOR", 1));
        JDialog calc = new Calculator(main);
        main.setContentPane(calc.getContentPane());
        main.setDefaultCloseOperation(3);
        main.setSize(calc.getSize());
        main.setLocation(calc.getLocation());
        main.setResizable(false);
        main.setVisible(true);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/calculator/Calculator);
    private JLabel statusLabel;
    private JFormattedTextField display;
    private static final double SQRT3 = Math.sqrt(3D);
    private static final double SQRT2 = Math.sqrt(2D);
    private JButton comma;
    private JButton change_sign;
    private JButton tan;
    private JButton log10;
    private JButton aCosD;
    private JButton aSinD;
    private JButton aTanD;
    private JButton sinH;
    private JButton cosH;
    private JButton tanH;
    private JButton ee;
    private JButton min;
    private JButton stdDev;
    private JButton average;
    private JButton pow;
    private JButton powerSum;
    private JButton floor;
    private JButton ceil;
    private JButton clear;
    protected JButton clearAll;
    protected JButton list_separate;
    protected JButton dbm2watt;
    protected JButton watt2dbm;
    protected JButton sin;
    protected JButton cos;
    protected JButton sqrt;
    protected JButton cbrt;
    protected JButton dgr2rad;
    protected JButton rad2dgr;
    protected JButton random;
    protected JButton minus;
    protected JButton add;
    protected JButton multiply;
    protected JButton divide;
    protected JButton equals;
    protected JButton transfer;
    protected JButton constant_pi;
    protected JButton constant_e;
    protected JButton constant_sqrt2;
    protected JButton constant_sqrt3;
    protected JButton constant_boltzmann;
    private static final String CANCEL_DIALOG = "cancel-dialog";
    private static final String NUMBER_0 = "0";
    private static final String NUMBER_1 = "1";
    private static final String NUMBER_2 = "2";
    private static final String NUMBER_3 = "3";
    private static final String NUMBER_4 = "4";
    private static final String NUMBER_5 = "5";
    private static final String NUMBER_6 = "6";
    private static final String NUMBER_7 = "7";
    private static final String NUMBER_8 = "8";
    private static final String NUMBER_9 = "9";
    public static final String COMMA = ",";
    private static final String LIST = ";";
    private static final String SIGN = "--";
    private static final String CLEAR = "C";
    private static final String CLEARALL = "CA";
    private static final String ADD = "+";
    private static final String MINUS = "-";
    private static final String MULTIPLY = "*";
    private static final String DIVIDE = "/";
    private static final String EQUALS = "==";
    private static final String E = "constant-E";
    private static final String B = "constant-B";
    private static final String TRANSFER = "transfer";
    private double tempEqualsValue;
    private boolean lastActionWasEquals;
    private double tempBinaryOperatorValue;
    private boolean insideBinaryOperation;
    private boolean firstEntryOfBinaryOperation;
    private BinaryOperation currentOperation;
    private boolean transferValue;
    private final ActionListener numberAction;
    private final ActionListener unique;



































}