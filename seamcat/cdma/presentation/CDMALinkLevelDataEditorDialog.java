// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMALinkLevelDataEditorDialog.java

package org.seamcat.cdma.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.jfree.chart.*;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.seamcat.cdma.*;
import org.seamcat.presentation.CDMALinkLevelDataEditBasicsDialog;
import org.seamcat.presentation.EscapeDialog;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.SpringUtilities;

// Referenced classes of package org.seamcat.cdma.presentation:
//            CDMAEditModel

public class CDMALinkLevelDataEditorDialog extends EscapeDialog
{
    private static class LinkLevelCellRenderer extends JLabel
        implements TableCellRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            if(value != null)
                setText(value.toString());
            else
                setText("");
            if(isSelected)
            {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else
            {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            if(table.getModel().getValueAt(row, 0) == null && !isSelected)
                setBackground(ILLEGAL_BGCOLOR);
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            setOpaque(true);
            return this;
        }

        private static final Color ILLEGAL_BGCOLOR = new Color(255, 120, 120);


        public LinkLevelCellRenderer()
        {
            super("", 4);
        }
    }

    private class ControlButtonsPanel extends NavigateButtonPanel
    {

        public void btnOkActionPerformed()
        {
            accept = true;
            if(storeDatapoints())
                setVisible(false);
        }

        public void btnCancelActionPerformed()
        {
            setVisible(false);
        }

        final CDMALinkLevelDataEditorDialog this$0;

        private ControlButtonsPanel()
        {
            this$0 = CDMALinkLevelDataEditorDialog.this;
            super();
        }

    }

    private static class TitlePanel extends JPanel
    {

        void setPanelTitle(String title)
        {
            lbTitle.setText(title);
        }

        private final JLabel lbTitle = new JLabel("title undefined");

        TitlePanel()
        {
            super(new FlowLayout(1));
            lbTitle.setFont(getFont().deriveFont(1));
            add(lbTitle);
        }
    }

    private class SideButtonPanel extends JPanel
    {
        private class DataPointAddDeletePanel extends JPanel
        {

            final SideButtonPanel this$1;

            DataPointAddDeletePanel()
            {
                this$1 = SideButtonPanel.this;
                super(new GridLayout(2, 1));
                JButton btnAdd = new JButton(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("BTN_CAPTION_ADD"));
                btnAdd.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e)
                    {
                        selectCell(addTableRow(), 0);
                    }

