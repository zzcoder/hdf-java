/*****************************************************************************
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of the HDF Java Products distribution.                  *
 * The full copyright notice, including terms governing use, modification,   *
 * and redistribution, is contained in the files COPYING and Copyright.html. *
 * COPYING can be found at the root of the source code distribution tree.    *
 * Or, see http://hdfgroup.org/products/hdf-java/doc/Copyright.html.         *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 ****************************************************************************/

package ncsa.hdf.view;

import ncsa.hdf.object.*;

import javax.swing.*;

import java.util.*;
import java.io.*;

import java.awt.event.*;
import java.awt.print.PrinterException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.JEditorPane;

/**
 * StringView displays an HDF dataset as a list of strings
 * 
 * @author Joseph P. DiGiovanna	
 * @date 5/20/10
 */
public class DefaultStringView extends JInternalFrame implements StringView,
        ActionListener, KeyListener {
    public static final long serialVersionUID = HObject.serialVersionUID;

    /**
     * The main HDFView.
     */
    private final ViewManager viewer;

    /**
     * The Scalar Dataset.
     */
    private ScalarDS dataset;
    
    private boolean isIntegerOrChar = false;
    private boolean isString = false;
    private boolean isMultiLinePerRowString = false;
    
    private boolean is0D = false;
    private boolean is1D = false;
    private boolean is2D = false;
    private boolean is3D = false;
    private boolean isMoreThan3D = false;
    
    private byte[][] charCacheGrid;
    private byte[] charCacheStream;
    private byte[] charSaveCacheStream;
    private byte[][] charSaveCacheGrid;
    
    private byte[][] stringCacheGrid;
    private byte[] stringCacheStream;
    private byte[] stringSaveCacheStream;
    private byte[][] stringSaveCacheGrid;
    
    private int maxVisibleLines = 40;
    private int gridWidth = 0;
    private int gridHeight = 0;
    private long totalWidth = 0;
    private long totalHeight = 0;
    private long currentPosition = 0;
    private long currentPage = 0;
    
    
    /*
     * two byte array's, one to read in the dataset 
     * the other stores it in chart form, makes it easier to access.
     */
    private byte[][] cacheGrid;
    private byte[] cacheStream;
    private byte[] saveCacheStream;
    private byte[][] saveCacheGrid;
    
    /*
     * Several pieces of information regarding our caches current state
     * Meant to make it easier to move between subroutines while maintaining efficiency
     */
    private long cacheStart = 0;
    private long cacheEnd = 100;
    private long cacheLength = 100;
    
    /*
     * Several bits of data about the way our dataset it set up
     * lets us keep track of how we need to treat it, and lets
     * us handle special occurences
     */
    private boolean needToUpdateCache = false;
    private boolean is2DDataset = false;
    private boolean is1DDataset = false;
    private boolean hasNoDimensions = false;
    private boolean hasHigherRank = false;
    private long totalRank = 0;
    private JTextField frameField;
    private long curFrame=0, maxFrame=1;

    /*
     * Keep track of the measurements of our dataset
     */
    private long totalRows;
    private long totalColumns;
    private long curRows;
    private long curColumns;
    
    /*
     * These values store and handle changes made to the dataset that have
     * yet to be applied, so that when simply scrolling around, you will always see 
     * the latest changes that have been made, none of the previous ones.  This is
     * also used to handle when the user selects to save the dataset to file.
     */
    private long rowChangeCount;
    private long[] rowNumbers;
    private byte[][] rowValues;
    
    /*
     * Used to store the original values for the dataset
     */
    int orank = 0;
    long[] odims = null;
	long[] oselected = null;
	long[] ostart = null;
	long[] ostride = null;
	int[] oselectedIndex = null;   
    
	/*
	 * Used to update the values on the currently selected dataset
	 */
	int rank = 0;
	long[] dims = null;
	long[] selected = null;
	long[] start = null;
	long[] stride = null;
	int[] selectedIndex = null;
    
	/*
	 * Keep track of information regarding the dataset
	 * and the panes that is it stored in so that we can
	 * resize them as necessary to best suit the quantity of
	 * data that we're attempting to display
	 */
    private byte[] displayText;
    private byte[][] displayGrid;
    private int numRows, numCols;
    private int paneWidth = 45;
    private int stringViewWidth = 350;
    private boolean noDblUpdate = false;
    
    /*
     * keep track of the current line we're on so we 
     * know how to use caching properly
     * also keep a display limit which is the number
     * of lines we will render at any given time.
     */
    private long currentLine;
    
    /*
     * These handle our GUI layout, which was 
     * originally created and imported via netbeans.
     */
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    
    
    /*
     * where we will display our text 
     */
    private JEditorPane pane;
    
    /*
     * Makes it easier to keep track of when we cannot edit a file
     * Also saves us time when attempting to save a file.
     */
    private boolean isReadOnly = false;

    /*
     * stores whether we've made any changes since the file was
     * opened in the visualizer or since the most recent time saved
     * if it has been saved at some point.
     */
    private boolean isStringChanged = false;

    /**
     * Constructs a StringView
     * <p>
     * 
     * @param theView
     *            the main HDFView.
     */
    public DefaultStringView(ViewManager theView) {
        this(theView, null);
    }
    
    /**
     * Constructs a StringView.
     * <p>
     * 
     * @param theView
     *            the main HDFView.
     * @param map
     *            the properties on how to show the data. The map is used to
     *            allow applications to pass properties on how to display the
     *            data, such as transposing data, showing data as character,
     *            applying bitmask, and etc. Predefined keys are listed at
     *            ViewProperties.DATA_VIEW_KEY.
     */
	@SuppressWarnings("unchecked")
	public DefaultStringView(ViewManager theView, HashMap map) {
        viewer = theView;
        displayText = null;
        dataset = null;

        //check to see if we have a hashmap
        HObject hobject = null;
        if (map != null)
            hobject = (HObject) map.get(ViewProperties.DATA_VIEW_KEY.OBJECT);
        else
            hobject = theView.getTreeView().getCurrentObject();

        if (!(hobject instanceof ScalarDS)) {
            return;
        }

        //our dataset to display
        dataset = (ScalarDS) hobject;
	
		rank = dataset.getRank(); // number of dimension of the dataset
		dims = dataset.getDims(); // the dimension sizes of the dataset
		selected = dataset.getSelectedDims(); // the selected size of the dataet
		start = dataset.getStartDims(); // the off set of the selection
		stride = dataset.getStride(); // the stride of the dataset
		selectedIndex = dataset.getSelectedIndex(); // the selected dimensions for display
		
		orank = dataset.getRank(); // number of dimension of the dataset
		odims = dataset.getDims(); // the dimension sizes of the dataset
		oselected = dataset.getSelectedDims(); // the selected size of the dataet
		ostart = dataset.getStartDims(); // the off set of the selection
		ostride = dataset.getStride(); // the stride of the dataset
		oselectedIndex = dataset.getSelectedIndex(); // the selected dimensions for display
		
		// We know we'll be starting at 0 when we initially load the file up
	    start[0] = 0;
		 
		// use our predefined cachelimit value for display lines.
	    if (dataset.getRank() == 1){
	    	selected[0] = cacheLength * 80;
	    } else if (dataset.getRank() == 2) {
	    	selected[0] = cacheLength;
	    } else if (dataset.getRank() == 3) {
	    	selected[0] = cacheLength;
	    }

		/*
		 * Apply these settings to the actual dataset so that when
		 * we make an attempt to read it, we'll actually be reading a 
		 * hyper-slab instead of the full dataset.  This will speed up 
		 * things tremendously when we read a file of any significant size
		 * and also allow us to handle extremely large files without the 
		 * risk of having errors due to variables not being able to handle
		 * values the size of which we are given.
		 */
		try {
			dataset.getData();
		} catch (OutOfMemoryError e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/*
		 * read the dataset in using the changes to its
		 * settings we just made, so we know that 
		 * we are selecting a hyper-slab subset.
		 */
		try {
			cacheStream = dataset.readBytes();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//give pre-set values incase we have a 1D dataset.
		curColumns = 1;
        totalColumns = 1;
        
        //Get the number of dimensions of our dataset.
        totalRank = dataset.getRank();
        
        /*
         * This statement handles when we have 0, 1, 2 or
         * more dimensions in our dataset.  0 forces all values
         * to 1, and won't display anything, 1 sets everything 
         * up to handle a 1D dataset and allows for special layouts.
         * 2 is handled normally, as we would any grid. 3 or more is 
         * taken as though it were 2, and only those dimensions are used,
         * the rest are just ignored for the time being.
         */
        if (totalRank == 1){
        	totalRows = (int) (dataset.getDims()[0] / 80 + 1);
        	curRows = dataset.getSelectedDims()[0];
        	totalColumns = 80;
        	curColumns = 80;
        	is1DDataset = true;
        	is2DDataset = false;
        	hasHigherRank = false;
        	hasNoDimensions = false;
        } else if (totalRank == 2) {
        	totalRows = dataset.getDims()[0];
        	totalColumns = dataset.getDims()[1];
        	curRows = dataset.getSelectedDims()[0];
        	curColumns = dataset.getSelectedDims()[1];
        	is1DDataset = false;
        	is2DDataset = true;
        	hasHigherRank = false;
        	hasNoDimensions = false;
        } else if (totalRank == 0){
        	hasNoDimensions = true;
        	is1DDataset = false;
        	is2DDataset = false;
        	hasHigherRank = false;
        	totalRows = 1;
        	totalColumns = 1;
        	curRows = 1;
        	curColumns = 1;
        } else if (totalRank == 3){
        	totalRows = dataset.getDims()[0];
        	totalColumns = dataset.getDims()[1];
        	curRows = dataset.getSelectedDims()[0];
        	curColumns = dataset.getSelectedDims()[1];
        	is1DDataset = false;
        	is2DDataset = true;
        	hasHigherRank = true;
        	is3D = true;
        	hasNoDimensions = false;
        } else {
        	totalRows = dataset.getDims()[0];
        	totalColumns = dataset.getDims()[1];
        	curRows = dataset.getSelectedDims()[0];
        	curColumns = dataset.getSelectedDims()[1];
        	is1DDataset = false;
        	is2DDataset = true;
        	hasHigherRank = true;
        	hasNoDimensions = false;
        }
        
        //for storing our data 
        cacheGrid = new byte[(int) curRows][(int) curColumns];
  
        //for handling changes made, current limit is 5000 without saving.
        //The values stored in these reset when a save is made, since now
        //everything in the file we're pulling from will be up to date.
        rowNumbers = new long[5000];
        rowValues = new byte[5000][(int) curColumns];
		
		/*
		 * if we can't read it, display the status and exit
		 */
		if (cacheStream == null){
			viewer.showStatus("Error loading dataset - " + dataset.getName());
			dataset = null;
			return;
		}
		
		/*
		 * Feed it into a 2D grid, making it much simpler to edit and save everything as we go
		 */
		int tRows = 0;
		if(is1DDataset){tRows = (int)(curRows / 80);}else{tRows = (int) curRows;}
		
        for (int i = 0; i < tRows; i++) {
        	for (int j = 0; j < curColumns; j++) {
        		try {
        			cacheGrid[i][j] = cacheStream[(int) (curColumns * i + j)];
        		} catch (Exception e) {
        			System.out.println(i);
        			System.out.println(j);
        		}
        	}
        }
        
        /*
         * Set up our outer window.
         */
        String fname = new java.io.File(dataset.getFile()).getName();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("StringView  -  " + dataset.getName() + "  -  "
                + dataset.getPath() + "  -  " + fname);
        this.setFrameIcon(ViewProperties.getTextIcon());
        
        initComponents();
       
    }

	public void initComponents() {

		 /*
         * Set up our viewing pane, where we will be writing text to.
         * Using monospaced font to make checking alignment easier
         */
        pane = new JEditorPane();
        pane.setBackground(Color.WHITE);
        pane.setFont(new java.awt.Font("monospaced", 0, 14));
        
        /*
         * Initialize all of our GUI variables.
         */
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
       
        /*
         * set up our new JEditorPane
         */
        new javax.swing.JEditorPane();

        /*
         * Handle when we click the up button, moving ourselves up one row
         */
        /*jButton1.setText("Up");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });*/

        /*
         * Handle when we click the down button, moving ourselves down one row.
         */
        /*jButton2.setText("Down");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
					jButton2ActionPerformed(evt);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
            }
        });*/
        
        /* 
         * Handle clicking the page up button, moving us up 20 rows.
         */
        jButton3.setText("Up");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        
        /*
         * Handle clicking the page down button, moving us down 20 rows.
         */
        jButton4.setText("Down");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        
        /*
         * Handle when we use the goto line button
         */
        /*jButton5.setText("Goto Line");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });*/
        
        /*
         * Handle whenever we make a direct change to the scroll bar
         */
        jScrollBar1.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar1AdjustmentValueChanged(evt);
            }
        });

        /*
         * set up our text field associated with the goto line button
         */
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("0");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jButton5ActionPerformed(evt);
            }
        });

        /*
         * set up our main scroll pane, the one in which alignment data will actually be visualized
         */
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setViewportView(pane);
        
        //jScrollPane1.setBounds(100, 100, 500, 500);
        
        
        /*
         * set up our secondary scroll pane, which will hold row numbers associated with our data.
         */
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane2.setAlignmentX(RIGHT_ALIGNMENT);
        jScrollPane2.setViewportView(jTextPane1);
        
        //jScrollPane2.setBounds(0, 0, 125, 125);
        
        /*
         * Generate a monospaced font to apply to all of our text boxes.
         * This will allow us to have equal spacing for all characters, allowing us 
         * to have a very accurate alignment.  We also set it up in a way that it will
         * be non-platform specific.  So regardless of what system we're running, it will
         * be able to find an appropriate mono-spaced font.
         */
        SimpleAttributeSet attribs = new SimpleAttributeSet();  
        StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_RIGHT);  
        StyleConstants.setFontSize(attribs, 14);
        StyleConstants.setFontFamily(attribs, "monospaced");
        jTextPane1.setParagraphAttributes(attribs, true);
        
        /*
         * Set up our top-most scroll pane, which will handle column numbers so it is easier for the user
         * to see where they are currently looking.
         */
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setViewportView(jTextPane2);
        
        jTextPane2.setFont(new java.awt.Font("monospaced", 0, 14));
        
        /*
         * Check the number of rows in our dataset so that we can find 
         * appropriate spacing.  That way we don't have to worry about word
         * wrap making our line numbers look abnormal or be incorrect
         */
        if (totalRows < 10000) {paneWidth = 45;}
        else if (totalRows >= 10000 && totalRows < 1000000) {paneWidth = 65;}
        else if (totalRows >= 1000000 && totalRows < 100000000) {paneWidth = 85;}
        else {paneWidth = 100;}
        
        /*
         * Also handle the width of the main viewing pane so that we can accurately
         * visualize data of any width.  The top-most pane is linked to the main
         * viewing pane, so it follows suite regardless and we need not set it.
         */
        if (totalColumns <= 10) {stringViewWidth = 110;}
        else if (totalColumns > 10 && totalColumns <= 20) {stringViewWidth = 210;}
        else if (totalColumns > 20 && totalColumns <= 30) {stringViewWidth = 310;}
        else if (totalColumns > 30 && totalColumns <= 40) {stringViewWidth = 410;}
        else if (totalColumns > 40 && totalColumns <= 50) {stringViewWidth = 510;}
        else {stringViewWidth = 600;}
        
        /*
         * 
         * This next block of code manually sets up the GUI for the string view visualizer
         * It was generated using Netbeans, and imported directly.
         * 
         * It also uses the variables pane with and takes that into account when resizing.
         *
         */
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, paneWidth, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, stringViewWidth, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, stringViewWidth, Short.MAX_VALUE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE))))
                .addContainerGap())
        );
        

        /*
         * Generate our menu bar to give user options for
         * saving, exporting to file, and printing.
         */
        setJMenuBar(createMenuBar());
	    
        /*
         * handle output for all 3 of our text panes.
         */
        String testLine = ""; String output = ""; String output2 = ""; String output3 = "";
		
        //get the topmost output by calling a separate subroutine.
        output3 = getOutput3();
        
        //initialize the current line we're sitting on.
        currentLine = 0;
        
        /*
         * Store all text in a string, formatting each line
         * We do this so that it will be simple for the user
         * to scroll down, checking for problems with alignments.
         * 
         */
        int curLim = (int) totalRows;
        if (curLim > cacheLength + currentLine) { curLim = (int) (cacheLength + currentLine); }
        
        for (int p = 0; p < curLim; p++){
        	
        	//attempt to create a string using each line of the cacheGrid
        	//along with the appropriate monospaced format.
			try {
				testLine = new String(cacheGrid[p], "Cp1252" /* encoding */);
			} catch (UnsupportedEncodingException e) {
				viewer.showStatus(e.toString());
			}
			//store with an end line char in output for main visualizing pane.
			output = output + testLine + "\n";
			
			//store line number in secondary pane along with end line char.
			output2 = output2 + p + "\n";
        }
       
        /*
         * Associate the third pane with the appropriate text pane
         */
        jScrollPane3.setViewportView(jTextPane2);
        ((JPanel) getContentPane()).add(jScrollPane3);
        
        /*
         * Apply non-editable settings to it and feed it the output3 string
         */
        jTextPane2.setEditable(false);
        jTextPane2.setText(output3);
        jTextPane2.setCaretPosition(0);
        
        /*
         * Associate the second scroll pane with the appropriate text pane
         */
        jScrollPane2.setViewportView(jTextPane1);
        ((JPanel) getContentPane()).add(jScrollPane2);
        
        /*
         * Apply non-editable settings, as well as feed it the output 2 line numbers string.
         */
        jTextPane1.setEditable(false);
        jTextPane1.setText(output2);
        jTextPane1.setCaretPosition(0);
        
        /*
         * Associate the main viewing port with the appropriate pane
         */
        jScrollPane1.setViewportView(pane);
        ((JPanel) getContentPane()).add(jScrollPane1);
        
        /*
         * feed the text into the pane, moving to the top when we're done.
         */
        pane.setText(output);
        pane.setCaretPosition(0);
              
        //pack all the swing components so they handle screen size change
        //as well as associations with one another.
        //pack();
        
	}
	
	/*
	 * Called when we push the up button.
	 * 
	 * Reduces the currentLine variable by one, and updates the visualizer appropriately. 
	 * 
	 * the process saves any changes made on the values currently in the cache, updates
	 * the scroll bar to be located appropriately, and then re-renders based on the 
	 * new information that is pulled directly out of the .h5 file.
	 */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
    	try {
			updateData();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    	if (currentLine > 0)
    		currentLine--;
    	updateScrollBar();
    	updateLines();    	
    }
    
	/*
	 * Called when we push the down button.
	 * 
	 * Increases the currentLine variable by one, and updates the visualizer appropriately. 
	 */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) throws BadLocationException {
    	updateData();
    	if (currentLine < totalRows){
    		currentLine++;
    	}else{
    		currentLine = totalRows;
    	}    	
    	updateScrollBar();
    	updateLines();
    }
    
	/*
	 * Called when we push the page up button.
	 * 
	 * decreases the currentLine variable by twenty, and updates the visualizer appropriately. 
	 */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
    	try {
			updateData();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    	if (currentLine >= 20){
    		currentLine-=20;
    	} else { 
    		currentLine = 0;
    	}
    	updateScrollBar();
    	updateLines();
    }
    
	/*
	 * Called when we push the page down button.
	 * 
	 * Increases the currentLine variable by twenty, and updates the visualizer appropriately. 
	 */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
    	try {
			updateData();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    	if (currentLine <= totalRows - 20)
    		currentLine+=20;
    	updateScrollBar();
    	updateLines();
    }

	/*
	 * Called when we push the goto line button
	 * 
	 * moves currentLine to the line specified by user, and updates the visualizer appropriately. 
	 */
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
    	try {
			updateData();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
    	int linegoto = Integer.parseInt(jTextField1.getText());
    	if (linegoto > totalRows){linegoto = (int) totalRows;}
    	if (linegoto < 0) {linegoto = 0;}
    	currentLine = linegoto;
    	gotoLine(linegoto);
    	updateScrollBar();
    	currentLine = linegoto;
    	updateLines();
    }
    
    /*
     * Handles whenever the scroll bar is altered in any way, either via code or
     * via user input.
     * 
     * it saves any current cache changes, updates the currentLine value, 
     * and then re-renders the visualizer appropriately.
     */
    private void jScrollBar1AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
    	
        float curVal = evt.getValue();
        
        float curPer = curVal / 90;
        
        float curLn = curPer * totalRows;
        
        if (curLn >= totalRows) {curLn = totalRows;}
        if (curLn <= 0) {curLn = 0;}
        
        if (noDblUpdate == false){
        	
        	try {
        		updateData();
        	} catch (BadLocationException e) {
        		e.printStackTrace();
        	}
		
        	currentLine = (int)curLn;
        	updateLines();
        	
        } else {
        	noDblUpdate = false;
        }
    }
    
    /*
     * The updateData function is what handles saving the data
     * to a temporary cache as we make changes in the main pane
     * while not actually writing any of them to the file. By doing 
     * so, we can see any changes we make as we go, and apply them at
     * a later time.  It works by cycling through everything in the 
     * main text pane, checking for descrepencies with the cacheGrid, 
     * and saving any of the lines that have been changed.  It also saves
     * the line number that had the change in it, and keeps track of 
     * the total number of changes we've seen.  The update of the cacheGrid
     * when we are looking for changes is not handled in this subroutine.
     */
    public void updateData() throws BadLocationException {

        byte testByte = 0;
        
        int p = 0; int k = 0; int newLineCount = 0;
        boolean keepMoving = true;
        
        if (is2DDataset) { 
        /*
         * This loop puts the displayed text into a 1D byte array.
         * This lets us have an updated version of the list while
         * simultaneously removing and \n char's from it that we used
         * to properly format the list.
         */
            while (keepMoving) {
            	
            	try {
            		testByte = pane.getText(p, 1).getBytes()[0];
    			} catch (BadLocationException e) {
    				keepMoving = false;
    			}
    			
    			if (keepMoving) {
    				if (testByte != 10){
    					if (k < totalColumns){
    						if (cacheGrid[(int) (newLineCount)][k] != testByte){
    							rowNumbers[(int) rowChangeCount] = (int)(currentLine + newLineCount);
    							for (int t = -k; t < totalColumns - k; t++){
    								rowValues[(int) rowChangeCount][k + t] = pane.getText(p + t, 1).getBytes()[0];
    							}
    							rowChangeCount++;
    							p = (int) (p + totalColumns - k - 1);
    							k = (int) totalColumns;
    						}
    						k++;
    						p++;
    					} else {
    						k++;
    						p++;
    					}
    				} else {
    					newLineCount++;
    					k = 0;
    					p++;
    				}
    			}
            }
        } else if (is1DDataset) {
            while (keepMoving) {
            	
            	try {
            		testByte = pane.getText(p, 1).getBytes()[0];
    			} catch (BadLocationException e) {
    				keepMoving = false;
    			}
    			
    			if (keepMoving) {
    				if (testByte != 10){
    					if (k < totalColumns){
    						if (cacheGrid[(int) (newLineCount)][k] != testByte){
    							rowNumbers[(int) rowChangeCount] = (int)(currentLine + newLineCount);
    							for (int t = -k; t < totalColumns - k; t++){
    								rowValues[(int) rowChangeCount][k + t] = pane.getText(p + t, 1).getBytes()[0];
    							}
    							rowChangeCount++;
    							p = (int) (p + totalColumns - k - 1);
    							k = (int) totalColumns;
    						}
    						k++;
    						p++;
    					} else {
    						k++;
    						p++;
    					}
    				} else {
    					newLineCount++;
    					k = 0;
    					p++;
    				}
    			}
            }
        } else {
            while (keepMoving) {
            	
            	try {
            		testByte = pane.getText(p, 1).getBytes()[0];
    			} catch (BadLocationException e) {
    				keepMoving = false;
    			}
    			
    			if (keepMoving) {
    				if (testByte != 10){
    					if (k < totalColumns){
    						if (cacheGrid[(int) (newLineCount)][k] != testByte){
    							rowNumbers[(int) rowChangeCount] = (int)(currentLine + newLineCount);
    							for (int t = -k; t < totalColumns - k; t++){
    								rowValues[(int) rowChangeCount][k + t] = pane.getText(p + t, 1).getBytes()[0];
    							}
    							rowChangeCount++;
    							p = (int) (p + totalColumns - k - 1);
    							k = (int) totalColumns;
    						}
    						k++;
    						p++;
    					} else {
    						k++;
    						p++;
    					}
    				} else {
    					newLineCount++;
    					k = 0;
    					p++;
    				}
    			}
            }
        }
    }
  
    /* 
     * handles when the scroll bar is updated via code, using noDblUpdate 
     * so that we don't get stuck in a loop (we update the scroll bar via code,
     * the listener detects a change, and calls the update again, rinse, repeat).
     */
    public void updateScrollBar() {
    	float curPer = (float)currentLine / (float)totalRows;
    	float curLn = curPer * 90;
    	noDblUpdate = true;
    	jScrollBar1.setValue((int)curLn);
    }
    
    /*
     * handles setting up the output for the top-most bar
     * organizes it so that the user can easily pin-point the data
     * they are looking at and the appropriate coordinates.
     */
    public String getOutput3() {
    	
    	byte output3b[] = new byte[(int) (totalColumns * 2 + 3)];
    	
        for (int k = 1; k <= (int) totalColumns; k++){
        	if (k % 10 == 0 && k != 0) {
        		if (k/10 == 1) {output3b[k] =  '1';}
        		if (k/10 == 2) {output3b[k] =  '2';}
        		if (k/10 == 3) {output3b[k] =  '3';}
        		if (k/10 == 4) {output3b[k] =  '4';}
        		if (k/10 == 5) {output3b[k] =  '5';}
        		if (k/10 == 6) {output3b[k] =  '6';}
        		if (k/10 == 7) {output3b[k] =  '7';}
        		if (k/10 == 8) {output3b[k] =  '8';}
        		if (k/10 == 9) {output3b[k] =  '9';}
        	} else {
        		output3b[k] = ' ';
        	}
        }
        
        output3b[(int) totalColumns + 1] = '\n';
        
        for (int k = (int) (totalColumns + 2); k <= totalColumns * 2 + 1; k++){
        	if ((k - totalColumns - 2)%10 == 0) {output3b[(int) (k)] =  '1';}
    		if ((k - totalColumns - 2)%10 == 1) {output3b[(int) (k)] =  '2';}
    		if ((k - totalColumns - 2)%10 == 2) {output3b[(int) (k)] =  '3';}
    		if ((k - totalColumns - 2)%10 == 3) {output3b[(int) (k)] =  '4';}
    		if ((k - totalColumns - 2)%10 == 4) {output3b[(int) (k)] =  '5';}
    		if ((k - totalColumns - 2)%10 == 5) {output3b[(int) (k)] =  '6';}
    		if ((k - totalColumns - 2)%10 == 6) {output3b[(int) (k)] =  '7';}
    		if ((k - totalColumns - 2)%10 == 7) {output3b[(int) (k)] =  '8';}
    		if ((k - totalColumns - 2)%10 == 8) {output3b[(int) (k)] =  '9';}
    		if ((k - totalColumns - 2)%10 == 9) {output3b[(int) (k)] =  '0';}
        }
        
        //testline will hold the string value and will be returned by the function.
        String testLine = "";
        
        //attempt to write it with monospaced formatting to the string
        try {
			testLine = new String(output3b, "Cp1252" /* encoding */);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
    	
		//return the formatted string
    	return testLine;
    }
    
    /*
     * Handles the special case when we use the goto line button 
     * as opposed to manual changes like everything else.
     */
    public void gotoLine(int ln) {
    	
    	String testLine = ""; String output = ""; String output2 = "";
        //int curLim = (int) totalRows;
        int max = (int) totalRows;
        int needCacheLength = 0;
        
        if (ln + (int)cacheLength > max) { 
        	//curLim = max - ln; 
        	needCacheLength = (int) (max - currentLine);
        } else {
        	//curLim = ln + (int)cacheLength;
        	needCacheLength = 100;
        }
 
        /*
         * find any previous changes made to our cacheGrid 
         * and apply them appropriately before displaying
         */
        updateCacheGrid(needCacheLength);
        
        for (int p = 0; p < 100; p++){
			try {
				testLine = new String(cacheGrid[p], "Cp1252" /* encoding */);
			} catch (UnsupportedEncodingException e) {
				viewer.showStatus(e.toString());
			}
			output = output + testLine + "\n";
			output2 = output2 + (p + currentLine) + "\n";
        }
        
        pane.setText(output);
        pane.setCaretPosition(0);
        
        jTextPane1.setText(output2);
        jTextPane1.setCaretPosition(0);
        jTextPane1.setEditable(false);
    }
    
    /*
     * Handles updating the rendered content for any changes that are 
     * made that are not done via gotoLine 
     */
    public void updateLines() {
    	
    	String testLine = ""; String output = ""; String output2 = "";
        int max = (int) totalRows;
        int needCacheLength = 0;
        
        if ((int)currentLine + (int)cacheLength > max) { 
        	needCacheLength = (int) (max - currentLine);
        } else {
        	needCacheLength = 100;
        }
        
        /*
         * find any previous changes made to our cacheGrid 
         * and apply them appropriately before displaying
         */
        updateCacheGrid(needCacheLength);

        for (int p = 0; p < curRows; p++){
			try {
				testLine = new String(cacheGrid[p], "Cp1252" /* encoding */);
				
			} catch (UnsupportedEncodingException e) {
				viewer.showStatus(e.toString());
			}
			output = output + testLine + "\n";
			output2 = output2 + (p + currentLine) + "\n";
        }
        
        pane.setText("");
        pane.setText(output);
        pane.setCaretPosition(0);
        
        jTextPane1.setText(output2);
        jTextPane1.setCaretPosition(0);
        jTextPane1.setEditable(false);
    }
     
    /*
     * This subroutine handles changing the settings of the dataset
     * so that when we access it, we are accessing the appropriate 
     * hyper-slab.  It also handles updating any of the previous
     * changes that were made but not yet saved so that we visualize
     * everything accurately.
     */
    public void updateCacheGrid(int curLim){
		rank = dataset.getRank(); // number of dimension of the dataset
		dims = dataset.getDims(); // the dimension sizes of the dataset
		selected = dataset.getSelectedDims(); // the selected size of the dataet
		start = dataset.getStartDims(); // the off set of the selection
		stride = dataset.getStride(); // the stride of the dataset
		selectedIndex = dataset.getSelectedIndex(); // the selected dimensions for display
		 
		 // reset the selection arrays
		start[0] = currentLine;
		 
		 // set the selection size of dim1 and dim2
		selected[0] = curLim;
		 
		if (start[0] + selected[0] > totalRows){
			selected[0] = totalRows - start[0] + 1;
		}
		 
		curRows = selected[0];
		
		cacheStart = start[0];
		cacheEnd = start[0] + selected[0];

		/*
		 * Give our dataset the appropriate settings so we only read in what is absolutely necessary
		 */
		try {
			dataset.getData();
		} catch (OutOfMemoryError e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/*
		 * stream in the dataset in byte format
		 */
		try {
			cacheStream = dataset.readBytes();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/*
		 * Convert that Data over to our 2D grid
		 */
        for (int i = 0; i < curRows; i++) {
        	for (int j = 0; j < curColumns; j++) {
        		cacheGrid[i][j] = cacheStream[(int) (curColumns * i + j)];
        	}
        }
        
        /*
         * Check all of our current changes and apply them if they are within the given boundaries
         */
        for (int i = 0; i < (int) rowChangeCount; i++){
        	if (rowNumbers[i] >= cacheStart && rowNumbers[i] <= cacheEnd){
        		for (int k = 0; k < totalColumns; k++){
        			cacheGrid[(int) (rowNumbers[i] - currentLine)][k] = rowValues[i][k];
        		}
        	}
        }
    }
   
	/**
     * Checks to see if some action was performed from the menu screen
     * and then takes appropriate action depending on user choice.
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals("Close"))
			dispose(); // terminate the application
		else if (cmd.equals("Save to text file"))
			try {
                saveAsText();
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog((JFrame) viewer, ex, getTitle(),
                        JOptionPane.ERROR_MESSAGE);
            }
		else if (cmd.equals("Save changes"))
			updateValueInFile();
		else if (cmd.equals("Print"))
			print();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    /*
     * Lets us check for any key input, saves us the time of writing an identical file when saving
     */
    public void keyTyped(KeyEvent e) {
        isStringChanged = true;
    }

    /**
     * Set up the menu bar that will be listed at the top
     * of the dataset visualizer in stringview mode.
     * 
     * The options given are as follows:
     * 
     * 1.) Save text to file -> export text to a .txt file
     * 		formatted identically to the way it is seen in viewer
     * 
     * 2.) Save changes -> stores all of our current data into
     * 		a byte array, then writes that array to dataset
     * 
     * 3.) Print -> allows user to print out the displayed list.
     * 
     */
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("String", false);
        menu.setMnemonic('T');
        bar.add(menu);

        JMenuItem item = new JMenuItem("Save To Text File");
        item.addActionListener(this);
        item.setActionCommand("Save to text file");
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Save Changes");
        item.addActionListener(this);
        item.setActionCommand("Save changes");
        menu.add(item);
        
        menu.addSeparator();

        item = new JMenuItem("Print");
        item.addActionListener(this);
        item.setActionCommand("Print");
        menu.add(item);

        menu.addSeparator();

        menu.addSeparator();

        item = new JMenuItem("Close");
        item.addActionListener(this);
        item.setActionCommand("Close");
        menu.add(item);
        
        JButton button;
        Insets margin = new Insets( 0, 2, 0, 2 );
        
        if (is3D) {
            bar.add( new JLabel("     ") );

            // first button
            button = new JButton( ViewProperties.getFirstIcon() );
            bar.add( button );
            button.setToolTipText( "First" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "First page" );

            // previous button
            button = new JButton( ViewProperties.getPreviousIcon() );
            bar.add( button );
            button.setToolTipText( "Previous" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "Previous page" );

            frameField = new JTextField(String.valueOf(curFrame));
            frameField.setMaximumSize(new Dimension(50,30));
            bar.add( frameField );
            frameField.setMargin( margin );
            frameField.addActionListener( this );
            frameField.setActionCommand( "Go to frame" );

            JLabel tmpField = new JLabel(String.valueOf(maxFrame),SwingConstants.CENTER);
            tmpField.setMaximumSize(new Dimension(50,30));
            bar.add( tmpField );

            // next button
            button = new JButton( ViewProperties.getNextIcon() );
            bar.add( button );
            button.setToolTipText( "Next" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "Next page" );

            // last button
            button = new JButton( ViewProperties.getLastIcon() );
            bar.add( button );
            button.setToolTipText( "Last" );
            button.setMargin( margin );
            button.addActionListener( this );
            button.setActionCommand( "Last page" );
        }

        return bar;
    }

    /**
     * Save any changes we've made to the .h5 file.
     * Does so by completely rewriting the dataset
     * that is currently held in memory.
     * 
     * If in read only mode, return after doing nothing
     */
    public void updateValueInFile() {
        if (isReadOnly) {
            return;
        }
        
        try {
			updateData();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
        
        saveCacheGrid = new byte[3][(int) totalColumns];
        saveCacheStream = new byte[(int) (3 * totalColumns + 3)];
        
        for (int f = 0; f < rowChangeCount; f++){
    		rank = dataset.getRank(); // number of dimension of the dataset
    		dims = dataset.getDims(); // the dimension sizes of the dataset
    		selected = dataset.getSelectedDims(); // the selected size of the dataet
    		start = dataset.getStartDims(); // the off set of the selection
    		stride = dataset.getStride(); // the stride of the dataset
    		selectedIndex = dataset.getSelectedIndex(); // the selected dimensions for display
     	
   		 	// reset the selection arrays
    		start[0] = rowNumbers[f];
    		 
    		 // set the selection size of dim1 and dim2
    		selected[0] = 2;
    		 
    		if (start[0] + selected[0] > totalRows){
    			selected[0] = totalRows - start[0] + 1;
    		}
    		 
    		int tcurRows = (int) selected[0];
    		int tcurColumns = (int) selected[1];
    		
    		/*
    		 * Give our dataset the appropriate settings so we only read in what is absolutely necessary
    		 */
    		try {
    			dataset.getData();
    		} catch (OutOfMemoryError e1) {
    			e1.printStackTrace();
    		} catch (Exception e1) {
    			e1.printStackTrace();
    		}
    		
    		/*
    		 * stream in the dataset in byte format
    		 */
    		try {
    			saveCacheStream = dataset.readBytes();
    		} catch (Exception e1) {
    			e1.printStackTrace();
    		}
    		
    		/*
    		 * Convert that Data over to our 2D grid
    		 */
            for (int i = 0; i < tcurRows; i++) {
            	for (int j = 0; j < tcurColumns; j++) {
            		saveCacheGrid[i][j] = saveCacheStream[(int) (tcurColumns * i + j)];
            	}
            }
            
            /*
             * Check all of our current changes and apply them if they are within the given boundaries
             */            
    		for (int k = 0; k < tcurColumns; k++){
    			saveCacheGrid[0][k] = rowValues[f][k];
    		}
    		
    		byte[] testDisplayGrid = new byte[tcurColumns * 3];
    		
            for (int i = 0; i < tcurRows; i++) {
            	for (int j = 0; j < tcurColumns; j++) {
            		testDisplayGrid[tcurColumns * i + j] = saveCacheGrid[i][j];
            	}
            }
            
            try {
                dataset.write(testDisplayGrid);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex, getTitle(),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            
        }
        
		rank = dataset.getRank(); // number of dimension of the dataset
		dims = dataset.getDims(); // the dimension sizes of the dataset
		selected = dataset.getSelectedDims(); // the selected size of the dataet
		start = dataset.getStartDims(); // the off set of the selection
		stride = dataset.getStride(); // the stride of the dataset
		selectedIndex = dataset.getSelectedIndex(); // the selected dimensions for display
		
		rank = orank; // number of dimension of the dataset
		dims = odims; // the dimension sizes of the dataset
		selected = oselected; // the selected size of the dataet
		start = ostart; // the off set of the selection
		stride = ostride; // the stride of the dataset
		selectedIndex = oselectedIndex; // the selected dimensions for display
		
		try {
			dataset.getData();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

        isStringChanged = false;

    }

    /**  
     * This routine Allows us to export the currently allocated
     * String array to an output txt file.
     * 
     * The option to overwrite an existing file is given.
     * 
     */
    @SuppressWarnings("unchecked")
	private void saveAsText() throws Exception {
        final JFileChooser fchooser = new JFileChooser(dataset.getFile());
        fchooser.setFileFilter(DefaultFileFilter.getFileFilterText());
        fchooser.changeToParentDirectory();
        fchooser.setDialogTitle("Save Current Data To Text File --- "
                + dataset.getName());

        File choosedFile = new File(dataset.getName() + ".txt");

        fchooser.setSelectedFile(choosedFile);
        int returnVal = fchooser.showSaveDialog(this);

        if (returnVal != JFileChooser.APPROVE_OPTION) {
            return;
        }

        choosedFile = fchooser.getSelectedFile();
        if (choosedFile == null) {
            return;
        }

        String fname = choosedFile.getAbsolutePath();

        /*
         * This section checks to see if the file is already
         * in use, and then displays the appropriate message 
         */
        List fileList = viewer.getTreeView().getCurrentFiles();
        if (fileList != null) {
            FileFormat theFile = null;
            Iterator iterator = fileList.iterator();
            while (iterator.hasNext()) {
                theFile = (FileFormat) iterator.next();
                if (theFile.getFilePath().equals(fname)) {
                    JOptionPane.showMessageDialog(this,
                            "Unable to save data to file \"" + fname
                                    + "\". \nThe file is being used.",
                            getTitle(), JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (choosedFile.exists()) {
            int newFileFlag = JOptionPane.showConfirmDialog(this,
                    "File exists. Do you want to replace it ?",
                    this.getTitle(), JOptionPane.YES_NO_OPTION);
            if (newFileFlag == JOptionPane.NO_OPTION) {
                return;
            }
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                choosedFile)));
        
        byte t;
        char y;
        for (int p = 0; p < numRows; p++){
        	for (int k = 0; k < numCols; k++){
				t = displayGrid[p][k];
				y = (char)t;
				out.print(y);
        	}
        	out.println();
        }

        out.flush();
        out.close();

        viewer.showStatus("Data save to: " + fname);

        try {
            RandomAccessFile rf = new RandomAccessFile(choosedFile, "r");
            long size = rf.length();
            rf.close();
            viewer.showStatus("File size (bytes): " + size);
        }
        catch (Exception ex) {
        	viewer.showStatus(ex.toString());
        }
    }

    @Override
	public void dispose() {
        if (isStringChanged && !isReadOnly) {
            int op = JOptionPane.showConfirmDialog(this, "\""
                    + dataset.getName() + "\" has changed.\n"
                    + "Do you want to save the changes?", getTitle(),
                    JOptionPane.YES_NO_OPTION);

            if (op == JOptionPane.YES_OPTION) {
                updateValueInFile();
            }
        }

        viewer.removeDataView(this);

        super.dispose();
    }

    // Implementing DataView.
    public HObject getDataObject() {
        return dataset;
    }

    // Implementing StringView.
    public byte[] getText() {
        return displayText;
    }

    /**
     * Print routine to print out our list of strings 
     * Utilizes Swing's ability to print out the contents of
     * any component using an interactive dialogue
     */
    private void print() {
    	try {
			pane.print();
		} catch (PrinterException e) {
			viewer.showStatus(e.toString());
			return;
		}
    }
}
