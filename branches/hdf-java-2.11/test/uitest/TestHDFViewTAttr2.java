package test.uitest;

import static org.fest.swing.data.TableCell.row;
import static org.fest.swing.finder.WindowFinder.findFrame;
import static org.fest.swing.launcher.ApplicationLauncher.application;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import org.fest.swing.core.BasicComponentFinder;
import org.fest.swing.core.BasicRobot;
import org.fest.swing.core.ComponentFinder;
import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.finder.JFileChooserFinder;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.swing.fixture.JInternalFrameFixture;
import org.fest.swing.fixture.JMenuItemFixture;
import org.fest.swing.fixture.JTabbedPaneFixture;
import org.fest.swing.fixture.JTableCellFixture;
import org.fest.swing.fixture.JTableFixture;
import org.fest.swing.fixture.JTreeFixture;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestHDFViewTAttr2 {
    private static FrameFixture mainFrameFixture;
    // the version of the HDFViewer
    private static String VERSION = "2.11";
    private static File hdf5file = null;

    private static File openHDF5File(String name, int initrows) {
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
    
    private static void closeHDFFile(File hdf_file, boolean delete_file) {
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

        hdf5file = openHDF5File("tattr2", 4);
        JTreeFixture filetree = mainFrameFixture.tree().focus();
        filetree.requireVisible();
        assertTrue("openTAttr2 filetree shows:"+filetree.target.getRowCount(), filetree.target.getRowCount()==4);
        assertTrue("openTAttr2 filetree has file", (filetree.valueAt(0)).compareTo("tattr2.h5")==0);
        assertTrue("openTAttr2 filetree has dataset", (filetree.valueAt(1)).compareTo("dset")==0);
        assertTrue("openTAttr2 filetree has group", (filetree.valueAt(2)).compareTo("g1")==0);
        assertTrue("openTAttr2 filetree has group", (filetree.valueAt(3)).compareTo("g2")==0);
    }

    @AfterClass
    public static void finishApplication() {
        mainFrameFixture.robot.waitForIdle();
        try {
            closeHDFFile(hdf5file, false);
        }
        catch (Exception ex) {}
        //mainFrameFixture.requireNotVisible();
        mainFrameFixture.cleanUp();
    }

    @Test 
    public void openTAttr2GroupArray() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupArray filetree has dataset", (filetree.valueAt(4)).compareTo("array")==0);
            JMenuItemFixture dataset4MenuItem = filetree.showPopupMenuAt(4).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset4MenuItem.requireVisible();
            dataset4MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset4table = mainFrameFixture.table("data");
            JTableCellFixture cell4 = dataset4table.cell(row(0).column(0));
            cell4.requireValue("1, 2, 3");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupArray filetree has dataset", (filetree.valueAt(5)).compareTo("array2D")==0);
            JMenuItemFixture dataset5MenuItem = filetree.showPopupMenuAt(5).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset5MenuItem.requireVisible();
            dataset5MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset5table = mainFrameFixture.table("data");
            JTableCellFixture cell5 = dataset5table.cell(row(1).column(1));
            cell5.requireValue("10, 11, 12");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupArray filetree has dataset", (filetree.valueAt(6)).compareTo("array3D")==0);
            JMenuItemFixture dataset6MenuItem = filetree.showPopupMenuAt(6).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset6MenuItem.requireVisible();
            dataset6MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset6table = mainFrameFixture.table("data");
            JTableCellFixture cell6 = dataset6table.cell(row(2).column(2));
            cell6.requireValue("49, 50, 51");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2GroupBitfield() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupBitfield filetree has dataset", (filetree.valueAt(7)).compareTo("bitfield")==0);
            JMenuItemFixture dataset7MenuItem = filetree.showPopupMenuAt(7).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset7MenuItem.requireVisible();
            dataset7MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset7table = mainFrameFixture.table("data");
            JTableCellFixture cell7 = dataset7table.cell(row(0).column(0));
            cell7.requireValue("01");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupBitfield filetree has dataset", (filetree.valueAt(8)).compareTo("bitfield2D")==0);
            JMenuItemFixture dataset8MenuItem = filetree.showPopupMenuAt(8).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset8MenuItem.requireVisible();
            dataset8MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset8table = mainFrameFixture.table("data");
            JTableCellFixture cell8 = dataset8table.cell(row(1).column(1));
            cell8.requireValue("04");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupBitfield filetree has dataset", (filetree.valueAt(9)).compareTo("bitfield3D")==0);
            JMenuItemFixture dataset9MenuItem = filetree.showPopupMenuAt(9).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset9MenuItem.requireVisible();
            dataset9MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset9table = mainFrameFixture.table("data");
            JTableCellFixture cell9 = dataset9table.cell(row(2).column(2));
            cell9.requireValue("11");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2GroupCompound() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupCompound filetree has dataset", (filetree.valueAt(10)).compareTo("compound")==0);
            JMenuItemFixture dataset10MenuItem = filetree.showPopupMenuAt(10).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset10MenuItem.requireVisible();
            dataset10MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset10table = mainFrameFixture.table("data");
            JTableCellFixture cell10 = dataset10table.cell(row(0).column(0));
            cell10.requireValue("1");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupCompound filetree has dataset", (filetree.valueAt(11)).compareTo("compound2D")==0);
            JMenuItemFixture dataset11MenuItem = filetree.showPopupMenuAt(11).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset11MenuItem.requireVisible();
            dataset11MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset11table = mainFrameFixture.table("data");
            JTableCellFixture cell11 = dataset11table.cell(row(1).column(1));
            cell11.requireValue("6.0");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupCompound filetree has dataset", (filetree.valueAt(12)).compareTo("compound3D")==0);
            JMenuItemFixture dataset12MenuItem = filetree.showPopupMenuAt(12).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset12MenuItem.requireVisible();
            dataset12MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset12table = mainFrameFixture.table("data");
            JTableCellFixture cell12 = dataset12table.cell(row(2).column(2));
            cell12.requireValue("29");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2GroupEnum() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupEnum filetree has dataset", (filetree.valueAt(13)).compareTo("enum")==0);
            JMenuItemFixture dataset13MenuItem = filetree.showPopupMenuAt(13).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset13MenuItem.requireVisible();
            dataset13MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset13table = mainFrameFixture.table("data");
            JTableCellFixture cell13 = dataset13table.cell(row(0).column(0));
            cell13.requireValue("RED");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupEnum filetree has dataset", (filetree.valueAt(14)).compareTo("enum2D")==0);
            JMenuItemFixture dataset14MenuItem = filetree.showPopupMenuAt(14).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset14MenuItem.requireVisible();
            dataset14MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset14table = mainFrameFixture.table("data");
            JTableCellFixture cell14 = dataset14table.cell(row(1).column(1));
            cell14.requireValue("RED");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupEnum filetree has dataset", (filetree.valueAt(15)).compareTo("enum3D")==0);
            JMenuItemFixture dataset15MenuItem = filetree.showPopupMenuAt(15).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset15MenuItem.requireVisible();
            dataset15MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset15table = mainFrameFixture.table("data");
            JTableCellFixture cell15 = dataset15table.cell(row(2).column(2));
            cell15.requireValue("RED");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2GroupFloat() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupFloat filetree has dataset", (filetree.valueAt(16)).compareTo("float")==0);
            JMenuItemFixture dataset16MenuItem = filetree.showPopupMenuAt(16).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset16MenuItem.requireVisible();
            dataset16MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset16table = mainFrameFixture.table("data");
            JTableCellFixture cell16 = dataset16table.cell(row(0).column(0));
            cell16.requireValue("1.0");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupFloat filetree has dataset", (filetree.valueAt(17)).compareTo("float2D")==0);
            JMenuItemFixture dataset17MenuItem = filetree.showPopupMenuAt(17).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset17MenuItem.requireVisible();
            dataset17MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset17table = mainFrameFixture.table("data");
            JTableCellFixture cell17 = dataset17table.cell(row(1).column(1));
            cell17.requireValue("4.0");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupFloat filetree has dataset", (filetree.valueAt(18)).compareTo("float3D")==0);
            JMenuItemFixture dataset18MenuItem = filetree.showPopupMenuAt(18).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset18MenuItem.requireVisible();
            dataset18MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset18table = mainFrameFixture.table("data");
            JTableCellFixture cell18 = dataset18table.cell(row(2).column(2));
            cell18.requireValue("17.0");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2GroupInteger() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupInteger filetree has dataset", (filetree.valueAt(19)).compareTo("integer")==0);
            JMenuItemFixture dataset19MenuItem = filetree.showPopupMenuAt(19).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset19MenuItem.requireVisible();
            dataset19MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset19table = mainFrameFixture.table("data");
            JTableCellFixture cell19 = dataset19table.cell(row(0).column(0));
            cell19.requireValue("1");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupInteger filetree has dataset", (filetree.valueAt(20)).compareTo("integer2D")==0);
            JMenuItemFixture dataset20MenuItem = filetree.showPopupMenuAt(20).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset20MenuItem.requireVisible();
            dataset20MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset20table = mainFrameFixture.table("data");
            JTableCellFixture cell20 = dataset20table.cell(row(1).column(1));
            cell20.requireValue("4");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupInteger filetree has dataset", (filetree.valueAt(21)).compareTo("integer3D")==0);
            JMenuItemFixture dataset21MenuItem = filetree.showPopupMenuAt(21).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset21MenuItem.requireVisible();
            dataset21MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset21table = mainFrameFixture.table("data");
            JTableCellFixture cell21 = dataset21table.cell(row(2).column(2));
            cell21.requireValue("17");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2GroupOpaque() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupOpaque filetree has dataset", (filetree.valueAt(22)).compareTo("opaque")==0);
            JMenuItemFixture dataset22MenuItem = filetree.showPopupMenuAt(22).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset22MenuItem.requireVisible();
            dataset22MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset22table = mainFrameFixture.table("data");
            JTableCellFixture cell22 = dataset22table.cell(row(0).column(0));
            cell22.requireValue("01");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupOpaque filetree has dataset", (filetree.valueAt(23)).compareTo("opaque2D")==0);
            JMenuItemFixture dataset23MenuItem = filetree.showPopupMenuAt(23).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset23MenuItem.requireVisible();
            dataset23MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset23table = mainFrameFixture.table("data");
            JTableCellFixture cell23 = dataset23table.cell(row(1).column(1));
            cell23.requireValue("04");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupOpaque filetree has dataset", (filetree.valueAt(24)).compareTo("opaque3D")==0);
            JMenuItemFixture dataset24MenuItem = filetree.showPopupMenuAt(24).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset24MenuItem.requireVisible();
            dataset24MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset24table = mainFrameFixture.table("data");
            JTableCellFixture cell24 = dataset24table.cell(row(2).column(2));
            cell24.requireValue("11");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2GroupReference() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupReference filetree has dataset", (filetree.valueAt(25)).compareTo("reference")==0);
            JMenuItemFixture dataset25MenuItem = filetree.showPopupMenuAt(25).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset25MenuItem.requireVisible();
            dataset25MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset25table = mainFrameFixture.table("data");
            JTableCellFixture cell25 = dataset25table.cell(row(0).column(0));
            cell25.requireValue("976");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupReference filetree has dataset", (filetree.valueAt(26)).compareTo("reference2D")==0);
            JMenuItemFixture dataset26MenuItem = filetree.showPopupMenuAt(26).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset26MenuItem.requireVisible();
            dataset26MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset26table = mainFrameFixture.table("data");
            JTableCellFixture cell26 = dataset26table.cell(row(1).column(1));
            cell26.requireValue("976");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupReference filetree has dataset", (filetree.valueAt(27)).compareTo("reference3D")==0);
            JMenuItemFixture dataset27MenuItem = filetree.showPopupMenuAt(27).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset27MenuItem.requireVisible();
            dataset27MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset27table = mainFrameFixture.table("data");
            JTableCellFixture cell27 = dataset27table.cell(row(2).column(2));
            cell27.requireValue("976");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2GroupString() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupString filetree has dataset", (filetree.valueAt(28)).compareTo("string")==0);
            JMenuItemFixture dataset28MenuItem = filetree.showPopupMenuAt(28).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset28MenuItem.requireVisible();
            dataset28MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset28table = mainFrameFixture.table("TextView");
            JTableCellFixture cell28 = dataset28table.cell(row(0).column(0));
            cell28.requireValue("ab");
            mainFrameFixture.menuItemWithPath("Text", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupString filetree has dataset", (filetree.valueAt(29)).compareTo("string2D")==0);
            JMenuItemFixture dataset29MenuItem = filetree.showPopupMenuAt(29).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset29MenuItem.requireVisible();
            dataset29MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
//            JTableFixture dataset29table = mainFrameFixture.table("TextView");
//            JTableCellFixture cell29 = dataset29table.cell(row(1).column(1));
//            cell29.requireValue("gh");
            mainFrameFixture.menuItemWithPath("Text", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupString filetree has dataset", (filetree.valueAt(30)).compareTo("string3D")==0);
            JMenuItemFixture dataset30MenuItem = filetree.showPopupMenuAt(30).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset30MenuItem.requireVisible();
            dataset30MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
//            JTableFixture dataset30table = mainFrameFixture.table("TextView");
//            JTableCellFixture cell30 = dataset30table.cell(row(2).column(2));
//            cell30.requireValue("IJ");
            mainFrameFixture.menuItemWithPath("Text", "Close").click();
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
    public void openTAttr2GroupVlen() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupVlen filetree has dataset", (filetree.valueAt(31)).compareTo("vlen")==0);
            JMenuItemFixture dataset31MenuItem = filetree.showPopupMenuAt(31).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset31MenuItem.requireVisible();
            dataset31MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset31table = mainFrameFixture.table("data");
            JTableCellFixture cell31 = dataset31table.cell(row(0).column(0));
            cell31.requireValue("1");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupVlen filetree has dataset", (filetree.valueAt(32)).compareTo("vlen2D")==0);
            JMenuItemFixture dataset32MenuItem = filetree.showPopupMenuAt(32).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset32MenuItem.requireVisible();
            dataset32MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset32table = mainFrameFixture.table("data");
            JTableCellFixture cell32 = dataset32table.cell(row(1).column(1));
            cell32.requireValue("4, 5");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupVlen filetree has dataset", (filetree.valueAt(33)).compareTo("vlen3D")==0);
            JMenuItemFixture dataset33MenuItem = filetree.showPopupMenuAt(33).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset33MenuItem.requireVisible();
            dataset33MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset33table = mainFrameFixture.table("data");
            JTableCellFixture cell33 = dataset33table.cell(row(2).column(2));
            cell33.requireValue("30, 31, 32");
            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
    public void openTAttr2Attribute() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();

            JMenuItemFixture propMenuItem = filetree.showPopupMenuAt(1).menuItemWithPath("Show Properties");
            mainFrameFixture.robot.waitForIdle();
            
            propMenuItem.requireVisible();
            propMenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            DialogFixture propDialog = mainFrameFixture.dialog();
            propDialog.requireVisible();
            
            JTabbedPaneFixture tabPane = propDialog.tabbedPane();
            tabPane.requireVisible();
            tabPane.requireTabTitles("General","Attributes");
            tabPane.selectTab("Attributes");
            mainFrameFixture.robot.waitForIdle();
            
            tabPane.requireVisible();
            JTableFixture attrTable = propDialog.table("attributes");
            JTableCellFixture cell = attrTable.cell(row(0).column(0));
            cell.requireValue("array");
            cell = attrTable.cell(row(1).column(0));
            cell.requireValue("array2D");
            cell = attrTable.cell(row(2).column(0));
            cell.requireValue("array3D");
            cell = attrTable.cell(row(3).column(0));
            cell.requireValue("bitfield");
            cell = attrTable.cell(row(4).column(0));
            cell.requireValue("bitfield2D");
            cell = attrTable.cell(row(5).column(0));
            cell.requireValue("bitfield3D");
            cell = attrTable.cell(row(6).column(0));
            cell.requireValue("compound");
            cell = attrTable.cell(row(7).column(0));
            cell.requireValue("compound2D");
            cell = attrTable.cell(row(8).column(0));
            cell.requireValue("compound3D");
            cell = attrTable.cell(row(9).column(0));
            cell.requireValue("enum");
            cell = attrTable.cell(row(10).column(0));
            cell.requireValue("enum2D");
            cell = attrTable.cell(row(11).column(0));
            cell.requireValue("enum3D");
            cell = attrTable.cell(row(12).column(0));
            cell.requireValue("float");
            cell = attrTable.cell(row(13).column(0));
            cell.requireValue("float2D");
            cell = attrTable.cell(row(14).column(0));
            cell.requireValue("float3D");
            cell = attrTable.cell(row(15).column(0));
            cell.requireValue("integer");
            cell = attrTable.cell(row(16).column(0));
            cell.requireValue("integer2D");
            cell = attrTable.cell(row(17).column(0));
            cell.requireValue("integer3D");
            cell = attrTable.cell(row(18).column(0));
            cell.requireValue("opaque");
            cell = attrTable.cell(row(19).column(0));
            cell.requireValue("opaque2D");
            cell = attrTable.cell(row(20).column(0));
            cell.requireValue("opaque3D");
            cell = attrTable.cell(row(21).column(0));
            cell.requireValue("reference");
            cell = attrTable.cell(row(22).column(0));
            cell.requireValue("reference2D");
            cell = attrTable.cell(row(23).column(0));
            cell.requireValue("reference3D");
            cell = attrTable.cell(row(24).column(0));
            cell.requireValue("string");
            cell = attrTable.cell(row(25).column(0));
            cell.requireValue("string2D");
            cell = attrTable.cell(row(26).column(0));
            cell.requireValue("string3D");
            cell = attrTable.cell(row(27).column(0));
            cell.requireValue("vlen");
            cell = attrTable.cell(row(28).column(0));
            cell.requireValue("vlen2D");
            cell = attrTable.cell(row(29).column(0));
            cell.requireValue("vlen3D");
            JTableCellFixture celldata = attrTable.cell(row(0).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6");
            celldata = attrTable.cell(row(1).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18");
            celldata = attrTable.cell(row(2).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72");
            celldata = attrTable.cell(row(3).column(1));
            celldata.requireValue("1, 2");
            celldata = attrTable.cell(row(4).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6");
            celldata = attrTable.cell(row(5).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24");
            celldata = attrTable.cell(row(6).column(1));
            celldata.requireValue("{1, 2} ,  {3, 4}");
            celldata = attrTable.cell(row(7).column(1));
            celldata.requireValue("{1, 2} ,  {3, 4} ,  {5, 6} ,  {7, 8} ,  {9, 10} ,  {11, 12}");
            celldata = attrTable.cell(row(8).column(1));
            celldata.requireValue("{1, 2} ,  {3, 4} ,  {5, 6} ,  {7, 8} ,  {9, 10} ,  {11, 12} ,  {13, 14} ,  {15, 16} ,  {17, 18} ,  {19, 20} ,  {21, 22} ,  {23, 24} ,  {25, 26} ,  {27, 28} ,  {29, 30} ,  {31, 32} ,  {33, 34} ,  {35, 36} ,  {37, 38} ,  {39, 40} ,  {41, 42} ,  {43, 44} ,  {45, 46} ,  {47, 48}");
            celldata = attrTable.cell(row(9).column(1));
            celldata.requireValue("RED, RED");
            celldata = attrTable.cell(row(10).column(1));
            celldata.requireValue("RED, RED, RED, RED, RED, RED");
            celldata = attrTable.cell(row(11).column(1));
            celldata.requireValue("RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED, RED");
            celldata = attrTable.cell(row(12).column(1));
            celldata.requireValue("1.0, 2.0");
            celldata = attrTable.cell(row(13).column(1));
            celldata.requireValue("1.0, 2.0, 3.0, 4.0, 5.0, 6.0");
            celldata = attrTable.cell(row(14).column(1));
            celldata.requireValue("1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0, 11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0, 21.0, 22.0, 23.0, 24.0");
            celldata = attrTable.cell(row(15).column(1));
            celldata.requireValue("1, 2");
            celldata = attrTable.cell(row(16).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6");
            celldata = attrTable.cell(row(17).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24");
            celldata = attrTable.cell(row(18).column(1));
            celldata.requireValue("1, 2");
            celldata = attrTable.cell(row(19).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6");
            celldata = attrTable.cell(row(20).column(1));
            celldata.requireValue("1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24");
            celldata = attrTable.cell(row(21).column(1));
            celldata.requireValue("976, 976");
            celldata = attrTable.cell(row(22).column(1));
            celldata.requireValue("976, 976, 976, 976, 976, 976");
            celldata = attrTable.cell(row(23).column(1));
            celldata.requireValue("976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976, 976");
            celldata = attrTable.cell(row(24).column(1));
            celldata.requireValue("ab, de");
            celldata = attrTable.cell(row(25).column(1));
            celldata.requireValue("ab, cd, ef, gh, ij, kl");
            celldata = attrTable.cell(row(26).column(1));
            celldata.requireValue("ab, cd, ef, gh, ij, kl, mn, pq, rs, tu, vw, xz, AB, CD, EF, GH, IJ, KL, MN, PQ, RS, TU, VW, XZ");
            celldata = attrTable.cell(row(27).column(1));
            celldata.requireValue("1, 2, 3");
            celldata = attrTable.cell(row(28).column(1));
            celldata.requireValue("0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11");
            celldata = attrTable.cell(row(29).column(1));
            celldata.requireValue("0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59");
            propDialog.button("Close").click();
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
    public void openTAttr2GroupReferenceAsTable() {
        try {
            JTreeFixture filetree = mainFrameFixture.tree().focus();
            filetree.requireVisible();
            JMenuItemFixture expandMenuItem = filetree.showPopupMenuAt(0).menuItemWithPath("Expand All");
            mainFrameFixture.robot.waitForIdle();
            
            expandMenuItem.requireVisible();
            expandMenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            assertTrue("openTAttr2GroupReference filetree has dataset", (filetree.valueAt(27)).compareTo("reference3D")==0);
            JMenuItemFixture dataset27MenuItem = filetree.showPopupMenuAt(27).menuItemWithPath("Open");
            mainFrameFixture.robot.waitForIdle();
            
            dataset27MenuItem.requireVisible();
            dataset27MenuItem.click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset27table = mainFrameFixture.table("data");
            JTableCellFixture cell22 = dataset27table.cell(row(2).column(2));
            cell22.requireValue("976");
            
            cell22.select();
            JMenuItemFixture dataset22MenuItem = cell22.showPopupMenu().menuItemWithPath("Show As Table");
            dataset22MenuItem.requireVisible();
            dataset22MenuItem.click();
            mainFrameFixture.robot.waitForIdle();

            ComponentFinder finder = BasicComponentFinder.finderWithCurrentAwtHierarchy();
            GenericTypeMatcher<JInternalFrame> matcher = new GenericTypeMatcher<JInternalFrame>(JInternalFrame.class) {
                   protected boolean isMatching(JInternalFrame frame) {
                       System.out.println("Find reference3D frame is "+frame.getName());
                       return "/g2/reference3D".equals(frame.getName());
                   }
            };
            JInternalFrame tableview = (JInternalFrame)finder.findByName("/g2/reference3D");
            JInternalFrameFixture framefix = new JInternalFrameFixture(mainFrameFixture.robot, tableview);
            framefix.moveToFront();
            framefix.menuItemWithPath("Table", "Close").click();
            mainFrameFixture.robot.waitForIdle();
            
            JTableFixture dataset22table = mainFrameFixture.table("data");
            JTableCellFixture cell1 = dataset22table.cell(row(1).column(0));
            cell1.requireValue("0");

            mainFrameFixture.menuItemWithPath("Table", "Close").click();
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
