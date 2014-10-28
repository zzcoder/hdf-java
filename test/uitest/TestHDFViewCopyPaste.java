package test.uitest;

import static org.fest.swing.data.TableCell.row;
import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.regex.Pattern;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JViewport;

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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class TestHDFViewCopyPaste {
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
		File hdf_file = new File("testcopypaste.h5");
		if(hdf_file.exists())
			hdf_file.delete();

		mainFrameFixture.robot.waitForIdle();
		//mainFrameFixture.requireNotVisible();
		mainFrameFixture.cleanUp();
	}

	@Test
	public void testCopyPasteDatasetInSameFile() {
		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();
			JFileChooserFixture fileChooser;

			mainFrameFixture.button("Open").click();
			mainFrameFixture.robot.waitForIdle();

			fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
			fileChooser.fileNameTextBox().setText("hdf5_test.h5");
			fileChooser.approve();
			mainFrameFixture.robot.waitForIdle();

			assertTrue("openHDF5File filetree has file",(filetree.valueAt(0)).compareTo("hdf5_test.h5") == 0);

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			try {
				assertFalse("filetree has group ", (filetree.valueAt(20)).compareTo("test_group")==0);
			}
			catch (AssertionError ae) {
				// Test group already exists, delete to ensure test functions as expected
				filetree.showPopupMenuAt(20).menuItemWithPath("Delete").click();
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.dialog().optionPane().component().setValue("Ok");
				mainFrameFixture.robot.waitForIdle();
			}
			catch (IndexOutOfBoundsException ex) {
				// Group didn't exist and test tried to access a non-valid row element
			}

			filetree.showPopupMenuAt(6).menuItemWithPath("Copy").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("New", "Group").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().textBox("groupname").enterText("test_group");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().comboBox("groupparent").selectItem("/");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(20).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Check copied dataset data content
			filetree.showPopupMenuAt(21).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tbl = mainFrameFixture.table("data");
			JTableCellFixture cell = tbl.cell(row(5).column(6));
			cell.requireValue("67");
			cell = tbl.cell(row(2).column(4));
			cell.requireValue("35");
			cell = tbl.cell(row(0).column(8));
			cell.requireValue("19");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check image view content
			filetree.showPopupMenuAt(21).menuItemWithPath("Open As").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().radioButton("imagebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			// Zoom in for clarity
			for(int i = 0; i < 7; i++){
				mainFrameFixture.button("zoomin").click();
				mainFrameFixture.robot.waitForIdle();
			}

			testSamplePixel(32, 40, "x=4,   y=5,   value=65");
			testSamplePixel(8, 48, "x=1,   y=6,   value=72");
			testSamplePixel(64, 32, "x=8,   y=4,   value=59");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check metadata
			filetree.showPopupMenuAt(21).menuItemWithPath("Show Properties").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().label("namefield").requireText("2D int8x9");
			mainFrameFixture.dialog().textBox("dimensions").requireText("2");
			mainFrameFixture.dialog().textBox("dimensionsize").requireText("8 x 9");

			JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
			tabPane.requireVisible();
			tabPane.requireTabTitles("General","Attributes");
			tabPane.selectTab("Attributes");
			mainFrameFixture.robot.waitForIdle();

			tabPane.requireVisible();
			JTableFixture attrTable = mainFrameFixture.dialog().table("attributes");
			attrTable.requireRowCount(0);

			mainFrameFixture.dialog().button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(20).menuItemWithPath("Delete").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			closeHDFFile(new File("hdf5_test.h5"), false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}

	@Test
	public void testCopyPasteDatasetInDifferentFile() {
		File hdf5_test = openHDF5File("hdf5_test", 5);
		File hdf_file = createHDF5File("testcopypaste");

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			assertTrue("openHDF5File filetree has file",(filetree.valueAt(0)).compareTo("hdf5_test.h5") == 0);
			assertTrue("openHDF5File filetree has file", (filetree.valueAt(5)).compareTo("testcopypaste.h5") == 0);

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(5).menuItemWithPath("Copy").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(20).menuItemWithPath("New", "Group").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().textBox("groupname").enterText("test_group");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().comboBox("groupparent").selectItem("/");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(21).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(21).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Check copied dataset data content
			filetree.showPopupMenuAt(22).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tbl = mainFrameFixture.table("data");
			JTableCellFixture cell = tbl.cell(row(57).column(32));
			cell.requireValue("6732");

			cell = tbl.cell(row(95).column(43));
			cell.requireValue("10543");

			cell = tbl.cell(row(16).column(22));
			cell.requireValue("2622");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check image view content
			filetree.showPopupMenuAt(22).menuItemWithPath("Open As").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().radioButton("imagebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			// Zoom in for clarity
			for(int i = 0; i < 7; i++){
				mainFrameFixture.button("zoomin").click();
				mainFrameFixture.robot.waitForIdle();
			}

			testSamplePixel(256, 56, "x=32,   y=7,   value=1732");
			testSamplePixel(96, 200, "x=12,   y=25,   value=3512");
			testSamplePixel(336, 688, "x=42,   y=86,   value=9642");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check metadata
			filetree.showPopupMenuAt(22).menuItemWithPath("Show Properties").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().label("namefield").requireText("2D int array");
			mainFrameFixture.dialog().textBox("dimensions").requireText("2");
			mainFrameFixture.dialog().textBox("dimensionsize").requireText("100 x 50");

			JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
			tabPane.requireVisible();
			tabPane.requireTabTitles("General","Attributes");
			tabPane.selectTab("Attributes");
			mainFrameFixture.robot.waitForIdle();

			tabPane.requireVisible();
			JTableFixture attrTable = mainFrameFixture.dialog().table("attributes");
			attrTable.requireRowCount(0);

			mainFrameFixture.dialog().button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(21).menuItemWithPath("Delete").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			closeHDFFile(hdf5_test, false);
			closeHDFFile(hdf_file, true);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}

	@Test
	public void testCopyPasteGroupInSameFile() {
		File hdf5_test = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			assertTrue("Filetree has file ", (filetree.valueAt(0)).compareTo("hdf5_test.h5") == 0);

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(13).menuItemWithPath("Copy").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			assertTrue("Filetree has group ", (filetree.valueAt(20)).compareTo("images~copy") == 0);
			assertTrue("Filetree has object ", (filetree.valueAt(21)).compareTo("3D THG") == 0);
			assertTrue("Filetree has object ", (filetree.valueAt(22)).compareTo("Iceberg") == 0);
			assertTrue("Filetree has object ", (filetree.valueAt(23)).compareTo("hst_lagoon_detail.jpg") == 0);
			assertTrue("Filetree has object ", (filetree.valueAt(24)).compareTo("iceberg_palette") == 0);
			assertTrue("Filetree has object ", (filetree.valueAt(25)).compareTo("pixel interlace") == 0);
			assertTrue("Filetree has object ", (filetree.valueAt(26)).compareTo("plane interlace") == 0);

			filetree.showPopupMenuAt(23).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			testSamplePixel(220, 174, "x=220,   y=174,   value=(52, 28, 16)");
			testSamplePixel(524, 276, "x=524,   y=276,   value=(146, 165, 169)");
			testSamplePixel(372, 461, "x=372,   y=461,   value=(21, 15, 29)");

			// Test metadata
			JMenuItemFixture metadataMenuItem = filetree.showPopupMenuAt(23).menuItemWithPath("Show Properties");
			mainFrameFixture.robot.waitForIdle();

			metadataMenuItem.requireVisible();
			metadataMenuItem.click();
			mainFrameFixture.robot.waitForIdle();

			DialogFixture dlg = mainFrameFixture.dialog();
			dlg.requireVisible();

			// Test Name
			dlg.label("namefield").requireText("hst_lagoon_detail.jpg");

			// Test # of dimensions
			dlg.textBox("dimensions").requireText("3");

			// Test Dimension Size
			dlg.textBox("dimensionsize").requireText("610 x 600 x 3");

			JTabbedPaneFixture tabPane = dlg.tabbedPane();
			tabPane.requireVisible();
			tabPane.requireTabTitles("General","Attributes");
			tabPane.selectTab("Attributes");
			mainFrameFixture.robot.waitForIdle();

			tabPane.requireVisible();
			JTableFixture attrTable = mainFrameFixture.dialog().table("attributes");
			attrTable.requireRowCount(5);

			dlg.button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Done
			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(20).menuItemWithPath("Delete").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			closeHDFFile(hdf5_test, false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}

	@Test
	public void testCopyPasteGroupInDifferentFile() {
		File hdf5_test = openHDF5File("hdf5_test", 5);
		File hdf_file = createHDF5File("testcopypaste");

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			assertTrue("Filetree shows ", (filetree.valueAt(0)).compareTo("hdf5_test.h5") == 0);
			assertTrue("Filetree has group ", (filetree.valueAt(5)).compareTo("testcopypaste.h5") == 0);

			// Copy group from hdf5_test.h5 into testcopypaste.h5
			filetree.showPopupMenuAt(2).menuItemWithPath("Copy").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(5).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			// Two reloads keeps hdf5_test.h5 at the top of the filetree since two files are open
			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(20).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Check that all datasets made it
			assertTrue("Filetree has file ", (filetree.valueAt(21)).compareTo("arrays") == 0);
			assertTrue("Filetree has file ", (filetree.valueAt(22)).compareTo("1D String") == 0);
			assertTrue("Filetree has file ", (filetree.valueAt(23)).compareTo("2D float array") == 0);
			assertTrue("Filetree has file ", (filetree.valueAt(24)).compareTo("2D int array") == 0);
			assertTrue("Filetree has file ", (filetree.valueAt(25)).compareTo("2D int8x9") == 0);
			assertTrue("Filetree has file ", (filetree.valueAt(26)).compareTo("3D int array") == 0);
			assertTrue("Filetree has file ", (filetree.valueAt(27)).compareTo("4D int") == 0);
			assertTrue("Filetree has file ", (filetree.valueAt(28)).compareTo("ArrayOfStructures") == 0);
			assertTrue("Filetree has file ", (filetree.valueAt(29)).compareTo("Vdata with mixed types") == 0);

			filetree.showPopupMenuAt(25).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tbl = mainFrameFixture.table("data");
			JTableCellFixture cell = tbl.cell(row(3).column(4));
			cell.requireValue("45");
			cell = tbl.cell(row(7).column(1));
			cell.requireValue("82");
			cell = tbl.cell(row(2).column(8));
			cell.requireValue("39");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check image view content
			filetree.showPopupMenuAt(25).menuItemWithPath("Open As").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().radioButton("imagebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			// Zoom in for clarity
			for(int i = 0; i < 7; i++){
				mainFrameFixture.button("zoomin").click();
				mainFrameFixture.robot.waitForIdle();
			}

			testSamplePixel(48, 40, "x=6,   y=5,   value=67");
			testSamplePixel(16, 32, "x=2,   y=4,   value=53");
			testSamplePixel(24, 56, "x=3,   y=7,   value=84");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check metadata
			filetree.showPopupMenuAt(25).menuItemWithPath("Show Properties").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().label("namefield").requireText("2D int8x9");
			mainFrameFixture.dialog().textBox("dimensions").requireText("2");
			mainFrameFixture.dialog().textBox("dimensionsize").requireText("8 x 9");

			JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
			tabPane.requireVisible();
			tabPane.requireTabTitles("General","Attributes");
			tabPane.selectTab("Attributes");
			mainFrameFixture.robot.waitForIdle();

			tabPane.requireVisible();
			JTableFixture attrTable = mainFrameFixture.dialog().table("attributes");
			attrTable.requireRowCount(0);

			mainFrameFixture.dialog().button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(21).menuItemWithPath("Delete").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			closeHDFFile(hdf5_test, false);
			closeHDFFile(hdf_file, true);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}
}