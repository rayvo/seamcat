// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScenarioCheckResult.java

package org.seamcat.model.scenariocheck;

import java.util.*;

public class ScenarioCheckResult
{
    public static final class Outcome extends Enum
    {

        public static Outcome[] values()
        {
            return (Outcome[])$VALUES.clone();
        }

        public static Outcome valueOf(String name)
        {
            return (Outcome)Enum.valueOf(org/seamcat/model/scenariocheck/ScenarioCheckResult$Outcome, name);
        }

        public static final Outcome OK;
        public static final Outcome FAILED;
        private static final Outcome $VALUES[];

        static 
        {
            OK = new Outcome("OK", 0);
            FAILED = new Outcome("FAILED", 1);
            $VALUES = (new Outcome[] {
                OK, FAILED
            });
        }

        private Outcome(String s, int i)
        {
            super(s, i);
        }
    }


    public ScenarioCheckResult()
    {
        outcome = Outcome.OK;
        messageList = new ArrayList();
    }

    public ScenarioCheckResult(Outcome out, String name, List messages)
    {
        outcome = Outcome.OK;
        messageList = new ArrayList();
        outcome = out;
        checkName = name;
        messageList.addAll(messages);
    }

    public String getCheckName()
    {
        return checkName == null ? "Undefined checkName" : checkName;
    }

    public void setCheckName(String checkName)
    {
        this.checkName = checkName;
    }

    public Outcome getOutcome()
    {
        return outcome;
    }

    public void setOutcome(Outcome outcome)
    {
        if(outcome == null)
        {
            throw new IllegalArgumentException("Outcome cant be null");
        } else
        {
            this.outcome = outcome;
            return;
        }
    }

    public void addMessage(String message)
    {
        if(message == null)
        {
            throw new IllegalArgumentException("Message cant be null");
        } else
        {
            messageList.add(message);
            return;
        }
    }

    public void removeMessage(String message)
    {
        messageList.remove(message);
    }

    public void clearMessages()
    {
        messageList.clear();
    }

    public List getMessages()
    {
        return Collections.unmodifiableList(messageList);
    }

    private Outcome outcome;
    private String checkName;
    private final List messageList;
}
