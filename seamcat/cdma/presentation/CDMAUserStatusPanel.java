// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAUserStatusPanel.java

package org.seamcat.cdma.presentation;

import java.awt.Color;
import javax.swing.*;
import org.seamcat.cdma.CDMABalancingListener;
import org.seamcat.cdma.UserTerminal;
import org.seamcat.presentation.components.SpringUtilities;

public class CDMAUserStatusPanel extends JPanel
    implements CDMABalancingListener
{

    public CDMAUserStatusPanel()
    {
        super(new SpringLayout());
        activeColor = Color.RED;
        droppedColor = Color.GRAY;
        setBackground(Color.WHITE);
        activeLabel.setForeground(activeColor);
        droppedLabel.setForeground(droppedColor);
        add(totalLabel);
        add(totalCount);
        add(activeLabel);
        add(activeCount);
        add(droppedLabel);
        add(droppedCount);
        SpringUtilities.makeCompactGrid(this, 3, 2, 6, 6, 10, 10);
    }

    public void balancingStarted()
    {
        totalUserCount = 0;
        activeUserCount = 0;
        droppedUserCount = 0;
        notAllowedUserCount = 0;
        voiceInActiveUserCount = 0;
        updateLabels();
    }

    private void updateLabels()
    {
        totalCount.setText(Integer.toString(totalUserCount));
        activeCount.setText((new StringBuilder()).append(Integer.toString(activeUserCount + voiceInActiveUserCount)).append(" [").append(activeUserCount).append(" / ").append(voiceInActiveUserCount).append("]").toString());
        droppedCount.setText(Integer.toString(droppedUserCount));
        notAllowedCount.setText(Integer.toString(notAllowedUserCount));
    }

    public void updateLabels(int _totalUserCount, int _activeCount, int _voiceInActive, int _dropped, int _notAllowed)
    {
        totalUserCount = _totalUserCount;
        activeUserCount = _activeCount;
        voiceInActiveUserCount = _voiceInActive;
        droppedUserCount = _dropped;
        notAllowedUserCount = _notAllowed;
        updateLabels();
    }

    public void balancingComplete()
    {
        updateLabels();
    }

    public void voiceActiveUserAdded(UserTerminal user)
    {
        totalUserCount++;
        activeUserCount++;
        totalCount.setText(Integer.toString(totalUserCount));
        activeCount.setText((new StringBuilder()).append(Integer.toString(activeUserCount + voiceInActiveUserCount)).append(" [").append(activeUserCount).append(" / ").append(voiceInActiveUserCount).append("]").toString());
    }

    public void voiceActiveUserDropped(UserTerminal user)
    {
        totalUserCount++;
        droppedUserCount++;
        totalCount.setText(Integer.toString(totalUserCount));
        droppedCount.setText(Integer.toString(droppedUserCount));
    }

    public void voiceActiveUserNotActivated(UserTerminal user)
    {
        totalUserCount++;
        notAllowedUserCount++;
        totalCount.setText(Integer.toString(totalUserCount));
        notAllowedCount.setText(Integer.toString(notAllowedUserCount));
    }

    public void voiceInActiveUserAdded()
    {
        totalUserCount++;
        voiceInActiveUserCount++;
        updateLabels();
    }

    public void voiceActiveUserIgnored()
    {
    }

    private final JLabel totalLabel = new JLabel("Total Users: ");
    private final JLabel activeLabel = new JLabel("Connected Users [active / inactive]: ");
    private final JLabel droppedLabel = new JLabel("Dropped Users: ");
    private final JLabel notAllowedLabel = new JLabel("Not Allowed Users: ");
    private final JLabel totalCount = new JLabel("0");
    private final JLabel activeCount = new JLabel("0");
    private final JLabel droppedCount = new JLabel("0");
    private final JLabel notAllowedCount = new JLabel("0");
    private int totalUserCount;
    private int activeUserCount;
    private int droppedUserCount;
    private int notAllowedUserCount;
    private int voiceInActiveUserCount;
    private Color activeColor;
    private Color droppedColor;
}