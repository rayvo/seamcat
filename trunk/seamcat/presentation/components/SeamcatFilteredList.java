// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatFilteredList.java

package org.seamcat.presentation.components;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Document;

public class SeamcatFilteredList extends JList
{
    class FilterField extends JTextField
        implements DocumentListener
    {

        public void insertUpdate(DocumentEvent e)
        {
            filterModel.refilter();
        }

        public void removeUpdate(DocumentEvent e)
        {
            filterModel.refilter();
        }

        public void changedUpdate(DocumentEvent e)
        {
            filterModel.refilter();
        }

        final SeamcatFilteredList this$0;

        public FilterField(int width)
        {
            this$0 = SeamcatFilteredList.this;
            super(width);
            getDocument().addDocumentListener(this);
            setToolTipText(SeamcatFilteredList.STRINGLIST.getString("BATCH_FILTER_TOOLTIP"));
        }
    }

    class FilterModel extends AbstractListModel
        implements ListDataListener
    {

        public Object getElementAt(int index)
        {
            if(index < filterItems.size())
                return filterItems.get(index);
            else
                return null;
        }

        public int getSize()
        {
            return filterItems.size();
        }

        public void setListModel(ListModel m)
        {
            if(origModel != null)
                origModel.removeListDataListener(this);
            origModel = m;
            origModel.addListDataListener(this);
            refilter();
        }

        private void refilter()
        {
            filterItems.clear();
            String term = getFilterField().getText();
            if(term == null || term.equals(""))
            {
                for(int i = 0; i < origModel.getSize(); i++)
                    filterItems.add(origModel.getElementAt(i));

            } else
            {
                for(int i = 0; i < origModel.getSize(); i++)
                {
                    Object o = origModel.getElementAt(i);
                    if(o.toString().toUpperCase().indexOf(term.toUpperCase(), 0) != -1)
                        filterItems.add(o);
                }

            }
            fireContentsChanged(this, 0, getSize());
        }

        public void intervalAdded(ListDataEvent e)
        {
            refilter();
        }

        public void intervalRemoved(ListDataEvent e)
        {
            refilter();
        }

        public void contentsChanged(ListDataEvent e)
        {
            refilter();
        }

        private ListModel origModel;
        private ArrayList filterItems;
        final SeamcatFilteredList this$0;


        public FilterModel()
        {
            this$0 = SeamcatFilteredList.this;
            super();
            filterItems = new ArrayList();
        }
    }


    public SeamcatFilteredList()
    {
        filterField = new FilterField(20);
        filterModel = new FilterModel();
        super.setModel(filterModel);
    }

    public SeamcatFilteredList(ListModel m)
    {
        filterField = new FilterField(20);
        filterModel = new FilterModel();
        super.setModel(filterModel);
        filterModel.setListModel(m);
    }

    public void setModel(ListModel m)
    {
        filterModel.setListModel(m);
    }

    public JTextField getFilterField()
    {
        return filterField;
    }

    private static final ResourceBundle STRINGLIST;
    private final FilterField filterField;
    private final FilterModel filterModel;
    private static final int DEFAULT_FIELD_WIDTH = 20;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }


}
