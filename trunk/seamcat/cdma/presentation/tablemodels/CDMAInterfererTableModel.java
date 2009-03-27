// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAInterfererTableModel.java

package org.seamcat.cdma.presentation.tablemodels;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.seamcat.cdma.CDMAInterferer;
import org.seamcat.model.Antenna;

// Referenced classes of package org.seamcat.cdma.presentation.tablemodels:
//            CDMAElementTableValue

public class CDMAInterfererTableModel
    implements TableModel
{

    public CDMAInterfererTableModel()
    {
        listeners = new ArrayList();
        tableentries = new ArrayList();
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Type";
                else
                    return "Interfering Transmitter";
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Transmit Power";
                else
                    return (new StringBuilder()).append(selectedInterferer.getTxPower()).append(" dBm").toString();
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
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
                    return (new StringBuilder()).append("(").append(selectedInterferer.getLocationX()).append(", ").append(selectedInterferer.getLocationY()).append(")").toString();
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
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
                    return (new StringBuilder()).append(selectedInterferer.getFrequency()).append(" MHz").toString();
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Antenna Peak Gain";
                else
                    return (new StringBuilder()).append(selectedInterferer.getFixedAntennaGain()).append(" dB").toString();
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Antenna Height";
                else
                    return (new StringBuilder()).append(selectedInterferer.getAntennaHeight()).append(" meters").toString();
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Use Horizontal Antenna Pattern";
                else
                    return Boolean.toString(!selectedInterferer.isUsingFixedGain() && selectedInterferer.getAntenna().getUseHorizontalPattern());
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Use Vertical Antenna Pattern";
                else
                    return Boolean.toString(!selectedInterferer.isUsingFixedGain() && selectedInterferer.getAntenna().getUseVerticalPattern());
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
                super();
            }
        }
);
        tableentries.add(new CDMAElementTableValue() {

            public Object getValue(int columnIndex)
            {
                if(columnIndex == 0)
                    return "Use Spherical Antenna Pattern";
                else
                    return Boolean.toString(!selectedInterferer.isUsingFixedGain() && selectedInterferer.getAntenna().getUseSphericalPattern());
            }

            final CDMAInterfererTableModel this$0;

            
            {
                this$0 = CDMAInterfererTableModel.this;
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
        if(selectedInterferer == null)
            return 0;
        else
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

    public CDMAInterferer getSelectedCell()
    {
        return selectedInterferer;
    }

    public void setSelectedInterferer(CDMAInterferer selectedInterferer)
    {
        this.selectedInterferer = selectedInterferer;
        for(int i = 0; i < listeners.size(); i++)
            ((TableModelListener)listeners.get(i)).tableChanged(new TableModelEvent(this));

    }

    private CDMAInterferer selectedInterferer;
    private List listeners;
    private List tableentries;

}