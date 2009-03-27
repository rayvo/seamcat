// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PostProcessingPlugin.java

package org.seamcat.model.plugin;


// Referenced classes of package org.seamcat.model.plugin:
//            ParameterFactory, ScenarioInfo

public interface PostProcessingPlugin
{
    public static final class ParameterType extends Enum
    {

        public static ParameterType[] values()
        {
            return (ParameterType[])$VALUES.clone();
        }

        public static ParameterType valueOf(String name)
        {
            return (ParameterType)Enum.valueOf(org/seamcat/model/plugin/PostProcessingPlugin$ParameterType, name);
        }

        public static final ParameterType Boolean;
        public static final ParameterType Integer;
        public static final ParameterType Double;
        public static final ParameterType Distribution;
        public static final ParameterType Function2D;
        public static final ParameterType Function3D;
        public static final ParameterType String;
        private static final ParameterType $VALUES[];

        static 
        {
            Boolean = new ParameterType("Boolean", 0);
            Integer = new ParameterType("Integer", 1);
            Double = new ParameterType("Double", 2);
            Distribution = new ParameterType("Distribution", 3);
            Function2D = new ParameterType("Function2D", 4);
            Function3D = new ParameterType("Function3D", 5);
            String = new ParameterType("String", 6);
            $VALUES = (new ParameterType[] {
                Boolean, Integer, Double, Distribution, Function2D, Function3D, String
            });
        }

        private ParameterType(String s, int i)
        {
            super(s, i);
        }
    }


    public abstract void init(ParameterFactory parameterfactory);

    public abstract void cleanUp();

    public abstract String getDescription();

    public abstract void process(ScenarioInfo scenarioinfo)
        throws Exception;

    public abstract int getNumberOfParameters();

    public abstract ParameterType getParameterType(int i);

    public abstract void setParameterValue(int i, Object obj);

    public abstract Object getParameterValue(int i);

    public abstract String getParameterName(int i);
}
