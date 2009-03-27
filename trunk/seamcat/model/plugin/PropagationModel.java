// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropagationModel.java

package org.seamcat.model.plugin;


public interface PropagationModel
{
    public static final class AssumedModel extends Enum
    {

        public static AssumedModel[] values()
        {
            return (AssumedModel[])$VALUES.clone();
        }

        public static AssumedModel valueOf(String name)
        {
            return (AssumedModel)Enum.valueOf(org/seamcat/model/plugin/PropagationModel$AssumedModel, name);
        }

        public static final AssumedModel FreeSpace;
        public static final AssumedModel Hata;
        public static final AssumedModel Hata_SRD;
        public static final AssumedModel ITU_P1546;
        public static final AssumedModel SphericalDiffraction;
        public static final AssumedModel UserDefinedPlugin;
        public static final AssumedModel NoModelDefined;
        private static final AssumedModel $VALUES[];

        static 
        {
            FreeSpace = new AssumedModel("FreeSpace", 0);
            Hata = new AssumedModel("Hata", 1);
            Hata_SRD = new AssumedModel("Hata_SRD", 2);
            ITU_P1546 = new AssumedModel("ITU_P1546", 3);
            SphericalDiffraction = new AssumedModel("SphericalDiffraction", 4);
            UserDefinedPlugin = new AssumedModel("UserDefinedPlugin", 5);
            NoModelDefined = new AssumedModel("NoModelDefined", 6);
            $VALUES = (new AssumedModel[] {
                FreeSpace, Hata, Hata_SRD, ITU_P1546, SphericalDiffraction, UserDefinedPlugin, NoModelDefined
            });
        }

        private AssumedModel(String s, int i)
        {
            super(s, i);
        }
    }


    public abstract int getEnviroment();

    public abstract AssumedModel getAssumedModel();
}
