/****************************************************************************
 * NCSA HDF                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                   *
 *                                                                          *
 ****************************************************************************/

package ncsa.hdf.view;

import ncsa.hdf.object.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.*;
import java.util.*;

/**
 * DataOptionDialog is an dialog window used to select display options.
 * Display opotions include selection of subset, display type (image, text, or
 * spreadsheet).
 * <p>
 * @version 1.3.0 2/20/2002
 * @author Peter X. Cao
 */
public class DataOptionDialog extends JDialog
implements ActionListener, ItemListener
{
    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

    /** the selected dataset/image */
    private Dataset dataset;

    /** the rank of the dataset/image */
    private int rank;

    /** the starting point of selected subset */
    private long start[];

    /** the sizes of all dimensions */
    private long dims[];

    /** the selected sizes of all dimensions */
    private long selected[];

    /** the stride */
    private long stride[];

    /** the indices of the selected dimensions. */
    private int selectedIndex[];

    private int currentIndex[];

    private JRadioButton spreadsheetButton, imageButton, charButton, transposeButton;

    private JComboBox choicePalette, choices[];

    private boolean isSelectionCancelled;

    private boolean isTrueColorImage;

    private boolean isText;

    private boolean isH5;

    private JLabel maxLabels[];

    private JTextField startFields[], endFields[], strideFields[];

    private JList fieldList;

    private final Toolkit toolkit;

    private final SubsetNavigator navigator;

    private int numberOfPalettes;

    /** JComboBox.setSelectedItem() or setSelectedIndex() always fires
     * action event. If you call setSelectedItem() or setSelectedIndex()
     * at itemStateChanged() or actionPerformed(), the setSelectedItem()
     * or setSelectedIndex() will make loop calls of itemStateChanged()
     * or actionPerformed(). This is not what we want. We want the
     * setSelectedItem() or setSelectedIndex() behavior like java.awt.Choice.
     * This flag is used to serve this purpose.
     */
    private boolean performJComboBoxEvent = false;

    /**
     * Constructs a DataOptionDialog with the given HDFView.
     */
    public DataOptionDialog(ViewManager theview)
    {
        super((Frame)theview, true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        viewer = theview;
        dataset = (Dataset)theview.getSelectedObject();
        isSelectionCancelled = true;
        isTrueColorImage = false;
        isText = false;
        numberOfPalettes = 1;
        toolkit = Toolkit.getDefaultToolkit();

        if (dataset == null)
            dispose();
        else
            setTitle("Dataset Selection - "+dataset.getPath()+dataset.getName());

        isH5 = dataset.getFileFormat().isThisType(FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5));

        rank = dataset.getRank();
        if (rank <=0 ) dataset.init();
        if (isH5 && (dataset instanceof ScalarDS))
        {
            byte[] palRefs = ((ScalarDS)dataset).getPaletteRefs();
            if (palRefs != null && palRefs.length > 8)
                numberOfPalettes = palRefs.length/8;
        }

        rank = dataset.getRank();
        dims = dataset.getDims();
        selected = dataset.getSelectedDims();
        start = dataset.getStartDims();
        selectedIndex = dataset.getSelectedIndex();
        stride = dataset.getStride();
        fieldList = null;

        int h=1, w=1;
        h = (int)dims[selectedIndex[0]];
        if (rank > 1) w = (int)dims[selectedIndex[1]];
        navigator = new SubsetNavigator(w, h);

        currentIndex = new int[Math.min(3, rank)];

        choicePalette = new JComboBox();

        choicePalette.addItem("Select palette");
        choicePalette.addItem("Default");
        for (int i=2; i<=numberOfPalettes; i++)
        {
            choicePalette.addItem("Default "+i);
        }
        choicePalette.addItem("Gray");
        choicePalette.addItem("Rainbow");
        choicePalette.addItem("Nature");
        choicePalette.addItem("Wave");

        spreadsheetButton = new JRadioButton("Spreadsheet ", true);
        spreadsheetButton.setMnemonic(KeyEvent.VK_S);
        imageButton = new JRadioButton("Image");
        imageButton.setMnemonic(KeyEvent.VK_I);
        charButton = new JRadioButton("Show As Char", false);
        charButton.setMnemonic(KeyEvent.VK_C);
        charButton.setEnabled(false);
        transposeButton = new JRadioButton("Transpose", false);
        transposeButton.setMnemonic(KeyEvent.VK_T);

        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        contentPane.setPreferredSize(new Dimension(700, 300));

        JPanel centerP = new JPanel();
        centerP.setLayout(new BorderLayout());
        centerP.setBorder(new TitledBorder("Dimension and Subset Selection"));

        JPanel navigatorP = new JPanel();
        navigatorP.add(navigator);
        navigatorP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        performJComboBoxEvent = true;

        if (dataset instanceof CompoundDS)
        {
            // setup GUI components for the field selection
            CompoundDS d = (CompoundDS)dataset;
            String[] names = d.getMemberNames();
            fieldList = new JList(names);
            fieldList.addSelectionInterval(0, names.length-1);
            JPanel fieldP = new JPanel();
            fieldP.setLayout(new BorderLayout());
            fieldP.setPreferredSize(new Dimension(150, 300));
            JScrollPane scrollP = new JScrollPane(fieldList);
            fieldP.add(scrollP, BorderLayout.CENTER);
            fieldP.setBorder(new TitledBorder("Select Members"));
            contentPane.add(fieldP, BorderLayout.WEST);
        }
        else if ( dataset instanceof ScalarDS)
        {
            ScalarDS sd = (ScalarDS)dataset;
            isText = sd.isText();

            if (!isText)
            {
                if (rank > 1)
                    centerP.add(navigatorP, BorderLayout.WEST);

                // setup GUI components for the display options: table or image
                imageButton.addItemListener(this);
                spreadsheetButton.addItemListener(this);
                ButtonGroup rgroup = new ButtonGroup();
                rgroup.add(spreadsheetButton);
                rgroup.add(imageButton);
                JPanel viewP = new JPanel();
                viewP.setLayout(new GridLayout(1,2));
                JPanel sheetP = new JPanel();
                sheetP.setLayout(new GridLayout(1,3, 5, 5));
                sheetP.add(spreadsheetButton);
                sheetP.add(charButton);
                sheetP.add(transposeButton);
                sheetP.setBorder(new TitledBorder(""));
                viewP.add(sheetP);
                JPanel imageP = new JPanel();
                imageP.setLayout(new BorderLayout(5,5));
                imageP.add(imageButton, BorderLayout.WEST);
                imageP.add(choicePalette, BorderLayout.CENTER);
                imageP.setBorder(new TitledBorder(""));
                viewP.add(imageP);
                viewP.setBorder(new TitledBorder("Display As"));
                contentPane.add(viewP, BorderLayout.NORTH);
            }
        }

        // setup GUI for dimension and subset selection
        JPanel selectionP = new JPanel();
        selectionP.setLayout(new GridLayout(5, 6, 10, 3));
        selectionP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));

        centerP.add(selectionP, BorderLayout.CENTER);
        contentPane.add(centerP, BorderLayout.CENTER);

        selectionP.add(new JLabel(" "));
        selectionP.add(new JLabel(" "));
        JLabel label = new JLabel("Start:");
        selectionP.add(label);
        label = new JLabel("End: ");
        selectionP.add(label);
        label = new JLabel("Stride:");
        selectionP.add(label);
        label = new JLabel("Max Size");
        selectionP.add(label);

        choices = new JComboBox[3];
        maxLabels = new JLabel[3];
        startFields = new JTextField[3];
        endFields = new JTextField[3];
        strideFields = new JTextField[3];
        JLabel dimLabels[] = {
            new JLabel("Height", JLabel.RIGHT),
            new JLabel("Width", JLabel.RIGHT),
            new JLabel("Depth", JLabel.RIGHT),
            };

         for (int i=0; i<3; i++)
        {
            choices[i] = new JComboBox();
            choices[i].addItemListener(this);
            for (int j=0; j<rank; j++) choices[i].addItem("dim "+j);
            maxLabels[i] = new JLabel("1");
            startFields[i] = new JTextField("1");
            endFields[i] = new JTextField("1");
            strideFields[i] = new JTextField("1");
            selectionP.add(dimLabels[i]);
            selectionP.add(choices[i]);
            selectionP.add(startFields[i]);
            selectionP.add(endFields[i]);
            selectionP.add(strideFields[i]);
            selectionP.add(maxLabels[i]);

            // disable the selection components
            // init() will set them appropriate
            choices[i].setEnabled(false);
            startFields[i].setEnabled(false);
            endFields[i].setEnabled(false);
            strideFields[i].setEnabled(false);
            maxLabels[i].setEnabled(false);
        }

        // add button dimension selection when dimension size >= 4
        JButton button = new JButton("More...");
        selectionP.add(new JLabel(" "));
        if (rank > 3)
        {
            button.setMnemonic(KeyEvent.VK_M);
            button.setActionCommand("Select more dimensions");
            button.addActionListener(this);
            selectionP.add(button);
        }
        else
        {
            selectionP.add(new JLabel(" "));
        } // padding last row of the center panel.
        selectionP.add(new JLabel(" "));
        selectionP.add(new JLabel(" "));
        selectionP.add(new JLabel(" "));
        selectionP.add(new JLabel(" "));

        // add OK and CANCEL buttons
        JPanel confirmP = new JPanel();
        contentPane.add(confirmP, BorderLayout.SOUTH);
        button = new JButton("   Ok   ");
        button.setMnemonic(KeyEvent.VK_O);
        button.setActionCommand("Ok");
        button.addActionListener(this);
        confirmP.add(button);
        button = new JButton("Cancel");
        button.setMnemonic(KeyEvent.VK_C);
        button.setActionCommand("Cancel");
        button.addActionListener(this);
        confirmP.add(button);

        init();

        // locate the H5Property dialog
        Point l = getParent().getLocation();
        l.x += 250;
        l.y += 80;
        setLocation(l);
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Ok"))
        {
            // set palette for image view
            if (dataset instanceof ScalarDS &&
                imageButton.isSelected())
                setPalette();

            isSelectionCancelled = !setSelection();

            if (isSelectionCancelled)
                return;

            dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            dispose();
        }
        else if (cmd.equals("Select more dimensions"))
        {
            if (rank < 4)
                return;

            int idx = 0;
            Vector choice4 = new Vector();
            int[] choice4Index = new int[rank-3];
            for (int i=0; i<rank; i++)
            {
                if ((i!=currentIndex[0]) &&
                    (i!=currentIndex[1]) &&
                    (i!=currentIndex[2]))
                {
                    choice4.add(choices[0].getItemAt(i));
                    choice4Index[idx++] = i;
                }
            }

            String msg = "Select slice location for dimension(s):\n\""
                       +choice4.get(0)+"["+dims[choice4Index[0]]+"]\"";
            String initValue = String.valueOf(start[choice4Index[0]]+1);
            int n = choice4.size();
            for (int i=1; i<n; i++)
            {
                msg +=" x \"" + choice4.get(i)+ "["+dims[choice4Index[i]]+"]\"";
                initValue += " x "+String.valueOf(start[choice4Index[i]]+1);
            }

            String result = JOptionPane.showInputDialog(this, msg, initValue);
            if (result==null || (result = result.trim())== null || result.length()<1)
                return;

            StringTokenizer st = new StringTokenizer(result, "x");
            if ( st.countTokens() < n)
            {
                JOptionPane.showMessageDialog(
                        this,
                        "Number of dimension(s) is less than "+n+"\n"+result,
                        "Select Slice Location",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            long[] start4 = new long[n];
            for (int i=0; i<n; i++)
            {
                try {
                    start4[i] = Long.parseLong(st.nextToken().trim());
                } catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Select Slice Location",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (start4[i] < 1 || start4[i] >dims[choice4Index[i]])
                {
                    JOptionPane.showMessageDialog(
                        this,
                        "Slice location is out of range.\n"+start4[i]+" > "+dims[choice4Index[i]],
                        "Select Slice Location",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

            }

            for (int i=0; i<n; i++)
            {
                start[choice4Index[i]] = start4[i]-1;
            }
        }

    }

    public void itemStateChanged(ItemEvent e)
    {
        Object source = e.getSource();

        if (source.equals(imageButton) ||
            source.equals(spreadsheetButton))
        {
            choicePalette.setEnabled( imageButton.isSelected() && !isTrueColorImage);

            // reset show char button
            if (spreadsheetButton.isSelected() &&
                dataset.getDatatype().getDatatypeSize()==1)
            {
                charButton.setEnabled(true);
            }
            else
            {
                charButton.setEnabled(false);
                charButton.setSelected(false);
            }

            transposeButton.setEnabled(spreadsheetButton.isSelected());
        }
        else if (source instanceof JComboBox)
        {
            if (!performJComboBoxEvent)
                return;

            if (e.getStateChange() == ItemEvent.DESELECTED)
                return; // don't care about the deselect

            JComboBox theChoice = (JComboBox)source;

            int theSelectedChoice = -1;

            int n = Math.min(3, rank);
            for (int i=0; i<n; i++)
            {
                if (theChoice.equals(choices[i]))
                    theSelectedChoice = i;
            }

            if (theSelectedChoice < 0)
                return; // the selected JComboBox is not a dimension choice

            int theIndex = theChoice.getSelectedIndex();
            if (theIndex == currentIndex[theSelectedChoice])
                return; // select the same item, no change

            start[currentIndex[theSelectedChoice]]=0;

            // reset the selected dimension choice
            startFields[theSelectedChoice].setText("1");
            endFields[theSelectedChoice].setText(String.valueOf(dims[theIndex]));
            strideFields[theSelectedChoice].setText("1");
            maxLabels[theSelectedChoice].setText(String.valueOf(dims[theIndex]));

            // if the seelcted choice selects the dimensin that is selected by
            // other dimension choice, exchange the dimensions
            for (int i=0; i<n; i++)
            {
                if (i==theSelectedChoice)
                    continue; // don't exchange itself
                else if (theIndex==choices[i].getSelectedIndex())
                {
                    setJComboBoxSelectedIndex(choices[i], currentIndex[theSelectedChoice]);
                    startFields[i].setText("1");
                    endFields[i].setText(String.valueOf(dims[currentIndex[theSelectedChoice]]));
                    strideFields[i].setText("1");
                    maxLabels[i].setText(String.valueOf(dims[currentIndex[theSelectedChoice]]));
                }
            }

            for (int i=0; i<n; i++)
                currentIndex[i] = choices[i].getSelectedIndex();

            // update the navigator
            if (rank > 1)
            {
                if (isText)
                    endFields[1].setText(startFields[1].getText());
                else
                {
                    int hIdx = choices[0].getSelectedIndex();
                    int wIdx = choices[1].getSelectedIndex();
                    long dims[] = dataset.getDims();
                    int w = (int)dims[wIdx];
                    int h = (int)dims[hIdx];
                    navigator.setDimensionSize(w, h);
                    navigator.updateUI();
                }
            }

            if (rank > 2)
                endFields[2].setText(startFields[2].getText());
        } // else if (source instanceof JComboBox)
    }


    /** Returns true if the data selection is cancelled. */
    public boolean isCancelled()
    {
        return isSelectionCancelled;
    }

    /** Returns true if the display option is image. */
    public boolean isImageDisplay() { return imageButton.isSelected(); }

    /**
     * Set the initial state of all the variables
     */
    private void init()
    {
        // set the imagebutton state
        boolean isImage = false;

        if (dataset instanceof ScalarDS)
        {
            ScalarDS sd = (ScalarDS)dataset;
            isImage = sd.isImage();
            isTrueColorImage = sd.isTrueColor();
        }
        else if (dataset instanceof CompoundDS)
        {
            imageButton.setEnabled(false);
        }
        imageButton.setSelected(isImage);
        choicePalette.setEnabled(isImage && !isTrueColorImage);

        int n = Math.min(3, rank);
        long endIdx = 0;
        for(int i=0; i<n; i++)
        {
            choices[i].setEnabled(true);
            startFields[i].setEnabled(true);
            endFields[i].setEnabled(true);
            strideFields[i].setEnabled(true);
            maxLabels[i].setEnabled(true);

            int idx = selectedIndex[i];
            endIdx = start[idx]+selected[idx]*stride[idx];
            if (endIdx >= dims[idx]) endIdx = dims[idx];

            setJComboBoxSelectedIndex(choices[i], idx);
            maxLabels[i].setText(String.valueOf(dims[idx]));
            startFields[i].setText(String.valueOf(start[idx]+1));
            endFields[i].setText(String.valueOf(endIdx));

            if (!isH5 && (dataset instanceof CompoundDS))
                strideFields[i].setEnabled(false);
            else
                strideFields[i].setText(String.valueOf(stride[idx]));
        }

        if (rank > 1 && isText)
        {
            endFields[1].setEnabled(false);
            endFields[1].setText(startFields[1].getText());
        }

        if (rank > 2 )
        {
            endFields[2].setEnabled(false);
            strideFields[2].setEnabled(false);
            if (isTrueColorImage && imageButton.isSelected())
            {
                choices[0].setEnabled(false);
                choices[1].setEnabled(false);
                choices[2].setEnabled(false);
                startFields[2].setEnabled(false);
                startFields[2].setText("1");
                endFields[2].setText("2");
            }
            else
            {
                choices[0].setEnabled(true);
                choices[1].setEnabled(true);
                choices[2].setEnabled(true);
                startFields[2].setEnabled(true);
                startFields[2].setText(String.valueOf(start[selectedIndex[2]]+1));
                endFields[2].setEnabled(!isText);
                endFields[2].setText(startFields[2].getText());
            }
        }

        for (int i=0; i<n; i++)
        {
            currentIndex[i] = choices[i].getSelectedIndex();
        }

        // reset show char button
        if (spreadsheetButton.isSelected() &&
            dataset.getDatatype().getDatatypeSize()==1)
        {
            charButton.setEnabled(true);
        }
        else
        {
            charButton.setEnabled(false);
            charButton.setSelected(false);
        }
    }

    /** JComboBox.setSelectedItem() or setSelectedIndex() always fires
     * action event. If you call setSelectedItem() or setSelectedIndex()
     * at itemStateChanged() or actionPerformed(), the setSelectedItem()
     * or setSelectedIndex() will make loop calls of itemStateChanged()
     * or actionPerformed(). This is not what we want. We want the
     * setSelectedItem() or setSelectedIndex() behavior like java.awt.Choice.
     * This flag is used to serve this purpose.
     */
    private void setJComboBoxSelectedIndex(JComboBox box, int idx)
    {
        performJComboBoxEvent = false;
        box.setSelectedIndex(idx);
        performJComboBoxEvent = true;
    }

    private void setPalette()
    {
        if (!(dataset instanceof ScalarDS))
            return;

        byte[][] pal = null;
        int palChoice = choicePalette.getSelectedIndex();

        if (palChoice == 0)
            pal = null; // using the first default palette
        else if (palChoice == numberOfPalettes+1)
            pal = ImageView.createGrayPalette();
        else if (palChoice == numberOfPalettes+2)
            pal = ImageView.createRainbowPalette();
        else if (palChoice == numberOfPalettes+3)
            pal = ImageView.createNaturePalette();
        else if (palChoice == numberOfPalettes+4)
            pal = ImageView.createWavePalette();
        else
        {
            // multuple palettes attached
            pal = ((ScalarDS)dataset).readPalette(palChoice-1);
        }

        ((ScalarDS)dataset).setPalette(pal);
    }

    private boolean setSelection()
    {
        long[] n0 = {0, 0, 0}; // start
        long[] n1= {0, 0, 0}; // end
        long[] n2= {1, 1, 1}; // stride
        int[] sIndex = {0, 1, 2};

        int n = Math.min(3, rank);
        for (int i=0; i<n; i++)
        {
            sIndex[i] = choices[i].getSelectedIndex();

            try {
                n0[i] = Long.parseLong(startFields[i].getText());
                if (i<2)
                {
                    n1[i] = Long.parseLong(endFields[i].getText());
                    n2[i] = Long.parseLong(strideFields[i].getText());
                }
            } catch (NumberFormatException ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    (Frame)viewer,
                    ex.getMessage(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (n0[i] < 1 ||
                n0[i] > dims[sIndex[i]] ||
                n1[i] > dims[sIndex[i]])
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    (Frame)viewer,
                    "Selected index is out of bound.",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (dataset instanceof CompoundDS)
        {
            CompoundDS d = (CompoundDS)dataset;
            int[] selectedFieldIndices = fieldList.getSelectedIndices();
            if (selectedFieldIndices==null || selectedFieldIndices.length<1)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    (Frame)viewer,
                    "No member/field is selected.",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            d.setMemberSelection(false); // deselect all members
            for (int i=0; i<selectedFieldIndices.length; i++)
                d.selectMember(selectedFieldIndices[i]);

        }

        // reset selected size
        for (int i=0; i<rank; i++)
        {
            selected[i] = 1;
            stride[i] = 1;
        }

        // find no error, set selection the the dataset object
        for (int i=0; i<n; i++)
        {
            selectedIndex[i] = sIndex[i];
            start[selectedIndex[i]] = n0[i]-1;
            if (i<2)
            {
                selected[selectedIndex[i]] = (int)((n1[i]-n0[i])/n2[i]+1);
                stride[selectedIndex[i]] = n2[i];
            }
        }

        if (rank > 1 && isText)
        {
            selected[selectedIndex[1]] = 1;
            stride[selectedIndex[1]] = 1;
        }

        if (rank >2 &&
            isTrueColorImage &&
            imageButton.isSelected())
        {
                start[selectedIndex[2]] = 0;
                selected[selectedIndex[2]] = 3;
        }

        //clear the old data
        dataset.clearData();

        return true;
    }

    /** SubsetNavigator draws selection rectangle of subset. */
    private class SubsetNavigator extends JComponent
        implements MouseListener, MouseMotionListener
    {
        private final int NAVIGATOR_SIZE = 150;
        private int dimX, dimY, x, y;
        private double r;
        private Point startPosition; // mouse clicked position
        private Rectangle selectedArea;
        private Image previewImage = null;

        private SubsetNavigator(int w, int h)
        {
            dimX = w;
            dimY = h;
            if (dimX > dimY)
            {
                x = NAVIGATOR_SIZE;
                r = dimX/(double)x;
                y = (int)(dimY/r);
            } else
            {
                y = NAVIGATOR_SIZE;
                r = dimY/(double)y;
                x = (int)(dimX/r);
            }


            selectedArea = new Rectangle();
            setPreferredSize(new Dimension(NAVIGATOR_SIZE, NAVIGATOR_SIZE));
            try { previewImage = createPreviewImage(); }
            catch (Exception ex) {}

            addMouseListener(this);
            addMouseMotionListener(this);
        }

        private Image createPreviewImage() throws Exception
        {
            if (rank <=1 || !(dataset instanceof ScalarDS))
                return null;

            Image preImage = null;
            ScalarDS sd = (ScalarDS)dataset;

            // backup the selection
            long[] strideBackup = new long[rank];
            long[] selectedBackup = new long[rank];
            long[] startBackup = new long[rank];
            int[] selectedIndexBackup = new int[rank];
            System.arraycopy(stride, 0, strideBackup, 0, rank);
            System.arraycopy(selected, 0, selectedBackup, 0, rank);
            System.arraycopy(start, 0, startBackup, 0, rank);
            System.arraycopy(selectedIndex, 0, selectedIndexBackup, 0, rank);

            // set the selection for preview
            for (int i=0; i<rank; i++)
            {
                start[i] = 0;
                stride[i] = 1;
                selected[i] = 1;
            }

            if (choices != null)
            {
                selectedIndex[0] = choices[0].getSelectedIndex();
                selectedIndex[1] = choices[1].getSelectedIndex();
            }
            long steps = (long)Math.ceil(r);
            selected[selectedIndex[0]] = (long)(dims[selectedIndex[0]]/steps);
            selected[selectedIndex[1]] = (long)(dims[selectedIndex[1]]/steps);
            stride[selectedIndex[0]] = stride[selectedIndex[1]] = steps;

            if (isTrueColorImage && start.length > 2)
            {
                start[selectedIndex[2]] = 0;
                selected[selectedIndex[2]] = 3;
                stride[selectedIndex[2]] = 1;
            }

            // update the ration of preview image size to the real dataset
            y = (int)selected[selectedIndex[0]];
            x = (int)selected[selectedIndex[1]];
            r = Math.min((double)dims[selectedIndex[0]]/(double)selected[selectedIndex[0]],
                (double)dims[selectedIndex[1]]/(double)selected[selectedIndex[1]]);

            try
            {
                Object data = sd.read();
                byte[] bData = ImageView.getBytes(data, null);
                int h = (int)sd.getHeight();
                int w = (int)sd.getWidth();

                if (isTrueColorImage)
                {
                    boolean isPlaneInterlace = (sd.getInterlace()==ScalarDS.INTERLACE_PLANE);
                    preImage = ImageView.createTrueColorImage(bData, isPlaneInterlace, w, h);
                }
                else
                {
                    byte[][] imagePalette = sd.getPalette();
                    if (imagePalette == null)
                        imagePalette = ImageView.createGrayPalette();
                    preImage = ImageView.createIndexedImage(bData, imagePalette, w, h);
                }
            }
            finally {
                // set back the origianl selection
                System.arraycopy(strideBackup, 0, stride, 0, rank);
                System.arraycopy(selectedBackup, 0, selected, 0, rank);
                System.arraycopy(startBackup, 0, start, 0, rank);
                System.arraycopy(selectedIndexBackup, 0, selectedIndex, 0, rank);
            }

            return preImage;
        }

        public void paint(Graphics g)
        {
            g.setColor(Color.blue);

            if (previewImage != null)
                g.drawImage(previewImage, 0, 0, this);
            else
                g.fillRect(0, 0, x, y);

            int w = selectedArea.width;
            int h = selectedArea.height;
            if (w>0 && h >0)
            {
                g.setColor(Color.red);
                g.drawRect(selectedArea.x, selectedArea.y, w, h);
            }
        }

        public void mousePressed(MouseEvent e)
        {
            startPosition = e.getPoint();
            selectedArea.setBounds(startPosition.x, startPosition.y, 0, 0);
        }

        public void mouseClicked(MouseEvent e)
        {
            startPosition = e.getPoint();
            selectedArea.setBounds(startPosition.x, startPosition.y, 0, 0);
            repaint();
        }

        public void mouseDragged(MouseEvent e)
        {
            Point p0 = startPosition;
            Point p1 = e.getPoint();

            int x0 = Math.max(0, Math.min(p0.x, p1.x));
            int y0 = Math.max(0, Math.min(p0.y, p1.y));
            int x1 = Math.min(x, Math.max(p0.x, p1.x));
            int y1 = Math.min(y, Math.max(p0.y, p1.y));

            int w = x1 - x0;
            int h = y1 - y0;
            selectedArea.setBounds(x0, y0, w, h);

            try { updateSelection(x0, y0, w, h); }
            catch (Exception ex ) {}

            repaint();
        }

        private void updateSelection(int x0, int y0, int w, int h)
        {
            int i0=0, i1=0;

            i0 = (int)(y0*r);
            if (i0 > dims[currentIndex[0]])
                i0 = (int)dims[currentIndex[0]];
            startFields[0].setText(String.valueOf(i0));

            i1 = (int)((y0+h)*r);
            if (i1 < i0) i1=i0;
            endFields[0].setText(String.valueOf(i1));

            if (rank > 1)
            {
                i0 = (int)(x0*r);
                if (i0 > dims[currentIndex[1]])
                    i0 = (int)dims[currentIndex[1]];
                startFields[1].setText(String.valueOf(i0));

                i1 = (int)((x0+w)*r);
                if (i1 < i0) i1=i0;
                endFields[1].setText(String.valueOf(i1));
            }
        }

        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e)  {}
        public void mouseMoved(MouseEvent e) {}

        private void setDimensionSize(int w, int h)
        {
            dimX = w;
            dimY = h;
            if (dimX > dimY)
            {
                x = NAVIGATOR_SIZE;
                r = dimX/(double)x;
                y = (int)(dimY/r);
            } else
            {
                y = NAVIGATOR_SIZE;
                r = dimY/(double)y;
                x = (int)(dimX/r);
            }
            setPreferredSize(new Dimension(NAVIGATOR_SIZE, NAVIGATOR_SIZE));
            selectedArea.setSize(0, 0);
            try { previewImage = createPreviewImage(); }
            catch (Exception ex) {}

            repaint();
        }
    } // private class SubsetNavigator extends JComponent

    public boolean isDisplayTypeChar() { return charButton.isSelected(); }

   public boolean isTransposed() { return transposeButton.isSelected(); }

}

