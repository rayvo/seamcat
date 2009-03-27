// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReferenceCellSelector.java

package org.seamcat.cdma.presentation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import org.seamcat.cdma.*;
import org.seamcat.mathematics.Mathematics;

public class ReferenceCellSelector extends JPanel
    implements MouseMotionListener, MouseListener
{

    public ReferenceCellSelector()
    {
        plotWrapAround = true;
        defaultCellColor = Color.BLUE;
        mouseOverColor = new Color(107, 232, 57);
        selectedCellColor = new Color(255, 53, 53);
        outsideNetworkColor = new Color(145, 145, 145);
        activeClusterBackgroundColor = new Color(125, 183, 255);
        textColor = Color.BLACK;
        activeClusterStroke = new BasicStroke(2.0F);
        wrapAroundStroke = new BasicStroke(1.0F);
        plotCenterCross = false;
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void paintComponent(Graphics _gr)
    {
        super.paintComponent(_gr);
        Graphics2D gr = (Graphics2D)_gr;
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        Dimension dim = getSize();
        double scaleFactorH;
        double scaleFactorW;
        if(plotWrapAround)
        {
            scaleFactorH = dim.getHeight() / ((double)(10 * cdma.getNumberOfTiers() + 2) * cdma.getInterCellDistance());
            scaleFactorW = dim.getWidth() / ((double)(6 * cdma.getNumberOfTiers() + 2) * cdma.getInterCellDistance());
        } else
        {
            scaleFactorH = dim.getHeight() / ((double)(2 * cdma.getNumberOfTiers() + 2) * cdma.getInterCellDistance());
            scaleFactorW = dim.getWidth() / ((double)(2 * cdma.getNumberOfTiers() + 2) * cdma.getInterCellDistance());
        }
        scaleFactor = Math.min(scaleFactorH, scaleFactorW);
        double lx = cdma.getLocationX();
        double ly = cdma.getLocationY();
        translateX = dim.getWidth() / 2D - lx * scaleFactor;
        translateY = dim.getHeight() / 2D + ly * scaleFactor;
        center_translateX = dim.getWidth() / 2D - lx * scaleFactor;
        center_translateY = dim.getHeight() / 2D + ly * scaleFactor;
        boolean networkEdgeCase = cdma.isSimulateNetworkEdge();
        boolean leftSideEdge = cdma.isNetworkEdgeLeftSide();
        double radius = scaleFactor * cdma.getCellRadius();
        CDMACell cells[][] = cdma.getCDMACells();
        double angle = 360 / cdma.getCellStructure();
        double d = radius * Math.sqrt(3D);
        int systemsToPlot = 1;
        if(plotWrapAround)
            systemsToPlot = 9;
        if(plotCenterCross)
        {
            gr.setColor(Color.BLACK);
            gr.drawLine(0, (int)center_translateY, (int)dim.getWidth(), (int)center_translateY);
            gr.drawLine((int)center_translateX, 0, (int)center_translateX, (int)dim.getHeight());
        }
        gr.translate(translateX, translateY);
        Color cellColor = defaultCellColor;
        for(int systemID = systemsToPlot - 1; systemID >= 0; systemID--)
        {
            translateX = 0.0D;
            translateY = 0.0D;
            cellColor = defaultCellColor;
            switch(systemID)
            {
            case 0: // '\0'
            default:
                break;

            case 1: // '\001'
                if(networkEdgeCase && !leftSideEdge)
                    cellColor = outsideNetworkColor;
                translateX += (1.5D * d) / Math.sqrt(3D) + (3D * d) / Math.sqrt(3D);
                translateY -= -2D * d + -1.5D * d;
                break;

            case 2: // '\002'
                if(networkEdgeCase && !leftSideEdge)
                    cellColor = outsideNetworkColor;
                translateX += ((3D * d) / Math.sqrt(3D)) * 2D + (1.5D * d) / Math.sqrt(3D);
                translateY -= d / 2D;
                break;

            case 3: // '\003'
                if(networkEdgeCase && !leftSideEdge)
                    cellColor = outsideNetworkColor;
                translateX += (3D * d) / Math.sqrt(3D);
                translateY -= 4D * d;
                break;

            case 4: // '\004'
                if(networkEdgeCase)
                    cellColor = outsideNetworkColor;
                translateX += (-1.5D * d) / Math.sqrt(3D);
                translateY -= 1.5D * d + 6D * d;
                break;

            case 5: // '\005'
                if(networkEdgeCase && leftSideEdge)
                    cellColor = outsideNetworkColor;
                translateX += (-1.5D * d) / Math.sqrt(3D) + (-3D * d) / Math.sqrt(3D);
                translateY -= 2D * d + 1.5D * d;
                break;

            case 6: // '\006'
                if(networkEdgeCase && leftSideEdge)
                    cellColor = outsideNetworkColor;
                translateX += (-1.5D * d) / Math.sqrt(3D) + ((-3D * d) / Math.sqrt(3D)) * 2D;
                translateY -= -d / 2D;
                break;

            case 7: // '\007'
                if(networkEdgeCase && leftSideEdge)
                    cellColor = outsideNetworkColor;
                translateX += (-3D * d) / Math.sqrt(3D);
                translateY -= -4D * d;
                break;

            case 8: // '\b'
                if(networkEdgeCase)
                    cellColor = outsideNetworkColor;
                translateX += (1.5D * d) / Math.sqrt(3D);
                translateY -= -1.5D * d + -6D * d;
                break;
            }
            for(int i = 0; i < cells.length; i++)
            {
                for(int k = 0; k < cells[i].length; k++)
                {
                    double cellX = cells[i][k].getLocationX();
                    double cellY = cells[i][k].getLocationY() * -1D;
                    cellX *= scaleFactor;
                    cellY *= scaleFactor;
                    cellX += translateX;
                    cellY += translateY;
                    int xPoints[] = new int[cdma.getCellStructure()];
                    int yPoints[] = new int[cdma.getCellStructure()];
                    for(int j = 0; j < cdma.getCellStructure(); j++)
                    {
                        xPoints[j] = (int)(Mathematics.cosD((double)j * angle) * radius + cellX);
                        yPoints[j] = (int)(Mathematics.sinD((double)j * angle) * radius + cellY);
                    }

                    gr.setColor(cellColor);
                    if(systemID == 0)
                        gr.setStroke(activeClusterStroke);
                    else
                        gr.setStroke(wrapAroundStroke);
                    for(int j = 0; j < cdma.getCellStructure(); j++)
                    {
                        gr.drawLine(xPoints[j], yPoints[j], (int)(Mathematics.cosD((double)(j + 1) * angle) * radius + cellX), (int)(Mathematics.sinD((double)(j + 1) * angle) * radius + cellY));
                        if(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna && j % 2 == 0)
                            gr.drawLine((int)(Mathematics.cosD((double)j * angle) * radius + cellX), (int)(Mathematics.sinD((double)j * angle) * radius + cellY), (int)cellX, (int)cellY);
                    }

                    if(systemID == 0 && k == 0 && plotWrapAround)
                    {
                        gr.setColor(activeClusterBackgroundColor);
                        gr.fillPolygon(xPoints, yPoints, xPoints.length);
                    }
                    if(systemID == 0 && cells[i][k] == mouseOverCell)
                    {
                        gr.setColor(mouseOverColor);
                        if(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna)
                        {
                            int xP[] = new int[4];
                            int yP[] = new int[4];
                            xP[0] = (int)cellX;
                            yP[0] = (int)cellY;
                            switch(k)
                            {
                            default:
                                break;

                            case 2: // '\002'
                                for(int zz = 0; zz < 3; zz++)
                                {
                                    xP[zz + 1] = xPoints[zz];
                                    yP[zz + 1] = yPoints[zz];
                                }

                                break;

                            case 1: // '\001'
                                for(int zz = 2; zz < 5; zz++)
                                {
                                    xP[zz - 1] = xPoints[zz];
                                    yP[zz - 1] = yPoints[zz];
                                }

                                break;

                            case 0: // '\0'
                                for(int zz = 4; zz < 6; zz++)
                                {
                                    xP[zz - 3] = xPoints[zz];
                                    yP[zz - 3] = yPoints[zz];
                                }

                                xP[3] = xPoints[0];
                                yP[3] = yPoints[0];
                                break;
                            }
                            gr.fillPolygon(xP, yP, xP.length);
                        } else
                        {
                            gr.fillPolygon(xPoints, yPoints, xPoints.length);
                        }
                    }
                    if(systemID == 0 && cells[i][k] == cdma.getReferenceCell())
                    {
                        gr.setColor(selectedCellColor);
                        if(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna)
                        {
                            int xP[] = new int[4];
                            int yP[] = new int[4];
                            xP[0] = (int)cellX;
                            yP[0] = (int)cellY;
                            switch(k)
                            {
                            default:
                                break;

                            case 2: // '\002'
                                for(int zz = 0; zz < 3; zz++)
                                {
                                    xP[zz + 1] = xPoints[zz];
                                    yP[zz + 1] = yPoints[zz];
                                }

                                break;

                            case 1: // '\001'
                                for(int zz = 2; zz < 5; zz++)
                                {
                                    xP[zz - 1] = xPoints[zz];
                                    yP[zz - 1] = yPoints[zz];
                                }

                                break;

                            case 0: // '\0'
                                for(int zz = 4; zz < 6; zz++)
                                {
                                    xP[zz - 3] = xPoints[zz];
                                    yP[zz - 3] = yPoints[zz];
                                }

                                xP[3] = xPoints[0];
                                yP[3] = yPoints[0];
                                break;
                            }
                            gr.fillPolygon(xP, yP, xP.length);
                        } else
                        {
                            gr.fillPolygon(xPoints, yPoints, xPoints.length);
                        }
                    }
                    gr.setColor(textColor);
                    if(systemID == 0 && !plotWrapAround)
                        gr.drawString((new StringBuilder()).append("#").append(i).toString(), (int)cellX, (int)cellY - 1);
                }

            }

        }

    }

    public void mouseClicked(MouseEvent e)
    {
        if(cdma == null)
            return;
        double x = ((double)e.getX() - center_translateX) / scaleFactor;
        double y = (((double)e.getY() - center_translateY) / scaleFactor) * -1D;
        CDMACell cells[][] = cdma.getCDMACells();
        int i = 0;
        for(int stop = cells.length; i < stop; i++)
        {
            CDMACell c = cells[i][0];
            if(c.isInsideCell(x, y))
            {
                if(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna)
                {
                    double angle = CDMALink.calculateKartesianAngle(x, y, c.getLocationX(), c.getLocationY());
                    if(angle <= 120D && angle >= 0.0D)
                        cdma.setTriSectorReferenceCellSelection(0);
                    else
                    if(angle <= 240D && angle >= 120D)
                        cdma.setTriSectorReferenceCellSelection(1);
                    else
                    if(angle <= 360D && angle >= 240D)
                        cdma.setTriSectorReferenceCellSelection(2);
                }
                cdma.setReferenceCellId(i);
                repaint();
                return;
            }
        }

    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent mouseevent)
    {
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    public void mouseDragged(MouseEvent mouseevent)
    {
    }

    public void mouseMoved(MouseEvent e)
    {
        if(cdma == null)
            return;
        double x = ((double)e.getX() - center_translateX) / scaleFactor;
        double y = (((double)e.getY() - center_translateY) / scaleFactor) * -1D;
        CDMACell cells[][] = cdma.getCDMACells();
        int i = 0;
        for(int stop = cells.length; i < stop; i++)
        {
            CDMACell c = cells[i][0];
            if(c.isInsideCell(x, y))
            {
                if(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna)
                {
                    double angle = CDMALink.calculateKartesianAngle(x, y, c.getLocationX(), c.getLocationY());
                    if(angle <= 120D && angle >= 0.0D)
                        mouseOverCell = cells[i][0];
                    else
                    if(angle <= 240D && angle >= 120D)
                        mouseOverCell = cells[i][1];
                    else
                    if(angle <= 360D && angle >= 240D)
                        mouseOverCell = cells[i][2];
                } else
                {
                    mouseOverCell = c;
                }
                repaint();
                return;
            }
        }

        mouseOverCell = null;
        repaint();
    }

    public CDMASystem getCdma()
    {
        return cdma;
    }

    public void setCdma(CDMASystem _cdma)
    {
        cdma = _cdma;
    }

    public boolean isPlotWrapAround()
    {
        return plotWrapAround;
    }

    public void setPlotWrapAround(boolean plotWrapAround)
    {
        this.plotWrapAround = plotWrapAround;
        repaint();
    }

    private CDMASystem cdma;
    private CDMACell mouseOverCell;
    private double scaleFactor;
    private double translateX;
    private double translateY;
    private double center_translateX;
    private double center_translateY;
    private boolean plotWrapAround;
    private Color defaultCellColor;
    private Color mouseOverColor;
    private Color selectedCellColor;
    private Color outsideNetworkColor;
    private Color activeClusterBackgroundColor;
    private Color textColor;
    private Stroke activeClusterStroke;
    private Stroke wrapAroundStroke;
    private boolean plotCenterCross;
}