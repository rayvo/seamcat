// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   FormattedCalculatorField.java

package org.seamcat.calculator;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.HashMap;
import javax.swing.*;
import org.apache.log4j.BasicConfigurator;
import org.seamcat.presentation.*;

// Referenced classes of package org.seamcat.calculator:
//            ImageIconBorder, Calculator

public class FormattedCalculatorField extends JFormattedTextField
{

    public FormattedCalculatorField(Window _owner)
    {
        super(SeamcatTextFieldFormats.getDoubleFactory());
        owner = _owner;
        setBorder(new ImageIconBorder(this, SeamcatIcons.getImageIcon("SEAMCAT_ICON_CALCULATOR", 0), SeamcatIcons.getImageIcon("SEAMCAT_ICON_CALCULATOR", 3)));
        setColumns(8);
        setHorizontalAlignment(4);
        addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
        addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e)
            {
                if(e.getPoint().getX() >= getSize().getWidth() - 16D)
                {
                    if(isEnabled())
                    {
                        try
                        {
                            commitEdit();
                        }
                        catch(ParseException ex)
                        {
                            setValue(Integer.valueOf(0));
                        }
                        try
                        {
                            if(!FormattedCalculatorField.CALCULATORS.containsKey(owner))
                                FormattedCalculatorField.CALCULATORS.put(owner, (owner instanceof JDialog) ? ((Object) (new Calculator((JDialog)owner))) : ((Object) (new Calculator((Frame)owner))));
                            ((Calculator)FormattedCalculatorField.CALCULATORS.get(owner)).show(FormattedCalculatorField.this);
                        }
                        catch(Exception ex) { }
                    }
                } else
                {
                    requestFocus();
                }
            }

            public void mouseEntered(MouseEvent mouseevent)
            {
            }

            public void mouseExited(MouseEvent e)
            {
                setCursor(Cursor.getPredefinedCursor(0));
            }

            public void mousePressed(MouseEvent mouseevent)
            {
            }

            public void mouseReleased(MouseEvent mouseevent)
            {
            }

            final FormattedCalculatorField this$0;

            
            {
                this$0 = FormattedCalculatorField.this;
                super();
            }
        }
);
        addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent mouseevent)
            {
            }

            public void mouseMoved(MouseEvent e)
            {
                if(e.getPoint().getX() >= getSize().getWidth() - 16D)
                    setCursor(Cursor.getPredefinedCursor(0));
                else
                    setCursor(Cursor.getPredefinedCursor(2));
            }

            final FormattedCalculatorField this$0;

            
            {
                this$0 = FormattedCalculatorField.this;
                super();
            }
        }
);
        getInputMap(0).put(KeyStroke.getKeyStroke(82, 2), "calculator");
        getActionMap().put("calculator", new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                try
                {
                    commitEdit();
                }
                catch(ParseException ex)
                {
                    setValue(Integer.valueOf(0));
                }
                try
                {
                    if(!FormattedCalculatorField.CALCULATORS.containsKey(owner))
                        FormattedCalculatorField.CALCULATORS.put(owner, (owner instanceof JDialog) ? ((Object) (new Calculator((JDialog)owner))) : ((Object) (new Calculator((Frame)owner))));
                    ((Calculator)FormattedCalculatorField.CALCULATORS.get(owner)).show(FormattedCalculatorField.this);
                }
                catch(Exception ex) { }
            }

            final FormattedCalculatorField this$0;

            
            {
                this$0 = FormattedCalculatorField.this;
                super();
            }
        }
);
    }

    public FormattedCalculatorField(double value, Window owner)
    {
        this(owner);
        setValue(Double.valueOf(value));
    }

    public static void main(String args[])
    {
        BasicConfigurator.configure();
        JFrame frame = new JFrame("BorderTest");
        frame.getContentPane().setLayout(new LabeledPairLayoutWithUnit());
        frame.getContentPane().add(new JLabel("Test Field1"), "label");
        frame.getContentPane().add(new FormattedCalculatorField(frame), "field");
        frame.getContentPane().add(new JLabel("Test Unit1"), "unit");
        frame.getContentPane().add(new JLabel("Test Field2"), "label");
        frame.getContentPane().add(new FormattedCalculatorField(frame), "field");
        frame.getContentPane().add(new JLabel("Test Unit2"), "unit");
        frame.setDefaultCloseOperation(3);
        frame.setSize(200, 100);
        frame.setLocation(600, 600);
        frame.setVisible(true);
    }

    private Calculator calc;
    private static final String CALCULATOR = "calculator";
    private static final HashMap CALCULATORS = new HashMap();
    private Window owner;



}