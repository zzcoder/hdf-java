package test.uitest;

import static org.fest.swing.data.TableCell.row;
import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.regex.Pattern;

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
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestTreeViewFiles {
    private static FrameFixture mainFrameFixture;
    // the version of the HDFViewer
    private static String VERSION = "2.X";

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
    
    private static void clearRemovePropertyFile() {
        // the local property file name
        // look for the property file at the use home directory
        String fn = ".hdfview" + VERSION.substring(0, 3);
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
        application("ncsa.hdf.view.HDFView").withArgs("-root", envvalue, envvalue).start();
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
    public static void finishApplication() {
        mainFrameFixture.robot.waitForIdle();
        //mainFrameFixture.requireNotVisible();
        mainFrameFixture.cleanUp();
    }

    @Test 
    public void openHDF5Group() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(1)).compareTo("DS08BITS")==0);

            JMenuItemFixture dataset4MenuItem = filetree.showPopupMenuAt(4).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset4MenuItem.requireVisible();
            dataset4MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset4table = mainFrameFixture.table("data");
            JTableCellFixture cell4 = dataset4table.cell(row(0).column(0));
            cell4.requireValue("-1");
            cell4 = dataset4table.cell(row(7).column(0));
            cell4.requireValue("-128");
            cell4 = dataset4table.cell(row(3).column(60));
            cell4.requireValue("-9223372036854775808");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            JMenuItemFixture dataset8MenuItem = filetree.showPopupMenuAt(8).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset8MenuItem.requireVisible();
            dataset8MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset8table = mainFrameFixture.table("data");
            JTableCellFixture cell8 = dataset8table.cell(row(0).column(0));
            cell8.requireValue("18446744073709551615");
            cell8 = dataset8table.cell(row(7).column(0));
            cell8.requireValue("18446744073709551488");
            cell8 = dataset8table.cell(row(7).column(56));
            cell8.requireValue("9223372036854775808");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5GroupDS08() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(1)).compareTo("DS08BITS")==0);

            JMenuItemFixture dataset1MenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset1MenuItem.requireVisible();
            dataset1MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset1table = mainFrameFixture.table("data");
            JTableCellFixture cell1 = dataset1table.cell(row(0).column(0));
            cell1.requireValue("-1");
            cell1 = dataset1table.cell(row(7).column(0));
            cell1.requireValue("-128");
            cell1 = dataset1table.cell(row(7).column(7));
            cell1.requireValue("0");
            cell1.enterValue("127");
            mainFrameFixture.robot.waitForIdle();

            cell1.requireValue("127");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5GroupDU08() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(5)).compareTo("DU08BITS")==0);

            JMenuItemFixture dataset5MenuItem = filetree.showPopupMenuAt(5).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset5MenuItem.requireVisible();
            dataset5MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset5table = mainFrameFixture.table("data");
            JTableCellFixture cell5 = dataset5table.cell(row(0).column(0));
            cell5.requireValue("255");
            cell5 = dataset5table.cell(row(7).column(0));
            cell5.requireValue("128");
            cell5 = dataset5table.cell(row(7).column(7));
            cell5.requireValue("0");
            cell5.enterValue("255");
            mainFrameFixture.robot.waitForIdle();

            cell5.requireValue("255");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5GroupDS16() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(2)).compareTo("DS16BITS")==0);

            JMenuItemFixture dataset2MenuItem = filetree.showPopupMenuAt(2).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset2MenuItem.requireVisible();
            dataset2MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset2table = mainFrameFixture.table("data");
            JTableCellFixture cell2 = dataset2table.cell(row(0).column(0));
            cell2.requireValue("-1");
            cell2 = dataset2table.cell(row(7).column(0));
            cell2.requireValue("-128");
            cell2 = dataset2table.cell(row(7).column(15));
            cell2.requireValue("0");
            cell2.enterValue("32767");
            mainFrameFixture.robot.waitForIdle();

            cell2.requireValue("32767");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5GroupDU16() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(6)).compareTo("DU16BITS")==0);

            JMenuItemFixture dataset6MenuItem = filetree.showPopupMenuAt(6).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset6MenuItem.requireVisible();
            dataset6MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset6table = mainFrameFixture.table("data");
            JTableCellFixture cell6 = dataset6table.cell(row(0).column(0));
            cell6.requireValue("65535");
            cell6 = dataset6table.cell(row(7).column(0));
            cell6.requireValue("65408");
            cell6 = dataset6table.cell(row(7).column(15));
            cell6.requireValue("0");
            cell6.enterValue("65535");
            mainFrameFixture.robot.waitForIdle();

            cell6.requireValue("65535");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5GroupDS32() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(3)).compareTo("DS32BITS")==0);

            JMenuItemFixture dataset3MenuItem = filetree.showPopupMenuAt(3).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset3MenuItem.requireVisible();
            dataset3MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset3table = mainFrameFixture.table("data");
            JTableCellFixture cell3 = dataset3table.cell(row(0).column(0));
            cell3.requireValue("-1");
            cell3 = dataset3table.cell(row(7).column(0));
            cell3.requireValue("-128");
            cell3 = dataset3table.cell(row(7).column(31));
            cell3.requireValue("0");
            cell3.enterValue("2147483647");
            mainFrameFixture.robot.waitForIdle();

            cell3.requireValue("2147483647");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5GroupDU32() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(7)).compareTo("DU32BITS")==0);

            JMenuItemFixture dataset7MenuItem = filetree.showPopupMenuAt(7).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset7MenuItem.requireVisible();
            dataset7MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset7table = mainFrameFixture.table("data");
            JTableCellFixture cell7 = dataset7table.cell(row(0).column(0));
            cell7.requireValue("4294967295");
            cell7 = dataset7table.cell(row(7).column(0));
            cell7.requireValue("4294967168");
            cell7 = dataset7table.cell(row(7).column(31));
            cell7.requireValue("0");
            cell7.enterValue("4294967295");
            mainFrameFixture.robot.waitForIdle();
            
            cell7.requireValue("4294967295");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5GroupDS64() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(4)).compareTo("DS64BITS")==0);

            JMenuItemFixture dataset4MenuItem = filetree.showPopupMenuAt(4).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset4MenuItem.requireVisible();
            dataset4MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset4table = mainFrameFixture.table("data");
            JTableCellFixture cell4 = dataset4table.cell(row(0).column(0));
            cell4.requireValue("-1");
            cell4 = dataset4table.cell(row(7).column(0));
            cell4.requireValue("-128");
            cell4 = dataset4table.cell(row(7).column(63));
            cell4.requireValue("0");
            cell4.enterValue("9223372036854775807");
            mainFrameFixture.robot.waitForIdle();

            cell4.requireValue("9223372036854775807");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5GroupDU64() {
        File hdf_file = openHDF5File("tintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(8)).compareTo("DU64BITS")==0);

            JMenuItemFixture dataset8MenuItem = filetree.showPopupMenuAt(8).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset8MenuItem.requireVisible();
            dataset8MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset8table = mainFrameFixture.table("data");
            JTableCellFixture cell8 = dataset8table.cell(row(0).column(0));
            cell8.requireValue("18446744073709551615");
            cell8 = dataset8table.cell(row(7).column(0));
            cell8.requireValue("18446744073709551488");
            cell8 = dataset8table.cell(row(7).column(63));
            cell8.requireValue("0");
            cell8.enterValue("18446744073709551615");
            mainFrameFixture.robot.waitForIdle();

            cell8.requireValue("18446744073709551615");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void openHDF5ScalarGroup() {
        File hdf_file = openHDF5File("tscalarintsize", 10);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5ScalarGroup filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("openHDF5ScalarGroup filetree has file", (filetree.valueAt(0)).compareTo("tscalarintsize.h5")==0);
            assertTrue("openHDF5ScalarGroup filetree has group", (filetree.valueAt(1)).compareTo("DS08BITS")==0);

            JMenuItemFixture dataset4MenuItem = filetree.showPopupMenuAt(4).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset4MenuItem.requireVisible();
            dataset4MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset4table = mainFrameFixture.table("data");
            JTableCellFixture cell4 = dataset4table.cell(row(0).column(0));
            Pattern pattern = Pattern.compile("^-1, .*");
            cell4.requireValue(pattern);
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            JMenuItemFixture dataset8MenuItem = filetree.showPopupMenuAt(8).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset8MenuItem.requireVisible();
            dataset8MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset8table = mainFrameFixture.table("data");
            JTableCellFixture cell8 = dataset8table.cell(row(0).column(0));
            pattern = Pattern.compile("^18446744073709551615, .*");
            cell8.requireValue(pattern);
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }
    
    @Test 
    public void openHDF5ScalarAttribute() {
        File hdf_file = openHDF5File("tscalarattrintsize", 1);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5ScalarAttribute filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("openHDF5ScalarAttribute filetree has file", (filetree.valueAt(0)).compareTo("tscalarattrintsize.h5")==0);

            JMenuItemFixture propMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();
            
            propMenuItem.requireVisible();
            propMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            DialogFixture propDialog = mainFrameFixture.dialog();
            propDialog.requireVisible();
            
            JTabbedPaneFixture tabPane = propDialog.tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes","User Block");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();
            
            tabPane.requireVisible();
            JTableFixture attrTable = propDialog.table("attributes");
            JTableCellFixture cell = attrTable.cell(row(0).column(0));
            cell.requireValue("DS08BITS");
            cell = attrTable.cell(row(1).column(0));
            cell.requireValue("DS16BITS");
            cell = attrTable.cell(row(2).column(0));
            cell.requireValue("DS32BITS");
            cell = attrTable.cell(row(3).column(0));
            cell.requireValue("DS64BITS");
            cell = attrTable.cell(row(4).column(0));
            cell.requireValue("DU08BITS");
            cell = attrTable.cell(row(5).column(0));
            cell.requireValue("DU16BITS");
            cell = attrTable.cell(row(6).column(0));
            cell.requireValue("DU32BITS");
            cell = attrTable.cell(row(7).column(0));
            cell.requireValue("DU64BITS");
            JTableCellFixture celldata = attrTable.cell(row(0).column(1));
            Pattern pattern = Pattern.compile("^. -1, .*");
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(1).column(1));
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(2).column(1));
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(3).column(1));
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(4).column(1));
            pattern = Pattern.compile("^. 255, .*");
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(5).column(1));
            pattern = Pattern.compile("^. 65535, .*");
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(6).column(1));
            pattern = Pattern.compile("^. 4294967295, .*");
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(7).column(1));
            pattern = Pattern.compile("^. 18446744073709551615, .*");
            celldata.requireValue(pattern);
            propDialog.button("Close").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test
    public void openCreateOrderHDF5Groups() {
        File hdf_file = openHDF5File("tordergr", 3);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("CreateOrderHDF5Groups filetree shows:", filetree.target.getRowCount()==3);
            assertTrue("CreateOrderHDF5Groups filetree has file", (filetree.valueAt(0)).compareTo("tordergr.h5")==0);
            assertTrue("CreateOrderHDF5Groups filetree has group", (filetree.valueAt(1)).compareTo("1")==0);
            assertTrue("CreateOrderHDF5Groups filetree has group", (filetree.valueAt(2)).compareTo("2")==0);

            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Change file indexing");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().radioButton("Index by Creation Order").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().button("Reload File").click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("CreateOrderHDF5Groups filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==3);
            assertTrue("CreateOrderHDF5Groups filetree has file", (filetree.valueAt(0)).compareTo("tordergr.h5")==0);
            assertTrue("CreateOrderHDF5Groups filetree has group", (filetree.valueAt(1)).compareTo("2")==0);
            assertTrue("CreateOrderHDF5Groups filetree has group", (filetree.valueAt(2)).compareTo("1")==0);

            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Change file indexing");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().radioButton("Index Decrements").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().button("Reload File").click();
            mainFrameFixture.robot.waitForIdle();

            expandMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("CreateOrderHDF5Groups filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==17);
            assertTrue("CreateOrderHDF5Groups filetree has file", (filetree.valueAt(0)).compareTo("tordergr.h5")==0);
            assertTrue("CreateOrderHDF5Groups filetree has group", (filetree.valueAt(1)).compareTo("1")==0);
            assertTrue("CreateOrderHDF5Groups filetree has group", (filetree.valueAt(9)).compareTo("2")==0);
            assertTrue("CreateOrderHDF5Groups filetree has group", (filetree.valueAt(16)).compareTo("c")==0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
        finally {
            try {
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }
    
    @Test 
    public void openHDF5Attribute() {
        File hdf_file = openHDF5File("tattrintsize", 1);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Attribute filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("openHDF5Attribute filetree has file", (filetree.valueAt(0)).compareTo("tattrintsize.h5")==0);

            JMenuItemFixture propMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();
            
            propMenuItem.requireVisible();
            propMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            DialogFixture propDialog = mainFrameFixture.dialog();
            propDialog.requireVisible();
            
            JTabbedPaneFixture tabPane = propDialog.tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes","User Block");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();
            
            tabPane.requireVisible();
            JTableFixture attrTable = propDialog.table("attributes");
            JTableCellFixture cell = attrTable.cell(row(0).column(0));
            cell.requireValue("DS08BITS");
            cell = attrTable.cell(row(1).column(0));
            cell.requireValue("DS16BITS");
            cell = attrTable.cell(row(2).column(0));
            cell.requireValue("DS32BITS");
            cell = attrTable.cell(row(3).column(0));
            cell.requireValue("DS64BITS");
            cell = attrTable.cell(row(4).column(0));
            cell.requireValue("DU08BITS");
            cell = attrTable.cell(row(5).column(0));
            cell.requireValue("DU16BITS");
            cell = attrTable.cell(row(6).column(0));
            cell.requireValue("DU32BITS");
            cell = attrTable.cell(row(7).column(0));
            cell.requireValue("DU64BITS");
            JTableCellFixture celldata = attrTable.cell(row(0).column(1));
            Pattern pattern = Pattern.compile("^-1, .*");
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(1).column(1));
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(2).column(1));
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(3).column(1));
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(4).column(1));
            pattern = Pattern.compile("^255, .*");
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(5).column(1));
            pattern = Pattern.compile("^65535, .*");
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(6).column(1));
            pattern = Pattern.compile("^4294967295, .*");
            celldata.requireValue(pattern);
            celldata = attrTable.cell(row(7).column(1));
            pattern = Pattern.compile("^18446744073709551615, .*");
            celldata.requireValue(pattern);
            propDialog.button("Close").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void openHDF5CompoundDS() {
        File hdf_file = openHDF5File("tcmpdintsize", 2);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("openHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("tcmpdintsize.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(1)).compareTo("CompoundIntSize")==0);

            JMenuItemFixture dataset1MenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset1MenuItem.requireVisible();
            dataset1MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset1table = mainFrameFixture.table("data");
            JTableCellFixture cell = dataset1table.cell(row(0).column(4));
            Pattern pattern = Pattern.compile("^-1, .*");
            cell.requireValue(pattern);
            cell = dataset1table.cell(row(1).column(5));
            cell.requireValue(pattern);
            cell = dataset1table.cell(row(2).column(6));
            cell.requireValue(pattern);
            cell = dataset1table.cell(row(3).column(7));
            cell.requireValue(pattern);
            cell = dataset1table.cell(row(0).column(0));
            pattern = Pattern.compile("^255, .*");
            cell.requireValue(pattern);
            cell = dataset1table.cell(row(1).column(1));
            pattern = Pattern.compile("^65535, .*");
            cell.requireValue(pattern);
            cell = dataset1table.cell(row(2).column(2));
            pattern = Pattern.compile("^4294967295, .*");
            cell.requireValue(pattern);
            cell = dataset1table.cell(row(3).column(3));
            pattern = Pattern.compile("^18446744073709551615, .*");
            cell.requireValue(pattern);
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }
    
    @Test 
    public void openHDF5CompoundAttribute() {
        File hdf_file = openHDF5File("tcmpdattrintsize", 1);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Attribute filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("openHDF5Attribute filetree has file", (filetree.valueAt(0)).compareTo("tcmpdattrintsize.h5")==0);

            JMenuItemFixture propMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();
            
            propMenuItem.requireVisible();
            propMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            DialogFixture propDialog = mainFrameFixture.dialog();
            propDialog.requireVisible();
            
            JTabbedPaneFixture tabPane = propDialog.tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes","User Block");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();
            
            tabPane.requireVisible();
            JTableFixture attrTable = propDialog.table("attributes");
            JTableCellFixture cell = attrTable.cell(row(0).column(0));
            cell.requireValue("CompoundAttrIntSize");
            propDialog.button("Close").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }
    
    @Test 
    public void openHDF5CompoundArrayImport() {
        File hdf_file = openHDF5File("temp_cmpdimport", 2);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Attribute filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("openHDF5Attribute filetree has file", (filetree.valueAt(0)).compareTo("temp_cmpdimport.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(1)).compareTo("CompoundIntSize")==0);

            JMenuItemFixture compondDSMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New","Compound DS");
            mainFrameFixture.robot.waitForIdle();
            
            compondDSMenuItem.requireVisible();
            compondDSMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("datasetname").setText("testcmpdname");
            mainFrameFixture.dialog().comboBox("templateChoice").selectItem(0);
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.dialog().comboBox("numbermembers").requireSelection("9");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Group filetree shows:", filetree.target.getRowCount()==3);
            assertTrue("openHDF5Attribute filetree has file", (filetree.valueAt(0)).compareTo("temp_cmpdimport.h5")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(1)).compareTo("CompoundIntSize")==0);
            assertTrue("openHDF5Group filetree has group", (filetree.valueAt(2)).compareTo("testcmpdname")==0);

            JMenuItemFixture propMenuItem = filetree.showPopupMenuAt(2).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();
            
            propMenuItem.requireVisible();
            propMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            DialogFixture propDialog = mainFrameFixture.dialog();
            propDialog.requireVisible();
            
            JTabbedPaneFixture tabPane = propDialog.tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes");
            
            JTableFixture dsproptable = mainFrameFixture.table("CompoundMetaData");
            JTableCellFixture cell = dsproptable.cell(row(0).column(0));
            cell.requireValue("DU08BITS");
            cell = dsproptable.cell(row(1).column(0));
            cell.requireValue("DU16BITS");
            cell = dsproptable.cell(row(2).column(0));
            cell.requireValue("DU32BITS");
            cell = dsproptable.cell(row(3).column(0));
            cell.requireValue("DU64BITS");
            cell = dsproptable.cell(row(4).column(0));
            cell.requireValue("DS08BITS");
            cell = dsproptable.cell(row(5).column(0));
            cell.requireValue("DS16BITS");
            cell = dsproptable.cell(row(6).column(0));
            cell.requireValue("DS32BITS");
            cell = dsproptable.cell(row(7).column(0));
            cell.requireValue("DS64BITS");
            propDialog.button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            JMenuItemFixture dataset2MenuItem = filetree.showPopupMenuAt(2).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset2MenuItem.requireVisible();
            dataset2MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture datasettable = mainFrameFixture.table("data");
            datasettable.requireColumnCount(9);
            assertTrue("dataset column index", datasettable.columnIndexFor("DU08BITS")==0);
            assertTrue("dataset column index", datasettable.columnIndexFor("DU16BITS")==1);
            assertTrue("dataset column index", datasettable.columnIndexFor("DU32BITS")==2);
            assertTrue("dataset column index", datasettable.columnIndexFor("DU64BITS")==3);
            assertTrue("dataset column index", datasettable.columnIndexFor("DS08BITS")==4);
            assertTrue("dataset column index", datasettable.columnIndexFor("DS16BITS")==5);
            assertTrue("dataset column index", datasettable.columnIndexFor("DS32BITS")==6);
            assertTrue("dataset column index", datasettable.columnIndexFor("DS64BITS")==7);
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
                closeFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }
}
