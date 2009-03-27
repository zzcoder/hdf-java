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

import hdf.h5.enums.H5T_CLASS;
import hdf.h5.enums.H5T_CSET;
import hdf.h5.enums.H5T_DIR;
import hdf.h5.enums.H5T_NORM;
import hdf.h5.enums.H5T_ORDER;
import hdf.h5.enums.H5T_PAD;
import hdf.h5.enums.H5T_SGN;
import hdf.h5.enums.H5T_STR;
import hdf.h5.exceptions.HDF5LibraryException;

//////////////////////////////////////////////////////////////
//                                                          //
//           H5T: Datatype Interface Functions              //
//                                                          //
//////////////////////////////////////////////////////////////

public class H5T {
//  // All datatype conversion functions are... /
//  public interface H5T_conv_t extends Callback {
//    int callback(int src_id, int dst_id, H5T_cdata_t cdata, long nelmts,
//        long buf_stride, long bkg_stride, Pointer buf, Pointer bkg,
//        int dset_xfer_plist);
//  }
//
//  // Exception handler. If an exception like overflow happenes during
//  // conversion,
//  // this function is called if it's registered through H5Pset_type_conv_cb.
//  public interface H5T_conv_except_func_t extends Callback {
//    int callback(H5T_conv_except_t except_type, int src_id, int dst_id,
//        Pointer src_buf, Pointer dst_buf, Pointer user_data);
//  }

  /* Operations defined on all datatypes */

  /**
   *  H5Tcreate creates a new dataype of the specified class with
   *  the specified number of bytes.
   *
   *  @param type IN: Class of datatype to create.
   *  @param size IN: The number of bytes in the datatype to create.
   *
   *  @return datatype identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tcreate(H5T_CLASS type, long size)
  throws HDF5LibraryException;

  /**
   *  H5Tcopy copies an existing datatype. The returned type is
   *  always transient and unlocked.
   *
   *  @param type_id IN: Identifier of datatype to copy. Can be a datatype 
   *                      identifier, a  predefined datatype (defined in 
   *                      H5Tpublic.h), or a dataset Identifier.
   *
   *  @return a datatype identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tcopy(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tclose releases a datatype.
   *
   *  @param type_id IN: Identifier of datatype to release.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tclose(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tequal determines whether two datatype identifiers refer
   *  to the same datatype.
   *
   *  @param type_id1 IN: Identifier of datatype to compare.
   *  @param type_id2 IN: Identifier of datatype to compare.
   *
   *  @return true if the datatype identifiers refer to the
   *  same datatype, else false.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Tequal(int type_id1, int type_id2)
  throws HDF5LibraryException;

  /**
   *  H5Tlock locks the datatype specified by the type_id
   *  identifier, making it read-only and non-destrucible.
   *
   *  @param type_id IN: Identifier of datatype to lock.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tlock(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tcommit2 saves a transient datatype as an immutable named datatype in a file.
   *
   *  @param loc_id   IN: Location identifier.
   *  @param name     IN: Name given to committed datatype.
   *  @param type_id  IN: Identifier of datatype to be committed.
   *  @param lcpl_id  IN: Link creation property list.
   *  @param tcpl_id  IN: Datatype creation property list.
   *  @param tapl_id  IN: Datatype access property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Tcommit2(int loc_id, String name, int type_id, int lcpl_id,
      int tcpl_id, int tapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Topen2 opens a named datatype at the location specified
   *  by loc_id and return an identifier for the datatype.
   *
   *  @param loc_id   IN: A file, group, or datatype identifier.
   *  @param name     IN: A datatype name, defined within the file or group identified by loc_id.
   *  @param tapl_id  IN: Datatype access property list.
   *
   *  @return a named datatype identifier if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native int H5Topen2(int loc_id, String name, int tapl_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Tcommit_anon commits a transient datatype (not immutable) to a file, 
   *  turning it into a named datatype with the specified creation and property lists.
   *
   *  @param loc_id   IN: Location identifier.
   *  @param type_id  IN: Identifier of datatype to be committed.
   *  @param tcpl_id  IN: Datatype creation property list.
   *  @param tapl_id  IN: Datatype access property list.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tcommit_anon(int loc_id, int type_id, int tcpl_id, int tapl_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_create_plist returns a property list identifier for the datatype 
   *  creation property list associated with the datatype specified by type_id. 
   *
   *  @param type_id   IN: Identifier of datatype.
   *
   *  @return a datatype property list identifier.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tget_create_plist(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tcommitted queries a type to determine whether the type specified 
   *  by the type_id identifier is a named type or a transient type.
   *
   *  @param type_id   IN: Identifier of datatype.
   *
   *  @return true the datatype has been committed
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Tcommitted(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tencode converts a data type description into binary form in a buffer.
   *
   *  @param obj_id   IN: Identifier of the object to be encoded.
   *  @param buf     OUT: Buffer for the object to be encoded into. 
   *                      If the provided buffer is NULL, only the 
   *                      size of buffer needed is returned.
   *  @param nalloc   IN: The size of the allocated buffer.
   *
   *  @return the size needed for the allocated buffer.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tencode(int obj_id, byte[] buf, long nalloc)
  throws HDF5LibraryException, NullPointerException;
  /**
   *  H5Tencode converts a data type description into binary form in a buffer.
   *
   *  @param obj_id   IN: Identifier of the object to be encoded.
   *
   *  @return the buffer for the object to be encoded into.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native byte[] H5Tencode(int obj_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Tdecode reconstructs the HDF5 data type object and 
   *  returns a new object handle for it.
   *
   *  @param buf   IN: Buffer for the data type object to be decoded.
   *
   *  @return a new object handle
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - buf is null.
   **/
  public synchronized static native int H5Tdecode(byte[] buf)
  throws HDF5LibraryException, NullPointerException;

