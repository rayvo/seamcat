// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AntennaAction.java

package org.seamcat.importer;

import java.io.BufferedReader;
import java.io.IOException;
import org.seamcat.model.Antenna;

interface AntennaAction
{

    public abstract void processLine(String s, BufferedReader bufferedreader, Antenna antenna)
        throws IOException;
}