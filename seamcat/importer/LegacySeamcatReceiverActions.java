// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LegacySeamcatReceiverActions.java

package org.seamcat.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.seamcat.model.Antenna;
import org.seamcat.model.Receiver;

// Referenced classes of package org.seamcat.importer:
//            ReceiverAction, LegacySeamcatTypes

final class LegacySeamcatReceiverActions
{

    private LegacySeamcatReceiverActions()
    {
    }

    public static final Map RECEIVER_ACTIONS;

    static 
    {
        RECEIVER_ACTIONS = new HashMap();
        RECEIVER_ACTIONS.put("RX_REFERENCE", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_ANT_REFERENCE", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.getAntenna().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        RECEIVER_ACTIONS.put("ANT_REFERENCE", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.getAntenna().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_DESCRIPTION", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_PC_MAX_INCREASE", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setPowerControlMaxThreshold(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_CI", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setProtectionRatio(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_CNI", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setExtendedProtectionRatio(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_NIN", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setNoiseAugmentation(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_IN", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setInterferenceToNoiseRatio(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_NOISE_FLOOR", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
                throws IOException
            {
                receiver.setNoiseFloorDistribution(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_INTERMOD", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
                throws IOException
            {
                receiver.setIntermodulationRejection(LegacySeamcatTypes.toFunction(line, reader));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_BLOCKING", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
                throws IOException
            {
                receiver.setBlockingResponse(LegacySeamcatTypes.toFunction(line, reader));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_CHECK_PC_MAX_INCREASE", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setUsePowerControlThreshold(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_SENSITIVITY", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setSensitivity(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        RECEIVER_ACTIONS.put("RX_BANDWIDTH", new ReceiverAction() {

            public void processLine(String line, BufferedReader reader, Receiver receiver)
            {
                receiver.setReceptionBandwith(LegacySeamcatTypes.toDouble(line));
            }

        }
);
    }
}