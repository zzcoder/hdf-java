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
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class TestHDFViewMenu {
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
        mainFrameFixture.dialog("dialog1").optionPane().requirePlainMessage().requireMessage("HDF5 1.8.7");
        mainFrameFixture.dialog("dialog1").optionPane().button("OptionPane.button").click();
    }

    @Test 
    public void verifyMenuOpen() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF4");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            File hdf_file = new File("testopenfile.hdf");
            if(hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().requireText("*.hdf");
            fileChooser.fileNameTextBox().setText("testopenfile.hdf");
            fileChooser.approve();

            assertTrue("File-Open-HDF4 file created", hdf_file.exists());

            fileMenuItem = mainFrameFixture.menuItemWithPath("File","Close All");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            fileMenuItem = mainFrameFixture.menuItemWithPath("File","Open");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testopenfile.hdf");
            fileChooser.approve();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Open-HDF4 filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("File-Open-HDF4 filetree has file", (filetree.valueAt(0)).compareTo("testopenfile.hdf")==0);

            filetree.clickRow(0);
            fileMenuItem = mainFrameFixture.menuItemWithPath("File","Close");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            assertTrue("File-Open-HDF4 file deleted", hdf_file.delete());
            assertFalse("File-Open-HDF4 file gone", hdf_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test 
    public void verifyMenuOpenReadOnly() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","Open Read-Only");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.cancel();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test 
    public void verifyMenuNewHDF4() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF4");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            File hdf_file = new File("testfile.hdf");
            if(hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().requireText("*.hdf");
            fileChooser.fileNameTextBox().setText("testfile.hdf");
            fileChooser.approve();

            assertTrue("File-New-HDF4 file created", hdf_file.exists());

            fileMenuItem = mainFrameFixture.menuItemWithPath("File","Close All");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            assertTrue("File-New-HDF4 file deleted", hdf_file.delete());
            assertFalse("File-New-HDF4 file gone", hdf_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test 
    public void verifyMenuNewHDF5() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF5");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            File hdf_file = new File("testfile.h5");
            if(hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().requireText("*.h5");
            fileChooser.fileNameTextBox().setText("testfile.h5");
            fileChooser.approve();

            assertTrue("File-New-HDF5 file created", hdf_file.exists());

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

    @Test
    public void verifyMenuClose() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF4");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            File hdf_file = new File("closefile.hdf");
            if(hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().requireText("*.hdf");
            fileChooser.fileNameTextBox().setText("closefile.hdf");
            fileChooser.approve();
            assertTrue("File-Close-HDF4 file created", hdf_file.exists());

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Close-HDF4 filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("File-Close-HDF4 filetree has file", (filetree.valueAt(0)).compareTo("closefile.hdf")==0);

            filetree.clickRow(0);
            fileMenuItem = mainFrameFixture.menuItemWithPath("File","Close");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            assertTrue("File-Close-HDF4 file deleted", hdf_file.delete());
            assertFalse("File-Close-HDF4 file gone", hdf_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test 
    public void verifyMenuCloseAll() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF4");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            File hdf4_file = new File("closeallfile.hdf");
            if(hdf4_file.exists())
                hdf4_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().requireText("*.hdf");
            fileChooser.fileNameTextBox().setText("closeallfile.hdf");
            fileChooser.approve();
            assertTrue("File-Close All-HDF4 file created", hdf4_file.exists());

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Close All-HDF4 filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("File-Close All-HDF4 filetree has file", (filetree.valueAt(0)).compareTo("closeallfile.hdf")==0);

            fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF5");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            File hdf5_file = new File("closeallfile.h5");
            if(hdf5_file.exists())
                hdf5_file.delete();

            fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().requireText("*.h5");
            fileChooser.fileNameTextBox().setText("closeallfile.h5");
            fileChooser.approve();

            assertTrue("File-Close All-HDF5 file created", hdf5_file.exists());

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Close All-HDF4 filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("File-Close All-HDF4 filetree has file", (filetree.valueAt(1)).compareTo("closeallfile.h5")==0);

            fileMenuItem = mainFrameFixture.menuItemWithPath("File","Close All");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            assertTrue("File-Close All-HDF4 file deleted", hdf4_file.delete());
            assertFalse("File-Close All-HDF4 file gone", hdf4_file.exists());

            assertTrue("File-Close All-HDF5 file deleted", hdf5_file.delete());
            assertFalse("File-Close All-HDF5 file gone", hdf5_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Ignore 
    public void verifyMenuSave() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","Save");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.cancel();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Ignore 
    public void verifyMenuSaveAs() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","Save As");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.cancel();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
