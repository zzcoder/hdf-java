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

    /** the indices of the selected dimensions. */
    private int selectedIndex[];

    /** choice for the Vertical dimesion */
    private Choice choiceVertical;

    /** choice for the Horizontal dimesion */
    private Choice choiceHorizontal;

    /** choice for the depth dimesion */
    private Choice choiceDepth;

    /** TextField for the starting point of the Vertical dimesion. */
    private JTextField startVertical;

    /** TextField for the starting point of the Horizontal dimesion. */
    private JTextField startHorizontal;

    /** TextField for the starting point of the depth dimesion. */
    private JTextField startDepth;

    /** TextField for the ending point of the Vertical dimesion. */
    private JTextField endVertical;

    /** TextField for the ending point of the Horizontal dimesion. */
    private JTextField endHorizontal;

    /** TextField for the ending point of the depth dimesion. */
    private JTextField endDepth;

    private JLabel hLabel, vLabel, dLabel;

    private int idxVertical, idxHorizontal, idxDepth;

    private JRadioButton spreadsheetButton, imageButton;

    private Choice choicePalette;

    private boolean isSelectionCancelled;

    private boolean isTrueColor;


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
        isTrueColor = false;

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

        JButton okButton = new JButton("   Ok   ");
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.setActionCommand("Ok");
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        choiceVertical = new Choice();
        choiceHorizontal = new Choice();
        choiceDepth = new Choice();
        choiceVertical.addItemListener(this);
        choiceHorizontal.addItemListener(this);
        choiceDepth.addItemListener(this);

        choicePalette = new Choice();
        choicePalette.add("Default palette");
        choicePalette.add("Gray");
        choicePalette.add("Rainbow");
        choicePalette.add("Nature");
        choicePalette.add("Wave");
        choicePalette.addItemListener(this);

        startVertical = new JTextField();
        startHorizontal = new JTextField();
        startDepth = new JTextField();
        endVertical = new JTextField();
        endHorizontal = new JTextField();
        endDepth = new JTextField("            +0");
        startVertical.setHorizontalAlignment(JTextField.RIGHT);
        startHorizontal.setHorizontalAlignment(JTextField.RIGHT);
        startDepth.setHorizontalAlignment(JTextField.RIGHT);
        endVertical.setHorizontalAlignment(JTextField.RIGHT);
        endHorizontal.setHorizontalAlignment(JTextField.RIGHT);
        endDepth.setHorizontalAlignment(JTextField.RIGHT);
        endDepth.setEnabled(false);

        // layout the components
        JPanel contentPane = (JPanel)getContentPane();
        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel labelP = new JPanel();
        labelP.setLayout(new GridLayout(3, 1, 3, 3));
        labelP.add(hLabel=new JLabel("Width: "));
        labelP.add(vLabel=new JLabel("Height:"));
        labelP.add(dLabel=new JLabel("Depth: "));

        JPanel dimP = new JPanel();
        dimP.setLayout(new GridLayout(3, 1, 3, 3));
        dimP.add(choiceHorizontal);
        dimP.add(choiceVertical);
        dimP.add(choiceDepth);

        JPanel startP = new JPanel();
        startP.setLayout(new GridLayout(3, 1, 3, 3));
        startP.add(startHorizontal);
        startP.add(startVertical);
        startP.add(startDepth);

        JPanel endP = new JPanel();
        endP.setLayout(new GridLayout(3, 1, 3, 3));
        endP.add(endHorizontal);
        endP.add(endVertical);
        endP.add(endDepth);

        JPanel tmp1 = new JPanel();
        tmp1.setLayout(new GridLayout(1,2,5,5));
        tmp1.add(startP);
        tmp1.add(endP);

        JPanel p = new JPanel();

        spreadsheetButton = new JRadioButton("Spreadsheet", true);
        spreadsheetButton.setMnemonic(KeyEvent.VK_S);
        imageButton = new JRadioButton("Image");
        imageButton.setMnemonic(KeyEvent.VK_I);
        imageButton.addItemListener(this);
        spreadsheetButton.addItemListener(this);
        ButtonGroup rgroup = new ButtonGroup();
        rgroup.add(spreadsheetButton);
        rgroup.add(imageButton);
        p.add(spreadsheetButton);
        p.add(imageButton);
        p.add(choicePalette);
        p.setBorder(new TitledBorder("View Option"));
        contentPane.add("North", p);

        p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add("West", labelP);
        p.add("Center", dimP);
        p.add("East", tmp1);
        p.setBorder(new TitledBorder("Dimension and Range Selection"));
        contentPane.add("Center", p);

        p = new JPanel();
        p.add(okButton);
        p.add(cancelButton);
        contentPane.add("South", p);

        init();

        // locate the H5Property dialog
        Point l = getParent().getLocation();
        l.x += 250;
        l.y += 80;
        setLocation(l);
        pack();
        setSize(350, getSize().height);
    }

    private void init()
    {
        idxVertical = selectedIndex[0];
        idxHorizontal = selectedIndex[1];;
        idxDepth = selectedIndex[2];

        if (rank == 1)
        {
            choiceVertical.add("dim 0 ["+dims[0]+"]");
            choiceVertical.select(0);
            startVertical.setText(String.valueOf(start[0]));
            endVertical.setText(String.valueOf(start[0]+selected[0]-1));

            // disable the other selection choice
            choiceHorizontal.setEnabled(false);
            choiceDepth.setEnabled(false);
            startHorizontal.setEnabled(false);
            startDepth.setEnabled(false);
            endHorizontal.setEnabled(false);
            hLabel.setEnabled(false);
            dLabel.setEnabled(false);
        }
        else if (rank == 2)
        {
            choiceVertical.add("dim0 ["+dims[0]+"]");
            choiceVertical.add("dim1 ["+dims[1]+"]");
            choiceHorizontal.add("dim0 ["+dims[0]+"]");
            choiceHorizontal.add("dim1 ["+dims[1]+"]");

            choiceVertical.select(idxVertical);
            choiceHorizontal.select(idxHorizontal);

            startVertical.setText(String.valueOf(start[idxVertical]));
            startHorizontal.setText(String.valueOf(start[idxHorizontal]));
            endVertical.setText(String.valueOf(start[idxVertical]+selected[idxVertical]-1));
            endHorizontal.setText(String.valueOf(start[idxHorizontal]+selected[idxHorizontal]-1));

            choiceDepth.setEnabled(false);
            startDepth.setEnabled(false);
            dLabel.setEnabled(false);
        }
        else if (rank >= 3)
        {

            for (int i=0; i<rank; i++)
            {
                choiceVertical.add("dim"+i +" ["+dims[i]+"]");
                choiceHorizontal.add("dim"+i +" ["+dims[i]+"]");
                choiceDepth.add("dim"+i +" ["+dims[i]+"]");
            }

            choiceVertical.select(idxVertical);
            choiceHorizontal.select(idxHorizontal);
            choiceDepth.select(idxDepth);

            startVertical.setText(String.valueOf(start[idxVertical]));
            startHorizontal.setText(String.valueOf(start[idxHorizontal]));
            startDepth.setText(String.valueOf(start[idxDepth]));
            endVertical.setText(String.valueOf(start[idxVertical]+selected[idxVertical]-1));
            endHorizontal.setText(String.valueOf(start[idxHorizontal]+selected[idxHorizontal]-1));
            endDepth.setText("            +"+String.valueOf(start[idxDepth]+selected[idxDepth]-1));
        }

        boolean isImage = false;
        if (dataset instanceof H4GRImage)
        {
            isImage = true;
            H4GRImage h4image = (H4GRImage)dataset;
            int n = h4image.getComponentCount();
            if (n >= 3) isTrueColor = true;
        }
        else if (dataset instanceof H5ScalarDS)
        {
            H5ScalarDS h5sd = (H5ScalarDS)dataset;
            isImage = h5sd.isImage();
            int interlace = h5sd.getInterlace();
            isTrueColor = (interlace == ScalarDS.INTERLACE_PIXEL ||
                interlace == ScalarDS.INTERLACE_PLANE);
        } //else if (dataset instanceof H5ScalarDS)

        if (dataset instanceof CompoundDS)
            imageButton.setEnabled(false);

        imageButton.setSelected(isImage);
        choicePalette.setEnabled(isImage);

        if (isTrueColor)
        {
            // disable dimension selection
            choiceVertical.setEnabled(false);
            choiceHorizontal.setEnabled(false);
            choiceDepth.setEnabled(false);
            startDepth.setEnabled(false);
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();
        long hstart=0, vstart=0, dstart=0, hend=0, vend=0;

        if (cmd.equals("Ok"))
        {
            try {
                vstart = Long.parseLong(startVertical.getText());
                vend = Long.parseLong(endVertical.getText());

                if (rank > 1) {
                    hstart = Long.parseLong(startHorizontal.getText());
                    hend = Long.parseLong(endHorizontal.getText());
                }

                if (rank > 2)
                    dstart = Long.parseLong(startDepth.getText());
            } catch (NumberFormatException ex) {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(
                    (Frame)viewer,
                    ex,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // check if selected range is valid
            String err = null;
            if (vstart < 0 || vend >= dims[idxVertical])
                err = "Height is out of range [0, "+String.valueOf(dims[idxVertical]-1)+"]";
            else if (hstart < 0 || (rank>1 && (hend >= dims[idxHorizontal])))
                err = "Width is out of range [0, "+String.valueOf(dims[idxHorizontal]-1)+"]";
            else if (dstart < 0 ||  (rank>2 && dstart >= dims[idxDepth]))
                err = "Depth is out of range [0, "+String.valueOf(dims[idxDepth]-1)+"]";
            else if (vend < vstart)
                err = "Height-start > height-end";
            else if (hend < hstart)
                err = "Width-start > width-end";
            else
                err = null;

            if (err != null)
            {
                Toolkit.getDefaultToolkit().beep();
                JOptionPane.showMessageDialog(
                    (Frame)viewer,
                    err,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            start[idxVertical] = vstart;
            selected[idxVertical] = vend-vstart+1;
            selectedIndex[0] = choiceVertical.getSelectedIndex();

            if (rank>1)
            {
                start[idxHorizontal] = hstart;
                selected[idxHorizontal] = hend-hstart+1;
                selectedIndex[1] = choiceHorizontal.getSelectedIndex();
            }

            if (rank > 2)
            {
                start[idxDepth] = dstart;
                if (!isTrueColor)
                    selected[idxDepth] = 1; //  one page a time
                selectedIndex[2] = choiceDepth.getSelectedIndex();
            }

            // set palette for image view
            if (dataset instanceof ScalarDS &&
                imageButton.isSelected())
            {
                int palChoice = choicePalette.getSelectedIndex();
                byte[][] pal = null;
                if (palChoice == 1)
                    pal = ImageView.createGrayPalette();
                else if (palChoice == 2)
                    pal = ImageView.createRainbowPalette();
                else if (palChoice == 3)
                    pal = ImageView.createNaturePalette();
                else if (palChoice == 4)
                    pal = ImageView.createWavePalette();
                else
                    pal = null;

                ((ScalarDS)dataset).setPalette(pal);
            }

            isSelectionCancelled = false;
            dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            dispose();
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        Object source = e.getSource();
        int v_idx = choiceVertical.getSelectedIndex();
        int h_idx = choiceHorizontal.getSelectedIndex();
        int d_idx = choiceDepth.getSelectedIndex();
        String strValue = null;

        if (source.equals(choiceVertical))
        {
            if (v_idx == idxVertical)
                return;

            if (h_idx == v_idx)
            {
                // exchange width and height
                idxHorizontal = idxVertical;
                choiceHorizontal.select(idxHorizontal);

                strValue = startVertical.getText();
                startVertical.setText(startHorizontal.getText());
                startHorizontal.setText(strValue);

                strValue = endVertical.getText();
                endVertical.setText(endHorizontal.getText());
                endHorizontal.setText(strValue);
            }
            else if (v_idx == d_idx)
            {
                idxDepth = idxVertical;
                choiceDepth.select(idxDepth);

                strValue = startVertical.getText();
                startVertical.setText(startDepth.getText());
                startDepth.setText(strValue);

                endVertical.setText(String.valueOf(dims[d_idx]-1));
            }
            else
            {
                startVertical.setText(String.valueOf(start[v_idx]));
                endVertical.setText(String.valueOf(dims[v_idx]-1));
            }

            idxVertical = v_idx;
        }
        else if (source.equals(choiceHorizontal))
        {
            if (h_idx == idxHorizontal)
                return;

            if (h_idx == v_idx)
            {
                // exchange width and height
                idxVertical = idxHorizontal;
                choiceVertical.select(idxVertical);

                strValue = startHorizontal.getText();
                startHorizontal.setText(startVertical.getText());
                startVertical.setText(strValue);

                strValue = endHorizontal.getText();
                endHorizontal.setText(endVertical.getText());
                endVertical.setText(strValue);
            }
            else if (h_idx == d_idx)
            {
                idxDepth = idxHorizontal;
                choiceDepth.select(idxDepth);

                strValue = startHorizontal.getText();
                startHorizontal.setText(startDepth.getText());
                startDepth.setText(strValue);

                endHorizontal.setText(String.valueOf(dims[d_idx]-1));
            }
            else
            {
                startHorizontal.setText(String.valueOf(start[v_idx]));
                endHorizontal.setText(String.valueOf(dims[v_idx]-1));
            }

            idxHorizontal = h_idx;
        }
        else if (source.equals(choiceDepth))
        {
            if (d_idx == idxDepth)
                return;

            if (d_idx == v_idx)
            {
                // exchange depth and height
                idxVertical = idxDepth;
                choiceVertical.select(idxVertical);

                strValue = startDepth.getText();
                startDepth.setText(startVertical.getText());
                startVertical.setText(strValue);

                endVertical.setText(String.valueOf(dims[idxDepth]-1));
            }
            else if (h_idx == d_idx)
            {
                idxHorizontal = idxDepth;
                choiceHorizontal.select(idxHorizontal);

                strValue = startDepth.getText();
                startDepth.setText(startHorizontal.getText());
                startHorizontal.setText(strValue);

                endHorizontal.setText(String.valueOf(dims[idxDepth]-1));
            }
            else
            {
                startDepth.setText(String.valueOf(start[d_idx]));
            }

            idxDepth = d_idx;
        }
        else if (source.equals(imageButton) ||
            source.equals(spreadsheetButton))
        {
            choicePalette.setEnabled(imageButton.isSelected());
        }
    }

    /** Returns true if the data selection is cancelled. */
    public boolean isCancelled()
    {
        return isSelectionCancelled;
    }

    /** Returns true if the display option is image. */
    public boolean isImageDisplay() { return imageButton.isSelected(); }
}


