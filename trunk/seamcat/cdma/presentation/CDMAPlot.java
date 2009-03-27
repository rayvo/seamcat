// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAPlot.java

package org.seamcat.cdma.presentation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.seamcat.cdma.CDMABalancingListener;
import org.seamcat.cdma.CDMACell;
import org.seamcat.cdma.CDMAInterferer;
import org.seamcat.cdma.CDMALink;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.cdma.UserTerminal;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Point2D;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.Antenna;
import org.seamcat.model.core.AntennaPattern;

// Referenced classes of package org.seamcat.cdma.presentation:
//            ActivatedUsersGraph

public class CDMAPlot extends JPanel
    implements CDMABalancingListener
{

    public CDMAPlot()
    {
        plotConnectionLines = false;
        plotUsers = false;
        plotUserID = false;
        plotDroppedUsers = true;
        plotCellid = false;
        plotCellCenter = true;
        plotTxStats = false;
        plotFixedLocations = false;
        plotCellBackground = false;
        plotExternalInterferers = true;
        plotAntennaPattern = false;
        plotLegend = true;
        plotHelp = true;
        plotScale = true;
        firstRun = true;
        patternColor = new Color(240, 193, 193);
        zoomFactor = 1.0D;
        focusShiftX = 0;
        focusShiftY = 0;
    }

    public void paint(Graphics _gr)
    {
        if(cdma == null)
            return;
        Graphics2D gr = (Graphics2D)_gr;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setBackground(Color.WHITE);
        Dimension dim = getSize();
        double scaleFactorH = dim.getHeight() / ((double)(2 * cdma.getNumberOfTiers() + 2) * cdma.getInterCellDistance());
        double scaleFactorW = dim.getWidth() / ((double)(2 * cdma.getNumberOfTiers() + 2) * cdma.getInterCellDistance());
        scaleFactor = Math.min(scaleFactorH, scaleFactorW);
        scaleFactor *= zoomFactor;
        gr.clearRect(0, 0, (int)dim.getWidth(), (int)dim.getHeight());
        if(plotScale)
        {
            gr.drawLine((int)dim.getWidth() / 20, (int)dim.getHeight() / 20, (int)(dim.getWidth() / 20D + scaleFactor * cdma.getInterCellDistance()), (int)dim.getHeight() / 20);
            gr.drawLine((int)(dim.getWidth() / 20D + scaleFactor * cdma.getInterCellDistance()), (int)dim.getHeight() / 20 - 5, (int)(dim.getWidth() / 20D + scaleFactor * cdma.getInterCellDistance()), (int)dim.getHeight() / 20 + 5);
            gr.drawLine((int)dim.getWidth() / 20, (int)dim.getHeight() / 20 - 5, (int)dim.getWidth() / 20, (int)dim.getHeight() / 20 + 5);
            gr.drawString((new StringBuilder()).append("D = ").append(CDMASystem.round(cdma.getInterCellDistance())).append(" km").toString(), (int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() / 20D) + 20);
        }
        if(plotLegend)
        {
            gr.setColor(Color.RED);
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() / 20D + 14D + (double)gr.getFont().getSize() * 1.5D), 4, 4);
            gr.drawString(" = voice active user", (int)(dim.getWidth() / 20D) + 35, (int)(dim.getHeight() / 20D + 20D + (double)gr.getFont().getSize() * 1.5D));
            gr.setColor(Color.ORANGE);
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() / 20D + 14D + 2D * ((double)gr.getFont().getSize() * 1.5D)), 4, 4);
            gr.drawString(" = voice active user in softhandover", (int)(dim.getWidth() / 20D) + 35, (int)(dim.getHeight() / 20D + 20D + 2D * ((double)gr.getFont().getSize() * 1.5D)));
            gr.setColor(Color.GRAY);
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() / 20D + 14D + 3D * ((double)gr.getFont().getSize() * 1.5D)), 4, 4);
            gr.drawString(" = dropped user", (int)(dim.getWidth() / 20D) + 35, (int)(dim.getHeight() / 20D + 20D + 3D * ((double)gr.getFont().getSize() * 1.5D)));
            gr.setColor(Color.MAGENTA);
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() / 20D + 14D + 4D * ((double)gr.getFont().getSize() * 1.5D)), 4, 4);
            gr.drawString(" = external interferer", (int)(dim.getWidth() / 20D) + 35, (int)(dim.getHeight() / 20D + 20D + 4D * ((double)gr.getFont().getSize() * 1.5D)));
        }
        if(plotHelp)
        {
            gr.setColor(Color.BLUE);
            int textOffset = 0;
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() - (dim.getHeight() / 20D + (double)textOffset + (double)gr.getFont().getSize() * 1.5D)), 4, 4);
            gr.drawString("Click on element to see details", (int)(dim.getWidth() / 20D) + 38, (int)(dim.getHeight() - (((dim.getHeight() / 20D + (double)textOffset) - 6D) + 5D * ((double)gr.getFont().getSize() * 1.5D))));
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() - (dim.getHeight() / 20D + (double)textOffset + 2D * ((double)gr.getFont().getSize() * 1.5D))), 4, 4);
            gr.drawString("Zoom using mousewheel or slider", (int)(dim.getWidth() / 20D) + 38, (int)(dim.getHeight() - (((dim.getHeight() / 20D + (double)textOffset) - 6D) + 4D * ((double)gr.getFont().getSize() * 1.5D))));
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() - (dim.getHeight() / 20D + (double)textOffset + 3D * ((double)gr.getFont().getSize() * 1.5D))), 4, 4);
            gr.drawString("Grab and drag to recenter", (int)(dim.getWidth() / 20D) + 38, (int)(dim.getHeight() - (((dim.getHeight() / 20D + (double)textOffset) - 6D) + 3D * ((double)gr.getFont().getSize() * 1.5D))));
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() - (dim.getHeight() / 20D + (double)textOffset + 4D * ((double)gr.getFont().getSize() * 1.5D))), 4, 4);
            gr.drawString("Double Right click to reset to 100% zoom", (int)(dim.getWidth() / 20D) + 38, (int)(dim.getHeight() - (((dim.getHeight() / 20D + (double)textOffset) - 6D) + 2D * ((double)gr.getFont().getSize() * 1.5D))));
            gr.fillOval((int)(dim.getWidth() / 20D) + 30, (int)(dim.getHeight() - (dim.getHeight() / 20D + (double)textOffset + 5D * ((double)gr.getFont().getSize() * 1.5D))), 4, 4);
            gr.drawString("Select user and Ctrl-click any BS to see link data", (int)(dim.getWidth() / 20D) + 38, (int)(dim.getHeight() - (((dim.getHeight() / 20D + (double)textOffset) - 6D) + (double)gr.getFont().getSize() * 1.5D)));
        }
        double radius = scaleFactor * cdma.getCellRadius();
        double lx = cdma.getLocationX();
        double ly = cdma.getLocationY();
        transX = dim.getWidth() / 2D - lx * scaleFactor;
        transY = dim.getHeight() / 2D + ly * scaleFactor;
        translateX = transX;
        translateY = transY;
        gr.translate(translateX, translateY);
        CDMACell cells[][] = cdma.getCDMACells();
        double angle = 360 / cdma.getCellStructure();
        java.util.List users = cdma.getActiveUsers();
        double cellX = cdma.getReferenceCell().getLocationX();
        double cellY = -cdma.getReferenceCell().getLocationY();
        cellX *= scaleFactor;
        cellY *= scaleFactor;
        cellX += focusShiftX;
        cellY += focusShiftY;
        int xPoints[] = new int[cdma.getCellStructure()];
        int yPoints[] = new int[cdma.getCellStructure()];
        for(int j = 0; j < cdma.getCellStructure(); j++)
        {
            xPoints[j] = (int)(Mathematics.cosD((double)j * angle) * radius + cellX);
            yPoints[j] = (int)(Mathematics.sinD((double)j * angle) * radius + cellY);
        }

        gr.setColor(Color.YELLOW);
        gr.fillPolygon(xPoints, yPoints, xPoints.length);
        if(selectedCell != null)
        {
            cellX = selectedCell.getLocationX();
            cellY = -selectedCell.getLocationY();
            cellX *= scaleFactor;
            cellY *= scaleFactor;
            cellX += focusShiftX;
            cellY += focusShiftY;
            if(plotAntennaPattern && selectedCell.getAntenna().getUseHorizontalPattern())
            {
                java.util.List points = selectedCell.getAntenna().getHorizontalPattern().getPattern().getPointsList();
                Collections.sort(points, Point2D.POINTY_COMPARATOR);
                double min = ((Point2D)points.get(0)).getY();
                double max = ((Point2D)points.get(points.size() - 1)).getY();
                Collections.sort(points, Point2D.POINTX_COMPARATOR);
                double diff = Math.abs(max - min);
                int xPatternPoints[] = new int[points.size()];
                int yPatternPoints[] = new int[points.size()];
                gr.setColor(Color.RED);
                for(int z = 0; z < points.size(); z++)
                {
                    Point2D po1 = (Point2D)points.get(z);
                    double distFactor = radius * ((radius - (radius / diff) * Math.abs(po1.getY())) / radius);
                    int p1 = (int)(Mathematics.cosD(((Point2D)points.get(z)).getX()) * distFactor + cellX);
                    int p2 = (int)(-(Mathematics.sinD(((Point2D)points.get(z)).getX()) * distFactor) + cellY);
                    xPatternPoints[z] = p1;
                    yPatternPoints[z] = p2;
                }

                gr.setColor(patternColor);
                gr.fillPolygon(xPatternPoints, yPatternPoints, xPatternPoints.length);
                gr.setColor(Color.RED);
                gr.drawPolygon(xPatternPoints, yPatternPoints, xPatternPoints.length);
            }
        }
        if(plotExternalInterferers)
        {
            java.util.List ext = cdma.getExternalInterferers();
            gr.setColor(Color.MAGENTA);
            int i = 0;
            for(int stop = ext.size(); i < stop; i++)
            {
                CDMAInterferer e = (CDMAInterferer)ext.get(i);
                double extX = e.getLocationX();
                double extY = -e.getLocationY();
                extX *= scaleFactor;
                extY *= scaleFactor;
                extX += focusShiftX;
                extY += focusShiftY;
                gr.fillOval((int)extX - 3, (int)extY - 3, 6, 6);
                if(e != selectedInterferer)
                    continue;
                if(plotAntennaPattern && e.getAntenna().getUseHorizontalPattern())
                {
                    java.util.List points = e.getAntenna().getHorizontalPattern().getPattern().getPointsList();
                    Collections.sort(points, Point2D.POINTY_COMPARATOR);
                    double min = ((Point2D)points.get(0)).getY();
                    double max = ((Point2D)points.get(points.size() - 1)).getY();
                    Collections.sort(points, Point2D.POINTX_COMPARATOR);
                    double diff = Math.abs(max - min);
                    int xPatternPoints[] = new int[points.size()];
                    int yPatternPoints[] = new int[points.size()];
                    gr.setColor(Color.RED);
                    for(int z = 0; z < points.size(); z++)
                    {
                        Point2D po1 = (Point2D)points.get(z);
                        double distFactor = radius * ((radius - (radius / diff) * Math.abs(po1.getY())) / radius);
                        int p1 = (int)(Mathematics.cosD(((Point2D)points.get(z)).getX()) * distFactor + extX);
                        int p2 = (int)(-(Mathematics.sinD(((Point2D)points.get(z)).getX()) * distFactor) + extY);
                        xPatternPoints[z] = p1;
                        yPatternPoints[z] = p2;
                    }

                    gr.setColor(patternColor);
                    gr.fillPolygon(xPatternPoints, yPatternPoints, xPatternPoints.length);
                    gr.setColor(Color.RED);
                    gr.drawPolygon(xPatternPoints, yPatternPoints, xPatternPoints.length);
                }
                gr.setColor(Color.BLACK);
                gr.drawOval((int)extX - 11, (int)extY - 11, 20, 20);
                gr.setColor(Color.MAGENTA);
            }

        }
