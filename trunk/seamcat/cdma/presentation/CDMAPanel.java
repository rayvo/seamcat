// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAPanel.java

package org.seamcat.cdma.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.cdma.CDMALinkLevelData;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.distribution.Distribution;
import org.seamcat.distribution.StairDistribution;
import org.seamcat.function.Point2D;
import org.seamcat.model.*;
import org.seamcat.presentation.*;
import org.seamcat.presentation.components.PropagationSelectPanel;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.cdma.presentation:
//            ReferenceCellSelectionPanel, CDMALinkLevelDataEditorDialog

public class CDMAPanel extends JTabbedPane
{
    public class UplinkModelPanel extends InnerCDMAPanel
    {

        public void initializeGui()
        {
            setBorderName("Uplink Model");
            gui_targetNetworkNoiseRise = new FormattedCalculatorField(getTargetNetworkNoiseRise(), parent);
            gui_targetNetworkNoiseRise.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setTargetNetworkNoiseRise(((Number)gui_targetNetworkNoiseRise.getValue()).doubleValue());
                }

                final UplinkModelPanel this$1;

                
                {
                    this$1 = UplinkModelPanel.this;
                    super();
                }
            }
);
            gui_targetNetworkNoiseRise_label = new JLabel("Target Network Noise Rise", 2);
            addGuiPair(gui_targetNetworkNoiseRise_label, gui_targetNetworkNoiseRise, new JLabel("dB"));
            gui_msMaximumTransitPower = new FormattedCalculatorField(getMsMaximumTransitPower(), parent);
            gui_msMaximumTransitPower.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setMsMaximumTransitPower(((Number)gui_msMaximumTransitPower.getValue()).doubleValue());
                }

                final UplinkModelPanel this$1;

                
                {
                    this$1 = UplinkModelPanel.this;
                    super();
                }
            }
);
            gui_msMaximumTransitPower_label = new JLabel("Mobile Station Maximum Transit Power", 2);
            addGuiPair(gui_msMaximumTransitPower_label, gui_msMaximumTransitPower, new JLabel("dBm"));
            gui_msPowerControlRange = new FormattedCalculatorField(getMsPowerControlRange(), parent);
            gui_msPowerControlRange.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setMsPowerControlRange(((Number)gui_msPowerControlRange.getValue()).doubleValue());
                }

                final UplinkModelPanel this$1;

                
                {
                    this$1 = UplinkModelPanel.this;
                    super();
                }
            }
);
            gui_msPowerControlRange_label = new JLabel("Mobile Station Power Control Range", 2);
            addGuiPair(gui_msPowerControlRange_label, gui_msPowerControlRange, new JLabel("dB"));
            gui_msPowerControlStep = new FormattedCalculatorField(getMsPowerControlStep(), parent);
            gui_msPowerControlStep.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setMsPowerControlStep(((Number)gui_msPowerControlStep.getValue()).doubleValue());
                }

                final UplinkModelPanel this$1;

                
                {
                    this$1 = UplinkModelPanel.this;
                    super();
                }
            }
);
            gui_msPowerControlStep_label = new JLabel("Mobile Station Power Control Step", 2);
            gui_pcConvergensThreshold = new FormattedCalculatorField(getPcConvergensThreshold(), parent);
            gui_pcConvergensThreshold.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setPcConvergensThreshold(((Number)gui_pcConvergensThreshold.getValue()).doubleValue());
                }

                final UplinkModelPanel this$1;

                
                {
                    this$1 = UplinkModelPanel.this;
                    super();
                }
            }
);
            gui_pcConvergensThreshold_label = new JLabel("PC Convergence precision (linear)", 2);
            addGuiPair(gui_pcConvergensThreshold_label, gui_pcConvergensThreshold, new JLabel("dB"));
        }

        protected void refreshFromModel()
        {
            gui_targetNetworkNoiseRise.setValue(new Double(getTargetNetworkNoiseRise()));
            gui_msMaximumTransitPower.setValue(new Double(getMsMaximumTransitPower()));
            gui_msPowerControlRange.setValue(new Double(getMsPowerControlRange()));
            gui_msPowerControlStep.setValue(new Double(getMsPowerControlStep()));
            gui_pcConvergensThreshold.setValue(new Double(getPcConvergensThreshold()));
        }

        protected void updateModel()
        {
            setTargetNetworkNoiseRise(((Number)gui_targetNetworkNoiseRise.getValue()).doubleValue());
            setMsMaximumTransitPower(((Number)gui_msMaximumTransitPower.getValue()).doubleValue());
            setMsPowerControlRange(((Number)gui_msPowerControlRange.getValue()).doubleValue());
            setMsPowerControlStep(((Number)gui_msPowerControlStep.getValue()).doubleValue());
            setPcConvergensThreshold(((Number)gui_pcConvergensThreshold.getValue()).doubleValue());
        }

        private JFormattedTextField gui_targetNetworkNoiseRise;
        private JFormattedTextField gui_msMaximumTransitPower;
        private JFormattedTextField gui_msPowerControlRange;
        private JFormattedTextField gui_msPowerControlStep;
        private JFormattedTextField gui_pcConvergensThreshold;
        private JLabel gui_targetNetworkNoiseRise_label;
        private JLabel gui_msMaximumTransitPower_label;
        private JLabel gui_msPowerControlRange_label;
        private JLabel gui_msPowerControlStep_label;
        private JLabel gui_pcConvergensThreshold_label;
        final CDMAPanel this$0;






        public UplinkModelPanel()
        {
            this$0 = CDMAPanel.this;
            super();
        }
    }

    public class DownlinkModelPanel extends InnerCDMAPanel
    {

        public void initializeGui()
        {
            setBorderName("Downlink Model");
            gui_bsPilotFraction = new FormattedCalculatorField(getBsPilotFraction(), parent);
            gui_bsPilotFraction.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setBsPilotFraction(((Number)gui_bsPilotFraction.getValue()).doubleValue());
                }

                final DownlinkModelPanel this$1;

                
                {
                    this$1 = DownlinkModelPanel.this;
                    super();
                }
            }
);
            gui_bsPilotFraction_label = new JLabel("Base Station Pilot Channel Fraction", 2);
            addGuiPair(gui_bsPilotFraction_label, gui_bsPilotFraction, new JLabel(""));
            gui_bsOverheadFraction = new FormattedCalculatorField(getBsOverheadFraction(), parent);
            gui_bsOverheadFraction.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setBsOverheadFraction(((Number)gui_bsOverheadFraction.getValue()).doubleValue());
                }

                final DownlinkModelPanel this$1;

                
                {
                    this$1 = DownlinkModelPanel.this;
                    super();
                }
            }
);
            gui_bsOverheadFraction_label = new JLabel("Base Station Overhead Channel Fraction", 2);
            addGuiPair(gui_bsOverheadFraction_label, gui_bsOverheadFraction, new JLabel(""));
            gui_bsMaximumBroadcastPower = new FormattedCalculatorField((new Double(getBsMaximumBroadcastPower())).doubleValue(), parent);
            gui_bsMaximumBroadcastPower.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setBsMaximumBroadcastPower(((Number)gui_bsMaximumBroadcastPower.getValue()).doubleValue());
                }

                final DownlinkModelPanel this$1;

                
                {
                    this$1 = DownlinkModelPanel.this;
                    super();
                }
            }
);
            gui_bsMaximumBroadcastPower_label = new JLabel("Base Station Maximum Broadcast Power", 2);
            addGuiPair(gui_bsMaximumBroadcastPower_label, gui_bsMaximumBroadcastPower, new JLabel("dBm"));
            gui_bsMaximumTrafficChannelFraction = new FormattedCalculatorField((new Double(getMaximumTrafficChannelFraction())).doubleValue(), parent);
            gui_bsMaximumTrafficChannelFraction.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setBsMaximumTrafficChannelFraction(((Number)gui_bsMaximumTrafficChannelFraction.getValue()).doubleValue());
                }

                final DownlinkModelPanel this$1;

                
                {
                    this$1 = DownlinkModelPanel.this;
                    super();
                }
            }
);
            gui_bsMaximumTrafficChannelFraction_label = new JLabel("Base Station Maximum Traffic Channel Power", 2);
            addGuiPair(gui_bsMaximumTrafficChannelFraction_label, gui_bsMaximumTrafficChannelFraction, new JLabel(""));
            gui_successThreshold = new FormattedCalculatorField((new Double(getSuccessThreshold())).doubleValue(), parent);
            gui_successThreshold.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setSuccessThreshold(((Number)gui_successThreshold.getValue()).doubleValue());
                }

                final DownlinkModelPanel this$1;

                
                {
                    this$1 = DownlinkModelPanel.this;
                    super();
                }
            }
);
            gui_cutOffCondition_label = new JLabel("Success Threshold", 2);
            addGuiPair(gui_cutOffCondition_label, gui_successThreshold, new JLabel("dB"));
        }

        protected void refreshFromModel()
        {
            gui_bsPilotFraction.setValue(new Double(getBsPilotFraction()));
            gui_bsOverheadFraction.setValue(new Double(getBsOverheadFraction()));
            gui_bsMaximumBroadcastPower.setValue(new Double(getBsMaximumBroadcastPower()));
            gui_bsMaximumTrafficChannelFraction.setValue(new Double(getMaximumTrafficChannelFraction()));
            gui_successThreshold.setValue(new Double(getSuccessThreshold()));
        }

        protected void updateModel()
        {
            setBsPilotFraction(((Number)gui_bsPilotFraction.getValue()).doubleValue());
            setBsOverheadFraction(((Number)gui_bsOverheadFraction.getValue()).doubleValue());
            setBsMaximumBroadcastPower(((Number)gui_bsMaximumBroadcastPower.getValue()).doubleValue());
            setBsMaximumTrafficChannelFraction(((Number)gui_bsMaximumTrafficChannelFraction.getValue()).doubleValue());
            setSuccessThreshold(((Number)gui_successThreshold.getValue()).doubleValue());
        }

        private JFormattedTextField gui_bsPilotFraction;
        private JFormattedTextField gui_bsOverheadFraction;
        private JFormattedTextField gui_bsMaximumBroadcastPower;
        private JFormattedTextField gui_bsMaximumTrafficChannelFraction;
        private JFormattedTextField gui_successThreshold;
        private JLabel gui_bsPilotFraction_label;
        private JLabel gui_bsOverheadFraction_label;
        private JLabel gui_bsMaximumBroadcastPower_label;
        private JLabel gui_bsMaximumTrafficChannelFraction_label;
        private JLabel gui_cutOffCondition_label;
        final CDMAPanel this$0;






        public DownlinkModelPanel()
        {
            this$0 = CDMAPanel.this;
            super();
        }
    }

    public class PositioningPanel extends InnerCDMAPanel
    {

        public void initializeGui()
        {
            container = posPanel;
            posPanel.setLayout(grid);
            powerDistDialog = new DistributionDialog(parent, true);
            setBorderName("Positioning");
            gui_numberOfCellSitesInPcCluster = new JFormattedTextField(new Integer(getNumberOfCellSitesInPcCluster()));
            gui_numberOfCellSitesInPcCluster.setEditable(false);
            gui_numberOfCellSitesInPcCluster.setHorizontalAlignment(4);
            gui_numberOfCellSitesInPcCluster.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            gui_numberOfCellSitesInPcCluster.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setNumberOfCellSitesInPcCluster(((Number)gui_numberOfCellSitesInPcCluster.getValue()).intValue());
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_numberOfCellSitesInPcCluster_label = new JLabel("Number of cell sites in Power Control Cluster", 2);
            addGuiPair(gui_numberOfCellSitesInPcCluster_label, gui_numberOfCellSitesInPcCluster, new JLabel(""));
            gui_typeOfCellsInPcCluster_omni = new JRadioButton("Omni", isTypeOfCellsInPcClusterOmni());
            gui_typeOfCellsInPcCluster_omni.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setTypeOfCellsInPcClusterOmni(gui_typeOfCellsInPcCluster_omni.isSelected());
                    gui_numberOfCellSitesInPcCluster.setValue(new Integer(19));
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_typeOfCellsInPcCluster_trisec = new JRadioButton("Tri-sector", !isTypeOfCellsInPcClusterOmni());
            gui_typeOfCellsInPcCluster_trisec.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setTypeOfCellsInPcClusterOmni(gui_typeOfCellsInPcCluster_omni.isSelected());
                    gui_numberOfCellSitesInPcCluster.setValue(new Integer(57));
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_typeOfCellsInPcCluster_group = new ButtonGroup();
            gui_typeOfCellsInPcCluster_group.add(gui_typeOfCellsInPcCluster_omni);
            gui_typeOfCellsInPcCluster_group.add(gui_typeOfCellsInPcCluster_trisec);
            gui_typeOfCellsInPcCluster_label = new JLabel("Type of cells in PC cluster");
            addGuiPair(gui_typeOfCellsInPcCluster_label, gui_typeOfCellsInPcCluster_omni, new JLabel(""));
            addGuiPair(new JLabel(""), gui_typeOfCellsInPcCluster_trisec, new JLabel(""));
            gui_cellRadius = new FormattedCalculatorField((new Double(getCellRadius())).doubleValue(), parent);
            gui_cellRadius.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setCellRadius(((Number)gui_cellRadius.getValue()).doubleValue());
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_cellRadius_label = new JLabel("Cell Radius", 2);
            addGuiPair(gui_cellRadius_label, gui_cellRadius, new JLabel("km"));
            gui_bsAntennaHeight = new JButton("Distribution");
            gui_bsAntennaHeight.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    Distribution d = getBsAntennaHeight();
                    boolean accept = d == null ? powerDistDialog.showDistributionDialog() : powerDistDialog.showDistributionDialog(d);
                    if(accept)
                        setBsAntennaHeight(powerDistDialog.getDistributionable());
                    gui_bsAntennaHeight_label.setText((new StringBuilder()).append("Base Station antenna height [").append(getBsAntennaHeight().toString()).append("] ").toString());
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_bsAntennaHeight_label = new JLabel((new StringBuilder()).append("Base Station antenna height [").append(getBsAntennaHeight().toString()).append("] ").toString(), 2);
            addGuiPair(gui_bsAntennaHeight_label, gui_bsAntennaHeight, new JLabel("m"));
            gui_triSectorAntenna = new JButton("Antenna");
            gui_triSectorAntenna.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    if(gui_typeOfCellsInPcCluster_omni.isSelected())
                    {
                        if(antennaEdit.show(cdmasystem.getOmniDirectionalAntenna()))
                        {
                            antennaEdit.updateModel(cdmasystem.getOmniDirectionalAntenna());
                            cdmasystem.updateOmniAntenna();
                        }
                    } else
                    if(antennaEdit.show(getTriSectorBsAntenna()))
                    {
                        antennaEdit.updateModel(getTriSectorBsAntenna());
                        cdmasystem.updateTriSectorAntenna();
                    }
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_triSectorAntenna_label = new JLabel("Base Station Antenna", 2);
            addGuiPair(gui_triSectorAntenna_label, gui_triSectorAntenna, new JLabel(""));
            gui_msAntennaHeight = new JButton("Distribution");
            gui_msAntennaHeight.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    Distribution d = getMsAntennaHeight();
                    boolean accept = d == null ? powerDistDialog.showDistributionDialog() : powerDistDialog.showDistributionDialog(d);
                    if(accept)
                        setMsAntennaHeight(powerDistDialog.getDistributionable());
                    gui_msAntennaHeight_label.setText((new StringBuilder()).append("Mobile Station Antenna Height [").append(getMsAntennaHeight().toString()).append("] ").toString());
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_msAntennaHeight_label = new JLabel((new StringBuilder()).append("Mobile Station Antenna Height [").append(getMsAntennaHeight().toString()).append("] ").toString(), 2);
            addGuiPair(gui_msAntennaHeight_label, gui_msAntennaHeight, new JLabel("m"));
            gui_msAntennaGain = new JButton("Distribution");
            gui_msAntennaGain.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    Distribution d = getMsAntennaGain();
                    boolean accept = d == null ? powerDistDialog.showDistributionDialog() : powerDistDialog.showDistributionDialog(d);
                    if(accept)
                        setMsAntennaGain(powerDistDialog.getDistributionable());
                    gui_msAntennaGain_label.setText((new StringBuilder()).append("Mobile Station Antenna Gain [").append(getMsAntennaGain().toString()).append(" ]").toString());
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_msAntennaGain_label = new JLabel((new StringBuilder()).append("Mobile Station Antenna Gain [").append(getMsAntennaGain().toString()).append(" ]").toString(), 2);
            addGuiPair(gui_msAntennaGain_label, gui_msAntennaGain, new JLabel("dB"));
            gui_msMobilityDistribution = new JButton("Distribution");
            gui_msMobilityDistribution.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    Distribution d = getMsMobilityDistribution();
                    boolean accept = d == null ? powerDistDialog.showDistributionDialog() : powerDistDialog.showDistributionDialog(d, false, new org.seamcat.presentation.DistributionDialog.DistributionCheck() {

                        public boolean validate(Distribution d)
                        {
                            if(d instanceof StairDistribution)
                            {
                                Point2D points[] = ((StairDistribution)d).getPoints();
                                if(points.length > 4 || points.length < 1)
                                    return false;
                                Point2D arr$[] = points;
                                int len$ = arr$.length;
                                for(int i$ = 0; i$ < len$; i$++)
                                {
                                    Point2D p = arr$[i$];
                                    if(p.getX() != 0.0D && p.getX() != 3D && p.getX() != 30D && p.getX() != 100D)
                                        return false;
                                }

                                return true;
                            } else
                            {
                                return false;
                            }
                        }

                        public String getErrorString()
                        {
                            return CDMAPanel.STRINGLIST.getString("CDMA_MOBILITY_WARNING");
                        }

                        final _cls9 this$2;

                        
                        {
                            this$2 = _cls9.this;
                            super();
                        }
                    }
);
                    if(accept)
                        setMsMobilityDistribution(powerDistDialog.getDistributionable());
                    gui_msMobilityDistribution_label.setText((new StringBuilder()).append("Mobile Station Mobility [").append(getMsMobilityDistribution().toString()).append("] ").toString());
                }

                final PositioningPanel this$1;

                
                {
                    this$1 = PositioningPanel.this;
                    super();
                }
            }
);
            gui_msMobilityDistribution_label = new JLabel((new StringBuilder()).append("Mobile Station Mobility [").append(getMsMobilityDistribution().toString()).append("] ").toString());
            addGuiPair(gui_msMobilityDistribution_label, gui_msMobilityDistribution, new JLabel("Km/h"));
            setLayout(new BorderLayout());
            add(posPanel, "Center");
        }

        protected void refreshFromModel()
        {
            gui_numberOfCellSitesInPcCluster.setValue(new Integer(getNumberOfCellSitesInPcCluster()));
            gui_typeOfCellsInPcCluster_omni.setSelected(isTypeOfCellsInPcClusterOmni());
            gui_typeOfCellsInPcCluster_trisec.setSelected(!isTypeOfCellsInPcClusterOmni());
            gui_cellRadius.setValue(new Double(getCellRadius()));
            gui_bsAntennaHeight_label.setText((new StringBuilder()).append("Base Station antenna height [").append(getBsAntennaHeight().toString()).append("] ").toString());
            gui_msAntennaHeight_label.setText((new StringBuilder()).append("Mobile Station Antenna Height [").append(getMsAntennaHeight().toString()).append("] ").toString());
            gui_msAntennaGain_label.setText((new StringBuilder()).append("Mobile Station Antenna Gain [").append(getMsAntennaGain().toString()).append(" ]").toString());
            gui_msMobilityDistribution_label.setText((new StringBuilder()).append("Mobile Station Mobility [").append(getMsMobilityDistribution().toString()).append("] ").toString());
        }

        protected void updateModel()
        {
            setNumberOfCellSitesInPcCluster(((Number)gui_numberOfCellSitesInPcCluster.getValue()).intValue());
            setCellRadius(((Number)gui_cellRadius.getValue()).doubleValue());
        }

        private JFormattedTextField gui_numberOfCellSitesInPcCluster;
        private JRadioButton gui_typeOfCellsInPcCluster_omni;
        private JRadioButton gui_typeOfCellsInPcCluster_trisec;
        private ButtonGroup gui_typeOfCellsInPcCluster_group;
        private JFormattedTextField gui_cellRadius;
        private JButton gui_bsAntennaHeight;
        private JButton gui_triSectorAntenna;
        private JButton gui_msAntennaHeight;
        private JButton gui_msAntennaGain;
        private JButton gui_msMobilityDistribution;
        private JLabel gui_numberOfCellSitesInPcCluster_label;
        private JLabel gui_typeOfCellsInPcCluster_label;
        private JLabel gui_cellRadius_label;
        private JLabel gui_bsAntennaHeight_label;
        private JLabel gui_omniBsAntennaGain_label;
        private JLabel gui_triSectorAntenna_label;
        private JLabel gui_msAntennaHeight_label;
        private JLabel gui_msAntennaGain_label;
        private JLabel gui_msMobilityDistribution_label;
        private JPanel posPanel;
        private DistributionDialog powerDistDialog;
        final CDMAPanel this$0;









        public PositioningPanel()
        {
            this$0 = CDMAPanel.this;
            super();
            posPanel = new JPanel();
        }
    }

    public class GeneralPanel extends InnerCDMAPanel
    {

        public void initializeGui()
        {
            setBorderName("General");
            gui_linkComponent_uplink = new JRadioButton("Uplink", isUplink());
            gui_linkComponent_uplink.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setUplink(gui_linkComponent_uplink.isSelected());
                    capacityPanel.updateSelectionStatus();
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_linkComponent_downlink = new JRadioButton("Downlink", !isUplink());
            gui_linkComponent_downlink.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setUplink(gui_linkComponent_uplink.isSelected());
                    capacityPanel.updateSelectionStatus();
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_linkComponent_group = new ButtonGroup();
            gui_linkComponent_group.add(gui_linkComponent_uplink);
            gui_linkComponent_group.add(gui_linkComponent_downlink);
            gui_linkComponent_group_label = new JLabel("CDMA Link component");
            addGuiPair(gui_linkComponent_group_label, gui_linkComponent_uplink, new JLabel(""));
            addGuiPair(new JLabel(""), gui_linkComponent_downlink, new JLabel(""));
            gui_receiverNoiseFigure = new FormattedCalculatorField((new Double(getReceiverNoiseFigure())).doubleValue(), parent);
            gui_receiverNoiseFigure.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setReceiverNoiseFigure(((Number)gui_receiverNoiseFigure.getValue()).doubleValue());
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_receiverNoiseFigure_label = new JLabel("Receiver Noise Figure", 2);
            addGuiPair(gui_receiverNoiseFigure_label, gui_receiverNoiseFigure, new JLabel("dB"));
            gui_handoverMargin = new FormattedCalculatorField((new Double(getHandoverMargin())).doubleValue(), parent);
            gui_handoverMargin.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setHandoverMargin(((Number)gui_handoverMargin.getValue()).doubleValue());
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_handoverMargin_label = new JLabel("Handover Margin", 2);
            addGuiPair(gui_handoverMargin_label, gui_handoverMargin, new JLabel("dB"));
            gui_callDropThreshold = new FormattedCalculatorField(getCallDropThreshold(), parent);
            gui_callDropThreshold.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setCallDropThreshold(((Number)gui_callDropThreshold.getValue()).doubleValue());
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_callDropThreshold_label = new JLabel("Call drop threshold", 2);
            addGuiPair(gui_callDropThreshold_label, gui_callDropThreshold, new JLabel("dB"));
            gui_voiceBitRate = new FormattedCalculatorField(getVoiceBitRate(), parent);
            gui_voiceBitRate.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setVoiceBitRate(((Number)gui_voiceBitRate.getValue()).doubleValue());
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_voiceBitRate_label = new JLabel("Voice bit rate", 2);
            addGuiPair(gui_voiceBitRate_label, gui_voiceBitRate, new JLabel("kbs"));
            gui_systemBandwith = new FormattedCalculatorField(getSystemBandwith(), parent);
            gui_systemBandwith.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setSystemBandwith(((Number)gui_systemBandwith.getValue()).doubleValue());
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_systemBandwith_label = new JLabel("Reference bandwidth", 2);
            addGuiPair(gui_systemBandwith_label, gui_systemBandwith, new JLabel("MHz"));
            gui_voiceActivityFactor = new FormattedCalculatorField(getVoiceActivityFactor(), parent);
            gui_voiceActivityFactor.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setVoiceActivityFactor(((Number)gui_voiceActivityFactor.getValue()).doubleValue());
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_voiceActivityFactor_label = new JLabel("Voice activity factor", 2);
            addGuiPair(gui_voiceActivityFactor_label, gui_voiceActivityFactor, new JLabel(""));
            gui_minimumCouplingLoss = new FormattedCalculatorField(getMinimumCouplingLoss(), parent);
            gui_minimumCouplingLoss.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setMinimumCouplingLoss(((Number)gui_minimumCouplingLoss.getValue()).doubleValue());
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            gui_minimumCouplingLoss_label = new JLabel("Minimum Coupling Loss", 2);
            addGuiPair(gui_minimumCouplingLoss_label, gui_minimumCouplingLoss, new JLabel("dB"));
            gui_linkLevelData = new JComboBox(Model.getInstance().getLibrary().getCDMALinkLevelData().createComboBoxModel());
            gui_linkLevelData.setEditable(false);
            try
            {
                gui_linkLevelData.setSelectedItem(cdmasystem.getLinkLevelData());
            }
            catch(NullPointerException ne) { }
            gui_linkLevelData.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    cdmasystem.setLinkLevelData(new CDMALinkLevelData((CDMALinkLevelData)gui_linkLevelData.getSelectedItem()));
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            JButton inspectButton = new JButton(SeamcatIcons.getImageIcon("SEAMCAT_ICON_INSPECT_LLD", 0));
            inspectButton.setMargin(new Insets(1, 1, 1, 1));
            inspectButton.setToolTipText("View or edit Link Level Data instance for this specific CDMA system");
            inspectButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    if(lldEditor.show(cdmasystem.getLinkLevelData()))
                        JOptionPane.showMessageDialog(_fld0, CDMAPanel.STRINGLIST.getString("CDMA_LLD_INSTANCE_CHANGE"), "Link Level Data Changed", 1);
                }

                final GeneralPanel this$1;

                
                {
                    this$1 = GeneralPanel.this;
                    super();
                }
            }
);
            addGuiPair(new JLabel("Link Level Data"), gui_linkLevelData, inspectButton);
        }

        protected void refreshFromModel()
        {
            gui_linkComponent_uplink.setSelected(isUplink());
            gui_linkComponent_downlink.setSelected(!isUplink());
            gui_receiverNoiseFigure.setValue(new Double(getReceiverNoiseFigure()));
            gui_handoverMargin.setValue(new Double(getHandoverMargin()));
            gui_callDropThreshold.setValue(new Double(getCallDropThreshold()));
            gui_voiceBitRate.setValue(new Double(getVoiceBitRate()));
            gui_systemBandwith.setValue(new Double(getSystemBandwith()));
            gui_voiceActivityFactor.setValue(new Double(getVoiceActivityFactor()));
            gui_minimumCouplingLoss.setValue(new Double(getMinimumCouplingLoss()));
            if(cdmasystem.getLinkLevelData() != null)
            {
                CDMALinkLevelData lld = cdmasystem.getLinkLevelData();
                gui_linkLevelData.getModel().setSelectedItem(lld);
            } else
            {
                gui_linkLevelData.setSelectedIndex(0);
            }
        }

        protected void updateModel()
        {
            if(initialized)
            {
                setUplink(gui_linkComponent_uplink.isSelected());
                setReceiverNoiseFigure(((Number)gui_receiverNoiseFigure.getValue()).doubleValue());
                setHandoverMargin(((Number)gui_handoverMargin.getValue()).doubleValue());
                setCallDropThreshold(((Number)gui_callDropThreshold.getValue()).doubleValue());
                setVoiceBitRate(((Number)gui_voiceBitRate.getValue()).doubleValue());
                setSystemBandwith(((Number)gui_systemBandwith.getValue()).doubleValue());
                setVoiceActivityFactor(((Number)gui_voiceActivityFactor.getValue()).doubleValue());
                setMinimumCouplingLoss(((Number)gui_minimumCouplingLoss.getValue()).doubleValue());
            }
        }

        protected JRadioButton gui_linkComponent_uplink;
        protected JRadioButton gui_linkComponent_downlink;
        private ButtonGroup gui_linkComponent_group;
        private JFormattedTextField gui_receiverNoiseFigure;
        private JFormattedTextField gui_handoverMargin;
        private JFormattedTextField gui_callDropThreshold;
        private JFormattedTextField gui_voiceBitRate;
        private JFormattedTextField gui_systemBandwith;
        private JFormattedTextField gui_voiceActivityFactor;
        private JFormattedTextField gui_minimumCouplingLoss;
        private JComboBox gui_linkLevelData;
        private JLabel gui_linkComponent_group_label;
        private JLabel gui_receiverNoiseFigure_label;
        private JLabel gui_handoverMargin_label;
        private JLabel gui_callDropThreshold_label;
        private JLabel gui_voiceBitRate_label;
        private JLabel gui_systemBandwith_label;
        private JLabel gui_voiceActivityFactor_label;
        private JLabel gui_minimumCouplingLoss_label;
        final CDMAPanel this$0;









        public GeneralPanel()
        {
            this$0 = CDMAPanel.this;
            super();
        }
    }

    public class CapacityPanel extends InnerCDMAPanel
    {

        protected void initializeGui()
        {
            setBorderName("Non interfered capacity parameters");
            usersPerCell = new JFormattedTextField(new Integer(getUsersPerCell()));
            usersPerCell.setHorizontalAlignment(4);
            usersPerCell.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            usersPerCell.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setUsersPerCell(((Number)usersPerCell.getValue()).intValue());
                }

                final CapacityPanel this$1;

                
                {
                    this$1 = CapacityPanel.this;
                    super();
                }
            }
);
            usersPerCell_label = new JLabel("Users per cell", 2);
            addGuiPair(usersPerCell_label, usersPerCell, new JLabel("users"));
            deltaUsersPerCell = new JFormattedTextField(new Integer(getDeltaUsersPerCell()));
            deltaUsersPerCell.setHorizontalAlignment(4);
            deltaUsersPerCell.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            deltaUsersPerCell.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setDeltaUsersPerCell(((Number)deltaUsersPerCell.getValue()).intValue());
                }

                final CapacityPanel this$1;

                
                {
                    this$1 = CapacityPanel.this;
                    super();
                }
            }
);
            deltaUsersPerCell_label = new JLabel("Delta users per cell", 2);
            addGuiPair(deltaUsersPerCell_label, deltaUsersPerCell, new JLabel("users"));
            numberOfTrials = new JFormattedTextField(new Integer(getNumberOfTrials()));
            numberOfTrials.setHorizontalAlignment(4);
            numberOfTrials.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            numberOfTrials.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setNumberOfTrials(((Number)numberOfTrials.getValue()).intValue());
                }

                final CapacityPanel this$1;

                
                {
                    this$1 = CapacityPanel.this;
                    super();
                }
            }
);
            numberOfTrials_label = new JLabel("Number of trials", 2);
            addGuiPair(numberOfTrials_label, numberOfTrials, new JLabel(""));
            allowableInitialOutage = new FormattedCalculatorField(getAllowableInitialOutage() * 100D, parent);
            allowableInitialOutage.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setAllowableInitialOutage(((Number)allowableInitialOutage.getValue()).doubleValue() / 100D);
                }

                final CapacityPanel this$1;

                
                {
                    this$1 = CapacityPanel.this;
                    super();
                }
            }
);
            allowableInitialOutage_label = new JLabel("Allowed tolerance of initial outage");
            addGuiPair(allowableInitialOutage_label, allowableInitialOutage, new JLabel("%"));
            targetNoiseRisePrecision = new FormattedCalculatorField(getAllowableInitialOutage(), parent);
            targetNoiseRisePrecision.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    setAllowableInitialOutage(((Number)targetNoiseRisePrecision.getValue()).doubleValue());
                }

                final CapacityPanel this$1;

                
                {
                    this$1 = CapacityPanel.this;
                    super();
                }
            }
);
            targetNoiseRisePrecision_label = new JLabel("Target noise rise precision");
            addGuiPair(targetNoiseRisePrecision_label, targetNoiseRisePrecision, new JLabel("dB"));
            simulateCapacity = new JCheckBox("Simulate Non Interfered Capacity");
            simulateCapacity.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0)
                {
                    updateSelectionStatus();
                }

                final CapacityPanel this$1;

                
                {
                    this$1 = CapacityPanel.this;
                    super();
                }
            }
);
            addGuiPair(new JLabel(""), simulateCapacity, new JLabel(""));
            refreshFromModel();
        }

        protected void updateSelectionStatus()
        {
            deltaUsersPerCell.setEnabled(simulateCapacity.isSelected());
            deltaUsersPerCell_label.setEnabled(simulateCapacity.isSelected());
            numberOfTrials.setEnabled(simulateCapacity.isSelected());
            numberOfTrials_label.setEnabled(simulateCapacity.isSelected());
            allowableInitialOutage.setEnabled(simulateCapacity.isSelected() && generalPanel.gui_linkComponent_downlink.isSelected());
            allowableInitialOutage_label.setEnabled(simulateCapacity.isSelected() && generalPanel.gui_linkComponent_downlink.isSelected());
            targetNoiseRisePrecision.setEnabled(simulateCapacity.isSelected() && generalPanel.gui_linkComponent_uplink.isSelected());
            targetNoiseRisePrecision_label.setEnabled(simulateCapacity.isSelected() && generalPanel.gui_linkComponent_uplink.isSelected());
        }

        protected void refreshFromModel()
        {
            usersPerCell.setValue(new Integer(getUsersPerCell()));
            deltaUsersPerCell.setValue(new Integer(getDeltaUsersPerCell()));
            numberOfTrials.setValue(new Integer(getNumberOfTrials()));
            allowableInitialOutage.setValue(Double.valueOf(getAllowableInitialOutage() * 100D));
            targetNoiseRisePrecision.setValue(Double.valueOf(getAllowableInitialOutage()));
            simulateCapacity.setSelected(cdmasystem.isSimulateCapacity());
            deltaUsersPerCell.setEnabled(simulateCapacity.isSelected());
            deltaUsersPerCell_label.setEnabled(simulateCapacity.isSelected());
            numberOfTrials.setEnabled(simulateCapacity.isSelected());
            numberOfTrials_label.setEnabled(simulateCapacity.isSelected());
            allowableInitialOutage.setEnabled(simulateCapacity.isSelected() && generalPanel.gui_linkComponent_downlink.isSelected());
            allowableInitialOutage_label.setEnabled(simulateCapacity.isSelected() && generalPanel.gui_linkComponent_downlink.isSelected());
            targetNoiseRisePrecision.setEnabled(simulateCapacity.isSelected() && generalPanel.gui_linkComponent_uplink.isSelected());
            targetNoiseRisePrecision_label.setEnabled(simulateCapacity.isSelected() && generalPanel.gui_linkComponent_uplink.isSelected());
        }

        protected void updateModel()
        {
            if(initialized)
            {
                setUsersPerCell(((Number)usersPerCell.getValue()).intValue());
                setDeltaUsersPerCell(((Number)deltaUsersPerCell.getValue()).intValue());
                setNumberOfTrials(((Number)numberOfTrials.getValue()).intValue());
                if(generalPanel.gui_linkComponent_uplink.isSelected())
                    setAllowableInitialOutage(((Number)targetNoiseRisePrecision.getValue()).doubleValue());
                else
                    setAllowableInitialOutage(((Number)allowableInitialOutage.getValue()).doubleValue() / 100D);
                cdmasystem.setSimulateCapacity(simulateCapacity.isSelected());
            }
        }

        private JFormattedTextField usersPerCell;
        private JFormattedTextField deltaUsersPerCell;
        private JFormattedTextField numberOfTrials;
        private JFormattedTextField allowableInitialOutage;
        private JFormattedTextField targetNoiseRisePrecision;
        private JCheckBox simulateCapacity;
        private JLabel usersPerCell_label;
        private JLabel deltaUsersPerCell_label;
        private JLabel numberOfTrials_label;
        private JLabel allowableInitialOutage_label;
        private JLabel targetNoiseRisePrecision_label;
        final CDMAPanel this$0;






        public CapacityPanel()
        {
            this$0 = CDMAPanel.this;
            super();
        }
    }

    public abstract class InnerCDMAPanel extends JPanel
    {

        public void initialize()
        {
            container.setLayout(new LabeledPairLayoutWithUnit());
            initializeGui();
            border = new TitledBorder(getBorderName());
            setBorder(border);
        }

        protected abstract void initializeGui();

        protected abstract void refreshFromModel();

        protected abstract void updateModel();

        public void enableGuiElements()
        {
            for(int i = 0; i < guiElements.size(); i++)
                ((JComponent)guiElements.get(i)).setEnabled(true);

            setEnabled(true);
        }

        public void disableGuiEleements()
        {
            for(int i = 0; i < guiElements.size(); i++)
                ((JComponent)guiElements.get(i)).setEnabled(false);

            setEnabled(false);
        }

        protected void addGuiPair(JLabel label, JComponent comp, JComponent unit)
        {
            if(label != null)
            {
                container.add(label, "label");
                guiElements.add(label);
            }
            if(comp != null)
            {
                container.add(comp, "field");
                guiElements.add(comp);
            }
            if(unit != null)
            {
                container.add(unit, "unit");
                guiElements.add(unit);
            }
        }

        public String getBorderName()
        {
            return borderName;
        }

        public void setBorderName(String _borderName)
        {
            borderName = _borderName;
        }

        protected LayoutManager grid;
        protected GridBagConstraints gridcon;
        protected String borderName;
        protected Vector guiElements;
        protected Container container;
        protected int numberOfRows;
        protected TitledBorder border;
        final CDMAPanel this$0;

        public InnerCDMAPanel()
        {
            this$0 = CDMAPanel.this;
            super();
            grid = new LabeledPairLayoutWithUnit();
            gridcon = new GridBagConstraints();
            borderName = "";
            guiElements = new Vector();
            container = this;
            numberOfRows = 10;
        }
    }


    public CDMAPanel(JDialog parent)
    {
        super(3);
        initialized = false;
        this.parent = parent;
        generalPanel = new GeneralPanel();
        positioningPanel = new PositioningPanel();
        uplinkModelPanel = new UplinkModelPanel();
        downlinkModelPanel = new DownlinkModelPanel();
        capacityPanel = new CapacityPanel();
        referencePanel = new ReferenceCellSelectionPanel();
        propagationModelSelector = new PropagationSelectPanel(parent);
        antennaEdit = new AntennaAddEditComponent(parent);
        lldEditor = new CDMALinkLevelDataEditorDialog(parent);
        JPanel mainTab = new JPanel(new BorderLayout());
        JPanel linkModelRow = new JPanel(new GridLayout(1, 2));
        linkModelRow.add(uplinkModelPanel);
        linkModelRow.add(downlinkModelPanel);
        mainTab.add(generalPanel, "Center");
        mainTab.add(linkModelRow, "South");
        addTab(STRINGLIST.getString("CDMA_TAB_GENERAL"), mainTab);
        addTab(STRINGLIST.getString("CDMA_TAB_POSITIONING"), positioningPanel);
        addTab(STRINGLIST.getString("CDMA_TAB_CAPACITY"), capacityPanel);
        addTab(STRINGLIST.getString("CDMA_TAB_REFERENCE"), referencePanel);
        addTab(STRINGLIST.getString("CDMA_TAB_PROPAGATIONMODEL"), new JScrollPane(propagationModelSelector));
    }

    public void updateModel()
    {
        if(initialized)
        {
            generalPanel.updateModel();
            positioningPanel.updateModel();
            uplinkModelPanel.updateModel();
            downlinkModelPanel.updateModel();
            capacityPanel.updateModel();
            setPropagationModel(propagationModelSelector.getPropagationModel());
        }
    }

    public void refreshFromModel()
    {
        if(!initialized)
        {
            generalPanel.initialize();
            positioningPanel.initialize();
            uplinkModelPanel.initialize();
            downlinkModelPanel.initialize();
            capacityPanel.initialize();
            propagationModelSelector.setPropagationModel(getPropagationModel());
            initialized = true;
        } else
        {
            generalPanel.refreshFromModel();
            positioningPanel.refreshFromModel();
            uplinkModelPanel.refreshFromModel();
            downlinkModelPanel.refreshFromModel();
            capacityPanel.refreshFromModel();
            propagationModelSelector.setPropagationModel(getPropagationModel());
        }
        referencePanel.setCdmaSystem(getCdmasystem());
        if(cdmasystem.getCDMALinkComponent() == org.seamcat.cdma.CDMASystem.LinkDirection.Downlink)
            enableDownlinkModel();
        else
            enableUplinkModel();
    }

    public void enableUplinkModel()
    {
        downlinkModelPanel.disableGuiEleements();
        uplinkModelPanel.enableGuiElements();
    }

    public void enableDownlinkModel()
    {
        downlinkModelPanel.enableGuiElements();
        uplinkModelPanel.disableGuiEleements();
    }

    public void setCdmasystem(CDMASystem cdmasystem)
    {
        this.cdmasystem = cdmasystem;
        refreshFromModel();
    }

    public CDMASystem getCdmasystem()
    {
        return cdmasystem;
    }

    public PropagationModel getPropagationModel()
    {
        return cdmasystem.getPropagationModel();
    }

    public Distribution getMsAntennaHeight()
    {
        return cdmasystem.getMobileStationAntennaHeight();
    }

    public double getCallDropThreshold()
    {
        return cdmasystem.getCallDropThreshold();
    }

    public double getMsMaximumTransitPower()
    {
        return cdmasystem.getMobileStationMaximumTransmitPower();
    }

    public boolean isTypeOfCellsInPcClusterOmni()
    {
        return cdmasystem.getTypeOfCellsInPowerControlCluster() == org.seamcat.cdma.CDMASystem.CellType.OmniDirectionalAntenna;
    }

    public double getMsPowerControlRange()
    {
        return cdmasystem.getMobileStationPowerControlRange();
    }

    public double getMsPowerControlStep()
    {
        return cdmasystem.getMobileStationPowerControlStep();
    }

    public double getBsPilotFraction()
    {
        return cdmasystem.getBaseStationPilotFraction();
    }

    public double getSystemBandwith()
    {
        return cdmasystem.getSystemBandwidth();
    }

    public double getTargetOutagePercentage()
    {
        return cdmasystem.getTargetOutagePercentage();
    }

    public void setTargetOutagePercentage(double stop)
    {
        cdmasystem.setTargetOutagePercentage(stop);
    }

    public boolean isUplink()
    {
        try
        {
            return cdmasystem.getCDMALinkComponent().isUplink();
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    public double getPcConvergensThreshold()
    {
        return cdmasystem.getPowerControlConvergenceThreshold();
    }

    public double getBsMaximumBroadcastPower()
    {
        return cdmasystem.getBaseStationMaximumBroadcastPowerdBm();
    }

    public double getBsOverheadFraction()
    {
        return cdmasystem.getBaseStationOverheadFraction();
    }

    public double getReceiverNoiseFigure()
    {
        return cdmasystem.getReceiverNoiseFigure();
    }

    public int getNumberOfCellSitesInPcCluster()
    {
        return cdmasystem.getNumberOfCellSitesInPowerControlCluster();
    }

    public double getTargetNetworkNoiseRise()
    {
        return cdmasystem.getTargetNetworkNoiseRiseOverThermalNoise_dB();
    }

    public double getCellRadius()
    {
        return cdmasystem.getCellRadius();
    }

    public double getHandoverMargin()
    {
        return cdmasystem.getHandoverMargin();
    }

    public Distribution getBsAntennaHeight()
    {
        return cdmasystem.getBaseStationAntennaHeight();
    }

    public Distribution getMsAntennaGain()
    {
        return cdmasystem.getMobileStationAntennaGain();
    }

    public double getVoiceBitRate()
    {
        return cdmasystem.getVoiceBitRate();
    }

    public CDMALinkLevelData getLinkLevelData()
    {
        return cdmasystem.getLinkLevelData();
    }

    public double getVoiceActivityFactor()
    {
        return cdmasystem.getVoiceActivityFactor();
    }

    public Antenna getTriSectorBsAntenna()
    {
        return cdmasystem.getTriSectorAntenna();
    }

    public void setBsMaximumTrafficChannelFraction(double bsMaxFract)
    {
        cdmasystem.setBaseStationMaxChannelPowerFraction(bsMaxFract);
    }

    public void setPropagationModel(PropagationModel propagationModel)
    {
        cdmasystem.setPropagationModel(propagationModel);
    }

    public void setMsAntennaHeight(Distribution msAntennaHeight)
    {
        cdmasystem.setMobileStationAntennaHeight(msAntennaHeight);
    }

    public void setCallDropThreshold(double callDropThreshold)
    {
        cdmasystem.setCallDropThreshold(callDropThreshold);
    }

    public void setMsMaximumTransitPower(double msMaximumTransitPower)
    {
        cdmasystem.setMobileStationMaximumTransmitPower(msMaximumTransitPower);
    }

    public void setTypeOfCellsInPcClusterOmni(boolean typeOfCellsInPcClusterOmni)
    {
        cdmasystem.setTypeOfCellsInPowerControlCluster(typeOfCellsInPcClusterOmni ? org.seamcat.cdma.CDMASystem.CellType.OmniDirectionalAntenna : org.seamcat.cdma.CDMASystem.CellType.TriSectorAntenna);
    }

    public void setMsPowerControlRange(double msPowerControlRange)
    {
        cdmasystem.setMobileStationPowerControlRange(msPowerControlRange);
    }

    public void setMsPowerControlStep(double msPowerControlStep)
    {
        cdmasystem.setMobileStationPowerControlStep(msPowerControlStep);
    }

    public void setBsPilotFraction(double bsPilotFraction)
    {
        cdmasystem.setBaseStationPilotFraction(bsPilotFraction);
    }

    public void setSystemBandwith(double systemBandwith)
    {
        cdmasystem.setSystemBandwidth(systemBandwith);
    }

    public void setUplink(boolean uplink)
    {
        cdmasystem.setCDMALinkComponent(uplink ? org.seamcat.cdma.CDMASystem.LinkDirection.Uplink : org.seamcat.cdma.CDMASystem.LinkDirection.Downlink);
        cdmasystem = CDMASystem.createCDMASystem(cdmasystem);
        if(uplink)
            enableUplinkModel();
        else
            enableDownlinkModel();
    }

    public void setPcConvergensThreshold(double pcConvergensThreshold)
    {
        cdmasystem.setPowerControlConvergenceThreshold(pcConvergensThreshold);
    }

    public void setBsMaximumBroadcastPower(double bsMaximumBroadcastPower)
    {
        cdmasystem.setBaseStationMaximumBroadcastPower(bsMaximumBroadcastPower);
    }

    public void setBsOverheadFraction(double bsOverheadFraction)
    {
        cdmasystem.setBaseStationOverheadFraction(bsOverheadFraction);
    }

    public void setReceiverNoiseFigure(double receiverNoiseFigure)
    {
        cdmasystem.setReceiverNoiseFigure(receiverNoiseFigure);
    }

    public void setNumberOfCellSitesInPcCluster(int numberOfCellSitesInPcCluster)
    {
        cdmasystem.setNumberOfCellSitesInPowerControlCluster(numberOfCellSitesInPcCluster);
    }

    public void setTargetNetworkNoiseRise(double targetNetworkNoiseRise)
    {
        cdmasystem.setTargetNetworkNoiseRiseOverThermalNoise(targetNetworkNoiseRise);
    }

    public void setCellRadius(double cellRadius)
    {
        cdmasystem.setCellRadius(cellRadius);
    }

    public void setHandoverMargin(double handoverMargin)
    {
        cdmasystem.setHandoverMargin(handoverMargin);
    }

    public void setBsAntennaHeight(Distribution bsAntennaHeight)
    {
        cdmasystem.setBaseStationAntennaHeight(bsAntennaHeight);
    }

    public void setMsAntennaGain(Distribution msAntennaGain)
    {
        cdmasystem.setMobileStationAntennaGain(msAntennaGain);
    }

    public void setVoiceBitRate(double voiceBitRate)
    {
        cdmasystem.setVoiceBitRate(voiceBitRate);
    }

    public void setVoiceActivityFactor(double voiceActivityFactor)
    {
        cdmasystem.setVoiceActivityFactor(voiceActivityFactor);
    }

    public void setTriSectorBsAntennaGain(double d)
    {
    }

    public void setMsMobilityDistribution(Distribution msMobilityDistribution)
    {
        cdmasystem.setMobileStationMobilityDistribution(msMobilityDistribution);
    }

    public double getMaximumTrafficChannelFraction()
    {
        return cdmasystem.getBaseStationMaxChannelPowerFraction();
    }

    public Distribution getMsMobilityDistribution()
    {
        return cdmasystem.getMobileStationMobilityDistribution();
    }

    public void setMinimumCouplingLoss(double mcl)
    {
        cdmasystem.setMinimumCouplingLoss(mcl);
    }

    public double getMinimumCouplingLoss()
    {
        return cdmasystem.getMinimumCouplingLoss();
    }

    public int getUsersPerCell()
    {
        return cdmasystem.getUsersPerCell();
    }

    public void setUsersPerCell(int _users)
    {
        cdmasystem.setUsersPerCell(_users);
    }

    public int getDeltaUsersPerCell()
    {
        return cdmasystem.getDeltaN();
    }

    public void setDeltaUsersPerCell(int _users)
    {
        cdmasystem.setDeltaN(_users);
    }

    public int getNumberOfTrials()
    {
        return cdmasystem.getNumberOfTrials();
    }

    public void setNumberOfTrials(int _trials)
    {
        cdmasystem.setNumberOfTrials(_trials);
    }

    public void setAllowableInitialOutage(double _value)
    {
        cdmasystem.setSystemTolerance(_value);
    }

    public double getAllowableInitialOutage()
    {
        return cdmasystem.getSystemTolerance();
    }

    public void setSuccessThreshold(double value)
    {
        cdmasystem.setSuccessThreshold(value);
    }

    public double getSuccessThreshold()
    {
        return cdmasystem.getSuccessThreshold();
    }

    private static final ResourceBundle STRINGLIST;
    private CDMASystem cdmasystem;
    private GeneralPanel generalPanel;
    private InnerCDMAPanel positioningPanel;
    private InnerCDMAPanel uplinkModelPanel;
    private InnerCDMAPanel downlinkModelPanel;
    private CapacityPanel capacityPanel;
    private ReferenceCellSelectionPanel referencePanel;
    private JDialog parent;
    private boolean initialized;
    private PropagationSelectPanel propagationModelSelector;
    private AntennaAddEditComponent antennaEdit;
    private CDMALinkLevelDataEditorDialog lldEditor;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }








}