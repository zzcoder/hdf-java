package test.uitest;

import static org.fest.swing.data.TableCell.row;
import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JViewport;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.finder.JFileChooserFinder;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.fest.swing.fixture.JScrollPaneFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestHDFViewImageFrameSelection {
	private static FrameFixture mainFrameFixture;
	// the version of the HDFViewer
	private static String VERSION = "2.11";
	private static File hdf_file = null;

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
			assertTrue("openHDF5File filetree shows:", filetree.target.getRowCount() == initrows);
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

	private static void closeFile(File hdf_file, boolean delete_file) {
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
	public static void finishApplication(){
		// cleanup leftover files
		try {
			closeFile(hdf_file, false);
		} catch (Exception ex) {}

		mainFrameFixture.robot.waitForIdle();
		mainFrameFixture.cleanUp();
	}

	@Test
	public void testNextFrame() {
		hdf_file = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Open 3D THG image
			filetree.showPopupMenuAt(14).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			testSamplePixel(82, 36, "x=82,   y=36,   value=225");
			testSamplePixel(130, 21, "x=130,   y=21,   value=177");
			testSamplePixel(15, 63, "x=15,   y=63,   value=245");

			// Go to next frame
			mainFrameFixture.button("nextframebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").requireText("1");

			testSamplePixel(92, 34, "x=92,   y=34,   value=234");
			testSamplePixel(32, 57, "x=32,   y=57,   value=251");
			testSamplePixel(75, 14, "x=75,   y=14,   value=255");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			closeFile(hdf_file, false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}

	@Test
	public void testPreviousFrame() {
		hdf_file = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Open 3D THG image
			filetree.showPopupMenuAt(14).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").deleteText();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").enterText("6");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").pressAndReleaseKeys(10);
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.button("prevframebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").requireText("5");

			testSamplePixel(49, 28, "x=49,   y=28,   value=158");
			testSamplePixel(118, 42, "x=118,   y=42,   value=194");
			testSamplePixel(82, 5, "x=82,   y=5,   value=252");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			closeFile(hdf_file, false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}

	@Test
	public void testFirstFrame() {
		hdf_file = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Open 3D THG image
			filetree.showPopupMenuAt(14).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").deleteText();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").enterText("4");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").pressAndReleaseKeys(10);
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.button("firstframebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").requireText("0");

			testSamplePixel(132, 15, "x=132,   y=15,   value=247");
			testSamplePixel(12, 62, "x=12,   y=62,   value=252");
			testSamplePixel(70, 36, "x=70,   y=36,   value=250");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			closeFile(hdf_file, false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}

	@Test
	public void testLastFrame() {
		hdf_file = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Open 3D THG image
			filetree.showPopupMenuAt(14).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.button("lastframebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").requireText("7");

			testSamplePixel(75, 48, "x=75,   y=48,   value=156");
			testSamplePixel(92, 18, "x=92,   y=18,   value=194");
			testSamplePixel(121, 61, "x=121,   y=61,   value=252");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			closeFile(hdf_file, false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}

	@Test
	public void testEnterFrame() {
		hdf_file = openHDF5File("hdf5_test", 5);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
			mainFrameFixture.robot.waitForIdle();

			// Open 3D THG image
			filetree.showPopupMenuAt(14).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").deleteText();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").enterText("4");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("enterFrameField").pressAndReleaseKeys(10);
			mainFrameFixture.robot.waitForIdle();

			testSamplePixel(34, 17, "x=34,   y=17,   value=171");
			testSamplePixel(127, 52, "x=127,   y=52,   value=155");
			testSamplePixel(11, 62, "x=11,   y=62,   value=189");

			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();

			closeFile(hdf_file, false);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
	}
}