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
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.Component;
import java.awt.image.BufferedImage;

/**
 * TextView displays an HDF string dataset in text.
 * <p>
 * @version 1.3.0 1/26/2002
 * @author Peter X. Cao
 */
public class DefaultTextView extends JInternalFrame
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

    /** the table to hold the text content */
    private JTable table;

    private boolean isReadOnly = false;

    /**
     * Constructs an TextView.
     * <p>
     * @param theView the main HDFView.
     */
    public DefaultTextView(ViewManager theView)
    {
        super("TextView", true, true, true, true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        setFrameIcon(ViewProperties.getTextIcon());

        viewer = theView;
        text = null;

        HObject obj = viewer.getTreeView().getCurrentObject();
        if (!(obj instanceof ScalarDS)) {
            viewer.showStatus("Data object is not a scalar dataset.");
            return;
        }

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
        setTitle("TextView - "+fname+" - " +dataset.getPath()+dataset.getName());

        // setup 1D table to display the text content
        table = new JTable(text.length, 1);
        table.setRowHeight(80);

        for (int i=0; i<text.length; i++)
            table.setValueAt(text[i], i, 0);

        TableColumn tableColumn = table.getColumnModel().getColumn(0);
        tableColumn.setCellRenderer(new ScrollableTextAreaRenderer(table,new JTextArea()));
        tableColumn.setCellEditor(new ScrollableTextAreaEditor(table,new JTextArea()));
        table.setTableHeader(null);

        ((JPanel)getContentPane()).add (new JScrollPane(table));
    }

    private void setFrameIcon() {
        int w=16, h=16;
        ImageIcon icon = null;
        BufferedImage image = new java.awt.image.BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int i=0; i<h; i++) {
            for (int j=i>>1; j<w-(i>>1); j++) {
                image.setRGB(j, i, 0xff0000);
            }
        }

        int offset = 0;
        for (int i=0; i<h; i++) {
            offset = i/2+i;
            for (int j=offset; j<w-offset; j++) {
                image.setRGB(j, i, 0);
            }
        }

        icon = new ImageIcon(image);
        setFrameIcon(icon);
    }

    /** update dataset value in file.
     *  The change will go to file.
     */
    private void updateValueInFile()
    {
        if (isReadOnly) return;

        if (!(dataset instanceof ScalarDS))
            return;

        String txt = null;
        boolean isTextChanged = false;
        for (int i=0; i<text.length; i++) {
            txt = table.getValueAt(i, 0).toString();
            if (!text[i].equals(txt)) {
                isTextChanged = true;
                text[i] = txt;
            }
        }

        if (isTextChanged) {
            try { dataset.write(); }  catch (Exception ex) {}
             for (int i=0; i<text.length; i++)
                table.setValueAt(text[i], i, 0);
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

    private class ScrollableTextAreaEditor extends AbstractCellEditor
    implements TableCellEditor {
        private JScrollPane spScroll;
        private JTextArea textArea;

        public ScrollableTextAreaEditor(JTable table, JTextArea txtArea) {
            textArea = txtArea;
            spScroll = new JScrollPane(textArea);
            spScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(true);
            textArea.setEditable(true);
        }

        public Object getCellEditorValue() {
            return ((JTextArea)textArea).getText();
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
            textArea.setText((value == null) ? "" : value.toString());
            return spScroll;
        }
    }

    private class ScrollableTextAreaRenderer extends JScrollPane
        implements TableCellRenderer {
        private JTextArea textArea;

        public ScrollableTextAreaRenderer(JTable table,JTextArea txtArea) {
            super(txtArea);
            textArea = txtArea;
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            textArea.setText((value == null) ? "" : value.toString());
            try { textArea.setCaretPosition(1);
            } catch(Exception expGen) { }

            if (hasFocus) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(new EmptyBorder(1, 1, 1, 1));
            }
            return this;
        }
    }

}
