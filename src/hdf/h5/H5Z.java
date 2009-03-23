package hdf.h5;

import hdf.h5.enums.H5Z.H5Z_filter_t;
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
  public synchronized static native void H5Zunregister(H5Z_filter_t id)
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
  public synchronized static native boolean H5Zfilter_avail(H5Z_filter_t id)
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
  public synchronized static native int H5Zget_filter_info(H5Z_filter_t filter)
      throws HDF5LibraryException;
//  int H5Zget_filter_info(H5Z_filter_t filter, IntByReference filter_config_flags);

}
