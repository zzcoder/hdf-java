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
import java.awt.BorderLayout;

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
     * Constructs an TextView.
     * <p>
     * @param theView the main HDFView.
     */
    public TextView(ViewManager theView)
    {
        super();

        viewer = theView;
        text = null;

        dataset = (ScalarDS)viewer.getSelectedObject();

        if (!dataset.isText())
        {
            viewer.showStatus("Cnnot display non-text dataset in text view.");
            return;
        }

        try {
            text = (String[])dataset.read();
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

        textPane = new JTextPane();
        textPane.setEditable(false);
        Document doc = textPane.getDocument();
        String[] styles = {"regular", "italic"};

        //Add "regular" and "italic" styles into the textPane.
        Style def = StyleContext.getDefaultStyleContext().
            getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = textPane.addStyle("regular", def);
        Style s = textPane.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        //Load the text pane with styled text.
        try
        {
            for (int i=0; i < text.length; i++)
            {
                doc.insertString(doc.getLength(), text[i],
                    textPane.getStyle(styles[i&0x1]));
                doc.insertString(doc.getLength(), "\n\n",
                    textPane.getStyle(styles[0]));
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
    }

    public void dispose()
    {
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
