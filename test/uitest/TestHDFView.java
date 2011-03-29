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

    private File createHDF5File(String name) {
        File hdf_file = new File(name+".h5");
        if(hdf_file.exists())
            hdf_file.delete();

        JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF5");
        fileMenuItem.robot.waitForIdle();
        fileMenuItem.requireVisible();
        fileMenuItem.click();

        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
        fileChooser.fileNameTextBox().requireText("*.h5");
        fileChooser.fileNameTextBox().setText(name+".h5");
        fileChooser.approve();

        assertTrue("File-HDF5 file created", hdf_file.exists());

        return hdf_file;
    }
    
    private void closeFile(File hdf_file, boolean delete_file) {
        JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","Close All");
        fileMenuItem.robot.waitForIdle();
        fileMenuItem.requireVisible();
        fileMenuItem.click();

        if(delete_file) {
            assertTrue("File file deleted", hdf_file.delete());
            assertFalse("File file gone", hdf_file.exists());
        }
    }

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
            File hdf_file = createHDF5File("testds");

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("File-Dataset-HDF5 filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("File-Dataset-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);

            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New","Group");
            groupMenuItem.robot.waitForIdle();
            groupMenuItem.requireVisible();
            groupMenuItem.click();

            mainFrameFixture.dialog("dialog2").textBox("groupname").setText("testgroupname");
            mainFrameFixture.dialog("dialog2").button("OK").click();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Dataset-HDF5 filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("File-Dataset-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            assertTrue("File-Dataset-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);

            JMenuItemFixture datasetMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("New","Dataset");
            datasetMenuItem.robot.waitForIdle();
            datasetMenuItem.requireVisible();
            datasetMenuItem.click();

            mainFrameFixture.dialog("dialog3").textBox("datasetname").setText("testdatasetname");
            mainFrameFixture.dialog("dialog3").button("OK").click();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Dataset-HDF5 filetree shows:", filetree.target.getRowCount()==3);
            assertTrue("File-Dataset-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            assertTrue("File-Dataset-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);
            assertTrue("File-Dataset-HDF5 filetree has dataset", (filetree.valueAt(2)).compareTo("testdatasetname")==0);

            closeFile(hdf_file, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }
}
