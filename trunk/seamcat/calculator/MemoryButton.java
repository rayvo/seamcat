// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   MemoryButton.java

package org.seamcat.calculator;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.seamcat.presentation.SeamcatIcons;

// Referenced classes of package org.seamcat.calculator:
//            Calculator

public class MemoryButton extends JButton
{

    public MemoryButton(int _index, Calculator _calc, int keyevent)
    {
        super(String.valueOf(_index), SeamcatIcons.getImageIcon("SEAMCAT_ICON_CALCULATOR_MEMORY_EMPTY"));
        valueSet = false;
        index = _index;
        no_value_tool_tip = (new StringBuilder()).append("<html>Memory space ").append(index).append(": No value stored<br>").append("Click or press F").append(index).append(" to store value").toString();
        setToolTipText(no_value_tool_tip);
        calc = _calc;
        setMargin(new Insets(2, 2, 2, 2));
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(!valueSet || (e.getModifiers() & 1) == 1)
                    storeValue();
                else
                if((e.getModifiers() & 2) == 2)
                    resetValue();
                else
                    retrieveValue();
                calc.resetFocus();
            }

            final MemoryButton this$0;

            
            {
                this$0 = MemoryButton.this;
                super();
            }
        }
);
        getInputMap(2).put(KeyStroke.getKeyStroke(keyevent, 0), (new StringBuilder()).append("Store-f").append(index).toString());
        getActionMap().put((new StringBuilder()).append("Store-f").append(index).toString(), new AbstractAction() {

            public void actionPerformed(ActionEvent event)
            {
                doClick();
            }

            final MemoryButton this$0;

            
            {
                this$0 = MemoryButton.this;
                super();
            }
        }
);
    }

    public void storeValue()
    {
        try
        {
            double value = calc.getValue();
            VALUES.put(Integer.valueOf(index), Double.valueOf(value));
            setValueSet(true);
            LOG.debug((new StringBuilder()).append("Stored value [").append(value).append("] in memory position ").append(index).toString());
        }
        catch(Exception e) { }
    }

    public void resetValue()
    {
        VALUES.remove(Integer.valueOf(index));
        setValueSet(false);
    }

    public void retrieveValue()
    {
        if(valueSet)
        {
            calc.setValue(((Double)VALUES.get(Integer.valueOf(index))).doubleValue());
            LOG.debug((new StringBuilder()).append("Retrieved value [").append(VALUES.get(Integer.valueOf(index))).append("] from memory position ").append(index).toString());
        }
    }

    private void setValueSet(boolean value)
    {
        valueSet = value;
        if(valueSet)
        {
            setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CALCULATOR_MEMORY_FULL"));
            setToolTipText((new StringBuilder()).append("<html>Memory space ").append(index).append(": ").append(VALUES.get(Integer.valueOf(index))).append("<br>").append("Click or press F").append(index).append(" to use value<br>").append("Shift + Click to store new value<br>").append("Control + Click to clear value").toString());
        } else
        {
            valueSet = false;
            setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CALCULATOR_MEMORY_EMPTY"));
            setToolTipText(no_value_tool_tip);
        }
    }

    public void repaint()
    {
        setValueSet(VALUES.containsKey(Integer.valueOf(index)));
        super.repaint();
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/calculator/MemoryButton);
    private static final HashMap VALUES = new HashMap(5, 1.0F);
    private int index;
    private boolean valueSet;
    private Calculator calc;
    private boolean shiftIsDown;
    private String no_value_tool_tip;
    private static final String KEYMAP_PREFIX = "Store-f";



}