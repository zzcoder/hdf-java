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

import javax.swing.*;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Dialog;
import java.awt.Choice;
import java.util.*;
import java.lang.reflect.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.*;
import ncsa.hdf.object.*;

/** NewAttributeDialog displays components for adding new attribute. */
public class NewAttributeDialog extends JDialog
implements ActionListener
{
    /** the object which the attribute to be attached to */
    private HObject hObject;

    private Attribute newAttribute;

    /** TextField for entering the name of the dataset */
    private JTextField nameField;

    /** The Choice of the datatypes */
    private Choice typeChoice;

    /** TextField for entering the attribute value. */
    private JTextField valueField;

    /** TextField for entering the length of the data array or string. */
    private JTextField lengthField;

    /** flag to indicate if the dataset is created */
    private boolean isAttributeCreated;

    private JScrollPane objListScroller;

    private final boolean isH5;

    private JDialog helpDialog;

    /** Constructs NewAttributeDialog with specified object (dataset, group, or
     *  image) which the new attribute to be attached to.
     *  @param owner the owner of the input
     *  @param obj the object which the attribute to be attached to.
     */
    public NewAttributeDialog(Dialog owner, HObject obj)
    {
        super (owner, "New Attribute...", true);

        hObject = obj;
        newAttribute = null;
        isH5 = (obj.getFileFormat() instanceof H5File);
        helpDialog = null;

        typeChoice = new Choice();
        typeChoice.add("string");
        typeChoice.add("byte (8-bit)");
        typeChoice.add("short (16-bit)");
        typeChoice.add("int (32-bit)");
        typeChoice.add("unsigned byte (8-bit)");
        typeChoice.add("unsigned short (16-bit)");
        typeChoice.add("unsigned int (32-bit)");
        typeChoice.add("long (64-bit)");
        typeChoice.add("float (native)");
        typeChoice.add("double (native)");
        if (hObject.getFileFormat() instanceof H5File)
            typeChoice.add("object reference");

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(20,10,0,10));
        contentPane.setPreferredSize(new Dimension(400, 180));

        JButton okButton = new JButton("   Ok   ");
        okButton.setActionCommand("Ok");
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.addActionListener(this);

        JButton helpButton = new JButton(" Help ");
        helpButton.setActionCommand("Show help");
        helpButton.setMnemonic(KeyEvent.VK_H);
        helpButton.addActionListener(this);

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(5,5));
        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(4,1,3,3));
        p2.add(new JLabel("Name: "));
        p2.add(new JLabel("Type: "));
        p2.add(new JLabel("Length: "));
        p2.add(new JLabel("Value: "));
        p.add("West", p2);

        p2 = new JPanel();
        p2.setLayout(new GridLayout(4,1,3,3));
        p2.add(nameField=new JTextField("",30));
        p2.add(typeChoice);
        p2.add(lengthField=new JTextField("1"));
        p2.add(valueField=new JTextField("0"));
        p.add("Center", p2);

        contentPane.add("Center", p);

        p = new JPanel();
        p.add(okButton);
        p.add(cancelButton);
        p.add(helpButton);
        contentPane.add("South", p);

        Point l = owner.getLocation();
        l.x += 50;
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
            if (createAttribute())
                dispose();
        }
        else if (cmd.equals("Cancel"))
        {
            newAttribute= null;
            dispose();
        }
        else if (cmd.equals("Show help"))
        {
            if (helpDialog == null)
                createHelpDialog();
            helpDialog.show();
        }
        else if (cmd.equals("Hide help"))
        {
            if (helpDialog != null)
                helpDialog.hide();
        }
    }

    private boolean createAttribute()
    {
        int string_length = 0;
        int tclass=-1, tsize=-1, torder=-1, tsign=-1;

        Object value = null;
        String dt = typeChoice.getSelectedItem();
        String strValue = valueField.getText();

        String attrName = nameField.getText();
        if (attrName != null) attrName = attrName.trim();

        if (attrName == null || attrName.length()<1)
        {
            JOptionPane.showMessageDialog(this,
                "No attribute name.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String lengthStr = lengthField.getText();

        int arraySize = 0;
        if (lengthStr == null || lengthStr.length() <=0)
        {
            arraySize = 1;
        }
        else
        {
            try { arraySize = Integer.parseInt(lengthStr); }
            catch (Exception e) { arraySize  = -1; }
        }

        if (arraySize <=0 )
        {
            JOptionPane.showMessageDialog(this,
                "Invalid attribute length.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        StringTokenizer st = new StringTokenizer(strValue, ",");
        int count = Math.min(arraySize, st.countTokens());
        String theToken;

        if (dt.startsWith("byte"))
        {
            byte[] b = new byte[arraySize];
            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { b[j] = Byte.parseByte(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            value = b;
            tclass = Datatype.CLASS_INTEGER;
            tsize = 1;
            torder = Datatype.NATIVE;
        }
        else if (dt.startsWith("short"))
        {
            short[] s = new short[arraySize];

            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { s[j] = Short.parseShort(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            value = s;
            tclass = Datatype.CLASS_INTEGER;
            tsize = 2;
            torder = Datatype.NATIVE;
        }
        else if (dt.startsWith("int"))
        {
            int[] i = new int[arraySize];

            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { i[j] = Integer.parseInt(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            value = i;
            tclass = Datatype.CLASS_INTEGER;
            tsize = 4;
            torder = Datatype.NATIVE;
        }
        else if (dt.startsWith("unsigned byte"))
        {
            byte[] b = new byte[arraySize];
            short sv = 0;
            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { sv = Short.parseShort(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (sv <0 ) sv = 0;
                else if ( sv > 255) sv = 255;
                b[j] = (byte) sv;
            }
            value = b;

            tclass = Datatype.CLASS_INTEGER;
            tsize = 1;
            torder = Datatype.NATIVE;
            tsign = Datatype.SIGN_NONE;
        }
        else if (dt.startsWith("unsigned short"))
        {
            short[] s = new short[arraySize];
            int iv = 0;
            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { iv = Integer.parseInt(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (iv <0 ) iv = 0;
                else if ( iv > 65535) iv = 65535;
                s[j] = (short)iv;
            }
            value = s;
            tclass = Datatype.CLASS_INTEGER;
            tsize = 2;
            torder = Datatype.NATIVE;
            tsign = Datatype.SIGN_NONE;
        }
        else if (dt.startsWith("unsigned int"))
        {
            int[] i = new int[arraySize];
            long lv = 0;
            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { lv = Long.parseLong(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (lv <0 ) lv = 0;
                if ( lv > 4294967295L) lv = 4294967295L;
                i[j] = (int)lv;
            }
            value = i;
            tclass = Datatype.CLASS_INTEGER;
            tsize = 4;
            torder = Datatype.NATIVE;
            tsign = Datatype.SIGN_NONE;
        }
        else if (dt.startsWith("long"))
        {
            long[] l = new long[arraySize];
            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { l[j] = Long.parseLong(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            value = l;
            tclass = Datatype.CLASS_INTEGER;
            tsize = 8;
            torder = Datatype.NATIVE;
        }
        else if (dt.startsWith("float"))
        {
            float[] f = new float[arraySize];
            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { f[j] = Float.parseFloat(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (Float.isInfinite(f[j]) || Float.isNaN(f[j])) f[j] = 0;
            }
            value = f;
            tclass = Datatype.CLASS_FLOAT;
            tsize = 4;
            torder = Datatype.NATIVE;
        }
        else if (dt.startsWith("double"))
        {
            double[] d = new double[arraySize];
            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { d[j] = Double.parseDouble(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                if (Double.isInfinite(d[j]) || Double.isNaN(d[j])) d[j] = 0;
            }
            value = d;
            tclass = Datatype.CLASS_FLOAT;
            tsize = 4;
            torder = Datatype.NATIVE;
        }
        else if (dt.startsWith("object reference"))
        {
            long[] ref = new long[arraySize];
            for (int j=0; j<count; j++)
            {
                theToken = st.nextToken().trim();
                try { ref[j] = Long.parseLong(theToken); }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
            value = ref;
            tclass = H5Datatype.CLASS_REFERENCE;
            tsize = 8;
            torder = Datatype.NATIVE;
        }
        else if (dt.equals("string"))
        {
            try {
                string_length = Integer.parseInt(lengthField.getText());
            } catch (Exception e) { string_length = 0; }

            strValue += " ";
            string_length = Math.max(string_length, strValue.length());

            if (string_length <=0)
                string_length = Attribute.DEFAULT_STRING_ATTRIBUTE_LENGTH;

            lengthField.setText(String.valueOf(string_length));

            tclass = Datatype.CLASS_STRING;
            tsize = string_length;

            String[] strArray = {strValue};
            value = strArray;

            if (isH5) arraySize = 1; // support string type
            else arraySize = string_length; // array of characters
        }

        Datatype datatype = null;
        if (isH5)
            datatype = new H5Datatype(tclass, tsize, torder, tsign);
        else
            datatype = new H4Datatype(tclass, tsize, torder, tsign);

        long[] dims = {arraySize};
        Attribute attr = new Attribute(attrName, datatype.toNative(), dims);
        attr.setValue(value);

        try { hObject.writeMetadata(attr); }
        catch (Exception ex )
        {
            JOptionPane.showMessageDialog(this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        newAttribute = attr;

        return true;
    }

    /** Creates a dialog to show the help information. */
    private void createHelpDialog()
    {
        helpDialog = new JDialog(this, "Creation New Attribute");

        JPanel contentPane = (JPanel)helpDialog.getContentPane();
        contentPane.setLayout(new BorderLayout(5,5));
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
        contentPane.setPreferredSize(new Dimension(500, 400));

        JButton b = new JButton("  Ok  ");
        b.addActionListener(this);
        b.setActionCommand("Hide help");
        JPanel tmpP = new JPanel();
        tmpP.add(b);
        contentPane.add (tmpP, BorderLayout.SOUTH);

        JEditorPane infoPane = new JEditorPane();
        infoPane.setContentType("text/html");
        infoPane.setEditable(false);
        JScrollPane editorScrollPane = new JScrollPane(infoPane);
        contentPane.add (editorScrollPane, BorderLayout.CENTER);

        StringBuffer buff = new StringBuffer();
        buff.append("<html>");
        buff.append("<body>");

        buff.append("<center><h1>How to Create a New Attribute</h1></center>");
        buff.append("<p>The following instruction expalins how to create a new ");
        buff.append("attribute.");

        buff.append("<h2>1) Attribute name</h2>");
        buff.append("The name of the new attibute must follow the HDF name ");
        buff.append("rules (similar to the unix name rules). The name must not ");
        buff.append("contain any path. For example, /attribute1 or attributes/attribute1");
        buff.append("are not accepted.");

        buff.append("<h2>2) Datatype</h2>");
        buff.append("A list of predefined datatypea are given. You can only select ");
        buff.append("a datatype from the list. Other datatypes are not supported. ");
        buff.append("The size specifies the size of a single data point in bits.");

        buff.append("<h2>3) Array length</h2>");
        buff.append("Length field is used to specify the length of array or string. ");
        buff.append("You can create an attribute with a single data point or ");
        buff.append("one dimension array. Two or more dimensions are not supported.");
        buff.append("Attributes must be relatively small, such as 64KB. You should ");
        buff.append("not set the .attribute array too large.");

        buff.append("<h2>4) Attribute value</h2>");
        buff.append("Value field is used to enter the value of the attribute. ");
        buff.append("If the attribute is an array, values of the array must be ");
        buff.append("separated by a comma, for example, 12, 3, 4, 5. Other delimiters ");
        buff.append(" such as, space, tab, colon, are not acceptable.");

        buff.append("</body>");
        buff.append("</html>");

        infoPane.setText(buff.toString());

        Point l = helpDialog.getOwner().getLocation();
        l.x += 50;
        l.y += 80;
        helpDialog.setLocation(l);
        helpDialog.validate();
        helpDialog.pack();
    }

    /** return the new attribute created. */
    public Attribute getAttribute() { return newAttribute; }

}
