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
import javax.swing.text.*;
import javax.swing.border.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * TextView displays an HDF string dataset in text.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public class TextView extends JInternalFrame
implements TextObserver
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

    /**
     * Constructs an TextView.
     * <p>
     * @param theView the main HDFView.
     */
    public TextView(ViewManager theView)
    {
        super();

        viewer = theView;
        text = null;
        textAreas = null;

        dataset = (ScalarDS)viewer.getSelectedObject();

        if (!dataset.isText())
        {
            viewer.showStatus("Cnnot display non-text dataset in text view.");
            return;
        }

        try {
            text = (String[])dataset.getData();
        } catch (Exception ex)
        {
            viewer.showStatus(ex.toString());
            text = null;
        }

        if (text == null)
        {
            viewer.showStatus("Loading text dataset failed - "+dataset.getName());
            return;
        }

        String fname = new java.io.File(dataset.getFile()).getName();
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setTitle("TextView - "+fname+" - " +dataset.getPath()+dataset.getName());
        this.setFrameIcon(ViewProperties.getTextIcon());

/*
        //set text styles
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Style def = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = styleContext.addStyle("regular", def);
        Style underline = styleContext.addStyle("underline", regular);
        StyleConstants.setUnderline(underline, true);
        Style[] styles = {regular, underline};

        // set contect document
        StringContent strContent = new StringContent();
        DefaultStyledDocument doc = new DefaultStyledDocument(strContent, styleContext);
        textPane = new JTextPane(doc);

        //Load the text pane with styled text.
        try
        {
            for (int i=0; i < text.length; i++)
            {
                doc.insertString(doc.getLength(), text[i]+"\n\n", styles[i&0x1]);
            }
        } catch (BadLocationException ex) {
            viewer.showStatus(ex.toString());
        }
        JScrollPane scroller = new JScrollPane(textPane);
        scroller.getVerticalScrollBar().setUnitIncrement(50);
        scroller.getHorizontalScrollBar().setUnitIncrement(50);

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add (scroller, BorderLayout.CENTER);
*/

        int size = text.length;
        textAreas = new JTextArea[size];
        JPanel txtPane = new JPanel();
        txtPane.setLayout(new GridLayout(size, 1));
        for (int i=0; i<size; i++)
        {
            textAreas[i] = new JTextArea(text[i]);
            textAreas[i].setWrapStyleWord(true);
            textAreas[i].setBorder(new LineBorder(java.awt.Color.black));
            txtPane.add(textAreas[i]);
        }

        JScrollPane scroller = new JScrollPane(txtPane);
        scroller.getVerticalScrollBar().setUnitIncrement(50);
        scroller.getHorizontalScrollBar().setUnitIncrement(50);

        JPanel contentPane = (JPanel)getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add (scroller, BorderLayout.CENTER);
    }

    /** update dataset value in file.
     *  The change will go to file.
     */
    private void updateValueInFile()
    {
        if (!(dataset instanceof ScalarDS))
            return;

        for (int i=0; i<text.length; i++)
            text[i] = textAreas[i].getText();

        try { dataset.write(); }
        catch (Exception ex) {}

    }

    public void dispose()
    {
        updateValueInFile();

        super.dispose();
        viewer.contentFrameWasRemoved(getName());
    }

    // Implementing DataObserver.
    public Object getDataObject()
    {
        return dataset;
    }

    // Implementing TextObserver.
    public String[] getText()
    {
        return text;
    }

    // Implementing DataObserver.
    public void previousPage() {;}

    // Implementing DataObserver.
    public void nextPage() {;}

    // Implementing DataObserver.
    public void firstPage() {;}

    // Implementing DataObserver.
    public void lastPage() {;}
}
