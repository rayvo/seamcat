// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   DialogGenerateMultipleInterferingSystems.java

package org.seamcat.presentation;

import java.awt.*;
import java.awt.event.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.help.HelpBroker;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.seamcat.Seamcat;
import org.seamcat.calculator.FormattedCalculatorField;
import org.seamcat.mathematics.Mathematics;
import org.seamcat.model.*;
import org.seamcat.model.core.InterferenceLink;

// Referenced classes of package org.seamcat.presentation:
//            EscapeDialog, LabeledPairLayout, SeamcatTextFieldFormats

final class DialogGenerateMultipleInterferingSystems extends EscapeDialog
{

    public DialogGenerateMultipleInterferingSystems(JFrame owner)
    {
        super(owner);
        generateIntermodulationVectors = true;
        setResizable(false);
        setTitle(resources.getString("MULTI_TITLE"));
        origText = resources.getString("MULTI_MEMORY_ALERT_TEXT");
        initComponents();
        numberOfTiers.setModel(new SpinnerNumberModel(1, 1, 5, 1));
    }

    private void initComponents()
    {
        buttonGroup1 = new ButtonGroup();
        jPanel2 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jComboBox1 = new JComboBox();
        jPanel3 = new JPanel();
        jPanel4 = new JPanel();
        jPanel5 = new JPanel();
        jLabel2 = new JLabel();
        numberOfTiers = new JSpinner();
        angleOffset = new FormattedCalculatorField(30D, owner);
        disAngle = new FormattedCalculatorField(60D, owner);
        frequencyReuseDistance = new FormattedCalculatorField(5D, owner);
        totalNumberOfGeneratedCells = new JFormattedTextField();
        totalNumberOfGeneratedCells.setColumns(15);
        totalNumberOfGeneratedCells.setHorizontalAlignment(4);
        totalNumberOfGeneratedCells.addFocusListener(SeamcatTextFieldFormats.SELECTALL_FOCUSHANDLER);
        totalNumberOfGeneratedCells.setFormatterFactory(DFORMATS.getIntegerFactory());
        totalNumberOfGeneratedCells.setEditable(false);
        numberOfCellsInRingSpin = new JSpinner();
        numberOfCellsInRingSpin.setModel(new SpinnerNumberModel(6, 1, 20, 1));
        numberOfCellsInRingSpin.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e)
            {
                disAngle.setValue(new Double(360 / ((Number)numberOfCellsInRingSpin.getValue()).intValue()));
                updateTotalNumberOfCells();
            }

