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
import java.awt.Choice;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Dimension;

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

    private JRadioButton spreadsheetButton, imageButton;

    private Choice choicePalette;

    private boolean isSelectionCancelled;

    private boolean isTrueColorImage;

    private Choice choices[];

    private JLabel maxLabels[];

    private JTextField startFields[], countFields[], strideFields[];

    private JList fieldList;

    private final Toolkit toolkit;

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
        toolkit = Toolkit.getDefaultToolkit();

        if (dataset == null)
            dispose();
        else
            setTitle("Data Selection - "+dataset.getPath()+dataset.getName());

        rank = dataset.getRank();
        if (rank <=0 ) dataset.init();

        rank = dataset.getRank();
        dims = dataset.getDims();
        selected = dataset.getSelectedDims();
        start = dataset.getStartDims();
        selectedIndex = dataset.getSelectedIndex();
        stride = dataset.getStride();
        fieldList = null;

        if (stride == null)
        {
            stride = new long[rank];
            for (int i=0; i<rank; i++)
                stride[i] = 1;
        }

        choicePalette = new Choice();
        choicePalette.add("Select palette");
        choicePalette.add("Default");
        choicePalette.add("Gray");
        choicePalette.add("Rainbow");
        choicePalette.add("Nature");
        choicePalette.add("Wave");
        choicePalette.addItemListener(this);

        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        // setup GUI components for the display options: table or image
        spreadsheetButton = new JRadioButton("Spreadsheet ", true);
        spreadsheetButton.setMnemonic(KeyEvent.VK_S);
        imageButton = new JRadioButton("Image");
        imageButton.setMnemonic(KeyEvent.VK_I);
        imageButton.addItemListener(this);
        spreadsheetButton.addItemListener(this);
        ButtonGroup rgroup = new ButtonGroup();
        rgroup.add(spreadsheetButton);
        rgroup.add(imageButton);
        JPanel viewP = new JPanel();
        viewP.add(spreadsheetButton);
        viewP.add(new JLabel("              "));
        viewP.add(imageButton);
        viewP.add(choicePalette);
        viewP.setBorder(new TitledBorder("Display As"));

        int heightGap = 150;
        int rows = Math.min(5, rank+1);

        if (dataset instanceof CompoundDS)
        {
            heightGap = 170;
            // setup GUI components for the field selection
            CompoundDS d = (CompoundDS)dataset;
            String[] names = d.getMemberNames();
            fieldList = new JList(names);
            fieldList.setVisibleRowCount(3);
            fieldList.addSelectionInterval(0, names.length-1);
            JPanel fieldP = new JPanel();
            fieldP.setLayout(new BorderLayout());
            JScrollPane scrollP = new JScrollPane(fieldList);
            fieldP.add(scrollP, BorderLayout.CENTER);
            fieldP.setBorder(new TitledBorder("Select Members/Fileds to Display"));
            contentPane.add(fieldP, BorderLayout.NORTH);
        }
        else if (((ScalarDS)dataset).isText())
            heightGap = 70;
        else
            contentPane.add(viewP, BorderLayout.NORTH);

        // setup GUI for dimension and subset selection
        JPanel selectionP = new JPanel();
        selectionP.setLayout(new GridLayout(rows, 6, 10, 3));
        selectionP.setBorder(new TitledBorder("Dimension and Subset Selection"));
        contentPane.add(selectionP, BorderLayout.CENTER);

        selectionP.add(new JLabel(" "));
        selectionP.add(new JLabel(" "));
        JLabel label = new JLabel("Start:");
        //label.setHorizontalAlignment(JLabel.CENTER);
        selectionP.add(label);
        label = new JLabel("Count:");
        //label.setHorizontalAlignment(JLabel.CENTER);
        selectionP.add(label);
        label = new JLabel("Stride:");
        //label.setHorizontalAlignment(JLabel.CENTER);
        selectionP.add(label);
        label = new JLabel("Max Size");
        //label.setHorizontalAlignment(JLabel.CENTER);
        selectionP.add(label);

        choices = new Choice[rows-1];
        maxLabels = new JLabel[rows-1];
        startFields = new JTextField[rows-1];
        countFields = new JTextField[rows-1];
        strideFields = new JTextField[rows-1];
        JLabel dimLabels[] = {
            new JLabel("Height", JLabel.RIGHT),
            new JLabel("Width", JLabel.RIGHT),
            new JLabel("Depth", JLabel.RIGHT),
            new JLabel("Slice", JLabel.RIGHT),
            };

        for (int i=0; i<rows-1; i++)
        {
            choices[i] = new Choice();
            choices[i].addItemListener(this);
            for (int j=0; j<rank; j++)
                choices[i].add("dim "+j);
            maxLabels[i] = new JLabel("0");
            startFields[i] = new JTextField("0");
            countFields[i] = new JTextField("0");
            strideFields[i] = new JTextField("1");
            selectionP.add(dimLabels[i]);
            selectionP.add(choices[i]);
            selectionP.add(startFields[i]);
            selectionP.add(countFields[i]);
            selectionP.add(strideFields[i]);
            selectionP.add(maxLabels[i]);
        }

        if (startFields.length >=4 )
            startFields[3].addActionListener(this);

        // add OK and CANCEL buttons
        JPanel confirmP = new JPanel();
        contentPane.add(confirmP, BorderLayout.SOUTH);
        JButton button = new JButton("   Ok   ");
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
        contentPane.setPreferredSize(new Dimension(420, heightGap+30*rows));
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
        else if (startFields.length>3 && source.equals(startFields[3]))
        {
            setSlicePosition();
        }

    }

    public void itemStateChanged(ItemEvent e)
    {
        Object source = e.getSource();

        if (source.equals(imageButton) ||
            source.equals(spreadsheetButton))
        {
            choicePalette.setEnabled( imageButton.isSelected() && !isTrueColorImage);
        }
        else if (source instanceof Choice && !source.equals(choicePalette))
        {
            int theSelectedChoice = 0;
            Choice theChoice = (Choice)source;

            int n = Math.min(3, choices.length);
            for (int i=0; i<n; i++)
            {
                if (theChoice.equals(choices[i]))
                    theSelectedChoice = i;
            }

            int theIndex = theChoice.getSelectedIndex();
            if (theIndex == currentIndex[theSelectedChoice])
                return; // select the same item, no change

            String tmpValue = null;
            boolean isChoiceSet = false;
            for (int i=0; i<n; i++)
            {
                if (choices[i].getSelectedIndex() == theIndex &&
                    i != theSelectedChoice)
                {
                    // exchange the selected item
                    tmpValue = maxLabels[theSelectedChoice].getText();
                    maxLabels[theSelectedChoice].setText( maxLabels[i].getText());
                    maxLabels[i].setText(tmpValue);
                    tmpValue = startFields[theSelectedChoice].getText();
                    startFields[theSelectedChoice].setText( startFields[i].getText());
                    startFields[i].setText(tmpValue);
                    tmpValue = countFields[theSelectedChoice].getText();
                    if (i == 2)
                    {
                        countFields[theSelectedChoice].setText(String.valueOf(dims[theIndex]-1));
                        countFields[2].setText("1");
                    }
                    else
                    {
                        countFields[theSelectedChoice].setText( countFields[i].getText());
                        countFields[i].setText(tmpValue);
                    }
                    choices[i].select(currentIndex[theSelectedChoice]);
                    currentIndex[i] = currentIndex[theSelectedChoice];
                    currentIndex[theSelectedChoice] = theIndex;
                    isChoiceSet = true;

                } // if (choices[i].getSelectedIndex() == theIndex)
            } // for (int i=0; i<n; i++)

            if (rank > 3 && !isChoiceSet)
            {
                String seletedItemOther = choices[3].getSelectedItem();
                boolean isSelected = seletedItemOther.equals(theChoice.getSelectedItem());

                tmpValue = maxLabels[theSelectedChoice].getText();
                maxLabels[theSelectedChoice].setText(String.valueOf(dims[theIndex]));
                if (isSelected)
                    maxLabels[3].setText(tmpValue);

                tmpValue = startFields[theSelectedChoice].getText();
                startFields[theSelectedChoice].setText( String.valueOf(start[theIndex]));
                if (isSelected)
                    startFields[3].setText(tmpValue);

                String seletedItem = theChoice.getItem(currentIndex[theSelectedChoice]);
                choices[3].remove(theChoice.getSelectedItem());
                choices[3].add(seletedItem);
                if (isSelected)
                    choices[3].select(seletedItem);

                currentIndex[theSelectedChoice] = theIndex;
                countFields[theSelectedChoice].setText(String.valueOf(dims[theIndex]-1));
            }

        } // else if (source instanceof Choice)
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
        if (dataset instanceof H4GRImage)
        {
            isImage = true;
            H4GRImage h4image = (H4GRImage)dataset;
            int n = h4image.getComponentCount();
            if (n >= 3) isTrueColorImage = true;
        }
        else if (dataset instanceof H5ScalarDS)
        {
            H5ScalarDS h5sd = (H5ScalarDS)dataset;
            isImage = h5sd.isImage();
            int interlace = h5sd.getInterlace();
            isTrueColorImage = (interlace == ScalarDS.INTERLACE_PIXEL ||
                interlace == ScalarDS.INTERLACE_PLANE);
        }
        else if (dataset instanceof CompoundDS)
        {
            imageButton.setEnabled(false);
        }
        imageButton.setSelected(isImage);
        choicePalette.setEnabled(isImage && !isTrueColorImage);

        int n = Math.min(3, choices.length);
        for(int i=0; i<n; i++)
        {
            int idx = selectedIndex[i];
            choices[i].select(idx);
            maxLabels[i].setText(String.valueOf(dims[idx]));
            startFields[i].setText(String.valueOf(start[idx]));
            countFields[i].setText(String.valueOf(selected[idx]+start[idx]));
            if (dataset instanceof H4Vdata)
                strideFields[i].setEnabled(false);
            else
                strideFields[i].setText(String.valueOf(stride[idx]));
        }

        if (rank > 2 )
        {
            countFields[2].setEnabled(false);
            strideFields[2].setEnabled(false);
            if (isTrueColorImage && imageButton.isSelected())
            {
                choices[0].setEnabled(false);
                choices[1].setEnabled(false);
                choices[2].setEnabled(false);
                startFields[2].setEnabled(false);
                startFields[2].setText("0");
                countFields[2].setText("2");
            }
            else
            {
                choices[0].setEnabled(true);
                choices[1].setEnabled(true);
                choices[2].setEnabled(true);
                startFields[2].setEnabled(true);
                startFields[2].setText(String.valueOf(start[selectedIndex[2]]));
                countFields[2].setText("1");
            }
        }

        if (rank >3)
        {
            countFields[3].setEnabled(false);
            strideFields[3].setEnabled(false);
            for (int i=0; i<3; i++)
                choices[3].remove(choices[i].getSelectedItem());
            choices[3].select(0);
            String dimName = choices[3].getSelectedItem().substring(4);

            int idx = Integer.parseInt(dimName);
            maxLabels[3].setText(String.valueOf(dims[idx]));
            startFields[3].setText(String.valueOf(start[idx]));
            countFields[3].setText("1");
        }

        currentIndex = new int[choices.length];
        for (int i=0; i<n; i++)
        {
            currentIndex[i] = choices[i].getSelectedIndex();
        }
    }

    private void setPalette()
    {
        byte[][] pal = null;
        int palChoice = choicePalette.getSelectedIndex();

        if (palChoice == 2)
            pal = ImageView.createGrayPalette();
        else if (palChoice == 3)
            pal = ImageView.createRainbowPalette();
        else if (palChoice == 4)
            pal = ImageView.createNaturePalette();
        else if (palChoice == 5)
            pal = ImageView.createWavePalette();
        else
            pal = null;

        ((ScalarDS)dataset).setPalette(pal);
    }

    private boolean setSelection()
    {
        if (!setSlicePosition())
            return false;

        long[] n0 = {0, 0, 0};
        long[] n1= {0, 0, 0};
        long[] n2= {1, 1, 1};
        int[] sIndex = {0, 1, 2};

        int n = Math.min(3, choices.length);
        for (int i=0; i<n; i++)
        {
            sIndex[i] = choices[i].getSelectedIndex();

            try {
                n0[i] = Long.parseLong(startFields[i].getText());
                if (i<2)
                {
                    n1[i] = Long.parseLong(countFields[i].getText());
                    n2[i] = Long.parseLong(strideFields[i].getText());
                }
            } catch (NumberFormatException ex) {
                toolkit.beep();
                JOptionPane.showMessageDialog(
                    (Frame)viewer,
                    ex,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (n0[i] < 0 ||
                n0[i] >= dims[sIndex[i]] ||
                (n0[i]+n1[i]*n2[i]) > dims[sIndex[i]])
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
            start[selectedIndex[i]] = n0[i];
            if (i<2)
            {
                selected[selectedIndex[i]] = n1[i];
                stride[selectedIndex[i]] = n2[i];
            }
        }

        if (rank >2 &&
            isTrueColorImage &&
            imageButton.isSelected())
        {
                start[selectedIndex[2]] = 0;
                selected[selectedIndex[2]] = 3;
        }

        dataset.setStride(stride);

        return true;
    }

    private boolean setSlicePosition()
    {
        if (startFields.length < 4)
            return true;

        long n0 = 0;
        try {
            n0 = Long.parseLong(startFields[3].getText());
        } catch (NumberFormatException ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                (Frame)viewer,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int sIdx = 0;
        try {
            sIdx = Integer.parseInt(choices[3].getSelectedItem().substring(4));
        } catch (NumberFormatException ex) {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                (Frame)viewer,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (n0 < 0 || n0 >= dims[sIdx])
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(
                (Frame)viewer,
                "Invalid starting point: "+n0,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        start[sIdx] = n0;

        return true;
    }


}


