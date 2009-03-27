// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:23 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMALinkLevelData.java

package org.seamcat.cdma;

import java.util.*;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import org.seamcat.cdma.presentation.CDMAEditModel;
import org.seamcat.model.SeamcatComponent;
import org.w3c.dom.*;

// Referenced classes of package org.seamcat.cdma:
//            CDMALinkLevelDataPoint, NoGeometryException

public class CDMALinkLevelData extends SeamcatComponent
{
    public static final class TargetERType extends Enum
    {

        public static TargetERType[] values()
        {
            return (TargetERType[])$VALUES.clone();
        }

        public static TargetERType valueOf(String name)
        {
            return (TargetERType)Enum.valueOf(org/seamcat/cdma/CDMALinkLevelData$TargetERType, name);
        }

        public static final TargetERType FER;
        public static final TargetERType BLER;
        private static final TargetERType $VALUES[];

        static 
        {
            FER = new TargetERType("FER", 0);
            BLER = new TargetERType("BLER", 1);
            $VALUES = (new TargetERType[] {
                FER, BLER
            });
        }

        private TargetERType(String s, int i)
        {
            super(s, i);
        }
    }

    public static final class LinkType extends Enum
    {

        public static LinkType[] values()
        {
            return (LinkType[])$VALUES.clone();
        }

        public static LinkType valueOf(String name)
        {
            return (LinkType)Enum.valueOf(org/seamcat/cdma/CDMALinkLevelData$LinkType, name);
        }

        public static final LinkType UPLINK;
        public static final LinkType DOWNLINK;
        private static final LinkType $VALUES[];

        static 
        {
            UPLINK = new LinkType("UPLINK", 0);
            DOWNLINK = new LinkType("DOWNLINK", 1);
            $VALUES = (new LinkType[] {
                UPLINK, DOWNLINK
            });
        }

        private LinkType(String s, int i)
        {
            super(s, i);
        }
    }


    public CDMALinkLevelData()
    {
        linkType = LinkType.DOWNLINK;
        currentPath = 1;
        dataPoints = new ArrayList();
        pathDescription = new String[3];
        sorted = false;
        geometryPrecision = 3;
        setPathDescription(1, "");
        setPathDescription(2, "");
        setFrequency(0.0D);
        setSystem("");
        setSource("");
        setTargetERpct("1");
        setTargetERType(TargetERType.FER);
    }

    public CDMALinkLevelData(Element element)
    {
        this();
        String linkType = element.getAttribute("system-type");
        if(linkType.equals("uplink"))
            this.linkType = LinkType.UPLINK;
        else
        if(linkType.equals("downlink"))
            this.linkType = LinkType.DOWNLINK;
        else
            throw new IllegalArgumentException((new StringBuilder()).append("system-type must be uplink or downlink (").append(linkType).append(")").toString());
        source = element.getAttribute("source");
        system = element.getAttribute("system");
        frequency = Double.parseDouble(element.getAttribute("frequency"));
        targetERpct = element.getAttribute("targetPct");
        String targetERType = element.getAttribute("targetType");
        if(targetERType.equals(TargetERType.BLER.toString()))
            this.targetERType = TargetERType.BLER;
        else
        if(targetERType.equals(TargetERType.FER.toString()))
            this.targetERType = TargetERType.FER;
        else
            throw new IllegalArgumentException((new StringBuilder()).append("targetERType must be FER or BLER (").append(this.targetERType).append(")").toString());
        NodeList paths = element.getElementsByTagName("path");
        int i = 0;
        for(int stop = paths.getLength(); i < stop; i++)
        {
            Element path = (Element)paths.item(i);
            int pathNo = Integer.parseInt(path.getAttribute("no"));
            setPathDescription(pathNo, path.getAttribute("caption"));
            NodeList points = path.getElementsByTagName("point");
            int j = 0;
            for(int _stop = points.getLength(); j < _stop; j++)
            {
                CDMALinkLevelDataPoint point = new CDMALinkLevelDataPoint((Element)points.item(j), frequency, pathNo);
                addDataPoint(point);
            }

        }

    }

