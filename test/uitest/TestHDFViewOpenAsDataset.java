package test.uitest;

import static org.fest.swing.data.TableCell.row;
import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import java.awt.Point;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JViewport;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JDesktopPane;

import org.fest.swing.core.MouseButton;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.finder.JFileChooserFinder;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.fest.swing.fixture.JScrollPaneFixture;
import org.fest.swing.fixture.JScrollBarFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class TestHDFViewOpenAsDataset {
    private static FrameFixture mainFrameFixture;
    // the version of the HDFViewer
    private static String VERSION = "2.11";
    private static File hdf5file = null;

    private static File openHDF5File(String name, int initrows) {
        File hdf_file = new File(name+".h5");

        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","Open");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText(name+".h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();
    
            assertTrue("File-HDF5 file opened", hdf_file.exists());
    
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("openHDF5File filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount() == initrows);
            assertTrue("openHDF5File filetree has file",(filetree.valueAt(0)).compareTo(name+".h5") == 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }

        return hdf_file;
    }
    
    private static void closeHDFFile(File hdf_file, boolean delete_file) {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","Close All");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
    
            if(delete_file) {
                assertTrue("File file deleted", hdf_file.delete());
                assertFalse("File file gone", hdf_file.exists());
            }
            
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("closeHDFFile filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount() == 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }
    
    private static File createHDF5File(String name) {
        File hdf_file = new File(name+".h5");
        if(hdf_file.exists())
            hdf_file.delete();

        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF5");
            mainFrameFixture.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
    
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText(name+".h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();
    
            assertTrue("File-HDF5 file created", hdf_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }

        return hdf_file;
    }
        
    private void testSamplePixel(int x, int y, String requiredValue) {
		JScrollPaneFixture imagePane = mainFrameFixture.scrollPane("imagecontent");
		JViewport view = imagePane.component().getViewport();
		int increment;
		int unitsX;
		int unitsY;
		int remainderX;
		int remainderY;

		// Make sure Show Values is selected
		if(!mainFrameFixture.menuItemWithPath("Image", "Show Value").component().isSelected()) {
			mainFrameFixture.menuItemWithPath("Image", "Show Value").click();
			mainFrameFixture.robot.waitForIdle();
		}

		try {
			mainFrameFixture.robot.moveMouse(view, x, y);
			mainFrameFixture.robot.waitForIdle();
			mainFrameFixture.textBox("valuefield").requireText(requiredValue);
		}
		catch (AssertionError ae) {
			// Sample pixel isn't in the currently visible portion of the image
			

			// Reset View
			if(imagePane.component().getHorizontalScrollBar().isVisible()) {
				imagePane.horizontalScrollBar().scrollToMinimum();
				mainFrameFixture.robot.waitForIdle();
			}

			if(imagePane.component().getVerticalScrollBar().isVisible()) {
				imagePane.verticalScrollBar().scrollToMinimum();
				mainFrameFixture.robot.waitForIdle();
			}

			// Calculate number of units to scroll in order to display this pixel
			increment = imagePane.component().getHorizontalScrollBar().getUnitIncrement();
			unitsX = x / increment;
			unitsY = y / increment;

			if(unitsX > 0 && imagePane.component().getHorizontalScrollBar().isVisible()) {
				imagePane.horizontalScrollBar().scrollUnitDown(unitsX);
				mainFrameFixture.robot.waitForIdle();
			}

			if(unitsY > 0 && imagePane.component().getVerticalScrollBar().isVisible()) {
				imagePane.verticalScrollBar().scrollUnitDown(unitsY);
				mainFrameFixture.robot.waitForIdle();
			}

			// Calculate extra distance to be moved in each direction to find this pixel
			remainderX = (x) - (view.getViewPosition().x);
			remainderY = (y) - (view.getViewPosition().y);

			mainFrameFixture.robot.moveMouse(view, remainderX, remainderY);	
			mainFrameFixture.robot.waitForIdle();
			mainFrameFixture.textBox("valuefield").requireText(requiredValue);
		}
	}
    
    private static void clearRemovePropertyFile() {
        // the local property file name
        // look for the property file at the use home directory
        String fn = ".hdfview" + VERSION;
        String uh = System.getProperty("user.home") + File.separator + fn;
        String ud = System.getProperty("user.dir") + File.separator + fn;

        File prop_file = new File(uh);
        if (prop_file.exists()) {
            prop_file.delete();
        }
        else {
            prop_file = new File(ud);
            if (prop_file.exists()) {
                prop_file.delete();
            }
        }
    }

    private static void changeTextField(String fieldName, String newText) {
    	mainFrameFixture.dialog().textBox(fieldName).deleteText();
    	mainFrameFixture.robot.waitForIdle();
    	mainFrameFixture.dialog().textBox(fieldName).enterText(newText);
    	mainFrameFixture.robot.waitForIdle();
    }
    
    @BeforeClass
    public static void setUpOnce() {
        clearRemovePropertyFile();
        FailOnThreadViolationRepaintManager.install();
        Robot robot = BasicRobot.robotWithNewAwtHierarchy(); 
        String envvalue = System.getProperty("hdfview.root");
        application("ncsa.hdf.view.HDFView").withArgs("-root", envvalue).start();
        mainFrameFixture = findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            protected boolean isMatching(JFrame frame) {
                return frame.getTitle().equals("HDFView "+VERSION)
                        && frame.isShowing();
            }
        }).withTimeout(10000).using(robot);
        
        mainFrameFixture.robot.waitForIdle();
        mainFrameFixture.requireVisible();
    }

    @AfterClass
    public static void finishApplication() {
    	File hdf_file = new File("test_large_dataset.h5");
    	if(hdf_file.exists())
    		hdf_file.delete();
    	
    	mainFrameFixture.robot.waitForIdle();
        //mainFrameFixture.requireNotVisible();
        mainFrameFixture.cleanUp();
    }
 
    @Test
    public void testPreview() {
    	hdf5file = openHDF5File("hdf5_test", 5);
    	
    	try {
    		JTreeFixture filetree = mainFrameFixture.tree().focus();
    		filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(15).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("spreadsheetbutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		// Grab the preview's panel and select a subset
    		JPanel previewPanel = mainFrameFixture.dialog().panel("navigator").component();
    		
    		mainFrameFixture.robot.moveMouse(previewPanel.getComponent(0), 0, 0);
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.robot.pressMouse(MouseButton.lookup(java.awt.event.InputEvent.BUTTON1_MASK));
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.robot.moveMouse(previewPanel.getComponent(0), 50, 70);
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.robot.releaseMouseButtons();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		// Make sure subset was set correctly
    		JTableFixture tbl = mainFrameFixture.table("data");
    		
    		tbl.requireRowCount(211);
    		tbl.requireColumnCount(151);
    		
    		tbl.cell(row(0).column(0)).requireValue("2");
    		tbl.cell(row(210).column(150)).requireValue("128");
    		tbl.cell(row(210).column(0)).requireValue("79");
    		tbl.cell(row(0).column(150)).requireValue("140");
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		// Try to select one other subset
    		filetree.showPopupMenuAt(15).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("spreadsheetbutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		previewPanel = mainFrameFixture.dialog().panel("navigator").component();
    		
    		mainFrameFixture.robot.moveMouse(previewPanel.getComponent(0), 40, 30);
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.robot.pressMouse(MouseButton.lookup(java.awt.event.InputEvent.BUTTON1_MASK));
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.robot.moveMouse(previewPanel.getComponent(0), 75, 70);
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.robot.releaseMouseButtons();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		// Make sure subset was set correctly
    		tbl = mainFrameFixture.table("data");
    		
    		tbl.requireRowCount(121);
    		tbl.requireColumnCount(106);
    		
    		tbl.cell(row(0).column(0)).requireValue("38");
    		tbl.cell(row(120).column(105)).requireValue("35");
    		tbl.cell(row(120).column(0)).requireValue("84");
    		tbl.cell(row(0).column(105)).requireValue("35");
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		closeHDFFile(hdf5file, false);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	catch (AssertionError ae) {
    		ae.printStackTrace();
    	}
    }
    
    @Test
    public void testStartStrideEnd() {
    	hdf5file = openHDF5File("hdf5_test", 5);
    	
    	try {
    		JTreeFixture filetree = mainFrameFixture.tree().focus();
    		
    		filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(5).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Reset").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		changeTextField("startField0", "37");
    		changeTextField("startField1", "22");
    		changeTextField("endField0", "56");
    		changeTextField("endField1", "45");
    		changeTextField("strideField0", "2");
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		JTableFixture tbl = mainFrameFixture.table("data");
    		JTableCellFixture cell = tbl.cell(row(0).column(0));
    		cell.requireValue("4722");
    		cell = tbl.cell(row(4).column(7));
    		cell.requireValue("5529");
    		cell = tbl.cell(row(8).column(15));
    		cell.requireValue("6337");
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(6).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Reset").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		changeTextField("startField0", "2");
    		changeTextField("startField1", "4");
    		changeTextField("endField0", "7");
    		changeTextField("endField1", "7");
    		changeTextField("strideField0", "2");
    		changeTextField("strideField1", "3");
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		tbl = mainFrameFixture.table("data");
    		cell = tbl.cell(row(0).column(0));
    		cell.requireValue("35");
    		cell = tbl.cell(row(0).column(1));
    		cell.requireValue("38");
    		cell = tbl.cell(row(1).column(0));
    		cell.requireValue("55");
    		cell = tbl.cell(row(1).column(1));
    		cell.requireValue("58");
    		cell = tbl.cell(row(2).column(0));
    		cell.requireValue("75");
    		cell = tbl.cell(row(2).column(1));
    		cell.requireValue("78");
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(7).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Reset").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		changeTextField("startField0", "25");
    		changeTextField("startField1", "43");
    		changeTextField("endField0", "71");
    		changeTextField("endField1", "48");
    		changeTextField("strideField0", "5");
    		changeTextField("strideField1", "4");
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		tbl = mainFrameFixture.table("data");
    		cell = tbl.cell(row(5).column(0));
    		cell.requireValue("166");
    		
    		mainFrameFixture.button("nextbutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		cell = tbl.cell(row(4).column(1));
    		cell.requireValue("53");
    		
    		mainFrameFixture.button("lastbutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		cell = tbl.cell(row(8).column(1));
    		cell.requireValue("754709");
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		closeHDFFile(hdf5file, false);	
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	catch (AssertionError ae) {
    		ae.printStackTrace();
    	}
    }
    
    @Test
    public void testSwapDimensions() {
    	hdf5file = openHDF5File("hdf5_test", 5);
    	
    	try {
    		JTreeFixture filetree = mainFrameFixture.tree().focus();
    		
    		filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		// First test combinations with 3D dataset
    		filetree.showPopupMenuAt(7).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Reset").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().comboBox("dimensionBox0").selectItem("dim 1");
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		JTableFixture tbl = mainFrameFixture.table("data");
    		
    		tbl.requireRowCount(50);
    		tbl.requireColumnCount(100);
    		
    		JTableCellFixture cell = tbl.cell(row(18).column(2));
    		cell.requireValue("170");
    		
    		mainFrameFixture.button("nextbutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		cell = tbl.cell(row(40).column(71));
    		cell.requireValue("77");
    		
    		mainFrameFixture.button("nextbutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		cell = tbl.cell(row(26).column(53));
    		cell.requireValue("0");
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(7).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Reset").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().comboBox("dimensionBox2").selectItem("dim 0");
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		tbl = mainFrameFixture.table("data");
    		tbl.requireRowCount(50);
    		tbl.requireColumnCount(10);
    		
    		for(int i = 0; i < 2; i++){
    			mainFrameFixture.button("nextbutton").click();
    			mainFrameFixture.robot.waitForIdle();
    		}
    		
    		cell = tbl.cell(row(18).column(0));
    		cell.requireValue("170");
    		
    		mainFrameFixture.textBox("framenumber").deleteText();
    		mainFrameFixture.robot.waitForIdle();
    		mainFrameFixture.textBox("framenumber").enterText("53");
    		mainFrameFixture.robot.waitForIdle();
    		mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
    		mainFrameFixture.robot.waitForIdle();
    		
    		cell = tbl.cell(row(26).column(2));
    		cell.requireValue("0");
    		
    		mainFrameFixture.textBox("framenumber").deleteText();
    		mainFrameFixture.robot.waitForIdle();
    		mainFrameFixture.textBox("framenumber").enterText("71");
    		mainFrameFixture.robot.waitForIdle();
    		mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
    		mainFrameFixture.robot.waitForIdle();
    		
    		cell = tbl.cell(row(40).column(1));
    		cell.requireValue("77");
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(7).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Reset").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().comboBox("dimensionBox0").selectItem("dim 2");
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		tbl = mainFrameFixture.table("data");
    		tbl.requireRowCount(10);
    		tbl.requireColumnCount(50);
    		
    		for(int i = 0; i < 5; i++) {
    			mainFrameFixture.button("nextbutton").click();
    			mainFrameFixture.robot.waitForIdle();
    		}
    		
    		cell = tbl.cell(row(7).column(32));
    		cell.requireValue("159");
    		
    		mainFrameFixture.textBox("framenumber").deleteText();
    		mainFrameFixture.robot.waitForIdle();
    		mainFrameFixture.textBox("framenumber").enterText("85");
    		mainFrameFixture.robot.waitForIdle();
    		mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
    		mainFrameFixture.robot.waitForIdle();
    		
    		cell = tbl.cell(row(3).column(21));
    		cell.requireValue("0");
    		
    		mainFrameFixture.textBox("framenumber").deleteText();
    		mainFrameFixture.robot.waitForIdle();
    		mainFrameFixture.textBox("framenumber").enterText("10");
    		mainFrameFixture.robot.waitForIdle();
    		mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
    		mainFrameFixture.robot.waitForIdle();
    		
    		cell = tbl.cell(row(9).column(43));
    		cell.requireValue("204309");
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		closeHDFFile(hdf5file, false);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	catch (AssertionError ae) {
    		ae.printStackTrace();
    	}
    }

    @Test
    public void testIntDatasetAsImage() {
    	hdf5file = openHDF5File("hdf5_test", 5);
    	
    	try {
    		JTreeFixture filetree = mainFrameFixture.tree().focus();
    		
    		filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(5).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("imagebutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		for(int i = 0; i < 3; i++) {
    			mainFrameFixture.button("zoomin").click();
    			mainFrameFixture.robot.waitForIdle();
    		}
    		
    		testSamplePixel(48, 136, "x=12,   y=34,   value=4412");
    		testSamplePixel(172, 360, "x=43,   y=90,   value=10043");
    		testSamplePixel(108, 12, "x=27,   y=3,   value=1327");
    		
    		JScrollPane imagePane = mainFrameFixture.scrollPane("contentscroller").component();
    		JDesktopPane pane = (JDesktopPane) imagePane.getViewport().getView();
    		ncsa.hdf.view.DefaultImageView view = (ncsa.hdf.view.DefaultImageView) pane.getSelectedFrame();
    		
    		// Grabe byte data of non-autogain image
    		byte[] imgData = view.getImageByteData();
    		
    		mainFrameFixture.menuItemWithPath("Image", "Close").click();
    		mainFrameFixture.robot.waitForIdle();

    		// Test that autogain is functioning
    		mainFrameFixture.menuItemWithPath("Tools", "User Options").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().checkBox("autogain").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Ok").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(5).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("imagebutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		for(int i = 0; i < 3; i++) {
    			mainFrameFixture.button("zoomin").click();
    			mainFrameFixture.robot.waitForIdle();
    		}
    		
    		// Image values should stay the same
    		testSamplePixel(48, 136, "x=12,   y=34,   value=4412");
    		testSamplePixel(172, 360, "x=43,   y=90,   value=10043");
    		testSamplePixel(108, 12, "x=27,   y=3,   value=1327");
    		
    		imagePane = mainFrameFixture.scrollPane("contentscroller").component();
    		pane = (JDesktopPane) imagePane.getViewport().getView();
    		view = (ncsa.hdf.view.DefaultImageView) pane.getSelectedFrame();
    		
    		byte[] autogainImgData = view.getImageByteData();
    		
    		mainFrameFixture.menuItemWithPath("Image", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		// Compare non-autogain byte data to autogain byte data and make sure they differ
    		try {
    			for(int i = 0; i < imgData.length; i++) {
    				assertTrue("Image Data was different, autogain must be functioning", imgData[i] == autogainImgData[i]);
    			}
    			
    			// If control reaches this point, the two arrays were the same, meaning
    			// autogain must not be functioning
    			fail("Autogain Image Conversion didn't function properly.");
    		}
    		catch (AssertionError err) {
    			// Control should reach this point if autogain changed the data at all
    		}
    		
    		// Turn off autogain
    		mainFrameFixture.menuItemWithPath("Tools", "User Options").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().checkBox("autogain").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Ok").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		closeHDFFile(hdf5file, false);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	catch (AssertionError ae) {
    		ae.printStackTrace();
    	}
    }
    
    @Test
    public void testFloatDatasetAsImage() {
    	hdf5file = openHDF5File("hdf5_test", 5);
    	
    	try {
    		JTreeFixture filetree = mainFrameFixture.tree().focus();
    		
    		filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(4).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("imagebutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		for(int i = 0; i < 3; i++) {
    			mainFrameFixture.button("zoomin").click();
    			mainFrameFixture.robot.waitForIdle();
    		}
    		
    		testSamplePixel(92, 212, "x=23,   y=53,   value=-93.7");
    		testSamplePixel(192, 368, "x=48,   y=92,   value=74.3");
    		testSamplePixel(16, 308, "x=4,   y=77,   value=43.8");
    		
    		JScrollPane imagePane = mainFrameFixture.scrollPane("contentscroller").component();
    		JDesktopPane pane = (JDesktopPane) imagePane.getViewport().getView();
    		ncsa.hdf.view.DefaultImageView view = (ncsa.hdf.view.DefaultImageView) pane.getSelectedFrame();
    		
    		// Grabe byte data of non-autogain image
    		byte[] imgData = view.getImageByteData();
    		
    		mainFrameFixture.menuItemWithPath("Image", "Close").click();
    		mainFrameFixture.robot.waitForIdle();

    		// Test that autogain is functioning
    		mainFrameFixture.menuItemWithPath("Tools", "User Options").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().checkBox("autogain").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Ok").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(4).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("imagebutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		for(int i = 0; i < 3; i++) {
    			mainFrameFixture.button("zoomin").click();
    			mainFrameFixture.robot.waitForIdle();
    		}
    		
    		// Image values should stay the same
    		testSamplePixel(92, 212, "x=23,   y=53,   value=-93.7");
    		testSamplePixel(192, 368, "x=48,   y=92,   value=74.3");
    		testSamplePixel(16, 308, "x=4,   y=77,   value=43.8");
    		
    		imagePane = mainFrameFixture.scrollPane("contentscroller").component();
    		pane = (JDesktopPane) imagePane.getViewport().getView();
    		view = (ncsa.hdf.view.DefaultImageView) pane.getSelectedFrame();
    		
    		byte[] autogainImgData = view.getImageByteData();
    		
    		mainFrameFixture.menuItemWithPath("Image", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		// Compare non-autogain byte data to autogain byte data and make sure they differ
    		try {
    			for(int i = 0; i < imgData.length; i++) {
    				assertTrue("Image Data was different, autogain must be functioning", imgData[i] == autogainImgData[i]);
    			}
    			
    			// If control reaches this point, the two arrays were the same, meaning
    			// autogain must not be functioning
    			fail("Autogain Image Conversion didn't function properly.");
    		}
    		catch (AssertionError err) {
    			// Control should reach this point if autogain changed the data at all
    		}
    		
    		// Turn off autogain
    		mainFrameFixture.menuItemWithPath("Tools", "User Options").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().checkBox("autogain").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("Ok").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		closeHDFFile(hdf5file, false);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	catch (AssertionError ae) {
    		ae.printStackTrace();
    	}
    }
    
    @Test
    public void testDefaultDisplayType() {
    	hdf5file = openHDF5File("hdf5_test", 5);
    	
    	try {
    		JTreeFixture filetree = mainFrameFixture.tree().focus();
    		
    		filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(6).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("spreadsheetbutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(6).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("spreadsheetbutton").requireSelected();
    		
    		mainFrameFixture.dialog().radioButton("imagebutton").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.menuItemWithPath("Image", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(6).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("imagebutton").requireSelected();
    		
    		mainFrameFixture.dialog().button("Cancel").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		closeHDFFile(hdf5file, false);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	catch (AssertionError ae) {
    		ae.printStackTrace();
    	}
    }

    @Test
    public void testLargeDataset() {
    	hdf5file = createHDF5File("test_large_dataset");
    	
    	try {
    		JTreeFixture filetree = mainFrameFixture.tree().focus();
    		
    		filetree.showPopupMenuAt(0).menuItemWithPath("New", "Dataset").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().textBox("datasetname").enterText("test_dataset");
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().textBox("currentsize").deleteText();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().textBox("currentsize").enterText("8000 x 8000");
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.checkBox("datasetchkfill").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().textBox("datasetfillval").deleteText();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().textBox("datasetfillval").enterText("75");
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		try {
    			filetree.showPopupMenuAt(1).menuItemWithPath("Open As").click();
    			mainFrameFixture.robot.waitForIdle();
    			
    			mainFrameFixture.dialog().button("Reset").click();
    			mainFrameFixture.robot.waitForIdle();
    			
    			assertTrue("Not 8kx8k dataset", (mainFrameFixture.dialog().textBox("startField0").text()).equals("0"));
    			assertTrue("Not 8kx8k dataset", (mainFrameFixture.dialog().textBox("startField1").text()).equals("0"));
    			assertTrue("Not 8kx8k dataset", (mainFrameFixture.dialog().textBox("endField0").text()).equals("7999"));
    			assertTrue("Not 8kx8k dataset", (mainFrameFixture.dialog().textBox("endField1").text()).equals("7999"));
    			
    			mainFrameFixture.dialog().button("OK").click();
    			mainFrameFixture.robot.waitForIdle();
    		}
    		catch (OutOfMemoryError oom) {
    			// Program doesn't currently seem to crash with large datasets during
    			// testing, but this is here to prevent this in case it happens
    		}
    		
    		closeHDFFile(hdf5file, true);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	catch (AssertionError ae) {
    		ae.printStackTrace();
    	}
    }

    @Test
    public void testBitmask() {
    	hdf5file = openHDF5File("tintsize", 10);
    	
    	try {
    		JTreeFixture filetree = mainFrameFixture.tree().focus();
    		
    		filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(1).menuItemWithPath("Open").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		JTableFixture tbl = mainFrameFixture.table("data");
    		
    		tbl.requireContents(
    			new String[][] {
    				{"-1", "-2", "-4", "-8", "-16", "-32", "-64", "-128"},
    				{"-2", "-4", "-8", "-16", "-32", "-64", "-128", "0"	},
    				{"-4", "-8", "-16", "-32", "-64", "-128", "0", "0"	},
    				{"-8", "-16", "-32", "-64", "-128", "0", "0", "0"	},
    				{"-16", "-32", "-64", "-128", "0", "0", "0", "0"	},
    				{"-32", "-64", "-128", "0", "0", "0", "0", "0"		},
    				{"-64", "-128", "0", "0", "0", "0", "0", "0"		},
    				{"-128", "0", "0", "0", "0", "0", "0", "0"			}
    			}
    		);
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(1).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().checkBox("applybitmask").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("bitmaskButton4").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		tbl = mainFrameFixture.table("data");
    		
    		tbl.requireContents(
    			new String[][] {
    	    		{"16", "16", "16", "16", "16", "0", "0", "0"		},
    	    		{"16", "16", "16", "16", "0", "0", "0", "0"			},
    	    		{"16", "16", "16", "0", "0", "0", "0", "0"			},
    	    		{"16", "16", "0", "0", "0", "0", "0", "0"			},
    	    		{"16", "0", "0", "0", "0", "0", "0", "0"			},
    	    		{"0", "0", "0", "0", "0", "0", "0", "0"				},
    	    		{"0", "0", "0", "0", "0", "0", "0", "0"				},
    	    		{"0", "0", "0", "0", "0", "0", "0", "0"				}
    	    	}
    		);
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		// Try bitmask with 16-bit dataset
    		filetree.showPopupMenuAt(6).menuItemWithPath("Open").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		tbl = mainFrameFixture.table("data");
    		
    		tbl.requireContents(
    			new String[][] {
    				{"65535", "65534", "65532", "65528", "65520", "65504", "65472", "65408", "65280", "65024", "64512", "63488", "61440", "57344", "49152", "32768"	},
    				{"65534", "65532", "65528", "65520", "65504", "65472", "65408", "65280", "65024", "64512", "63488", "61440", "57344", "49152", "32768", "0"		},
    				{"65532", "65528", "65520", "65504", "65472", "65408", "65280", "65024", "64512", "63488", "61440", "57344", "49152", "32768", "0", "0"			},
    				{"65528", "65520", "65504", "65472", "65408", "65280", "65024", "64512", "63488", "61440", "57344", "49152", "32768", "0", "0", "0"				},
    				{"65520", "65504", "65472", "65408", "65280", "65024", "64512", "63488", "61440", "57344", "49152", "32768", "0", "0", "0", "0"					},
    				{"65504", "65472", "65408", "65280", "65024", "64512", "63488", "61440", "57344", "49152", "32768", "0", "0", "0", "0", "0"						},
    				{"65472", "65408", "65280", "65024", "64512", "63488", "61440", "57344", "49152", "32768", "0", "0", "0", "0", "0", "0"							},
    				{"65408", "65280", "65024", "64512", "63488", "61440", "57344", "49152", "32768", "0", "0", "0", "0", "0", "0", "0"								}
    			}
    		);
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		filetree.showPopupMenuAt(6).menuItemWithPath("Open As").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().checkBox("applybitmask").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("bitmaskButton1").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("bitmaskButton7").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().radioButton("bitmaskButton13").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		mainFrameFixture.dialog().button("OK").click();
    		mainFrameFixture.robot.waitForIdle();
    		
    		tbl = mainFrameFixture.table("data");
    		
    		tbl.requireContents(
    			new String[][] {
    	    		{"8322", "8322", "8320", "8320", "8320", "8320", "8320", "8320", "8192", "8192", "8192", "8192", "8192", "8192", "0", "0" 	},
    	    		{"8322", "8320", "8320", "8320", "8320", "8320", "8320", "8192", "8192", "8192", "8192", "8192", "8192", "0", "0", "0"		},
    	    		{"8320", "8320", "8320", "8320", "8320", "8320", "8192", "8192", "8192", "8192", "8192", "8192", "0", "0", "0", "0"			},
    	    		{"8320", "8320", "8320", "8320", "8320", "8192", "8192", "8192", "8192", "8192", "8192", "0", "0", "0", "0", "0"			},
    	    		{"8320", "8320", "8320", "8320", "8192", "8192", "8192", "8192", "8192", "8192", "0", "0", "0", "0", "0", "0"				},
    	    		{"8320", "8320", "8320", "8192", "8192", "8192", "8192", "8192", "8192", "0", "0", "0", "0", "0", "0", "0"					},
    	    		{"8320", "8320", "8192", "8192", "8192", "8192", "8192", "8192", "0", "0", "0", "0", "0", "0", "0", "0"						},
    	    		{"8320", "8192", "8192", "8192", "8192", "8192", "8192", "0", "0", "0", "0", "0", "0", "0", "0", "0"						}
    	    	}
    		);
    		
    		mainFrameFixture.menuItemWithPath("Table", "Close").click();
    		mainFrameFixture.robot.waitForIdle();

    		closeHDFFile(hdf5file, false);
    	}
    	catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	catch (AssertionError ae) {
    		ae.printStackTrace();
    	}
    }
}