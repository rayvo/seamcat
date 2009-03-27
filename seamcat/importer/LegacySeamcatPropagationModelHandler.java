// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LegacySeamcatPropagationModelHandler.java

package org.seamcat.importer;

import org.apache.log4j.Logger;
import org.seamcat.distribution.*;
import org.seamcat.propagation.*;

// Referenced classes of package org.seamcat.importer:
//            LegacySeamcatConverter, LegacySeamcatTypes

final class LegacySeamcatPropagationModelHandler
{

    LegacySeamcatPropagationModelHandler()
    {
        pModel = new HataSE24Model();
    }

    public void processLine(String line)
    {
        String param = LegacySeamcatTypes.getParamname(line).toUpperCase();
        if(param.endsWith("_B"))
            b = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_CHECK_MEDIAN_LOSS"))
            medianLoss = LegacySeamcatTypes.toBoolean(line);
        else
        if(param.endsWith("_CHECK_VARIATION"))
            variations = LegacySeamcatTypes.toBoolean(line);
        else
        if(param.endsWith("_DROOM"))
            roomSize = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_GEN_ENV"))
        {
            String data = LegacySeamcatTypes.toString(line).toUpperCase();
            int x = 0;
            do
            {
                if(x >= GENERAL_ENV.length)
                    break;
                if(data.equals(GENERAL_ENV[x]))
                {
                    generalEnv = x;
                    break;
                }
                x++;
            } while(true);
        } else
        if(param.endsWith("_HFLOOR"))
            floorHeight = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_LF"))
            floorLoss = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_PROPAG_ENV"))
        {
            String data = LegacySeamcatTypes.toString(line).toUpperCase();
            int x = 0;
            do
            {
                if(x >= PROPAG_ENV.length)
                    break;
                if(data.equals(PROPAG_ENV[x]))
                {
                    propagEnv = x;
                    break;
                }
                x++;
            } while(true);
        } else
        if(param.endsWith("_PROPAGATION_SELECTION") || param.endsWith("_PROPAGATION"))
        {
            String data = LegacySeamcatTypes.toString(line).toUpperCase();
            if(data.equals("EXTENDED HATA (SRD)"))
            {
                if(!(pModel instanceof HataSE24Model))
                    pModel = new HataSE24Model();
            } else
            if(data.equals("HATA") || data.equals("EXTENDED HATA"))
            {
                if((pModel instanceof HataSE24Model) || !(pModel instanceof HataSE21Model))
                    pModel = new HataSE21Model();
            } else
            if(data.equals("FREE SPACE"))
            {
                if(!(pModel instanceof FreeSpaceModel))
                    pModel = new FreeSpaceModel();
            } else
            if(data.equals("SPHERICAL DIFFRACTION"))
            {
                if(!(pModel instanceof SDModel))
                    pModel = new SDModel();
            } else
            if(data.equals("ITU-R P.1546"))
            {
                if(!(pModel instanceof R370Model))
                    pModel = new R370Model();
            } else
            if(data.equals("USER-DEFINED"))
            {
                LOG.warn("Ignoring user-defined propagationmodel. Setting Ext. hata(SRD)");
                if(!(pModel instanceof HataSE24Model))
                    pModel = new HataSE24Model();
            } else
            {
                throw new IllegalArgumentException((new StringBuilder()).append("Unknown propagation model <").append(data).append(">").toString());
            }
        } else
        if(param.endsWith("_LOCAL_ENV"))
        {
            String data = LegacySeamcatTypes.toString(line).toUpperCase();
            int x = 0;
            do
            {
                if(x >= LOCAL_ENV.length)
                    break;
                if(data.equals(LOCAL_ENV[x]))
                {
                    if(param.endsWith("TX_LOCAL_ENV"))
                        txLocalEnv = x;
                    else
                    if(param.endsWith("RX_LOCAL_ENV"))
                        rxLocalEnv = x;
                    else
                        throw new IllegalArgumentException("Unknown LOCAL_ENV target");
                    break;
                }
                x++;
            } while(true);
        } else
        if(param.endsWith("_SPH_EARTH"))
            earthAdmittance = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_SPH_GRAD"))
            refracIdxGrad = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_SPH_REFRAC"))
            refracProb = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_SPH_WATER"))
            waterConc = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_WL_II"))
            wallLossII = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_WL_IO"))
            wallLossIO = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_WL_STD_DEV_II"))
            wallLossStdDevII = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_WL_STD_DEV_IO"))
            wallLossStdDevIO = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_FS_STD_DEV"))
            fsLossStdDev = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_PT_MIN"))
            timePercDistMin = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_PT_MAX"))
            timePercDistMax = LegacySeamcatTypes.toDouble(line);
        else
        if(param.endsWith("_SYSTEM"))
        {
            String data = LegacySeamcatTypes.toString(line).toUpperCase();
            int x = 0;
            do
            {
                if(x >= SYSTEM_TYPE.length)
                    break;
                if(data.equals(SYSTEM_TYPE[x]))
                {
                    systemType = x;
                    break;
                }
                x++;
            } while(true);
        } else
        if(!param.endsWith("_MEMO_PROPAG"))
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown propagation model parameter <").append(param).append(">").toString());
    }

