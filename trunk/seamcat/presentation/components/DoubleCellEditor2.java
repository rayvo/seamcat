// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoubleCellEditor2.java

package org.seamcat.presentation.components;

import java.text.ParseException;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

// Referenced classes of package org.seamcat.presentation.components:
//            ValidatorDocument

public class DoubleCellEditor2 extends DefaultCellEditor
{
    static class DoubleFormatter extends DefaultFormatter
    {

        public Object stringToValue(String string)
        {
            Double n;
            try
            {
                n = new Double(string.replace(',', '.'));
            }
            catch(NumberFormatException e)
            {
                n = null;
            }
            return n;
        }

        public String valueToString(Object value)
            throws ParseException
        {
            return value == null ? null : ((Number)value).toString();
        }

        DoubleFormatter()
        {
        }
    }


    public DoubleCellEditor2()
    {
        super(new JTextField());
        final JTextField textField = (JTextField)editorComponent;
        textField.removeActionListener(_flddelegate);
        final DoubleFormatter formatter = new DoubleFormatter();
        _flddelegate = new javax.swing.DefaultCellEditor.EditorDelegate() {

            public void setValue(Object value)
            {
                textField.setText(value == null ? "" : value.toString());
            }

            public Object getCellEditorValue()
            {
                return formatter.stringToValue(textField.getText());
            }

            final JTextField val$textField;
            final DoubleFormatter val$formatter;
            final DoubleCellEditor2 this$0;

            
            {
                this$0 = DoubleCellEditor2.this;
                textField = jtextfield;
                formatter = doubleformatter;
                super(DoubleCellEditor2.this);
            }
        }
;
        textField.setDocument(new ValidatorDocument(new ValidatorDocument.Type[] {
            ValidatorDocument.Type.INTEGERS, ValidatorDocument.Type.FLOAT_DELIMITERS, ValidatorDocument.Type.NEGATE
        }));
        textField.addActionListener(_flddelegate);
    }
}
