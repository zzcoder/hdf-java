package test.uitest;

import static org.fest.swing.data.TableCell.row;
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
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class TestTreeViewExport {
    private static FrameFixture mainFrameFixture;
    // the version of the HDFViewer
    private static String VERSION = "2.11";

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
            assertTrue("openHDF5File filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount() == initrows);
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
    
    private void closeHDFFile(File hdf_file, boolean delete_file) {
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
            
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            assertTrue("closeHDFFile filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount() == 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
    }
    
    private void removeFile(File remove_file) {
        try {
            assertTrue("File remove_file deleted", remove_file.delete());
            assertFalse("File remove_file gone", remove_file.exists());
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

    private File createHDF5File(String name) {
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

    private File createNewHDF5Group() {
        File hdf_file = createHDF5File("testds");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("createNewHDF5Group filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("createNewHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);

            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New","Group");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("groupname").setText("testgroupname");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("createNewHDF5Group filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==2);

            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("createNewHDF5Group filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("createNewHDF5Group filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            assertTrue("createNewHDF5Group filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }

        return hdf_file;
    }

    private File createImportHDF5Dataset(String datasetname) {
        File hdf_file = createNewHDF5Group();

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            JMenuItemFixture datasetMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("New","Dataset");
            mainFrameFixture.robot.waitForIdle();
            
            datasetMenuItem.requireVisible();
            datasetMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("datasetname").setText(datasetname);
            mainFrameFixture.dialog().textBox("currentsize").setText("8 x 64");
            mainFrameFixture.dialog().comboBox("datasetsize").selectItem("64");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("createImportHDF5Dataset filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==3);
            assertTrue("createImportHDF5Dataset filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            assertTrue("createImportHDF5Dataset filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);
            assertTrue("createImportHDF5Dataset filetree has dataset", (filetree.valueAt(2)).compareTo(datasetname)==0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }

        return hdf_file;
    }

    private void importHDF5Dataset(File hdf_file, String importfilename) {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();

            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("importHDF5Dataset filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==3);

            JMenuItemFixture dataset2MenuItem = filetree.showPopupMenuAt(2).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset2MenuItem.requireVisible();
            dataset2MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset2table = mainFrameFixture.table("data");
            JTableCellFixture cell = dataset2table.cell(row(0).column(0));
            cell.select();

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("Table", "Import Data from Text File");
            mainFrameFixture.robot.waitForIdle();
            
            fileMenuItem.requireVisible();
            fileMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.fileNameTextBox().setText(importfilename);
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().optionPane().component().setValue("Ok");
            mainFrameFixture.robot.waitForIdle();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
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
    public static void finishApplication() {
        mainFrameFixture.robot.waitForIdle();
        //mainFrameFixture.requireNotVisible();
        mainFrameFixture.cleanUp();
    }

    @Test 
    public void saveHDF5DatasetText() {
        File hdf_file = openHDF5File("tintsize", 10);
        File export_file = null;
        try {
            new File("DS64BITS.txt").delete();
        }
        catch (Exception ex) {}

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("saveHDF5DatasetText filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("saveHDF5DatasetText filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("saveHDF5DatasetText filetree has group", (filetree.valueAt(1)).compareTo("DS08BITS")==0);

            JMenuItemFixture dataset4MenuItem = filetree.showPopupMenuAt(4).menuItemWithPath("Export Dataset", "Export Data to Text File");
            mainFrameFixture.robot.waitForIdle();
            
            dataset4MenuItem.requireVisible();
            dataset4MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();
    
            export_file = new File("DS64BITS.txt");
            assertTrue("File-export text file created", export_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
        finally {
            try {
                closeHDFFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void saveHDF5DatasetBinary() {
        File hdf_file = openHDF5File("tintsize", 10);
        File export_file = null;

        try {
            new File("DU64BITS.bin").delete();
        }
        catch (Exception ex) {}

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("saveHDF5DatasetBinary filetree shows:", filetree.target.getRowCount()==10);
            assertTrue("saveHDF5DatasetBinary filetree has file", (filetree.valueAt(0)).compareTo("tintsize.h5")==0);
            assertTrue("saveHDF5DatasetBinary filetree has group", (filetree.valueAt(1)).compareTo("DS08BITS")==0);

            JMenuItemFixture dataset8MenuItem = filetree.showPopupMenuAt(8).menuItemWithPath("Export Dataset", "Export Data as Little Endian");
            mainFrameFixture.robot.waitForIdle();
            
            dataset8MenuItem.requireVisible();
            dataset8MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(mainFrameFixture.robot);
            fileChooser.approve();
            mainFrameFixture.robot.waitForIdle();
    
            export_file = new File("DU64BITS.bin");
            assertTrue("File-export binary file created", export_file.exists());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
        finally {
            try {
                closeHDFFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test
    public void importHDF5DatasetWithTab() {
        File hdf_file = createImportHDF5Dataset("testdstab");
        importHDF5Dataset(hdf_file, "DS64BITS.ttxt");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("importHDF5DatasetWithComma filetree shows:", filetree.target.getRowCount()==3);
            assertTrue("importHDF5DatasetWithComma filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            assertTrue("importHDF5DatasetWithComma filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);
            assertTrue("importHDF5DatasetWithComma filetree has dataset", (filetree.valueAt(2)).compareTo("testdstab")==0);

            JMenuItemFixture dataset4MenuItem = filetree.showPopupMenuAt(2).menuItemWithPath("Open");
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
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
        finally {
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
            mainFrameFixture.robot.waitForIdle();

            try {
                closeHDFFile(hdf_file, true);
            }
            catch (Exception ex) {}
        }
    }

    @Ignore 
    public void importHDF5DatasetWithComma() {
        File hdf_file = createImportHDF5Dataset("testdscomma");
        importHDF5Dataset(hdf_file, "DS64BITS.xtxt");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("importHDF5DatasetWithComma filetree shows:", filetree.target.getRowCount()==3);
            assertTrue("importHDF5DatasetWithComma filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            assertTrue("importHDF5DatasetWithComma filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);
            assertTrue("importHDF5DatasetWithComma filetree has dataset", (filetree.valueAt(2)).compareTo("testdscomma")==0);

            JMenuItemFixture dataset4MenuItem = filetree.showPopupMenuAt(2).menuItemWithPath("Open");
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
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        catch (AssertionError ae) {
            ae.printStackTrace();
        }
        finally {
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            mainFrameFixture.optionPane().buttonWithText("No").click();
            mainFrameFixture.robot.waitForIdle();
            try {
                closeHDFFile(hdf_file, true);
            }
            catch (Exception ex) {}
        }
    }
}