    public CDMALinkLevelData(CDMALinkLevelData tmp)
    {
        this();
        currentPath = tmp.currentPath;
        description = tmp.description;
        frequency = tmp.frequency;
        name = tmp.name;
        source = tmp.source;
        system = tmp.system;
        targetERpct = tmp.targetERpct;
        linkType = tmp.linkType;
        for(int x = 1; x <= 2; x++)
            pathDescription[x] = tmp.pathDescription[x];

        CDMALinkLevelDataPoint point;
        for(Iterator i$ = tmp.dataPoints.iterator(); i$.hasNext(); addDataPoint(new CDMALinkLevelDataPoint(point)))
            point = (CDMALinkLevelDataPoint)i$.next();

    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(system == null ? "" : system);
        sb.append(" : ");
        sb.append(source == null ? "" : source);
        sb.append(" : ");
        sb.append(frequency);
        sb.append(" MHz : ");
        sb.append(linkType != LinkType.UPLINK ? "downlink" : "uplink");
        sb.append(" : ");
        sb.append(targetERpct);
        sb.append("% ");
        sb.append(targetERType.toString());
        return sb.toString();
    }

    public void addDataPoint(CDMALinkLevelDataPoint point)
    {
        if(!dataPoints.contains(point))
        {
            dataPoints.add(point);
            sorted = false;
        }
    }

    public void sort()
    {
        if(!sorted)
        {
            Collections.sort(dataPoints, DATAPOINT_COMPARATOR_DESCENDING_GEOMETRY);
            sorted = true;
        }
    }

