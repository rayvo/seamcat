// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReferenceCellSelectionPanel.java

package org.seamcat.cdma.presentation;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.cdma.CDMASystem;

// Referenced classes of package org.seamcat.cdma.presentation:
//            ReferenceCellSelector

public class ReferenceCellSelectionPanel extends JPanel
{

    public ReferenceCellSelectionPanel()
    {
        super(new BorderLayout());
        plotWrapAround = new JCheckBox("Plot Wrap-Around structure");
        interferenceFromCluster = new JCheckBox("Measure Interference from entire cluster");
        center = new JRadioButton("Center of \"infinite\" network");
        left = new JRadioButton("Left hand side of network edge");
        right = new JRadioButton("Right hand side of network edge");
        selector = new ReferenceCellSelector();
        add(selector, "Center");
        ButtonGroup networkEdgeButtons = new ButtonGroup();
        networkEdgeButtons.add(center);
        networkEdgeButtons.add(left);
        networkEdgeButtons.add(right);
        center.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                selector.getCdma().setSimulateNetworkEdge(!center.isSelected());
                if(center.isSelected())
                {
                    selector.getCdma().setTriSectorReferenceCellSelection(0);
                    selector.getCdma().setReferenceCellId(0);
                }
                selector.repaint();
            }

            final ReferenceCellSelectionPanel this$0;

            
            {
                this$0 = ReferenceCellSelectionPanel.this;
                super();
            }
        }
);
        left.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                selector.getCdma().setSimulateNetworkEdge(left.isSelected());
                selector.getCdma().setNetworkEdgeLeftSide(left.isSelected());
                if(left.isSelected())
                {
                    selector.getCdma().setTriSectorReferenceCellSelection(0);
                    selector.getCdma().setReferenceCellId(13);
                }
                selector.repaint();
            }

            final ReferenceCellSelectionPanel this$0;

            
            {
                this$0 = ReferenceCellSelectionPanel.this;
                super();
            }
        }
);
        right.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                selector.getCdma().setSimulateNetworkEdge(right.isSelected());
                selector.getCdma().setNetworkEdgeLeftSide(!right.isSelected());
                if(right.isSelected())
                {
                    selector.getCdma().setTriSectorReferenceCellSelection(0);
                    selector.getCdma().setReferenceCellId(7);
                }
                selector.repaint();
            }

            final ReferenceCellSelectionPanel this$0;

            
            {
                this$0 = ReferenceCellSelectionPanel.this;
                super();
            }
        }
);
        plotWrapAround.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                selector.setPlotWrapAround(plotWrapAround.isSelected());
            }

            final ReferenceCellSelectionPanel this$0;

            
            {
                this$0 = ReferenceCellSelectionPanel.this;
                super();
            }
        }
);
        plotWrapAround.setSelected(true);
        interferenceFromCluster.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                selector.getCdma().setMeasureInterferenceFromCluster(interferenceFromCluster.isSelected());
            }

            final ReferenceCellSelectionPanel this$0;

            
            {
                this$0 = ReferenceCellSelectionPanel.this;
                super();
            }
        }
);
        FormLayout layout = new FormLayout("left:pref, 3dlu, pref, 9dlu, right:pref, 3dlu, pref", "p, 3dlu, p, 3dlu, p, 11dlu, p, 3dlu, p, 3dlu, p");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("Network Wrap-Around Model:", cc.xyw(1, 1, 7));
        builder.add(center, cc.xy(1, 3));
        builder.add(left, cc.xy(3, 3));
        builder.add(right, cc.xy(5, 3));
        builder.add(interferenceFromCluster, cc.xy(7, 3));
        builder.addSeparator("Plotting options", cc.xyw(1, 7, 7));
        builder.add(plotWrapAround, cc.xy(1, 9));
        builder.addLabel(STRINGLIST.getString("CDMA_REFERENCE_NOTE"), cc.xyw(1, 11, 7));
        add(builder.getPanel(), "South");
        setBorder(new TitledBorder(STRINGLIST.getString("CDMA_REFERENCE_TITLE")));
    }

    public void setCdmaSystem(CDMASystem cdma)
    {
        selector.setCdma(cdma);
        if(cdma.isSimulateNetworkEdge())
        {
            if(cdma.isNetworkEdgeLeftSide())
                left.setSelected(true);
            else
                right.setSelected(true);
        } else
        {
            center.setSelected(true);
        }
        if(cdma.isVictimSystem())
        {
            interferenceFromCluster.setEnabled(false);
        } else
        {
            interferenceFromCluster.setEnabled(true);
            interferenceFromCluster.setSelected(cdma.isMeasureInterferenceFromCluster());
        }
    }

    private static final ResourceBundle STRINGLIST;
    private ReferenceCellSelector selector;
    private JCheckBox plotWrapAround;
    private JCheckBox interferenceFromCluster;
    private JRadioButton center;
    private JRadioButton left;
    private JRadioButton right;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }






}