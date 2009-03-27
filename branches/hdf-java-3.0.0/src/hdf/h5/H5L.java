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

import hdf.h5.enums.H5L_TYPE;
import hdf.h5.enums.H5_INDEX;
import hdf.h5.enums.H5_ITER;
import hdf.h5.exceptions.HDF5LibraryException;

public class H5L {
//  // Callback prototypes for user-defined links
//  // Link creation callback
//  public interface H5L_create_func_t extends Callback {
//    int callback(String link_name, int loc_group, Pointer lnkdata,
//        long lnkdata_size, int lcpl_id);
//  }
//  // Callback for when the link is moved
//  public interface H5L_move_func_t extends Callback {
//    int callback(String new_name, int new_loc, Pointer lnkdata,
//        long lnkdata_size);
//  }
//  // Callback for when the link is copied
//  public interface H5L_copy_func_t extends Callback {
//    int callback(String new_name, int new_loc, Pointer lnkdata,
//        long lnkdata_size);
//  }
//  // Callback during link traversal
//  public interface H5L_traverse_func_t extends Callback {
//    int callback(String link_name, int cur_group, Pointer lnkdata,
//        long lnkdata_size, int lapl_id);
//  }
//  // Callback for when the link is deleted
//  public interface H5L_delete_func_t extends Callback {
//    int callback(String link_name, int file, Pointer lnkdata, long lnkdata_size);
//  }
//  // Callback for querying the link
//  // Returns the size of the buffer needed
//  public interface H5L_query_func_t extends Callback {
//    long callback(String link_name, Pointer lnkdata, long lnkdata_size,
//        Pointer buf /* out */, long buf_size);
//  }
//  // Prototype for H5Literate/H5Literate_by_name() operator
//  public interface H5L_iterate_t extends Callback {
//    int callback(int group, String name, H5L_info_t info, Pointer op_data);
//  }
//  // Callback for external link traversal
//  public interface H5L_elink_traverse_t extends Callback {
//    int callback(String parent_file_name, String parent_group_name,
//        String child_file_name, String child_object_name,
//        IntByReference acc_flags, int fapl_id, Pointer op_data);
//  }

