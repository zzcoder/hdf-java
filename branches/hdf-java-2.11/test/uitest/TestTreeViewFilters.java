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
import org.fest.swing.fixture.JLabelFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestTreeViewFilters {
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
    public void openHDF5Filters() {
        File hdf_file = openHDF5File("tfilters", 17);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Filters filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==17);
            assertTrue("openHDF5Filters filetree has file", (filetree.valueAt(0)).compareTo("tfilters.h5")==0);
            assertTrue("openHDF5Filters filetree has dataset", (filetree.valueAt(10)).compareTo("fletcher32")==0);

            JMenuItemFixture dataset14MenuItem = filetree.showPopupMenuAt(14).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset14MenuItem.requireVisible();
            dataset14MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset14table = mainFrameFixture.table("data");
            JTableCellFixture cell14 = dataset14table.cell(row(0).column(0));
            cell14.requireValue("0");
            cell14 = dataset14table.cell(row(0).column(9));
            cell14.requireValue("9");
            cell14 = dataset14table.cell(row(11).column(4));
            cell14.requireValue("114");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            JMenuItemFixture dataset15MenuItem = filetree.showPopupMenuAt(15).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset15MenuItem.requireVisible();
            dataset15MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset15table = mainFrameFixture.table("data");
            JTableCellFixture cell15 = dataset15table.cell(row(9).column(1));
            cell15.requireValue("91");
            cell15 = dataset15table.cell(row(19).column(1));
            cell15.requireValue("191");
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
                closeHDFFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }

    @Test 
    public void checkHDF5Filters() {
        File hdf_file = openHDF5File("tfilters", 17);

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("openHDF5Filters filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==17);
            assertTrue("openHDF5Filters filetree has file", (filetree.valueAt(0)).compareTo("tfilters.h5")==0);
            assertTrue("openHDF5Filters filetree has dataset", (filetree.valueAt(10)).compareTo("fletcher32")==0);

            JMenuItemFixture dataset11MenuItem = filetree.showPopupMenuAt(11).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();
            
            dataset11MenuItem.requireVisible();
            dataset11MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            DialogFixture dataset11props = mainFrameFixture.dialog();
            dataset11props.requireVisible();
            
            JTabbedPaneFixture tabPane = dataset11props.tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes");
            tabPane.selectTab("General");
            mainFrameFixture.robot.waitForIdle();

            JLabelFixture labelChunk = dataset11props.label("chunkdata");
            labelChunk.requireText("10 X 5");
            JLabelFixture labelComp = dataset11props.label("compressiondata");
            labelComp.requireText("1.000:1");
            JLabelFixture labelFilt = dataset11props.label("filterdata");
            labelFilt.requireText("USERDEFINED myfilter(405): 5, 6");
            JLabelFixture labelStore = dataset11props.label("storagedata");
            labelStore.requireText("800, allocation time: Incremental");
            JLabelFixture labelFill = dataset11props.label("fillvaluedata");
            labelFill.requireText("NONE");
            
            dataset11props.button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            JMenuItemFixture dataset15MenuItem = filetree.showPopupMenuAt(15).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();
            
            dataset15MenuItem.requireVisible();
            dataset15MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            DialogFixture dataset15props = mainFrameFixture.dialog();
            dataset15props.requireVisible();
            
            tabPane = dataset15props.tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes");
            tabPane.selectTab("General");
            mainFrameFixture.robot.waitForIdle();

            labelChunk = dataset15props.label("chunkdata");
            labelChunk.requireText("10 X 5");
            labelComp = dataset15props.label("compressiondata");
            labelComp.requireText("1.000:1");
            labelFilt = dataset15props.label("filterdata");
            labelFilt.requireText("SHUFFLE: Nbytes = 4");
            labelStore = dataset15props.label("storagedata");
            labelStore.requireText("800, allocation time: Incremental");
            labelFill = dataset15props.label("fillvaluedata");
            labelFill.requireText("NONE");

            dataset15props.button("Close").click();
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
                closeHDFFile(hdf_file, false);
            }
            catch (Exception ex) {}
        }
    }
}
