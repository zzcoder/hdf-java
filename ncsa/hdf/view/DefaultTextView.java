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

import ncsa.hdf.view.*;
import ncsa.hdf.object.*;
import javax.swing.*;
import javax.print.*;
import javax.print.attribute.*;
import java.util.*;
import java.io.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Font;

/**
 * TextView displays an HDF string dataset in text.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public class DefaultTextView extends JInternalFrame
implements TextView, ActionListener, KeyListener
{
    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

    /**
     * The Scalar Dataset.
     */
    private ScalarDS dataset;

    /**
     * GUI component: the text area used to dispaly the text content.
     */
    private JTextPane textPane;

    /**
     * The string text.
     */
    private String[] text;

    /**
     * text areas to hold the text.
     */
    private JTextArea[] textAreas;

    /** the table to hold the text content */
    private JTable table;

    private boolean isReadOnly = false;

    private boolean isTextChanged = false;

    /**
     * Constructs an TextView.
     * <p>
     * @param theView the main HDFView.
     */
    public DefaultTextView(ViewManager theView)
    {
        viewer = theView;
        text = null;
        textAreas = null;
        dataset = null;

        HObject obj = viewer.getTreeView().getCurrentObject();
        if (!(obj instanceof ScalarDS))
            return;

        dataset = (ScalarDS)obj;

        if (!dataset.isText()) {
            viewer.showStatus("Cannot display non-text dataset in text view.");
            return;
        }

        isReadOnly = dataset.getFileFormat().isReadOnly();

        try {
            text = (String[])dataset.getData();
        } catch (Exception ex) {
            viewer.showStatus(ex.toString());
            text = null;
        }

        if (text == null) {
            viewer.showStatus("Loading text dataset failed - "+dataset.getName());
            return;
        }

        String fname = new java.io.File(dataset.getFile()).getName();
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setTitle("TextView - "+fname+" - " +dataset.getPath()+dataset.getName());
        this.setFrameIcon(ViewProperties.getTextIcon());

        int size = text.length;
        textAreas = new JTextArea[size];
        JPanel txtPane = new JPanel();
        txtPane.setLayout(new GridLayout(size, 1));

        Border txtBorder = new MatteBorder(0, 0, 1, 0, java.awt.Color.BLUE);
        for (int i=0; i<size; i++)
        {
            textAreas[i] = new JTextArea(text[i]);
            textAreas[i].setEditable(!isReadOnly);
            textAreas[i].setWrapStyleWord(true);
            if (!isReadOnly)
                textAreas[i].addKeyListener(this);
            textAreas[i].setBorder(txtBorder);
            txtPane.add(textAreas[i]);
        }

        ((JPanel)getContentPane()).add (new JScrollPane(txtPane));

        setJMenuBar(createMenuBar());
    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();
        String cmd = e.getActionCommand();

        if (cmd.equals("Close")) {
            dispose();  // terminate the application
        } else if (cmd.startsWith("Font size")) {
            int fsize = Integer.parseInt(cmd.substring(10));
            Font font = textAreas[0].getFont();
            if (font != null) {
                font = new Font(font.getName(), font.getStyle(), fsize);
                for (int i=0; i<textAreas.length; i++)
                    textAreas[i].setFont(font);
            }
        } else if (cmd.equals("Save to text file")) {
            try { saveAsText(); }
            catch (Exception ex) {
                JOptionPane.showMessageDialog((JFrame)viewer,
                        ex,
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (cmd.equals("Save changes")) {
            updateValueInFile();
        }
        else if (cmd.equals("Print")) {
            print();
        }
    }

    public void keyPressed(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e)
    {
        isTextChanged = true;
    }

    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JButton button;
        boolean isEditable = !dataset.getFileFormat().isReadOnly();

        JMenu menu = new JMenu("Text", false);
        menu.setMnemonic('T');
        bar.add(menu);

        JMenuItem item = new JMenuItem( "Save To Text File");
        //item.setMnemonic(KeyEvent.VK_T);
        item.addActionListener(this);
        item.setActionCommand("Save to text file");
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem( "Save Changes");
        item.addActionListener(this);
        item.setActionCommand("Save changes");
        menu.add(item);

        menu.addSeparator();

        JMenu subMenu = new JMenu( "Font Size");
        for (int i=0; i<6; i++) {
            int fsize = 10+2*i;
            item = new JMenuItem( String.valueOf(fsize));
            item.addActionListener(this);
            item.setActionCommand("Font size "+fsize);
            subMenu.add(item);
        }
        menu.add(subMenu);

/*
        menu.addSeparator();

        item = new JMenuItem( "Print");
        item.addActionListener(this);
        item.setActionCommand("Print");
        menu.add(item);
*/
        menu.addSeparator();

        item = new JMenuItem( "Close");
        item.addActionListener(this);
        item.setActionCommand("Close");
        menu.add(item);

        return bar;
    }

    /** update dataset value in file.
     *  The change will go to file.
     */
    private void updateValueInFile()
    {
        if (isReadOnly) return;

        if (!(dataset instanceof ScalarDS))
            return;

        if (!isTextChanged)
            return;

        int op = JOptionPane.showConfirmDialog(this,
            "\""+ dataset.getName() +"\" has changed.\n"+
            "you want to save the changes?",
            getTitle(),
            JOptionPane.YES_NO_OPTION);

        if (op == JOptionPane.NO_OPTION)
            return;

        for (int i=0; i<text.length; i++)
            text[i] = textAreas[i].getText();

        try { dataset.write(); }
        catch (Exception ex)
        {
             JOptionPane.showMessageDialog(
                this,
                ex,
                getTitle(),
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        isTextChanged = false;

        // refresh text in memory. After writing, text is cut off to its max string length
        for (int i=0; i<text.length; i++)
            textAreas[i].setText(text[i]);
    }

    /** Save data as text. */
    private void saveAsText() throws Exception
    {
        final JFileChooser fchooser = new JFileChooser(dataset.getFile());
        fchooser.setFileFilter(DefaultFileFilter.getFileFilterText());
        fchooser.changeToParentDirectory();
        fchooser.setDialogTitle("Save Current Data To Text File --- "+dataset.getName());

        File choosedFile = new File(dataset.getName()+".txt");;
        fchooser.setSelectedFile(choosedFile);
        int returnVal = fchooser.showSaveDialog(this);

        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;

        choosedFile = fchooser.getSelectedFile();
        if (choosedFile == null)
            return;

        String fname = choosedFile.getAbsolutePath();

        // check if the file is in use
        List fileList = viewer.getTreeView().getCurrentFiles();
        if (fileList != null)
        {
            FileFormat theFile = null;
            Iterator iterator = fileList.iterator();
            while(iterator.hasNext())
            {
                theFile = (FileFormat)iterator.next();
                if (theFile.getFilePath().equals(fname))
                {
                     JOptionPane.showMessageDialog(this,
                        "Unable to save data to file \""+fname+"\". \nThe file is being used.",
                        getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (choosedFile.exists())
        {
            int newFileFlag = JOptionPane.showConfirmDialog(this,
                "File exists. Do you want to replace it ?",
                this.getTitle(),
                JOptionPane.YES_NO_OPTION);
            if (newFileFlag == JOptionPane.NO_OPTION)
                return;
        }

        PrintWriter out = new PrintWriter(
            new BufferedWriter(new FileWriter(choosedFile)));

        int rows = text.length;
        for (int i=0; i<rows; i++)
        {
            out.print(textAreas[i].getText().trim());
            out.println();
            out.println();
        }

        out.flush();
        out.close();

        viewer.showStatus("Data save to: "+fname);

        try {
            RandomAccessFile rf = new RandomAccessFile(choosedFile, "r");
            long size = rf.length();
            rf.close();
            viewer.showStatus("File size (bytes): "+size);
        } catch (Exception ex) {}
    }

    public void dispose()
    {
        updateValueInFile();
        viewer.removeDataView(this);

        super.dispose();
    }

    // Implementing DataView.
    public HObject getDataObject() {
        return dataset;
    }

    // Implementing TextView.
    public String[] getText()  {
        return text;
    }

    // print the image
    private void print() {
StreamPrintServiceFactory[] spsf = StreamPrintServiceFactory.lookupStreamPrintServiceFactories(null, null);
for (int i = 0; i<spsf.length; i++)
    System.out.println(spsf[i]);
DocFlavor[] docFlavors = spsf[0].getSupportedDocFlavors();
for (int i = 0; i<docFlavors.length; i++)
    System.out.println(docFlavors[i]);

        // Get a text DocFlavor
        InputStream is = null;
        try { is = new BufferedInputStream( new java.io.FileInputStream("e:\\temp\\t.html"));}
        catch (Exception ex) {}
        DocFlavor flavor = DocFlavor.STRING.TEXT_HTML;

        // Get all available print services
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        // Print this job on the first print server
        DocPrintJob job = services[0].createPrintJob();
        Doc doc = new SimpleDoc(is, flavor, null);

        // Print it
        try { job.print(doc, null); }
        catch (Exception ex) {System.out.println(ex);}
/*
        //DocFlavor flavor = DocFlavor.BYTE_ARRAY.STRING.TEXT_PLAIN;//.STRING.TEXT_PLAIN;
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.JPEG;//.STRING.TEXT_PLAIN;
System.out.println(flavor);
        PrintRequestAttributeSet printAttr = new HashPrintRequestAttributeSet();
        PrintService[] services = PrintServiceLookup.lookupPrintServices( flavor, printAttr);
System.out.println(services);
        PrintService service =  ServiceUI.printDialog(null, 50, 50,services, services[0], flavor, printAttr);
        if (service != null) {
            HashDocAttributeSet docAttr = new HashDocAttributeSet();
            SimpleDoc doc = new SimpleDoc("this is a test of Java print", flavor, docAttr);
            try { service.createPrintJob().print(doc, printAttr); }
            catch (Exception ex) {}
        }

*/
    }

}
