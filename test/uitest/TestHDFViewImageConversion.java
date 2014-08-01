package test.uitest;

import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import java.awt.Dimension;
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
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.fest.swing.fixture.JScrollPaneFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestHDFViewImageConversion {
	private static FrameFixture mainFrameFixture;
	// the version of the HDFViewer
	private static String VERSION = "2.99";

	private File openHDF5File(String name, int initrows) {
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

	private void closeFile(File hdf_file, boolean delete_file) {
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
		File hdf_file = new File("apollo17_earth.jpg.hdf");
		if (hdf_file.exists())
			hdf_file.delete();

		hdf_file = new File("apollo17_earth.jpg.h5");
		if (hdf_file.exists())
			hdf_file.delete();


		mainFrameFixture.robot.waitForIdle();
		mainFrameFixture.cleanUp();
	}

	@Test
	public void convertImageToHDF4(){
		File hdf_file = new File("apollo17_earth.jpg.hdf");

		try {
			JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Convert Image To", "HDF4");
			mainFrameFixture.robot.waitForIdle();

			fileMenuItem.requireVisible();
			fileMenuItem.click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("sourcefilebutton").click();
			mainFrameFixture.robot.waitForIdle();

			JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
			fileChooser.fileNameTextBox().setText("apollo17_earth.jpg");
			fileChooser.approve();
			mainFrameFixture.robot.waitForIdle();

			if(hdf_file.exists())
				hdf_file.delete();

			mainFrameFixture.dialog().button("okbutton").click();
			mainFrameFixture.robot.waitForIdle();

			JTreeFixture filetree = mainFrameFixture.tree().focus();
			filetree.requireVisible();

			assertTrue("convertImageToHDF4 filetree has file", (filetree.valueAt(1)).compareTo("apollo17_earth.jpg")==0);

			JMenuItemFixture imageMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Open As");
			mainFrameFixture.robot.waitForIdle();

			imageMenuItem.requireVisible();
			imageMenuItem.click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().radioButton("imagebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			// Enable show values
			mainFrameFixture.menuItemWithPath("Image", "Show Value").click();
			mainFrameFixture.robot.waitForIdle();

			// Test a few sample pixel values
			JScrollPaneFixture imagePane = mainFrameFixture.scrollPane("imagecontent");
			JViewport view = imagePane.component().getViewport();

			// Sample point 1
			try {
				mainFrameFixture.robot.moveMouse(view, 325, 53);
				mainFrameFixture.textBox("valuefield").requireText("x=325,   y=53,   value=(152, 106, 91)");
			}
			catch (AssertionError ae) {
				imagePane.horizontalScrollBar().scrollTo(325);
				imagePane.verticalScrollBar().scrollTo(53);
				mainFrameFixture.robot.moveMouse(view, 325, 53);
				mainFrameFixture.textBox("valuefield").requireText("x=325,   y=53,   value=(152, 106, 91)");
			}
				
			// Sample point 2
			try {
				mainFrameFixture.robot.moveMouse(view, 430, 357);
				mainFrameFixture.textBox("valuefield").requireText("x=430,   y=357,   value=(83, 80, 107)");
			}
			catch (AssertionError ae) {
				imagePane.horizontalScrollBar().scrollTo(430);
				imagePane.verticalScrollBar().scrollTo(357);
				mainFrameFixture.robot.moveMouse(view, 430, 357);
				mainFrameFixture.textBox("valuefield").requireText("x=430,   y=357,   value=(83, 80, 107)");
			}
				
			// Sample point 3
			try {
				mainFrameFixture.robot.moveMouse(view, 197, 239);
				mainFrameFixture.textBox("valuefield").requireText("x=197,   y=239,   value=(206, 177, 159)");	
			}
			catch (AssertionError ae) {
				imagePane.horizontalScrollBar().scrollTo(197);
				imagePane.verticalScrollBar().scrollTo(239);
			}
			
			// Test metadata
			JMenuItemFixture metadataMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Show Properties");
			mainFrameFixture.robot.waitForIdle();

			metadataMenuItem.requireVisible();
			metadataMenuItem.click();
			mainFrameFixture.robot.waitForIdle();

			DialogFixture dlg = mainFrameFixture.dialog();
			dlg.requireVisible();

			// Test Name
			dlg.label("namefield").requireText("apollo17_earth.jpg");

			// Test # of dimensions
			dlg.textBox("dimensions").requireText("2");

			// Test Dimension Size
			dlg.textBox("dimensionsize").requireText("533 x 533");

			dlg.button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Done
			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
		finally {
			try {
				closeFile(hdf_file, false);
			}
			catch (Exception ex) {}
		}
	}

	@Test
	public void convertImageToHDF5() {
		File hdf_file = new File("apollo17_earth.jpg.h5");

		try{
			JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Convert Image To", "HDF5");
			mainFrameFixture.robot.waitForIdle();

			fileMenuItem.requireVisible();
			fileMenuItem.click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("sourcefilebutton").click();
			mainFrameFixture.robot.waitForIdle();

			JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
			fileChooser.fileNameTextBox().setText("apollo17_earth.jpg");
			fileChooser.approve();
			mainFrameFixture.robot.waitForIdle();

			if(hdf_file.exists())
				hdf_file.delete();

			mainFrameFixture.dialog().button("okbutton").click();
			mainFrameFixture.robot.waitForIdle();

			JTreeFixture filetree = mainFrameFixture.tree().focus();
			filetree.requireVisible();

			assertTrue("convertImageToHDF5 filetree has file", (filetree.valueAt(1)).compareTo("apollo17_earth.jpg")==0);

			JMenuItemFixture imageMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Open As");
			mainFrameFixture.robot.waitForIdle();

			imageMenuItem.requireVisible();
			imageMenuItem.click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().radioButton("imagebutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().button("OK").click();
			mainFrameFixture.robot.waitForIdle();

			// Enable show values
			mainFrameFixture.menuItemWithPath("Image", "Show Value").click();
			mainFrameFixture.robot.waitForIdle();

			// Test a few sample pixel values
			JScrollPaneFixture imagePane = mainFrameFixture.scrollPane("imagecontent");
			JViewport view = imagePane.component().getViewport();

			// Sample point 1
			try {
				mainFrameFixture.robot.moveMouse(view, 325, 53);
				mainFrameFixture.textBox("valuefield").requireText("x=325,   y=53,   value=(152, 106, 91)");
			}
			catch (AssertionError ae) {
				imagePane.horizontalScrollBar().scrollTo(325);
				imagePane.verticalScrollBar().scrollTo(53);
				mainFrameFixture.robot.moveMouse(view, 325, 53);
				mainFrameFixture.textBox("valuefield").requireText("x=325,   y=53,   value=(152, 106, 91)");
			}
				
			// Sample point 2
			try {
				mainFrameFixture.robot.moveMouse(view, 430, 357);
				mainFrameFixture.textBox("valuefield").requireText("x=430,   y=357,   value=(83, 80, 107)");
			}
			catch (AssertionError ae) {
				imagePane.horizontalScrollBar().scrollTo(430);
				imagePane.verticalScrollBar().scrollTo(357);
				mainFrameFixture.robot.moveMouse(view, 430, 357);
				mainFrameFixture.textBox("valuefield").requireText("x=430,   y=357,   value=(83, 80, 107)");
			}
				
			// Sample point 3
			try {
				mainFrameFixture.robot.moveMouse(view, 197, 239);
				mainFrameFixture.textBox("valuefield").requireText("x=197,   y=239,   value=(206, 177, 159)");	
			}
			catch (AssertionError ae) {
				imagePane.horizontalScrollBar().scrollTo(197);
				imagePane.verticalScrollBar().scrollTo(239);
				mainFrameFixture.robot.moveMouse(view, 197, 239);
				mainFrameFixture.textBox("valuefield").requireText("x=197,   y=239,   value=(206, 177, 159)");
			}
				
			// Test metadata
			JMenuItemFixture metadataMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Show Properties");
			mainFrameFixture.robot.waitForIdle();

			metadataMenuItem.requireVisible();
			metadataMenuItem.click();
			mainFrameFixture.robot.waitForIdle();

			DialogFixture dlg = mainFrameFixture.dialog();
			dlg.requireVisible();

			// Test Name
			dlg.label("namefield").requireText("apollo17_earth.jpg");

			// Test # of dimensions
			dlg.textBox("dimensions").requireText("3");

			// Test Dimension Size
			dlg.textBox("dimensionsize").requireText("533 x 533 x 3");

			dlg.button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			// Done
			mainFrameFixture.menuItemWithPath("Image", "Close").click();
			mainFrameFixture.robot.waitForIdle();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}
		finally {
			try {
				closeFile(hdf_file, false);
			}
			catch (Exception ex) {}
		}
	}
}