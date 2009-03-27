// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   RelativeLocationInterferingPanel.java

package org.seamcat.presentation.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.distribution.Distribution;
import org.seamcat.model.*;
import org.seamcat.model.core.*;
import org.seamcat.presentation.*;

public class RelativeLocationInterferingPanel extends JPanel
{
    private static class LocationModeComboBoxModel extends DefaultComboBoxModel
    {

        public Object getElementAt(int index)
        {
            return values[index];
        }

        public int getIndexOf(Object anObject)
        {
            for(int i = 0; i < values.length; i++)
                if(values[i].equals(anObject))
                    return i;

            return 0;
        }

        public int getSize()
        {
            return values.length;
        }

        public String[] getValues()
        {
            return values;
        }

        public void setValues(String values[])
        {
            this.values = values;
        }

        private String values[];

        private LocationModeComboBoxModel()
        {
            values = InterferenceLink.RELATIVE_LOCATION_MODES;
        }

    }


    public RelativeLocationInterferingPanel(JDialog parent)
    {
        super(new LabeledPairLayout());
        cmbModel = new LocationModeComboBoxModel();
        modeSelector = new JComboBox(cmbModel);
        azimuthButton = new JButton(STRINGS.getString("BTN_CAPTION_DISTRIBUTION"));
        distFactorButton = new JButton(STRINGS.getString("BTN_CAPTION_DISTRIBUTION"));
        activeTransmittersField = new JFormattedTextField(SeamcatTextFieldFormats.getIntegerFactory());
        colocation = new JCheckBox(STRINGS.getString("RELATIVE_INTERFERING_COLOCATION"));
        colocationLinks = new JComboBox();
        modeLabel = new JLabel(STRINGS.getString("RELATIVE_INTERFERING_MODE"));
        deltaXLabel = new JLabel(STRINGS.getString("RELATIVE_INTERFERING_DELTA_X"));
        deltaYLabel = new JLabel(STRINGS.getString("RELATIVE_INTERFERING_DELTA_Y"));
        azimuthLabel = new JLabel(STRINGS.getString("RELATIVE_INTERFERING_PATH_AZIMUTH"));
        distFactorLabel = new JLabel(STRINGS.getString("RELATIVE_INTERFERING_PATH_DISTANCE"));
        simulationLabel = new JLabel(STRINGS.getString("RELATIVE_INTERFERING_SIMULATION_RADIUS"));
        activeTransmittersLabel = new JLabel(STRINGS.getString("RELATIVE_INTERFERING_NUMBER_ACTIVE_TX"));
        cdmaMode = false;
        cmbModel.setValues(InterferenceLink.RELATIVE_LOCATION_MODES);
        powerDistDialog = new DistributionDialog(parent, true);
        deltaXField = new FormattedCalculatorField(parent);
        deltaYField = new FormattedCalculatorField(parent);
        simulationRadiusField = new FormattedCalculatorField(parent);
        activeTransmittersField.setColumns(15);
        activeTransmittersField.setHorizontalAlignment(4);
        modeSelector.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                setElementStatus(modeSelector.getSelectedIndex());
            }

