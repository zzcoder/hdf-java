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

import hdf.h5.enums.H5D_ALLOC_TIME;
import hdf.h5.enums.H5D_FILL_TIME;
import hdf.h5.enums.H5D_FILL_VALUE;
import hdf.h5.enums.H5D_LAYOUT;
import hdf.h5.enums.H5FD_MEM;
import hdf.h5.enums.H5F_CLOSE_DEGREE;
import hdf.h5.enums.H5F_LIBVER;
import hdf.h5.enums.H5T_CSET;
import hdf.h5.enums.H5Z_EDC;
import hdf.h5.enums.H5Z_FILTER;
import hdf.h5.enums.H5Z_SO_SCALE;
import hdf.h5.exceptions.HDF5Exception;
import hdf.h5.exceptions.HDF5LibraryException;

//////////////////////////////////////////////////////////////
//                                                          //
//         H5P: Property List Interface Functions           //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5P {
//  /* Define property list class callback function pointer types */
//  public interface H5P_cls_create_func_t extends Callback {
//    int callback(int prop_id, Pointer create_data);
//  }
//  public interface H5P_cls_copy_func_t extends Callback {
//    int callback(int new_prop_id, int old_prop_id,
//        Pointer copy_data);
//  }
//  public interface H5P_cls_close_func_t extends Callback {
//    int callback(int prop_id, Pointer close_data);
//  }
//
//  /* Define property list callback function pointer types */
//  public interface H5P_prp_cb1_t extends Callback {
//    int callback(String name, long size, Pointer value);
//  }
//  public interface H5P_prp_cb2_t extends Callback {
//    int callback(int prop_id, String name, long size, Pointer value);
//  }
////  typedef H5P_prp_cb1_t H5P_prp_create_func_t;
////  typedef H5P_prp_cb2_t H5P_prp_set_func_t;
////  typedef H5P_prp_cb2_t H5P_prp_get_func_t;
////  typedef H5P_prp_cb2_t H5P_prp_delete_func_t;
////  typedef H5P_prp_cb1_t H5P_prp_copy_func_t;
//  public interface H5P_prp_compare_func_t extends Callback {
//    int callback(Pointer value1, Pointer value2, long size);
//  }
////  typedef H5P_prp_cb1_t H5P_prp_close_func_t;
//
//  /* Define property list iteration function type */
//  public interface H5P_iterate_t extends Callback {
//    int callback(int id, String name, Pointer iter_data);
//  }

  /* Generic property list routines */