  /**
   *  H5Lmove renames a link within an HDF5 file.
   *
   *  @param src_loc   IN: Original file or group identifier.
   *  @param src_name  IN: Original link name.
   *  @param dst_loc   IN: Destination file or group identifier.
   *  @param dst_name  IN: New link name.
   *  @param lcpl_id   IN: Link creation property list identifier to be associated with the new link.
   *  @param lapl_id   IN: Link access property list identifier to be associated with the new link.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Lmove(int src_loc, String src_name, int dst_loc,
      String dst_name, int lcpl_id, int lapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Lcopy copies a link from one location to another. 
   *
   *  @param src_loc   IN: Location identifier of the source link 
   *  @param src_name  IN: Name of the link to be copied 
   *  @param dst_loc   IN: Location identifier specifying the destination of the copy 
   *  @param dst_name  IN: Name to be assigned to the new copy
   *  @param lcpl_id   IN: Link creation property list identifier
   *  @param lapl_id   IN: Link access property list identifier
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Lcopy(int src_loc, String src_name, int dst_loc,
      String dst_name, int lcpl_id, int lapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Lcreate_hard creates a new hard link to a pre-existing object in an HDF5 file.
   *
   *  @param cur_loc   IN: The file or group identifier for the target object.
   *  @param cur_name  IN: Name of the target object, which must already exist.
   *  @param dst_loc   IN: The file or group identifier for the new link.
   *  @param dst_name  IN: The name of the new link.
   *  @param lcpl_id   IN: Link creation property list identifier
   *  @param lapl_id   IN: Link access property list identifier
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Lcreate_hard(int cur_loc, String cur_name,
      int dst_loc, String dst_name, int lcpl_id, int lapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Lcreate_soft creates a new soft link to an object in an HDF5 file.
   *
   *  @param link_target IN: Path to the target object, which is not required to exist.
   *  @param link_loc_id IN: The file or group identifier for the new link.
   *  @param link_name   IN: The name of the new link.
   *  @param lcpl_id     IN: Link creation property list identifier
   *  @param lapl_id     IN: Link access property list identifier
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Lcreate_soft(String link_target, int link_loc_id,
      String link_name, int lcpl_id, int lapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Ldelete removes the link specified from a group. 
   *
   *  @param loc_id  IN: Identifier of the file or group containing the object.
   *  @param name    IN: Name of the link to delete.
   *  @param lapl_id IN: Link access property list identifier
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Ldelete(int loc_id, String name, int lapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Ldelete_by_idx removes the nth link in a group according to the specified order 
   *  and in the specified index.
   *
   *  @param loc_id     IN: File or group identifier specifying location of subject group
   *  @param group_name IN: Name of subject group
   *  @param idx_type   IN: Index or field which determines the order 
   *  @param order      IN: Order within field or index
   *  @param n          IN: Link for which to retrieve information 
   *  @param lapl_id    IN: Link access property list identifier 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - group_name is null.
   **/
  public synchronized static native void H5Ldelete_by_idx(int loc_id, String group_name,
      H5_INDEX idx_type, H5_ITER order, long n, int lapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Lget_val returns the link value of a symbolic link.
   *
   *  @param loc_id  IN: Identifier of the file or group containing the object.
   *  @param name    IN: Name of the link to delete.
   *  @param size    IN: Maximum number of characters of link value to be returned.
   *  @param lapl_id IN: Link access property list identifier
   *
   *  @return the link value (byte[]) of maximum size
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native byte[] H5Lget_val(int loc_id, String name,
      long size, int lapl_id)
  throws HDF5LibraryException, NullPointerException;
//  int H5Lget_val(int loc_id, String name, Pointer buf/*out*/, long size, int lapl_id);

  /**
   *  H5Lget_val_by_idx retrieves value of the nth link in a group, according to the order within an index. 
   *
   *  @param loc_id     IN: File or group identifier specifying location of subject group
   *  @param group_name IN: Name of subject group
   *  @param idx_type   IN: Type of index
   *  @param order      IN: Order within field or index
   *  @param n          IN: Link for which to retrieve information 
   *  @param size       IN: Maximum number of characters of link value to be returned.
   *  @param lapl_id    IN: Link access property list identifier 
   *
   *  @return the link value (byte[]) of maximum size
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - group_name is null.
   **/
  public synchronized static native byte[] H5Lget_val_by_idx(int loc_id, String group_name,
      H5_INDEX idx_type, H5_ITER order, long n, long size, int lapl_id)
  throws HDF5LibraryException, NullPointerException;
//  int H5Lget_val_by_idx(int loc_id, String group_name,
//      H5_INDEX idx_type, H5_ITER order, long n,
//      Pointer buf/*out*/, long size, int lapl_id);

  /**
   *  H5Lexists checks if a link with a particular name exists in a group. 
   *
   *  @param loc_id  IN: Identifier of the file or group to query. 
   *  @param name    IN: The name of the link to check. 
   *  @param lapl_id IN: Link access property list identifier
   *
   *  @return a boolean, true if the name exists, otherwise false.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native boolean H5Lexists(int loc_id, String name, int lapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Lget_info returns information about the specified link.
   *
   *  @param loc_id  IN: Identifier of the file or group. 
   *  @param name    IN: Name of the link for which information is being sought.
   *  @param lapl_id IN: Link access property list identifier
   *
   *  @return a buffer(H5L_info_t) for the link information.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native byte[] H5Lget_info(int loc_id, String name,
      int lapl_id)
  throws HDF5LibraryException, NullPointerException;
//  int H5Lget_info(int loc_id, String name, H5L_info_t linfo /*out*/, int lapl_id);

  /**
   *  H5Lget_info_by_idx opens a named datatype at the location specified
   *  by loc_id and return an identifier for the datatype.
   *
   *  @param loc_id     IN: File or group identifier specifying location of subject group
   *  @param group_name IN: Name of subject group
   *  @param idx_type   IN: Type of index
   *  @param order      IN: Order within field or index
   *  @param n          IN: Link for which to retrieve information 
   *  @param lapl_id    IN: Link access property list identifier 
   *
   *  @return a buffer(H5L_info_t) for the link information.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - group_name is null.
   **/
  public synchronized static native byte[] H5Lget_info_by_idx(int loc_id, String group_name,
      H5_INDEX idx_type, H5_ITER order, long n, int lapl_id)
  throws HDF5LibraryException, NullPointerException;
//  int H5Lget_info_by_idx(int loc_id, String group_name,
//      H5_INDEX idx_type, H5_ITER order, long n,
//      H5L_info_t linfo /*out*/, int lapl_id);

