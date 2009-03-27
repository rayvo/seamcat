// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMALink.java

package org.seamcat.cdma;

import java.util.Comparator;
import org.apache.log4j.Logger;
import org.seamcat.cdma.exceptions.PowerSummationException;
import org.seamcat.mathematics.Mathematics;

// Referenced classes of package org.seamcat.cdma:
//            CDMAElement, UserTerminal, CDMACell, CDMASystem

public abstract class CDMALink
{

    public CDMALink(CDMACell _cell, UserTerminal _user, CDMASystem _cdmasystem)
    {
        distance = 0.0D;
        angle = 0.0D;
        elevation = 0.0D;
        pathLoss = 0.0D;
        minimumCouplingLoss = 0.0D;
        userAntGain = 0.0D;
        bsAntGain = 0.0D;
        powerScaledDownToMax = false;
        user = _user;
        cell = _cell;
        cdmasystem = _cdmasystem;
        distance = calculateDistance(cell, user);
        userAntGain = user.calculateAntennaGainTo(0.0D, 0.0D);
        bsAntGain = cell.calculateAntennaGainTo(angle, elevation);
        minimumCouplingLoss = cdmasystem.getMinimumCouplingLoss();
    }

    public abstract double calculateTotalTransmitPower(double d);

    public abstract double calculateReceivePower()
        throws PowerSummationException;

    public abstract void determinePathLoss();

    public abstract String toString();

    public double calculateCurrentReceivePower_dBm()
    {
        totalTransmitPower = user.getCurrentTransmitPowerIndBm();
        double result = (totalTransmitPower - getEffectivePathloss()) + bsAntGain + userAntGain;
        setReceivePower_dB(result);
        return result;
    }

    public double calculateDistance(CDMAElement cell, UserTerminal user)
    {
        double x = user.getLocationX();
        double y = user.getLocationY();
        double a = cell.getLocationX();
        double b = cell.getLocationY();
        double D = cdmasystem.getInterCellDistance();
        boolean networkEdge = cdmasystem.isSimulateNetworkEdge();
        boolean leftSide = cdmasystem.isNetworkEdgeLeftSide();
        double x_dist = a;
        double y_dist = b;
        boolean isWrapAround = false;
        double distance = Math.sqrt((x - a) * (x - a) + (y - b) * (y - b));
        if(!networkEdge || networkEdge && leftSide)
        {
            double temp = Math.sqrt((x - (a + (3D * D) / SQ3)) * (x - (a + (3D * D) / SQ3)) + (y - (b + 4D * D)) * (y - (b + 4D * D)));
            if(temp < distance)
            {
                distance = temp;
                x_dist = a + (3D * D) / SQ3;
                y_dist = b + 4D * D;
                isWrapAround = true;
            }
        }
        if(!networkEdge || networkEdge && !leftSide)
        {
            double temp = Math.sqrt((x - (a - (3D * D) / SQ3)) * (x - (a - (3D * D) / SQ3)) + (y - (b - 4D * D)) * (y - (b - 4D * D)));
            if(temp < distance)
            {
                distance = temp;
                x_dist = a - (3D * D) / SQ3;
                y_dist = b - 4D * D;
                isWrapAround = true;
            }
        }
        if(!networkEdge || networkEdge && leftSide)
        {
            double temp = Math.sqrt((x - (a + (4.5D * D) / SQ3)) * (x - (a + (4.5D * D) / SQ3)) + (y - (b - (7D * D) / 2D)) * (y - (b - (7D * D) / 2D)));
            if(temp < distance)
            {
                distance = temp;
                x_dist = a + (4.5D * D) / SQ3;
                y_dist = b - (7D * D) / 2D;
                isWrapAround = true;
            }
        }
        if(!networkEdge || networkEdge && !leftSide)
        {
            double temp = Math.sqrt((x - (a - (4.5D * D) / SQ3)) * (x - (a - (4.5D * D) / SQ3)) + (y - (b + 7D * D)) * (y - (b + 7D * D)));
            if(temp < distance)
            {
                distance = temp;
                x_dist = a - (4.5D * D) / SQ3;
                y_dist = b + 7D * D;
                isWrapAround = true;
            }
        }
        if(!networkEdge || networkEdge && leftSide)
        {
            double temp = Math.sqrt((x - (a + (7.5D * D) / SQ3)) * (x - (a + (7.5D * D) / SQ3)) + (y - (b + D / 2D)) * (y - (b + D / 2D)));
            if(temp < distance)
            {
                distance = temp;
                x_dist = a + (7.5D * D) / SQ3;
                y_dist = b + D / 2D;
                isWrapAround = true;
            }
        }
        if(!networkEdge || networkEdge && !leftSide)
        {
            double temp = Math.sqrt((x - (a - (7.5D * D) / SQ3)) * (x - (a - (7.5D * D) / SQ3)) + (y - (b - D / 2D)) * (y - (b - D / 2D)));
            if(temp < distance)
            {
                distance = temp;
                x_dist = a - (7.5D * D) / SQ3;
                y_dist = b - D / 2D;
                isWrapAround = true;
            }
        }
        usingWrapAround = isWrapAround;
        if(isWrapAround)
            System.currentTimeMillis();
        calculateAngle(x, y, x_dist, y_dist);
        calculateElevation(x, y, cell.getAntennaHeight(), x_dist, y_dist, user.getAntennaHeight());
        return distance;
    }

