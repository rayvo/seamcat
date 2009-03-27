// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatTextFieldFormats.java

package org.seamcat.presentation;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.*;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.*;
import org.apache.log4j.Logger;
import org.seamcat.presentation.components.DoubleCellEditor2;

public class SeamcatTextFieldFormats
{
    private static final class IntegerJTextFieldVerifier extends InputVerifier
    {

        public final boolean verify(JComponent input)
        {
            try
            {
                int value = Integer.parseInt(((JTextComponent)input).getText());
                return value >= min && value <= max;
            }
            catch(NumberFormatException e)
            {
                return false;
            }
        }

        private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/SeamcatTextFieldFormats$IntegerJTextFieldVerifier);
        private final int min;
        private final int max;


        public IntegerJTextFieldVerifier()
        {
            min = 0x80000000;
            max = 0x7fffffff;
        }

        public IntegerJTextFieldVerifier(int min, int max)
        {
            this.min = min;
            this.max = max;
        }
    }

    private static final class DoubleJTextFieldVerifier extends InputVerifier
    {

        public final boolean verify(JComponent input)
        {
            boolean valid;
            try
            {
                if(input instanceof JTextField)
                {
                    JTextField inputTextField = (JTextField)input;
                    double value = Double.parseDouble(inputTextField.getText());
                    valid = value >= min && value <= max;
                } else
                {
                    valid = false;
                }
            }
            catch(Exception e)
            {
                valid = false;
            }
            return valid;
        }

        private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/SeamcatTextFieldFormats$DoubleJTextFieldVerifier);
        private final double min;
        private final double max;


        public DoubleJTextFieldVerifier()
        {
            min = 4.9406564584124654E-324D;
            max = 1.7976931348623157E+308D;
        }

        public DoubleJTextFieldVerifier(double min, double max)
        {
            this.min = min;
            this.max = max;
        }
    }

    public static class DoubleCellRenderer extends DefaultTableCellRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Double _value = (Double)value;
            String _formattedValue;
            if(value == null)
                _formattedValue = "Undefined";
            else
                _formattedValue = doubleFormat.format(_value);
            JLabel testLabel = new JLabel(_formattedValue, 4);
            if(isSelected)
            {
                testLabel.setBackground(table.getSelectionBackground());
                testLabel.setOpaque(true);
                testLabel.setForeground(table.getSelectionForeground());
            }
            if(hasFocus)
            {
                testLabel.setForeground(table.getSelectionBackground());
                testLabel.setBackground(table.getSelectionForeground());
                testLabel.setOpaque(true);
            }
            return testLabel;
        }

        private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/SeamcatTextFieldFormats$DoubleCellRenderer);
        NumberFormat doubleFormat;


        public DoubleCellRenderer(NumberFormat nf)
        {
            doubleFormat = nf;
        }
    }


    public SeamcatTextFieldFormats()
    {
    }

    public static DefaultFormatter getDoubleFormatter()
    {
        return DOUBLE_FORMAT;
    }

    public static DefaultFormatter getIntegerFmt()
    {
        return INTEGER_FORMAT;
    }

    public static DefaultFormatterFactory getDoubleFactory()
    {
        return DOUBLE_FACTORY;
    }

    public static DefaultFormatterFactory getIntegerFactory()
    {
        return INTEGER_FACTORY;
    }

    public static DefaultFormatter getStringFmt()
    {
        return STRING_FORMAT;
    }

    public static DefaultFormatterFactory getStringFactory()
    {
        return STRING_FACTORY;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/SeamcatTextFieldFormats);
    private static final String DOUBLE_FORMAT_STRING = "#0.0########";
    private static final String INTEGER_FORMAT_STRING = "#0";
    private static final DefaultFormatter DOUBLE_FORMAT;
    private static final DefaultFormatter INTEGER_FORMAT;
    private static final DefaultFormatter STRING_FORMAT;
    public static final FocusListener SELECTALL_FOCUSHANDLER = new FocusListener() {

        public void focusGained(final FocusEvent e)
        {
            if(e.getComponent() instanceof JTextComponent)
                SwingUtilities.invokeLater(new Runnable() {

                    public void run()
                    {
                        ((JTextComponent)e.getComponent()).selectAll();
                    }

                    final FocusEvent val$e;
                    final _cls1 this$0;

                    
                    {
                        this$0 = _cls1.this;
                        e = focusevent;
                        super();
                    }
                }
);
        }

        public void focusLost(FocusEvent focusevent)
        {
        }

    }
;
    public static final DoubleCellRenderer DOUBLE_RENDERER;
    public static final DoubleCellEditor2 DOUBLE_EDITOR = new DoubleCellEditor2();
    public static final DefaultFormatterFactory DOUBLE_FACTORY;
    public static final DefaultFormatterFactory INTEGER_FACTORY;
    public static final DefaultFormatterFactory STRING_FACTORY;
    public static final InputVerifier DOUBLE_JTEXTFIELD_VERIFIER = new DoubleJTextFieldVerifier();
    public static final InputVerifier INTEGER_JTEXTFIELD_VERIFIER = new IntegerJTextFieldVerifier();

    static 
    {
        INTEGER_FORMAT = new NumberFormatter(new DecimalFormat("#0"));
        STRING_FORMAT = new DefaultFormatter();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.ENGLISH);
        DecimalFormat format = new DecimalFormat();
        format.setDecimalFormatSymbols(dfs);
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(15);
        DOUBLE_FORMAT = new NumberFormatter(format);
        DOUBLE_RENDERER = new DoubleCellRenderer(format);
        DOUBLE_FORMAT.setAllowsInvalid(false);
        INTEGER_FORMAT.setAllowsInvalid(false);
        DOUBLE_FACTORY = new DefaultFormatterFactory(DOUBLE_FORMAT, DOUBLE_FORMAT, DOUBLE_FORMAT);
        INTEGER_FACTORY = new DefaultFormatterFactory(INTEGER_FORMAT);
        STRING_FACTORY = new DefaultFormatterFactory(STRING_FORMAT, STRING_FORMAT, STRING_FORMAT);
    }
}
