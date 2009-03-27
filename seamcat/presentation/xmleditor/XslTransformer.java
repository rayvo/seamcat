// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   XslTransformer.java

package org.seamcat.presentation.xmleditor;

import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XslTransformer
{

    public XslTransformer()
    {
        factory = TransformerFactory.newInstance();
    }

    public void process(Reader xmlFile, Reader xslFile, Writer output)
        throws TransformerException
    {
        process(((Source) (new StreamSource(xmlFile))), ((Source) (new StreamSource(xslFile))), ((Result) (new StreamResult(output))));
    }

    public void process(File xmlFile, File xslFile, Writer output)
        throws TransformerException
    {
        process(((Source) (new StreamSource(xmlFile))), ((Source) (new StreamSource(xslFile))), ((Result) (new StreamResult(output))));
    }

    public void process(File xmlFile, File xslFile, OutputStream out)
        throws TransformerException
    {
        process(((Source) (new StreamSource(xmlFile))), ((Source) (new StreamSource(xslFile))), ((Result) (new StreamResult(out))));
    }

    public void process(Source xml, Source xsl, Result result)
        throws TransformerException
    {
        try
        {
            Templates template = factory.newTemplates(xsl);
            Transformer transformer = template.newTransformer();
            transformer.transform(xml, result);
        }
        catch(TransformerConfigurationException tce)
        {
            throw new TransformerException(tce.getMessageAndLocation());
        }
        catch(TransformerException te)
        {
            throw new TransformerException(te.getMessageAndLocation());
        }
    }

    private TransformerFactory factory;
}
