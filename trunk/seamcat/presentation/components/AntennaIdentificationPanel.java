// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AntennaIdentificationPanel.java

package org.seamcat.presentation.components;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.model.*;
import org.seamcat.presentation.LabeledPairLayout;

// Referenced classes of package org.seamcat.presentation.components:
//            AntennaPanel

public class AntennaIdentificationPanel extends JPanel
{

    public AntennaIdentificationPanel()
    {
        super(new LabeledPairLayout());
        tfName = new JTextField();
        lbDescription = new JLabel(STRINGS.getString("IDENTIFICATION_DESC"));
        tfDescription = new JTextArea(3, 20);
        cbAntennas = new JComboBox(Model.getInstance().getLibrary().getAntennas().createComboBoxModel());
        nameLayout = new CardLayout();
        antennaNamePanel = new JPanel(nameLayout);
        readonly = false;
        JPanel selectAntennaPanel = new JPanel(new GridLayout());
        selectAntennaPanel.add(cbAntennas);
        JPanel enterAntennaPanel = new JPanel(new GridLayout());
        enterAntennaPanel.add(tfName);
        antennaNamePanel.add(enterAntennaPanel, Boolean.FALSE.toString());
        antennaNamePanel.add(selectAntennaPanel, Boolean.TRUE.toString());
        add(new JLabel(STRINGS.getString("IDENTIFICATION_NAME")), "label");
        add(antennaNamePanel, "field");
        add(lbDescription, "label");
        add(new JScrollPane(tfDescription), "field");
        setBorder(new TitledBorder(STRINGS.getString("ANTENNA_IDENTIFICATION_TITLE")));
        cbAntennas.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(antennaPanel != null)
                    try
                    {
                        antennaPanel.setAntenna((Antenna)cbAntennas.getSelectedItem(), readonly);
                        tfDescription.setText(((Antenna)cbAntennas.getSelectedItem()).getDescription());
                    }
                    catch(NullPointerException e1) { }
            }

            final AntennaIdentificationPanel this$0;

            
            {
                this$0 = AntennaIdentificationPanel.this;
                super();
            }
        }
);
    }

    public void setReadonly(boolean _readonly)
    {
        readonly = _readonly;
        nameLayout.show(antennaNamePanel, Boolean.valueOf(readonly).toString());
    }

    public void updateModel(Antenna ant)
    {
        if(!readonly)
        {
            ant.setReference(tfName.getText());
            ant.setDescription(tfDescription.getText());
        }
    }

    public void addAntennaPanel(AntennaPanel antennaPanel)
    {
        this.antennaPanel = antennaPanel;
    }

    public void setAntenna(Antenna antenna, boolean _readonly)
    {
        readonly = _readonly;
        if(readonly)
        {
            cbAntennas.setSelectedItem(antenna);
        } else
        {
            this.antenna = antenna;
            tfName.setText(antenna.getReference());
            tfDescription.setText(antenna.getDescription());
            tfDescription.setEditable(!readonly);
            lbDescription.setEnabled(!readonly);
            nameLayout.show(antennaNamePanel, Boolean.valueOf(readonly).toString());
        }
    }

    public void updateModel(Receiver r)
    {
        if(readonly)
        {
            antenna = (Antenna)cbAntennas.getSelectedItem();
        } else
        {
            antenna.setReference(tfName.getText());
            antenna.setDescription(tfDescription.getText());
        }
        r.setAntenna(antenna);
    }

    public void updateModel(Transmitter t)
    {
        if(readonly)
        {
            antenna = (Antenna)cbAntennas.getSelectedItem();
        } else
        {
            antenna.setReference(tfName.getText());
            antenna.setDescription(tfDescription.getText());
        }
        t.setAntenna(antenna);
    }

    public boolean isReadonly()
    {
        return readonly;
    }

    public String getName()
    {
        try
        {
            if(readonly)
                return ((Antenna)cbAntennas.getSelectedItem()).getReference();
        }
        catch(Exception ex)
        {
            return null;
        }
        return tfName.getText();
    }

    private static final ResourceBundle STRINGS;
    private JTextField tfName;
    private JLabel lbDescription;
    private JTextArea tfDescription;
    private JComboBox cbAntennas;
    private CardLayout nameLayout;
    private JPanel antennaNamePanel;
    private boolean readonly;
    private Antenna antenna;
    private AntennaPanel antennaPanel;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }




}
