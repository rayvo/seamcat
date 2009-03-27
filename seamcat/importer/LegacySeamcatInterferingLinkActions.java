// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LegacySeamcatInterferingLinkActions.java

package org.seamcat.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import org.seamcat.function.Function2;
import org.seamcat.function.Point3D;
import org.seamcat.model.*;
import org.seamcat.model.core.*;

// Referenced classes of package org.seamcat.importer:
//            LegacySeamcatPropagationModelHandler, InterferingLinkAction, LegacySeamcatTypes

final class LegacySeamcatInterferingLinkActions
{
    private static class PropagationModelAction
        implements InterferingLinkAction
    {

        public void processLine(String line, BufferedReader reader, InterferenceLink il)
        {
            propagationModelHandler.processLine(line);
            switch(state)
            {
            case 0: // '\0'
                il.getWt2VrPath().setPropagationModel(propagationModelHandler.getPropagationModel());
                break;

            case 1: // '\001'
                il.getInterferingLink().getWt2VrPath().setPropagationModel(propagationModelHandler.getPropagationModel());
                break;
            }
        }

        public static final int IT2VRPATH = 0;
        public static final int IT2WRPATH = 1;
        private final LegacySeamcatPropagationModelHandler propagationModelHandler = new LegacySeamcatPropagationModelHandler();
        private final int state;

        PropagationModelAction(int state)
        {
            if(state == 0 || state == 1)
                this.state = state;
            else
                throw new IllegalArgumentException("State must be IT2VRPATH or IT2WRPATH");
        }
    }


    private LegacySeamcatInterferingLinkActions()
    {
    }

    public static final Map INTERFERING_LINK_ACTIONS;

