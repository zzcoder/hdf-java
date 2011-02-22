package test.uitest;

import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF5");
        fileMenuItem.robot.waitForIdle();
        fileMenuItem.requireVisible();
        fileMenuItem.click();

        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
        fileChooser.fileNameTextBox().requireText("*.h5");
        fileChooser.fileNameTextBox().setText("testds.h5");
        fileChooser.approve();
        
        File hdf_file = new File("testds.h5");
        assertTrue(hdf_file.exists());

        JTreeFixture filetree = mainFrameFixture.tree().focus();
        filetree.requireVisible();
        assertEquals(filetree.valueAt(2), "testds.h5");
        JMenuItemFixture datasetMenuItem = filetree.showPopupMenuAt(2).menuItemWithPath("New","Dataset");
        datasetMenuItem.robot.waitForIdle();
        datasetMenuItem.requireVisible();
        datasetMenuItem.click();
        
        mainFrameFixture.dialog("dialog2").textBox("datasetname").setText("dsname");
        mainFrameFixture.dialog("dialog2").button("OK").click();
        
        hdf_file.delete();
    }
}
