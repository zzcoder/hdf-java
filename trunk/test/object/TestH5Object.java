package test.object;

import java.io.*;
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
    private String MESSAGE = null;
    private PrintStream out = null;

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
        out.println("PASSED:\t "+MESSAGE);
    }

    public void failed(Exception ex) {
        out.println("FAILED:\t "+MESSAGE +"--"+ex.toString());
    }

    /**
     * Main routine for the testing. Use "-f" to save the test result to a log file.
     * If "-f" flag is specified, the test results will printed to System.out.
     * <p>
     * For example, "test.object.TestH5Object -f test.log" to save the test results
     *     at file test.log.
     * @param args
     */
    public static void main(String[] args)
    {
        PrintStream out = null;
        int numOfFails = 0;

        int n = args.length;
        if (n > 2 && args[1].equals("-f"))
        {
            try {
                out = new PrintStream(new BufferedOutputStream( new FileOutputStream(args[2])));
            } catch (FileNotFoundException ex)
            {
                out = null;
                ex.printStackTrace();
            }
        }

        TestH5Object test = new TestH5Object(out);

        numOfFails += test.testH5File_create(FILE_NAME);
        numOfFails += test.testH5File_open(FILE_NAME);
        numOfFails += test.testH5File_createGroup(FILE_NAME);
        numOfFails += test.testH5File_createDatatype(FILE_NAME);

        try { H5FILE.close(); } catch (Exception ex) {}
    }

    /**
     * Test H5File create() function.
     *
     * @param fname the name of the file to create
     * @return zero if successful; otherwise returns one
     */
    public int testH5File_create(String fname)
    {
        H5File file = null;

        // test create file using the constructor
        MESSAGE = "new H5File("+fname+", H5File.CREATE)";
        try {
            file = new H5File(fname, H5File.CREATE);
            file.open();
            file.close();
        } catch (Exception ex) { failed(ex); return 1;}
        passed();


        // test create file using the H5File.create
        MESSAGE = "H5File.create("+fname+")";
        try {
            file = (H5File)H5FILE.create(fname);
            file.close();
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
    public int testH5File_open(String fname)
    {
        H5File file = null;
        int acc_ids[] = {H5File.CREATE, H5File.READ, H5File.WRITE};
        String msgs[] = { "H5File.open("+fname+", H5File.CREATE)",
                          "H5File.open("+fname+", H5File.READ)",
                          "H5File.open("+fname+", H5File.WRITE)"};

        // test create file using the H5File.open
        for (int i=0; i<3; i++)
        {
            MESSAGE = msgs[i];
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
    public int testH5File_createGroup(String fname)
    {
        H5File file = null;
        String grp_names[] = {"/g0", "/g0/g00", "/g0/g01"};
        String msgs[] = { "H5File.createGroup("+grp_names[0]+", Group pgroup)",
                          "H5File.createGroup("+grp_names[1]+", Group pgroup)",
                          "H5File.createGroup("+grp_names[2]+", Group pgroup)"};

        MESSAGE = "H5File.create("+fname+", H5File.CREATE)";
        try { file = (H5File)H5FILE.open(fname, H5File.CREATE); }
        catch (Exception ex) {failed(ex); return 1;}

        // create groups
        Group grp = null;
        for (int i=0; i<3; i++)
        {
            MESSAGE = msgs[i];
            try {
                grp = file.createGroup(grp_names[i], null);
            } catch (Exception ex) { grp = null; }

            if (grp == null) {
                failed(new HDF5LibraryException("cannot create group"));
                return 1;
            }
            passed();
        }

        try {file.close();} catch (Exception ex) {}

        return 0;
    }

    /**
     * Test H5File createDatatype() function.
     *
     * @param fname the name of the file to open
     * @return zero if successful; otherwise returns one
     */
    public int testH5File_createDatatype(String fname)
    {
        H5File file = null;
        int N = 5;
        int dtype_cls[] = {Datatype.CLASS_INTEGER, Datatype.CLASS_FLOAT,
            Datatype.CLASS_CHAR, Datatype.CLASS_STRING, Datatype.CLASS_ENUM};
        String dtype_names[] = {"INTEGER", "FLOAT", "CHAR", "STRING", "ENUM"};
        String msgs[] = { "H5File.createDatatype(int tclass, int tsize, int torder, int tsign, "+dtype_names[0]+")",
                          "H5File.createDatatype(int tclass, int tsize, int torder, int tsign, "+dtype_names[1]+")",
                          "H5File.createDatatype(int tclass, int tsize, int torder, int tsign, "+dtype_names[2]+")",
                          "H5File.createDatatype(int tclass, int tsize, int torder, int tsign, "+dtype_names[3]+")",
                          "H5File.createDatatype(int tclass, int tsize, int torder, int tsign, "+dtype_names[4]+")"};

        MESSAGE = "H5File.create("+fname+", H5File.CREATE)";
        try { file = (H5File)H5FILE.open(fname, H5File.CREATE); }
        catch (Exception ex) {failed(ex); return 1;}

        // create groups
        Datatype dtype = null;
        for (int i=0; i<N; i++)
        {
            MESSAGE = msgs[i];
            try {
                dtype = file.createDatatype(dtype_cls[i], -1, -1, -1, dtype_names[i]);
            } catch (Exception ex) { dtype = null; }

            if (dtype == null) {
                failed(new HDF5LibraryException("cannot create named datatype"));
                return 1;
            }
            passed();
        }

        try {file.close();} catch (Exception ex) {}

        return 0;
    }

}
