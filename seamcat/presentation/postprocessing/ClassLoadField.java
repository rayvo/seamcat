// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ClassLoadField.java

package org.seamcat.presentation.postprocessing;

import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public abstract class ClassLoadField extends JTextField
    implements DocumentListener
{

    public ClassLoadField()
    {
        validClassName = false;
        invalid = new Color(255, 120, 120);
        valid = getBackground();
        getDocument().addDocumentListener(this);
        setToolTipText(STRINGS.getString("BATCH_FILTER_TOOLTIP"));
        validClassName = evaluateClassName();
        setBackground(validClassName ? valid : invalid);
    }

    public void insertUpdate(DocumentEvent e)
    {
        validClassName = evaluateClassName();
        setBackground(validClassName ? valid : invalid);
    }

    public void removeUpdate(DocumentEvent e)
    {
        validClassName = evaluateClassName();
        setBackground(validClassName ? valid : invalid);
    }

    public void changedUpdate(DocumentEvent e)
    {
        validClassName = evaluateClassName();
        setBackground(validClassName ? valid : invalid);
    }

    public void setDocument(Document doc)
    {
        super.setDocument(doc);
        getDocument().addDocumentListener(this);
    }

    public abstract boolean evaluateClassName();

    private static final ResourceBundle STRINGS;
    private boolean validClassName;
    private Color valid;
    private Color invalid;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
