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

package test.ncsa.hdf.view;

import ncsa.hdf.view.*;
import ncsa.hdf.object.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.Component;
import java.awt.event.*;
import java.awt.AWTEvent;
import java.lang.Boolean;
import javax.swing.table.*;
import javax.swing.event.*;
import java.util.EventObject;
import javax.swing.tree.*;
import java.io.Serializable;
import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.Dimension;

/**
 * TextView displays an HDF string dataset in text.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public class DefaultTextViewTest extends JInternalFrame
implements TextView
{
    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

    /**
     * The Scalar Dataset.
     */
    private final ScalarDS dataset;

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

    /**
     * Constructs an TextView.
     * <p>
     * @param theView the main HDFView.
     */
    public DefaultTextViewTest(ViewManager theView)
    {
        viewer = theView;
        text = null;
        textAreas = null;

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
        String ftype = ViewProperties.getFontType();
        int fsize = ViewProperties.getFontSize();
        Font font = null;
        try { font = new Font(ftype, Font.PLAIN, fsize); }
        catch (Exception ex) {font = null; }
        for (int i=0; i<size; i++)
        {
            textAreas[i] = new JTextArea(text[i]);
            textAreas[i].setEditable(!isReadOnly);
            textAreas[i].setWrapStyleWord(true);
            textAreas[i].setBorder(txtBorder);
            txtPane.add(textAreas[i]);
            if (font != null) textAreas[i].setFont(font);
        }

        ((JPanel)getContentPane()).add (new JScrollPane(txtPane));
    }

    /** update dataset value in file.
     *  The change will go to file.
     */
    private void updateValueInFile()
    {
        if (isReadOnly) return;

        if (!(dataset instanceof ScalarDS))
            return;

        boolean isValueChanged = false;
        String txt = null;
        for (int i=0; i<text.length; i++)
        {
            txt = textAreas[i].getText();
            if (!text[i].equals(txt)) {
                isValueChanged = true;
                text[i] = txt;
            }
        }

        if (isValueChanged) {
            try { dataset.write(); }  catch (Exception ex) {}

            // refresh text in memory. After writing, text is cut off to its max string length
            for (int i=0; i<text.length; i++)
                textAreas[i].setText(text[i]);
        }

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
}
