// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferenceCriterionPanel.java

package org.seamcat.presentation.components.interferencecalc;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.model.engines.ICEConfiguration;

public class InterferenceCriterionPanel extends JPanel
{

    public InterferenceCriterionPanel()
    {
        ci = new JRadioButton("C / I");
        cin = new JRadioButton("C / (I + N)");
        nin = new JRadioButton("(N + I) / N");
        in = new JRadioButton("I / N");
        ci.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    iceconf.setInterferenceCriterionType(1);
                }
                catch(NullPointerException ne) { }
            }

            final InterferenceCriterionPanel this$0;

            
            {
                this$0 = InterferenceCriterionPanel.this;
                super();
            }
        }
);
        cin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    iceconf.setInterferenceCriterionType(2);
                }
                catch(NullPointerException ne) { }
            }

            final InterferenceCriterionPanel this$0;

            
            {
                this$0 = InterferenceCriterionPanel.this;
                super();
            }
        }
);
        nin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    iceconf.setInterferenceCriterionType(3);
                }
                catch(NullPointerException ne) { }
            }

            final InterferenceCriterionPanel this$0;

            
            {
                this$0 = InterferenceCriterionPanel.this;
                super();
            }
        }
);
        in.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    iceconf.setInterferenceCriterionType(4);
                }
                catch(NullPointerException ne) { }
            }

            final InterferenceCriterionPanel this$0;

            
            {
                this$0 = InterferenceCriterionPanel.this;
                super();
            }
        }
);
        setLayout(new GridLayout(4, 1));
        setBorder(new TitledBorder("Interference Criterion"));
        ButtonGroup group = new ButtonGroup();
        group.add(ci);
        group.add(cin);
        group.add(nin);
        group.add(in);
        add(ci);
        add(cin);
        add(nin);
        add(in);
    }

    public void init(ICEConfiguration _iceconf)
    {
        iceconf = _iceconf;
        switch(iceconf.getInterferenceCriterionType())
        {
        case 1: // '\001'
            ci.setSelected(true);
            break;

        case 2: // '\002'
            cin.setSelected(true);
            break;

        case 3: // '\003'
            nin.setSelected(true);
            break;

        case 4: // '\004'
            in.setSelected(true);
            break;
        }
    }

    private JRadioButton ci;
    private JRadioButton cin;
    private JRadioButton nin;
    private JRadioButton in;
    private ICEConfiguration iceconf;

}
