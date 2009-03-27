// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Functionable.java

package org.seamcat.interfaces;

import java.util.List;
import javax.swing.table.TableModel;

public interface Functionable
{

    public abstract TableModel getTableModel();

    public abstract void setTableModel(TableModel tablemodel);

    public abstract List getPointsList();
}