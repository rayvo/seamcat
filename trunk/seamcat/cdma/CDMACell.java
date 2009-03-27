// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMACell.java

package org.seamcat.cdma;

import java.util.*;
import org.apache.log4j.Logger;
import org.seamcat.cdma.exceptions.ScalingException;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.Antenna;
import org.seamcat.model.technical.exception.PatternException;

// Referenced classes of package org.seamcat.cdma:
//            CDMAElement, CDMALink, CDMADownlink, UserTerminal, 
//            CDMASystem, CDMAUplink

public abstract class CDMACell extends CDMAElement
{

    public CDMACell(double _locationX, double _locationY, CDMASystem _system, int _cellid, double antHeight, Antenna ant)
    {
        super(_locationX, _locationY, antHeight, _system);
        uplinkMode = false;
        cellid = _cellid;
        antenna = ant;
        activeConnections = new ArrayList();
        inActiveConnections = new ArrayList();
        inactiveUsers = new ArrayList();
        droppedUsers = new ArrayList();
        calculateHexagon();
    }

    public double calculateAntennaGainTo(double horizontalAngle, double elevationAngle)
    {
        if(horizontalAngle > 180D)
            horizontalAngle -= 360D;
        double gain = 0.0D;
        try
        {
            gain = antenna.calculateGain(horizontalAngle, elevationAngle);
        }
        catch(PatternException ex)
        {
            LOG.error("An Error occured", ex);
        }
        return gain;
    }

    protected void calculateHexagon()
    {
        int n = 6;
        hexagonPoints = new double[n + 1][2];
        int k = 1;
        for(int z = 0; k <= cdmasystem.getCellStructure(); z++)
        {
            hexagonPoints[k][0] = Mathematics.cosD(z * 60) * cdmasystem.getCellRadius() + locationX;
            hexagonPoints[k][1] = Mathematics.sinD(z * 60) * cdmasystem.getCellRadius() + locationY;
            k++;
        }

    }

    public void resetCell()
    {
        activeConnections.clear();
        inActiveConnections.clear();
        droppedUsers.clear();
        inactiveUsers.clear();
        currentChannelTransmitPower = 0.0D;
        converged = false;
        externalInterferenceUnwanted = -1000D;
        externalInterferenceBlocking = -1000D;
    }

    public double calculateTotalInterference_dBm(CDMAUplink link)
    {
        double internalSystem = CDMASystem.fromdBm2Watt(calculateInterSystemInterference_dBm(link));
        double external = getExternalInterference();
        double thermal = cdmasystem.getThermalNoiseInWatt();
        external = CDMASystem.fromdBm2Watt(external);
        totalInterference = 10D * Math.log10(internalSystem + external + thermal) + 30D;
        if(totalInterference > -85D)
            System.currentTimeMillis();
        return totalInterference;
    }

    public double calculateTotalInterference_Watt(CDMAUplink link)
    {
        return CDMASystem.fromdBm2Watt(calculateTotalInterference_dBm(link));
    }

