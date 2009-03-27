/*****************************************************************************
 * Copyright by The HDF Group.                                               *
 * Copyright by the Board of Trustees of the University of Illinois.         *
 * All rights reserved.                                                      *
 *                                                                           *
 * This file is part of the HDF Java Products distribution.                  *
 * The full copyright notice, including terms governing use, modification,   *
 * and redistribution, is contained in the files COPYING and Copyright.html. *
 * COPYING can be found at the root of the source code distribution tree.    *
 * Or, see http://hdfgroup.org/products/hdf-java/doc/Copyright.html.         *
 * If you do not have access to either file, you may request a copy from     *
 * help@hdfgroup.org.                                                        *
 ****************************************************************************/

package hdf.h5;

import hdf.h5.enums.H5F_SCOPE;
import hdf.h5.exceptions.HDF5LibraryException;

//////////////////////////////////////////////////////////////
//                                                          //
//             H5F: File Interface Functions                //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5F {

  /**
   *  H5Fis_hdf5 determines whether a file is in the HDF5 format.
   *
   *  @param name IN: File name to check format.
   *
   *  @return boolean true if is HDF-5, false if not.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native boolean H5Fis_hdf5(String filename)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Fcreate is the primary function for creating HDF5 files.
   *
   *  @param filename   IN: Name of the file to access.
   *  @param flags      IN: File access flags. Possible values include:
   *  <ul>
   *  <li>H5F_ACC_RDWR Allow read and write access to file.</li>
   *  <li>H5F_ACC_RDONLY Allow read-only access to file.</li>
   *  <li>H5F_ACC_TRUNC Truncate file, if it already exists,
   *      erasing all data previously stored in the file.</li>
   *  <li>H5F_ACC_EXCL Fail if file already exists.</li>
   *  <li>H5F_ACC_DEBUG Print debug information.</li>
   *  <li>H5P_DEFAULT Apply default file access and creation properties.</li>
   *  </ul>
   *
   *  @param create_plist  IN: File creation property list identifier,
   *                           used when modifying default file meta-data.
   *                           Use H5P_DEFAULT for default access properties.
   *  @param access_plist  IN: File access property list identifier.
   *                           If parallel file access is desired,
   *                           this is a collective call according to the communicator
   *                           stored in the access_id (not supported in Java).
   *                           Use H5P_DEFAULT for default access properties.
   *
   *  @return a file identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - filename is null.
   **/
  public synchronized static native int H5Fcreate(String filename, int flags, int create_plist, int access_plist)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Fopen opens an existing file and is the primary function
   *  for accessing existing HDF5 files.
   *
   *  @param access_plist IN: Name of the file to access.
   *  @param flags        IN: File access flags.
   *  <ul>
   *  <li>H5F_ACC_RDWR Allow read and write access to file.</li>
   *  <li>H5F_ACC_RDONLY Allow read-only access to file.</li>
   *  </ul>
   *  @param access_plist IN: Identifier for the file access properties list.
   *
   *  @return a file identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Fopen(String filename, int flags, int access_plist)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Freopen returns a new file identifier for an already-open HDF5 file.
   *
   *  @param file_id  IN: Identifier of a file for which an additional identifier is required.
   *
   *  @return a new file identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Freopen(int file_id)
  throws HDF5LibraryException;
  
  /**
   *  H5Fflush causes all buffers associated with a file to be immediately 
   *  flushed to disk without removing the data from the cache. 
   *
   *  @param object_id  IN: Identifier of object used to identify the file.
   *  @param scope      IN: Specifies the scope of the flushing action.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Fflush(int object_id, H5F_SCOPE scope)
  throws HDF5LibraryException;

  /**
   *  H5Fclose terminates access to an HDF5 fileby flushing all data to storage 
   *  and terminating access to the file.
   *
   *  @param file_id  IN: Identifier of a file to terminate access to.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Fclose(int file_id)
  throws HDF5LibraryException;

  /**
   *  H5Fget_create_plist returns a file creation property list
   *  identifier identifying the creation properties used to
   *  create this file.
   *
   *  @param file_id  IN: Identifier of the file to get creation property list
   *
   *  @return a file creation property list identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Fget_create_plist(int file_id)
  throws HDF5LibraryException;

  /**
   *  H5Fget_access_plist returns the file access property list
   *  identifier of the specified file.
   *
   *  @param file_id  IN: Identifier of file to get access property list of
   *
   *  @return a file access property list identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Fget_access_plist(int file_id)
  throws HDF5LibraryException;

  /**
   *  H5Fget_intent retrieves the “intended access mode” flag passed 
   *  with H5Fopen when the file was opened. 
   *
   *  @param file_id  IN: File identifier for a currently-open HDF5 file
   *
   *  @return the intended access mode flag, as originally passed with H5Fopen.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Fget_intent(int file_id)
  throws HDF5LibraryException;
//    int H5Fget_intent(int file_id, IntByReference intent);

  /**
   *  H5Fget_obj_count returns the number of open object identifiers for the file. 
   *
   *  @param file_id  IN: File identifier for a currently-open HDF5 file
   *  @param types    IN: Type of object for which identifiers are to be returned.
   *  <ul>
   *  <li>H5F_OBJ_FILE      Files only</li>
   *  <li>H5F_OBJ_DATASET   Datasets only</li>
   *  <li>H5F_OBJ_GROUP     Groups only</li>
   *  <li>H5F_OBJ_DATATYPE  Named datatypes only</li>
   *  <li>H5F_OBJ_ATTR      Attributes only</li>
   *  <li>H5F_OBJ_ALL       All of the above</li>
   *  <li>H5F_OBJ_LOCAL     Restrict search to objects opened through current file identifier.</li>
   *  </ul>
   *
   *  @return the number of open objects.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Fget_obj_count(int file_id, int types)
  throws HDF5LibraryException;

  /**
   *  H5Fget_obj_ids returns the list of identifiers for all open HDF5 objects fitting the specified criteria. 
   *
   *  @param file_id      IN: File identifier for a currently-open HDF5 file
   *  @param types        IN: Type of object for which identifiers are to be returned.
   *  @param max_objs     IN: Maximum number of object identifiers to place into obj_id_list.
   *  @param obj_id_list OUT: Pointer to the returned list of open object identifiers.
   *
   *  @return the number of objects placed into obj_id_list.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Fget_obj_ids(int file_id, int types, long max_objs, int[] obj_id_list)
  throws HDF5LibraryException, NullPointerException;

//  /**
//   *  H5Fget_vfd_handle returns a pointer to the file handle from the low-level file driver 
//   *  currently being used by the HDF5 library for file I/O. 
//   *
//   *  @param file_id      IN: Identifier of the file to be queried.
//   *  @param fapl         IN: File access property list identifier.
//   *
//   *  @return a pointer to the file handle being used by the low-level virtual file driver.
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   **/
//  public synchronized static native Pointer file_handle H5Fget_vfd_handle(int file_id, int fapl)
//  throws HDF5LibraryException;

  /**
   *  H5Fmount mounts the file specified by child_id onto the
   *  group specified by loc_id and name using the mount
   *  properties plist_id.
   *
   *  @param loc_id   IN: The identifier for the group onto which
   *                      the file specified by child_id is to be mounted.
   *  @param name     IN: The name of the group onto which the file
   *                      specified by child_id is to be mounted.
   *  @param child_id IN: The identifier of the file to be mounted.
   *  @param plist_id IN: The identifier of the property list to be used.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Fmount(int loc_id, String name,
          int child_id, int plist_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Funmount dissassociates the mount point's file from the file mounted there.
   *
   *  @param loc_id IN: The identifier for the location at which
   *                    the specified file is to be unmounted.
   *  @param name   IN: The name of the file to be unmounted.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Funmount(int loc_id, String name)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Fget_freespace returns the amount of space that is unused by any objects in the file. 
   *
   *  @param file_id  IN: File identifier for a currently-open HDF5 file
   *
   *  @return the amount of free space in the file
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Fget_freespace(int file_id)
  throws HDF5LibraryException, NullPointerException;


  /**
   *  H5Fget_filesize returns the size of the HDF5 file specified by file_id.  
   *
   *  @param file_id  IN: File identifier for a currently-open HDF5 file
   *
   *  @return the size of the file, in bytes. 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Fget_filesize (int file_id)
  throws HDF5LibraryException;

//  /**
//   *  H5Fget_mdc_config loads the current metadata cache configuration into 
//   *  the instance of H5AC_cache_config_t pointed to by the config_ptr parameter. 
//   *
//   *  @param file_id        IN: Identifier of the target file
//   *  @param config_ptr IN/OUT: Pointer to the instance of H5AC_cache_config_t in which the current metadata cache configuration is to be reported.
//   *
//   *  @return none
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   *  @exception NullPointerException - config_ptr is null.
//   **/
//  public synchronized static native void H5Fget_mdc_config(int file_id,
//          H5AC_cache_config_t config_ptr)
//  throws HDF5LibraryException, NullPointerException;
//
//  /**
//   *  H5Fset_mdc_config attempts to configure the file's metadata cache according to the configuration supplied.
//   *
//   *  @param file_id    IN: Identifier of the target file
//   *  @param config_ptr IN: Pointer to the instance of H5AC_cache_config_t containing the desired configuration.
//   *
//   *  @return none
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   *  @exception NullPointerException - config_ptr is null.
//   **/
//  public synchronized static native int H5Fset_mdc_config(int file_id,
//          H5AC_cache_config_t config_ptr)
//  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Fget_mdc_hit_rate queries the metadata cache of the target file to 
   *  obtain its hit rate (cache hits / (cache hits + cache misses)) 
   *  since the last time hit rate statistics were reset.
   *
   *  @param file_id  IN: Identifier of the target file.
   *
   *  @return the double in which the hit rate is returned.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native double H5Fget_mdc_hit_rate(int file_id)
  throws HDF5LibraryException;

  /**
   *  H5Fget_mdc_size queries the metadata cache of the target file for the desired size information. 
   *
   *  @param file_id              IN: Identifier of the target file.
   *  @param metadata_cache      OUT: Current metadata cache information
   *  <ul>
   *      <li>metadata_cache[0] = max_size_ptr       // current cache maximum size</li>
   *      <li>metadata_cache[1] = min_clean_size_ptr // current cache minimum clean size</li>
   *      <li>metadata_cache[2] = cur_size_ptr       // current cache size</li>
   *  </ul>
   *
   *  @return current number of entries in the cache
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - metadata_cache is null.
   **/
  public synchronized static native void H5Fget_mdc_size(int file_id, long[] metadata_cache)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Freset_mdc_hit_rate_stats resets the hit rate statistics 
   *  counters in the metadata cache associated with the specified file. 
   *
   *  @param file_id  IN: Identifier of the target file.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Freset_mdc_hit_rate_stats(int file_id)
  throws HDF5LibraryException;


  /**
   *  H5Fget_name retrieves the name of the file to which the object obj_id belongs. 
   *
   *  @param obj_id  IN: Identifier of the object for which the associated filename is sought.
   *
   *  @return the filename.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Fget_name (int obj_id)
  throws HDF5LibraryException;
//  long H5Fget_name(int obj_id, Buffer name/*out*/, long size);

//  /**
//   *  H5Fget_info returns global information for the file associated with the 
//   *  object identifier obj_id. 
//   *
//   *  @param obj_id  IN: Object identifier for any object in the file. 
//   *
//   *  @return the structure containing global file information.
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   **/
//  public synchronized static native H5F_info_t H5Fget_info(int obj_id)
//  throws HDF5LibraryException, NullPointerException;
//  int H5Fget_info(int obj_id, H5F_info_t file_info);
}
