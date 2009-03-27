// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAOmniDirectionalCell.java

package org.seamcat.cdma;

import org.seamcat.model.Antenna;

// Referenced classes of package org.seamcat.cdma:
//            CDMACell, CDMASystem

public class CDMAOmniDirectionalCell extends CDMACell
{

    public CDMAOmniDirectionalCell(double _locationX, double _locationY, CDMASystem _system, int _cellid, double antHeight, Antenna ant)
    {
        super(_locationX, _locationY, _system, _cellid, antHeight, ant);
    }
}