// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CDMACellTestCase.java

package org.seamcat.test;

import java.io.PrintStream;
import junit.framework.Assert;
import org.seamcat.cdma.*;

// Referenced classes of package org.seamcat.test:
//            AbstractTestCase

public class CDMACellTestCase extends AbstractTestCase
{

    public CDMACellTestCase()
    {
    }

    protected void setUp()
        throws Exception
    {
        super.setUp();
        sys = new CDMADownlinkSystem();
        sys.setCellRadius(1.5D);
        sys.repositionSystem(0.0D, 0.0D);
        cell = sys.getCDMACells()[8][0];
    }

    protected void tearDown()
        throws Exception
    {
        super.tearDown();
    }

    public void testIsInsideCell()
    {
        System.out.println((new StringBuilder()).append("Cell radius = ").append(sys.getCellRadius()).toString());
        System.out.println((new StringBuilder()).append("LocationX = ").append(cell.getLocationX()).toString());
        System.out.println((new StringBuilder()).append("LocationY = ").append(cell.getLocationY()).toString());
        testIt(new Runnable() {

            public void run()
            {
                Assert.assertFalse(cell.isInsideCell(5.6715847308563871D, 1.7999295649491718D));
            }

            final CDMACellTestCase this$0;

            
            {
                this$0 = CDMACellTestCase.this;
                super();
            }
        }
);
    }

    private CDMASystem sys;
    private CDMACell cell;

}
