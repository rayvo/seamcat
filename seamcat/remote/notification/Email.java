// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Email.java

package org.seamcat.remote.notification;

import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import org.seamcat.remote.util.ServerSetting;

// Referenced classes of package org.seamcat.remote.notification:
//            EmailMessage

public class Email
{

    public Email()
    {
    }

    public String sendMessage(ServerSetting serverSetting, EmailMessage emailMessage)
    {
        Transport transport;
        Session session;
        transport = null;
        Properties props = new Properties();
        props.put("mail.smtp.host", serverSetting.getSmtpServer());
        if(serverSetting.getSmtpRequireAuthentication())
            props.put("mail.smtp.auth", "true");
        if(serverSetting.getEmailDebug())
            props.put("mail.debug", "true");
        session = Session.getInstance(props, null);
        session.setDebug(serverSetting.getEmailDebug());
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(emailMessage.getFromAddress()));
        InternetAddress address[] = {
            new InternetAddress(emailMessage.getToAddress())
        };
        msg.setRecipients(javax.mail.Message.RecipientType.TO, address);
        msg.setSubject(emailMessage.getMessageSubject());
        msg.setSentDate(new Date());
        msg.setText(emailMessage.getMessageBody());
        transport = session.getTransport("smtp");
        transport.connect(serverSetting.getSmtpServer(), serverSetting.getEmailUser(), serverSetting.getEmailUserPw());
        transport.sendMessage(msg, msg.getAllRecipients());
        try
        {
            transport.close();
        }
        catch(MessagingException me) { }
        break MISSING_BLOCK_LABEL_507;
        MessagingException mex;
        mex;
        LOG.error("Error occured while trying to send email", mex);
        Exception ex = mex;
        do
        {
            if(ex instanceof SendFailedException)
            {
                SendFailedException sfex = (SendFailedException)ex;
                javax.mail.Address invalid[] = sfex.getInvalidAddresses();
                if(invalid != null)
                {
                    LOG.info("sendMessage(ServerSetting, EmailMessage) -     ** Invalid Addresses");
                    if(invalid != null)
                    {
                        for(int i = 0; i < invalid.length; i++)
                            LOG.info((new StringBuilder()).append("sendMessage(ServerSetting, EmailMessage)").append(invalid[i]).toString());

                    }
                }
                javax.mail.Address validUnsent[] = sfex.getValidUnsentAddresses();
                if(validUnsent != null)
                {
                    LOG.info("sendMessage(ServerSetting, EmailMessage) -     ** ValidUnsent Addresses");
                    if(validUnsent != null)
                    {
                        for(int i = 0; i < validUnsent.length; i++)
                            LOG.info((new StringBuilder()).append("sendMessage(ServerSetting, EmailMessage)").append(validUnsent[i]).toString());

                    }
                }
                javax.mail.Address validSent[] = sfex.getValidSentAddresses();
                if(validSent != null)
                {
                    LOG.info("sendMessage(ServerSetting, EmailMessage) -     ** ValidSent Addresses");
                    if(validSent != null)
                    {
                        for(int i = 0; i < validSent.length; i++)
                            LOG.info((new StringBuilder()).append("sendMessage(ServerSetting, EmailMessage)").append(validSent[i]).toString());

                    }
                }
            }
            if(ex instanceof MessagingException)
                ex = ((MessagingException)ex).getNextException();
            else
                ex = null;
        } while(ex != null);
        try
        {
            transport.close();
        }
        catch(MessagingException me) { }
        break MISSING_BLOCK_LABEL_507;
        Exception exception;
        exception;
        try
        {
            transport.close();
        }
        catch(MessagingException me) { }
        throw exception;
        return "";
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/remote/notification/Email);

}