    private double calculateAngle(CDMAElement user, CDMAElement cell)
    {
        return calculateAngle(user.getLocationX(), user.getLocationY(), cell.getLocationX(), cell.getLocationY());
    }

    public double calculateAngle(double x, double y, double a, double b)
    {
        angle = calculateKartesianAngle(x, y, a, b);
        return angle;
    }

    public static double calculateKartesianAngle(CDMAElement user, CDMAElement cell)
    {
        return calculateKartesianAngle(cell.getLocationX(), cell.getLocationY(), user.getLocationX(), user.getLocationY());
    }

    public static double calculateKartesianAngle(double x, double y, double a, double b)
    {
        double linearyDistance = Math.sqrt((x - a) * (x - a) + (y - b) * (y - b));
        double yProjection = Math.abs(b - y);
        double angle = Mathematics.asinD(yProjection / linearyDistance);
        if(x < a && y > b)
            angle = 180D - angle;
        else
        if(x < a && y < b)
            angle += 180D;
        else
        if(x > a && y < b)
            angle = 360D - angle;
        return angle;
    }

    public static double calculateElevation(CDMAElement cell, CDMAElement user)
    {
        double x = user.getLocationX();
        double y = user.getLocationY();
        double a = cell.getLocationX();
        double b = cell.getLocationY();
        double cellAntHeight = cell.getAntennaHeight();
        double userAntHeight = user.getAntennaHeight();
        return calculateElevation(x, y, cellAntHeight, a, b, userAntHeight);
    }

    public static double calculateElevation(double x, double y, double h1, double a, 
            double b, double h2)
    {
        double distance = Math.sqrt((x - a) * (x - a) + (y - b) * (y - b));
        return Mathematics.atanD((h1 - h2) / (distance * 1000D));
    }

    public double getReceivePower_dB()
    {
        return totalReceivedPower;
    }

    public void setReceivePower_dB(double receivePower)
    {
        totalReceivedPower = receivePower;
    }

    public boolean isPowerScaledDownToMax()
    {
        return powerScaledDownToMax;
    }

    public void setPowerScaledDownToMax(boolean powerScaledDownToMax)
    {
        this.powerScaledDownToMax = powerScaledDownToMax;
        powerScaledBy = "CDMALink.setPowerScaledDownToMax";
    }

    public double getBsAntGain()
    {
        return bsAntGain;
    }