            final RelativeLocationInterferingPanel this$0;

            
            {
                this$0 = RelativeLocationInterferingPanel.this;
                super();
            }
        }
);
        azimuthButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                Distribution d = il.getWt2VrPath().getPathAzimuth();
                boolean accept = d == null ? powerDistDialog.showDistributionDialog(RelativeLocationInterferingPanel.STRINGS.getString("RELATIVE_INTERFERING_PATH_AZIMUTH_TITLE")) : powerDistDialog.showDistributionDialog(d, RelativeLocationInterferingPanel.STRINGS.getString("RELATIVE_INTERFERING_PATH_AZIMUTH_TITLE"));
                if(accept)
                    il.getWt2VrPath().setPathAzimuth(powerDistDialog.getDistributionable());
                azimuthLabel.setText((new StringBuilder()).append(RelativeLocationInterferingPanel.STRINGS.getString("RELATIVE_INTERFERING_PATH_AZIMUTH")).append(" [").append(il.getWt2VrPath().getPathAzimuth().toString()).append("] ").toString());
            }

            final RelativeLocationInterferingPanel this$0;

            
            {
                this$0 = RelativeLocationInterferingPanel.this;
                super();
            }
        }
);
        distFactorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                Distribution d = il.getWt2VrPath().getPathDistanceFactor();
                boolean accept = d == null ? powerDistDialog.showDistributionDialog(RelativeLocationInterferingPanel.STRINGS.getString("RELATIVE_INTERFERING_PATH_DISTANCE_TITLE")) : powerDistDialog.showDistributionDialog(d, RelativeLocationInterferingPanel.STRINGS.getString("RELATIVE_INTERFERING_PATH_DISTANCE_TITLE"));
                if(accept)
                    il.getWt2VrPath().setPathDistanceFactor(powerDistDialog.getDistributionable());
                distFactorLabel.setText((new StringBuilder()).append(RelativeLocationInterferingPanel.STRINGS.getString("RELATIVE_INTERFERING_PATH_DISTANCE")).append(" [").append(il.getWt2VrPath().getPathDistanceFactor().toString()).append("] ").toString());
            }

            final RelativeLocationInterferingPanel this$0;

            
            {
                this$0 = RelativeLocationInterferingPanel.this;
                super();
            }
        }
);
        colocation.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                modeLabel.setEnabled(!colocation.isSelected());
                modeSelector.setEnabled(!colocation.isSelected());
                if(colocation.isSelected())
                {
                    deltaXLabel.setEnabled(false);
                    deltaXField.setEnabled(false);
                    deltaYLabel.setEnabled(false);
                    deltaYField.setEnabled(false);
                    azimuthLabel.setEnabled(false);
                    azimuthButton.setEnabled(false);
                    distFactorLabel.setEnabled(false);
                    distFactorButton.setEnabled(false);
                    simulationLabel.setEnabled(false);
                    simulationRadiusField.setEnabled(false);
                    activeTransmittersLabel.setEnabled(false);
                    activeTransmittersField.setEnabled(false);
                } else
                {
                    setElementStatus(modeSelector.getSelectedIndex());
                }
                colocationLinks.setEnabled(colocation.isSelected());
            }

            final RelativeLocationInterferingPanel this$0;

            
            {
                this$0 = RelativeLocationInterferingPanel.this;
                super();
            }
        }
);
        add(modeLabel, "label");
        add(modeSelector, "field");
        add(deltaXLabel, "label");
        add(deltaXField, "field");
        add(deltaYLabel, "label");
        add(deltaYField, "field");
        add(azimuthLabel, "label");
        add(azimuthButton, "field");
        add(distFactorLabel, "label");
        add(distFactorButton, "field");
        add(simulationLabel, "label");
        add(simulationRadiusField, "field");
        add(activeTransmittersLabel, "label");
        add(activeTransmittersField, "field");
        add(colocation, "label");
        add(colocationLinks, "field");
        setElementStatus(0);
        setBorder(new TitledBorder(""));
    }

    public void setInterferenceLink(InterferenceLink il, boolean canBeCoLocated, Workspace workspace)
    {
        this.il = il;
        colocation.setEnabled(canBeCoLocated);
        colocationLinks.setEnabled(canBeCoLocated);
        if(canBeCoLocated)
        {
            Vector links = new Vector();
            Components intLinks = workspace.getInterferenceLinks();
            for(int i = 0; i < intLinks.size(); i++)
            {
                InterferenceLink ilk = (InterferenceLink)intLinks.get(i);
                if(ilk != il)
                    links.add(ilk);
            }

            colocationLinks.setModel(new DefaultComboBoxModel(links));
            if(il.getColocationLink() == null)
                colocationLinks.setSelectedIndex(0);
            else
                colocationLinks.setSelectedItem(il.getColocationLink());
        } else
        {
            colocationLinks.setSelectedItem(null);
        }
        if(il.isCDMASystem())
        {
            cdmaMode = true;
            cmbModel.setValues(CDMA_MODES);
            if(il.getCorrelationMode() == 4)
                modeSelector.setSelectedIndex(0);
            else
                modeSelector.setSelectedIndex(1);
        } else
        {
            cdmaMode = false;
            cmbModel.setValues(InterferenceLink.RELATIVE_LOCATION_MODES);
            modeSelector.setSelectedIndex(il.getCorrelationMode());
        }
        colocation.setSelected(il.isColocated());
        if(il.isColocated())
        {
            modeLabel.setEnabled(!colocation.isSelected());
            modeSelector.setEnabled(!colocation.isSelected());
            deltaXLabel.setEnabled(!colocation.isSelected());
            deltaXField.setEnabled(!colocation.isSelected());
            deltaYLabel.setEnabled(!colocation.isSelected());
            deltaYField.setEnabled(!colocation.isSelected());
            azimuthLabel.setEnabled(!colocation.isSelected());
            azimuthButton.setEnabled(!colocation.isSelected());
            distFactorLabel.setEnabled(!colocation.isSelected());
            distFactorButton.setEnabled(!colocation.isSelected());
            simulationLabel.setEnabled(!colocation.isSelected());
            simulationRadiusField.setEnabled(!colocation.isSelected());
            activeTransmittersLabel.setEnabled(!colocation.isSelected());
            activeTransmittersField.setEnabled(!colocation.isSelected());
            colocationLinks.setEnabled(colocation.isSelected());
        } else
        {
            modeLabel.setEnabled(true);
            modeSelector.setEnabled(true);
            colocationLinks.setEnabled(false);
            setDeltaX(il.getWt2VrPath().getDeltaX());
            setDeltaY(il.getWt2VrPath().getDeltaY());
            simulationRadiusField.setValue(new Double(il.getInterferingLink().getInterferingTransmitter().getRsimu()));
            activeTransmittersField.setValue(new Integer(il.getInterferingLink().getInterferingTransmitter().getNbActiveTx()));
            setElementStatus(modeSelector.getSelectedIndex());
        }
        azimuthLabel.setText((new StringBuilder()).append(STRINGS.getString("RELATIVE_INTERFERING_PATH_AZIMUTH")).append(" [").append(il.getWt2VrPath().getPathAzimuth().toString()).append("] ").toString());
        distFactorLabel.setText((new StringBuilder()).append(STRINGS.getString("RELATIVE_INTERFERING_PATH_DISTANCE")).append(" [").append(il.getWt2VrPath().getPathDistanceFactor().toString()).append("] ").toString());
    }

    public void setCdmaMode(boolean value)
    {
        if(value)
        {
            cdmaMode = true;
            cmbModel.setValues(CDMA_MODES);
            if(il.getCorrelationMode() == 4)
                modeSelector.setSelectedIndex(0);
            else
                modeSelector.setSelectedIndex(1);
        } else
        {
            cdmaMode = false;
            cmbModel.setValues(InterferenceLink.RELATIVE_LOCATION_MODES);
            modeSelector.setSelectedIndex(il.getCorrelationMode());
        }
    }

    public void setInterferersDensityPanel(JPanel _panel)
    {
        idpanel = _panel;
        setElementStatus(0);
    }

    public void updateModel(InterferenceLink il)
    {
        if(cdmaMode)
        {
            if(modeSelector.getSelectedIndex() == 0)
                il.setCorrelationMode(4);
            else
                il.setCorrelationMode(0);
        } else
        {
            il.setCorrelationMode(modeSelector.getSelectedIndex());
        }
        il.getWt2VrPath().setUseCorrelatedDistance(MODE_ENABLE_STATUS[modeSelector.getSelectedIndex()][0]);
        il.getWt2VrPath().setDeltaX(((Number)deltaXField.getValue()).doubleValue());
        il.getWt2VrPath().setDeltaY(((Number)deltaYField.getValue()).doubleValue());
        if(activeTransmittersField.isEnabled())
            il.getInterferingLink().getInterferingTransmitter().setNbActiveTx(((Number)activeTransmittersField.getValue()).intValue());
        else
            il.getInterferingLink().getInterferingTransmitter().setNbActiveTx(1);
        il.getInterferingLink().getInterferingTransmitter().setRsimu(((Number)simulationRadiusField.getValue()).doubleValue());
        if(colocation.isSelected())
        {
            il.setColocated(true);
            il.setColocationLink((InterferenceLink)colocationLinks.getSelectedItem());
        } else
        {
            il.setColocated(false);
        }
    }

    private void setElementStatus(int index)
    {
        if(cdmaMode)
        {
            deltaXField.setEnabled(CDMA_MODE_ENABLE_STATUS[index][0]);
            deltaYField.setEnabled(CDMA_MODE_ENABLE_STATUS[index][1]);
            azimuthButton.setEnabled(CDMA_MODE_ENABLE_STATUS[index][2]);
            distFactorButton.setEnabled(CDMA_MODE_ENABLE_STATUS[index][3]);
            simulationRadiusField.setEnabled(CDMA_MODE_ENABLE_STATUS[index][4]);
            activeTransmittersField.setEnabled(CDMA_MODE_ENABLE_STATUS[index][5]);
            deltaXLabel.setEnabled(CDMA_MODE_ENABLE_STATUS[index][0]);
            deltaYLabel.setEnabled(CDMA_MODE_ENABLE_STATUS[index][1]);
            azimuthLabel.setEnabled(CDMA_MODE_ENABLE_STATUS[index][2]);
            distFactorLabel.setEnabled(CDMA_MODE_ENABLE_STATUS[index][3]);
            simulationLabel.setEnabled(CDMA_MODE_ENABLE_STATUS[index][4]);
            activeTransmittersLabel.setEnabled(CDMA_MODE_ENABLE_STATUS[index][5]);
            try
            {
                idpanel.setVisible(CDMA_MODE_ENABLE_STATUS[index][6]);
            }
            catch(Exception ex) { }
        } else
        {
            deltaXField.setEnabled(MODE_ENABLE_STATUS[index][0]);
            deltaYField.setEnabled(MODE_ENABLE_STATUS[index][1]);
            azimuthButton.setEnabled(MODE_ENABLE_STATUS[index][2]);
            distFactorButton.setEnabled(MODE_ENABLE_STATUS[index][3]);
            simulationRadiusField.setEnabled(MODE_ENABLE_STATUS[index][4]);
            activeTransmittersField.setEnabled(MODE_ENABLE_STATUS[index][5]);
            deltaXLabel.setEnabled(MODE_ENABLE_STATUS[index][0]);
            deltaYLabel.setEnabled(MODE_ENABLE_STATUS[index][1]);
            azimuthLabel.setEnabled(MODE_ENABLE_STATUS[index][2]);
            distFactorLabel.setEnabled(MODE_ENABLE_STATUS[index][3]);
            simulationLabel.setEnabled(MODE_ENABLE_STATUS[index][4]);
            activeTransmittersLabel.setEnabled(MODE_ENABLE_STATUS[index][5]);
            try
            {
                idpanel.setVisible(MODE_ENABLE_STATUS[index][6]);
            }
            catch(Exception ex) { }
        }
    }

    public int getNumberOfActiveTransmitters()
    {
        return ((Integer)activeTransmittersField.getValue()).intValue();
    }

    public double getSimulationRadius()
    {
        return ((Number)simulationRadiusField.getValue()).doubleValue();
    }

    public double getDeltaY()
    {
        return ((Number)deltaYField.getValue()).doubleValue();
    }

    public void setDeltaX(double deltaX)
    {
        deltaXField.setValue(new Double(deltaX));
    }

    public void setNumberOfActiveTransmitters(int numberOfActiveTransmitters)
    {
        activeTransmittersField.setValue(new Integer(numberOfActiveTransmitters));
    }

    public void setSimulationRadius(double simulationRadius)
    {
        simulationRadiusField.setValue(new Double(simulationRadius));
    }

    public void setDeltaY(double deltaY)
    {
        deltaYField.setValue(new Double(deltaY));
    }

    public double getDeltaX()
    {
        return ((Number)deltaXField.getValue()).doubleValue();
    }

    private static final ResourceBundle STRINGS;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private static final boolean MODE_ENABLE_STATUS[][] = {
        {
            false, false, true, true, true, true, false
        }, {
            false, false, true, false, false, true, true
        }, {
            false, false, true, false, false, false, true
        }, {
            true, true, false, false, false, false, false
        }, {
            true, true, false, false, false, false, false
        }, {
            true, true, false, false, false, false, false
        }, {
            true, true, false, false, false, false, false
        }
    };
    private static final boolean CDMA_MODE_ENABLE_STATUS[][] = {
        {
            true, true, false, false, false, false, false
        }, {
            false, false, true, true, false, false, false
        }
    };
    private static final String CDMA_MODES[] = {
        "Cor. (WTx -> Reference Cell BS)", "Dyn. (WTx -> Reference Cell BS)"
    };
    private LocationModeComboBoxModel cmbModel;
    private JComboBox modeSelector;
    private JFormattedTextField deltaXField;
    private JFormattedTextField deltaYField;
    private JButton azimuthButton;
    private JButton distFactorButton;
    private JFormattedTextField simulationRadiusField;
    private JFormattedTextField activeTransmittersField;
    private JCheckBox colocation;
    private JComboBox colocationLinks;
    private JLabel modeLabel;
    private JLabel deltaXLabel;
    private JLabel deltaYLabel;
    private JLabel azimuthLabel;
    private JLabel distFactorLabel;
    private JLabel simulationLabel;
    private JLabel activeTransmittersLabel;
    private DistributionDialog powerDistDialog;
    private JPanel idpanel;
    private boolean cdmaMode;
    private InterferenceLink il;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }




















}
