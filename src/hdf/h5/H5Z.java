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

import hdf.h5.enums.H5Z_FILTER;
import hdf.h5.exceptions.HDF5LibraryException;

//////////////////////////////////////////////////////////////
//                                                          //
//H5Z: Filter and Compression Interface Functions           //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5Z {

//  /**
//   *  H5Zregister determines whether the filter specified is 
//   *  available to the application. 
//   *
//   *  @param filter  IN:  Filter identifier. 
//   *
//   *  @return a boolean, true, if the filter is available.
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   **/
//  public synchronized static native int H5Zregister(H5Z_class_t cls)
//      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Zunregister unregisters the filter specified. 
   *
   *  @param filter  IN:  Filter identifier. 
   *
   *  @return none.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Zunregister(H5Z_FILTER id)
      throws HDF5LibraryException;

  /**
   *  H5Zfilter_avail determines whether the filter specified is 
   *  available to the application. 
   *
   *  @param filter  IN:  Filter identifier. 
   *
   *  @return a boolean, true, if the filter is available.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Zfilter_avail(H5Z_FILTER id)
      throws HDF5LibraryException;

  /**
   *  H5Zget_filter_info retrieves information about a filter.
   *
   *  @param filter  IN:  Filter identifier. 
   *
   *  @return a bit field encoding the returned filter information .
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Zget_filter_info(H5Z_FILTER filter)
      throws HDF5LibraryException;
//  int H5Zget_filter_info(H5Z_FILTER filter, IntByReference filter_config_flags);

}
