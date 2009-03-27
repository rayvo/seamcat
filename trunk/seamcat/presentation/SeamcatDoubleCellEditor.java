// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatDoubleCellEditor.java

package org.seamcat.presentation;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.text.*;
import java.util.Locale;
import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class SeamcatDoubleCellEditor extends DefaultCellEditor
{

    public SeamcatDoubleCellEditor()
    {
        this(4.9406564584124654E-324D, 1.7976931348623157E+308D);
    }

    public SeamcatDoubleCellEditor(double min, double max)
    {
        super(new JFormattedTextField());
        DEBUG = false;
        ftf = (JFormattedTextField)getComponent();
        minimum = Double.valueOf(min);
        maximum = Double.valueOf(max);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
        dfs.setDecimalSeparator(',');
        format = new DecimalFormat();
        format.setDecimalFormatSymbols(dfs);
        format.setMinimumFractionDigits(1);
        format.setMaximumFractionDigits(15);
        NumberFormatter dobFormatter = new NumberFormatter(format);
        dobFormatter.setMinimum(minimum);
        dobFormatter.setMaximum(maximum);
        ftf.setFormatterFactory(new DefaultFormatterFactory(dobFormatter));
        ftf.setValue(minimum);
        ftf.setHorizontalAlignment(11);
        ftf.setFocusLostBehavior(3);
        ftf.getInputMap().put(KeyStroke.getKeyStroke(10, 0), "check");
        ftf.getActionMap().put("check", new AbstractAction() {

            public void actionPerformed(ActionEvent e)
            {
                if(!ftf.isEditValid())
                {
                    if(userSaysRevert())
                        ftf.postActionEvent();
                } else
                {
                    try
                    {
                        ftf.commitEdit();
                        ftf.postActionEvent();
                    }
                    catch(ParseException exc) { }
                }
            }

            final SeamcatDoubleCellEditor this$0;

            
            {
                this$0 = SeamcatDoubleCellEditor.this;
                super();
            }
        }
);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
        JFormattedTextField ftf = (JFormattedTextField)super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ftf.setValue(value);
        return ftf;
    }

    public Object getCellEditorValue()
    {
        JFormattedTextField ftf = (JFormattedTextField)getComponent();
        Object o = ftf.getValue();
        if(o instanceof Number)
            return o;
        try
        {
            return format.parse(o.toString());
        }
        catch(Exception exc)
        {
            return null;
        }
    }

    public boolean stopCellEditing()
    {
        JFormattedTextField ftf = (JFormattedTextField)getComponent();
        if(ftf.isEditValid())
            try
            {
                ftf.commitEdit();
            }
            catch(ParseException exc) { }
        else
        if(!userSaysRevert())
            return false;
        return super.stopCellEditing();
    }

    protected boolean userSaysRevert()
    {
        Toolkit.getDefaultToolkit().beep();
        ftf.selectAll();
        Object options[] = {
            "Edit", "Revert"
        };
        int answer = JOptionPane.showOptionDialog(SwingUtilities.getWindowAncestor(ftf), (new StringBuilder()).append("The value must be a number between ").append(minimum).append(" and ").append(maximum).append(".\n").append("You can either continue editing ").append("or revert to the last valid value.").toString(), "Invalid Text Entered", 0, 0, null, options, options[1]);
        if(answer == 1)
        {
            ftf.setValue(ftf.getValue());
            return true;
        } else
        {
            return false;
        }
    }

    JFormattedTextField ftf;
    private DecimalFormat format;
    private Double minimum;
    private Double maximum;
    private boolean DEBUG;
}
