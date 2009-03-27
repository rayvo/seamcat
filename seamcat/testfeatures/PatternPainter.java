// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PatternPainter.java

package org.seamcat.testfeatures;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.seamcat.cdma.CDMAUplinkSystem;
import org.seamcat.function.DiscreteFunction;
import org.seamcat.function.Point2D;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.Antenna;
import org.seamcat.model.core.AntennaPattern;

public class PatternPainter extends JComponent
{

    public PatternPainter()
    {
        frame = new JFrame();
        frame.getContentPane().add(this, "Center");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(0);
    }

    public void setPattern(AntennaPattern _pattern)
    {
        pattern = _pattern;
    }

    public void openFrame()
    {
        frame.setVisible(true);
    }

    public void paintComponent(Graphics graphics)
    {
        java.util.List points = pattern.getPattern().getPointsList();
        double min = ((Point2D)points.get(0)).getY();
        double max = ((Point2D)points.get(points.size() - 1)).getY();
        int x0 = getWidth() / 2;
        int y0 = getHeight() / 2;
        graphics.translate(x0, y0);
        double scale = (double)(x0 - 20) / (max - min);
        double zeroFactor = scale * 0.0D;
        Graphics2D gr = (Graphics2D)graphics;
        for(int i = 0; i <= x0; i += x0 / 20)
            gr.drawLine(i, y0, i, -y0);

        for(int i = 0; i >= -x0; i -= x0 / 20)
            gr.drawLine(i, y0, i, -y0);

        for(int i = 0; i <= x0; i += x0 / 20)
            gr.drawLine(x0, i, -x0, i);

        for(int i = 0; i >= -x0; i -= x0 / 20)
            gr.drawLine(x0, i, -x0, i);

        gr.setColor(Color.RED);
        gr.fillOval(5, 5, 10, 10);
        gr.setColor(Color.BLUE);
        int x = (int)((double)x0 + 3D * scale);
        int y = (int)((double)y0 + 3D * scale);
        boolean notFirst = false;
        int oldX = 0;
        int oldY = 0;
        for(Iterator i$ = points.iterator(); i$.hasNext();)
        {
            Point2D point = (Point2D)i$.next();
            x = (int)(Mathematics.cosD(point.getX()) * (point.getY() * scale + zeroFactor));
            y = (int)(Mathematics.sinD(point.getX()) * (point.getY() * scale + zeroFactor));
            gr.fillOval(x - 2, y - 2, 4, 4);
            if(notFirst)
                gr.drawLine(x, y, oldX, oldY);
            else
                notFirst = true;
            oldX = x;
            oldY = y;
        }

    }

    public static void main(String args[])
    {
        JFrame frame = new JFrame();
        CDMAUplinkSystem cdma = new CDMAUplinkSystem();
        PatternPainter pattern = new PatternPainter();
        pattern.pattern = cdma.getTriSectorAntenna().getHorizontalPattern();
        frame.getContentPane().add(pattern, "Center");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }

    private AntennaPattern pattern;
    private JFrame frame;
}
