// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Patternable.java

package org.seamcat.interfaces;

import javax.swing.table.TableModel;
import org.seamcat.function.DiscreteFunction;

public interface Patternable
    extends TableModel
{

    public abstract DiscreteFunction getPattern();
}