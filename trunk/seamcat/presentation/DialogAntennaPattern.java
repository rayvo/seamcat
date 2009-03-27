// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogAntennaPattern.java

package org.seamcat.presentation;

import java.awt.Container;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import org.seamcat.Seamcat;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.model.core.AntennaPattern;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.UserDefinedFunctionPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public class DialogAntennaPattern extends EscapeDialog
{
    class ControlPanel extends NavigateButtonPanel
    {

        public void btnOkActionPerformed()
        {
            double min = functionPanel.getDiscreteFunction().getRangeMin();
            double max = functionPanel.getDiscreteFunction().getRangeMax();
            if(min != pattern.getRangeMin() || max != pattern.getRangeMax())
            {
                JOptionPane.showMessageDialog(DialogAntennaPattern.this, (new StringBuilder()).append("Range is not complete.\nRange should be [").append(pattern.getRangeMin()).append(" to ").append(pattern.getRangeMax()).append("]").toString(), "Range error", 0);
            } else
            {
                accept = true;
                setVisible(false);
            }
        }

        public void btnCancelActionPerformed()
        {
            setVisible(false);
        }

        final DialogAntennaPattern this$0;

        ControlPanel()
        {
            this$0 = DialogAntennaPattern.this;
            super();
        }
    }


    public DialogAntennaPattern(JDialog parent)
    {
        super(parent, true);
        functionPanel = new UserDefinedFunctionPanel(STRINGS.getString("FUNCTION_ANTENNA_PATTERN"), "X", "Y", true);
        getContentPane().add(functionPanel, "Center");
        getContentPane().add(new ControlPanel(), "South");
        pack();
        setLocationRelativeTo(parent);
        try
        {
            Seamcat.helpBroker.enableHelpKey(getRootPane(), helplist.getString(getClass().getName()), null);
        }
        catch(Exception e) { }
        setResizable(false);
    }

    public void setVisible()
    {
        throw new UnsupportedOperationException("Call show(AntennaPattern, String) instead");
    }

    public boolean show(AntennaPattern data, String title)
    {
        accept = false;
        setTitle(title);
        setData(data);
        super.setVisible(true);
        return accept;
    }

    public void setData(AntennaPattern pattern)
    {
        this.pattern = pattern;
        functionPanel.setDiscreteFunction(pattern.getPattern());
        functionPanel.setSymmetryPoint(pattern.getSymmetryPoint());
    }

    public AntennaPattern getData()
    {
        if(pattern != null)
            pattern.setPattern(functionPanel.getDiscreteFunction());
        return pattern;
    }

    public void clear()
    {
        pattern = null;
    }

    private static final ResourceBundle STRINGS;
    private static final ResourceBundle helplist;
    private boolean accept;
    private UserDefinedFunctionPanel functionPanel;
    private AntennaPattern pattern;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }



}