  /* Operations defined on compound datatypes */
  /**
   *  H5Tinsert adds another member to the compound datatype type_id.
   *
   *  @param type_id  IN: Identifier of compound datatype to modify.
   *  @param name     IN: Name of the field to insert.
   *  @param offset   IN: Offset in memory structure of the field to insert.
   *  @param field_id IN: Datatype identifier of the field to insert.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Tinsert(int type_id, String name,
          long offset, int field_id)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Tpack recursively removes padding from within a
   *  compound datatype to make it more efficient (space-wise)
   *  to store that data.
   *  
   *  <p><b>WARNING:</b> This call only affects the
   *  C-data, even if it succeeds, there may be no visible
   *  effect on Java objects.</p>
   *
   *  @param type_id IN: Identifier of datatype to modify.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tpack(int type_id)
  throws HDF5LibraryException;

  /* Operations defined on enumeration datatypes */
  /**
   *  H5Tenum_create creates a new enumeration datatype based on the 
   *  specified base datatype, parent_id, which must be an integer type. 
   *
   *  @param base_id IN: Identifier of the parent datatype to release.
   *
   *  @return the datatype identifier for the new enumeration datatype
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tenum_create(int base_id)
  throws HDF5LibraryException;

  /**
   *  H5Tenum_insert inserts a new enumeration datatype member
   *  into an enumeration datatype.
   *
   *  @param type  IN: Identifier of datatype.
   *  @param name  IN: The name of the member
   *  @param value IN: The value of the member, data of the correct type
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - name is null.
   **/
  public synchronized static native void H5Tenum_insert(int type, String name, byte[] value)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Tenum_nameof finds the symbol name that corresponds
   *  to the specified value of the enumeration datatype type.
   *
   *  @param type   IN: Identifier of datatype.
   *  @param value  IN: The value of the member, data of the correct
   *
   *  @return the symbol name.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - value is null.
   **/
  public synchronized static native String H5Tenum_nameof(int type, byte[] value)
  throws HDF5LibraryException, NullPointerException;
//  int H5Tenum_nameof(int type, Pointer value, Buffer name/* out */, long size);

