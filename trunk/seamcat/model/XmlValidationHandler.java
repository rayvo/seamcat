// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:26 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   XmlValidationHandler.java

package org.seamcat.model;

import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.seamcat.presentation.MainWindow;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlValidationHandler extends DefaultHandler
{

    public XmlValidationHandler(boolean silent)
    {
        validationError = false;
        saxParseException = null;
        silentMode = false;
        silentMode = silent;
    }

    public void error(SAXParseException exception)
        throws SAXException
    {
        validationError = true;
        saxParseException = exception;
        LOG.error((new StringBuilder()).append("Error parsing XML: ").append(exception.getMessage()).toString());
        if(!silentMode)
            JOptionPane.showMessageDialog(MainWindow.getInstance(), exception.getMessage(), "Error parsing XML", 2);
    }

    public void fatalError(SAXParseException exception)
        throws SAXException
    {
        validationError = true;
        saxParseException = exception;
        if(!silentMode)
            JOptionPane.showMessageDialog(MainWindow.getInstance(), exception.getMessage(), "Fatal Error while parsing XML", 0);
        LOG.error("Fatal Error while parsing XML", exception);
    }

    public void warning(SAXParseException exception)
        throws SAXException
    {
        if(!silentMode)
            JOptionPane.showMessageDialog(MainWindow.getInstance(), exception.getMessage(), "XML Parsing Warning", 2);
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/model/XmlValidationHandler);
    public boolean validationError;
    public SAXParseException saxParseException;
    private boolean silentMode;

}