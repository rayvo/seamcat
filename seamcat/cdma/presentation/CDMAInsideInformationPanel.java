// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAInsideInformationPanel.java

package org.seamcat.cdma.presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import org.apache.log4j.Logger;
import org.seamcat.cdma.CDMACell;
import org.seamcat.cdma.CDMAElement;
import org.seamcat.cdma.CDMAInterferer;
import org.seamcat.cdma.CDMALink;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.cdma.UserTerminal;
import org.seamcat.cdma.presentation.tablemodels.CDMACellTableModel;
import org.seamcat.cdma.presentation.tablemodels.CDMAInterfererTableModel;
import org.seamcat.cdma.presentation.tablemodels.CDMALinkInfoTableModel;
import org.seamcat.cdma.presentation.tablemodels.CDMASystemTableModel;
import org.seamcat.cdma.presentation.tablemodels.CDMAUserInfoTableModel;
import org.seamcat.presentation.SeamcatIcons;
import org.seamcat.presentation.components.SpringUtilities;

// Referenced classes of package org.seamcat.cdma.presentation:
//            CDMAPlot, CDMASystemsComboBoxModel

public class CDMAInsideInformationPanel extends JPanel
    implements MouseListener, MouseMotionListener, MouseWheelListener
{

    public CDMAInsideInformationPanel()
    {
        super(new BorderLayout());
        clickRadius = 0.11D;
        selectedSector = 0;
        offsetX = 0;
        offsetY = 0;
        ButtonGroup bg = new ButtonGroup();
        bg.add(sector1);
        bg.add(sector2);
        bg.add(sector3);
        sector1.setBackground(Color.WHITE);
        sector2.setBackground(Color.WHITE);
        sector3.setBackground(Color.WHITE);
        sector1.setSelected(true);
        sector1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                selectedSector = 0;
                selectedSectorChanged();
            }

            final CDMAInsideInformationPanel this$0;

            
            {
                this$0 = CDMAInsideInformationPanel.this;
                super();
            }
        }
);
        sector2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                selectedSector = 1;
                selectedSectorChanged();
            }

            final CDMAInsideInformationPanel this$0;

            
            {
                this$0 = CDMAInsideInformationPanel.this;
                super();
            }
        }
);
        sector3.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                selectedSector = 2;
                selectedSectorChanged();
            }

            final CDMAInsideInformationPanel this$0;

            
            {
                this$0 = CDMAInsideInformationPanel.this;
                super();
            }
        }
);
        systems.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                CDMASystem cdma = (CDMASystem)systems.getSelectedItem();
                if(cdma != null)
                {
                    plot.setCDMASystem(cdma);
                    userTableModel.setUserTerminal(null);
                    cellTableModel.setSelectedCell(null);
                    interfererTableModel.setSelectedInterferer(null);
                    sector1.setEnabled(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna);
                    sector2.setEnabled(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna);
                    sector3.setEnabled(cdma.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna);
                }
            }

            final CDMAInsideInformationPanel this$0;

            
            {
                this$0 = CDMAInsideInformationPanel.this;
                super();
            }
        }
);
        JScrollPane scp = new JScrollPane(table);
        selectedLabel.setFont(new Font(selectedLabel.getFont().getName(), 1, selectedLabel.getFont().getSize()));
        clickRadiusSlider.setToolTipText("Adjust click radius (meters)");
        clickRadiusSlider.setPaintLabels(true);
        clickRadiusSlider.setBackground(Color.WHITE);
        clickRadiusSlider.setMinimumSize(new Dimension(0, 0));
        clickRadiusSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e)
            {
                clickRadiusLabel.setText((new StringBuilder()).append("Click Radius: ").append(clickRadiusSlider.getValue()).append(" meters").toString());
            }

            final CDMAInsideInformationPanel this$0;

            
            {
                this$0 = CDMAInsideInformationPanel.this;
                super();
            }
        }
);
        zoomFactorSlider.setToolTipText("Zoom");
        zoomFactorSlider.setPaintTicks(true);
        zoomFactorSlider.setBackground(Color.WHITE);
        zoomFactorSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e)
            {
                zoomFactorLabel.setText((new StringBuilder()).append("Zoom Factor: ").append(zoomFactorSlider.getValue()).append("%").toString());
                plot.setZoomFactor((double)zoomFactorSlider.getValue() / 100D);
                plot.repaint();
            }

            final CDMAInsideInformationPanel this$0;

            
            {
                this$0 = CDMAInsideInformationPanel.this;
                super();
            }
        }
);
        JPanel northPanel = new JPanel(new SpringLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.add(sector1);
        northPanel.add(sector2);
        northPanel.add(sector3);
        northPanel.add(zoomFactorLabel);
        northPanel.add(zoomFactorSlider);
        northPanel.add(systems);
        northPanel.add(iconLabel);
        northPanel.add(clickRadiusLabel);
        northPanel.add(clickRadiusSlider);
        northPanel.add(new JLabel("Clicked Element:"));
        northPanel.add(selectedLabel);
        SpringUtilities.makeCompactGrid(northPanel, 11, 1, 3, 3, 5, 5);
        add(northPanel, "North");
        add(scp, "Center");
        table.setBackground(Color.WHITE);
        setBackground(Color.WHITE);
    }

    public void setComboBoxModel(CDMASystemsComboBoxModel cmbml)
    {
        systems.setModel(cmbml);
    }

    public void addCDMAActionListener(ActionListener act)
    {
        systems.addActionListener(act);
    }

    public void setCDMAPlot(CDMAPlot _plot)
    {
        plot = _plot;
        if(plot == null)
        {
            cdmasystem = null;
            userTableModel.setUserTerminal(null);
            cellTableModel.setSelectedCell(null);
            interfererTableModel.setSelectedInterferer(null);
        } else
        {
            cdmasystem = plot.getCDMASystem();
            plot.setSelectedCell(null);
            plot.setSelectedUser(null);
            plot.setSelectedInterferer(null);
            systemTableModel.setCDMASystem(cdmasystem);
            plot.addMouseListener(this);
            plot.addMouseMotionListener(this);
            plot.addMouseWheelListener(this);
            userTableModel.setUserTerminal(null);
            cellTableModel.setSelectedCell(null);
            interfererTableModel.setSelectedInterferer(null);
            sector1.setEnabled(cdmasystem.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna);
            sector2.setEnabled(cdmasystem.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna);
            sector3.setEnabled(cdmasystem.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna);
        }
    }

    public void mouseClicked(MouseEvent e)
    {
        java.util.List users;
        double x;
        double y;
label0:
        {
            if(e.getButton() == 3)
            {
                if(e.getClickCount() == 2)
                {
                    plot.setFocusShiftX(0);
                    plot.setFocusShiftY(0);
                    plot.setZoomFactor(1.0D);
                    zoomFactorSlider.setValue((int)(plot.getZoomFactor() * 100D));
                    zoomFactorLabel.setText((new StringBuilder()).append("Zoom Factor: ").append(plot.getZoomFactor() * 100D).append("%").toString());
                    plot.repaint();
                }
                return;
            }
            if(cdmasystem == null)
                return;
            clickRadius = (double)clickRadiusSlider.getValue() / 1000D;
            users = cdmasystem.getActiveUsers();
            x = ((double)e.getX() - plot.getTranslateX() - (double)plot.getFocusShiftX()) / plot.getScaleFactor();
            y = -(((double)e.getY() - plot.getTranslateY() - (double)plot.getFocusShiftY()) / plot.getScaleFactor());
            if(!e.isControlDown())
                break label0;
            Iterator i$;
            CDMALink link;
            if(plot.getSelectedUser() != null)
            {
                UserTerminal user = plot.getSelectedUser();
                for(i$ = user.getActiveList().iterator(); i$.hasNext();)
                {
                    link = (CDMALink)i$.next();
                    if(isClickHit(x, y, link.getCDMACell(), clickRadius))
                    {
                        linkTableModel.setCDMALink(link);
                        table.setModel(linkTableModel);
                        selectedLabel.setText("Active Link");
                        plot.setSelectedLink(link);
                        plot.setSelectedUser(link.getUserTerminal());
                        plot.setSelectedCell(null);
                        plot.setSelectedInterferer(null);
                        plot.repaint();
                        return;
                    }
                }

                CDMALink arr$[] = user.getCDMALinks();
                int len$ = arr$.length;
                for(int i$ = 0; i$ < len$; i$++)
                {
                    CDMALink link = arr$[i$];
                    if(!user.getActiveList().contains(link) && isClickHit(x, y, link.getCDMACell(), clickRadius))
                    {
                        linkTableModel.setCDMALink(link);
                        table.setModel(linkTableModel);
                        selectedLabel.setText("Inactive Link");
                        plot.setSelectedLink(link);
                        plot.setSelectedUser(link.getUserTerminal());
                        plot.setSelectedCell(null);
                        plot.setSelectedInterferer(null);
                        plot.repaint();
                        return;
                    }
                }

            }
            if(plot.getSelectedCell() == null)
                break label0;
            CDMACell cell = plot.getSelectedCell();
            for(arr$ = cell.getActiveConnections().iterator(); arr$.hasNext();)
            {
                len$ = (CDMALink)arr$.next();
                if(isClickHit(x, y, len$.getUserTerminal(), clickRadius))
                {
                    linkTableModel.setCDMALink(len$);
                    table.setModel(linkTableModel);
                    selectedLabel.setText("Active Link");
                    plot.setSelectedLink(len$);
                    plot.setSelectedUser(len$.getUserTerminal());
                    plot.setSelectedInterferer(null);
                    plot.repaint();
                    return;
                }
            }

            arr$ = cell.getDroppedUsers().iterator();
            do
            {
                if(!arr$.hasNext())
                    break label0;
                len$ = (CDMALink)arr$.next();
            } while(!isClickHit(x, y, len$.getCDMACell(), clickRadius));
            linkTableModel.setCDMALink(len$);
            table.setModel(linkTableModel);
            selectedLabel.setText("Link to dropped user");
            plot.setSelectedLink(len$);
            plot.setSelectedUser(len$.getUserTerminal());
            plot.setSelectedInterferer(null);
            plot.repaint();
            return;
        }
label1:
        {
            LOG.debug((new StringBuilder()).append("Clicked button ").append(e.getButton()).append(": (").append(x).append(", ").append(y).append(")").toString());
            if(plot.isPlotUsers())
            {
                int i = 0;
                for(int stop = users.size(); i < stop; i++)
                {
                    UserTerminal u = (UserTerminal)users.get(i);
                    if(Math.abs(u.getLocationX() - x) < clickRadius && Math.abs(u.getLocationY() - y) < clickRadius)
                    {
                        userTableModel.setUserTerminal(u);
                        table.setModel(userTableModel);
                        iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_USERTERMINAL", 2));
                        selectedLabel.setText("Connected - Voice Active User");
                        plot.setSelectedUser(u);
                        plot.setSelectedCell(null);
                        plot.setSelectedInterferer(null);
                        plot.setSelectedLink(null);
                        plot.repaint();
                        return;
                    }
                }

            }
            if(plot.isPlotDroppedUsers())
            {
                users = cdmasystem.getDroppedUsers();
                int i = 0;
                for(int stop = users.size(); i < stop; i++)
                {
                    UserTerminal u = (UserTerminal)users.get(i);
                    if(Math.abs(u.getLocationX() - x) < clickRadius && Math.abs(u.getLocationY() - y) < clickRadius)
                    {
                        userTableModel.setUserTerminal(u);
                        table.setModel(userTableModel);
                        iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_USERTERMINAL", 2));
                        selectedLabel.setText("Dropped User");
                        plot.setSelectedUser(u);
                        plot.setSelectedCell(null);
                        plot.setSelectedInterferer(null);
                        plot.setSelectedLink(null);
                        plot.repaint();
                        return;
                    }
                }

                users = cdmasystem.getNotActivatedUsers();
                i = 0;
                for(int stop = users.size(); i < stop; i++)
                {
                    UserTerminal u = (UserTerminal)users.get(i);
                    if(Math.abs(u.getLocationX() - x) < clickRadius && Math.abs(u.getLocationY() - y) < clickRadius)
                    {
                        userTableModel.setUserTerminal(u);
                        table.setModel(userTableModel);
                        iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_USERTERMINAL", 2));
                        selectedLabel.setText("Not Allowed - Voice Active User");
                        plot.setSelectedUser(u);
                        plot.setSelectedCell(null);
                        plot.setSelectedInterferer(null);
                        plot.setSelectedLink(null);
                        plot.repaint();
                        return;
                    }
                }

            }
            if(plot.isPlotCellCenter())
            {
                CDMACell cells[][] = cdmasystem.getCDMACells();
                int i = 0;
                for(int stop = cells.length; i < stop; i++)
                {
                    CDMACell c = cells[i][selectedSector];
                    if(Math.abs(c.getLocationX() - x) < clickRadius && Math.abs(c.getLocationY() - y) < clickRadius)
                    {
                        cellTableModel.setSelectedCell(c);
                        table.setModel(cellTableModel);
                        iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_BASESTATION", 2));
                        selectedLabel.setText((new StringBuilder()).append("CDMA Cell #").append(c.getCellid()).toString());
                        plot.setSelectedCell(c);
                        plot.setSelectedUser(null);
                        plot.setSelectedInterferer(null);
                        plot.setSelectedLink(null);
                        plot.repaint();
                        return;
                    }
                }

            }
            if(plot.isPlotExternalInterferers())
            {
                java.util.List ints = cdmasystem.getExternalInterferers();
                int i = 0;
                for(int stop = ints.size(); i < stop; i++)
                {
                    CDMAInterferer ei = (CDMAInterferer)ints.get(i);
                    if(Math.abs(ei.getLocationX() - x) < clickRadius && Math.abs(ei.getLocationY() - y) < clickRadius)
                    {
                        interfererTableModel.setSelectedInterferer(ei);
                        table.setModel(interfererTableModel);
                        iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_EXTERNAL_INTERFERER", 2));
                        selectedLabel.setText("CDMA Interferer");
                        plot.setSelectedCell(null);
                        plot.setSelectedUser(null);
                        plot.setSelectedInterferer(ei);
                        plot.setSelectedLink(null);
                        plot.repaint();
                        return;
                    }
                }

            }
            if(plot.getSelectedCell() == null)
                break label1;
            java.util.List cellUsers = plot.getSelectedCell().getActiveConnections();
            Iterator i$;
            UserTerminal u;
            for(i$ = cellUsers.iterator(); i$.hasNext();)
            {
                CDMALink link = (CDMALink)i$.next();
                u = link.getUserTerminal();
                if(Math.abs(u.getLocationX() - x) < clickRadius && Math.abs(u.getLocationY() - y) < clickRadius)
                {
                    userTableModel.setUserTerminal(u);
                    table.setModel(userTableModel);
                    iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_USERTERMINAL", 2));
                    selectedLabel.setText("Connected - Voice Active User");
                    plot.setSelectedUser(u);
                    plot.setSelectedInterferer(null);
                    plot.setSelectedLink(null);
                    plot.repaint();
                    return;
                }
            }

            cellUsers = plot.getSelectedCell().getDroppedUsers();
            i$ = cellUsers.iterator();
            do
            {
                if(!i$.hasNext())
                    break label1;
                CDMALink link = (CDMALink)i$.next();
                u = link.getUserTerminal();
            } while(Math.abs(u.getLocationX() - x) >= clickRadius || Math.abs(u.getLocationY() - y) >= clickRadius);
            userTableModel.setUserTerminal(u);
            table.setModel(userTableModel);
            iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_USERTERMINAL", 2));
            selectedLabel.setText("Dropped User");
            plot.setSelectedUser(u);
            plot.setSelectedInterferer(null);
            plot.setSelectedLink(null);
            plot.repaint();
            return;
        }
        selectedLabel.setText("Click on system element to view details");
        plot.setSelectedCell(null);
        plot.setSelectedUser(null);
        plot.setSelectedInterferer(null);
        plot.setSelectedLink(null);
        table.setModel(systemTableModel);
        iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_SYSTEM", 2));
        plot.repaint();
        if(table.getModel() == cellTableModel)
            cellTableModel.setSelectedCell(null);
        else
        if(table.getModel() == userTableModel)
            userTableModel.setUserTerminal(null);
        else
        if(table.getModel() == interfererTableModel)
            interfererTableModel.setSelectedInterferer(null);
    }

    public void mouseEntered(MouseEvent mouseevent)
    {
    }

    public void mouseExited(MouseEvent mouseevent)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        offsetX = e.getX();
        offsetY = e.getY();
    }

    public void mouseReleased(MouseEvent mouseevent)
    {
    }

    private void selectedSectorChanged()
    {
        if(plot.getSelectedCell() != null)
        {
            CDMACell c = cdmasystem.getCDMACells()[cellTableModel.getSelectedCell().getCellLocationId()][selectedSector];
            cellTableModel.setSelectedCell(c);
            table.setModel(cellTableModel);
            iconLabel.setIcon(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_BASESTATION", 2));
            selectedLabel.setText((new StringBuilder()).append("CDMA Cell #").append(c.getCellid()).toString());
            plot.setSelectedCell(c);
            plot.setSelectedUser(null);
            plot.setSelectedInterferer(null);
            plot.repaint();
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        int x = e.getX() - offsetX;
        int y = e.getY() - offsetY;
        if(plot != null)
        {
            plot.adjustFocusShiftX(x);
            plot.adjustFocusShiftY(y);
            plot.repaint();
            offsetX = e.getX();
            offsetY = e.getY();
        }
    }

    public void mouseMoved(MouseEvent mouseevent)
    {
    }

    private boolean isClickHit(double x, double y, CDMAElement u, double clickRadius)
    {
        return Math.abs(u.getLocationX() - x) < clickRadius && Math.abs(u.getLocationY() - y) < clickRadius;
    }

    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if(e.getScrollType() == 0)
        {
            plot.adjustZoom(e.getUnitsToScroll());
            zoomFactorSlider.setValue((int)(plot.getZoomFactor() * 100D));
            zoomFactorLabel.setText((new StringBuilder()).append("Zoom Factor: ").append(plot.getZoomFactor() * 100D).append("%").toString());
            plot.repaint();
        }
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/presentation/CDMAInsideInformationPanel);
    private final JTable table = new JTable() {

        public TableCellEditor getCellEditor(int row, int column)
        {
            TableCellEditor editor = null;
            if(column == 1 && row == 8 && (getModel() instanceof CDMAUserInfoTableModel))
            {
                JComboBox comboBox = new JComboBox(((CDMAUserInfoTableModel)getModel()).getUser().getActiveList().toArray());
                comboBox.setEditable(false);
                editor = new DefaultCellEditor(comboBox);
            } else
            if(column == 1 && row == 9 && (getModel() instanceof CDMAUserInfoTableModel))
            {
                JComboBox comboBox = new JComboBox(((CDMAUserInfoTableModel)getModel()).getUser().getCDMALinks());
                comboBox.setEditable(false);
                editor = new DefaultCellEditor(comboBox);
            } else
            if(column == 1 && row == 8 && (getModel() instanceof CDMACellTableModel))
            {
                JComboBox comboBox = new JComboBox(((CDMACellTableModel)getModel()).getSelectedCell().getActiveConnections().toArray());
                comboBox.setEditable(false);
                editor = new DefaultCellEditor(comboBox);
            } else
            {
                editor = super.getCellEditor(row, column);
            }
            return editor;
        }

        final CDMAInsideInformationPanel this$0;

            
            {
                this$0 = CDMAInsideInformationPanel.this;
                super();
            }
    }
;
    private double clickRadius;
    private CDMASystem cdmasystem;
    private CDMAPlot plot;
    private final JLabel iconLabel = new JLabel(SeamcatIcons.getImageIcon("SEAMCAT_ICON_CDMA_SYSTEM", 2), 2);
    private final JLabel selectedLabel = new JLabel("Click inside CDMA System to see details");
    private final JLabel clickRadiusLabel = new JLabel("Click Radius: 150 meters");
    private final JSlider clickRadiusSlider = new JSlider(0, 1, 1000, 150);
    private final JLabel zoomFactorLabel = new JLabel("Zoom Factor: 100%");
    private final JSlider zoomFactorSlider = new JSlider(0, 1, 2000, 100);
    private final CDMAUserInfoTableModel userTableModel = new CDMAUserInfoTableModel();
    private final CDMACellTableModel cellTableModel = new CDMACellTableModel();
    private final CDMAInterfererTableModel interfererTableModel = new CDMAInterfererTableModel();
    private final CDMASystemTableModel systemTableModel = new CDMASystemTableModel();
    private final CDMALinkInfoTableModel linkTableModel = new CDMALinkInfoTableModel();
    private final JComboBox systems = new JComboBox();
    private final JRadioButton sector1 = new JRadioButton("1. sector (Main beam at 60\260)");
    private final JRadioButton sector2 = new JRadioButton("2. sector (Main beam at 180\260)");
    private final JRadioButton sector3 = new JRadioButton("3. sector (Main beam at -60\260)");
    private int selectedSector;
    private int offsetX;
    private int offsetY;















}