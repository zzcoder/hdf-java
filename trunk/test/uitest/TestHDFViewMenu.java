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
import org.junit.Test;

public class TestHDFViewMenu {
    private static FrameFixture mainFrameFixture;
    private static String HDF5VERSION = "HDF5 1.8.9";
    private static String HDF4VERSION = "HDF 4.2.7";

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

        JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "New", file_type);
        fileMenuItem.robot.waitForIdle();
        fileMenuItem.requireVisible();
        fileMenuItem.click();
        mainFrameFixture.robot.waitForIdle();

        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
        fileChooser.fileNameTextBox().setText(name + file_ext);
        fileChooser.approve();

        assertTrue("File-" + file_type + " file created", hdf_file.exists());

        return hdf_file;
    }

    private File createHDF4File(String name) {
        return createFile(name, true);
    }

    private File createHDF5File(String name) {
        return createFile(name, false);
    }

    private void closeFile(File hdf_file, boolean delete_file) {
        JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Close All");
        fileMenuItem.robot.waitForIdle();
        fileMenuItem.requireVisible();
        fileMenuItem.click();
        mainFrameFixture.robot.waitForIdle();

        if (delete_file) {
            assertTrue("File file deleted", hdf_file.delete());
            assertFalse("File file gone", hdf_file.exists());
        }
    }

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
        Robot robot = BasicRobot.robotWithNewAwtHierarchy();
        String envvalue = System.getProperty("hdfview.root");
        application("ncsa.hdf.view.HDFView").withArgs("-root", envvalue, envvalue).start();
        mainFrameFixture = findFrame(
                new GenericTypeMatcher<JFrame>(JFrame.class) {
                    protected boolean isMatching(JFrame frame) {
                        return "HDFView".equals(frame.getTitle())
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
        try {
            File hdf_file = createHDF4File("testopenbutton");
            closeFile(hdf_file, false);

            mainFrameFixture.button("Open").click();
            mainFrameFixture.robot.waitForIdle();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testopenbutton.hdf");
            fileChooser.approve();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("Button-Open-HDF4 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("Button-Open-HDF4 filetree has file", (filetree.valueAt(0)).compareTo("testopenbutton.hdf") == 0);

            closeFile(hdf_file, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
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
        try {
            File hdf_file = createHDF4File("testopenfile");
            closeFile(hdf_file, false);

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Open");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testopenfile.hdf");
            fileChooser.approve();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Open-HDF4 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("File-Open-HDF4 filetree has file",(filetree.valueAt(0)).compareTo("testopenfile.hdf") == 0);

            closeFile(hdf_file, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyMenuOpenReadOnly() {
        try {
            File hdf_file = createHDF5File("testopenrofile");
            closeFile(hdf_file, false);

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Open Read-Only");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testopenrofile.h5");
            fileChooser.approve();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("File-OpenRO-HDF5 filetree shows:", filetree.target.getRowCount() == 1);
            assertTrue("File-OpenRO-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testopenrofile.h5") == 0);

            JMenuItemFixture deleteMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Delete");
            deleteMenuItem.robot.waitForIdle();
            deleteMenuItem.requireDisabled();

            closeFile(hdf_file, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyMenuNewHDF4() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "New", "HDF4");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            File hdf_file = new File("testfile.hdf");
            if (hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testfile.hdf");
            fileChooser.approve();

            assertTrue("File-New-HDF4 file created", hdf_file.exists());

            closeFile(hdf_file, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyMenuNewHDF5() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "New", "HDF5");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            File hdf_file = new File("testfile.h5");
            if (hdf_file.exists())
                hdf_file.delete();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testfile.h5");
            fileChooser.approve();

            assertTrue("File-New-HDF5 file created", hdf_file.exists());

            closeFile(hdf_file, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
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
            fileMenuItem.robot.waitForIdle();
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
            fileMenuItem.robot.waitForIdle();
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
        try {
            File hdf_file = createHDF5File("testsavefile");

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New", "Group");
            groupMenuItem.robot.waitForIdle();
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("groupname").setText("grouptestname");
            mainFrameFixture.dialog().button("OK").click();

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Save");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();

            closeFile(hdf_file, false);

            fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Open");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testsavefile.h5");
            fileChooser.approve();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Save-HDF5 filetree shows:", filetree.target.getRowCount() == 2);
            assertTrue("File-Save-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testsavefile.h5") == 0);
            assertTrue("File-Save-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("grouptestname") == 0);

            closeFile(hdf_file, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyMenuSaveAs() {
        try {
            File hdf_file = createHDF5File("testsaveasfile");

            File hdf_save_file = new File("testsaveasfile2.h5");
            if (hdf_save_file.exists())
                hdf_save_file.delete();

            JTreeFixture filetree = mainFrameFixture.tree().focus();
            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New", "Group");
            groupMenuItem.robot.waitForIdle();
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("groupname").setText("grouptestname");
            mainFrameFixture.dialog().button("OK").click();

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Save As");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testsaveasfile2.h5");
            fileChooser.approve();

            closeFile(hdf_file, true);

            JMenuItemFixture fileOpenMenuItem = mainFrameFixture.menuItemWithPath("File", "Open");
            fileOpenMenuItem.robot.waitForIdle();
            fileOpenMenuItem.requireVisible();
            fileOpenMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("testsaveasfile2.h5");
            fileChooser.approve();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-SaveAs-HDF5 filetree shows:", filetree.target.getRowCount() == 2);
            assertTrue("File-SaveAs-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testsaveasfile2.h5") == 0);
            assertTrue("File-SaveAs-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("grouptestname") == 0);

            closeFile(hdf_save_file, true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyTextInLabelWhenClickingHDF4Help() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "HDF4 Library Version");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage(HDF4VERSION);
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
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
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage(HDF5VERSION);
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
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
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("Compiled at jdk 1.6.*\\sRunning at.*");
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
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
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("HDF Viewer, Version 2.8\\sFor.*\\s\\sCopyright.*2006-2011 The HDF Group.\\sAll rights reserved.");
            mainFrameFixture.dialog().optionPane().button("OptionPane.button").click();
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
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("\\sSupported File Formats: \\s.*HDF5\\s.*HDF4\\s\\s");
            mainFrameFixture.dialog().optionPane().okButton().click();
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
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Register File Format");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().textBox("OptionPane.textField").setText("FITS:ncsa.hdf.object.fits.FitsFile:fits");
            mainFrameFixture.dialog().optionPane().okButton().click();
            
            fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "Supported File Formats");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("\\sSupported File Formats: \\s.*HDF5\\s.*HDF4\\s.*FITS\\s\\s");
            mainFrameFixture.dialog().optionPane().okButton().click();

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void verifyUnregisterFileFormatTools() {
        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Tools", "Unregister File Format");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().comboBox("OptionPane.comboBox").selectItem("FITS");
            mainFrameFixture.dialog().optionPane().okButton().click();
            
            fileMenuItem = mainFrameFixture.menuItemWithPath("Help", "Supported File Formats");
            fileMenuItem.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            mainFrameFixture.dialog().optionPane().requirePlainMessage().requireMessage("\\sSupported File Formats: \\s.*HDF5\\s.*HDF4\\s\\s");
            mainFrameFixture.dialog().optionPane().okButton().click();

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }
}
