// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LegacySeamcatAntennaActions.java

package org.seamcat.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.seamcat.model.Antenna;
import org.seamcat.model.core.*;

// Referenced classes of package org.seamcat.importer:
//            AntennaAction, LegacySeamcatTypes

final class LegacySeamcatAntennaActions
{

    private LegacySeamcatAntennaActions()
    {
    }

    public static final Map ANTENNA_ACTIONS;

    static 
    {
        ANTENNA_ACTIONS = new HashMap();
        ANTENNA_ACTIONS.put("ANT_REFERENCE", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
            {
                antenna.setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        ANTENNA_ACTIONS.put("ANT_DESCRIPTION", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
            {
                antenna.setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        ANTENNA_ACTIONS.put("ANT_PEAK_GAIN", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
            {
                antenna.setPeakGain(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        ANTENNA_ACTIONS.put("ANT_HOR_PATTERN", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
                throws IOException
            {
                antenna.setHorizontalPattern(LegacySeamcatTypes.toPattern(line, reader, new HorizontalPattern()));
            }

        }
);
        ANTENNA_ACTIONS.put("ANT_VER_PATTERN", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
                throws IOException
            {
                antenna.setVerticalPattern(LegacySeamcatTypes.toPattern(line, reader, new VerticalPattern()));
            }

        }
);
        ANTENNA_ACTIONS.put("ANT_SPH_PATTERN", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
                throws IOException
            {
                antenna.setSphericalPattern(LegacySeamcatTypes.toPattern(line, reader, new SphericalPattern()));
            }

        }
);
        ANTENNA_ACTIONS.put("ANT_CHECK_VPATTERN", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
            {
                antenna.setUseVerticalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        ANTENNA_ACTIONS.put("ANT_CHECK_HPATTERN", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
            {
                antenna.setUseHorizontalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        ANTENNA_ACTIONS.put("ANT_CHECK_SPATTERN", new AntennaAction() {

            public void processLine(String line, BufferedReader reader, Antenna antenna)
            {
                antenna.setUseSphericalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
    }
}