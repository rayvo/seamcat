// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SimulationControlDialog.java

package org.seamcat.presentation;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.seamcat.Seamcat;
import org.seamcat.model.EventGenerationData;
import org.seamcat.presentation.components.NavigateButtonPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, LabeledPairLayout, SeamcatTextFieldFormats

public class SimulationControlDialog extends EscapeDialog
{
    private final class DialogNavigateButtonPanel extends NavigateButtonPanel
    {

        public void btnOkActionPerformed()
        {
            accept = true;
            setVisible(false);
        }

        public void btnCancelActionPerformed()
        {
            setVisible(false);
        }

        final SimulationControlDialog this$0;

        private DialogNavigateButtonPanel()
        {
            this$0 = SimulationControlDialog.this;
            super();
        }

    }

    private static final class EventGenerationPanel extends JPanel
    {

        public boolean getUseExperimentalCDMA()
        {
            return cbTestCDMA.isSelected();
        }

        public final int getNumberOfEvents()
        {
            return ((Number)spNumberOfEvents.getValue()).intValue();
        }

        public final boolean getRunEGEInDebugMode()
        {
            return cbDebugMode.isSelected();
        }

        public final double getTimeLeft()
        {
            return ((Number)tfTimeLeft.getValue()).doubleValue();
        }

        public final void setNumberOfEvents(int numberOfEvents)
        {
            spNumberOfEvents.setValue(Integer.valueOf(numberOfEvents));
            int step = numberOfEvents / 10;
            if(step < 1)
                step = 1;
            ((SpinnerNumberModel)spNumberOfEvents.getModel()).setStepSize(Integer.valueOf(step));
        }

        public final void setTimeLeft(double timeLeft)
        {
            tfTimeLeft.setValue(new Double(timeLeft));
        }

        private final JSpinner spNumberOfEvents = new JSpinner(new SpinnerNumberModel(20000, 1, 0x4c4b40, 2500));
        private final JCheckBox cbTimeLimited = new JCheckBox(SimulationControlDialog.STRINGLIST.getString("EGE_CONTROL_TIME_LIMITED"));
        private final JFormattedTextField tfTimeLeft;
        private final JCheckBox cbDebugMode = new JCheckBox(SimulationControlDialog.STRINGLIST.getString("EGE_CONTROL_DEBUG_MODE"));
        private final JCheckBox cbTestCDMA = new JCheckBox("Use experimental CDMA Engine");
        private final JLabel lblTimeLeft = new JLabel(SimulationControlDialog.STRINGLIST.getString("EGE_CONTROL_TIME_LEFT"));







        public EventGenerationPanel()
        {
            super(new LabeledPairLayout());
            tfTimeLeft = new JFormattedTextField(SeamcatTextFieldFormats.DOUBLE_FACTORY);
            cbTimeLimited.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    tfTimeLeft.setEnabled(cbTimeLimited.isSelected());
                    lblTimeLeft.setEnabled(cbTimeLimited.isSelected());
                }

                final EventGenerationPanel this$0;

                
                {
                    this$0 = EventGenerationPanel.this;
                    super();
                }
            }
);
            tfTimeLeft.setHorizontalAlignment(4);
            JLabel lblNumberOfEvents = new JLabel(SimulationControlDialog.STRINGLIST.getString("EGE_CONTROL_NB_EVENTS"));
            lblNumberOfEvents.setLabelFor(spNumberOfEvents);
            lblTimeLeft.setLabelFor(tfTimeLeft);
            cbDebugMode.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e)
                {
                    if(cbDebugMode.isSelected())
                    {
                        spNumberOfEvents.getModel().setValue(new Integer(5));
                        ((SpinnerNumberModel)spNumberOfEvents.getModel()).setStepSize(new Integer(1));
                    } else
                    {
                        ((SpinnerNumberModel)spNumberOfEvents.getModel()).setStepSize(new Integer(2500));
                        ((SpinnerNumberModel)spNumberOfEvents.getModel()).setValue(new Integer(20000));
                    }
                }

                final EventGenerationPanel this$0;

                
                {
                    this$0 = EventGenerationPanel.this;
                    super();
                }
            }
);
            lblTimeLeft.setEnabled(false);
            tfTimeLeft.setEnabled(false);
            add(lblNumberOfEvents, "label");
            add(spNumberOfEvents, "field");
            add(new JLabel(""), "label");
            add(cbTimeLimited, "field");
            add(lblTimeLeft, "label");
            add(tfTimeLeft, "field");
            add(new JLabel(""), "label");
            add(cbDebugMode, "field");
            add(new JLabel(""), "label");
            add(cbTestCDMA, "field");
            setBorder(new TitledBorder(SimulationControlDialog.STRINGLIST.getString("EGE_CONTROL_EGE_CAPTION")));
        }
    }


    public SimulationControlDialog(Frame owner)
    {
        super(owner, true);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, 1));
        centerPanel.add(eventGenerationPanel);
        getContentPane().add(centerPanel, "Center");
        getContentPane().add(new DialogNavigateButtonPanel(), "South");
        setTitle(STRINGLIST.getString("EGE_CONTROL_CAPTION"));
        setSize(300, 200);
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), HELPLIST.getString(getClass().getName()), null);
    }

    public void setVisible()
    {
        throw new UnsupportedOperationException("Use show(EventGenerationData, DistributionEvaluationData)");
    }

    public boolean show(EventGenerationData eg)
    {
        accept = false;
        eventGenerationPanel.setNumberOfEvents(eg.getNumberOfEvents());
        eventGenerationPanel.setTimeLeft(eg.getTimeLeft());
        eventGenerationPanel.cbDebugMode.setSelected(eg.isInDebugMode());
        eventGenerationPanel.cbTestCDMA.setSelected(eg.getUseTestCDMAAlgoritm());
        eventGenerationPanel.cbTimeLimited.setSelected(eg.isTimeLimited());
        eventGenerationPanel.tfTimeLeft.setEnabled(eventGenerationPanel.cbTimeLimited.isSelected());
        eventGenerationPanel.lblTimeLeft.setEnabled(eventGenerationPanel.cbTimeLimited.isSelected());
        setLocationRelativeTo(getOwner());
        ((javax.swing.JSpinner.DefaultEditor)eventGenerationPanel.spNumberOfEvents.getEditor()).getTextField().selectAll();
        eventGenerationPanel.cbTestCDMA.setVisible(showExperimentalCDMASwitch);
        super.setVisible(true);
        return accept;
    }

    public void updateModel(EventGenerationData eg)
    {
        eg.setNumberOfEvents(eventGenerationPanel.getNumberOfEvents());
        eg.setTimeLeft(eventGenerationPanel.getTimeLeft());
        eg.setDebugMode(eventGenerationPanel.getRunEGEInDebugMode());
        eg.setTimeLimited(eventGenerationPanel.cbTimeLimited.isSelected());
        eg.setUseTestCDMAAlgoritm(eventGenerationPanel.getUseExperimentalCDMA());
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/SimulationControlDialog);
    private static final ResourceBundle STRINGLIST;
    private static final ResourceBundle HELPLIST;
    private final EventGenerationPanel eventGenerationPanel = new EventGenerationPanel();
    private boolean accept;
    public static boolean showExperimentalCDMASwitch = false;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        HELPLIST = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }


}
