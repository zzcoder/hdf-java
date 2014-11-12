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
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestTreeViewNewVLDatatypes {
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
        File hdf_file = new File("testvldt.h5");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testvlattr.h5");
        if (hdf_file.exists())
            hdf_file.delete();
        hdf_file = new File("testvldataset.h5");
        if (hdf_file.exists())
            hdf_file.delete();

        mainFrameFixture.robot.waitForIdle();
        //mainFrameFixture.requireNotVisible();
        mainFrameFixture.cleanUp();
    }

    @Test 
    public void createNewHDF5VLDatatype() {
        File hdf_file = createHDF5File("testvldt");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("createNewHDF5VLDatatype filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("createNewHDF5VLDatatype filetree has file", (filetree.valueAt(0)).compareTo("testvldt.h5")==0);

            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New","Datatype");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("dtname").setText("testvldtname");
            mainFrameFixture.dialog().comboBox("dtclass").selectItem("VLEN_INTEGER");
            mainFrameFixture.dialog().comboBox("dtsize").selectItem("16");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("createNewHDF5VLDatatype filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("createNewHDF5VLDatatype filetree has file", (filetree.valueAt(0)).compareTo("testvldt.h5")==0);
            assertTrue("createNewHDF5VLDatatype filetree has dataset", (filetree.valueAt(1)).compareTo("testvldtname")==0);
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
    public void createNewHDF5VLDataset() {
        File hdf_file = createHDF5File("testvldataset");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("createNewHDF5VLDataset filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("createNewHDF5VLDataset filetree has file", (filetree.valueAt(0)).compareTo("testvldataset.h5")==0);

            JMenuItemFixture groupMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("New","Dataset");
            mainFrameFixture.robot.waitForIdle();
            
            groupMenuItem.requireVisible();
            groupMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            mainFrameFixture.dialog().textBox("datasetname").setText("testvldatasetname");
            mainFrameFixture.dialog().comboBox("datasetclass").selectItem("VLEN_FLOAT");
            mainFrameFixture.dialog().comboBox("datasetsize").selectItem("32");
            mainFrameFixture.dialog().button("OK").click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("createNewHDF5VLDataset filetree shows:", filetree.target.getRowCount()==2);
            assertTrue("createNewHDF5VLDataset filetree has file", (filetree.valueAt(0)).compareTo("testvldataset.h5")==0);
            assertTrue("createNewHDF5VLDataset filetree has dataset", (filetree.valueAt(1)).compareTo("testvldatasetname")==0);
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
    public void createNewHDF5VLAttribute() {
        File hdf_file = createHDF5File("testvlattr");

        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            assertTrue("createNewHDF5VLAttribute filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("createNewHDF5VLAttribute filetree has file", (filetree.valueAt(0)).compareTo("testvlattr.h5")==0);

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
            propDialog.button("Add").click();
            mainFrameFixture.robot.waitForIdle();

            propDialog.textBox("attrname").setText("testvlattrname");
            propDialog.comboBox("attrclass").selectItem("VLEN_STRING");
            propDialog.textBox("attrvalue").setText("ABC");
            propDialog.button("OK").click();
            mainFrameFixture.robot.waitForIdle();
            propDialog.button("Close").click();
            mainFrameFixture.robot.waitForIdle();

            filetree = mainFrameFixture.tree().focus();
            assertTrue("createNewHDF5VLAttribute filetree shows:", filetree.target.getRowCount()==1);
            assertTrue("createNewHDF5VLAttribute filetree has file", (filetree.valueAt(0)).compareTo("testvlattr.h5")==0);
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
