// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:22 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ImageIconBorder.java

package org.seamcat.calculator;

import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;

public class ImageIconBorder extends AbstractBorder
{
    private class ButtonBorder extends AbstractBorder
    {

        public Insets getBorderInsets(Component c, Insets insets)
        {
            insets.set(0, 0, 0, 0);
            return insets;
        }

        public Insets getBorderInsets(Component c)
        {
            return new Insets(0, 0, 0, icon.getIconWidth());
        }

        public Rectangle getInteriorRectangle(Component c, int x, int y, int width, int height)
        {
            return super.getInteriorRectangle(c, x, y, width - icon.getIconWidth(), height);
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
        {
            Graphics2D gr = (Graphics2D)g;
            ImageIcon img;
            if(c.isEnabled())
                img = icon;
            else
                img = disabledIcon;
            gr.drawImage(img.getImage(), x + ((width - icon.getIconWidth()) + 2), y, null);
        }

        private ImageIcon icon;
        private ImageIcon disabledIcon;
        final ImageIconBorder this$0;

        private ButtonBorder(ImageIcon _icon, ImageIcon _disabledIcon)
        {
            this$0 = ImageIconBorder.this;
            super();
            icon = _icon;
            if(_disabledIcon == null)
                disabledIcon = icon;
            else
                disabledIcon = _disabledIcon;
        }

    }


    public ImageIconBorder(JComponent owner, ImageIcon icon, ImageIcon disabledIcon)
    {
        buttonBorder = new ButtonBorder(icon, disabledIcon);
        border = new CompoundBorder(owner.getBorder(), buttonBorder);
    }

    public Insets getBorderInsets(Component c, Insets insets)
    {
        return border.getBorderInsets(c, insets);
    }

    public Insets getBorderInsets(Component c)
    {
        return border.getBorderInsets(c);
    }

    public Rectangle getInteriorRectangle(Component c, int x, int y, int width, int height)
    {
        return border.getInteriorRectangle(c, x, y, width, height);
    }

    public boolean isBorderOpaque()
    {
        return border.isBorderOpaque();
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        border.paintBorder(c, g, x, y, width, height);
    }

    private ButtonBorder buttonBorder;
    private CompoundBorder border;
}