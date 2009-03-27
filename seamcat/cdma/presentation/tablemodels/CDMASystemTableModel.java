// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMASystemTableModel.java

package org.seamcat.cdma.presentation.tablemodels;

import java.util.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.cdma.*;

// Referenced classes of package org.seamcat.cdma.presentation.tablemodels:
//            CDMAElementTableValue

public class CDMASystemTableModel
    implements TableModel
{

    public CDMASystemTableModel()
    {
        tableentries = new ArrayList();
        listeners = new ArrayList();
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Link Direction";
                else
                    return cdma.isUplink() ? "Uplink" : "Downlink";
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Frequency";
                else
                    return (new StringBuilder()).append(cdma.getFrequency()).append(" MHz").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Bandwidth";
                else
                    return (new StringBuilder()).append(cdma.getSystemBandwidth()).append(" MHz").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Cell Radius";
                else
                    return (new StringBuilder()).append(cdma.getCellRadius()).append(" km").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "BitRate";
                else
                    return (new StringBuilder()).append(cdma.getVoiceBitRate()).append(" kbps").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Link Level Data";
                else
                    return cdma.getLinkLevelData();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Total Non Interfered Capacity";
                else
                    return (new StringBuilder()).append(cdma.getSimulatedUsersPerCell() * cdma.getNumberOfCellSitesInPowerControlCluster()).append(" users").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Non Interfered Capacity per Cell";
                else
                    return (new StringBuilder()).append(cdma.getSimulatedUsersPerCell()).append(" users").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Number of trials during capacity test";
                else
                    return Integer.valueOf(cdma.getNumberOfTrials());
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return cdma.isUplink() ? "Average Noise Rise, last snapshot" : "Obtained succes rate";
                else
                    return Double.valueOf(cdma.isUplink() ? ((CDMAUplinkSystem)cdma).calculateAverageNoiseRise_dB() : cdma.getSuccesRate() / (double)cdma.getNumberOfTrials());
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Number of ignored users (due to LLD)";
                else
                    return Integer.valueOf(cdma.getNumberOfNoLinkLevelDataUsers());
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Total dropped users";
                else
                    return Integer.valueOf(cdma.countDroppedUsers());
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Users dropped before interference";
                else
                    return Integer.valueOf(cdma.getDroppedBeforeInterference());
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Number of ignored users (due to LLD)";
                else
                    return Integer.valueOf(cdma.getNumberOfNoLinkLevelDataUsers());
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Propagation Model";
                else
                    return cdma.getPropagationModel();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return cdma.isUplink() ? "Highest PC loop count" : "Maximum Traffic Channel Proportion";
                else
                    return cdma.isUplink() ? Integer.toString(cdma.getHighestPcLoopCount()) : (new StringBuilder()).append(CDMASystem.fromWatt2dBm(cdma.getMaxTrafficChannelPowerInWatt())).append(" dBm").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Thermal Noise";
                else
                    return (new StringBuilder()).append(CDMASystem.fromWatt2dBm(cdma.getThermalNoiseInWatt())).append(" dBm").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Percentage of active users in soft handover";
                List users = cdma.getActiveUsers();
                int softHandover = 0;
                Iterator i$ = users.iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    UserTerminal user = (UserTerminal)i$.next();
                    if(user.isInSoftHandover())
                        softHandover++;
                } while(true);
                double percentage = ((double)softHandover / (double)users.size()) * 100D;
                return (new StringBuilder()).append(percentage).append("%").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Percentage of dropped users in soft handover";
                List users = cdma.getDroppedUsers();
                int softHandover = 0;
                Iterator i$ = users.iterator();
                do
                {
                    if(!i$.hasNext())
                        break;
                    UserTerminal user = (UserTerminal)i$.next();
                    if(user.isInSoftHandover())
                        softHandover++;
                } while(true);
                double percentage = ((double)softHandover / (double)users.size()) * 100D;
                return (new StringBuilder()).append(percentage).append("%").toString();
            }

            final CDMASystemTableModel this$0;

            
            {
                this$0 = CDMASystemTableModel.this;
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
        return tableentries.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
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

    public CDMASystem getSelectedCell()
    {
        return cdma;
    }

    public void setCDMASystem(CDMASystem _cdma)
    {
        cdma = _cdma;
        for(int i = 0; i < listeners.size(); i++)
            ((TableModelListener)listeners.get(i)).tableChanged(new TableModelEvent(this));

    }

    private CDMASystem cdma;
    private List tableentries;
    private List listeners;

}