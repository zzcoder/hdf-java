package test.hdf5lib;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5G_info_t;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class TestH5G {
    private static final boolean is16 = H5.isAPI16;
    private static final String H5_FILE = "test.h5";
    private static final String[] GROUPS = { "/G1", "/G1/G11", "/G1/G12",
            "/G1/G11/G111", "/G1/G11/G112", "/G1/G11/G113", "/G1/G11/G114" };
    int H5fid = -1;

    private final int _createGroup(int fid, String name) {
        int gid = -1;
        try {
            if (is16)
                gid = H5.H5Gcreate(fid, name, 0);
            else
                gid = H5.H5Gcreate2(fid, name, HDF5Constants.H5P_DEFAULT,
                        HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gcreate: " + err);
        }

        return gid;
    }

    private final int _openGroup(int fid, String name) {
        int gid = -1;
        try {
            if (is16)
                gid = H5.H5Gopen(fid, name);
            else
                gid = H5.H5Gopen2(fid, name, HDF5Constants.H5P_DEFAULT);
            ;
        }
        catch (Throwable err) {
            gid = -1;
            err.printStackTrace();
            fail("H5.H5Gopen: " + err);
        }

        return gid;
    }

    private final void _deleteFile(String filename) {
        File file = new File(filename);

        if (file.exists()) {
            try {
                file.delete();
            }
            catch (SecurityException e) {
                ;// e.printStackTrace();
            }
        }
    }

    @Before
    public void createH5file()
            throws HDF5LibraryException, NullPointerException {
        H5fid = H5.H5Fcreate(H5_FILE, HDF5Constants.H5F_ACC_TRUNC,
                HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);

        int gid = -1;

        for (int i = 0; i < GROUPS.length; i++) {
            gid = _createGroup(H5fid, GROUPS[i]);
            assertTrue(gid > 0);
            try {
                H5.H5Gclose(gid);
            }
            catch (Exception ex) {
            }
        }

        H5.H5Fflush(H5fid, HDF5Constants.H5F_SCOPE_LOCAL);
    }

    @After
    public void deleteH5file() throws HDF5LibraryException {
        if (H5fid > 0) {
            H5.H5Fclose(H5fid);
        }
        _deleteFile(H5_FILE);
    }

    @Test
    public void testH5Gopen() {
        for (int i = 0; i < GROUPS.length; i++) {
            int gid = _openGroup(H5fid, GROUPS[i]);
            assertTrue(gid > 0);
            try {
                H5.H5Gclose(gid);
            }
            catch (Exception ex) {
            }
        }
    }

    @Test
    public void testH5Gget_create_plist() {
        int gid = -1;
        int pid = -1;

        for (int i = 0; i < GROUPS.length; i++) {
            gid = _openGroup(H5fid, GROUPS[i]);
            assertTrue(gid > 0);

            try {
                pid = H5.H5Gget_create_plist(gid);
            }
            catch (Throwable err) {
                err.printStackTrace();
                fail("H5.H5Gget_create_plist: " + err);
            }
            assertTrue(pid > 0);

            try {
                H5.H5Gclose(gid);
            }
            catch (Exception ex) {
            }
        }
    }

    @Test
    public void testH5Gget_info() {
        H5G_info_t info = null;

        for (int i = 0; i < GROUPS.length; i++) {

            try {
                info = H5.H5Gget_info(H5fid);
            }
            catch (Throwable err) {
                err.printStackTrace();
                fail("H5.H5Gget_info: " + err);
            }
            assertNotNull(info);
        }
    }

    @Test
    public void testH5Gget_info_by_name() {
        H5G_info_t info = null;

        for (int i = 0; i < GROUPS.length; i++) {
            try {
                info = H5.H5Gget_info_by_name(H5fid, GROUPS[i],
                        HDF5Constants.H5P_DEFAULT);
            }
            catch (Throwable err) {
                err.printStackTrace();
                fail("H5.H5Gget_info_by_name: " + err);
            }
            assertNotNull(info);
        }
    }

    @Test
    public void testH5Gget_info_by_idx() {
        H5G_info_t info = null;
        for (int i = 0; i < 2; i++) {
            try {
                info = H5.H5Gget_info_by_idx(H5fid, "/G1",
                        HDF5Constants.H5_INDEX_NAME, HDF5Constants.H5_ITER_INC,
                        i, HDF5Constants.H5P_DEFAULT);
            }
            catch (Throwable err) {
                err.printStackTrace();
                fail("H5.H5Gget_info_by_idx: " + err);
            }
            assertNotNull(info);
        }
    }

    @Test
    public void testH5Gget_obj_info_all() {
        H5G_info_t info = null;

        int gid = _openGroup(H5fid, GROUPS[0]);

        try {
            info = H5.H5Gget_info(gid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gget_info: " + err);
        }
        try {
            H5.H5Gclose(gid);
        }
        catch (Exception ex) {
        }
        assertNotNull(info);
        assertTrue("number of links is empty", info.nlinks > 0);
        String objNames[] = new String[(int) info.nlinks];
        int objTypes[] = new int[(int) info.nlinks];
        int lnkTypes[] = new int[(int) info.nlinks];
        long objRefs[] = new long[(int) info.nlinks];

        int names_found = 0;
        try {
            names_found = H5.H5Gget_obj_info_all(H5fid, GROUPS[0], objNames,
                    objTypes, lnkTypes, objRefs);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gget_obj_info_all: " + err);
        }

        assertTrue("number found[" + names_found + "] different than expected["
                + objNames.length + "]", names_found == objNames.length);
        for (int i = 0; i < objNames.length; i++) {
            assertNotNull("name #" + i + " does not exist", objNames[i]);
            assertTrue(objNames[i].length() > 0);
        }
    }

    @Test
    public void testH5Gget_obj_info_all_gid() {
        H5G_info_t info = null;

        int gid = _openGroup(H5fid, GROUPS[0]);

        try {
            info = H5.H5Gget_info(gid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gget_info: " + err);
        }
        assertNotNull(info);
        assertTrue("number of links is empty", info.nlinks > 0);
        String objNames[] = new String[(int) info.nlinks];
        long objRefs[] = new long[(int) info.nlinks];
        int lnkTypes[] = new int[(int) info.nlinks];
        int objTypes[] = new int[(int) info.nlinks];

        int names_found = 0;
        try {
            names_found = H5.H5Gget_obj_info_all(gid, null, objNames, objTypes, lnkTypes,
                    objRefs);
        }
        catch (Throwable err) {
            try {
                H5.H5Gclose(gid);
            }
            catch (Exception ex) {
            }
            err.printStackTrace();
            fail("H5.H5Gget_obj_info_all: " + err);
        }
        try {
            H5.H5Gclose(gid);
        }
        catch (Exception ex) {
        }

        assertTrue("number found[" + names_found + "] different than expected["
                + objNames.length + "]", names_found == objNames.length);
        for (int i = 0; i < objNames.length; i++) {
            assertNotNull("name #" + i + " does not exist", objNames[i]);
            assertTrue(objNames[i].length() > 0);
        }
    }

    @Test
    public void testH5Gget_obj_info_all_gid2() {
        H5G_info_t info = null;

        int gid = _openGroup(H5fid, GROUPS[1]);

        try {
            info = H5.H5Gget_info(gid);
        }
        catch (Throwable err) {
            err.printStackTrace();
            fail("H5.H5Gget_info: " + err);
        }
        assertNotNull(info);
        assertTrue("number of links is empty", info.nlinks > 0);
        String objNames[] = new String[(int) info.nlinks];
        long objRefs[] = new long[(int) info.nlinks];
        int lnkTypes[] = new int[(int) info.nlinks];
        int objTypes[] = new int[(int) info.nlinks];

        int names_found = 0;
        try {
            names_found = H5.H5Gget_obj_info_all(gid, null, objNames, objTypes, lnkTypes,
                    objRefs);
        }
        catch (Throwable err) {
            try {
                H5.H5Gclose(gid);
            }
            catch (Exception ex) {
            }
            err.printStackTrace();
            fail("H5.H5Gget_obj_info_all: " + err);
        }
        try {
            H5.H5Gclose(gid);
        }
        catch (Exception ex) {
        }

        assertTrue("number found[" + names_found + "] different than expected["
                + objNames.length + "]", names_found == objNames.length);
        for (int i = 0; i < objNames.length; i++) {
            assertNotNull("name #" + i + " does not exist", objNames[i]);
            assertTrue(objNames[i].length() > 0);
        }
    }

    @Test
    public void testH5Gget_obj_info_max() {
        int gid = _openGroup(H5fid, GROUPS[0]);
        int groups_max_size = GROUPS.length + 1;
        String objNames[] = new String[groups_max_size];
        int objTypes[] = new int[groups_max_size];
        int lnkTypes[] = new int[groups_max_size];
        long objRefs[] = new long[groups_max_size];

        int names_found = 0;
        try {
            names_found = H5.H5Gget_obj_info_max(gid, objNames, objTypes, lnkTypes,
                    objRefs, groups_max_size);
        }
        catch (Throwable err) {
            try {
                H5.H5Gclose(gid);
            }
            catch (Exception ex) {
            }
            err.printStackTrace();
            fail("H5.H5Gget_obj_info_max: " + err);
        }
        try {
            H5.H5Gclose(gid);
        }
        catch (Exception ex) {
        }

        // expected number does not include root group
        assertTrue("number found[" + names_found + "] different than expected["
                + (GROUPS.length - 1) + "]", names_found == (GROUPS.length - 1));
        for (int i = 0; i < GROUPS.length-1; i++) {
            assertNotNull("name #"+i+" does not exist",objNames[i]);
            assertTrue(objNames[i].length()>0);
        }
    }

    @Test
    public void testH5Gget_obj_info_max_limit() {
        int gid = _openGroup(H5fid, GROUPS[0]);
        int groups_max_size = GROUPS.length - 3;
        String objNames[] = new String[groups_max_size];
        int objTypes[] = new int[groups_max_size];
        int lnkTypes[] = new int[groups_max_size];
        long objRefs[] = new long[groups_max_size];

        int names_found = 0;
        try {
            names_found = H5.H5Gget_obj_info_max(gid, objNames, objTypes, lnkTypes,
                    objRefs, groups_max_size);
        }
        catch (Throwable err) {
            try {
                H5.H5Gclose(gid);
            }
            catch (Exception ex) {
            }
            err.printStackTrace();
            fail("H5.H5Gget_obj_info_max: " + err);
        }
        try {
            H5.H5Gclose(gid);
        }
        catch (Exception ex) {
        }

        assertTrue("number found[" + names_found + "] different than expected["
                + groups_max_size + "]", names_found == groups_max_size);
        for (int i = 0; i < objNames.length; i++) {
            assertNotNull("name #" + i + " does not exist", objNames[i]);
            assertTrue(objNames[i].length() > 0);
        }
    }

}