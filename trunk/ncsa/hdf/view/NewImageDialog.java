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

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Choice;
import java.awt.Toolkit;
import java.util.*;
import ncsa.hdf.object.*;

/**
 * NewImageDialog shows a message dialog requesting user input for creating
 * a new HDF4/5 Image.
 * <p>
 * @version 1.3.0 04/25/2002
 * @author Peter X. Cao
 */
public class NewImageDialog extends JDialog
implements ActionListener, ItemListener
{
    private JTextField nameField, widthField, heightField;

    private Choice parentChoice;

    private JRadioButton checkIndex, checkTrueColor, checkInterlacePixel, checkInterlacePlane;

    /** a list of current groups */
    private List groupList;

    private boolean isH5;

    private HObject newObject;

    private FileFormat fileFormat;

    private final Toolkit toolkit;

    /** Constructs NewImageDialog with specified list of possible parent groups.
     *  @param owner the owner of the input
     *  @param pGroup the parent group which the new group is added to.
     *  @param objs the list of all objects.
     */
    public NewImageDialog(Frame owner, Group pGroup, List objs)
    {
        super (owner, "New HDF Image...", true);

        newObject = null;

        isH5 = (pGroup instanceof H5Group);
        fileFormat = pGroup.getFileFormat();
        toolkit = Toolkit.getDefaultToolkit();

        groupList = new Vector();
        parentChoice = new Choice();
        Object obj = null;
        Iterator iterator = objs.iterator();
        while (iterator.hasNext())
        {
            obj = iterator.next();
            if (obj instanceof Group)
            {
                Group g = (Group)obj;
                groupList.add(obj);
                if (g.isRoot())
                    parentChoice.addItem(HObject.separator);
                else
                    parentChoice.addItem(g.getPath()+g.getName()+HObject.separator);
            }
        }
        if (pGroup.isRoot())
            parentChoice.select(HObject.separator);
        else
            parentChoice.select(pGroup.getPath()+pGroup.getName()+HObject.separator);

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));
        contentPane.setPreferredSize(new Dimension(400, 250));

        JButton okButton = new JButton("   Ok   ");
        okButton.setActionCommand("Ok");
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        // set OK and CANCEL buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // set name, parent, width and height panel
        JPanel centerP = new JPanel();
        centerP.setLayout(new BorderLayout(5,5));
        JPanel tmpP = new JPanel();
        tmpP.setLayout(new GridLayout(6, 1, 5, 5));
        tmpP.add(new JLabel("Image name: "));
        tmpP.add(new JLabel("Parent group: "));
        tmpP.add(new JLabel("Width: "));
        tmpP.add(new JLabel("Height: "));
        tmpP.add(new JLabel("Image type: "));
        tmpP.add(new JLabel("Data layout: "));
        centerP.add(tmpP, BorderLayout.WEST);

        tmpP = new JPanel();
        tmpP.setLayout(new GridLayout(6, 1, 5, 5));
        tmpP.add(nameField=new JTextField());
        tmpP.add(parentChoice);
        tmpP.add(widthField=new JTextField());
        tmpP.add(heightField=new JTextField());

        JPanel tmpP0 = new JPanel();
        tmpP0.setLayout(new GridLayout(1,2));
        tmpP0.add(checkIndex = new JRadioButton("Indexed colormap", true));
        tmpP0.add(checkTrueColor = new JRadioButton("24-bit truecolor"));
        tmpP0.setBorder(new TitledBorder(""));
        tmpP.add(tmpP0);

        tmpP0 = new JPanel();
        tmpP0.setLayout(new GridLayout(1,2));
        tmpP0.add(checkInterlacePixel = new JRadioButton("Pixel interlace"));
        tmpP0.add(checkInterlacePlane = new JRadioButton("Plane interlace"));
        tmpP0.setBorder(new TitledBorder(""));
        tmpP.add(tmpP0);

        centerP.add(tmpP, BorderLayout.CENTER);

        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(checkInterlacePixel);
        bgroup.add(checkInterlacePlane);
        bgroup = new ButtonGroup();
        bgroup.add(checkTrueColor);
        bgroup.add(checkIndex);
        checkIndex.addItemListener(this);
        checkTrueColor.addItemListener(this);
        checkInterlacePixel.setSelected(true);
        checkInterlacePixel.setEnabled(false);
        checkInterlacePlane.setEnabled(false);

        centerP.setBorder(new TitledBorder(""));
        contentPane.add(centerP, BorderLayout.CENTER);

        // locate the H5Property dialog
        Point l = owner.getLocation();
        l.x += 250;
        l.y += 80;
        setLocation(l);
        validate();
        pack();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Ok"))
        {
            newObject = createHDFimage();
            if (newObject != null)
                dispose();
        }
        if (cmd.equals("Cancel"))
        {
            newObject = null;
            dispose();
        }
    }

    public void itemStateChanged(ItemEvent e)
    {
        Object source = e.getSource();

        if (source.equals(checkIndex))
        {
            checkInterlacePixel.setSelected(true);
            checkInterlacePixel.setEnabled(false);
            checkInterlacePlane.setEnabled(false);
        }
        else if (source.equals(checkTrueColor))
        {
            checkInterlacePixel.setEnabled(true);
            checkInterlacePlane.setEnabled(true);
        }
    }

    private Dataset createHDFimage()
    {
        Dataset dataset = null;

        String name = nameField.getText();
        if (name != null) name = name.trim();
        if (name == null || name.length()<=0)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Dataset name is not specified.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (name.indexOf(HObject.separator) >= 0)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Dataset name cannot contain path.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        Group pgroup = (Group)groupList.get(parentChoice.getSelectedIndex());
        if (pgroup == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Select a parent group.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int w=0, h=0;
        try {
            w = Integer.parseInt(widthField.getText());
            h = Integer.parseInt(heightField.getText());
        } catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        long[] dims = null;
        int tclass = Datatype.CLASS_CHAR;
        int tsign = Datatype.SIGN_NONE;
        int tsize = 1;
        int torder = Datatype.NATIVE;
        int interlace = ScalarDS.INTERLACE_PIXEL;
        int ncomp = 2;

        if (checkIndex.isSelected())
        {
            // indexed colormap
            if (isH5)
            {
                long[] tmpdims = {h, w};
                dims = tmpdims;
            }
            else
            {
                long[] tmpdims = {w, h};
                dims = tmpdims;
            }
        }
        else
        {
            // true color image
            if (isH5)
            {
                // HDF5 true color image
                if (checkInterlacePixel.isSelected())
                {
                    long[] tmpdims = {h, w, 3};
                    dims = tmpdims;
                }
                else
                {
                    interlace = ScalarDS.INTERLACE_PLANE;
                    long[] tmpdims = {3, h, w};
                    dims = tmpdims;
                }
            }
            else
            {
                // HDF4 true color image
                ncomp = 3;
                long[] tmpdims = {w, h};
                dims = tmpdims;
                if (checkInterlacePlane.isSelected())
                    interlace = ScalarDS.INTERLACE_PLANE;
            }
        }

        try
        {
            if (isH5)
            {
                Datatype datatype = new H5Datatype(tclass, tsize, torder, tsign);
                dataset = H5ScalarDS.create(fileFormat, name, pgroup, datatype, dims, dims, null, -1, null);
                H5File.createImageAttributes(dataset, interlace);
                dataset.init();
            }
            else
            {
                Datatype datatype = new H4Datatype(tclass, tsize, torder, tsign);
                dataset = H4GRImage.create(fileFormat, name, pgroup, datatype, dims, dims, null, -1, ncomp, interlace, null);
            }
        } catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return dataset;
    }

    /** Returns the new dataset created. */
    public DataFormat getObject() { return newObject; }

    /** Returns the parent group of the new dataset. */
    public Group getParentGroup() {
        return (Group)groupList.get(parentChoice.getSelectedIndex());
    }

}
