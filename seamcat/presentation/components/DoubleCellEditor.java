// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DoubleCellEditor.java

package org.seamcat.presentation.components;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import org.apache.log4j.Logger;

public class DoubleCellEditor extends DefaultCellEditor
{

    public DoubleCellEditor(final JFormattedTextField tf, NumberFormat nf)
    {
        super(tf);
        doubleFormat = nf;
        tf.setFocusLostBehavior(0);
        tf.setHorizontalAlignment(4);
        tf.setBorder(null);
        _flddelegate = new javax.swing.DefaultCellEditor.EditorDelegate() {

            public void setValue(Object param)
            {
                if(param != null)
                    System.out.printf("EditorValue is a <%s>\n", new Object[] {
                        param.getClass().getName()
                    });
                else
                    System.out.println("EditorValue is null");
                Double _value = (Double)param;
                if(_value == null)
                    tf.setValue(new Double(0.0D));
                else
                    tf.setValue(_value);
            }

            public Object getCellEditorValue()
            {
                System.out.println("getCellEditorValue()");
                try
                {
                    String _field = tf.getText();
                    Number _number = doubleFormat.parse(_field);
                    double _parsed = _number.doubleValue();
                    Double d = new Double(_parsed);
                    return d;
                }
                catch(ParseException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            final JFormattedTextField val$tf;
            final DoubleCellEditor this$0;

            
            {
                this$0 = DoubleCellEditor.this;
                tf = jformattedtextfield;
                super(DoubleCellEditor.this);
            }
        }
;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/components/DoubleCellEditor);
    private NumberFormat doubleFormat;


}
