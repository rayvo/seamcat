// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogFunctionDefine.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;
import org.seamcat.Seamcat;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.function.*;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.UserDefinedFunctionPanel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, SeamcatTextFieldFormats

public class DialogFunctionDefine extends EscapeDialog
{
    private class DialogNavigateButtonPanel extends NavigateButtonPanel
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

        final DialogFunctionDefine this$0;

        public DialogNavigateButtonPanel()
        {
            this$0 = DialogFunctionDefine.this;
            super();
        }
    }

    private class TypePanel extends JPanel
    {

        private void btnUserDefinedActionPerformed(ActionEvent e)
        {
            if(selectedButton != 1)
            {
                userDefinitionPanel.clear();
                setSelectedButton(1);
            }
        }

        private void btnConstantActionPerformed(ActionEvent e)
        {
            if(selectedButton != 0)
            {
                constantFunctionPanel.clear();
                setSelectedButton(0);
            }
        }

        public void setSelectedButton(int button)
        {
            selectedButton = button;
            switch(button)
            {
            case 0: // '\0'
                btnConstant.setSelected(true);
                btnConstant.requestFocus();
                parametersLayout.show(parametersPanel, DialogFunctionDefine.LAYOUTS[0]);
                break;

            case 1: // '\001'
                btnUserDefined.setSelected(true);
                btnUserDefined.requestFocus();
                parametersLayout.show(parametersPanel, DialogFunctionDefine.LAYOUTS[1]);
                break;

            default:
                throw new IllegalArgumentException("Illegal button state");
            }
        }

        public int getSelectedButton()
        {
            return selectedButton;
        }

        private JRadioButton btnConstant;
        private JRadioButton btnUserDefined;
        private int selectedButton;
        final DialogFunctionDefine this$0;




        public TypePanel()
        {
            this$0 = DialogFunctionDefine.this;
            super();
            selectedButton = 0;
            btnConstant = new JRadioButton(DialogFunctionDefine.stringlist.getString("FUNCTION_CONSTANT"));
            btnUserDefined = new JRadioButton(DialogFunctionDefine.stringlist.getString("FUNCTION_USERDEFINED"));
            ButtonGroup buttonGroupType = new ButtonGroup();
            buttonGroupType.add(btnConstant);
            buttonGroupType.add(btnUserDefined);
            btnConstant.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnConstantActionPerformed(evt);
                }

                final DialogFunctionDefine val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            btnUserDefined.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt)
                {
                    btnUserDefinedActionPerformed(evt);
                }

                final DialogFunctionDefine val$this$0;
                final TypePanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            setLayout(new BoxLayout(this, 1));
            add(btnConstant);
            add(btnUserDefined);
            setBorder(new TitledBorder("Type"));
        }
    }

    private class ConstantFunctionPanel extends JPanel
    {

        public void setConstantFunction(ConstantFunction c)
        {
            tfConstant.setValue(new Double(c.getConstant()));
        }

        public ConstantFunction getConstantFunction()
        {
            return new ConstantFunction(((Number)tfConstant.getValue()).doubleValue());
        }

        public void clear()
        {
            setConstantFunction(new ConstantFunction(30D));
        }

        private JFormattedTextField tfConstant;
        final DialogFunctionDefine this$0;

        public ConstantFunctionPanel()
        {
            this$0 = DialogFunctionDefine.this;
            super(new FlowLayout(0));
            tfConstant = new FormattedCalculatorField(0.0D, owner);
            JLabel lblConstant = new JLabel(DialogFunctionDefine.stringlist.getString("FUNCTION_CONSTANT"));
            lblConstant.setLabelFor(tfConstant);
            add(lblConstant);
            add(tfConstant);
            setBorder(new TitledBorder("Parameters"));
        }
    }


    public DialogFunctionDefine(Frame _parent, boolean modal)
    {
        super(_parent, modal);
        parametersLayout = new CardLayout();
        typePanel = new TypePanel();
        constantFunctionPanel = new ConstantFunctionPanel();
        parametersPanel = new JPanel(parametersLayout);
        userDefinitionPanel = new UserDefinedFunctionPanel("User defined function", null, null);
        init();
        setLocationRelativeTo(_parent);
    }

    public DialogFunctionDefine(JDialog _parent, boolean modal)
    {
        super(_parent, modal);
        parametersLayout = new CardLayout();
        typePanel = new TypePanel();
        constantFunctionPanel = new ConstantFunctionPanel();
        parametersPanel = new JPanel(parametersLayout);
        userDefinitionPanel = new UserDefinedFunctionPanel("User defined function", null, null);
        init();
        setLocationRelativeTo(_parent);
    }

    private void init()
    {
        parametersPanel.add(LAYOUTS[0], constantFunctionPanel);
        parametersPanel.add(LAYOUTS[1], userDefinitionPanel);
        typePanel.btnConstant.doClick();
        getContentPane().add(typePanel, "West");
        getContentPane().add(parametersPanel, "Center");
        getContentPane().add(new DialogNavigateButtonPanel(), "South");
        Seamcat.helpBroker.enableHelpKey(super.getRootPane(), helplist.getString(getClass().getName()), null);
        pack();
    }

    public Function getFunction()
    {
        Function f;
        switch(typePanel.getSelectedButton())
        {
        case 0: // '\0'
            f = constantFunctionPanel.getConstantFunction();
            break;

        case 1: // '\001'
            f = userDefinitionPanel.getDiscreteFunction();
            break;

        default:
            throw new IllegalStateException("Illegal function state, cannot determine class");
        }
        return f;
    }

    public void setFunction(Function f)
    {
        if(f instanceof ConstantFunction)
        {
            constantFunctionPanel.setConstantFunction((ConstantFunction)f);
            typePanel.setSelectedButton(0);
        } else
        if(f instanceof DiscreteFunction)
        {
            userDefinitionPanel.setDiscreteFunction((DiscreteFunction)f);
            typePanel.setSelectedButton(1);
        } else
        {
            throw new IllegalArgumentException("Function must be of class ConstantFunction or DiscreteFunction");
        }
    }

    /**
     * @deprecated Method setVisible is deprecated
     */

    public void setVisible()
    {
        throw new UnsupportedOperationException("Call show(Function) or show(String) instead");
    }

    public boolean show(String windowtitle)
    {
        return show(((Function) (new DiscreteFunction())), windowtitle);
    }

    public boolean show(Function function)
    {
        return show(function, "Function definition");
    }

    public boolean show(Function function, String windowtitle)
    {
        setFunction(function);
        setTitle(windowtitle);
        accept = false;
        super.setVisible(true);
        return accept;
    }

    public void setConstantEnabled(boolean value)
    {
        typePanel.btnConstant.setEnabled(value);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/DialogFunctionDefine);
    private static final ResourceBundle stringlist;
    private static final ResourceBundle helplist;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    private static final int CONSTANT = 0;
    private static final int USERDEFINED = 1;
    private static final String LAYOUTS[] = {
        "Constant", "User defined"
    };
    private CardLayout parametersLayout;
    private TypePanel typePanel;
    private ConstantFunctionPanel constantFunctionPanel;
    private JPanel parametersPanel;
    private UserDefinedFunctionPanel userDefinitionPanel;
    private boolean accept;

    static 
    {
        stringlist = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
    }







}
