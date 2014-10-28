package test.uitest;

import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestHDFViewCLRootMultipleFiles {
    private static FrameFixture mainFrameFixture;
    // the version of the HDFViewer
    private static String VERSION = "2.11";
    private static String root_dir = System.getenv("PWD");
    private static String OS_type = System.getProperty("os.name").toLowerCase();


    private void closeFile() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Close All");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
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
        application("ncsa.hdf.view.HDFView").withArgs("-root", root_dir, "tattrintsize.h5", "tintsize.h5").start();
        mainFrameFixture = findFrame(
                new GenericTypeMatcher<JFrame>(JFrame.class) {
                    protected boolean isMatching(JFrame frame) {
                        return frame.getTitle().equals("HDFView "+VERSION)
                                && frame.isShowing();
                    }
                }).withTimeout(10000).using(robot);
        mainFrameFixture.robot.waitForIdle();
        mainFrameFixture.requireVisible();
    }

    @AfterClass
    public static void finishApplication() {

        mainFrameFixture.robot.waitForIdle();
        // mainFrameFixture.requireNotVisible();
        mainFrameFixture.cleanUp();
    }

    @Test
    public void verifyFileList() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("Verify filetree shows:", filetree.target.getRowCount() == 11);
            assertTrue("Verify filetree has file", (filetree.valueAt(0)).compareTo("tattrintsize.h5") == 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
        finally {
            try {
                closeFile();
            }
            catch (Exception ex) {}
        }
    }

    @Test
    public void verifyrootDir() {
        //Only run test if not on Windows because root_dir is null on windows
        assumeTrue(OS_type.indexOf("win") < 0);
        try {
            JTabbedPaneFixture tabPane = mainFrameFixture.tabbedPane("tabpane").focus();
            tabPane.selectTab("Log Info");
            mainFrameFixture.robot.waitForIdle();
            
            tabPane.requireVisible();
            String first_line = "^HDFView root - " + root_dir + ".*";
            Pattern pattern = Pattern.compile(first_line, Pattern.DOTALL);
            mainFrameFixture.textBox("status").requireText(pattern);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
        finally {
            try {
                closeFile();
            }
            catch (Exception ex) {}
        }
    }
}