  /**
   *  H5Tenum_valueof finds the value that corresponds to
   *  the specified name of the enumeration datatype type.
   *
   *  @param type  IN: Identifier of datatype.
   *  @param name  IN: The name of the member
   *
   *  @return the value of the enumeration datatype.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native byte[] H5Tenum_valueof(int type, String name)
  throws HDF5LibraryException, NullPointerException;

  /* Operations defined on variable-length datatypes */
  /**
   *  H5Tvlen_create creates a new variable-length (VL) dataype.
   *
   *  @param base_id  IN: Identifier of parent datatype.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tvlen_create(int base_id)
  throws HDF5LibraryException;

  /* Operations defined on array datatypes */
  /**
   *  H5Tarray_create2 creates a new array datatype object. 
   *
   *  @param base_id  IN: Datatype identifier for the array base datatype.
   *  @param ndims    IN: Rank of the array.
   *  @param dim      IN: Size of each array dimension.
   *
   *  @return a valid datatype identifier
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tarray_create2(int base_id, int ndims, long dim[/* ndims */])
  throws HDF5LibraryException;

  /**
   *  H5Tget_array_ndims returns the rank, the number of dimensions, of an array datatype object. 
   *
   *  @param type_id  IN: Datatype identifier of array object.
   *
   *  @return the rank of the array
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tget_array_ndims(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_array_dims2 returns the sizes of the dimensions of the specified array datatype object. 
   *
   *  @param type_id  IN: Datatype identifier of array object.
   *  @param dims    OUT: Sizes of array dimensions.
   *
   *  @return the non-negative number of dimensions of the array type
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tget_array_dims2(int type_id, long[] dims)
  throws HDF5LibraryException, NullPointerException;

  /* Operations defined on opaque datatypes */
  /**
   *  H5Tset_tag tags an opaque datatype dtype_id  with a 
   *  descriptive ASCII identifier, tag. 
   *
   *  @param type IN: Datatype identifier for the opaque datatype to be tagged.
   *  @param tag  IN: Descriptive ASCII string with which the opaque datatype is to be tagged.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_tag(int type, String tag)
  throws HDF5LibraryException;

  /**
   *  H5Tget_tag returns the tag associated with datatype type_id.
   *
   *  @param type  IN: Identifier of datatype.
   *
   *  @return the tag
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Tget_tag(int type)
  throws HDF5LibraryException;

  /* Querying property values */
  /**
   *  H5Tget_super returns the type from which TYPE is derived.
   *
   *  @param type  IN: Identifier of datatype.
   *
   *  @return the parent type
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tget_super(int type)
  throws HDF5LibraryException;

  /**
   *  H5Tget_class returns the datatype class identifier.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return datatype class identifier if successful; otherwise H5T_NO_CLASS.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_CLASS H5Tget_class(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tdetect_class determines whether the datatype specified in dtype_id contains 
   *  any datatypes of the datatype class specified in dtype_class. 
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *  @param cls      IN: Identifier of datatype cls.
   *
   *  @return true if the datatype specified in dtype_id contains any datatypes of the datatype class
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Tdetect_class(int type_id, H5T_CLASS cls)
  throws HDF5LibraryException;

  /**
   *  H5Tget_size returns the size of a datatype in bytes.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return the size of the datatype in bytes
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Tget_size(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_order returns the byte order of an atomic datatype.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return a byte order constant
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_ORDER H5Tget_order(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_precision returns the precision of an atomic datatype.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return the number of significant bits if successful
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Tget_precision(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_offset retrieves the bit offset of the first
   *  significant bit.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return a positive offset value
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tget_offset(int type_id)
  throws HDF5LibraryException;
  /**
   *  H5Tget_pad retrieves the padding type of the least and
   *  most-significant bit padding.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return least-significant and most-significant bit padding type.
   *  <ul>
   *      <li>pad[0] = lsb // least-significant bit padding type</li>
   *      <li>pad[1] = msb // most-significant bit padding type</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_PAD[] H5Tget_pad(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_sign retrieves the sign type for an integer type.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return a valid sign type
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_SGN H5Tget_sign(int type_id)
  throws HDF5LibraryException;
  /**
   *  H5Tget_fields retrieves information about the locations of
   *  the various bit fields of a floating point datatype.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return size and bit-position.
   *  <ul>
   *      <li>fields[0] = spos  : location to return size of in bits.</li>
   *      <li>fields[1] = epos  : location to return exponent bit-position.</li>
   *      <li>fields[2] = esize : location to return size of exponent in bits.</li>
   *      <li>fields[3] = mpos  : location to return mantissa bit-position.</li>
   *      <li>fields[4] = msize : location to return size of mantissa in bits.</li>
   *  </ul>
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long[] H5Tget_fields(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_ebias retrieves the exponent bias of a
   *  floating-point type.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return the bias 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Tget_ebias(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_norm retrieves the mantissa normalization of a
   *  floating-point datatype.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return a valid normalization type
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_NORM H5Tget_norm(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_inpad retrieves the internal padding type for unused
   *  bits in floating-point datatypes.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return a valid padding type
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_PAD H5Tget_inpad(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_strpad retrieves the string padding method for
   *  a string datatype.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return a valid string padding type
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_STR H5Tget_strpad(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_nmembers retrieves the number of fields a
   *  compound datatype has.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return number of members datatype has
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tget_nmembers(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_member_name retrieves the name of a field of a compound datatype or 
   *  an element of an enumeration datatype. 
   *
   *  @param type_id    IN: Identifier of datatype to query.
   *  @param field_idx  IN: Field index (0-based) of the field name to retrieve.
   *
   *  @return the name
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native String H5Tget_member_name(int type_id, int field_idx)
  throws HDF5LibraryException;

  /**
   *  H5Tget_member_index retrieves the index of a field of a compound datatype.
   *
   *  @param type_id    IN: Identifier of datatype to query.
   *  @param field_name IN: Field name of the field index to retrieve.
   *
   *  @return if field is defined, the index; else negative.
   *
   **/
  public synchronized static native int H5Tget_member_index(int type_id, String field_name)
  throws HDF5LibraryException;

  /**
   *  H5Tget_member_offset returns the byte offset of the
   *  specified member of the compound datatype.  This
   *  is the byte offset in the HDF-5 file/library, NOT
   *  the offset of any Java object which might be mapped
   *  to this data item.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *  @param membno   IN: Field index (0-based) of the field type to retrieve.
   *
   *  @return the offset of the member.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native long H5Tget_member_offset(int type_id, int membno)
  throws HDF5LibraryException;

  /**
   *  H5Tget_member_class returns the datatype class of the compound datatype member specified by memno. 
   *
   *  @param type_id   IN: Datatype identifier of compound object.
   *  @param membno    IN: Compound object member number.
   *
   *  @return the datatype class.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_CLASS H5Tget_member_class(int type_id, int membno)
  throws HDF5LibraryException;

  /**
   *  H5Tget_member_type returns the datatype of the specified member.
   *
   *  @param type_id   IN: Identifier of datatype to query.
   *  @param field_idx IN: Field index (0-based) of the field type to retrieve.
   *
   *  @return the identifier of a copy of the datatype of the field.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native int H5Tget_member_type(int type_id, int field_idx)
  throws HDF5LibraryException;
  /**
   *  H5Tget_member_value returns the value of the enumeration datatype member memb_no. 
   *
   *  @param type_id  IN: Datatype identifier for the enumeration datatype.
   *  @param membno   IN: Number of the enumeration datatype member.
   *
   *  @return the value of the enumeration datatype member.
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native byte[] H5Tget_member_value(int type_id, int membno)
  throws HDF5LibraryException, NullPointerException;

  /**
   *  H5Tget_cset retrieves the character set type of a string datatype.
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return a valid character set type
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_CSET H5Tget_cset(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tis_variable_str determines whether the datatype identified in type_id is a variable-length string. 
   *
   *  @param type_id  IN: Identifier of datatype to query.
   *
   *  @return true if type_id is a variable-length string. 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native boolean H5Tis_variable_str(int type_id)
  throws HDF5LibraryException;

  /**
   *  H5Tget_native_type returns the equivalent native datatype for the datatype specified in type_id. 
   *
   *  @param type_id   IN: Identifier of datatype to query.
   *  @param direction IN: Direction of search.
   *
   *  @return the native datatype identifier for the specified dataset datatype. 
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native H5T_DIR H5Tget_native_type(int type_id, H5T_DIR direction)
  throws HDF5LibraryException;
  public synchronized static H5T_DIR H5Tget_native_type(int tid)
  throws HDF5LibraryException
  {
      return H5Tget_native_type(tid, H5T_DIR.ASCEND);
  }

  /* Setting property values */
  /**
   *  H5Tset_size sets the total size in bytes, size, for an
   *  atomic datatype (this operation is not permitted on
   *  compound datatypes).
   *
   *  @param type_id  IN: Identifier of datatype to change size.
   *  @param size     IN: Size in bytes to modify datatype.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_size(int type_id, long size)
  throws HDF5LibraryException;

  /**
   *  H5Tset_order sets the byte ordering of an atomic datatype.
   *
   *  @param type_id  IN: Identifier of datatype to set.
   *  @param order    IN: Byte ordering constant.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_order(int type_id, H5T_ORDER order)
  throws HDF5LibraryException;

  /**
   *  H5Tset_precision sets the precision of an atomic datatype.
   *
   *  @param type_id    IN: Identifier of datatype to set.
   *  @param precision  IN: Number of bits of precision for datatype.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_precision(int type_id, long precision)
  throws HDF5LibraryException;

  /**
   *  H5Tset_offset sets the bit offset of the first significant bit.
   *
   *  @param type_id  IN: Identifier of datatype to set.
   *  @param offset   IN: Offset of first significant bit.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_offset(int type_id, long offset)
  throws HDF5LibraryException;

  /**
   *  H5Tset_pad sets the least and most-significant bits padding types.
   *
   *  @param type_id  IN: Identifier of datatype to set.
   *  @param lsb      IN: Padding type for least-significant bits.
   *  @param msb      IN: Padding type for most-significant bits.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_pad(int type_id, H5T_PAD lsb, H5T_PAD msb)
  throws HDF5LibraryException;

  /**
   *  H5Tset_sign sets the sign proprety for an integer type.
   *
   *  @param type_id  IN: Identifier of datatype to set.
   *  @param sign     IN: Sign type.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_sign(int type_id, H5T_SGN sign)
  throws HDF5LibraryException;

  /**
   *  H5Tset_fields sets the locations and sizes of the various
   *  floating point bit fields.
   *
   *  @param type_id  IN: Identifier of datatype to set.
   *  @param spos     IN: Size position.
   *  @param epos     IN: Exponent bit position.
   *  @param esize    IN: Size of exponent in bits.
   *  @param mpos     IN: Mantissa bit position.
   *  @param msize    IN: Size of mantissa in bits.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_fields(int type_id, long spos, long epos, long esize, long mpos, long msize)
  throws HDF5LibraryException;

  /**
   *  H5Tset_ebias sets the exponent bias of a floating-point type.
   *
   *  @param type_id  IN: Identifier of datatype to set.
   *  @param ebias    IN: Exponent bias value.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_ebias(int type_id, long ebias)
  throws HDF5LibraryException;

  /**
   *  H5Tset_norm sets the mantissa normalization of a
   *  floating-point datatype.
   *
   *  @param type_id  IN: Identifier of datatype to set.
   *  @param norm     IN: Mantissa normalization type.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_norm(int type_id, H5T_NORM norm)
  throws HDF5LibraryException;

  /**
   *  If any internal bits of a floating point type are unused
   *  (that is, those significant bits which are not part of
   *  the sign, exponent, or mantissa), then  H5Tset_inpad will
   *  be filled according to the value of the padding value
   *  property inpad.
   *
   *  @param type_id  IN: Identifier of datatype to modify.
   *  @param inpad    IN: Padding type.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_inpad(int type_id, H5T_PAD pad)
  throws HDF5LibraryException;

  /**
   *  H5Tset_cset the character set to be used.
   *
   *  @param type_id  IN: Identifier of datatype to modify.
   *  @param cset     IN: Character set type.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_cset(int type_id, H5T_CSET cset)
  throws HDF5LibraryException;

  /**
   *  H5Tset_strpad defines the storage mechanism for the string.
   *
   *  @param type_id IN: Identifier of datatype to modify.
   *  @param strpad  IN: String padding type.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tset_strpad(int type_id, H5T_STR strpad)
  throws HDF5LibraryException;

  /* Type conversion database */