    static 
    {
        INTERFERING_LINK_ACTIONS = new HashMap();
        InterferingLinkAction itVrPropagationModelAction = new PropagationModelAction(0);
        InterferingLinkAction itWrPropagationModelAction = new PropagationModelAction(1);
        INTERFERING_LINK_ACTIONS.put("ILK_REFERENCE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_DESCRIPTION", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_CHECK_DISTANCE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setUseCorrelatedDistance(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_CORRELATION_MODE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                String data = LegacySeamcatTypes.toString(line).toUpperCase();
                int x = 0;
                do
                {
                    if(x >= LegacySeamcatTypes.CORRELATION_MODES.length)
                        break;
                    if(data.equalsIgnoreCase(LegacySeamcatTypes.CORRELATION_MODES[x]))
                    {
                        il.setCorrelationMode(x);
                        break;
                    }
                    x++;
                } while(true);
                if(x >= LegacySeamcatTypes.CORRELATION_MODES.length)
                    throw new IllegalArgumentException((new StringBuilder()).append("Unknown correlation mode <").append(data).append(">").toString());
                else
                    return;
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_COVERAGE_RADIUS", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setCoverageRadius(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_COVERAGE_RADIUS_MODE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                String data = LegacySeamcatTypes.toString(line).toUpperCase();
                int x = 0;
                do
                {
                    if(x >= LegacySeamcatTypes.COVERAGE_RADIUS_CALCULATION_MODE.length)
                        break;
                    if(data.equalsIgnoreCase(LegacySeamcatTypes.COVERAGE_RADIUS_CALCULATION_MODE[x]))
                    {
                        il.getInterferingLink().getWt2VrPath().setCoverageRadiusCalculationMode(x);
                        break;
                    }
                    x++;
                } while(true);
                if(x == LegacySeamcatTypes.COVERAGE_RADIUS_CALCULATION_MODE.length)
                    throw new IllegalArgumentException((new StringBuilder()).append("Unknown coverage radius calculation mode <").append(data).append(">").toString());
                else
                    return;
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_FREQUENCY", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getInterferingTransmitter().setFrequency(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_MAX_DIST", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setMaximumDistance(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_MIN_DIST", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setMinimumDistance(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_REF_FREQUENCY", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setReferenceTransmitterFrequency(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_REF_POWER", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setReferenceTransmitterPower(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_DESCRIPTION", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_PEAK_GAIN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().getAntenna().setPeakGain(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_REFERENCE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().getAntenna().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_UPPER_BAND", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                throw new UnsupportedOperationException("Not implemeneted");
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_CHECK_HPATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().getAntenna().setUseHorizontalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_CHECK_VPATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().getAntenna().setUseVerticalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_CHECK_SPATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().getAntenna().setUseSphericalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_HOR_PATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getWantedReceiver().getAntenna().setHorizontalPattern(LegacySeamcatTypes.toPattern(line, reader, new HorizontalPattern()));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_VER_PATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getWantedReceiver().getAntenna().setVerticalPattern(LegacySeamcatTypes.toPattern(line, reader, new VerticalPattern()));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_SPH_PATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getWantedReceiver().getAntenna().setSphericalPattern(LegacySeamcatTypes.toPattern(line, reader, new SphericalPattern()));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_AZIMUTH", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().setReceiverToTransmitterAzimuth(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_COVERAGE_RADIUS", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setCoverageRadius(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_DESCRIPTION", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ELEVATION", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().setReceiverToTransmitterElevation(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_REF_ANT_HEIGHT", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setReferenceReceiverAntennaHeight(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_ANT_HEIGHT", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getWantedReceiver().setAntennaHeight(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_SENSITIVITY", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().setSensitivity(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_RX_REFERENCE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWantedReceiver().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ACTIVITY", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getInterferingTransmitter().setActivity(LegacySeamcatTypes.toFunction(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_DESCRIPTION", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_PEAK_GAIN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().getAntenna().setPeakGain(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_REFERENCE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().getAntenna().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_CHECK_HPATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().getAntenna().setUseHorizontalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_CHECK_VPATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().getAntenna().setUseVerticalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_CHECK_SPATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().getAntenna().setUseSphericalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_HOR_PATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getInterferingTransmitter().getAntenna().setHorizontalPattern(LegacySeamcatTypes.toPattern(line, reader, new HorizontalPattern()));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_VER_PATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getInterferingTransmitter().getAntenna().setVerticalPattern(LegacySeamcatTypes.toPattern(line, reader, new VerticalPattern()));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_SPH_PATTERN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getInterferingTransmitter().getAntenna().setSphericalPattern(LegacySeamcatTypes.toPattern(line, reader, new SphericalPattern()));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_AVAILABILITY", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setAvailability(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_FADING", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setFadingStdDev(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_AZIMUTH", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().setTransmitterToReceiverAzimuth(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_BANDWIDTH", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getInterferingTransmitter().setBandwidth(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_REF_BANDWIDTH", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                double value = LegacySeamcatTypes.toDouble(line);
                try
                {
                    List points = il.getInterferingLink().getInterferingTransmitter().getUnwantedEmissions().getPointsList();
                    int i = 0;
                    for(int stop = points.size(); i < stop; i++)
                        ((Point3D)points.get(i)).setRZ(value);

                }
                catch(Exception ex) { }
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_CHECK_NOISE_FLOOR", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setUseUnwantedEmission(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_CHECK_POWER_CONTRO", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setUsePowerControl(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_DENS_ACTIVE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setDensActiveTx(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_DESCRIPTION", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ELEVATION", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().setTransmitterToReceiverElevation(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_NBR_ACTIVE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setNbActiveTx(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_PC_RANGE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setPowerControlRange(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_PC_MIN", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setPowerControlMinimum(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_PC_MAX", new InterferingLinkAction() {

            public void processLine(String s, BufferedReader bufferedreader, InterferenceLink interferencelink)
            {
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_PC_STEP_SIZE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setPowerControlStep(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_POWER_SUPPLIED", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getInterferingTransmitter().setPowerSuppliedDistribution(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_PROB_TRANS", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setTransProb(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_REF_ANT_HEIGHT", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setReferenceTransmitterAntennaHeight(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_ANT_HEIGHT", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getInterferingTransmitter().setAntennaHeight(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_REFERENCE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_TIME", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setTime(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_TRAFFIC_DENSITY", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setUserDensity(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_TRAFFIC_FREQ_CLUSTER", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setFreqCluster(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_TRAFFIC_NBR_CHANNELS", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setNumberOfChannels(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_TRAFFIC_NBR_USERS", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setNumberOfUsersPerChannel(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_UNWANTED(F)", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                Function2 df2 = LegacySeamcatTypes.toFunction2(line, reader);
                if(df2 != null)
                    il.getInterferingLink().getInterferingTransmitter().setUnwantedEmissions(df2);
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_UNWANTED0(F)", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                Function2 df2 = LegacySeamcatTypes.toFunction2(line, reader);
                if(df2 != null)
                    il.getInterferingLink().getInterferingTransmitter().setUnwantedEmissionsFloor(df2);
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_TX_VR_PROTECT_DIST", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.setProtectionDistance(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_SIM_RADIUS", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getInterferingTransmitter().setRsimu(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_B", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_CHECK_MEDIAN_LOSS", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_CHECK_VARIATION", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_DELTAX", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getWt2VrPath().setDeltaX(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_DELTAY", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getWt2VrPath().setDeltaY(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_DROOM", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_GEN_ENV", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_HFLOOR", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_LF", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_MEMO_PROPAG", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_PROPAG_ENV", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_PROPAGATION", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_RX_LOCAL_ENV", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_SPH_EARTH", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_SPH_GRAD", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_SPH_REFRAC", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_SPH_WATER", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_TX_LOCAL_ENV", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_WL_II", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_WL_IO", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_WL_STD_DEV_II", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_WL_STD_DEV_IO", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_FS_STD_DEV", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_PT_MIN", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_PT_MAX", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_SYSTEM", itVrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_LOC_ANGLE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getWt2VrPath().setPathAzimuth(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_VR_LOC_DISTANCE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getWt2VrPath().setPathDistanceFactor(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_B", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_CHECK_MEDIAN_LOSS", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_CHECK_VARIATION", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_DELTAX", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setDeltaX(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_DELTAY", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
            {
                il.getInterferingLink().getWt2VrPath().setDeltaY(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_DROOM", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_GEN_ENV", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_HFLOOR", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_LF", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_LOC_ANGLE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getWt2VrPath().setPathAzimuth(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_LOC_DISTANCE", new InterferingLinkAction() {

            public void processLine(String line, BufferedReader reader, InterferenceLink il)
                throws IOException
            {
                il.getInterferingLink().getWt2VrPath().setPathDistanceFactor(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_MEMO_PROPAG", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_PROPAG_ENV", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_PROPAGATION", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_RX_LOCAL_ENV", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_SPH_EARTH", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_SPH_GRAD", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_SPH_REFRAC", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_SPH_WATER", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_TX_LOCAL_ENV", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_WL_II", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_WL_IO", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_WL_STD_DEV_II", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_WL_STD_DEV_IO", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_FS_STD_DEV", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_PT_MIN", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_PT_MAX", itWrPropagationModelAction);
        INTERFERING_LINK_ACTIONS.put("ILK_WR_SYSTEM", itWrPropagationModelAction);
    }
}