            final DialogGenerateMultipleInterferingSystems this$0;

            
            {
                this$0 = DialogGenerateMultipleInterferingSystems.this;
                super();
            }
        }
);
        numberOfTiers.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e)
            {
                updateTotalNumberOfCells();
            }

            final DialogGenerateMultipleInterferingSystems this$0;

            
            {
                this$0 = DialogGenerateMultipleInterferingSystems.this;
                super();
            }
        }
);
        getContentPane().setLayout(new BorderLayout(8, 8));
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt)
            {
                exitForm(evt);
            }

            final DialogGenerateMultipleInterferingSystems this$0;

            
            {
                this$0 = DialogGenerateMultipleInterferingSystems.this;
                super();
            }
        }
);
        jButton1.setMnemonic('G');
        jButton1.setText(resources.getString("MULTI_BUTTON_GENERATE"));
        jButton1.setEnabled(false);
        jButton1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    InterferenceLink orig = (InterferenceLink)jComboBox1.getSelectedItem();
                    radius = ((Number)frequencyReuseDistance.getValue()).doubleValue();
                    offset = ((Number)angleOffset.getValue()).doubleValue();
                    angle = ((Number)disAngle.getValue()).doubleValue();
                    numberOfCellsInRing = ((Number)numberOfCellsInRingSpin.getValue()).intValue();
                    numberOfRings = ((Number)numberOfTiers.getValue()).intValue();
                    if(checkMemory(calculateNumberOfCells(numberOfCellsInRing, numberOfRings)))
                    {
                        generateSurroundingCells(orig, 1, generateIntermodulationVectors);
                        for(int i = 1; i < generatedLinks.length; i++)
                            workspace.addInterferingSystemLink(generatedLinks[i], generateIntermodulationVectors);

                        generatedLinks = null;
                        setVisible(false);
                    }
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(DialogGenerateMultipleInterferingSystems.this, DialogGenerateMultipleInterferingSystems.resources.getString("MULTI_GENERATION_ERROR_MESSAGE"));
                    ex.printStackTrace();
                }
            }

            final DialogGenerateMultipleInterferingSystems this$0;

            
            {
                this$0 = DialogGenerateMultipleInterferingSystems.this;
                super();
            }
        }
);
        jPanel2.add(jButton1);
        jButton2.setMnemonic('C');
        jButton2.setText(resources.getString("BTN_CAPTION_CLOSE"));
        jButton2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }

            final DialogGenerateMultipleInterferingSystems this$0;

            
            {
                this$0 = DialogGenerateMultipleInterferingSystems.this;
                super();
            }
        }
);
        jPanel2.add(jButton2);
        jButton3.setMnemonic('H');
        jButton3.setText(resources.getString("BTN_CAPTION_HELP"));
        Seamcat.helpBroker.enableHelpOnButton(jButton3, helplist.getString(getClass().getName()), null);
        jPanel2.add(jButton3);
        getContentPane().add(jPanel2, "South");
        jPanel1.setLayout(new FlowLayout(0));
        jLabel1.setText(resources.getString("MULTI_ORIGLINK"));
        jPanel1.add(jLabel1);
        jComboBox1.setPreferredSize(new Dimension(200, 24));
        jPanel1.add(jComboBox1);
        getContentPane().add(jPanel1, "North");
        jPanel3.setLayout(new BorderLayout());
        jPanel4.setLayout(new FlowLayout(0));
        jPanel3.add(jPanel4, "North");
        jPanel5.setLayout(new LabeledPairLayout());
        jPanel5.setBorder(new TitledBorder(resources.getString("MULTI_DEFINITIONS_TITLE")));
        jLabel2.setText(resources.getString("MULTI_NUMBER_OF_TIERS"));
        jPanel5.add(jLabel2, "label");
        numberOfTiers.setPreferredSize(new Dimension(40, 20));
        jPanel5.add(numberOfTiers, "field");
        jPanel5.add(new JLabel(resources.getString("MULTI_CELLS_IN_FIRST")), "label");
        jPanel5.add(numberOfCellsInRingSpin, "field");
        jPanel5.add(new JLabel(resources.getString("MULTI_FREQUENCY_REUSE")), "label");
        jPanel5.add(frequencyReuseDistance, "field");
        jPanel5.add(new JLabel(resources.getString("MULTI_CELL_DISPLACEMENT")), "label");
        jPanel5.add(disAngle, "field");
        jPanel5.add(new JLabel(resources.getString("MULTI_CELL_DISPLACEMENT_OFFSET")), "label");
        jPanel5.add(angleOffset, "field");
        jPanel5.add(new JLabel(resources.getString("MULTI_TOTAL_CELLS")), "label");
        jPanel5.add(totalNumberOfGeneratedCells, "field");
        jPanel3.add(jPanel5, "Center");
        getContentPane().add(jPanel3, "Center");
        pack();
    }

    private void updateTotalNumberOfCells()
    {
        totalNumberOfGeneratedCells.setValue(new Integer(calculateNumberOfCells(((Number)numberOfCellsInRingSpin.getValue()).intValue(), ((Number)numberOfTiers.getValue()).intValue())));
    }

    private boolean checkMemory(int totalNumberOfNewInterferingLinks)
    {
        int total = workspace.getInterferenceLinks().size();
        total += totalNumberOfNewInterferingLinks;
        int vectorCount = (int)(3D * Math.pow(total, 2D) + (double)(3 * total) + 4D);
        double byteConversionValue = 1048576D;
        double memoryNeeded = (double)vectorCount * ((double)(workspace.getControl().getEgData().getNumberOfEvents() * 8) / 1048576D);
        Runtime r = Runtime.getRuntime();
        double memoryAvailable = ((double)(r.maxMemory() - (r.totalMemory() - r.freeMemory())) / 1048576D) * 0.84999999999999998D;
        if(memoryNeeded > memoryAvailable)
        {
            int response = getOptionResponse(totalNumberOfNewInterferingLinks, vectorCount, (int)memoryNeeded);
            if(response == 0)
            {
                workspace.getControl().getEgData().setNumberOfEvents((int)(memoryAvailable / (((double)vectorCount * 8D) / 1048576D)));
                return true;
            }
            if(response == 1)
            {
                generateIntermodulationVectors = false;
                vectorCount = 6 * total + 4;
                memoryNeeded = (double)vectorCount * ((double)(workspace.getControl().getEgData().getNumberOfEvents() * 8) / 1048576D);
                if(memoryNeeded > memoryAvailable)
                    workspace.getControl().getEgData().setNumberOfEvents((int)(memoryAvailable / (((double)vectorCount * 8D) / 1048576D)));
                return true;
            } else
            {
                return false;
            }
        } else
        {
            return true;
        }
    }

    public void generateSurroundingCells(InterferenceLink orig, int ringid, boolean intermodulation)
    {
        if(ringid > numberOfRings)
            return;
        if(generatedLinks == null)
        {
            generatedLinks = new InterferenceLink[calculateNumberOfCells(numberOfCellsInRing, numberOfRings)];
            generatedLinks[0] = orig;
        }
        double angleFromCenter = angle / (double)ringid;
        int numberOfCellsInThisRing = ringid * numberOfCellsInRing;
        int cellsInnerRows = calculateNumberOfCells(numberOfCellsInRing, ringid - 1);
        String namePattern = resources.getString("MULTI_NAME_PATTERN");
        for(int i = 0; i < numberOfCellsInThisRing; i++)
        {
            int cellid = cellsInnerRows + i;
            if(generatedLinks[cellid] == null)
                generatedLinks[cellid] = new InterferenceLink(replaceVariables(namePattern, new String[] {
                    Integer.toString(cellid), orig.getReference()
                }), orig);
            double x = Mathematics.cosD((double)i * angleFromCenter + offset) * (radius * (double)ringid);
            double y = Mathematics.sinD((double)i * angleFromCenter + offset) * (radius * (double)ringid);
            generatedLinks[cellid].getWt2VrPath().setDeltaX(x);
            generatedLinks[cellid].getWt2VrPath().setDeltaY(y);
        }

        generateSurroundingCells(orig, ringid + 1, intermodulation);
    }

    public int calculateNumberOfCells(int numberOfCellsInRing, int tier)
    {
        if(tier == 0)
            return 0;
        else
            return numberOfCellsInRing * tier + calculateNumberOfCells(numberOfCellsInRing, tier - 1);
    }

    private int getNumberOfRings()
    {
        return ((Number)numberOfTiers.getValue()).intValue();
    }

    private void jButton2ActionPerformed(ActionEvent evt)
    {
        setVisible(false);
    }

    private void jRadioButton1ActionPerformed(ActionEvent actionevent)
    {
    }

    private void exitForm(WindowEvent evt)
    {
        setVisible(false);
    }

    public void setWorkspace(Workspace _workspace)
    {
        if(_workspace != null)
        {
            workspace = _workspace;
            DefaultComboBoxModel model = new DefaultComboBoxModel();
            Components v = workspace.getInterferenceLinks();
            int i = 0;
            for(int stop = v.size(); i < stop; i++)
                if(((InterferenceLink)v.get(i)).getCorrelationMode() != 0 && ((InterferenceLink)v.get(i)).getCorrelationMode() != 2 && ((InterferenceLink)v.get(i)).getCorrelationMode() != 1 && !((InterferenceLink)v.get(i)).isCDMASystem())
                    model.addElement(v.get(i));

            jComboBox1.setModel(model);
            jButton1.setEnabled(true);
            if(model.getSize() < 1)
            {
                JOptionPane.showMessageDialog(this, resources.getString("MULTI_NO_FIXED_LINKS_EXISTS"));
                setVisible(false);
            } else
            {
                generateIntermodulationVectors = true;
                updateTotalNumberOfCells();
            }
        } else
        {
            JOptionPane.showMessageDialog(this, resources.getString("MULTI_NO_WORKSPACE"));
            hide();
        }
    }

    private int getOptionResponse(int total, int vectorCount, int memoryNeeded)
    {
        String text = replaceVariables(origText, new String[] {
            Integer.toString(total), Integer.toString(vectorCount), Integer.toString(memoryNeeded)
        });
        return JOptionPane.showOptionDialog(this, text, resources.getString("MULTI_MEMORY_ALERT_TITLE"), 1, 2, null, options, null);
    }

    private static String replaceVariables(String org, String texts[])
    {
        return replaceVariables(org, texts, 0);
    }

    private static String replaceVariables(String org, String texts[], int start)
    {
        if(start < texts.length)
            return replaceVariables(replaceVariables(org, texts[start], start + 1), texts, start + 1);
        else
            return org;
    }

    private static String replaceVariables(String org, String txt, int no)
    {
        StringBuffer sb = new StringBuffer(org);
        String variable = (new StringBuilder()).append("%").append(no).toString();
        boolean end = false;
        while(!end) 
        {
            int idx = sb.toString().indexOf(variable);
            if(idx != -1)
            {
                sb.delete(idx, idx + variable.length());
                sb.insert(idx, txt);
            } else
            {
                end = true;
            }
        }
        return sb.toString();
    }

    private Workspace workspace;
    private static final SeamcatTextFieldFormats DFORMATS = new SeamcatTextFieldFormats();
    protected static final ResourceBundle helplist;
    private JFormattedTextField angleOffset;
    private JFormattedTextField disAngle;
    private JFormattedTextField frequencyReuseDistance;
    private JSpinner numberOfCellsInRingSpin;
    private JFormattedTextField totalNumberOfGeneratedCells;
    private InterferenceLink generatedLinks[];
    private double radius;
    private double offset;
    private double angle;
    private int numberOfCellsInRing;
    private int numberOfRings;
    private String origText;
    private static ResourceBundle resources;
    private static String options[];
    private boolean generateIntermodulationVectors;
    private ButtonGroup buttonGroup1;
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JComboBox jComboBox1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;
    private JPanel jPanel5;
    private JRadioButton jRadioButton1;
    private JSpinner numberOfTiers;

    static 
    {
        helplist = ResourceBundle.getBundle("javahelp", Locale.ENGLISH);
        resources = ResourceBundle.getBundle("stringlist", Locale.ENGLISH);
        options = (new String[] {
            resources.getString("MULTI_MEMORY_ALERT_BUTTON_ADJUST"), resources.getString("MULTI_MEMORY_ALERT_BUTTON_NO_INTERMODULATION"), resources.getString("BTN_CAPTION_CANCEL")
        });
    }






















}