//  int H5Pcreate_class(int parent, String name,
//      H5P_cls_create_func_t cls_create, Pointer create_data,
//      H5P_cls_copy_func_t cls_copy, Pointer copy_data,
//      H5P_cls_close_func_t cls_close, Pointer close_data);

  /**
   *  H5Pget_class_name retrieves the name of a generic property list class
   *
   *  @param pclass_id  IN: Identifier of property object to query
   *
   *  @return name of a property list
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Pget_class_name(int pclass_id)
  throws HDF5LibraryException;

  /**
   *  H5Pcreate creates a new property as an instance of some property list class.
   *
   *  @param cls_id  IN: The type of property list to create.
   *
   *  @return a property list identifier (plist).
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pcreate(int cls_id)
  throws HDF5LibraryException;
  
//  int H5Pregister2(int cls_id, String name, long size,
//      Pointer def_value, H5P_prp_cb1_t prp_create,
//      H5P_prp_cb2_t prp_set, H5P_prp_cb2_t prp_get,
//      H5P_prp_cb2_t prp_del, H5P_prp_cb1_t prp_copy,
//      H5P_prp_compare_func_t prp_cmp, H5P_prp_cb1_t prp_close);
//  int H5Pinsert2(int plist_id, String name, long size,
//      Pointer value, H5P_prp_cb2_t prp_set, H5P_prp_cb2_t prp_get,
//      H5P_prp_cb2_t prp_delete, H5P_prp_cb1_t prp_copy,
//      H5P_prp_compare_func_t prp_cmp, H5P_prp_cb1_t prp_close);

  /**
   *  Sets a property list value
   *
   *  @param plid  IN: Property list identifier to modify
   *  @param name  IN: Name of property to modify
   *  @param value IN: value to set the property to
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Pset( int plid, String name, byte[] value)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pexist determines whether a property exists within a property list or class
   *
   *  @param plid IN: Identifier for the property to query
   *  @param name IN: Name of property to check for
   *
   *  @return boolean true the property exists in the property object.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native boolean H5Pexist(int plid, String name)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pget_size retrieves the size of a property's value in bytes
   *
   *  @param plid IN: Identifier of property object to query
   *  @param name IN: Name of property to query
   *
   *  @return size of a property's value in bytes.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native long H5Pget_size(int plid, String name)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pget_nprops retrieves the number of properties in a property list or class
   *
   *  @param plid IN: Identifier of property object to query
   *
   *  @return number of properties
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_nprops(int plid)
  throws HDF5LibraryException;

  /**
   *  H5Pget_class returns the property list class for the
   *  property list identified by the plist parameter.
   *
   *  @param plist  IN: Identifier of property list to query.
   *
   *  @return a property list class identifier.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_class(int plist)
  throws HDF5LibraryException;

  /**
   *  H5Pget_class_parent retrieves an identifier for the parent class of a property class
   *
   *  @param plid IN: Identifier of the property class to query
   *
   *  @return a valid parent class object identifier.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_class_parent(int plid)
  throws HDF5LibraryException;

  /**
   *  H5Pget retrieves a copy of the value for a property in a property list
   *
   *  @param plid IN: Identifier of property object to query
   *  @param name IN: Name of property to query
   *
   *  @return value for a property if successful; a negative value if failed
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native byte[] H5Pget(int plid, String name )
  throws HDF5LibraryException;

  /**
   *  H5Pequal determines if two property lists or classes are equal
   *
   *  @param plid1 IN: First property object to be compared
   *  @param plid2 IN: Second property object to be compared
   *
   *  @return boolean true the property lists or classes are equal.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Pequal(int plid1, int plid2)
  throws HDF5LibraryException;

  /**
   *  H5Pisa_class checks to determine whether a property list is a member of the specified class
   *
   *  @param plist  IN: Identifier of the property list
   *  @param pclass IN: Identifier of the property class
   *
   *  @return boolean true the property list is a member of the specified class.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Pisa_class(int plist, int pclass)
  throws HDF5LibraryException;
  
//  public synchronized static native int H5Piterate(int id, IntByReference idx, H5P_iterate_t iter_func, Pointer iter_data)
//  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pcopy_prop copies a property from one property list or class to another
   *
   *  @param dst_id IN: Identifier of the destination property list or class
   *  @param src_id IN: Identifier of the source property list or class
   *  @param name   IN: Name of the property to copy
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Pcopy_prop(int dst_id, int src_id, String name)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Premove removes a property from a property list
   *
   *  @param plid IN: Identifier of the property list to modify
   *  @param name IN: Name of property to remove
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Premove(int plid, String name)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Punregister removes a property from a property list class
   *
   *  @param pclass_id IN: Property list class from which to remove permanent property
   *  @param name      IN: Name of property to remove
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Punregister(int pclass_id, String name)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pclose_class closes an existing property list class
   *
   *  @param plid IN: Property list class to close
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pclose_class(int plid)
  throws HDF5LibraryException;

  /**
   *  H5Pclose terminates access to a property list.
   *
   *  @param plist  IN: Identifier of the property list to terminate access to.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pclose(int plist)
  throws HDF5LibraryException;

  /**
   *  H5Pcopy copies an existing property list to create a
   *  new property list.
   *
   *  @param plist  IN: Identifier of property list to duplicate.
   *
   *  @return a property list identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pcopy(int plist)
  throws HDF5LibraryException;

  /* Object creation property list (OCPL) routines */

  /**
   *  H5Pset_attr_phase_change sets threshold values for attribute 
   *  storage on an object.
   *
   *  @param plist_id    IN: Identifier of the object (dataset or group) creation property list.
   *  @param max_compact IN: Maximum number of attributes to be stored in compact storage
   *  @param min_dense   IN: Minimum number of attributes to be stored in dense storage
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_attr_phase_change(int plist_id, int max_compact, int min_dense)
  throws HDF5LibraryException;

  /**
   *  H5Pget_attr_phase_change retrieves threshold values for attribute 
   *  storage on an object.
   *
   *  @param plist_id  IN: Identifier of the object (dataset or group) creation property list.
   *  
   *  @return compact_storage information.
   *  <ul>
   *      <li>compact_storage[0] = max_compact  // Maximum number of attributes to be stored in compact storage </li>
   *      <li>compact_storage[1] = min_dense    // Minimum number of attributes to be stored in dense storage </li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int[] H5Pget_attr_phase_change(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_attr_creation_order sets flags specifying whether to track and 
   *  index attribute creation order on an object. 
   *
   *  @param plist_id        IN: Identifier of the object creation property list.
   *  @param crt_order_flags IN: Flags specifying whether to track and index attribute creation order 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_attr_creation_order(int plist_id, int crt_order_flags)
  throws HDF5LibraryException;

  /**
   *  H5Pget_attr_creation_order retrieves the settings for tracking and indexing attribute 
   *  creation order on an object. 
   *
   *  @param plist_id        IN: Identifier of the object creation property list.
   *
   *  @return Flags specifying whether to track and index attribute creation order 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_attr_creation_order(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_obj_track_times sets a property in the object creation property list, 
   *  that governs the recording of times associated with an object.  
   *
   *  @param plist_id    IN: Identifier of the object creation property list.
   *  @param track_times IN: Boolean value, true or false, specifying whether object times are to be tracked 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_obj_track_times(int plist_id, boolean track_times)
  throws HDF5LibraryException;

  /**
   *  H5get_obj_track_times queries the object creation property list, 
   *  to determine whether object times are being recorded. 
   *
   *  @param plist_id  IN: Identifier of the object creation property list.
   *
   *  @return boolean true if object times are tracked, false otherwise
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Pget_obj_track_times(int plist_id)
  throws HDF5LibraryException;

  /* File creation property list (FCPL) routines */

  /**
   *  H5Pget_version retrieves the version information of various
   *  objects for a file creation property list.
   *
   *  @param plist  IN: Identifier of the file creation property list.
   *  
   *  @return version information.
   *  <ul>
   *      <li>version_info[0] = boot      // boot block version number</li>
   *      <li>version_info[1] = freelist  // global freelist version</li>
   *      <li>version_info[2] = stab      // symbol tabl version number</li>
   *      <li>version_info[3] = shhdr     // hared object header version</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int[] H5Pget_version(int plist)
  throws HDF5LibraryException;
//  int H5Pget_version(int plist_id, IntByReference boot/*out*/,
//           IntByReference freelist/*out*/, IntByReference stab/*out*/,
//           IntByReference shhdr/*out*/);

  /**
   *  H5Pset_userblock sets the user block size of a file
   *  creation property list.
   *
   *  @param plist  IN: Identifier of property list to modify.
   *  @param size   IN: Size of the user-block in bytes.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_userblock(int plist, long size)
  throws HDF5LibraryException;

  /**
   *  H5Pget_userblock retrieves the size of a user block in a
   *  file creation property list.
   *
   *  @param plist  IN: Identifier for property list to query.
   *
   *  @return the size of the user block.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_userblock(int plist)
  throws HDF5LibraryException;

  /**
   *  H5Pset_sizes sets the byte size of the offsets and lengths
   *  used to address objects in an HDF5 file.
   *
   *  @param plist        IN: Identifier of property list to modify.
   *  @param sizeof_addr  IN: Size of an object offset in bytes.
   *  @param sizeof_size  IN: Size of an object length in bytes.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_sizes(int plist_id, long sizeof_addr, long sizeof_size)
  throws HDF5LibraryException;

  /**
   *  H5Pget_sizes retrieves the size of the offsets and lengths
   *  used in an HDF5 file. This function is only valid for file
   *  creation property lists.
   *
   *  @param plist  IN: Identifier of property list to query.
   *  
   *  @return the size of the offsets and length.
   *  <ul>
   *      <li>size[0] = sizeof_addr // offset size in bytes</li>
   *      <li>size[1] = sizeof_size // length size in bytes</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long[] H5Pget_sizes(int plist)
  throws HDF5LibraryException;
//  int H5Pget_sizes(int plist_id, LongByReference sizeof_addr/*out*/,
//         LongByReference sizeof_size/*out*/);

  /**
   *  H5Pset_sym_k sets the size of parameters used to control the
   *  symbol table nodes.
   *
   *  @param plist_id IN: Identifier for property list to query.
   *  @param ik       IN: Symbol table tree rank.
   *  @param lk       IN: Symbol table node size.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_sym_k(int plist_id, int ik, int lk)
  throws HDF5LibraryException;

  /**
   *  H5Pget_sym_k retrieves the size of the symbol table
   *  B-tree 1/2 rank and the symbol table leaf node 1/2 size.
   *
   *  @param plist_id  IN: Property list to query.
   *
   *  @return the symbol table's B-tree 1/2 rank
   *  and leaf node 1/2 size.
   *  <ul>
   *      <li>size[0] = ik // the symbol table's B-tree 1/2 rank</li>
   *      <li>size[1] = lk // leaf node 1/2 size</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int[] H5Pget_sym_k(int plist_id)
  throws HDF5LibraryException;
//  int H5Pget_sym_k(int plist_id, IntByReference ik/*out*/, IntByReference lk/*out*/);

  /**
   *  H5Pset_istore_k sets the size of the parameter used to
   *  control the B-trees for indexing chunked datasets.
   *
   *  @param plist_id IN: Identifier of property list to query.
   *  @param ik       IN: 1/2 rank of chunked storage B-tree.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_istore_k(int plist_id, int ik)
  throws HDF5LibraryException;

  /**
   *  H5Pget_istore_k queries the 1/2 rank of an indexed
   *  storage B-tree.
   *
   *  @param plist_id  IN: Identifier of property list to query.
   *
   *  @return the chunked storage B-tree 1/2 rank.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_istore_k(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_shared_mesg_nindexes sets the number of shared object header 
   *  message indexes in the specified file creation property list. 
   *
   *  @param plist_id IN: Identifier of file creation property list to query.
   *  @param nindexes IN: Number of shared object header message indexes to be available 
   *                      in files created with this property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_shared_mesg_nindexes(int plist_id, int nindexes)
  throws HDF5LibraryException;

  /**
   *  H5Pget_shared_mesg_nindexes retrieves the number of shared object header message indexes in the 
   *  specified file creation property list fcpl_id. 
   *
   *  @param plist_id IN: Identifier of file creation property list to query.
   *
   *  @return number of shared object header message indexes available 
   *                      in files created with this property list.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_shared_mesg_nindexes(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_shared_mesg_index is used to configure the specified shared object header message index, setting the types of 
   *  messages that may be stored in the index and the minimum size of each message.  
   *
   *  @param plist_id        IN: Identifier of file creation property list.
   *  @param index_num       IN: Index being configured.
   *  @param mesg_type_flags IN: Types of messages that should be stored in this index.
   *  @param min_mesg_size   IN: Minimum message size.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_shared_mesg_index(int plist_id, 
      int index_num, int mesg_type_flags, int min_mesg_size)
  throws HDF5LibraryException;

  /**
   *  H5Pget_shared_mesg_index retrieves the message type and minimum message 
   *  size settings from the file creation property list for the 
   *  shared object header message index specified by index_num. 
   *
   *  @param plist_id  IN: Identifier of file creation property list to query.
   *  @param index_num IN: Index being configured.
   *
   *  @return the messages_storage information
   *  <ul>
   *      <li>messages_storage[0] = mesg_type_flags // Types of messages that may be stored in this index.</li>
   *      <li>messages_storage[1] = min_mesg_size   // Minimum message size.</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int[] H5Pget_shared_mesg_index(int plist_id, int index_num)
  throws HDF5LibraryException;

  /**
   *  H5Pset_shared_mesg_phase_change sets threshold values for storage of shared 
   *  object header message indexes in a file.
   *
   *  @param plist_id   IN: Identifier of file creation property list.
   *  @param max_list   IN: Threshold above which storage of a shared object header message index shifts from list to B-tree 
   *  @param min_btree  IN: Threshold below which storage of a shared object header message index reverts to list format 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_shared_mesg_phase_change(int plist_id, int max_list, int min_btree)
  throws HDF5LibraryException;

  /**
   *  H5Pget_shared_mesg_phase_change retrieves the threshold values for storage of 
   *  shared object header message indexes in a file. 
   *
   *  @param plist_id  IN: Identifier of file creation property list to query.
   *  @param index_num IN: Index being configured.
   *
   *  @return the thresholds information
   *  <ul>
   *      <li>thresholds[0] = max_list  // Threshold above which storage of a shared object header message index shifts from list to B-tree </li>
   *      <li>thresholds[1] = min_btree // Threshold below which storage of a shared object header message index reverts to list format </li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int[] H5Pget_shared_mesg_phase_change(int plist_id)
  throws HDF5LibraryException;

  /* File access property list (FAPL) routines */

  /**
   *  H5Pset_alignment sets the alignment properties of a
   *  file access property list so that any file object >=
   *  THRESHOLD bytes will be aligned on an address which
   *  is a multiple of ALIGNMENT.
   *
   *  @param fapl_id    IN: Identifier for a file access property list.
   *  @param threshold  IN: Threshold value.
   *  @param alignment  IN: Alignment value.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_alignment(int fapl_id, long threshold, long alignment)
  throws HDF5LibraryException;

  /**
   *  H5Pget_alignment retrieves the current settings for
   *  alignment properties from a file access property list.
   *
   *  @param fapl_id  IN: Identifier of a file access property list.
   *  
   *  @return the threshold value and alignment value.
   *  <ul>
   *      <li>alignment[0] = threshold // threshold value</li>
   *      <li>alignment[1] = alignment // alignment value</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long[] H5Pget_alignment(int fapl_id)
  throws HDF5LibraryException;
//  int H5Pget_alignment(int fapl_id, LongByReference threshold/*out*/,
//      LongByReference alignment/*out*/);
//  int H5Pset_driver(int plist_id, int driver_id, Pointer driver_info);

  /**
   *  H5Pget_driver returns the identifier of the low-level file driver associated 
   *  with the file access property list or data transfer property list plist_id. 
   *
   *  @param plist_id IN: File access or data transfer property list identifier.
   *
   *  @return a valid low-level driver identifier.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_driver(int plist_id)
  throws HDF5LibraryException;
//  Pointer H5Pget_driver_info(int plist_id);

  /**
   *  H5Pset_family_offset sets the offset property in the file access property 
   *  list so that the user application can retrieve a file handle for 
   *  low-level access to a particular member of a family of files.
   *
   *  @param plist_id IN: File access property list identifier.
   *  @param offset   IN: Offset in bytes within the HDF5 file.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_family_offset(int fapl_id, long offset)
  throws HDF5LibraryException;

  /**
   *  H5Pget_family_offset retrieves the value of offset from the file access 
   *  property list so that the user application can retrieve a file handle 
   *  for low-level access to a particular member of a family of files.
   *
   *  @param plist_id IN: File access property list identifier.
   *
   *  @return the offset of the data in the HDF5 file that is stored on disk in the selected member file in a family of files. 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_family_offset (int fapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_multi_type sets the type of data property in the file access property list.
   *
   *  @param fapl_id IN: File access property list identifier.
   *  @param type    IN: Type of data.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_multi_type(int fapl_id, H5FD_MEM type)
  throws HDF5LibraryException;

  /**
   *  H5Pget_multi_type retrieves the type of data setting from the file access or data transfer property list.
   *
   *  @param fapl_id IN: File access property list identifier.
   *
   *  @return the type of data
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5FD_MEM H5Pget_multi_type(int fapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_cache sets the number of elements (objects) in the meta
   *  data cache and the total number of bytes in the raw data chunk cache.
   *
   *  @param plist_id    IN: Identifier of the file access property list.
   *  @param rdcc_nslots IN: Number of elements (objects) in the raw data chunk cache.
   *  @param rdcc_nbytes IN: Total size of the raw data chunk cache, in bytes.
   *  @param rdcc_w0     IN: Preemption policy.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pset_cache(int plist_id, long rdcc_nslots, 
        long rdcc_nbytes, double rdcc_w0)
  throws HDF5LibraryException;

  /**
   *  Retrieves the maximum possible number of elements in the meta
   *  data cache and the maximum possible number of bytes and the
   *  RDCC_W0 value in the raw data chunk cache.
   *
   *  @param plist_id        IN: Identifier of the file access property list.
   *  @param rdcc           OUT: The raw data chunk cache information.
   *  <ul>
   *      <li>rdcc[0] = rdcc_nslots // Number of elements (objects) in the raw data chunk cache.</li>
   *      <li>rdcc[1] = rdcc_nbytes // Total size of the raw data chunk cache, in bytes.</li>
   *  </ul>
   *
   *  @return Preemption policy.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - rdcc array is null.
   **/
  public synchronized static native double H5Pget_cache(int plist_id, int[] rdcc)
  throws HDF5LibraryException, NullPointerException;
