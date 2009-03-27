/****************************************************************************
 * NCSA HDF5                                                                 *
 * National Comptational Science Alliance                                   *
 * University of Illinois at Urbana-Champaign                               *
 * 605 E. Springfield, Champaign IL 61820                                   *
 *                                                                          *
 * For conditions of distribution and use, see the accompanying             *
 * hdf-java/COPYING file.                                                  *
 *                                                                          *
 ****************************************************************************/

package hdf.h5.exceptions;

import hdf.h5.H5;
import hdf.h5.constants.H5Zconstant;

/**
 *  <p>
 *  The class HDF5LibraryException returns errors raised by the HDF5
 *  library.
 *  <p>
 *  Each major error code from the HDF-5 Library is represented
 *  by a sub-class of this class, and by default the 'detailedMessage'
 *  is set according to the minor error code from the HDF-5 Library.
 *  <p>
 *  For major and minor error codes, see <b>H5Epublic.h</b> in the HDF-5
 *  library.
 *  <p>
 */


public class HDF5LibraryException extends HDF5Exception 
{
    /**
     * Constructs an <code>HDF5LibraryException</code> with
     * no specified detail message.
     */
     public HDF5LibraryException() {
        super();

        // this code forces the loading of the HDF-5 library
        // to assure that the native methods are available
        try { H5.H5open(); } catch (Exception e) {};

        detailMessage = getMinorError(getMinorErrorNumber());
    }

    /**
     * Constructs an <code>HDF5LibraryException</code> with
     * the specified detail message.
     *
     * @param   s   the detail message.
     */
    public HDF5LibraryException(String s) {
        super(s);
        // this code forces the loading of the HDF-5 library
        // to assure that the native methods are available
        try { H5.H5open(); } catch (Exception e) {};
    }

    /**
     * Get the major error number of the first error on the
     * HDF5 library error stack.
     *
     * @return the major error number
     */
    public native int getMajorErrorNumber();

    /**
     * Get the minor error number of the first error on the
     * HDF5 library error stack.
     *
     * @return the minor error number
     */
    public native int getMinorErrorNumber();

    /**
     *  Return a error message for the minor error number.
     * <p>
     *  These messages come from <b>H5Epublic.h</b>.
     *
     *  @param err_code the error code
     *
     *  @return the string of the minor error
     */
    public String getMinorError(int err_code)
    {
        if (err_code == HDF5Constants.H5Zconstant ) {
            return "special zero no error";
        } else if (err_code == HDF5Constants.H5Zconstant) {
            return"information is unitialized";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "feature is unsupported";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "incorrect type found";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "argument out of range";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "bad value for argument";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "no space available for allocation";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "unable to copy object";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "unable to free object";
        } else if (err_code == HDF5Constants.H5Zconstant) {
            return "Object already exists";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Unable to lock object";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Unable to unlock object";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "file already exists";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "file already open";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't create file";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't open file";
        } else if (err_code == HDF5Constants.H5Zconstant) {
            return "Can't close file";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "not an HDF5 format file";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "bad file ID accessed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "file has been truncated";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "file mount error";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "seek failed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "read failed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "write failed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "close failed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "address overflowed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "file fcntl failed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't initialize object";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "object already initialized";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't release object";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't find atom information";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't find group information";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't register new atom";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't increment reference count";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't decrement reference count";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Out of IDs for group";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't flush object from cache";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't load object into cache";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "protected object error";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "object not currently cached";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "object not found";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "object already exists";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't encode value";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't decode value";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't split node";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't insert object";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't list node";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "bad object header link count";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "wrong version number";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "alignment error";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "unrecognized message";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return " Can't delete message";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't open object";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "name component is too long";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "problem with current working group";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "link count failure";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "symbolic link error";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't convert datatypes";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Bad size for object";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't clip hyperslab region";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't count elements";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't select hyperslab";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't move to next iterator location";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Invalid selection";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't get value";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Can't set value";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "Duplicate class name in parent class";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "some MPI function failed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "MPI Error String";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "can't make a TBBT tree";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "can't receive messages from processes";
        } else if (err_code == HDF5Constants.H5Zconstant) {
            return "can't send metadata message";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "can't register change on server";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "can't allocate from file";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "requested filter is not available";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "callback failed";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "error from filter \"can apply\" callback";
        } else if (err_code == HDF5Constants.H5Zconstant ) {
            return "error from filter \"set local\" callback";
        } else {
            return "undefined error";
        }
    }


    /**
     * Prints this <code>HDF5LibraryException</code>,
     * the HDF-5 Library error stack, and
     * and the Java stack trace to the standard error stream.
     */
    public void printStackTrace() {
        System.err.println(this);
        printStackTrace0(null); // the HDF-5 Library error stack
        super.printStackTrace(); // the Java stack trace
    }

    /**
     * Prints this <code>HDF5LibraryException</code>
     * the HDF-5 Library error stack, and
     * and the Java stack trace to the
     * specified print stream.
     *
     */
    public void printStackTrace(java.io.File f) {
        if ((f==null) || !f.exists() || f.isDirectory() || !f.canWrite()) {
            printStackTrace();
        } else
        {
            try {
            java.io.FileOutputStream o = new java.io.FileOutputStream(f);
            java.io.PrintWriter p = new java.io.PrintWriter(o);
            p.println(this);
            p.close();
            } catch (Exception ex) {
            System.err.println(this);
            };
            // the HDF-5 Library error stack
            printStackTrace0(f.getPath());
            super.printStackTrace(); // the Java stack trace
        }
    }

    /*
     *  This private method calls the HDF-5 library to extract
     *  the error codes and error stack.
     */
    private native void printStackTrace0(String s);

}