    public CDMALinkLevelDataPoint getLinkLevelDataPoint(CDMALinkLevelDataPoint keypoint)
    {
        if(dataPoints.size() < 1)
            throw new IllegalStateException("No Link Level Data Points found");
        if(!sorted)
            sort();
        int index = Collections.binarySearch(dataPoints, keypoint, DATAPOINT_COMPARATOR_DESCENDING_GEOMETRY);
        if(index > 0 && index < dataPoints.size())
            return (CDMALinkLevelDataPoint)dataPoints.get(index);
        CDMALinkLevelDataPoint data[] = getDataSet(keypoint);
        CDMALinkLevelDataPoint p1 = null;
        CDMALinkLevelDataPoint p2 = null;
        if(data.length == 0)
        {
            if(LOG.isDebugEnabled())
                LOG.debug((new StringBuilder()).append("No matching data was found for keypoint: ").append(keypoint).toString());
            throw new IllegalStateException((new StringBuilder()).append("No matching data was found for keypoint = ").append(keypoint).toString());
        }
        if(data.length == 1)
        {
            if(LOG.isDebugEnabled())
                LOG.debug((new StringBuilder()).append("Only one mathing data point found - no extrapolation possible for: ").append(keypoint).toString());
            return data[0];
        }
        if(LOG.isDebugEnabled())
            LOG.debug((new StringBuilder()).append("Extrapolating new value for: ").append(keypoint).toString());
        Arrays.sort(data, DATAPOINT_COMPARATOR_ASCENDING_GEOMETRY);
        if(keypoint.getGeometry() > data[data.length - 1].getGeometry())
        {
            p1 = data[data.length - 2];
            p2 = data[data.length - 1];
        } else
        if(keypoint.getGeometry() < data[0].getGeometry())
        {
            p1 = data[0];
            p2 = data[1];
        } else
        {
            int i = 0;
            int stop = data.length - 1;
            do
            {
                if(i >= stop)
                    break;
                p1 = data[i];
                p2 = data[i + 1];
                if(keypoint.getGeometry() > p1.getGeometry() && keypoint.getGeometry() < p2.getGeometry())
                    break;
                i++;
            } while(true);
        }
        double a = (p2.getEcIor() - p1.getEcIor()) / (p2.getGeometry() - p1.getGeometry());
        double b = -(a * p1.getGeometry()) + p1.getEcIor();
        keypoint.setEcior(a * keypoint.getGeometry() + b);
        if(Double.isNaN(keypoint.getEcIor()))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.error((new StringBuilder()).append("P1: ").append(p1).toString());
                LOG.error((new StringBuilder()).append("P2: ").append(p2).toString());
                LOG.error((new StringBuilder()).append("Keypoint: ").append(keypoint).toString());
            }
            throw new IllegalStateException((new StringBuilder()).append("Invalid extrapolation for keypoint = ").append(keypoint).toString());
        } else
        {
            addDataPoint(new CDMALinkLevelDataPoint(keypoint));
            return keypoint;
        }
    }

    public double getMinimumGeometry(CDMALinkLevelDataPoint key)
    {
        double min = 1.7976931348623157E+308D;
        int i = 0;
        for(int stop = dataPoints.size(); i < stop; i++)
        {
            CDMALinkLevelDataPoint pt = (CDMALinkLevelDataPoint)dataPoints.get(i);
            if((int)pt.getSpeed() == (int)key.getSpeed() && pt.getPath() == key.getPath() && pt.getGeometry() < min)
                min = pt.getGeometry();
        }

        return min;
    }

    public double getMaximumGeometry(CDMALinkLevelDataPoint key)
    {
        double max = 4.9406564584124654E-324D;
        int i = 0;
        for(int stop = dataPoints.size(); i < stop; i++)
        {
            CDMALinkLevelDataPoint pt = (CDMALinkLevelDataPoint)dataPoints.get(i);
            if((int)pt.getSpeed() == (int)key.getSpeed() && pt.getPath() == key.getPath() && pt.getGeometry() > max)
                max = pt.getGeometry();
        }

        return max;
    }

    public CDMALinkLevelDataPoint[] getDataSet(CDMALinkLevelDataPoint key)
    {
        List t = new ArrayList();
        int i = 0;
        for(int stop = dataPoints.size(); i < stop; i++)
        {
            CDMALinkLevelDataPoint pt = (CDMALinkLevelDataPoint)dataPoints.get(i);
            if((int)pt.getSpeed() == (int)key.getSpeed() && pt.getPath() == key.getPath())
                t.add(pt);
        }

        return (CDMALinkLevelDataPoint[])t.toArray(new CDMALinkLevelDataPoint[t.size()]);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getColumnCount()
    {
        return 5;
    }

    public List getDataPoints()
    {
        return getDataPoints(currentPath);
    }

    public List getDataPoints(int pathNo)
    {
        List points = new ArrayList();
        Iterator i$ = dataPoints.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            CDMALinkLevelDataPoint point = (CDMALinkLevelDataPoint)i$.next();
            if(point.getPath() == pathNo)
                points.add(point);
        } while(true);
        return points;
    }

    public int getRowCount()
    {
        return getDataPoints().size();
    }

    public CDMALinkLevelDataPoint findDataPointForSpeed(int pathNo, double speed, double geometry)
    {
        CDMALinkLevelDataPoint point = null;
        int _speed = (int)speed;
        Iterator i$ = dataPoints.iterator();
        do
        {
            if(!i$.hasNext())
                break;
            CDMALinkLevelDataPoint p = (CDMALinkLevelDataPoint)i$.next();
            if(p.getPath() != pathNo || p.getGeometry() != geometry || p.getSpeed() != (double)_speed)
                continue;
            point = p;
            break;
        } while(true);
        return point;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        CDMALinkLevelDataPoint p = (CDMALinkLevelDataPoint)getDataPoints().get(rowIndex);
        double geometry = p.getGeometry();
        Object value;
        switch(columnIndex)
        {
        case 0: // '\0'
            value = Double.valueOf(geometry);
            break;

        case 1: // '\001'
            value = Double.valueOf(findDataPointForSpeed(currentPath, SPEED_VALUES[0], geometry).getEcIor());
            break;

        case 2: // '\002'
            value = Double.valueOf(findDataPointForSpeed(currentPath, SPEED_VALUES[1], geometry).getEcIor());
            break;

        case 3: // '\003'
            value = Double.valueOf(findDataPointForSpeed(currentPath, SPEED_VALUES[1], geometry).getEcIor());
            break;

        case 4: // '\004'
            value = Double.valueOf(findDataPointForSpeed(currentPath, SPEED_VALUES[2], geometry).getEcIor());
            break;

        default:
            throw new IllegalArgumentException((new StringBuilder()).append("Columns must be in range 0-4 (").append(columnIndex).append(")").toString());
        }
        return value;
    }

    public double getFrequency()
    {
        return frequency;
    }

    public void setFrequency(double frequency)
    {
        this.frequency = frequency;
    }

    public int getCurrentPath()
    {
        return currentPath;
    }

    public void setCurrentPath(int path)
    {
        if(path >= 1 && path <= 2)
            currentPath = path;
        else
            throw new IllegalArgumentException((new StringBuilder()).append("Path must be 0 or 1 (").append(path).append(")").toString());
    }

    public void setPathDescription(int path, String desc)
    {
        pathDescription[path] = desc;
    }

    public String getPathDescription()
    {
        return getPathDescription(currentPath);
    }

    public String getPathDescription(int path)
    {
        return pathDescription[path];
    }

    public TableModel getTableModel(int pathNo)
    {
        CDMAEditModel model = new CDMAEditModel();
        model.setData(loadDataFromSource(pathNo));
        return model;
    }

    private Vector loadDataFromSource(int pathNo)
    {
        setCurrentPath(pathNo);
        Vector result = new Vector();
        double currentGeom = -2147483648D;
        Vector tmp = null;
        Iterator i$ = getDataPoints(pathNo).iterator();
        do
            if(i$.hasNext())
            {
                CDMALinkLevelDataPoint point = (CDMALinkLevelDataPoint)i$.next();
                double geom = point.getGeometry();
                if(geom != currentGeom)
                {
                    currentGeom = geom;
                    tmp = new Vector(5);
                    tmp.add(new Double(geom));
                    for(int x = 1; x < 5; x++)
                        tmp.add(null);

                    result.add(tmp);
                }
                switch((int)point.getSpeed())
                {
                case 0: // '\0'
                    tmp.set(1, Double.valueOf(point.getEcIor()));
                    break;

                case 3: // '\003'
                    tmp.set(2, Double.valueOf(point.getEcIor()));
                    break;

                case 30: // '\036'
                    tmp.set(3, Double.valueOf(point.getEcIor()));
                    break;

                case 100: // 'd'
                    tmp.set(4, Double.valueOf(point.getEcIor()));
                    break;
                }
            } else
            {
                return result;
            }
        while(true);
    }

    public void storeDataModel(TableModel model, int path)
        throws NoGeometryException
    {
        dataPoints.removeAll(getDataPoints(path));
        int x = 0;
        for(int rows = model.getRowCount(); x < rows; x++)
        {
            Number geom = null;
            try
            {
                geom = Double.valueOf(((Number)model.getValueAt(x, 0)).doubleValue());
            }
            catch(RuntimeException ex)
            {
                throw new NoGeometryException((new StringBuilder()).append("No Geometry defined for row ").append(x).toString());
            }
            if(geom == null)
                continue;
            for(int y = 1; y < 5; y++)
            {
                Number n = (Number)model.getValueAt(x, y);
                if(n != null)
                    addDataPoint(new CDMALinkLevelDataPoint(getFrequency(), path, geom.doubleValue(), SPEED_VALUES[y - 1], n.doubleValue()));
            }

        }

    }

    public String getTargetERpct()
    {
        return targetERpct;
    }

    public void setTargetERpct(String targetERpct)
    {
        this.targetERpct = targetERpct;
    }

    public String getSystem()
    {
        return system;
    }

    public void setSystem(String system)
    {
        this.system = system;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public Element toElement(Document doc)
    {
        Element rootElem = doc.createElement("CDMA-Link-level-data");
        rootElem.setAttribute("system-type", linkType != LinkType.UPLINK ? "downlink" : "uplink");
        rootElem.setAttribute("source", source);
        rootElem.setAttribute("system", system);
        rootElem.setAttribute("frequency", Double.toString(frequency));
        rootElem.setAttribute("targetPct", targetERpct);
        rootElem.setAttribute("targetType", targetERType.toString());
        for(int pathNo = 1; pathNo <= 2; pathNo++)
        {
            Element pathElem = doc.createElement("path");
            pathElem.setAttribute("no", Integer.toString(pathNo));
            pathElem.setAttribute("caption", getPathDescription(pathNo));
            Iterator i$ = dataPoints.iterator();
            do
            {
                if(!i$.hasNext())
                    break;
                CDMALinkLevelDataPoint point = (CDMALinkLevelDataPoint)i$.next();
                if(point.getPath() == pathNo)
                    pathElem.appendChild(point.toElement(doc));
            } while(true);
            rootElem.appendChild(pathElem);
        }

        return rootElem;
    }

    protected void initNodeAttributes()
    {
    }

    public int hashCode()
    {
        return toString().hashCode();
    }

    public boolean equals(Object o)
    {
        return o == null || !(o instanceof CDMALinkLevelData) ? false : toString().equals(o.toString());
    }

    public LinkType getLinkType()
    {
        return linkType;
    }

    public void setLinkType(LinkType linkType)
    {
        if(linkType == null)
        {
            throw new IllegalArgumentException("LinkType cannot be null");
        } else
        {
            this.linkType = linkType;
            return;
        }
    }

    public TargetERType getTargetERType()
    {
        return targetERType;
    }

    public void setTargetERType(TargetERType targetERType)
    {
        this.targetERType = targetERType;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/CDMALinkLevelData);
    private static final double SPEED_VALUES[] = {
        0.0D, 3D, 30D, 100D
    };
    public static final int MAX_PATH_VALUE = 2;
    public static final double MAX_EC_IOR = 0D;
    private LinkType linkType;
    private TargetERType targetERType;
    private String name;
    private String system;
    private String source;
    private String targetERpct;
    private String description;
    private double frequency;
    private int currentPath;
    private List dataPoints;
    private String pathDescription[];
    private boolean sorted;
    private int geometryPrecision;
    public static final Comparator DATAPOINT_COMPARATOR_DESCENDING_GEOMETRY = new Comparator() {

        public int compare(CDMALinkLevelDataPoint p1, CDMALinkLevelDataPoint p2)
        {
            double result;
            if(p1.getPath() != p2.getPath())
                result = p2.getPath() - p1.getPath();
            else
            if(p1.getGeometry() != p2.getGeometry())
                result = p2.getGeometry() <= p1.getGeometry() ? -1D : 1.0D;
            else
            if((int)p1.getSpeed() != (int)p2.getSpeed())
                result = (int)(p2.getSpeed() - p1.getSpeed());
            else
                result = 0.0D;
            return (int)Math.rint(result);
        }

        public volatile int compare(Object x0, Object x1)
        {
            return compare((CDMALinkLevelDataPoint)x0, (CDMALinkLevelDataPoint)x1);
        }

    }
;
    public static final Comparator DATAPOINT_COMPARATOR_ASCENDING_GEOMETRY = new Comparator() {

        public int compare(CDMALinkLevelDataPoint p1, CDMALinkLevelDataPoint p2)
        {
            return -CDMALinkLevelData.DATAPOINT_COMPARATOR_DESCENDING_GEOMETRY.compare(p1, p2);
        }

        public volatile int compare(Object x0, Object x1)
        {
            return compare((CDMALinkLevelDataPoint)x0, (CDMALinkLevelDataPoint)x1);
        }

    }
;
    public static final Comparator TABLEINDEX_COMPARATOR = new Comparator() {

        public int compare(Double p1, Double p2)
        {
            int result;
            if(p1.doubleValue() < p2.doubleValue())
                result = 1;
            else
            if(p1.doubleValue() > p2.doubleValue())
                result = -1;
            else
                result = 0;
            return result;
        }

        public volatile int compare(Object x0, Object x1)
        {
            return compare((Double)x0, (Double)x1);
        }

    }
;
    public static final Comparator DATAPOINT_COMPARATOR_NO_GEOMETRY = new Comparator() {

        public int compare(CDMALinkLevelDataPoint p1, CDMALinkLevelDataPoint p2)
        {
            int result;
            if(p1.getPath() != p2.getPath())
                result = p2.getPath() - p1.getPath();
            else
            if((int)p1.getFrequency() != (int)p2.getFrequency())
                result = (int)(p2.getFrequency() - p1.getFrequency());
            else
            if((int)p1.getSpeed() != (int)p2.getSpeed())
                result = (int)(p2.getSpeed() - p1.getSpeed());
            else
                result = 0;
            return result;
        }

        public volatile int compare(Object x0, Object x1)
        {
            return compare((CDMALinkLevelDataPoint)x0, (CDMALinkLevelDataPoint)x1);
        }

    }
;

}