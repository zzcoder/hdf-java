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
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Choice;
import java.awt.Toolkit;
import java.io.File;
import java.util.*;
import ncsa.hdf.object.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * NewDatasetDialog shows a message dialog requesting user input for creating
 * a new HDF4/5 dataset.
 * <p>
 * @version 1.3.0 04/25/2002
 * @author Peter X. Cao
 */
public class NewDatasetDialog extends JDialog
implements ActionListener, ItemListener, HyperlinkListener
{
    private JTextField nameField, currentSizeField, maxSizeField, chunkSizeField, stringLengthField;

    private Choice parentChoice, classChoice, sizeChoice, endianChoice, rankChoice, compressionLevel;

    private JCheckBox checkUnsigned, checkCompression;

    private JRadioButton checkContinguous, checkChunked;

    private JDialog helpDialog;

    /** a list of current groups */
    private List groupList;

    private boolean isH5;

    private HObject newObject;

    private FileFormat fileFormat;

    private final Toolkit toolkit;


    /** Constructs NewDatasetDialog with specified list of possible parent groups.
     *  @param owner the owner of the input
     *  @param pGroup the parent group which the new group is added to.
     *  @param objs the list of all objects.
     */
    public NewDatasetDialog(Frame owner, Group pGroup, List objs)
    {
        super (owner, "New Dataset...", true);

        helpDialog = null;
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
        contentPane.setBorder(BorderFactory.createEmptyBorder(15,5,5,5));
        contentPane.setPreferredSize(new Dimension(550, 350));

        JButton okButton = new JButton("   Ok   ");
        okButton.setActionCommand("Ok");
        okButton.setMnemonic(KeyEvent.VK_O);
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setMnemonic(KeyEvent.VK_C);
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        JButton helplButton = new JButton("Help");
        helplButton.setMnemonic(KeyEvent.VK_H);
        helplButton.setActionCommand("Show help");
        helplButton.addActionListener(this);

        // set OK and CANCEL buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(helplButton);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // set NAME and PARENT GROUP panel
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout(5,5));
        JPanel tmpP = new JPanel();
        tmpP.setLayout(new GridLayout(2,1));
        tmpP.add(new JLabel("Dataset name: "));
        tmpP.add(new JLabel("Add to group: "));
        namePanel.add(tmpP, BorderLayout.WEST);
        tmpP = new JPanel();
        tmpP.setLayout(new GridLayout(2,1));
        tmpP.add(nameField=new JTextField("dataset",40));
        tmpP.add(parentChoice);
        namePanel.add(tmpP, BorderLayout.CENTER);
        contentPane.add(namePanel, BorderLayout.NORTH);

        // set DATATYPE
        JPanel typePanel = new JPanel();
        typePanel.setLayout(new GridLayout(2,4,15,3));
        TitledBorder border = new TitledBorder("Datatype");
        border.setTitleColor(Color.blue);
        typePanel.setBorder(border);

        classChoice = new Choice();
        sizeChoice = new Choice();
        endianChoice = new Choice();
        stringLengthField = new JTextField("String length");
        stringLengthField.setEnabled(false);

        classChoice.add("INTEGER");
        classChoice.add("FLOAT");
        classChoice.add("CHAR");


        typePanel.add(new JLabel("Datatype class"));
        typePanel.add(new JLabel("Size (bits)"));
        typePanel.add(new JLabel("Byte ordering"));
        typePanel.add(checkUnsigned = new JCheckBox("Unsigned"));
        typePanel.add(classChoice);
        typePanel.add(sizeChoice);
        typePanel.add(endianChoice);

        if (isH5)
        {
            classChoice.add("STRING");
            classChoice.add("REFERENCE");
            sizeChoice.add("NATIVE");
            endianChoice.add("NATIVE");
            endianChoice.add("LITTLE ENDIAN");
            endianChoice.add("BIG ENDIAN");
            typePanel.add(stringLengthField);
        }
        else
        {
            sizeChoice.add("DEFAULT");
            endianChoice.add("DEFAULT");
            endianChoice.setEnabled(false);
            typePanel.add(new JLabel());
        }
        sizeChoice.add("64");
        sizeChoice.add("32");
        sizeChoice.add("16");
        sizeChoice.add("8");

        // set DATATSPACE
        JPanel spacePanel = new JPanel();
        spacePanel.setLayout(new GridLayout(2,3,15,3));
        border = new TitledBorder("Dataspace");
        border.setTitleColor(Color.blue);
        spacePanel.setBorder(border);
        rankChoice = new Choice();
        for (int i=1; i<33; i++)
            rankChoice.add(String.valueOf(i));
        rankChoice.select(1);
        currentSizeField = new JTextField("1 x 1");
        maxSizeField = new JTextField("0 x 0");
        spacePanel.add(new JLabel("No. of dimensions"));
        spacePanel.add(new JLabel("Current dimension size"));
        spacePanel.add(new JLabel("Max dimension size"));
        spacePanel.add(rankChoice);
        spacePanel.add(currentSizeField);
        spacePanel.add(maxSizeField);
        maxSizeField.setEnabled(isH5);

        // set storage layout and data compression
        JPanel layoutPanel = new JPanel();
        layoutPanel.setLayout(new GridLayout(2,4));
        border = new TitledBorder("Data Layout and Compression");
        border.setTitleColor(Color.blue);
        layoutPanel.setBorder(border);
        checkContinguous = new JRadioButton("Contiguous");
        checkContinguous.setSelected(true);
        checkChunked = new JRadioButton("Chunked");
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(checkChunked);
        bgroup.add(checkContinguous);
        chunkSizeField = new JTextField("1 x 1");
        chunkSizeField.setEnabled(false);
        checkCompression = new JCheckBox("gzip");
        compressionLevel = new Choice();
        for (int i=0; i<10; i++)
        {
            compressionLevel.add(String.valueOf(i));
        }
        compressionLevel.select(6);
        compressionLevel.setEnabled(false);
        layoutPanel.add(new JLabel("Storage layout:"));
        layoutPanel.add(checkContinguous);
        layoutPanel.add(checkChunked);
        tmpP = new JPanel();
        tmpP.setLayout(new BorderLayout());
        tmpP.add(new JLabel("Size:"), BorderLayout.WEST);
        tmpP.add(chunkSizeField, BorderLayout.CENTER);
        layoutPanel.add(tmpP);
        layoutPanel.add(new JLabel("Compression:"));
        layoutPanel.add(checkCompression);
        tmpP = new JPanel();
        tmpP.setLayout(new BorderLayout());
        tmpP.add(new JLabel("Level:"), BorderLayout.WEST);
        tmpP.add(compressionLevel, BorderLayout.CENTER);
        layoutPanel.add(tmpP);
        layoutPanel.add(new JLabel(""));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3,1,5,10));
        infoPanel.add(typePanel);
        infoPanel.add(spacePanel);
        infoPanel.add(layoutPanel);
        contentPane.add(infoPanel, BorderLayout.CENTER);

        classChoice.addItemListener(this);
        rankChoice.addItemListener(this);
        checkCompression.addItemListener(this);
        checkContinguous.addItemListener(this);
        checkChunked.addItemListener(this);

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
            newObject = create();
            if (newObject != null)
                dispose();
        }
        if (cmd.equals("Cancel"))
        {
            newObject = null;
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

    public void itemStateChanged(ItemEvent e)
    {
        Object source = e.getSource();

        if (source.equals(classChoice))
        {
            int idx = classChoice.getSelectedIndex();
            sizeChoice.select(0);
            endianChoice.select(0);
            stringLengthField.setEnabled(false);

            if (idx == 0)
            {
                sizeChoice.setEnabled(true);
                endianChoice.setEnabled(isH5);
                checkUnsigned.setEnabled(true);

                if (sizeChoice.getItemCount() == 3)
                {
                    sizeChoice.add("16");
                    sizeChoice.add("8");
                }
            }
            else if (idx == 1)
            {
                sizeChoice.setEnabled(true);
                endianChoice.setEnabled(isH5);
                checkUnsigned.setEnabled(false);

                if (sizeChoice.getItemCount() == 5)
                {
                    sizeChoice.remove("16");
                    sizeChoice.remove("8");
                }
            }
            else if (idx == 2)
            {
                sizeChoice.setEnabled(false);
                endianChoice.setEnabled(isH5);
                checkUnsigned.setEnabled(true);
            }
            else if (idx == 3)
            {
                sizeChoice.setEnabled(false);
                endianChoice.setEnabled(false);
                checkUnsigned.setEnabled(false);
                stringLengthField.setEnabled(true);
                stringLengthField.setText("String length");
            }
            else if (idx == 4)
            {
                sizeChoice.setEnabled(false);
                endianChoice.setEnabled(false);
                checkUnsigned.setEnabled(false);
                stringLengthField.setEnabled(false);
            }
        }
        else if (source.equals(rankChoice))
        {
            int rank = rankChoice.getSelectedIndex()+1;
            String currentSizeStr = "1";
            String maxSizeStr = "0";

            for (int i=1; i<rank; i++)
            {
                currentSizeStr += " x 1";
                maxSizeStr += " x 0";
            }

            currentSizeField.setText(currentSizeStr);
            maxSizeField.setText(maxSizeStr);
            chunkSizeField.setText(currentSizeStr);
        }
        else if (source.equals(checkContinguous))
            chunkSizeField.setEnabled(false);
        else if (source.equals(checkChunked))
        {
            chunkSizeField.setEnabled(true);
        }
        else if (source.equals(checkCompression))
        {
            boolean isCompressed = checkCompression.isSelected();

            if (isCompressed)
            {
                compressionLevel.setEnabled(true);
                checkContinguous.setEnabled(false);
                checkChunked.setSelected(true);
                chunkSizeField.setEnabled(true);
            }
            else
            {
                compressionLevel.setEnabled(false);
                checkContinguous.setEnabled(true);
            }
        }
    }

    /** Creates a dialog to show the help information. */
    private void createHelpDialog()
    {
        helpDialog = new JDialog(this, "Create New Dataset");

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
        infoPane.setEditable(false);
        JScrollPane editorScrollPane = new JScrollPane(infoPane);
        contentPane.add (editorScrollPane, BorderLayout.CENTER);

        try {
            URL url= null, url2=null, url3=null;
            String rootPath = ViewProperties.getViewRoot();

            try {
                url = new URL("file:"+rootPath+"/lib/jhdfview.jar");
            } catch (java.net.MalformedURLException mfu) {;}

            try {
                url2 = new URL("file:"+rootPath+"/");
            } catch (java.net.MalformedURLException mfu) {;}

            try {
                url3 = new URL("file:"+rootPath+"/src/");
            } catch (java.net.MalformedURLException mfu) {;}

            URL uu[] = {url, url2, url3};
            URLClassLoader cl = new URLClassLoader(uu);
            URL u = cl.findResource("ncsa/hdf/view/NewDatasetHelp.html");

            infoPane.setPage(u);
            infoPane.addHyperlinkListener(this);
        } catch (Exception e) {
            infoPane.setContentType("text/html");
            StringBuffer buff = new StringBuffer();
            buff.append("<html>");
            buff.append("<body>");
            buff.append("ERROR: cannot load help information.");
            buff.append("</body>");
            buff.append("</html>");
            infoPane.setText(buff.toString());
       }

        Point l = helpDialog.getOwner().getLocation();
        l.x += 50;
        l.y += 80;
        helpDialog.setLocation(l);
        helpDialog.validate();
        helpDialog.pack();
    }

    public void hyperlinkUpdate(HyperlinkEvent e)
    {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
            JEditorPane pane = (JEditorPane) e.getSource();

            if (e instanceof HTMLFrameHyperlinkEvent)
            {
                HTMLFrameHyperlinkEvent  evt = (HTMLFrameHyperlinkEvent)e;
                HTMLDocument doc = (HTMLDocument)pane.getDocument();
                doc.processHTMLFrameHyperlinkEvent(evt);
            } else
            {
                try { pane.setPage(e.getURL()); }
                catch (Throwable t)  {}
            }
        }
    }

    private HObject create()
    {
        String name = null;
        Group pgroup = null;
        int rank=-1, gzip=-1, tclass=-1, tsize=-1, torder=-1, tsign=-1;
        long dims[], maxdims[], chunks[];

        name = nameField.getText();
        if (name == null)
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

        pgroup = (Group)groupList.get(parentChoice.getSelectedIndex());

        if (pgroup == null)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Parent group is null.",
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // set datatype class
        int idx = classChoice.getSelectedIndex();
        if (idx == 0)
        {
            tclass = Datatype.CLASS_INTEGER;
            if (checkUnsigned.isSelected())
                tsign = Datatype.SIGN_NONE;
        }
        else if (idx == 1)
            tclass = Datatype.CLASS_FLOAT;
        else if (idx == 2)
        {
            tclass = Datatype.CLASS_CHAR;
            if (checkUnsigned.isSelected())
                tsign = Datatype.SIGN_NONE;
        }
        else if (idx == 3)
        {
            tclass = Datatype.CLASS_STRING;
        }
        else if (idx == 4)
        {
            tclass = H5Datatype.CLASS_REFERENCE;
        }

        // set datatype size/order
        idx = sizeChoice.getSelectedIndex();
        if (tclass == Datatype.CLASS_STRING)
        {
            int stringLength = 0;
            try { stringLength = Integer.parseInt(stringLengthField.getText()); }
            catch (NumberFormatException ex)
            {
                stringLength = -1;
            }

            if (stringLength<=0)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    "Invalid string length: "+stringLengthField.getText(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }

            tsize = stringLength;
        }
        else if (tclass == H5Datatype.CLASS_REFERENCE)
        {
            tsize = 1;
        }
        else if (idx == 0)
        {
            tsize = Datatype.NATIVE;
        }
        else
        {
            tsize = 1 << (idx-1);
        }

        // set order
        idx = endianChoice.getSelectedIndex();
        if (idx == 0)
            torder = Datatype.NATIVE;
        else if (idx == 1)
            torder = Datatype.ORDER_LE;
        else
            torder = Datatype.ORDER_BE;

        rank = rankChoice.getSelectedIndex()+1;
        StringTokenizer st = new StringTokenizer(currentSizeField.getText(), "x");
        if (st.countTokens() < rank)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Number of values in the current dimension size is less than "+rank,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        long l = 0;
        dims = new long[rank];
        String token = null;
        for (int i=0; i<rank; i++)
        {
            token = st.nextToken().trim();
            try { l = Long.parseLong(token); }
            catch (NumberFormatException ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    "Invalid dimension size: "+currentSizeField.getText(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }

            if (l <=0)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    "Dimension size must be greater than zero.",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }

            dims[i] = l;
        }

        st = new StringTokenizer(maxSizeField.getText(), "x");
        if (st.countTokens() < rank)
        {
            toolkit.beep();
            JOptionPane.showMessageDialog(this,
                "Number of values in the max dimension size is less than "+rank,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return null;
        }

        l = 0;
        maxdims = new long[rank];
        for (int i=0; i<rank; i++)
        {
            token = st.nextToken().trim();
            try { l = Long.parseLong(token); }
            catch (NumberFormatException ex)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    "Invalid max dimension size: "+maxSizeField.getText(),
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }

            if (l < -1)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    "Dimension size cannot be less than -1.",
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }
            else if ( l == 0)
                l = dims[i];

            maxdims[i] = l;
        }

        chunks = null;
        if (checkChunked.isSelected())
        {
            st = new StringTokenizer(chunkSizeField.getText(), "x");
            if (st.countTokens() < rank)
            {
                toolkit.beep();
                JOptionPane.showMessageDialog(this,
                    "Number of values in the chunk size is less than "+rank,
                    getTitle(),
                    JOptionPane.ERROR_MESSAGE);
                return null;
            }

            l = 0;
            chunks = new long[rank];
            for (int i=0; i<rank; i++)
            {
                token = st.nextToken().trim();
                try { l = Long.parseLong(token); }
                catch (NumberFormatException ex)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this,
                        "Invalid chunk dimension size: "+chunkSizeField.getText(),
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                if (l < 1)
                {
                    toolkit.beep();
                    JOptionPane.showMessageDialog(this,
                        "Dimension size cannot be less than 1.",
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                chunks[i] = l;
            }
        }

        if (checkCompression.isSelected())
            gzip = compressionLevel.getSelectedIndex();
        else
            gzip = 0;

        HObject obj = null;
        try
        {
            if (isH5)
            {
                Datatype datatype = new H5Datatype(tclass, tsize, torder, tsign);
                obj = H5ScalarDS.create(fileFormat, name, pgroup, datatype,
                    dims, maxdims, chunks, gzip);
            }
            else
            {
                Datatype datatype = new H4Datatype(tclass, tsize, torder, tsign);
                obj = H4SDS.create(fileFormat, name, pgroup, datatype,
                    dims, maxdims, chunks, gzip);
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

        return obj;
    }

    /** Returns the new dataset created. */
    public DataFormat getObject() { return newObject; }

}
