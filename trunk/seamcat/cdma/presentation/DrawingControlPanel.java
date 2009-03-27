// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   DrawingControlPanel.java

package org.seamcat.cdma.presentation;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

// Referenced classes of package org.seamcat.cdma.presentation:
//            CDMAPlot

public class DrawingControlPanel extends JPanel
{

    public DrawingControlPanel(CDMAPlot _tp)
    {
        add(new JLabel("Plot: "));
        setBackground(Color.WHITE);
        tp = _tp;
        final JCheckBox c1 = new JCheckBox("Users");
        c1.setSelected(tp.isPlotUsers());
        c1.setBackground(Color.WHITE);
        c1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotUsers(c1.isSelected());
            }

            final JCheckBox val$c1;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c1 = jcheckbox;
                super();
            }
        }
);
        add(c1);
        final JCheckBox c3 = new JCheckBox("Dropped Users");
        c3.setSelected(tp.isPlotDroppedUsers());
        c3.setBackground(Color.WHITE);
        c3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotDroppedUsers(c3.isSelected());
            }

            final JCheckBox val$c3;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c3 = jcheckbox;
                super();
            }
        }
);
        add(c3);
        final JCheckBox c4 = new JCheckBox("Connection Lines");
        c4.setSelected(tp.isPlotConnectionLines());
        c4.setBackground(Color.WHITE);
        c4.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotConnectionLines(c4.isSelected());
            }

            final JCheckBox val$c4;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c4 = jcheckbox;
                super();
            }
        }
);
        add(c4);
        final JCheckBox c5 = new JCheckBox("TX Stats");
        c5.setSelected(tp.isPlotTxStats());
        c5.setBackground(Color.WHITE);
        c5.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotTxStats(c5.isSelected());
            }

            final JCheckBox val$c5;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c5 = jcheckbox;
                super();
            }
        }
);
        add(c5);
        final JCheckBox c6 = new JCheckBox("Antenna Pattern");
        c6.setSelected(tp.isPlotAntennaPattern());
        c6.setBackground(Color.WHITE);
        c6.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotAntennaPattern(c6.isSelected());
            }

            final JCheckBox val$c6;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c6 = jcheckbox;
                super();
            }
        }
);
        add(c6);
        final JCheckBox c7 = new JCheckBox("Cell Center");
        c7.setSelected(tp.isPlotCellCenter());
        c7.setBackground(Color.WHITE);
        c7.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotCellCenter(c7.isSelected());
            }

            final JCheckBox val$c7;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c7 = jcheckbox;
                super();
            }
        }
);
        add(c7);
        final JCheckBox c8 = new JCheckBox("External Interferers");
        c8.setSelected(tp.isPlotExternalInterferers());
        c8.setBackground(Color.WHITE);
        c8.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotExternalInterferers(c8.isSelected());
            }

            final JCheckBox val$c8;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c8 = jcheckbox;
                super();
            }
        }
);
        add(c8);
        final JCheckBox c9 = new JCheckBox("Cell ID#");
        c9.setSelected(tp.isPlotCellid());
        c9.setBackground(Color.WHITE);
        c9.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotCellid(c9.isSelected());
            }

            final JCheckBox val$c9;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c9 = jcheckbox;
                super();
            }
        }
);
        add(c9);
        final JCheckBox c10 = new JCheckBox("Legend");
        c10.setSelected(tp.isPlotLegend());
        c10.setBackground(Color.WHITE);
        c10.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotLegend(c10.isSelected());
            }

            final JCheckBox val$c10;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c10 = jcheckbox;
                super();
            }
        }
);
        add(c10);
        final JCheckBox c11 = new JCheckBox("Display tips");
        c11.setSelected(tp.isPlotHelp());
        c11.setBackground(Color.WHITE);
        c11.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                tp.setPlotHelp(c11.isSelected());
            }

            final JCheckBox val$c11;
            final DrawingControlPanel this$0;

            
            {
                this$0 = DrawingControlPanel.this;
                c11 = jcheckbox;
                super();
            }
        }
);
        add(c11);
    }

    private CDMAPlot tp;

}