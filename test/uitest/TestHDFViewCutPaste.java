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


public class TestHDFViewCutPaste {
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
		mainFrameFixture.robot.waitForIdle();
		//mainFrameFixture.requireNotVisible();
		mainFrameFixture.cleanUp();
	}

	@Test
	public void testCutPasteDatasets() {
		hdf5file = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			assertTrue("Filetree shows ", (filetree.valueAt(0)).compareTo("hdf5_test.h5") == 0);

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(4).menuItemWithPath("Cut").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(11).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Make sure object was actually moved
			assertFalse("Filetree has ", (filetree.valueAt(4)).compareTo("2D float array") == 0);
			assertTrue("Filetree has ", (filetree.valueAt(11)).compareTo("2D float array") == 0);

			// Check data content
			filetree.showPopupMenuAt(11).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tbl = mainFrameFixture.table("data");
			JTableCellFixture cell = tbl.cell(row(52).column(37));
			cell.requireValue("-88.3");
			cell = tbl.cell(row(27).column(43));
			cell.requireValue("-64.9");
			cell = tbl.cell(row(85).column(2));
			cell.requireValue("99.6");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check image content
			filetree.showPopupMenuAt(11).menuItemWithPath("Open As").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().radioButton("imagebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			// Zoom in for clarity
			for(int i = 0; i < 6; i++){
				mainFrameFixture.button("zoomin").click();
				mainFrameFixture.robot.waitForIdle();
			}

			testSamplePixel(133, 441, "x=19,   y=63,   value=-85.3");
			testSamplePixel(42, 658, "x=6,   y=94,   value=-97.8");
			testSamplePixel(224, 532, "x=32,   y=76,   value=69.5");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check metadata
			filetree.showPopupMenuAt(11).menuItemWithPath("Show Properties").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().label("namefield").requireText("2D float array");
			mainFrameFixture.dialog().textBox("dimensions").requireText("2");
			mainFrameFixture.dialog().textBox("dimensionsize").requireText("100 x 50");

			JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
			tabPane.requireVisible();
			tabPane.requireTabTitles("General","Attributes");
			tabPane.selectTab("Attributes");
			mainFrameFixture.robot.waitForIdle();

			tabPane.requireVisible();
			JTableFixture attrTable = mainFrameFixture.dialog().table("attributes");
			attrTable.requireRowCount(1);

			mainFrameFixture.dialog().button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Reset file hdf5_test to how it was
			filetree.showPopupMenuAt(11).menuItemWithPath("Cut").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(2).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			assertTrue("Filetree has ", (filetree.valueAt(4)).compareTo("2D float array") == 0);

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
	public void testCutPasteGroup() {
		hdf5file = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();
			assertTrue("Filetree shows ", (filetree.valueAt(0)).compareTo("hdf5_test.h5") == 0);

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(2).menuItemWithPath("Cut").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(11).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Make sure group was actually moved
			assertFalse("Filetree has", (filetree.valueAt(2)).compareTo("arrays") == 0);
			assertTrue("Filetree has", (filetree.valueAt(4)).compareTo("arrays") == 0);
			assertTrue("Filetree has", (filetree.valueAt(5)).compareTo("1D String") == 0);
			assertTrue("Filetree has", (filetree.valueAt(6)).compareTo("2D float array") == 0);
			assertTrue("Filetree has", (filetree.valueAt(7)).compareTo("2D int array") == 0);
			assertTrue("Filetree has", (filetree.valueAt(8)).compareTo("2D int8x9") == 0);
			assertTrue("Filetree has", (filetree.valueAt(9)).compareTo("3D int array") == 0);
			assertTrue("Filetree has", (filetree.valueAt(10)).compareTo("4D int") == 0);
			assertTrue("Filetree has", (filetree.valueAt(11)).compareTo("ArrayOfStructures") == 0);
			assertTrue("Filetree has", (filetree.valueAt(12)).compareTo("Vdata with mixed types") == 0);

			// Check data content
			filetree.showPopupMenuAt(10).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tbl = mainFrameFixture.table("data");
			JTableCellFixture cell = tbl.cell(row(3).column(2));
			cell.requireValue("1143");

			mainFrameFixture.button("nextbutton").click();
			mainFrameFixture.robot.waitForIdle();

			cell = tbl.cell(row(0).column(3));
			cell.requireValue("1214");

			mainFrameFixture.button("nextbutton").click();
			mainFrameFixture.robot.waitForIdle();

			cell = tbl.cell(row(2).column(0));
			cell.requireValue("1331");

			mainFrameFixture.button("firstbutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check image content
			filetree.showPopupMenuAt(6).menuItemWithPath("Open As").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().radioButton("imagebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			for (int i = 0; i < 6; i++) {
				mainFrameFixture.button("zoomin").click();
				mainFrameFixture.robot.waitForIdle();
			}

			testSamplePixel(189, 560, "x=27,   y=80,   value=0.0");
			testSamplePixel(294, 70, "x=42,   y=10,   value=-50.0");
			testSamplePixel(322, 539, "x=46,   y=77,   value=-48.5");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check metadata
			filetree.showPopupMenuAt(11).menuItemWithPath("Show Properties").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().label("namefield").requireText("ArrayOfStructures");
			mainFrameFixture.dialog().textBox("dimensions").requireText("2");
			mainFrameFixture.dialog().textBox("dimensionsize").requireText("3 x 10");

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


			// Reset file hdf5_test to how it was
			filetree.showPopupMenuAt(4).menuItemWithPath("Cut").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			assertTrue("Filetree has", (filetree.valueAt(2)).compareTo("arrays") == 0);

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
	public void testCutPasteImage() {
		hdf5file = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();
			assertTrue("Filetree shows ", (filetree.valueAt(0)).compareTo("hdf5_test.h5") == 0);

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(18).menuItemWithPath("Cut").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Make sure image was actually moved
			assertFalse("Filetree has ", (filetree.valueAt(18)).compareTo("pixel interlace") == 0);
			assertTrue("Filetree has ", (filetree.valueAt(19)).compareTo("pixel interlace") == 0);

			// Check image content
			filetree.showPopupMenuAt(19).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			for (int i = 0; i < 2; i++) {
				mainFrameFixture.button("zoomin").click();
				mainFrameFixture.robot.waitForIdle();
			}

			testSamplePixel(450, 321, "x=150,   y=107,   value=(125, 58, 42)");
			testSamplePixel(282, 405, "x=94,   y=135,   value=(216, 174, 150)");
			testSamplePixel(528, 195, "x=176,   y=65,   value=(253, 134, 140)");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check data content
			filetree.showPopupMenuAt(19).menuItemWithPath("Open As").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().radioButton("spreadsheetbutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tbl = mainFrameFixture.table("data");
			JTableCellFixture cell = tbl.cell(row(102).column(57));
			cell.requireValue("223");

			mainFrameFixture.button("nextbutton").click();
			mainFrameFixture.robot.waitForIdle();

			cell = tbl.cell(row(89).column(213));
			cell.requireValue("140");

			mainFrameFixture.button("nextbutton").click();
			mainFrameFixture.robot.waitForIdle();

			cell = tbl.cell(row(27).column(172));
			cell.requireValue("105");

			mainFrameFixture.button("firstbutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Check metadata
			filetree.showPopupMenuAt(19).menuItemWithPath("Show Properties").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().label("namefield").requireText("pixel interlace");
			mainFrameFixture.dialog().textBox("dimensions").requireText("3");
			mainFrameFixture.dialog().textBox("dimensionsize").requireText("149 x 227 x 3");

			JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
			tabPane.requireVisible();
			tabPane.requireTabTitles("General","Attributes");
			tabPane.selectTab("Attributes");
			mainFrameFixture.robot.waitForIdle();

			tabPane.requireVisible();
			JTableFixture attrTable = mainFrameFixture.dialog().table("attributes");
			attrTable.requireRowCount(5);

			mainFrameFixture.dialog().button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(19).menuItemWithPath("Cut").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(13).menuItemWithPath("Paste").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			assertTrue("Filetree has ", (filetree.valueAt(18)).compareTo("pixel interlace") == 0);

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