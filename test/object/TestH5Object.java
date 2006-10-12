//package test.object;

import java.io.*;
import java.util.*;
import java.lang.reflect.Array;
import ncsa.hdf.object.*;
import ncsa.hdf.object.h5.*;
import ncsa.hdf.hdf5lib.*;
import ncsa.hdf.hdf5lib.exceptions.*;

/**
 * Test object at the ncsa.hdf.object package.
 * <p>
 *
 * @version 1.3.0 9/21/2006
 * @author Peter X. Cao
 *
 */
public class TestH5Object
{
    private final static String FILE_NAME = "G:\\temp\\TestH5Obejct.h5";
    private final static String LOG_FILE_NAME = "TestH5Obejct.log";
    private final static String GROUP_NAME = "simple_dataset";
    private final static String DATASET_NAME_SIMPLE = "simple_dataset";
    private final static H5File H5FILE = new H5File();
    private final static long DIM1 = 50;
    private final static long DIM2 = 10;
    private final static long[] DIMs = {DIM1, DIM2};
    private final static long[] CHUNKs = {DIM1/2, DIM2/2};
    private final static int RANK = 2;
    private final static int STRLEN = 20;

    private static String message = null;
    private static PrintStream out = null;
    private static H5File file = null;

    /**
     * Constructs an instance of TestH5Object.
     * @param out_stream the out stream for printing the test results.
     */
    public TestH5Object(PrintStream print_stream)
    {
        if (print_stream == null)
            out = System.out;
        else
            out = print_stream;
    }

    public void passed() {
        out.println("PASSED:\t"+message);
    }

    public void failed(Exception ex) {
        out.println("FAILED:\t"+message +"--"+ex.toString());
    }

    /**
     * Check if all the data values of two data buffers are the same
     * @param buf1 the first buffer to compare
     * @param buf2 the second buffer to compare
     * @return true if all the vlaues equal; otherwise, returns false.
     */
    public static boolean dataEquals(Object buf1, Object buf2) {

        // array cannot be null
        if (buf1 == null || buf2==null)
            return false;

        // must be array
        if (!buf1.getClass().isArray() || !buf2.getClass().isArray())
            return false;

        // must be the same kind of array
        String cname = buf1.getClass().getName();
        if (!cname.equals(buf2.getClass().getName()))
            return false;

        // must have the same size
        int n = Array.getLength(buf1);
        if (n != Array.getLength(buf2))
            return false;

        if (cname.equals("[I")) {
            int[] data1 = (int[])buf1;
            int[] data2 = (int[])buf2;
            for (int i=0; i<n; i++) {
                if (data1[i] != data2[i])
                    return false;
            }
        }
        else if (cname.equals("[F")) {
            float[] data1 = (float[])buf1;
            float[] data2 = (float[])buf2;
            for (int i=0; i<n; i++) {
                if (data1[i] != data2[i])
                    return false;
            }
        }
        else if (cname.equals("[B")) {
            byte[] data1 = (byte[])buf1;
            byte[] data2 = (byte[])buf2;
            for (int i=0; i<n; i++) {
                if (data1[i] != data2[i])
                    return false;
            }
        }
        else if (cname.equals("[Ljava.lang.String;")) {
            String[] data1 = (String[])buf1;
            String[] data2 = (String[])buf2;
            for (int i=0; i<n; i++) {
                if (!data1[i].equals(data2[i]))
                    return false;
            }
        }
        else // untested datatype
            return false;

        return true;
    }

