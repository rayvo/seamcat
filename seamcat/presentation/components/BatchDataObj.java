// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchDataObj.java

package org.seamcat.presentation.components;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package org.seamcat.presentation.components:
//            BatchData, BatchParameterData

public class BatchDataObj
    implements BatchData
{

    public BatchDataObj()
    {
        parameters = new ArrayList();
    }

    public void setParameter(BatchParameterData param)
    {
        parameters.add(param);
    }

    public List getParameterData()
    {
        return parameters;
    }

    public String getValueString(int index)
    {
        return test_values[index];
    }

    private String test_values[] = {
        "Untitled Workspace 1", "1", "0", "0", "Complete1", "C/I", "Yes", "No", "Yes", "10000"
    };
    private List parameters;
}
