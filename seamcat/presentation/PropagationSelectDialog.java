// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PropagationSelectDialog.java

package org.seamcat.presentation;

import java.awt.*;
import org.seamcat.presentation.components.NavigateButtonPanel;
import org.seamcat.presentation.components.PropagationSelectPanel;
import org.seamcat.propagation.PropagationModel;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog

public class PropagationSelectDialog extends EscapeDialog
{

    public PropagationSelectDialog(Frame parent)
    {
        super(parent);
        panel = new PropagationSelectPanel(parent);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, "Center");
        getContentPane().add(new NavigateButtonPanel() {

            public void btnCancelActionPerformed()
            {
                setVisible(false);
            }

            public void btnOkActionPerformed()
            {
                accept = true;
                setVisible(false);
            }

            final PropagationSelectDialog this$0;

            
            {
                this$0 = PropagationSelectDialog.this;
                super();
            }
        }
, "South");
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setModal(true);
    }

    public boolean show(PropagationModel model)
    {
        panel.setPropagationModel(model);
        accept = false;
        super.setVisible(true);
        return accept;
    }

    public PropagationModel getPropagationModel()
    {
        return panel.getPropagationModel();
    }

    private PropagationSelectPanel panel;
    private boolean accept;

}
