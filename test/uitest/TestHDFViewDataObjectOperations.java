package test.uitest;

import static org.fest.swing.data.TableCell.row;
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
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.fest.swing.fixture.JScrollPaneFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class TestHDFViewDataObjectOperations {
    private static FrameFixture mainFrameFixture;
    // the version of the HDFViewer
    private static String VERSION = "2.11";
    File hdf5file = null;

    private File openHDF5File(String name, int initrows) {
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

    private void closeFile(File hdf_file, boolean delete_file) {
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

    private static File createHDF5File(String name) {
        File hdf_file = new File(name+".h5");
        if(hdf_file.exists())
            hdf_file.delete();

        try {
            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File","New","HDF5");
            mainFrameFixture.robot.waitForIdle();
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText(name+".h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("File-HDF5 file created", hdf_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }

        return hdf_file;
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
            imagePane.horizontalScrollBar().scrollToMinimum();
            mainFrameFixture.robot.waitForIdle();

            imagePane.verticalScrollBar().scrollToMinimum();
            mainFrameFixture.robot.waitForIdle();

            // Calculate number of units to scroll in order to display this pixel
            increment = imagePane.component().getHorizontalScrollBar().getUnitIncrement();
            unitsX = x / increment;
            unitsY = y / increment;

            if(unitsX > 0) {
                imagePane.horizontalScrollBar().scrollUnitDown(unitsX);
                mainFrameFixture.robot.waitForIdle();
            }

            if(unitsY > 0) {
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
        File hdf_file = new File("dataset_saveto_test.h5");
        if(hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("image_saveto_test.h5");
        if(hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("group_saveto_test.h5");
        if(hdf_file.exists())
            hdf_file.delete();

        mainFrameFixture.robot.waitForIdle();
        mainFrameFixture.cleanUp();
    }

    @Test
    public void testDatasetSaveTo() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(2).menuItemWithPath("Save to").click();
            mainFrameFixture.robot.waitForIdle();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("dataset_saveto_test.h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("dataset_saveto_test", 2);

            assertTrue("Filetree shows", (filetree.valueAt(0)).compareTo("dataset_saveto_test.h5") == 0);
            assertTrue("Filetree shows", (filetree.valueAt(1)).compareTo("test_dataset_1") == 0);

            // Check copied dataset's data
            filetree.showPopupMenuAt(1).menuItemWithPath("Open").click();
            mainFrameFixture.robot.waitForIdle();

            JTableFixture tbl = mainFrameFixture.table("data");
            JTableCellFixture cell = tbl.cell(row(2).column(1));
            cell.requireValue("56");

            mainFrameFixture.button("nextbutton").click();
            mainFrameFixture.robot.waitForIdle();

            cell = tbl.cell(row(0).column(2));
            cell.requireValue("2");

            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Check Properties
            filetree.showPopupMenuAt(1).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().label("namefield").requireText("test_dataset_1");
            mainFrameFixture.dialog().textBox("dimensions").requireText("3");
            mainFrameFixture.dialog().textBox("dimensionsize").requireText("3 x 3 x 3");

            // Check Attributes
            JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            JTableFixture attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(2);

            attrTable.cell(row(0).column(0)).requireValue("Test1");
            attrTable.cell(row(0).column(1)).requireValue("456");
            attrTable.cell(row(0).column(2)).requireValue("64-bit integer");
            attrTable.cell(row(0).column(3)).requireValue("1");
            attrTable.cell(row(1).column(0)).requireValue("Test2");
            attrTable.cell(row(1).column(1)).requireValue("This is a test");
            attrTable.cell(row(1).column(2)).requireValue("String, length = 15");
            attrTable.cell(row(1).column(3)).requireValue("1");

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(new File("dataset_saveto_test.h5"), true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void testImageSaveTo() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(3).menuItemWithPath("Save to").click();
            mainFrameFixture.robot.waitForIdle();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("image_saveto_test.h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("image_saveto_test", 2);

            assertTrue("Filetree shows", (filetree.valueAt(0)).compareTo("image_saveto_test.h5") == 0);
            assertTrue("Filetree shows", (filetree.valueAt(1)).compareTo("test_image") == 0);

            // Check copied image's data
            filetree.showPopupMenuAt(1).menuItemWithPath("Open").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("Image", "Show Value").click();
            mainFrameFixture.robot.waitForIdle();

            testSamplePixel(118, 217, "x=118,   y=217,   value=0");
            testSamplePixel(251, 430, "x=251,   y=430,   value=0");
            testSamplePixel(186, 31, "x=186,   y=31,   value=0");

            mainFrameFixture.menuItemWithPath("Image", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Check Properties
            filetree.showPopupMenuAt(1).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().label("namefield").requireText("test_image");
            mainFrameFixture.dialog().textBox("dimensions").requireText("2");
            mainFrameFixture.dialog().textBox("dimensionsize").requireText("500 x 300");

            // Check Attributes
            JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            JTableFixture attrTable = mainFrameFixture.dialog().table("attributes");
            JTableCellFixture cell;

            attrTable.requireRowCount(5);
            attrTable.requireColumnCount(4);

            cell = attrTable.cell(row(0).column(1));
            cell.requireValue("IMAGE");
            cell = attrTable.cell(row(1).column(1));
            cell.requireValue("0, 255");
            cell = attrTable.cell(row(2).column(1));
            cell.requireValue("IMAGE_TRUECOLOR");
            cell = attrTable.cell(row(3).column(1));
            cell.requireValue("1.2");
            cell = attrTable.cell(row(4).column(1));
            cell.requireValue("INTERLACE_PIXEL");

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(new File("image_saveto_test.h5"), true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void testGroupSaveTo() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(4).menuItemWithPath("Save to").click();
            mainFrameFixture.robot.waitForIdle();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText("group_saveto_test.h5");
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("group_saveto_test", 2);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("Filetree shows", (filetree.valueAt(0)).compareTo("group_saveto_test.h5") == 0);
            assertTrue("Filetree shows", (filetree.valueAt(1)).compareTo("Group B") == 0);
            assertTrue("Filetree shows", (filetree.valueAt(2)).compareTo("test_compound") == 0);

            // Check copied group's data
            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(2).menuItemWithPath("Open").click();
            mainFrameFixture.robot.waitForIdle();

            JTableFixture tbl = mainFrameFixture.table("data");
            JTableCellFixture cell = tbl.cell(row(0).column(0));
            cell.requireValue("0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0");
            cell = tbl.cell(row(0).column(1));
            cell.requireValue("test");
            cell = tbl.cell(row(0).column(3));
            cell.requireValue("more test");
            cell = tbl.cell(row(0).column(5));
            cell.requireValue("test3");

            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Check Properties
            filetree.showPopupMenuAt(1).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().label("namefield").requireText("Group B");

            JTableFixture groupInfo = mainFrameFixture.dialog().table("GroupInfo");
            cell = groupInfo.cell(row(0).column(0));
            cell.requireValue("test_compound");
            cell = groupInfo.cell(row(0).column(1));
            cell.requireValue("Dataset");

            // Check Attributes
            JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            JTableFixture attrTable = mainFrameFixture.dialog().table("attributes");
            JTableCellFixture attrCell;

            attrTable.requireRowCount(1);
            attrTable.requireColumnCount(4);

            attrCell = attrTable.cell(row(0).column(0));
            attrCell.requireValue("Test_Attribute");
            attrCell = attrTable.cell(row(0).column(1));
            attrCell.requireValue("800");
            attrCell = attrTable.cell(row(0).column(2));
            attrCell.requireValue("32-bit integer");
            attrCell = attrTable.cell(row(0).column(3));
            attrCell.requireValue("1");

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(new File("group_saveto_test.h5"), true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void testRenameDataset() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Set new name
            filetree.showPopupMenuAt(2).menuItemWithPath("Rename").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().deleteText();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().enterText("renamed_dataset");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            // Reload file to check for changes
            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("File didn't get renamed", (filetree.valueAt(2)).compareTo("renamed_dataset") == 0);

            filetree.showPopupMenuAt(2).menuItemWithPath("Rename").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().deleteText();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().enterText("test_dataset");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("Couldn't rename file to original name", (filetree.valueAt(2)).compareTo("test_dataset") == 0);

            closeFile(hdf5file, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void testRenameImage() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Set new name
            filetree.showPopupMenuAt(3).menuItemWithPath("Rename").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().deleteText();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().enterText("renamed_image");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            // Reload file to check for changes
            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("File didn't get renamed", (filetree.valueAt(2)).compareTo("renamed_image") == 0);

            filetree.showPopupMenuAt(2).menuItemWithPath("Rename").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().deleteText();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().enterText("test_image");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("Couldn't rename file to original name", (filetree.valueAt(3)).compareTo("test_image") == 0);

            closeFile(hdf5file, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void testRenameGroup() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Set new name
            filetree.showPopupMenuAt(4).menuItemWithPath("Rename").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().deleteText();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().enterText("renamed_group");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            // Reload file to check for changes
            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("File didn't get renamed", (filetree.valueAt(4)).compareTo("renamed_group") == 0);

            filetree.showPopupMenuAt(4).menuItemWithPath("Rename").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().deleteText();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().textBox().enterText("Group B");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("Couldn't rename file to original name", (filetree.valueAt(4)).compareTo("Group B") == 0);

            closeFile(hdf5file, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    /*
     * Until issue with editing attributes [JAVA-1863] is fixed, all
     * attribute tests will currently fail
     */

    @Ignore
    public void testChangeDatasetAttribute() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(2).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            JTableFixture attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(2);

            attrTable.cell(row(0).column(0)).requireValue("Test1");
            attrTable.cell(row(0).column(1)).requireValue("456");
            attrTable.cell(row(0).column(2)).requireValue("64-bit integer");
            attrTable.cell(row(0).column(3)).requireValue("1");
            attrTable.cell(row(1).column(0)).requireValue("Test2");
            attrTable.cell(row(1).column(1)).requireValue("This is a test");
            attrTable.cell(row(1).column(2)).requireValue("String, length = 15");
            attrTable.cell(row(1).column(3)).requireValue("1");

            // Try changing an attribute name
            attrTable.cell(row(1).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(1).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(1).column(0)).enterValue("Renamed_Attribute");
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(1).column(1)).click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Save changes to file and re-open it
            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Close All").click();
            mainFrameFixture.robot.waitForIdle();

            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Check if the attribute got renamed
            filetree.showPopupMenuAt(2).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(2);

            attrTable.cell(row(1).column(0)).requireValue("Renamed_Attribute");

            // Try changing attribute back to original
            attrTable.cell(row(1).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(1).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(1).column(0)).enterValue("Test2");
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(1).column(1)).click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Save changes to file and re-open it
            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Close All").click();
            mainFrameFixture.robot.waitForIdle();

            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Check if the attribute got renamed
            filetree.showPopupMenuAt(2).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(2);

            attrTable.cell(row(1).column(0)).requireValue("Test2");

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Ignore
    public void testChangeImageAttribute() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(3).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            JTableFixture attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(5);

            attrTable.cell(row(0).column(0)).requireValue("CLASS");
            attrTable.cell(row(0).column(1)).requireValue("IMAGE");
            attrTable.cell(row(0).column(2)).requireValue("String, length = 6");
            attrTable.cell(row(0).column(3)).requireValue("1");
            attrTable.cell(row(1).column(0)).requireValue("IMAGE_VERSION");
            attrTable.cell(row(1).column(1)).requireValue("1.2");
            attrTable.cell(row(1).column(2)).requireValue("String, length = 4");
            attrTable.cell(row(1).column(3)).requireValue("1");
            attrTable.cell(row(2).column(0)).requireValue("IMAGE_MINMAXRANGE");
            attrTable.cell(row(2).column(1)).requireValue("0, 255");
            attrTable.cell(row(2).column(2)).requireValue("8-bit unsigned integer");
            attrTable.cell(row(2).column(3)).requireValue("1");
            attrTable.cell(row(3).column(0)).requireValue("IMAGE_SUBCLASS");
            attrTable.cell(row(3).column(1)).requireValue("IMAGE_TRUECOLOR");
            attrTable.cell(row(3).column(2)).requireValue("String, length = 16");
            attrTable.cell(row(3).column(3)).requireValue("1");
            attrTable.cell(row(3).column(0)).requireValue("INTERLACE_MODE");
            attrTable.cell(row(3).column(1)).requireValue("INTERLACE_PIXEL");
            attrTable.cell(row(3).column(2)).requireValue("String, length = 16");
            attrTable.cell(row(3).column(3)).requireValue("1");

            // Try changing an attribute name
            attrTable.cell(row(3).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(3).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(3).column(0)).enterValue("Renamed_Attribute");
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(3).column(1)).click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Save changes to file and re-open it
            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Close All").click();
            mainFrameFixture.robot.waitForIdle();

            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Check if the attribute got renamed
            filetree.showPopupMenuAt(3).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(5);

            attrTable.cell(row(3).column(0)).requireValue("Renamed_Attribute");

            // Try changing attribute back to original
            attrTable.cell(row(3).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(3).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(3).column(0)).enterValue("IMAGE_VERSION");
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(3).column(1)).click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Save changes to file and re-open it
            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Close All").click();
            mainFrameFixture.robot.waitForIdle();

            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Check if the attribute got renamed
            filetree.showPopupMenuAt(3).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(5);

            attrTable.cell(row(3).column(0)).requireValue("IMAGE_VERSION");

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Ignore
    public void testChangeGroupAttribute() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(4).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            JTabbedPaneFixture tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            JTableFixture attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(1);

            attrTable.cell(row(0).column(0)).requireValue("Test_Attribute");
            attrTable.cell(row(0).column(1)).requireValue("800");
            attrTable.cell(row(0).column(2)).requireValue("32-bit integer");
            attrTable.cell(row(0).column(3)).requireValue("1");

            // Try changing an attribute name
            attrTable.cell(row(0).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(0).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(0).column(0)).enterValue("Renamed_Attribute");
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(0).column(1)).click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Save changes to file and re-open it
            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Close All").click();
            mainFrameFixture.robot.waitForIdle();

            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Check if the attribute got renamed
            filetree.showPopupMenuAt(4).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(1);

            attrTable.cell(row(0).column(0)).requireValue("Renamed_Attribute");

            // Try changing attribute back to original
            attrTable.cell(row(0).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(0).column(0)).doubleClick();
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(0).column(0)).enterValue("Test_Attribute");
            mainFrameFixture.robot.waitForIdle();

            attrTable.cell(row(0).column(1)).click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            // Save changes to file and re-open it
            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Close All").click();
            mainFrameFixture.robot.waitForIdle();

            openHDF5File("test_dataobject_operations", 3);

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Check if the attribute got renamed
            filetree.showPopupMenuAt(4).menuItemWithPath("Show Properties").click();
            mainFrameFixture.robot.waitForIdle();

            tabPane = mainFrameFixture.dialog().tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General", "Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();

            attrTable = mainFrameFixture.table("attributes");
            attrTable.requireRowCount(1);

            attrTable.cell(row(0).column(0)).requireValue("Test_Attribute");

            mainFrameFixture.dialog().button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            closeFile(hdf5file, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        hdf5file = openHDF5File("test_dataobject_operations", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            // Test delete group function
            filetree.showPopupMenuAt(4).menuItemWithPath("Delete").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
            mainFrameFixture.robot.waitForIdle();

            try {
                assertFalse("Filetree still has file", (filetree.valueAt(2)).compareTo("Group B") == 0);
            }
            catch (IndexOutOfBoundsException ex) {
                // if this is caught, group was deleted
            }

            // Test delete dataset function
            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(2).menuItemWithPath("Delete").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            try {
                assertFalse("Filetree still has file", (filetree.valueAt(2)).compareTo("test_dataset_1") == 0);
            }
            catch (IndexOutOfBoundsException ex) {
                // if this is caught, dataset was deleted
            }

            // Test delete image function
            filetree.showPopupMenuAt(2).menuItemWithPath("Delete").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(0).menuItemWithPath("Reload File").click();
            mainFrameFixture.robot.waitForIdle();

            filetree.showPopupMenuAt(0).menuItemWithPath("Expand All").click();
            mainFrameFixture.robot.waitForIdle();

            try {
                assertFalse("Filetree still has file", (filetree.valueAt(2)).compareTo("test_image") == 0);
            }
            catch (IndexOutOfBoundsException ex) {
                // if this is caught, dataset was deleted
            }

            // Delete the last empty group
            filetree.showPopupMenuAt(1).menuItemWithPath("Delete").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().pressAndReleaseKeys(10);
            mainFrameFixture.robot.waitForIdle();

            filetree.selectPath("test_dataobject_operations.h5");
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.menuItemWithPath("File", "Save").click();
            mainFrameFixture.robot.waitForIdle();

            // Close and re-open file to make sure nothing remains
            closeFile(hdf5file, false);
            openHDF5File("test_dataobject_operations", 1);

            assertTrue("Filetree isn't empty", (filetree.component().getRowCount()) == 1);

            closeFile(hdf5file, false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }

}