                    final SideButtonPanel val$this$1;
                    final DataPointAddDeletePanel this$2;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
                }
);
                JButton btnDelete = new JButton(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("BTN_CAPTION_DELETE"));
                btnDelete.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e)
                    {
                        removeTableRow();
                    }

                    final SideButtonPanel val$this$1;
                    final DataPointAddDeletePanel this$2;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
                }
);
                setBorder(new TitledBorder(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_DATAPOINTS")));
                add(btnAdd);
                add(btnDelete);
            }
        }

        private class PathSelectorPanel extends JPanel
        {

            void selectPath(int path)
            {
                setSelectedPathModel(path);
            }

            private final JRadioButton rb1Path = new JRadioButton(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_1PATH"));
            private final JRadioButton rb2Path = new JRadioButton(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_2PATH"));
            final SideButtonPanel this$1;

            PathSelectorPanel()
            {
                this$1 = SideButtonPanel.this;
                super(new GridLayout(2, 1));
                ButtonGroup bgPath = new ButtonGroup();
                bgPath.add(rb1Path);
                bgPath.add(rb2Path);
                rb1Path.setSelected(true);
                rb1Path.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e)
                    {
                        selectPath(1);
                    }

                    final SideButtonPanel val$this$1;
                    final PathSelectorPanel this$2;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
                }
);
                rb2Path.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e)
                    {
                        selectPath(2);
                    }

                    final SideButtonPanel val$this$1;
                    final PathSelectorPanel this$2;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
                }
);
                setBorder(new TitledBorder(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_PATHS")));
                add(rb1Path);
                add(rb2Path);
            }
        }


        void btnBasicsActionPerformed()
        {
            if(storeDatapoints() && basicsDialog.showDialog(data))
                setData(data);
        }

        private final PathSelectorPanel pathSelectorPanel = new PathSelectorPanel();
        private final DataPointAddDeletePanel dataPointAddDeletePanel = new DataPointAddDeletePanel();
        private final CDMALinkLevelDataEditBasicsDialog basicsDialog;
        final CDMALinkLevelDataEditorDialog this$0;

        SideButtonPanel()
        {
            this$0 = CDMALinkLevelDataEditorDialog.this;
            super();
            basicsDialog = new CDMALinkLevelDataEditBasicsDialog(CDMALinkLevelDataEditorDialog.this);
            JButton btnBasics = new JButton(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_LINKBASICS"));
            btnBasics.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    btnBasicsActionPerformed();
                }

                final CDMALinkLevelDataEditorDialog val$this$0;
                final SideButtonPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            JButton btnAdvanced = new JButton("Advanced");
            btnAdvanced.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    double geo = 7D;
                    CDMALinkLevelDataPoint point = new CDMALinkLevelDataPoint(data.getFrequency(), 1, geo, 0.0D, 0.0D);
                    data.getLinkLevelDataPoint(point);
                    setData(data);
                }

                final CDMALinkLevelDataEditorDialog val$this$0;
                final SideButtonPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            JPanel btnBasicsPanel = new JPanel(new GridLayout());
            btnBasicsPanel.setBorder(new TitledBorder(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_BTN_BORDER")));
            btnBasicsPanel.add(btnBasics);
            setLayout(new BoxLayout(this, 3));
            add(pathSelectorPanel);
            add(dataPointAddDeletePanel);
            add(btnBasicsPanel);
            add(Box.createVerticalStrut(1000));
        }
    }

    private static class CenterPanel extends JPanel
        implements TableModelListener
    {
        private static class GraphPanel extends JPanel
        {

            void updateGraph()
            {
                CDMALinkLevelDataEditorDialog.LOG.debug("Updating graph");
                setGraph(model);
            }

            void setGraph(CDMAEditModel model)
            {
                this.model = model;
                dataSet.removeAllSeries();
                if(model != null)
                {
                    XYSeriesCollection collection = model.getXYSeriesCollection();
                    int x = 0;
                    for(int stop = collection.getSeriesCount(); x < stop; x++)
                        dataSet.addSeries(collection.getSeries(x));

                } else
                {
                    CDMALinkLevelDataEditorDialog.LOG.debug("Graph model is null");
                }
            }

            void setRangeLabel(String rangeLabel)
            {
                chart.getXYPlot().getRangeAxis().setLabel(rangeLabel);
            }

            void setGraphRangeLegend(String legend)
            {
                chart.getXYPlot().getRangeAxis().setLabel(legend);
            }

            private final XYSeriesCollection dataSet = new XYSeriesCollection();
            private final JFreeChart chart;
            private final ChartPanel chartPanel;
            private CDMAEditModel model;

            GraphPanel()
            {
                super(new GridLayout());
                chart = ChartFactory.createXYLineChart("", CDMALinkLevelDataEditorDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_GEOMETRY"), "", dataSet, PlotOrientation.VERTICAL, true, true, false);
                chartPanel = new ChartPanel(chart);
                for(int x = 1; x < CDMAEditModel.COLUMN_NAMES.length; x++)
                    dataSet.addSeries(new XYSeries(CDMAEditModel.COLUMN_NAMES[x]));

                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)chart.getXYPlot().getRenderer();
                renderer.setShapesVisible(true);
                chartPanel.setMouseZoomable(true, false);
                add(chartPanel);
            }
        }


        void setSelectedPathModel(int path)
        {
            pointTablePanel.setSelectedPathModel(path);
            graphPanel.setGraph(getPathModel(path));
        }

        void setModels(CDMAEditModel path1Model, CDMAEditModel path2Model, String pct, String targetType)
        {
            pointTablePanel.setModels(path1Model, path2Model);
            if(path1Model != null && path2Model != null)
            {
                pointTablePanel.setSelectedPathModel(1);
                graphPanel.setRangeLabel(String.format(CDMALinkLevelDataEditorDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_GRAPH_RANGE_LABEL"), new Object[] {
                    pct, targetType
                }));
            }
        }

        CDMAEditModel getPathModel(int path)
        {
            return pointTablePanel.getPathModel(path);
        }

        int addTableRow()
        {
            return pointTablePanel.addTableRow();
        }

        void removeTableRow()
        {
            pointTablePanel.removeTableRow();
        }

        public void tableChanged(TableModelEvent e)
        {
            graphPanel.updateGraph();
        }

        private final PointTablePanel pointTablePanel = new PointTablePanel();
        private final GraphPanel graphPanel = new GraphPanel();


        CenterPanel()
        {
            super(new SpringLayout());
            add(pointTablePanel);
            add(Box.createVerticalGlue());
            add(graphPanel);
            SpringUtilities.makeCompactGrid(this, 3, 1, 0, 0, 0, 0);
        }
    }


    public CDMALinkLevelDataEditorDialog(JDialog owner)
    {
        super(owner, true);
        this.owner = owner;
        Container cp = getContentPane();
        cp.add(titlePanel, "North");
        cp.add(sideButtonPanel, "East");
        cp.add(centerPanel, "Center");
        cp.add(new ControlButtonsPanel(), "South");
        cp.add(Box.createHorizontalStrut(50), "West");
        setTitle(STRINGLIST.getString("LIBRARY_CDMA_LLD_DETAILS_WINDOWTITLE"));
        setSize(800, 600);
    }

    private void setSelectedPathModel(int path)
    {
        centerPanel.setSelectedPathModel(path);
        titlePanel.setPanelTitle(data.getPathDescription(path));
    }

    public boolean show(CDMALinkLevelData data)
    {
        setData(data);
        setLocationRelativeTo(owner);
        accept = false;
        setVisible(true);
        if(accept)
            storeDatapoints();
        setData(null);
        return accept;
    }

    private void setData(CDMALinkLevelData data)
    {
        this.data = data;
        if(data != null)
        {
            centerPanel.setModels((CDMAEditModel)data.getTableModel(1), (CDMAEditModel)data.getTableModel(2), data.getTargetERpct(), data.getTargetERType().toString());
            setSelectedPathModel(1);
        } else
        {
            centerPanel.setModels(null, null, "", "");
        }
    }

    private boolean storeDatapoints()
    {
        try
        {
            data.storeDataModel(centerPanel.getPathModel(1), 1);
            data.storeDataModel(centerPanel.getPathModel(2), 2);
            return true;
        }
        catch(NoGeometryException ex)
        {
            JOptionPane.showMessageDialog(this, STRINGLIST.getString("CDMA_NO_GEOMETRY"), STRINGLIST.getString("CDMA_NO_GEOMETRY_TITLE"), 0);
        }
        return false;
    }

    private int addTableRow()
    {
        return centerPanel.addTableRow();
    }

    private void removeTableRow()
    {
        centerPanel.removeTableRow();
    }

    private void selectCell(int row, int col)
    {
        CenterPanel.PointTablePanel.access._mth200(centerPanel.pointTablePanel).changeSelection(row, col, true, false);
        CenterPanel.PointTablePanel.access._mth200(centerPanel.pointTablePanel).editCellAt(row, col);
        CenterPanel.PointTablePanel.access._mth200(centerPanel.pointTablePanel).grabFocus();
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/presentation/CDMALinkLevelDataEditorDialog);
    private static final ResourceBundle STRINGLIST;
    private final CenterPanel centerPanel = new CenterPanel();
    private final SideButtonPanel sideButtonPanel = new SideButtonPanel();
    private final TitlePanel titlePanel = new TitlePanel();
    private final JDialog owner;
    private CDMALinkLevelData data;
    private boolean accept;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }










}