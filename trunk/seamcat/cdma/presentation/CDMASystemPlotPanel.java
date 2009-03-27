// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMASystemPlotPanel.java

package org.seamcat.cdma.presentation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.model.Workspace;

// Referenced classes of package org.seamcat.cdma.presentation:
//            CDMAPlot, DrawingControlPanel, CDMAInformationPanel, CDMASystemsComboBoxModel

public class CDMASystemPlotPanel extends JPanel
{

    public CDMASystemPlotPanel()
    {
        super(new BorderLayout());
        controls = new DrawingControlPanel(cdmaplot);
        infoPanel.addCDMAActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actevent)
            {
                CDMASystem cdma = (CDMASystem)((JComboBox)actevent.getSource()).getSelectedItem();
                if(cdma != null)
                {
                    cdmaplot.setCDMASystem(cdma);
                    infoPanel.setCDMAPlot(cdmaplot);
                    cdmaplot.repaint();
                }
            }

            final CDMASystemPlotPanel this$0;

            
            {
                this$0 = CDMASystemPlotPanel.this;
                super();
            }
        }
);
        JSplitPane sp = new JSplitPane(1, cdmaplot, infoPanel);
        sp.setOneTouchExpandable(true);
        sp.setDividerLocation(800);
        add(sp, "Center");
        add(controls, "North");
    }

    public void setWorkspace(Workspace wks)
    {
        CDMASystemsComboBoxModel model = new CDMASystemsComboBoxModel(wks);
        if(model.getSize() > 0)
        {
            setCDMASystem((CDMASystem)model.getElementAt(0));
            infoPanel.setCDMASelector(model);
        }
    }

    public void setCDMASystem(CDMASystem cdma)
    {
        cdmaplot.setCDMASystem(cdma);
        infoPanel.setCDMAPlot(cdmaplot);
    }

    public void reset()
    {
        cdmaplot.setCDMASystem(null);
        cdmaplot.repaint();
        infoPanel.reset();
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/presentation/CDMASystemPlotPanel);
    private final CDMAPlot cdmaplot = new CDMAPlot();
    private final DrawingControlPanel controls;
    private final CDMAInformationPanel infoPanel = new CDMAInformationPanel();



}