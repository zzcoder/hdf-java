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
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.*;
import java.lang.reflect.Array;
import ncsa.hdf.object.*;

/**
 * MathConversionDialog shows a message dialog requesting user input for
 * math conversion.
 * <p>
 * @version 1.3.0 10/07/2002
 * @author Peter X. Cao
 */
public class MathConversionDialog extends JDialog
implements ActionListener, ListSelectionListener
{
    private JTextField aField, bField;

    private JTextArea infoArea;

    private JList functionList;

    private Object dataValue;

    private char NT;

    private final Toolkit toolkit;

    private String[] functionDescription;

    private boolean isConverted;

    /** Constructs MathConversionDialog.
     *  @param owner the owner of the input
     *  @param data the data array to convert.
     */
    public MathConversionDialog(Frame owner, Object data)
    {
        super (owner, "Convert Data...", true);

        toolkit = Toolkit.getDefaultToolkit();
        isConverted = false;
        dataValue = data;
        NT = ' ';

        String cName = data.getClass().getName();
        int cIndex = cName.lastIndexOf("[");
        if (cIndex >= 0 ) NT = cName.charAt(cIndex+1);

        String[] functionNames = {
            "[a, b]",
            "abs (x)",
            "a + b * x",
            "pow (x, a)",
            "exp (x)",
            "ln (x)",
            "log (a, x)",
            "sin (x)",
            "cos (x)",
            "tan (x)"};
        functionList = new JList(functionNames);
        functionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        functionList.addListSelectionListener(this);

        String[] tmpStrs = {
            "The filter by lower and upper bounds. \nx=a if x<a; x=b if x>b.",
            "The absolute value of a number, the number without its sign.",
            "Linear function.",
            "The result of a number raised to power of a.",
            "The exponential number e (i.e., 2.718...) raised to the power of x.",
            "The natural logarithm (base e) of x.",
            "The logarithm of x to the base of a, a must be an integer greater than zero.",
            "The trigonometric sine of angle x in radians.",
            "The trigonometric cosine of angle x in radians.",
            "The trigonometric tangent of angle x in radians."};

        functionDescription = tmpStrs;

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
        centerP.setLayout(new BorderLayout(10, 10));
        JScrollPane scroller = new JScrollPane(functionList);
        centerP.add(scroller, BorderLayout.CENTER);

        JPanel tmpP = new JPanel();
        tmpP.setLayout(new BorderLayout(5,5));

        JPanel tmpP0 = new JPanel();
        tmpP0.setLayout(new GridLayout(4,1,5,5));
        tmpP0.add(new JLabel("a = "));
        tmpP0.add(new JLabel("b = "));
        tmpP0.add(new JLabel(""));
        tmpP0.add(new JLabel(""));
        tmpP.add(tmpP0, BorderLayout.WEST);

        tmpP0 = new JPanel();
        tmpP0.setLayout(new GridLayout(4,1,5,5));
        tmpP0.add(aField = new JTextField("0          "));
        tmpP0.add(bField = new JTextField("1"));
        tmpP0.add(new JLabel(""));
        tmpP0.add(new JLabel(""));
        tmpP.add(tmpP0, BorderLayout.CENTER);

        centerP.add(tmpP, BorderLayout.EAST);

        centerP.setBorder(new TitledBorder("Converting Data With A Mathematic Function"));
        centerP.add(infoArea = new JTextArea(2, 50), BorderLayout.SOUTH);
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        aField.setEnabled(false);
        bField.setEnabled(false);

        contentPane.add(centerP, BorderLayout.CENTER);

        // locate the H5Property dialog
        Point l = owner.getLocation();
        l.x += 250;
        l.y += 80;
        setLocation(l);
        validate();
        pack();
    }

    private boolean convertData()
    {
        boolean status = false;
        double a=0, b=1;

        int index = functionList.getSelectedIndex();
        try
        {
            if (index==0 || index == 2)
            {
                a = Double.parseDouble(aField.getText());
                b = Double.parseDouble(bField.getText());
            }
            else if (index == 3)
            {
                a = Double.parseDouble(aField.getText());
            }
            else if (index == 6)
            {
                a = Integer.parseInt(aField.getText());
                if (a <=0)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this,
                        "a must be an integer greater than zero.",
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (Exception ex)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int n = Array.getLength(dataValue);
        switch (NT)
        {
            case 'B':
                byte[] bdata = (byte[])dataValue;
                for (int i=0; i<n; i++)
                {
                    switch (index)
                    {
                        case 0:
                            if (bdata[i] < a) bdata[i] = (byte)a;
                            else if (bdata[i] > b) bdata[i] = (byte)b;
                            break;
                        case 1:
                            bdata[i] = (byte)Math.abs(bdata[i]);  break;
                        case 2:
                            bdata[i] = (byte)(a+b*bdata[i]); break;
                        case 3:
                            bdata[i] = (byte)Math.pow(bdata[i], a); break;
                        case 4:
                            bdata[i] = (byte)Math.exp(bdata[i]); break;
                        case 5:
                            bdata[i] = (byte)Math.log(bdata[i]); break;
                        case 6:
                            bdata[i] = (byte)(Math.log(bdata[i])/Math.log(a)); break;
                        case 7:
                            bdata[i] = (byte)Math.sin(bdata[i]); break;
                        case 8:
                            bdata[i] = (byte)Math.cos(bdata[i]); break;
                        case 9:
                            bdata[i] = (byte)Math.tan(bdata[i]); break;
                        default:
                            break;
                    }
                }
                break;
            case 'S':
                short[] sdata = (short[])dataValue;
                for (int i=0; i<n; i++)
                {
                    switch (index)
                    {
                        case 0:
                            if (sdata[i] < a) sdata[i] = (short)a;
                            else if (sdata[i] > b) sdata[i] = (short)b;
                            break;
                        case 1:
                            sdata[i] = (short)Math.abs(sdata[i]);  break;
                        case 2:
                            sdata[i] = (short)(a+b*sdata[i]); break;
                        case 3:
                            sdata[i] = (short)Math.pow(sdata[i], a); break;
                        case 4:
                            sdata[i] = (short)Math.exp(sdata[i]); break;
                        case 5:
                            sdata[i] = (short)Math.log(sdata[i]); break;
                        case 6:
                            sdata[i] = (short)(Math.log(sdata[i])/Math.log(a)); break;
                        case 7:
                            sdata[i] = (short)Math.sin(sdata[i]); break;
                        case 8:
                            sdata[i] = (short)Math.cos(sdata[i]); break;
                        case 9:
                            sdata[i] = (short)Math.tan(sdata[i]); break;
                        default:
                            break;
                    }
                }
                break;
            case 'I':
                int[] idata = (int[])dataValue;
                for (int i=0; i<n; i++)
                {
                    switch (index)
                    {
                        case 0:
                            if (idata[i] < a) idata[i] = (int)a;
                            else if (idata[i] > b) idata[i] = (int)b;
                            break;
                        case 1:
                            idata[i] = (int)Math.abs(idata[i]);  break;
                        case 2:
                            idata[i] = (int)(a+b*idata[i]); break;
                        case 3:
                            idata[i] = (int)Math.pow(idata[i], a); break;
                        case 4:
                            idata[i] = (int)Math.exp(idata[i]); break;
                        case 5:
                            idata[i] = (int)Math.log(idata[i]); break;
                        case 6:
                            idata[i] = (int)(Math.log(idata[i])/Math.log(a)); break;
                        case 7:
                            idata[i] = (int)Math.sin(idata[i]); break;
                        case 8:
                            idata[i] = (int)Math.cos(idata[i]); break;
                        case 9:
                            idata[i] = (int)Math.tan(idata[i]); break;
                        default:
                            break;
                    }
                }
                break;
            case 'J':
                long[] ldata = (long[])dataValue;
                for (int i=0; i<n; i++)
                {
                    switch (index)
                    {
                        case 0:
                            if (ldata[i] < a) ldata[i] = (long)a;
                            else if (ldata[i] > b) ldata[i] = (long)b;
                            break;
                        case 1:
                            ldata[i] = (long)Math.abs(ldata[i]);  break;
                        case 2:
                            ldata[i] = (long)(a+b*ldata[i]); break;
                        case 3:
                            ldata[i] = (long)Math.pow(ldata[i], a); break;
                        case 4:
                            ldata[i] = (long)Math.exp(ldata[i]); break;
                        case 5:
                            ldata[i] = (long)Math.log(ldata[i]); break;
                        case 6:
                            ldata[i] = (long)(Math.log(ldata[i])/Math.log(a)); break;
                        case 7:
                            ldata[i] = (long)Math.sin(ldata[i]); break;
                        case 8:
                            ldata[i] = (long)Math.cos(ldata[i]); break;
                        case 9:
                            ldata[i] = (long)Math.tan(ldata[i]); break;
                        default:
                            break;
                    }
                }
                break;
            case 'F':
                float[] fdata = (float[])dataValue;
                for (int i=0; i<n; i++)
                {
                    switch (index)
                    {
                        case 0:
                            if (fdata[i] < a) fdata[i] = (float)a;
                            else if (fdata[i] > b) fdata[i] = (float)b;
                            break;
                        case 1:
                            fdata[i] = (float)Math.abs(fdata[i]);  break;
                        case 2:
                            fdata[i] = (float)(a+b*fdata[i]); break;
                        case 3:
                            fdata[i] = (float)Math.pow(fdata[i], a); break;
                        case 4:
                            fdata[i] = (float)Math.exp(fdata[i]); break;
                        case 5:
                            fdata[i] = (float)Math.log(fdata[i]); break;
                        case 6:
                            fdata[i] = (float)(Math.log(fdata[i])/Math.log(a)); break;
                        case 7:
                            fdata[i] = (float)Math.sin(fdata[i]); break;
                        case 8:
                            fdata[i] = (float)Math.cos(fdata[i]); break;
                        case 9:
                            fdata[i] = (float)Math.tan(fdata[i]); break;
                        default:
                            break;
                    }

                    if (fdata[i] == Float.NaN ||
                        fdata[i] <= Float.NEGATIVE_INFINITY ||
                        fdata[i] >= Float.POSITIVE_INFINITY)
                    {
                        toolkit.beep();
                        JOptionPane.showMessageDialog(this,
                            "Invalid float value: "+fdata[i],
                            getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                }
                break;
            case 'D':
                double[] ddata = (double[])dataValue;
                for (int i=0; i<n; i++)
                {
                    switch (index)
                    {
                        case 0:
                            if (ddata[i] < a) ddata[i] = (double)a;
                            else if (ddata[i] > b) ddata[i] = (double)b;
                            break;
                        case 1:
                            ddata[i] = (double)Math.abs(ddata[i]);  break;
                        case 2:
                            ddata[i] = (double)(a+b*ddata[i]); break;
                        case 3:
                            ddata[i] = (double)Math.pow(ddata[i], a); break;
                        case 4:
                            ddata[i] = (double)Math.exp(ddata[i]); break;
                        case 5:
                            ddata[i] = (double)Math.log(ddata[i]); break;
                        case 6:
                            ddata[i] = (double)(Math.log(ddata[i])/Math.log(a)); break;
                        case 7:
                            ddata[i] = (double)Math.sin(ddata[i]); break;
                        case 8:
                            ddata[i] = (double)Math.cos(ddata[i]); break;
                        case 9:
                            ddata[i] = (double)Math.tan(ddata[i]); break;
                        default:
                            break;
                    }

                    if (ddata[i] == Double.NaN ||
                        ddata[i] <= Double.NEGATIVE_INFINITY ||
                        ddata[i] >= Double.POSITIVE_INFINITY)
                    {
                        toolkit.beep();
                        JOptionPane.showMessageDialog(this,
                            "Invalid float value: "+ddata[i],
                            getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                }
                break;
            default:
                break;
        }

        return true;
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Ok"))
        {
            isConverted = convertData();
            //if (isConverted)
                dispose();
        }
        if (cmd.equals("Cancel"))
        {
            isConverted = false;
            dispose();
        }
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getValueIsAdjusting())
            return;

        if (!e.getSource().equals(functionList))
            return;

        if (functionList.isSelectionEmpty())
            return;

        int index = functionList.getSelectedIndex();
        infoArea.setText(functionDescription[index]);

        if (index==0 || index == 2)
        {
            aField.setEnabled(true);
            bField.setEnabled(true);
        }
        else if (index == 3 || index == 6)
        {
            aField.setEnabled(true);
            bField.setEnabled(false);
        }
        else
        {
            aField.setEnabled(false);
            bField.setEnabled(false);
        }
    }

    /** Returns true if the data is successfully converted. */
    public boolean isConverted()
    {
        return isConverted;
    }

}
