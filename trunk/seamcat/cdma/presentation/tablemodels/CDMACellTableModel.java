// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMACellTableModel.java

package org.seamcat.cdma.presentation.tablemodels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.cdma.CDMACell;
import org.seamcat.cdma.CDMASystem;

// Referenced classes of package org.seamcat.cdma.presentation.tablemodels:
//            CDMAElementTableValue

public class CDMACellTableModel
    implements TableModel
{

    public CDMACellTableModel()
    {
        listeners = new ArrayList();
        tableentries = new ArrayList();
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Cell ID";
                else
                    return new Integer(selectedCell.getCellid());
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(selectedCell.isUplinkMode())
                    if(columnIndex == 0)
                        return "Total Interference";
                    else
                        return (new StringBuilder()).append(selectedCell.getTotalInterference()).append(" dBm").toString();
                if(columnIndex == 0)
                    return "Transmit Power";
                selectedCell.calculateCurrentChannelPower_dBm();
                if(selectedCell.getCurrentTransmitPower() > selectedCell.getMaximumTransmitPower_dBm())
                    System.currentTimeMillis();
                return (new StringBuilder()).append(selectedCell.getCurrentTransmitPower()).append(" dBm").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Number of served users";
                else
                    return (new StringBuilder()).append(new Integer(selectedCell.countServedUsers())).append(" users").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Antenna height";
                else
                    return (new StringBuilder()).append(selectedCell.getAntennaHeight()).append(" meters").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Number of dropped users";
                else
                    return (new StringBuilder()).append(selectedCell.countDroppedUsers()).append(" users").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(selectedCell.isUplinkMode())
                    if(columnIndex == 0)
                        return "Noise Rise over Thermal Noise";
                    else
                        return (new StringBuilder()).append(selectedCell.calculateNoiseRiseOverThermalNoise_dB()).append(" dB").toString();
                if(columnIndex == 0)
                    return "Outage percentage";
                else
                    return (new StringBuilder()).append(selectedCell.getOutagePercentage()).append(" %").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
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
                    return (new StringBuilder()).append("(").append(selectedCell.getLocationX()).append(", ").append(selectedCell.getLocationY()).append(")").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(selectedCell.isUplinkMode())
                    if(columnIndex == 0)
                        return "InterSystem Interference";
                    else
                        return (new StringBuilder()).append(selectedCell.getInterSystemInterference()).append(" dBm").toString();
                if(columnIndex == 0)
                    return "Pilot Channel Power";
                else
                    return (new StringBuilder()).append(selectedCell.getPilotPower_Watt()).append(" dBm").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(selectedCell.isUplinkMode())
                    if(columnIndex == 0)
                        return "External Interference, Unwanted";
                    else
                        return (new StringBuilder()).append(selectedCell.getExternalInterferenceUnwanted()).append(" dBm").toString();
                if(columnIndex == 0)
                    return "Overhead Channel Power";
                else
                    return (new StringBuilder()).append(selectedCell.getOverheadPower_Watt()).append(" dBm").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(selectedCell.isUplinkMode())
                    if(columnIndex == 0)
                        return "External Interference, Selectivity";
                    else
                        return (new StringBuilder()).append(selectedCell.getExternalInterferenceBlocking()).append(" dBm").toString();
                if(columnIndex == 0)
                    return "Number of active users";
                else
                    return (new StringBuilder()).append(selectedCell.countActiveUsers()).append(" users").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Active Connections";
                else
                    return (new StringBuilder()).append(selectedCell.getActiveConnections().size()).append(" connections").toString();
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Is Reference Cell";
                else
                    return Boolean.toString(selectedCell == selectedCell.getCdmasystem().getReferenceCell());
            }

            final CDMACellTableModel this$0;

            
            {
                this$0 = CDMACellTableModel.this;
                super();
            }
        }
);
    }

    public int getColumnCount()
    {
        return 2;
    }

    public int getRowCount()
    {
        if(selectedCell == null)
            return 0;
        else
            return tableentries.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return rowIndex == 8;
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
        return columnIndex != 0 ? "Value" : "Name";
    }

    public void addTableModelListener(TableModelListener l)
    {
        listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l)
    {
        listeners.remove(l);
    }

    public CDMACell getSelectedCell()
    {
        return selectedCell;
    }

    public void setSelectedCell(CDMACell selectedCell)
    {
        this.selectedCell = selectedCell;
        for(int i = 0; i < listeners.size(); i++)
            ((TableModelListener)listeners.get(i)).tableChanged(new TableModelEvent(this));

    }

    private CDMACell selectedCell;
    private List listeners;
    private List tableentries;

}