    public double calculateInterSystemInterference_dBm(CDMAUplink excludelink)
    {
        double values[] = new double[inActiveConnections.size() + activeConnections.size()];
        int index = 0;
        Iterator i$ = inActiveConnections.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            CDMALink link = (CDMALink)i$.next();
            if(link != excludelink)
            {
                double temp = link.calculateCurrentReceivePower_dBm();
                if(temp > -90D)
                    System.currentTimeMillis();
                values[index++] = temp;
            }
        } while(true);
        i$ = activeConnections.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            CDMALink link = (CDMALink)i$.next();
            if(link != excludelink)
            {
                double temp = link.calculateCurrentReceivePower_dBm();
                if(temp > -90D)
                    System.currentTimeMillis();
                values[index++] = temp;
            }
        } while(true);
        double value_excluding_input_link = CDMASystem.powerSummation(values);
        double value_including_all_links = value_excluding_input_link;
        if(excludelink != null)
            value_including_all_links = CDMASystem.powerSummation(new double[] {
                value_excluding_input_link, excludelink.calculateCurrentReceivePower_dBm()
            });
        if(getInterSystemInterference() > value_including_all_links)
            System.currentTimeMillis();
        setInterSystemInterference(value_including_all_links);
        return value_excluding_input_link;
    }

    public double calculateNoiseRiseOverThermalNoise_dB()
    {
        double Nt = cdmasystem.getThermalNoiseInWatt();
        double Itotal = calculateTotalInterference_dBm(null);
        noiseRise = Itotal - CDMASystem.fromWatt2dBm(Nt);
        return noiseRise;
    }

    public double calculateNoiseRiseOverThermalNoise_LinearyFactor()
    {
        noiseRiseLinearyFactor = Math.pow(10D, calculateNoiseRiseOverThermalNoise_dB() / 10D);
        if(noiseRiseLinearyFactor > 10D)
            System.currentTimeMillis();
        return noiseRiseLinearyFactor;
    }

    public void addInActiveConnection(CDMALink link)
    {
        inActiveConnections.add(link);
    }

    public void scaleChannelPower()
        throws ScalingException
    {
        scaleChannelPower(calculateScaling());
    }

    public double calculateOutage()
    {
        double connected = countActiveUsers();
        connected += countInActiveUsers();
        if(connected < 1.0D)
        {
            return 1.0D;
        } else
        {
            double dropped = countDroppedUsers();
            double outage = dropped / (connected + dropped);
            return outage;
        }
    }

    public double getOutagePercentage()
    {
        return calculateOutage() * 100D;
    }

    public void removeHighestTransmittingUser()
    {
        if(activeConnections.size() > 0)
        {
            Collections.sort(activeConnections, highestTransmittingUserLink);
            cdmasystem.dropActiveUser(((CDMALink)activeConnections.get(0)).getUserTerminal());
        }
    }

    public void scaleChannelPower(double scaleFactor)
        throws ScalingException
    {
        if(scaleFactor == 1.0D)
        {
            setConverged(true);
            return;
        }
        if(scaleFactor > 1.0D)
            throw new ScalingException((new StringBuilder()).append("CDMA Power levels cannot be scaled up. [Scale value was: ").append(scaleFactor).append("]").toString());
        for(int i = 0; i < activeConnections.size(); i++)
            ((CDMADownlink)activeConnections.get(i)).scaleTransmitPower(scaleFactor);

        calculateCurrentChannelPower_dBm();
        for(int i = 0; i < activeConnections.size(); i++)
        {
            UserTerminal u = ((CDMALink)activeConnections.get(i)).getUserTerminal();
            try
            {
                u.calculateReceivedPower();
                u.calculateAchievedEcIor();
                if(!u.meetsEcIorRequirement(cdmasystem.getCallDropThreshold()))
                {
                    cdmasystem.dropActiveUser(u);
                    u.setDropReason("Inside Power Balance");
                    i--;
                }
            }
            catch(Exception ex)
            {
                LOG.error(ex);
            }
        }

    }

    public double getAvailableTrafficChannelInWatts()
    {
        double current = getCurrentChannelTransmitPowerInWatts();
        double max = getMaximumChannelPower_Watt();
        return max - current;
    }

    public double calculateScaling()
    {
        double pMax = getMaximumChannelPower_Watt();
        double pCalc = getCurrentChannelTransmitPowerInWatts();
        if(pCalc > pMax)
            return pMax / pCalc;
        else
            return 1.0D;
    }

    public double getCurrentChannelTransmitPowerInWatts()
    {
        return CDMASystem.fromdBm2Watt(getCurrentChannelTransmitPower_dBm());
    }

    public void intializeConnection(CDMALink link)
    {
        activeConnections.add(link);
        inActiveConnections.remove(link);
    }

    public void deinitializeConnection(CDMALink link)
    {
        activeConnections.remove(link);
        inActiveConnections.add(link);
    }

    public void addVoiceInActiveUser(CDMALink user)
    {
        inactiveUsers.add(user);
    }

    public void disconnectUser(CDMALink user)
    {
        if(activeConnections.contains(user))
        {
            droppedUsers.add(user);
            activeConnections.remove(user);
        } else
        {
            inActiveConnections.remove(user);
        }
    }

    public double getOverheadPower_Watt()
    {
        return CDMASystem.fromdBm2Watt(maximumTransmitPower) * getOverheadFraction();
    }

    public double getPilotPower_Watt()
    {
        return CDMASystem.fromdBm2Watt(maximumTransmitPower) * getPilotFraction();
    }

    public double getMaximumChannelPower_dBm()
    {
        return CDMASystem.fromWatt2dBm(getMaximumChannelPower_Watt());
    }

    public double getMaximumChannelPower_Watt()
    {
        return CDMASystem.fromdBm2Watt(maximumTransmitPower) * (1.0D - (getOverheadFraction() + getPilotFraction()));
    }

    public void initializeTransmitPowerLevels()
    {
        double maxPowerInWatts = CDMASystem.fromdBm2Watt(maximumTransmitPower);
        pilotTransmitPower = maxPowerInWatts * pilotFraction;
        overheadTransmitPower = maxPowerInWatts * overheadFraction;
        pilotTransmitPower = CDMASystem.fromWatt2dBm(pilotTransmitPower);
        overheadTransmitPower = CDMASystem.fromWatt2dBm(overheadTransmitPower);
        currentChannelTransmitPower = CDMASystem.fromWatt2dBm(0.0D);
    }

    public double calculateCurrentChannelPower_dBm()
    {
        double result = 0.0D;
        int i = 0;
        for(int stop = activeConnections.size(); i < stop; i++)
        {
            double val = ((CDMADownlink)activeConnections.get(i)).getTransmittedTrafficChannelPowerWatt();
            result += val;
        }

        currentChannelTransmitPower = CDMASystem.fromWatt2dBm(result);
        return currentChannelTransmitPower;
    }

    public double getPilotTransmitPower_dBm()
    {
        return pilotTransmitPower;
    }

    public double getOverheadTransmitPower_dBm()
    {
        return overheadTransmitPower;
    }

    public int countServedUsers()
    {
        return countActiveUsers() + countInActiveUsers();
    }

    public int countActiveUsers()
    {
        int capacity = 0;
        Iterator i$ = activeConnections.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            CDMALink link = (CDMALink)i$.next();
            if(((CDMALink)link.getUserTerminal().getActiveList().get(0)).getCDMACell() == this)
                capacity++;
        } while(true);
        return capacity;
    }

    public int countInActiveUsers()
    {
        int capacity = 0;
        Iterator i$ = inactiveUsers.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            CDMALink link = (CDMALink)i$.next();
            if(((CDMALink)link.getUserTerminal().getActiveList().get(0)).getCDMACell() == this)
                capacity++;
        } while(true);
        return capacity;
    }

    public int countDroppedUsers()
    {
        int capacity = 0;
        Iterator i$ = droppedUsers.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            CDMALink link = (CDMALink)i$.next();
            if(((CDMALink)link.getUserTerminal().getActiveList().get(0)).getCDMACell() == this)
                capacity++;
        } while(true);
        return capacity;
    }

    public void setLocation(double x, double y)
    {
        setLocationX(x);
        setLocationY(y);
        calculateHexagon();
    }

    public void translateLocationX(double factor)
    {
        locationX += factor;
        calculateHexagon();
    }

    public void translateLocationY(double factor)
    {
        locationY += factor;
        calculateHexagon();
    }

    public void setMaximumTransmitPower_dBm(double maximumTransmitPower)
    {
        this.maximumTransmitPower = maximumTransmitPower;
    }

    public void setMaximumChannelPowerFraction(double maximumChannelPowerFraction)
    {
        this.maximumChannelPowerFraction = maximumChannelPowerFraction;
    }

    public void setPilotFraction(double pilotFraction)
    {
        this.pilotFraction = pilotFraction;
    }

    public void setOverheadFraction(double overheadFraction)
    {
        this.overheadFraction = overheadFraction;
    }

    public void setConvergenceValue(double convergenceValue)
    {
        this.convergenceValue = convergenceValue;
    }

    public void setConverged(boolean converged)
    {
        this.converged = converged;
    }

    public void setCellid(int cellid)
    {
        this.cellid = cellid;
    }

    public void setCurrentTransmitPower_dBm(double currentTransmitPower)
    {
        currentChannelTransmitPower = currentTransmitPower;
    }

    public double getMaximumTransmitPower_dBm()
    {
        return maximumTransmitPower;
    }

    public double getMaximumChannelPowerFraction()
    {
        return maximumChannelPowerFraction;
    }

    public double getPilotFraction()
    {
        return pilotFraction;
    }

    public double getOverheadFraction()
    {
        return overheadFraction;
    }

    public int getCellid()
    {
        return cellid;
    }

    public double getCurrentChannelTransmitPower_dBm()
    {
        return currentChannelTransmitPower;
    }

    public List getActiveConnections()
    {
        return activeConnections;
    }

    public double getCurrentTransmitPower()
    {
        double traf = calculateCurrentChannelPower_dBm();
        double traf_W = CDMASystem.fromdBm2Watt(traf);
        double overhead = getOverheadTransmitPower_dBm();
        double overhead_W = CDMASystem.fromdBm2Watt(overhead);
        double pilot = getPilotTransmitPower_dBm();
        double pilot_W = CDMASystem.fromdBm2Watt(pilot);
        double value_W = traf_W + overhead_W + pilot_W;
        double value = CDMASystem.fromWatt2dBm(value_W);
        return value;
    }

    public boolean isInsideCell(double x, double y)
    {
        int n = 6;
        for(int i = 1; i <= n; i++)
        {
            int j = i % n + 1;
            if(hexagonPoints[i][0] * (hexagonPoints[j][1] - y) + hexagonPoints[j][0] * (y - hexagonPoints[i][1]) + x * (hexagonPoints[i][1] - hexagonPoints[j][1]) < 0.0D)
                return false;
        }

        return true;
    }

    public String toString()
    {
        return (new StringBuilder()).append("CDMA Cell # ").append(cellid).append(" at (").append(locationX).append(",").append(locationY).append(")").append(isUplinkMode() ? (new StringBuilder()).append(" - Noise Rise: ").append(noiseRise).append("dB").toString() : "").append(isUplinkMode() ? (new StringBuilder()).append(" - Lineary Noise Rise: ").append(noiseRiseLinearyFactor).toString() : "").toString();
    }

    public double getInitialInterference()
    {
        return initialInterference;
    }

    public void setInitialInterference(double initialInterference)
    {
        this.initialInterference = initialInterference;
    }

    public boolean isUplinkMode()
    {
        return uplinkMode;
    }

    public void setUplinkMode(boolean uplinkMode)
    {
        this.uplinkMode = uplinkMode;
    }

    public List getDroppedUsers()
    {
        return droppedUsers;
    }

    public int getCellLocationId()
    {
        return cellLocationId;
    }

    public void setCellLocationId(int cellLocationId)
    {
        this.cellLocationId = cellLocationId;
    }

    public int getSectorId()
    {
        return sectorid;
    }

    public void setSectorId(int sectorId)
    {
        sectorid = sectorId;
    }

    public void setLocationX(double locationX)
    {
        this.locationX = locationX;
        calculateHexagon();
    }

    public void setLocationY(double locationY)
    {
        this.locationY = locationY;
        calculateHexagon();
    }

    public Antenna getAntenna()
    {
        return antenna;
    }

    public void setAntenna(Antenna antenna)
    {
        this.antenna = antenna;
    }

    protected static final Logger LOG = Logger.getLogger(org/seamcat/cdma/CDMACell);
    protected int cellid;
    protected double currentChannelTransmitPower;
    protected double pilotTransmitPower;
    protected double overheadTransmitPower;
    protected double maximumTransmitPower;
    protected double maximumChannelPowerFraction;
    protected double convergenceValue;
    protected boolean converged;
    protected double pilotFraction;
    protected double overheadFraction;
    protected double initialTrafficPower;
    protected double currentInitialTrafficPower;
    protected double initialInterference;
    protected boolean uplinkMode;
    protected double noiseRise;
    protected double noiseRiseLinearyFactor;
    protected int cellLocationId;
    protected int sectorid;
    protected List activeConnections;
    protected List inActiveConnections;
    protected List inactiveUsers;
    protected List droppedUsers;
    protected double hexagonPoints[][];
    protected Antenna antenna;
    public static Comparator highestTransmittingUser = new Comparator() {

        public int compare(UserTerminal u1, UserTerminal u2)
        {
            if(u1.getCurrentTransmitPowerInWatt() > u2.getCurrentTransmitPowerInWatt())
                return -1;
            return u1.getCurrentTransmitPowerInWatt() >= u2.getCurrentTransmitPowerInWatt() ? 0 : 1;
        }

        public volatile int compare(Object x0, Object x1)
        {
            return compare((UserTerminal)x0, (UserTerminal)x1);
        }

    }
;
    public static Comparator highestTransmittingUserLink = new Comparator() {

        public int compare(CDMALink l1, CDMALink l2)
        {
            UserTerminal u1 = l1.getUserTerminal();
            UserTerminal u2 = l2.getUserTerminal();
            if(u1.getCurrentTransmitPowerInWatt() > u2.getCurrentTransmitPowerInWatt())
                return -1;
            return u1.getCurrentTransmitPowerInWatt() >= u2.getCurrentTransmitPowerInWatt() ? 0 : 1;
        }

        public volatile int compare(Object x0, Object x1)
        {
            return compare((CDMALink)x0, (CDMALink)x1);
        }

    }
;

}