// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:24 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CDMAXMLUtils.java

package org.seamcat.cdma.xml;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.seamcat.cdma.CDMALinkLevelData;
import org.seamcat.model.Model;
import org.seamcat.model.XmlValidationHandler;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class CDMAXMLUtils
{

    private CDMAXMLUtils()
    {
    }

    public static void saveAsXML(Collection data, File file)
        throws IOException, TransformerException, ParserConfigurationException
    {
        DocumentBuilder db = Model.SEAMCAT_DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        db.setErrorHandler(new XmlValidationHandler(false));
        Document doc = db.newDocument();
        doc.appendChild(createCDMALibraryElement(doc, data));
        if(!file.exists())
            file.mkdir();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new FileOutputStream(file));
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transformer = transFactory.newTransformer();
        transformer.transform(source, result);
        result.getOutputStream().close();
    }

    public static List loadFromXML(File file, DocumentBuilder docBuilder)
        throws IOException, SAXException
    {
        if(docBuilder == null)
            try
            {
                docBuilder = Model.getSeamcatDocumentBuilderFactory().newDocumentBuilder();
            }
            catch(ParserConfigurationException ex)
            {
                LOG.error("An Error occured while creating default document builder", ex);
                return Collections.EMPTY_LIST;
            }
        docBuilder.setErrorHandler(new XmlValidationHandler(false));
        try
        {
            Document doc = docBuilder.parse(file);
            Element lib = doc.getDocumentElement();
            List data = lib == null ? Collections.EMPTY_LIST : getDataFromLibrary(lib);
            return data;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    public static Element createCDMALibraryElement(Document doc, Collection data)
    {
        Element element = doc.createElement("cdmalld");
        CDMALinkLevelData lld;
        for(Iterator i$ = data.iterator(); i$.hasNext(); element.appendChild(lld.toElement(doc)))
            lld = (CDMALinkLevelData)i$.next();

        return element;
    }

    public static List getDataFromLibrary(Element e)
    {
        List datas = new ArrayList();
        NodeList lldNodes = e.getElementsByTagName("cdmalld");
        Element lld = null;
        if(lldNodes.getLength() > 0)
            lld = (Element)lldNodes.item(0);
        else
        if(e.getLocalName().equals("cdmalld"))
            lld = e;
        if(lld != null)
        {
            NodeList links = lld.getElementsByTagName("CDMA-Link-level-data");
            int i = 0;
            for(int stop = links.getLength(); i < stop; i++)
                datas.add(new CDMALinkLevelData((Element)links.item(i)));

        }
        return datas;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/cdma/xml/CDMAXMLUtils);

}