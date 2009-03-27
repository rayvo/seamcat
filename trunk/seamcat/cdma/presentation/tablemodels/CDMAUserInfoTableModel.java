// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAUserInfoTableModel.java

package org.seamcat.cdma.presentation.tablemodels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.cdma.*;

// Referenced classes of package org.seamcat.cdma.presentation.tablemodels:
//            CDMAElementTableValue

public class CDMAUserInfoTableModel
    implements TableModel
{

    public CDMAUserInfoTableModel()
    {
        tableentries = new ArrayList();
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Userid";
                else
                    return Integer.toString(user.getUserid());
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Position";
                else
                    return (new StringBuilder()).append("(").append(user.getLocationX()).append(", ").append(user.getLocationY()).append(")").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Status";
                if(user.isConnected())
                    return "Connected";
                if(user.isDroppedAsHighest())
                    return "Removed as highest transmitting user";
                if(user.isDropped())
                    return (new StringBuilder()).append("Dropped - ").append(user.getDropReason()).toString();
                if(!user.isAllowedToConnect())
                    return "Not allowed to connect";
                else
                    return "Not connected";
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Speed";
                else
                    return (new StringBuilder()).append(Double.toString(user.getMobilitySpeed())).append(" km/h").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(user.isUplinkMode())
                    if(columnIndex == 0)
                        return "Multi-Path";
                    else
                        return Integer.toString(user.getMultiPathChannel());
                if(columnIndex == 0)
                    return "Geometry";
                else
                    return (new StringBuilder()).append(user.getGeometry()).append(" dB").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(user.isUplinkMode())
                    if(columnIndex == 0)
                        return "Achieved CI";
                    else
                        return (new StringBuilder()).append(Double.toString(CDMASystem.round(user.getAchievedCI()))).append(" dB").toString();
                if(columnIndex == 0)
                    return "Achieved Ec/Ior";
                else
                    return (new StringBuilder()).append(Double.toString(CDMASystem.round(user.getAchievedEcIor()))).append(" dB").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(user.isUplinkMode())
                    if(columnIndex == 0)
                        return "Required Eb/No";
                    else
                        return (new StringBuilder()).append(Double.toString(CDMASystem.round(user.getLinkLevelData().getEbNo()))).append(" dB").toString();
                if(columnIndex == 0)
                    return "Required Ec/Ior";
                else
                    return (new StringBuilder()).append(Double.toString(CDMASystem.round(user.getLinkLevelData().getEcIor()))).append(" dB").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(user.isUplinkMode())
                    if(columnIndex == 0)
                        return "Transmit Power";
                    else
                        return (new StringBuilder()).append(Double.toString(CDMASystem.fromWatt2dBm(user.getCurrentTransmitPowerInWatt()))).append(" dBm").toString();
                if(columnIndex == 0)
                    return "External interference";
                double extInf = user.getExternalInterference();
                if(extInf < -900D)
                    return "No External Interference";
                else
                    return (new StringBuilder()).append(Double.toString(CDMASystem.round(extInf))).append(" dBm").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Active List";
                else
                    return (new StringBuilder()).append(user.getActiveList().size()).append(" connection(s)").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Connection List";
                else
                    return (new StringBuilder()).append(user.getCDMALinks().length - user.getActiveList().size()).append(" connection(s)").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Is in softhandover";
                else
                    return Boolean.toString(user.isInSoftHandover());
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Total Power Received from Inactive Set";
                if(user.isUplinkMode())
                    return "Not applicable";
                else
                    return (new StringBuilder()).append(user.getTotalPowerReceivedFromBaseStationsNotInActiveSetdBm()).append(" dBm").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Total Power Received from Active Set";
                if(user.isUplinkMode())
                    return "Not applicable";
                else
                    return (new StringBuilder()).append(CDMASystem.fromWatt2dBm(user.getTotalPowerReceivedFromBaseStationsActiveSetInWatt())).append(" dBm").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Traffic Channel Power";
                if(user.isUplinkMode())
                    return "Not applicable";
                else
                    return (new StringBuilder()).append(CDMASystem.fromWatt2dBm(user.getReceivedTrafficChannelPowerWatt())).append(" dBm").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Distance to first cell in active list";
                else
                    return (new StringBuilder()).append(Double.toString(((CDMALink)user.getActiveList().get(0)).getDistance())).append(" km").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Angle to first cell in active list";
                else
                    return (new StringBuilder()).append(Double.toString(((CDMALink)user.getActiveList().get(0)).getAngle())).append('\260').toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Elevation angle to first cell in active list";
                else
                    return (new StringBuilder()).append(Double.toString(((CDMALink)user.getActiveList().get(0)).getElevation())).append('\260').toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Connected sector of first cell in active list";
                CDMACell cell = ((CDMALink)user.getActiveList().get(0)).getCDMACell();
                if(cell instanceof CDMATriSectorCell)
                    return Integer.valueOf(((CDMATriSectorCell)cell).getSectorId());
                else
                    return "N/A - Cell is Omni directional";
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Antenna Gain from first BS in active list";
                else
                    return (new StringBuilder()).append(Double.toString(((CDMALink)user.getActiveList().get(0)).getBsAntGain())).append(" dB").toString();
            }

            final CDMAUserInfoTableModel this$0;

            
            {
                this$0 = CDMAUserInfoTableModel.this;
                super();
            }
        }
);
    }

    public void setUserTerminal(UserTerminal _user)
    {
        user = _user;
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
        if(user == null)
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

    public UserTerminal getUser()
    {
        return user;
    }

    private UserTerminal user;
    private final List listeners = new ArrayList();
    private List tableentries;

}