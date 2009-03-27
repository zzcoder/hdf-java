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

import hdf.h5.enums.H5_INDEX;
import hdf.h5.enums.H5_ITER;
import hdf.h5.exceptions.HDF5Exception;
import hdf.h5.exceptions.HDF5LibraryException;
import hdf.h5.structs.H5A_info_t;

//////////////////////////////////////////////////////////////
//                                                          //
//             H5A: Attribute Interface Functions           //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5A {
//  
//  public interface H5A_operator2_t extends Callback {
//    int callback(int location_id/*in*/,
//        String attr_name/*in*/, H5A_info_t ainfo/*in*/, Pointer op_data/*in,out*/);
//  }
  /**
   *  H5Acreate2 creates an attribute which is attached to the
   *  object specified with loc_id.
   *
   *  @param loc_id    IN: Object (dataset, group, or named datatype) to be attached to.
   *  @param attr_name IN: Name of attribute to create.
   *  @param type_id   IN: Identifier of datatype for attribute.
   *  @param space_id  IN: Identifier of dataspace for attribute.
   *  @param acpl_id   IN: Identifier of creation property list (currently not used).
   *  @param aapl_id   IN: Identifier of access property list (currently not used).
   *
   *  @return an attribute identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Acreate2(int loc_id, String attr_name, int type_id,
        int space_id, int acpl_id, int aapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Acreate_by_name creates an attribute which is attached to the
   *  object specified with loc_id and obj_name.
   *
   *  @param loc_id    IN: Object (dataset, group, or named datatype) to be attached to.
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is to be attached to.
   *  @param attr_name IN: Name of attribute to create.
   *  @param type_id   IN: Identifier of datatype for attribute.
   *  @param space_id  IN: Identifier of dataspace for attribute.
   *  @param acpl_id   IN: Identifier of creation property list (currently not used).
   *  @param aapl_id   IN: Identifier of access property list (currently not used).
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @return an attribute identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Acreate_by_name(int loc_id, String obj_name, String attr_name,
        int type_id, int space_id, int acpl_id, int aapl_id, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aopen opens an existing attribute, attr_name, that is attached to an object 
   *  specified an object identifier, obj_id. 
   *
   *  @param obj_id    IN: Identifer for object to which attribute is attached 
   *  @param attr_name IN: Name of attribute to open
   *  @param aapl_id   IN: Identifier of access property list
   *
   *  @return attribute identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - attr_name is null.
   **/
  public synchronized static native int H5Aopen(int obj_id, String attr_name, int aapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aopen_by_name opens an existing attribute, attr_name, that is attached to an 
   *  object specified by location and name, loc_id and obj_name, respectively. 
   *
   *  @param loc_id    IN: Location from which to find object to which attribute is attached 
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param attr_name IN: Name of attribute to open.
   *  @param aapl_id   IN: Identifier of access property list.
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @return attribute identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Aopen_by_name(int loc_id,  String obj_name,
        String attr_name, int aapl_id, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aopen_by_idx opens an existing attribute that is attached to an object 
   *  specified by location and name, loc_id and obj_name, respectively.
   *
   *  @param loc_id    IN: Location from which to find object to which attribute is attached 
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param idx_type  IN: Type of index by which objects are ordered  
   *  @param order     IN: Order of iteration within index 
   *  @param n         IN: Object to open 
   *  @param aapl_id   IN: Identifier of access property list (currently not used; should be passed in as H5P_DEFAULT.).
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @return attribute identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Aopen_by_idx(int loc_id,  String obj_name,
        H5_INDEX idx_type, H5_ITER order, long n, int aapl_id,
        int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Awrite writes an attribute, specified with attr_id. The
   *  attribute's memory datatype is specified with mem_type_id.
   *  The entire attribute is written from buf to the file.
   *
   *  @param attr_id     IN: Identifier of an attribute to write.
   *  @param mem_type_id IN: Identifier of the attribute datatype (in memory).
   *  @param buf         IN: Data to be written.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - data is null.
   **/
  public synchronized static native void H5Awrite(int attr_id, int mem_type_id,
          byte[] buf)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Awrite writes an attribute, specified with attr_id. The
   *  attribute's memory datatype is specified with mem_type_id.
   *  The entire attribute is written from data object to the file.
   *
   *  @param attr_id     IN: Identifier of an attribute to write.
   *  @param mem_type_id IN: Identifier of the attribute datatype (in memory).
   *  @param obj         IN: Data object to be written.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - data object is null.
   *  See public synchronized static native int H5Awrite(int attr_id, int mem_type_id, byte[] buf);
   **/
  public synchronized static void H5Awrite(int attr_id, int mem_type_id, Object obj )
      throws HDF5Exception, NullPointerException
  {
      HDFArray theArray = new HDFArray(obj);
      byte[] buf = theArray.byteify();

      H5Awrite(attr_id, mem_type_id, buf);
      buf = null;
      theArray = null;
  }
//  public synchronized static native void H5Awrite(int attr_id, int type_id, Buffer buf)
//  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aread reads an attribute, specified with attr_id. The
   *  attribute's memory datatype is specified with mem_type_id.
   *  The entire attribute is read into buf from the file.
   *
   *  @param attr_id     IN: Identifier of an attribute to read.
   *  @param mem_type_id IN: Identifier of the attribute datatype (in memory).
   *  @param buf        OUT: Buffer for data to be read.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - data buffer is null.
   **/
  public synchronized static native void H5Aread(int attr_id, int mem_type_id, byte[] buf)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aread reads an attribute, specified with attr_id. The
   *  attribute's memory datatype is specified with mem_type_id.
   *  The entire attribute is read into data object from the file.
   *
   *  @param attr_id     IN: Identifier of an attribute to read.
   *  @param mem_type_id IN: Identifier of the attribute datatype (in memory).
   *  @param obj        OUT: Object for data to be read.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - data buffer is null.
   *  See public synchronized static native int H5Aread( )
   **/
  public synchronized static void H5Aread(int attr_id, int mem_type_id, Object obj)
      throws HDF5Exception, NullPointerException
  {
      HDFArray theArray = new HDFArray(obj);
      byte[] buf = theArray.emptyBytes();

      //  This will raise an exception if there is an error
      H5Aread(attr_id, mem_type_id, buf);

      obj = theArray.arrayify( buf);
  }

  public synchronized static native void H5AreadVL(int attr_id, int mem_type_id, String[] buf)
  throws HDF5LibraryException, NullPointerException;
//  public synchronized static native int H5Aread(int attr_id, int type_id, Pointer buf)
//  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aclose terminates access to the attribute specified by
   *  its identifier, attr_id.
   *
   *  @param attr_id  IN: Attribute to release access to.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Aclose(int attr_id)
      throws HDF5LibraryException;

  /**
   *  H5Aget_space retrieves a copy of the dataspace for an attribute.
   *
   *  @param attr_id IN: Identifier of an attribute.
   *
   *  @return attribute dataspace identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Aget_space(int attr_id) 
      throws HDF5LibraryException;

  /**
   *  H5Aget_type retrieves a copy of the datatype for an attribute.
   *
   *  @param attr_id  IN: Identifier of an attribute.
   *
   *  @return a datatype identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Aget_type(int attr_id) 
      throws HDF5LibraryException;
  
  public synchronized static native int H5Aget_create_plist(int attr_id)
  throws HDF5LibraryException;

  /**
   *  H5Aget_name retrieves the name of an attribute specified by
   *  the identifier, attr_id.
   *
   *  @param attr_id   IN: Identifier of the attribute.
   *  @param buf_size  IN: The size of the buffer to store the name in.
   *  @param name     OUT: Buffer to store name in.
   *
   *  @return the length of the attribute's name if successful.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception ArrayIndexOutOfBoundsException  JNI error writing back array
   *  @exception ArrayStoreException   JNI error writing back array
   *  @exception NullPointerException - name is null.
   *  @exception IllegalArgumentException - buf_size <= 0.
   **/
  public synchronized static native long H5Aget_name(int attr_id, long buf_size,
          String[] name)
      throws HDF5LibraryException, ArrayIndexOutOfBoundsException, ArrayStoreException,
          NullPointerException, IllegalArgumentException;

  /**
   *  H5Aget_name retrieves the name of an attribute specified by
   *  the identifier, attr_id.
   *
   *  @param attr_id   IN: Identifier of the attribute.
   *
   *  @return the attribute's name.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Aget_name(int attr_id)
      throws HDF5LibraryException;
//  public synchronized static native long H5Aget_name(int attr_id, long buf_size, Buffer buf)
//  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aget_name_by_idx retrieves the name of an attribute that is attached to an object, which is 
   *  specified by its location and name, loc_id and obj_name, respectively.
   *
   *  @param loc_id    IN: Location of object to which attribute is attached 
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param idx_type  IN: Type of index by which objects are ordered  
   *  @param order     IN: Order of iteration within index 
   *  @param n         IN: Attribute’s position in index . 
   *  @param name     OUT: Buffer to store attribute name in.
   *  @param size      IN: The size of the buffer to store the name in.
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @return the length of the attribute's name if successful.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception ArrayIndexOutOfBoundsException  JNI error writing back array
   *  @exception ArrayStoreException   JNI error writing back array
   *  @exception NullPointerException - name is null.
   *  @exception IllegalArgumentException - buf_size <= 0.
   **/
  public synchronized static native long H5Aget_name_by_idx(int loc_id, String obj_name,
        H5_INDEX idx_type, H5_ITER order, long n,
        String[] name /*out*/, long size, int lapl_id)
      throws HDF5LibraryException, ArrayIndexOutOfBoundsException, ArrayStoreException,
        NullPointerException, IllegalArgumentException;
  /**
   *  H5Aget_name_by_idx retrieves the name of an attribute that is attached to an object, which is 
   *  specified by its location and name, loc_id and obj_name, respectively.
   *
   *  @param loc_id    IN: Location from which to find object to which attribute is attached 
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param idx_type  IN: Type of index by which objects are ordered  
   *  @param order     IN: Order of iteration within index 
   *  @param n         IN: Object to open 
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @return attribute identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native String H5Aget_name_by_idx(int loc_id, String obj_name,
        H5_INDEX idx_type, H5_ITER order, long n, int lapl_id)
      throws HDF5LibraryException, NullPointerException;
//  public synchronized static native long H5Aget_name_by_idx(int loc_id, String obj_name,
//      H5_INDEX idx_type, H5_ITER order, long n,
//      Buffer name /*out*/, long size, int lapl_id)
//  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aget_storage_size returns the amount of storage that 
   *  is required for the specified attribute, attr_id. 
   *
   *  @param attr_id   IN: Identifier of the attribute.
   *
   *  @return the amount of storage size allocated for the attribute.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Aget_storage_size(int attr_id)
      throws HDF5LibraryException;

  /**
   *  H5Aget_info retrieves attribute information, locating the 
   *  attribute with an attribute identifier, attr_id. 
   *
   *  @param attr_id   IN: Identifier of the attribute.
   *
   *  @return the attribute information.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5A_info_t H5Aget_info(int attr_id)
      throws HDF5LibraryException;
//  int H5Aget_info(int attr_id, H5A_info_t ainfo /*out*/);

  /**
   *  H5Aget_info_by_name retrieves information for an attribute, attr_name, that is attached to an object, 
   *  specified by its location and name, loc_id and obj_name, respectively.
   *
   *  @param loc_id    IN: Location of object to which attribute is attached .
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param attr_name IN: Name of attribute.
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native H5A_info_t H5Aget_info_by_name(int loc_id, String obj_name,
        String attr_name, int lapl_id)
      throws HDF5LibraryException, NullPointerException;
//  int H5Aget_info_by_name(int loc_id, String obj_name,
//      String attr_name, H5A_info_t ainfo /*out*/, int lapl_id);

  /**
   *  H5Aget_info_by_idx retrieves information for an attribute that is attached to an object, which is 
   *  specified by its location and name, loc_id and obj_name, respectively. The attribute is located 
   *  by its index position. 
   *
   *  @param loc_id    IN: Location of object to which attribute is attached .
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param idx_type  IN: Type of index by which objects are ordered  
   *  @param order     IN: Order of iteration within index 
   *  @param n         IN: Attribute’s position in index 
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native H5A_info_t H5Aget_info_by_idx(int loc_id, String obj_name,
        H5_INDEX idx_type, H5_ITER order, long n, int lapl_id)
      throws HDF5LibraryException, NullPointerException;
//  int H5Aget_info_by_idx(int loc_id, String obj_name,H5_INDEX idx_type, 
//      H5_ITER order, long n, H5A_info_t ainfo /*out*/, int lapl_id);

  /**
   *  H5Arename changes the name of the attribute located at loc_id. 
   *
   *  @param loc_id    IN: Location of object to which attribute is attached .
   *  @param old_name  IN: Name of the attribute to be changed.
   *  @param new_name  IN: New name of attribute.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Arename(int loc_id, String old_name, String new_name)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aget_info_by_name retrieves information for an attribute, attr_name, that is attached to an object, 
   *  specified by its location and name, loc_id and obj_name, respectively.
   *
   *  @param loc_id         IN: Location of object to which attribute is attached .
   *  @param obj_name       IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param old_attr_name  IN: Name of the attribute to be changed.
   *  @param new_attr_name  IN: New name of attribute.
   *  @param lapl_id        IN: Identifier of link access property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Arename_by_name(int loc_id, String obj_name,
        String old_attr_name, String new_attr_name, int lapl_id)
      throws HDF5LibraryException, NullPointerException;
//  public synchronized static native int H5Aiterate2(int loc_id, H5_INDEX idx_type,
//        H5_ITER order, LongByReference idx, H5A_operator2_t op, Pointer op_data)
//  throws HDF5LibraryException, NullPointerException;
//  public synchronized static native int H5Aiterate_by_name(int loc_id, String obj_name, H5_INDEX idx_type,
//        H5_ITER order, LongByReference idx, H5A_operator2_t op, Pointer op_data,
//        int lapd_id)
//  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Adelete removes the attribute specified by its name, name,
   *  from a dataset, group, or named datatype.
   *
   *  @param loc_id  IN: Identifier of the dataset, group, or named datatype.
   *  @param name    IN: Name of the attribute to delete.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Adelete(int loc_id, String name)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Adelete_by_name removes the attribute attr_name from an object 
   *  specified by location and name, loc_id and obj_name, respectively. 
   *
   *  @param loc_id    IN: Location of object to which attribute is attached .
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param attr_name IN: Name of attribute to delete.
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Adelete_by_name(int loc_id, String obj_name,
        String attr_name, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Adelete_by_idx removes an attribute, specified by its location in an index, from an object. 
   *
   *  @param loc_id    IN: Location of object to which attribute is attached .
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param idx_type  IN: Type of index by which objects are ordered  
   *  @param order     IN: Order of iteration within index 
   *  @param n         IN: Attribute’s position in index 
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Adelete_by_idx(int loc_id, String obj_name,
        H5_INDEX idx_type, H5_ITER order, long n, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aexists determines whether the attribute attr_name exists on the 
   *  object specified by obj_id. 
   *
   *  @param obj_id    IN: Object identifier.
   *  @param attr_name IN: Name of the attribute.
   *
   *  @return boolean true if an attribute with a given name exists.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - attr_name is null.
   **/
  public synchronized static native boolean H5Aexists(int obj_id, String attr_name)
      throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Aexists_by_name determines whether the attribute attr_name exists on an object. That object is 
   *  specified by its location and name, loc_id and obj_name, respectively. 
   *
   *  @param loc_id    IN: Location of object to which attribute is attached .
   *  @param obj_name  IN: Name, relative to loc_id, of object that attribute is attached to.
   *  @param attr_name IN: Name of attribute.
   *  @param lapl_id   IN: Identifier of link access property list.
   *
   *  @return boolean true if an attribute with a given name exists.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native boolean H5Aexists_by_name(int obj_id, String obj_name,
        String attr_name, int lapl_id)
      throws HDF5LibraryException, NullPointerException;

  /** H5Acopy copies the content of one attribute to another.
   *  @param src_aid the identifier of the source attribute
   *  @param dst_aid the identifier of the destinaiton attribute
   */
  public synchronized static native int H5Acopy(int src_aid, int dst_aid)
    throws HDF5LibraryException;
}