//  public synchronized static native int H5Tregister(H5T_pers_t pers, String name, int src_id, int dst_id,
//      H5T_conv_t func)
//  throws HDF5LibraryException, NullPointerException;
//
//  public synchronized static native int H5Tunregister(H5T_pers_t pers, String name, int src_id, int dst_id,
//      H5T_conv_t func)
//  throws HDF5LibraryException, NullPointerException;

//  H5T_conv_t H5Tfind(int src_id, int dst_id, H5T_cdata_t *pcdata);

  /**
   *  H5Tcompiler_conv finds out whether the library’s conversion function from 
   *  type src_id to type dst_id is a compiler (hard) conversion.
   *
   *  @param src_id     IN: Identifier of source datatype.
   *  @param dst_id     IN: Identifier of destination datatype.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   **/
  public synchronized static native void H5Tcompiler_conv(int src_id, int dst_id)
  throws HDF5LibraryException;

  /**
   **  H5Tconvert converts nelmts elements from the type specified by the src_id identifier to type dst_id.
   *
   *  @param src_id     IN: Identifier of source datatype.
   *  @param dst_id     IN: Identifier of destination datatype.
   *  @param nelmts     IN: Size of array buf.
   *  @param buf        IN: Array containing pre- and post-conversion values.
   *  @param background IN: Optional background buffer.
   *  @param plist_id   IN: Dataset transfer property list identifier.
   *
   *  @return none
   *
   *  @exception HDF5LibraryException - Error from the HDF-5 Library.
   *  @exception NullPointerException - buf is null.
   **/
  public synchronized static native void H5Tconvert(int src_id, int dst_id, long nelmts, byte[] buf,
      byte[] background, int plist_id)
  throws HDF5LibraryException, NullPointerException;
//    int H5Tconvert(int src_id, int dst_id, long nelmts, Pointer buf, Pointer background, int plist_id);
}
