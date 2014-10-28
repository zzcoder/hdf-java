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
import org.junit.Test;


public class TestTreeViewNewMenu {
    private static FrameFixture mainFrameFixture;
    // the version of the HDFViewer
    private static String VERSION = "2.11";

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
    public static void finishApplication() {
        File hdf_file = new File("testgrp.h5");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testds.h5");
        if (hdf_file.exists())
            hdf_file.delete();

        mainFrameFixture.robot.waitForIdle();
        //mainFrameFixture.requireNotVisible();
        mainFrameFixture.cleanUp();
    }

    @Test 
    public void createNewHDF5Group() {
        File hdf_file = createHDF5File("testgrp");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("File-Dataset-HDF5 filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("File-Dataset-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testgrp.h5")==0);

            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New","Group");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("groupname").setText("testgroupname");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Dataset-HDF5 filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("File-Dataset-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testgrp.h5")==0);
            assertTrue("File-Dataset-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);
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
    public void createNewHDF5Dataset() {
        File hdf_file = createHDF5File("testds");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("File-Dataset-HDF5 filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("File-Dataset-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);

            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New","Group");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("groupname").setText("testgroupname");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Dataset-HDF5 filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("File-Dataset-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            assertTrue("File-Dataset-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);

            JMenuItemFixture datasetMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("New","Dataset");
            mainFrameFixture.robot.waitForIdle();
            
            datasetMenuItem.requireVisible();
            datasetMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("datasetname").setText("testdatasetname");
            mainFrameFixture.dialog().textBox("currentsize").setText("4 x 4");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Dataset-HDF5 filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==2);

            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("File-Dataset-HDF5 filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==3);
            assertTrue("File-Dataset-HDF5 filetree has file", (filetree.valueAt(0)).compareTo("testds.h5")==0);
            assertTrue("File-Dataset-HDF5 filetree has group", (filetree.valueAt(1)).compareTo("testgroupname")==0);
            assertTrue("File-Dataset-HDF5 filetree has dataset", (filetree.valueAt(2)).compareTo("testdatasetname")==0);

            JMenuItemFixture dataset2MenuItem = filetree.showPopupMenuAt(2).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset2MenuItem.requireVisible();
            dataset2MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset2table = mainFrameFixture.table("data");
            JTableCellFixture cell = dataset2table.cell(row(0).column(0));
            cell.enterValue("1");
            cell = dataset2table.cell(row(0).column(1));
            cell.enterValue("2");
            cell = dataset2table.cell(row(0).column(2));
            cell.enterValue("3");
            cell = dataset2table.cell(row(0).column(3));
            cell.enterValue("4");
            dataset2table.requireContents(
              new String[][] {
                  { "1", "2", "3", "4" }, 
                  { "0", "0", "0", "0" },
                  { "0", "0", "0", "0" },
                  { "0", "0", "0", "0" }
              }
            );

            JMenuItemFixture fileMenuItem = mainFrameFixture.menuItemWithPath("File", "Save");
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
        finally {
            try {
                closeFile(hdf_file, true);
            }
            catch (Exception ex) {}
        }
    }
}
