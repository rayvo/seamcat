// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAInformationPanel.java

package org.seamcat.cdma.presentation;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.presentation.components.SpringUtilities;

// Referenced classes of package org.seamcat.cdma.presentation:
//            CDMAUserStatusPanel, CDMAInsideInformationPanel, CDMAPlot, CDMASystemsComboBoxModel

public class CDMAInformationPanel extends JPanel
{

    public CDMAInformationPanel()
    {
        super(new SpringLayout());
        setBackground(Color.WHITE);
        add(userPanel);
        add(new JSeparator(0));
        add(detailPanel);
        SpringUtilities.makeCompactGrid(this, 3, 1, 6, 6, 10, 10);
    }

    public void setCDMAPlot(CDMAPlot cdma)
    {
        CDMASystem c = cdma.getCDMASystem();
        if(c == null)
        {
            userPanel.updateLabels(0, 0, 0, 0, 0);
            detailPanel.setCDMAPlot(null);
        } else
        {
            userPanel.updateLabels(c.countTotalNumberOfUsers(), c.getActiveUsers().size(), c.getInactiveUserCount(), c.countDroppedUsers(), c.countNotActivatedUsers());
            cdma.getCDMASystem().addPowerBalancingListener(userPanel);
            detailPanel.setCDMAPlot(cdma);
        }
    }

    public void addCDMAActionListener(ActionListener act)
    {
        detailPanel.addCDMAActionListener(act);
    }

    public void setCDMASelector(CDMASystemsComboBoxModel mdl)
    {
        detailPanel.setComboBoxModel(mdl);
    }

    public void reset()
    {
        userPanel.updateLabels(0, 0, 0, 0, 0);
        detailPanel.setCDMAPlot(null);
    }

    public CDMAInsideInformationPanel getMouseListener()
    {
        return detailPanel;
    }

    private final CDMAUserStatusPanel userPanel = new CDMAUserStatusPanel();
    private final CDMAInsideInformationPanel detailPanel = new CDMAInsideInformationPanel();
}