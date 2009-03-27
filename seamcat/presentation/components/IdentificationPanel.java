// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IdentificationPanel.java

package org.seamcat.presentation.components;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import org.seamcat.model.SeamcatComponent;
import org.seamcat.presentation.LabeledPairLayout;

public class IdentificationPanel extends JPanel
{

    public IdentificationPanel()
    {
        nameField = new JTextField();
        descriptionField = new JTextArea(3, 20);
        LabeledPairLayout layout = new LabeledPairLayout();
        layout.setXGap(5);
        layout.setYGap(5);
        setLayout(layout);
        nameField.setColumns(20);
        JLabel nameLabel = new JLabel(STRINGS.getString("IDENTIFICATION_NAME"));
        JLabel descriptionLabel = new JLabel(STRINGS.getString("IDENTIFICATION_DESC"));
        add(nameLabel, "label");
        add(nameField, "field");
        add(descriptionLabel, "label");
        add(new JScrollPane(descriptionField), "field");
        setBorder(new TitledBorder(STRINGS.getString("IDENTIFICATION_TITLE")));
    }

    public void setComponent(SeamcatComponent component)
    {
        setName(component.getReference());
        setDescription(component.getDescription());
    }

    public void updateModel(SeamcatComponent component)
    {
        component.setReference(getName());
        component.setDescription(getDescription());
        component.updateNodeAttributes();
    }

    public String getName()
    {
        return nameField.getText();
    }

    public void setDescription(String description)
    {
        descriptionField.setText(description);
    }

    public void setName(String name)
    {
        nameField.setText(name);
    }

    public String getDescription()
    {
        return descriptionField.getText();
    }

    public void clear()
    {
        setDescription("");
    }

    private static final ResourceBundle STRINGS;
    private JTextField nameField;
    private JTextArea descriptionField;

    static 
    {
        STRINGS = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }
}
