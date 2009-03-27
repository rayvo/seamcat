// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SeamcatIcons.java

package org.seamcat.presentation;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;

// Referenced classes of package org.seamcat.presentation:
//            Icons

public class SeamcatIcons
{

    private SeamcatIcons()
    {
    }

    public static ImageIcon getImageIcon(String identifier)
    {
        return getImageIcon(identifier, 0);
    }

    public static BufferedImage getBufferedImage(String identifier, int imageSize)
    {
        ImageIcon icon = getImageIcon(identifier, imageSize);
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconWidth(), 2);
        Graphics gr = image.createGraphics();
        gr.drawImage(icon.getImage(), 0, 0, null);
        gr.dispose();
        return image;
    }

    public static Image getImage(String identifier, int imageSize)
    {
        try
        {
            return getImageIcon(identifier, imageSize).getImage();
        }
        catch(RuntimeException ex)
        {
            return null;
        }
    }

    public static ImageIcon getImageIcon(String identifier, int imageSize)
    {
        String postfix = null;
        switch(imageSize)
        {
        case 0: // '\0'
            postfix = POSTFIX_16x16;
            break;

        case 1: // '\001'
            postfix = POSTFIX_32x32;
            break;

        case 2: // '\002'
            postfix = "";
            break;

        case 3: // '\003'
            postfix = POSTFIX_16x16_DISABLED;
            break;

        default:
            throw new IllegalArgumentException("Invalid Image Size");
        }
        ImageIcon icon = (ImageIcon)images.get((new StringBuilder()).append(identifier).append(postfix).toString());
        if(icon == null)
        {
            try
            {
                icon = new ImageIcon(org/seamcat/presentation/SeamcatIcons.getResource((new StringBuilder()).append(IMAGE_URL_PREFIX).append(Icons.getString(identifier)).append(postfix).toString()));
                LOG.debug((new StringBuilder()).append("Loaded image: ").append(IMAGE_URL_PREFIX).append(Icons.getString(identifier)).append(postfix).toString());
            }
            catch(Exception e)
            {
                LOG.error((new StringBuilder()).append("Unable to load image: ").append(identifier).append(" [").append(IMAGE_URL_PREFIX).append(Icons.getString(identifier)).append(postfix).append("] due to ").append(e).toString());
            }
            images.put((new StringBuilder()).append(identifier).append(postfix).toString(), icon);
        }
        return icon;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/SeamcatIcons);
    private static final ResourceBundle STRINGLIST;
    private static final String IMAGE_URL_PREFIX;
    private static final String POSTFIX_16x16;
    private static final String POSTFIX_32x32;
    private static final String POSTFIX_16x16_DISABLED;
    private static Map images = new HashMap();
    public static final int IMAGE_SIZE_16x16 = 0;
    public static final int IMAGE_SIZE_32x32 = 1;
    public static final int IMAGE_SIZE_TOOLBAR = 0;
    public static final int IMAGE_SIZE_CUSTOM = 2;
    public static final int IMAGE_SIZE_16x16_DISABLED = 3;
    private static final SeamcatIcons instance = new SeamcatIcons();

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        IMAGE_URL_PREFIX = STRINGLIST.getString("GENERAL_IMAGE_LOCATION");
        POSTFIX_16x16 = STRINGLIST.getString("GENERAL_POSTFIX_16x16_ICONS");
        POSTFIX_32x32 = STRINGLIST.getString("GENERAL_POSTFIX_32x32_ICONS");
        POSTFIX_16x16_DISABLED = STRINGLIST.getString("GENERAL_POSTFIX_16x16_DISABLED_ICONS");
    }
}
