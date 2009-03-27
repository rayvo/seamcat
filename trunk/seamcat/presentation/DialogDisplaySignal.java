// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogDisplaySignal.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.seamcat.mathematics.Mathematics;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, FileDataIO

public class DialogDisplaySignal extends EscapeDialog
{
    public class ControlButtonPanel extends JPanel
    {

        private JButton save;
        private JButton close;
        private JButton help;
        final DialogDisplaySignal this$0;

        public ControlButtonPanel()
        {
            this$0 = DialogDisplaySignal.this;
            super(new GridBagLayout());
            save = new JButton(DialogDisplaySignal.STRINGLIST.getString("BTN_CAPTION_SAVE"));
            close = new JButton(DialogDisplaySignal.STRINGLIST.getString("BTN_CAPTION_CLOSE"));
            help = new JButton(DialogDisplaySignal.STRINGLIST.getString("BTN_CAPTION_HELP"));
            close.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    dispose();
                }

                final DialogDisplaySignal val$this$0;
                final ControlButtonPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            save.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    JFileChooser chooser = new JFileChooser();
                    FileDataIO fileIO = new FileDataIO();
                    int returnVal = chooser.showSaveDialog(_fld0);
                    fileIO.setFile(chooser.getSelectedFile());
                    if(returnVal == 0)
                        fileIO.saveDoubleArray(data);
                }

