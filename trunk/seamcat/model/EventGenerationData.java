// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   EventGenerationData.java

package org.seamcat.model;

import java.util.*;
import javax.swing.*;
import org.seamcat.presentation.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Referenced classes of package org.seamcat.model:
//            SeamcatComponent, NodeAttribute

public class EventGenerationData extends SeamcatComponent
{
    private static class DebugModeVerifier extends InputVerifier
    {

        public boolean verify(JComponent input)
        {
            return true;
        }

        private DebugModeVerifier()
        {
        }
    }

    private static class TerminationConditionVerifier extends InputVerifier
    {

        public boolean verify(JComponent input)
        {
            return true;
        }

        private TerminationConditionVerifier()
        {
        }

    }

    private static class ExpectedDurationVerifier extends InputVerifier
    {

        public boolean verify(JComponent input)
        {
            boolean valid;
            try
            {
                if(input instanceof JTextField)
                {
                    double value = Double.parseDouble(((JTextField)input).getText());
                    valid = value > 0.0D && value < 500000D;
                } else
                {
                    valid = false;
                }
            }
            catch(Exception e)
            {
                valid = false;
            }
            return valid;
        }

        private ExpectedDurationVerifier()
        {
        }

    }

    private static class NumberOfEventsVerifier extends InputVerifier
    {

        public boolean verify(JComponent input)
        {
            boolean valid;
            try
            {
                if(input instanceof JTextField)
                {
                    int value = Integer.parseInt(((JTextField)input).getText());
                    valid = value > 0 && value < 0x7a120;
                } else
                {
                    valid = false;
                }
            }
            catch(Exception e)
            {
                valid = false;
            }
            return valid;
        }

        private NumberOfEventsVerifier()
        {
        }

    }


    public EventGenerationData(Node parent)
    {
        numberOfEvents = 20000;
        debugMode = false;
        timeLimited = false;
        useTestCDMAAlgoritm = false;
        this.parent = parent;
        ensureNodeAttributesLoaded();
    }

    public EventGenerationData(Node parent, Element element)
    {
        this(parent);
        setNumberOfEvents(Integer.parseInt(element.getAttribute("numberOfEvents")));
        setTerminationCondition(element.getAttribute("terminationCondition"));
        setTimeLeft(Double.parseDouble(element.getAttribute("expectedDuration")));
        try
        {
            setDebugMode(Boolean.valueOf(element.getAttribute("debugMode")).booleanValue());
        }
        catch(Exception ex)
        {
            setDebugMode(false);
        }
        try
        {
            setTimeLimited(Boolean.valueOf(element.getAttribute("timeLimited")).booleanValue());
        }
        catch(Exception ex)
        {
            setTimeLimited(false);
        }
    }

    protected void initNodeAttributes()
    {
        List nodeList = new ArrayList();
        nodeList.add(new NodeAttribute("Number of events", "", new Integer(numberOfEvents), "Integer", null, false, true, VERIFIER_NUMBER_OF_EVENTS));
        nodeList.add(new NodeAttribute("Termination condition", "", EVENT_TERMINATION_CONDITION[0], "String", EVENT_TERMINATION_CONDITION, true, true, VERIFIER_TERMINATION_CONDITION));
        nodeList.add(new NodeAttribute("Expected duration", "min", new Double("1.0"), "Float", null, false, true, VERIFIER_EXPECTED_DURATION));
        nodeList.add(new NodeAttribute("Debug Mode", "", Boolean.valueOf(debugMode), "Boolean", DEBUG_MODES, true, true, null));
        nodeAttributes = (NodeAttribute[])nodeList.toArray(new NodeAttribute[nodeList.size()]);
    }

    public Element toElement(Document doc)
    {
        Element element = doc.createElement("EventGenerationData");
        element.setAttribute("numberOfEvents", Integer.toString(getNumberOfEvents()));
        element.setAttribute("terminationCondition", getTerminationCondition());
        element.setAttribute("expectedDuration", Double.toString(getTimeLeft()));
        element.setAttribute("debugMode", Boolean.toString(debugMode));
        element.setAttribute("timeLimited", Boolean.toString(timeLimited));
        return element;
    }

    public boolean isInDebugMode()
    {
        return debugMode;
    }

    public int getNumberOfEvents()
    {
        return numberOfEvents;
    }

    public void setNumberOfEvents(int numberOfEvents)
        throws IllegalArgumentException
    {
        setValueAt(Integer.valueOf(numberOfEvents), 0);
    }

    public InputVerifier getNumberOfEventsInputVerifier()
    {
        return nodeAttributes[0].getInputVerifier();
    }

    public double getTimeLeft()
    {
        return Double.parseDouble(nodeAttributes[2].getValue().toString());
    }

