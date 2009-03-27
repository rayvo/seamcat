// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMALinkInfoTableModel.java

package org.seamcat.cdma.presentation.tablemodels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.cdma.*;

// Referenced classes of package org.seamcat.cdma.presentation.tablemodels:
//            CDMAElementTableValue

public class CDMALinkInfoTableModel
    implements TableModel
{

    public CDMALinkInfoTableModel()
    {
        tableentries = new ArrayList();
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Userid";
                else
                    return Integer.toString(link.getUserTerminal().getUserid());
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "In users active list";
                else
                    return Boolean.toString(link.getUserTerminal().getActiveList().contains(link));
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Cellid";
                else
                    return Integer.toString(link.getCDMACell().getCellid());
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Cell location id";
                else
                    return Integer.toString(link.getCDMACell().getCellLocationId());
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Connected sector of cell location";
                else
                    return Integer.toString(link.getCDMACell().getSectorId());
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "User Position";
                else
                    return (new StringBuilder()).append("(").append(link.getUserTerminal().getLocationX()).append(", ").append(link.getUserTerminal().getLocationY()).append(")").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Cell Position";
                else
                    return (new StringBuilder()).append("(").append(link.getCDMACell().getLocationX()).append(", ").append(link.getCDMACell().getLocationY()).append(")").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Using WrapAround";
                else
                    return Boolean.toString(link.isUsingWrapAround());
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Distance";
                else
                    return (new StringBuilder()).append(Double.toString(link.getDistance())).append(" km").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "BS antenna gain";
                else
                    return (new StringBuilder()).append(Double.toString(link.getBsAntGain())).append(" dB").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "UE antenna gain";
                else
                    return (new StringBuilder()).append(Double.toString(link.getUserAntGain())).append(" dB").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Pathloss";
                else
                    return (new StringBuilder()).append(Double.toString(link.getPathLoss())).append(" dB").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Effective Pathloss";
                else
                    return (new StringBuilder()).append(Double.toString(link.getEffectivePathloss())).append(" dB").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Horizontal Angle";
                else
                    return (new StringBuilder()).append(Double.toString(link.getAngle())).append('\260').toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Vertical Angle";
                else
                    return (new StringBuilder()).append(Double.toString(link.getElevation())).append('\260').toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Total Transmit Power";
                if(link instanceof CDMADownlink)
                    return (new StringBuilder()).append(Double.toString(link.getCDMACell().getCurrentTransmitPower())).append(" dBm").toString();
                else
                    return (new StringBuilder()).append(link.getUserTerminal().getCurrentTransmitPowerIndBm()).append(" dBm").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Power scaled down to max";
                else
                    return Boolean.toString(link.isPowerScaledDownToMax());
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Power scaled down to max by";
                else
                    return link.getPowerScaledBy();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Total Received power";
                else
                    return (new StringBuilder()).append(Double.toString(link.getReceivePower_dB())).append(" dBm").toString();
            }

            final CDMALinkInfoTableModel this$0;

            
            {
                this$0 = CDMALinkInfoTableModel.this;
                super();
            }
        }
);
    }

    public void setCDMALink(CDMALink _link)
    {
        link = _link;
        int i = 0;
        for(int stop = listeners.size(); i < stop; i++)
            ((TableModelListener)listeners.get(i)).tableChanged(new TableModelEvent(this));

    }

    public int getColumnCount()
    {
        return 2;
    }

    public int getRowCount()
    {
        if(link == null)
            return 0;
        else
            return tableentries.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return columnIndex == 1 && (rowIndex == 8 || rowIndex == 9);
    }

    public Class getColumnClass(int columnIndex)
    {
        return java/lang/Object;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return ((CDMAElementTableValue)tableentries.get(rowIndex)).getValue(columnIndex);
    }

    public void setValueAt(Object obj, int i, int j)
    {
    }

    public String getColumnName(int columnIndex)
    {
        switch(columnIndex)
        {
        case 0: // '\0'
            return "Name";

        case 1: // '\001'
            return "Value";
        }
        return "Unknown";
    }

    public void addTableModelListener(TableModelListener l)
    {
        listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        listeners.remove(l);
    }

    public CDMALink getLink()
    {
        return link;
    }

    private CDMALink link;
    private final List listeners = new ArrayList();
    private List tableentries;

}