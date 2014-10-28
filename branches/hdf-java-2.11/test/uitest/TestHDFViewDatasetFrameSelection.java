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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestHDFViewDatasetFrameSelection {
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
		hdf_file = openHDF5File("tframeselection", 2);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			// Open test_dataset
			filetree.showPopupMenuAt(1).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tblFix = mainFrameFixture.table("data");

			try {
				mainFrameFixture.textBox("framenumber").requireText("0");
			} catch (AssertionError ae) {
				mainFrameFixture.textBox("framenumber").deleteText();
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").enterText("0");
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
				mainFrameFixture.robot.waitForIdle();
			}

			JTableCellFixture cellFix = tblFix.cell(row(4).column(2));
			cellFix.requireValue("478");

			cellFix = tblFix.cell(row(1).column(3));
			cellFix.requireValue("52");

			mainFrameFixture.button("nextbutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("framenumber").requireText("1");

			cellFix = tblFix.cell(row(3).column(4));
			cellFix.requireValue("454");

			cellFix = tblFix.cell(row(2).column(1));
			cellFix.requireValue("984");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
		hdf_file = openHDF5File("tframeselection", 2);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			// Open test_dataset
			filetree.showPopupMenuAt(1).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tblFix = mainFrameFixture.table("data");

			try {
				mainFrameFixture.textBox("framenumber").requireText("1");
			} catch (AssertionError ae) {
				mainFrameFixture.textBox("framenumber").deleteText();
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").enterText("1");
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
				mainFrameFixture.robot.waitForIdle();
			}

			JTableCellFixture cellFix = tblFix.cell(row(3).column(1));
			cellFix.requireValue("6");

			cellFix = tblFix.cell(row(4).column(2));
			cellFix.requireValue("215");

			mainFrameFixture.button("prevbutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("framenumber").requireText("0");

			cellFix = tblFix.cell(row(0).column(0));
			cellFix.requireValue("13");

			cellFix = tblFix.cell(row(4).column(4));
			cellFix.requireValue("4");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
		hdf_file = openHDF5File("tframeselection", 2);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			// Open test_dataset
			filetree.showPopupMenuAt(1).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tblFix = mainFrameFixture.table("data");

			try {
				mainFrameFixture.textBox("framenumber").requireText("3");
			} catch (AssertionError ae) {
				mainFrameFixture.textBox("framenumber").deleteText();
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").enterText("3");
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
				mainFrameFixture.robot.waitForIdle();
			}

			JTableCellFixture cellFix = tblFix.cell(row(3).column(1));
			cellFix.requireValue("456");

			cellFix = tblFix.cell(row(0).column(2));
			cellFix.requireValue("7");

			mainFrameFixture.button("firstbutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("framenumber").requireText("0");

			cellFix = tblFix.cell(row(3).column(4));
			cellFix.requireValue("52");

			cellFix = tblFix.cell(row(4).column(1));
			cellFix.requireValue("345");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
		hdf_file = openHDF5File("tframeselection", 2);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			// Open test_dataset
			filetree.showPopupMenuAt(1).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tblFix = mainFrameFixture.table("data");

			try {
				mainFrameFixture.textBox("framenumber").requireText("0");
			} catch (AssertionError ae) {
				mainFrameFixture.textBox("framenumber").deleteText();
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").enterText("0");
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
				mainFrameFixture.robot.waitForIdle();
			}

			JTableCellFixture cellFix = tblFix.cell(row(2).column(3));
			cellFix.requireValue("63");

			cellFix = tblFix.cell(row(1).column(0));
			cellFix.requireValue("2");

			mainFrameFixture.button("lastbutton").click();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("framenumber").requireText("4");

			cellFix = tblFix.cell(row(0).column(4));
			cellFix.requireValue("789");

			cellFix = tblFix.cell(row(4).column(1));
			cellFix.requireValue("7945");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
		hdf_file = openHDF5File("tframeselection", 2);

		try {
			JTreeFixture filetree = mainFrameFixture.tree().focus();

			// Open test_dataset
			filetree.showPopupMenuAt(1).menuItemWithPath("Open").click();
			mainFrameFixture.robot.waitForIdle();

			JTableFixture tblFix = mainFrameFixture.table("data");

			try {
				mainFrameFixture.textBox("framenumber").requireText("0");
			} catch (AssertionError ae) {
				mainFrameFixture.textBox("framenumber").deleteText();
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").enterText("0");
				mainFrameFixture.robot.waitForIdle();

				mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
				mainFrameFixture.robot.waitForIdle();
			}

			JTableCellFixture cellFix = tblFix.cell(row(3).column(2));
			cellFix.requireValue("99");

			cellFix = tblFix.cell(row(4).column(3));
			cellFix.requireValue("86");

			mainFrameFixture.textBox("framenumber").deleteText();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("framenumber").enterText("3");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
			mainFrameFixture.robot.waitForIdle();

			cellFix = tblFix.cell(row(2).column(1));
			cellFix.requireValue("63");

			cellFix = tblFix.cell(row(0).column(2));
			cellFix.requireValue("7");

			mainFrameFixture.textBox("framenumber").deleteText();
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("framenumber").enterText("2");
			mainFrameFixture.robot.waitForIdle();

			mainFrameFixture.textBox("framenumber").pressAndReleaseKeys(10);
			mainFrameFixture.robot.waitForIdle();

			cellFix = tblFix.cell(row(2).column(1));
			cellFix.requireValue("88");

			cellFix = tblFix.cell(row(0).column(2));
			cellFix.requireValue("66");

			mainFrameFixture.menuItemWithPath("Table", "Close").click();
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