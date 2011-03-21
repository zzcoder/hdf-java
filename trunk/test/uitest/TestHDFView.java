package test.uitest;

import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;

import javax.swing.JFrame;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.finder.JFileChooserFinder;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Ignore;


public class TestHDFView {
    private static FrameFixture mainFrameFixture;

    @BeforeClass 
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
        Robot robot = BasicRobot.robotWithNewAwtHierarchy(); 
        application("ncsa.hdf.view.HDFView").start();
        mainFrameFixture = findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
            protected boolean isMatching(JFrame frame) {
                return "HDFView".equals(frame.getTitle()) && frame.isShowing();
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
    public void createNewHDF5Dataset() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF5");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            File hdf_file = new File("testds.h5");
            if(hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().requireText("*.h5");
            fileChooser.fileNameTextBox().setText("testds.h5");
            fileChooser.approve();

            assertTrue("File-New-HDF5 file created", hdf_file.exists());

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("File-New-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            JMenuItemFixture datasetMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New","Dataset");
            datasetMenuItem.robot.waitForIdle();
            datasetMenuItem.requireVisible();
            datasetMenuItem.click();

            mainFrameFixture.dialog("dialog2").textBox("datasetname").setText("dsname");
            mainFrameFixture.dialog("dialog2").button("OK").click();

            fileMenuItem = mainFrameFixture.menuItemWithPath("File","Close All");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            assertTrue("File-New-HDF5 file deleted", hdf_file.delete());
            assertFalse("File-New-HDF5 file gone", hdf_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
