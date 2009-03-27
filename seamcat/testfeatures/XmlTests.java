// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XmlTests.java

package org.seamcat.testfeatures;

import java.io.*;
import javax.xml.parsers.*;
import org.apache.log4j.Logger;
import org.seamcat.model.Model;
import org.seamcat.model.XmlValidationHandler;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class XmlTests
{

    public XmlTests()
    {
    }

    public static void main(String args[])
    {
        try
        {
            DocumentBuilderFactory FACTORY = DocumentBuilderFactory.newInstance();
            FACTORY.setNamespaceAware(true);
            FACTORY.setValidating(true);
            FACTORY.setIgnoringElementContentWhitespace(true);
            FACTORY.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            DocumentBuilder db = FACTORY.newDocumentBuilder();
            db.setErrorHandler(new XmlValidationHandler(false));
            String filename = "c:\\temp\\seamcat2.xml";
            System.out.printf("Filename is <%s>\n", new Object[] {
                filename
            });
            Document doc = db.parse(new File(filename));
            expnNode(doc.getChildNodes(), new int[0]);
        }
        catch(IllegalArgumentException ex)
        {
            LOG.error("An Error occured", ex);
        }
        catch(ParserConfigurationException ex)
        {
            LOG.error("An Error occured", ex);
        }
        catch(SAXException ex)
        {
            LOG.error("An Error occured", ex);
        }
        catch(IOException ex)
        {
            LOG.error("An Error occured", ex);
        }
    }

    private static void expnNode(NodeList n, int level[])
    {
        int length = n.getLength();
        System.out.printf((new StringBuilder()).append(printLevel(level)).append("NodeList <%s> has %d children\n").toString(), new Object[] {
            n.toString(), Integer.valueOf(length)
        });
        if(length > 0)
        {
            int i[] = new int[level.length + 1];
            System.arraycopy(level, 0, i, 0, level.length);
            i[i.length - 1] = 1;
            for(int x = 0; x < length; x++)
            {
                expnNode(n.item(x).getChildNodes(), i);
                i[i.length - 1]++;
            }

        }
    }

    private static String printLevel(int level[])
    {
        StringBuffer sb = new StringBuffer();
        int arr$[] = level;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            int i = arr$[i$];
            sb.append(i).append('.');
        }

        if(sb.length() > 0)
        {
            sb.deleteCharAt(sb.length() - 1);
            sb.append(' ');
        }
        return sb.toString();
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/Model);

}
