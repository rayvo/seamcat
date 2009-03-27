// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Scenario.java

package org.seamcat.model;

import java.util.Enumeration;
import org.seamcat.model.core.InterferenceLink;
import org.seamcat.model.core.VictimSystemLink;
import org.seamcat.presentation.Node;
import org.seamcat.presentation.components.LocalComponent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model:
//            SeamcatComponent, NodeAttribute, Workspace, Components

public class Scenario extends SeamcatComponent
{

    public Scenario(Workspace wks)
    {
        this.wks = wks;
    }

    public String toString()
    {
        return "Scenario";
    }

    public Enumeration children()
    {
        return null;
    }

    public Node getChildAt(int childIndex)
    {
        Node child;
        if(childIndex == 0)
            child = new LocalComponent(wks.getVictimSystemLink(), (new StringBuilder()).append("Victim Link: ").append(wks.getVictimSystemLink().getReference()).toString());
        else
        if(childIndex > 0 && childIndex <= wks.getInterferenceLinks().size())
            child = new LocalComponent((SeamcatComponent)wks.getInterferenceLinks().get(childIndex - 1), (new StringBuilder()).append("Interfering Link ").append(childIndex).append(": ").append(((InterferenceLink)wks.getInterferenceLinks().get(childIndex - 1)).getReference()).toString());
        else
            throw new IllegalArgumentException((new StringBuilder()).append("ChildIndex out of range <").append(childIndex).append(">").toString());
        return child;
    }

    public int getChildCount()
    {
        return wks.getInterferenceLinks().size() + 1;
    }

    public Node getParent()
    {
        return wks;
    }

    public boolean isLeaf()
    {
        return false;
    }

    protected void initNodeAttributes()
    {
        nodeAttributes = new NodeAttribute[0];
    }

    public Element toElement(Document doc)
    {
        return null;
    }

    private Workspace wks;
}