    public void setTimeLeft(double expectedDuration)
    {
        setValueAt(Double.valueOf(expectedDuration), 2);
    }

    public InputVerifier getExpectedDurationInputVerifier()
    {
        return nodeAttributes[2].getInputVerifier();
    }

    public String getTerminationCondition()
    {
        return (String)nodeAttributes[1].getValue();
    }

    public int getTerminationConditionInt()
    {
        String stringValue = (String)nodeAttributes[1].getValue();
        int terminationCondition;
        if(stringValue.equals(EVENT_TERMINATION_CONDITION[1]))
            terminationCondition = 1;
        else
        if(stringValue.equals(EVENT_TERMINATION_CONDITION[0]))
            terminationCondition = 0;
        else
        if(stringValue.equals(EVENT_TERMINATION_CONDITION[2]))
            terminationCondition = 2;
        else
            throw new IllegalStateException("Illegal termination condition state");
        return terminationCondition;
    }

    public void setTerminationCondition(String terminationCondition)
    {
        setValueAt(terminationCondition, 1);
    }

    public InputVerifier getTerminationConditionInputVerifier()
    {
        return nodeAttributes[1].getInputVerifier();
    }

    public String toString()
    {
        return STRINGLIST.getString("NODE_TEXT_EVENT_GENERATION");
    }

    public boolean isLeaf()
    {
        return true;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Object valueAt;
        if(columnIndex == 0)
            valueAt = nodeAttributes[rowIndex].getName();
        else
        if(columnIndex == 1)
            valueAt = nodeAttributes[rowIndex].getValue();
        else
        if(columnIndex == 2)
            valueAt = nodeAttributes[rowIndex].getUnit();
        else
        if(columnIndex == 3)
            valueAt = nodeAttributes[rowIndex].getType();
        else
            throw new IllegalArgumentException("Illegal column index requested");
        return valueAt;
    }

    public int getColumnCount()
    {
        return 4;
    }

    public void setValueAt(Object aValue, int rowIndex)
    {
        setValueAt(aValue, rowIndex, 1);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if(columnIndex == 1)
        {
            switch(rowIndex)
            {
            case 0: // '\0'
                numberOfEvents = Integer.parseInt(aValue.toString());
                break;

            case 3: // '\003'
                debugMode = ((Boolean)aValue).booleanValue();
                break;

            default:
                throw new IllegalStateException((new StringBuilder()).append("RowIndex ").append(rowIndex).append(" unknown in EventGenerationData.setValueAt()").toString());

            case 1: // '\001'
            case 2: // '\002'
                break;
            }
            if(nodeAttributes[rowIndex].getType().equalsIgnoreCase("FLOAT"))
                nodeAttributes[rowIndex].setValue(new Double(aValue.toString()));
            else
            if(nodeAttributes[rowIndex].getType().equalsIgnoreCase("INTEGER"))
                nodeAttributes[rowIndex].setValue(new Integer(aValue.toString()));
            else
                nodeAttributes[rowIndex].setValue(aValue);
        }
    }

    public Node getParent()
    {
        return parent;
    }

    public void setDebugMode(boolean value)
    {
        setValueAt(Boolean.valueOf(value), 3);
    }

    public boolean isTimeLimited()
    {
        return timeLimited;
    }

    public void setTimeLimited(boolean timeLimited)
    {
        this.timeLimited = timeLimited;
    }

    public boolean getUseTestCDMAAlgoritm()
    {
        return useTestCDMAAlgoritm;
    }

    public void setUseTestCDMAAlgoritm(boolean useTestCDMAAlgoritm)
    {
        this.useTestCDMAAlgoritm = useTestCDMAAlgoritm;
    }

    public static final String EVENT_TERMINATION_CONDITION[] = {
        "Number of events", "DEE driven", "Expected duration"
    };
    private static final Boolean DEBUG_MODES[];
    public static final int NB_EVENTS = 0;
    public static final int DEE_DRIVEN = 1;
    public static final int EXPECTED_DURATION = 2;
    private static final InputVerifier VERIFIER_EXPECTED_DURATION = new ExpectedDurationVerifier();
    private static final InputVerifier VERIFIER_NUMBER_OF_EVENTS = new NumberOfEventsVerifier();
    private static final InputVerifier VERIFIER_TERMINATION_CONDITION = new TerminationConditionVerifier();
    private static final ResourceBundle STRINGLIST;
    private Node parent;
    private int numberOfEvents;
    private boolean debugMode;
    private boolean timeLimited;
    private boolean useTestCDMAAlgoritm;

    static 
    {
        DEBUG_MODES = (new Boolean[] {
            Boolean.TRUE, Boolean.FALSE
        });
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}