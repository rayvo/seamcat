// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DistributionDialog.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.seamcat.Seamcat;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.distribution.*;
import org.seamcat.function.Point2D;
import org.seamcat.interfaces.Functionable;
import org.seamcat.presentation.components.FunctionButtonPanel;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.SeamcatTable;
import org.seamcat.presentation.components.StairDistributionTableModelAdapter;
import org.seamcat.presentation.components.UserDefinedFunctionPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, StairDistributionGraph, FileDataIO, DialogTableToDataSet, 
//            SeamcatTextFieldFormats, LabeledPairLayout

public class DistributionDialog extends EscapeDialog
{
    private static class StairDistributionPanel extends JPanel
    {
        private class DialogFunctionButtonPanel extends FunctionButtonPanel
        {

            public void btnLoadActionPerformed()
            {
                if(fileChooser.showOpenDialog(this) == 0)
                {
                    fileio.setFile(fileChooser.getSelectedFile());
                    model.setPoints(fileio.loadTableData().getPointsList());
                }
            }

            public void btnSaveActionPerformed()
            {
                if(fileChooser.showSaveDialog(this) == 0)
                {
                    fileio.setFile(fileChooser.getSelectedFile());
                    fileio.saveTableData(model.getPoints());
                }
            }

            public void btnClearActionPerformed()
            {
                ((StairDistributionTableModelAdapter)dataTable.getModel()).clear();
            }

            public void btnAddActionPerformed()
            {
                ((StairDistributionTableModelAdapter)dataTable.getModel()).addRow();
            }

            public void btnDeleteActionPerformed()
            {
                model.deleteRow(dataTable.getSelectedRow());
            }

            public void btnSymActionPerformed()
            {
                DialogTableToDataSet.symmetrize(model.getPointsList(), 0.0D);
                model.sortPoints();
                model.fireChangeListeners();
            }

            final StairDistributionPanel this$0;

            public DialogFunctionButtonPanel()
            {
                this$0 = StairDistributionPanel.this;
                super();
            }
        }


        public boolean isLastCell()
        {
            int rows = dataTable.getRowCount();
            int cols = dataTable.getColumnCount();
            int selectedRow = dataTable.getSelectedRow();
            int selectedCol = dataTable.getSelectedColumn();
            return rows == selectedRow + 1 && cols == selectedCol + 1;
        }

        public TableModel getModel()
        {
            return model;
        }

        public void setStairDistribution(StairDistribution d)
        {
            model.setPoints(d);
        }

        public Point2D[] getPoints()
        {
            return model.getPoints();
        }

        private JTable dataTable;
        private StairDistributionGraph functionGraph;
        private StairDistributionTableModelAdapter model;



        public StairDistributionPanel()
        {
            model = new StairDistributionTableModelAdapter();
            dataTable = new SeamcatTable(model);
            JScrollPane dataTableScrollPane = new JScrollPane(dataTable);
            dataTableScrollPane.setPreferredSize(new Dimension(200, 200));
            functionGraph = new StairDistributionGraph(model);
            setLayout(new BoxLayout(this, 0));
            add(dataTableScrollPane);
            add(new DialogFunctionButtonPanel());
            add(functionGraph);
            setBorder(new TitledBorder("Stair distribution"));
        }
    }

    private class DiscreteUniformDistributionState extends NonUserDefinedDistributionState
    {

        public Distribution getDistribution()
        {
            return new DiscreteUniformDistribution(parametersPanel.getComponentValue(3).doubleValue(), parametersPanel.getComponentValue(4).doubleValue(), parametersPanel.getComponentValue(7).doubleValue());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            if(!(d instanceof DiscreteUniformDistribution))
            {
                throw new IllegalArgumentException("Instance should be of class DiscreteUniformDistribution");
            } else
            {
                parametersPanel.setComponentValue(3, new Double(d.getMin()));
                parametersPanel.setComponentValue(4, new Double(d.getMax()));
                parametersPanel.setComponentValue(7, new Double(d.getStep()));
                return;
            }
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(8);
            parametersPanel.setComponentState(3, enabled);
            parametersPanel.setComponentState(4, enabled);
            parametersPanel.setComponentState(7, enabled);
        }

        final DistributionDialog this$0;