  /**
   *  H5Lget_name_by_idx retrieves name of the nth link in a group, according to 
   *  the order within a specified field or index. 
   *
   *  @param loc_id     IN: File or group identifier specifying location of subject group
   *  @param group_name IN: Name of subject group
   *  @param idx_type   IN: Type of index
   *  @param order      IN: Order within field or index
   *  @param n          IN: Link for which to retrieve information 
   *  @param lapl_id    IN: Link access property list identifier 
   *
   *  @return a String for the link name.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - group_name is null.
   **/
  public synchronized static native String H5Lget_name_by_idx(int loc_id, String group_name,
      H5_INDEX idx_type, H5_ITER order, long n, int lapl_id)
  throws HDF5LibraryException, NullPointerException;
//  long H5Lget_name_by_idx(int loc_id, String group_name,
//      H5_INDEX idx_type, H5_ITER order, long n,
//      String name /*out*/, long size, int lapl_id)

//  /**
//   *  H5Literate iterates through links in a group. 
//   *
//   *  @param grp_id     IN: Identifier specifying subject group
//   *  @param idx_type   IN: Type of index  
//   *  @param order      IN: Order of iteration within index 
//   *  @param idx        IN: Iteration position at which to start  
//   *  @param op         IN: Callback function passing data regarding the link to the calling application  
//   *  @param op_data    IN: User-defined pointer to data required by the application for its processing of the link 
//   *
//   *  @return returns the return value of the first operator that returns a positive value, or zero if all members were 
//   *      processed with no operator returning non-zero.
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   **/
//  public synchronized static native int H5Literate(int grp_id, 
//      H5_INDEX idx_type, H5_ITER order,
//      LongByReference idx, H5L_iterate_t op, Pointer op_data)
//  throws HDF5LibraryException;

// /**
//  *  H5Literate_by_name iterates through links in a group. 
//  *
//  *  @param grp_id     IN: Identifier specifying subject group
//  *  @param group_name IN: Name of subject group
//  *  @param idx_type   IN: Type of index  
//  *  @param order      IN: Order of iteration within index 
//  *  @param idx        IN: Iteration position at which to start  
//  *  @param op         IN: Callback function passing data regarding the link to the calling application  
//  *  @param op_data    IN: User-defined pointer to data required by the application for its processing of the link 
//  *  @param lapl_id    IN: Link access property list identifier 
//  *
//  *  @return returns the return value of the first operator that returns a positive value, or zero if all members were 
//  *    processed with no operator returning non-zero.
//  *
//  *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//  *  @exception NullPointerException - group_name is null.
//  **/
//  public synchronized static native int H5Literate_by_name(int loc_id, String group_name,
//      H5_INDEX idx_type, H5_ITER order, LongByReference idx,
//      H5L_iterate_t op, Pointer op_data, int lapl_id)
//  throws HDF5LibraryException, NullPointerException;

// /**
//  *  H5Lvisit recursively visits all links starting from a specified group.
//  *
//  *  @param grp_id     IN: Identifier specifying subject group
//  *  @param idx_type   IN: Type of index  
//  *  @param order      IN: Order of iteration within index 
//  *  @param op         IN: Callback function passing data regarding the link to the calling application  
//  *  @param op_data    IN: User-defined pointer to data required by the application for its processing of the link 
//  *
//  *  @return returns the return value of the first operator that returns a positive value, or zero if all members were 
//  *      processed with no operator returning non-zero.
//  *
//  *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//  **/
//  public synchronized static native int H5Lvisit(int grp_id, 
//      H5_INDEX idx_type, H5_ITER order,
//      H5L_iterate_t op, Pointer op_data)
//  throws HDF5LibraryException;

// /**
//  *  H5Lvisit_by_name recursively visits all links starting from a specified group. 
//  *
//  *  @param loc_id     IN: Identifier specifying subject group
//  *  @param group_name IN: Name of subject group
//  *  @param idx_type   IN: Type of index  
//  *  @param order      IN: Order of iteration within index 
//  *  @param op         IN: Callback function passing data regarding the link to the calling application  
//  *  @param op_data    IN: User-defined pointer to data required by the application for its processing of the link 
//  *
//  *  @return returns the return value of the first operator that returns a positive value, or zero if all members were 
//  *      processed with no operator returning non-zero.
//  *
//  *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//  *  @exception NullPointerException - group_name is null.
//  **/
//  public synchronized static native int H5Lvisit_by_name(int loc_id, String group_name,
//      H5_INDEX idx_type, H5_ITER order, H5L_iterate_t op,
//      Pointer op_data, int lapl_id)
//  throws HDF5LibraryException, NullPointerException;