//  int H5Pget_cache(int plist_id,
//         LongByReference rdcc_nslots/*out*/, 
//         LongByReference rdcc_nbytes/*out*/, 
//         DoubleByReference rdcc_w0);
//  public synchronized static native int H5Pset_mdc_config(int plist_id, H5AC_cache_config_t config_ptr)
//  throws HDF5LibraryException, NullPointerException;
//  public synchronized static native int H5Pget_mdc_config(int plist_id, H5AC_cache_config_t config_ptr)
//  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pset_gc_references Sets the flag for garbage collecting
   *  references for the file.  Default value for garbage
   *  collecting references is off.
   *
   *  @param fapl_id  IN: File access property list
   *  @param gc_ref   IN: set GC on  (true) or off (false)
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void  H5Pset_gc_references(int fapl_id, boolean gc_ref)
  throws HDF5LibraryException;

  /**
   *  H5Pget_gc_references returns the current setting for the garbage 
   *  collection refernces property from a file access property list.
   *
   *  @param fapl_id  IN: File access property list
   *
   *  @return boolean true if garbage collection is on, false otherwise
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Pget_gc_references(int fapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_fclose_degree sets the file close degree property fc_degree in the 
   *  file access property list fapl_id.  
   *
   *  @param fapl_id   IN: File access property list
   *  @param fc_degree IN: The file close degree property, the value of fc_degree.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_fclose_degree(int fapl_id, H5F_CLOSE_DEGREE degree)
  throws HDF5LibraryException;

  /**
   *  H5Pget_fclose_degree returns the current setting of the file close degree 
   *  property fc_degree in the file access property list fapl_id. 
   *
   *  @param fapl_id   IN: File access property list
   *
   *  @return the file close degree property, the value of fc_degree.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5F_CLOSE_DEGREE H5Pget_fclose_degree(int fapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_meta_block_size sets the minimum size, in bytes, of metadata 
   *  block allocations when H5FD_FEAT_AGGREGATE_METADATA is set by a VFL driver. 
   *
   *  @param fapl_id  IN: File access property list
   *  @param size     IN: Minimum size, in bytes, of metadata block allocations.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_meta_block_size(int fapl_id, long size)
  throws HDF5LibraryException;

  /**
   *  H5Pget_meta_block_size returns the current minimum size, in bytes, of 
   *  new metadata block allocations.
   *
   *  @param fapl_id  IN: File access property list
   *
   *  @return minimum size, in bytes, of metadata block allocations.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_meta_block_size(int fapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_sieve_buf_size sets size, the maximum size in bytes of the data 
   *  sieve buffer, which is used by file drivers that are capable of using 
   *  data sieving. 
   *
   *  @param fapl_id  IN: File access property list
   *  @param size     IN: Maximum size, in bytes, of data sieve buffer.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_sieve_buf_size(int fapl_id, long size)
  throws HDF5LibraryException;

  /**
   *  H5Pget_sieve_buf_size retrieves, size, the current maximum size of 
   *  the data sieve buffer. 
   *
   *  @param fapl_id  IN: File access property list
   *
   *  @return maximum size, in bytes, of data sieve buffer.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_sieve_buf_size(int fapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_small_data_block_size reserves blocks of size bytes for the
   *  contiguous storage of the raw data portion of small datasets.
   *
   *  @param fapl_id IN: File access property list identifier.
   *  @param size    IN: Maximum size, in bytes, of the small data block. 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_small_data_block_size(int fapl_id, long size)
  throws HDF5LibraryException;

  /**
   *  H5Pget_small_data_block_size retrieves the current setting for the size of the small data block.
   *
   *  @param fapl_id  IN: File access property list identifier.
   *
   *  @return Maximum size, in bytes, of the small data block.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_small_data_block_size(int fapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_libver_bounds controls the versions of the object formats that 
   *  will be used when creating objects in a file.
   *
   *  @param fapl_id IN: Identifier for a file access property list.
   *  @param low     IN: The earliest version of the library that will be used for writing objects.
   *  @param high    IN: The latest version of the library that will be used for writing objects.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_libver_bounds(int plist_id, H5F_LIBVER low, H5F_LIBVER high)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pget_libver_bounds retrieves the lower and upper bounds on the 
   *  HDF5 Library versions that indirectly determine the object formats 
   *  versions used when creating objects in the file. 
   *
   *  @param fapl_id  IN: Identifier of a file access property list.
   *  
   *  @return the versions.
   *  <ul>
   *      <li>versions[0] = low  // The earliest version of the library that will be used for writing objects.</li>
   *      <li>versions[1] = high // The latest version of the library that will be used for writing objects.</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5F_LIBVER[] H5Pget_libver_bounds(int plist_id)
  throws HDF5LibraryException, NullPointerException;

  /* Dataset creation property list (DCPL) routines */

  /**
   *  H5Pset_layout sets the type of storage used store the
   *  raw data for a dataset.
   *
   *  @param plist_id  IN: Identifier of property list to query.
   *  @param layout    IN: Type of storage layout for raw data.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_layout(int plist_id, H5D_LAYOUT layout)
  throws HDF5LibraryException;

  /**
   *  H5Pget_layout returns the layout of the raw data for a dataset.
   *
   *  @param plist_id  IN: Identifier for property list to query.
   *
   *  @return the layout type of a dataset creation property.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5D_LAYOUT H5Pget_layout(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_chunk sets the size of the chunks used to store a
   *  chunked layout dataset.
   *
   *  @param dcpl_id IN: Dataset creation property list identifier.
   *  @param ndims   IN: The number of dimensions of each chunk.
   *  @param dim     IN: An array defining the size, in dataset elements, of each chunk.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - dim array is null.
   *  @exception IllegalArgumentException - ndims <=0 or ndims > H5S_MAX_RANK
   **/
  public synchronized static native void H5Pset_chunk(int dcpl_id, int ndims, long[/*ndims*/] dim)
  throws HDF5LibraryException, NullPointerException, IllegalArgumentException;

  /**
   *  H5Pget_chunk retrieves the size of chunks for the raw
   *  data of a chunked layout dataset.
   *
   *  @param dcpl_id    IN: Dataset creation property list identifier.
   *  @param max_ndims  IN: Size of the dims array.
   *  @param dims      OUT: Array to store the chunk dimensions.
   *
   *  @return chunk dimensionality
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - dims array is null.
   *  @exception IllegalArgumentException - max_ndims <=0
   **/
  public synchronized static native int H5Pget_chunk(int dcpl_id, int max_ndims, long dim[]/*out*/)
  throws HDF5LibraryException, NullPointerException, IllegalArgumentException;

  /**
   *  H5Pset_external adds an external file to the list of
   *  external files.
   *
   *  @param dcpl_id  IN: Identifier of a dataset creation property list.
   *  @param name     IN: Name of an external file.
   *  @param offset   IN: Offset, in bytes, from the beginning of the file to 
   *                      the location in the file where the data starts.
   *  @param size     IN: Number of bytes reserved in the file for the data.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Pset_external(int dcpl_id, String name, long offset, long size)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pget_external_count returns the number of external
   *  files for the specified dataset.
   *
   *  @param dcpl_id  IN: Identifier of a dataset creation property list.
   *
   *  @return the number of external files
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_external_count(int dcpl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pget_external returns information about an external file.
   *
   *  @param dcpl_id    IN: Identifier of a dataset creation property list.
   *  @param idx        IN: External file index.
   *  @param size      OUT: the offset value and the size of the external file data.
   *  <ul>
   *      <li>size[0] = offset // a location to return an offset value</li>
   *      <li>size[1] = size // a location to return the size of the external file data.</li>
   *  </ul>
   *
   *  @return Name of the external file.
   *
   *  @exception ArrayIndexOutOfBoundsException - Fatal error on Copyback
   *  @exception ArrayStoreException - Fatal error on Copyback
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - size is null.
   **/
  public synchronized static native String H5Pget_external(int dcpl_id, int idx, long[] size)
  throws ArrayIndexOutOfBoundsException, ArrayStoreException, HDF5LibraryException, NullPointerException ;
