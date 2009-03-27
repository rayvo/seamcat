// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ModalLockTest.java

package org.seamcat.testfeatures;

import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
import javax.swing.*;

public class ModalLockTest extends JDialog
{

    public ModalLockTest(Frame f)
    {
        super(f, true);
        frame = f;
        locked = true;
        init();
        addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e)
            {
                System.out.println((new StringBuilder()).append("Dialog #").append(index).append(" gained focus").toString());
            }

            public void windowLostFocus(WindowEvent e)
            {
                System.out.println((new StringBuilder()).append("Dialog #").append(index).append(" lost focus").toString());
            }

            final ModalLockTest this$0;

            
            {
                this$0 = ModalLockTest.this;
                super();
            }
        }
);
    }

    public ModalLockTest(Frame f, JDialog d)
    {
        super(d, true);
        frame = f;
        locked = false;
        init();
    }

    private void init()
    {
        index = counter++;
        JButton first = new JButton("Open non-locking dialog");
        JButton second = new JButton("Open locking dialog");
        JButton close = new JButton("Close");
        getContentPane().setLayout(new GridLayout(3, 1));
        getContentPane().add(first);
        getContentPane().add(second);
        getContentPane().add(close);
        first.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                if(nonLockChildDiag == null)
                    nonLockChildDiag = new ModalLockTest(frame, ModalLockTest.this);
                System.out.println((new StringBuilder()).append("Showing non-locking dialog #").append(nonLockChildDiag.index).toString());
                nonLockChildDiag.setVisible(true);
                System.out.println((new StringBuilder()).append("Showed non-locking dialog #").append(nonLockChildDiag.index).toString());
            }

            final ModalLockTest this$0;

            
            {
                this$0 = ModalLockTest.this;
                super();
            }
        }
);
        second.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                if(lockChildDiag == null)
                    lockChildDiag = new ModalLockTest(frame);
                System.out.println((new StringBuilder()).append("Showing locking dialog #").append(lockChildDiag.index).toString());
                lockChildDiag.setVisible(true);
                System.out.println((new StringBuilder()).append("Showed locking dialog #").append(lockChildDiag.index).toString());
            }

            final ModalLockTest this$0;

            
            {
                this$0 = ModalLockTest.this;
                super();
            }
        }
);
        close.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0)
            {
                setVisible(false);
                System.out.println((new StringBuilder()).append("Closing ").append(locked ? "locked" : "unlocked").append(" dialog #").append(index).toString());
            }

            final ModalLockTest this$0;

            
            {
                this$0 = ModalLockTest.this;
                super();
            }
        }
);
        setTitle("Test of modal lock problem");
        setSize(300, 300);
        setLocationRelativeTo(frame);
    }

    public static void main(String args[])
    {
        JFrame fra = new JFrame("Modal Lock Test");
        JButton open = new JButton("Open Dialog");
        JButton close = new JButton("Close Test App");
        ModalLockTest diag = new ModalLockTest(fra);
        open.addActionListener(new ActionListener(diag) {

            public void actionPerformed(ActionEvent arg0)
            {
                System.out.println((new StringBuilder()).append("Showing dialog #").append(diag.index).toString());
                diag.setVisible(true);
                System.out.println((new StringBuilder()).append("Showed dialog #").append(diag.index).toString());
            }

            final ModalLockTest val$diag;

            
            {
                diag = modallocktest;
                super();
            }
        }
);
        close.addActionListener(new ActionListener(fra) {

            public void actionPerformed(ActionEvent arg0)
            {
                fra.setVisible(false);
                System.out.println("Closed frame");
                System.exit(0);
            }

            final JFrame val$fra;

            
            {
                fra = jframe;
                super();
            }
        }
);
        fra.addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e)
            {
                System.out.println("Top Frame gained focus");
            }

            public void windowLostFocus(WindowEvent e)
            {
                System.out.println("Top Frame lost focus");
            }

        }
);
        fra.getContentPane().setLayout(new GridLayout(2, 1));
        fra.getContentPane().add(open);
        fra.getContentPane().add(close);
        fra.setSize(300, 300);
        fra.setDefaultCloseOperation(3);
        fra.setVisible(true);
    }

    private static int counter = 0;
    private Frame frame;
    private ModalLockTest lockChildDiag;
    private ModalLockTest nonLockChildDiag;
    private int index;
    private boolean locked;








}
