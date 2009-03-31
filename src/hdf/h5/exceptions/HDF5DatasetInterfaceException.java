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
 *  The class HDF5LibraryException returns errors raised by the HDF5
 *  library.
 *  <p>
 *  This sub-class represents HDF-5 major error code
 *       <b>H5E_DATASET</b>
 */
public class HDF5DatasetInterfaceException extends HDF5LibraryException 
{
    /**
     * Constructs an <code>HDF5DatasetInterfaceException</code> with
     * no specified detail message.
     */
     public HDF5DatasetInterfaceException() {
        super();
     }

    /**
     * Constructs an <code>HDF5DatasetInterfaceException</code> with
     * the specified detail message.
     *
     * @param   s   the detail message.
     */
    public HDF5DatasetInterfaceException(String s) {
        super(s);
    }
}
