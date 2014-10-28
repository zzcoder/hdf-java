package test.uitest;

import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.JFrame;

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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestHDFViewLibBounds {
	private static FrameFixture mainFrameFixture;
	// the version of the HDFViewer
	private static String VERSION = "2.11";

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

	private File createFile(String name, boolean hdf4_type) {
		String file_ext;
		String file_type;
		if (hdf4_type) {
			file_ext = new String(".hdf");
			file_type = new String("HDF4");
		}
		else {
			file_ext = new String(".h5");
			file_type = new String("HDF5");
		}

		File hdf_file = new File(name + file_ext);
		if (hdf_file.exists())
			hdf_file.delete();

		try {
			JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "New", file_type);
			mainFrameFixture.robot.waitForIdle();
			fileMenuItem.requireVisible();
			fileMenuItem.click();
			mainFrameFixture.robot.waitForIdle();

			JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
			fileChooser.fileNameTextBox().setText(name + file_ext);
			fileChooser.approve();
			mainFrameFixture.robot.waitForIdle();

			assertTrue("File-" + file_type + " file created", hdf_file.exists());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch (AssertionError ae) {
			ae.printStackTrace();
		}

		return hdf_file;
	}

	private File createHDF5File(String name) {
		return createFile(name, false);
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
		File hdf_file = new File("test_libversion.h5");
		if (hdf_file.exists())
			hdf_file.delete();


		mainFrameFixture.robot.waitForIdle();
		mainFrameFixture.cleanUp();
	}

	@Test
	public void testLibVersion() {
		File hdf_file = createHDF5File("test_libversion");

		try {
			closeFile(hdf_file, false);

			mainFrameFixture.button("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
			fileChooser.fileNameTextBox().setText("test_libversion.h5");
			mainFrameFixture.robot.waitForIdle();
			
			fileChooser.approve();
			mainFrameFixture.robot.waitForIdle();

			JTreeFixture filetree = mainFrameFixture.tree().focus();
			assertTrue("Button-Open-HDF5 filetree shows:", filetree.target.getRowCount() == 1);
			assertTrue("Button-Open-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("test_libversion.h5") == 0);

			filetree.showPopupMenuAt(0).menuItemWithPath("Set Lib version bounds").click();
			mainFrameFixture.robot.waitForIdle();

			// Test Earliest and Latest
			mainFrameFixture.dialog().comboBox("earliestversion").selectItem("Earliest");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Show Properties").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().label("libverbound").requireText("Earliest and Latest");

			mainFrameFixture.dialog().button("Close").click();
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Set Lib version bounds").click();
			mainFrameFixture.robot.waitForIdle();

			// Test Latest and Latest
			mainFrameFixture.dialog().comboBox("earliestversion").selectItem("Latest");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().optionPane().component().setValue("Ok");
			mainFrameFixture.robot.waitForIdle();

			filetree.showPopupMenuAt(0).menuItemWithPath("Show Properties").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.dialog().label("libverbound").requireText("Latest and Latest");

			mainFrameFixture.dialog().button("Close").click();
			mainFrameFixture.robot.waitForIdle();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		catch(AssertionError ae) {
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
