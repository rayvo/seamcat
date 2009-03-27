// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/17/2009 2:02:25 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ClassPathHacker.java

package org.seamcat.integration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathHacker
{

    public ClassPathHacker()
    {
    }

    public static void addFile(String s)
        throws IOException
    {
        addFile(new File(s));
    }

    public static void addFile(File f)
        throws IOException
    {
        addURL(f.toURL());
    }

    public static void addURL(URL u)
        throws IOException
    {
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class sysclass = java/net/URLClassLoader;
        try
        {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] {
                u
            });
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }

    private static final Class parameters[] = {
        java/net/URL
    };

}