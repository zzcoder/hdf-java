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
   *  H5Gcreate creates a new group with the specified name at
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
  public static int H5Gcreate(int loc_id, String name, int lcpl_id,
        int gcpl_id, int gapl_id)
      throws HDF5LibraryException, NullPointerException
  {
    return H5Gcreate2(loc_id, name, lcpl_id, gcpl_id, gapl_id);
  }
  /**
   *  H5Gcreate2 creates a new group with the specified name at
   *  the specified location, loc_id.
   *
   *  @see public static int H5Gcreate(int loc_id, String name, int lcpl_id,
   *     int gcpl_id, int gapl_id)
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
   *  H5Gopen opens an existing group, name, at the location specified by loc_id. 
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
  public static int H5Gopen(int loc_id, String name, int gapl_id)
      throws HDF5LibraryException, NullPointerException
  {
    return H5Gopen2(loc_id, name, gapl_id);
  }
  /**
   *  H5Gopen2 opens an existing group, name, at the location specified by loc_id. 
   *
   *  @see public static int H5Gopen(int loc_id, String name, int gapl_id)
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
   *  @param n          IN: Attribute's position in index 
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
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Gclose(int group_id)
  throws HDF5LibraryException;

  
  
  /**
   *  H5Gcreate creates a new group with the specified name at
   *  the specified location, loc_id.
   *
   *  @deprecated As of HDF5 1.8, replaced by {@link #H5Gcreate2(int, String, int, int, int)}
   *
   *  @param loc_id    IN: The file or group identifier.
   *  @param name      IN: The absolute or relative name of the new group.
   *  @param size_hint IN: the number of bytes to reserve for the names that will appear in the group.
   *
   *  @return a valid group identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public static int H5Gcreate(int loc_id, String name, long size_hint)
      throws HDF5LibraryException, NullPointerException
  {
    return H5Gcreate1(loc_id, name, size_hint);
  }
  /**
   *  H5Gcreate1 creates a new group with the specified name at
   *  the specified location, loc_id.
   *
   *  @deprecated As of HDF5 1.8, replaced by {@link #H5Gcreate2(int, String, int, int, int)}
   *
   *  @see public static int H5Gcreate(int loc_id, String name, long size_hint)
   **/
  private synchronized static native int H5Gcreate1(int loc_id, String name, long size_hint)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Gopen opens an existing group, name, at the location specified by loc_id. 
   *
   *  @deprecated As of HDF5 1.8, replaced by {@link #H5Gopen(int, String, int)}
   *
   *  @param loc_id   IN: File or group identifier specifying the location of the group to be opened.
   *  @param name     IN: Name of group to open.
   *
   *  @return a valid group identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public static int H5Gopen(int loc_id, String name)
      throws HDF5LibraryException, NullPointerException
  {
    return H5Gopen1(loc_id, name);
  }
  /**
   *  H5Gopen1 opens an existing group, name, at the location specified by loc_id. 
   *
   *  @deprecated As of HDF5 1.8, replaced by {@link #H5Gopen2(int, String, int)}
   *
   *  @see public static int H5Gopen(int loc_id, String name)
   **/
  private synchronized static native int H5Gopen1(int loc_id, String name)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Glink creates a new name for an already existing object.
   *
   *  @deprecated As of HDF5 1.8
   *
   *  @param loc_id    IN: File, group, dataset, or datatype identifier.
   *  @param link_type IN: Link type. Possible values are:
   *  <UL>
   *  <LI>
   *  H5G_LINK_HARD
   *  </LI>
   *  <LI>
   *  H5G_LINK_SOFT.
   *  </LI>
   *  </UL>
   *  @param current_name IN: A name of the existing object if link is
   *                          a hard link. Can be anything for the soft link.
   *  @param new_name     IN: New name for the object.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - current_name or name is null.
   **/
  public synchronized static native void H5Glink(int loc_id, int link_type,
      String current_name, String new_name)
      throws HDF5LibraryException, NullPointerException;


  /**
   *  H5Glink2 creates a new name for an already existing object.
   *
   *  @deprecated As of HDF5 1.8
   *
   *  @param curr_loc_id  IN: File, group, dataset, or datatype identifier.
   *  @param current_name IN: A name of the existing object if link is
   *                          a hard link. Can be anything for the soft link.
   *  @param link_type    IN: Link type. Possible values are:
   *  <UL>
   *  <LI>
   *  H5G_LINK_HARD
   *  </LI>
   *  <LI>
   *  H5G_LINK_SOFT.
   *  </LI>
   *  </UL>
   *  @param new_loc_id   IN: New file, group, dataset, or datatype identifier.
   *  @param new_name     IN: New name for the object.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - current_name or name is null.
   **/
  public synchronized static native void H5Glink2(int curr_loc_id,
      String current_name, int link_type, int new_loc_id, String new_name )
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Gunlink removes an association between a name and an object.
   *
   *  @deprecated As of HDF5 1.8
   *
   *  @param loc_id IN: Identifier of the file containing the object.
   *  @param name   IN: Name of the object to unlink.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Gunlink(int loc_id, String name)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Gmove renames an object within an HDF5 file.
   *  The original name, src, is unlinked from the group graph
   *  and the new name, dst, is inserted as an atomic operation.
   *  Both names are interpreted relative to loc_id, which is
   *  either a file or a group identifier.
   *
   *  @deprecated As of HDF5 1.8
   *
   *  @param loc_id IN: File or group identifier.
   *  @param src    IN: Object's original name.
   *  @param dst    IN: Object's new name.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - src or dst is null.
   **/
  public synchronized static native void H5Gmove(int loc_id, String src,
      String dst)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Gget_linkval returns size characters of the link value
   *  through the value argument if loc_id (a file or group
   *  identifier) and name specify a symbolic link.
   *
   *  @deprecated As of HDF5 1.8
   *
   *  @param loc_id IN: Identifier of the file, group, dataset, or datatype.
   *  @param name   IN: Name of the object whose link value is to be checked.
   *  @param size   IN: Maximum number of characters of value to be returned.
   *
   *  @return  the link value
   *
   *  @exception ArrayIndexOutOfBoundsException   Copy back failed
   *  @exception ArrayStoreException  Copy back failed
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   *  @exception IllegalArgumentException - size is invalid
   **/
  public synchronized static native String H5Gget_linkval(int loc_id, String name,
      int size)
      throws ArrayIndexOutOfBoundsException,
      ArrayStoreException,
      HDF5LibraryException,
      NullPointerException,
      IllegalArgumentException;


  /**
   *  H5Gset_comment sets the comment for the the object name
   *  to comment.   Any previously existing comment is overwritten.
   *
   *  @deprecated As of HDF5 1.8
   *
   *  @param loc_id  IN: Identifier of the file, group, dataset, or datatype.
   *  @param name    IN: Name of the object whose comment is to be set or reset.
   *  @param comment IN: The new comment.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name or comment is null.
   **/
  public synchronized static native void H5Gset_comment(int loc_id, String name,
      String comment)
      throws HDF5LibraryException, NullPointerException;


  /**
   *  H5Gget_comment retrieves the comment for the the object name.
   *  The comment is returned in the buffer comment.
   *
   *  @deprecated As of HDF5 1.8
   *
   *  @param loc_id   IN: Identifier of the file, group, dataset, or datatype.
   *  @param name     IN: Name of the object whose comment is to be set or reset.
   *  @param bufsize  IN: Anticipated size of the buffer required to hold comment.
   *  @param comment OUT: The comment.
   *  
   *  @return the number of characters in the comment, counting the null terminator
   *
   *  @exception ArrayIndexOutOfBoundsException - JNI error writing back data
   *  @exception ArrayStoreException - JNI error writing back data
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   *  @exception IllegalArgumentException - size < 1, comment is invalid.
   **/
  public synchronized static native int H5Gget_comment(int loc_id, String name,
      int bufsize,
      String[] comment)
      throws ArrayIndexOutOfBoundsException,
      ArrayStoreException,
      HDF5LibraryException,
      NullPointerException,
      IllegalArgumentException;
  
/*
/////////////////////////////////////////////////////////////////////////////////
//
//
//Add these methods so that we don't need to call H5Gget_objtype_by_idx
//in a loop to get information for all the object in a group, which takes
//a lot of time to finish if the number of objects is more than 10,000
//
/////////////////////////////////////////////////////////////////////////////////
*/
  
/**
 * retrieves information of all objects under the group (name) located in the 
 * file or group specified by loc_id.
 * 
 * @param loc_id     IN:  File or group identifier
 * @param name       IN:  Name of group for which information is to be retrieved
 * @param objNames   OUT: Names of all objects under the group, name.
 * @param objTypes   OUT: Types of all objects under the group, name.
 */
  public synchronized static void H5Gget_obj_info_all( int loc_id,
          String name, String[] objNames, int[] objTypes)
  throws HDF5LibraryException, NullPointerException
  {
      if (name == null || name.length()<=0) {
          throw new NullPointerException("H5Gget_obj_info_all(): name is null");
      }

      if (objNames == null) {
          throw new NullPointerException("H5Gget_obj_info_all(): name array is null");
      }

      if (objTypes == null) {
          throw new NullPointerException("H5Gget_obj_info_all(): type array is null");
      }

      if (objNames.length <= 0) {
          throw new HDF5LibraryException("H5Gget_obj_info_all(): array size is zero");
      }

      if (objNames.length != objTypes.length) {
          throw new HDF5LibraryException("H5Gget_obj_info_all(): name and type array sizes are different");
      }

      H5Gget_obj_info_all( loc_id, name, objNames, objTypes, objNames.length);
  }

  private synchronized static native void H5Gget_obj_info_all( int loc_id,
          String name, String[] oname, int[]type, int n)
  throws HDF5LibraryException, NullPointerException;

}
