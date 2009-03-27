// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InterferenceCalculationsPanel.java

package org.seamcat.presentation.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.seamcat.function.Point2D;
import org.seamcat.model.Workspace;
import org.seamcat.model.engines.*;
import org.seamcat.presentation.components.interferencecalc.AlgorithmSamplesPanel;
import org.seamcat.presentation.components.interferencecalc.CalculationModePanel;
import org.seamcat.presentation.components.interferencecalc.ICEControlPanel;
import org.seamcat.presentation.components.interferencecalc.ICEResultPanel;
import org.seamcat.presentation.components.interferencecalc.ICEStatusPanel;
import org.seamcat.presentation.components.interferencecalc.InterferenceCriterionPanel;
import org.seamcat.presentation.components.interferencecalc.SignalTypePanel;
import org.seamcat.presentation.components.interferencecalc.TranslationParametersPanel;

public class InterferenceCalculationsPanel extends JPanel
    implements InterferenceCalculationListener
{

    public InterferenceCalculationsPanel()
    {
        layout = new BorderLayout();
        calculationMode = new CalculationModePanel();
        signalType = new SignalTypePanel();
        interferenceCriterion = new InterferenceCriterionPanel();
        algorithmSamples = new AlgorithmSamplesPanel();
        iceControl = new ICEControlPanel(this);
        translationParameters = new TranslationParametersPanel();
        resultsPanel = new ICEResultPanel();
        statusPanel = new ICEStatusPanel();
        setLayout(layout);
        translationParameters.setElementStatusEnabled(false);
        calculationMode.addModeListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                translationParameters.setElementStatusEnabled(calculationMode.modeIsTranslation());
            }

            final InterferenceCalculationsPanel this$0;

            
            {
                this$0 = InterferenceCalculationsPanel.this;
                super();
            }
        }
);
        iceControl.addActionListenerStart(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                ice = new ICE(workspace);
                ice.addIceListener(InterferenceCalculationsPanel.this);
                updateModel();
                ice.calculateInterference(iceconf);
            }

            final InterferenceCalculationsPanel this$0;

            
            {
                this$0 = InterferenceCalculationsPanel.this;
                super();
            }
        }
);
        JPanel firstRow = new JPanel(new GridLayout(1, 4));
        firstRow.add(calculationMode);
        firstRow.add(signalType);
        firstRow.add(interferenceCriterion);
        firstRow.add(algorithmSamples);
        add(firstRow, "North");
        JPanel secondRow = new JPanel(new BorderLayout());
        secondRow.add(iceControl, "North");
        JPanel secondMiddle = new JPanel(new BorderLayout());
        secondMiddle.add(translationParameters, "West");
        secondMiddle.add(resultsPanel, "Center");
        secondRow.add(secondMiddle, "Center");
        add(secondRow, "Center");
        add(statusPanel, "South");
    }

    public void setWorkspace(Workspace workspace)
    {
        this.workspace = workspace;
        if(workspace != null)
        {
            if(workspace.getIceConfigurationCount() == 0)
                workspace.addIceConfiguration(new ICEConfiguration());
            iceControl.init(workspace);
        }
    }

    public void init(ICEConfiguration _iceconf)
    {
        iceconf = _iceconf;
        calculationMode.init(iceconf);
        signalType.init(iceconf);
        interferenceCriterion.init(iceconf);
        algorithmSamples.init(iceconf);
        translationParameters.init(iceconf, workspace);
        resultsPanel.init(iceconf);
    }

    public void updateModel()
    {
        translationParameters.updateModel();
        algorithmSamples.updateModel();
    }

    public void addTranslationResult(Point2D point)
    {
        resultsPanel.addTranslationResult(point);
        iceconf.addTranslationPoint(point);
    }

    public void calculationComplete()
    {
        statusPanel.setCurrentProcessCompletionPercentage(100);
        statusPanel.setTotalProcessCompletionPercentage(100);
        statusPanel.updateStatus("Interference Calculation Complete");
        iceControl.updateIceConf();
    }

    public void calculationStarted()
    {
        statusPanel.updateStatus("ICE is running");
        iceControl.updateIceConf();
    }

    public boolean confirmContinueOnWarning(String warning)
    {
        return JOptionPane.showConfirmDialog(this, warning, "ICE Warning", 0, 2) == 0;
    }

    public void incrementCurrentProcessCompletionPercentage(int value)
    {
        statusPanel.incrementCurrentProcessCompletionPercentage(value);
    }

    public void incrementTotalProcessCompletionPercentage(int value)
    {
        statusPanel.incrementTotalProcessCompletionPercentage(value);
    }

    public void propabilityResult(double result)
    {
        resultsPanel.setProbabilityResult(result);
        iceconf.setPropabilityResult(result);
    }

    public void setCurrentProcessCompletionPercentage(int value)
    {
        statusPanel.setCurrentProcessCompletionPercentage(value);
    }

    public void setTotalProcessCompletionPercentage(int value)
    {
        statusPanel.setTotalProcessCompletionPercentage(value);
    }

    public void updateStatus(String status)
    {
        statusPanel.updateStatus(status);
    }

    public void warningMessage(String warning)
    {
        JOptionPane.showMessageDialog(this, warning, "ICE Error", 0);
    }

    public void infoMessage(String message)
    {
        JOptionPane.showMessageDialog(this, message, "ICE Information", 1);
    }

    private Workspace workspace;
    private LayoutManager layout;
    private CalculationModePanel calculationMode;
    private SignalTypePanel signalType;
    private InterferenceCriterionPanel interferenceCriterion;
    private AlgorithmSamplesPanel algorithmSamples;
    private ICEControlPanel iceControl;
    private TranslationParametersPanel translationParameters;
    private ICEResultPanel resultsPanel;
    private ICEStatusPanel statusPanel;
    private ICE ice;
    private ICEConfiguration iceconf;






}
