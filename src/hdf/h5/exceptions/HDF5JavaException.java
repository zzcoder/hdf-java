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

package hdf.h5.exceptions;


/**
 *  <p>
 *  The class HDF5JavaException returns errors from the Java
 *  wrapper of theHDF5 library.
 *  <p>
 *  These errors include Java configuration errors, security
 *  violations, and resource exhaustion.
 */
public class HDF5JavaException extends HDF5Exception 
{
    /**
     * Constructs an <code>HDF5JavaException</code> with no
     * specified detail message.
     */
    public HDF5JavaException() {
        super();
    }

    /**
     * Constructs an <code>HDF5JavaException</code> with the
     * specified detail message.
     *
     * @param   s   the detail message.
     */
    public HDF5JavaException(String s) {
        super(s);
    }
}