  // UD link functions

  /**
   *  H5Lcreate_ud creates a link of user-defined type.
   *
   *  @param link_loc_id IN: Link location identifier 
   *  @param link_name   IN: Link name.
   *  @param link_type   IN: User-defined link class
   *  @param udata       IN: User-supplied link information 
   *  @param udata_size  IN: Size of user-supplied link information 
   *  @param lcpl_id     IN: Link creation property list identifier
   *  @param lapl_id     IN: Link access property list identifier
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - link_name is null.
   **/
  public synchronized static native void H5Lcreate_ud(int link_loc_id, String link_name,
      H5L_TYPE link_type, byte[] udata, long udata_size, int lcpl_id,
      int lapl_id)
  throws HDF5LibraryException, NullPointerException;

//  /**
//   *  H5Lregister registers a class of user-defined links, or changes 
//   *  the behavior of an existing class. 
//   *
//   *  @param cls  IN: Struct describing user-defined link class 
//   *
//   *  @return none
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   **/
//  public synchronized static native void H5Lregister(H5L_class_t cls)
//  throws HDF5LibraryException;

  /**
   *  H5Lunregister unregisters a class of user-defined links.
   *
   *  @param id  IN: User-defined link class identifier 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Lunregister(H5L_TYPE id)
  throws HDF5LibraryException;

  /**
   *  H5Lis_registered tests whether a user-defined link class is currently 
   *  registered, either by the HDF5 Library or by the user.
   *
   *  @param id   IN: User-defined link class identifier 
   *
   *  @return true if the link class has been registered and false if it is unregistered.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception IllegalArgumentException - identifier is not a valid 
   *                                        user-defined class identifier.
   **/
  public synchronized static native boolean H5Lis_registered(H5L_TYPE id)
  throws HDF5LibraryException, IllegalArgumentException;

  // External link functions

  /**
   *  H5Lunpack_elink_val decodes the external link information.
   *
   *  @param ext_linkval IN: Buffer containing external link information 
   *  @param link_size   IN: Size, in bytes, of the ext_linkval  buffer 
   *  @param file_path  OUT: file and object path information.
   *      <pre>
   *          file_path[0] = filename
   *          file_path[1] = obj_path  //relative to filename
   *      </pre>
   *
   *  @return an external link flags, packed as a bitmap (Reserved as a bitmap for flags; no flags are currently defined, so the only valid value is 0.)
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Lunpack_elink_val(byte[] ext_linkval, 
      long link_size, String[] file_path /*out*/)
  throws HDF5LibraryException;
//  int H5Lunpack_elink_val(Pointer ext_linkval/*in*/, long link_size,
//      IntByReference flags/*out*/, Buffer filename/*out*/, Buffer obj_path /*out*/);

  /**
   *  H5Lcreate_external creates a new soft link to an external object, which is 
   *  an object in a different HDF5 file from the location of the link. 
   *
   *  @param file_name   IN: Name of the target file containing the target object.
   *  @param obj_name    IN: Path within the target file to the target object.
   *  @param link_loc_id IN: The file or group identifier for the new link. 
   *  @param link_name   IN: The name of the new link.
   *  @param lcpl_id     IN: Link creation property list identifier
   *  @param lapl_id     IN: Link access property list identifier
   *
   *  @return a named datatype identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Lcreate_external(String file_name, String obj_name,
      int link_loc_id, String link_name, int lcpl_id, int lapl_id)
  throws HDF5LibraryException, NullPointerException;

}
