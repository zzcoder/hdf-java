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
import org.fest.swing.fixture.DialogFixture;
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
    public void verifyOpenButtonEnabled() {
        mainFrameFixture.button("Open").requireEnabled();
    }
    
    @Test 
    public void verifyCloseButtonEnabled() {
        mainFrameFixture.button("Close").requireEnabled();
    }
    
    @Test 
    public void verifyHelpButtonEnabled() {
        mainFrameFixture.button("Help").requireEnabled();
    }
    
    @Test 
    public void verifyHDF4ButtonEnabled() {
        mainFrameFixture.button("HDF4 library").requireEnabled();
    }
    
    @Test
    public void verifyHDF5ButtonEnabled() {
        mainFrameFixture.button("HDF5 library").requireEnabled();
    }
    
    @Test 
    public void verifyTextInLabelWhenClickingHDF4Button() {
        mainFrameFixture.button("HDF4 library").click();
        mainFrameFixture.dialog("dialog0").optionPane().requirePlainMessage().requireMessage("HDF 4.2.5");
        mainFrameFixture.dialog("dialog0").optionPane().button("OptionPane.button").click();
    }
    
    @Test 
    public void verifyTextInLabelWhenClickingHDF5Button() {
        mainFrameFixture.button("HDF5 library").click();
        mainFrameFixture.dialog("dialog1").optionPane().requirePlainMessage().requireMessage("HDF5 1.8.6");
        mainFrameFixture.dialog("dialog1").optionPane().button("OptionPane.button").click();
    }
    
    @Test 
    public void verifyMenuNewHDF4() {
        JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF4");
        fileMenuItem.robot.waitForIdle();
        fileMenuItem.requireVisible();
        fileMenuItem.click();

        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
        fileChooser.fileNameTextBox().requireText("*.hdf");
        fileChooser.fileNameTextBox().setText("testfile.hdf");
        fileChooser.approve();
        File hdf_file = new File("testfile.hdf");
        assertTrue(hdf_file.exists());
        hdf_file.delete();
    }
    
    @Test 
    public void verifyMenuNewHDF5() {
        JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF5");
        fileMenuItem.robot.waitForIdle();
        fileMenuItem.requireVisible();
        fileMenuItem.click();

        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
        fileChooser.fileNameTextBox().requireText("*.h5");
        fileChooser.fileNameTextBox().setText("testfile.h5");
        fileChooser.approve();
        File hdf_file = new File("testfile.h5");
        assertTrue(hdf_file.exists());
        hdf_file.delete();
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