    public PropagationModel getPropagationModel()
    {
        pModel.setGeneralEnv(generalEnv);
        pModel.setMedianSelected(medianLoss);
        pModel.setPropagEnv(propagEnv);
        pModel.setRxLocalEnv(rxLocalEnv);
        pModel.setTxLocalEnv(txLocalEnv);
        pModel.setVariationsSelected(variations);
        if(pModel instanceof HataAndSDModel)
        {
            HataAndSDModel m = (HataAndSDModel)pModel;
            m.setEmpiricalParameter(b);
            m.setFloorHeight(floorHeight);
            m.setFloorLoss(floorLoss);
            m.setRoomSize(roomSize);
            m.setWiLoss(wallLossII);
            m.setWiStdDev(wallLossStdDevII);
            m.setWeLoss(wallLossIO);
            m.setWeStdDev(wallLossStdDevIO);
            if(pModel instanceof SDModel)
            {
                SDModel n = (SDModel)pModel;
                n.setEarthSurfaceAdmittance(earthAdmittance);
                n.setRefrIndexGradient(refracIdxGrad);
                n.setRefrLayerProb(refracProb);
                n.getTimePercentage().setMin(timePercDistMin);
                n.getTimePercentage().setMax(timePercDistMax);
                n.setWaterCtr(waterConc);
            }
        } else
        if(pModel instanceof R370Model)
        {
            R370Model m = (R370Model)pModel;
            m.setSystemType(systemType);
            m.getTimePercentage().setMin(timePercDistMin);
            m.getTimePercentage().setMax(timePercDistMax);
        } else
        if(pModel instanceof FreeSpaceModel)
        {
            FreeSpaceModel m = (FreeSpaceModel)pModel;
            m.getVariationsDistrib().setStdDev(fsLossStdDev);
        } else
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown propagationmodel <").append(pModel).append(">").toString());
        }
        return pModel;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/importer/LegacySeamcatConverter);
    private static final String GENERAL_ENV[] = {
        "URBAN", "SUBURBAN", "RURAL"
    };
    private static final String PROPAG_ENV[] = {
        "ABOVE ROOF", "BELOW ROOF"
    };
    private static final String LOCAL_ENV[] = {
        "INDOOR", "OUTDOOR"
    };
    private static final String SYSTEM_TYPE[] = {
        "DIGITAL (Bw < 1MHz)", "DIGITAL (Bw > 1MHz)", "ANALOGUE"
    };
    private double b;
    private double roomSize;
    private double floorHeight;
    private double floorLoss;
    private double earthAdmittance;
    private double refracIdxGrad;
    private double refracProb;
    private double waterConc;
    private double wallLossII;
    private double wallLossIO;
    private double wallLossStdDevII;
    private double wallLossStdDevIO;
    private double fsLossStdDev;
    private double timePercDistMin;
    private double timePercDistMax;
    private boolean medianLoss;
    private boolean variations;
    private int generalEnv;
    private int propagEnv;
    private int rxLocalEnv;
    private int txLocalEnv;
    private int systemType;
    private PropagationModel pModel;

}