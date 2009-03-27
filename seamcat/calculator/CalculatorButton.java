// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CalculatorButton.java

package org.seamcat.calculator;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class CalculatorButton extends JButton
{

    public CalculatorButton(String _key)
    {
        key = _key;
        setMargin(new Insets(2, 2, 2, 2));
        getInputMap(0).put(KeyStroke.getKeyStroke(10, 0), "seamcat-no-action");
        getInputMap(1).put(KeyStroke.getKeyStroke(10, 0), "seamcat-no-action");
        getInputMap(2).put(KeyStroke.getKeyStroke(10, 0), "seamcat-no-action");
        if(key != null)
        {
            getInputMap(2).put(KeyStroke.getKeyStroke(key), "action");
            getActionMap().put("action", new AbstractAction() {

                public void actionPerformed(ActionEvent event)
                {
                    doClick();
                }

                final CalculatorButton this$0;

            
            {
                this$0 = CalculatorButton.this;
                super();
            }
            }
);
        }
    }

    public void setText(String s)
    {
        if(key == null)
            super.setText(s);
        else
            super.setText((new StringBuilder()).append(s).append(" [").append(key).append("]").toString());
    }

    private static final String NOACTION = "seamcat-no-action";
    private static final String ACTION = "action";
    private String key;
}