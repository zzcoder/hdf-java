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

import hdf.h5.enums.H5_INDEX;
import hdf.h5.enums.H5_ITER;
import hdf.h5.exceptions.HDF5LibraryException;

public class H5O {
//  // Prototype for H5Ovisit/H5Ovisit_by_name() operator
//  public interface H5O_iterate_t extends Callback {
//    int callback(int obj, String name, H5O_info_t info,
//      Pointer op_data);
//  }
  

  /**
   *  H5Oopen opens a group, dataset, or named datatype specified by a location and a path name.
   *
   *  @param loc_id  IN: File or group identifier 
   *  @param name    IN: Relative path to the object
   *  @param lapl_id IN: Access property list identifier for the link pointing to the object 
   *
   *  @return an object identifier for the opened object
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Oopen(int loc_id, String name, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Oopen_by_addr opens a group, dataset, or named datatype using its address within an HDF5 file.
   *
   *  @param loc_id  IN: File or group identifier 
   *  @param addr    IN: Object’s address in the file 
   *
   *  @return an object identifier for the opened object
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Oopen_by_addr(int loc_id, long addr)
      throws HDF5LibraryException;

  /**
   *  H5Oopen_by_idx opens the nth object in the group specified.
   *
   *  @param loc_id     IN: File or group identifier 
   *  @param group_name IN: Name of group, relative to loc_id, in which object is located
   *  @param idx_type   IN: Type of index by which objects are ordered  
   *  @param order      IN: Order of iteration within index 
   *  @param n          IN: Object to open 
   *  @param lapl_id    IN: Access property list identifier for the link pointing to the object 
   *
   *  @return an object identifier for the opened object
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - group_name is null.
   **/
  public synchronized static native int H5Oopen_by_idx(int loc_id, String group_name,
          H5_INDEX idx_type, H5_ITER order, long n, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

//  /**
//   *  H5Oget_info retrieves the metadata for an object specified by an identifier. 
//   *
//   *  @param loc_id  IN: Identifier for target object 
//   *  @param oinfo  OUT: Buffer in which to return object information
//   *
//   *  @return none
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   *  @exception NullPointerException - name is null.
//   **/
//  public synchronized static native void H5Oget_info(int loc_id, H5O_info_t oinfo)
//      throws HDF5LibraryException, NullPointerException;
//
//  /**
//   *  H5Oget_info_by_name retrieves the metadata for an object, identifying the object by location and relative name. 
//   *
//   *  @param loc_id  IN: File or group identifier specifying location of group in which object is located
//   *  @param name    IN: Relative name of group
//   *  @param oinfo  OUT: Buffer in which to return object information
//   *  @param lapl_id IN: Access property list identifier for the link pointing to the object (Not currently used; pass as H5P_DEFAULT.)
//   *
//   *  @return none
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   *  @exception NullPointerException - name is null.
//   **/
//  public synchronized static native void H5Oget_info_by_name(int loc_id, String name, 
//          H5O_info_t oinfo, int lapl_id)
//      throws HDF5LibraryException, NullPointerException;
//
//  /**
//   *  H5Oget_info_by_idx retrieves the metadata for an object, identifying the object by an index position. 
//   *
//   *  @param loc_id     IN: File or group identifier 
//   *  @param group_name IN: Name of group, relative to loc_id, in which object is located
//   *  @param idx_type   IN: Type of index by which objects are ordered  
//   *  @param order      IN: Order of iteration within index 
//   *  @param n          IN: Object to open 
//   *  @param oinfo     OUT: Buffer in which to return object information
//   *  @param lapl_id    IN: Access property list identifier for the link pointing to the object (Not currently used; pass as H5P_DEFAULT.)
//   *
//   *  @return none
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   *  @exception NullPointerException - name is null.
//   **/
//  public synchronized static native void H5Oget_info_by_idx(int loc_id, String group_name,
//          H5_index_t idx_type, H5_iter_order_t order, long n, H5O_info_t oinfo, int lapl_id)
//      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Olink creates a new hard link to an object in an HDF5 file. 
   *
   *  @param obj_id      IN: Object to be linked.
   *  @param new_loc_id  IN: File or group identifier specifying location at which object is to be linked. 
   *  @param name        IN: Relative name of link to be created.
   *  @param lcpl_id     IN: Link creation property list identifier. 
   *  @param lapl_id     IN: Access property list identifier.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Olink(int obj_id, int new_loc_id, String new_name,
          int lcpl_id, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Oincr_refcount increments the hard link reference count for an object.
   *
   *  @param object_id  IN: Object identifier 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Oincr_refcount(int object_id)
      throws HDF5LibraryException;

  /**
   *  H5Odecr_refcount decrements the hard link reference count for an object.
   *
   *  @param object_id  IN: Object identifier 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Odecr_refcount(int object_id)
      throws HDF5LibraryException;

  /**
   *  H5Ocopy copies the group, dataset or named datatype specified from the file or 
   *  group specified by source location to the destination location. 
   *
   *  @param src_loc_id  IN: Object identifier indicating the location of the source object to be copied 
   *  @param src_name    IN: Name of the source object to be copied
   *  @param dst_loc_id  IN: Location identifier specifying the destination  
   *  @param dst_name    IN: Name to be assigned to the new copy 
   *  @param ocpypl_id   IN: Object copy property list  
   *  @param lcpl_id     IN: Link creation property list for the new hard link  
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Ocopy(int src_loc_id, String src_name, int dst_loc_id,
          String dst_name, int ocpypl_id, int lcpl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Oset_comment sets the comment for the specified object.
   *
   *  @param obj_id  IN: Identifier of the target object
   *  @param comment IN: The new comment.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - comment is null.
   **/
  public synchronized static native void H5Oset_comment(int obj_id, String comment)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Oset_comment_by_name sets the comment for the specified object.
   *
   *  @param loc_id  IN: Identifier of a file, group, dataset, or named datatype.
   *  @param name    IN: Relative name of the object whose comment is to be set or reset.
   *  @param comment IN: The new comment.
   *  @param lapl_id IN: Link access property list identifier. 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Oset_comment_by_name(int loc_id, String name,
          String comment, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Oget_comment retrieves the comment for the specified object.
   *
   *  @param obj_id  IN: File or group identifier 
   *
   *  @return the comment
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Oget_comment(int obj_id)
      throws HDF5LibraryException;
//  long H5Oget_comment(int obj_id, String comment, long bufsize);

  /**
   *  H5Oget_comment_by_name retrieves the comment for an object.
   *
   *  @param loc_id  IN: Identifier of a file, group, dataset, or named datatype.
   *  @param name    IN: Relative name of the object whose comment is to be set or reset.
   *  @param lapl_id IN: Link access property list identifier. 
   *
   *  @return the comment
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native String H5Oget_comment_by_name(int loc_id, String name, int lapl_id)
      throws HDF5LibraryException, NullPointerException;
//  long H5Oget_comment_by_name(int loc_id, String name, String comment, long bufsize, int lapl_id);

//  /**
//   *  H5Ovisit recursively visits all objects accessible from a specified object. 
//   *
//   *  @param obj_id     IN: Identifier of the object at which the recursive iteration begins.  
//   *  @param idx_type   IN: Type of index  
//   *  @param order      IN: Order of iteration within index 
//   *  @param op         IN: Callback function passing data regarding the object to the calling application  
//   *  @param op_data    IN: User-defined pointer to data required by the application for its processing of the object 
//   *
//   *  @return returns the return value of the first operator that returns a positive value, or zero if all members were 
//         processed with no operator returning non-zero.
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   *  @exception NullPointerException - name is null.
//   **/
//  public synchronized static native int H5Ovisit(int obj_id, H5_index_t idx_type, H5_iter_order_t order,
//          H5O_iterate_t op, Pointer op_data)
//      throws HDF5LibraryException, NullPointerException;
//
//  /**
//   *  H5Ovisit_by_name recursively visits all objects starting from a specified object.
//   *
//   *  @param loc_id    IN: File or group identifier 
//   *  @param obj_name  IN: Relative path to the object
//   *  @param idx_type   IN: Type of index  
//   *  @param order      IN: Order of iteration within index 
//   *  @param op         IN: Callback function passing data regarding the object to the calling application  
//   *  @param op_data    IN: User-defined pointer to data required by the application for its processing of the object 
//   *  @param lapl_id   IN: Link access property list identifier
//   *
//   *  @return returns the return value of the first operator that returns a positive value, or zero if all members 
//        were processed with no operator returning non-zero.
//   *
//   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
//   *  @exception NullPointerException - name is null.
//   **/
//  public synchronized static native int H5Ovisit_by_name(int loc_id, String obj_name,
//          H5_index_t idx_type, H5_iter_order_t order, H5O_iterate_t op,
//          Pointer op_data, int lapl_id)
//      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Oclose closes the group, dataset, or named datatype specified.
   *
   *  @param object_id  IN: Object identifier 
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Oclose(int object_id)
      throws HDF5LibraryException;

}