    public void setBsAntGain(double bsAntGain)
    {
        this.bsAntGain = bsAntGain;
    }

    public double getUserAntGain()
    {
        return userAntGain;
    }

    public void setUserAntGain(double userAntGain)
    {
        this.userAntGain = userAntGain;
    }

    public double getAngle()
    {
        return angle;
    }

    public void setAngle(double angle)
    {
        this.angle = angle;
    }

    public double getEffectivePathloss()
    {
        return Math.max(getPathLoss(), minimumCouplingLoss);
    }

    public void initializeConnection()
    {
        cell.intializeConnection(this);
    }

    public void deinitilizeConnection()
    {
        cell.deinitializeConnection(this);
    }

    public void connectToInActiveBaseStation()
    {
        cell.addInActiveConnection(this);
    }

    public void disconnect()
    {
        cell.disconnectUser(this);
    }

    public double getMaximumReceivedPower()
    {
        return cell.getMaximumChannelPower_dBm() - getPathLoss();
    }

    public double getDistance()
    {
        return distance;
    }

    public double getPathLoss()
    {
        return pathLoss;
    }

    public double getMinimumCouplingLoss()
    {
        return minimumCouplingLoss;
    }

    public double getTotalTransmitPowerIndBm()
    {
        return totalTransmitPower;
    }

    public double getTransmitPowerInWatt()
    {
        return CDMASystem.fromdBm2Watt(totalTransmitPower);
    }

    public abstract CDMACell getCDMACell();

    public UserTerminal getUserTerminal()
    {
        return user;
    }

    public void setPathLoss(double _pathloss)
    {
        pathLoss = _pathloss;
    }

    public void setMinimumCouplingLoss(double minimumCouplingLoss)
    {
        this.minimumCouplingLoss = minimumCouplingLoss;
    }

    public void setTransmitPowerIndBm(double _transmitPower)
    {
        totalTransmitPower = _transmitPower;
    }

    public double getElevation()
    {
        return elevation;
    }

    public void setElevation(double elevation)
    {
        this.elevation = elevation;
    }

    public boolean isUsingWrapAround()
    {
        return usingWrapAround;
    }

    public void setUsingWrapAround(boolean usingWrapAround)
    {
        this.usingWrapAround = usingWrapAround;
    }

    public String getPowerScaledBy()
    {
        return powerScaledBy;
    }

    public void setPowerScaledBy(String powerScaledBy)
    {
        this.powerScaledBy = powerScaledBy;
    }

    protected static final Logger LOG = Logger.getLogger(org/seamcat/cdma/CDMALink);
    protected UserTerminal user;
    protected CDMACell cell;
    protected CDMASystem cdmasystem;
    protected double totalTransmitPower;
    protected double totalReceivedPower;
    protected double distance;
    protected double angle;
    protected double elevation;
    protected double pathLoss;
    protected double minimumCouplingLoss;
    protected double userAntGain;
    protected double bsAntGain;
    private boolean usingWrapAround;
    private static final double SQ3 = Math.sqrt(3D);
    protected boolean powerScaledDownToMax;
    protected String powerScaledBy;
    public static Comparator CDMALinkDistanceComparator = new Comparator() {

        public boolean equals(Object obj)
        {
            return false;
        }

        public int compare(Object o1, Object o2)
        {
            return (int)(((CDMALink)o1).getDistance() - ((CDMALink)o2).getDistance());
        }

    }
;
    public static Comparator CDMALinkPathlossComparator = new Comparator() {

        public boolean equals(Object obj)
        {
            return false;
        }

        public int compare(CDMALink l1, CDMALink l2)
        {
            return l1.getPathLoss() - l1.getBsAntGain() - l1.getUserAntGain() <= l2.getPathLoss() - l2.getBsAntGain() - l2.getUserAntGain() ? 0 : 1;
        }

        public volatile int compare(Object x0, Object x1)
        {
            return compare((CDMALink)x0, (CDMALink)x1);
        }

    }
;

}