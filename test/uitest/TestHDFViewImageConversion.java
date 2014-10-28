package test.uitest;

import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

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
import org.fest.swing.fixture.JScrollPaneFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestHDFViewImageConversion {
    private static FrameFixture mainFrameFixture;
    // the version of the HDFViewer
    private static String VERSION = "2.11";
    private static String JPGFILE = "apollo17_earth.jpg";
    private static String HDF4IMAGE = JPGFILE+".hdf";
    private static String HDF5IMAGE = JPGFILE+".h5";

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
    
    private void closeFile(File hdf_file, boolean delete_file) {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Close All");
            mainFrameFixture.robot.waitForIdle();

            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            if (delete_file) {
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
                return frame.getTitle().equals("HDFView " + VERSION) && frame.isShowing();
            }
        }).withTimeout(10000).using(robot);
        mainFrameFixture.robot.waitForIdle();
        mainFrameFixture.requireVisible();
    }

    @AfterClass
    public static void finishApplication() {
        // cleanup leftover files
        File hdf_file = new File(HDF4IMAGE);
        if (hdf_file.exists())
            hdf_file.delete();

        hdf_file = new File(HDF5IMAGE);
        if (hdf_file.exists())
            hdf_file.delete();

        mainFrameFixture.robot.waitForIdle();
        mainFrameFixture.cleanUp();
    }

    @Test
    public void convertImageToHDF4() {
        File hdf_file = createFile(JPGFILE, true);
        closeFile(hdf_file, true);
        try {            
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Convert Image To", "HDF4");
            mainFrameFixture.robot.waitForIdle();

            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("sourcefilebutton").click();
            mainFrameFixture.robot.waitForIdle();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText(JPGFILE);
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().button("okbutton").click();
            mainFrameFixture.robot.waitForIdle();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();

            assertTrue("convertImageToHDF4 filetree has file",
                    (filetree.valueAt(1)).compareTo(JPGFILE) == 0);

            JMenuItemFixture imageMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Open As");
            mainFrameFixture.robot.waitForIdle();

            imageMenuItem.requireVisible();
            imageMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().radioButton("imagebutton").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            // Test a few sample pixel values
            testSamplePixel(325, 53, "x=325,   y=53,   value=(152, 106, 91)");
            testSamplePixel(430, 357, "x=430,   y=357,   value=(83, 80, 107)");
            testSamplePixel(197, 239, "x=197,   y=239,   value=(206, 177, 159)");

            // Test metadata
            JMenuItemFixture metadataMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();

            metadataMenuItem.requireVisible();
            metadataMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            DialogFixture dlg = mainFrameFixture.dialog();
            dlg.requireVisible();

            // Test Name
            dlg.label("namefield").requireText(JPGFILE);

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
                closeFile(hdf_file, true);
            }
            catch (Exception ex) {
            }
        }
    }

    @Test
    public void convertImageToHDF5() {
        File hdf_file = new File(HDF5IMAGE);

        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Convert Image To", "HDF5");
            mainFrameFixture.robot.waitForIdle();

            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("sourcefilebutton").click();
            mainFrameFixture.robot.waitForIdle();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText(JPGFILE);
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("okbutton").click();
            mainFrameFixture.robot.waitForIdle();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();

            assertTrue("convertImageToHDF5 filetree has file",
                    (filetree.valueAt(1)).compareTo(JPGFILE) == 0);

            JMenuItemFixture imageMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Open As");
            mainFrameFixture.robot.waitForIdle();

            imageMenuItem.requireVisible();
            imageMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().radioButton("imagebutton").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            // Test a few sample pixel values
            testSamplePixel(325, 53, "x=325,   y=53,   value=(152, 106, 91)");
            testSamplePixel(430, 357, "x=430,   y=357,   value=(83, 80, 107)");
            testSamplePixel(197, 239, "x=197,   y=239,   value=(206, 177, 159)");

            // Test metadata
            JMenuItemFixture metadataMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();

            metadataMenuItem.requireVisible();
            metadataMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            DialogFixture dlg = mainFrameFixture.dialog();
            dlg.requireVisible();

            // Test Name
            dlg.label("namefield").requireText(JPGFILE);

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
                closeFile(hdf_file, true);
            }
            catch (Exception ex) {
            }
        }
    }
}