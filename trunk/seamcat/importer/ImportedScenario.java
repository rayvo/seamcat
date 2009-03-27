// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ImportedScenario.java

package org.seamcat.importer;

import org.seamcat.model.core.InterferenceLink;
import org.seamcat.model.core.VictimSystemLink;

public class ImportedScenario
{

    public ImportedScenario()
    {
    }

    public InterferenceLink[] getInterferenceLinks()
    {
        return interferenceLinks;
    }

    public void setVictimSystemLink(VictimSystemLink victimSystemLink)
    {
        this.victimSystemLink = victimSystemLink;
    }

    public void setInterferenceLinks(InterferenceLink interferenceLinks[])
    {
        this.interferenceLinks = interferenceLinks;
    }

    public VictimSystemLink getVictimSystemLink()
    {
        return victimSystemLink;
    }

    private InterferenceLink interferenceLinks[];
    private VictimSystemLink victimSystemLink;
}