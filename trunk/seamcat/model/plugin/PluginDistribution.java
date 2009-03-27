// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PluginDistribution.java

package org.seamcat.model.plugin;


public interface PluginDistribution
{
    public static final class DistributionType extends Enum
    {

        public static DistributionType[] values()
        {
            return (DistributionType[])$VALUES.clone();
        }

        public static DistributionType valueOf(String name)
        {
            return (DistributionType)Enum.valueOf(org/seamcat/model/plugin/PluginDistribution$DistributionType, name);
        }

        public static final DistributionType Constant;
        public static final DistributionType Uniform;
        public static final DistributionType Gaussian;
        public static final DistributionType Rayleigh;
        public static final DistributionType PolarAngle;
        public static final DistributionType PolarDistance;
        public static final DistributionType DiscreteUniform;
        public static final DistributionType UserDefined;
        public static final DistributionType UserDefinedStair;
        private static final DistributionType $VALUES[];

        static 
        {
            Constant = new DistributionType("Constant", 0);
            Uniform = new DistributionType("Uniform", 1);
            Gaussian = new DistributionType("Gaussian", 2);
            Rayleigh = new DistributionType("Rayleigh", 3);
            PolarAngle = new DistributionType("PolarAngle", 4);
            PolarDistance = new DistributionType("PolarDistance", 5);
            DiscreteUniform = new DistributionType("DiscreteUniform", 6);
            UserDefined = new DistributionType("UserDefined", 7);
            UserDefinedStair = new DistributionType("UserDefinedStair", 8);
            $VALUES = (new DistributionType[] {
                Constant, Uniform, Gaussian, Rayleigh, PolarAngle, PolarDistance, DiscreteUniform, UserDefined, UserDefinedStair
            });
        }

        private DistributionType(String s, int i)
        {
            super(s, i);
        }
    }


    public abstract double trial();

    public abstract DistributionType getDistributionType();
}
