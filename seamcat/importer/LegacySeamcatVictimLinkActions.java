// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LegacySeamcatVictimLinkActions.java

package org.seamcat.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.seamcat.distribution.ConstantDistribution;
import org.seamcat.model.*;
import org.seamcat.model.core.*;

// Referenced classes of package org.seamcat.importer:
//            LegacySeamcatPropagationModelHandler, VictimLinkAction, LegacySeamcatTypes

final class LegacySeamcatVictimLinkActions
{
    private static class PropagationModelAction
        implements VictimLinkAction
    {

        public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
        {
            propagationModelHandler.processLine(line);
            vlk.getWt2VrPath().setPropagationModel(propagationModelHandler.getPropagationModel());
        }

        private final LegacySeamcatPropagationModelHandler propagationModelHandler;

        private PropagationModelAction()
        {
            propagationModelHandler = new LegacySeamcatPropagationModelHandler();
        }

    }


    private LegacySeamcatVictimLinkActions()
    {
    }

    public static final Map VLK_ACTIONS;

    static 
    {
        VLK_ACTIONS = new HashMap();
        VictimLinkAction propagationModel = new PropagationModelAction();
        VLK_ACTIONS.put("VLK_REFERENCE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_REFERENCE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().getAntenna().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_DESCRIPTION", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_ATTENUATION_SELECTION", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                String data = LegacySeamcatTypes.toString(line).toUpperCase();
                int x = 0;
                do
                {
                    if(x >= VictimReceiver.ATTENUATION_MODES.length)
                        break;
                    if(VictimReceiver.ATTENUATION_MODES[x].toUpperCase().equals(data))
                    {
                        vlk.getVictimReceiver().setBlockingAttenuationMode(x);
                        break;
                    }
                    x++;
                } while(true);
            }

        }
);
        VLK_ACTIONS.put("VLK_CHECK_DISTANCE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setUseCorrelatedDistance(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_B", propagationModel);
        VLK_ACTIONS.put("VLK_CHECK_MEDIAN_LOSS", propagationModel);
        VLK_ACTIONS.put("VLK_CHECK_VARIATION", propagationModel);
        VLK_ACTIONS.put("VLK_CHECK_TX", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.setUseWantedTransmitter(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_COVERAGE_RADIUS", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setCoverageRadius(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_COVERAGE_RADIUS_MODE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                String data = LegacySeamcatTypes.toString(line).toUpperCase();
                int x = 0;
                do
                {
                    if(x >= LegacySeamcatTypes.COVERAGE_RADIUS_CALCULATION_MODE.length)
                        break;
                    if(data.equalsIgnoreCase(LegacySeamcatTypes.COVERAGE_RADIUS_CALCULATION_MODE[x]))
                    {
                        vlk.getWt2VrPath().setCoverageRadiusCalculationMode(x);
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
        VLK_ACTIONS.put("VLK_DELTAX", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setDeltaX(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_DELTAY", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setDeltaY(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_DROOM", propagationModel);
        VLK_ACTIONS.put("VLK_DRSS", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.setDRSS(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_FREQUENCY", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                switch(line.indexOf("Random"))
                {
                case -1: 
                    vlk.setFrequency(new ConstantDistribution(LegacySeamcatTypes.toDouble(line)));
                    break;

                default:
                    vlk.setFrequency(LegacySeamcatTypes.toDistribution(line, reader));
                    break;
                }
            }

        }
);
        VLK_ACTIONS.put("VLK_REF_FREQUENCY", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setReferenceTransmitterFrequency(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_GEN_ENV", propagationModel);
        VLK_ACTIONS.put("VLK_HFLOOR", propagationModel);
        VLK_ACTIONS.put("VLK_LF", propagationModel);
        VLK_ACTIONS.put("VLK_LOC_ANGLE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWt2VrPath().setPathAzimuth(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_LOC_DISTANCE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWt2VrPath().setPathDistanceFactor(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_MAX_DIST", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setMaximumDistance(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_MEMO_PROPAG", propagationModel);
        VLK_ACTIONS.put("VLK_MIN_DIST", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setMinimumDistance(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_PROPAG_ENV", propagationModel);
        VLK_ACTIONS.put("VLK_PROPAGATION_SELECTION", propagationModel);
        VLK_ACTIONS.put("VLK_REF_POWER", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setReferenceTransmitterPower(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_CHECK_HPATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().getAntenna().setUseHorizontalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_BANDWIDTH", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setReceptionBandwith(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_CHECK_VPATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().getAntenna().setUseVerticalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_CHECK_SPATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().getAntenna().setUseSphericalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_DESCRIPTION", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().getAntenna().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_HEIGHT", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getVictimReceiver().setAntennaHeight(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_HOR_PATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getVictimReceiver().getAntenna().setHorizontalPattern(LegacySeamcatTypes.toPattern(line, reader, new HorizontalPattern()));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_PEAK_GAIN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().getAntenna().setPeakGain(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_VER_PATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getVictimReceiver().getAntenna().setVerticalPattern(LegacySeamcatTypes.toPattern(line, reader, new VerticalPattern()));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ANT_SPH_PATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getVictimReceiver().getAntenna().setSphericalPattern(LegacySeamcatTypes.toPattern(line, reader, new SphericalPattern()));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_AZIMUTH", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.setReceiverToTransmitterAzimuth(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_BLOCKING", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getVictimReceiver().setBlockingResponse(LegacySeamcatTypes.toFunction(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_CHECK_PC_MAX", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setUsePowerControlThreshold(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_CI", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setProtectionRatio(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_IN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setInterferenceToNoiseRatio(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_CNI", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setExtendedProtectionRatio(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_DESCRIPTION", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_ELEVATION", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.setReceiverToTransmitterElevation(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_INTERMOD", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getVictimReceiver().setIntermodulationRejection(LegacySeamcatTypes.toFunction(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_LOCAL_ENV", propagationModel);
        VLK_ACTIONS.put("VLK_RX_NIN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setNoiseAugmentation(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_NOISE_FLOOR", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getVictimReceiver().setNoiseFloorDistribution(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_PC_MAX_INCREASE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setPowerControlMaxThreshold(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_REF_ANT_HEIGHT", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setReferenceReceiverAntennaHeight(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_REFERENCE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_SENSITIVITY", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getVictimReceiver().setSensitivity(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_RX_SPH_EARTH", propagationModel);
        VLK_ACTIONS.put("VLK_RX_GRAD", propagationModel);
        VLK_ACTIONS.put("VLK_RX_REFRAC", propagationModel);
        VLK_ACTIONS.put("VLK_RX_WATER", propagationModel);
        VLK_ACTIONS.put("VLK_TX_ANT_DESCRIPTION", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().getAntenna().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_HEIGHT", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWantedTransmitter().setAntennaHeight(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_PEAK_GAIN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().getAntenna().setPeakGain(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_REFERENCE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().getAntenna().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_REFERENCE", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().setReference(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_CHECK_HPATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWantedTransmitter().getAntenna().setUseHorizontalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_CHECK_VPATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().getAntenna().setUseVerticalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_CHECK_SPATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().getAntenna().setUseSphericalPattern(LegacySeamcatTypes.toBoolean(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_HOR_PATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWantedTransmitter().getAntenna().setHorizontalPattern(LegacySeamcatTypes.toPattern(line, reader, new HorizontalPattern()));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_VER_PATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWantedTransmitter().getAntenna().setVerticalPattern(LegacySeamcatTypes.toPattern(line, reader, new VerticalPattern()));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ANT_SPH_PATTERN", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWantedTransmitter().getAntenna().setSphericalPattern(LegacySeamcatTypes.toPattern(line, reader, new SphericalPattern()));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_AZIMUTH", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.setTransmitterToReceiverAzimuth(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_DESCRIPTION", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().setDescription(LegacySeamcatTypes.toString(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_ELEVATION", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.setTransmitterToReceiverElevation(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_LOCAL_ENV", propagationModel);
        VLK_ACTIONS.put("VLK_TX_POWER_SUPPLIED", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWantedTransmitter().setPowerSuppliedDistribution(LegacySeamcatTypes.toDistribution(line, reader));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_AVAILABILITY", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setAvailability(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_FADING", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWt2VrPath().setFadingStdDev(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_REF_ANT_HEIGHT", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
                throws IOException
            {
                vlk.getWt2VrPath().setReferenceTransmitterAntennaHeight(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_REF_FREQUENCY", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().setRefFrequency(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_TRAFFIC_DENSITY", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().setUserDensity(LegacySeamcatTypes.toDouble(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_TRAFFIC_FREQ_CLUSTER", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().setFreqCluster(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_TRAFFIC_NBR_CHANNELS", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().setNumberOfChannels(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_TX_TRAFFIC_NBR_USERS", new VictimLinkAction() {

            public void processLine(String line, BufferedReader reader, VictimSystemLink vlk)
            {
                vlk.getWantedTransmitter().setNumberOfUsersPerChannel(LegacySeamcatTypes.toInteger(line));
            }

        }
);
        VLK_ACTIONS.put("VLK_WL_II", propagationModel);
        VLK_ACTIONS.put("VLK_WL_IO", propagationModel);
        VLK_ACTIONS.put("VLK_WL_STD_DEV_II", propagationModel);
        VLK_ACTIONS.put("VLK_WL_STD_DEV_IO", propagationModel);
        VLK_ACTIONS.put("VLK_FS_STD_DEV", propagationModel);
        VLK_ACTIONS.put("VLK_PT_MIN", propagationModel);
        VLK_ACTIONS.put("VLK_PT_MAX", propagationModel);
        VLK_ACTIONS.put("VLK_SYSTEM", propagationModel);
    }
}