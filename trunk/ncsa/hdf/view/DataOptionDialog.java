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
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.*;

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

    private JTextField startFields[], endFields[], strideFields[];

    private JList fieldList;

    private final Toolkit toolkit;

    private final SubsetNavigator navigator;

    private int numberOfPalettes;

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
        numberOfPalettes = 1;
        toolkit = Toolkit.getDefaultToolkit();

        if (dataset == null)
            dispose();
        else
            setTitle("Dataset Selection - "+dataset.getPath()+dataset.getName());

        rank = dataset.getRank();
        if (rank <=0 ) dataset.init();
        if (dataset instanceof H5ScalarDS)
        {
            byte[] palRefs = ((H5ScalarDS)dataset).getPaletteRefs();
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

        choicePalette = new Choice();
        choicePalette.add("Select palette");
        choicePalette.add("Default");
        for (int i=2; i<=numberOfPalettes; i++)
        {
            choicePalette.add("Default "+i);
        }
        choicePalette.add("Gray");
        choicePalette.add("Rainbow");
        choicePalette.add("Nature");
        choicePalette.add("Wave");
        choicePalette.addItemListener(this);

        spreadsheetButton = new JRadioButton("Spreadsheet ", true);
        spreadsheetButton.setMnemonic(KeyEvent.VK_S);
        imageButton = new JRadioButton("Image");
        imageButton.setMnemonic(KeyEvent.VK_I);

        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        contentPane.setPreferredSize(new Dimension(580, 325));

        int rows = Math.min(5, rank+1);

        JPanel northP = new JPanel();
        northP.setLayout(new GridLayout(1, 1));
        if (dataset instanceof CompoundDS)
        {
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
            fieldP.setBorder(new TitledBorder("Select Members/Fields to Display"));
            northP.add(fieldP);
        }
        else if (!((ScalarDS)dataset).isText())
        {
            // setup GUI components for the display options: table or image
            imageButton.addItemListener(this);
            spreadsheetButton.addItemListener(this);
            ButtonGroup rgroup = new ButtonGroup();
            rgroup.add(spreadsheetButton);
            rgroup.add(imageButton);
            JPanel viewP = new JPanel();
            viewP.setLayout(new GridLayout(1,2));
            viewP.add(spreadsheetButton);
            JPanel imageP = new JPanel();
            imageP.setLayout(new BorderLayout());
            imageP.add(imageButton, BorderLayout.WEST);
            imageP.add(choicePalette, BorderLayout.CENTER);
            viewP.add(imageP);
            viewP.setBorder(new TitledBorder("Display As"));
            northP.add(viewP);
        }

        contentPane.add(northP, BorderLayout.NORTH);

        JPanel centerP = new JPanel();
        centerP.setLayout(new BorderLayout());
        centerP.setBorder(new TitledBorder("Dimension and Subset Selection"));

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

        choices = new Choice[rows-1];
        maxLabels = new JLabel[rows-1];
        startFields = new JTextField[rows-1];
        endFields = new JTextField[rows-1];
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
            endFields[i] = new JTextField("0");
            strideFields[i] = new JTextField("1");
            selectionP.add(dimLabels[i]);
            selectionP.add(choices[i]);
            selectionP.add(startFields[i]);
            selectionP.add(endFields[i]);
            selectionP.add(strideFields[i]);
            selectionP.add(maxLabels[i]);
        }

        for (int i=0; i<5-rows; i++)
        {
            for (int j=0; j<6; j++)
                selectionP.add(new JLabel(" "));
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

        JPanel navigatorP = new JPanel();
        int h=1, w=1;
        h = (int)dims[selectedIndex[0]];
        if (rank > 1) w = (int)dims[selectedIndex[1]];
        navigator = new SubsetNavigator(w, h);
        navigatorP.add(navigator);
        navigatorP.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        if (rank>1 && !(dataset instanceof CompoundDS))
            centerP.add(navigatorP, BorderLayout.WEST);

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
            int thisIndex =  0;
            long tmpLong = 0;
            boolean isChoiceSet = false;
            for (int i=0; i<n; i++)
            {
                thisIndex = choices[i].getSelectedIndex();
                if (thisIndex == theIndex &&  i != theSelectedChoice)
                {
                    // the selected dimension is this index
                    // exchange the selected item and reset their values

                    // reset start fields
                    startFields[theSelectedChoice].setText("0");
                    startFields[i].setText("0");

                    // reset end max labels
                    tmpValue = maxLabels[theSelectedChoice].getText();
                    maxLabels[theSelectedChoice].setText( maxLabels[i].getText());
                    tmpLong = Long.parseLong(maxLabels[i].getText())-1;
                    endFields[theSelectedChoice].setText( String.valueOf(tmpLong));
                    tmpLong = Long.parseLong(tmpValue)-1;
                    endFields[i].setText(String.valueOf(tmpLong));
                    maxLabels[i].setText(tmpValue);

                    // reset stride
                    strideFields[theSelectedChoice].setText("1");
                    strideFields[i].setText("1");

                    // reset GUI choices
                    choices[i].select(currentIndex[theSelectedChoice]);
                    currentIndex[i] = currentIndex[theSelectedChoice];
                    currentIndex[theSelectedChoice] = theIndex;
                    isChoiceSet = true;
                } // if (choices[i].getSelectedIndex() == theIndex)
            } // for (int i=0; i<n; i++)

            if (rank > 3 && !isChoiceSet)
            {
                // update the 4th slice choice menu
                String seletedItemOther = choices[3].getSelectedItem();
                boolean isSelected = seletedItemOther.equals(theChoice.getSelectedItem());

                tmpValue = maxLabels[theSelectedChoice].getText();
                maxLabels[theSelectedChoice].setText(String.valueOf(dims[theIndex]));
                if (isSelected)
                    maxLabels[3].setText(tmpValue);

                tmpValue = startFields[theSelectedChoice].getText();
                startFields[theSelectedChoice].setText( String.valueOf(start[theIndex]));
                if (isSelected) startFields[3].setText(tmpValue);

                String seletedItem = theChoice.getItem(currentIndex[theSelectedChoice]);
                choices[3].remove(theChoice.getSelectedItem());
                choices[3].add(seletedItem);
                if (isSelected) choices[3].select(seletedItem);

                currentIndex[theSelectedChoice] = theIndex;
            }

            // update the navigator
            if (rank > 1)
            {
                int hIdx = choices[0].getSelectedIndex();
                int wIdx = choices[1].getSelectedIndex();
                long dims[] = dataset.getDims();
                int w = (int)dims[wIdx];
                int h = (int)dims[hIdx];
                navigator.setDimensionSize(w, h);
                navigator.updateUI();
            }

            if (rank > 2)
                endFields[2].setText(startFields[2].getText());

            if (rank > 3)
                endFields[3].setText(startFields[3].getText());

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
        long endIdx = 0;
        for(int i=0; i<n; i++)
        {
            int idx = selectedIndex[i];
            endIdx = start[idx]+selected[idx]*stride[idx]-1;
            if (endIdx >= dims[idx]) endIdx = dims[idx]-1;

            choices[i].select(idx);
            maxLabels[i].setText(String.valueOf(dims[idx]));
            startFields[i].setText(String.valueOf(start[idx]));
            endFields[i].setText(String.valueOf(endIdx));

            if (dataset instanceof H4Vdata)
                strideFields[i].setEnabled(false);
            else
                strideFields[i].setText(String.valueOf(stride[idx]));
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
                startFields[2].setText("0");
                endFields[2].setText("2");
            }
            else
            {
                choices[0].setEnabled(true);
                choices[1].setEnabled(true);
                choices[2].setEnabled(true);
                startFields[2].setEnabled(true);
                startFields[2].setText(String.valueOf(start[selectedIndex[2]]));
                endFields[2].setText(startFields[2].getText());
            }
        }

        if (rank >3)
        {
            endFields[3].setEnabled(false);
            strideFields[3].setEnabled(false);
            for (int i=0; i<3; i++)
                choices[3].remove(choices[i].getSelectedItem());
            choices[3].select(0);
            String dimName = choices[3].getSelectedItem().substring(4);

            int idx = Integer.parseInt(dimName);
            maxLabels[3].setText(String.valueOf(dims[idx]));
            startFields[3].setText(String.valueOf(start[idx]));
            endFields[3].setText(startFields[3].getText());
        }

        currentIndex = new int[choices.length];
        for (int i=0; i<n; i++)
        {
            currentIndex[i] = choices[i].getSelectedIndex();
        }
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
            pal = ((H5ScalarDS)dataset).readPalette(palChoice-1);
        }

        ((ScalarDS)dataset).setPalette(pal);
    }

    private boolean setSelection()
    {
        if (!setSlicePosition())
            return false;

        long[] n0 = {0, 0, 0}; // start
        long[] n1= {0, 0, 0}; // end
        long[] n2= {1, 1, 1}; // stride
        int[] sIndex = {0, 1, 2};

        int n = Math.min(3, choices.length);
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
                    ex,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (n0[i] < 0 ||
                n0[i] >= dims[sIndex[i]] ||
                n1[i] >= dims[sIndex[i]])
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
                selected[selectedIndex[i]] = (int)((n1[i]-n0[i])/n2[i]+1);
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

        //clear the old data
        dataset.clearData();

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
            selectedIndex[0] = choices[0].getSelectedIndex();
            selectedIndex[1] = choices[1].getSelectedIndex();
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
            int s = Integer.parseInt(strideFields[0].getText());
            startFields[0].setText(String.valueOf((int)(y0*r)));
            endFields[0].setText(String.valueOf((int)((y0+h)*r-1)));

            if (rank > 1)
            {
                s = Integer.parseInt(strideFields[1].getText());
                startFields[1].setText(String.valueOf((int)(x0*r)));
                endFields[1].setText(String.valueOf((int)((x0+w)*r-1)));
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


}


