// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LegacySeamcatTypes.java

package org.seamcat.importer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.seamcat.distribution.*;
import org.seamcat.function.*;
import org.seamcat.model.core.AntennaPattern;

// Referenced classes of package org.seamcat.importer:
//            LegacySeamcatConverter

final class LegacySeamcatTypes
{

    private LegacySeamcatTypes()
    {
    }

    static final AntennaPattern toPattern(String line, BufferedReader reader, AntennaPattern pattern)
        throws IOException
    {
        Matcher matcher = PATTERN_1PARAM.matcher(toString(line));
        matcher.find();
        int size = Integer.parseInt(matcher.group(1));
        Point2D points[] = new Point2D[size];
        for(int x = 0; x < size; x++)
        {
            matcher = PATTERN_2DIGITS.matcher(reader.readLine());
            LegacySeamcatConverter.lineno++;
            matcher.find();
            points[x] = new Point2D(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));
        }

        pattern.setPattern(new DiscreteFunction(points));
        return pattern;
    }

    static final Function2 toFunction2(String line, BufferedReader reader)
        throws IOException
    {
        String data = toString(line);
        Function2 function;
        if(data.indexOf("Constant") != -1)
        {
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            function = new ConstantFunction2(Double.parseDouble(matcher.group(1)));
        } else
        if(data.indexOf("Array") != -1)
        {
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            int size = Integer.parseInt(matcher.group(1));
            Point3D points[] = new Point3D[size];
            for(int x = 0; x < size; x++)
            {
                String currentLine = reader.readLine();
                LegacySeamcatConverter.lineno++;
                matcher = PATTERN_3DIGITS.matcher(currentLine);
                matcher.find();
                try
                {
                    points[x] = new Point3D(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)), Double.parseDouble(matcher.group(3)));
                    continue;
                }
                catch(IllegalStateException ex)
                {
                    matcher = PATTERN_2DIGITS.matcher(currentLine);
                }
                matcher.find();
                points[x] = new Point3D(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)), 0.0D);
            }

            function = new DiscreteFunction2(points);
        } else
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown Function type in line <").append(line).append(">").toString());
        }
        return function;
    }

    static final DiscreteFunction2 toFunction2FromFunction(String line, BufferedReader reader)
        throws IOException
    {
        DiscreteFunction2 function = null;
        String data = toString(line);
        if(data.toUpperCase().indexOf("CONSTANT") != -1)
            return null;
        if(data.indexOf("Array") != -1)
            try
            {
                Matcher matcher = PATTERN_1PARAM.matcher(data);
                matcher.find();
                int size = Integer.parseInt(matcher.group(1));
                Point3D points[] = new Point3D[size];
                for(int x = 0; x < size; x++)
                {
                    matcher = PATTERN_2DIGITS.matcher(reader.readLine());
                    LegacySeamcatConverter.lineno++;
                    matcher.find();
                    points[x] = new Point3D(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)), 0.0D);
                }

                function = new DiscreteFunction2(points);
            }
            catch(Exception ex)
            {
                LOG.error((new StringBuilder()).append("Error reading function2 from function line: \n").append(line).toString(), ex);
                throw new IllegalArgumentException((new StringBuilder()).append("Error reading Function from line <").append(line).append(">").toString());
            }
        else
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown Function type in line <").append(line).append(">").toString());
        return function;
    }

    static final Function toFunction(String line, BufferedReader reader)
        throws IOException
    {
        String data = toString(line);
        Function function;
        if(data.indexOf("Constant") != -1)
        {
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            function = new ConstantFunction(Double.parseDouble(matcher.group(1)));
        } else
        if(data.indexOf("Array") != -1)
        {
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            int size = Integer.parseInt(matcher.group(1));
            Point2D points[] = new Point2D[size];
            for(int x = 0; x < size; x++)
            {
                matcher = PATTERN_2DIGITS.matcher(reader.readLine());
                LegacySeamcatConverter.lineno++;
                matcher.find();
                points[x] = new Point2D(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));
            }

            function = new DiscreteFunction(points);
        } else
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown Function type in line <").append(line).append(">").toString());
        }
        return function;
    }

    static final Distribution toDistribution(String line, BufferedReader reader)
        throws IOException
    {
        String data = toString(line);
        Distribution dist;
        if(data.indexOf("Constant") != -1)
        {
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            dist = new ConstantDistribution(Double.parseDouble(matcher.group(1)));
        } else
        if(data.indexOf("Gaussian") != -1)
        {
            Matcher matcher = PATTERN_2PARAMS.matcher(data);
            matcher.find();
            dist = new GaussianDistribution(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));
        } else
        if(data.indexOf("Uniform polar angle") != -1)
        {
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            dist = new UniformPolarAngleDistribution(Double.parseDouble(matcher.group(1)));
        } else
        if(data.indexOf("Uniform polar distance") != -1)
        {
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            dist = new UniformPolarDistanceDistribution(Double.parseDouble(matcher.group(1)));
        } else
        if(data.indexOf("Uniform polar angle") != -1)
        {
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            dist = new UniformPolarAngleDistribution(Double.parseDouble(matcher.group(1)));
        } else
        if(data.indexOf("Uniform") != -1)
        {
            Matcher matcher = PATTERN_2PARAMS.matcher(data);
            matcher.find();
            dist = new UniformDistribution(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));
        } else
        if(data.indexOf("Rayleigh") != -1)
        {
            Matcher matcher = PATTERN_2PARAMS.matcher(data);
            matcher.find();
            dist = new RayleighDistribution(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2)));
        } else
        if(data.indexOf("Array") != -1)
        {
            DiscreteFunction function = new DiscreteFunction();
            Matcher matcher = PATTERN_1PARAM.matcher(data);
            matcher.find();
            int size = Integer.parseInt(matcher.group(1));
            for(int x = 0; x < size; x++)
            {
                matcher = PATTERN_2DIGITS.matcher(reader.readLine());
                LegacySeamcatConverter.lineno++;
                matcher.find();
                function.addPoint(new Point2D(Double.parseDouble(matcher.group(1)), Double.parseDouble(matcher.group(2))));
            }

            dist = new ContinousDistribution(function);
        } else
        {
            throw new IllegalArgumentException((new StringBuilder()).append("Unknown Distribution type in line <").append(line).append(">").toString());
        }
        return dist;
    }

    static final boolean toBoolean(String line)
    {
        return toString(line).equalsIgnoreCase("Yes");
    }

    static final double toDouble(String line)
    {
        return Double.parseDouble(toString(line));
    }

    static final int toInteger(String line)
    {
        return Integer.parseInt(toString(line));
    }

    static final String toString(String line)
    {
        return line.substring(line.indexOf('=') + 1);
    }

    static final String getParamname(String line)
    {
        String paramName;
        if(line != null)
        {
            int colonIdx = line.indexOf(':');
            int equalsIdx = line.indexOf('=');
            if(equalsIdx == -1 || colonIdx >= equalsIdx)
                paramName = null;
            else
            if(colonIdx == -1)
                paramName = line.substring(0, equalsIdx);
            else
                paramName = line.substring(0, colonIdx);
        } else
        {
            paramName = null;
        }
        return paramName;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/importer/LegacySeamcatTypes);
    public static final char DATA_SEPARATOR = 61;
    public static final char TYPE_SEPARATOR = 58;
    private static final String BOOLEAN_TRUE = "Yes";
    private static final String CONSTANT_DIST = "Constant";
    private static final String GAUSSIAN_DIST = "Gaussian";
    private static final String UNIFORM_DIST = "Uniform";
    private static final String DISTANCE_DIST = "Uniform polar distance";
    private static final String ANGLE_DIST = "Uniform polar angle";
    private static final String USERDIST_DIST = "Array";
    private static final String RAYLEIGH_DIST = "Rayleigh";
    private static final String USER_FUNCTION = "Array";
    private static final String CONSTANT_FUNCTION = "Constant";
    static final String CORRELATION_MODES[] = {
        "None", "Uniform density", "Closest interferer", "ITx->VRx", "ITx->WTx", "WRx->WTx", "WRx->VRx"
    };
    static final String COVERAGE_RADIUS_CALCULATION_MODE[] = {
        "User-defined radius", "Noise-limited network", "Traffic limited network"
    };
    private static final Pattern PATTERN_1PARAM = Pattern.compile("\\((\\S+)\\)");
    private static final Pattern PATTERN_2PARAMS = Pattern.compile("\\((\\S+)\\,(\\S+)\\)");
    private static final Pattern PATTERN_2DIGITS = Pattern.compile("\\s+(\\S+)\\s+(\\S+)");
    private static final Pattern PATTERN_3DIGITS = Pattern.compile("\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)");

}