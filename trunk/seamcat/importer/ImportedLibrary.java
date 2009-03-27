// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ImportedLibrary.java

package org.seamcat.importer;

import org.seamcat.model.*;

public class ImportedLibrary
{

    public ImportedLibrary()
    {
    }

    public Receiver[] getReceivers()
    {
        return receivers;
    }

    public Antenna[] getAntennas()
    {
        return antennas;
    }

    public void setTransmitters(Transmitter transmitters[])
    {
        this.transmitters = transmitters;
    }

    public void setReceivers(Receiver receivers[])
    {
        this.receivers = receivers;
    }

    public void setAntennas(Antenna antennas[])
    {
        this.antennas = antennas;
    }

    public Transmitter[] getTransmitters()
    {
        return transmitters;
    }

    private Antenna antennas[];
    private Receiver receivers[];
    private Transmitter transmitters[];
}