//  int H5Pget_external(int plist_id, int idx, long name_size,
//            String name/*out*/, LongByReference offset/*out*/,
//            LongByReference size/*out*/);
  public synchronized static native int H5Pmodify_filter(int plist_id, H5Z_FILTER filter,
          int flags, long cd_nelmts, int[/*cd_nelmts*/] cd_values)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pset_filter adds the specified filter and corresponding
   *  properties to the end of an output filter pipeline.
   *
   *  @param plist_id  IN: Property list identifier.
   *  @param filter    IN: Filter to be added to the pipeline.
   *  @param flags     IN: Bit vector specifying certain general properties of the filter.
   *  @param cd_nelmts IN: Number of elements in cd_values
   *  @param cd_values IN: Auxiliary data for the filter.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_filter(int plist_id, H5Z_FILTER filter,
          int flags, long cd_nelmts, int cd_values[])
  throws HDF5LibraryException;

  /**
   *  H5Pget_nfilters returns the number of filters defined in
   *  the filter pipeline associated with the property list plist.
   *
   *  @param plist_id IN: Property list identifier.
   *
   *  @return the number of filters in the pipeline
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_nfilters(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pget_filter2 returns information about a filter, specified
   *  by its filter number, in a filter pipeline, specified by
   *  the property list with which it is associated.
   *
   *  @param plist_id       IN: Property list identifier.
   *  @param filter_idx     IN: Sequence number within the filter pipeline of the 
   *                            filter for which information is sought.
   *  @param flags         OUT: Bit vector specifying certain general properties of the filter.
   *  @param cd_nelmts  IN/OUT: Number of elements in cd_values
   *  @param cd_values     OUT: Auxiliary data for the filter.
   *  @param filter_id     OUT: The filter identification number.
   *  @param filter_config OUT: Bit field, as described in  H5Zget_filter_info.
   *
   *  @return the name of the filter.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - an array is null.
   *
   **/
  public synchronized static native String H5Pget_filter2(int plist_id, int filter_idx,
         int[] flags/*out*/, int[] cd_nelmts/*out*/,
         int[] cd_values/*out*/, H5Z_FILTER[] filter_id /*out*/,
         int[] filter_config /*out*/)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pget_filter_by_id2 returns information about the filter specified 
   *  in filter_id, a filter identifier. 
   *
   *  @param plist_id       IN: Property list identifier.
   *  @param filter_id      IN: The filter identification number.
   *  @param flags         OUT: Bit vector specifying certain general properties of the filter.
   *  @param cd_nelmts  IN/OUT: Number of elements in cd_values
   *  @param cd_values     OUT: Auxiliary data for the filter.
   *  @param filter_config OUT: Bit field, as described in  H5Zget_filter_info.
   *
   *  @return the name of the filter.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - an array is null.
   *
   **/
  public synchronized static native String H5Pget_filter_by_id2(int plist_id, H5Z_FILTER filter_id,
         int[] flags/*out*/, long[] cd_nelmts/*out*/,
         int[] cd_values/*out*/, int[] filter_config/*out*/)
  throws HDF5LibraryException, NullPointerException;


  /**
   *  H5Pall_filters_avail verifies that all of the filters set in the dataset 
   *  creation property list dcpl_id are currently available. 
   *
   *  @param dcpl_id  IN: Identifier for the dataset creation property list.
   *
   *  @return boolean true if all filters are available and false if one or more is not currently available.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Pall_filters_avail(int dcpl_id)
  throws HDF5LibraryException;

  /**
   *  H5Premove_filter removes the specified filter from the filter pipeline in the 
   *  dataset creation property list plist. 
   *
   *  @param plist_id  IN: Identifier for the dataset creation property list.
   *  @param filter    IN: Filter to be deleted. 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Premove_filter(int plist_id, H5Z_FILTER filter)
  throws HDF5LibraryException;

  /**
   *  H5Pset_deflate sets the compression method for a dataset.
   *
   *  @param plist_id  IN: Identifier for the dataset creation property list.
   *  @param level     IN: Compression level.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_deflate(int plist_id, int level)
  throws HDF5LibraryException;

  /**
   *  H5Pset_szip sets an SZIP compression filter, H5Z_FILTER_SZIP, for a dataset.
   *
   *  @param plist_id         IN: Identifier for the dataset creation property list.
   *  @param options_mask     IN: A bit-mask conveying the desired SZIP options.
   *  @param pixels_per_block IN: The number of pixels or data elements in each data block.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_szip(int plist_id, 
      int options_mask, int pixels_per_block)
  throws HDF5LibraryException;

  /**
   *  H5Pset_shuffle sets the shuffle filter, H5Z_FILTER_SHUFFLE, in the dataset creation property list plist_id.   
   *
   *  @param plist_id  IN: Identifier for the dataset creation property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_shuffle(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_nbit sets the N-Bit filter, H5Z_FILTER_NBIT, in the dataset creation property list plist_id. 
   *
   *  @param plist_id  IN: Identifier for the dataset creation property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_nbit(int plist_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pset_scaleoffset sets the Scale-Offset filter, H5Z_FILTER_SCALEOFFSET, for a dataset. 
   *
   *  @param plist_id     IN: Identifier for the dataset creation property list.
   *  @param scale_type   IN: Flag indicating compression method.
   *  @param scale_factor IN: Parameter related to scale.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_scaleoffset(int plist_id, 
      H5Z_SO_SCALE scale_type, int scale_factor)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pset_fletcher32 sets the Fletcher32 checksum filter in the dataset creation property list plist.
   *
   *  @param plist_id  IN: Identifier for the dataset creation property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_fletcher32(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_fill_value sets the fill value for a dataset creation property list.
   *
   *  @param plist_id  IN: Dataset creation property list identifier.
   *  @param type_id   IN: The datatype identifier of value.
   *  @param value     IN: The value to use as fill value.
   *
   *  @return a non-negative value if successful
   *
   *  @exception HDF5Exception - Error converting data array
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - value is null.
   **/
  public synchronized static native void H5Pset_fill_value(int plist_id,
          int type_id, byte[] value)
  throws HDF5Exception, HDF5LibraryException, NullPointerException;

  /**
   *  H5Pget_fill_value queries the fill value defined in a dataset
   *  creation property list.
   *
   *  @param plist_id IN: Dataset creation property list identifier.
   *  @param type_id  IN: The datatype identifier of value.
   *
   *  @return the fill value.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native byte[] H5Pget_fill_value(int plist_id, int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Pfill_value_defined determines whether a fill value is defined in the dataset creation property list plist_id. 
   *
   *  @param plist_id  IN: Identifier for the dataset creation property list.
   *
   *  @return the status of fill value in property list.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5D_FILL_VALUE H5Pfill_value_defined(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_alloc_time sets up the timing for the allocation of storage space for a dataset's raw data.
   *
   *  @param plist_id   IN: Identifier for the dataset creation property list.
   *  @param alloc_time IN: When to allocate dataset storage space.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_alloc_time(int plist_id, H5D_ALLOC_TIME alloc_time)
  throws HDF5LibraryException;

  /**
   *  H5Pget_alloc_time retrieves the timing for allocating storage space for a dataset's raw data.
   *
   *  @param plist_id IN: Property list identifier.
   *
   *  @return the timing for allocating storage space
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5D_ALLOC_TIME H5Pget_alloc_time(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_fill_time sets up the timing for writing fill values to a dataset.
   *
   *  @param plist_id  IN: Identifier for the dataset creation property list.
   *  @param fill_time IN: When to write fill values to a dataset.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_fill_time(int plist_id, H5D_FILL_TIME fill_time)
  throws HDF5LibraryException;

  /**
   *  H5Pget_fill_time examines the dataset creation property list plist_id to 
   *  determine when fill values are to be written to a dataset. 
   *
   *  @param plist_id IN: Property list identifier.
   *
   *  @return the timing of writing fill values to the dataset.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5D_FILL_TIME H5Pget_fill_time(int plist_id)
  throws HDF5LibraryException;

  /* Dataset access property list (DAPL) routines */

  /**
   *  H5Pset_chunk_cache sets the number of elements (objects) in the meta
   *  data cache and the total number of bytes in the raw data chunk cache.
   *
   *  @param plist_id    IN: Identifier of the datset access property list.
   *  @param rdcc_nslots IN: Number of elements (objects) in the raw data chunk cache.
   *  @param rdcc_nbytes IN: Total size of the raw data chunk cache, in bytes.
   *  @param rdcc_w0     IN: Preemption policy.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_chunk_cache(int dapl_id, long rdcc_nslots, long rdcc_nbytes, double rdcc_w0)
  throws HDF5LibraryException;

  /**
   *  Retrieves the maximum possible number of elements in the meta
   *  data cache and the maximum possible number of bytes and the
   *  RDCC_W0 value in the raw data chunk cache.
   *
   *  @param plist_id        IN: Identifier of the dataset access property list.
   *  @param rdcc           OUT: The raw data chunk cache information.
   *  <ul>
   *      <li>rdcc[0] = rdcc_nslots // Number of elements (objects) in the raw data chunk cache.</li>
   *      <li>rdcc[1] = rdcc_nbytes // Total size of the raw data chunk cache, in bytes.</li>
   *  </ul>
   *
   *  @return Preemption policy.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - rdcc array is null.
   **/
  public synchronized static native double H5Pget_chunk_cache(int dapl_id, long[] rdcc/*out*/)
  throws HDF5LibraryException, NullPointerException;
