// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   FileDataIO.java

package org.seamcat.presentation;

import java.io.*;
import java.util.*;
import org.seamcat.function.*;
import org.seamcat.interfaces.Functionable;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.statistics.NormalDistribution;

public class FileDataIO
{

    public FileDataIO()
    {
    }

    public void setFile(File _file)
    {
        file = _file;
    }

    public File getFile()
    {
        return file;
    }

    public void saveDoubleArray(double data[])
    {
        try
        {
            double mean = Mathematics.getAverage(data);
            double stddev = Mathematics.getStdDev(data, mean);
            double cumulative[] = new double[data.length];
            NormalDistribution normDist = new NormalDistribution(mean, stddev);
            for(int i = 0; i < data.length; i++)
                cumulative[i] = normDist.getCDF(data[i]);

            BufferedWriter wri = new BufferedWriter(new FileWriter(file));
            int i = 0;
            for(int stop = data.length; i < stop; i++)
            {
                wri.write((new StringBuilder()).append(i).append("\t").append(data[i]).append("\t").append(cumulative[i]).toString());
                wri.newLine();
            }

            wri.flush();
            wri.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveTableData(Point2D points[])
    {
        try
        {
            BufferedWriter wri = new BufferedWriter(new FileWriter(file));
            int i = 0;
            for(int stop = points.length; i < stop; i++)
            {
                wri.write((new StringBuilder()).append(points[i].getX()).append("\t").append(points[i].getY()).append("\t").toString());
                wri.newLine();
            }

            wri.flush();
            wri.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveTableData(Functionable data)
    {
        try
        {
            BufferedWriter wri = new BufferedWriter(new FileWriter(file));
            for(Iterator i$ = data.getPointsList().iterator(); i$.hasNext(); wri.newLine())
            {
                Point2D row = (Point2D)i$.next();
                wri.write((new StringBuilder()).append(row.getX()).append("\t").append(row.getY()).append("\t").toString());
                if(row instanceof Point3D)
                    wri.write(Double.toString(((Point3D)row).getRZ()));
            }

            wri.flush();
            wri.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public Functionable loadTableData()
    {
        try
        {
            return loadTableData(new BufferedReader(new FileReader(file)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public Functionable loadTableData(BufferedReader read)
    {
        List data;
        data = new ArrayList();
        boolean pointsIs3D = false;
        double values[] = new double[3];
        String temp;
        while((temp = read.readLine()) != null) 
        {
            int index = 0;
            for(StringTokenizer strtk = new StringTokenizer(temp, "\t", false); strtk.hasMoreTokens();)
                try
                {
                    values[index++] = Double.parseDouble(strtk.nextToken());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            if(index > 2)
            {
                data.add(new Point3D(values[0], values[1], values[2]));
                pointsIs3D = true;
            } else
            {
                data.add(new Point2D(values[0], values[1]));
                pointsIs3D = false;
            }
        }
        read.close();
        if(pointsIs3D)
            return new DiscreteFunction2(data);
        try
        {
            return new DiscreteFunction(data);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private File file;
}
