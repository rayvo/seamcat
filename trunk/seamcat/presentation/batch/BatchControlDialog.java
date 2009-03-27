// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BatchControlDialog.java

package org.seamcat.presentation.batch;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.seamcat.batch.BatchJobList;
import org.seamcat.batch.BatchJobListListener;
import org.seamcat.presentation.EscapeDialog;

// Referenced classes of package org.seamcat.presentation.batch:
//            BatchStatusPanel

public class BatchControlDialog extends EscapeDialog
    implements BatchJobListListener
{

    public BatchControlDialog(JDialog parent)
    {
        super(parent, true);
        stop = new JButton(STRINGLIST.getString("BTN_CAPTION_STOP_BATCH"));
        batchIsComplete = false;
        status = new BatchStatusPanel();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(status, "Center");
        JPanel stopPanel = new JPanel();
        stopPanel.add(stop);
        getContentPane().add(stopPanel, "South");
        stop.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                if(batchIsComplete)
                {
                    BatchControlDialog.LOG.debug("Closing dialog");
                    setVisible(false);
                    BatchControlDialog.LOG.debug("Closed dialog");
                } else
                {
                    batchJobList.stopBatch();
                }
            }

            final BatchControlDialog this$0;

            
            {
                this$0 = BatchControlDialog.this;
                super();
            }
        }
);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setTitle(STRINGLIST.getString("BATCH_RUNNING_TITLE"));
    }

    public void startingJob(int i)
    {
    }

    public void endingJob(int i)
    {
    }

    public void show(BatchJobList batchJobList)
    {
        this.batchJobList = batchJobList;
        batchIsComplete = false;
        batchJobList.addBatchJobListListener(this);
        status.setBatchJobList(batchJobList);
        stop.setText(STRINGLIST.getString("BTN_CAPTION_STOP_BATCH"));
        if(LOG.isDebugEnabled())
            LOG.debug("show() - Showing control dialog");
        setVisible(true);
        if(LOG.isDebugEnabled())
            LOG.debug("show() - Showed controldialog");
        if(LOG.isDebugEnabled())
            LOG.debug("show() - Performing cleanup");
        batchJobList.removeBatchJobListListeners(this);
        this.batchJobList = null;
        if(LOG.isDebugEnabled())
            LOG.debug("show() - Cleanup done");
    }

    public void entireBatchIsComplete()
    {
        stop.setText(STRINGLIST.getString("BTN_CAPTION_CLOSE"));
        batchIsComplete = true;
    }

    private static final Logger LOG = Logger.getLogger(org/seamcat/presentation/batch/BatchControlDialog);
    private static final ResourceBundle STRINGLIST;
    private BatchStatusPanel status;
    private JButton stop;
    private BatchJobList batchJobList;
    private boolean batchIsComplete;

    static 
    {
        STRINGLIST = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
    }



}
