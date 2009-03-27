// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropagationSelectPanel.java

package org.seamcat.presentation.components;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
import org.seamcat.model.propagation.PluginModelWrapper;
import org.seamcat.presentation.propagation.*;
import org.seamcat.propagation.*;

public class PropagationSelectPanel extends JPanel
{
    class SelectionPanel extends JPanel
    {

        public int getSelectedIndex()
        {
            return selector.getSelectedIndex();
        }

        public void setSelectedIndex(int i)
        {
            selector.setSelectedIndex(i);
        }

        private JLabel label;
        private JComboBox selector;
        final PropagationSelectPanel this$0;


        public SelectionPanel()
        {
            this$0 = PropagationSelectPanel.this;
            super();
            label = new JLabel("Propagation Model Selection");
            selector = new JComboBox(PropagationSelectPanel.MODELS);
            selector.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent evt)
                {
                    changePropagationModel();
                }

                final PropagationSelectPanel val$this$0;
                final SelectionPanel this$1;


// JavaClassFileOutputException: Invalid index accessing method local variables table of <init>
            }
);
            add(label);
            add(selector);
        }
    }


    public PropagationSelectPanel(Window parent)
    {
        layout = new CardLayout();
        propagationPanel = new JPanel(layout);
        selectionPanel = new SelectionPanel();
        extendedHata = new ExtendedHataPanel(parent);
        extendedHataSRD = new ExtendedHataSRDPanel(parent);
        spherical = new SDPanel(parent);
        freespace = new FreeSpacePanel(parent);
        itu = new R370Panel(parent);
        plugin = new PluginModelPanel(parent);
        propagationPanel.setLayout(layout);
        propagationPanel.add(MODELS[0], extendedHata);
        propagationPanel.add(MODELS[1], extendedHataSRD);
        propagationPanel.add(MODELS[2], new JScrollPane(spherical));
        propagationPanel.add(MODELS[3], freespace);
        propagationPanel.add(MODELS[4], itu);
        propagationPanel.add(MODELS[5], plugin);
        setPreferredSize(new Dimension(400, 600));
        changePropagationModel();
        setLayout(new BorderLayout());
        add(selectionPanel, "North");
        add(propagationPanel, "Center");
    }

    public void addPropagationChangeListener(ItemListener act)
    {
        selectionPanel.selector.addItemListener(act);
    }

    private void changePropagationModel()
    {
        layout.show(propagationPanel, (String)selectionPanel.selector.getSelectedItem());
    }

    public String getSelectedModelName()
    {
        return MODELS[selectionPanel.getSelectedIndex()];
    }

    public PropagationModel getPropagationModel()
    {
        PropagationModel p;
        switch(selectionPanel.getSelectedIndex())
        {
        case 0: // '\0'
            p = extendedHata.getPropagationable();
            break;

        case 1: // '\001'
            p = extendedHataSRD.getPropagationable();
            break;

        case 2: // '\002'
            p = spherical.getPropagationable();
            break;

        case 3: // '\003'
            p = freespace.getPropagationable();
            break;

        case 4: // '\004'
            p = itu.getPropagationable();
            break;

        case 5: // '\005'
            p = plugin.getPropagationable();
            break;

        default:
            throw new IllegalStateException("Unknown propagationmodel in getPropagationable()");
        }
        return p;
    }

    public void setPropagationModel(PropagationModel p)
    {
        if(p instanceof SDModel)
        {
            spherical.setPropagationable(p);
            selectionPanel.setSelectedIndex(2);
        } else
        if(p instanceof HataSE24Model)
        {
            extendedHataSRD.setPropagationable(p);
            selectionPanel.setSelectedIndex(1);
        } else
        if(p instanceof HataSE21Model)
        {
            extendedHata.setPropagationable(p);
            selectionPanel.setSelectedIndex(0);
        } else
        if(p instanceof FreeSpaceModel)
        {
            freespace.setPropagationable(p);
            selectionPanel.setSelectedIndex(3);
        } else
        if(p instanceof R370Model)
        {
            itu.setPropagationable(p);
            selectionPanel.setSelectedIndex(4);
        } else
        if(p instanceof PluginModelWrapper)
        {
            plugin.setPropagationable(p);
            selectionPanel.setSelectedIndex(5);
        } else
        {
            throw new IllegalStateException((new StringBuilder()).append("Unknown propagationmodel (").append(p.getClass()).append(") in setPropagationable(PropagationModel p)").toString());
        }
    }

    private ExtendedHataPanel extendedHata;
    private ExtendedHataSRDPanel extendedHataSRD;
    private SDPanel spherical;
    private FreeSpacePanel freespace;
    private R370Panel itu;
    private PluginModelPanel plugin;
    private CardLayout layout;
    private JPanel propagationPanel;
    private SelectionPanel selectionPanel;
    public static final String MODELS[] = {
        "Extended Hata", "Extended Hata - SRD", "Spherical Diffraction", "Free Space", "ITU-R P.1546", "Plugin"
    };


}