    /**
     * Test H5File create() function.
     *
     * @param fname the name of the file to create
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_create(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        // test create file using the constructor
        message = "new H5File("+fname+", H5File.CREATE)";
        try {
            file = new H5File(fname, H5File.CREATE);
            file.open();
            file.close();
        } catch (Exception ex) { failed(ex); return 1;}
        passed();

        // test create file using the H5File.create
        message = "H5File.create("+fname+")";
        try {
            file = (H5File)H5FILE.create(fname);
        } catch (Exception ex) { failed(ex); return 1;}

        passed();
        return 0;
    }

    /**
     * Test H5File open() function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_open(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        int acc_ids[] = {H5File.CREATE, H5File.READ, H5File.WRITE};
        String msgs[] = { "H5File.open("+fname+", H5File.CREATE)",
                          "H5File.open("+fname+", H5File.READ)",
                          "H5File.open("+fname+", H5File.WRITE)"};

        // test create file using the H5File.open
        for (int i=0; i<3; i++)
        {
            message = msgs[i];
            try {
                file = (H5File)H5FILE.open(fname, acc_ids[i]);
                file.close();
            } catch (Exception ex) { failed(ex); return 1;}
            passed();
        }

        return 0;
    }

    /**
     * Test H5File createGroup() function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_createGroup(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        // create groups
        Group g0 = null;
        message = "H5File.createGroup and H5Group.create(\"/g0\", root_group)";
        try {
            g0 = file.createGroup("/g0", null);
        } catch (Exception ex) { failed(ex); return 1;}
        passed();

        Group g00 = null;
        message = "H5File.createGroup and H5Group.create(\"g0/g00\", root_group)";
        try {
            g00 = file.createGroup("g0/g00", null);
        } catch (Exception ex) { failed(ex); return 1;}
        passed();

        Group g01 = null;
        message = "H5File.createGroup and H5Group.create(\"/g0/g01/\", group_g0)";
        try {
            g01 = file.createGroup("/g0/g01/", g0);
        } catch (Exception ex) { failed(ex); return 1;}
        passed();

        return 0;
    }

    /**
     * Test H5File createDatatype() function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_createDatatype(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        int N = 5;
        int dtype_cls[] = {Datatype.CLASS_INTEGER, Datatype.CLASS_FLOAT,
            Datatype.CLASS_CHAR, Datatype.CLASS_STRING, Datatype.CLASS_ENUM};
        String dtype_names[] = {"INTEGER", "FLOAT", "CHAR", "STRING", "ENUM"};
        String msgs[] = { "H5File.createDatatype(..., "+dtype_names[0]+")",
                          "H5File.createDatatype(..., "+dtype_names[1]+")",
                          "H5File.createDatatype(..., "+dtype_names[2]+")",
                          "H5File.createDatatype(..., "+dtype_names[3]+")",
                          "H5File.createDatatype(..., "+dtype_names[4]+")"};

        message = "H5File.create("+fname+", H5File.CREATE)";
        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
        } catch (Exception ex) {failed(ex); return 1;}

        // create groups
        Datatype dtype = null;
        for (int i=0; i<N; i++)
        {
            message = msgs[i];
            try {
                dtype = file.createDatatype(dtype_cls[i],Datatype.NATIVE, Datatype.NATIVE, Datatype.NATIVE, dtype_names[i]);
            } catch (Exception ex) { failed(ex); return 1;}

            passed();
        }

        return 0;
    }

    /**
     * Test H5File createScalarDS function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_createScalarDS(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        int N=5;
        int dtype_cls[] = {Datatype.CLASS_INTEGER, Datatype.CLASS_FLOAT,
            Datatype.CLASS_CHAR, Datatype.CLASS_STRING, Datatype.CLASS_ENUM};
        int dtype_sizes[] = {-1, -1, 1, 80, -1};
        String names[] = {"INTEGER", "FLOAT", "CHAR", "STRING", "ENUM"};
        String msgs[] = { "H5File.createScalarDS and H5ScalarDS.create (..., "+names[0]+", ...)\n\tH5ScalarDS.read(), H5ScalarDS.write() "+names[0],
                          "H5File.createScalarDS and H5ScalarDS.create (..., "+names[1]+", ...)\n\tH5ScalarDS.read(), H5ScalarDS.write() "+names[1],
                          "H5File.createScalarDS and H5ScalarDS.create (..., "+names[2]+", ...)\n\tH5ScalarDS.read(), H5ScalarDS.write() "+names[2],
                          "H5File.createScalarDS and H5ScalarDS.create (..., "+names[3]+", ...)\n\tH5ScalarDS.read(), H5ScalarDS.write() "+names[3],
                          "H5File.createScalarDS and H5ScalarDS.create (..., "+names[4]+", ...)\n\tH5ScalarDS.read(), H5ScalarDS.write() "+names[4]};

        message = "H5File.create("+fname+", H5File.CREATE)";
        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        int size = (int) (DIM1*DIM2);
        int[] idata = new int[size];
        float[] fdata = new float[size];
        byte[] bdata = new byte[size];
        String[] str_data = new String[size];
        int[] edata = new int[size];

        Object[] all_data = new Object[N];
        all_data[0] = idata;
        all_data[1] = fdata;
        all_data[2] = bdata;
        all_data[3] = str_data;
        all_data[4] = edata;

        for (int i=0; i<size; i++) {
            idata[i] = i;
            fdata[i] = i+i/100.0f;
            bdata[i] = (byte)Math.IEEEremainder(i, 127);
            str_data[i] = "str"+i;
            edata[i] = (int)Math.IEEEremainder(i, 2);
        }

        // create groups
        Datatype dtype = null;
        Dataset dset = null;
        Object data_read = null;
        for (int i=0; i<N; i++)
        {
            message = msgs[i];
            try {
                dtype = new H5Datatype(dtype_cls[i], dtype_sizes[i], -1, -1);
                dset = file.createScalarDS(names[i], pgroup, dtype, DIMs, null, CHUNKs, 9, all_data[i]);
            } catch (Exception ex) { failed(ex); return 1;}

            // test data valuse
            try {
                dset.init();
                long[] selectedDims = dset.getSelectedDims();
                long[] dims = dset.getDims();
                long[] starts = dset.getStartDims();
                int rank = dset.getRank();
                // read all data
                for (int j=0; j<rank; j++) {
                    starts[j] = 0;
                    selectedDims[j] = dims[j];
                }

                data_read = dset.read();
            } catch (Exception ex) { failed(ex); return 1;}

            if ( !dataEquals(all_data[i], data_read) ) {
                failed(new HDF5LibraryException("Incorrect data values in file"));
                return 1;
            }

            passed();
        }

        return 0;
    }

    /**
     * Test H5File createLink function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_createLink(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "H5File.createLink(Group parentGroup, String name, HObject currentObj)";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        String gname = "Group";
        Group grp = null;
        try {
            grp = file.createGroup(gname, null);
        } catch (Exception ex) { failed(ex); return 1;}

        HObject hobj = null;
        try {
            hobj = file.createLink(pgroup, "link to "+gname, grp);
        } catch (Exception ex) { failed(ex); return 1;}

        long oid[] = grp.getOID();
        if (!hobj.equalsOID(oid))
        {
            failed(new HDF5LibraryException("link to the wrong object"));
            return 1;
        }

        passed();


        return 0;
    }

    /**
     * Test H5File createImage function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_createImage(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "H5File.createImage(...) and H5File.createImageAttributes()";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        int size = (int) (DIM1*DIM2);
        byte[] bdata = new byte[size];
        for (int i=0; i<size; i++) {
            bdata[i] = (byte)Math.IEEEremainder(i, 127);
        }

        // create groups
        Datatype dtype = null;
        Dataset dset = null;
        Object data_read = null;
        try {
            dtype = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1);
            dset = file.createImage("Image", pgroup, dtype, DIMs, null, CHUNKs, 9, 1, -1, bdata);
        } catch (Exception ex) { failed(ex); return 1;}

        // test data valuse
        try {
            data_read = dset.read();
        } catch (Exception ex) { failed(ex); return 1;}

        if ( !dataEquals(bdata, data_read) ) {
            failed(new HDF5LibraryException("Incorrect data values in file"));
            return 1;
        }

        passed();
        return 0;
    }

    /**
     * Test H5File createCompoundDS function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_createCompoundDS(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "H5File.createCompoundDS(...) and H5CompoundDS.create(...)\n\tH5CompoundDS.read(), H5CompoundDS.write() {INTEGER, FLOAT, STRING}";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        int size = (int) (DIM1*DIM2);
        int[] idata = new int[size];
        float[] fdata = new float[size];
        String[] sdata = new String[size];

        for (int i=0; i<size; i++) {
            idata[i] = i;
            fdata[i] = i+i/100.0f;
            sdata[i] = "str"+i;
        }
        Vector data = new Vector();
        data.add(0, idata);
        data.add(1, fdata);
        data.add(2, sdata);

        // create groups
        Datatype[]  mdtypes = new H5Datatype[3];
        String[] mnames = {"int", "float", "string"};
        Dataset dset = null;
        try {
            mdtypes[0] = new H5Datatype(Datatype.CLASS_INTEGER, -1, -1, -1);
            mdtypes[1] = new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1);
            mdtypes[2] = new H5Datatype(Datatype.CLASS_STRING, STRLEN, -1, -1);
            dset = file.createCompoundDS("/CompoundDS", pgroup, DIMs, null, CHUNKs, 9, mnames, mdtypes, null, data);
        } catch (Exception ex) { failed(ex); return 1;}

        // test data valuse
        List data_read = null;
        try {
            data_read = (List)dset.read();
        } catch (Exception ex) { failed(ex); return 1;}

        if ( !dataEquals(idata, data_read.get(0)) ||
             !dataEquals(fdata, data_read.get(1)) ||
             !dataEquals(sdata, data_read.get(2))) {
                failed(new HDF5LibraryException("Incorrect data values in file"));
                return 1;
            }

        // create dataset at non root group
        Group g0 = null;
        try {
            g0 = file.createGroup("/g0", null);
        } catch (Exception ex) { failed(ex); return 1;}
        try {
            mdtypes[0] = new H5Datatype(Datatype.CLASS_INTEGER, -1, -1, -1);
            mdtypes[1] = new H5Datatype(Datatype.CLASS_FLOAT, -1, -1, -1);
            mdtypes[2] = new H5Datatype(Datatype.CLASS_STRING, STRLEN, -1, -1);
            dset = file.createCompoundDS("/g0/CompoundDS/", g0, DIMs, null, CHUNKs, 9, mnames, mdtypes, null, data);
        } catch (Exception ex) { failed(ex); return 1;}

        passed();
        return 0;
    }

    /**
     * Test H5File copy function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_copy(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "H5File.copy(...) and H5File.get(...): dataset, group and attributes";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        int size = (int) (DIM1*DIM2);
        byte[] bdata = new byte[size];
        for (int i=0; i<size; i++) {
            bdata[i] = (byte)Math.IEEEremainder(i, 127);
        }

        Group grp = null;
        Datatype dtype = null;
        Dataset dset = null;
        try {
            grp = file.createGroup("/Group", null);
            dtype = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1);
            dset = file.createImage("Dataset", pgroup, dtype, DIMs, null, CHUNKs, 9, 1, -1, bdata);
        } catch (Exception ex) { failed(ex); return 1;}

        try {
            file.copy(dset, grp);
            file.copy(grp, pgroup, "~Group");
        } catch (Exception ex) { failed(ex); return 1;}

        // load the copied object into memory
        file.reloadTree(pgroup);

        try {
            Dataset dset2 = (Dataset) file.get("/~Group/Dataset");
            if ( !dataEquals(dset.read(), dset2.read()) ) {
                failed(new HDF5LibraryException("Incorrect data values in file"));
                return 1;
            }
        } catch (Exception ex) { failed(ex); return 1;}

        passed();
        return 0;
    }

    /**
     * Test H5File getAttribute function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_getAttribute(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "H5File.getAttribute(...), H5File.writeAttribute(...)\n\tHObject.getMetadata(), HObject.writeMetadata()";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        int size = (int) (DIM1*DIM2);
        byte[] bdata = new byte[size];
        for (int i=0; i<size; i++) {
            bdata[i] = (byte)Math.IEEEremainder(i, 127);
        }

        Datatype dtype = null;
        Dataset dset = null;
        Object data_read = null;
        try {
            dtype = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1);
            dset = file.createImage("Image", pgroup, dtype, DIMs, null, CHUNKs, 9, 1, -1, bdata);
        } catch (Exception ex) { failed(ex); return 1;}

        try {
            int did = dset.open();
            List attrs = file.getAttribute(did);
            try {dset.close(did);} catch (Exception ex2) {}
            if (attrs==null || attrs.size()<1) {
                failed(new HDF5LibraryException("failed to attached attributes"));
                return 1;
            }
        } catch (Exception ex) { failed(ex); return 1;}

        passed();
        return 0;
    }

    /**
     * Test H5File getHObject() function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5File_getHObject(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "H5File.getHObject(String filename, String path)\n\tH5File.getHObject(fname#//Dataset)";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        int size = (int) (DIM1*DIM2);
        byte[] bdata = new byte[size];
        for (int i=0; i<size; i++) {
            bdata[i] = (byte)Math.IEEEremainder(i, 127);
        }

        Datatype dtype = null;
        Dataset dset = null;
        Object data_read = null;
        try {
            dtype = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1);
            dset = file.createScalarDS("Dataset", pgroup, dtype, DIMs, null, CHUNKs, 9, bdata);
        } catch (Exception ex) { failed(ex); return 1;}
        try { file.close(); } catch (Exception ex) {}

        long[] oid = dset.getOID();
        Dataset dset2 = null;
        try {
            dset2 = (Dataset)FileFormat.getHObject(fname, "/Dataset");
        } catch (Exception ex) { failed(ex); return 1;}

        if (!dset2.equalsOID(oid)) {
            failed(new HDF5LibraryException("get() returns the wrong object"));
            try { dset2.getFileFormat().close(); } catch (Exception ex) {}
            return 1;
        }
        try { dset2.getFileFormat().close(); } catch (Exception ex) {}

        try {
            dset2 = (Dataset)FileFormat.getHObject(fname+"#//Dataset");
        } catch (Exception ex) { failed(ex); return 1;}

        if (!dset2.equalsOID(oid)) {
            failed(new HDF5LibraryException("get() returns the wrong object"));
            try { dset2.getFileFormat().close(); } catch (Exception ex) {}
            return 1;
        }
        try { dset2.getFileFormat().close(); } catch (Exception ex) {}

        passed();

        return 0;
    }

    /**
     * Test HObject getFID()  function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_HObject_getFID(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        int fid = 0;
        Group pgroup = null;
        message = "Group.getFID()\n\tDataset.getFID()";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            fid = file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        if (fid != pgroup.getFID()) {
            failed(new HDF5LibraryException("wrong object ID in group"));
            return 1;
        }

        Datatype dtype = null;
        Dataset dset = null;
        try {
            dtype = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1);
            dset = file.createScalarDS("Dataset", pgroup, dtype, DIMs, null, CHUNKs, 9, null);
        } catch (Exception ex) { failed(ex); return 1;}

        if (fid != dset.getFID()) {
            failed(new HDF5LibraryException("wrong object ID in dataset"));
            return 1;
        }

        passed();
        return 0;
    }

    /**
     * Test HObject getName()  function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_HObject_getName(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        int fid = 0;
        Group pgroup = null;
        message = "Group.getName(), Group.getPath(), Group.getFullName()\n\tDataset.getName(), Dataset.getPath(), Dataset.getFullName()";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            fid = file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        Group grp = null;
        try {
            grp = file.createGroup("/Group", null);
        } catch (Exception ex) {failed(ex); return 1;}

        Group grp2 = null;
        try {
            grp2 = file.createGroup("/Group/Group2", grp);
        } catch (Exception ex) { failed(ex); return 1;}

        if (!grp2.getName().endsWith("Group2")) {
            failed(new HDF5LibraryException("wrong name for the group"));
            return 1;
        }

        if (!grp2.getPath().endsWith("/Group/")) {
            failed(new HDF5LibraryException("wrong path for the group"));
            return 1;
        }

        if (!grp2.getFullName().endsWith("/Group/Group2")) {
            failed(new HDF5LibraryException("wrong full path for the group"));
            return 1;
        }

        Datatype dtype = null;
        Dataset dset = null;
        try {
            dtype = new H5Datatype(Datatype.CLASS_INTEGER, 1, -1, -1);
            dset = file.createScalarDS("Dataset", pgroup, dtype, DIMs, null, CHUNKs, 9, null);
        } catch (Exception ex) { failed(ex); return 1;}

        if (!dset.getName().endsWith("Dataset")) {
            failed(new HDF5LibraryException("wrong name for the dataset"));
            return 1;
        }

        if (!dset.getPath().endsWith("/")) {
            failed(new HDF5LibraryException("wrong path for the dataset"));
            return 1;
        }

        if (!dset.getFullName().endsWith("/Dataset")) {
            failed(new HDF5LibraryException("wrong full path for the dataset"));
            return 1;
        }

        passed();
        return 0;
    }

    /**
     * Test Group isRoot function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_Group_isRoot(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "Group.isRoot()";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        if (!pgroup.isRoot())
        {
            failed(new HDF5LibraryException("failed to test root group"));
            return 1;
        }

        Group grp = null;
        try {
            grp = file.createGroup("/Group", null);
        } catch (Exception ex) { failed(ex); return 1;}
        try { file.close(); } catch (Exception ex) {}

        try {
            grp = (Group)FileFormat.getHObject(fname, "/Group");
        } catch (Exception ex) {failed(ex); return 1;}

        if (grp.isRoot())
        {
            failed(new HDF5LibraryException("failed to test non-root group"));
            return 1;
        }
        try { grp.getFileFormat().close(); } catch (Exception ex) {}

        passed();
        return 0;
    }

    /**
     * Test Group getParent function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_Group_getParent(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "Group.getParent()";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        if(pgroup.getParent() != null) {
            failed(new HDF5Exception("the parent of root group is not null"));
            return 1;
        }

        // create groups
        Group g0 = null;
        try {
            g0 = file.createGroup("/g0", pgroup);
        } catch (Exception ex) { failed(ex); return 1;}

        if(g0.getParent() == null) {
            failed(new HDF5Exception("the parent of the group is null"));
            return 1;
        }

        passed();
        return 0;
    }

    /**
     * Test Dataset byteToString function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_Dataset_byteToString(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "Dataset.byteToString() and Dataset.stringToByte()\n\tH5ScalarDS.readBytes()";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        String[] sdata = new String[(int)DIM1];

        for (int i=0; i<DIM1; i++) {
            sdata[i] = "str"+i;
        }

        // create groups
        Datatype dtype = null;
        Dataset dset = null;
        String[] sdata_read = null;
        byte[] bdata_read = null;
        long[] dims = {DIM1};
       byte[] bdata = null;

        try {
            dtype = new H5Datatype(Datatype.CLASS_STRING, STRLEN, -1, -1);
            dset = file.createScalarDS("String", pgroup, dtype, dims, null, null, -1, sdata);
        } catch (Exception ex) { failed(ex); return 1;}

        // test data valuse
        try {
            dset.setConvertByteToString(false);
            bdata = dset.readBytes();
            bdata_read = (byte[])dset.read();
            sdata_read = dset.byteToString(bdata_read, STRLEN);
            bdata_read = dset.stringToByte(sdata, STRLEN);
        } catch (Exception ex) { failed(ex); return 1;}

        if ( !dataEquals(bdata, bdata_read) ) {
            failed(new HDF5LibraryException("Incorrect data from stringToByte()"));
            return 1;
        }

        if ( !dataEquals(sdata, sdata_read) ) {
            failed(new HDF5LibraryException("Incorrect data from byteToString()"));
            return 1;
        }

        passed();
        return 0;
    }

    /**
     * Test H5Datatype toNative() function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_H5Datatype_toNative(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        Datatype dtype = null;
        int tid;

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        message = "H5Datatype.toNative() and H5Datatype.fromNative(): INTEGER";
        try {
            dtype = file.createDatatype(Datatype.CLASS_INTEGER,-1, -1, -1);
            tid = dtype.toNative();
            if (!H5.H5Tequal(tid, HDF5Constants.H5T_NATIVE_INT)) {
                failed(new HDF5Exception("Failed to convert native integer"));
                return 1;
            }
        } catch (Exception ex) { failed(ex); return 1;}
        passed();

        message = "H5Datatype.toNative() and H5Datatype.fromNative(): FLOAT";
        try {
            dtype = file.createDatatype(Datatype.CLASS_FLOAT,-1, -1, -1);
            tid = dtype.toNative();
            if (!H5.H5Tequal(tid, HDF5Constants.H5T_NATIVE_FLOAT)) {
                failed(new HDF5Exception("Failed to convert native float"));
                return 1;
            }
        } catch (Exception ex) { failed(ex); return 1;}
        passed();

        message = "H5Datatype.toNative() and H5Datatype.fromNative(): CHAR";
        try {
            dtype = file.createDatatype(Datatype.CLASS_CHAR, 1, -1, -1);
            tid = dtype.toNative();
            if (!H5.H5Tequal(tid, HDF5Constants.H5T_NATIVE_CHAR)) {
                failed(new HDF5Exception("Failed to convert native char"));
                return 1;
            }
        } catch (Exception ex) { failed(ex); return 1;}
        passed();

        message = "H5Datatype.toNative() and H5Datatype.fromNative(): STRING";
        try {
            dtype = file.createDatatype(Datatype.CLASS_STRING, STRLEN, -1, -1);
            tid = dtype.toNative();
            int tid2 = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
            H5.H5Tset_size(tid2, STRLEN);
            H5.H5Tset_strpad(tid2, HDF5Constants.H5T_STR_NULLPAD);
            if (!H5.H5Tequal(tid, tid2)) {
                failed(new HDF5Exception("Failed to convert string"));
                return 1;
            }
        } catch (Exception ex) { failed(ex); return 1;}
        passed();

        return 0;
    }


    /**
     * Test ***func name*** function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int test_temp(String fname)
    {
        //clean what's left from the previous test
        try {file.close();} catch (Exception ex) {}

        Group pgroup = null;
        message = "***********func name*********";

        try {
            file = (H5File)H5FILE.open(fname, H5File.CREATE);
            file.open();
            pgroup = (Group)file.get("/");
        } catch (Exception ex) {failed(ex); return 1;}

        passed();
        return 0;
    }

    /*****************************************************************************
     * Main routine for the testing. Use "-f" to save the test result to a log file.
     * If "-f" flag is specified, the test results will printed to System.out.
     * <p>
     * For example, "test.object.TestH5Object -f test.log" to save the test results
     *     at file test.log.
     * @param args
     ****************************************************************************/
    public static void main(String[] args)
    {
        PrintStream printStream = null;
        int numOfFails = 0;

        int n = args.length;
        if (n > 1 && args[0].equals("-f"))
        {
            try {
                printStream = new PrintStream(new BufferedOutputStream(
                        new FileOutputStream(args[1])));
            } catch (FileNotFoundException ex)
            {
                printStream = null;
                ex.printStackTrace();
            }
        }

        TestH5Object test = new TestH5Object(printStream);

        numOfFails += test.test_H5File_create(FILE_NAME);
        numOfFails += test.test_H5File_open(FILE_NAME);
        numOfFails += test.test_H5File_createGroup(FILE_NAME);
        numOfFails += test.test_H5File_createDatatype(FILE_NAME);
        numOfFails += test.test_H5File_createLink(FILE_NAME);
        numOfFails += test.test_H5File_createImage(FILE_NAME);
        numOfFails += test.test_H5File_createScalarDS(FILE_NAME);
        numOfFails += test.test_H5File_createCompoundDS(FILE_NAME);
        numOfFails += test.test_H5File_copy(FILE_NAME);
        numOfFails += test.test_H5File_getAttribute(FILE_NAME);
        numOfFails += test.test_H5File_getHObject(FILE_NAME);
        numOfFails += test.test_HObject_getFID(FILE_NAME);
        numOfFails += test.test_HObject_getName(FILE_NAME);
        numOfFails += test.test_Group_isRoot(FILE_NAME);
        numOfFails += test.test_Group_getParent(FILE_NAME);
        numOfFails += test.test_Dataset_byteToString(FILE_NAME);
        numOfFails += test.test_H5Datatype_toNative(FILE_NAME);

        try { file.close(); } catch (Exception ex) {}
        try { H5FILE.close(); } catch (Exception ex) {}

        if (numOfFails<=0)
            test.out.println("\n\nAll tests passed.");
        else
            test.out.println("\n\n*** "+numOfFails+" tests failed.");

    }
}
