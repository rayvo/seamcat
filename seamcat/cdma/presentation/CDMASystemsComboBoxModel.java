// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMASystemsComboBoxModel.java

package org.seamcat.cdma.presentation;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import org.seamcat.cdma.CDMASystem;
import org.seamcat.model.Components;
import org.seamcat.model.Workspace;
import org.seamcat.model.core.InterferenceLink;
import org.seamcat.model.core.VictimSystemLink;

public class CDMASystemsComboBoxModel
    implements ComboBoxModel
{

    public CDMASystemsComboBoxModel(Workspace wks)
    {
        systems = new ArrayList();
        if(wks.getVictimSystemLink().isCDMASystem())
        {
            wks.getVictimSystemLink().getCDMASystem().setReference("Victim System");
            systems.add(wks.getVictimSystemLink().getCDMASystem());
        }
        Components intLinks = wks.getInterferenceLinks();
        for(int i = 0; i < intLinks.size(); i++)
        {
            InterferenceLink il = (InterferenceLink)intLinks.get(i);
            if(il.isCDMASystem())
            {
                il.getCDMASystem().setReference((new StringBuilder()).append("Interfering System #").append(i).toString());
                systems.add(il.getCDMASystem());
            }
        }

        if(systems.size() > 0)
            selected = (CDMASystem)systems.get(0);
    }

    public void setSelectedItem(Object _selected)
    {
        selected = (CDMASystem)_selected;
    }

    public Object getSelectedItem()
    {
        return selected;
    }

    public int getSize()
    {
        return systems.size();
    }

    public Object getElementAt(int arg0)
    {
        return systems.get(arg0);
    }

    public void addListDataListener(ListDataListener listdatalistener)
    {
    }

    public void removeListDataListener(ListDataListener listdatalistener)
    {
    }

    private CDMASystem selected;
    private List systems;
}