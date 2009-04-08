/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of HDF Java Products. The full HDF Java copyright       *
 * notice, including terms governing use, modification, and redistribution,  *
 * is contained in the file, COPYING.  COPYING can be found at the root of   *
 * the source code distribution tree. You can also access it online  at      *
 * http://www.hdfgroup.org/products/licenses.html.  If you do not have       *
 * access to the file, you may request a copy from help@hdfgroup.org.        *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package hdf.h5;

import hdf.h5.exceptions.HDF5LibraryException;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *  This class is the Java interface for the HDF5 library.
 *  <div>
 *  This code is the called by Java programs to access the
 *  entry points of the HDF5 1.2 library.
 *  Each routine wraps a single HDF5 entry point, generally with the
 *  arguments and return codes analogous to the C interface.
 *  </div>
 *  <div>
 *  For details of the HDF5 library, see the HDF5 Documentation at:
 *  <a href="http://hdf.ncsa.uiuc.edu/HDF5/">http://hdf.ncsa.uiuc.edu/HDF5/</a>
 *  <div>
 *  <hr>
 *  <h2>Mapping of arguments for Java</h2>
 *  <div>
 *  In general, arguments to the HDF Java API are straightforward
 *  translations from the 'C' API described in the HDF Reference
 *  Manual.
 *  </div>
 *  <div>
 *  <center>
 *  <table border=2 cellpadding=2>
 *  <caption><b>HDF-5 C types to Java types</b>   </caption>
 *  <tr><td> <b>HDF-5</b>       </td><td> <b>Java</b>    </td></tr>
 *  <tr><td> H5T_NATIVE_INT     </td><td> int, Integer   </td></tr>
 *  <tr><td> H5T_NATIVE_SHORT   </td><td> short, Short   </td></tr>
 *  <tr><td> H5T_NATIVE_FLOAT   </td><td> float, Float   </td></tr>
 *  <tr><td> H5T_NATIVE_DOUBLE  </td><td> double, Double </td></tr>
 *  <tr><td> H5T_NATIVE_CHAR    </td><td> byte, Byte     </td></tr>
 *  <tr><td> H5T_C_S1           </td><td> java.lang.String    </td></tr>
 *  <tr><td> void * <BR>(i.e., pointer to `Any')     </td><td> Special -- see HDFArray </td></tr>
 *  </table>
 *  </center>
 *  </div>
 *  <center>
 *  <h2>General Rules for Passing Arguments and Results</h2>
 *  </center>
 *  <div>
 *  In general, arguments passed <b>IN</b> to Java are the analogous basic types, as above.
 *  The exception is for arrays, which are discussed below.
 *  </div>
 *  <div>
 *  The <i>return value</i> of Java methods is also the analogous type, as above.
 *  A major exception to that rule is that all HDF functions that
 *  return SUCCEED/FAIL are declared <i>boolean</i> in the Java version, rather than
 *  <i>int</i> as in the C.
 *  Functions that return a value or else FAIL are declared
 *  the equivalent to the C function.
 *  However, in most cases the Java method will raise an exception instead
 *  of returning an error code.  See
 *  <a href="#ERRORS">Errors and Exceptions</a> below.
 *  </div>
 *  <div>
 *  Java does not support pass by reference of arguments, so
 *  arguments that are returned through <b>OUT</b> parameters
 *  must be wrapped in an object or array.
 *  The Java API for HDF consistently wraps arguments in
 *  arrays.
 *  </div>
 *  <div>
 *  For instance, a function that returns two integers is
 *  declared:
 *  </div>
 *  <div>
 *  <pre>
 *       h_err_t HDF5dummy( int *a1, int *a2)
 *  </pre>
 *  For the Java interface, this would be declared:
 *  </div>
 *  <div>
 *  <pre>
 *       public synchronized static native int HDF5dummy( int args[] );
 *  </pre>
 *  where <i>a1</i> is <i>args[0]</i>
 *  and <i>a2</i> is <i>args[1]</i>, and would be invoked:
 *  </div>
 *  <div>
 *  <pre>
 *       H5.HDF5dummy( a );
 *  </pre>
 *  </div>
 *  <div>
 *  All the routines where this convention is used will have
 *  specific documentation of the details, given below.
 *  </div>
 *  <div>
 *  <a NAME="ARRAYS">
 *  <h2>Arrays</h2>
 *  </a>
 *  </div>
 *  <div>
 *  HDF5 needs to read and write multi-dimensional arrays
 *  of any number type (and records).
 *  The HDF5 API describes the layout of the source and destination,
 *  and the data for the array passed as a block of bytes, for instance,
 *  </div>
 *  <div>
 *  <pre>
 *      herr_t H5Dread(int fid, int filetype, int memtype, int memspace,
 *      void * data);
 *  </pre>
 *  </div>
 *  <div>
 *  where ``void *'' means that the data may be any valid numeric
 *  type, and is a contiguous block of bytes that is the data
 *  for a multi-dimensional array.  The other parameters describe the
 *  dimensions, rank, and datatype of the array on disk (source) and
 *  in memory (destination).
 *  </div>
 *  <div>
 *  For Java, this ``ANY'' is a problem, as the type of data must
 *  always be declared.  Furthermore, multidimensional arrays
 *  are definitely <i>not</i> layed out contiguously
 *  in memory.
 *  It would be infeasible to declare a separate routine for
 *  every combination of number type and dimensionality.
 *  For that reason, the
 *  <a href="./hdf.h5.HDFArray.html><b>HDFArray</b></a>
 *  class is used to
 *  discover the type, shape, and size of the data array
 *  at run time, and to convert to and from a contiguous array
 *  of bytes in synchronized static native C order.
 *  </div>
 *  <div>
 *  The upshot is that any Java array of numbers (either primitive
 *  or sub-classes of type <b>Number</b>) can be passed as
 *  an ``Object'', and the Java API will translate to and from
 *  the appropriate packed array of bytes needed by the C library.
 *  So the function above would be declared:
 *  </div>
 *  <div>
 *  <pre>
 *      public synchronized static native int H5Dread(int fid, int filetype,
 *          int memtype, int memspace, Object data);
 *  </pre>
 *  and the parameter <i>data</i> can be any multi-dimensional
 *  array of numbers, such as float[][], or int[][][], or Double[][].
 *  </div>
 *  <div>
 *  <a NAME="CONSTANTS">
 *  <h2>HDF-5 Constants</h2>
 *  </div>
 *  <div>
 *  The HDF-5 API defines a set of constants and enumerated values.
 *  Most of these values are available to Java programs via the class
 *  <a href="./hdf.h5.HDF5Constants.html">
 *  <b>HDF5Constants</b></a>.
 *  For example, the parameters for the h5open() call include two
 *  numeric values, <b><i>HDFConstants.H5F_ACC_RDWR</i></b> and
 *  <b><i>HDF5Constants.H5P_DEFAULT</i></b>.  As would be expected,
 *  these numbers correspond to the C constants <b><i>H5F_ACC_RDWR</i></b>
 *  and <b><i>H5P_DEFAULT</i></b>.
 *  </div>
 *  <div>
 *  The HDF-5 API defines a set of values that describe number types and
 *  sizes, such as "H5T_NATIVE_INT" and "hsize_t". These values are
 *  determined at run time by the HDF-5 C library.
 *  To support these parameters,
 *  the Java class
 *  <a href="./hdf.h5.HDF5CDataTypes.html">
 *  <b>HDF5CDataTypes</b></a> looks up the values when
 *  initiated.  The values can be accessed as public variables of the
 *  Java class, such as:
 *  <pre> int data_type = HDF5CDataTypes.JH5T_NATIVE_INT;</pre>
 *  The Java application uses both types of constants the same way, the only
 *  difference is that the <b><i>HDF5CDataTypes</i></b> may have different
 *  values on different platforms.
 *  </div>
 *  <div>
 *  <a NAME="ERRORS">
 *  <h2>Error handling and Exceptions</h2>
 *  </div>
 *  <div>
 *  The HDF5 error API (H5E) manages the behavior of the error stack in
 *  the HDF-5 library. This API is omitted from the JHI5. Errors
 *  are converted into Java exceptions. This is totally different from the
 *  C interface, but is very natural for Java programming.
 *  </div>
 *  <div>
 *  The exceptions of the JHI5 are organized as sub-classes of the class
 *  <a href="./hdf.h5.exceptions.HDF5Exception.html">
 *  <b>HDF5Exception</b></a>.  There are two subclasses of
 *  <b>HDF5Exception</b>,
 *  <a href="./hdf.h5.exceptions.HDF5LibraryException.html">
 *  <b>HDF5LibraryException</b></a>
 *  and
 *  <a href="./hdf.h5.exceptions.HDF5JavaException.html">
 *  <b>HDF5JavaException</b></a>. The sub-classes of the former
 *  represent errors from the HDF-5 C library, while sub-classes of the latter
 *  represent errors in the JHI5 wrapper and support code.
 *  </div>
 *  <div>
 *  The super-class <b><i>HDF5LibraryException</i></b> implements the method
 *  '<b><i>printStackTrace()</i></b>', which prints out the HDF-5 error stack,
 *  as described in the HDF-5 C API <i><b>H5Eprint()</b>.</i> This may
 *  be used by Java exception handlers to print out the HDF-5 error stack.
 *  </div>
 *  <div>
 *
 *  @version HDF5 1.2 <br />
 *  <b>See also:
 *  <a href ="./hdf.h5.HDFArray.html">
 *  </b> hdf.h5.HDFArray</a><br />
 *  <a href ="./hdf.h5.HDF5Constants.html">
 *  </b> hdf.h5.HDF5Constants</a><br />
 *  <a href ="./hdf.h5.HDF5CDataTypes.html">
 *  </b> hdf.h5.HDF5CDataTypes</a><br />
 *  <a href ="./hdf.h5.HDF5Exception.html">
 *  hdf.h5.HDF5Exception<br />
 *  <a href="http://hdf.ncsa.uiuc.edu/HDF5/">
 *  http://hdf.ncsa.uiuc.edu/HDF5"</a>
 *  </div>
 **/
public class H5 {
    /**
     * The version number of the HDF5 library: <br />
     *     LIB_VERSION[0]: The major version of the library.<br />
     *     LIB_VERSION[1]: The minor version of the library.<br />
     *     LIB_VERSION[2]: The release number of the library.<br />
     *     
     * Make sure to update the verions number when a different
     * library is used.    
     */
    public final static int LIB_VERSION[] = {1, 8, 2};

    /**
     * A key of system property to load library from the library path.
     */
    public final static String H5PATH_PROPERTY_KEY = "hdf.h5";
    
    /** 
     * A key of system property to load library by name from library path, 
     * via System.loadLibrary() 
     */ 
    public final static String H5_LIBRARY_NAME_PROPERTY_KEY = "hdf.h5.loadLibraryName"; 

    private static Logger s_logger; 
    private static String s_libraryName; 
    private static boolean isLibraryLoaded = false;
    
    static { loadH5Lib(); }
    
    /**
     * True if H5 uses 16 APIs; otherwise, false.
     * This variable must be defined after loadH5Lib() was called.
     */
    public static final boolean isAPI16 = H5Use16();
    
    
    /**
     * load the hdf5 library. loadH5Lib() is only accessible from hdf.h5 package.
     */
    public static void loadH5Lib()
    {
        // Make sure that the library is loaded only once 
        if (isLibraryLoaded)
            return;
        
        // use default logger, since spanning sources 
        s_logger = Logger.getLogger("hdf.h5"); 
        s_logger.setLevel(Level.INFO); 
        // first try loading library by name from user supplied library path 
        s_libraryName = System.getProperty(H5_LIBRARY_NAME_PROPERTY_KEY,null); 
        String mappedName = null; 
        if ((s_libraryName != null) && (s_libraryName.length() > 0)) 
        { 
            try { 
                mappedName = System.mapLibraryName(s_libraryName); 
                System.loadLibrary(s_libraryName); 
                isLibraryLoaded = true; 
            } catch (Throwable err) { 
                err.printStackTrace(); 
                isLibraryLoaded = false; 
            } finally { 
                s_logger.log(Level.INFO,"HDF5 library: " + s_libraryName + " resolved to: "+mappedName+"; "+(isLibraryLoaded ? "" : " NOT") + " successfully loaded from java.library.path");

            } 
        } 
        
        if (!isLibraryLoaded) { 
            // else try loading library via full path 
            String filename = System.getProperty(H5PATH_PROPERTY_KEY,null); 
            if ((filename != null) && (filename.length() > 0)) 
            { 
                File h5dll = new File(filename); 
                if (h5dll.exists() && h5dll.canRead() && h5dll.isFile()) { 
                    try { 
                        System.load(filename); 
                        isLibraryLoaded = true; 
                    } catch (Throwable err) { 
                        err.printStackTrace(); 
                        isLibraryLoaded = false; 
                    } finally { 
                        s_logger.log(Level.INFO,"HDF5 library: " + filename + (isLibraryLoaded ? "" : " NOT") + " successfully loaded.");

                    } 
                } else { 
                    isLibraryLoaded = false; 
                    throw (new UnsatisfiedLinkError("Invalid HDF5 library, "+filename)); 
                } 
            } 
        } 

        // else load standard library 
        if (!isLibraryLoaded) 
        { 
            try { 
                s_libraryName = "jhdf5"; 
                mappedName = System.mapLibraryName(s_libraryName); 
                System.loadLibrary("jhdf5"); 
                isLibraryLoaded = true; 
            } catch (Throwable err) { 
                err.printStackTrace(); isLibraryLoaded = false; 
            } finally { 
                s_logger.log(Level.INFO,"HDF5 library: " + s_libraryName + " resolved to: "+mappedName+"; "+(isLibraryLoaded ? "" : " NOT") + " successfully loaded from java.library.path");

            } 
        } 

        /* Important!  Exit quietly */
        try { 
            H5.H5dont_atexit();
        } catch (HDF5LibraryException e) {
            System.exit(1);
        }

        /* Important!  Disable error output to C stdout */
        try { H5.H5error_off(); } catch (HDF5LibraryException ex) {}

        /*  Optional:  confirm the version
         *     This will crash immediately if not the
         *     specified version.
         */
        Integer majnum = Integer.getInteger("hdf.h5.H5.hdf5maj",null);
        Integer minnum = Integer.getInteger("hdf.h5.H5.hdf5min",null);
        Integer relnum = Integer.getInteger("hdf.h5.H5.hdf5rel",null);
        if ((majnum != null) && (minnum != null) && (relnum != null)) {
            H5.H5check_version(majnum.intValue(),minnum.intValue(),relnum.intValue());
        }
    } // static void loadH5Lib()

    //////////////////////////////////////////////////////////////////

    /** Turn off error handling
     *  By default, the C library prints the error stack
     *  of the HDF-5 C library on stdout.  This behavior
     *  may be disabled by calling H5error_off().
     **/
    public synchronized static native void H5error_off()
    throws HDF5LibraryException;


    //////////////////////////////////////////////////////////////
    //                                                          //
    //             H5: General Library Functions                //
    //                                                          //
    //////////////////////////////////////////////////////////////

    /**
     *  H5open initialize the library.
     *
     *  @return none.
     *
     *  @exception HDF5LibraryException - Error from the HDF-5 Library.
     **/
    public synchronized static native void H5open() 
        throws HDF5LibraryException;

    /**
     *  H5close flushes all data to disk, closes all file
     *  identifiers, and cleans up all memory used by the library.
     *
     *  @return none.
     *
     *  @exception HDF5LibraryException - Error from the HDF-5 Library.
     **/
    public synchronized static native void H5close() 
        throws HDF5LibraryException;

    /**
     *  H5dont_atexit indicates to the library that an atexit()
     *  cleanup routine should not be installed.  In order to be
     *  effective, this routine must be called before any other HDF
     *  function calls, and must be called each time the library
     *  is loaded/linked into the application (the first time and
     *  after it's been un-loaded).
     *  <div>
     *  This is called by the static initializer, so this should
     *  never need to be explicitly called by a Java program.
     *  </div>
     *
     *  @return none
     *
     *  @exception HDF5LibraryException - Error from the HDF-5 Library.
     **/
    private synchronized static native void H5dont_atexit() 
        throws HDF5LibraryException;

    /**
     *  H5garbage_collect collects on all free-lists of all types.
     *  <div>
     *  ** Note: this is new with HDF5.1.2.2.  If using
     *           an earlier version, use
     *           'configure --enable-hdf5_1_2_1'
     *           so this routine will fail safely.
     *  </div>
     *
     *  @return none.
     *
     *  @exception HDF5LibraryException - Error from the HDF-5 Library.
     **/
    public synchronized static native void H5garbage_collect() 
        throws HDF5LibraryException;

    /**
     *  H5set_free_list_limits sets size limits on all types of free lists.
     *
     *  @param reg_global_lim  IN: The limit on all regular free list memory used
     *  @param reg_list_lim    IN: The limit on memory used in each regular free list
     *  @param arr_global_lim  IN: The limit on all array free list memory used
     *  @param arr_list_lim    IN: The limit on memory used in each array free list
     *  @param blk_global_lim  IN: The limit on all block free list memory used
     *  @param blk_list_lim    IN: The limit on memory used in each block free list
     *
     *  @return none.
     *
     *  @exception HDF5LibraryException - Error from the HDF-5 Library.
     **/
    public synchronized static native void H5set_free_list_limits(int reg_global_lim, int reg_list_lim, 
          int arr_global_lim, int arr_list_lim, int blk_global_lim, int blk_list_lim )
        throws HDF5LibraryException;

    /**
     *  H5get_libversion retrieves the major, minor, and release
     *  numbers of the version of the HDF library which is linked
     *  to the application.
     *
     *  @param libversion OUT: The version information of the HDF library.
     *    <ul>
     *      <li>libversion[0] = majnum // The major version of the library.</li>
     *      <li>libversion[1] = minnum // The minor version of the library.</li>
     *      <li>libversion[2] = relnum // The release number of the library.</li>
     *    </ul>
     *
     *  @return none.
     *
     *  @exception HDF5LibraryException - Error from the HDF-5 Library.
     *  @exception NullPointerException - libversion is null.
     **/
    public synchronized static native void H5get_libversion(int[] libversion) 
        throws HDF5LibraryException, NullPointerException;
//    public synchronized static native void H5get_libversion(IntByReference majnum, IntByReference minnum,
//  				IntByReference relnum)
//        throws HDF5LibraryException, NullPointerException;

    /**
     *  H5check_version verifies that the arguments match the version numbers
     *  compiled into the library.
     *
     *  @param majnum IN: The major version of the library.
     *  @param minnum IN: The minor version of the library.
     *  @param relnum IN: The release number of the library.
     *
     *  @return none. Upon failure (when the versions do not match), 
     *  this function causes the application to abort (i.e., crash)
     *
     *  See C API function: herr_t H5check_version()
     **/
    public synchronized static native void H5check_version(int majnum, int minnum, int relnum);

    /**
     *  Check if 16 APIs are used.
     *
     *  @return true if 16 APIs are used; otherwise, false.
     **/
    private synchronized static native boolean H5Use16();
    
}

