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
import hdf.h5.structs.H5G_info_t;

//////////////////////////////////////////////////////////////
//                                                          //
//             H5G: Group Interface Functions               //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5G {

  /**
   *  H5Gcreate2 creates a new group with the specified name at
   *  the specified location, loc_id.
   *
   *  @param loc_id    IN: The file or group identifier.
   *  @param name      IN: The absolute or relative name of the new group.
   *  @param lcpl_id   IN: Identifier of link creation property list.
   *  @param gcpl_id   IN: Identifier of group creation property list.
   *  @param gapl_id   IN: Identifier of group access property list.
   *                       (No group access properties have been implemented at this time; use H5P_DEFAULT.)
   *
   *  @return a valid group identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Gcreate2(int loc_id, String name, int lcpl_id,
        int gcpl_id, int gapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Gcreate_anon creates a new empty group in the file specified by loc_id.
   *
   *  @param loc_id    IN: File or group identifier specifying the file in which the new group is to be created.
   *  @param gcpl_id   IN: Identifier of group creation property list.
   *  @param gapl_id   IN: Identifier of group access property list.
   *                       (No group access properties have been implemented at this time; use H5P_DEFAULT.)
   *
   *  @return a valid group identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Gcreate_anon(int loc_id, int gcpl_id, int gapl_id)
      throws HDF5LibraryException;

  /**
   *  H5Gopen2 opens an existing group, name, at the location specified by loc_id. 
   *
   *  @param loc_id   IN: File or group identifier specifying the location of the group to be opened.
   *  @param name     IN: Name of group to open.
   *  @param gapl_id  IN: Identifier of group access property list.
   *                       (No group access properties have been implemented at this time; use H5P_DEFAULT.)
   *
   *  @return a valid group identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Gopen2(int loc_id, String name, int gapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Gget_create_plist returns an identifier for the group creation 
   *  property list associated with the group specified by group_id. 
   *
   *  @param group_id IN: Identifier of the group.
   *
   *  @return an identifier for the group’s creation property list
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Gget_create_plist(int group_id)
      throws HDF5LibraryException;

  /**
   *  H5Gget_info retrieves information about the group specified by 
   *  group_id. The information is returned in the group_info struct.  
   *
   *  @param group_id IN: Identifier of the group.
   *
   *  @return a structure in which group information is returned 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5G_info_t H5Gget_info(int group_id)
      throws HDF5LibraryException;
//  int H5Gget_info(int loc_id, H5G_info_t ginfo);

  /**
   *  H5Gget_info_by_name retrieves information about the group group_name 
   *  located in the file or group specified by loc_id.
   *
   *  @param group_id IN: File or group identifier.
   *  @param name     IN: Name of group for which information is to be retrieved.
   *  @param lapl_id  IN: Link access property list.
   *
   *  @return a structure in which group information is returned 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native H5G_info_t H5Gget_info_by_name(int group_id, String name, int lapl_id)
      throws HDF5LibraryException, NullPointerException;
//  int H5Gget_info_by_name(int group_id, String name, H5G_info_t ginfo, int lapl_id);  

  /**
   *  H5Gget_info_by_idx retrieves information about a group, according to the
   *  group’s position within an index.  
   *
   *  @param group_id   IN: File or group identifier.
   *  @param group_name IN: Name of group for which information is to be retrieved.
   *  @param idx_type   IN: Type of index by which objects are ordered  
   *  @param order      IN: Order of iteration within index 
   *  @param n          IN: Attribute’s position in index 
   *  @param lapl_id    IN: Link access property list.
   *
   *  @return a structure in which group information is returned 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native H5G_info_t H5Gget_info_by_idx(int group_id, String group_name,
        H5_INDEX idx_type, H5_ITER order, long n, int lapl_id)
      throws HDF5LibraryException, NullPointerException;
//  int H5Gget_info_by_idx(int group_id, String group_name,
//        H5_index_t idx_type, H5_iter_order_t order, long n, H5G_info_t ginfo, int lapl_id);  

  /**
   *  H5Gclose releases resources used by a group which was opened
   *  by a call to H5Gcreate() or H5Gopen().
   *
   *  @param group_id  Group identifier to release.
   *
   *  @return a non-negative value if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Gclose(int group_id)
  throws HDF5LibraryException;

}
