// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EmailMessage.java

package org.seamcat.remote.notification;


public class EmailMessage
{

    public EmailMessage()
    {
    }

    public EmailMessage(String toAddress, String _fromAddress, String messageSubject, String messageBody)
    {
        this.toAddress = toAddress;
        fromAddress = _fromAddress;
        this.messageSubject = messageSubject;
        this.messageBody = messageBody;
    }

    public String getFromAddress()
    {
        return fromAddress;
    }

    public String getMessageBody()
    {
        return messageBody;
    }

    public String getMessageSubject()
    {
        return messageSubject;
    }

    public String getToAddress()
    {
        return toAddress;
    }

    public void setFromAddress(String string)
    {
        fromAddress = string;
    }

    public void setMessageBody(String string)
    {
        messageBody = string;
    }

    public void setMessageSubject(String messageSubject)
    {
        this.messageSubject = messageSubject;
    }

    public void setToAddress(String string)
    {
        toAddress = string;
    }

    private String toAddress;
    private String fromAddress;
    private String messageSubject;
    private String messageBody;
}