//  int H5Pget_chunk_cache(int dapl_id, LongByReference rdcc_nslots/*out*/,
//      LongByReference rdcc_nbytes/*out*/, DoubleByReference rdcc_w0/*out*/);

  /* Dataset xfer property list (DXPL) routines */

  /**
   *  H5Pset_data_transform sets the data transform to be used for reading and writing data.
   *
   *  @param plist_id   IN: Identifier for the dataset creation property list.
   *  @param expression IN: The null-terminated data transform expression 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_data_transform(int plist_id, String expression)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pget_data_transform retrieves the data transform expression previously set in the 
   *  dataset transfer property list plist_id by H5Pset_data_transform. 
   *
   *  @param plist_id IN: Property list identifier.
   *
   *  @return the transform expression
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Pget_data_transform(int plist_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pset_buffer sets type conversion and background buffers.
   *  status to TRUE or FALSE.
   *
   *  Given a dataset transfer property list, H5Pset_buffer sets the maximum
   *  size for the type conversion buffer and background buffer and optionally
   *  supplies pointers to application-allocated buffers. If the buffer size
   *  is smaller than the entire amount of data being transferred between the
   *  application and the file, and a type conversion buffer or background
   *  buffer is required, then strip mining will be used.
   *
   *  Note that there are minimum size requirements for the buffer. Strip
   *  mining can only break the data up along the first dimension, so the
   *  buffer must be large enough to accommodate a complete slice that
   *  encompasses all of the remaining dimensions. For example, when strip
   *  mining a 100x200x300 hyperslab of a simple data space, the buffer must
   *  be large enough to hold 1x200x300 data elements. When strip mining a
   *  100x200x300x150 hyperslab of a simple data space, the buffer must be
   *  large enough to hold 1x200x300x150 data elements.
   *
   *  If tconv and/or bkg are null pointers, then buffers will be allocated
   *  and freed during the data transfer.
   *
   *  @param plist_id  IN: Identifier for the dataset transfer property list.
   *  @param size      IN: Size, in bytes, of the type conversion and background buffers.
   *  @param tconv     IN: byte array of application-allocated type conversion buffer.
   *  @param bkg       IN: byte array of application-allocated background buffer.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception IllegalArgumentException - plist_id is invalid.
   **/
  public synchronized static native void H5Pset_buffer(int plist_id, long size,
          byte[] tconv, byte[] bkg)
  throws HDF5LibraryException, IllegalArgumentException;

  /**
   *  H5Pget_buffer gets type conversion and background buffers.
   *
   *  @param plist_id  IN: Identifier for the dataset transfer property list.
   *  @param tconv    OUT: byte array of application-allocated type conversion buffer.
   *  @param bkg      OUT: byte array of application-allocated background buffer.
   *
   *  @return buffer size, in bytes
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception IllegalArgumentException - plist is invalid.
   *  @exception NullPointerException - tconv or bkg is null.
   **/
  public synchronized static native long H5Pget_buffer(int plist_id, byte[] tconv/*out*/, byte[] bkg/*out*/)
  throws HDF5LibraryException, NullPointerException, IllegalArgumentException;

  /**
   *  H5Pset_edc_check sets the dataset transfer property list plist to enable 
   *  or disable error detection when reading data. 
   *
   *  @param plist_id IN: Identifier for the dataset transfer property list.
   *  @param check    IN: Specifies whether error checking is enabled or disabled for dataset read operations. 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_edc_check(int plist_id, H5Z_EDC check)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Pget_edc_check queries the dataset transfer property list plist to 
   *  determine whether error detection is enabled for data read operations. 
   *
   *  @param plist_id IN: Dataset transfer property list identifier.
   *
   *  @return whether error detection is enabled for data read operations. 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5Z_EDC H5Pget_edc_check(int plist_id)
  throws HDF5LibraryException, NullPointerException;
//  int H5Pset_filter_callback(int plist_id, H5Z_filter_func_t func,
//                                       Pointer op_data);

  /**
   *  H5Pset_btree_ratio Sets B-tree split ratios for a dataset
   *  transfer property list. The split ratios determine what
   *  percent of children go in the first node when a node splits.
   *
   *  @param plist_id  IN: Dataset transfer property list
   *  @param left      IN: The B-tree split ratio for leftmost nodes
   *  @param right     IN: The B-tree split ratio for rightmost nodes
   *  @param middle    IN: The B-tree split ratio for all other nodes
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_btree_ratios(int plist_id,
          double left, double middle, double right)
  throws HDF5LibraryException;

  /**
   *  H5Pget_btree_ratio Get the B-tree split ratios for a dataset
   *  transfer property list.
   *
   *  @param plist_id  IN Dataset transfer property list
   *
   *  @return split_ratio
   *  <ul>
   *      <li>split_ratio[0] = left   // The B-tree split ratio for leftmost nodes</li>
   *      <li>split_ratio[1] = right  // The B-tree split ratio for rightmost nodes</li>
   *      <li>split_ratio[2] = middle // The B-tree split ratio for all other nodes</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native double[] H5Pget_btree_ratios(int plist_id)
  throws HDF5LibraryException;
//  int H5Pget_btree_ratios(int plist_id, DoubleByReference left/*out*/,
//        DoubleByReference middle/*out*/, DoubleByReference right/*out*/);
  
