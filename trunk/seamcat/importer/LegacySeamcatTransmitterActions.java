// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LegacySeamcatTransmitterActions.java

package org.seamcat.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.seamcat.model.Antenna;
import org.seamcat.model.Transmitter;

// Referenced classes of package org.seamcat.importer:
//            TransmitterAction, LegacySeamcatTypes

class LegacySeamcatTransmitterActions
{

    LegacySeamcatTransmitterActions()
    {
    }

    public static final Map TRANSMITTER_ACTIONS;

    static 
    {
        TRANSMITTER_ACTIONS = new HashMap();
        TRANSMITTER_ACTIONS.put("TX_REFERENCE", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_ANT_REFERENCE", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.getAntenna().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("ANT_REFERENCE", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.getAntenna().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_DESCRIPTION", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_POWER_SUPPLIED", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
                throws IOException
            {
                transmitter.setPowerSuppliedDistribution(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_COVERAGE_RADIUS", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setCoverageRadius(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_UNWANTED(F)", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
                throws IOException
            {
                transmitter.setUnwantedEmissions(LegacySeamcatTypes.toFunction2(line, reader));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_UNWANTED0(F)", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
                throws IOException
            {
                transmitter.setUnwantedEmissionsFloor(LegacySeamcatTypes.toFunction2(line, reader));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_PC_STEP_SIZE", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setPowerControlStep(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_PC_MIN", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setPowerControlMinimum(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_PC_RANGE", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setPowerControlRange(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_CHECK_POWER_CONTROL", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setUsePowerControl(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_CHECK_NOISE_FLOOR", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setUseUnwantedEmission(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_TRAFFIC_DENSITY", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setDens(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_TRAFFIC_NBR_CHANNELS", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setNumberOfChannels(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_TRAFFIC_NBR_USERS", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setNumberOfUsersPerChannel(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        TRANSMITTER_ACTIONS.put("TX_TRAFFIC_FREQ_CLUSTER", new TransmitterAction() {

            public void processLine(String line, BufferedReader reader, Transmitter transmitter)
            {
                transmitter.setFreqCluster(LegacySeamcatTypes.toInteger(line));
            }

        }
);
    }
}