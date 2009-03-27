// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMASystemXmlTags.java

package org.seamcat.cdma.xml;


public interface CDMASystemXmlTags
{

    public static final String CDMASYSTEM = "CdmaSystem";
    public static final String LINKCOMPONENT = "linkComponentDownlink";
    public static final String RECEIVERNOISEFIGURE = "receiverNoiseFigure";
    public static final String HANDOVERMARGIN = "handover";
    public static final String CALLDROPTHRESHOLD = "callDropThreshold";
    public static final String SUCCESSTHRESHOLD = "successThreshold";
    public static final String POWERCONTROL_THRESHOLD = "powerControlThreshold";
    public static final String VOICE_BITRATE = "voiceBitrate";
    public static final String SYSTEM_BANDWITH = "systemBandwith";
    public static final String CELLSTRUCTURE = "cellStructure";
    public static final String NUMBER_OF_TIERS = "number_of_tiers";
    public static final String DISPLACEMENT_ANGLE = "displacementAngle";
    public static final String LOCATION_X = "location_x";
    public static final String LOCATION_Y = "location_y";
    public static final String INTERCELLDISTANCE = "intercell_distance";
    public static final String CELLRADIUS = "cell_radius";
    public static final String VOICE_ACTIVITY_FACTOR = "voice_activity_factor";
    public static final String CELL_TYPE = "cellType";
    public static final String REFERENCE_CELL_SECTOR = "referenceSector";
    public static final String NUMBER_OF_CELLS = "number_of_cells";
    public static final String TARGET_NETWORK_NOISE = "target_noise";
    public static final String FREQUENCY = "frequency";
    public static final String REFERENCE_CELL_ID = "reference_cell_id";
    public static final String USERS_PER_CELL = "users_per_cell";
    public static final String DELTA_USERS_PER_CELL = "delta_users_per_cell";
    public static final String ALLOWABLE_INITIAL_OUTAGE = "allowable-initial-outage";
    public static final String NUMBER_OF_TRIALS = "number_of_trials";
    public static final String SIMULATE_CAPACITY = "simulate_capacity";
    public static final String NETWORK_EDGE = "simulate_network_edge";
    public static final String NETWORK_EDGE_IS_LEFT = "simulate_left_network_edge";
    public static final String VICTIM_SYSTEM = "is_victim_system";
    public static final String INTERFERENCE_FROM_CLUSTER = "interference_from_cluster";
    public static final String BASESTATION = "BaseStation";
    public static final String BS_PILOT_FRACTION = "pilotFraction";
    public static final String BS_OVERHEAD_FRACTION = "overheadFraction";
    public static final String BS_MAX_POWER_FRACTION = "maxPowerFraction";
    public static final String BS_MAX_POWER = "maxPower";
    public static final String BS_POWER_STEP = "powerStep";
    public static final String BS_ANTENNA_HEIGHT = "antenna-height";
    public static final String BS_OMNI_ANTENNA_GAIN = "omni-antenna-gain";
    public static final String BS_INITIAL_POWER = "initialPower";
    public static final String BS_ANTENNA_TRI_SECTOR_ANTENNA = "Tri-Sector-Antenna";
    public static final String BS_ANTENNA_OMNI_ANTENNA = "Omni-Antenna";
    public static final String MOBILESTATION = "MobileStation";
    public static final String MS_POWER_RANGE = "powerRange";
    public static final String MS_MAX_POWER = "maxPower";
    public static final String MS_POWER_STEP = "powerStep";
    public static final String MS_ANTENNA_HEIGHT = "antenna-height";
    public static final String MS_ANTENNA_GAIN = "AntennaGain";
    public static final String MS_MOBILITY = "UserMobility";
    public static final String USER_DISTANCE = "UserDistance";
    public static final String USER_ANGLE = "UserAngle";
    public static final String PROPAGATION_MODEL = "PropagationModel";
    public static final String MINIMUM_COUPLING_LOSS = "minimum_coupling_loss";
}