label0:
        for(int i = 0; i < users.size() && plotUsers; i++)
        {
            UserTerminal user = (UserTerminal)users.get(i);
            if(user == null)
            {
                if(LOG.isDebugEnabled())
                    LOG.debug("User is null");
                continue;
            }
            if(user.isInSoftHandover())
                gr.setColor(Color.ORANGE);
            else
                gr.setColor(Color.RED);
            double userX = user.getLocationX();
            double userY = -user.getLocationY();
            userX *= scaleFactor;
            userY *= scaleFactor;
            userX += focusShiftX;
            userY += focusShiftY;
            gr.fillOval((int)userX - 2, (int)userY - 2, 4, 4);
            if(plotUserID)
                gr.drawString((new StringBuilder()).append("#").append(user.getUserid()).toString(), (int)userX + 6, (int)userY);
            int j = 0;
            do
            {
                if(j >= user.getActiveList().size() || !plotConnectionLines)
                    continue label0;
                gr.setColor(Color.LIGHT_GRAY);
                if(user.isInSoftHandover() && user.isUplinkMode() && (user.getActiveList().get(j) == user.getActiveUplink() || user.isInSofterHandover()))
                    gr.setColor(Color.DARK_GRAY);
                double cellX = ((CDMALink)user.getActiveList().get(j)).getCDMACell().getLocationX();
                double cellY = -((CDMALink)user.getActiveList().get(j)).getCDMACell().getLocationY();
                cellX *= scaleFactor;
                cellY *= scaleFactor;
                cellX += focusShiftX;
                cellY += focusShiftY;
                gr.drawLine((int)userX, (int)userY, (int)cellX, (int)cellY);
                j++;
            } while(true);
        }

        if(selectedUser != null)
        {
            gr.setColor(Color.BLACK);
            double userX = selectedUser.getLocationX();
            double userY = -selectedUser.getLocationY();
            userX *= scaleFactor;
            userY *= scaleFactor;
            userX += focusShiftX;
            userY += focusShiftY;
            gr.drawOval((int)userX - 7, (int)userY - 7, 13, 13);
            gr.drawString((new StringBuilder()).append("#").append(selectedUser.getUserid()).toString(), (int)userX + 6, (int)userY);
            gr.setColor(Color.BLUE);
            gr.fillOval((int)userX - 2, (int)userY - 2, 4, 4);
            gr.setColor(Color.LIGHT_GRAY);
            for(int j = 0; j < selectedUser.getActiveList().size(); j++)
            {
                gr.setColor(Color.LIGHT_GRAY);
                if(selectedUser.isInSoftHandover() && selectedUser.isUplinkMode() && (selectedUser.getActiveList().get(j) == selectedUser.getActiveUplink() || selectedUser.isInSofterHandover()))
                    gr.setColor(Color.DARK_GRAY);
                double cellX = ((CDMALink)selectedUser.getActiveList().get(j)).getCDMACell().getLocationX();
                double cellY = -((CDMALink)selectedUser.getActiveList().get(j)).getCDMACell().getLocationY();
                cellX *= scaleFactor;
                cellY *= scaleFactor;
                cellX += focusShiftX;
                cellY += focusShiftY;
                gr.drawLine((int)userX, (int)userY, (int)cellX, (int)cellY);
            }

        }
        if(selectedCell != null)
        {
            java.util.List connections = selectedCell.getActiveConnections();
            double cellX = selectedCell.getLocationX();
            double cellY = -selectedCell.getLocationY();
            cellX *= scaleFactor;
            cellY *= scaleFactor;
            cellX += focusShiftX;
            cellY += focusShiftY;
            gr.setColor(Color.BLACK);
            gr.drawOval((int)cellX - 9, (int)cellY - 9, 16, 16);
            for(int i = 0; i < connections.size(); i++)
            {
                UserTerminal user = ((CDMALink)connections.get(i)).getUserTerminal();
                if(user == null)
                    continue;
                if(user == selectedUser)
                    gr.setColor(Color.BLUE);
                else
                    gr.setColor(Color.RED);
                double userX = user.getLocationX();
                double userY = -user.getLocationY();
                userX *= scaleFactor;
                userY *= scaleFactor;
                userX += focusShiftX;
                userY += focusShiftY;
                gr.fillOval((int)userX - 2, (int)userY - 2, 4, 4);
                if(plotUserID)
                    gr.drawString((new StringBuilder()).append("#").append(user.getUserid()).toString(), (int)userX + 6, (int)userY);
                gr.setColor(Color.LIGHT_GRAY);
                for(int j = 0; j < user.getActiveList().size(); j++)
                {
                    gr.setColor(Color.LIGHT_GRAY);
                    if(user.isInSoftHandover() && user.isUplinkMode() && (user.getActiveList().get(j) == user.getActiveUplink() || user.isInSofterHandover()))
                        gr.setColor(Color.DARK_GRAY);
                    double cX = ((CDMALink)user.getActiveList().get(j)).getCDMACell().getLocationX();
                    double cY = -((CDMALink)user.getActiveList().get(j)).getCDMACell().getLocationY();
                    cX *= scaleFactor;
                    cY *= scaleFactor;
                    cX += focusShiftX;
                    cY += focusShiftY;
                    gr.drawLine((int)userX, (int)userY, (int)cX, (int)cY);
                }

                gr.drawLine((int)userX, (int)userY, (int)cellX, (int)cellY);
            }

            double userX;
            double userY;
            for(Iterator i$ = selectedCell.getDroppedUsers().iterator(); i$.hasNext(); gr.fillOval((int)userX - 2, (int)userY - 2, 4, 4))
            {
                CDMALink user = (CDMALink)i$.next();
                gr.setColor(Color.GRAY);
                userX = user.getUserTerminal().getLocationX();
                userY = -user.getUserTerminal().getLocationY();
                userX *= scaleFactor;
                userY *= scaleFactor;
                userX += focusShiftX;
                userY += focusShiftY;
                if(plotUserID)
                    gr.drawString((new StringBuilder()).append("#").append(user.getUserTerminal().getUserid()).toString(), (int)userX + 6, (int)userY);
            }

        }
        for(int i = 0; i < cells.length && plotDroppedUsers; i++)
        {
            for(int j = 0; j < cells[i].length; j++)
            {
                double userX;
                double userY;
                int diameter;
                for(Iterator i$ = cells[i][j].getDroppedUsers().iterator(); i$.hasNext(); gr.fillOval((int)userX - diameter / 2, (int)userY - diameter / 2, diameter, diameter))
                {
                    CDMALink user = (CDMALink)i$.next();
                    if(user.getUserTerminal().getDropReason() != null && user.getUserTerminal().getDropReason().equals("Inside Power Balance"))
                        gr.setColor(Color.LIGHT_GRAY);
                    else
                        gr.setColor(Color.GRAY);
                    userX = user.getUserTerminal().getLocationX();
                    userY = -user.getUserTerminal().getLocationY();
                    userX *= scaleFactor;
                    userY *= scaleFactor;
                    userX += focusShiftX;
                    userY += focusShiftY;
                    if(plotUserID)
                        gr.drawString((new StringBuilder()).append("#").append(user.getUserTerminal().getUserid()).toString(), (int)userX + 6, (int)userY);
                    diameter = 4;
                }

            }

        }

        if(selectedLink != null)
        {
            UserTerminal user = selectedLink.getUserTerminal();
            gr.setColor(Color.BLUE);
            double userX = user.getLocationX();
            double userY = -user.getLocationY();
            userX *= scaleFactor;
            userY *= scaleFactor;
            userX += focusShiftX;
            userY += focusShiftY;
            gr.fillOval((int)userX - 2, (int)userY - 2, 4, 4);
            gr.setColor(Color.GREEN);
            double cX = selectedLink.getCDMACell().getLocationX();
            double cY = -selectedLink.getCDMACell().getLocationY();
            cX *= scaleFactor;
            cY *= scaleFactor;
            cX += focusShiftX;
            cY += focusShiftY;
            gr.drawLine((int)userX, (int)userY, (int)cX, (int)cY);
        }
        for(int i = 0; i < cells.length; i++)
        {
            int k = 0;
            double cellX = cells[i][k].getLocationX();
            double cellY = -cells[i][k].getLocationY();
            cellX *= scaleFactor;
            cellY *= scaleFactor;
            cellX += focusShiftX;
            cellY += focusShiftY;
            gr.setColor(Color.BLUE);
            int xPoints[] = new int[cdma.getCellStructure()];
            int yPoints[] = new int[cdma.getCellStructure()];
            for(int j = 0; j < cdma.getCellStructure(); j++)
            {
                xPoints[j] = (int)(Mathematics.cosD((double)j * angle) * radius + cellX);
                yPoints[j] = (int)(Mathematics.sinD((double)j * angle) * radius + cellY);
                gr.drawLine(xPoints[j], yPoints[j], (int)(Mathematics.cosD((double)(j + 1) * angle) * radius + cellX), (int)(Mathematics.sinD((double)(j + 1) * angle) * radius + cellY));
                if(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna && j % 2 == 0)
                    gr.drawLine((int)(Mathematics.cosD((double)j * angle) * radius + cellX), (int)(Mathematics.sinD((double)j * angle) * radius + cellY), (int)cellX, (int)cellY);
            }

            gr.setColor(Color.GREEN);
            if(plotCellCenter)
                gr.fillOval((int)cellX - 3, (int)cellY - 3, 6, 6);
            gr.setColor(Color.BLACK);
            if(plotCellid)
                if(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna)
                {
                    gr.drawString((new StringBuilder()).append("BS #").append(cells[i][0].getCellid()).append(":").toString(), (int)(cellX + radius / 5D), (int)(cellY - (double)gr.getFont().getSize() * 1.5D));
                    gr.drawString((new StringBuilder()).append("BS #").append(cells[i][1].getCellid()).append(":").toString(), (int)(cellX - 2D * (radius / 3D)), (int)cellY);
                    gr.drawString((new StringBuilder()).append("BS #").append(cells[i][2].getCellid()).append(":").toString(), (int)(cellX + radius / 5D), (int)(cellY + (double)gr.getFont().getSize() * 1.5D));
                } else
                {
                    gr.drawString((new StringBuilder()).append("Cell #").append(cells[i][0].getCellid()).append(":").toString(), (int)(cellX - 2D * (radius / 3D)), (int)(cellY - (double)gr.getFont().getSize() * 1.5D));
                }
            if(plotTxStats)
            {
                if(cdma.isUplink())
                {
                    gr.drawString((new StringBuilder()).append("Itotal: ").append(CDMASystem.round(cells[i][0].getTotalInterference())).append(" dBm").toString(), (int)(cellX - 2D * (radius / 3D)), (int)cellY);
                    gr.drawString((new StringBuilder()).append("NoiseRise: ").append(Math.rint(cells[i][0].calculateNoiseRiseOverThermalNoise_dB() * 1000D) / 1000D).append(" dB").toString(), (int)(cellX - 2D * (radius / 3D)), (int)(cellY + (double)gr.getFont().getSize() * 1.5D));
                } else
                {
                    gr.drawString((new StringBuilder()).append("TX: ").append(Math.rint(cells[i][0].getCurrentTransmitPower() * 1000D) / 1000D).append(" dBm").toString(), (int)(cellX - 2D * (radius / 3D)), (int)cellY);
                }
                gr.drawString((new StringBuilder()).append("Users: ").append(cells[i][0].countServedUsers()).toString(), (int)(cellX - 2D * (radius / 3D)), (int)(cellY + (double)(gr.getFont().getSize() * 3)));
            }
            if(plotFixedLocations)
            {
                gr.drawString((new StringBuilder()).append("X: ").append(fixedLocationsStrings[i][0]).toString(), (int)cellX, (int)(cellY - 1.5D * (double)gr.getFont().getSize()));
                gr.drawString((new StringBuilder()).append("Y: ").append(fixedLocationsStrings[i][1]).toString(), (int)cellX, (int)cellY);
            }
        }

    }

    public void setCDMASystem(CDMASystem _cdma)
    {
        if(_cdma != null)
            _cdma.addPowerBalancingListener(this);
        else
        if(cdma != null)
            cdma.removePowerBalancingListener(this);
        cdma = _cdma;
    }

    public CDMASystem getCDMASystem()
    {
        return cdma;
    }

    public void balancingStarted()
    {
        selectedUser = null;
    }

    public void balancingComplete()
    {
        repaint();
        try
        {
            cdma.removePowerBalancingListener(this);
        }
        catch(Exception e) { }
    }

    public void voiceActiveUserAdded(UserTerminal user)
    {
        if(firstRun)
            repaint();
    }

    public void voiceActiveUserDropped(UserTerminal userterminal)
    {
    }

    public void voiceActiveUserNotActivated(UserTerminal userterminal)
    {
    }

    public boolean isPlotDroppedUsers()
    {
        return plotDroppedUsers;
    }

    public boolean isPlotUserID()
    {
        return plotUserID;
    }

    public boolean isPlotUsers()
    {
        return plotUsers;
    }

    public boolean isPlotCellBackground()
    {
        return plotCellBackground;
    }

    public boolean isPlotConnectionLines()
    {
        return plotConnectionLines;
    }

    public boolean isPlotCellCenter()
    {
        return plotCellCenter;
    }

    public boolean isPlotTxStats()
    {
        return plotTxStats;
    }

    public void setPlotFixedLocations(boolean plotFixedLocations)
    {
        this.plotFixedLocations = plotFixedLocations;
        repaint();
    }

    public void setPlotDroppedUsers(boolean plotDroppedUsers)
    {
        this.plotDroppedUsers = plotDroppedUsers;
        repaint();
    }

    public void setPlotUserID(boolean plotUserID)
    {
        this.plotUserID = plotUserID;
        repaint();
    }

    public void setPlotUsers(boolean plotUsers)
    {
        this.plotUsers = plotUsers;
        repaint();
    }

    public void setPlotCellBackground(boolean plotCellBackground)
    {
        this.plotCellBackground = plotCellBackground;
        repaint();
    }

    public void setPlotConnectionLines(boolean plotConnectionLines)
    {
        this.plotConnectionLines = plotConnectionLines;
        repaint();
    }

    public void setPlotCellCenter(boolean plotCellCenter)
    {
        this.plotCellCenter = plotCellCenter;
        repaint();
    }

    public void setPlotTxStats(boolean plotTxStats)
    {
        this.plotTxStats = plotTxStats;
        repaint();
    }

    public void setPlotCellid(boolean plotCellid)
    {
        this.plotCellid = plotCellid;
        repaint();
    }

    public void setSelectedUser(UserTerminal selectedUser)
    {
        this.selectedUser = selectedUser;
    }

    public void setSelectedCell(CDMACell selectedCell)
    {
        this.selectedCell = selectedCell;
    }

    public void setSelectedInterferer(CDMAInterferer inter)
    {
        selectedInterferer = inter;
    }

    public boolean isPlotExternalInterferers()
    {
        return plotExternalInterferers;
    }

    public void setPlotExternalInterferers(boolean plotExternalInterferers)
    {
        this.plotExternalInterferers = plotExternalInterferers;
        repaint();
    }

    public boolean isPlotFixedLocations()
    {
        return plotFixedLocations;
    }

    public boolean isPlotCellid()
    {
        return plotCellid;
    }

    public double getTranslateX()
    {
        return translateX;
    }

    public double getTranslateY()
    {
        return translateY;
    }

    public double getScaleFactor()
    {
        return scaleFactor;
    }

    public UserTerminal getSelectedUser()
    {
        return selectedUser;
    }

    public CDMACell getSelectedCell()
    {
        return selectedCell;
    }

    public void voiceInActiveUserAdded()
    {
    }

    public void voiceActiveUserIgnored()
    {
    }

    public boolean isPlotAntennaPattern()
    {
        return plotAntennaPattern;
    }

    public void setPlotAntennaPattern(boolean plotAntennaPattern)
    {
        this.plotAntennaPattern = plotAntennaPattern;
        repaint();
    }

    public double getZoomFactor()
    {
        return zoomFactor;
    }

    public void setZoomFactor(double zoomFactor)
    {
        this.zoomFactor = zoomFactor;
    }

    public int getFocusShiftX()
    {
        return focusShiftX;
    }

    public void adjustFocusShiftX(int _focusShiftX)
    {
        focusShiftX += _focusShiftX;
    }

    public int getFocusShiftY()
    {
        return focusShiftY;
    }

    public void adjustFocusShiftY(int _focusShiftY)
    {
        focusShiftY += _focusShiftY;
    }

    public void adjustZoom(double adjustment)
    {
        double adjust = adjustment / 100D;
        zoomFactor += adjust;
        if(zoomFactor < 0.0D)
            zoomFactor = 0.0D;
    }

    public void setFocusShiftX(int focusShiftX)
    {
        this.focusShiftX = focusShiftX;
    }

    public void setFocusShiftY(int focusShiftY)
    {
        this.focusShiftY = focusShiftY;
    }

    public CDMALink getSelectedLink()
    {
        return selectedLink;
    }

    public void setSelectedLink(CDMALink selectedLink)
    {
        this.selectedLink = selectedLink;
    }

    public boolean isPlotHelp()
    {
        return plotHelp;
    }

    public void setPlotHelp(boolean plotHelp)
    {
        this.plotHelp = plotHelp;
        repaint();
    }

    public boolean isPlotLegend()
    {
        return plotLegend;
    }

    public void setPlotLegend(boolean plotLegend)
    {
        this.plotLegend = plotLegend;
        repaint();
    }

    public boolean isPlotScale()
    {
        return plotScale;
    }

    public void setPlotScale(boolean plotScale)
    {
        this.plotScale = plotScale;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/presentation/CDMAPlot);
    public static ActivatedUsersGraph graph = new ActivatedUsersGraph();
    private CDMASystem cdma;
    public static boolean updateGraph = true;
    private boolean plotConnectionLines;
    private boolean plotUsers;
    private boolean plotUserID;
    private boolean plotDroppedUsers;
    private boolean plotCellid;
    private boolean plotCellCenter;
    private boolean plotTxStats;
    private boolean plotFixedLocations;
    private boolean plotCellBackground;
    private boolean plotExternalInterferers;
    private boolean plotAntennaPattern;
    private boolean plotLegend;
    private boolean plotHelp;
    private boolean plotScale;
    private static String fixedLocationsStrings[][];
    private boolean firstRun;
    private double scaleFactor;
    private double translateX;
    private double translateY;
    private double transX;
    private double transY;
    private UserTerminal selectedUser;
    private CDMACell selectedCell;
    private CDMALink selectedLink;
    private CDMAInterferer selectedInterferer;
    private Color patternColor;
    private double zoomFactor;
    private int focusShiftX;
    private int focusShiftY;

    static 
    {
        fixedLocationsStrings = new String[19][2];
        fixedLocationsStrings[0][0] = "x";
        fixedLocationsStrings[0][1] = "y";
        fixedLocationsStrings[1][0] = "x + 1.5*D/Math.sqrt(3)";
        fixedLocationsStrings[1][1] = "y + D / 2";
        fixedLocationsStrings[2][0] = "x";
        fixedLocationsStrings[2][1] = "y + D";
        fixedLocationsStrings[3][0] = "x - 1.5*D / Math.sqrt(3)";
        fixedLocationsStrings[3][1] = "y + D / 2";
        fixedLocationsStrings[4][0] = "x - 1.5*D / Math.sqrt(3)";
        fixedLocationsStrings[4][1] = "y - D / 2";
        fixedLocationsStrings[5][0] = "x";
        fixedLocationsStrings[5][1] = "y - D";
        fixedLocationsStrings[6][0] = "x + 1.5*D/Math.sqrt(3)";
        fixedLocationsStrings[6][1] = "y - d / 2";
        fixedLocationsStrings[7][0] = "x + 3*D / Math.sqrt(3)";
        fixedLocationsStrings[7][1] = "y";
        fixedLocationsStrings[8][0] = "x + 3*D / Math.sqrt(3)";
        fixedLocationsStrings[8][1] = "y + D";
        fixedLocationsStrings[9][0] = "x + 1.5*D / Math.sqrt(3)";
        fixedLocationsStrings[9][1] = "y + 1.5*D";
        fixedLocationsStrings[10][0] = "x";
        fixedLocationsStrings[10][1] = "y + 2*D";
        fixedLocationsStrings[11][0] = "x - 1.5*D / Math.sqrt(3)";
        fixedLocationsStrings[11][1] = "y + 1.5*D";
        fixedLocationsStrings[12][0] = "x - 3*D / Math.sqrt(3)";
        fixedLocationsStrings[12][1] = "y + D";
        fixedLocationsStrings[13][0] = "x - 3*D / Math.sqrt(3)";
        fixedLocationsStrings[13][1] = "y";
        fixedLocationsStrings[14][0] = "x - 3*D / Math.sqrt(3)";
        fixedLocationsStrings[14][1] = "y - D";
        fixedLocationsStrings[15][0] = "x - 1.5*D / Math.sqrt(3)";
        fixedLocationsStrings[15][1] = "y - 1.5*D";
        fixedLocationsStrings[16][0] = "x";
        fixedLocationsStrings[16][1] = "y - 2*D";
        fixedLocationsStrings[17][0] = "x + 1.5*D / Math.sqrt(3)";
        fixedLocationsStrings[17][1] = "y - 1.5*D";
        fixedLocationsStrings[18][0] = "x + 3*D / Math.sqrt(3)";
        fixedLocationsStrings[18][1] = "y - D";
    }
}