        private DiscreteUniformDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class UserDefinedStairDistributionState extends UserDefinedDistributionState
    {

        public Distribution getDistribution()
        {
            return new StairDistribution(stairDistributionPanel.getPoints());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            stairDistributionPanel.setStairDistribution((StairDistribution)d);
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(7);
        }

        public void enterState()
        {
            super.enterState();
            contentPanelLayout.show(contentPanel, DistributionDialog.LAYOUTS[2]);
        }

        final DistributionDialog this$0;

        private UserDefinedStairDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class UniformPolarAngleDistributionState extends NonUserDefinedDistributionState
    {

        public Distribution getDistribution()
        {
            return new UniformPolarAngleDistribution(parametersPanel.getComponentValue(6).doubleValue());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            if(!(d instanceof UniformPolarAngleDistribution))
            {
                throw new IllegalArgumentException((new StringBuilder()).append("Instance should be of class UniformPolarAngelDistribution (is ").append(d.getClass()).append(")").toString());
            } else
            {
                parametersPanel.setComponentValue(6, new Double(d.getMaxAngle()));
                return;
            }
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(6);
            parametersPanel.setComponentState(6, enabled);
        }

        final DistributionDialog this$0;

        private UniformPolarAngleDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class UniformPolarDistanceDistributionState extends NonUserDefinedDistributionState
    {

        public Distribution getDistribution()
        {
            return new UniformPolarDistanceDistribution(parametersPanel.getComponentValue(5).doubleValue());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            if(!(d instanceof UniformPolarDistanceDistribution))
            {
                throw new IllegalArgumentException("Instance should be of class UniformPolarDistanceDistribution");
            } else
            {
                parametersPanel.setComponentValue(5, new Double(d.getMaxDistance()));
                return;
            }
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(5);
            parametersPanel.setComponentState(5, enabled);
        }

        final DistributionDialog this$0;

        private UniformPolarDistanceDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class RayleighDistributionState extends NonUserDefinedDistributionState
    {

        public Distribution getDistribution()
        {
            return new RayleighDistribution(parametersPanel.getComponentValue(3).doubleValue(), parametersPanel.getComponentValue(2).doubleValue());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            if(!(d instanceof RayleighDistribution))
            {
                throw new IllegalArgumentException("Instance should be of class RayleighDistribution");
            } else
            {
                parametersPanel.setComponentValue(3, new Double(d.getMin()));
                parametersPanel.setComponentValue(2, new Double(d.getStdDev()));
                return;
            }
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(4);
            parametersPanel.setComponentState(3, enabled);
            parametersPanel.setComponentState(2, enabled);
        }

        final DistributionDialog this$0;

        private RayleighDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class GaussianDistributionState extends NonUserDefinedDistributionState
    {

        public Distribution getDistribution()
        {
            return new GaussianDistribution(parametersPanel.getComponentValue(1).doubleValue(), parametersPanel.getComponentValue(2).doubleValue());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            if(!(d instanceof GaussianDistribution))
            {
                throw new IllegalArgumentException("Instance should be of class GaussianDistribution");
            } else
            {
                parametersPanel.setComponentValue(1, new Double(d.getMean()));
                parametersPanel.setComponentValue(2, new Double(d.getStdDev()));
                return;
            }
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(3);
            parametersPanel.setComponentState(1, enabled);
            parametersPanel.setComponentState(2, enabled);
        }

        final DistributionDialog this$0;

        private GaussianDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class UniformDistributionState extends NonUserDefinedDistributionState
    {

        public Distribution getDistribution()
        {
            return new UniformDistribution(parametersPanel.getComponentValue(3).doubleValue(), parametersPanel.getComponentValue(4).doubleValue());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            if(!(d instanceof UniformDistribution))
            {
                throw new IllegalArgumentException("Instance should be of class UniformDistribution");
            } else
            {
                parametersPanel.setComponentValue(3, new Double(d.getMin()));
                parametersPanel.setComponentValue(4, new Double(d.getMax()));
                return;
            }
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(2);
            parametersPanel.setComponentState(3, enabled);
            parametersPanel.setComponentState(4, enabled);
        }

        final DistributionDialog this$0;

        private UniformDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class UserDefinedDistributionState extends DistributionState
    {

        public Distribution getDistribution()
        {
            return new ContinousDistribution(userDefinedFunctionPanel.getDiscreteFunction());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            userDefinedFunctionPanel.setDiscreteFunction(((ContinousDistribution)d).getCdf());
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(1);
        }

        public void enterState()
        {
            super.enterState();
            contentPanelLayout.show(contentPanel, DistributionDialog.LAYOUTS[1]);
        }

        final DistributionDialog this$0;

        private UserDefinedDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class ConstantDistributionState extends NonUserDefinedDistributionState
    {

        public Distribution getDistribution()
        {
            return new ConstantDistribution(parametersPanel.getComponentValue(0).doubleValue());
        }

        public void setDistribution(Distribution d)
            throws IllegalArgumentException
        {
            if(!(d instanceof ConstantDistribution))
            {
                throw new IllegalArgumentException("Instance should be of class ConstantDistribution");
            } else
            {
                parametersPanel.setComponentValue(0, new Double(d.getConstant()));
                return;
            }
        }

        protected void setComponentState(boolean enabled)
        {
            if(enabled)
                typePanel.setSelectedButton(0);
            parametersPanel.setComponentState(0, enabled);
        }

        final DistributionDialog this$0;

        private ConstantDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private abstract class NonUserDefinedDistributionState extends DistributionState
    {

        public void enterState()
        {
            super.enterState();
            contentPanelLayout.show(contentPanel, DistributionDialog.LAYOUTS[0]);
        }

        final DistributionDialog this$0;

        private NonUserDefinedDistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private abstract class DistributionState
    {

        public abstract Distribution getDistribution();

        public abstract void setDistribution(Distribution distribution)
            throws IllegalArgumentException;

        protected abstract void setComponentState(boolean flag);

        public void enterState()
        {
            setComponentState(true);
        }

        public void leaveState()
        {
            setComponentState(false);
        }

        final DistributionDialog this$0;

        private DistributionState()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private class ParametersPanel extends JPanel
    {

        public void setComponentState(int component, boolean enabled)
        {
            switch(component)
            {
            case 0: // '\0'
                lblConstant.setEnabled(enabled);
                tfConstant.setEnabled(enabled);
                break;

            case 1: // '\001'
                lblMean.setEnabled(enabled);
                tfMean.setEnabled(enabled);
                break;

            case 2: // '\002'
                lblStdDev.setEnabled(enabled);
                tfStdDev.setEnabled(enabled);
                break;

            case 3: // '\003'
                lblMin.setEnabled(enabled);
                tfMin.setEnabled(enabled);
                break;

            case 4: // '\004'
                lblMax.setEnabled(enabled);
                tfMax.setEnabled(enabled);
                break;

            case 5: // '\005'
                lblMaxDistance.setEnabled(enabled);
                tfMaxDistance.setEnabled(enabled);
                break;

            case 6: // '\006'
                lblMaxAngle.setEnabled(enabled);
                tfMaxAngle.setEnabled(enabled);
                break;

            case 7: // '\007'
                lblStep.setEnabled(enabled);
                tfStep.setEnabled(enabled);
                break;

            default:
                throw new IllegalArgumentException("Illegal componentvalue");
            }
        }

        public void setComponentValue(int component, Number value)
        {
            switch(component)
            {
            case 0: // '\0'
                tfConstant.setValue(value);
                break;

            case 1: // '\001'
                tfMean.setValue(value);
                break;

            case 2: // '\002'
                tfStdDev.setValue(value);
                break;

            case 3: // '\003'
                tfMin.setValue(value);
                break;

            case 4: // '\004'
                tfMax.setValue(value);
                break;

            case 5: // '\005'
                tfMaxDistance.setValue(value);
                break;

            case 6: // '\006'
                tfMaxAngle.setValue(value);
                break;

            case 7: // '\007'
                tfStep.setValue(value);
                break;

            default:
                throw new IllegalArgumentException("Illegal componentvalue");
            }
        }

        public Number getComponentValue(int component)
        {
            Number n;
            switch(component)
            {
            case 0: // '\0'
                n = (Number)tfConstant.getValue();
                break;

            case 1: // '\001'
                n = (Number)tfMean.getValue();
                break;

            case 2: // '\002'
                n = (Number)tfStdDev.getValue();
                break;

            case 3: // '\003'
                n = (Number)tfMin.getValue();
                break;

            case 4: // '\004'
                n = (Number)tfMax.getValue();
                break;

            case 5: // '\005'
                n = (Number)tfMaxDistance.getValue();
                break;

            case 6: // '\006'
                n = (Number)tfMaxAngle.getValue();
                break;

            case 7: // '\007'
                n = (Number)tfStep.getValue();
                break;

            default:
                throw new IllegalArgumentException("Illegal componentvalue");
            }
            return n;
        }

        public static final int CONSTANT = 0;
        public static final int MEAN = 1;
        public static final int STDDEV = 2;
        public static final int MIN = 3;
        public static final int MAX = 4;
        public static final int MAXDISTANCE = 5;
        public static final int MAXANGLE = 6;
        public static final int STEP = 7;
        private SeamcatTextFieldFormats df;
        private JFormattedTextField tfConstant;
        private JFormattedTextField tfMaxAngle;
        private JFormattedTextField tfMax;
        private JFormattedTextField tfMean;
        private JFormattedTextField tfMin;
        private JFormattedTextField tfStdDev;
        private JFormattedTextField tfStep;
        private JFormattedTextField tfMaxDistance;
        private JLabel lblConstant;
        private JLabel lblMax;
        private JLabel lblMaxAngle;
        private JLabel lblMaxDistance;
        private JLabel lblMean;
        private JLabel lblMin;
        private JLabel lblStdDev;
        private JLabel lblStep;
        final DistributionDialog this$0;

        public ParametersPanel()
        {
            this$0 = DistributionDialog.this;
            super(new FlowLayout(0));
            df = new SeamcatTextFieldFormats();
            tfConstant = new FormattedCalculatorField(30D, owner);
            tfMaxAngle = new FormattedCalculatorField(360D, owner);
            tfMax = new FormattedCalculatorField(1.0D, owner);
            tfMean = new FormattedCalculatorField(0.0D, owner);
            tfMin = new FormattedCalculatorField(0.0D, owner);
            tfStdDev = new FormattedCalculatorField(0.0D, owner);
            tfStep = new FormattedCalculatorField(0.20000000000000001D, owner);
            tfMaxDistance = new FormattedCalculatorField(1.0D, owner);
            lblConstant = new JLabel(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_CONSTANT"));
            lblMax = new JLabel(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_PARAM_MAX"));
            lblMaxAngle = new JLabel(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_PARAM_MAXANGLE"));
            lblMaxDistance = new JLabel(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_PARAM_MAXDIST"));
            lblMean = new JLabel(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_PARAM_MEAN"));
            lblMin = new JLabel(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_PARAM_MIN"));
            lblStdDev = new JLabel(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_PARAM_STDDEV"));
            lblStep = new JLabel(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_PARAM_STEP"));
            lblConstant.setLabelFor(tfConstant);
            lblMean.setLabelFor(tfMean);
            lblStdDev.setLabelFor(tfStdDev);
            lblMin.setLabelFor(tfMin);
            lblMax.setLabelFor(tfMax);
            lblMaxDistance.setLabelFor(tfMaxDistance);
            lblMaxAngle.setLabelFor(tfMaxAngle);
            lblStep.setLabelFor(tfMaxAngle);
            tfConstant.setEnabled(false);
            tfMean.setEnabled(false);
            tfStdDev.setEnabled(false);
            tfMin.setEnabled(false);
            tfMax.setEnabled(false);
            tfMaxDistance.setEnabled(false);
            tfMaxAngle.setEnabled(false);
            tfStep.setEnabled(false);
            lblConstant.setEnabled(false);
            lblMean.setEnabled(false);
            lblStdDev.setEnabled(false);
            lblMin.setEnabled(false);
            lblMax.setEnabled(false);
            lblMaxDistance.setEnabled(false);
            lblMaxAngle.setEnabled(false);
            lblStep.setEnabled(false);
            int columns = 12;
            tfConstant.setColumns(columns);
            tfMean.setColumns(columns);
            tfStdDev.setColumns(columns);
            tfMin.setColumns(columns);
            tfMax.setColumns(columns);
            tfMaxDistance.setColumns(columns);
            tfMaxAngle.setColumns(columns);
            tfStep.setColumns(columns);
            JPanel innerPanel = new JPanel(new LabeledPairLayout());
            innerPanel.add(lblConstant, "label");
            innerPanel.add(tfConstant, "field");
            innerPanel.add(lblMean, "label");
            innerPanel.add(tfMean, "field");
            innerPanel.add(lblStdDev, "label");
            innerPanel.add(tfStdDev, "field");
            innerPanel.add(lblMin, "label");
            innerPanel.add(tfMin, "field");
            innerPanel.add(lblMax, "label");
            innerPanel.add(tfMax, "field");
            innerPanel.add(lblMaxDistance, "label");
            innerPanel.add(tfMaxDistance, "field");
            innerPanel.add(lblMaxAngle, "label");
            innerPanel.add(tfMaxAngle, "field");
            innerPanel.add(lblStep, "label");
            innerPanel.add(tfStep, "field");
            add(innerPanel);
            setBorder(new TitledBorder("Parameters"));
        }
    }

    private class TypePanel extends JPanel
    {

        public void setTypeChangeEnabled(boolean value)
        {
            btnConstant.setEnabled(value || btnConstant.isSelected());
            btnDiscreteUniform.setEnabled(value || btnDiscreteUniform.isSelected());
            btnGaussian.setEnabled(value || btnGaussian.isSelected());
            btnRayleigh.setEnabled(value || btnRayleigh.isSelected());
            btnUniform.setEnabled(value || btnUniform.isSelected());
            btnUniformPolarAngle.setEnabled(value || btnUniformPolarAngle.isSelected());
            btnUniformPolarDist.setEnabled(value || btnUniformPolarDist.isSelected());
            btnUserDefined.setEnabled(value || btnUserDefined.isSelected());
            btnUserDefinedStair.setEnabled(value || btnUserDefinedStair.isSelected());
        }

        private void btnUniformPolarAngleActionPerformed(ActionEvent e)
        {
            setState(UNIFORMPOLARANGLE_STATE);
        }

        private void btnUserDefinedStairActionPerformed(ActionEvent e)
        {
            setState(USERDEFINEDSTAIR_STATE);
        }

        private void btnUniformPolarDistActionPerformed(ActionEvent e)
        {
            setState(UNIFORMPOLARDISTANCE_STATE);
        }

        private void btnRayleighActionPerformed(ActionEvent e)
        {
            setState(RAYLEIGH_STATE);
        }

        private void btnUniformActionPerformed(ActionEvent e)
        {
            setState(UNIFORM_STATE);
        }

        private void btnUserDefinedActionPerformed(ActionEvent e)
        {
            setState(USERDEFINED_STATE);
        }

        private void btnConstantActionPerformed(ActionEvent e)
        {
            setState(CONSTANT_STATE);
        }

        private void btnGaussianActionPerformed(ActionEvent e)
        {
            setState(GAUSSIAN_STATE);
        }

        private void btnDiscreteUniformActionPerformed(ActionEvent e)
        {
            setState(DISCRETEUNIFORM_STATE);
        }

        public void setSelectedButton(int button)
        {
            switch(button)
            {
            case 0: // '\0'
                btnConstant.setSelected(true);
                btnConstant.requestFocus();
                break;

            case 1: // '\001'
                btnUserDefined.setSelected(true);
                btnUserDefined.requestFocus();
                break;

            case 2: // '\002'
                btnUniform.setSelected(true);
                btnUniform.requestFocus();
                break;

            case 3: // '\003'
                btnGaussian.setSelected(true);
                btnGaussian.requestFocus();
                break;

            case 4: // '\004'
                btnRayleigh.setSelected(true);
                btnRayleigh.requestFocus();
                break;

            case 5: // '\005'
                btnUniformPolarDist.setSelected(true);
                btnUniformPolarDist.requestFocus();
                break;

            case 6: // '\006'
                btnUniformPolarAngle.setSelected(true);
                btnUniformPolarAngle.requestFocus();
                break;

            case 7: // '\007'
                btnUserDefinedStair.setSelected(true);
                btnUserDefinedStair.requestFocus();
                break;

            case 8: // '\b'
                btnDiscreteUniform.setSelected(true);
                btnDiscreteUniform.requestFocus();
                break;

            default:
                throw new IllegalArgumentException("Illegal button state");
            }
        }

        private JRadioButton btnConstant;
        private JRadioButton btnDiscreteUniform;
        private JRadioButton btnGaussian;
        private JRadioButton btnRayleigh;
        private JRadioButton btnUniform;
        private JRadioButton btnUniformPolarAngle;
        private JRadioButton btnUniformPolarDist;
        private JRadioButton btnUserDefined;
        private JRadioButton btnUserDefinedStair;
        final DistributionDialog this$0;










        public TypePanel()
        {
            this$0 = DistributionDialog.this;
            super();
            btnConstant = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_CONSTANT"));
            btnDiscreteUniform = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_DISCRETE_UNIFORM"));
            btnGaussian = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_GAUSSIAN"));
            btnRayleigh = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_RAYLEIGH"));
            btnUniform = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_UNIFORM"));
            btnUniformPolarAngle = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_UNIFORM_POLAR_ANGLE"));
            btnUniformPolarDist = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_UNIFORM_POLAR_DIST"));
            btnUserDefined = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_USERDEFINED"));
            btnUserDefinedStair = new JRadioButton(DistributionDialog.STRINGLIST.getString("DISTRIBUTION_USERDEFINED_STAIR"));
            ButtonGroup buttonGroupType = new ButtonGroup();
            buttonGroupType.add(btnConstant);
            buttonGroupType.add(btnDiscreteUniform);
            buttonGroupType.add(btnGaussian);
            buttonGroupType.add(btnRayleigh);
            buttonGroupType.add(btnUniform);
            buttonGroupType.add(btnUniformPolarAngle);
            buttonGroupType.add(btnUniformPolarDist);
            buttonGroupType.add(btnUserDefined);
            buttonGroupType.add(btnUserDefinedStair);
            btnConstant.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnConstantActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnUserDefined.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnUserDefinedActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnUniform.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnUniformActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnGaussian.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnGaussianActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnRayleigh.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnRayleighActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnUniformPolarDist.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnUniformPolarDistActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnUniformPolarAngle.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnUniformPolarAngleActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnUserDefinedStair.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnUserDefinedStairActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnDiscreteUniform.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnDiscreteUniformActionPerformed(evt);
                }

                final DistributionDialog val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            setLayout(new BoxLayout(this, 1));
            add(btnConstant);
            add(btnUserDefined);
            add(btnUniform);
            add(btnGaussian);
            add(btnRayleigh);
            add(btnUniformPolarDist);
            add(btnUniformPolarAngle);
            add(btnUserDefinedStair);
            add(btnDiscreteUniform);
            setBorder(new TitledBorder("Type"));
        }
    }

    private class ButtonPanel extends NavigateButtonPanel
    {

        public void btnOkActionPerformed()
        {
            if(check != null && !check.validate(state.getDistribution()))
            {
                JOptionPane.showMessageDialog(DistributionDialog.this, check.getErrorString(), "Distribution not valid", 2);
                return;
            }
            if(state == USERDEFINEDSTAIR_STATE && !((StairDistribution)state.getDistribution()).validate())
            {
                JOptionPane.showMessageDialog(DistributionDialog.this, "Cumulative Propability MUST be between 0 and 1", "CDF Warning", 2);
                return;
            } else
            {
                accept = true;
                setVisible(false);
                return;
            }
        }

        public void btnCancelActionPerformed()
        {
            setVisible(false);
        }

        final DistributionDialog this$0;

        private ButtonPanel()
        {
            this$0 = DistributionDialog.this;
            super();
        }

    }

    private static class FunctionPointsVerifier
        implements TableModelListener
    {

        public void tableChanged(TableModelEvent t)
        {
            DistributionDialog.LOG.debug((new StringBuilder()).append("Change in column <").append(t.getColumn()).append("> first-row <").append(t.getFirstRow()).append("> last-row <").append(t.getLastRow()).append("> type <").append(t.getType()).append(">").toString());
            switch(t.getType())
            {
            case -1: 
            case 1: // '\001'
                break;

            case 0: // '\0'
                if(t.getColumn() == 1)
                {
                    double value = ((Number)((TableModel)t.getSource()).getValueAt(t.getFirstRow(), t.getColumn())).doubleValue();
                    if(value > 1.0D || value < 0.0D)
                        JOptionPane.showMessageDialog(null, "Cumulated probability P(X < x) must be between 0 and 1", "Illegal value", 0);
                }
                break;

            default:
                throw new IllegalArgumentException("Illegal TableModelEvent type. Must be INSERT/UPDATE or DELETE");
            }
        }

        private FunctionPointsVerifier()
        {
        }

    }

    public static interface DistributionCheck
    {

        public abstract boolean validate(Distribution distribution);

        public abstract String getErrorString();
    }


    public DistributionDialog(JDialog parent, boolean modal)
    {
        super(parent, modal);
        CONSTANT_STATE = new ConstantDistributionState();
        USERDEFINED_STATE = new UserDefinedDistributionState();
        UNIFORM_STATE = new UniformDistributionState();
        GAUSSIAN_STATE = new GaussianDistributionState();
        RAYLEIGH_STATE = new RayleighDistributionState();
        UNIFORMPOLARDISTANCE_STATE = new UniformPolarDistanceDistributionState();
        UNIFORMPOLARANGLE_STATE = new UniformPolarAngleDistributionState();
        USERDEFINEDSTAIR_STATE = new UserDefinedStairDistributionState();
        DISCRETEUNIFORM_STATE = new DiscreteUniformDistributionState();
        typePanel = new TypePanel();
        parametersPanel = new ParametersPanel();
        userDefinedFunctionPanel = new UserDefinedFunctionPanel("User defined distribution", "Value", "Prob.");
        stairDistributionPanel = new StairDistributionPanel();
        contentPanelLayout = new CardLayout();
        contentPanel = new JPanel(contentPanelLayout);
        userDefinedFunctionPanel.getModel().addTableModelListener(new FunctionPointsVerifier());
        userDefinedFunctionPanel.setUsePropabilitySymmetrizeFunction(true);
        contentPanel.add(parametersPanel, LAYOUTS[0]);
        contentPanel.add(userDefinedFunctionPanel, LAYOUTS[1]);
        contentPanel.add(stairDistributionPanel, LAYOUTS[2]);
        getContentPane().add(typePanel, "West");
        getContentPane().add(new ButtonPanel(), "South");
        getContentPane().add(contentPanel, "Center");
        pack();
        setLocationRelativeTo(parent);
        try
        {
            Seamcat.helpBroker.enableHelpKey(super.getRootPane(), HELPLIST.getString(getClass().getName()), null);
        }
        catch(Exception e) { }
    }

    public DistributionDialog(Frame parent, boolean modal)
    {
        super(parent, modal);
        CONSTANT_STATE = new ConstantDistributionState();
        USERDEFINED_STATE = new UserDefinedDistributionState();
        UNIFORM_STATE = new UniformDistributionState();
        GAUSSIAN_STATE = new GaussianDistributionState();
        RAYLEIGH_STATE = new RayleighDistributionState();
        UNIFORMPOLARDISTANCE_STATE = new UniformPolarDistanceDistributionState();
        UNIFORMPOLARANGLE_STATE = new UniformPolarAngleDistributionState();
        USERDEFINEDSTAIR_STATE = new UserDefinedStairDistributionState();
        DISCRETEUNIFORM_STATE = new DiscreteUniformDistributionState();
        typePanel = new TypePanel();
        parametersPanel = new ParametersPanel();
        userDefinedFunctionPanel = new UserDefinedFunctionPanel("User defined distribution", "Value", "Prob.");
        stairDistributionPanel = new StairDistributionPanel();
        contentPanelLayout = new CardLayout();
        contentPanel = new JPanel(contentPanelLayout);
        userDefinedFunctionPanel.getModel().addTableModelListener(new FunctionPointsVerifier());
        userDefinedFunctionPanel.setUsePropabilitySymmetrizeFunction(true);
        contentPanel.add(parametersPanel, LAYOUTS[0]);
        contentPanel.add(userDefinedFunctionPanel, LAYOUTS[1]);
        contentPanel.add(stairDistributionPanel, LAYOUTS[2]);
        getContentPane().add(typePanel, "West");
        getContentPane().add(new ButtonPanel(), "South");
        getContentPane().add(contentPanel, "Center");
        pack();
        setLocationRelativeTo(parent);
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), HELPLIST.getString(getClass().getName()), null);
    }

    protected void setState(DistributionState s)
    {
        if(state != null)
            state.leaveState();
        state = s;
        if(state != null)
            state.enterState();
    }

    /**
     * @deprecated Method setVisible is deprecated
     */

    public void setVisible()
    {
        throw new UnsupportedOperationException("Use showDistributionDialog() or showDistributionDialog(Distribution)");
    }

    public boolean showDistributionDialog(Distribution d)
    {
        return showDistributionDialog(d, "Distribution definition");
    }

    public boolean showDistributionDialog(Distribution d, boolean typeChangeAllowed, DistributionCheck _check)
    {
        typePanel.setTypeChangeEnabled(typeChangeAllowed);
        boolean b = showDistributionDialog(d, "Distribution definition", _check);
        typePanel.setTypeChangeEnabled(true);
        check = null;
        return b;
    }

    public boolean showDistributionDialog(Distribution d, String windowtitle)
    {
        return showDistributionDialog(d, windowtitle, null);
    }

    public boolean showDistributionDialog(Distribution d, String windowtitle, DistributionCheck _check)
    {
        check = _check;
        if(d == null)
        {
            d = new ConstantDistribution();
            LOG.debug("showDistributionDialog() was called with <null>. Defaulting to ConstantDistribution");
        }
        if(d instanceof ConstantDistribution)
            setState(CONSTANT_STATE);
        else
        if(d instanceof UniformDistribution)
            setState(UNIFORM_STATE);
        else
        if(d instanceof GaussianDistribution)
            setState(GAUSSIAN_STATE);
        else
        if(d instanceof RayleighDistribution)
            setState(RAYLEIGH_STATE);
        else
        if(d instanceof UniformPolarAngleDistribution)
            setState(UNIFORMPOLARANGLE_STATE);
        else
        if(d instanceof UniformPolarDistanceDistribution)
            setState(UNIFORMPOLARDISTANCE_STATE);
        else
        if(d instanceof DiscreteUniformDistribution)
            setState(DISCRETEUNIFORM_STATE);
        else
        if(d instanceof ContinousDistribution)
            setState(USERDEFINED_STATE);
        else
        if(d instanceof StairDistribution)
            setState(USERDEFINEDSTAIR_STATE);
        else
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown distribution type <").append(d.getClass()).append(">").toString());
        state.setDistribution(d);
        setTitle(windowtitle);
        accept = false;
        super.setVisible(true);
        check = null;
        return accept;
    }

    public boolean showDistributionDialog()
    {
        return showDistributionDialog((Distribution)null);
    }

    public boolean showDistributionDialog(String title)
    {
        return showDistributionDialog((Distribution)null, title);
    }

    public Distribution getDistributionable()
    {
        if(state != null)
        {
            return state.getDistribution();
        } else
        {
            LOG.debug("There's no active distribution state. Cannot return Distribution object");
            throw new IllegalStateException("No distribution entered");
        }
    }

    public void clear()
    {
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/DistributionDialog);
    private static final ResourceBundle STRINGLIST;
    private static final ResourceBundle HELPLIST;
    private static final int CONSTANT = 0;
    private static final int USERDEFINED = 1;
    private static final int UNIFORM = 2;
    private static final int GAUSSIAN = 3;
    private static final int RAYLEIGH = 4;
    private static final int UNIFORMPOLARDISTANCE = 5;
    private static final int UNIFORMPOLARANGLE = 6;
    private static final int USERDEFINEDSTAIR = 7;
    private static final int DISCRETEUNIFORM = 8;
    private final DistributionState CONSTANT_STATE;
    private final DistributionState USERDEFINED_STATE;
    private final DistributionState UNIFORM_STATE;
    private final DistributionState GAUSSIAN_STATE;
    private final DistributionState RAYLEIGH_STATE;
    private final DistributionState UNIFORMPOLARDISTANCE_STATE;
    private final DistributionState UNIFORMPOLARANGLE_STATE;
    private final DistributionState USERDEFINEDSTAIR_STATE;
    private final DistributionState DISCRETEUNIFORM_STATE;
    private static final String LAYOUTS[] = {
        "NON_USERDEFINED_LAYOUT", "USERDEFINED_LAYOUT", "USERDEFINED_STAIR_LAYOUT"
    };
    private static final int NON_USERDEFINED_LAYOUT = 0;
    private static final int USERDEFINED_LAYOUT = 1;
    private static final int USERDEFINED_STAIR_LAYOUT = 2;
    private TypePanel typePanel;
    private ParametersPanel parametersPanel;
    private UserDefinedFunctionPanel userDefinedFunctionPanel;
    private StairDistributionPanel stairDistributionPanel;
    private CardLayout contentPanelLayout;
    private JPanel contentPanel;
    private boolean accept;
    private DistributionState state;
    private DistributionCheck check;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        HELPLIST = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }





















}
