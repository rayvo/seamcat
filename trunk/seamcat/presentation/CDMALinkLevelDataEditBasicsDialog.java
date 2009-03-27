// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CDMALinkLevelDataEditBasicsDialog.java

package org.seamcat.presentation;

import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.cdma.CDMALinkLevelData;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.SpringUtilities;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, SeamcatTextFieldFormats

public class CDMALinkLevelDataEditBasicsDialog extends EscapeDialog
{
    private class ControlButtonPanel extends NavigateButtonPanel
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

        final CDMALinkLevelDataEditBasicsDialog this$0;

        public ControlButtonPanel()
        {
            this$0 = CDMALinkLevelDataEditBasicsDialog.this;
            super();
        }
    }

    private static class PathCaptionPanel extends JPanel
    {

        private void setFields(CDMALinkLevelData data)
        {
            String path1 = data.getPathDescription(1);
            String path2 = data.getPathDescription(2);
            tf1Path.setText(path1 == null ? "" : path1);
            tf2Path.setText(path2 == null ? "" : path2);
        }

        private void getFields(CDMALinkLevelData data)
        {
            data.setPathDescription(1, tf1Path.getText());
            data.setPathDescription(2, tf2Path.getText());
        }

        private final JTextField tf1Path = new JTextField(30);
        private final JTextField tf2Path = new JTextField(30);



        PathCaptionPanel()
        {
            super(new SpringLayout());
            add(new JLabel(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_1PATH")));
            add(tf1Path);
            add(new JLabel(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_2PATH")));
            add(tf2Path);
            setBorder(new TitledBorder(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_PATHCAPTIONS")));
            SpringUtilities.makeCompactGrid(this, 4, 1, 0, 0, 0, 0);
        }
    }

    private class IdentificationPanel extends JPanel
    {

        private void setFields(CDMALinkLevelData data)
        {
            tfSystem.setText(data.getSystem());
            tfSource.setText(data.getSource());
            tfFrequency.setValue(Double.valueOf(data.getFrequency()));
            tfTargetPct.setText(data.getTargetERpct());
            cbTargetType.setSelectedItem(data.getTargetERType());
        }

        private void getFields(CDMALinkLevelData data)
        {
            data.setSystem(tfSystem.getText());
            data.setSource(tfSource.getText());
            data.setFrequency(((Number)tfFrequency.getValue()).doubleValue());
            data.setTargetERpct(tfTargetPct.getText());
            data.setTargetERType((org.seamcat.cdma.CDMALinkLevelData.TargetERType)cbTargetType.getSelectedItem());
        }

        private final JTextField tfSystem = new JTextField(30);
        private final JTextField tfSource = new JTextField(30);
        private final JFormattedTextField tfFrequency;
        private final JTextField tfTargetPct = new JTextField(5);
        private final JComboBox cbTargetType = new JComboBox();
        final CDMALinkLevelDataEditBasicsDialog this$0;



        IdentificationPanel()
        {
            this$0 = CDMALinkLevelDataEditBasicsDialog.this;
            super(new SpringLayout());
            tfFrequency = new FormattedCalculatorField(owner);
            tfSystem.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            tfSource.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            tfTargetPct.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
            org.seamcat.cdma.CDMALinkLevelData.TargetERType arr$[] = org.seamcat.cdma.CDMALinkLevelData.TargetERType.values();
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                org.seamcat.cdma.CDMALinkLevelData.TargetERType t = arr$[i$];
                cbTargetType.addItem(t);
            }

            JPanel targetTypePanel = new JPanel(new FlowLayout(0));
            targetTypePanel.add(new JLabel(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_TARGETTYPE_1")));
            targetTypePanel.add(tfTargetPct);
            targetTypePanel.add(new JLabel(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_TARGETTYPE_2")));
            targetTypePanel.add(cbTargetType);
            add(new JLabel(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_SYSTEM")));
            add(tfSystem);
            add(new JLabel(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_SOURCE")));
            add(tfSource);
            add(new JLabel(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_FREQUENCY")));
            add(tfFrequency);
            add(targetTypePanel);
            setBorder(new TitledBorder(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_IDENTIFICATION")));
            SpringUtilities.makeCompactGrid(this, 7, 1, 0, 0, 0, 0);
        }
    }

    private static class LinkTypePanel extends JPanel
    {

        private void setFields(CDMALinkLevelData data)
        {
            static class _cls1
            {

                static final int $SwitchMap$org$seamcat$cdma$CDMALinkLevelData$LinkType[];

                static 
                {
                    $SwitchMap$org$seamcat$cdma$CDMALinkLevelData$LinkType = new int[org.seamcat.cdma.CDMALinkLevelData.LinkType.values().length];
                    try
                    {
                        $SwitchMap$org$seamcat$cdma$CDMALinkLevelData$LinkType[org.seamcat.cdma.CDMALinkLevelData.LinkType.UPLINK.ordinal()] = 1;
                    }
                    catch(NoSuchFieldError ex) { }
                    try
                    {
                        $SwitchMap$org$seamcat$cdma$CDMALinkLevelData$LinkType[org.seamcat.cdma.CDMALinkLevelData.LinkType.DOWNLINK.ordinal()] = 2;
                    }
                    catch(NoSuchFieldError ex) { }
                }
            }

            switch(_cls1..SwitchMap.org.seamcat.cdma.CDMALinkLevelData.LinkType[data.getLinkType().ordinal()])
            {
            case 1: // '\001'
                jbUplink.doClick();
                break;

            case 2: // '\002'
                jbDownlink.doClick();
                break;

            default:
                throw new IllegalStateException("Unknown link type");
            }
        }

        private void getFields(CDMALinkLevelData data)
        {
            data.setLinkType(jbUplink.isSelected() ? org.seamcat.cdma.CDMALinkLevelData.LinkType.UPLINK : org.seamcat.cdma.CDMALinkLevelData.LinkType.DOWNLINK);
        }

        private final JRadioButton jbUplink;
        private final JRadioButton jbDownlink;



        LinkTypePanel()
        {
            super(new GridLayout(2, 1));
            jbUplink = new JRadioButton(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_UPLINK"));
            jbDownlink = new JRadioButton(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_DOWNLOAD"));
            ButtonGroup bgType = new ButtonGroup();
            bgType.add(jbUplink);
            bgType.add(jbDownlink);
            add(jbUplink);
            add(jbDownlink);
            setBorder(new TitledBorder(CDMALinkLevelDataEditBasicsDialog.STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_LINKTYPE")));
        }
    }


    public CDMALinkLevelDataEditBasicsDialog(JDialog owner)
    {
        super(owner, true);
        this.owner = owner;
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, 3));
        dialogPanel.add(linkTypePanel);
        dialogPanel.add(idPanel);
        dialogPanel.add(pathCaptionPanel);
        getContentPane().add(dialogPanel, "Center");
        getContentPane().add(new ControlButtonPanel(), "South");
        setTitle(STRINGLIST.getString("LIBRARY_CDMA_LLD_BASICS_WINDOWTITLE"));
        pack();
        setResizable(false);
    }

    public boolean showDialog(CDMALinkLevelData data)
    {
        setLocationRelativeTo(owner);
        accept = false;
        setFields(data);
        setVisible(true);
        if(accept)
            getFields(data);
        return accept;
    }

    private void setFields(CDMALinkLevelData data)
    {
        linkTypePanel.setFields(data);
        idPanel.setFields(data);
        pathCaptionPanel.setFields(data);
    }

    private void getFields(CDMALinkLevelData data)
    {
        linkTypePanel.getFields(data);
        idPanel.getFields(data);
        pathCaptionPanel.getFields(data);
    }

    protected static final ResourceBundle STRINGLIST;
    private final LinkTypePanel linkTypePanel = new LinkTypePanel();
    private final IdentificationPanel idPanel = new IdentificationPanel();
    private final PathCaptionPanel pathCaptionPanel = new PathCaptionPanel();
    private final JDialog owner;
    private boolean accept;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }


}
