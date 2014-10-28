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
    private static String HDF5VERSION = "HDF5 1.8.14";
    private static String HDF4VERSION = "HDF 4.2.10";
    // the version of the HDFViewer
    private static String VERSION = "2.11";

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

    private File createHDF4File(String name) {
        return createFile(name, true);
    }

    private File createHDF5File(String name) {
        return createFile(name, false);
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
        //cleanup any left over files
        File hdf_file = new File("testopenbutton.hdf");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("closebutton.hdf");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testopenfile.hdf");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testopenrofile.h5");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testfile.hdf");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testfile.h5");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("closefile.hdf");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("closeallfile.hdf");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("closeallfile.h5");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testsavefile.h5");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testsaveasfile.h5");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testsaveasfile2.h5");
        if (hdf_file.exists())
            hdf_file.delete();

        mainFrameFixture.robot.waitForIdle();
        // mainFrameFixture.requireNotVisible();
        mainFrameFixture.cleanUp();
    }

    @Test
    public void verifyOpenButtonEnabled() {
        try {
            mainFrameFixture.button("Open").requireEnabled();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyCloseButtonEnabled() {
        try {
            mainFrameFixture.button("Close").requireEnabled();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyHelpButtonEnabled() {
        mainFrameFixture.button("Help").requireEnabled();
    }

    @Test
    public void verifyHDF4ButtonEnabled() {
        try {
            mainFrameFixture.button("HDF4 library").requireEnabled();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyHDF5ButtonEnabled() {
        try {
            mainFrameFixture.button("HDF5 library").requireEnabled();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyTextInLabelWhenClickingHDF4Button() {
        try {
            mainFrameFixture.button("HDF4 library").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage(HDF4VERSION);
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyTextInLabelWhenClickingHDF5Button() {
        try {
            mainFrameFixture.button("HDF5 library").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage(HDF5VERSION);
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyButtonOpen() {
        File hdf_file = createHDF4File("testopenbutton");

        try {
            closeFile(hdf_file, false);

            mainFrameFixture.button("Open").click();
            mainFrameFixture.robot.waitForIdle();
            
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testopenbutton.hdf");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("Button-Open-HDF4 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("Button-Open-HDF4 filetree has file", (filetree.valueAt(0)).compareTo("testopenbutton.hdf") == 0);
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
            catch (Exception ex) {}
        }
    }

    @Test
    public void verifyButtonClose() {
        try {
            File hdf_file = createHDF4File("closebutton");

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("Button-Close-HDF4 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("Button-Close-HDF4 filetree has file", (filetree.valueAt(0)).compareTo("closebutton.hdf") == 0);

            filetree.clickRow(0);
            mainFrameFixture.button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("Button-Close-HDF4 file deleted", hdf_file.delete());
            assertFalse("Button-Close-HDF4 file gone", hdf_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyMenuOpen() {
        File hdf_file = createHDF4File("testopenfile");

        try {
            closeFile(hdf_file, false);

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Open");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testopenfile.hdf");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Open-HDF4 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("File-Open-HDF4 filetree has file",(filetree.valueAt(0)).compareTo("testopenfile.hdf") == 0);
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
            catch (Exception ex) {}
        }
    }

    @Test
    public void verifyMenuOpenReadOnly() {
        File hdf_file = createHDF5File("testopenrofile");

        try {
            closeFile(hdf_file, false);

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Open Read-Only");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testopenrofile.h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-OpenRO-HDF5 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("File-OpenRO-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testopenrofile.h5") == 0);

            JMenuItemFixture deleteMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Delete");
            mainFrameFixture.robot.waitForIdle();
            deleteMenuItem.requireDisabled();
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
            catch (Exception ex) {}
        }
    }

    @Test
    public void verifyMenuNewHDF4() {
        File hdf_file = null;
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "New", "HDF4");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            hdf_file = new File("testfile.hdf");
            if (hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testfile.hdf");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("File-New-HDF4 file created", hdf_file.exists());
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
            catch (Exception ex) {}
        }
    }

    @Test
    public void verifyMenuNewHDF5() {
        File hdf_file = null;
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "New", "HDF5");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

             hdf_file = new File("testfile.h5");
            if (hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testfile.h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("File-New-HDF5 file created", hdf_file.exists());
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
            catch (Exception ex) {}
        }
    }

    @Test
    public void verifyMenuClose() {
        try {
            File hdf_file = createHDF4File("closefile");

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Close-HDF4 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("File-Close-HDF4 filetree has file", (filetree.valueAt(0)).compareTo("closefile.hdf") == 0);

            filetree.clickRow(0);
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Close");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("File-Close-HDF4 file deleted", hdf_file.delete());
            assertFalse("File-Close-HDF4 file gone", hdf_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyMenuCloseAll() {
        try {
            File hdf4_file = createHDF4File("closeallfile");

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Close All-HDF4 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("File-Close All-HDF4 filetree has file", (filetree.valueAt(0)).compareTo("closeallfile.hdf") == 0);

            File hdf5_file = createHDF5File("closeallfile");

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Close All-HDF4 filetree shows:", filetree.target.getRowCount() == 2);
            assertTrue("File-Close All-HDF4 filetree has file", (filetree.valueAt(1)).compareTo("closeallfile.h5") == 0);

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Close All");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("File-Close All-HDF4 file deleted", hdf4_file.delete());
            assertFalse("File-Close All-HDF4 file gone", hdf4_file.exists());

            assertTrue("File-Close All-HDF5 file deleted", hdf5_file.delete());
            assertFalse("File-Close All-HDF5 file gone", hdf5_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyMenuSave() {
        File hdf_file = createHDF5File("testsavefile");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New", "Group");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("groupname").setText("grouptestname");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Save");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf_file, false);

            fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Open");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testsavefile.h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Save-HDF5 filetree shows:", filetree.target.getRowCount() == 2);
            assertTrue("File-Save-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testsavefile.h5") == 0);
            assertTrue("File-Save-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("grouptestname") == 0);
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
            catch (Exception ex) {}
        }
    }

    @Test
    public void verifyMenuSaveAs() {
        File hdf_file = createHDF5File("testsaveasfile");
        File hdf_save_file = new File("testsaveasfile2.h5");
        if (hdf_save_file.exists())
            hdf_save_file.delete();

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New", "Group");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("groupname").setText("grouptestname");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Save As");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testsaveasfile2.h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf_file, true);

            JMenuItemFixture fileOpenMenuItem = mainFrameFixture.menuItemWithPath("File", "Open");
            mainFrameFixture.robot.waitForIdle();
            
            fileOpenMenuItem.requireVisible();
            fileOpenMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testsaveasfile2.h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-SaveAs-HDF5 filetree shows:", filetree.target.getRowCount() == 2);
            assertTrue("File-SaveAs-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testsaveasfile2.h5") == 0);
            assertTrue("File-SaveAs-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("grouptestname") == 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
        finally {
            try {
                closeFile(hdf_save_file, true);
            }
            catch (Exception ex) {}
        }
    }

    @Test
    public void verifyTextInLabelWhenClickingHDF4Help() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "HDF4 Library Version");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage(HDF4VERSION);
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyTextInLabelWhenClickingHDF5Help() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "HDF5 Library Version");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage(HDF5VERSION);
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyTextInLabelWhenClickingJavaHelp() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "Java Version");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("Compiled at jdk 1.7.*\\sRunning at.*");
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyTextInLabelWhenClickingAboutHelp() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "About...");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("HDF Viewer, Version 2.11\\sFor.*\\s\\sCopyright.*2006-2014 The HDF Group.\\sAll rights reserved.");
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyTextInLabelWhenClickingSupportedFileFormatsHelp() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "Supported File Formats");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("\\sSupported File Formats: \\s.*Fits\\s.*HDF5\\s.*NetCDF\\s.*HDF4\\s\\s");
            mainFrameFixture.dialog().optionPane().okButton().click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyRegisterFileFormatTools() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Unregister File Format");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().comboBox("OptionPane.comboBox").selectItem("Fits");
            mainFrameFixture.dialog().optionPane().okButton().click();
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "Supported File Formats");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("\\sSupported File Formats: \\s.*HDF5\\s.*NetCDF\\s.*HDF4\\s\\s");
            mainFrameFixture.dialog().optionPane().okButton().click();
            mainFrameFixture.robot.waitForIdle();

            fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Register File Format");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().textBox("OptionPane.textField").setText("Fits:ncsa.hdf.object.fits.FitsFile:fits");
            mainFrameFixture.dialog().optionPane().okButton().click();
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "Supported File Formats");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("\\sSupported File Formats: \\s.*Fits\\s.*HDF5\\s.*NetCDF\\s.*HDF4\\s\\s");
            mainFrameFixture.dialog().optionPane().okButton().click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Ignore
    public void verifyUnregisterFileFormatTools() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Unregister File Format");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().comboBox("OptionPane.comboBox").selectItem("Fits");
            mainFrameFixture.dialog().optionPane().okButton().click();
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "Supported File Formats");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("\\sSupported File Formats: \\s.*HDF5\\s.*NetCDF\\s.*HDF4\\s\\s");
            mainFrameFixture.dialog().optionPane().okButton().click();
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }
}
