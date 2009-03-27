// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   AntennaDefinitionPanel.java

package org.seamcat.presentation.components;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.seamcat.model.*;

// Referenced classes of package org.seamcat.presentation.components:
//            AntennaPanel, AntennaIdentificationPanel

public class AntennaDefinitionPanel extends JPanel
{

    public AntennaDefinitionPanel(JDialog parent)
    {
        antennaPanel = new AntennaPanel(parent);
        identificationPanel = new AntennaIdentificationPanel();
        identificationPanel.addAntennaPanel(antennaPanel);
        setLayout(new BorderLayout());
        add(identificationPanel, "North");
        add(antennaPanel, "Center");
    }

    public void setAntenna(Antenna antenna, boolean readonly)
    {
        if(antenna != null)
        {
            identificationPanel.setAntenna(antenna, readonly);
            antennaPanel.setAntenna(antenna, readonly);
        }
    }

    public void setReadonly(boolean value)
    {
        identificationPanel.setReadonly(value);
    }

    public void updateModel(Antenna antenna)
    {
        identificationPanel.updateModel(antenna);
        antennaPanel.updateModel(antenna);
    }

    public void updateModel(Transmitter transmitter)
    {
        Antenna a = antennaPanel.getAntenna();
        updateModel(a);
        transmitter.setAntenna(a);
    }

    public void updateModel(Receiver receiver)
    {
        Antenna a = antennaPanel.getAntenna();
        updateModel(a);
        receiver.setAntenna(a);
    }

    public String getName()
    {
        return identificationPanel.getName();
    }

    public void setName(String name)
    {
        identificationPanel.setName(name);
    }

    private AntennaPanel antennaPanel;
    private AntennaIdentificationPanel identificationPanel;
}
