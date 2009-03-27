// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMATriSectorCell.java

package org.seamcat.cdma;

import java.util.Iterator;
import java.util.List;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Point2D;
import org.seamcat.model.Antenna;
import org.seamcat.model.core.AntennaPattern;

// Referenced classes of package org.seamcat.cdma:
//            CDMACell, CDMASystem

public class CDMATriSectorCell extends CDMACell
{

    public CDMATriSectorCell(double _locationX, double _locationY, CDMASystem _system, int _cellid, double antHeight, Antenna _antenna, int _sectorid)
    {
        super(_locationX, _locationY, _system, _cellid, antHeight, _antenna);
        sectorid = _sectorid;
        translateAntennaPatternToCurrentSector();
    }

    private void translateAntennaPatternToCurrentSector()
    {
        double sectorOffset = 0.0D;
        if(sectorid == 1)
            sectorOffset = 60D;
        else
        if(sectorid == 2)
            sectorOffset = 180D;
        else
        if(sectorid == 3)
            sectorOffset = 300D;
        else
            throw new IllegalStateException("Unknown sector id");
        List points = antenna.getHorizontalPattern().getPattern().getPointsList();
        Iterator i$ = points.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            Point2D p = (Point2D)i$.next();
            p.setX(p.getX() + sectorOffset);
            if(p.getX() > 180D)
                p.setX(p.getX() - 360D);
        } while(true);
        antenna.getHorizontalPattern().getPattern().sortPoints();
        Point2D first = (Point2D)points.get(0);
        Point2D last = (Point2D)points.get(points.size() - 1);
        if(last.getX() != 180D)
            if(first.getX() == -180D)
            {
                points.add(new Point2D(180D, first.getY()));
            } else
            {
                points.add(new Point2D(180D, last.getY()));
                points.add(new Point2D(-180D, first.getY()));
            }
        antenna.getHorizontalPattern().getPattern().sortPoints();
        first = (Point2D)points.get(0);
        if(first.getX() != -180D)
            points.add(new Point2D(-180D, last.getY()));
        antenna.getHorizontalPattern().getPattern().sortPoints();
        Point2D p = (Point2D)points.get(0);
        for(int i = 1; i < points.size(); i++)
        {
            Point2D p1 = (Point2D)points.get(i);
            if(p.getX() == p1.getX())
            {
                points.remove(i);
                i--;
            }
        }

    }
}