//  int H5Pset_vlen_mem_manager(int plist_id,
//                                         H5MM_allocate_t alloc_func,
//                                         Pointer alloc_info, H5MM_free_t free_func,
//                                         Pointer free_info);
//  int H5Pget_vlen_mem_manager(int plist_id,
//                                         H5MM_allocate_t alloc_func,
//                                         PointerByReference alloc_info,
//                                         H5MM_free_t free_func,
//                                         PointerByReference free_info);

  /**
   *  H5Pset_hyper_vector_size sets the number of I/O vectors to be accumulated 
   *  in memory before being issued to the lower levels of the HDF5 library 
   *  for reading or writing the actual data.  
   *
   *  @param plist_id IN: Identifier for the dataset transfer property list.
   *  @param size     IN: Number of I/O vectors to accumulate in memory for I/O operations. 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_hyper_vector_size(int plist_id, long size)
  throws HDF5LibraryException;

  /**
   *  H5Pset_hyper_vector_size retrieves the number of I/O vectors to be 
   *  accumulated in memory before being issued to the lower levels of 
   *  the HDF5 library for reading or writing the actual data. 
   *
   *  @param plist_id IN: Identifier for the dataset transfer property list.
   *
   *  @return the number of I/O vectors to accumulate in memory for I/O operations. 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_hyper_vector_size(int plist_id)
  throws HDF5LibraryException;
//  public synchronized static native int H5Pset_type_conv_cb(int dxpl_id, H5T_conv_except_func_t op, Pointer operate_data)
//  throws HDF5LibraryException, NullPointerException;
//  public synchronized static native int H5Pget_type_conv_cb(int dxpl_id, H5T_conv_except_func_t op, PointerByReference operate_data)
//  throws HDF5LibraryException, NullPointerException;

  /* Link creation property list (LCPL) routines */

  /**
   *  H5Pset_create_intermediate_group specifies whether to set the link 
   *  creation property list lcpl_id so that calls to functions that create 
   *  objects in groups different from the current working group will create 
   *  intermediate groups that may be missing in the path of a new or moved 
   *  object.  
   *
   *  @param plist_id  IN: Identifier for the link creation property list.
   *  @param crt_intmd IN: Flag specifying whether to create intermediate groups upon the creation of an object.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_create_intermediate_group(int plist_id, int crt_intmd)
  throws HDF5LibraryException;

  /**
   *  H5Pget_create_intermediate_group determines whether the link creation 
   *  property list lcpl_id is set to allow functions that create objects in 
   *  groups different from the current working group to create intermediate 
   *  groups that may be missing in the path of a new or moved object. 
   *
   *  @param plist_id IN: Identifier for the link creation property list.
   *
   *  @return the flag specifying whether to create intermediate groups upon creation of an object
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_create_intermediate_group(int plist_id)
  throws HDF5LibraryException;

  /* Group creation property list (GCPL) routines */

  /**
   *  H5Pset_local_heap_size_hint sets the local heap size hint, size_hint, 
   *  in the group creation property list, gcpl_id, for original-style groups. 
   *
   *  @param plist_id  IN: Identifier for the group creation property list.
   *  @param size_hint IN: Hint for size of local heap 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_local_heap_size_hint(int plist_id, long size_hint)
  throws HDF5LibraryException;

  /**
   *  H5Pget_local_heap_size_hint queries the group creation property list, 
   *  gcpl_id, for the local heap size hint, size_hint, for original-style groups
   *
   *  @param plist_id IN: Identifier for the group creation property list.
   *
   *  @return the hint for size of local heap
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_local_heap_size_hint(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_link_phase_change sets the maximum number of entries for a 
   *  compact group and the minimum number of links to allow before 
   *  converting a dense group to back to the compact format. 
   *
   *  @param plist_id     IN: Identifier for the group creation property list.
   *  @param max_compact  IN: Maximum number of links for compact storage 
   *  @param min_dense    IN: Minimum number of links for dense storage  
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_link_phase_change(int plist_id, int max_compact, int min_dense)
  throws HDF5LibraryException;

  /**
   *  H5Pget_link_phase_change queries the maximum number of entries for a 
   *  compact group and the minimum number links to require before converting 
   *  a group to a dense form. 
   *
   *  @param plist_id  IN: Identifier of the group creation property list.
   *
   *  @return links
   *  <ul>
   *      <li>links[0] = max_compact // Maximum number of links for compact storage</li>
   *      <li>links[1] = min_dense // Minimum number of links for dense storage</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int[] H5Pget_link_phase_change(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_est_link_info inserts two settings into the group creation 
   *  property list gcpl_id: the estimated number of links that are expected 
   *  to be inserted into a group created with the property list and the 
   *  estimated average length of those link names. 
   *
   *  @param plist_id        IN: Identifier for the group creation property list.
   *  @param est_num_entries IN: Specifies whether error checking is enabled or disabled for dataset read operations. 
   *  @param est_name_len    IN: Specifies whether error checking is enabled or disabled for dataset read operations. 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_est_link_info(int plist_id, int est_num_entries, int est_name_len)
  throws HDF5LibraryException;

  /**
   *  H5Pget_est_link_info queries a group creation property list, gcpl_id, 
   *  for its “estimated number of links” and “estimated average name length” 
   *  settings. 
   *
   *  @param plist_id  IN: Identifier of the group creation property list.
   *
   *  @return links
   *  <ul>
   *      <li>links[0] = est_num_entries // Specifies whether error checking is enabled or disabled for dataset read operations.</li>
   *      <li>links[1] = est_name_len    // Specifies whether error checking is enabled or disabled for dataset read operations.</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int[] H5Pget_est_link_info(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_link_creation_order sets flags in a group creation property list, 
   *  gcpl_id, for tracking and/or indexing links on creation order.  
   *
   *  @param plist_id        IN: Identifier for the group creation property list.
   *  @param crt_order_flags IN: Creation order flag(s)
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_link_creation_order(int plist_id, int crt_order_flags)
  throws HDF5LibraryException;

  /**
   *  H5Pget_link_creation_order queries the group creation property list, 
   *  gcpl_id, and returns a flag indicating whether link creation order 
   *  is tracked and/or indexed in a group. 
   *
   *  @param plist_id IN: Identifier for the group creation property list.
   *
   *  @return the creation order flag(s)
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_link_creation_order(int plist_id)
  throws HDF5LibraryException;

  /* String creation property list (STRCPL) routines */

  /**
   *  H5Pset_char_encoding sets the character encoding used to encode 
   *  strings or object names that are created with the property list plist_id.  
   *
   *  @param plist_id IN: Identifier for the string creation property list.
   *  @param encoding IN: String encoding character set.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_char_encoding(int plist_id, H5T_CSET encoding)
  throws HDF5LibraryException;

  /**
   *  H5Pget_char_encoding retrieves the character encoding used to encode 
   *  strings or object names that are created with the property list plist_id. 
   *
   *  @param plist_id IN: Identifier for the string creation property list.
   *
   *  @return the string encoding character set.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_CSET H5Pget_char_encoding(int plist_id)
  throws HDF5LibraryException;

  /* Link access property list (LAPL) routines */

  /**
   *  H5Pset_nlinks sets the maximum number of soft or user-defined link 
   *  traversals allowed, nlinks, before the library assumes it has found 
   *  a cycle and aborts the traversal. 
   *
   *  @param plist_id IN: Identifier for the link access property list.
   *  @param nlinks   IN: Maximum number of links to traverse.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_nlinks(int plist_id, long nlinks)
  throws HDF5LibraryException;

  /**
   *  
   *  H5Pget_nlinks retrieves the maximum number of soft or user-defined 
   *  link traversals allowed, nlinks, before the library assumes it has 
   *  found a cycle and aborts the traversal.
   *
   *  @param plist_id IN: Identifier for the link access property list.
   *
   *  @return the maximum number of links to traverse.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Pget_nlinks(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_elink_prefix sets the prefix to be applied to the path of any 
   *  external links traversed. 
   *
   *  @param plist_id IN: Identifier for the link access property list.
   *  @param prefix   IN: Prefix to be applied to external link paths 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_elink_prefix(int plist_id, String prefix)
  throws HDF5LibraryException;

  /**
   *  H5Pget_elink_prefix retrieves the prefix applied to the path of any 
   *  external links traversed. 
   *
   *  @param plist_id IN: Identifier for the link access property list.
   *
   *  @return the prefix to be applied to external link paths 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Pget_elink_prefix(int plist_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_elink_fapl sets the file access property list identifier, 
   *  fapl_id, to use when the target file of the external link associated 
   *  with lapl_id is accessed. 
   *
   *  @param lapl_id IN: Link access property list identifier
   *  @param fapl_id IN: File access property list identifier
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_elink_fapl(int lapl_id, int fapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pget_elink_fapl retrieves the file access property list identifier 
   *  that is set for the link access property list identifier
   *
   *  @param lapl_id IN: Link access property list identifier
   *
   *  @return the file access property list identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_elink_fapl(int lapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Pset_elink_acc_flags 
   *
   *  @param lapl_id IN: Link access property list identifier
   *  @param flags   IN: Flags.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_elink_acc_flags(int lapl_id, int flags)
  throws HDF5LibraryException;

  /**
   *  H5Pget_elink_acc_flags
   *
   *  @param plist_id IN: Link access property list identifier
   *
   *  @return the flags
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_elink_acc_flags(int lapl_id)
  throws HDF5LibraryException;
//  public synchronized static native int H5Pset_elink_cb(int lapl_id, H5L_elink_traverse_t func, Pointer op_data)
//  throws HDF5LibraryException, NullPointerException;
//  public synchronized static native int H5Pget_elink_cb(int lapl_id, H5L_elink_traverse_t func, PointerByReference op_data)
//  throws HDF5LibraryException, NullPointerException;

  /* Object copy property list (OCPYPL) routines */

  /**
   *  H5Pset_copy_object sets properties in the object copy property list 
   *  ocp_plist_id that will be invoked when a new copy is made of an existing 
   *  object.  
   *
   *  @param plist_id  IN: Object copy property list identifier 
   *  @param crt_intmd IN: Copy option(s) to be set
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Pset_copy_object(int plist_id, int crt_intmd)
  throws HDF5LibraryException;

  /**
   *  H5Pget_copy_object retrieves the properties currently specified in 
   *  the object copy property list ocp_plist_id, which will be invoked when 
   *  a new copy is made of an existing object. 
   *
   *  @param plist_id IN: Object copy property list identifier
   *
   *  @return the copy option(s)
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Pget_copy_object(int plist_id)
  throws HDF5LibraryException;

}