                final DialogDisplaySignal val$this$0;
                final ControlButtonPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            GridBagConstraints gc = new GridBagConstraints(0, -1, 1, 1, 1.0D, 50D, 14, 2, new Insets(0, 0, 0, 0), 1, 1);
            add(Box.createVerticalGlue(), gc);
            gc.weighty = 1.0D;
            add(save, gc);
            add(close, gc);
            add(help, gc);
        }
    }

    public class IdentificationPanel extends JPanel
    {

        public void setName(String name)
        {
            lbName.setText(name);
        }

        public void setMean(double mean)
        {
            lbMean.setText((new StringBuilder()).append(DialogDisplaySignal.STRINGLIST.getString("RESULTS_VECTOR_MEAN_PREFIX")).append(nf.format(mean)).toString());
        }

        public void setStddev(double stddev)
        {
            lbStddev.setText((new StringBuilder()).append(DialogDisplaySignal.STRINGLIST.getString("RESULTS_VECTOR_STD_PREFIX")).append(nf.format(stddev)).toString());
        }

        public String formatDouble(double _value)
        {
            return nf.format(_value);
        }

        private NumberFormat nf;
        private JLabel lbName;
        private JLabel lbMean;
        private JLabel lbStddev;
        final DialogDisplaySignal this$0;

        public IdentificationPanel()
        {
            this$0 = DialogDisplaySignal.this;
            super(new GridLayout(3, 1));
            nf = new DecimalFormat("0.00");
            lbName = new JLabel();
            lbMean = new JLabel();
            lbStddev = new JLabel();
            add(lbName);
            add(lbMean);
            add(lbStddev);
            setBorder(new TitledBorder(DialogDisplaySignal.STRINGLIST.getString("RESULTS_VECTOR_IDENTIFICATION_TITLE")));
        }
    }

    public class DisplaySelectorPanel extends JPanel
    {

        private JRadioButton vector;
        private JRadioButton cdf;
        private JRadioButton density;
        private ButtonGroup buttons;
        final DialogDisplaySignal this$0;




        public DisplaySelectorPanel()
        {
            this$0 = DialogDisplaySignal.this;
            super();
            vector = new JRadioButton(DialogDisplaySignal.STRINGLIST.getString("RESULTS_VECTOR_GRAPH_VECTOR_BUTTON_TITLE"));
            cdf = new JRadioButton(DialogDisplaySignal.STRINGLIST.getString("RESULTS_VECTOR_GRAPH_CDF_BUTTON_TITLE"));
            density = new JRadioButton(DialogDisplaySignal.STRINGLIST.getString("RESULTS_VECTOR_GRAPH_DENSITY_BUTTON_TITLE"));
            buttons = new ButtonGroup();
            setLayout(new GridLayout(3, 1));
            vector.setSelected(true);
            add(vector);
            add(cdf);
            add(density);
            buttons.add(vector);
            buttons.add(cdf);
            buttons.add(density);
            setBorder(new TitledBorder(DialogDisplaySignal.STRINGLIST.getString("RESULTS_VECTOR_BUTTONS_TITLE")));
        }
    }


    public DialogDisplaySignal(Frame owner, String xTitle, String yTitle)
    {
        super(owner, true);
        selector = new DisplaySelectorPanel();
        idPanel = new IdentificationPanel();
        vectorSeries = new XYSeries(STRINGLIST.getString("VECTOR_GRAPH_TITLE"));
        cumulativeSeries = new XYSeries(STRINGLIST.getString("CUMULATIVE_DISTRIBUTION_SERIES_TITLE"));
        signalIsConstant = false;
        cardLayout = new CardLayout();
        graphPanel = new JPanel(cardLayout);
        init(xTitle, yTitle);
        setLocationRelativeTo(owner);
    }

    public DialogDisplaySignal(JDialog owner, String xTitle, String yTitle)
    {
        super(owner, true);
        selector = new DisplaySelectorPanel();
        idPanel = new IdentificationPanel();
        vectorSeries = new XYSeries(STRINGLIST.getString("VECTOR_GRAPH_TITLE"));
        cumulativeSeries = new XYSeries(STRINGLIST.getString("CUMULATIVE_DISTRIBUTION_SERIES_TITLE"));
        signalIsConstant = false;
        cardLayout = new CardLayout();
        graphPanel = new JPanel(cardLayout);
        init(xTitle, yTitle);
        setLocationRelativeTo(owner);
    }

    private void init(String xTitle, String yTitle)
    {
        selector.vector.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                cardLayout.show(graphPanel, "vector");
            }

            final DialogDisplaySignal this$0;

            
            {
                this$0 = DialogDisplaySignal.this;
                super();
            }
        }
);
        selector.cdf.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                cardLayout.show(graphPanel, "cumulative");
            }

            final DialogDisplaySignal this$0;

            
            {
                this$0 = DialogDisplaySignal.this;
                super();
            }
        }
);
        selector.density.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(signalIsConstant)
                {
                    JOptionPane.showMessageDialog(DialogDisplaySignal.this, DialogDisplaySignal.STRINGLIST.getString("HISTOGRAM_CONSTANTSIGNAL_WARNING"));
                    selector.vector.setSelected(true);
                    cardLayout.show(graphPanel, "vector");
                } else
                {
                    cardLayout.show(graphPanel, "density");
                }
            }

            final DialogDisplaySignal this$0;

            
            {
                this$0 = DialogDisplaySignal.this;
                super();
            }
        }
);
        JFreeChart chart = ChartFactory.createXYLineChart(STRINGLIST.getString("VECTOR_GRAPH_TITLE"), xTitle, yTitle, new XYSeriesCollection(vectorSeries), PlotOrientation.VERTICAL, true, true, false);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)chart.getXYPlot().getRenderer();
        renderer.setShapesVisible(false);
        renderer.setShapesFilled(false);
        vectorChart = new ChartPanel(chart);
        vectorChart.setMouseZoomable(true, false);
        JFreeChart densityGraph = ChartFactory.createHistogram(STRINGLIST.getString("HISTOGRAM_TITLE"), yTitle, STRINGLIST.getString("HISTOGRAM_AXIX_TITLE_Y"), new HistogramDataset(), PlotOrientation.VERTICAL, false, false, false);
        densityChart = new ChartPanel(densityGraph);
        XYSeriesCollection cumulativeDataset = new XYSeriesCollection();
        cumulativeDataset.addSeries(cumulativeSeries);
        JFreeChart chart = ChartFactory.createXYLineChart(STRINGLIST.getString("CUMULATIVE_DISTRIBUTION_TITLE"), yTitle, STRINGLIST.getString("CUMULATIVE_DISTRIBUTION_AXIX_TITLE_Y"), cumulativeDataset, PlotOrientation.VERTICAL, false, true, false);
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)chart.getXYPlot().getRenderer();
        renderer.setShapesVisible(false);
        cumulativeChart = new ChartPanel(chart);
        cumulativeChart.setMouseZoomable(true, false);
        JPanel right = new JPanel(new GridLayout(3, 1));
        right.add(selector);
        right.add(idPanel);
        right.add(new ControlButtonPanel());
        graphPanel.add(vectorChart, "vector");
        graphPanel.add(cumulativeChart, "cumulative");
        graphPanel.add(densityChart, "density");
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(right, "East");
        getContentPane().add(graphPanel, "Center");
        setDefaultLookAndFeelDecorated(true);
        pack();
    }

    public void setVisible()
    {
        throw new UnsupportedOperationException("Use show(double[], String)");
    }

    public boolean isSorted(double _data[])
    {
        boolean sorted = true;
        for(int i = 0; i < _data.length - 1 && sorted; i++)
            sorted = _data[i] < _data[i + 1];

        return sorted;
    }

    public void show(double _data[], String title)
    {
        try
        {
            if(isSorted(_data))
                LOG.info((new StringBuilder()).append("Data is sorted - length is ").append(data.length).toString());
            data = _data;
            vectorSeries.clear();
            cumulativeSeries.clear();
            double mean = Mathematics.getAverage(data);
            double stddev = Mathematics.getStdDev(data, mean);
            signalIsConstant = idPanel.formatDouble(stddev).equals(idPanel.formatDouble(0.0D));
            if(data.length >= EVENT_LIMIT)
            {
                int factor = data.length / EVENT_LIMIT;
                displayData = new double[Math.min(data.length, EVENT_LIMIT)];
                int i = 0;
                for(int j = 0; j < displayData.length;)
                {
                    displayData[j] = data[i];
                    j++;
                    i += factor;
                }

            } else
            {
                displayData = data;
            }
            if(isSorted(displayData))
                LOG.info("Display data is sorted");
            vectorSeries.setDescription(title);
            for(int i = 0; i < displayData.length; i++)
                vectorSeries.add(i, displayData[i]);

            graphPanel.remove(densityChart);
            if(!signalIsConstant)
            {
                HistogramDataset densityDataset = new HistogramDataset();
                densityDataset.addSeries("Density", data, (int)Math.sqrt(data.length));
                densityDataset.setType(HistogramType.SCALE_AREA_TO_1);
                JFreeChart densityGraph = ChartFactory.createHistogram(STRINGLIST.getString("HISTOGRAM_TITLE"), STRINGLIST.getString("HISTOGRAM_AXIX_TITLE_X"), STRINGLIST.getString("HISTOGRAM_AXIX_TITLE_Y"), densityDataset, PlotOrientation.VERTICAL, false, false, false);
                densityChart = new ChartPanel(densityGraph);
                densityChart.setMouseZoomable(true);
                graphPanel.add(densityChart, "density");
            }
            if(signalIsConstant)
            {
                cumulativeSeries.add(displayData[0], 0.0D);
                cumulativeSeries.add(displayData[0], 1.0D);
                ((NumberAxis)cumulativeChart.getChart().getXYPlot().getRangeAxis()).setRange(new Range(0.0D, 1.0D));
            } else
            {
                double sortedDisplayData[] = new double[displayData.length];
                System.arraycopy(displayData, 0, sortedDisplayData, 0, displayData.length);
                Arrays.sort(sortedDisplayData);
                for(int i = 0; i < sortedDisplayData.length; i++)
                    cumulativeSeries.add(sortedDisplayData[i], new Double((double)i / (double)sortedDisplayData.length));

            }
            NumberAxis x = (NumberAxis)vectorChart.getChart().getXYPlot().getDomainAxis();
            NumberAxis y = (NumberAxis)vectorChart.getChart().getXYPlot().getRangeAxis();
            x.setAutoRange(false);
            x.setRange(new Range(0.0D, EVENT_LIMIT));
            if(!signalIsConstant)
            {
                y.setAutoRange(false);
                double min = Mathematics.min(displayData) - stddev;
                double max = Mathematics.max(displayData) + stddev;
                y.setRange(new Range(min, max));
                x = (NumberAxis)densityChart.getChart().getXYPlot().getDomainAxis();
                x.setRange(new Range(min, max));
            } else
            {
                y.setAutoRange(true);
            }
            idPanel.setName(title);
            idPanel.setMean(mean);
            idPanel.setStddev(stddev);
            setTitle((new StringBuilder()).append(STRINGLIST.getString("RESULTS_VECTOR_GRAPH_TITLE_PREFIX")).append(title).toString());
            selector.vector.doClick();
            setVisible(true);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, (new StringBuilder()).append("Unable to show generated values, due to:\n").append(e.getMessage()).toString(), "Error showing vector", 0);
        }
    }

    public void hideDialog()
    {
        vectorSeries.clear();
        cumulativeSeries.clear();
        graphPanel.remove(densityChart);
        displayData = null;
        setVisible(false);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/DialogDisplaySignal);
    private static final ResourceBundle STRINGLIST;
    private static final int EVENT_LIMIT;
    private DisplaySelectorPanel selector;
    private IdentificationPanel idPanel;
    private ChartPanel vectorChart;
    private double displayData[];
    private double data[];
    private XYSeries vectorSeries;
    private XYSeries cumulativeSeries;
    private ChartPanel densityChart;
    private ChartPanel cumulativeChart;
    private boolean signalIsConstant;
    private CardLayout cardLayout;
    private JPanel graphPanel;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        EVENT_LIMIT = Integer.parseInt(STRINGLIST.getString("VECTOR_GRAPH_EVENT_LIMIT"));
    }






}
